#!/usr/bin/env node
/**
 * positive_control_login.js — the rate-limit verifier's POSITIVE CONTROL
 * (BR-177, 2026-08-06): one full login sequence through the ACTUAL UI endpoints
 * that must return a session token.
 *
 * Why this exists: the 6A filter's original body-read consumed the request
 * stream before the controller, so every LOGIN-GROUP endpoint's @RequestBody
 * saw an empty body and returned HIS-400 "Bad request: Invalid parameters".
 * The unit dropdown therefore never populated and NO login could complete —
 * while the rate-limit verifier's 429-vs-not-429 assertions stayed green
 * (the 400s were read as ordinary envelopes). This control fails when the body
 * is destroyed: the unit dropdown never populates, the submit can't run, and
 * no token comes back.
 *
 * Prints TOKEN-OK on success and exits 0; prints the failure point and exits 1
 * otherwise. Invoked by tools/verify_rate_limit.py.
 */
const { chromium } = require('playwright');

const LEGACY = 'http://localhost:8080';

async function main() {
  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage();
  try {
    await page.goto(`${LEGACY}/index.html`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForTimeout(1500);
    await page.fill('#userName', 'admin');
    await page.fill('#password', '12345');
    await page.locator('#password').blur();
    await page.waitForFunction(
      () => document.querySelectorAll('#organizationId option').length > 1,
      { timeout: 10000 },
    );
    await page.selectOption('#organizationId', '1');
    const unitsLoaded = await page
      .waitForFunction(() => document.querySelectorAll('#unitId option').length > 1, { timeout: 15000 })
      .then(() => true)
      .catch(() => false);
    if (!unitsLoaded) {
      console.error('UNIT-DROPDOWN-NEVER-POPULATED: the unit fetch failed (body destroyed or endpoint error) — login cannot complete');
      process.exit(1);
    }
    const unitVals = await page.$$eval('#unitId option', (o) => o.map((x) => x.value));
    await page.selectOption('#unitId', unitVals[1]);
    await page.click('input[type="submit"]');
    await page.waitForTimeout(1500);
    // Session-collision dialog: with an already-active admin session the backend
    // answers "You are already logged in...!!" and the UI shows an Ok prompt
    // whose click continues the login (the golden-trace login helper handles the
    // same dialog). Without it the token never gets written.
    const confirmOk = page.locator('button:has-text("Ok")').first();
    if (await confirmOk.isVisible().catch(() => false)) await confirmOk.click();
    const token = await page
      .waitForFunction(() => !!window.sessionStorage.getItem('token'), { timeout: 20000 })
      .then(() => true)
      .catch(() => false);
    if (!token) {
      console.error('NO-TOKEN: the login POST completed but no session token came back');
      process.exit(1);
    }
    console.error('TOKEN-OK: full UI login completed with a session token');
    process.exit(0);
  } finally {
    await browser.close();
  }
}

main().catch((e) => {
  console.error('FATAL', String(e).slice(0, 200));
  process.exit(1);
});
