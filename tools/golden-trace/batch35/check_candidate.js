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
  const state = process.argv[2];
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();
  await login(page);

  const netLog = [];
  page.on('response', async (res) => {
    const req = res.request();
    if (req.resourceType() === 'xhr' || req.resourceType() === 'fetch') {
      let bodySnippet = null;
      try { bodySnippet = (await res.text()).slice(0, 300); } catch (e) {}
      netLog.push({ url: res.url(), status: res.status(), bodySnippet });
    }
  });
  const consoleErrors = [];
  page.on('console', (msg) => { if (msg.type() === 'error') consoleErrors.push(msg.text()); });

  await page.goto(`${LEGACY}/index.html#${state}`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForTimeout(2500);

  const bodyText = await page.locator('#page-content-wrapper, .container-fluid').first().innerText().catch(() => '(could not read content)');
  const url = page.url();

  console.log(JSON.stringify({ state, url, netLog, consoleErrors, bodyText: bodyText.slice(0, 800) }, null, 1));
  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
