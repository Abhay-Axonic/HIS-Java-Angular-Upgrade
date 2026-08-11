#!/usr/bin/env python3
"""count_dead_filters.py — ITEM 3 (count closure): the 18 mixed screens
classified + the 5 unbound-input screens confirmed as dead UI stubs.

The BOTH-criteria test (the linen-issue lesson): a search box is DEAD when
the input mutates a plain property AND no computed that reads it exists (a
computed re-evaluates only when a SIGNAL it reads changes). LIVE when the
property is a signal read by a computed, OR the value is sent server-side.

Classification per feature dir with a search-ish input:
  - SIGNAL-FILTER (live client filter): searchTerm = signal('') + computed
    reads searchTerm().
  - SERVER-KEYWORD (live server filter): the search value is sent in the
    API payload (server-side search).
  - PLAIN-PROP-DEAD (the dead-filter defect): a plain property + ngModel,
    no computed reads it, not sent server-side.
  - STUB-UNBOUND (dead UI stub): a search input with NO wiring at all
    (placeholder-only; the 5 unbound screens).

The 5 unbound candidates (from the copied-filter audit): delivery-type-master,
immunization-schedule, nutritional-master, pharmacy-formulation-type-master,
schedule-worklist.
"""
import os
import re

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade/new/src/app/features"

UNBOUND = [
    "delivery-type-master", "immunization-schedule", "nutritional-master",
    "pharmacy-formulation-type-master", "schedule-worklist",
]


def classify(feat):
    ts = None
    for f in os.listdir(os.path.join(ROOT, feat)):
        if f.endswith(".ts") and not f.endswith(".spec.ts") and not f.endswith("models.ts") and "service" not in f:
            ts = open(os.path.join(ROOT, feat, f), encoding="utf-8").read()
    if ts is None:
        return "NO-TS"
    has_search_input = bool(re.search(r"searchTerm|quickFilter|searchText|filterText|searchKeyword", ts))
    if not has_search_input:
        return "NO-SEARCH-REF"
    # signal-bound?
    signal_bound = bool(re.search(r"(searchTerm|quickFilter|searchText)\s*=\s*signal\(", ts))
    # computed reading it?
    computed_reads = bool(re.search(r"computed\s*\([^)]*\)\s*\{[\s\S]{0,400}?(searchTerm|quickFilter)\(\)", ts)) or \
                     bool(re.search(r"computed\s*\([\s\S]{0,200}?=>\s*[\s\S]{0,200}?(searchTerm|quickFilter)\(\)", ts))
    # sent server-side?
    server_side = bool(re.search(r"(searchTerm|quickFilter|searchText|filterText|searchKeyword)\s*[:=]\s*[^,;{}]+\s*,?\s*$", ts, re.M)) and \
                  bool(re.search(r"(this\.base|http\.(get|post)|\.post\(|\.get\()", ts))
    if signal_bound and computed_reads:
        return "SIGNAL-FILTER (live)"
    if server_side:
        return "SERVER-KEYWORD (live server-side)"
    if re.search(r"(searchTerm|quickFilter)\s*=\s*['\"]", ts) and not signal_bound:
        return "PLAIN-PROP-DEAD (the defect)"
    return "STUB-UNBOUND (dead UI stub)"


def main():
    rows = []
    for feat in sorted(os.listdir(ROOT)):
        fpath = os.path.join(ROOT, feat)
        if not os.path.isdir(fpath):
            continue
        c = classify(feat)
        if c not in ("NO-TS", "NO-SEARCH-REF"):
            rows.append((feat, c))
    print(f"FEATURES WITH SEARCH REFS: {len(rows)}")
    print()
    for feat, c in rows:
        mark = "  <-- 5-UNBOUND" if feat in UNBOUND else ""
        print(f"{feat}: {c}{mark}")
    print()
    for u in UNBOUND:
        found = [f for f, c in rows if f == u]
        print(f"UNBOUND {u}: {'FOUND -> ' + dict(rows).get(u, '?') if found else 'MISSING (no search ref at all)'}")


if __name__ == "__main__":
    main()
