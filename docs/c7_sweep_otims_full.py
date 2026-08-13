#!/usr/bin/env python3
"""c7_sweep_otims_full.py — the FULL 19-state OTIMS classification (owner
directive 2026-08-10, third ask). For each state, classifies:
  already-rebuilt   — C7: feature dir AND route loadComponent on main
  dir-only/route-only — partial rebuild states (C7 PARTIAL)
  live              — state registered (not commented) + menu node actionLink
                      + controller loaded + backend controller exists
  dangle            — menu node exists but state NOT registered (legacy
                      navigates to NG04002) — the drug-recall class
  commented-out     — state registration inside a comment block
  not-a-screen-yet  — state registered but NO backend controller/endpoints
States: the 19 the walk's hand-list called 'not rebuilt' (the owner's list)."""
import json
import re
import subprocess
import sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
APPJS = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js"
INDEX = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/index.html"
ROUTES = f"{ROOT}/new/src/app/app.routes.ts"
BACKEND = f"{ROOT}/legacy"

STATES = ["procedureType", "operationTableType", "anaesthesiaType", "checklistItem",
          "checklistMaster", "staffRole", "consentType", "inventoryItem",
          "inventoryItemType", "equipmentType", "serviceType", "insuranceCompany",
          "typeOfStay", "generalPractitioner", "surgicalCalendar",
          "resourceChargesConfiguration", "notesMaster", "plannedProcedure",
          "procedureTemplate"]

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
    # privilege tree (menu nodes) via psql — same loader as preflight C5
    try:
        r = subprocess.run(["psql", "-h", "localhost", "-d", "his_local", "-tAc",
                            "select modules from privilege.privilege where role_id=1 limit 1;"],
                           capture_output=True, text=True, timeout=20)
        tree = json.loads(r.stdout.strip())
    except Exception:
        tree = None
    nodes_by_link = {}
    if tree:
        for n in tree:
            al = (n.get("actionLink") or "").replace("#", "").strip("/").lower()
            if al:
                nodes_by_link.setdefault(al, n)

    for st in STATES:
        k = kebab(st)
        # --- C7: rebuild presence on main
        feats = git(["ls-tree", "-r", "--name-only", "main", "--",
                     f"new/src/app/features/{k}"])
        dir_hits = len([l for l in feats.splitlines() if l.strip()])
        route_hits = routes.count(f"path: '{st}'")
        comp_ref = f"features/{k}/"
        comp_hits = routes.count(comp_ref)
        # --- legacy state registration (exact + kebab/underscore alternates)
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
        # --- menu node (actionLink)
        node = nodes_by_link.get(st.lower())
        node_chain = ""
        if node and tree:
            by_id = {n.get("id"): n for n in tree}
            p = by_id.get(node.get("parentId"))
            chain = [node.get("name", "").strip()]
            while p:
                chain.append(p.get("name", "").strip())
                p = by_id.get(p.get("parentId"))
            node_chain = " -> ".join(chain[:3])
        # --- controller JS loaded in index.html
        ctrl_js = "controller" in index.lower()  # placeholder; refined below
        ctrl_loaded = bool(re.search(rf"controller/[^\"']*{re.escape(st)}[^\"']*\.js", index))
        # --- backend Java controller (class name contains the state root)
        root_pascal = "".join(w.capitalize() for w in re.sub(r"([a-z0-9])([A-Z])", r"\1 \2", st).split())
        bfind = subprocess.run(
            ["grep", "-rl", "--include=*.java", f"class {root_pascal}",
             f"{BACKEND}/Global/src/main/java", f"{BACKEND}/Billing/src/main/java",
             f"{BACKEND}/Pharmacy/src/main/java", f"{BACKEND}/ADT/src/main/java",
             f"{BACKEND}/OTIMS/src/main/java", f"{BACKEND}/Entity/src/main/java"],
            capture_output=True, text=True)
        backend_files = [l for l in bfind.stdout.splitlines() if "Controller" in l]
        # --- classification
        if dir_hits and route_hits:
            cls = "ALREADY-REBUILT"
        elif dir_hits and not route_hits:
            cls = "DIR-ONLY (partial)"
        elif route_hits and not dir_hits:
            cls = "ROUTE-ONLY (partial)"
        elif not hits and node:
            cls = "DANGLE (menu'd, state absent)"
        elif hits and commented:
            cls = "COMMENTED-OUT"
        elif hits and not backend_files and not ctrl_loaded:
            cls = "NOT-A-SCREEN-YET (no backend)"
        elif hits and node:
            cls = "LIVE"
        else:
            cls = "UNCLASSIFIED"
        print(f"{st:<34}{k:<26}{cls:<34}legacy={'reg' if hits else 'abs'}"
              f"{'/' + ('cmt' if commented else '') if hits else ''} "
              f"node={'Y' if node else 'N'} ctrlJS={'Y' if ctrl_loaded else 'N'} "
              f"java={'Y' if backend_files else 'N'} {node_chain}")

if __name__ == "__main__":
    main()
