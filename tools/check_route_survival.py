#!/usr/bin/env python3
"""check_route_survival.py — the ROUTE-SURVIVAL standing check (owner rule,
2026-08-13).

Any change to app.routes.ts must assert that EVERY existing route still
exists — not just that the new one arrived. A fuzzy patch corrupted the
kitCreation route while adding the cross-match route; check_route_targets
verifies targets resolve but cannot see that a route you did not mean to
touch is gone. This tool closes that gap.

Semantics:
- A BASELINE of every route path is stored (tools/route_paths_baseline.txt).
- The check: every baseline path must still be present in the CURRENT
  app.routes.ts — a missing path FAILS (the route was lost).
- After a DELIBERATE route removal, refresh the baseline with --update.
  The default run never writes; --update writes the current set.

USAGE:
  python3 tools/check_route_survival.py                # check (fail = a baseline route vanished)
  python3 tools/check_route_survival.py --update       # refresh the baseline (deliberate change)
  python3 tools/check_route_survival.py --routes <file> --baseline <file>   # testable form
"""

import argparse
import os
import re
import sys

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DEFAULT_ROUTES = os.path.join(REPO, "new", "src", "app", "app.routes.ts")
DEFAULT_BASELINE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "route_paths_baseline.txt")

PATH_RE = re.compile(r"path:\s*'([^']+)'")


def extract_paths(routes_file):
    with open(routes_file, encoding="utf-8") as fh:
        src = fh.read()
    paths = PATH_RE.findall(src)
    # drop the empty-string path (the catch-all) if present
    return sorted(p for p in paths if p)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--routes", default=DEFAULT_ROUTES)
    ap.add_argument("--baseline", default=DEFAULT_BASELINE)
    ap.add_argument("--update", action="store_true", help="refresh the baseline (deliberate route change)")
    args = ap.parse_args()

    current = extract_paths(args.routes)
    if args.update:
        with open(args.baseline, "w", encoding="utf-8") as fh:
            fh.write("\n".join(current) + "\n")
        print(f"route-survival baseline updated: {len(current)} routes -> {args.baseline}")
        return 0

    if not os.path.exists(args.baseline):
        print("FAIL: no baseline file — run with --update once to create it")
        return 1
    with open(args.baseline, encoding="utf-8") as fh:
        baseline = sorted(line.strip() for line in fh if line.strip())

    missing = [p for p in baseline if p not in current]
    extra = [p for p in current if p not in baseline]
    if missing:
        print(f"FAIL: {len(missing)} baseline route(s) VANISHED (a route you did not mean to touch is gone):")
        for p in missing:
            print("  -", p)
        print("If the removal was deliberate, re-run with --update.")
        return 1
    print(f"route-survival PASS: all {len(baseline)} baseline routes still present "
          f"({len(extra)} new route(s) since the baseline)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
