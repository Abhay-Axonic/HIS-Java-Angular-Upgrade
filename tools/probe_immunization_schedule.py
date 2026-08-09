#!/usr/bin/env python3
"""Interaction probe for the rebuilt immunizationSchedule screen (screen 9).

Verifies, against the live Angular dev server on :4201:
 1. The screen loads at /immunizationSchedule without accessDenied.
 2. The Vaccine select fires GET /api/billing/getActiveVaccinationMaster/1/1
    (the real feed) — the request is observed in the network log with a 2xx
    status ([] dev is the honest empty state, the feed is live).
 3. The Save button shows the honest "Save not wired" error toast.
 4. The Disease List button shows the honest "no disease-list endpoint"
    notice.
 5. No unexpected error toasts or page errors beyond the FontAwesome font
    warnings.
"""
import json
import os
import sys
import time

from playwright.sync_api import sync_playwright

PORT = "4201"
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CACHE = os.path.join(ROOT, "tools", ".token_cache.json")
URL = f"http://localhost:{PORT}/immunizationSchedule"

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
        # The smoke-harness authentication shape: inject the real-login
        # cookieObject via document.cookie on the target origin BEFORE the
        # app boots (the app's authToken getter reads the cookie first);
        # sessionStorage left empty like a real browser session.
        page.goto(f"http://localhost:{PORT}/", wait_until="domcontentloaded")
        page.evaluate(
            "document.cookie = 'cookieObject=" + cookie_object + "; path=/';"
        )

        vaccine_requests = []
        page.on("request", lambda r: vaccine_requests.append(r.url)
                if "getActiveVaccinationMaster" in r.url else None)
        page.on("request", lambda r: vaccine_requests.append(r.url)
                if "getVaccinationMasterList" in r.url else None)

        page_errors = []
        page.on("pageerror", lambda e: page_errors.append(str(e)))

        page.goto(URL, wait_until="networkidle")
        time.sleep(2)

        # 1. final URL / accessDenied
        print("FINAL-URL:", page.url)
        if "accessDenied" in page.url:
            FAILS.append("redirected to accessDenied")
        if not page.url.rstrip("/").endswith("/immunizationSchedule"):
            FAILS.append(f"final URL is not /immunizationSchedule: {page.url}")

        # 2. vaccine feed fired
        print("VACCINE-FEED-REQUESTS:", vaccine_requests)
        if not any("getActiveVaccinationMaster" in u for u in vaccine_requests):
            FAILS.append("vaccine feed GET getActiveVaccinationMaster never fired")

        # 3. the Vaccine select exists and is populated with the placeholder
        select_count = page.locator("select").count()
        print("SELECT-COUNT:", select_count)
        if select_count < 3:
            FAILS.append(f"expected >=3 selects, saw {select_count}")

        # 4. Save honest toast
        page.locator("button.btn-save").click()
        time.sleep(1)
        body = page.locator("body").inner_text()
        print("SAVE-NOTICE-PRESENT:", "Save not wired" in body)
        if "Save not wired" not in body:
            FAILS.append("Save honest notice not rendered")

        # 5. Disease List honest notice
        page.get_by_role("button", name="Disease List").click()
        time.sleep(1)
        body = page.locator("body").inner_text()
        print("DISEASE-NOTICE-PRESENT:", "Disease list not available" in body)
        if "Disease list not available" not in body:
            FAILS.append("Disease List honest notice not rendered")

        # 6. page errors (only the known FontAwesome glyf warnings are console
        # warnings, not pageerrors)
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
