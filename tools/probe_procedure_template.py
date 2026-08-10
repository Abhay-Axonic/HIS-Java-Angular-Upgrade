#!/usr/bin/env python3
"""probe_procedure_template.py — browser probe for screen 25 (procedureTemplate,
module 515). Injected real-login token (stated plainly per browser-smoke-harness)
in the cookieObject cookie + permissions store seeded with moduleId 515.
Zero backend seeding.

The screen's config-list returns 200 [] (dev-empty — the parent list works),
but the config-TEMPLATE core is PHANTOM-BROKEN (the walk's SIXTH phantom:
role_desc in public.m_role_master — every getProcedureConfigTemplate and
saveProcedureConfigTemplate 500s loudly with a correlation-id envelope). The
screen's contract is to surface the empty-list state honestly; the probe
asserts the route renders, the honest empty-state OR the named-phantom banner
(with correlation ref), and 0 page errors.
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

BASE = "http://localhost:4201"
ROUTE = "/procedureTemplate"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 515}]}))"
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
        # the list is dev-empty: the honest empty-state must render. If the
        # phantom core is reached (a template fetch), the named banner must show.
        empty = "list is empty" in body or "loaded successfully" in body
        named_phantom = "role_desc" in body and "does not exist" in body
        print(f"HONEST EMPTY STATE: {empty}, NAMED PHANTOM BANNER: {named_phantom}")
        if not (empty or named_phantom):
            FAILS.append("neither the honest empty state nor the named phantom banner rendered")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, honest state rendered, 0 page errors)")


if __name__ == "__main__":
    main()
