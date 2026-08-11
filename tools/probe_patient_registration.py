#!/usr/bin/env python3
"""probe_patient_registration.py — browser probe for screen 40
(patientRegistration, module 3). Injected real-login token (stated plainly
per browser-smoke-harness). Zero backend seeding.

The search endpoint is LIVE (handler responds; the empty-criteria path
returns the 501 error envelope — honest, verified by calling). The probe
asserts BOTH directions: the route renders and the search surface is
present (present); the honest error state is shown after a search (the
backend's envelope is displayed, no fabricated rows) and 0 page errors
(absent).
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
ROUTE = "/patientRegistration"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 3}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(6)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        title = "Patient Registration" in body
        surface = "Patient search" in body and "Search" in body and "Clear" in body
        inputs = page.locator("input").count() >= 3
        print(f"TITLE: {title}, SEARCH SURFACE: {surface}, INPUTS: {inputs}")
        if not (title and surface and inputs):
            FAILS.append(f"screen not rendered (title: {title}, surface: {surface}, inputs: {inputs})")

        # click Search with empty criteria -> the backend's 501 envelope ->
        # the honest error state
        page.click("text=Search")
        time.sleep(5)
        body2 = page.inner_text("body")
        honest = "error envelope" in body2 or "501" in body2 or "backend's error envelope" in body2
        print(f"HONEST ERROR STATE after search: {honest}")
        if not honest:
            FAILS.append("honest error state not shown after the backend 501")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, search surface present, honest error state shown on the backend 501, 0 page errors)")


if __name__ == "__main__":
    main()
