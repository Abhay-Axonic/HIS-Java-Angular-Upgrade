#!/usr/bin/env python3
"""probe_operation_theatre_type.py — browser probe for screen 20
(operationTheatreType, module 521). Reads the real login token from the cache
and drives the REAL form in the browser via the same URL the user would use.

AUTH: injected real-login token (stated plainly per browser-smoke-harness):
the token cache from a real 8-hour login is placed into the cookieObject
cookie the SessionService reads, plus the permissions store seeded with the
moduleId (the guard's read path). Zero backend seeding.

Asserts: route renders (final URL == /operationTheatreType, no accessDenied),
the real dev rows render in the table, and 0 page errors.
"""
import json
import sys
import time
import urllib.parse

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

try:
    from playwright.sync_api import sync_playwright
except ImportError:
    print("FAIL: playwright not installed")
    sys.exit(1)

BASE = "http://localhost:4201"
ROUTE = "/operationTheatreType"
FAILS = []


def main():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        ctx = browser.new_context()
        page = ctx.new_page()
        errors = []
        page.on("pageerror", lambda e: errors.append(str(e)))
        page.on("console", lambda m: errors.append(m.text) if m.type == "error" else None)

        # seed the session the guard reads (injected state, zero backend writes)
        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.goto(BASE, wait_until="domcontentloaded")
        page.evaluate(
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 521}]}))"
        )

        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(4)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url:
            FAILS.append(f"guard denial: {final_url}")
        if ROUTE not in final_url:
            FAILS.append(f"not on route: {final_url}")

        body = page.inner_text("body")
        # the list call goes through the dev proxy; if the API returns rows they
        # render; if it 401s the honest-error toast renders. Both are assertions
        # of the screen's contract; rows are the positive signal.
        has_row = "general" in body or "test theatre" in body
        print(f"DEV ROWS RENDERED: {has_row}")
        if not has_row:
            FAILS.append("no dev rows rendered")

        # 0 page errors (console errors from the 401 XHR are expected ONLY if
        # the API is unreachable through the proxy; page errors are fatal)
        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, dev rows visible, 0 page errors)")


if __name__ == "__main__":
    main()
