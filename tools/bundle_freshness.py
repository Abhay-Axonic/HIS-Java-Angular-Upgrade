#!/usr/bin/env python3
"""bundle_freshness.py — served-bundle freshness gate for browser probes.

A stale dev-server bundle silently runs code that no longer matches the
working tree. TWO false findings came from this class:
  1. screen-11 stale tree (smoke harness — fixed by the SERVE-BRANCH
     assertion, RULES.md:371)
  2. the guard fail-open retraction (2026-08-12) — a broken HMR compile at
     module-access.guard.ts:58 (TS2345, `moduleIds ?? [moduleId]` union
     producing `(number|undefined)[]`) froze ng serve at last-good while the
     source showed the fix; the probe ran against the frozen bundle.
Both times the browser was running code that did not match the tree, and the
probe reported a behavioural result anyway.

STANDING RULE (owner directive 2026-08-12, same shape as the smoke harness's
branch assertion): before ANY behavioural assertion, the probe verifies the
served bundle matches the current source; a stale bundle FAILS the probe
loudly (distinct exit code 2, FRESHNESS-FAIL lines) instead of producing a
finding.

Usage:
    from bundle_freshness import check_freshness
    fails = check_freshness([
        {"label": "module-access.guard",
         "source": "new/src/app/core/module-access.guard.ts",
         "url": "http://localhost:4201/main.js",
         "absent": ["GUARD-DEBUG", "GUARD-NAV", "GUARD-DENY", "navigateByUrl"]},
    ])
    if fails:
        sys.exit(2)   # loud, distinct from behavioural FAIL (exit 1)

How it detects staleness without a hash:
  - reads the CURRENT source file, extracts its distinctive identifier-like
    string literals (len >= 8)
  - fetches the served bundle
  - requires the 3 longest current-source literals to be present in the
    served bundle (the served bundle is frozen at last-good, so a source edit
    that never compiled shows up as a missing literal)
  - applies the caller's explicit `absent` markers (debug leftovers that must
    not be in the served bundle)
"""
import re
import sys
import urllib.request

EXIT_STALE = 2
_LITERAL_RE = re.compile(r"""['"]([A-Za-z_][A-Za-z0-9_]{7,})['"]""")


def _read_source(path):
    with open(path, encoding="utf-8", errors="replace") as f:
        return f.read()


def _fetch(url, timeout=30):
    req = urllib.request.Request(url, headers={"User-Agent": "freshness-probe"})
    with urllib.request.urlopen(req, timeout=timeout) as r:
        return r.read().decode("utf-8", errors="replace")


def _current_source_markers(source_text, n=3):
    """Distinctive identifier-like string literals from the CURRENT source."""
    seen = []
    for m in _LITERAL_RE.finditer(source_text):
        lit = m.group(1)
        if lit not in seen:
            seen.append(lit)
    # longest first — the most distinctive
    seen.sort(key=len, reverse=True)
    return seen[:n]


def check_freshness(specs, base_url=None, timeout=30):
    """Return list of failure strings (empty = bundle fresh for all specs).

    spec keys: label (str), source (path), url (absolute, or relative when
    base_url given), present (optional extra required markers), absent
    (optional markers that must not appear).
    """
    failures = []
    for spec in specs:
        label = spec["label"]
        try:
            src = _read_source(spec["source"])
        except OSError as e:
            failures.append(f"{label}: source unreadable ({e})")
            continue
        url = spec["url"]
        if base_url and url.startswith("/"):
            url = base_url.rstrip("/") + url
        try:
            served = _fetch(url, timeout)
        except Exception as e:  # noqa: BLE001 — any fetch failure is a FAIL
            failures.append(f"{label}: served bundle unreadable at {url} ({e})")
            continue
        # 1. current-source literals must be present (frozen bundle lacks them)
        for lit in _current_source_markers(src):
            if lit not in served:
                failures.append(
                    f"{label}: STALE — served {url} lacks current-source "
                    f"marker '{lit}'; ng serve frozen at last-good?")
        # 2. explicit required markers
        for mk in spec.get("present", []):
            if mk not in served:
                failures.append(f"{label}: served {url} lacks required marker '{mk}'")
        # 3. explicit absent markers (debug leftovers / old code)
        for mk in spec.get("absent", []):
            if mk in served:
                failures.append(
                    f"{label}: served {url} CONTAINS forbidden marker '{mk}' "
                    f"(contaminated bundle, not current source)")
    return failures


def main():
    """CLI: check_freshness from JSON spec on argv[1]."""
    import json
    if len(sys.argv) < 2:
        print("usage: bundle_freshness.py spec.json [base_url]", file=sys.stderr)
        sys.exit(2)
    spec = json.load(open(sys.argv[1]))
    base = sys.argv[2] if len(sys.argv) > 2 else None
    fails = check_freshness(spec, base_url=base)
    if fails:
        print("FRESHNESS-FAIL:")
        for f in fails:
            print("  -", f)
        sys.exit(EXIT_STALE)
    print("FRESHNESS PASS — served bundle matches current source")
    sys.exit(0)


if __name__ == "__main__":
    main()
