const { chromium } = require('playwright');
const LEGACY = 'http://localhost:8080';

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

async function main() {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();
  await login(page);
  await page.goto(`${LEGACY}/index.html#/formulationTypeROAMaster`, { waitUntil: 'load', timeout: 20000 });
  await page.waitForTimeout(1500);
  const token = await page.evaluate(() => window.sessionStorage.getItem('token'));
  const h = { 'Authorization': 'Bearer ' + token, 'userId': '1', 'unitId': '1', 'Content-Type': 'application/json' };

  const forgedList = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/999', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: READ (getAll), forged orgId=999:');
  console.error(JSON.stringify(forgedList, null, 1));

  const byIdVictim = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/1/3', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: READ (getById), org1 path but id=3 belongs to victim org 999:');
  console.error(JSON.stringify(byIdVictim, null, 1));

  const countResp = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/getCount/999', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: READ (getCount), forged orgId=999:');
  console.error(JSON.stringify(countResp, null, 1));

  const saveForged = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/save', {
      method: 'POST', headers: hdrs,
      body: JSON.stringify({ formulationtypeid: 1, routeOfAdministrationId: [7], status: true, unitId: 999, organizationId: 999, userId: 1 })
    });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: WRITE (save), forged organizationId=999 in body:');
  console.error(JSON.stringify(saveForged, null, 1));

  // Legitimate same-tenant flow must still work.
  const legitList = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/1', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: legitimate org1 request (own tenant) -- must still succeed:');
  console.error(JSON.stringify(legitList, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
