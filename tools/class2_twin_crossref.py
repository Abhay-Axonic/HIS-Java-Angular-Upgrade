#!/usr/bin/env python3
"""Cross-reference the Class-2 census (org-commented / unit-commented named queries)
against the 55 twins + 18 live-duplicates. Per finding: does the name resolve to a
live twin? NO re-classification — Rule 10 traces are a separate (unauthorised) step."""
import re, glob

def block_text(src, start):
    depth = 0
    for t in range(start, len(src)):
        if src[t] == "(":
            depth += 1
        elif src[t] == ")":
            depth -= 1
            if depth == 0:
                return src[start:t + 1]
    return ""

def scan():
    org_commented = []   # (name, file) — a live @NamedQuery whose block has a commented org predicate
    unit_commented = []  # (name, file) — a live @NamedQuery whose block has a commented unit predicate
    twin = set()         # name in both a commented block and a live declaration (any file)
    live_dup = {}        # name -> [files] declared live in 2+ files
    live_files = {}
    for f in glob.glob("legacy/**/*.java", recursive=True):
        if "/target/" in f or "Test.java" in f:
            continue
        fn = f.split("/")[-1]
        src = open(f, encoding="utf-8", errors="replace").read()
        for m in re.finditer(r'@NamedQuery\(name\s*=\s*"([A-Z][A-Z0-9_]+)"', src):
            nm = m.group(1)
            block = block_text(src, m.start())
            lines = block.split("\n")
            # the live query text lines (the query= part) — commented-out predicate lines
            for ln in lines:
                if "organizationId" in ln and (ln.lstrip().startswith("//") or "/**" in block and "*/" in block):
                    org_commented.append((nm, fn))
                    break
            for ln in lines:
                if re.search(r'//[^\n]*unitId[^\n]*|/\*[^*]*unitId[^*]*\*/', ln):
                    unit_commented.append((nm, fn))
                    break
        # twin detection: names in commented + live
        names = list(re.finditer(r'name\s*=\s*"([A-Z][A-Z0-9_]+)"', src))
        commented = set()
        live = set()
        for mm in names:
            ls = src.rfind("\n", 0, mm.start()) + 1
            line = src[ls:src.find("\n", mm.start())]
            before = src[max(0, mm.start() - 150):mm.start()]
            in_block = before.rfind("/*") > before.rfind("*/")
            if in_block or line.lstrip().startswith("//"):
                commented.add(mm.group(1))
            else:
                live.add(mm.group(1))
                live_files.setdefault(mm.group(1), set()).add(fn)
        twin.update(commented.intersection(live))
    for nm, files in live_files.items():
        if len(files) >= 2:
            live_dup[nm] = sorted(files)
    return org_commented, unit_commented, twin, live_dup

org_c, unit_c, twin, live_dup = scan()

def classify(findings, label):
    twins_hit = []
    dup_hit = []
    stand = []
    for nm, fn in findings:
        if nm in twin:
            twins_hit.append(nm)
        elif nm in live_dup:
            dup_hit.append(nm)
        else:
            stand.append(nm)
    print(f"\n{label}: {len(findings)} findings")
    print(f"  resolves to a LIVE TWIN (classification invalidated — needs Rule-10 trace of the twin): {len(set(twins_hit))}")
    print("   " + ", ".join(sorted(set(twins_hit))))
    print(f"  in a LIVE-DUPLICATE pair (ambiguous resolution — load-order dependent — needs trace): {len(set(dup_hit))}")
    print("   " + ", ".join(sorted(set(dup_hit))))
    print(f"  STANDING after the twin filter (no twin, no dup — hypothesis unchanged): {len(set(stand))}")
    print("   " + ", ".join(sorted(set(stand))))

classify(org_c, "CLASS-2 ORG-COMMENTED")
classify(unit_c, "CLASS-2 UNIT-COMMENTED")
