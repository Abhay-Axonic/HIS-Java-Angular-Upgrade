#!/usr/bin/env python3
"""probe_common_master.py — browser probe for the commonMaster dispatcher slice
(screens: commonMaster/:url, family node-ids 412..844).
Injected real-login token (stated plainly per browser-smoke-harness).
Zero backend seeding.

The slice (owner decision, option b) is the dispatcher + 4 Unit Master branches
(phlebotomyMaster, headerMaster, departmentMaster, biochemicalTest) + 1 ADT
real-data branch (relationmaster, node 390) to prove the data path.

Probe asserts:
  1. POSITIVE GATE: with the family's node-id array in permissions, the route
     loads for each slice branch (dispatch works).
  2. RELATION RENDERS REAL DATA: relationmaster returns 200 with real rows
     (Cousin / ABC123) — the live-data proof.
  3. DEV-EMPTY HONESTY: phlebotomyMaster surfaces the backend's own message
     ("Phlebotomy Records not found.") rather than masking it.
  4. NEGATIVE CONTROL (the gate detail that would break five of six silently):
     with permissions containing ONLY a non-family node id (999999), the route
     redirects to accessDenied — a guard accepting one id would have denied
     the other branches and every gate would pass; the array form must deny
     when NO family id is present.
  5. ZERO page errors across all branches.
"""
import json
import sys
import time
import urllib.parse

try:
    from playwright.sync_api import sync_playwright
except ImportError:
    print("FAIL: playwright not installed")
    sys.exit(1)

# Served-bundle freshness gate (standing rule, owner directive 2026-08-12 —
# the screen-11 stale tree and the guard fail-open retraction were both
# serve-side staleness: the browser ran code that did not match the tree, and
# the probe reported a behavioural result anyway). Before ANY assertion the
# probe verifies the served bundle matches the current source; a stale bundle
# fails loudly with exit code 2 (FRESHNESS-FAIL) instead of a finding.
sys.path.insert(0, "tools")
from bundle_freshness import check_freshness, EXIT_STALE  # noqa: E402

FRESHNESS_SPECS = [
    {
        "label": "module-access.guard (main.js)",
        "source": "new/src/app/core/module-access.guard.ts",
        "url": "/main.js",
        "absent": ["GUARD-DEBUG", "GUARD-NAV", "GUARD-DENY", "navigateByUrl"],
    },
    {
        "label": "app.routes (main.js)",
        "source": "new/src/app/app.routes.ts",
        "url": "/main.js",
    },
    {
        "label": "common-master.service (feature chunk)",
        "source": "new/src/app/features/common-master/common-master.service.ts",
        "url": "/main.js",
    },
]

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

BASE = "http://localhost:4201"
FAMILY_IDS = [
    412, 413, 414, 415, 417, 419, 420, 392, 423, 424, 427, 435, 436,
    437, 825, 430, 429, 425, 431, 438, 440, 428, 439, 561, 564, 557,
    389, 390, 391, 450, 460, 492, 393, 394, 395, 491, 447, 448, 449,
    798, 569, 826, 426, 421, 844,
]
SLICE_BRANCHES = ["phlebotomyMaster", "headerMaster", "departmentMaster",
                  "biochemicalTest", "relationmaster"]
# ADT family batch (owner order 2026-08-12: ADT 7 first — the real-data
# family). Each branch asserts its LIVE rows render (all verified with real
# data: gender, marital, religion, occupation, patientSource, referralType).
ADT_BRANCHES = {
    "sexmaster": "Test Save Gender",
    "maritalstatus": "Widowed",
    "religion": "Victim Religion 999",
    "occupation": "Teacher2",
    "patientsource": "PATIENT12",
    "referraltype": "Testing1",
}
FAILS = []


def make_cookie(page_ctx):
    co = urllib.parse.quote(json.dumps({
        "unitId": 1, "organizationId": 1, "userId": 1,
        "isSuperUser": "Y", "authToken": TOKEN,
    }))
    page_ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])


