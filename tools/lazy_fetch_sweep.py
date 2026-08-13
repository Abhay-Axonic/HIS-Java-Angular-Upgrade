#!/usr/bin/env python3
"""BR-173 lazy-fetch gap sweep (analysis only, no changes).

Every endpoint the frontend calls that is NOT reachable at page load — event
handlers, non-landing controller init, deferred/modal fetches — checked against
the 67 exact + 4 prefixes + 4 patterns. Rule-10 controls FIRST: the org-list
subdomain call and /checkParti/ are known lazy-fetch cases and MUST appear in
the extracted set before any negative is trusted."""
import re, os, sys, json

ROOT = "."
WEB = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js")

# ---- the coverage set (the current filter) ----
src = open(os.path.join(ROOT, "legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java")).read()
m = re.search(r'PUBLIC_ALLOWLIST = new LinkedHashSet<>\(List\.of\((.*?)\)\);', src, re.S)
exact = [l.strip().strip('",') for l in m.group(1).splitlines() if l.strip().startswith('"/')]
m2 = re.search(r'STATIC_PREFIXES = List\.of\((.*?)\);', src, re.S)
prefs = [l.strip().strip('",') for l in m2.group(1).splitlines() if l.strip().startswith('"/')]
pats = [r'^/api/outPatientFeedback/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+$',
        r'^/api/inPatientFeedback/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+$',
        r'^/adt/organizationMasterList/[^/]+$',
        r'^/checkParti/[^/]+$']

def norm(u):
    u = re.sub(r'^https?://[^/]+', "", u)
    u = u.split("?")[0]
    u = u.rstrip("/") if u != "/" else u
    return u

def covered(u):
    u = norm(u)
    if not u:
        return True
    if u in exact:
        return True
    if any(u.startswith(p) for p in prefs):
        return True
    if any(re.match(p, u) for p in pats):
        return True
    return False

# ---- the config constants (var NAME = "/path") ----
constants = {}
for dp, _, fs in os.walk(WEB):
    for f in fs:
        if f.endswith(".url.config.js") or f.endswith(".config.js"):
            t = open(os.path.join(dp, f), encoding="utf-8", errors="replace").read()
            for mm in re.finditer(r'var\s+([A-Z][A-Z0-9_]{2,})\s*=\s*["\'](/[^"\']+)["\']', t):
                constants[mm.group(1)] = mm.group(2)

# ---- the calls: serviceAction/fetch with their enclosing function + init-ness ----
def enclosing_function(t, pos):
    """the last $scope.fnName = function(...) or function fnName(...) before pos"""
    head = t[:pos]
    cands = re.findall(r'\$scope\.([A-Za-z0-9_]+)\s*=\s*function\s*\([^)]*\)\s*\{', head)
    if cands:
        return cands[-1]
    return "(top-level)"

calls = []          # (url, file, line, function, is_init)
url_literals = []   # quoted /path literals
for dp, _, fs in os.walk(WEB):
    for f in fs:
        if not f.endswith(".js"):
            continue
        p = os.path.join(dp, f)
        t = open(p, encoding="utf-8", errors="replace").read()
        # the URI constructions: URI = BASE_URL + ROOT_URL + CONSTANT or literal
        for mm in re.finditer(r'URI\w*\s*=\s*BASE_URL\s*\+\s*ROOT_URL\s*\+\s*([^;\n]+)', t):
            expr = mm.group(1)
            fn = enclosing_function(t, mm.start())
            is_init = fn.startswith("init") or fn in ("init", "initialize")
            for cm in re.finditer(r'([A-Z][A-Z0-9_]{2,})|("(?:/[^"\']+)"|\'(?:/[^\']+)\')', expr):
                tok = cm.group(1) or cm.group(2)
                if cm.group(1) and cm.group(1) in constants:
                    url = constants[cm.group(1)]
                elif cm.group(2):
                    url = cm.group(2).strip("'\"")
                else:
                    continue
                if url.startswith("/"):
                    calls.append((url, os.path.basename(p), t.count("\n", 0, mm.start()) + 1, fn, is_init))
        # the raw fetch forms: fetch(BASE_URL + ROOT_URL + ...) and fetch("/path")
        for mm in re.finditer(r'fetch\(\s*BASE_URL\s*\+\s*ROOT_URL\s*\+\s*([^)]+)\)', t):
            expr = mm.group(1)
            fn = enclosing_function(t, mm.start())
            is_init = fn.startswith("init") or fn in ("init", "initialize")
            for cm in re.finditer(r'([A-Z][A-Z0-9_]{2,})|("(?:/[^"\']+)"|\'(?:/[^\']+)\')', expr):
                tok = cm.group(1) or cm.group(2)
                if cm.group(1) and cm.group(1) in constants:
                    url = constants[cm.group(1)]
                elif cm.group(2):
                    url = cm.group(2).strip("'\"")
                else:
                    continue
                if url.startswith("/"):
                    calls.append((url, os.path.basename(p), t.count("\n", 0, mm.start()) + 1, fn, is_init))
        for mm in re.finditer(r'fetch\(\s*["\'](/(?:[^"\']+))["\']', t):
            fn = enclosing_function(t, mm.start())
            is_init = fn.startswith("init") or fn in ("init", "initialize")
            calls.append((mm.group(1), os.path.basename(p), t.count("\n", 0, mm.start()) + 1, fn, is_init))

