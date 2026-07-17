#!/usr/bin/env python3
"""
One-command entry point for the whole audit-extraction pipeline:

    python3 tools/angular-audit/run_audit.py

Runs extract_states -> build_controller_index -> classify_risk ->
generate_inventory in order and regenerates
docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md from a fresh read of the actual
source tree. Safe to re-run any time the AngularJS routing config,
controllers, or views change -- it's read-only against the app source and
only ever writes to tools/angular-audit/data/ and the one inventory doc.
"""
import subprocess
import sys
from pathlib import Path

STEPS = [
    "extract_states.py",
    "build_controller_index.py",
    "classify_risk.py",
    "generate_inventory.py",
]


def main():
    tools_dir = Path(__file__).resolve().parent
    for step in STEPS:
        print(f"=== {step} ===", file=sys.stderr)
        result = subprocess.run([sys.executable, str(tools_dir / step)], cwd=str(tools_dir))
        if result.returncode != 0:
            print(f"FAILED at {step} (exit {result.returncode})", file=sys.stderr)
            sys.exit(result.returncode)
    print("Audit complete.", file=sys.stderr)


if __name__ == "__main__":
    main()
