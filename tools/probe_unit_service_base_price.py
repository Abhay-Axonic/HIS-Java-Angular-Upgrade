#!/usr/bin/env python3
"""probe_unit_service_base_price.py — browser probe for screen 28
(unitServiceBasePrice, module 585). Injected real-login token (stated plainly
per browser-smoke-harness) in the cookieObject cookie + permissions store
seeded with moduleId 585. Zero backend seeding.

The list is LIVE (2 real dev rows: serviceMasterId 3, basePrice 250.0 —
verified by calling). The probe asserts: route renders, the grid shows the
live rows (a real base price value visible), the search filters, and 0 page
errors. The save path is NOT wired on this rebuild (read-only list parity —
the update path is the primary write and carries the dup-check-scoping
question flagged for the BR).
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

BASE = "http://localhost:4201"
ROUTE = "/unitServiceBasePrice"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 585}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(5)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url:
            FAILS.append(f"guard denial: {final_url}")
        if ROUTE not in final_url:
            FAILS.append(f"not on route: {final_url}")

        body = page.inner_text("body")
        live = "250" in body and "Cardiology Consultation" in body
        print(f"LIVE LIST: base price + SERVICE NAME visible: {live}")
        if not live:
            FAILS.append("service name not visible (expected 'Cardiology Consultation')")

        # search filter — BY SERVICE NAME (the owner's interaction): type the
        # name and assert the row narrows to it
        s = page.locator('input[name="search"]')
        s.click()
        s.press_sequentially("Cardiology", delay=40)
        time.sleep(1)
        body2 = page.inner_text("body")
        by_name = "Cardiology Consultation" in body2 and "1 row(s)" in body2
        print(f"SEARCH BY SERVICE NAME: {by_name}")
        if not by_name:
            FAILS.append("search by service name did not find the row")
        # clear, then search by base price still works
        s.fill("")
        s.press_sequentially("250", delay=40)
        time.sleep(1)
        body3 = page.inner_text("body")
        by_price = "Cardiology Consultation" in body3 and "1 row(s)" in body3
        print(f"SEARCH BY BASE PRICE: {by_price}")
        if not by_price:
            FAILS.append("search by base price did not find the row")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, live base-price rows visible, search filters, 0 page errors)")


if __name__ == "__main__":
    main()
