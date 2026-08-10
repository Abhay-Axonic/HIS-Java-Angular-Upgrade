#!/usr/bin/env python3
"""Interaction probe for the rebuilt frequencyMaster screen (screen 15).

Verifies, against the live Angular dev server on :4201:
 1. The screen loads at /frequencyMaster (no accessDenied, no blank).
 2. The form renders (Code + Description + No.of-times inputs, Save button).
 3. The grid renders with the LIVE dev row (the real frequency feed).
 4. Zero page errors / zero failed requests.
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
ROUTE = '/frequencyMaster'
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

    check('code input renders', page.locator('input[name="code"]').count() == 1)
    check('description input renders', page.locator('input[name="description"]').count() == 1)
    check('no-of-times input renders', page.locator('input[name="noOfTimes"]').count() == 1)
    check('save button renders', page.locator('button:has-text("Save")').count() >= 1)

    # the grid must render the LIVE dev row (list feed len=1)
    rows = page.locator('table tbody tr').count()
    check('grid rendered', rows >= 1, f'{rows} tbody rows')
    grid_text = page.locator('table tbody').inner_text()
    check('live row in grid', 'Active' in grid_text or 'Inactive' in grid_text, repr(grid_text[:60]))

    check('zero page errors', not page_errors, '; '.join(page_errors[:2]))
    check('zero failed requests', not failed, '; '.join(failed[:2]))
    browser.close()

print()
if FAILS:
    print('RESULT: FAIL — ' + '; '.join(FAILS))
    sys.exit(1)
print('RESULT: PASS')