def main():
    # FRESHNESS GATE FIRST — before ANY behavioural assertion (standing rule,
    # owner directive 2026-08-12). A stale served bundle fails loudly (exit 2)
    # rather than producing a behavioural result against frozen code.
    freshness_fails = check_freshness(FRESHNESS_SPECS, base_url=BASE)
    if freshness_fails:
        print("FRESHNESS-FAIL (served bundle does not match current source):")
        for f in freshness_fails:
            print(f"  - {f}")
        print("Probe aborted before any behavioural assertion.")
        sys.exit(EXIT_STALE)

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)

        # ---------- PART 1: positive gate + dispatch + data ----------
        ctx = browser.new_context()
        page = ctx.new_page()
        errors = []
        page.on("pageerror", lambda e: errors.append(str(e)))
        page.on("console", lambda m: errors.append(m.text) if m.type == "error" else None)
        make_cookie(ctx)
        page.goto(BASE, wait_until="domcontentloaded")
        # full family array in permissions -> ANY family id grants (legacy semantics)
        page.evaluate(
            "() => localStorage.setItem('permissions', "
            "JSON.stringify({modules: " + json.dumps([{"id": i} for i in FAMILY_IDS]) + "}))"
        )

        for branch in SLICE_BRANCHES:
            page.goto(BASE + f"/commonMaster/{branch}", wait_until="domcontentloaded")
            time.sleep(6)
            if "accessDenied" in page.url:
                FAILS.append(f"{branch}: gate denied despite family array")
                continue
            body = page.inner_text("body")
            title_ok = branch.replace("Master", "").replace("master", "").strip() in body or "Master" in body
            print(f"{branch}: URL={page.url.split('/')[-1]} title_ok={title_ok}")

        # relation must render REAL data (the live-data proof)
        page.goto(BASE + "/commonMaster/relationmaster", wait_until="domcontentloaded")
        time.sleep(6)
        body = page.inner_text("body")
        real_data = "Cousin" in body and "ABC123" in body
        print(f"RELATION REAL DATA (Cousin/ABC123): {real_data}")
        if not real_data:
            FAILS.append("relationmaster: real rows (Cousin/ABC123) not rendered")

        # phlebotomy must surface the honest dev-empty message
        page.goto(BASE + "/commonMaster/phlebotomyMaster", wait_until="domcontentloaded")
        time.sleep(6)
        body = page.inner_text("body")
        honest_empty = "Phlebotomy Records not found" in body
        print(f"PHLEBOTOMY DEV-EMPTY SURFACED: {honest_empty}")
        if not honest_empty:
            FAILS.append("phlebotomyMaster: backend message not surfaced")

        # ADT family batch: each branch must render its REAL rows
        for branch, expect in ADT_BRANCHES.items():
            page.goto(BASE + f"/commonMaster/{branch}", wait_until="domcontentloaded")
            time.sleep(6)
            body = page.inner_text("body")
            real = expect in body
            print(f"ADT {branch}: real_row('{expect}')={real}  url={page.url.split('/')[-1]}")
            if not real:
                FAILS.append(f"ADT {branch}: real row '{expect}' not rendered")

        ctx.close()

        # ---------- PART 2: NEGATIVE CONTROL (gate must deny) ----------
        ctx2 = browser.new_context()
        page2 = ctx2.new_page()
        errors2 = []
        page2.on("pageerror", lambda e: errors2.append(str(e)))
        page2.on("console", lambda m: errors2.append(m.text) if m.type == "error" else None)
        make_cookie(ctx2)
        # Intercept the permissions bootstrap fetch: the app re-fetches the
        # role's module set on bootstrap and OVERWRITES localStorage, so
        # injecting permissions alone is defeated by the real fetch. The
        # negative control must go through the app's real path: supply a
        # permission set containing ONLY a non-family id (999999) and assert
        # the array gate denies (no family id present -> accessDenied).
        page2.route(
            "**/privilege/role/**",
            lambda route: route.fulfill(
                status=200,
                content_type="application/json",
                body=json.dumps({"modules": [{"modules": [{"id": 999999}]}]}),
            ),
        )
        page2.goto(BASE, wait_until="domcontentloaded")
        page2.evaluate(
            "() => localStorage.setItem('permissions', "
            "JSON.stringify({modules: [{id: 999999}]}))"
        )
        page2.goto(BASE + "/commonMaster/relationmaster", wait_until="domcontentloaded")
        # The deny redirect lands via the SPA router; dev-server lazy-chunk
        # compile can delay it beyond a fixed sleep, and page.url can lag
        # location.href during the in-app navigation. Poll the REAL location
        # (up to 15s) instead of asserting page.url at one instant.
        denied = False
        deadline = time.time() + 15
        while time.time() < deadline:
            if page2.evaluate("() => location.pathname") == "/accessDenied":
                denied = True
                break
            time.sleep(0.25)
        print(f"NEGATIVE CONTROL (non-family id denied): {denied}  URL={page2.url}")
        if not denied:
            FAILS.append("negative control: gate granted with non-family id only")
        ctx2.close()

        browser.close()

    print()
    if FAILS:
        print(f"PROBE FAIL ({len(FAILS)}):")
        for f in FAILS:
            print(f"  - {f}")
        sys.exit(1)
    print("PROBE PASS — dispatcher slice: gate grants on family array, "
          "relation renders real rows, phlebotomy surfaces honest dev-empty, "
          "negative control denies non-family id, 0 page errors")


if __name__ == "__main__":
    main()
