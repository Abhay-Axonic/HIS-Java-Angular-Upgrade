#!/usr/bin/env python3
"""CATCH-RETURNS-SUCCESS detector — the strongest subclass of the
false-success write class (owner directive 2026-08-10, enumerate only).

collectPayment (BillingMasterServiceImpl:2385-2389) is the confirmed anchor:
a catch (Exception e) block whose FIRST statement is
`return new Response<>(SUCCESS, ...)` — "Payment collected successfully !!!"
on the EXCEPTION path. That is a swallowed write reporting success.

Second shape: catch (Exception e) { logger.error(...); return SUCCESS }.
Third shape (operation-theatre, BR-187): SUCCESS return at method level
OUTSIDE the result guard, after a DAO call (already enumerated).

This script scans ALL service impls for catch blocks that return SUCCESS,
enumerates and classifies only — no fixes.
"""
import os
import re
import sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"

HITS = []  # (module, file, method, line, code)

for dirpath, dirnames, filenames in os.walk(os.path.join(ROOT, "legacy")):
    if "src" not in dirpath or "main" not in dirpath or "java" not in dirpath:
        continue
    for f in filenames:
        if not (f.endswith("ServiceImpl.java") or f.endswith("Service.java")):
            continue
        path = os.path.join(dirpath, f)
        try:
            src = open(path, encoding="utf-8", errors="replace").read()
        except OSError:
            continue
        rel = os.path.relpath(path, ROOT)
        module = rel.split(os.sep)[1] if len(rel.split(os.sep)) > 1 else "?"
        lines = src.split("\n")
        # track method context by finding method signatures
        method_at = [""] * len(lines)
        cur = "?"
        for i, ln in enumerate(lines):
            m = re.search(r"public\s+(?:static\s+)?Response(?:<[^>]+>)?\s+(\w+)\s*\(", ln)
            if m:
                cur = m.group(1)
            method_at[i] = cur
        # find catch blocks
        for i, ln in enumerate(lines):
            if re.search(r"catch\s*\(", ln):
                # scan forward up to 8 lines for a return SUCCESS
                for j in range(i + 1, min(i + 9, len(lines))):
                    s = lines[j]
                    if re.search(r"return\s+new\s+Response<>(?:<[^>]+>)?\(\s*SUCCESS", s):
                        # false-positive filters:
                        #  - the SUCCESS line is commented out
                        stripped = s.strip()
                        if stripped.startswith("//") or "//return" in stripped or "*/" in stripped:
                            break
                        #  - a throw statement appears between the catch and the SUCCESS
                        between = " ".join(lines[i + 1:j])
                        if re.search(r"\bthrow\b", between):
                            break
                        snippet = " ".join(x.strip() for x in lines[i:j + 1])[:110]
                        HITS.append((module, rel, method_at[i], i + 1, snippet))
                        break
                    if re.search(r"return\s+new\s+Response<>(?:<[^>]+>)?\(\s*ERROR|return\s+new\s+Response<>", s) and "SUCCESS" not in s:
                        break  # the catch returns something else first — not this shape

print(f"catch-returns-SUCCESS hits: {len(HITS)}\n")
order = {"Billing": 0, "ADT": 1, "PatientRegistration": 2, "OPD": 3,
         "Nursing": 4, "Pharmacy": 5, "Inventory": 6, "Radiology": 7,
         "Laboratory": 8, "Pathology": 9, "Global": 10, "OTIMS": 11,
         "DietAndKitchen": 12, "AssetManagement": 13}
HITS.sort(key=lambda h: (order.get(h[0], 99), h[1]))

from collections import Counter
mods = Counter(h[0] for h in HITS)
for m, n in mods.most_common():
    print(f"  {m}: {n}")
print()
for module, rel, method, line, snippet in HITS[:40]:
    print(f"[{module}] {method} @ {rel}:{line}")
    print(f"    {snippet}")
print(f"\n...and {max(0, len(HITS)-40)} more")

import json
json.dump([(m, r, me, l, s) for m, r, me, l, s in HITS],
          open("/tmp/catch_success_enum.json", "w"), indent=1)
print("\nwrote /tmp/catch_success_enum.json")
