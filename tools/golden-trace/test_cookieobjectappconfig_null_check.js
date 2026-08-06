const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8978;

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

// docs/ANGULAR_PENDING_FINDINGS.md entry 17: preventiveScheduleWorklistController.js
// (scheduleWorklist) and pmScheduleController.js (PMSchedule) both dereferenced
// $cookies.getObject('cookieObjectAppConfig') with zero guard. Fix adds a guard
// matching the app's own designated shared helper for this exact cookie
// ($rootScope.checkCookies(), app.js ~line 1889) -- redirect to
// 'applicationConfiguration', not 'login' (the app has both patterns in use
// elsewhere; 'applicationConfiguration' was chosen as the more authoritative,
// semantically-correct precedent, confirmed with the owner before implementing).
const SEED_SCRIPT = `
try {
  document.cookie = 'cookieObject=' + encodeURIComponent(JSON.stringify({
    unitId: 1, organizationId: 1, userId: 1, doctorId: 0
  })) + '; path=/';
} catch (e) {}
`;

const CONTROLLERS = ['preventiveScheduleWorklistController', 'pmScheduleController'];

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

  for (const controllerName of CONTROLLERS) {
    for (const appConfigCookie of [undefined, { assetMaintenanceSpecialityId: 42 }]) {
      const label = appConfigCookie === undefined ? 'crash-state (cookieObjectAppConfig missing)' : 'normal-state (cookieObjectAppConfig set)';
      const result = await page.evaluate(({ controllerName, appConfigCookie, label }) => {
        // preventiveScheduleWorklistController also has the separate,
        // independently-branched entry-16 selectedTopStore bug (fixed on
        // fix/asset-management-selectedtopstore-null-check, not this branch).
        // Seed it so that unrelated bug doesn't mask the fix under test here.
        sessionStorage.setItem('selectedTopStore', JSON.stringify({ storeId: 1, storeName: 'Test Store' }));

        const injector = angular.element(document.body).injector();
        const $controller = injector.get('$controller');
        const $rootScope = injector.get('$rootScope');
        const scope = $rootScope.$new();
        const fakeCookies = {
          getObject: function (name) {
            if (name === 'cookieObject') return { unitId: 1, organizationId: 1, userId: 1 };
            if (name === 'cookieObjectAppConfig') return appConfigCookie;
            return undefined;
          },
        };
        const stateGoLog = [];
        const fakeState = { go: function (name) { stateGoLog.push(name); } };
        try {
          // Both controllers self-invoke $scope.init() once at the end of
          // their own constructor body -- calling it again here would
          // double-invoke and isn't representative of real usage.
          $controller(controllerName, { $scope: scope, $cookies: fakeCookies, $state: fakeState });
          return { controllerName, label, threw: false, error: null, stateGoLog, specialityId: scope.assetMaintenanceSpecialityId };
        } catch (e) {
          return { controllerName, label, threw: true, error: String(e.message || e), stateGoLog, specialityId: scope.assetMaintenanceSpecialityId };
        }
      }, { controllerName, appConfigCookie, label });

      console.log(`=== ${result.controllerName} :: ${result.label} ===`);
      console.log('RESULT:', JSON.stringify(result));
      if (appConfigCookie === undefined) {
        if (result.threw) {
          console.log('=> CRASH CASE STILL THROWS:', result.error);
          allPass = false;
        } else if (result.stateGoLog.includes('applicationConfiguration')) {
          console.log("=> does NOT throw, correctly redirects to 'applicationConfiguration' -- FIX CONFIRMED");
        } else {
          console.log('=> does NOT throw, but did NOT redirect to applicationConfiguration -- unexpected:', result.stateGoLog);
          allPass = false;
        }
      } else {
        if (result.threw) {
          console.log('=> NORMAL CASE UNEXPECTEDLY THROWS:', result.error);
          allPass = false;
        } else if (result.specialityId === 42 && !result.stateGoLog.includes('applicationConfiguration')) {
          console.log('=> normal case: assetMaintenanceSpecialityId set correctly, no redirect -- NO REGRESSION');
        } else {
          console.log('=> normal case: unexpected result -- specialityId=', result.specialityId, 'stateGoLog=', result.stateGoLog);
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
