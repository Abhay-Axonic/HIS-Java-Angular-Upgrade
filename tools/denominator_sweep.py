#!/usr/bin/env python3
"""denominator_sweep.py — classify EVERY node in the privilege tree by top-level
subtree. No building, no deep-dive: per-parent counts of report/leaf, rebuilt,
shell/decoy, genuine candidate (+ partial/other), then a total.

Fixes the c7_sweep tool's blind spot: no-actionlink nodes get the name-map
fallback (node name -> registered state) so DANGLE candidates hidden among
menu-only leaves (the 686/701/703/744/747/760 class) are NOT mislabeled reports,
and MasterController+0-bindings shells (the 702 class) ARE labeled shells.
"""
import json
import os
import re
import subprocess
import sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
BACKEND = f"{ROOT}/legacy"
APPJS = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js"
INDEX = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/index.html"


def git(args):
    return subprocess.run(["git", "-C", ROOT] + args, capture_output=True, text=True)


def kebab(s):
    return re.sub(r"([a-z0-9])([A-Z])", r"\1-\2", s).lower()


def main():
    r = subprocess.run(
        ["psql", "-h", "localhost", "-d", "his_local", "-tAc",
         "select modules from privilege.privilege where role_id=1 limit 1;"],
        capture_output=True, text=True, timeout=20)
    tree = json.loads(r.stdout.strip())

    appjs = open(APPJS, encoding="utf-8").read()
    index = open(INDEX, encoding="utf-8", errors="replace").read()
    all_states = set(re.findall(r"\.state\('([^']+)'", appjs))

    # feature dirs + routes ON MAIN (authoritative rebuilt set)
    feats = git(["ls-tree", "-r", "--name-only", "origin/main", "--",
                 "new/src/app/features"]).stdout
    feat_dirs = set()
    for l in feats.splitlines():
        parts = l.split("/")
        if len(parts) >= 5 and parts[:4] == ["new", "src", "app", "features"]:
            feat_dirs.add(parts[4])
    routes_src = git(["show", "origin/main:new/src/app/app.routes.ts"]).stdout
    route_paths = set(re.findall(r"path:\s*'([^']+)'", routes_src))

    # backend controller classes: PascalCase -> file presence
    backend_dirs = [f"{BACKEND}/Global/src/main/java", f"{BACKEND}/ADT/src/main/java",
                    f"{BACKEND}/Billing/src/main/java", f"{BACKEND}/Pharmacy/src/main/java",
                    f"{BACKEND}/OTIMS/src/main/java", f"{BACKEND}/Entity/src/main/java"]
    java_files = {}
    for d in backend_dirs:
        for root, dirs, files in os.walk(d):
            if "target" in root:
                continue
            for f in files:
                if f.endswith(".java"):
                    java_files[f[:-5]] = os.path.join(root, f)

    # exact controller-JS basenames (sans 'Controller' suffix) loaded in index.html
    ctrl_roots = {c[:-len("Controller")].lower() for c in
                  set(re.findall(r"controller/[^\"']*?([A-Za-z0-9_]+Controller)\.js", index))}

    def classify(n):
        """Return (bucket, detail). Buckets:
        report    — menu-only leaf, no state, no backend
        rebuilt   — feature dir + route on main
        partial   — dir XOR route on main
        shell     — MasterController + 0-bind template (decoy) / commented-out state
        candidate — genuine DANGLE/LIVE shape (state + controller/template + backend)
        other     — menu'd but state absent (broken link), or state w/o backend
        """
        nid = n.get("id")
        name = (n.get("name") or "").strip()
        link = (n.get("actionLink") or "").strip()
        link_state = link.replace("#", "").strip("/")
        st = link_state.split("/")[0] if link_state else ""

        # rebuilt / partial by main's feature dir + route
        if st:
            k = kebab(st)
            dir_hit = k in feat_dirs
            route_hit = st in route_paths
            if dir_hit and route_hit:
                return "rebuilt", f"#{st}"
            if dir_hit or route_hit:
                return "partial", f"#{st} ({'dir' if dir_hit else ''}{'route' if route_hit else ''})"
        else:
            # no actionLink: name-map fallback (node name -> state). The candidate
            # set includes the acronym-preserved base AND a case-insensitive pass
            # (e.g. RMOAllocationMaster, QMS, opdBill, pendingForPOCreation) —
            # first-letter-lowercase alone misses those (the 2026-08-11 hand-check:
            # 4 state + 19 controller-basename misses; the full-bucket re-run found
            # 13 more case-insensitive misses of 511 report-classified nodes).
            base = re.sub(r"[^a-zA-Z0-9]", "", name)
            cands = {base[:1].lower() + base[1:], kebab(name), base.lower(), base}
            st = next((c for c in cands if c in all_states), "")
            if not st:
                low = base.lower()
                st = next((s for s in all_states if s.lower() == low), "")
            if st:
                # a no-actionlink node whose state is already rebuilt on main
                k = kebab(st)
                if k in feat_dirs and st in route_paths:
                    return "rebuilt", f"#{st} (name-mapped)"
                if k in feat_dirs or st in route_paths:
                    return "partial", f"#{st} ({'dir' if k in feat_dirs else ''}{'route' if st in route_paths else ''})"

        if not st:
            # exact controller-basename match (the 19 QMS/RMO/OTIMS-class misses):
            # the node has its own controller JS even without a matching state
            base = re.sub(r"[^a-zA-Z0-9]", "", (n.get("name") or "").strip())
            if base and base.lower() in ctrl_roots:
                return "candidate", f"#{base.lower()} (ctrl-basename)"
            return "report", ""

        # state-registered node: find its block for controller/template
        m = re.search(r"\.state\('" + re.escape(st) + r"'\s*,\s*\{", appjs)
        if not m:
            return "report", f"#{st} state-absent"
        start = m.start()
        nxt = appjs.find(".state('", start + 10)
        block = appjs[start:nxt if nxt > 0 else start + 1500]
        if re.search(r"/\*\s*$", appjs[max(0, start - 200):start]) and not re.search(r"\*/", appjs[max(0, start - 200):start]):
            return "shell", f"#{st} commented"
        ctrl = re.search(r"controller\s*:\s*[\"']([^\"']+)[\"']", block)
        ctrl_name = ctrl.group(1) if ctrl else ""
        tplm = re.search(r"templateUrl\s*:\s*'([^']+)'", block)
        tpl_path = tplm.group(1) if tplm else ""
        tpl = ""
        if tpl_path:
            try:
                tpl = open(f"{ROOT}/legacy/HIS-WAR/src/main/webapp/{tpl_path}",
                           encoding="utf-8", errors="replace").read()
            except OSError:
                tpl = ""
        tpl_bindings = len(re.findall(r"ng-(?:model|click|bind|repeat|change|show|if)\s*=", tpl)) \
            + len(re.findall(r"\{\{", tpl))

        # backend: PascalCase class name in Java, OR the controller's own JS
        # loaded in index.html (the endpoint-truth signal the class-name grep
        # misses — Java class names differ from state roots)
        pascal = "".join(w.capitalize() for w in
                         re.sub(r"([a-z0-9])([A-Z])", r"\1 \2", st).split())
        backend_hit = any(f.startswith(pascal) and "Controller" in f for f in java_files)
        if ctrl_name and not backend_hit:
            # controller loaded as a script (own controller JS, not MasterController)
            ctrl_loaded = bool(re.search(
                rf"controller/[^\"']*{re.escape(ctrl_name)}[^\"']*\.js", index))
            backend_hit = ctrl_loaded and ctrl_name.lower() != "mastercontroller"

        shell_state = "MasterController" in block or "masterController" in block
        if shell_state and tpl_bindings == 0:
            return "shell", f"#{st} MasterController+0bind"
        if backend_hit and tpl_bindings > 0:
            return "candidate", f"#{st} verified"
        if backend_hit or tpl_bindings > 0:
            return "candidate", f"#{st} (backend={'Y' if backend_hit else 'N'},bind={tpl_bindings})"
        return "other", f"#{st} no-backend-no-bind"

    # group by top-level root (walk parentId up to 0)
    by_id = {}
    for n in tree:
        by_id.setdefault(n.get("id"), []).append(n)

    def root_of(n, seen=None):
        seen = seen or set()
        pid = n.get("parentId")
        if pid in (0, None) or n.get("id") in seen:
            return n
        seen.add(n.get("id"))
        parents = by_id.get(pid, [])
        if not parents:
            return n
        # prefer the parent whose own parentId chain doesn't loop
        return root_of(parents[0], seen)

    counts = {}   # root_id -> {bucket: count}
    order = []
    cand_verified = cand_bind_only = 0
    for n in tree:
        root = root_of(n)
        rid = root.get("id")
        if rid not in counts:
            counts[rid] = {"root": (root.get("name") or "").strip(), "total": 0,
                           "report": 0, "rebuilt": 0, "partial": 0,
                           "shell": 0, "candidate": 0, "other": 0, "samples": {}}
            order.append(rid)
        c = counts[rid]
        c["total"] += 1
        bucket, detail = classify(n)
        c[bucket] += 1
        if bucket == "candidate":
            if detail.endswith("verified"):
                cand_verified += 1
            else:
                cand_bind_only += 1
        if len(c["samples"].get(bucket, [])) < 3 and detail:
            c["samples"].setdefault(bucket, []).append(f"{n.get('id')}:{detail}")

    print(f"{'ROOT':<6} {'SUBTREE':<30} {'TOT':>4} {'RPT':>4} {'REB':>4} {'PAR':>4} {'SHL':>4} {'CAND':>4} {'OTH':>4}")
    print("-" * 78)
    t_tot = t_rpt = t_reb = t_par = t_shl = t_cand = t_oth = 0
    for rid in sorted(order, key=lambda x: -(counts[x]["total"])):
        c = counts[rid]
        print(f"{rid:<6} {c['root'][:30]:<30} {c['total']:>4} {c['report']:>4} "
              f"{c['rebuilt']:>4} {c['partial']:>4} {c['shell']:>4} {c['candidate']:>4} {c['other']:>4}")
        t_tot += c["total"]; t_rpt += c["report"]; t_reb += c["rebuilt"]
        t_par += c["partial"]; t_shl += c["shell"]; t_cand += c["candidate"]
        t_oth += c["other"]
    print("-" * 78)
    print(f"{'TOTAL':<6} {'':<30} {t_tot:>4} {t_rpt:>4} {t_reb:>4} {t_par:>4} "
          f"{t_shl:>4} {t_cand:>4} {t_oth:>4}")
    print(f"\nLEGEND: RPT=report/leaf  REB=rebuilt  PAR=partial(dir|route)  "
          f"SHL=shell/decoy  CAND=genuine candidate  OTH=other(menu'd-state-absent/no-backend)")
    print(f"CANDIDATE SPLIT: {cand_verified} backend-verified, {cand_bind_only} bindings-only "
          f"(of {cand_verified + cand_bind_only} total)")
    print("\nCANDIDATE SAMPLES (first 3 per subtree):")
    for rid in order:
        s = counts[rid]["samples"].get("candidate", [])
        if s:
            print(f"  root {rid} ({counts[rid]['root']}): {', '.join(s)}")


if __name__ == "__main__":
    main()
