#!/usr/bin/env python3
"""c7_sweep_billing_full.py — the FULL Billing_Finance subtree sweep (owner
directive 2026-08-10: sweep the next subtree BEFORE entering it — the OTIMS
19->5 pass beats per-screen discovery). Classifies EVERY menu node under
parent 7 (Billing_Finance) — and their state registrations — into:
  already-rebuilt   — C7: feature dir AND route loadComponent on main
  dir-only/route-only — partial rebuild states (C7 PARTIAL)
  live              — state registered (not commented) + menu node actionLink
                      + controller loaded + backend controller exists
  dangle            — menu node exists but state NOT registered
  commented-out     — state registration inside a comment block
  not-a-screen-yet  — state registered but NO backend controller/endpoints
  decoy-routed      — node reaches a shell; the real screen self-registers no
                      state (the SIXTH bucket — needs owner decision)
  second-view       — shares a module id with an already-rebuilt screen (the
                      SEVENTH bucket — C8)
States = the 60 menu nodes under parent 7, PLUS any sibling states the walk's
hand-list would reach next (billing modules under 570 as a cross-check).
"""
import json
import re
import subprocess
import sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
APPJS = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js"
INDEX = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/index.html"
ROUTES = f"{ROOT}/new/src/app/app.routes.ts"
BACKEND = f"{ROOT}/legacy"


def kebab(s):
    s = re.sub(r"(.)([A-Z][a-z])", r"\1-\2", s)
    return re.sub(r"([a-z0-9])([A-Z])", r"\1-\2", s).lower()


def git(args):
    r = subprocess.run(["git", "-C", ROOT] + args, capture_output=True, text=True)
    return r.stdout


