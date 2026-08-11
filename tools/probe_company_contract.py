#!/usr/bin/env python3
"""probe_company_contract.py — browser probe for screen 37
(companyContractMaster, module 608). Injected real-login token (stated
plainly per browser-smoke-harness). Zero backend seeding.

The visit-type + package-type feeds are LIVE with REAL rows (verified by
calling); the company list is dev-empty. The probe asserts BOTH directions:
the route renders and the REAL rows are visible (present); the honest
dev-empty company state renders and 0 page errors (absent).
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
ROUTE = "/companyContractMaster"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 608}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(6)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        title = "Company Contract" in body
        real_rows = "Day Care" in body and "EHC Package" in body
        honest = "No companies found" in body
        print(f"TITLE: {title}, REAL feed rows (Day Care/EHC Package): {real_rows}, honest dev-empty companies: {honest}")
        if not (title and real_rows and honest):
            FAILS.append(f"screen not rendered honestly (title: {title}, rows: {real_rows}, empty: {honest})")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, real visit-type + package-type rows visible, honest dev-empty companies, 0 page errors)")


if __name__ == "__main__":
    main()
