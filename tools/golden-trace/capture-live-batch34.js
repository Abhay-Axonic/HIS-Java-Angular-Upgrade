/**
 * Golden-trace capture against the LIVE deployment (localhost:8080), for
 * batch 34's 6 screens. Does a REAL login through the actual login form
 * (no synthetic cookie seeding), then navigates via hash URL to each
 * target state -- ON THE SAME PAGE the login happened on, never a new
 * tab/page -- and records every real network request/response Playwright
 * observes, plus console errors. Nothing under legacy/ is read, written,
 * or modified -- this only drives a real browser against the already-
 * running Tomcat deployment.
 *
 * IMPORTANT, found the hard way: a first version of this script opened a
 * NEW Playwright page per target screen. That produced a false
 * cross-cutting finding -- every screen but one silently redirected to
 * 'applicationConfiguration' with a "Cannot read properties of undefined
 * (reading 'isKiosk')" console error. Root-caused via
 * angular.element(document.body).injector().get('$cookies') on the SAME
 * page right after login: AngularJS's $cookies.putObject('cookieObjectAppConfig', ...)
 * (loginController.js:788) updates the service's own in-memory cache
 * (getObject() sees it immediately) but that page never actually
 * persisted a matching Set-Cookie/document.cookie entry (getAll() only
 * showed 'cookieObject', not 'cookieObjectAppConfig') -- confirmed via
 * angular.element(...).injector().get('$cookies').getAll(). Real users
 * never open a new tab per screen in this hash-routed AngularJS SPA, so
 * this never affects them; it was purely this harness's earlier
 * methodology. Fixed by reusing one page for the whole session.
 *
 * Usage: node capture-live-batch34.js
 */
const fs = require('fs');
const path = require('path');
const { chromium } = require('playwright');

const BASE = 'http://localhost:8080';
const OUT_DIR = path.resolve(__dirname, 'golden-traces');

const TARGETS = [
  { name: 'InsuranceTPAMapping', url: '/InsuranceTPAMapping' },
  { name: 'operationTable', url: '/operationTable' },
  { name: 'bloodBankTypeComponent', url: '/bloodBankTypeComponent' },
  { name: 'tubeFeedingProducts', url: '/tubeFeedingProducts' },
  { name: 'mpesaApplicationConfiguration', url: '/mpesaApplicationConfiguration' },
  { name: 'pharmacyStrengthUnitMaster', url: '/pharmacyStrengthUnitMaster' },
];

async function login(page) {
  await page.goto(`${BASE}/index.html`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForTimeout(1500);
  await page.fill('#userName', 'admin');
  await page.fill('#password', '12345');
  await page.locator('#password').blur();
  await page.waitForFunction(
    () => document.querySelectorAll('#organizationId option').length > 1,
    { timeout: 10000 },
  );
  await page.selectOption('#organizationId', '1');
  await page.waitForFunction(
    () => document.querySelectorAll('#unitId option').length > 1,
    { timeout: 10000 },
  );
  const firstUnitValue = await page.$eval('#unitId option:nth-child(2)', (o) => o.value);
  await page.selectOption('#unitId', firstUnitValue);
  await page.click('input[type="submit"]');
  await page.waitForTimeout(1500);
  const confirmOk = page.locator('button:has-text("Ok"), a:has-text("Ok")').first();
  if (await confirmOk.isVisible().catch(() => false)) {
    await confirmOk.click();
  }
  await page.waitForFunction(() => !!window.sessionStorage.getItem('token'), { timeout: 15000 });
  const token = await page.evaluate(() => window.sessionStorage.getItem('token'));
  // cookieObjectAppConfig only needs to exist in AngularJS's own $cookies
  // cache on THIS page (confirmed real, see header comment) -- checking
  // via the Angular service itself, not raw document.cookie, since that's
  // what checkCookies() in app.js actually reads.
  await page.waitForFunction(
    () => {
      try {
        return !!angular.element(document.body).injector().get('$cookies').getObject('cookieObjectAppConfig');
      } catch (e) {
        return false;
      }
    },
    { timeout: 15000 },
  );
  return token;
}

async function captureOne(page, target) {
  const requests = [];
  const responses = [];
  const consoleErrors = [];
  const onRequest = (req) => {
    if (req.resourceType() === 'xhr' || req.resourceType() === 'fetch') {
      requests.push({ method: req.method(), url: req.url(), postData: req.postData() });
    }
  };
  const onResponse = async (res) => {
    const req = res.request();
    if (req.resourceType() === 'xhr' || req.resourceType() === 'fetch') {
      let body = null;
      try {
        body = await res.text();
        if (body.length > 2000) body = body.slice(0, 2000) + '...(truncated)';
      } catch (e) {
        body = '(unreadable: ' + e.message + ')';
      }
      responses.push({ url: res.url(), status: res.status(), body });
    }
  };
  const onConsole = (msg) => {
    if (msg.type() === 'error') consoleErrors.push(msg.text());
  };
  const onPageError = (err) => consoleErrors.push('pageerror: ' + err.message);

  page.on('request', onRequest);
  page.on('response', onResponse);
  page.on('console', onConsole);
  page.on('pageerror', onPageError);

  // Hash-only change on the same document/origin -- no full page reload,
  // same SPA instance, same in-memory $cookies cache as login. This is
  // what a real user does clicking a sidebar link.
  await page.goto(`${BASE}/index.html#${target.url}`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForTimeout(2500);

  const result = {
    state: target.name,
    url: target.url,
    capturedAt: new Date().toISOString(),
    finalUrl: page.url(),
    requests,
    responses,
    consoleErrors: consoleErrors.slice(0, 20),
  };

  page.off('request', onRequest);
  page.off('response', onResponse);
  page.off('console', onConsole);
  page.off('pageerror', onPageError);

  return result;
}

async function main() {
  fs.mkdirSync(OUT_DIR, { recursive: true });
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();

  console.error('Logging in as admin/org=1/unit=1 ...');
  const token = await login(page);
  console.error('Login OK, token acquired (len=' + token.length + ')');

  for (const target of TARGETS) {
    process.stderr.write(`  ${target.name} ... `);
    const result = await captureOne(page, target);
    const outFile = path.join(OUT_DIR, `LIVE_${target.name}.json`);
    fs.writeFileSync(outFile, JSON.stringify(result, null, 1));
    process.stderr.write(
      `finalUrl=${result.finalUrl} requests=${result.requests.length} consoleErrors=${result.consoleErrors.length}\n`,
    );
  }

  await page.close();
  await browser.close();
  console.error('Done.');
}

main().catch((e) => {
  console.error('FATAL:', e);
  process.exit(1);
});
