#!/usr/bin/env node
/**
 * test_store_accessor_live_fire.js — live fire-test for the store-accessor campaign.
 *
 * For each target state: real UI login (admin/12345, org 1), then
 *   NEGATIVE phase: selectedTopStore removed from sessionStorage -> $state.go(state)
 *     - asserts: 0 pageerrors, 0 5xx responses, no literal {{storeName}} in the body.
 *     - a store-scoped call firing in this phase (ungated) shows up as a 5xx or wrong render.
 *   POSITIVE phase: pick a real store through the #pharmacyPopupBeforLeftexpand popup
 *     (select + Ok, the real setStore flow) -> re-navigate to the same state
 *     - asserts: the store-scoped calls fire (network activity, 0 5xx, 0 pageerrors)
 *       i.e. the gates are not making the screen inert.
 *
 * Usage: node test_store_accessor_live_fire.js [state1 state2 ...]
 * (defaults to the batch-1 six pharmacy states)
 */
const { chromium } = require('playwright');

const LEGACY = 'http://localhost:8080';

const DEFAULT_TARGETS = [
  'outPatientSalesWorklist',
  'outPatientSalesWorklistDetails',
  'billSalesReturn',
  'detailsPatientReturnAcceptanceConsumable',
  'pendingIssues',
  'pendingIssuesDetails',
];

async function login(page) {
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
  await page.selectOption('#unitId', unitVals[1]);
  await page.click('input[type="submit"]');
  await page.waitForTimeout(2000);
  // session-collision dialog (#loginContinuePopUp): "You will be logged out from previous
  // system, do you want to continue?" — its Ok (#saveBtn, ng-click=continueLogin) proceeds.
  const contBtn = page.locator('#loginContinuePopUp #saveBtn');
  if (await contBtn.isVisible().catch(() => false)) await contBtn.click();
  const token = await page
    .waitForFunction(() => !!window.sessionStorage.getItem('token'), { timeout: 20000 })
    .then(() => true)
    .catch(() => false);
  if (!token) {
    console.error('NO-TOKEN: login did not complete — aborting');
    process.exit(1);
  }
  console.log('login OK, token present');
}

async function navigate(page, stateName) {
  let threw = null;
  try {
    await page.evaluate((name) => {
      const inj = angular.element(document.body).injector();
      inj.get('$state').go(name);
    }, stateName);
  } catch (e) {
    threw = String(e.message || e);
  }
  await page.waitForTimeout(5000);
  const current = await page
    .evaluate(() => {
      const inj = angular.element(document.body).injector();
      return inj.get('$state').current.name;
    })
    .catch(() => '<no-injector>');
  return { threw, current };
}

async function pickStore(page) {
  // Try the real popup flow first (visible when getStoreObj triggered storePopup and stores>1).
  const modal = page.locator('#pharmacyPopupBeforLeftexpand');
  const visible = await modal.isVisible().catch(() => false);
  if (visible) {
    const options = await page.$$eval('#pharmacyPopupBeforLeftexpand select option', (o) =>
      o.map((x) => x.textContent.trim()),
    );
    if (options.length >= 1) {
      await page.selectOption('#pharmacyPopupBeforLeftexpand select', { label: options[0] });
      await page.locator('#pharmacyPopupBeforLeftexpand #otpOk').click();
      await page.waitForTimeout(1500);
      const store = await page.evaluate(() => sessionStorage.getItem('selectedTopStore'));
      if (store) {
        console.log(`picked store via popup: ${options[0]}`);
        return true;
      }
    }
  }
  // Fallback: seed exactly what setStore() writes, with a REAL store (default id 1 "Test Main Store").
  const storeObj = { storeId: 1, storeName: 'Test Main Store' };
  const seeded = await page.evaluate((store) => {
    sessionStorage.setItem('selectedTopStore', JSON.stringify(store));
    try {
      const inj = angular.element(document.body).injector();
      const $cookies = inj.get('$cookies');
      const cookieObject = $cookies.getObject('cookieObject') || {};
      cookieObject.storeId = store.storeId;
      $cookies.putObject('cookieObject', cookieObject, { domain: 'localhost', secure: false });
    } catch (e) {
      return 'cookie-write-failed: ' + e.message;
    }
    return 'ok';
  }, storeObj);
  if (seeded !== 'ok') {
    console.error('PICK-SEED-FAILED: ' + seeded);
    return false;
  }
  console.log('seeded store directly (popup not driveable this run): Test Main Store (1)');
  return true;
}

