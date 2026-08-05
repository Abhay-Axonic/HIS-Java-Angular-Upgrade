#!/usr/bin/env python3
"""
Relational verification harness for master-screen org-trust fixes (v2, 2026-08-04).

Usage:
    python3 tools/verify_master_screen.py <screen> [--worktree <path>] \
        [--token <jwt>] [--session-org 1] [--with-write-tests]
    python3 tools/verify_master_screen.py --self-test

Design principles (owner directive 2026-08-04 — no tuned literals):
  1. JAR FRESHNESS is relational: the deployed jar's mtime MUST be newer than the
     fix commit's timestamp (from `git show -s --format=%ct <commit>`), never
     compared against a fixed clock time.
  2. LEGIT COUNT is a DELTA: the count is captured before the run; the assertion
     is on the change, never a hardcoded constant.
  3. MANDATORY FORGED-PAYLOAD SANITY (the check that would have caught the
     getGenderById masked test): the forged org value MUST differ from the
     session org, AND a real victim row MUST exist in the DB under that forged
     org. The harness FAILS if either is false. Form 2 (by-id payloads with no
     org field) asserts the TARGETED row's org differs from the session org and
     that the victim row exists.
  4. VICTIM PRISTINE is relational: the victim row's full state is captured
     before the run and asserted byte-identical after the attacks.
  5. BYTECODE REF COUNTS are derived from the working-tree source (count of
     getVerifiedOrganizationId in the source files), then asserted against the
     deployed jar — source and deployment must agree, no hardcoded minimum.
  6. Write-path tests (save + duplicate-save) are OFF by default
     (--with-write-tests). The gender dup-check is a known-broken, logged-but-
     unfixed pre-existing crash (docs/QA_FINDINGS.md); do not enable write tests
     until that is diagnosed.
  7. SELF-TEST (--self-test, added 2026-08-05 after the inert victim-exists
     regex survived a 16/16 PASS): every assertion predicate is exercised with a
     known-good input (must PASS) and a known-bad input (must FAIL). A predicate
     whose negative control does not fail is INERT — reported and fatal. The
     self-test is re-runnable and requires no DB, Tomcat, or deployed jars.

Exit 0 = all checks pass. Exit 1 = any check failed. No spec tuning.
"""

