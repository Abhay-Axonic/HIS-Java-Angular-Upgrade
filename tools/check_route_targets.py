#!/usr/bin/env python3
"""check_route_targets.py — preflight gate: every route's loadComponent path
must resolve to a file that exists.

WHY (DQ-2026-08-12-028 / consentType incident): the b10 delegation created
new/src/app/features/consent-type/ but never staged it; the route 'consentType'
(528) shipped in commit 972c69dd with a lazy import pointing at a file absent
from the tree. tsc misses it (a lazy path is only resolved at navigation);
the live probe missed it (it sampled other screens); the route-presence check
missed it (presence != target existence). This static check catches it before
any probe runs.

USAGE:
  python3 tools/check_route_targets.py [--ref <git-ref|WORKTREE>]
    default: check the WORKING TREE (new/src/app/app.routes.ts + files on disk)
    --ref HEAD: check the committed tree (routes at HEAD, files at HEAD)
    --ref <sha>/<branch>: same against that ref (e.g. a branch tip before merge)

EXIT CODES: 0 = all targets resolve; 1 = one or more missing targets.

NEGATIVE CONTROL (must FAIL): point any route's loadComponent at a
nonexistent file, e.g. temporarily:
  path: 'x' ... import('./features/does-not-exist/x')
then run the check — it must exit 1 naming the route. Restore afterwards.
"""
import os
import re
import subprocess
import sys

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ROUTES_REL = "new/src/app/app.routes.ts"

PATH_RE = re.compile(
    r"path:\s*'([^']+)'[\s\S]*?loadComponent:\s*\(\)\s*=>\s*import\('([^']+)'\)"
)


def git(args: list[str]) -> str:
    r = subprocess.run(["git", "-C", REPO] + args, capture_output=True, text=True)
    if r.returncode != 0:
        raise RuntimeError(f"git {' '.join(args)} failed: {r.stderr[:300]}")
    return r.stdout


def main() -> int:
    ref = "WORKTREE"
    if len(sys.argv) > 1:
        if sys.argv[1] == "--ref":
            ref = sys.argv[2] if len(sys.argv) > 2 else "HEAD"
        else:
            ref = sys.argv[1]

    if ref == "WORKTREE":
        routes_src = open(os.path.join(REPO, ROUTES_REL), encoding="utf-8").read()
        tree = set(
            os.path.relpath(os.path.join(dp, f), REPO)
            for dp, _, fns in os.walk(os.path.join(REPO, "new/src/app/features"))
            for f in fns
            if f.endswith(".ts")
        )
        # also include core/shared .ts that routes may import
        for extra in ("new/src/app/core", "new/src/app/shared"):
            tree |= {
                os.path.relpath(os.path.join(dp, f), REPO)
                for dp, _, fns in os.walk(os.path.join(REPO, extra))
                for f in fns
                if f.endswith(".ts")
            }
    else:
        routes_src = git(["show", f"{ref}:{ROUTES_REL}"])
        tree = set(git(["ls-tree", "-r", "--name-only", ref, "--", "new/src/app/"]).splitlines())

    missing = []
    total = 0
    for m in PATH_RE.finditer(routes_src):
        path, imp = m.group(1), m.group(2)
        total += 1
        rel = imp.lstrip("./")
        if rel.startswith("features/") or rel.startswith("core/") or rel.startswith("shared/"):
            file = f"new/src/app/{rel}.ts"
        else:
            file = os.path.normpath(os.path.join(os.path.dirname(ROUTES_REL), rel)) + ".ts"
        if file not in tree:
            missing.append((path, imp, file))

    print(f"check_route_targets[{ref}]: {total} routes scanned")
    if missing:
        print(f"FAIL: {len(missing)} route(s) with unresolvable loadComponent target:")
        for path, imp, file in missing:
            print(f"  route '{path}' -> import '{imp}' -> file '{file}' MISSING")
        return 1
    print("PASS: all loadComponent targets resolve to existing files")
    return 0


if __name__ == "__main__":
    sys.exit(main())
