#!/usr/bin/env python3
"""probe_procedure_type.py — both-directions search probe for screen 21
(procedureType, module 520). Injected real-login token (stated plainly per
browser-smoke-harness). Zero backend seeding; the dev row is real
(7|GNRL|GENERAL PROCEDURE|DESCP123, otims.m_procedure_type_master).

BOTH DIRECTIONS (the QF-AN standing requirement): a matching term ('GENERAL')
must INCLUDE the row; a non-matching term ('abc') must EXCLUDE it with the
counter reading '0 row(s)'; clearing must restore the row.
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
ROUTE = "/procedureType"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 520}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(5)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        row_present = "GENERAL PROCEDURE" in body
        print(f"DEV ROW VISIBLE (positive baseline): {row_present}")
        if not row_present:
            FAILS.append("dev row GENERAL PROCEDURE not visible initially")

        # POSITIVE: matching term includes the row
        s = page.locator('input.pt-search')
        s.click()
        s.press_sequentially("GENERAL", delay=40)
        time.sleep(1)
        body2 = page.inner_text("body")
        pos = "GENERAL PROCEDURE" in body2 and "1 row(s)" in body2
        print(f"POSITIVE (GENERAL includes): {pos}")
        if not pos:
            FAILS.append("matching term did not include the row")

        # NEGATIVE: non-matching term excludes + counter zero
        s.fill("")
        s.press_sequentially("abc", delay=40)
        time.sleep(1)
        body3 = page.inner_text("body")
        neg = "GENERAL PROCEDURE" not in body3 and "0 row(s)" in body3
        print(f"NEGATIVE (abc excludes, counter 0): {neg}")
        if not neg:
            FAILS.append("non-matching term did not exclude the row (counter/row visible)")

        # CLEAR restores
        s.fill("")
        time.sleep(1)
        body4 = page.inner_text("body")
        restored = "GENERAL PROCEDURE" in body4
        print(f"CLEAR RESTORES: {restored}")
        if not restored:
            FAILS.append("clearing did not restore the row")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, dev row visible, matching term includes, 'abc' excludes with counter 0, clear restores, 0 page errors)")


if __name__ == "__main__":
    main()
