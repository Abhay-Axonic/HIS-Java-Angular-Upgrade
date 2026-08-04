#!/usr/bin/env python3
"""
Relational verification harness for master-screen org-trust fixes (v2, 2026-08-04).

Usage:
    python3 tools/verify_master_screen.py <screen> [--worktree <path>] \
        [--token <jwt>] [--session-org 1] [--with-write-tests]

Design principles (owner directive 2026-08-04 — no tuned literals):
  1. JAR FRESHNESS is relational: the deployed jar's mtime MUST be newer than the
     fix commit's timestamp (from `git show -s --format=%ct <commit>`), never
     compared against a fixed clock time.
  2. LEGIT COUNT is a DELTA: the count is captured before the run; the assertion
     is on the change, never a hardcoded constant.
  3. MANDATORY FORGED-PAYLOAD SANITY (the check that would have caught the
     getGenderById masked test): the forged org value MUST differ from the
     session org, AND a real victim row MUST exist in the DB under that forged
     org. The harness FAILS if either is false.
  4. VICTIM PRISTINE is relational: the victim row's full state is captured
     before the run and asserted byte-identical after the attacks.
  5. BYTECODE REF COUNTS are derived from the working-tree source (count of
     getVerifiedOrganizationId in the source files), then asserted against the
     deployed jar — source and deployment must agree, no hardcoded minimum.
  6. Write-path tests (save + duplicate-save) are OFF by default
     (--with-write-tests). The gender dup-check is a known-broken, logged-but-
     unfixed pre-existing crash (docs/QA_FINDINGS.md); do not enable write tests
     until that is diagnosed.

Exit 0 = all checks pass. Exit 1 = any check failed. No spec tuning.
"""

import argparse
import os
import re
import shutil
import subprocess
import sys
import time

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
WEB = "/opt/homebrew/opt/tomcat@9/libexec/webapps/ROOT/WEB-INF/lib"
JAVAP = "/opt/homebrew/Cellar/openjdk@21/21.0.11/libexec/openjdk.jdk/Contents/Home/bin/javap"
WT = os.path.join(ROOT, ".claude/worktrees/sweep-lis-common")

SCREENS = {
    "sex": {
        "module": "Global",
        "fix_commit": "af7611f3",
        "payload_org_field": "organizationId",   # form 1: payload carries an org field
        "service_class": "com/param/global/service/GenderMasterServiceImpl.class",
        "dao_class": "com/param/global/dao/GenderMasterDaoImpl.class",
        "entity_class": "com/param/global/model/GenderMaster.class",
        "service_src": "legacy/Global/src/main/java/com/param/global/service/GenderMasterServiceImpl.java",
        "dao_src": "legacy/Global/src/main/java/com/param/global/dao/GenderMasterDaoImpl.java",
        "query_org_filters": ["WHERE genderMaster.organizationId=:orgId",
                              "genderMaster.id=:id AND genderMaster.organizationId=:orgId"],
        "victim_id": 99,
        "victim_org": 999,
        "victim_table": "m_gender_master",
        "victim_pk": "gender_id",
        "list_endpoint": "/adt/getGenderMasterList",
        "getbyid_endpoint": "/adt/getGenderById",
        "getbyid_id_field": "id",
        "legit_marker": "Male",
        "victim_marker": "G-999-X",
        "save_endpoint": "/adt/saveGenderMaster",
    },
}


def run(cmd, **kw):
    kw.setdefault("capture_output", True)
    kw.setdefault("text", True)
    return subprocess.run(cmd, **kw)


def check(name, cond, detail=""):
    tag = "PASS" if cond else "FAIL"
    print(f"[{tag}] {name} {detail}")
    return cond


def psql(q):
    r = run(["psql", "-U", "axonichealth", "-d", "his_local", "-t", "-c", q])
    return r.stdout.strip()


