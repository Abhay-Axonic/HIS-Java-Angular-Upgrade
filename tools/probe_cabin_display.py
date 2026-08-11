#!/usr/bin/env python3
"""probe_cabin_display.py — browser probe for screen 52 (cabinDisplay, module 760).
Injected real-login token (stated plainly per browser-smoke-harness).
Zero backend seeding.

The screen (DANGLE class — owner's BUILD ALL SEVEN decision) renders the cabin
token table and surfaces the phantom-column defect on its core feed as an
AMBER-permanent banner (the response it would read is the thing that lies).
The probe asserts: the route renders, the AMBER banner names the phantom column
(doctor_latest_call) + why-permanent, the refresh round-trip settles on the
honest masked-error outcome, and 0 page errors.
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
ROUTE = "/cabinDisplay"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 760}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(8)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        surface = "Cabin Display" in body and "Refresh" in body
        print(f"SURFACE: {surface}")
        if not surface:
            FAILS.append("cabin display surface not rendered")

        amber = "AMBER" in body and "permanent warning" in body
        mechanism = "doctor_latest_call" in body and "phantom" in body
        why_permanent = "Why this warning is permanent" in body and "the thing that lies" in body
        print(f"AMBER-PERMANENT BANNER: {amber}, names phantom column: {mechanism}, why-permanent: {why_permanent}")
        if not (amber and mechanism and why_permanent):
            FAILS.append("AMBER-permanent phantom-column banner not rendered")

        # refresh round-trip: the live endpoint returns the masked 501, surfaced honestly
        try:
            page.get_by_role("button", name="Refresh").click()
            time.sleep(5)
        except Exception as e:
            FAILS.append(f"refresh click failed: {e}")
        body2 = page.inner_text("body")
        honest = "broken by" in body2 and ("phantom column" in body2 or "error envelope" in body2)
        print(f"REFRESH ROUND-TRIP (honest masked-error outcome): {honest}")
        if not honest:
            FAILS.append("refresh round-trip did not surface the honest error outcome")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, AMBER-permanent banner names the phantom column doctor_latest_call + why-permanent, refresh surfaces the honest error, 0 page errors)")


if __name__ == "__main__":
    main()
