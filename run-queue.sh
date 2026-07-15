#!/bin/bash
set -e
cd ~/Projects/legacy-modernization

QUEUE="docs/BILLING_FIX_QUEUE_remaining3.txt"
DIGEST="docs/DAILY_DIGEST.md"
ROUTINE_LOG="docs/BATCH_ROUTINE_FIXES.md"
DECISION_LOG="docs/BATCH_NEEDS_DECISION.md"

echo "# Routine Fixes -- Ready to Bulk-Approve ($(date))" > "$ROUTINE_LOG"
echo "" >> "$ROUTINE_LOG"
echo "| BR# | File | What it does | Today -> After |" >> "$ROUTINE_LOG"
echo "|---|---|---|---|" >> "$ROUTINE_LOG"

echo "# Findings That Need Your Judgment ($(date))" > "$DECISION_LOG"
echo "" >> "$DECISION_LOG"

while IFS='|' read -r FILEPATH COUNT DESC; do
  [ -z "$FILEPATH" ] && continue
  SLUG=$(echo "$FILEPATH" | tr '/.' '--' | tr -cs 'a-zA-Z0-9-' '-' | cut -c1-60)
  HASH=$(echo -n "$FILEPATH" | md5sum | cut -c1-8)
  BRANCH="fix/queue-$SLUG-$HASH"

  echo ""
  echo "=================================================="
  echo "Processing: $FILEPATH ($COUNT findings)"
  echo "=================================================="

  git checkout master >/dev/null 2>&1
  git checkout -b "$BRANCH" >/dev/null 2>&1 || { echo "Branch exists, skipping"; continue; }

  set +e
  claude -p "Read CLAUDE.md and RULES.md. Do NOT ask for confirmation -- implement directly and immediately, no one will respond in this unattended run.

Fix every silent-exception-swallow finding for $FILEPATH ($COUNT findings, listed in docs/AUDIT_Billing_exceptions.md). Match the approved BR-006 pattern: log via logger.error, then throw ServiceException/BillingException so it routes to the centralized handler. Add a logger if none exists, matching this package's convention.

IMPORTANT -- classify each finding as you go:
- ROUTINE: the fix only changes a masked/silent error into a visible one. Nothing about a currently-successful outcome changes.
- NEEDS_OWNER_DECISION: the fix would change what counts as success -- e.g. a method that today reports success despite a downstream failure, where the standard fix would make that same case report failure instead. Do NOT apply the standard fix to these -- leave them untouched and describe them plainly instead.

For ROUTINE findings: apply the fix, add a Business Rule Manifest entry (next BR number), Owner Decision: Pending, and append one row to docs/BATCH_ROUTINE_FIXES.md in this exact format: '| BR-XXX | $FILEPATH | <one sentence what this file does> | <one sentence: today vs after the fix> |'

For NEEDS_OWNER_DECISION findings: do not change them. Append a plain-English paragraph to docs/BATCH_NEEDS_DECISION.md explaining what happens today, what the standard fix would change, and why it needs a real decision -- same style as docs/DECISION_NEEDED_collectPayment.md.

Compile the affected module afterward, confirm BUILD SUCCESS. Commit your work with git add -A && git commit (run the commit yourself, do not just stage and wait)." \
    --permission-mode bypassPermissions \
    --max-turns 90
  MAKER_STATUS=$?
  set -e

  echo "=== Checker pass ==="
  set +e
  claude -p "You are acting as an independent Checker. Read CLAUDE.md and RULES.md. Verify every ROUTINE fix just made to $FILEPATH: matches the approved BR-006 pattern, actually compiles (re-run yourself), doesn't silently swallow anything, and genuinely doesn't touch any NEEDS_OWNER_DECISION case. Append your verdict to docs/SESSION_LOG.md. If you find a ROUTINE-classified fix that actually changes a success outcome, move it to docs/BATCH_NEEDS_DECISION.md instead and flag this clearly." \
    --permission-mode bypassPermissions \
    --max-turns 50
  set -e

  git add -A
  git commit -m "Queue: $FILEPATH ($COUNT findings)" >/dev/null 2>&1 || true

  if [ $MAKER_STATUS -ne 0 ]; then
    echo "| (FAILED) | $FILEPATH | $DESC | needs manual look -- branch: $BRANCH |" >> "$ROUTINE_LOG"
  fi

  git checkout master >/dev/null 2>&1
done < "$QUEUE"

echo ""
echo "===================================================="
echo "QUEUE COMPLETE."
echo "Routine fixes ready to bulk-approve: $ROUTINE_LOG"
echo "Findings needing your judgment: $DECISION_LOG"
echo "Nothing merged. Nothing pushed."
echo "===================================================="
