#!/usr/bin/env python3
"""
Backend-existence + content-based clinical-keyword sweep, combined into one
pass over the same screen population -- built in response to a real gap
found rebuilding Phase 4 batch 7 (docs/ANGULAR_PENDING_FINDINGS.md entry 67):
modalityTypeMaster's frontend is fully real and correctly wired, but NONE of
its 6 endpoints were ever implemented on the Java side -- confirmed by
hitting the identical 404 against the real legacy app itself, not just the
rebuild. Step 0's existing checks (state live, template/controller exist,
controller has real-looking service calls) cannot detect this: they are
frontend-only. This script adds the missing check -- does the endpoint a
screen calls actually respond as something other than a 404 -- and, since
it's already opening every screen's controller/view file to do that, also
runs a content-based clinical-keyword sweep in the same pass (the check that
would have caught doorToNeedle's mistag, entry 66, which named/URL/module
heuristics missed because the *name* "doorToNeedle" has no clinical keyword
-- only the controller's actual patient/diagnosis/medication API calls do).

These two checks answer different questions (is this screen's BACKEND real?
vs. is this screen's RISK TAG correct?) but share the same file-reading walk
over the same screen population, so they're combined here for efficiency,
not because they're the same check.

Usage:
    python3 tools/angular-audit/check_backend_endpoints.py [--base-url http://localhost:8080] [--limit N]

Requires `data/states_enriched.json` to already exist (run run_audit.py
first if missing) and a live legacy backend at --base-url (defaults to the
local dev Tomcat). Read-only against app source; makes live HTTP requests
against the backend but never with real session data (empty bodies, no
mutating intent -- a 404 vs. anything-else check only).

Output: tools/angular-audit/data/endpoint_check_report.json (full detail)
plus a human-readable summary printed to stdout.
"""
import argparse
import json
import re
import sys
import time
import urllib.error
import urllib.request
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from common import WEBAPP, STATES_ENRICHED_JSON, DATA_DIR, REPO_ROOT, ensure_data_dir
from extract_states import strip_js_comments

CONFIG_DIR = WEBAPP / "resources" / "assets" / "js" / "config"
CONSTANTS_JS = WEBAPP / "resources" / "assets" / "js" / "constants" / "constants.js"
ROUTES_TS = REPO_ROOT / "new" / "src" / "app" / "app.routes.ts"

# ---- constant resolution (url.config.js + constants.js) ----

VAR_DECL = re.compile(r"^\s*var\s+([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.+?)\s*;?\s*$")
STR_LIT = re.compile(r"""^['"](.*)['"]$""")
LEAF_KV = re.compile(r"^\s*([A-Za-z_][A-Za-z0-9_]*)\s*:\s*(.+?),?\s*$")
OPEN_OBJ = re.compile(r"^\s*([A-Za-z_][A-Za-z0-9_]*)\s*:\s*\{\s*$")


def _split_concat(expr):
    """Split a JS `+`-concatenation expression into tokens, respecting quotes."""
    tokens, cur, in_str, q = [], "", False, ""
    for ch in expr:
        if in_str:
            cur += ch
            if ch == q:
                in_str = False
        elif ch in ("'", '"'):
            in_str = True
            q = ch
            cur += ch
        elif ch == "+":
            tokens.append(cur.strip())
            cur = ""
        else:
            cur += ch
    if cur.strip():
        tokens.append(cur.strip())
    return tokens


def _resolve_expr(expr, env, stack=None):
    """Resolve a `+`-concatenation of string literals and known identifiers.
    Returns a resolved string, or None if any token can't be resolved."""
    stack = stack or set()
    parts = []
    for tok in _split_concat(expr):
        m = STR_LIT.match(tok)
        if m:
            parts.append(m.group(1))
            continue
        ident = tok.strip()
        if ident in stack:
            return None  # cycle guard
        if ident in env:
            val = env[ident]
            if val is None:
                return None
            parts.append(val)
            continue
        return None  # unknown token -- can't resolve
    return "".join(parts)


