#!/usr/bin/env python3
"""
smoke_screen.py — the browser smoke harness (Playwright, FIREFOX — Chromium is
excluded per the Docker-network instability).

Usage: smoke_screen.py <route> [selector1 selector2 ...]

Logs in through the legacy UI (the get_dev_token flow), seeds the token +
cookieObject into the :4200 origin, navigates to the route, and reports:
  - every console error and warning, verbatim
  - every failed / non-2xx network request (URL + status)
  - any request that returned HTML where JSON was expected (the
    unproxied/wrong-URL shape)
  - whether the route rendered or redirected (a module-guard denial is
    distinguishable from a render)
  - whether the expected selectors exist

FAILS (exit 1) on any console error. Warnings are reported without failing.
"""
import argparse
import re
import sys
import os

from playwright.sync_api import sync_playwright

LEGACY = 'http://localhost:8080'
NEW = 'http://localhost:4200'


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
        sys.stderr.write('NO-CREDENTIALS\n')
        sys.exit(2)
    return username, password


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('route')
    ap.add_argument('selectors', nargs='*', default=[])
    ap.add_argument('--port', default='4200')
    args = ap.parse_args()
    route = args.route if args.route.startswith('/') else f'/{args.route}'
    username, password = load_credentials()

    # the single source of truth for minting: get_dev_token (never mint here —
    # a second mint path would reintroduce the login-group limiter problem)
    import get_dev_token as gdt
    from get_dev_token import login as legacy_login

    console_errors, console_warnings = [], []
    bad_requests, html_as_json = [], []
    selector_counts = {}

    with sync_playwright() as p:
        browser = p.firefox.launch(headless=True)
        context = browser.new_context()
        page = context.new_page()

        # the token cache first (one login per 8h — the rate limiter 429s
        # the login-group bucket on consecutive logins); the login only on a miss
        import get_dev_token as gdt
        cached = gdt.read_cache()
        if cached:
            token, cookie, unit = cached['token'], cached.get('cookie', ''), cached.get('unit', '1')
            permissions = cached.get('permissions', '')
        else:
            login_page = context.new_page()
            try:
                token, cookie, unit = legacy_login(login_page, username, password)
            except Exception as exc:  # noqa: BLE001
                print(f'LOGIN-FAILED: {exc}')
                browser.close()
                sys.exit(1)
            gdt.write_cache(token, unit, cookie)
            # release the login-log row so the NEXT invocation does not self-collide
            try:
                login_page.evaluate("""async () => {
                    try {
                        const co = (() => { try { return JSON.parse(decodeURIComponent(
                            document.cookie.split('cookieObject=')[1].split(';')[0])); }
                            catch (e) { return {}; } })();
                        await fetch('/adt/saveUserLogDetails', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify({
                                userId: sessionStorage.getItem('userId') || co.userId || 0,
                                unitId: sessionStorage.getItem('unitId') || co.unitId || 0,
                                organizationId: sessionStorage.getItem('organizationId') || co.organizationId || 0,
                                isLogin: 'N',
                                macAddress: localStorage.getItem('macAddress') || ''
                            })
                        });
                    } catch (e) { /* best-effort */ }
                }""")
            except Exception:  # noqa: BLE001
                pass
            permissions = login_page.evaluate(
                "localStorage.getItem('permissions') || localStorage.getItem('PermissionList') || ''")
            login_page.close()

        # seed the token + cookieObject into the new-app origin
        cookie_entries = []
        for part in cookie.split(';'):
            if '=' in part:
                k, v = part.strip().split('=', 1)
                cookie_entries.append((k, v))
        cookie_object = ''
        for k, v in cookie_entries:
            if k == 'cookieObject':
                cookie_object = v
        page.add_init_script(
            "window.sessionStorage.setItem('token', " + repr(token) + ");"
            + (f"localStorage.setItem('permissions', {repr(permissions)});" if permissions else "")
            + "document.cookie = 'cookieObject=" + cookie_object + "; path=/';"
        )

        page.on('console', lambda m: (console_errors.append(m.text)
                                      if m.type == 'error' else
                                      console_warnings.append(m.text)
                                      if m.type == 'warning' else None))
        page.on('response', lambda r: (
            bad_requests.append(f'{r.status} {r.url[:140]}')
            if r.status >= 400 or (r.status < 200 and r.status >= 100 and r.status != 101)
            else None))
        page.on('response', lambda r: (
            html_as_json.append(
                f'{r.url[:140]} -> HTTP {r.status} HTML-where-JSON-expected'
            )
            if r.status < 400 and 'json' not in (r.headers.get('content-type') or '')
            and r.request.resource_type == 'xhr'
            and 'html' in (r.headers.get('content-type') or '')
            else None))

        try:
            base = NEW if args.port == '4200' else f'http://localhost:{args.port}'
            page.goto(f'{base}{route}', wait_until='load', timeout=30000)
        except Exception as exc:  # noqa: BLE001
            print(f'NAV-FAILED: {exc}')
            browser.close()
            sys.exit(1)
        page.wait_for_timeout(4000)

        # rendered vs redirected
        url_now = page.url
        body_text = page.evaluate('document.body ? document.body.innerText.slice(0, 200) : ""')
        rendered = route.replace('/', '') in url_now or route in url_now
        print(f'URL-after-nav: {url_now}')
        print(f'RENDERED: {rendered} | body-prefix: {body_text[:120]!r}')

        for sel in args.selectors:
            count = page.locator(sel).count()
            selector_counts[sel] = count
            print(f'SELECTOR {sel}: {count} found')

        browser.close()

    for w in console_warnings:
        print(f'[WARN] {w}')
    for e in console_errors:
        print(f'[ERROR] {e}')
    for b in bad_requests:
        print(f'[BAD-REQ] {b}')
    for h in html_as_json:
        print(f'[HTML-AS-JSON] {h}')

    failed = bool(console_errors) or not rendered or bool(bad_requests)
    for sel in args.selectors:
        if selector_counts.get(sel, 0) == 0:
            failed = True
    print(f'RESULT: {"FAIL" if failed else "PASS"} '
          f'(console-errors={len(console_errors)}, warnings={len(console_warnings)}, '
          f'bad-requests={len(bad_requests)}, html-as-json={len(html_as_json)})')
    sys.exit(1 if failed else 0)


if __name__ == '__main__':
    main()