# ---- the Rule-10 controls ----
ctrl1 = any("/adt/organizationMasterList" in c[0] for c in calls)
ctrl2 = any("checkParti" in c[0] for c in calls)
print(f"CONTROL: org-list subdomain call present in extracted set: {ctrl1}")
print(f"CONTROL: /checkParti/ present in extracted set: {ctrl2}")
assert ctrl1 and ctrl2, "CONTROL FAILED — the extraction missed a known lazy-fetch case"
print("CONTROL PASS — the extraction finds the known lazy-fetch calls\n")

# ---- the uncovered endpoints ----
seen = set()
uncovered = []
for url, f, ln, fn, is_init in calls:
    key = norm(url)
    if not key or key in seen or covered(key):
        continue
    # noise filter: param-fragments from assembled URLs (/0, /1/0/false, /5, /Billing/)
    segs = [s for s in key.split("/") if s]
    if not segs:
        continue
    if all(re.fullmatch(r'\d+', s) or s.lower() in ("false", "true") for s in segs):
        continue
    seen.add(key)
    uncovered.append((url, f, ln, fn, is_init))

# ---- the pre-login classification: the caller's controller -> the route ----
appjs = open(os.path.join(WEB, "config/app.js"), encoding="utf-8", errors="replace").read()
route_of = {}
for mm in re.finditer(r"url:\s*'([^']+)'[^}]*?controller:\s*[\"']([A-Za-z0-9_]+)Controller[\"']", appjs):
    route_of[mm.group(2) + "Controller.js"] = mm.group(1)
for mm in re.finditer(r"controller:\s*[\"']([A-Za-z0-9_]+)Controller[\"'][^}]*?url:\s*'([^']+)'", appjs):
    route_of[mm.group(1) + "Controller.js"] = mm.group(2)
PRELOGIN_ROUTE_HINTS = ("login", "selfregistration", "bookappointment", "bookview", "webappointment",
                        "feedback", "kiosk", "patienttypehead", "createorganization", "forgot", "otp",
                        "unknownpatientreg")
def route_of_file(f):
    r = route_of.get(f)
    if r:
        return r
    return "?"

def is_prelogin_route(route, f):
    if route != "?" and any(h in route.lower() for h in PRELOGIN_ROUTE_HINTS):
        return True
    return any(h in f.lower() for h in PRELOGIN_ROUTE_HINTS)

prelogin = [u for u in uncovered if is_prelogin_route(route_of_file(u[1]), u[1])]
postlogin = [u for u in uncovered if not is_prelogin_route(route_of_file(u[1]), u[1])]

print(f"=== UNCOVERED endpoints: {len(uncovered)} | pre-login-caller: {len(prelogin)} | post-login-only: {len(postlogin)} ===")
print(f"\n=== PRE-LOGIN REACHABLE (real gaps — same class as the org-list call) ===")
for url, f, ln, fn, is_init in sorted(prelogin, key=lambda x: x[0]):
    route = route_of_file(f)
    tag = "PAGE-LOAD(init)" if is_init else "LAZY(event/other)"
    print(f"  {url}  <- {f}:{ln} ({fn}) [{tag}] route={route}")
