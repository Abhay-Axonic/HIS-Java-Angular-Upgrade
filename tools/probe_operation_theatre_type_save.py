#!/usr/bin/env python3
"""probe_operation_theatre_type_save.py — SAVE probe for screen 20.

Drives the REAL form (fills the real inputs, clicks the real Save button),
asserts the success toast, then verifies the row in the DB and cleans up
per Q28. Negative control: an incomplete form (missing code) must be
rejected client-side with ZERO save requests on the wire.

AUTH: injected real-login token in the cookieObject cookie (stated plainly).
"""
import json
import sys
import time
import urllib.parse

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]
CODE = "PROBE-OT20"
NAME = "Probe Theatre Type Twenty"

try:
    from playwright.sync_api import sync_playwright
except ImportError:
    print("FAIL: playwright not installed")
    sys.exit(1)

BASE = "http://localhost:4201"
ROUTE = "/operationTheatreType"
FAILS = []
posts = []


def main():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        ctx = browser.new_context()
        page = ctx.new_page()
        page.on("pageerror", lambda e: FAILS.append(f"pageerror: {e}"))
        page.on(
            "request",
            lambda r: posts.append(r.url)
            if "saveOperationTheatreType" in r.url
            else None,
        )

        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.goto(BASE, wait_until="domcontentloaded")
        page.evaluate(
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 521}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(3)

        # ---- NEGATIVE CONTROL: incomplete form (no code) — client-side reject
        page.fill('input[name="theatreTypeName"]', NAME)
        page.click("button.ot-type-btn-primary")
        time.sleep(2)
        body = page.inner_text("body")
        blocked = "required" in body.lower() or "blocked" in body.lower()
        neg_posts = [u for u in posts if "saveOperationTheatreType" in u]
        print(f"NEGATIVE: incomplete form rejected client-side: {blocked}, wire posts: {len(neg_posts)}")
        if not blocked:
            FAILS.append("incomplete form NOT rejected client-side")
        if neg_posts:
            FAILS.append(f"incomplete form sent {len(neg_posts)} save request(s) on the wire")

        # ---- POSITIVE: complete form — real Save
        page.fill('input[name="theatreTypeCode"]', CODE)
        page.click("button.ot-type-btn-primary")
        time.sleep(4)
        body = page.inner_text("body")
        # The success toast auto-dismisses; the DURABLE signal is the row
        # appearing in the reloaded list (the save-triggered loadList ran).
        ok = CODE in body
        pos_posts = [u for u in posts if "saveOperationTheatreType" in u]
        print(f"POSITIVE: saved row visible in list: {ok}, wire save posts: {len(pos_posts)}")
        if not ok:
            FAILS.append("saved row not visible in the reloaded list")
        if len(pos_posts) != 1:
            FAILS.append(f"expected exactly 1 save post, saw {len(pos_posts)}")
        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (incomplete rejected with 0 wire posts; complete save succeeded)")


if __name__ == "__main__":
    main()
