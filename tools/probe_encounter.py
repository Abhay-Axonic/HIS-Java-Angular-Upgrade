#!/usr/bin/env python3
"""probe_encounter.py — browser probe for screen 46 (encounter, module 5).
Injected real-login token (stated plainly per browser-smoke-harness). Zero
backend seeding.

The screen surfaces the list-feed header-contract defect (GET
/api/opd/getListOfEncounterMaster 500s under the rebuild's authenticated
header names — the legacy browser sent unit_id/organization_id itself) as an
AMBER-permanent banner, and renders the search surface honestly: the search
feed (POST /api/opd/getEncounterDetails) is live but routes on required
fields — with no patient context it falls through to its 501 envelope
(screen-40 lesson), which the screen surfaces as the truthful outcome. The
probe asserts both directions: the AMBER finding is present and states its
mechanism + why it is permanent; the search round-trip settles on an honest
outcome (surfaced error or empty result) and the button resets; 0 page
errors.
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

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

BASE = "http://localhost:4201"
ROUTE = "/encounter"
FAILS = []


def main():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        ctx = browser.new_context()
        page = ctx.new_page()
        errors = []
        page.on("pageerror", lambda e: errors.append(str(e)))
        page.on("console", lambda m: errors.append(m.text) if m.type == "error" else None)

        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.goto(BASE, wait_until="domcontentloaded")
        page.evaluate(
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 5}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(7)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        amber = "AMBER" in body and "permanent warning" in body
        mechanism = "getListOfEncounterMaster" in body and "500" in body
        why_permanent = "Why this warning is permanent" in body and "legacy header" in body
        print(f"AMBER-PERMANENT BANNER: {amber}, names the mechanism: {mechanism}, why-permanent stated: {why_permanent}")
        if not (amber and mechanism and why_permanent):
            FAILS.append("AMBER-permanent finding not rendered (tier/mechanism/why-permanent)")

        search = "Encounter / Patient Visit" in body and "Search" in body
        print(f"SEARCH SURFACE: {search}")
        if not search:
            FAILS.append("search surface not rendered")

        # round-trip the search: click Search. With no patient context the
        # endpoint falls through to its 501 envelope (the service routes on
        # required fields, patientId among them — the screen-40 lesson), so
        # the HONEST result is the surfaced error, not a fake empty list.
        try:
            page.get_by_role("button", name="Search").click()
            time.sleep(4)
        except Exception as e:
            FAILS.append(f"search button click failed: {e}")
        body2 = page.inner_text("body")
        honest = ("Something went wrong" in body2) or ("No encounters found" in body2)
        btn_back = "Searching…" not in body2
        print(f"SEARCH ROUND-TRIP (honest outcome): {honest}, button reset: {btn_back}")
        if not (honest and btn_back):
            FAILS.append("search round-trip did not settle on an honest outcome")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, AMBER-permanent banner names the list-feed 500 header artifact + why-permanent, search round-trips to honest dev-empty, 0 page errors)")


if __name__ == "__main__":
    main()
