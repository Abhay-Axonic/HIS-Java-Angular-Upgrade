#!/usr/bin/env python3
"""
check_ledger_merge_state.py — two-direction ledger/merge-state check (RULES.md rule 9d).

Assertions over docs/BR_INDEX.md (the pipe-separated BR index), verified against git:
  A. Every BR marked held/pending/not-merged/on-branch: its cited fix commit(s) must
     NOT be ancestors of main.  (FAIL => the BR is actually merged but the ledger says
     otherwise — the exact staleness that bit BR-147..161.)
  B. Every BR marked merged: every cited commit hash in the line MUST be an ancestor
     of main.  (FAIL => "merged" label cites a hash that never landed — the reverse
     direction.)

Scope: BR-numbered lines only (^BR-<digits>). BR-PENDING / BR-NB entries are out of
scope. A line with no parseable commit hash is reported as "no hash to check" (pass
with a note) — it cannot be verified, which is itself a flag for the record.

Usage:  python3 tools/check_ledger_merge_state.py [--repo <path>] [--branch main]
Exit:   0 all pass, 1 any fail.
"""

import argparse
import re
import subprocess
import sys
from pathlib import Path

HELD_MARKERS = re.compile(r"\b(held|HELD|pending merge|not merged|on branch|Pending\b)", re.IGNORECASE)
FIXED_MARKERS = re.compile(r"\bfixed\b", re.IGNORECASE)
MERGED_MARKERS = re.compile(r"\bmerged\b", re.IGNORECASE)
HASH_RE = re.compile(r"\b[0-9a-f]{7,40}\b")
BR_LINE_RE = re.compile(r"^BR-(\d+)\s*\|")


def git(repo, *args):
    r = subprocess.run(["git", "-C", str(repo), *args],
                       capture_output=True, text=True)
    return r.returncode, r.stdout.strip()


def is_commit(repo, cand):
    rc, _ = git(repo, "rev-parse", "--verify", "--quiet", f"{cand}^{{commit}}")
    return rc == 0


def is_ancestor_of_main(repo, cand, branch):
    rc, _ = git(repo, "merge-base", "--is-ancestor", cand, branch)
    return rc == 0


def extract_commits(repo, text):
    """Return the set of 7+ hex tokens in the line that resolve to real commits."""
    commits = set()
    for cand in HASH_RE.findall(text):
        if len(cand) < 7:
            continue
        if is_commit(repo, cand):
            commits.add(cand)
    return commits


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--repo", default=Path(__file__).resolve().parent.parent)
    ap.add_argument("--branch", default="main")
    ap.add_argument("--index", default="docs/BR_INDEX.md")
    args = ap.parse_args()

    repo = Path(args.repo)
    index_path = repo / args.index
    if not index_path.exists():
        print(f"[ERROR] index not found: {index_path}")
        sys.exit(2)

    ok = True
    checked = 0
    no_hash = 0
    for lineno, line in enumerate(index_path.read_text(encoding="utf-8", errors="replace").splitlines(), 1):
        m = BR_LINE_RE.match(line.strip())
        if not m:
            continue
        br = m.group(1)
        # status = last pipe-delimited field; body = everything after "BR-N |"
        parts = [p.strip() for p in line.strip().split("|")]
        status = parts[-1] if len(parts) > 1 else ""
        body = " | ".join(parts[1:])

        commits = extract_commits(repo, line)
        if not commits:
            no_hash += 1
            print(f"[NOTE] BR-{br}: no parseable commit hash in line — cannot verify (line {lineno})")
            continue

        checked += 1
        # Classify by the FIRST decisive marker: corrected lines legitimately
        # contain both "Merged" and a historical "previously said Held" note.
        pos_merged = MERGED_MARKERS.search(status)
        pos_held = HELD_MARKERS.search(status)
        pos_fixed = FIXED_MARKERS.search(status)
        if pos_held is not None and (pos_merged is None or pos_held.start() < pos_merged.start()):
            # Assertion A: held => fix commits must NOT be ancestors of main
            bad = [c for c in commits if is_ancestor_of_main(repo, c, args.branch)]
            if bad:
                ok = False
                print(f"[FAIL] BR-{br}: marked '{status.strip()}' but cited commit(s) "
                      f"{sorted(bad)} ARE ancestors of main — ledger lags reality (line {lineno})")
            else:
                print(f"[PASS] BR-{br}: held, cited commits {sorted(commits)} not in main")
        elif pos_fixed is not None and pos_merged is None:
            # Assertion A-variant: FIXED without a merged marker = fixed-but-unmerged =>
            # cited commits must NOT be ancestors of main yet (a FIXED line whose commit
            # already landed in main was flipped before the merge — the premature-flip
            # failure). Lines carrying BOTH "fixed" and "merged" (logged-then-merged
            # histories like BR-171/177) fall through to the merged branch below.
            bad = [c for c in commits if is_ancestor_of_main(repo, c, args.branch)]
            if bad:
                ok = False
                print(f"[FAIL] BR-{br}: marked '{status.strip()}' but cited commit(s) "
                      f"{sorted(bad)} ARE ancestors of main — flipped before the merge landed (line {lineno})")
            else:
                print(f"[PASS] BR-{br}: fixed, cited commits {sorted(commits)} not in main (pre-merge state)")
        else:
            # Assertion B: merged => every cited hash must be an ancestor of main
            bad = [c for c in commits if not is_ancestor_of_main(repo, c, args.branch)]
            if bad:
                ok = False
                print(f"[FAIL] BR-{br}: marked merged but cited commit(s) {sorted(bad)} "
                      f"are NOT ancestors of main — 'merged' label unverifiable (line {lineno})")
            else:
                print(f"[PASS] BR-{br}: merged, cited commits {sorted(commits)} all in main")

    print(f"\n=== RESULT: {'PASS' if ok else 'FAIL'} — {checked} BR lines hash-verified, "
          f"{no_hash} without parseable hashes ===")
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
