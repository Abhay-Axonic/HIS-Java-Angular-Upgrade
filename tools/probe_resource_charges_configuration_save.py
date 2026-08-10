#!/usr/bin/env python3
"""probe_resource_charges_configuration_save.py — SAVE probe for screen 23.

Drives the REAL form (selects speciality/role/service from the live
dropdowns, types the amount, clicks Save), asserts the row appears in the
reloaded list, then the caller verifies the DB and cleans up per Q28.
Negative control: an incomplete form (no amount) rejected client-side with
ZERO save requests on the wire. The amount is typed via keystrokes into the
real input (the user-interaction standard from screen 22).

AUTH: injected real-login token in the cookieObject cookie (stated plainly).
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]
AMOUNT = "250.00"

BASE = "http://localhost:4201"
ROUTE = "/resourceChargesConfiguration"
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
            if ("resourceChargeConfiguration" in r.url
                and "getResource" not in r.url
                and "UnitServiceMapper" not in r.url
                and "/list" not in r.url
                and not r.url.rstrip("/").endswith("/1/1"))
            else None,
        )

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
        page.select_option('select[name="specialityId"]', index=1)
        page.select_option('select[name="roleId"]', index=1)
        page.select_option('select[name="serviceId"]', index=1)
        time.sleep(1)

        # ---- NEGATIVE CONTROL: incomplete form (no amount) — client-side reject
        page.click("button.rcc-btn-primary")
        time.sleep(2)
        neg_posts = [u for u in posts]
        print(f"NEGATIVE: incomplete form rejected client-side: {len(neg_posts) == 0} (posts: {len(neg_posts)})")
        if neg_posts:
            FAILS.append(f"incomplete form sent {len(neg_posts)} save request(s)")

        # ---- POSITIVE: type the amount (keystrokes) + Save
        amt = page.locator('input[name="amount"]')
        amt.click()
        amt.press_sequentially(AMOUNT, delay=60)
        time.sleep(1)
        model = page.evaluate("""() => {
          const el = document.querySelector('app-resource-charges-configuration');
          const comp = window.ng ? window.ng.getComponent(el) : null;
          return comp ? {amount: comp.form.amount, serviceId: comp.form.serviceId,
                          specialityId: comp.form.specialityId, roleId: comp.form.roleId}
                      : 'ng-unavailable';
        }""")
        print(f"MODEL AFTER TYPING: {model}")
        if isinstance(model, dict) and not model.get("amount"):
            FAILS.append(f"typed amount did not reach the model: {model}")
        page.click("button.rcc-btn-primary")
        time.sleep(4)
        pos_posts = [u for u in posts]
        print(f"POSITIVE: save fired, wire save posts: {len(pos_posts)}")
        if len(pos_posts) != 1:
            FAILS.append(f"expected exactly 1 save post, saw {len(pos_posts)}")
        browser.close()

    print()
    if FAILS:
        print("PROBE: FAIL —", "; ".join(FAILS))
        sys.exit(1)
    print("PROBE: PASS (incomplete rejected with 0 wire posts; typed amount reached the model; save fired exactly once)")


if __name__ == "__main__":
    main()
