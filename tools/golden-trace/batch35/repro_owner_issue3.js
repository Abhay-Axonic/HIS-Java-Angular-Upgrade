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
  await login(page);
  await page.goto(`${DEV}/`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForTimeout(1000);
  await page.evaluate(() => {
    localStorage.setItem('permissions', JSON.stringify({ modules: [{ id: 573 }, { id: 522 }, { id: 739 }, { id: 468 }] }));
  });

  const targets = ['operationTable', 'pharmacyStrengthUnitMaster'];
  for (const t of targets) {
    try {
      await page.goto(`${DEV}/${t}`, { waitUntil: 'load', timeout: 15000 });
    } catch (e) {
      console.error(t, 'goto error:', e.message);
    }
    await page.waitForTimeout(2500);
    const finalUrl = page.url();
    const bodyLen = await page.evaluate(() => document.body.innerText.length);
    const bodySnippet = await page.evaluate(() => document.body.innerText.slice(0, 200));
    console.error(JSON.stringify({ target: t, finalUrl, bodyLen, bodySnippet }));
  }
  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
