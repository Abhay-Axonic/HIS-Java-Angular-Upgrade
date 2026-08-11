#!/usr/bin/env python3
"""check_commonmaster_gate.py — programmatic drift check for the commonMaster
moduleIds gate array.

The route's `moduleIds` array (new/src/app/app.routes.ts, commonMaster/:url)
is a COPIED CONSTANT from the legacy access-gate filter
(legacy/.../config/app.js, commonMaster state resolve `data`, the
`item.id == ...` chain, ~line 3910). Nothing detects drift: if the legacy
list changes, or if the copy was wrong, branches get denied while every gate
still passes.

This tool diffs the two lists programmatically (set equality both directions
+ duplicate check) and exits non-zero on ANY mismatch, naming the ids.

Usage:
    python3 tools/check_commonmaster_gate.py
    exit 0 = exact match; exit 1 = drift (details on stdout)

Preflight integration: the commonMaster screen preflight (C1-C9 family) runs
this as the gate-drift check — the route's moduleIds must match the legacy
access-gate array for that state, verified programmatically, not by eye.
"""
import re
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parent.parent
LEGACY = REPO / "legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js"
ROUTES = REPO / "new/src/app/app.routes.ts"

LEGACY_ANCHOR = "item.id == 412"  # the commonMaster gate filter chain start
ROUTE_ANCHOR = "path: 'commonMaster/:url'"


def legacy_ids() -> list[int]:
    text = LEGACY.read_text(encoding="utf-8", errors="replace")
    i = text.find(LEGACY_ANCHOR)
    if i < 0:
        raise RuntimeError(f"legacy anchor '{LEGACY_ANCHOR}' not found in app.js")
    # Bound the window at the chain terminator (the `);` that closes the
    # filter call) so a neighbouring state's filter (e.g. `item.id == 396`
    # in the next resolve) can never bleed into the commonMaster list.
    seg = text[i:i + 3000]
    end = seg.find(");")
    if end > 0:
        seg = seg[:end + 2]
    ids = [int(x) for x in re.findall(r"item\.id\s*==\s*(\d+)", seg)]
    if len(ids) < 40:
        raise RuntimeError(f"legacy gate chain looks truncated: {len(ids)} ids")
    return ids


def route_ids() -> list[int]:
    text = ROUTES.read_text(encoding="utf-8", errors="replace")
    j = text.find(ROUTE_ANCHOR)
    if j < 0:
        raise RuntimeError(f"route anchor '{ROUTE_ANCHOR}' not found in app.routes.ts")
    seg = text[j:j + 900]
    m = re.search(r"moduleIds: \[(.*?)\]", seg, re.S)
    if not m:
        raise RuntimeError("moduleIds array not found on commonMaster route")
    return [int(x) for x in re.findall(r"\d+", m.group(1))]


def main() -> int:
    try:
        legacy = legacy_ids()
        route = route_ids()
    except RuntimeError as e:
        print(f"GATE-DRIFT CHECK ERROR: {e}")
        return 2

    s_legacy, s_route = set(legacy), set(route)
    only_legacy = sorted(s_legacy - s_route)
    only_route = sorted(s_route - s_legacy)
    dup_legacy = sorted({x for x in legacy if legacy.count(x) > 1})
    dup_route = sorted({x for x in route if route.count(x) > 1})

    print(f"legacy gate ids: {len(legacy)}  route moduleIds: {len(route)}")
    print(f"only in legacy : {only_legacy or 'none'}")
    print(f"only in route  : {only_route or 'none'}")
    print(f"dups in legacy : {dup_legacy or 'none'}")
    print(f"dups in route  : {dup_route or 'none'}")

    if only_legacy or only_route or dup_legacy or dup_route:
        print("GATE-DRIFT FAIL — moduleIds array does not match the legacy "
              "access gate (see above)")
        return 1
    print("GATE-DRIFT PASS — route moduleIds exactly matches the legacy "
          "access-gate array (45 ids, no drift)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
