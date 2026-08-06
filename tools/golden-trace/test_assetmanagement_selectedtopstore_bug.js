const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8977;

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

// Same technique as test_assigningpickerdispenser_storename_bug.js. Covers
// the 4 Asset Management screens found (docs/ANGULAR_PENDING_FINDINGS.md
// entry 16) to share the exact same "guarded but not returned" crash on
// sessionStorage.selectedTopStore, now fixed identically to
// assigningPickerDispenser. Each controller needs its own $stateParams
// mock to get past an earlier, unrelated pre-init gate before reaching the
// code under test -- provided explicitly below, not left to ambient defaults.
const SEED_SCRIPT = `
try {
  document.cookie = 'cookieObject=' + encodeURIComponent(JSON.stringify({
    unitId: 1, organizationId: 1, userId: 1, doctorId: 0
  })) + '; path=/';
} catch (e) {}
`;

const CASES = [
  { controller: 'viewAssetSchedulerController', stateParams: { assetDataObj: { itemNameId: 1, serialNo: 'S1' } } },
  { controller: 'preventiveScheduleWorklistController', stateParams: {} },
  { controller: 'createAssetSchedulerController', stateParams: { assetInfoObj: { itemNameId: 1, serialNo: 'S1' } } },
  { controller: 'detailScheduleWorklistController', stateParams: { assetDataObject: { assetScheduleId: 1, pmsType: 'InHouse' } } },
];

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

  for (const { controller, stateParams } of CASES) {
    for (const sessionStorageValue of [null, JSON.stringify({ storeName: 'Main Store', storeId: 7 })]) {
      const label = sessionStorageValue === null ? 'crash-state (selectedTopStore unset)' : 'normal-state (selectedTopStore set)';
      const result = await page.evaluate(({ controller, stateParams, sessionStorageValue, label }) => {
        if (sessionStorageValue === null) {
          sessionStorage.removeItem('selectedTopStore');
        } else {
          sessionStorage.setItem('selectedTopStore', sessionStorageValue);
        }

        const injector = angular.element(document.body).injector();
        const $controller = injector.get('$controller');
        const $rootScope = injector.get('$rootScope');
        const scope = $rootScope.$new();
        const fakeCookies = { getObject: () => ({ unitId: 1, organizationId: 1, userId: 1, storeId: 1 }) };
        const storePopupCalls = [];
        const realStorePopup = $rootScope.storePopup;
        $rootScope.storePopup = function () {
          storePopupCalls.push(true);
          return realStorePopup ? realStorePopup.apply(this, arguments) : undefined;
        };

        try {
          $controller(controller, { $scope: scope, $cookies: fakeCookies, $stateParams: stateParams });
          if (typeof scope.init === 'function') scope.init();
          return { controller, label, threw: false, error: null, storeId: scope.storeId, storePopupCalled: storePopupCalls.length > 0 };
        } catch (e) {
          return { controller, label, threw: true, error: String(e.message || e), storeId: scope.storeId, storePopupCalled: storePopupCalls.length > 0 };
        } finally {
          $rootScope.storePopup = realStorePopup;
        }
      }, { controller, stateParams, sessionStorageValue, label });

      console.log(`=== ${result.controller} :: ${result.label} ===`);
      console.log('RESULT:', JSON.stringify(result));
      if (sessionStorageValue === null) {
        if (result.threw) {
          console.log('=> CRASH CASE STILL THROWS:', result.error);
          allPass = false;
        } else if (result.storePopupCalled) {
          console.log('=> crash case: does NOT throw, correctly triggers storePopup() -- FIX CONFIRMED');
        } else {
          console.log('=> crash case: does NOT throw, but storePopup() not called -- unexpected');
          allPass = false;
        }
      } else {
        if (result.threw) {
          console.log('=> NORMAL CASE UNEXPECTEDLY THROWS:', result.error);
          allPass = false;
        } else if (result.storeId === 7 && !result.storePopupCalled) {
          console.log('=> normal case: storeId set correctly, popup NOT triggered -- NO REGRESSION');
        } else {
          console.log('=> normal case: unexpected result -- storeId=', result.storeId, 'popupCalled=', result.storePopupCalled);
          allPass = false;
        }
      }
      console.log('');
    }
  }

  console.log(allPass ? '=== ALL CASES PASSED ===' : '=== AT LEAST ONE CASE FAILED ===');
  await browser.close();
  server.close();
  process.exit(allPass ? 0 : 1);
}
main().catch(e => { console.error('FATAL', e); process.exit(1); });
