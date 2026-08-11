#!/usr/bin/env python3
"""probe_counter_worklist.py — browser probe for screen 49 (counterWorklist,
module 701). Injected real-login token (stated plainly per browser-smoke-harness).
Zero backend seeding.

The screen (DANGLE class — owner's BUILD ALL SEVEN decision) renders the
date-range search surface + nursing station feed, and surfaces the phantom-column
defect on the search feed as an AMBER-permanent banner (the response it would
read is the thing that lies). The probe asserts: the route renders, the AMBER
banner is present and names the mechanism (latest_call / nursing_station_id) +
why-permanent, the nursing station feed shows a real dev row (NST1), the search
round-trip settles on the honest masked-error outcome, and 0 page errors.
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
ROUTE = "/counterWorklist"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 701}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(8)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        surface = "Counter Worklist" in body and "Search" in body
        print(f"SURFACE: {surface}")
        if not surface:
            FAILS.append("counter worklist surface not rendered")

        amber = "AMBER" in body and "permanent warning" in body
        mechanism = "latest_call" in body and "nursing_station_id" in body and "phantom" in body
        why_permanent = "Why this warning is permanent" in body and "the thing that lies" in body
        print(f"AMBER-PERMANENT BANNER: {amber}, names phantom columns: {mechanism}, why-permanent: {why_permanent}")
        if not (amber and mechanism and why_permanent):
            FAILS.append("AMBER-permanent phantom-column banner not rendered")

        feed = "Nursing stations" in body and "NST1" in body
        print(f"NURSING STATION FEED (real row): {feed}")
        if not feed:
            FAILS.append("nursing station feed / real dev row NST1 not rendered")

        # search round-trip: the live endpoint returns the masked error envelope,
        # which the screen surfaces honestly
        try:
            page.get_by_role("button", name="Search").click()
            time.sleep(5)
        except Exception as e:
            FAILS.append(f"search button click failed: {e}")
        body2 = page.inner_text("body")
        honest = "broken by phantom columns" in body2 or "Search returned an error envelope" in body2
        print(f"SEARCH ROUND-TRIP (honest masked-error outcome): {honest}")
        if not honest:
            FAILS.append("search round-trip did not surface the honest error outcome")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, AMBER-permanent banner names the phantom columns latest_call/nursing_station_id + why-permanent, nursing station feed shows NST1, search surfaces the honest error, 0 page errors)")


if __name__ == "__main__":
    main()
