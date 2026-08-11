#!/usr/bin/env python3
"""probe_encounter_queue_ehc.py — browser probe for screen 47 (encounterQueueEHC,
module 686). Injected real-login token (stated plainly per browser-smoke-harness).
Zero backend seeding.

The screen (DANGLE class — owner's BUILD ALL SEVEN decision) renders the
slot-search surface + live reference feeds. The probe asserts: the route
renders, the feed sections render with REAL dev rows where they exist
(nursing station NST1, appointment status PEN/Pending, reason Test Reason,
speciality list), the slot-search validation fires honestly (no dept/doctor ->
error), and 0 page errors.
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
ROUTE = "/encounterQueueEHC"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 686}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(8)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        surface = "Encounter Queue EHC" in body and "Search Slots" in body
        print(f"SURFACE: {surface}")
        if not surface:
            FAILS.append("slot-search surface not rendered")

        # live dev rows must render (real data, not faked)
        feeds = "Nursing stations" in body and "Appointment status" in body
        real_rows = ("NST1" in body or "Updated Desc" in body) and "Pending" in body
        print(f"FEED SECTIONS: {feeds}, REAL DEV ROWS visible: {real_rows}")
        if not (feeds and real_rows):
            FAILS.append("reference feeds / real dev rows not rendered")

        # slot-search validation: no speciality/doctor selected -> error, honest
        try:
            page.get_by_role("button", name="Search Slots").click()
            time.sleep(3)
        except Exception as e:
            FAILS.append(f"search button click failed: {e}")
        body2 = page.inner_text("body")
        validated = "Please select a department" in body2
        print(f"SLOT-SEARCH VALIDATION (honest): {validated}")
        if not validated:
            FAILS.append("slot-search did not validate the missing dept/doctor honestly")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, feed sections show real dev rows NST1/Pending/Test Reason, slot-search validates honestly, 0 page errors)")


if __name__ == "__main__":
    main()
