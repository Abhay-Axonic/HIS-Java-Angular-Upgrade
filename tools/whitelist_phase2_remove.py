#!/usr/bin/env python3
"""PHASE 2 removal: delete the 45 confirmed-dead whitelist entries from BOTH copies
of the JwtTokenFilter list (the line-76 condition + the else-if). Exact quoted-argument
matching — a fragment must match requestUrl.contains("<fragment>") exactly, so
/HIS-WAR/deleteParticipants cannot delete its /* and /** siblings."""
import re, sys, os
from _whitelist_dead45 import DEAD45

FILTER = "legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java"

src = open(FILTER, encoding="utf-8").read()
i76 = src.find('if (unitId != null && userId != null && authorizationHeader != null')
ielse = src.find('} else if (splitUrl.length <= 2')
iend = src.find('chain.doFilter(request, response);', ielse)
assert i76 != -1 and ielse != -1 and iend != -1
copy1 = src[i76:ielse]
copy2 = src[ielse:iend]

def count_contains(region):
    return len(re.findall(r'requestUrl\.contains\(', region))

c1_before, c2_before = count_contains(copy1), count_contains(copy2)
print(f"BEFORE: copy1={c1_before}  copy2={c2_before}")

# per-fragment line hits — whole lines carrying an in-45 argument (deduped), then rebuilt
# at argument level so NON-45 args sharing the line (e.g. /br/ and /resultUrl) survive.
line_spans = {}  # (start,end) whole-line span -> (region, line)
for region, tag in ((copy1, 1), (copy2, 2)):
    for frag in DEAD45:
        pat = re.compile(r'[^\n]*requestUrl\.contains\("' + re.escape(frag) + r'"\)[^\n]*\n')
        for m in pat.finditer(region):
            line_spans.setdefault((m.start(), m.end(), tag), (region, m.group(0)))

missing = [f for f in DEAD45
           if not re.search(r'requestUrl\.contains\("' + re.escape(f) + r'"\)', copy1)
           and not re.search(r'requestUrl\.contains\("' + re.escape(f) + r'"\)', copy2)]
if missing:
    print("FRAGMENTS NOT FOUND IN EITHER COPY (stop, do not remove):")
    for m in missing:
        print("  ", m)
    sys.exit(1)

def rebuild_line(line, frags):
    """remove the in-45 requestUrl.contains(...) segments from the line, keep everything else"""
    parts = re.split(r'(\|\|)', line)
    out = []
    for p in parts:
        if re.search(r'requestUrl\.contains\("(' + "|".join(re.escape(f) for f in frags) + r')"\)', p):
            continue
        out.append(p)
    rebuilt = "".join(out)
    rebuilt = re.sub(r'\|\|\s*\|\|', "||", rebuilt)
    rebuilt = re.sub(r'\(\s*\|\|', "(", rebuilt)
    rebuilt = re.sub(r'\|\|\s*\)', ")", rebuilt)
    return rebuilt

def apply_region(region, tag):
    spans_here = sorted([(s, e) for (s, e, t), _ in line_spans.items() if t == tag], reverse=True)
    for s, e in spans_here:
        line = region[s:e]
        rebuilt = rebuild_line(line, DEAD45)
        if rebuilt.strip().strip("|").strip() == "" or "requestUrl.contains(" not in rebuilt:
            region = region[:s] + region[e:]
        else:
            region = region[:s] + rebuilt + region[e:]
    return region

copy1_new = apply_region(copy1, 1)
copy2_new = apply_region(copy2, 2)

src_new = src[:i76] + copy1_new + copy2_new + src[iend:]

# verify: per-fragment file-wide count == 0, per-copy counts match expectations
missing_after = [f for f in DEAD45 if re.search(r'requestUrl\.contains\("' + re.escape(f) + r'"\)', src_new)]
c1_after = count_contains(src_new[i76:src_new.find('} else if (splitUrl.length <= 2')])
ielse2 = src_new.find('} else if (splitUrl.length <= 2')
iend2 = src_new.find('chain.doFilter(request, response);', ielse2)
c2_after = count_contains(src_new[ielse2:iend2])
# expected: the before minus the in-45 argument occurrences per copy
in45_c1 = sum(len(re.findall(r'requestUrl\.contains\("' + re.escape(f) + r'"\)', copy1)) for f in DEAD45)
in45_c2 = sum(len(re.findall(r'requestUrl\.contains\("' + re.escape(f) + r'"\)', copy2)) for f in DEAD45)
print(f"in-45 argument occurrences: copy1={in45_c1}  copy2={in45_c2}")
print(f"AFTER:  copy1={c1_after}  copy2={c2_after}  (expected {c1_before - in45_c1} / {c2_before - in45_c2})")
print(f"fragments still present: {len(missing_after)}")
assert not missing_after, missing_after
assert c1_after == c1_before - in45_c1 and c2_after == c2_before - in45_c2

open(FILTER, "w", encoding="utf-8").write(src_new)
print(f"written {FILTER} — {len(DEAD45)} entries removed from both copies")
