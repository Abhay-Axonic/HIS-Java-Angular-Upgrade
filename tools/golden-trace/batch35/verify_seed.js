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

  await page.goto(`${DEV}/InsuranceTPAMapping`, { waitUntil: 'load', timeout: 15000 });
  await page.waitForTimeout(2000);
  const tpaOptions = await page.$$eval('#tpaId option, select[name="tpaId"] option', opts => opts.map(o => o.textContent.trim()));
  console.error('InsuranceTPAMapping TPA dropdown options:', tpaOptions);

  await page.goto(`${DEV}/bloodBankTypeComponent`, { waitUntil: 'load', timeout: 15000 });
  await page.waitForTimeout(2000);
  const bagOptions = await page.$$eval('#bloodBagTypeId option', opts => opts.map(o => o.textContent.trim()));
  const compLabels = await page.$$eval('.component-picker label.checkbox', labels => labels.map(l => l.textContent.trim()));
  console.error('bloodBankTypeComponent bag options:', bagOptions);
  console.error('bloodBankTypeComponent component checkboxes:', compLabels);

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
