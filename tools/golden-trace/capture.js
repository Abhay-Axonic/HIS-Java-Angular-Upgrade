/**
 * Golden-trace capture runner.
 *
 * Serves legacy/HIS-WAR/src/main/webapp as static files (read-only --
 * nothing under legacy/ is written to), injects tracer.js before any page
 * script runs, seeds a synthetic permission set so state resolve-guards
 * don't block navigation, then for each target state: loads the app fresh,
 * waits for Angular to bootstrap, drives navigation via $state.go(name)
 * (sidesteps needing to know the app's URL/hash-prefix scheme), lets it
 * settle, and writes the captured trace to golden-traces/<name>.json.
 *
 * No application code is touched or modified -- this only reads
 * legacy/HIS-WAR files over HTTP and injects a script at the browser-
 * automation layer.
 *
 * IMPORTANT, stated plainly: there is no live Java backend or database in
 * this environment. Every $http call in the traces captured this way WILL
 * fail (connection refused / 404) -- that's expected and still useful,
 * because it proves *which* calls a screen attempts, in what order, before
 * the network layer is even involved. It does NOT prove what the backend
 * would have returned, or exercise resolve blocks that depend on real
 * server data. Full end-to-end golden traces (with real API responses)
 * need this pointed at a live deployment instead of the static server
 * below -- swap BASE_URL/serveStatic for that when one exists.
 *
 * Usage:
 *   node capture.js states.json            # states.json: [{"name": "...", ...}, ...]
 *   node capture.js states.json --limit 20 # cap how many states to visit
 */
const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const OUT_DIR = path.resolve(__dirname, 'golden-traces');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8971;

const MIME = {
  '.html': 'text/html', '.js': 'application/javascript', '.css': 'text/css',
  '.json': 'application/json', '.png': 'image/png', '.jpg': 'image/jpeg',
  '.gif': 'image/gif', '.svg': 'image/svg+xml', '.woff': 'font/woff',
  '.woff2': 'font/woff2', '.ttf': 'font/ttf', '.ico': 'image/x-icon',
  '.eot': 'application/vnd.ms-fontobject',
};

function startStaticServer() {
  const server = http.createServer((req, res) => {
    let urlPath = decodeURIComponent(req.url.split('?')[0]);
    if (urlPath === '/') urlPath = '/index.html';
    const filePath = path.join(WEBAPP_DIR, urlPath);
    if (!filePath.startsWith(WEBAPP_DIR)) {
      res.writeHead(403);
      res.end();
      return;
    }
    fs.readFile(filePath, (err, data) => {
      if (err) {
        res.writeHead(404);
        res.end('not found: ' + urlPath);
        return;
      }
      const ext = path.extname(filePath);
      res.writeHead(200, { 'Content-Type': MIME[ext] || 'application/octet-stream' });
      res.end(data);
    });
  });
  return new Promise((resolve) => server.listen(PORT, () => resolve(server)));
}

// Broad synthetic permission set so this harness's own navigation isn't
// blocked by the app's real (data-driven) access-control resolve guards --
// test-harness seeding only, not a real user session. Module IDs 1-999
// covers every numeric ID this app's resolve blocks were observed to check.
// isSuperUser: 'Y' added 2026-07-17 after organizationMaster's resolve block
// (app.js, not its controller) turned out to gate on
// `$cookies.getObject('cookieObject').isSuperUser` -- without it, that one
// unconditional property read throws when the harness's synthetic cookie
// doesn't carry the field, a real defensive-coding gap worth flagging
// separately (see docs/PHASE2_PILOT_AUDIT.md), not something to route
// around silently. Setting it here is a test-harness convenience (there's
// no real backend to authenticate against) so future captures of any other
// superuser-gated screen aren't blocked by the same gap.
//
// permissions (localStorage) is safe to seed before page load. cookieObject
// is NOT: loginController.js (lines 44-48) unconditionally clears every
// browser cookie via `$cookies.getAll()` + `.remove()` on each key, every
// time it instantiates -- and every capture transits through the 'login'
// state first as part of normal app bootstrap (confirmed empirically, not
// assumed), so any cookie seeded before page load is wiped before the
// target state is ever reached. cookieObject is seeded separately, in
// captureOne() below, AFTER bootstrap completes -- i.e. after
// loginController's one-time wipe has already run.
const PERMISSIONS_SEED_SCRIPT = `
try {
  localStorage.setItem('permissions', JSON.stringify({
    modules: Array.from({length: 999}, (_, i) => ({ id: i + 1 }))
  }));
} catch (e) {}
`;

