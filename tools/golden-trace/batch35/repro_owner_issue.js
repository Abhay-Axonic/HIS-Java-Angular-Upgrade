const { chromium } = require('playwright');
const LEGACY = 'http://localhost:8080';
const DEV = 'http://localhost:4200';

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
  const consoleErrors = [];
  page.on('console', (msg) => { if (msg.type() === 'error') consoleErrors.push(msg.text()); });
  page.on('pageerror', (err) => consoleErrors.push('pageerror: ' + err.message));

  console.error('Logging in via :8080...');
  await login(page);
  console.error('Login OK. Cookie check:');
  const cookies = await context.cookies();
  console.error(cookies.filter(c => c.name === 'cookieObject').map(c => ({name: c.name, domain: c.domain})));

  await page.goto(`${DEV}/`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForTimeout(1000);
  await page.evaluate(() => {
    localStorage.setItem('permissions', JSON.stringify({ modules: [{ id: 573 }, { id: 522 }, { id: 739 }, { id: 468 }] }));
  });
  const stored = await page.evaluate(() => localStorage.getItem('permissions'));
  console.error('localStorage.permissions after set:', stored);

  const targets = ['InsuranceTPAMapping', 'bloodBankTypeComponent', 'operationTable', 'pharmacyStrengthUnitMaster'];
  for (const t of targets) {
    consoleErrors.length = 0;
    await page.goto(`${DEV}/${t}`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForTimeout(2000);
    const finalUrl = page.url();
    const heading = await page.locator('h1,h2,legend,.heading-div').first().innerText().catch(() => '(none)');
    console.error(JSON.stringify({ target: t, finalUrl, heading, consoleErrors: consoleErrors.slice(0, 5) }));
  }

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