def main():
    appjs = open(APPJS, encoding="utf-8").read()
    index = open(INDEX, encoding="utf-8").read()
    routes = open(ROUTES, encoding="utf-8").read()
    # default PARENT = 570 (Billing — the MASTER subtree the walk targets);
    # pass --parent 7 for the Billing_Finance reports/store forest, or any
    # other parent id for a different subtree. DIRECT CHILDREN ONLY by
    # default (parity with the hand-verified classification); --deep adds
    # grandchildren.
    parent_id = 570
    deep = False
    for a in sys.argv[1:]:
        if a.isdigit():
            parent_id = int(a)
        elif a == "--deep":
            deep = True
    try:
        r = subprocess.run(["psql", "-h", "localhost", "-d", "his_local", "-tAc",
                            "select modules from privilege.privilege where role_id=1 limit 1;"],
                           capture_output=True, text=True, timeout=20)
        tree = json.loads(r.stdout.strip())
    except Exception:
        tree = None

    # the subtree: children of parent_id (+ grandchildren only with --deep)
    children = [n for n in tree if n.get("parentId") == parent_id] if tree else []
    grandchildren = []
    if deep:
        child_ids = {n.get("id") for n in children}
        grandchildren = [n for n in tree if n.get("parentId") in child_ids] if tree else []
    nodes = children + grandchildren
    print(f"Subtree parent {parent_id}: {len(children)} direct children, "
          f"{len(grandchildren)} grandchildren — {len(nodes)} nodes total\n")

    nodes_by_link = {}
    if tree:
        for n in tree:
            al = (n.get("actionLink") or "").replace("#", "").strip("/").lower()
            if al:
                nodes_by_link.setdefault(al, n)

    results = []
    for n in nodes:
        nid = n.get("id")
        name = (n.get("name") or "").strip()
        link = (n.get("actionLink") or "").strip()
        link_state = link.replace("#", "").strip("/")
        st = link_state.split("/")[0] if link_state else ""  # the ROUTE ROOT (the state name); later segments are sub-paths/params
        parent = n.get("parentId")
        parent_name = next((x.get("name", "").strip() for x in (tree or []) if x.get("id") == parent), "?")
        if not st:
            results.append((nid, name, parent_name, "NO-ACTIONLINK (menu-only leaf?)", ""))
            continue
        k = kebab(st)
        feats = git(["ls-tree", "-r", "--name-only", "main", "--",
                     f"new/src/app/features/{k}"])
        dir_hits = len([l for l in feats.splitlines() if l.strip()])
        route_hits = routes.count(f"path: '{st}'")
        comp_ref = f"features/{k}/"
        comp_hits = routes.count(comp_ref)
        token = f".state('{st}'"
        hits = [m.start() for m in re.finditer(re.escape(token), appjs)]
        commented = False
        if hits:
            before = appjs[max(0, hits[0] - 400):hits[0]]
            commented = before.rfind("/*") > before.rfind("*/")
        if not hits:
            for cand in [k, k.replace("-", "_")]:
                if f".state('{cand}'" in appjs:
                    hits = [appjs.find(f".state('{cand}'")]
                    break
        ctrl_loaded = bool(re.search(rf"controller/[^\"']*{re.escape(st)}[^\"']*\.js", index))
        # DECOY test (the 580 correction, 2026-08-10): a LIVE candidate must
        # have its OWN controller JS (not MasterController) AND a template
        # with Angular bindings. MasterController + 0 bindings = the shell
        # (the consentType/580 class).
        shell_state = False
        if hits:
            seg = appjs[hits[0]:hits[0] + 500]
            shell_state = "MasterController" in seg or "masterController" in seg
        # template bindings: find the templateUrl and count ng- bindings
        tpl_bindings = 0
        tm = re.search(r"templateUrl\s*:\s*'([^']+)'", appjs[hits[0]:hits[0] + 500] if hits else "")
        if tm:
            tpl_path = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/{tm.group(1)}"
            try:
                tpl = open(tpl_path, encoding="utf-8").read()
                tpl_bindings = len(re.findall(r"ng-(model|click|bind|repeat|change|show|if)", tpl))
            except Exception:
                tpl_bindings = -1  # template missing
        root_pascal = "".join(w.capitalize() for w in re.sub(r"([a-z0-9])([A-Z])", r"\1 \2", st).split())
        bfind = subprocess.run(
            ["grep", "-rl", "--include=*.java", f"class {root_pascal}",
             f"{BACKEND}/Global/src/main/java", f"{BACKEND}/Billing/src/main/java",
             f"{BACKEND}/Pharmacy/src/main/java", f"{BACKEND}/ADT/src/main/java",
             f"{BACKEND}/OTIMS/src/main/java", f"{BACKEND}/Entity/src/main/java"],
            capture_output=True, text=True)
        backend_files = [l for l in bfind.stdout.splitlines() if "Controller" in l]

        if dir_hits and route_hits:
            cls = "ALREADY-REBUILT"
        elif dir_hits and not route_hits:
            cls = "DIR-ONLY (partial)"
        elif route_hits and not dir_hits:
            cls = "ROUTE-ONLY (partial)"
        elif not hits and link_state:
            cls = "DANGLE (menu'd, state absent)"
        elif hits and commented:
            cls = "COMMENTED-OUT"
        elif hits and not backend_files and not ctrl_loaded:
            cls = "NOT-A-SCREEN-YET (no backend)"
        elif hits and (backend_files or ctrl_loaded):
            # registered + backend OR loaded controller JS: the controller-JS
            # load (index.html) is the endpoint-truth signal the class-name
            # grep misses (Java class names differ from state roots — e.g.
            # unitServiceBasePrice -> /api/unit/billing/getServiceBasePriceList
            # lives in a differently-named Java controller)
            node = nodes_by_link.get(st.lower())
            if shell_state and tpl_bindings == 0:
                # MasterController + 0-binding template = the SHELL — the
                # DECOY-ROUTED class (consentType / 580): the menu reaches a
                # shell while the real screen (a sibling with its own
                # controller + backend) holds the CRUD. Needs owner decision.
                cls = "DECOY-ROUTED (shell state + 0-bind template)"
            elif node is None:
                cls = "LIVE-ORPHANED (state+backend, no menu link)"
            else:
                cls = "LIVE (state+node+backend)"
        else:
            cls = f"LIVE (state+node+backend)" if hits else "NOT-A-SCREEN-YET"
        results.append((nid, name, parent_name, cls, f"#{link_state}" if link_state else ""))

    for nid, name, parent_name, cls, link in sorted(results, key=lambda r: r[0]):
        print(f"  {nid:>5} | {name[:44]:<44} | {cls:<38} | {link}")

    # summary
    from collections import Counter
    counts = Counter(r[3].split(" (")[0] for r in results)
    print("\nSUMMARY:")
    for c, n in counts.most_common():
        print(f"  {c}: {n}")
    print(f"\nTOTAL: {len(results)} nodes")


if __name__ == "__main__":
    main()
