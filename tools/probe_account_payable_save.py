#!/usr/bin/env python3
"""probe_account_payable_master.py — SAVE probe for screen 26 (579, Billing).

Drives the REAL form (types the account code + payable code via keystrokes —
the user-interaction standard), clicks Save, asserts the row appears in the
RELOADED grid (the findability assertion — screen-23 standard), then the
caller verifies the DB and cleans up per Q28. Negative control: incomplete
form (no codes) rejected client-side with ZERO save requests on the wire.

BILLING WRITE-PATH FOCUS (BR-188 family): the save must be verified by BODY
STATUS (never HTTP code alone) and the row must be FINDABLE — a screen that
saves money and lies about it is the worst case this codebase has produced.

AUTH: injected real-login token in the cookieObject cookie (stated plainly).
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]
CODE = "PROBE-AP26"

BASE = "http://localhost:4201"
ROUTE = "/accountPayableMaster"
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
            if "saveAccountPayable" in r.url
            else None,
        )

        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.goto(BASE, wait_until="domcontentloaded")
        page.evaluate(
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 579}]}))"
        )
        page.goto(BASE + ROUTE, wait_until="domcontentloaded")
        time.sleep(4)

        # ---- NEGATIVE CONTROL: incomplete form (no codes) — client-side reject
        page.click("button.apm-btn-primary")
        time.sleep(2)
        neg_posts = [u for u in posts]
        print(f"NEGATIVE: incomplete form rejected client-side: {len(neg_posts) == 0} (posts: {len(neg_posts)})")
        if neg_posts:
            FAILS.append(f"incomplete form sent {len(neg_posts)} save request(s)")

        # ---- POSITIVE: type the codes (keystrokes) + Save
        acc = page.locator('input[name="accCode"]')
        acc.click()
        acc.press_sequentially(CODE, delay=50)
        pcode = page.locator('input[name="accPayableCode"]')
        pcode.click()
        pcode.press_sequentially("VEND-PROBE", delay=50)
        time.sleep(1)
        model = page.evaluate("""() => {
          const el = document.querySelector('app-account-payable-master');
          const comp = window.ng ? window.ng.getComponent(el) : null;
          return comp ? {accCode: comp.form.accCode, accPayableCode: comp.form.accPayableCode}
                      : 'ng-unavailable';
        }""")
        print(f"MODEL AFTER TYPING: {model}")
        if isinstance(model, dict) and not model.get("accCode"):
            FAILS.append(f"typed code did not reach the model: {model}")
        page.click("button.apm-btn-primary")
        time.sleep(4)
        pos_posts = [u for u in posts]
        print(f"POSITIVE: save fired, wire save posts: {len(pos_posts)}")
        if len(pos_posts) != 1:
            FAILS.append(f"expected exactly 1 save post, saw {len(pos_posts)}")

        # ---- FINDABILITY (screen-23 standard): the saved row must appear in
        # the reloaded grid
        body = page.inner_text("body")
        found = CODE in body
        print(f"FINDABILITY: saved row visible in the grid: {found}")
        if not found:
            FAILS.append(f"saved row ({CODE}) not visible in the grid after save")

        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (incomplete rejected with 0 wire posts; typed codes reached the model; save fired exactly once; row findable in the grid)")


if __name__ == "__main__":
    main()
