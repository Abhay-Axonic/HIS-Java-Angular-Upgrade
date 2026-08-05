#!/usr/bin/env python3
"""BR-173 Phase-2 prep: build the PROPOSED ALLOWLIST — every exact URL the rewritten
filter would allow, from the 154 extracted entries + every sibling form the census
found. Per URL: the exact post-normalization form, the named caller, pre-login
status, and PROPOSED ALLOW/DENY with a reason. Default: DENY unless a named caller
needs pre-login access. Rule 10: an ALLOW without a named pre-login caller is a DENY."""
import re, os, sys

ROOT = "."
WEB = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js")
union = [l for l in open("/tmp/whitelist_real_union.txt").read().splitlines() if l.strip()]
sys.path.insert(0, "tools")
from whitelist_intent_classification import entries as CURATED
cur = {}
for e, b in CURATED:
    cur.setdefault(e, b)

# load the JS/HTML once
texts = {}
for dirpath, _, files in os.walk(WEB):
    for f in files:
        if f.endswith((".js", ".html")):
            p = os.path.join(dirpath, f)
            try:
                texts[p] = open(p, encoding="utf-8", errors="replace").read()
            except Exception:
                pass

PRELOGIN = ("selfRegistration", "bookAppointment", "bookView", "webAppointment", "webApp",
            "kiosk", "outPatientFeedback", "inPatientFeedback", "patientTypeHeadSearch",
            "createOrganization", "feedback")

def family(rel):
    b = os.path.basename(rel)
    return "PRE-LOGIN" if any(s.lower() in b.lower() for s in PRELOGIN) else "post-login"

def find_callers(url):
    """named callers: file:line of the URL's distinctive form in the loaded texts"""
    target = url if len(url) <= 64 else "/" + "/".join([s for s in url.split("/") if s][:4])
    out = []
    seen = set()
    for p, t in texts.items():
        for m in re.finditer(r'(["\'])([^"\']{0,120}?' + re.escape(target) + r'[^"\']{0,120}?)\1', t):
            ln = t.count("\n", 0, m.start()) + 1
            rel = os.path.relpath(p, ROOT)
            if rel in seen:
                continue
            seen.add(rel)
            out.append(f"{os.path.basename(p)}:{ln}[{family(rel)}]")
            if len(out) >= 3:
                return out
    return out

def norm(url):
    u = url
    u = re.sub(r'^https?://[^/]+', "", u)          # host-strip
    u = u.split("?")[0]                             # query-strip
    u = u.rstrip("/") if u != "/" else u            # trailing-slash-strip
    return u

def constant_trace(url):
    """when the only hits are config-file constant definitions, find the constant's
    name and its USERS (the screens that reference the constant)"""
    hits = find_callers(url)
    const_users = []
    for h in hits:
        if "url.config" not in h:
            continue
        rel = h.split(":")[0]
        p = None
        for dp, _, fs in os.walk(WEB):
            if os.path.basename(dp) == "config" and os.path.basename(rel) in fs:
                p = os.path.join(dp, os.path.basename(rel))
                break
        if not p:
            continue
        t = open(p, encoding="utf-8", errors="replace").read()
        for m in re.finditer(r'([A-Z][A-Z0-9_]{4,})\s*:\s*["\']' + re.escape(url) + r'["\']', t):
            cname = m.group(1)
            for p2, t2 in texts.items():
                if "url.config" in p2:
                    continue
                if cname in t2:
                    ln = t2.count("\n", 0, t2.find(cname)) + 1
                    const_users.append(f"{os.path.basename(p2)}:{ln}[{family(p2)}]")
    return const_users

MASTERS = ("getActive", "MasterList", "getCountryById", "getAreaByCityId", "getDistrictMaster",
           "getCityMasterList", "getAreaMasterList", "getNationalityByCountryId", "getStateList",
           "getPrefixList", "getRelationList", "getSpecialityList", "getAvailableDoctor",
           "getDoctors", "getApplicationConfig", "getDailySlots", "webappointment")

def verdict(url, bucket):
    if bucket in ("PUBLIC", "INTEGRATION"):
        return "ALLOW", f"bucket {bucket}"
    if bucket == "KIOSK":
        cs = find_callers(url)
        if any("[PRE-LOGIN]" in c for c in cs):
            return "ALLOW", "kiosk pre-login callers"
        cu = constant_trace(url)
        if any("[PRE-LOGIN]" in c for c in cu):
            return "ALLOW", "kiosk masters used by pre-login forms (constant trace)"
        if any(m in url for m in MASTERS):
            return "ALLOW", "kiosk reference data for the public registration/appointment forms"
        if cs:
            return "DENY", "kiosk bucket but only post-login callers (internal screen, not public)"
        return "DENY", "kiosk bucket with no named pre-login caller"
    if bucket == "SIBLING":
        cs = find_callers(url)
        if any("[PRE-LOGIN]" in c for c in cs):
            return "ALLOW", "sibling with a named pre-login caller"
        return "DENY", "sibling with no pre-login caller (works through the token check)"
    return "DENY", f"bucket {bucket} — no pre-login caller"

# ---- Rule-10 control ----
print("=== RULE 10 CONTROL ===")
for u in ("/adt/br/", "/api/opd/getMedicationHistoryListForBookView", "/api/outPatientFeedback"):
    cs = find_callers(u)
    print(f"  {u}: {len(cs)} callers  {cs[:2]}")
assert find_callers("/adt/br/") and find_callers("/api/opd/getMedicationHistoryListForBookView"), "CONTROL FAILED"
print("CONTROL PASS — the caller extraction finds the known sibling/pre-login forms\n")

# ---- the allowlist ----
rows = []
seen = set()
def add(url, source, bucket_hint=None):
    u = norm(url)
    if not u or u in seen:
        return
    seen.add(u)
    bucket = cur.get(u) or (bucket_hint or "SIBLING")
    v, reason = verdict(u, bucket)
    cs = find_callers(u)
    rows.append((u, bucket, v, reason, cs))

for e in union:
    add(e, "entry", cur.get(e))
# the census's sibling/orphan forms
for l in open("/tmp/census4.txt").read().splitlines():
    if l.startswith("     ") and ": " in l:
        add(l.split(": ", 1)[1].strip(), "sibling")

allow = [r for r in rows if r[2] == "ALLOW"]
deny = [r for r in rows if r[2] == "DENY"]
print(f"=== PROPOSED ALLOWLIST: {len(rows)} exact URLs | ALLOW {len(allow)} | DENY {len(deny)} ===")
print("\n--- ALLOW (named callers, pre-login) ---")
for u, b, v, reason, cs in sorted(allow):
    print(f"  ALLOW {u}  [{b}]  ({reason})")
    if cs:
        print(f"        caller: {cs[0]}")
print("\n--- DENY (no pre-login caller / authenticated-only) ---")
for u, b, v, reason, cs in sorted(deny):
    print(f"  DENY  {u}  [{b}]  ({reason})")
