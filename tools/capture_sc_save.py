#!/usr/bin/env python3
"""Capture the surgical-calendar save response body (debug)."""
import json
import time
import urllib.parse

from playwright.sync_api import sync_playwright

TOKEN = json.load(open("tools/.token_cache.json"))["token"]
BASE = "http://localhost:4201"
RESULT = []


def main():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        ctx = browser.new_context()
        page = ctx.new_page()

        def on_response(r):
            if "surgicalCalender" in r.url and "list" not in r.url:
                try:
                    RESULT.append((r.status, r.url, r.text()))
                except Exception as e:  # noqa: BLE001
                    RESULT.append((r.status, r.url, f"<body read failed: {e}>"))

        page.on("response", on_response)
        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.goto(BASE, wait_until="domcontentloaded")
        page.evaluate(
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 710}]}))"
        )
        page.goto(BASE + "/surgicalCalendar", wait_until="domcontentloaded")
        time.sleep(4)
        page.select_option('select[name="specialityId"]', label="General Medicine")
        time.sleep(3)
        page.select_option('select[name="doctorId"]', index=1)
        page.fill('input[name="fromDate"]', '2026-08-11')
        page.fill('input[name="toDate"]', '2026-08-11')
        page.fill('input[name="fromTime"]', '09:00')
        page.fill('input[name="toTime"]', '17:00')
        page.click("button.sc-btn-primary")
        time.sleep(5)
        browser.close()

    for s, u, t in RESULT:
        print("SAVE RESP:", s, "BODY:", t[:300])


if __name__ == "__main__":
    main()
