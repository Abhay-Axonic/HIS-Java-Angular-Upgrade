#!/usr/bin/env python3
"""C7 sweep across the 19 OTIMS states the walk listed as not-rebuilt —
the owner-directed one-pass check (2026-08-10): report which are already
rebuilt, live, dangle, commented-out, not-a-screen-yet BEFORE picking screen 21.

For each state: (1) C7 logic — does MAIN already have the feature dir (under
its kebab name) AND the route loadComponent? (2) legacy state registration in
app.js (.state('<name>') + commented?); (3) menu node (privilege tree
actionLink); (4) rebuild route presence in app.routes.ts."""
import json
import re
import subprocess
import sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
APPJS = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js"
ROUTES = f"{ROOT}/new/src/app/app.routes.ts"

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
    routes = open(ROUTES, encoding="utf-8").read()
    results = []
    for st in STATES:
        k = kebab(st)
        # C7 part 1: feature dir on main
        feats = git(["ls-tree", "-r", "--name-only", "main", "--",
                     f"new/src/app/features/{k}"])
        dir_hits = len([l for l in feats.splitlines() if l.strip()])
        # C7 part 2: route loadComponent on main
        route_hits = routes.count(f"path: '{st}'")
        comp_ref = f"features/{k}/"
        comp_hits = routes.count(comp_ref)
        # legacy state registration (name variants: exact camel + legacy spellings)
        token = f".state('{st}'"
        hits = [m.start() for m in re.finditer(re.escape(token), appjs)]
        commented = False
        if hits:
            before = appjs[max(0, hits[0] - 400):hits[0]]
            commented = before.rfind("/*") > before.rfind("*/")
        if not hits:
            # legacy may use a different spelling (e.g. operation_Table)
            alt = None
            for cand in [k, k.replace("-", "_")]:
                t2 = f".state('{cand}'"
                if t2 in appjs:
                    alt = cand
                    break
        state_status = ("COMMENTED" if commented else "REGISTERED") if hits else (
            f"ABSENT (alt:{alt})" if alt else "ABSENT")
        if dir_hits and route_hits:
            cls = "ALREADY-REBUILT"
        elif dir_hits and not route_hits:
            cls = "DIR-ONLY"
        elif route_hits and not dir_hits:
            cls = "ROUTE-ONLY"
        else:
            cls = "NOT-REBUILT"
        results.append((st, k, cls, state_status, dir_hits, route_hits, comp_hits))
    print(f"{'STATE':<32}{'DIR':<28}{'CLASS':<16}{'LEGACY'}")
    for st, k, cls, ss, dh, rh, ch in results:
        print(f"{st:<32}{k:<28}{cls:<16}{ss}")
    print()
    rebuilt = [r[0] for r in results if r[2] == "ALREADY-REBUILT"]
    print(f"ALREADY-REBUILT: {len(rebuilt)} — {rebuilt}")
    print(f"Genuinely NOT-REBUILT: {len([r for r in results if r[2] == 'NOT-REBUILT'])}")

if __name__ == "__main__":
    main()
