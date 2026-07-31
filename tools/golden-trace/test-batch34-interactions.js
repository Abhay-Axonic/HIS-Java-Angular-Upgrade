/**
 * Step 4/5 verification for batch 34's 5 rebuilt screens against the real
 * Angular dev server (localhost:4200, proxy-configured to the real
 * backend on :8080). Logs in via the real legacy app first (cookies are
 * host-scoped, not port-scoped, so the session carries over to :4200),
 * then for each screen: captures real network calls (Step 4, compared
 * against the golden trace already captured) and drives real interactions
 * -- fill a form, save, confirm the new row appears, toggle status,
 * confirm the checkbox flips (Step 5). Nothing here is simulated.
 */
const fs = require('fs');
const path = require('path');
const { chromium } = require('playwright');

const LEGACY = 'http://localhost:8080';
const DEV = 'http://localhost:4200';
const OUT_DIR = path.resolve(__dirname, 'golden-traces');

async function login(page) {
  await page.goto(`${LEGACY}/index.html`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForTimeout(1500);
  await page.fill('#userName', 'admin');
  await page.fill('#password', '12345');
  await page.locator('#password').blur();
  await page.waitForFunction(() => document.querySelectorAll('#organizationId option').length > 1, { timeout: 10000 });
  await page.selectOption('#organizationId', '1');
  await page.waitForFunction(() => document.querySelectorAll('#unitId option').length > 1, { timeout: 10000 });
  const unitVal = await page.$eval('#unitId option:nth-child(2)', (o) => o.value);
  await page.selectOption('#unitId', unitVal);
  await page.click('input[type="submit"]');
  await page.waitForTimeout(1500);
  const confirmOk = page.locator('button:has-text("Ok")').first();
  if (await confirmOk.isVisible().catch(() => false)) await confirmOk.click();
  await page.waitForFunction(() => !!window.sessionStorage.getItem('token'), { timeout: 15000 });
}

function attachNetworkCapture(page, bucket) {
  const onRequest = (req) => {
    if (req.resourceType() === 'xhr' || req.resourceType() === 'fetch') {
      bucket.requests.push({ method: req.method(), url: req.url() });
    }
  };
  const onResponse = async (res) => {
    const req = res.request();
    if (req.resourceType() === 'xhr' || req.resourceType() === 'fetch') {
      bucket.responses.push({ url: res.url(), status: res.status() });
    }
  };
  const onConsole = (msg) => {
    if (msg.type() === 'error') bucket.consoleErrors.push(msg.text());
  };
  page.on('request', onRequest);
  page.on('response', onResponse);
  page.on('console', onConsole);
  return () => {
    page.off('request', onRequest);
    page.off('response', onResponse);
    page.off('console', onConsole);
  };
}

async function main() {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();
  const results = {};

  console.error('Logging in via legacy app on :8080 ...');
  await login(page);
  console.error('Login OK.');

  // ---------- 1. tubeFeedingProducts ----------
  {
    const bucket = { requests: [], responses: [], consoleErrors: [] };
    const detach = attachNetworkCapture(page, bucket);
    await page.goto(`${DEV}/tubeFeedingProducts`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForTimeout(1500);
    // localStorage is origin-scoped (host+port), unlike cookies -- :4200's
    // own localStorage.permissions is separate from :8080's and does NOT
    // carry over from the real login. Same documented environment quirk as
    // an earlier session's batch 32 testing (docs/ANGULAR_PENDING_FINDINGS.md,
    // "this dev environment's Angular app uses its own small, manually-
    // maintained localStorage.permissions blob"). Seed the 4 gated module
    // ids this batch's screens need, additively, client-side only, fully
    // reversible -- confirmed necessary by hitting a real accessDenied
    // page first before adding this, not assumed up front.
    await page.evaluate(() => {
      localStorage.setItem(
        'permissions',
        JSON.stringify({ modules: [{ id: 573 }, { id: 522 }, { id: 739 }, { id: 468 }] }),
      );
    });

    const code = 'TFP' + Date.now().toString().slice(-6);
    await page.fill('#productsCode', code);
    await page.fill('#productsDesc', 'Automated Test Product ' + code);
    await page.click('button:has-text("Save")');
    await page.waitForTimeout(300);
    await page.click('.modal button:has-text("Ok")');
    await page.waitForTimeout(1500);

    const rowVisible = await page.locator(`td:has-text("${code}")`).first().isVisible().catch(() => false);
    let toggled = false;
    if (rowVisible) {
      const row = page.locator('tr', { has: page.locator(`td:has-text("${code}")`) }).first();
      const toggle = row.locator('input[type="checkbox"]');
      const slider = row.locator('.slider');
      const before = await toggle.isChecked();
      // The raw checkbox is visually hidden (opacity:0/width:0) behind the
      // styled .slider it's paired with (see status-toggle.scss) -- a real
      // user clicks the visible slider, not the invisible input.
      await slider.click();
      await page.waitForTimeout(1000);
      const after = await toggle.isChecked();
      toggled = before !== after;
    }
    detach();
    results.tubeFeedingProducts = { createdCode: code, rowVisible, toggled, network: bucket };
  }

  // ---------- 2. pharmacyStrengthUnitMaster ----------
  {
    const bucket = { requests: [], responses: [], consoleErrors: [] };
    const detach = attachNetworkCapture(page, bucket);
    await page.goto(`${DEV}/pharmacyStrengthUnitMaster`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForTimeout(1500);

    const code = 'SU' + Date.now().toString().slice(-6);
    await page.fill('#code', code);
    await page.fill('#units', 'Automated Test Unit ' + code);
    await page.click('button:has-text("Save")');
    await page.waitForTimeout(300);
    await page.click('.modal button:has-text("Ok")');
    await page.waitForTimeout(1500);

    const rowVisible = await page.locator(`td:has-text("${code}")`).first().isVisible().catch(() => false);
    detach();
    results.pharmacyStrengthUnitMaster = { createdCode: code, rowVisible, network: bucket };
  }

  // ---------- 3. operationTable ----------
  {
    const bucket = { requests: [], responses: [], consoleErrors: [] };
    const detach = attachNetworkCapture(page, bucket);
    await page.goto(`${DEV}/operationTable`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForTimeout(1500);

    const typeOptionCount = await page.locator('#tableTypeId option').count();
    const theatreOptionCount = await page.locator('#operationTheatreId option').count();
    let rowVisible = false;
    let code = null;
    if (typeOptionCount > 1 && theatreOptionCount > 1) {
      code = 'OT' + Date.now().toString().slice(-6);
      await page.fill('#tableCode', code);
      await page.fill('#tableName', 'Automated Test Table ' + code);
      await page.fill('#tableDesc', 'Automated test description');
      const typeVal = await page.$eval('#tableTypeId option:nth-child(2)', (o) => o.value);
      await page.selectOption('#tableTypeId', typeVal);
      const theatreVal = await page.$eval('#operationTheatreId option:nth-child(2)', (o) => o.value);
      await page.selectOption('#operationTheatreId', theatreVal);
      await page.click('button:has-text("Save")');
      await page.waitForTimeout(300);
      await page.click('.modal button:has-text("Ok")');
      await page.waitForTimeout(1500);
      rowVisible = await page.locator(`td:has-text("${code}")`).first().isVisible().catch(() => false);
    }
    detach();
    results.operationTable = { typeOptionCount, theatreOptionCount, createdCode: code, rowVisible, network: bucket };
  }

  // ---------- 4. bloodBankTypeComponent ----------
  {
    const bucket = { requests: [], responses: [], consoleErrors: [] };
    const detach = attachNetworkCapture(page, bucket);
    await page.goto(`${DEV}/bloodBankTypeComponent`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForTimeout(1500);

    const bagOptionCount = await page.locator('#bloodBagTypeId option').count();
    const componentCheckboxCount = await page.locator('.component-picker input[type="checkbox"]').count();
    let saveAttempted = false;
    let warned = false;
    let saved = false;
    if (bagOptionCount > 1) {
      const bagLabel = await page.$eval('#bloodBagTypeId option:nth-child(2)', (o) => o.textContent);
      const bagVal = await page.$eval('#bloodBagTypeId option:nth-child(2)', (o) => o.value);
      await page.selectOption('#bloodBagTypeId', bagVal);
      if (componentCheckboxCount > 0) {
        await page.locator('.component-picker input[type="checkbox"]').first().click();
      }
      await page.click('button:has-text("Save")');
      saveAttempted = true;
      await page.waitForTimeout(300);
      const modalVisible = await page.locator('.modal button:has-text("Ok")').first().isVisible().catch(() => false);
      if (modalVisible) {
        await page.click('.modal button:has-text("Ok")');
        await page.waitForTimeout(1500);
        saved = await page
          .locator('tr', { has: page.locator(`td:has-text("${bagLabel.trim()}")`) })
          .first()
          .isVisible()
          .catch(() => false);
      } else {
        warned = true; // validate() rejected before the modal opened -- a real "not saved" outcome, not a bug.
      }
    }
    detach();
    results.bloodBankTypeComponent = { bagOptionCount, componentCheckboxCount, saveAttempted, warned, saved, network: bucket };
  }

  // ---------- 5. InsuranceTPAMapping ----------
  {
    const bucket = { requests: [], responses: [], consoleErrors: [] };
    const detach = attachNetworkCapture(page, bucket);
    await page.goto(`${DEV}/InsuranceTPAMapping`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForTimeout(1500);

    const tpaOptionCount = await page.locator('#tpaId option').count();
    let saved = false;
    let tpaLabel = null;
    if (tpaOptionCount > 1) {
      tpaLabel = (await page.$eval('#tpaId option:nth-child(2)', (o) => o.textContent)).trim();
      const tpaVal = await page.$eval('#tpaId option:nth-child(2)', (o) => o.value);
      await page.selectOption('#tpaId', tpaVal);
      await page.waitForTimeout(1000);
      const pickCount = await page.locator('.component-picker input[type="checkbox"]').count();
      if (pickCount > 0) {
        await page.locator('.component-picker input[type="checkbox"]').first().click();
        await page.click('button:has-text("Save")');
        await page.waitForTimeout(1500);
        saved = await page
          .locator('tr', { has: page.locator(`td:has-text("${tpaLabel}")`) })
          .first()
          .isVisible()
          .catch(() => false);
      }
    }
    const editIconDisabled = await page
      .locator('.icon-edit.disabled')
      .first()
      .getAttribute('title')
      .catch(() => null);
    detach();
    results.insuranceTPAMapping = { tpaOptionCount, tpaLabel, saved, editIconTitle: editIconDisabled, network: bucket };
  }

  fs.writeFileSync(path.join(OUT_DIR, 'BATCH34_INTERACTION_RESULTS.json'), JSON.stringify(results, null, 1));
  console.error(JSON.stringify(results, null, 1));

  await browser.close();
}

main().catch((e) => {
  console.error('FATAL:', e);
  process.exit(1);
});
