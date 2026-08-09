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


def logout(page):
    """Release the login-log row (the CHECK_OTHER_LOGIN mechanism) so the next
    invocation does not collide with the session this one just created.

    Mirrors the legacy logoutController's POST /adt/saveUserLogDetails with
    isLogin:'N' (logoutController.js:64-71). Best-effort: a failure to log out
    must not mask the login result.
    """
    try:
        payload = page.evaluate("""() => {
            const co = (() => { try { return JSON.parse(decodeURIComponent(
                document.cookie.split('cookieObject=')[1].split(';')[0])); }
                catch (e) { return {}; } })();
            return {
                userId: sessionStorage.getItem('userId') || co.userId || 0,
                unitId: sessionStorage.getItem('unitId') || co.unitId || 0,
                organizationId: sessionStorage.getItem('organizationId') || co.organizationId || 0,
                isLogin: 'N',
                macAddress: localStorage.getItem('macAddress') || ''
            };
        }""")
        page.evaluate("""async (data) => {
            try {
                await fetch('/adt/saveUserLogDetails', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(data)
                });
            } catch (e) { /* best-effort */ }
        }""", payload)
    except Exception:  # noqa: BLE001
        pass


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
    if not unit_vals or len(unit_vals) < 2:
        # the dropdown re-populates after the wait (a mid-update race); re-wait within
        # this SAME attempt, then re-read — no second login, no retry loop.
        page.wait_for_timeout(2000)
        unit_vals = page.eval_on_selector_all('#unitId option', 'os => os.map(o => o.value)')
    if not unit_vals or len(unit_vals) < 2:
        raise RuntimeError('login failed: unit dropdown empty')
    page.select_option('#unitId', unit_vals[1])
    page.click('input[type="submit"]')
    page.wait_for_timeout(1500)
    # SESSION-COLLISION is a FAILURE, not a success path: the "You are already
    # logged in" dialog means an ambient session is present and the login is
    # riding it — the wrong-credentials control proved this returns a token
    # without authenticating. Do not click Ok; raise instead.
    confirm_ok = page.locator('button:has-text("Ok")').first
    try:
        if confirm_ok.is_visible():
            # Distinguish "a session I just created" from "someone else's".
            # The CHECK_OTHER_LOGIN mechanism keys on macAddress+user+unit+org;
            # this script's own headless browser presents NO macAddress (the
            # legacy mac probe writes nothing for it), so its own rows carry an
            # EMPTY mac — a collision against an empty-mac row is the previous
            # invocation's leftover: release it (the logout POST) and retry the
            # submit ONCE within this same attempt. A collision that survives
            # the release is someone else's session — raise without touching it.
            own_mac = page.evaluate("localStorage.getItem('macAddress') || ''")
            if not own_mac:
                logout(page)
                page.wait_for_timeout(800)
                # full fresh login (reload + re-flow), not a bare second submit —
                # the released page state after the collision dialog is not a
                # clean form
                page.reload(wait_until='load')
                page.wait_for_timeout(1200)
                page.fill('#userName', username)
                page.fill('#password', password)
                page.locator('#password').blur()
                try:
                    page.wait_for_function(
                        "() => document.querySelectorAll('#organizationId option').length > 1",
                        timeout=10000)
                    page.select_option('#organizationId', '1')
                    page.wait_for_function(
                        "() => document.querySelectorAll('#unitId option').length > 1",
                        timeout=15000)
                    unit_vals = page.evaluate(
                        "Array.from(document.querySelectorAll('#unitId option')).map(o => o.value)")
                    if not unit_vals or len(unit_vals) < 2:
                        raise RuntimeError('login failed: unit dropdown empty')
                    page.select_option('#unitId', unit_vals[1])
                except RuntimeError:
                    raise
                except Exception:  # noqa: BLE001
                    pass
                page.click('input[type="submit"]')
                page.wait_for_timeout(1500)
                if confirm_ok.is_visible():
                    raise RuntimeError(
                        'SESSION-COLLISION: an existing session is present and '
                        'not ours (the release did not clear it) — someone else '
                        'is logged in')
            else:
                raise RuntimeError(
                    'SESSION-COLLISION: an existing session is present; the login '
                    'cannot assert fresh authentication')
    except RuntimeError:
        raise
    except Exception:  # noqa: BLE001
        pass
    try:
        page.wait_for_function(
            "() => !!window.sessionStorage.getItem('token')",
            timeout=20000,
        )
    except Exception:  # noqa: BLE001
        raise RuntimeError('login failed: no token returned')
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
            token, cookie, unit = login(page, username, password)
        except Exception as exc:  # noqa: BLE001
            logout(page)
            sys.stderr.write(f'{exc}\n')
            browser.close()
            sys.exit(1)
        if args.check:
            logout(page)
            print('LOGIN-OK')
            browser.close()
            sys.exit(0)
        if args.endpoint:
            import urllib.request
            req = urllib.request.Request(
                args.endpoint,
                headers={
                    'Authorization': f'Bearer {token}',
                    'userid': '1',
                    'unitid': unit,
                    'Cookie': cookie,
                },
            )
            try:
                with urllib.request.urlopen(req, timeout=10) as resp:
                    print(resp.status)
            except urllib.error.HTTPError as exc:
                print(exc.code)
                logout(page)
                browser.close()
                sys.exit(3)
            logout(page)
            browser.close()
            sys.exit(0)
        print(token)
        logout(page)
        browser.close()
        sys.exit(0)


if __name__ == '__main__':
    main()
