#!/bin/bash
set -e
MODULE="$1"
if [ -z "$MODULE" ]; then
  echo "Usage: ./audit-module.sh <ModuleName>   (e.g. Billing)"
  exit 1
fi
cd ~/Projects/legacy-modernization
OUTFILE="docs/AUDIT_${MODULE}_exceptions.md"

claude -p "Read CLAUDE.md and RULES.md. Scan every .java file in legacy/$MODULE for the silent-exception-swallow pattern (empty catch blocks, catches returning null with no status code, catches that neither log nor rethrow). For each finding: file path, line number, exception type, severity (silent-swallow / status-masks-failure / other), and whether it's on a billing-critical path (payment, invoice, calculation) or peripheral. This is READ-ONLY -- do not modify any file in legacy/. Save the full table to $OUTFILE." \
  --permission-mode bypassPermissions \
  --max-turns 60

git add "$OUTFILE" 2>/dev/null || true
echo "Audit complete: $OUTFILE"