def parse_url_config_files():
    """Flat `var NAME = expr;` files -- resolved iteratively to a fixed point."""
    env = {}
    raw = {}
    for f in sorted(CONFIG_DIR.glob("*.url.config.js")):
        text = f.read_text(errors="replace")
        for line in text.splitlines():
            m = VAR_DECL.match(line)
            if m:
                raw[m.group(1)] = m.group(2)
    # iterative resolution -- a few passes is enough for this codebase's
    # shallow nesting (base constant -> one or two levels of concatenation)
    for _ in range(6):
        progress = False
        for name, expr in raw.items():
            if name in env:
                continue
            val = _resolve_expr(expr, env)
            if val is not None:
                env[name] = val
                progress = True
        if not progress:
            break
    return env


def parse_constants_js():
    """Nested `CONSTANTS.A.B.KEY = baseVar + 'literal'` object, tracked via
    brace-depth + key-stack since this isn't valid JSON (it's a JS factory
    function body). Best-effort: unresolvable leaves are simply omitted,
    never guessed."""
    if not CONSTANTS_JS.exists():
        return {}
    text = CONSTANTS_JS.read_text(errors="replace")
    lines = text.splitlines()

    # Phase 1: top-level `var name = expr;` base URL fragments declared
    # before the `return { ... }` block (host, root, rootUrl, globalApi, ...).
    base_env = {}
    raw_vars = {}
    for line in lines:
        m = VAR_DECL.match(line)
        if m:
            raw_vars[m.group(1)] = m.group(2)
    for _ in range(6):
        progress = False
        for name, expr in raw_vars.items():
            if name in base_env:
                continue
            val = _resolve_expr(expr, base_env)
            if val is not None:
                base_env[name] = val
                progress = True
        if not progress:
            break

    # Phase 2: walk the returned object literal, tracking a key-path stack.
    resolved = {}
    stack = []
    in_return = False
    for line in lines:
        if not in_return:
            if re.search(r"^\s*return\s*\{", line):
                in_return = True
            continue
        stripped = line.strip()
        if stripped in ("}", "};", "},"):
            if stack:
                stack.pop()
            continue
        m = OPEN_OBJ.match(line)
        if m:
            stack.append(m.group(1))
            continue
        m = LEAF_KV.match(line)
        if m:
            key, expr = m.group(1), m.group(2)
            val = _resolve_expr(expr, base_env)
            dotted = ".".join(stack + [key])
            if val is not None:
                resolved[dotted] = val
                resolved[key] = val  # also index by bare leaf name, last-wins
    return resolved


def build_constant_map():
    env = parse_url_config_files()
    env.update({k: v for k, v in parse_constants_js().items()})
    return env


# ---- endpoint extraction from a controller file ----
#
# Anchored on this codebase's dominant, well-established API-call
# construction convention (confirmed present in 863 controller files):
# `<something> = BASE_URL + ROOT_URL + <CONST or literal>[+ dynamic suffix];`
# used as the URI argument to GenericService/BillingGenericService's
# `.serviceAction(method, URI, ...)` (1175 call sites total), whether that
# concatenation sits in a local `var URI = ...` a few lines above the call,
# or inline as the call's own argument. Anchoring here (rather than "any
# quoted /-prefixed string in the file") is deliberate: an earlier,
# unanchored version of this extraction picked up unrelated stray literals
# (image paths, partial-view fragments) and, worse, attributed an entire
# giant SHARED controller's content to every one of the ~353 screens that
# happen to route through it -- inflating false positives to 208/274
# screens on the first real sweep. Screens on a shared/generic controller
# (`ctrl_shared` in states_enriched.json) are excluded entirely here for
# the same reason `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` already
# excludes them from its own per-screen API-call counts: the shared file's
# content isn't meaningfully attributable to any one of its screens.
BASE_ROOT_CONCAT = re.compile(
    r"BASE_URL\s*\+\s*ROOT_URL\s*\+\s*(.+?)(?=[;,)])"
)
CONST_TOKEN = re.compile(r"\bCONSTANTS((?:\.[A-Za-z_][A-Za-z0-9_]*)+)")
BARE_CONST = re.compile(r"\b([A-Z][A-Z0-9_]{4,})\b")
STR_LIT_ANYWHERE = re.compile(r"""["'](/[A-Za-z0-9_/\-]{3,})["']""")
CONST_STOPWORDS = {
    "BASE_URL", "ROOT_URL", "HTTP", "HTTPS", "JSON", "POST", "GET", "PUT",
    "DELETE", "PATCH", "TRUE", "FALSE", "NULL", "UNDEFINED", "NAN",
}


