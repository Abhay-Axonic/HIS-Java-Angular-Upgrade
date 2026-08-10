#!/usr/bin/env python3
"""probe_notes_master.py — browser probe for screen 24 (notesMaster, module
529). Injected real-login token (stated plainly per browser-smoke-harness)
in the cookieObject cookie + permissions store seeded with moduleId 529.
Zero backend seeding.

The screen's list is PHANTOM-BROKEN (every read 500s with a correlation-id
envelope — the walk's fourth phantom finding). The screen's contract is to
SURFACE that defect honestly (the screen-17 pattern): the probe asserts the
phantom-defect banner renders with the correlation reference, and 0 page
errors. A green grid is IMPOSSIBLE here by design — the banner IS the
correct state.
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

BASE = "http://localhost:4201"
ROUTE = "/notesMaster"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 529}]}))"
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
        banner = "Backend defect surfaced" in body
        named = "columns that do not exist" in body
        ref = "ERR-" in body
        print(f"PHANTOM BANNER: {banner}, names the defect: {named}, correlation ref: {ref}")
        if not (banner and named and ref):
            FAILS.append("phantom-defect banner not rendered honestly")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, phantom-defect banner named honestly with correlation ref, 0 page errors)")


if __name__ == "__main__":
    main()
