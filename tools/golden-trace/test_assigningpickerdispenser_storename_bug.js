const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8976;

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

// Same technique as tools/golden-trace/test_orgmaster_bug.js /
// test_batch2_state_injection_bug.js: normal, full seed so bootstrap
// succeeds cleanly, then a surgical per-controller test via Angular's own
// $controller service.
//
// This bug (docs/ANGULAR_PENDING_FINDINGS.md entry 9) isn't a $cookies/
// $state DI bug -- it's a real sessionStorage.getItem('selectedTopStore')
// read dereferenced before its null check. $cookies is given a REAL,
// valid cookieObject here (unlike the $state-DI tests) so the controller
// gets past its own login guard and actually reaches the code under test;
// sessionStorage itself is set/cleared directly in the page before each
// $controller() call, since it isn't an injectable Angular service.
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

  async function runCase(label, sessionStorageValue) {
    const result = await page.evaluate(({ label, sessionStorageValue }) => {
      if (sessionStorageValue === null) {
        sessionStorage.removeItem('selectedTopStore');
      } else {
        sessionStorage.setItem('selectedTopStore', sessionStorageValue);
      }

      const injector = angular.element(document.body).injector();
      const $controller = injector.get('$controller');
      const $rootScope = injector.get('$rootScope');
      const scope = $rootScope.$new();
      // Real, valid cookieObject -- this test is NOT about the login
      // guard, it needs to get past it to reach the code under test.
      const fakeCookies = {
        getObject: function () {
          return { unitId: 1, organizationId: 1, userId: 1, storeId: 1 };
        },
      };
      const storePopupCalls = [];
      const realStorePopup = $rootScope.storePopup;
      $rootScope.storePopup = function () {
        storePopupCalls.push(true);
        return realStorePopup ? realStorePopup.apply(this, arguments) : undefined;
      };

      try {
        $controller('AssigningPickerDispenserController.Controller', {
          $scope: scope,
          $cookies: fakeCookies,
        });
        return {
          label,
          threw: false,
          error: null,
          storeName: scope.storeName,
          storePopupCalled: storePopupCalls.length > 0,
        };
      } catch (e) {
        return { label, threw: true, error: String(e.message || e), storeName: scope.storeName, storePopupCalled: storePopupCalls.length > 0 };
      } finally {
        $rootScope.storePopup = realStorePopup;
      }
    }, { label, sessionStorageValue });

    console.log(`=== ${result.label} ===`);
    console.log('RESULT:', JSON.stringify(result));
    console.log('');
    return result;
  }

  // Case 1: crash state -- nothing populated in sessionStorage.selectedTopStore
  // this session (e.g. a multi-store user who hasn't been through
  // store-selection yet). Before the fix: throws. After: should not throw,
  // and should call storePopup() (the popup that was always supposed to fire
  // here but was previously dead code, unreachable because of the crash).
  const crashCase = await runCase('crash-state (selectedTopStore unset)', null);

  // Case 2: normal state -- a real store already selected this session.
  // Must behave identically before and after the fix: no throw, storeName
  // set correctly, popup NOT triggered.
  const normalCase = await runCase(
    'normal-state (selectedTopStore set)',
    JSON.stringify({ storeName: 'Main Pharmacy Store', storeId: 7 }),
  );

  console.log('=== Verdict ===');
  if (crashCase.threw) {
    console.log('CRASH CASE: still throws ->', crashCase.error);
  } else if (crashCase.storePopupCalled) {
    console.log('CRASH CASE: does NOT throw, correctly triggers storePopup() -- FIX CONFIRMED');
  } else {
    console.log('CRASH CASE: does NOT throw, but storePopup() was NOT called -- unexpected');
  }
  if (normalCase.threw) {
    console.log('NORMAL CASE: unexpectedly throws ->', normalCase.error);
  } else if (normalCase.storeName === 'Main Pharmacy Store' && !normalCase.storePopupCalled) {
    console.log('NORMAL CASE: storeName set correctly, popup NOT triggered -- NO REGRESSION');
  } else {
    console.log('NORMAL CASE: unexpected result -- storeName=', normalCase.storeName, 'popupCalled=', normalCase.storePopupCalled);
  }

  await browser.close();
  server.close();
}
main().catch(e => { console.error('FATAL', e); process.exit(1); });