def extract_endpoint_refs(text):
    """Returns a list of (kind, token) -- kind in {'literal','constants_dotted','bare_const'}.
    Only looks inside `BASE_URL + ROOT_URL + <...>` construction sites, this
    codebase's real API-call-building convention -- not any quoted string
    anywhere in the file."""
    refs = []
    for m in BASE_ROOT_CONCAT.finditer(text):
        tail = m.group(1)
        # tail may itself be a further `+`-concatenation (e.g. `GETX + "/" + id`)
        # -- pull out every literal and every ALL_CAPS/CONSTANTS token inside it.
        for lm in STR_LIT_ANYWHERE.finditer(tail):
            refs.append(("literal", lm.group(1)))
        for cm in CONST_TOKEN.finditer(tail):
            refs.append(("constants_dotted", cm.group(1).lstrip(".")))
        for bm in BARE_CONST.finditer(tail):
            tok = bm.group(1)
            if tok in CONST_STOPWORDS:
                continue
            refs.append(("bare_const", tok))
    return refs


def resolve_ref(kind, token, const_map):
    if kind == "literal":
        return token
    if kind == "constants_dotted":
        return const_map.get(token)
    if kind == "bare_const":
        return const_map.get(token)
    return None


# ---- clinical-keyword content sweep ----
# Broader than classify_risk.py's name/url/module-only pass -- these run
# against actual controller+view FILE CONTENTS, the exact gap that let
# doorToNeedle (entry 66) slip through: its name/url/module carried no
# clinical signal, only its real API calls (getPatientByKeyword,
# getActiveEmergencyDiagnosis, per-medication administration timing) did.
CONTENT_CLINICAL_KEYWORDS = [
    "patient", "diagnos", "medicat", "administ", "vital", "dose", "dosage",
    "symptom", "treatment", "prescri", "clinical", "admission", "encounter",
    "allerg", "vaccin", "surger", "anesthe", "consult", "ward", "bed",
    "nursing", "physician", "doctor", "specimen", "lab result", "radiolog",
    "pathology", "discharge summary", "chief complaint",
]


def content_clinical_hits(text):
    lower = text.lower()
    return sorted({kw for kw in CONTENT_CLINICAL_KEYWORDS if kw in lower})


# ---- live backend check ----

def _check_once(base_url, path, auth_headers, method, timeout=4):
    url = base_url.rstrip("/") + path
    headers = {"Content-Type": "application/json"}
    headers.update(auth_headers)
    data = b"{}" if method == "POST" else None
    req = urllib.request.Request(url, data=data, method=method, headers=headers)
    try:
        with urllib.request.urlopen(req, timeout=timeout) as resp:
            return resp.status
    except urllib.error.HTTPError as e:
        return e.code
    except Exception as e:
        return f"ERROR:{e}"


# Many endpoints are built as `CONSTANT + "/" + dynamicId[+ "/" + dynamicId2
# ...]` at the call site (confirmed up to 3 chained path variables, e.g.
# `getDetailedNursingStationList/{userId}/{orgId}/{unitId}`) -- the static
# extraction only ever sees the constant, never the dynamic suffix. Probe
# dummy-ID variants before concluding a route is genuinely missing, to
# avoid flagging "needs more path segments than we guessed" as "doesn't
# exist" (confirmed: this exact gap produced a false "ALL endpoints
# missing" read on the entire *Dashboard* screen family, which turned out
# to need 3 segments, not 2).
PATH_VARIABLE_SUFFIXES = ["", "/1", "/1/1", "/1/1/1"]
# Some real, working endpoints are GET-mapped, not POST (confirmed:
# CEOdashbordController.js's `getDetailedNursingStationList` call is a
# genuine `GenericService.serviceAction("GET", ...)`) -- a real GET-only
# route probed with POST returns 405 Method Not Allowed, not 404, so this
# alone wouldn't have caused a false positive, but trying both verbs
# before giving up is cheap and removes any doubt.
METHODS = ["POST", "GET"]


def check_live(base_url, path, auth_headers, timeout=4):
    """Returns (best_status, all_statuses). Only reports 404 if EVERY
    suffix/method combination also 404s -- otherwise takes the best
    (non-404) status found, on the theory that a real route exists."""
    statuses = []
    for suffix in PATH_VARIABLE_SUFFIXES:
        for method in METHODS:
            st = _check_once(base_url, path + suffix, auth_headers, method, timeout=timeout)
            statuses.append(st)
            if st != 404:
                return st, statuses
    return 404, statuses


