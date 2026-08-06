#!/usr/bin/env node
/**
 * mint_token.js — real UI login (admin/12345, org 1, second unit) and write the
 * session token to /tmp/fresh_token.txt (no trailing newline, no "Bearer " prefix)
 * — the harness convention. Mirrors positive_control_login.js.
 * Run from tools/golden-trace/ (needs its node_modules).
 */
const fs = require('fs');
const path = require('path');
const { chromium } = require('playwright');

const LEGACY = 'http://localhost:8080';

async function main() {
  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage();
  await page.addInitScript(() => {
    window.__errs = [];
    window.addEventListener('error', (e) => window.__errs.push('error: ' + e.message));
    window.addEventListener('unhandledrejection', (e) =>
      window.__errs.push('rejection: ' + ((e.reason && (e.reason.message || e.reason)) || e.reason)),
    );
  });
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
    await page.waitForFunction(() => document.querySelectorAll('#unitId option').length > 1, {
      timeout: 15000,
    });
    const unitVals = await page.$$eval('#unitId option', (o) => o.map((x) => x.value));
    console.log('unit options:', unitVals.join(','));
    await page.selectOption('#unitId', unitVals[1]);
    page.on('response', async (r) => {
      if (r.url().includes('userLogin')) {
        const body = await r.text().catch(() => '<no-body>');
        console.log(`  userLogin ${r.status()} body: ${body.slice(0, 500)}`);
      } else if (r.status() >= 400) {
        console.log(`  resp ${r.status()} ${r.url()}`);
      }
    });
    page.on('console', (m) => { if (m.type() === 'error') console.log('  console-error:', m.text()); });
    await page.click('input[type="submit"]');
    await page.waitForTimeout(2000);
    // session-collision dialog (#loginContinuePopUp): "You will be logged out from previous
    // system, do you want to continue?" — its Ok (#saveBtn, ng-click=continueLogin) proceeds.
    const contBtn = page.locator('#loginContinuePopUp #saveBtn');
    if (await contBtn.isVisible().catch(() => false)) {
      console.log('  continue-dialog shown, clicking continue');
      await contBtn.click();
      await page.waitForTimeout(1500);
    }
    const token = await page
      .waitForFunction(() => !!window.sessionStorage.getItem('token'), { timeout: 25000 })
      .then(() => window.sessionStorage.getItem('token'))
      .catch(() => null)
      // a full-page reload during the wait destroys the polling context; re-read directly
      || await page.evaluate(() => sessionStorage.getItem('token')).catch(() => null);
    if (!token) {
      const errs = await page.evaluate(() => window.__errs || []);
      const keys = await page.evaluate(() => Object.keys(sessionStorage));
      const url = page.url();
      console.error('NO-TOKEN. url=' + url + ' sessionKeys=' + JSON.stringify(keys));
      errs.forEach((e) => console.error('  captured: ' + e));
      process.exit(1);
    }
    fs.writeFileSync('/tmp/fresh_token.txt', token);
    const payload = JSON.parse(Buffer.from(token.split('.')[1], 'base64url').toString());
    console.log('TOKEN-WRITTEN /tmp/fresh_token.txt, claims:', JSON.stringify(payload));
  } finally {
    await browser.close();
  }
}

main().catch((e) => {
  console.error('MINT FAILED:', e);
  process.exit(2);
});
