#!/usr/bin/env python3
"""Gap reconciliation: 217 (line-76 copy) vs 220 (else-if copy) raw contains( occurrences
vs the classification's 196 dedup'd entries. Answers: (a) what the three numbers mean,
(b) every entry in either copy but absent from the 196, (c) every entry in one copy but
not the other (the copies' drift). Verified against known positives FIRST: the bare
'deleteParticipants' must appear in the gap (the known miss), and
'getMedicationHistoryList' must be in the 196 AND both copies (the known present)."""
import re, os
from whitelist_intent_classification import entries as CLASS196

FILTER = "legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java"
src = open(FILTER, encoding="utf-8").read()
i76 = src.find('if (unitId != null && userId != null && authorizationHeader != null')
ielse = src.find('} else if (splitUrl.length <= 2')
iend = src.find('chain.doFilter(request, response);', ielse)
copy1 = src[i76:ielse]
copy2 = src[ielse:iend]

raw1 = re.findall(r'requestUrl\.contains\(', copy1)
raw2 = re.findall(r'requestUrl\.contains\(', copy2)
args1 = re.findall(r'requestUrl\.contains\(\(?"([^"]+)"', copy1)
args2 = re.findall(r'requestUrl\.contains\(\(?"([^"]+)"', copy2)
u1 = list(dict.fromkeys(args1))
u2 = list(dict.fromkeys(args2))
union = list(dict.fromkeys(args1 + args2))
inter = [e for e in u1 if e in u2]
only1 = [e for e in u1 if e not in u2]
only2 = [e for e in u2 if e not in u1]
class196 = [e for e, b in CLASS196]

print("=== the three numbers ===")
print(f"PRE-REMOVAL (Phase-2 baseline): copy1 raw = 217, copy2 raw = 220 (217 vs 220 = the copies had drifted)")
print(f"copy1 (line-76 condition): raw contains( occurrences = {len(raw1)}, unique entries = {len(u1)}")
print(f"copy2 (else-if):           raw contains( occurrences = {len(raw2)}, unique entries = {len(u2)}")
print(f"union (either copy):       unique entries = {len(union)}")
print(f"classification:            entries = {len(class196)}")
print(f"duplicates WITHIN copy1: {len(raw1) - len(u1)}  within copy2: {len(raw2) - len(u2)}")

print("\n=== Rule-10 control (known positives BEFORE any negative is trusted) ===")
gap = [e for e in union if e not in class196]
ok1 = "deleteParticipants" in gap
ok2 = "/adt/userLogin" in class196 and "/adt/userLogin" in u1 and "/adt/userLogin" in u2
print(f"known miss 'deleteParticipants' in the gap: {ok1}")
print(f"known present '/adt/userLogin' in 196 + both copies: {ok2}")
assert ok1 and ok2, "CONTROL FAILED"
print("CONTROL PASS — the gap/drift extraction matches the known facts\n")

print(f"=== entries in EITHER copy but ABSENT from the 196 ({len(gap)}) ===")
for e in sorted(gap):
    print(f"  {e}")

print(f"\n=== drift: entries in ONE copy but not the other ===")
print(f"copy1-only ({len(only1)}):")
for e in sorted(only1):
    print(f"  {e}")
print(f"copy2-only ({len(only2)}):")
for e in sorted(only2):
    print(f"  {e}")

print(f"\n=== shared ({len(inter)}) ===")
print(f"  {len(inter)} entries present in BOTH copies identically")
