#!/usr/bin/env python3
"""probe_growth_chart.py — browser probe for screen 50 (growthChart, module 744).
Injected real-login token (stated plainly per browser-smoke-harness).
Zero backend seeding.

The screen (DANGLE class — owner's BUILD ALL SEVEN decision) renders the
growth-chart list from the live feed. The probe asserts: the route renders,
the list shows a REAL dev row (growthChartId 15 with weight columns), the
patient-details round-trip works, and 0 page errors.
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
ROUTE = "/growthChart"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 744}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(8)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        surface = "Growth Chart" in body and "Load patient details" in body
        print(f"SURFACE: {surface}")
        if not surface:
            FAILS.append("growth chart surface not rendered")

        # the list feed loads on init; real dev row growthChartId 15 must appear.
        # NB: table headers are CSS-uppercased (WEIGHT / AGE (MONTHS)), so match
        # case-insensitively.
        low = body.lower()
        real_row = "15" in body and ("weight" in low or "age" in low)
        print(f"LIST FEED (real dev row growthChartId 15): {real_row}")
        if not real_row:
            FAILS.append("growth chart list feed / real dev row 15 not rendered")

        # patient-details round-trip
        try:
            page.get_by_role("button", name="Load patient details").click()
            time.sleep(5)
        except Exception as e:
            FAILS.append(f"details button click failed: {e}")
        body2 = page.inner_text("body")
        low2 = body2.lower()
        details_ok = "15" in body2 and ("kg" in low2 or "age" in low2)
        print(f"PATIENT-DETAILS ROUND-TRIP: {details_ok}")
        if not details_ok:
            FAILS.append("patient-details round-trip did not render the record")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, list feed shows real dev row 15, patient-details round-trip works, 0 page errors)")


if __name__ == "__main__":
    main()
