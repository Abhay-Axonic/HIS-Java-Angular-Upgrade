#!/usr/bin/env python3
"""probe_token_display.py — browser probe for screen 51 (tokenDisplay, module 747).
Injected real-login token (stated plainly per browser-smoke-harness).
Zero backend seeding.

The screen (DANGLE class — owner's BUILD ALL SEVEN decision) renders the token
list + broadcast feeds. The probe asserts: the route renders, the token table
surface appears, the honest dev-empty state is shown, the refresh round-trip
works, and 0 page errors.
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
ROUTE = "/tokenDisplay"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 747}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(8)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        surface = "Token Display" in body and "Refresh" in body and "UHID" in body
        print(f"SURFACE: {surface}")
        if not surface:
            FAILS.append("token display surface not rendered")

        # dev-empty is the honest state on the seeded database
        low = body.lower()
        honest_empty = "no tokens for today" in low
        print(f"HONEST DEV-EMPTY STATE: {honest_empty}")
        if not honest_empty:
            FAILS.append("dev-empty token state not rendered honestly")

        # refresh round-trip
        try:
            page.get_by_role("button", name="Refresh").click()
            time.sleep(4)
        except Exception as e:
            FAILS.append(f"refresh click failed: {e}")
        body2 = page.inner_text("body")
        refresh_ok = "no tokens for today" in body2.lower()
        print(f"REFRESH ROUND-TRIP: {refresh_ok}")
        if not refresh_ok:
            FAILS.append("refresh round-trip failed")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, token table surface, honest dev-empty state, refresh round-trip works, 0 page errors)")


if __name__ == "__main__":
    main()
