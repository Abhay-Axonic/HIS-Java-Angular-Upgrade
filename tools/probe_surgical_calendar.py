#!/usr/bin/env python3
"""probe_surgical_calendar.py — browser probe for screen 22 (surgicalCalendar,
module 710). Injected real-login token (stated plainly per
browser-smoke-harness) in the cookieObject cookie + permissions store seeded
with moduleId 710. Zero backend seeding.

Asserts: route renders (final URL == /surgicalCalendar, no accessDenied),
form + grid state render, the speciality dropdown is LIVE-populated (2 real
dev rows — the speciality feed working), 0 page errors.
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
ROUTE = "/surgicalCalendar"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 710}]}))"
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
        has_form = "Speciality" in body and "Save" in body
        print(f"FORM RENDERED: {has_form}")
        if not has_form:
            FAILS.append("form not rendered")
        grid_ok = "No surgical calendar entries found" in body or "row(s)" in body
        print(f"GRID STATE RENDERED: {grid_ok}")
        if not grid_ok:
            FAILS.append("grid state not rendered")
        # the speciality dropdown must be live-populated from the real feed
        opts = page.eval_on_selector_all('select[name="specialityId"] option',
                                         "els => els.map(e => e.textContent.trim())")
        print(f"SPECIALITY OPTIONS ({len(opts)}): {opts[:4]}")
        if len(opts) < 2:
            FAILS.append("speciality dropdown not live-populated")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, form + grid + live speciality dropdown, 0 page errors)")


if __name__ == "__main__":
    main()