def built_route_urls():
    """Route paths already wired in new/src/app/app.routes.ts, to exclude
    from the 'remaining' pool -- these were already Step-5-verified live."""
    if not ROUTES_TS.exists():
        return set()
    text = ROUTES_TS.read_text(errors="replace")
    return {"/" + m for m in re.findall(r"path:\s*'([^']*)'", text) if m}


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--base-url", default="http://localhost:8080")
    ap.add_argument("--limit", type=int, default=None, help="cap number of screens (debugging)")
    ap.add_argument("--only", default=None, help="comma-separated state names, for a targeted re-check")
    ap.add_argument("--auth-token", default=None,
                     help="A real, currently-valid JWT captured from an active logged-in session's "
                          "cookieObject.authToken (devtools). REQUIRED for a reliable sweep -- some "
                          "endpoint prefixes reject unauthenticated requests at a security filter "
                          "BEFORE Spring MVC's routing layer ever runs, returning a generic 401 for "
                          "both 'route doesn't exist' and 'route exists, needs auth' alike. Without "
                          "a valid token those two cases are indistinguishable and missing-backend "
                          "screens will silently NOT be flagged (confirmed: this exact false-negative "
                          "happened for modalityTypeMaster during this check's own development).")
    ap.add_argument("--user-id", default="1")
    ap.add_argument("--unit-id", default="1")
    args = ap.parse_args()

    auth_headers = {}
    if args.auth_token:
        auth_headers = {
            "Authorization": f"Bearer {args.auth_token}",
            "userId": args.user_id,
            "unitId": args.unit_id,
        }
    else:
        print(
            "WARNING: no --auth-token supplied. Some endpoint prefixes 401 at a security "
            "filter before routing even runs, making 'route missing' and 'route exists, "
            "needs auth' indistinguishable -- results will under-report missing backends. "
            "Capture a fresh token from cookieObject.authToken in a live logged-in session "
            "and pass --auth-token for a reliable run.",
            file=sys.stderr,
        )

    if not STATES_ENRICHED_JSON.exists():
        print(f"Missing {STATES_ENRICHED_JSON} -- run run_audit.py first.", file=sys.stderr)
        sys.exit(1)

    states = json.loads(STATES_ENRICHED_JSON.read_text())
    built = built_route_urls()

    if args.only:
        wanted = set(args.only.split(","))
        pool = [s for s in states if s["name"] in wanted]
        shared_skipped = []
    else:
        candidates = [
            s for s in states
            if s.get("clinical_risk") == "no" and s.get("billing_risk") == "no"
            and s.get("url") not in built
            and s["name"] != "doorToNeedle"  # already reclassified, entry 66
        ]
        pool = [s for s in candidates if not s.get("ctrl_shared")]
        shared_skipped = [s["name"] for s in candidates if s.get("ctrl_shared")]
    if args.limit:
        pool = pool[: args.limit]

    print(f"Screens in sweep population: {len(pool)} (built-excluded, doorToNeedle-excluded, "
          f"{len(shared_skipped)} shared-controller screens excluded -- not meaningfully "
          f"attributable, same caveat as the inventory doc's own API-call counts)")

    const_map = build_constant_map()
    print(f"Resolved {len(const_map)} URL constants from config files")

    # Pass 1: per-screen extraction (endpoints + clinical content hits).
    # Comments are stripped first (extract_states.strip_js_comments /
    # strip_html_comments) -- an earlier version of this script didn't, and
    # flagged dead/commented-out endpoint references (real, but never
    # executed) as if they were live calls, inflating false positives.
    per_screen = {}
    endpoint_to_screens = {}
    for s in pool:
        name = s["name"]
        ctrl_files = s.get("ctrl_files") or []
        resolved_paths = set()
        unresolved_tokens = set()
        endpoint_tokens_raw = set()  # for the clinical-keyword scan below
        for cf in ctrl_files:
            fpath = WEBAPP / cf
            if not fpath.exists():
                continue
            text = strip_js_comments(fpath.read_text(errors="replace"))
            for kind, token in extract_endpoint_refs(text):
                endpoint_tokens_raw.add(token)
                resolved = resolve_ref(kind, token, const_map)
                if resolved and resolved.startswith("/"):
                    resolved_paths.add(resolved)
                elif kind != "literal":
                    unresolved_tokens.add(token)

        # Clinical-keyword hits are checked against the REAL, LIVE API
        # surface this screen actually calls -- the resolved endpoint path
        # strings plus the raw constant/token names referenced at those
        # same call sites -- not the whole controller/view file's raw text.
        # An earlier version scanned whole-file text and got swamped by
        # shared CSS-class/markup boilerplate (e.g. a generic
        # `class="patient-details-row"` modal wrapper reused across dozens
        # of unrelated Asset Management screens) -- exactly the kind of
        # false signal that doesn't reflect what the screen's own backend
        # traffic actually touches, unlike doorToNeedle's real API
        # constants (GETPATIENTBYKEYWORD, getActiveEmergencyDiagnosis).
        clinical_surface = " ".join(sorted(resolved_paths) + sorted(endpoint_tokens_raw))
        clinical_hits = set(content_clinical_hits(clinical_surface))

        per_screen[name] = {
            "resolved_paths": sorted(resolved_paths),
            "unresolved_tokens": sorted(unresolved_tokens),
            "clinical_content_hits": sorted(clinical_hits),
            "module": s.get("module"),
            "url": s.get("url"),
        }
        for p in resolved_paths:
            endpoint_to_screens.setdefault(p, set()).add(name)

    print(f"Distinct resolved endpoint paths to live-check: {len(endpoint_to_screens)}")

    # Pass 2: live-check each distinct endpoint once
    endpoint_status = {}
    endpoint_status_detail = {}
    for i, path in enumerate(sorted(endpoint_to_screens), 1):
        best, all_statuses = check_live(args.base_url, path, auth_headers)
        endpoint_status[path] = best
        endpoint_status_detail[path] = all_statuses
        if i % 50 == 0:
            print(f"  ...checked {i}/{len(endpoint_to_screens)}")

    # Assemble report
    flagged_missing_backend = []
    flagged_clinical_content = []
    unresolved_screens = []

    for name, info in per_screen.items():
        statuses = {p: endpoint_status[p] for p in info["resolved_paths"]}
        missing = [p for p, st in statuses.items() if st == 404]
        if missing and info["resolved_paths"]:
            flagged_missing_backend.append({
                "name": name, "url": info["url"], "module": info["module"],
                "missing_endpoints": missing,
                "total_endpoints_checked": len(info["resolved_paths"]),
                "all_missing": len(missing) == len(info["resolved_paths"]),
            })
        if info["clinical_content_hits"]:
            flagged_clinical_content.append({
                "name": name, "url": info["url"], "module": info["module"],
                "keyword_hits": info["clinical_content_hits"],
            })
        if not info["resolved_paths"] and info["unresolved_tokens"]:
            unresolved_screens.append({
                "name": name, "url": info["url"],
                "unresolved_tokens": info["unresolved_tokens"][:10],
            })

    report = {
        "swept_at": "2026-07-21",
        "pool_size": len(pool),
        "shared_controller_screens_excluded": shared_skipped,
        "distinct_endpoints_checked": len(endpoint_to_screens),
        "flagged_missing_backend": sorted(flagged_missing_backend, key=lambda x: -x["total_endpoints_checked"]),
        "flagged_clinical_content": flagged_clinical_content,
        "unresolved_screens": unresolved_screens,
        "endpoint_status_raw": endpoint_status,
        "endpoint_status_detail": endpoint_status_detail,
    }
    ensure_data_dir()
    out_path = DATA_DIR / "endpoint_check_report.json"
    out_path.write_text(json.dumps(report, indent=2))

    print()
    print("=" * 70)
    print(f"Backend-missing (>=1 endpoint 404'd): {len(flagged_missing_backend)} screens")
    for f in flagged_missing_backend:
        tag = "ALL endpoints missing" if f["all_missing"] else f"{len(f['missing_endpoints'])}/{f['total_endpoints_checked']} missing"
        print(f"  - {f['name']} ({f['module']}): {tag}")
    print()
    print(f"Content-based clinical-keyword hits (possible mistag, doorToNeedle-class): {len(flagged_clinical_content)} screens")
    for f in flagged_clinical_content:
        print(f"  - {f['name']} ({f['module']}): {', '.join(f['keyword_hits'])}")
    print()
    print(f"Screens with unresolved endpoint tokens (not checked, needs manual review): {len(unresolved_screens)}")
    print()
    print(f"Full detail written to {out_path}")


if __name__ == "__main__":
    main()
