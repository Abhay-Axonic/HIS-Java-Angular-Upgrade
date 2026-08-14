#!/usr/bin/env python3
"""capture_survey_session.py — one full UI login; cache (token, cookie, unit)
to tools/.survey_cookie.json for the overnight survey's probe harness."""
import json, os, sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
import get_dev_token as g
from playwright.sync_api import sync_playwright

with sync_playwright() as pw:
    browser = pw.chromium.launch(headless=True)
    page = browser.new_page()
    try:
        username, password = g.load_credentials()
        token, cookie_object, unit_id = g.login(page, username, password)
        json.dump({"token": token, "cookie": cookie_object, "unit": unit_id},
                  open(os.path.join(HERE, ".survey_cookie.json"), "w"))
        print(f"OK token={len(token)} cookie={len(cookie_object)} unit={unit_id}")
    except Exception as e:
        print("LOGIN-FAILED:", str(e)[:160])
        sys.exit(1)
    finally:
        browser.close()
