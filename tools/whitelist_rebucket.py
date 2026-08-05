#!/usr/bin/env python3
"""Section 3: re-bucket the EXTRACTED union (154) against the curated buckets.
Every extracted entry gets a bucket: the curated bucket if present, else a heuristic.
Output: bucket counts vs the curated 45/22/47/63/19, every bucket change, every
entry never in the curated list."""
import re, sys
sys.path.insert(0, "tools")
from whitelist_intent_classification import entries as CURATED

union = [l for l in open("/tmp/whitelist_real_union.txt").read().splitlines() if l.strip()]
cur = {}
for e, b in CURATED:
    cur.setdefault(e, b)

def heur(e):
    low = e.lower()
    if any(s in low for s in ("userlogin", "login", "getunitbycredentials", "ldap", "organizationmasterlist",
                              "/report", "generatetoken", "/qr", "pathology/report", "ris/radiology",
                              "resources/", "/views/", "swagger", "webjars", "applicationconfiguration")):
        return "PUBLIC"
    if any(s in low for s in ("mylife", "abdm", "razorpay", "callback", "sync", "flipbook", "checkparti")):
        return "INTEGRATION"
    if any(s in low for s in ("kiosk", "appointment", "feedback", "schedule", "selfregistration", "patientregistration",
                              "slots", "decrypt", "deleteparticipants")):
        return "KIOSK" if "deleteparticipants" not in low else "UNEXPLAINED"
    if any(s in low for s in ("save", "update", "cancel", "delete", "get", "search", "medication", "investigation",
                              "diagnosis", "coversheet", "complaint", "quicknotes", "vital", "favorite", "frequency",
                              "employee", "doctor", "template", "instruction", "habit", "familyhistory", "surgical",
                              "consultation", "report", "remind", "service", "roomrent", "refreshallcaches")):
        return "STALE"
    return "UNEXPLAINED"

rows = []
for e in union:
    b = cur.get(e) or heur(e)
    rows.append((e, b, "curated" if e in cur else "NEW"))

from collections import Counter
buckets = Counter(b for _, b, _ in rows)
print("=== extracted-union bucket counts (154) ===")
for b in ("PUBLIC", "INTEGRATION", "KIOSK", "STALE", "UNEXPLAINED"):
    print(f"  {b}: {buckets.get(b, 0)}")
print("curated counts were: PUBLIC 45, INTEGRATION 22, KIOSK 47, STALE 63, UNEXPLAINED 19 (total 196)\n")

print("=== entries whose bucket CHANGES vs the curated list ===")
changes = 0
for e, b, src in rows:
    if e in cur and cur[e] != b:
        changes += 1
        print(f"  {e}: curated={cur[e]} -> extracted-list bucket={b}")
if changes == 0:
    print("  (none — the overlap kept its curated bucket)")

print(f"\n=== entries NEVER in the curated list ({sum(1 for _, _, s in rows if s == 'NEW')}) ===")
for e, b, s in rows:
    if s == "NEW":
        print(f"  {e}  [{b}]")

print("\n=== curated entries ABSENT from the extracted filter (the curated list's dead weight) ===")
extracted = set(union)
for e in cur:
    if e not in extracted:
        print(f"  {e}  [curated {cur[e]}]")
