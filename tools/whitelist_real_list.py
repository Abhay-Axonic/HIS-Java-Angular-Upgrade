#!/usr/bin/env python3
"""Authoritative whitelist extraction from JwtTokenFilter source (both copies).

Handles the shapes that have bitten before: double-paren contains(("...")),
multiple contains(...) args on one line, and || chains. Rule-10 control FIRST:
/adt/userLogin, bare deleteParticipants, and /api/opd/getMedicationHistoryList
must all be found before any count is trusted."""
import re, sys

FILTER = "legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java"
src = open(FILTER, encoding="utf-8").read()
i76 = src.find('if (unitId != null && userId != null && authorizationHeader != null')
ielse = src.find('} else if (splitUrl.length <= 2')
iend = src.find('chain.doFilter(request, response);', ielse)
assert i76 != -1 and ielse != -1 and iend != -1
copy1 = src[i76:ielse]
copy2 = src[ielse:iend]

# extract every contains("...") argument; the `\(?` handles the double-paren form
def extract(region):
    return re.findall(r'requestUrl\.contains\(\(?"([^"]+)"', region)

raw1, raw2 = extract(copy1), extract(copy2)
u1 = list(dict.fromkeys(raw1))
u2 = list(dict.fromkeys(raw2))
union = list(dict.fromkeys(raw1 + raw2))
inter = [e for e in u1 if e in u2]
symdiff = [e for e in union if e in u1 and e not in u2] + [e for e in union if e in u2 and e not in u1]

print("=== RULE 10 CONTROL (before any count is trusted) ===")
ctrl = {
    "/adt/userLogin": "/adt/userLogin" in u1 and "/adt/userLogin" in u2,
    "deleteParticipants (bare)": "deleteParticipants" in u1 or "deleteParticipants" in u2,
    "/api/opd/getMedicationHistoryList": "/api/opd/getMedicationHistoryList" in u1 or "/api/opd/getMedicationHistoryList" in u2,
}
for k, v in ctrl.items():
    print(f"  {k}: {'PRESENT' if v else 'MISSING'}")
assert all(ctrl.values()), "CONTROL FAILED"
print("CONTROL PASS — extraction finds all three known positives\n")

print(f"copy1 (line-76): raw={len(raw1)} unique={len(u1)}")
print(f"copy2 (else-if): raw={len(raw2)} unique={len(u2)}")
print(f"union: {len(union)}  intersection: {len(inter)}  symmetric difference: {len(symdiff)}")
print(f"\n=== copy1-only (in line-76, not in else-if): {len([e for e in u1 if e not in u2])} ===")
for e in sorted(e for e in u1 if e not in u2):
    print(f"  {e}")
print(f"\n=== copy2-only (in else-if, not in line-76): {len([e for e in u2 if e not in u1])} ===")
for e in sorted(e for e in u2 if e not in u1):
    print(f"  {e}")

# write the authoritative list for downstream tools
with open("/tmp/whitelist_real_union.txt", "w") as f:
    f.write("\n".join(union))
with open("/tmp/whitelist_real_copy1.txt", "w") as f:
    f.write("\n".join(u1))
with open("/tmp/whitelist_real_copy2.txt", "w") as f:
    f.write("\n".join(u2))
print("\nlists written to /tmp/whitelist_real_{union,copy1,copy2}.txt")