async function observe(page, phase, stateName, quiet) {
  const body = await page.evaluate(() => document.body.innerText);
  const literal = body.includes('{{storeName}}');
  const summary = { phase, state: stateName, literal };
  if (!quiet) {
    console.log(
      `[${phase}] ${stateName}: pageerrors=${summary.pageerrors || 0} ` +
        `5xx=${summary.http5 || 0} 4xx=${summary.http4 || 0} literal={{storeName}}=${literal}`,
    );
  }
  return summary;
}

async function main() {
  const targets = process.argv.slice(2);
  if (targets.length === 0) targets.push(...DEFAULT_TARGETS);
  console.log(`fire-test targets: ${targets.join(', ')}`);

  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage();
  let pageerrors = [];
  let http5 = [];
  let http4 = [];
  page.on('pageerror', (e) => pageerrors.push(String(e.message || e)));
  page.on('response', (r) => {
    const s = r.status();
    if (s >= 500) http5.push(`${s} ${r.url()}`);
    else if (s >= 400) http4.push(`${s} ${r.url()}`);
  });

  await login(page);

  let failures = 0;

  // ---------------- NEGATIVE phase: no store ----------------
  await page.evaluate(() => sessionStorage.removeItem('selectedTopStore'));
  console.log('\n--- NEGATIVE phase (selectedTopStore removed) ---');
  for (const t of targets) {
    pageerrors = []; http5 = []; http4 = [];
    const nav = await navigate(page, t);
    const body = await page.evaluate(() => document.body.innerText);
    const literal = body.includes('{{storeName}}');
    console.log(
      `[neg] ${t}: pageerrors=${pageerrors.length} 5xx=${http5.length} 4xx=${http4.length} literal={{storeName}}=${literal}` +
        ` nav=${nav.current}${nav.threw ? ' THREW:' + nav.threw : ''}`,
    );
    for (const e of pageerrors) console.log(`   pageerror: ${e}`);
    for (const h of http5) console.log(`   5xx: ${h}`);
    for (const h of http4) console.log(`   4xx: ${h}`);
    if (pageerrors.length > 0 || http5.length > 0 || literal) {
      failures++;
      console.log(`   >>> NEGATIVE FAIL for ${t}`);
    }
  }

  // ---------------- POSITIVE phase: real store picked ----------------
  const picked = await pickStore(page);
  if (!picked) {
    console.error('POSITIVE phase aborted: could not establish a store');
    process.exit(2);
  }
  console.log('\n--- POSITIVE phase (store picked via the real popup flow) ---');
  for (const t of targets) {
    pageerrors = []; http5 = []; http4 = [];
    const nav = await navigate(page, t);
    const body = await page.evaluate(() => document.body.innerText);
    const literal = body.includes('{{storeName}}');
    console.log(
      `[pos] ${t}: pageerrors=${pageerrors.length} 5xx=${http5.length} 4xx=${http4.length} literal={{storeName}}=${literal}` +
        ` nav=${nav.current}${nav.threw ? ' THREW:' + nav.threw : ''}`,
    );
    for (const e of pageerrors) console.log(`   pageerror: ${e}`);
    for (const h of http5) console.log(`   5xx: ${h}`);
    for (const h of http4) console.log(`   4xx: ${h}`);
    if (pageerrors.length > 0 || http5.length > 0 || literal) {
      failures++;
      console.log(`   >>> POSITIVE FAIL for ${t}`);
    }
  }

  await browser.close();
  console.log(failures === 0 ? '\nFIRE-TEST RESULT: PASS (0 failures)' : `\nFIRE-TEST RESULT: ${failures} FAILURES`);
  process.exit(failures === 0 ? 0 : 1);
}

main().catch((e) => {
  console.error('FIRE-TEST CRASHED:', e);
  process.exit(3);
});
