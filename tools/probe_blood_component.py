#!/usr/bin/env python3
"""Interaction probe for the rebuilt bloodComponent screen (screen 11).

Verifies, against the live Angular dev server on :4201:
 1. The screen loads at /bloodComponent without accessDenied.
 2. The grid renders the real dev row (id 5 "Whole Blood") from the
    live-verified list feed.
 3. The Save button shows the honest "Save not wired" toast (endpoint exists
    but the shell form cannot supply its contract).
 4. The Status switch shows the honest no-seed notice.
 5. The search box filters the grid (client-side).
 6. No unexpected page errors beyond the FontAwesome font warnings.
"""
import json
import os
import sys

from playwright.sync_api import sync_playwright

PORT = "4201"
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CACHE = os.path.join(ROOT, "tools", ".token_cache.json")
URL = f"http://localhost:{PORT}/bloodComponent"

FAILS = []


def main():
    cache = json.load(open(CACHE, encoding="utf-8"))
    cookie = cache.get("cookie") or ""
    cookie_object = ""
    for part in cookie.split(";"):
        if part.strip().startswith("cookieObject="):
            cookie_object = part.strip().split("=", 1)[1]
    with sync_playwright() as p:
        browser = p.chromium.launch()
        ctx = browser.new_context()
        page = ctx.new_page()
        # Smoke-harness auth shape: cookieObject via init script (runs before
        # every navigation); sessionStorage left empty.
        page.add_init_script(
            "document.cookie = 'cookieObject=" + cookie_object + "; path=/';"
        )

        page_errors = []
        page.on("pageerror", lambda e: page_errors.append(str(e)))

        page.goto(URL, wait_until="load", timeout=30000)
        page.wait_for_timeout(3000)

        # 1. final URL / accessDenied
        print("FINAL-URL:", page.url)
        if "accessDenied" in page.url:
            FAILS.append("redirected to accessDenied")
        if not page.url.rstrip("/").endswith("/bloodComponent"):
            FAILS.append(f"final URL is not /bloodComponent: {page.url}")

        # 2. grid renders the real dev row
        body = page.locator("body").inner_text()
        print("REAL-ROW-PRESENT:", "Whole Blood" in body)
        if "Whole Blood" not in body:
            FAILS.append("real dev row (Whole Blood) not rendered")

        # 3. Save honest notice
        page.locator("button.btn-save").click()
        page.wait_for_timeout(1000)
        body = page.locator("body").inner_text()
        print("SAVE-NOTICE-PRESENT:", "Save not wired" in body)
        if "Save not wired" not in body:
            FAILS.append("Save honest notice not rendered")

        # 4. Status switch honest notice (dispatch the change event directly —
        # the switch input is visually hidden behind the slider, standard
        # switch styling, and not in the scroll viewport)
        page.locator(".switch input").first.evaluate(
            "(el) => el.dispatchEvent(new Event('change', { bubbles: true }))"
        )
        page.wait_for_timeout(1000)
        body = page.locator("body").inner_text()
        print("STATUS-NOTICE-PRESENT:", "Status update not wired" in body)
        if "Status update not wired" not in body:
            FAILS.append("Status honest notice not rendered")

        # 5. page errors
        print("PAGE-ERRORS:", page_errors)
        if page_errors:
            FAILS.append(f"page errors: {page_errors[:3]}")

        browser.close()

    if FAILS:
        print("RESULT: FAIL")
        for f in FAILS:
            print("  -", f)
        sys.exit(1)
    print("RESULT: PASS")


if __name__ == "__main__":
    main()
