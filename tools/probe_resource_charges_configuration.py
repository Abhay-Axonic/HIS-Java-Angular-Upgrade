#!/usr/bin/env python3
"""probe_resource_charges_configuration.py — browser probe for screen 23
(resourceChargesConfiguration, module 761). Injected real-login token (stated
plainly per browser-smoke-harness) in the cookieObject cookie + permissions
store seeded with moduleId 761. Zero backend seeding.

Asserts: route renders (final URL == /resourceChargesConfiguration, no
accessDenied), form + grid state render, the speciality + role dropdowns are
LIVE-populated from the real feeds, 0 page errors.
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

BASE = "http://localhost:4201"
ROUTE = "/resourceChargesConfiguration"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 761}]}))"
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
        has_form = "Speciality" in body and "Amount" in body and "Save" in body
        print(f"FORM RENDERED: {has_form}")
        if not has_form:
            FAILS.append("form not rendered")
        grid_ok = "No resource charges configured" in body or "row(s)" in body
        print(f"GRID STATE RENDERED: {grid_ok}")
        if not grid_ok:
            FAILS.append("grid state not rendered")

        spec_opts = page.eval_on_selector_all('select[name="specialityId"] option',
                                              "els => els.map(e => e.textContent.trim())")
        print(f"SPECIALITY OPTIONS ({len(spec_opts)}): {spec_opts[:4]}")
        if len(spec_opts) < 2:
            FAILS.append("speciality dropdown not live-populated")
        role_opts = page.eval_on_selector_all('select[name="roleId"] option',
                                              "els => els.map(e => e.textContent.trim())")
        print(f"ROLE OPTIONS ({len(role_opts)}): {role_opts[:4]}")
        if len(role_opts) < 2:
            FAILS.append("role dropdown not live-populated")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, form + grid + live dropdowns, 0 page errors)")


if __name__ == "__main__":
    main()
