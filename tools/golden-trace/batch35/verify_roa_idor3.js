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

  const netLog = [];
  page.on('response', async (res) => {
    const req = res.request();
    if (req.url().includes('route/of/administration')) {
      let body = '';
      try { body = (await res.text()).slice(0, 500); } catch(e) {}
      netLog.push({ url: res.url(), status: res.status(), body, reqHeaders: req.headers() });
    }
  });

  await login(page);
  await page.goto(`${LEGACY}/index.html#/routeAdministration`, { waitUntil: 'load', timeout: 20000 });
  await page.waitForTimeout(2500);
  console.error('Real page load network calls to route/of/administration:');
  console.error(JSON.stringify(netLog, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
