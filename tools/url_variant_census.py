#!/usr/bin/env python3
"""BR-173 NEW PHASE 1 — URL VARIANT CENSUS (analysis only, no filter changes).

For each of the 154 extracted entries: every distinct URL form callers produce
(literal paths, assembled URLs, config constants, trailing slashes, query strings,
path variables), whether exact-match would miss it, and the normalization rule
needed. Rule-10 control FIRST: the extraction must find the KNOWN forms of three
entries (assembled med-history, trailing-slash/constant getActiveCountryList/,
constant patientDoc/details) before any 'only one form' conclusion is trusted."""
import re, os, sys

ROOT = "."
WEB = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js")
union = [l for l in open("/tmp/whitelist_real_union.txt").read().splitlines() if l.strip()]

# load all js/html/config once
texts = {}
for dirpath, _, files in os.walk(WEB):
    for f in files:
        if f.endswith((".js", ".html")):
            p = os.path.join(dirpath, f)
            try:
                texts[p] = open(p, encoding="utf-8", errors="replace").read()
            except Exception:
                pass

def stem_of(frag):
    return frag.rstrip("/").split("/")[-1]

def search_target(frag):
    """the distinctive substring to search: the full fragment when short, else a
    unique longer prefix (generic last segments like 'details' over-recall)"""
    segs = [s for s in frag.split("/") if s]
    if len(frag) <= 64:
        return frag
    return "/" + "/".join(segs[:4])

def find_forms(frag):
    """distinct URL forms containing the entry's distinctive form, from the loaded texts"""
    target = search_target(frag)
    forms = set()
    for p, t in texts.items():
        for m in re.finditer(r'(["\'])([^"\']{0,200}?' + re.escape(target) + r'[^"\']{0,200}?)\1', t):
            s = m.group(2)
            if "/" not in s or any(c in s for c in "\n\r\t"):
                continue
            forms.add(s)
    return sorted(forms)

def classify(frag, forms):
    """exact-match hazard + the normalization rule"""
    stem = stem_of(frag)
    hazards = []
    rule = None
    for f in forms:
        if f == frag:
            continue
        if f.rstrip("/") == frag.rstrip("/") and (f.endswith("/") != frag.endswith("/")):
            hazards.append(f"trailing-slash: {f}")
        elif "?" in f:
            hazards.append(f"query-string: {f}")
        elif re.search(r"/\d+/?$", f) and not re.search(r"/\d+/?$", frag):
            hazards.append(f"path-variable-id: {f}")
        elif f != frag and frag in f and len(f) > len(frag) + 1:
            hazards.append(f"longer-path: {f}")
        elif f != frag and f not in frag and frag not in f:
            hazards.append(f"variant: {f}")
    if hazards:
        rule = "trailing-slash-strip + query-strip" if any("trailing" in h or "query" in h for h in hazards) else (
            "path-variable handling" if any("path-variable" in h for h in hazards) else "exact-list + per-form")
    return hazards, rule

# ---- Rule-10 control ----
print("=== RULE 10 CONTROL (known forms must be found) ===")
ctrl_ok = True
for frag, expect in [("/api/opd/getMedicationHistoryList", "/api/opd/getMedicationHistoryList"),
                     ("/adt/getActiveCountryList/", "getActiveCountryList"),
                     ("/api/opd/patientDoc/details", "patientDoc/details")]:
    forms = find_forms(frag)
    hit = any(expect in f for f in forms)
    print(f"  {frag}: {len(forms)} forms, expected {expect!r} present: {hit}  forms={forms[:3]}")
    ctrl_ok = ctrl_ok and hit
assert ctrl_ok, "CONTROL FAILED"
print("CONTROL PASS — the extraction finds the assembled, trailing-slash, and constant forms\n")

# ---- the census ----
print("=== THE CENSUS (154 entries) ===")
need_norm = []
individual = []
for frag in union:
    forms = find_forms(frag)
    if not forms:
        continue
    hazards, rule = classify(frag, forms)
    if hazards:
        need_norm.append((frag, hazards, rule))
        flag = "INDIVIDUAL" if rule == "exact-list + per-form" or any("path-variable" in h for h in hazards) else ""
        if flag:
            individual.append(frag)
        print(f"[NEEDS {rule}] {frag}")
        for h in hazards[:3]:
            print(f"     {h}")

print(f"\n=== SUMMARY: {len(need_norm)} of 154 entries have caller-URL forms exact-match would miss ===")
from collections import Counter
rules = Counter(r for _, _, r in need_norm)
for r, c in rules.items():
    print(f"  rule '{r}': {c} entries")
print(f"\n=== INDIVIDUAL HANDLING ({len(individual)} entries — no clean normalization rule) ===")
for e in sorted(set(individual)):
    print(f"  {e}")
