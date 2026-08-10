#!/usr/bin/env python3
"""Capture the resource-charges save response body (debug)."""
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
            if "resourceChargeConfiguration" in r.url and "getResource" not in r.url and "UnitServiceMapper" not in r.url and not r.url.rstrip("/").endswith("/1/1"):
                try:
                    RESULT.append((r.status, r.url, r.text()))
                except Exception:  # noqa: BLE001
                    RESULT.append((r.status, r.url, "<body read failed>"))

        page.on("response", on_response)
        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.goto(BASE, wait_until="domcontentloaded")
        page.evaluate(
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 761}]}))"
        )
        page.goto(BASE + "/resourceChargesConfiguration", wait_until="domcontentloaded")
        time.sleep(4)
        page.select_option('select[name="specialityId"]', index=1)
        page.select_option('select[name="roleId"]', index=1)
        page.select_option('select[name="serviceId"]', index=1)
        time.sleep(1)
        amt = page.locator('input[name="amount"]')
        amt.click()
        amt.press_sequentially("250.00", delay=60)
        time.sleep(1)
        page.click("button.rcc-btn-primary")
        time.sleep(5)
        browser.close()

    for s, u, t in RESULT:
        print("RESP:", s, u)
        print("  BODY:", t[:250])


if __name__ == "__main__":
    main()
