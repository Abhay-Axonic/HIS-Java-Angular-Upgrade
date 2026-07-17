#!/usr/bin/env python3
"""
RULES.md circuit-breaker condition 6: "A try/catch block is removed without
a documented alternative error path in the centralized handler." See
docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md, Tier 1.

Heuristic, diff-based PR check -- NOT a static analyzer. Looks at the diff
between a base ref and HEAD for *.java files. If any `catch (` line is
removed, the diff must also show some evidence the removal was intentional
and routed: either
  (a) an added line referencing GlobalExceptionHandler / ErrorCorrelationUtil
      / "routed to" / "centralized handler" (anywhere in the diff), or
  (b) a PR description/commit message passed via --justification containing
      the same evidence (CI wires this from the PR body).

This will both false-positive (e.g. a catch block moved, not removed) and
false-negative (a removal disguised as an edit). It's a prompt for human
review, not a verdict -- same spirit as this project's other heuristic
scripts in this directory (find_swallowed_return_pattern.py etc.), which
are explicitly documented as "candidates for manual verification."

Usage:
  python3 scripts/check_trycatch_removal.py <base-ref> [--justification TEXT]

Exit code 0: no removed catch blocks, or removal looks routed.
Exit code 1: removed catch block(s) found with no routing evidence in the
             diff -- fails the PR check, does not silently pass.
"""
import re
import subprocess
import sys

CATCH_RE = re.compile(r'^-\s*\}?\s*catch\s*\(')
ROUTING_EVIDENCE_RE = re.compile(
    r'GlobalExceptionHandler|ErrorCorrelationUtil|centralized handler|routed to',
    re.IGNORECASE,
)


def get_diff(base_ref):
    result = subprocess.run(
        ["git", "diff", f"{base_ref}...HEAD", "--", "*.java"],
        capture_output=True, text=True, check=True,
    )
    return result.stdout


def main():
    if len(sys.argv) < 2:
        print("Usage: check_trycatch_removal.py <base-ref> [--justification TEXT]")
        sys.exit(2)

    base_ref = sys.argv[1]
    justification = ""
    if "--justification" in sys.argv:
        idx = sys.argv.index("--justification")
        if idx + 1 < len(sys.argv):
            justification = sys.argv[idx + 1]

    diff = get_diff(base_ref)
    removed_catch_lines = [line for line in diff.splitlines() if CATCH_RE.match(line)]

    if not removed_catch_lines:
        print("check_trycatch_removal: no removed catch blocks in diff. OK.")
        sys.exit(0)

    has_routing_evidence = bool(ROUTING_EVIDENCE_RE.search(diff)) or bool(
        ROUTING_EVIDENCE_RE.search(justification)
    )

    print(f"check_trycatch_removal: {len(removed_catch_lines)} removed catch block line(s) found:")
    for line in removed_catch_lines:
        print(f"  {line.strip()}")

    if has_routing_evidence:
        print("Routing evidence found (GlobalExceptionHandler/ErrorCorrelationUtil/"
              "'centralized handler'/'routed to' referenced in diff or PR justification). OK.")
        sys.exit(0)

    print()
    print("FAIL: try/catch block(s) removed with no evidence of routing to the")
    print("centralized exception handler, per RULES.md's non-negotiable rule:")
    print('  "Never remove or alter a local try/catch without routing the')
    print('   exception to the single centralized global exception handler."')
    print("If this removal is legitimate, document the replacement error path")
    print("(reference GlobalExceptionHandler or ErrorCorrelationUtil) in the")
    print("diff or PR description, or restore the catch block.")
    sys.exit(1)


if __name__ == "__main__":
    main()
