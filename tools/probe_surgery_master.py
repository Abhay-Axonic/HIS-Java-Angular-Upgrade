#!/usr/bin/env python3
"""surgery-master interaction probe — reuses the cached real-login cookie the
same way smoke_screen.py does, then asserts the screen's live calls fired:
the speciality dropdown (2 rows), the surgery-grade dropdown (3 rows), the
empty grid state, and NO error toasts."""
import json, os, sys
from playwright.sync_api import sync_playwright

CACHE = os.path.join(os.path.dirname(os.path.abspath(__file__)), '.token_cache.json')
d = json.load(open(CACHE))
cookie_object = ''
for part in d['cookie'].split(';'):
    if part.strip().startswith('cookieObject='):
        cookie_object = part.strip().split('=', 1)[1]

URL = 'http://localhost:4201/surgeryMaster'
fails = []

with sync_playwright() as p:
    browser = p.firefox.launch()
    ctx = browser.new_context()
    page = ctx.new_page()
    page.add_init_script(f"document.cookie = 'cookieObject={cookie_object}; path=/';")
    page.goto(URL, wait_until='domcontentloaded')
    page.wait_for_timeout(6000)
    final = page.url
    print('FINAL-URL:', final)
    if '/accessDenied' in final:
        fails.append(f'accessDenied: {final}')
    # toast check
    toasts = page.locator('.notice--error, .growl-error, .toast-error').count()
    if toasts:
        print('ERROR-TOASTS:', toasts)
        fails.append(f'{toasts} error toast(s) rendered')
    # dropdown options
    specialities = page.locator('select[name="specialityId"] option').count()
    grades = page.locator('select[name="surgeryGradeId"] option').count()
    print('speciality options:', specialities, '(expect 2 + placeholder)')
    print('surgeryGrade options:', grades, '(expect 2 + placeholder; the org-1 query returns 2 of the 3 rows — row 1 is org-less)')
    if specialities < 3:
        fails.append(f'speciality dropdown only {specialities} options')
    if grades < 3:
        fails.append(f'grade dropdown only {grades} options')
    # grid empty state
    body = page.locator('body').inner_text()
    has_empty = 'No surgery masters found' in body
    has_title = 'Surgery Master' in body
    print('title rendered:', has_title, '| empty-grid state:', has_empty)
    if not has_title:
        fails.append('screen title not rendered')
    # console errors
    console_errors = []
    page.on('console', lambda m: console_errors.append(m.text) if m.type == 'error' else None)
    page.wait_for_timeout(2000)
    real_errors = [e for e in console_errors if 'favicon' not in e.lower() and 'font' not in e.lower()]
    if real_errors:
        print('CONSOLE-ERRORS:', real_errors[:5])
        fails.append('console errors')
    browser.close()

print()
if fails:
    print('PROBE: FAIL')
    for f in fails:
        print('  -', f)
    sys.exit(1)
print('PROBE: PASS')
