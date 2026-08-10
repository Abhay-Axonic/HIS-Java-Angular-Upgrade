#!/usr/bin/env python3
"""Interaction probe for the rebuilt frequencyMaster screen (screen 15).

The probe drives the REAL form — fills the actual inputs, clicks the actual
buttons, asserts the actual result. It never constructs a payload itself.

Checks, against the live Angular dev server on :4201:
 1. The screen loads at /frequencyMaster.
 2. The form renders (Code + Description + time-items list, Save button).
 3. The grid renders the LIVE dev row (TESTFREQ1).
 4. THE USER'S EXACT FAILING INPUT (regression): code test123 / description
    decp123 / one time "8:00" (single-digit hour — the input the backend
    rejected with HIS-400 "Text '8:00' could not be parsed"). The form must
    normalize it to 08:00 and SAVE successfully (success toast + row in the
    list feed). The row is DELETEd afterwards.
 5. NEGATIVE CONTROL: a deliberately invalid time ("25:99") must be rejected
    CLIENT-SIDE with a plain-language toast ("Save not sent: each
    administration time must be HH:MM") and NO save request may reach the
    server (wire capture asserts zero frequency/save POSTs).
 6. Edit-click on TESTFREQ1: byId 500s (phantom t_translated_frequency) —
    the toast must name the failure; the form stays usable.
 7. Zero page errors / zero failed requests.
"""
import json
import subprocess
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
SAVE_POSTS = []


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

    def on_request(req):
        if '/api/globals/frequency/save' in req.url and req.method == 'POST':
            SAVE_POSTS.append(req.post_data or '')

    page.on('request', on_request)
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
    check('time-items list renders', page.locator('.time-row input').count() >= 1)
    check('save button renders', page.locator('button:has-text("Save")').count() >= 1)

    grid_text = page.locator('table tbody').inner_text()
    check('live row in grid', 'TESTFREQ1' in grid_text, repr(grid_text[:80]))

    # ---- 4. THE USER'S EXACT FAILING INPUT — "8:00" (single-digit hour) ----
    page.locator('input[name="code"]').fill('test123')
    page.locator('input[name="description"]').fill('decp123')
    page.locator('.time-row input').nth(0).fill('8:00')
    page.locator('button:has-text("Save")').click()
    page.wait_for_timeout(3500)

    body_after = page.evaluate('document.body.innerText')
    saved_ok = 'Frequency saved successfully' in body_after
    check('user input 8:00: success toast', saved_ok, repr(body_after[-260:] if not saved_ok else 'toast seen'))
    if not saved_ok and 'Save failed' in body_after:
        i = body_after.find('Save failed')
        check('user input 8:00: failure named plainly', 'Http failure response' not in body_after[i:i+300], repr(body_after[i:i+300]))

    grid_after = page.locator('table tbody').inner_text()
    check('user input 8:00: row visible in list feed', 'test123' in grid_after, repr(grid_after[:120]))

    # ---- 5. NEGATIVE CONTROL — invalid time "25:99" must NOT reach the server ----
    posts_before = len(SAVE_POSTS)
    page.locator('input[name="code"]').fill('NEG-CTRL')
    page.locator('input[name="description"]').fill('negative control')
    page.locator('.time-row input').nth(0).fill('25:99')
    page.locator('button:has-text("Save")').click()
    page.wait_for_timeout(2000)
    body_neg = page.evaluate('document.body.innerText')
    check('negative control: plain-language rejection toast', 'Save not sent: each administration time must be HH:MM' in body_neg,
          repr(body_neg[-260:] if 'Save not sent' not in body_neg else 'toast seen'))
    check('negative control: NO save request fired', len(SAVE_POSTS) == posts_before,
          f'posts {posts_before} -> {len(SAVE_POSTS)}')

    # ---- 6. edit-click on the LIVE dev row: phantom 500 must surface ----
    page.locator('table tbody tr', has_text='TESTFREQ1').locator('button:has-text("Edit")').first.click()
    page.wait_for_timeout(2500)
    body_edit = page.evaluate('document.body.innerText')
    toast_ok = 't_translated_frequency' in body_edit and 'Edit failed to load record' in body_edit
    check('edit-click: honest toast naming the phantom', toast_ok, repr(body_edit[-300:] if not toast_ok else 'toast seen'))
    code_val = page.locator('input[name="code"]').input_value()
    check('edit-click: form populated from list row (usable)', code_val == 'TESTFREQ1', f'code={code_val!r}')

    check('zero page errors', not page_errors, '; '.join(page_errors[:2]))
    check('zero failed requests', not failed, '; '.join(failed[:2]))
    browser.close()

# cleanup: delete the probe rows (test123 + NEG-CTRL if any) + their detail rows
subprocess.run(
    "psql -h localhost -d his_local -c \"DELETE FROM public.m_frequency_detail WHERE frequency_id IN (SELECT id FROM public.m_frequency WHERE code IN ('test123','NEG-CTRL')); DELETE FROM public.m_frequency WHERE code IN ('test123','NEG-CTRL');\"",
    shell=True, capture_output=True, text=True)
left = subprocess.run(
    "psql -h localhost -d his_local -t -A -c \"SELECT count(*) FROM public.m_frequency WHERE code IN ('test123','NEG-CTRL');\"",
    shell=True, capture_output=True, text=True)
check('probe rows cleaned up', left.stdout.strip() == '0', f"remaining={left.stdout.strip()!r}")

print()
if FAILS:
    print('RESULT: FAIL — ' + '; '.join(FAILS))
    sys.exit(1)
print('RESULT: PASS')
