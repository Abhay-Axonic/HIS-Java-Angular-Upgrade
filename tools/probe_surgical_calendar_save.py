#!/usr/bin/env python3
"""probe_surgical_calendar_save.py — SAVE probe for screen 22.

Drives the REAL form (speciality -> doctor cascade, date range, Save), then
the caller verifies the DB and cleans up per Q28. Negative control: an
incomplete form (no date range) must be rejected client-side with ZERO save
requests on the wire.

AUTH: injected real-login token in the cookieObject cookie (stated plainly).
"""
import json
import sys
import time
import urllib.parse

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]

try:
    from playwright.sync_api import sync_playwright
except ImportError:
    print("FAIL: playwright not installed")
    sys.exit(1)

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
        time.sleep(3)

        # ---- NEGATIVE CONTROL: incomplete form (no date range) — client-side reject
        page.select_option('select[name="specialityId"]', label="General Medicine")
        time.sleep(2)  # doctor cascade fires
        page.click("button.sc-btn-primary")
        time.sleep(2)
        body = page.inner_text("body")
        blocked = "required" in body.lower() or "blocked" in body.lower()
        neg_posts = [u for u in posts]
        print(f"NEGATIVE: incomplete form rejected client-side: {blocked}, wire save posts: {len(neg_posts)}")
        if not blocked:
            FAILS.append("incomplete form NOT rejected client-side")
        if neg_posts:
            FAILS.append(f"incomplete form sent {len(neg_posts)} save request(s) on the wire")

        # ---- POSITIVE: complete form — doctor + multi-day range (the
        # single-day range is a documented legacy silent-no-op:
        # ServiceImpl:267 isBefore-strict skips same-day ranges — finding,
        # not reproduced by design)
        page.select_option('select[name="doctorId"]', index=1)
        page.fill('input[name="fromDate"]', '2026-08-10')
        page.fill('input[name="toDate"]', '2026-08-14')
        page.fill('input[name="fromTime"]', '09:00')
        page.fill('input[name="toTime"]', '17:00')
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
    print("PROBE: PASS (incomplete rejected with 0 wire posts; complete save fired exactly once)")


if __name__ == "__main__":
    main()
