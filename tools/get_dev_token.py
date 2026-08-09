#!/usr/bin/env python3
"""
get_dev_token.py — perform ONE full UI login against the legacy app and print
ONLY the session token to stdout. Exits non-zero on failure.

Credentials: env vars HIS_DEV_USERNAME / HIS_DEV_PASSWORD, or an UNTRACKED
local file tools/.dev_credentials (lines: username=... / password=...).
Never hardcoded, never committed.

Flags:
  --check   print only whether login succeeded (no token), exit 0/1.
  --endpoint URL   after login, call an authenticated endpoint with the full
                   header set (Authorization Bearer + userid/unitid headers +
                   the cookieObject cookie) and print the HTTP status; used to
                   PROVE the token works (a token-only call 401s).

Rate limiting is live: exactly ONE login attempt, no retry loops.
"""
import argparse
import json
import os
import sys

from playwright.sync_api import sync_playwright

LEGACY = 'http://localhost:8080'


def load_credentials():
    username = os.environ.get('HIS_DEV_USERNAME')
    password = os.environ.get('HIS_DEV_PASSWORD')
    if not username or not password:
        here = os.path.dirname(os.path.abspath(__file__))
        cred = os.path.join(here, '.dev_credentials')
        if os.path.exists(cred):
            for line in open(cred, encoding='utf-8'):
                line = line.strip()
                if line.startswith('username='):
                    username = line.split('=', 1)[1]
                elif line.startswith('password='):
                    password = line.split('=', 1)[1]
    if not username or not password:
        sys.stderr.write('NO-CREDENTIALS: set HIS_DEV_USERNAME/HIS_DEV_PASSWORD '
                         'or create tools/.dev_credentials (untracked)\n')
        sys.exit(2)
    return username, password


def login(page, username, password):
    """One login attempt. Returns (token, cookie_object, unit_id) or raises."""
    page.goto(f'{LEGACY}/index.html', wait_until='load', timeout=30000)
    page.wait_for_timeout(1500)
    page.fill('#userName', username)
    page.fill('#password', password)
    page.locator('#password').blur()
    page.wait_for_function(
        "() => document.querySelectorAll('#organizationId option').length > 1",
        timeout=10000,
    )
    page.select_option('#organizationId', '1')
    try:
        page.wait_for_function(
            "() => document.querySelectorAll('#unitId option').length > 1",
            timeout=15000,
        )
    except Exception:  # noqa: BLE001
        raise RuntimeError('UNIT-DROPDOWN-NEVER-POPULATED')
    unit_vals = page.eval_on_selector_all('#unitId option', 'os => os.map(o => o.value)')
    if len(unit_vals) < 2:
        # the dropdown re-populates after the wait (a mid-update race); re-wait within
        # this SAME attempt, then re-read — no second login, no retry loop.
        page.wait_for_timeout(2000)
        unit_vals = page.eval_on_selector_all('#unitId option', 'os => os.map(o => o.value)')
    if len(unit_vals) < 2:
        raise RuntimeError('UNIT-DROPDOWN-EMPTY')
    page.select_option('#unitId', unit_vals[1])
    page.click('input[type="submit"]')
    page.wait_for_timeout(1500)
    confirm_ok = page.locator('button:has-text("Ok")').first
    try:
        if confirm_ok.is_visible():
            confirm_ok.click()
    except Exception:  # noqa: BLE001
        pass
    try:
        page.wait_for_function(
            "() => !!window.sessionStorage.getItem('token')",
            timeout=20000,
        )
    except Exception:  # noqa: BLE001
        raise RuntimeError('NO-TOKEN')
    value = page.evaluate("sessionStorage.getItem('token')")
    cookie = page.evaluate('document.cookie')
    unit_id = unit_vals[1]
    return value, cookie, unit_id


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--check', action='store_true')
    ap.add_argument('--endpoint')
    args = ap.parse_args()
    username, password = load_credentials()
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()
        try:
            token, cookie, unit_id = login(page, username, password)
        except Exception as exc:  # noqa: BLE001
            sys.stderr.write(f'{exc}\n')
            browser.close()
            sys.exit(1)
        browser.close()
    if args.check:
        print('LOGIN-OK')
        sys.exit(0)
    if args.endpoint:
        import urllib.request
        req = urllib.request.Request(
            args.endpoint,
            headers={
                'Authorization': f'Bearer {token}',
                'userid': '1',
                'unitid': unit_id,
                'Cookie': cookie,
            },
        )
        try:
            with urllib.request.urlopen(req, timeout=10) as resp:
                print(resp.status)
        except urllib.error.HTTPError as exc:
            print(exc.code)
            sys.exit(3)
        sys.exit(0)
    print(token)
    sys.exit(0)


if __name__ == '__main__':
    main()