def source_vis_count(wt, relpath):
    p = os.path.join(wt, relpath)
    if not os.path.exists(p):
        return -1
    with open(p, "r", encoding="utf-8", errors="replace") as f:
        return f.read().count("getVerifiedOrganizationId")


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("screen", choices=sorted(SCREENS.keys()))
    ap.add_argument("--worktree", default=WT)
    ap.add_argument("--token", default=os.environ.get("HIS_JWT", ""),
                    help="JWT for the live session (or export HIS_JWT)")
    ap.add_argument("--session-org", type=int, default=1,
                    help="org the session belongs to (from the login)")
    ap.add_argument("--with-write-tests", action="store_true",
                    help="enable save + duplicate-save write tests (default OFF)")
    args = ap.parse_args()

    spec = SCREENS[args.screen]
    wt = args.worktree
    ok = True

    # --- 1. source compile gate (worktree) ---
    rc = run(f"mvn -q compile -DskipTests -o -pl {spec['module']} -am",
             cwd=os.path.join(wt, "legacy"), shell=True).returncode
    ok &= check("mvn compile", rc == 0, f"({spec['module']})")

    # --- 2. jar freshness: mtime > fix-commit timestamp (relational) ---
    jar = os.path.join(WEB, f"{spec['module']}-0.0.1-SNAPSHOT.jar")
    if os.path.exists(jar):
        jar_mt = int(os.stat(jar).st_mtime)
    else:
        jar_mt = 0
    c = run(["git", "show", "-s", "--format=%ct", spec["fix_commit"]], cwd=ROOT)
    commit_ts = int(c.stdout.strip()) if c.returncode == 0 else 0
    ok &= check("jar newer than fix commit",
                jar_mt > commit_ts and commit_ts > 0,
                f"(jar_mtime={time.strftime('%H:%M', time.localtime(jar_mt))} > "
                f"commit_ts={time.strftime('%H:%M', time.localtime(commit_ts))})")

    # --- 3. deployed bytecode VIS refs == source VIS refs (relational) ---
    bdir = "/tmp/hermes-verify-screen"
    if os.path.exists(bdir):
        shutil.rmtree(bdir)
    os.makedirs(bdir)
    run(["unzip", "-o", "-q", jar, spec["service_class"], spec["dao_class"]], cwd=bdir)
    sp = os.path.join(bdir, spec["service_class"].replace("/", os.sep))
    dp = os.path.join(bdir, spec["dao_class"].replace("/", os.sep))
    svc_src = source_vis_count(wt, spec["service_src"])
    dao_src = source_vis_count(wt, spec["dao_src"])
    if os.path.exists(sp) and os.path.exists(dp):
        svc_dep = run([JAVAP, "-c", "-p", sp], capture_output=True, text=True).stdout.count("getVerifiedOrganizationId")
        dao_dep = run([JAVAP, "-c", "-p", dp], capture_output=True, text=True).stdout.count("getVerifiedOrganizationId")
        ok &= check("service VIS refs match source", svc_dep == svc_src,
                    f"(deployed={svc_dep} source={svc_src})")
        ok &= check("dao VIS refs match source", dao_dep == dao_src,
                    f"(deployed={dao_dep} source={dao_src})")
    else:
        ok &= check("extraction produced class files", False)

    # --- 4. entity named-query org filters present in deployed Entity jar ---
    ejar = os.path.join(WEB, "Entity-0.0.1-SNAPSHOT.jar")
    ebdir = "/tmp/hermes-verify-screen-entity"
    if os.path.exists(ebdir):
        shutil.rmtree(ebdir)
    os.makedirs(ebdir)
    run(["unzip", "-o", "-q", ejar, spec["entity_class"]], cwd=ebdir)
    ep = os.path.join(ebdir, spec["entity_class"].replace("/", os.sep))
    if os.path.exists(ep):
        ec = run([JAVAP, "-v", ep], capture_output=True, text=True).stdout
        for filt in spec["query_org_filters"]:
            ok &= check(f"entity filter present: {filt[:55]}", filt in ec)
    else:
        ok &= check("entity class extracted", False)

    # --- 5. tomcat liveness ---
    pid = run(["lsof", "-tiTCP:8080", "-sTCP:LISTEN"], capture_output=True, text=True).stdout.strip().split("\n")[0]
    http = run(["curl", "-s", "-o", "/dev/null", "-w", "%{http_code}", "http://localhost:8080/"],
               capture_output=True, text=True).stdout
    ok &= check("tomcat :8080 live", bool(pid) and http == "200", f"(PID {pid})")

    # --- 6. MANDATORY forged-payload sanity — BOTH forms (the masked-test check) ---
    # Form 1: the payload carries an org field -> that forged org MUST differ from
    #         the session org, AND a real victim row must exist under that org.
    # Form 2: the payload carries NO org field (pure by-id attack) -> the TARGETED
    #         row's org MUST differ from the session org, AND the victim row must
    #         exist. The harness picks the form by the spec's payload shape.
    session_org = args.session_org
    forged_org = spec["victim_org"]
    if spec.get("payload_org_field"):
        ok &= check("MANDATORY (form 1): forged org != session org",
                    forged_org != session_org,
                    f"(forged={forged_org} session={session_org})")
        victim_row = psql(f"SELECT {spec['victim_pk']}, organization_id, status FROM {spec['victim_table']} "
                          f"WHERE {spec['victim_pk']}={spec['victim_id']}")
        victim_org_ok = bool(re.search(rf"\|\s*{forged_org}\s*\|", victim_row))
        ok &= check("MANDATORY (form 1): real victim row exists under forged org",
                    victim_org_ok,
                    f"(row='{victim_row}')")
    else:
        row = psql(f"SELECT organization_id FROM {spec['victim_table']} "
                   f"WHERE {spec['victim_pk']}={spec['victim_id']}")
        ok &= check("MANDATORY (form 2): targeted row's org != session org",
                    row.isdigit() and int(row) != session_org,
                    f"(targeted_row_org={row} session_org={session_org})")
        ok &= check("MANDATORY (form 2): victim row exists",
                    row.isdigit(), f"(row='{row}')")

    # --- 7. victim pristine: full pre-state captured, compared post-attacks ---
    vcols = f"SELECT * FROM {spec['victim_table']} WHERE {spec['victim_pk']}={spec['victim_id']}"
    victim_before = psql(vcols)
    ok &= check("victim row exists", bool(victim_before), f"({victim_before[:80]})")

    # --- 8. legit count DELTA (read-only run: expected delta 0) ---
    count_q = f"SELECT count(*) FROM {spec['victim_table']} WHERE organization_id={session_org}"
    count_before = int(psql(count_q) or -1)
    ok &= check("legit count captured", count_before >= 0, f"(before={count_before})")

    # --- 9. live attacks (read-only) + legit checks ---
    if args.token:
        H = ["-H", "Content-Type: application/json",
             "-H", f"Authorization: Bearer {args.token}",
             "-H", "userId: 1", "-H", "unitId: 1", "-H", "Origin: http://localhost:8080"]
        r = run(["curl", "-s", "-X", "POST", f"http://localhost:8080{spec['list_endpoint']}",
                 *H, "-d", '{"organizationId":%d,"offset":0,"noOfRecordsPerPage":50}' % forged_org])
        leaked = spec["victim_marker"] in r.stdout
        ok &= check("forged list leaks no victim row", not leaked,
                    f"(payload org={forged_org}; victim_marker_in_response={leaked})")
        r2 = run(["curl", "-s", "-X", "POST", f"http://localhost:8080{spec['list_endpoint']}",
                  *H, "-d", '{"organizationId":%d,"offset":0,"noOfRecordsPerPage":50}' % session_org])
        ok &= check("legit list returns own rows", spec["legit_marker"] in r2.stdout,
                    f"(marker={spec['legit_marker']} present={spec['legit_marker'] in r2.stdout})")
        r3 = run(["curl", "-s", "-X", "POST", f"http://localhost:8080{spec['getbyid_endpoint']}",
                  *H, "-d", '{"%s":%d,"organizationId":%d}' % (spec["getbyid_id_field"], spec["victim_id"], forged_org)])
        try:
            import json as _json
            body = _json.loads(r3.stdout)
            rows = body.get("listObject") or []
            ok &= check("forged getById returns 0 rows", len(rows) == 0,
                        f"(rows={len(rows)})")
        except Exception as e:
            ok &= check("forged getById parses", False, f"({e}: {r3.stdout[:120]})")

        if args.with_write_tests:
            print("[WARN] --with-write-tests is DISABLED-BY-POLICY for sex until the "
                  "dup-check crash is diagnosed (docs/QA_FINDINGS.md). Ignoring flag.")
    else:
        print("[SKIP] no token provided — live attack checks skipped (pass --token or set HIS_JWT)")

    # --- 10. victim pristine after attacks + legit count delta ---
    victim_after = psql(vcols)
    ok &= check("victim pristine after run", victim_before == victim_after)
    count_after = int(psql(count_q) or -1)
    ok &= check("legit count delta == 0 (read-only run)",
                count_after == count_before,
                f"(before={count_before} after={count_after})")

    print(f"\n=== RESULT: {'PASS' if ok else 'FAIL'} (verify_master_screen: {args.screen}) ===")
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
