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

  const legitSave = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/save', {
      method: 'POST', headers: hdrs,
      body: JSON.stringify({ formulationtypeid: 1, routeOfAdministrationId: [8], status: true, unitId: 1, organizationId: 1, userId: 1 })
    });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('Legit org1 save (both FKs org1):', JSON.stringify(legitSave));

  const legitList = await page.evaluate(async (hdrs) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/1', { headers: hdrs });
    return { status: res.status, body: await res.text() };
  }, h);
  console.error('Legit org1 list after clean save -- must show the new row:', JSON.stringify(legitList));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
