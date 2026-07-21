#!/usr/bin/env python3
"""
Static census for the disconnected-view/controller bug found in
docs/ANGULAR_PENDING_FINDINGS.md entry 80 (relationMaster/occupationMaster/
maritalstatusMaster/religionMaster/referralTypeMaster/patientSourceMaster,
6 confirmed live-broken screens, identical 3-axis break: wrong Save/Update
function names, wrong form-model binding, wrong list-render variable).

Entry 80's original framing scoped this to the `adt/global/` controller
folder, but `maritalstatusMaster`'s controller lives elsewhere entirely --
the true common thread across all 6 confirmed screens is their shared
`views/master/GlobalMaster/*.html` VIEW template location (excluding the
`Adt/` subfolder, which is a separate, already-audited family), a
180-screen population, not the 19-screen `adt/global/` controller-folder
subset originally checked. This script runs the same axis-1 check (the
most decisive of the 3 -- 100% accurate against the 19 hand-verified
`adt/global/` screens) against the full 180-screen population.

Checks, for each screen:
  - axis 1 (primary signal): does the view's Save/Update `ng-click` call a
    function name that isn't defined as `$scope.<name> = function` anywhere
    in the resolved controller?
  - axis 3 (secondary signal, where determinable): does the view's main
    `ng-repeat` list variable ever get assigned via `$scope.<name> =` in the
    controller?

A screen flagged on axis 1 is a strong candidate for entry 80's exact bug
class. axis 3 alone (without axis 1) is weaker signal (many false shapes
exist -- client-side-filtered arrays, computed lists, etc.) and is reported
separately, not merged into the primary flag.

Usage:
    python3 tools/angular-audit/check_disconnected_wiring.py [--built-only] [--exclude-built]

Read-only against app source. No live HTTP requests (that's a separate,
manual verification step for anything this flags).
"""
import argparse
import json
import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from common import WEBAPP, STATES_ENRICHED_JSON, REPO_ROOT
from extract_states import strip_js_comments, strip_html_comments

ROUTES_TS = REPO_ROOT / "new" / "src" / "app" / "app.routes.ts"


def built_route_urls():
    if not ROUTES_TS.exists():
        return set()
    text = ROUTES_TS.read_text(errors="replace")
    return {"/" + m for m in re.findall(r"path:\s*'([^']*)'", text) if m}


def axis1_mismatch(ctrl_text, tmpl_text):
    ngclicks = re.findall(r'ng-click\s*=\s*"([^"]+)"', tmpl_text)
    save_update_calls = [c for c in ngclicks if re.search(r'\b(save|update)[A-Za-z]*\s*\(', c, re.I)]
    defined = set(re.findall(r'\$scope\.([A-Za-z_][A-Za-z0-9_]*)\s*=\s*function', ctrl_text))
    mismatches = []
    for call in save_update_calls:
        m = re.match(r'([A-Za-z_][A-Za-z0-9_]*)\s*\(', call.strip())
        if m and m.group(1) not in defined:
            mismatches.append(m.group(1))
    return save_update_calls, sorted(set(mismatches))


def axis3_list_var_unassigned(ctrl_text, tmpl_text):
    repeats = re.findall(r'ng-repeat\s*=\s*"[^"]*\sin\s+([A-Za-z_][A-Za-z0-9_.]*)', tmpl_text)
    if not repeats:
        return None, None
    # take the most common / first table-like repeat var, strip any filter chain already excluded by the regex
    var = repeats[0].split('.')[0]
    assigned = bool(re.search(rf'\$scope\.{re.escape(var)}\s*=(?!=)', ctrl_text))
    return var, assigned


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--built-only", action="store_true", help="only check screens already in app.routes.ts")
    ap.add_argument("--exclude-built", action="store_true", help="skip screens already in app.routes.ts")
    args = ap.parse_args()

    states = json.loads(STATES_ENRICHED_JSON.read_text())
    built = built_route_urls()

    pool = [
        s for s in states
        if (s.get("templateUrl") or "").startswith("views/master/GlobalMaster/")
        and not (s.get("templateUrl") or "").startswith("views/master/GlobalMaster/Adt/")
    ]
    if args.built_only:
        pool = [s for s in pool if s.get("url") in built]
    elif args.exclude_built:
        pool = [s for s in pool if s.get("url") not in built]

    print(f"Population: {len(pool)} screens (views/master/GlobalMaster/*.html, excluding Adt/ subfolder)")

    axis1_flagged = []
    axis3_flagged = []
    no_signal = []
    missing_files = []

    for s in pool:
        name = s["name"]
        ctrl_files = s.get("ctrl_files") or []
        tmpl = s.get("templateUrl")
        if not ctrl_files or not tmpl:
            missing_files.append(name)
            continue
        ctrl_path = WEBAPP / ctrl_files[0]
        tmpl_path = WEBAPP / tmpl
        if not ctrl_path.exists() or not tmpl_path.exists():
            missing_files.append(name)
            continue

        ctrl_text = strip_js_comments(ctrl_path.read_text(errors="replace"))
        tmpl_text = strip_html_comments(tmpl_path.read_text(errors="replace"))

        calls, mismatches = axis1_mismatch(ctrl_text, tmpl_text)
        var, assigned = axis3_list_var_unassigned(ctrl_text, tmpl_text)

        if mismatches:
            axis1_flagged.append({
                "name": name, "module": s.get("module"), "url": s.get("url"),
                "built": s.get("url") in built,
                "ctrl": ctrl_files[0], "mismatches": mismatches, "all_calls": calls,
            })
        elif var and assigned is False:
            axis3_flagged.append({
                "name": name, "module": s.get("module"), "url": s.get("url"),
                "built": s.get("url") in built, "ctrl": ctrl_files[0], "list_var": var,
            })
        elif not calls:
            no_signal.append(name)

    print(f"\n{'='*70}")
    print(f"AXIS-1 FLAGGED (wrong Save/Update function name -- entry #80's exact signature): {len(axis1_flagged)}")
    for f in sorted(axis1_flagged, key=lambda x: not x["built"]):
        tag = "*** ALREADY BUILT ***" if f["built"] else ""
        print(f"  - {f['name']} ({f['module']}) mismatches={f['mismatches']} {tag}")

    print(f"\nAXIS-3-ONLY FLAGGED (list-render variable never assigned, no axis-1 mismatch -- weaker signal): {len(axis3_flagged)}")
    for f in sorted(axis3_flagged, key=lambda x: not x["built"]):
        tag = "*** ALREADY BUILT ***" if f["built"] else ""
        print(f"  - {f['name']} ({f['module']}) list_var={f['list_var']} {tag}")

    print(f"\nNo Save/Update ng-click found at all (not applicable / different UI shape): {len(no_signal)}")
    print(f"Missing controller/template file on disk: {len(missing_files)} -> {missing_files}")

    import json as _json
    out = {
        "population": len(pool),
        "axis1_flagged": axis1_flagged,
        "axis3_flagged": axis3_flagged,
        "no_signal_count": len(no_signal),
        "missing_files": missing_files,
    }
    out_path = Path(__file__).resolve().parent / "data" / "disconnected_wiring_report.json"
    out_path.write_text(_json.dumps(out, indent=2))
    print(f"\nFull detail written to {out_path}")


if __name__ == "__main__":
    main()
