#!/usr/bin/env python3
"""Interaction probe for the rebuilt frequencyMaster screen (screen 15).

Verifies, against the live Angular dev server on :4201:
 1. The screen loads at /frequencyMaster (no accessDenied, no blank).
 2. The form renders (Code + Description + time-items list, Save button).
 3. The grid renders with the LIVE dev row (the real frequency feed).
 4. WRITE-VERIFICATION (Q28): fills the form with the legacy contract
    (code/description/times), clicks Save, and asserts the observed response
    — success toast + the row visible in the list feed. The probe row is
    DELETEd afterwards (dev table back to its prior state).
 5. Edit-click on the LIVE row: byId 500s (phantom t_translated_frequency) —
    the toast must name the failure, the form stays usable.
 6. Zero page errors / zero failed requests.
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
PROBE_CODE = 'PROBE-TIMES'
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
    check('time-items list renders', page.locator('.time-row input').count() >= 1)
    check('save button renders', page.locator('button:has-text("Save")').count() >= 1)

    # grid renders the LIVE dev row
    rows = page.locator('table tbody tr').count()
    check('grid rendered', rows >= 1, f'{rows} tbody rows')
    grid_text = page.locator('table tbody').inner_text()
    check('live row in grid', 'Active' in grid_text or 'Inactive' in grid_text, repr(grid_text[:60]))

    # ---- WRITE-VERIFICATION: fire the save with the legacy contract ----
    page.locator('input[name="code"]').fill(PROBE_CODE)
    page.locator('input[name="description"]').fill('probe times row')
    time_inputs = page.locator('.time-row input')
    time_inputs.nth(0).fill('07:30')
    page.locator('button:has-text("+ Add time")').click()
    page.wait_for_timeout(400)
    page.locator('.time-row input').nth(1).fill('19:30')
    page.locator('button:has-text("Save")').click()
    page.wait_for_timeout(3500)

    body_after_save = page.evaluate('document.body.innerText')
    saved_ok = 'Frequency saved successfully' in body_after_save
    check('save fired: success toast', saved_ok, repr(body_after_save[-250:] if not saved_ok else 'toast seen'))
    if not saved_ok and ('Save failed' in body_after_save):
        # capture the failure text — this is the honesty evidence either way
        idx = body_after_save.find('Save failed')
        check('save failure named in plain language (not raw HTTP)', 'Http failure response' not in body_after_save[idx:idx+300], repr(body_after_save[idx:idx+300]))

    # the row must be visible in the list feed after save
    grid_after = page.locator('table tbody').inner_text()
    check('probe row visible in list feed', PROBE_CODE in grid_after, repr(grid_after[:120]))

    # ---- edit-click on the LIVE dev row: phantom 500 must surface ----
    page.locator('table tbody tr', has_text='TESTFREQ1').locator('button:has-text("Edit")').first.click()
    page.wait_for_timeout(2500)
    body_after_edit = page.evaluate('document.body.innerText')
    toast_ok = 't_translated_frequency' in body_after_edit and 'Edit failed to load record' in body_after_edit
    check('edit-click: honest toast naming the phantom', toast_ok, repr(body_after_edit[-300:] if not toast_ok else 'toast seen'))
    code_val = page.locator('input[name="code"]').input_value()
    check('edit-click: form populated from list row (usable)', code_val == 'TESTFREQ1', f'code={code_val!r}')

    check('zero page errors', not page_errors, '; '.join(page_errors[:2]))
    check('zero failed requests', not failed, '; '.join(failed[:2]))
    browser.close()

# cleanup: delete the probe row + its detail rows (dev table back to prior state)
r = subprocess.run(
    "psql -h localhost -d his_local -c \"DELETE FROM public.m_frequency_detail WHERE frequency_id IN (SELECT id FROM public.m_frequency WHERE code='PROBE-TIMES'); DELETE FROM public.m_frequency WHERE code='PROBE-TIMES';\"",
    shell=True, capture_output=True, text=True)
left = subprocess.run(
    "psql -h localhost -d his_local -t -A -c \"SELECT count(*) FROM public.m_frequency WHERE code='PROBE-TIMES';\"",
    shell=True, capture_output=True, text=True)
check('probe row cleaned up', left.stdout.strip() == '0', f"remaining={left.stdout.strip()!r} rc={r.returncode}")

print()
if FAILS:
    print('RESULT: FAIL — ' + '; '.join(FAILS))
    sys.exit(1)
print('RESULT: PASS')
