#!/usr/bin/env python3
"""probe_service_tariff_master.py — browser probe for screen 30
(serviceTariffMaster, module 610). Injected real-login token (stated plainly
per browser-smoke-harness). Zero backend seeding.

The screen surfaces a FINDING (the legacy primary grid endpoint
getUnitServiceTariffMasterList is broken for empty filters — IN () syntax
error swallowed by the controller's EMPTY catch into a masked HTTP-200
null-message envelope) and calls the WORKING ForWeb variant. The probe
asserts BOTH directions: the finding banner IS present and names the
mechanism (IN () + empty catch), and the working feed's honest state renders
with 0 page errors.
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
ROUTE = "/serviceTariffMaster"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 610}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(6)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        banner = "primary grid endpoint is broken" in body
        mechanism = "IN ()" in body and "catch is EMPTY" in body and "getUnitServiceTariffMasterList" in body
        print(f"FINDING BANNER: {banner}, names the mechanism: {mechanism}")
        if not (banner and mechanism):
            FAILS.append("finding banner not rendered (banner/mechanism)")

        # the working feed's honest state (dev-empty)
        feed_honest = "Tariff list" in body and ("No tariff rows" in body or "Tariff rows loaded" in body)
        print(f"WORKING FEED STATE: {feed_honest}")
        if not feed_honest:
            FAILS.append("tariff list section not rendered honestly")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, finding banner names the IN () + empty-catch mechanism, working feed honest, 0 page errors)")


if __name__ == "__main__":
    main()
