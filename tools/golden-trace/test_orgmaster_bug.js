const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8974;

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

// Normal, full seed -- bootstrap should succeed cleanly and completely.
const SEED_SCRIPT = `
try {
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
  console.log('bootstrap OK, app fully loaded with normal session');

  // Surgical test: directly ask Angular's $controller service to instantiate
  // organizationController with a LOCAL override of $cookies that returns
  // undefined for cookieObject -- exactly what "session cookie lost
  // mid-session" looks like from the controller's point of view -- without
  // touching page navigation, real cookies, or ui-router's transition
  // machinery (which is what caused the earlier, unrelated hang).
  const result = await page.evaluate(() => {
    const injector = angular.element(document.body).injector();
    const $controller = injector.get('$controller');
    const $rootScope = injector.get('$rootScope');
    const scope = $rootScope.$new();
    const fakeCookies = { getObject: function () { return undefined; } };
    const stateGoLog = [];
    const fakeState = { go: function (name) { stateGoLog.push(name); } };
    try {
      $controller('organizationController', { $scope: scope, $cookies: fakeCookies, $state: fakeState });
      return { threw: false, error: null, stateGoLog: stateGoLog };
    } catch (e) {
      return { threw: true, error: String(e.message || e), stateGoLog: stateGoLog };
    }
  });

  console.log('RESULT:', JSON.stringify(result, null, 1));
  if (result.threw) {
    console.log('=> Controller instantiation THREW:', result.error);
  } else if (result.stateGoLog.includes('login')) {
    console.log('=> Controller instantiation did NOT throw, and correctly called $state.go(\'login\')');
  } else {
    console.log('=> Controller instantiation did NOT throw, but did NOT call $state.go(\'login\') either -- unexpected');
  }

  await browser.close();
  server.close();
}
main().catch(e => { console.error('FATAL', e); process.exit(1); });
