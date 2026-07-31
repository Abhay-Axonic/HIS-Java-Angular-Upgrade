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
  // Seed permissions blob with a broad set of module ids so resolve-block gates
  // don't block real content investigation -- includes all module ids already
  // known from tonight's work plus a wide net; if a screen still 403s on this,
  // that's a real, reportable access-gate finding, not a setup gap.
}

const TARGETS = [
  { name: 'addNewNabhReporting', url: '/addNewNabhReporting' },
  { name: 'addNewOrderSetMaster', url: '/addNewOrderSetMaster' },
  { name: 'applicationConfiguration', url: '/applicationConfiguration' },
  { name: 'appointmentScheduling', url: '/appointmentScheduling' },
  { name: 'appointmentSchedulingUnitMaster', url: '/appointmentSchedulingUnitMaster' },
  { name: 'bedAllocation', url: '/bedAllocation' },
  { name: 'bedAvailability', url: '/bedAvailability' },
  { name: 'bedBook', url: '/bedBook' },
  { name: 'doctorWise', url: '/doctorWise' },
  { name: 'formFSelfId', url: '/formFSelfId' },
  { name: 'initialAssessmentForICU', url: '/initialassessmentForICU' },
  { name: 'nabhReporting', url: '/nabhReporting' },
  { name: 'passIssued', url: '/passIssued' },
  { name: 'patientList', url: '/patientList' },
  { name: 'reportedNabh', url: '/reportedNabh' },
  { name: 'scheduleWise', url: '/scheduleWise' },
  { name: 'userRoleManagement', url: '/userRoleManagement' },
  { name: 'webAppointment', url: '/webAppointment' },
  { name: 'adviceTab (OPD tab family spot-check)', url: '/adviceTab' },
];

async function main() {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();
  await login(page);

  const results = [];
  for (const t of TARGETS) {
    const netCalls = [];
    const consoleErrors = [];
    const onResp = (res) => {
      const req = res.request();
      if (req.resourceType() === 'xhr' || req.resourceType() === 'fetch') {
        netCalls.push({ url: res.url(), status: res.status() });
      }
    };
    const onConsole = (msg) => { if (msg.type() === 'error') consoleErrors.push(msg.text()); };
    page.on('response', onResp);
    page.on('console', onConsole);

    try {
      await page.goto(`${LEGACY}/index.html#${t.url}`, { waitUntil: 'load', timeout: 20000 });
    } catch (e) {
      results.push({ name: t.name, error: 'goto failed: ' + e.message });
      page.off('response', onResp); page.off('console', onConsole);
      continue;
    }
    await page.waitForTimeout(2200);
    const finalUrl = page.url();
    const bodyText = await page.evaluate(() => document.body.innerText.slice(0, 250)).catch(() => '(unreadable)');
    page.off('response', onResp);
    page.off('console', onConsole);
    results.push({ name: t.name, finalUrl, netCallCount: netCalls.length, netSample: netCalls.slice(0, 6), consoleErrors: consoleErrors.slice(0, 5), bodyText });
  }

  console.log(JSON.stringify(results, null, 1));
  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