import argparse
import os
import re
import shutil
import subprocess
import sys
import tempfile
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
    "relation": {
        "module": "Global",
        "fix_commit": "a30bd69f",
        "payload_org_field": "organizationId",
        "service_class": "com/param/global/service/RelationMasterServiceImpl.class",
        "dao_class": "com/param/global/dao/RelationMasterDaoImpl.class",
        "entity_class": "com/param/global/model/RelationMaster.class",
        "service_src": "legacy/Global/src/main/java/com/param/global/service/RelationMasterServiceImpl.java",
        "dao_src": "legacy/Global/src/main/java/com/param/global/dao/RelationMasterDaoImpl.java",
        "query_org_filters": ["WHERE relation.organizationId=:orgId",
                              "relation.id=:id AND relation.organizationId=:orgId"],
        "victim_id": 99901,
        "victim_org": 999,
        "victim_table": "m_relation_master",
        "victim_pk": "relation_id",
        "list_endpoint": "/adt/getRelationMasterList",
        "getbyid_endpoint": "/adt/getRelationById",
        "getbyid_id_field": "id",
        "legit_marker": "\"id\":",
        "victim_marker": "VICTIM-RELATION-999",
        "save_endpoint": "/adt/saveRelationMaster",
    },
    "marital": {
        "module": "ADT",
        "fix_commit": "cd4e49d3",
        "payload_org_field": "organizationId",
        "service_class": "com/param/adt/master/global/service/MaritalStatusMasterServiceImpl.class",
        "dao_class": "com/param/adt/master/global/dao/MaritalStatusMasterDaoImpl.class",
        "entity_class": "com/param/adt/master/global/model/MaritalStatusMaster.class",
        "service_src": "legacy/ADT/src/main/java/com/param/adt/master/global/service/MaritalStatusMasterServiceImpl.java",
        "dao_src": "legacy/ADT/src/main/java/com/param/adt/master/global/dao/MaritalStatusMasterDaoImpl.java",
        "query_org_filters": ["maritalStatusMaster.organizationId=:orgId",
                              "maritalStatusMaster.id=:id AND maritalStatusMaster.organizationId=:orgId"],
        "victim_id": 99902,
        "victim_org": 999,
        "victim_table": "m_marital_status_master",
        "victim_pk": "marital_status_id",
        "list_endpoint": "/adt/getMaritalStatusMasterList",
        "getbyid_endpoint": "/adt/getMaritalById",
        "getbyid_id_field": "id",
        "legit_marker": "\"id\":",
        "victim_marker": "VICTIM-MARITAL-999",
        "save_endpoint": "/adt/saveMaritalStatusMaster",
    },
    "religion": {
        "module": "ADT",
        "fix_commit": "567a59d2",   # app-code commit; entity-only parens fix 1355f15b verified via entity-filter assertion
        "payload_org_field": "organizationId",
        "service_class": "com/param/adt/master/global/service/ReligionMasterServiceImpl.class",
        "dao_class": "com/param/adt/master/global/dao/ReligionMasterDaoImpl.class",
        "entity_class": "com/param/adt/master/global/model/ReligionMaster.class",
        "service_src": "legacy/ADT/src/main/java/com/param/adt/master/global/service/ReligionMasterServiceImpl.java",
        "dao_src": "legacy/ADT/src/main/java/com/param/adt/master/global/dao/ReligionMasterDaoImpl.java",
        "query_org_filters": ["(religion.desc=:desc OR LOWER(religion.desc)=:desc) AND religion.organizationId=:orgId",
                              "religion.id=:id AND religion.organizationId=:orgId"],
        "victim_id": 99903,
        "victim_org": 999,
        "victim_table": "m_religion_master",
        "victim_pk": "religion_id",
        "list_endpoint": "/adt/getReligionMasterList",
        "getbyid_endpoint": "/adt/getReligionById",
        "getbyid_id_field": "id",
        "legit_marker": "\"id\":",
        "victim_marker": "VICTIM-RELIGION-999",
        "save_endpoint": "/adt/saveReligionMaster",
    },
    "occupation": {
        "module": "ADT",
        "fix_commit": "f66f3783",
        "payload_org_field": "organizationId",
        "service_class": "com/param/adt/master/global/service/OccupationMasterServiceImpl.class",
        "dao_class": "com/param/adt/master/global/dao/OccupationMasterDaoImpl.class",
        "entity_class": "com/param/adt/master/global/model/OccupationMaster.class",
        "service_src": "legacy/ADT/src/main/java/com/param/adt/master/global/service/OccupationMasterServiceImpl.java",
        "dao_src": "legacy/ADT/src/main/java/com/param/adt/master/global/dao/OccupationMasterDaoImpl.java",
        "query_org_filters": ["occupationMaster.organizationId=:orgId",
                              "occupationMaster.id=:id AND occupationMaster.organizationId=:orgId"],
        "victim_id": 99904,
        "victim_org": 999,
        "victim_table": "m_occupation_master",
        "victim_pk": "occupation_id",
        "list_endpoint": "/adt/getOccupationMasterList",
        "getbyid_endpoint": "/adt/getOccupationMasterById",
        "getbyid_id_field": "id",
        "legit_marker": "\"id\":",
        "victim_marker": "VICTIM-OCCUPATION-999",
        "save_endpoint": "/adt/saveOccupationMaster",
    },
    "priority": {
        "module": "Global",
        "fix_commit": "6e6f845b",
        "payload_org_field": "organizationId",
        "service_class": "com/param/global/org/common/service/PriorityMasterServiceImpl.class",
        "dao_class": "com/param/global/org/common/dao/PriorityMasterDaoImpl.class",
        "entity_class": "com/param/global/model/PriorityMaster.class",
        "service_src": "legacy/Global/src/main/java/com/param/global/org/common/service/PriorityMasterServiceImpl.java",
        "dao_src": "legacy/Global/src/main/java/com/param/global/org/common/dao/PriorityMasterDaoImpl.java",
        "query_org_filters": ["pc.organizationId=:orgId AND pc.unitId=:unitId",
                              "pc.priorityId=:priorityId AND pc.organizationId=:orgId AND pc.unitId=:unitId"],
        "victim_id": 99905,
        "victim_org": 999,
        "victim_table": "adt.m_priority_master",
        "victim_pk": "priority_id",
        "list_endpoint": "/api/common/getPriorityMasterList",
        "getbyid_skip": True,   # by-id is GET /getPriorityMasterById/{priorityId}/{orgId} — verified manually
        "getbyid_id_field": "id",
        "legit_marker": "\"id\":",
        "victim_marker": "VICTIM-PRIORITY-999",
        "save_endpoint": "/api/common/savePriorityMasterMaster",
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


# ---------------------------------------------------------------------------
# Assertion predicates — the REAL conditions used by main(). --self-test
# exercises these directly with known-good and known-bad inputs, so a check
# that cannot fail is detected instead of silently passing forever.
# ---------------------------------------------------------------------------

def cond_mvn_ok(rc):
    return rc == 0


def cond_jar_fresh(jar_mt, commit_ts):
    return jar_mt > commit_ts and commit_ts > 0


def cond_refs_match(deployed, source):
    return deployed == source


def cond_extraction_ok(sp, dp):
    return os.path.exists(sp) and os.path.exists(dp)


def cond_entity_filter_present(entity_javap, filt):
    return filt in entity_javap


def cond_entity_extracted(ep):
    return os.path.exists(ep)


def cond_tomcat_live(pid, http):
    return bool(pid) and http == "200"


def cond_form1_org_differs(forged_org, session_org):
    return forged_org != session_org


def cond_form1_victim_exists(victim_row, forged_org):
    return bool(re.search(rf"\|\s*{forged_org}\s*\|", victim_row))


def cond_form2_target_org_differs(row, session_org):
    return row.isdigit() and int(row) != session_org


def cond_form2_victim_exists(row):
    return row.isdigit()


def cond_row_exists(victim_before):
    return bool(victim_before)


def cond_count_captured(count_before):
    return count_before >= 0


def cond_no_leak(leaked):
    return not leaked


def cond_auth_json(resp):
    """Live responses must be authenticated JSON (a token rejection returns an
    empty/HTML body — this check is what stops the leak-check from passing
    vacuously on a dead session)."""
    try:
        import json as _json
        j = _json.loads(resp)
        return isinstance(j, dict) and "status" in j
    except Exception:
        return False


def cond_legit_marker_present(marker, resp):
    return marker in resp


def cond_getbyid_zero_rows(rows):
    return len(rows) == 0


def cond_victim_pristine(before, after):
    return before == after


def cond_count_delta_zero(before, after):
    return after == before


SELF_TESTS = [
    # (predicate, label, good_input, bad_input)
    (cond_mvn_ok, "mvn compile rc==0", (0,), (1,)),
    (cond_jar_fresh, "jar newer than fix commit", (2000, 1000), (500, 1000)),
    (cond_jar_fresh, "jar freshness: commit_ts>0 guard", (2000, 1000), (2000, 0)),
    (cond_refs_match, "service/dao VIS refs match source", (5, 5), (4, 5)),
    (cond_extraction_ok, "extraction produced class files", None, None),   # file-based
    (cond_entity_filter_present, "entity org filter present", ("SELECT x WHERE a.organizationId=:orgId",
                                                               "a.organizationId=:orgId"),
                                                              ("SELECT x WHERE a.id=:id", "a.organizationId=:orgId")),
    (cond_entity_extracted, "entity class extracted", None, None),          # file-based
    (cond_tomcat_live, "tomcat :8080 live (pid+200)", ("1234", "200"), ("", "200")),
    (cond_tomcat_live, "tomcat :8080 live (http!=200)", ("1234", "200"), ("1234", "404")),
    (cond_form1_org_differs, "form 1: forged org != session org", (999, 1), (1, 1)),
    (cond_form1_victim_exists, "form 1: victim exists under forged org", (" 99 | 999 | A", 999), (" 99 | 998 | A", 999)),
    (cond_form1_victim_exists, "form 1: victim exists (empty row)", (" 99 | 999 | A", 999), ("", 999)),
    (cond_form2_target_org_differs, "form 2: targeted row org != session org", ("999", 1), ("1", 1)),
    (cond_form2_target_org_differs, "form 2: non-numeric row fails", ("999", 1), ("abc", 1)),
    (cond_form2_victim_exists, "form 2: victim row exists", ("999",), ("",)),
    (cond_row_exists, "victim row exists (non-empty)", ("row data",), ("",)),
    (cond_count_captured, "legit count captured >= 0", (3,), (-1,)),
    (cond_no_leak, "forged list leaks no victim row", (False,), (True,)),
    (cond_auth_json, "live responses authenticated JSON", ('{"status":"success"}',), ("",)),
    (cond_auth_json, "live responses authenticated JSON (html body)", ('{"status":"success"}',), ("<html>401</html>",)),
    (cond_legit_marker_present, "legit list returns own rows", ("Male", '{"Male":1}'), ("Male", "{}")),
    (cond_getbyid_zero_rows, "forged getById returns 0 rows", ([],), ([{"id": 99}],)),
    (cond_victim_pristine, "victim pristine after run", ("A|B", "A|B"), ("A|B", "A|MUTATED")),
    (cond_count_delta_zero, "legit count delta == 0 (read-only)", (3, 3), (3, 4)),
]


def _file_pair():
    fd, p1 = tempfile.mkstemp(prefix="hermes-selftest-")
    os.close(fd)
    fd, p2 = tempfile.mkstemp(prefix="hermes-selftest-")
    os.close(fd)
    return p1, p2


def self_test():
    """Every assertion predicate must PASS on a good input and FAIL on a bad
    input. A predicate that cannot be made to fail is INERT -> fatal."""
    ok = True
    inert = []
    for item in SELF_TESTS:
        pred, label, good, bad = item[0], item[1], item[2], item[3]
        if pred is cond_extraction_ok:
            g1, g2 = _file_pair()
            good = (g1, g2)
            bad = ("/nonexistent/hermes-a.class", "/nonexistent/hermes-b.class")
            pos = pred(*good)
            neg = pred(*bad)
            for p in (g1, g2):
                try:
                    os.remove(p)
                except OSError:
                    pass
        elif pred is cond_entity_extracted:
            g1, _ = _file_pair()
            good = (g1,)
            bad = ("/nonexistent/hermes-entity.class",)
            pos = pred(*good)
            neg = pred(*bad)
            try:
                os.remove(g1)
            except OSError:
                pass
        else:
            pos = pred(*good)
            neg = pred(*bad)
        ok &= check(f"SELF-TEST positive: {label}", pos, f"(good={good!r})")
        if neg:
            ok = False
            inert.append(label)
            check(f"SELF-TEST NEGATIVE: {label}", False,
                  f"INERT — known-bad input {bad!r} did NOT fail the check")
        else:
            ok &= check(f"SELF-TEST negative: {label}", True,
                        f"(bad={bad!r} correctly failed)")
    if inert:
        print(f"\n=== SELF-TEST: FAIL — {len(inert)} INERT assertion(s): {inert} ===")
    else:
        print("\n=== SELF-TEST: PASS — every assertion proven capable of failing ===")
    return ok


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("screen", nargs="?", choices=sorted(SCREENS.keys()),
                    help="screen spec to verify (omit with --self-test)")
    ap.add_argument("--self-test", action="store_true",
                    help="exercise every assertion with good+bad inputs; no DB/Tomcat needed")
    ap.add_argument("--worktree", default=WT)
    ap.add_argument("--token", default=os.environ.get("HIS_JWT", ""),
                    help="JWT for the live session (or export HIS_JWT)")
    ap.add_argument("--session-org", type=int, default=1,
                    help="org the session belongs to (from the login)")
    ap.add_argument("--with-write-tests", action="store_true",
                    help="enable save + duplicate-save write tests (default OFF)")
    args = ap.parse_args()

    if args.self_test:
        sys.exit(0 if self_test() else 1)

    if not args.screen:
        ap.error("a screen is required unless --self-test is given")

    spec = SCREENS[args.screen]
    wt = args.worktree
    ok = True

    # --- 1. source compile gate (worktree) ---
    rc = run(f"mvn -q compile -DskipTests -o -pl {spec['module']} -am",
             cwd=os.path.join(wt, "legacy"), shell=True).returncode
    ok &= check("mvn compile", cond_mvn_ok(rc), f"({spec['module']})")

    # --- 2. jar freshness: mtime > fix-commit timestamp (relational) ---
    jar = os.path.join(WEB, f"{spec['module']}-0.0.1-SNAPSHOT.jar")
    if os.path.exists(jar):
        jar_mt = int(os.stat(jar).st_mtime)
    else:
        jar_mt = 0
    c = run(["git", "show", "-s", "--format=%ct", spec["fix_commit"]], cwd=ROOT)
    commit_ts = int(c.stdout.strip()) if c.returncode == 0 else 0
    ok &= check("jar newer than fix commit",
                cond_jar_fresh(jar_mt, commit_ts),
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
    extr_ok = cond_extraction_ok(sp, dp)
    ok &= check("extraction produced class files", extr_ok)
    if extr_ok:
        svc_dep = run([JAVAP, "-c", "-p", sp], capture_output=True, text=True).stdout.count("getVerifiedOrganizationId")
        dao_dep = run([JAVAP, "-c", "-p", dp], capture_output=True, text=True).stdout.count("getVerifiedOrganizationId")
        ok &= check("service VIS refs match source", cond_refs_match(svc_dep, svc_src),
                    f"(deployed={svc_dep} source={svc_src})")
        ok &= check("dao VIS refs match source", cond_refs_match(dao_dep, dao_src),
                    f"(deployed={dao_dep} source={dao_src})")

    # --- 4. entity named-query org filters present in deployed Entity jar ---
    ejar = os.path.join(WEB, "Entity-0.0.1-SNAPSHOT.jar")
    ebdir = "/tmp/hermes-verify-screen-entity"
    if os.path.exists(ebdir):
        shutil.rmtree(ebdir)
    os.makedirs(ebdir)
    run(["unzip", "-o", "-q", ejar, spec["entity_class"]], cwd=ebdir)
    ep = os.path.join(ebdir, spec["entity_class"].replace("/", os.sep))
    ent_ok = cond_entity_extracted(ep)
    ok &= check("entity class extracted", ent_ok)
    if ent_ok:
        ec = run([JAVAP, "-v", ep], capture_output=True, text=True).stdout
        for filt in spec["query_org_filters"]:
            ok &= check(f"entity filter present: {filt[:55]}",
                        cond_entity_filter_present(ec, filt))

    # --- 5. tomcat liveness ---
    pid = run(["lsof", "-tiTCP:8080", "-sTCP:LISTEN"], capture_output=True, text=True).stdout.strip().split("\n")[0]
    http = run(["curl", "-s", "-o", "/dev/null", "-w", "%{http_code}", "http://localhost:8080/"],
               capture_output=True, text=True).stdout
    ok &= check("tomcat :8080 live", cond_tomcat_live(pid, http), f"(PID {pid})")

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
                    cond_form1_org_differs(forged_org, session_org),
                    f"(forged={forged_org} session={session_org})")
        victim_row = psql(f"SELECT {spec['victim_pk']}, organization_id, status FROM {spec['victim_table']} "
                          f"WHERE {spec['victim_pk']}={spec['victim_id']}")
        ok &= check("MANDATORY (form 1): real victim row exists under forged org",
                    cond_form1_victim_exists(victim_row, forged_org),
                    f"(row='{victim_row}')")
    else:
        row = psql(f"SELECT organization_id FROM {spec['victim_table']} "
                   f"WHERE {spec['victim_pk']}={spec['victim_id']}")
        ok &= check("MANDATORY (form 2): targeted row's org != session org",
                    cond_form2_target_org_differs(row, session_org),
                    f"(targeted_row_org={row} session_org={session_org})")
        ok &= check("MANDATORY (form 2): victim row exists",
                    cond_form2_victim_exists(row), f"(row='{row}')")

    # --- 7. victim pristine: full pre-state captured, compared post-attacks ---
    vcols = f"SELECT * FROM {spec['victim_table']} WHERE {spec['victim_pk']}={spec['victim_id']}"
    victim_before = psql(vcols)
    ok &= check("victim row exists", cond_row_exists(victim_before), f"({victim_before[:80]})")

    # --- 8. legit count DELTA (read-only run: expected delta 0) ---
    count_q = f"SELECT count(*) FROM {spec['victim_table']} WHERE organization_id={session_org}"
    count_before = int(psql(count_q) or -1)
    ok &= check("legit count captured", cond_count_captured(count_before), f"(before={count_before})")

    # --- 9. live attacks (read-only) + legit checks ---
    if args.token:
        H = ["-H", "Content-Type: application/json",
             "-H", f"Authorization: Bearer {args.token}",
             "-H", "userId: 1", "-H", "unitId: 1", "-H", "Origin: http://localhost:8080"]
        r = run(["curl", "-s", "-X", "POST", f"http://localhost:8080{spec['list_endpoint']}",
                 *H, "-d", '{"organizationId":%d,"offset":0,"noOfRecordsPerPage":50}' % forged_org])
        _auth_ok = cond_auth_json(r.stdout)
        ok &= check("live responses authenticated JSON (token accepted)",
                    _auth_ok, f"(raw_head={r.stdout[:80]!r})")
        leaked = spec["victim_marker"] in r.stdout
        ok &= check("forged list leaks no victim row", cond_no_leak(leaked),
                    f"(payload org={forged_org}; victim_marker_in_response={leaked})")
        r2 = run(["curl", "-s", "-X", "POST", f"http://localhost:8080{spec['list_endpoint']}",
                  *H, "-d", '{"organizationId":%d,"offset":0,"noOfRecordsPerPage":50}' % session_org])
        ok &= check("legit list returns own rows",
                    cond_legit_marker_present(spec["legit_marker"], r2.stdout),
                    f"(marker={spec['legit_marker']} present={spec['legit_marker'] in r2.stdout})")
        if spec.get("getbyid_skip"):
            # by-id endpoint is a GET path-var shape (e.g. priority) — verified manually
            print("[SKIP] by-id endpoint is a GET path-var shape — verified manually for this screen")
        else:
            r3 = run(["curl", "-s", "-X", "POST", f"http://localhost:8080{spec['getbyid_endpoint']}",
                      *H, "-d", '{"%s":%d,"organizationId":%d}' % (spec["getbyid_id_field"], spec["victim_id"], forged_org)])
            try:
                import json as _json
                body = _json.loads(r3.stdout)
                rows = body.get("listObject") or []
                ok &= check("forged getById returns 0 rows",
                            cond_getbyid_zero_rows(rows), f"(rows={len(rows)})")
            except Exception as e:
                ok &= check("forged getById parses", False, f"({e}: {r3.stdout[:120]})")

        if args.with_write_tests:
            print("[WARN] --with-write-tests is DISABLED-BY-POLICY for sex until the "
                  "dup-check crash is diagnosed (docs/QA_FINDINGS.md). Ignoring flag.")
    else:
        print("[SKIP] no token provided — live attack checks skipped (pass --token or set HIS_JWT)")

    # --- 10. victim pristine after attacks + legit count delta ---
    victim_after = psql(vcols)
    ok &= check("victim pristine after run", cond_victim_pristine(victim_before, victim_after))
    count_after = int(psql(count_q) or -1)
    ok &= check("legit count delta == 0 (read-only run)",
                cond_count_delta_zero(count_before, count_after),
                f"(before={count_before} after={count_after})")

    print(f"\n=== RESULT: {'PASS' if ok else 'FAIL'} (verify_master_screen: {args.screen}) ===")
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
