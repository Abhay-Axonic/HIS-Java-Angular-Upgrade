#!/usr/bin/env node
/**
 * verify_permissions_bootstrap.js — the seed-free permissions verification
 * (Track A #65 fix, tools/golden-trace family).
 *
 * This is the check that REPLACES the deleted permission seeds: it exercises
 * the REAL path end to end and asserts the four properties that prove the
 * bootstrap works — and proves the guard is enforcing, not bypassed.
 *
 *   a. REAL POPULATION: after a real legacy login (which now writes roleId
 *      into cookieObject), the rebuild's SessionService.ensurePermissions()
 *      calls GET /privilege/role/{org}/{unit}/{roleId}; assert localStorage
 *      permissions holds the ACTUAL role's module set — count AND the specific
 *      moduleIds the batch screens are gated on, not merely non-empty.
 *   b. REACHABILITY: the batch screens whose moduleId is in the real set
 *      resolve past the guard (URL stays on the route, no /accessDenied).
 *   c. NEGATIVE CONTROL: a route whose moduleId is genuinely ABSENT from the
 *      real set still bounces, with reason=denied. This is what proves the
 *      guard enforces rather than being bypassed. The absent moduleId is
 *      chosen DYNAMICALLY (routes file vs the loaded set); if every rebuilt
 *      route's moduleId happens to be in the role's set, the control is
 *      reported N/A with the evidence, not silently skipped.
 *   d. FAILURE PATH: the bootstrap stubbed to fail (route aborted) renders
 *      reason=permissionsUnavailable with the distinct message — the
 *      non-silent failure is itself verified.
 *
 * Run against the running dev server (npx ng serve --proxy-config
 * proxy.conf.json --port 4200) + the legacy Tomcat at :8080:
 *   node tools/golden-trace/verify_permissions_bootstrap.js
 */
const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const LEGACY = 'http://localhost:8080';
const DEV = 'http://localhost:4200';
const ROUTES_FILE = path.join(__dirname, '../../new/src/app/app.routes.ts');

// The batch screens whose moduleIds the seed used to fake — now asserted
// against the REAL role set.
const BATCH_TARGETS = [
  { route: 'InsuranceTPAMapping', moduleId: 573 },
  { route: 'operationTable', moduleId: 522 },
  { route: 'bloodBankTypeComponent', moduleId: 739 },
  { route: 'pharmacyStrengthUnitMaster', moduleId: 468 },
  { route: 'rackMasters', moduleId: 1068 },
];

let failures = 0;
const fail = (msg) => { failures += 1; console.error(`[FAIL] ${msg}`); };
const pass = (msg) => console.error(`[PASS] ${msg}`);

async function login(page) {
  await page.goto(`${LEGACY}/index.html`, { waitUntil: 'load', timeout: 30000 });
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
  const unitVal = await page.$eval('#unitId option:nth-child(2)', (o) => o.value);
  await page.selectOption('#unitId', unitVal);
  await page.click('input[type="submit"]');
  await page.waitForTimeout(1500);
  const confirmOk = page.locator('button:has-text("Ok")').first();
  if (await confirmOk.isVisible().catch(() => false)) await confirmOk.click();
  await page.waitForFunction(() => !!window.sessionStorage.getItem('token'), { timeout: 15000 });
}

/** Parse (path, moduleId) pairs out of app.routes.ts. */
function routesWithModuleIds() {
  const src = fs.readFileSync(ROUTES_FILE, 'utf8');
  const pairs = [];
  const re = /path:\s*'([^']+)',\s*\n\s*canActivate:\s*\[moduleAccessGuard\],\s*\n\s*data:\s*\{\s*moduleId:\s*(\d+)/g;
  let m;
  while ((m = re.exec(src)) !== null) pairs.push({ route: m[1], moduleId: Number(m[2]) });
  return pairs;
}

