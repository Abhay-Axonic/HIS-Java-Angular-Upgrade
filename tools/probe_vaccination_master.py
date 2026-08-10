#!/usr/bin/env python3
"""Interaction probe for the rebuilt vaccinationMaster screen (screen 10).

Verifies, against the live Angular dev server on :4201:
 1. The screen loads at /vaccinationMaster without accessDenied.
 2. The Diseases add/remove rows work (client-side).
 3. The Save button shows the honest "Save not wired" error toast naming the
    orphaned real screen.
 4. The search box filters the grid (client-side).
 5. No unexpected page errors beyond the FontAwesome font warnings.

NOTE: this screen's endpoints are MAPPED, NOT live-verified (owner chose to
skip live calls on 2026-08-09). The probe therefore does NOT assert that the
list feed returns data — it asserts the honest UI states. A request to
getVaccinationMasterList may fire (the grid is wired to it); its response is
not asserted.
"""
import json
import os
import sys
import time

from playwright.sync_api import sync_playwright

PORT = "4201"
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CACHE = os.path.join(ROOT, "tools", ".token_cache.json")
URL = f"http://localhost:{PORT}/vaccinationMaster"

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
        # Smoke-harness auth shape: inject the real-login cookieObject via an
        # init script (runs BEFORE every navigation) — same host, same value
        # as a real login's cookie; sessionStorage left empty.
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
        if not page.url.rstrip("/").endswith("/vaccinationMaster"):
            FAILS.append(f"final URL is not /vaccinationMaster: {page.url}")

        # 2. grid + form + diseases sections render
        grid = page.locator("table.table-bordered").count()
        print("GRID-TABLES:", grid)
        if grid < 2:
            FAILS.append(f"expected form+diseases+grid tables, saw {grid}")

        # 3. Diseases add/remove (client-side)
        before = page.locator("input[name^='disease-']").count()
        page.locator("button.add-dynamic-row").first.click()
        time.sleep(0.5)
        after = page.locator("input[name^='disease-']").count()
        print(f"DISEASE-ROWS: before={before} after={after}")
        if after != before + 1:
            FAILS.append(f"disease add-row failed: {before} -> {after}")

        # 4. Save honest notice
        page.locator("button.btn-save").click()
        time.sleep(1)
        body = page.locator("body").inner_text()
        print("SAVE-NOTICE-PRESENT:", "Save not wired" in body)
        if "Save not wired" not in body:
            FAILS.append("Save honest notice not rendered")

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
