#!/usr/bin/env python3
"""probe_self_registration.py — browser probe for screen 48 (selfRegistration,
module 703). Injected real-login token (stated plainly per browser-smoke-harness).
Zero backend seeding.

The screen (DANGLE class — owner's BUILD ALL SEVEN decision) renders the
self-registration form + live reference feeds. The probe asserts: the route
renders, the form surface renders, REAL dev rows appear (country India/IN,
gender rows, prefix Mr.), and 0 page errors. The payment save path is
documented, not exercised.
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
ROUTE = "/selfRegistration"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 703}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(8)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        surface = "Self Registration" in body and "First name" in body and "Marital status" in body
        print(f"FORM SURFACE: {surface}")
        if not surface:
            FAILS.append("self-registration form surface not rendered")

        # real dev rows must be visible through the legacy-header GET feeds
        real_rows = "India" in body and "Mr." in body
        feeds = "Reference feeds" in body and "Countries" in body and "Genders" in body
        print(f"REFERENCE FEEDS: {feeds}, REAL DEV ROWS: {real_rows}")
        if not (feeds and real_rows):
            FAILS.append("reference feeds / real dev rows (India, Mr.) not rendered")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, form surface renders, reference feeds show real dev rows India/Mr., 0 page errors)")


if __name__ == "__main__":
    main()