async function main() {
  const browser = await chromium.launch({ headless: true });

  // ---- (a) REAL POPULATION ----
  const context = await browser.newContext();
  const page = await context.newPage();
  await login(page);
  await page.goto(`${DEV}/`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForFunction(
    () => {
      try {
        const p = JSON.parse(localStorage.getItem('permissions'));
        return Array.isArray(p?.modules) && p.modules.length > 0;
      } catch {
        return false;
      }
    },
    { timeout: 20000 },
  );
  const perms = await page.evaluate(() => JSON.parse(localStorage.getItem('permissions')));
  const ids = perms.modules.map((m) => m.id);
  console.error(`[a] real module set: count=${ids.length}, sample ids=${ids.slice(0, 8).join(',')}, ...`);
  if (ids.length < 50) {
    fail(`(a) real set count ${ids.length} looks too small for the admin role — expected a full module tree`);
  } else {
    pass(`(a) real set count ${ids.length} (meaningful — not the old 4-module fake)`);
  }
  const missing = BATCH_TARGETS.filter((t) => !ids.includes(t.moduleId));
  if (missing.length > 0) {
    // STOP-AND-REPORT condition: the admin role's real set lacks a moduleId the
    // batch screens are gated on — a DATA problem (role/permission data), not
    // plumbing. Do NOT paper over it.
    fail(
      `(a) admin role's REAL set LACKS batch moduleIds: ${missing.map((t) => t.moduleId).join(',')} ` +
        `(${missing.map((t) => t.route).join(',')}). This is the stop-and-report condition — ` +
        `the role's module data does not cover the rebuilt screens; the fix changes.`,
    );
  } else {
    pass(`(a) real set contains ALL batch moduleIds (${BATCH_TARGETS.map((t) => t.moduleId).join(',')})`);
  }

  // ---- (b) REACHABILITY ----
  for (const t of BATCH_TARGETS) {
    await page.goto(`${DEV}/${t.route}`, { waitUntil: 'load', timeout: 15000 });
    await page.waitForTimeout(1500);
    const url = page.url();
    if (url.includes('/accessDenied')) {
      fail(`(b) ${t.route} (${t.moduleId}) DENIED at ${url}`);
    } else {
      pass(`(b) ${t.route} (${t.moduleId}) resolves past the guard`);
    }
  }

  // ---- (c) NEGATIVE CONTROL — dynamic: a route whose moduleId is absent from the real set ----
  const allRoutes = routesWithModuleIds();
  const absent = allRoutes.filter((r) => !ids.includes(r.moduleId));
  if (absent.length === 0) {
    console.error(
      `[c] N/A — every one of the ${allRoutes.length} rebuilt routes' moduleIds is in the admin role's real set; ` +
        'the negative control cannot be constructed from the routes with this role. Evidence: the set covers ' +
        `${ids.length} modules and all ${allRoutes.length} gated routes. The guard's denial path needs a role ` +
        'that lacks some rebuilt screen to be negatively exercised.',
    );
  } else {
    const c = absent[0];
    await page.goto(`${DEV}/${c.route}`, { waitUntil: 'load', timeout: 15000 });
    await page.waitForTimeout(1500);
    const url = page.url();
    if (url.includes('reason=denied')) {
      pass(`(c) negative control ${c.route} (${c.moduleId}, absent from set) bounces with reason=denied`);
    } else {
      fail(`(c) negative control ${c.route} (${c.moduleId}) UNEXPECTED: ${url}`);
    }
  }

  // ---- (d) FAILURE PATH — bootstrap stubbed to fail ----
  const ctx2 = await browser.newContext();
  const p2 = await ctx2.newPage();
  await login(p2);
  await p2.route('**/privilege/role/**', (route) => route.abort());
  await p2.goto(`${DEV}/`, { waitUntil: 'load', timeout: 30000 });
  await p2.waitForTimeout(3000);
  await p2.goto(`${DEV}/operationTable`, { waitUntil: 'load', timeout: 15000 });
  await p2.waitForTimeout(1500);
  const url2 = p2.url();
  const bodyText = await p2.evaluate(() => document.body.innerText);
  if (!url2.includes('reason=permissionsUnavailable')) {
    fail(`(d) stubbed bootstrap did not render permissionsUnavailable: ${url2}`);
  } else {
    pass('(d) stubbed bootstrap renders reason=permissionsUnavailable');
  }
  if (!bodyText.includes('Your permissions could not be loaded')) {
    fail('(d) distinct permissionsUnavailable message missing');
  } else {
    pass('(d) distinct message shown ("Your permissions could not be loaded...")');
  }

  await browser.close();
  console.error(failures === 0 ? '\nRESULT: ALL PASS' : `\nRESULT: ${failures} FAILURE(S)`);
  process.exit(failures === 0 ? 0 : 1);
}

main().catch((e) => {
  console.error('FATAL', e);
  process.exit(1);
});
