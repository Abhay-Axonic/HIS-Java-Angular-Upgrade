#!/usr/bin/env python3
"""FEATURE-DIR COLLISION PRECHECK (2026-08-13, owner: a bulk generator
overwrote the 339-line doctor-dashboard screen — the second bulk-edit damage
after the kitCreation route. The route-survival check catches a vanished
route; NOTHING caught an overwritten component. This is that missing check.)

Usage (BEFORE any bulk generation into new/src/app/features/):
    python3 tools/check_feature_dir_collisions.py <target-dir-name>...

FAILS (exit 1) if ANY target feature directory already exists — a bulk
generator must never write into an existing feature dir. PASSES (exit 0)
only when every target is fresh.

Negative control (proven both directions 2026-08-13):
    python3 tools/check_feature_dir_collisions.py doctor-dashboard  # FAIL — exists
    python3 tools/check_feature_dir_collisions.py fresh-dashboard-x # PASS — fresh
"""
import os, sys

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
FEATURES = os.path.join(REPO, "new/src/app/features")

def main() -> int:
    targets = [a for a in sys.argv[1:] if not a.startswith("-")]
    if not targets:
        print("usage: check_feature_dir_collisions.py <target-dir-name>...")
        return 2
    collisions = [t for t in targets if os.path.isdir(os.path.join(FEATURES, t))]
    if collisions:
        print(f"FAIL: target feature dir(s) ALREADY EXIST — bulk generation would overwrite them: {', '.join(collisions)}")
        print("Refusing to proceed. Choose fresh names, or explicitly plan to merge into the existing screen.")
        return 1
    print(f"PASS: all {len(targets)} target feature dir(s) are fresh — bulk generation is safe.")
    return 0

if __name__ == "__main__":
    sys.exit(main())
