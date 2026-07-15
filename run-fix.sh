#!/bin/bash
set -e

TASK="$1"
if [ -z "$TASK" ]; then
  echo "Usage: ./run-fix.sh \"description of the fix\""
  exit 1
fi

cd ~/Projects/legacy-modernization

SLUG=$(echo "$TASK" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9' '-' | sed 's/^-//;s/-$//' | cut -c1-50)
BRANCH="fix/$SLUG"
REVIEW_FILE="docs/REVIEW_${SLUG}.md"

echo "=== Starting autonomous run ==="
echo "Task: $TASK"
echo "Branch: $BRANCH"

git checkout master
git checkout -b "$BRANCH"

echo ""
echo "=== Step 1/2: Maker (autonomous, may take a while -- do not interrupt) ==="
claude -p "Read CLAUDE.md and RULES.md first. Task: $TASK

You are acting as Maker on branch $BRANCH. Implement this fix, scoped tightly to what is described -- do not touch unrelated files. After making changes, compile the affected module (check pom.xml to find the right module, use mvn -pl <module> -am compile -DskipTests). If compilation fails for reasons unrelated to your change, note that clearly rather than claiming success.

Log your work to docs/SESSION_LOG.md. If this involves a Behavioral Change per RULES.md's definition, add an entry to docs/BUSINESS_RULE_MANIFEST.md with Owner Decision: Pending -- never mark it Approved yourself, that is reserved for the owner.

Create $REVIEW_FILE summarizing: what changed and why, before/after content of anything modified, confirmation that anything you reused already existed elsewhere in the codebase, and the compile result.

Commit your work yourself with git, immediately after making it -- do not wait until the end of your turn and do not rely on anything outside this session to commit for you. Write the commit message from the actual diff you produced (git diff --stat / git diff, read your own change back before describing it) -- never copy or paraphrase the task description above as the commit message, since that describes intent, not outcome. If you end up making no code changes at all (e.g. the fix already exists, or you determine the task cannot be completed as scoped), do not create a commit for it -- instead state explicitly, in your final output and in the docs/SESSION_LOG.md entry, that no changes were made and why. Never run git push. Never merge to master." \
  --permission-mode dontAsk \
  --allowedTools "Read,Edit,Write,Bash,Grep,Glob" \
  --max-turns 60

echo ""
echo "=== Step 2/2: Independent Checker (fresh context) ==="
claude -p "You are acting as an independent Checker, not a Maker, on branch $BRANCH. Read CLAUDE.md and RULES.md first. Then read $REVIEW_FILE in full -- this describes a change already made by a separate Maker process. Independently verify every claim in it against the actual current files. Do not simply agree with the review.

Flag any RULES.md violation, any unlogged Behavioral Change, any claim that does not match reality, or any concern a careful reviewer would raise. Give a clear verdict: approve, approve with concerns, or reject -- and why. Append your full verdict to the end of $REVIEW_FILE and log a summary line to docs/SESSION_LOG.md.

Commit your own doc edits yourself with git, immediately after making them -- do not rely on anything outside this session to commit for you. Write the commit message from the actual diff you produced (git diff --stat / git diff) and from your actual verdict (approve / approve with concerns / reject) -- never from the original task description, and never claim a code fix was made or verified beyond what you actually found. Never run git push. Never merge to master." \
  --permission-mode dontAsk \
  --allowedTools "Read,Grep,Glob,Edit,Write,Bash" \
  --max-turns 40

echo ""
echo "===================================================="
echo "DONE. Branch: $BRANCH"
echo "Nothing was pushed to GitHub. Nothing was merged to master."
echo ""
echo "This script no longer commits anything itself -- the Maker and Checker"
echo "steps above are responsible for committing their own work (or explicitly"
echo "stating that no changes were made). Run 'git log --oneline master..$BRANCH'"
echo "and 'git status' now to see what was actually committed, if anything."
echo ""
echo "Next: open and read $REVIEW_FILE -- the Checker's verdict is at the bottom."
echo "Tell me what it says, and whether you approve."
echo "===================================================="
