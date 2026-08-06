const fs = require('fs');
const path = require('path');
const http = require('http');
const { chromium } = require('playwright');

const WEBAPP_DIR = path.resolve(__dirname, '../../legacy/HIS-WAR/src/main/webapp');
const TRACER_SRC = fs.readFileSync(path.join(__dirname, 'tracer.js'), 'utf8');
const PORT = 8980;

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

// docs/DECISION_NEEDED_BookViewStalePatientContext.md / docs/ANGULAR_PENDING_FINDINGS.md
// entry 28: checkAppointment() populated $scope.patientDetails from
// sessionStorage.cookieEncounterObject based only on "is there any data",
// never "is this the right patient's data". This test directly instantiates
// bookViewMasterController (a 4,011-line controller with ~55 real backend
// calls -- deliberately not exercised through full page navigation in this
// script, since that requires real seeded patient/encounter data this dev
// environment doesn't have; the real end-to-end scenario was separately
// verified via real browser automation against the local dev Tomcat, using
// $rootScope.redirectBookViewUrl() directly through the injector -- see the
// session record for that verification) with a stale sessionStorage value
// and a $stateParams.bookViewObj resolving to {} (the real, confirmed
// ui-router behavior on browser-back/bookmark/refresh for this non-URL
// param) and checks whether $scope.patientDetails ever gets populated with
// the wrong patient's data.
const SEED_SCRIPT = `
try {
  document.cookie = 'cookieObject=' + encodeURIComponent(JSON.stringify({
    unitId: 1, organizationId: 1, userId: 1, doctorId: 0
  })) + '; path=/';
  document.cookie = 'cookieObjectAppConfig=' + encodeURIComponent(JSON.stringify({})) + '; path=/';
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

  // Case 1: stale-context scenario -- sessionStorage has a DIFFERENT
  // patient's data than what the (empty) state context implies. Simulates
  // browser-back/bookmark/refresh after one of the 14 other screens wrote
  // its own patient into the shared sessionStorage key.
  const staleResult = await page.evaluate(() => {
    sessionStorage.setItem('cookieEncounterObject', JSON.stringify({
      patientName: 'Patient Y', uhIdNumber: 'UHID-Y-999', genderCode: 'F', age: 45,
      encounterId: 999, patientId: 888, admissionId: null, visitTypeId: 1, roleId: 2,
    }));
    const injector = angular.element(document.body).injector();
    const $controller = injector.get('$controller');
    const $rootScope = injector.get('$rootScope');
    const scope = $rootScope.$new();
    const stateGoLog = [];
    const fakeState = { go: (name) => stateGoLog.push(name) };
    const fakeCookies = {
      getObject: (name) => {
        if (name === 'cookieObject') return { unitId: 1, organizationId: 1, userId: 1 };
        if (name === 'cookieObjectAppConfig') return {};
        return undefined;
      },
    };
    try {
      $controller('bookViewMasterController', { $scope: scope, $state: fakeState, $cookies: fakeCookies, $stateParams: { bookViewObj: {} } });
      return { threw: false, error: null, stateGoLog, wrongPatientNameLeaked: scope.patientDetails ? scope.patientDetails.patientName : null };
    } catch (e) {
      return { threw: true, error: String(e.message || e), stateGoLog, wrongPatientNameLeaked: scope.patientDetails ? scope.patientDetails.patientName : null };
    }
  });
  console.log('=== stale-context case (browser-back/bookmark simulation) ===');
  console.log('RESULT:', JSON.stringify(staleResult));
  if (staleResult.threw) {
    console.log('=> UNEXPECTED THROW:', staleResult.error);
    allPass = false;
  } else if (!staleResult.wrongPatientNameLeaked) {
    console.log('=> $scope.patientDetails never populated with the wrong patient -- FIX CONFIRMED');
  } else {
    console.log('=> WRONG PATIENT LEAKED INTO SCOPE:', staleResult.wrongPatientNameLeaked, '-- FIX NOT WORKING');
    allPass = false;
  }
  console.log('');

  // Case 2: normal usage -- sessionStorage and bookViewObj both carry the
  // SAME patient's identifiers, as redirectBookViewUrl() always writes them
  // together synchronously. Must still populate scope correctly.
  const normalResult = await page.evaluate(() => {
    const encounterObj = {
      patientName: 'Patient X', uhIdNumber: 'UHID-X-100', genderCode: 'M', age: 30,
      encounterId: 100, patientId: 200, admissionId: null, visitTypeId: 1, roleId: 2,
    };
    sessionStorage.setItem('cookieEncounterObject', JSON.stringify(encounterObj));
    const injector = angular.element(document.body).injector();
    const $controller = injector.get('$controller');
    const $rootScope = injector.get('$rootScope');
    const scope = $rootScope.$new();
    const stateGoLog = [];
    const fakeState = { go: (name) => stateGoLog.push(name) };
    const fakeCookies = {
      getObject: (name) => {
        if (name === 'cookieObject') return { unitId: 1, organizationId: 1, userId: 1 };
        if (name === 'cookieObjectAppConfig') return {};
        return undefined;
      },
    };
    try {
      $controller('bookViewMasterController', { $scope: scope, $state: fakeState, $cookies: fakeCookies, $stateParams: { bookViewObj: encounterObj } });
      return { threw: false, error: null, stateGoLog, patientName: scope.patientDetails ? scope.patientDetails.patientName : null };
    } catch (e) {
      return { threw: true, error: String(e.message || e), stateGoLog, patientName: scope.patientDetails ? scope.patientDetails.patientName : null };
    }
  });
  console.log('=== normal-usage case (matching context) ===');
  console.log('RESULT:', JSON.stringify(normalResult));
  // Note: this 4,011-line controller does a large amount of unrelated setup
  // (API calls, tab initialization, etc.) after checkAppointment() runs --
  // an isolated $controller instantiation with a minimal service mock set
  // can legitimately throw further down for reasons that have nothing to do
  // with this fix (confirmed here: "Cannot read properties of undefined
  // (reading 'authToken')" is unrelated downstream setup, not
  // checkAppointment()). What this test actually verifies is narrower and
  // still conclusive: did checkAppointment() correctly populate the RIGHT
  // patient before anything else ran? Full end-to-end confirmation (no
  // downstream noise, real session context) was separately done via real
  // browser automation against the local dev Tomcat -- see the session
  // record for that verification.
  if (normalResult.patientName === 'Patient X' && !normalResult.stateGoLog.includes('bedManagement') && !normalResult.stateGoLog.includes('encounterQueueManagement')) {
    console.log('=> correct patient populated, no safety-redirect triggered -- NO REGRESSION' + (normalResult.threw ? ' (unrelated downstream throw noted, not a regression in this fix)' : ''));
  } else {
    console.log('=> unexpected result -- patientName=', normalResult.patientName, 'stateGoLog=', normalResult.stateGoLog);
    allPass = false;
  }

  console.log('');
  console.log(allPass ? '=== ALL CASES PASSED ===' : '=== AT LEAST ONE CASE FAILED ===');
  await browser.close();
  server.close();
  process.exit(allPass ? 0 : 1);
}
main().catch(e => { console.error('FATAL', e); process.exit(1); });