const COOKIE_OBJECT = {
  unitId: 1, organizationId: 1, userId: 1, doctorId: 0, isSuperUser: 'Y',
};
// A second cookie several controllers check via $rootScope.checkCookies()
// (app.js) in addition to cookieObject -- e.g. MISReportsController and
// inputOutputChartsController both redirect to 'applicationConfiguration'
// (a real, separate app state) when this one is missing, exactly like the
// cookieObject guard. Found empirically the same way: capture without it
// first, read the resulting state-change trace and console errors, then
// seed what was missing -- not assumed up front.
const COOKIE_OBJECT_APP_CONFIG = {
  defaultSelfTariffId: 1, isPreOrPostBilling: 'Pre', isAppointmentRequired: 'N',
};

async function captureOne(context, state) {
  const page = await context.newPage();
  const consoleMessages = [];
  page.on('console', (msg) => {
    if (msg.type() === 'error') consoleMessages.push(msg.text());
  });
  page.on('pageerror', (err) => {
    consoleMessages.push('pageerror: ' + err.message);
  });

  const result = {
    state: state.name,
    url: state.url,
    capturedAt: new Date().toISOString(),
    bootstrapped: false,
    navigationAttempted: false,
    navigationError: null,
    trace: null,
    rootScopeKeysAfter: null,
    pageConsoleErrors: null,
  };

  try {
    await page.goto(`http://localhost:${PORT}/index.html`, { waitUntil: 'load', timeout: 30000 });
    await page.waitForFunction('!!window.__goldenTraceState', { timeout: 15000 }).catch(() => {});
    result.bootstrapped = await page.evaluate('!!window.__goldenTraceState');

    if (result.bootstrapped) {
      // Seed cookieObject NOW, after loginController's one-time
      // clear-all-cookies has already run during the bootstrap redirect
      // through 'login' -- see the comment above COOKIE_OBJECT.
      await page.evaluate(({ cookieObj, appConfigObj }) => {
        document.cookie = 'cookieObject=' + encodeURIComponent(JSON.stringify(cookieObj)) + '; path=/';
        document.cookie = 'cookieObjectAppConfig=' + encodeURIComponent(JSON.stringify(appConfigObj)) + '; path=/';
      }, { cookieObj: COOKIE_OBJECT, appConfigObj: COOKIE_OBJECT_APP_CONFIG });

      result.navigationAttempted = true;
      try {
        await page.evaluate((name) => {
          window.__goldenTraceState.go(name);
        }, state.name);
      } catch (e) {
        result.navigationError = String(e.message || e);
      }
      // let the transition, resolve blocks, and controller instantiation settle
      await page.waitForTimeout(1200);

      result.trace = await page.evaluate('window.__goldenTrace');
      result.rootScopeKeysAfter = await page.evaluate(() => {
        const rs = window.__goldenTraceRootScope;
        if (!rs) return null;
        return Object.keys(rs).filter((k) => !k.startsWith('$') && typeof rs[k] !== 'function');
      });
    }
  } catch (e) {
    result.navigationError = String(e.message || e);
  }

  result.pageConsoleErrors = consoleMessages.slice(0, 20);
  await page.close();
  return result;
}

async function main() {
  const statesPath = process.argv[2];
  if (!statesPath) {
    console.error('Usage: node capture.js <states.json> [--limit N]');
    process.exit(1);
  }
  const limitIdx = process.argv.indexOf('--limit');
  const limit = limitIdx >= 0 ? parseInt(process.argv[limitIdx + 1], 10) : Infinity;

  const targets = JSON.parse(fs.readFileSync(statesPath, 'utf8')).slice(0, limit);
  fs.mkdirSync(OUT_DIR, { recursive: true });

  console.error(`Starting static server for ${WEBAPP_DIR} on :${PORT} ...`);
  const server = await startStaticServer();

  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  await context.addInitScript({ content: PERMISSIONS_SEED_SCRIPT });
  await context.addInitScript({ content: TRACER_SRC });

  console.error(`Capturing ${targets.length} state(s)...`);
  for (const state of targets) {
    process.stderr.write(`  ${state.name} ... `);
    const result = await captureOne(context, state);
    const outFile = path.join(OUT_DIR, `${state.name.replace(/[^a-zA-Z0-9._-]/g, '_')}.json`);
    fs.writeFileSync(outFile, JSON.stringify(result, null, 1));
    const ctrlCount = result.trace ? result.trace.controllerInstantiations.length : 0;
    process.stderr.write(`bootstrapped=${result.bootstrapped} controllers=${ctrlCount} err=${result.navigationError || 'none'}\n`);
  }

  await browser.close();
  server.close();
  console.error(`Done. Traces written to ${OUT_DIR}`);
}

main().catch((e) => {
  console.error('FATAL:', e);
  process.exit(1);
});
