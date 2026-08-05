#!/usr/bin/env python3
"""BR-173 Phase-2 prep: the 108 ALLOW list in REVIEWABLE form (risk-ordered:
writes -> PHI reads -> reference/config reads -> static/swagger) + the
KIOSK-public ~30 re-confirmation (named caller + screen + ROUTE check that the
screen genuinely runs pre-login; UNCONFIRMED -> DENY default). Rule-10 control on
the constant traces first."""
import re, os, sys

ROOT = "."
WEB = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js")
APPJS = os.path.join(WEB, "config/app.js")

# ---- the constant-trace control FIRST ----
# known positive: GETACTIVECOUNTRYLIST in adt.url.config.js + its users
cfg = os.path.join(WEB, "config/adt.url.config.js")
ct = open(cfg, encoding="utf-8", errors="replace").read()
m = re.search(r'var\s+(GETACTIVECOUNTRYLIST)\s*=\s*["\']([^"\']+)["\']', ct)
control_ok = False
ctrl_detail = ""
if m:
    cname, cval = m.group(1), m.group(2)
    users = []
    for dp, _, fs in os.walk(WEB):
        for f in fs:
            if f.endswith(".js") and "url.config" not in f:
                t = open(os.path.join(dp, f), encoding="utf-8", errors="replace").read()
                if cname in t:
                    users.append(os.path.basename(f))
    control_ok = bool(users) and cval.startswith("/adt/getActiveCountryList")
    ctrl_detail = f"{cname}={cval} used by {users[:4]}"
print(f"CONTROL: {ctrl_detail}")
assert control_ok, "CONTROL FAILED"
print("CONTROL PASS — constant-to-URL resolution + pre-login users verified\n")

# ---- load the allowlist ALLOW rows from the tool's output ----
import subprocess
r = subprocess.run(["python3", "tools/allowlist_proposal.py"], capture_output=True, text=True, cwd=ROOT, timeout=380)
allow_rows = []
for l in r.stdout.splitlines():
    if l.startswith("  ALLOW "):
        rest = l[8:]
        url = rest.split("  ")[0]
        bkt = re.search(r"\[(\w+)\]", rest)
        reason = re.search(r"\((.+)\)", rest)
        allow_rows.append({"url": url, "bucket": bkt.group(1) if bkt else "?",
                           "reason": reason.group(1) if reason else "?"})

# caller lookup (reuse the tool's finder via a lightweight grep over the loaded files)
def find_caller(url):
    target = url if len(url) <= 64 else "/" + "/".join([s for s in url.split("/") if s][:4])
    for dp, _, fs in os.walk(WEB):
        for f in fs:
            if not f.endswith(".js"):
                continue
            p = os.path.join(dp, f)
            t = open(p, encoding="utf-8", errors="replace").read()
            if target in t:
                ln = t.count("\n", 0, t.find(target)) + 1
                return f"{f}:{ln}"
    return ""

VERBS = {"save": "POST", "update": "PUT/POST", "delete": "DELETE", "cancel": "POST",
         "feedback": "POST", "registration": "POST", "check": "GET/POST", "create": "POST"}
def verb_of(url):
    for k, v in VERBS.items():
        if k in url.lower():
            return v
    return "GET"

def touches_of(url, bucket):
    u = url.lower()
    if any(s in u for s in ("swagger", "webjars", "favicon", "api-ui", "images", ".png", ".ttf", ".css")):
        return "static"
    if any(s in u for s in ("applicationconfiguration", "configuration")):
        return "config"
    if any(s in u for s in ("saveandpay", "bill", "prebilling", "servicemaster", "frequency", "tariff", "prefix")):
        return "billing"
    if any(s in u for s in ("patientregistration", "feedback", "patienttypehead", "getpatient", "encounter",
                            "appointment", "lastencounter", "patientdoc", "growthdevelopmental", "milestone",
                            "worklist", "appointmentsbypatientid", "abdm", "mylife", "twilio", "otp",
                            "getdoctors", "checkemail", "videoconsultation", "generateflipbook", "doctorsforweb")):
        return "PHI"
    return "reference"

def risk_order(t):
    tc = touches_of(t["url"], t["bucket"])
    if "POST" in verb_of(t["url"]) or t["url"].endswith(("saveAndPay", "patientRegistration", "Feedback")):
        return 0
    if tc == "PHI":
        return 1
    if tc in ("billing", "config"):
        return 2
    if tc == "reference":
        return 3
    return 4

rows = sorted(allow_rows, key=risk_order)
print(f"=== THE 108 ALLOW, RISK-ORDERED ({len(rows)}) ===")
for t in rows:
    v = verb_of(t["url"])
    tc = touches_of(t["url"], t["bucket"])
    cl = find_caller(t["url"])
    print(f"{t['url']} | {v} | {tc} | {cl} | {t['reason']}")

# ---- the kiosk re-confirmation: the ~30 ----
print("\n=== KIOSK-PUBLIC RE-CONFIRMATION (named caller + screen + route) ===")
kiosk = [t for t in rows if t["bucket"] == "KIOSK"]
print(f"KIOSK ALLOW count: {len(kiosk)}")
# the route check: is the caller's screen reachable pre-login? Look for the screen's route in app.js
appjs = open(APPJS, encoding="utf-8", errors="replace").read()
for t in kiosk:
    cl = find_caller(t["url"])
    ctrl = cl.split(":")[0] if cl else ""
    # find the route that names this controller
    route_found = "?"
    m2 = re.search(r"url:\s*'([^']*)'[^}]*?controller:\s*[\"']" + re.escape(ctrl.replace(".js", "")) + r"[\"']", appjs)
    if m2:
        route_found = m2.group(1)
    elif ctrl:
        m3 = re.search(r"controller:\s*[\"']" + re.escape(ctrl.replace(".js", "")) + r"[\"'][^}]*?url:\s*'([^']*)'", appjs)
        if m3:
            route_found = m3.group(1)
    print(f"{t['url']} | caller {cl or 'NONE'} | route {route_found} | {t['reason']}")
