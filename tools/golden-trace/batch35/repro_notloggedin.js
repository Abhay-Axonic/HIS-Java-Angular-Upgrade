const { chromium } = require('playwright');
const DEV = 'http://localhost:4200';

async function main() {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();

  // NO login step -- simulate a :4200 tab with no cookieObject cookie present,
  // but WITH the correct localStorage.permissions already set (e.g. set on
  // a prior visit, or set now but session.current still resolves null for
  // some other reason -- either way, isolate the moduleId check from the
  // login check).
  await page.goto(`${DEV}/`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForTimeout(1000);
  const cookiesBefore = await context.cookies();
  console.error('cookies present:', cookiesBefore.map(c => c.name));

  await page.goto(`${DEV}/InsuranceTPAMapping`, { waitUntil: 'load', timeout: 15000 });
  await page.waitForTimeout(2000);
  const finalUrl = page.url();
  const bodyText = await page.evaluate(() => document.body.innerText);
  console.error(JSON.stringify({ finalUrl, bodyText: bodyText.slice(0, 300) }));

  await browser.close();
}
main().catch((e) => { console.error('FATAL', e); process.exit(1); });
