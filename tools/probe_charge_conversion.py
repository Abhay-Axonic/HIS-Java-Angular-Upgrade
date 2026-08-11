#!/usr/bin/env python3
"""probe_charge_conversion.py — browser probe for screen 29 (chargeConversion,
module 592). Injected real-login token (stated plainly per
browser-smoke-harness). Zero backend seeding.

The screen surfaces a FINDING (the silent partial-failure on the billing
write path — ChargeConversionServiceImpl:74-83) and renders the one
standalone feed (visit types). The probe asserts BOTH directions: the
finding banner IS present (the thing that should be present), and there are
NO page errors / the empty-or-loaded visit-type state is honest (the things
that should be absent are absent).
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
ROUTE = "/chargeConversion"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 592}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(6)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        banner = "partial success on a billing write path" in body
        mechanism = "getOrderDetailsListByOrderId" in body and "SUCCESS" in body
        print(f"FINDING BANNER: {banner}, names the mechanism: {mechanism}")
        if not (banner and mechanism):
            FAILS.append("finding banner not rendered (banner/mechanism)")

        # the visit-type feed renders honestly (loaded list OR its empty state)
        feed_honest = "Visit type" in body
        print(f"FLOW FEED PRESENT: {feed_honest}")
        if not feed_honest:
            FAILS.append("conversion flow section not rendered")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, finding banner names the partial-failure mechanism, flow feed present, 0 page errors)")


if __name__ == "__main__":
    main()
