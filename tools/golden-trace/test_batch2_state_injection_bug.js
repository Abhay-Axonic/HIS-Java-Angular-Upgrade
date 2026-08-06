const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8975;

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

// Same technique as tools/golden-trace/test_orgmaster_bug.js: normal, full
// seed so bootstrap succeeds cleanly, then a surgical per-controller test
// via Angular's own $controller service with a LOCAL $cookies override that
// returns undefined -- exactly what "session cookie lost mid-session"
// looks like from each controller's own point of view -- without touching
// page navigation or ui-router's transition machinery.
const SEED_SCRIPT = `
try {
  document.cookie = 'cookieObject=' + encodeURIComponent(JSON.stringify({
    unitId: 1, organizationId: 1, userId: 1, doctorId: 0
  })) + '; path=/';
} catch (e) {}
`;

const CONTROLLERS_TO_TEST = ['AssetCategoryMaster.Controller', 'ImplantMaster.Controller'];

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

  for (const controllerName of CONTROLLERS_TO_TEST) {
    const result = await page.evaluate((name) => {
      const injector = angular.element(document.body).injector();
      const $controller = injector.get('$controller');
      const $rootScope = injector.get('$rootScope');
      const scope = $rootScope.$new();
      const fakeCookies = { getObject: function () { return undefined; } };
      const stateGoLog = [];
      const fakeState = { go: function (n) { stateGoLog.push(n); } };
      try {
        $controller(name, { $scope: scope, $cookies: fakeCookies, $state: fakeState });
        return { threw: false, error: null, stateGoLog: stateGoLog };
      } catch (e) {
        return { threw: true, error: String(e.message || e), stateGoLog: stateGoLog };
      }
    }, controllerName);

    console.log(`=== ${controllerName} ===`);
    console.log('RESULT:', JSON.stringify(result));
    if (result.threw) {
      console.log(`=> THREW: ${result.error}`);
    } else if (result.stateGoLog.includes('login')) {
      console.log(`=> did NOT throw, correctly called $state.go('login')`);
    } else {
      console.log(`=> did NOT throw, but did NOT call $state.go('login') either -- unexpected`);
    }
    console.log('');
  }

  await browser.close();
  server.close();
}
main().catch(e => { console.error('FATAL', e); process.exit(1); });
