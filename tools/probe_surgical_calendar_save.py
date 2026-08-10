#!/usr/bin/env python3
"""probe_surgical_calendar_save.py — SAVE probe for screen 22 (v2, the
user-interaction fix).

The owner's click-through found the probe-vs-user class: the probe's
page.fill() on native <input type="time"> set the value property directly
(bypassing the browser's segmented HH:MM:-- commit), while a user's typed
keystrokes never reached the model — the inputs DISPLAYED 09:00/12:00 with a
trailing '--' but the DOM value stayed empty (captured: fromTime_dom "",
model fromTime ""). The inputs are now type="text" (matching the legacy
template) and THIS PROBE TYPES KEYSTROKES into them exactly as a user does
(press_sequentially), asserting the typed value reaches the model before
Save fires. This is the interaction the fix must survive.

Negative controls: (1) incomplete form (no date range) rejected client-side
with 0 wire posts; (2) malformed time (not HH:MM) rejected client-side with 0
wire posts. Positive: typed HH:MM times -> exactly 1 save post.

AUTH: injected real-login token in the cookieObject cookie (stated plainly).
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

BASE = "http://localhost:4201"
ROUTE = "/surgicalCalendar"
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
            if "surgicalCalender" in r.url and "list" not in r.url
            else None,
        )

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
        page.select_option('select[name="specialityId"]', label="General Medicine")
        time.sleep(3)
        page.select_option('select[name="doctorId"]', index=1)
        page.fill('input[name="fromDate"]', '2026-08-02')
        page.fill('input[name="toDate"]', '2026-08-09')

        # ---- NEGATIVE CONTROL 1: no times -> client-side reject, 0 posts
        page.click("button.sc-btn-primary")
        time.sleep(2)
        neg1_posts = [u for u in posts]
        print(f"NEG1: missing times rejected: {len(neg1_posts) == 0} (posts: {len(neg1_posts)})")
        if neg1_posts:
            FAILS.append(f"missing-times form sent {len(neg1_posts)} save request(s)")

        # ---- NEGATIVE CONTROL 2: malformed time (not HH:MM) -> reject, 0 posts
        ft = page.locator('input[name="fromTime"]')
        ft.click()
        ft.press_sequentially("900", delay=60)  # user types "900", not "09:00"
        page.click("button.sc-btn-primary")
        time.sleep(2)
        neg2_posts = [u for u in posts]
        print(f"NEG2: malformed time rejected: {len(neg2_posts) == 0} (posts: {len(neg2_posts)})")
        if neg2_posts:
            FAILS.append(f"malformed-time form sent {len(neg2_posts)} save request(s)")

        # ---- POSITIVE: TYPE times like a user (keystrokes), assert the model
        # receives them, then Save
        ft.fill("")  # clear the malformed value
        ft.click()
        ft.press_sequentially("09:00", delay=80)
        tt = page.locator('input[name="toTime"]')
        tt.click()
        tt.press_sequentially("12:00", delay=80)
        time.sleep(1)
        model = page.evaluate("""() => {
          const el = document.querySelector('app-surgical-calendar');
          const comp = window.ng ? window.ng.getComponent(el) : null;
          return comp ? {fromTime: comp.form.fromTime, toTime: comp.form.toTime}
                      : {fromTime: 'ng-unavailable', toTime: 'ng-unavailable'};
        }""")
        print(f"MODEL AFTER TYPING: {model}")
        if model.get("fromTime") != "09:00" or model.get("toTime") != "12:00":
            FAILS.append(f"typed times did not reach the model: {model}")
        page.click("button.sc-btn-primary")
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
    print("PROBE: PASS (missing/malformed times rejected with 0 posts; typed HH:MM reached the model; save fired exactly once)")


if __name__ == "__main__":
    main()
