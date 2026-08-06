#!/usr/bin/env python3
"""verify_allowlist_reality.py — live re-sweep of the JwtTokenFilter allowlist.

THE CHECK THE 66/66 SWEEP SHOULD HAVE BEEN. The old sweep curled the allowlist's
own strings, so a 404 counted as not-401 and a wrong-form entry passed. This
verifier derives the form a REAL caller produces from the controller mappings
(allowlist_audit.build_real_paths) and the audited pattern shapes — never from
the allowlist's own strings — and treats BOTH 401 and 404 on an ALLOW entry as
FAILURES (404 = allowlisted but no mapping = wrong or dead).

Passes:
  A. source cross-check: every allowlist exact must resolve to a controller
     mapping (or be a static shell file on disk)
  B. live ALLOW: curl the mapping-derived form -> NOT 401 and NOT 404
     (static shell: 200)
  C. live pattern shapes: one realistic shape per anchored pattern -> NOT 401/404
  D. live DENY samples: must 401 (the removed entries, the pattern-denials, and
     generic API paths)

Usage: python3 tools/verify_allowlist_reality.py
Exit 0 iff every assertion passes.
"""
import os
import subprocess
import sys
import urllib.error
import urllib.request

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from allowlist_audit import allowlist_entries, build_real_paths  # noqa: E402

ROOT = "http://localhost:8080"
REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
WEBAPP = os.path.join(REPO, "legacy/HIS-WAR/src/main/webapp")
STATIC_SHELL = ["/", "/index.html", "/healthcheck.html"]

# One realistic shaped form per anchored pattern (variable segments as a real
# caller would produce them). Derived from the frontend callers, not the filter.
PATTERN_SHAPES = [
    ("OPD feedback GET (6 vars)", "/api/outPatientFeedback/1/1/1/1/1/1"),
    ("IPD feedback GET (6 vars)", "/api/inPatientFeedback/1/1/1/1/1/1"),
    ("org-master subdomain GET", "/adt/organizationMasterList/localhost"),
    ("checkParti (twilio)", "/checkParti/12345"),
    ("bookView investigations (4 vars)", "/api/opd/getInvestigationsListForBookView/1/1/2026-01-01/2026-12-31"),
    ("twilio video token (2 vars)", "/token/Doctor/dataroom_TEST"),
    ("adt/br barcode (1 var)", "/adt/br/QR-TEST"),
]

# DENY samples: every removed entry (Class C/D/E + screen-images + redundant
# org-master bare), the pattern-denials, and generic API paths. All must 401.
DENY_SAMPLES = [
    # Class C+D — removed feedback bases; the colon-literal forms are now
    # pattern-covered (6 literal segments match the feedback patterns, which is
    # harmless — no caller produces literal colons), so the DENY sample uses
    # 5-segment feedback shapes the 6-segment patterns cannot match.
    "/api/inPatientFeedback",
    "/api/inPatientFeedback/1/2/3/4/5",
    "/api/outPatientFeedback",
    "/api/outPatientFeedback/1/2/3/4/5",
    # screen-images (post-login callers only) — removed, both forms
    "/applicationConfiguration/screen-images",
    "/api/unit/applicationConfiguration/screen-images/1/2",
    # redundant org-master bare — removed (pattern covers the variable form)
    "/adt/organizationMasterList",
    # Class E — swagger/config dropped entirely (ROOT + /HIS-WAR forms)
    "/v2/api-docs",
    "/swagger-ui.html",
    "/swagger-resources",
    "/api-ui",
    "/configuration/security",
    "/configuration/ui",
    "/images/favicon-16x16.png",
    "/HIS-WAR/v2/api-docs",
    "/HIS-WAR/swagger-ui.html",
    "/HIS-WAR/configuration/security",
    "/HIS-WAR/configuration/ui",
    # pattern-denials (no named pre-login caller)
    "/api/global/getVideoConsultationLinkFromHIS/abc123",
    "/LIS/global/getTotalLanguageMasterRecordActive/1/1",
    # M-Pesa callbacks — controller removed (abandoned-in-baseline); must now DENY
    "/Billing/resultUrl",
    "/Billing/validationResultResponse",
    # generic API paths
    "/api/unit/applicationConfiguration/billing/orgId/1/unitId/1",
    "/api/billing/unit/bill/Report/1/1",
]


def http_code(path, method="GET"):
    req = urllib.request.Request(ROOT + path, method=method)
    try:
        with urllib.request.urlopen(req, timeout=15) as r:
            return r.status
    except urllib.error.HTTPError as e:
        return e.code
    except Exception as e:
        return f"ERR:{e}"


def main():
    entries = allowlist_entries()
    real = build_real_paths()
    fails = []

    def check(name, cond, detail=""):
        tag = "PASS" if cond else "FAIL"
        print(f"[{tag}] {name}" + (f"  ({detail})" if detail else ""))
        if not cond:
            fails.append(name)

    print(f"allowlist exact entries parsed: {len(entries)}")
    print(f"real controller-mapping paths indexed: {len(real)}")

    # A + B: every exact entry — source cross-check then live curl of the
    # mapping-derived form.
    for entry in entries:
        if entry in STATIC_SHELL:
            fname = os.path.join(WEBAPP, entry.lstrip("/")) if entry != "/" else os.path.join(WEBAPP, "index.html")
            exists = os.path.isfile(fname)
            code = http_code(entry)
            check(f"shell {entry}", exists and code == 200, f"file={exists} code={code}")
            continue
        if entry not in real:
            check(f"ALLOW source: {entry} has no controller mapping", False, "wrong form or dead")
            continue
        code = http_code(entry)  # the mapping-derived form IS the curl target
        ok = code not in (401, 404)
        check(f"ALLOW {entry}", ok, f"code={code}")

    # C: pattern shapes
    for name, shape in PATTERN_SHAPES:
        code = http_code(shape)
        ok = code not in (401, 404)
        check(f"PATTERN {name} {shape}", ok, f"code={code}")

    # D: DENY samples
    for path in DENY_SAMPLES:
        code = http_code(path)
        check(f"DENY {path}", code == 401, f"code={code}")

    print(f"\nRESULT: {len(fails)} failure(s)")
    sys.exit(1 if fails else 0)


if __name__ == "__main__":
    main()
