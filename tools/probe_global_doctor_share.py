#!/usr/bin/env python3
"""probe_global_doctor_share.py — browser probe for screen 32
(globalDoctorShare, module 613). Injected real-login token (stated plainly
per browser-smoke-harness). Zero backend seeding.

The screen surfaces a FINDING (the batch-partial-commit shape on the
doctor-share write path) and renders the live dev-empty grid + grades feed
(verified by calling). The probe asserts BOTH directions: the finding banner
IS present and names the mechanism (batch/commit/no rollback); the honest
dev-empty list renders; 0 page errors.
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
ROUTE = "/globalDoctorShare"
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
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 613}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(6)
        final_url = page.url
        print(f"FINAL URL: {final_url}")
        if "accessDenied" in final_url or ROUTE not in final_url:
            FAILS.append(f"route not reached: {final_url}")

        body = page.inner_text("body")
        banner = "batch-partial-commit" in body
        amber = "AMBER" in body and "permanent warning" in body
        mechanism = "executeBatch" in body and "rollback" in body
        why_permanent = "Why this warning is permanent" in body and "cannot be triggered in dev" in body
        print(f"FINDING BANNER: {banner}, AMBER-permanent tier: {amber}, names the mechanism: {mechanism}, why-permanent stated: {why_permanent}")
        if not (amber and banner and mechanism and why_permanent):
            FAILS.append("finding banner not rendered (tier/banner/mechanism/why-permanent)")

        honest = "No global doctor share rows found" in body
        print(f"HONEST DEV-EMPTY LIST: {honest}")
        if not honest:
            FAILS.append("dev-empty list state not rendered")

        page_errors = [e for e in errors if not e.startswith("Failed to load resource")]
        print(f"PAGE ERRORS: {page_errors if page_errors else 'none'}")
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:2]}")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (route renders, finding banner names the batch-partial-commit mechanism, honest dev-empty list, 0 page errors)")


if __name__ == "__main__":
    main()
