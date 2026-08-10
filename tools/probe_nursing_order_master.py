#!/usr/bin/env python3
"""Interaction probe for the rebuilt nursingOrderMaster screen (screen 12).

Verifies, against the live Angular dev server on :4201:
 1. The screen loads at /nursingOrderMaster (no accessDenied, no blank).
 2. The speciality select is populated from the LIVE feed (>= 1 option).
 3. The grid renders (empty-state row or data rows).
 4. Saving with code+desc fires the save endpoint (honest response handling;
    the probe observes the network call rather than asserting persistence).
 5. Zero page errors / zero failed requests.
"""
import json
import sys
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = json.load(open('tools/.token_cache.json'))
COOKIE = ''
for part in CACHE.get('cookie', '').split(';'):
    if part.strip().startswith('cookieObject='):
        COOKIE = part.strip().split('=', 1)[1]
        break

BASE = 'http://localhost:4201'
ROUTE = '/nursingOrderMaster'
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

    # speciality select populated from the LIVE feed
    sp_count = page.locator('select[name="specialityId"] option').count()
    check('speciality select populated', sp_count >= 2, f'{sp_count} options')

    # grid present (empty state or rows)
    rows = page.locator('table tbody tr').count()
    check('grid rendered', rows >= 1, f'{rows} tbody rows')

    # select a speciality -> sub-speciality feed fires
    page.select_option('select[name="specialityId"]', index=1)
    page.wait_for_timeout(1500)
    sub_count = page.locator('select[name="subSpecialityId"] option').count()
    check('sub-speciality populated after selection', sub_count >= 1, f'{sub_count} options')

    # fill the form and click Save; observe the network call
    page.fill('input[name="nursingOrderCode"]', 'PROBE-NO1')
    page.fill('input[name="nursingOrderDesc"]', 'Probe Nursing Order')
    with page.expect_response(lambda r: '/saveNursingOrderMaster' in r.url and r.request.method == 'POST', timeout=15000) as ri:
        page.click('button:has-text("Save")')
    resp = ri.value
    check('save endpoint fired', resp.status == 200, f'HTTP {resp.status}')

    check('zero page errors', not page_errors, '; '.join(page_errors[:2]))
    check('zero failed requests', not failed, '; '.join(failed[:2]))
    browser.close()

print()
if FAILS:
    print('RESULT: FAIL — ' + '; '.join(FAILS))
    sys.exit(1)
print('RESULT: PASS')
