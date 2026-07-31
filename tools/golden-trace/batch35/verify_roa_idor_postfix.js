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
  await page.goto(`${LEGACY}/index.html#/routeAdministration`, { waitUntil: 'load', timeout: 20000 });
  await page.waitForTimeout(1500);
  const token = await page.evaluate(() => window.sessionStorage.getItem('token'));
  const h = { 'Authorization': 'Bearer ' + token, 'userId': '1', 'unitId': '1', 'Content-Type': 'application/json' };

  const forgedList = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/global/route/of/administration/999', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: Forged orgId=999 (victim), real org1/unit1 identity headers:');
  console.error(JSON.stringify(forgedList, null, 1));

  const byIdVictim = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/global/route/of/administration/1/4', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: org1 path but id=4 belongs to victim org 999 (getById scoping test):');
  console.error(JSON.stringify(byIdVictim, null, 1));

  const createForged = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/global/route/of/administration/999/1', {
      method: 'POST', headers: hdrs,
      body: JSON.stringify({ code: 'WRTEST2', description: 'post-fix write-IDOR retest', status: true })
    });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: Create, forged orgId=999 in URL, real org1/unit1 identity:');
  console.error(JSON.stringify(createForged, null, 1));

  // Legitimate same-tenant request MUST still work post-fix.
  const legitList = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/global/route/of/administration/1', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: legitimate org1 request (own tenant) -- must still succeed:');
  console.error(JSON.stringify(legitList, null, 1));

  const legitCreate = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/global/route/of/administration/1/1', {
      method: 'POST', headers: hdrs,
      body: JSON.stringify({ code: 'LEGIT1', description: 'legitimate org1 create post-fix', status: true })
    });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('POST-FIX: legitimate org1 create (own tenant) -- must still succeed:');
  console.error(JSON.stringify(legitCreate, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
