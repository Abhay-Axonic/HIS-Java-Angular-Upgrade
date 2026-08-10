#!/usr/bin/env python3
"""Interaction probe for the rebuilt formulationTypeROAMaster screen (screen 13).

Verifies, against the live Angular dev server on :4201:
 1. The screen loads at /formulationTypeROAMaster (no accessDenied, no blank).
 2. The formulation-type select is populated from the LIVE feed (>= 1 option —
    real dev row "Test Formulation" id 1).
 3. The ROA checkbox group renders (empty state text when the dev feed is
    empty, or checkboxes when rows exist).
 4. The grid renders (empty-state row).
 5. Zero page errors / zero failed requests.
"""
import json
import sys

from playwright.sync_api import sync_playwright

CACHE = json.load(open('tools/.token_cache.json'))
COOKIE = ''
for part in CACHE.get('cookie', '').split(';'):
    if part.strip().startswith('cookieObject='):
        COOKIE = part.strip().split('=', 1)[1]
        break

BASE = 'http://localhost:4201'
ROUTE = '/formulationTypeROAMaster'
FAILS = []


def check(name, cond, detail=''):
    print(f'{"PASS" if cond else "FAIL"}-{name}' + (f' {detail}' if detail else ''))
    if not cond:
        FAILS.append(name)


with sync_playwright() as p:
    browser = p.firefox.launch(headless=True)
    ctx = browser.new_context()
    page = ctx.new_page()
    page.add_init_script(f"document.cookie = 'cookieObject={COOKIE}; path=/';")
    page_errors, failed = [], []
    page.on('pageerror', lambda e: page_errors.append(str(e)))
    page.on('requestfailed', lambda r: failed.append(f'{r.url[:100]} -> {r.failure or "failed"}'))

    page.goto(f'{BASE}{ROUTE}', wait_until='load', timeout=30000)
    page.wait_for_timeout(3500)

    url_now = page.url
    check('FINAL-URL', url_now.rstrip('/').endswith(ROUTE), url_now)

    body = page.evaluate('document.body ? document.body.innerText.slice(0, 400) : ""')
    check('BODY non-empty', bool(body.strip()), repr(body[:80]))
    check('no Access Denied', 'Access Denied' not in body)

    # formulation-type select populated from the LIVE feed
    ft_count = page.locator('select[name="formulationType"] option').count()
    check('formulation-type select populated', ft_count >= 2, f'{ft_count} options')

    # ROA checkboxes or the empty-state hint
    checks = page.locator('.roa-check').count()
    empty_hint = page.locator('.empty-hint').count()
    check('ROA group renders', checks >= 1 or empty_hint >= 1, f'checkboxes={checks} hint={empty_hint}')

    # grid present (empty state)
    rows = page.locator('table tbody tr').count()
    check('grid rendered', rows >= 1, f'{rows} tbody rows')

    check('zero page errors', not page_errors, '; '.join(page_errors[:2]))
    check('zero failed requests', not failed, '; '.join(failed[:2]))
    browser.close()

print()
if FAILS:
    print('RESULT: FAIL — ' + '; '.join(FAILS))
    sys.exit(1)
print('RESULT: PASS')
