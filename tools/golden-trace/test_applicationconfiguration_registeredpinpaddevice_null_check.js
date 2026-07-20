const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8981;

const MIME = { '.html': 'text/html', '.js': 'application/javascript', '.css': 'text/css', '.json': 'application/json', '.png': 'image/png', '.ico': 'image/x-icon' };

function startStaticServer() {
  const server = http.createServer((req, res) => {
    let urlPath = decodeURIComponent(req.url.split('?')[0]);
    if (urlPath === '/') urlPath = '/index.html';
    const filePath = path.join(WEBAPP_DIR, urlPath);
    fs.readFile(filePath, (err, data) => {
      if (err) { res.writeHead(404); res.end(); return; }
      const ext = path.extname(filePath);
      res.writeHead(200, { 'Content-Type': MIME[ext] || 'application/octet-stream' });
      res.end(data);
    });
  });
  return new Promise((resolve) => server.listen(PORT, () => resolve(server)));
}

// docs/ANGULAR_PENDING_FINDINGS.md entry 34 /
// docs/DECISION_NEEDED_ApplicationConfigurationFallbackCrash.md:
// applicationConfigurationController.js -- the designated fallback screen
// for a missing cookieObjectAppConfig -- itself dereferenced that same
// cookie with zero guard. Confirmed the single live registration
// ("ApplicationConfiguration.Controller"), so this one fix protects all
// 52 real callers app-wide.
const SEED_SCRIPT = `
try {
  localStorage.setItem('permissions', JSON.stringify({
    modules: Array.from({length: 999}, (_, i) => ({ id: i + 1 }))
  }));
  document.cookie = 'cookieObject=' + encodeURIComponent(JSON.stringify({
    unitId: 1, organizationId: 1, userId: 1, doctorId: 0
  })) + '; path=/';
} catch (e) {}
`;

async function main() {
  const server = await startStaticServer();
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  await context.addInitScript({ content: SEED_SCRIPT });
  await context.addInitScript({ content: TRACER_SRC });
  const page = await context.newPage();

  await page.goto(`http://localhost:${PORT}/index.html`, { waitUntil: 'load', timeout: 30000 });
  await page.waitForFunction('!!window.__goldenTraceState', { timeout: 15000 });
  console.log('bootstrap OK, app fully loaded with normal session\n');

  let allPass = true;

  for (const appConfigCookie of [undefined, { registeredPinPadDevice: 'PIN-PAD-42' }]) {
    const label = appConfigCookie === undefined ? 'crash-state (cookieObjectAppConfig missing)' : 'normal-state (cookieObjectAppConfig set, real device configured)';
    const result = await page.evaluate(({ appConfigCookie, label }) => {
      const injector = angular.element(document.body).injector();
      const $controller = injector.get('$controller');
      const $rootScope = injector.get('$rootScope');
      const scope = $rootScope.$new();
      const fakeCookies = {
        getObject: (name) => {
          if (name === 'cookieObject') return { unitId: 1, organizationId: 1, userId: 1 };
          if (name === 'cookieObjectAppConfig') return appConfigCookie;
          return undefined;
        },
      };
      const stateGoLog = [];
      const fakeState = { go: (name) => stateGoLog.push(name) };
      try {
        $controller('ApplicationConfiguration.Controller', { $scope: scope, $cookies: fakeCookies, $state: fakeState });
        return { label, threw: false, error: null, stateGoLog, registeredPinPadDevice: scope.registeredPinPadDevice };
      } catch (e) {
        return { label, threw: true, error: String(e.message || e), stateGoLog, registeredPinPadDevice: scope.registeredPinPadDevice };
      }
    }, { appConfigCookie, label });

    console.log(`=== ${result.label} ===`);
    console.log('RESULT:', JSON.stringify(result));
    if (appConfigCookie === undefined) {
      if (result.threw) {
        console.log('=> CRASH CASE STILL THROWS:', result.error);
        allPass = false;
      } else if (result.registeredPinPadDevice === null) {
        console.log('=> does NOT throw, registeredPinPadDevice safely defaulted to null -- FIX CONFIRMED');
      } else {
        console.log('=> does NOT throw, but unexpected value:', result.registeredPinPadDevice);
        allPass = false;
      }
    } else {
      if (result.threw) {
        console.log('=> NORMAL CASE UNEXPECTEDLY THROWS:', result.error);
        allPass = false;
      } else if (result.registeredPinPadDevice === 'PIN-PAD-42') {
        console.log('=> real configured device value read correctly -- NO REGRESSION');
      } else {
        console.log('=> unexpected value:', result.registeredPinPadDevice);
        allPass = false;
      }
    }
    console.log('');
  }

  console.log(allPass ? '=== ALL CASES PASSED ===' : '=== AT LEAST ONE CASE FAILED ===');
  await browser.close();
  server.close();
  process.exit(allPass ? 0 : 1);
}
main().catch(e => { console.error('FATAL', e); process.exit(1); });
