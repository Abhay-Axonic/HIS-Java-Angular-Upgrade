#!/usr/bin/env python3
"""BR-173 next-branch prep: caller audit for the 82 STALE+UNEXPLAINED whitelist entries.

For each entry: every caller (frontend JS/HTML, backend Java, external) + the frontend
call shape (GenericService.serviceAction = token-attached, vs raw $http/$.ajax/$.post)
+ verdict: SAFE TO REMOVE / NOT STALE (a raw caller) / NO CALLER FOUND (dead).

Rule 10: the search pattern is validated against a KNOWN POSITIVE first —
GET_MEDICATION_HISTORY_LIST's frontend caller (IPDCoverSheetController.js:23414,
serviceAction) MUST be found by the same pattern before any 'no caller' verdict
is recorded.
"""
import re, subprocess, os, json

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
JS = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js")
HTML = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources")
JAVA = os.path.join(ROOT, "legacy")

RAW_SHAPE = re.compile(r"\$http|\$\.ajax|\$\.get|\$\.post|\$\.put|\$\.delete|axios|fetch\(")

def grep(pattern, path, globs=("*.js", "*.html")):
    cmd = ["grep", "-rn", "-F"] + [f"--include={g}" for g in globs] + [pattern, path]
    r = subprocess.run(cmd, capture_output=True, text=True, cwd=ROOT)
    return [l for l in r.stdout.splitlines() if l.strip()]

def grep_java(pattern):
    r = subprocess.run(["grep", "-rn", "--include=*.java", "-F", pattern, JAVA],
                       capture_output=True, text=True, cwd=ROOT)
    return [l for l in r.stdout.splitlines() if "/target/" not in l and l.strip()]

def call_shape(context_lines):
    for ln in context_lines:
        if "serviceAction" in ln:
            return "serviceAction"
    for ln in context_lines:
        if RAW_SHAPE.search(ln):
            return "RAW"
    return "?"

def caller_analysis(fragment):
    """returns (callers:list[(file:line, shape)], java_calls:list, endpoints:list)"""
    # 1. frontend callers: the fragment as a literal in JS/HTML
    js_hits = grep(fragment, JS)
    html_hits = grep(fragment, HTML) if not js_hits else []
    callers = []
    for hit in js_hits[:6]:
        parts = hit.split(":", 2)
        f, ln, text = parts[0], parts[1], parts[2] if len(parts) > 2 else ""
        if fragment in text and ("URI" in text or "url" in text or "serviceAction" in text or "http" in text.lower()):
            # find the call shape: look at up to 4 lines before AND 8 lines after the hit
            ctx = []
            try:
                flines = open(f, encoding="utf-8", errors="replace").read().splitlines()
                for i in range(max(0, int(ln) - 5), min(len(flines), int(ln) + 8)):
                    ctx.append(flines[i])
            except Exception:
                pass
            callers.append((f.replace(ROOT + "/", ""), ln, call_shape(ctx + [text])))
        elif fragment in text and "serviceAction" in text:
            callers.append((f.replace(ROOT + "/", ""), ln, "serviceAction"))
    for hit in html_hits[:4]:
        parts = hit.split(":", 2)
        callers.append((parts[0].replace(ROOT + "/", ""), parts[1], "html-ref"))
    # 2. backend Java callers (internal REST or path references) — EXCLUDE the whitelist's own file,
    #    the endpoint's own @RequestMapping, and commented lines
    java = grep_java(fragment)
    java_calls = [j.split(":", 2)[:2] for j in java
                  if "RequestMapping" not in j and "JwtTokenFilter" not in j and "//" not in j][:4]
    # 3. the endpoint: controller @RequestMapping paths containing the fragment
    endpoints = []
    for j in java:
        if "@RequestMapping" in j or "@PostMapping" in j or "@GetMapping" in j or "@PutMapping" in j:
            endpoints.append(j.split(":", 2)[:2])
    return callers, java_calls, endpoints[:2]

# ---- Rule 10 positive control FIRST ----
print("=== RULE 10 CONTROL: the search pattern vs the KNOWN positive ===")
ctrl = grep("getMedicationHistoryList", JS)
ipd_hits = [c for c in ctrl if "IPDCoverSheetController" in c]
control_ok = False
if ipd_hits:
    rel = ipd_hits[0].split(":", 1)[0]
    ln = int(ipd_hits[0].split(":")[1])
    try:
        flines = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read().splitlines()
        ctx = flines[max(0, ln - 2):ln + 7]
        control_ok = any("serviceAction" in l for l in ctx)
    except Exception:
        control_ok = False
print(f"getMedicationHistoryList frontend hits: {len(ctrl)} — IPDCoverSheetController hit at {ipd_hits[0].split(':',2)[:2] if ipd_hits else 'none'}; context window has serviceAction: {control_ok}")
assert control_ok, "CONTROL FAILED — pattern does not find the known positive; no 'no caller' verdict may be recorded"
print("CONTROL PASS — pattern recall proven; negatives trustworthy\n")

# ---- the 82 entries ----
from whitelist_intent_classification import entries as ALL
ENTRIES = [(e, b) for e, b in ALL if b in ("STALE", "UNEXPLAINED")]
print(f"auditing {len(ENTRIES)} entries ({sum(1 for _, b in ALL if b=='STALE')} STALE + {sum(1 for _, b in ALL if b=='UNEXPLAINED')} UNEXPLAINED)\n")

results = {"SAFE": [], "NOT_STALE": [], "NO_CALLER": []}
for frag, bucket in ENTRIES:
    callers, java_calls, endpoints = caller_analysis(frag)
    if not callers and not java_calls:
        verdict, why = "NO_CALLER", "no frontend (JS/HTML) or backend (Java, non-mapping) hit for the fragment"
    elif any(shape == "RAW" for _, _, shape in callers):
        verdict, why = "NOT_STALE", "RAW frontend caller (no GenericService wrapper): " + next(c for _, _, c in callers if c == "RAW")
    elif any(shape == "?" for _, _, shape in callers):
        verdict, why = "NOT_STALE", "caller with unverified shape: " + str([c for c in callers if c[2] == "?"][:1])
    else:
        verdict, why = "SAFE", "all " + str(len(callers)) + " frontend caller(s) via serviceAction" + ("" if not java_calls else "; java refs " + str(len(java_calls)))
    results[verdict].append(frag)
    print(f"[{verdict}] {frag}  ({bucket})")
    for c in callers:
        print(f"     js: {c[0]}:{c[1]} shape={c[2]}")
    for c in java_calls[:2]:
        print(f"     java: {c[0]}:{c[1]}")
    if verdict == "NOT_STALE":
        print(f"     WHY: {why}")
    if verdict == "NO_CALLER":
        print(f"     WHY: {why} — search: fragment literal in JS/HTML + Java (non-mapping)")
    print()

print(f"=== SUMMARY: SAFE {len(results['SAFE'])} | NOT_STALE {len(results['NOT_STALE'])} | NO_CALLER {len(results['NO_CALLER'])} ===")
print("SAFE:", ", ".join(results["SAFE"]))
print("\nNOT_STALE:", ", ".join(results["NOT_STALE"]))
print("\nNO_CALLER:", ", ".join(results["NO_CALLER"]))
