# Decision needed: `bookView` renders clinical data from a shared session key with no identity check

*Blueprint (docs only, no code applied). 2026-07-20. Follow-up on `docs/ANGULAR_PENDING_FINDINGS.md` entry 28, found during the Phase 4 first-batch Step 0/1 audit. This document includes a material correction to entry 28's own severity framing — read section 0 before anything else.*

---

## 0. Correction to entry 28's severity mechanics — read this first

Entry 28 describes the browser-back/bookmark scenario as producing a **sustained**, silent wrong-patient render. Tracing the actual ui-router v0.2.18 mechanics (confirmed hash-mode routing — no `$locationProvider.html5Mode` anywhere in the app) shows this framing is not quite accurate, in a way that changes what the fix needs to do:

- `bookViewObj` is declared as a **non-URL** state param (`app.js:19080-19089`) — never encoded into the hash.
- `redirectBookViewUrl()` (`app.js:2532-2571`) is confirmed the **only** call site anywhere in the codebase that transitions into `bookView` (full-repo grep for `state.go("bookView"`, `ui-sref="bookView"`, `#/bookView` — zero other matches).
- Ui-router only inherits a param forward across a transition if it's declared on a shared ancestor state. `bookView` and its siblings (`encounterQueueManagement`, `OPDCoverSheet`, etc.) share no such parent.

**Net effect:** browser back, forward, refresh, a bookmark, or manually typing `#/bookView` all resolve `$stateParams.bookViewObj` to its declared default, `{}` — never to a stale previous value. The existing accidental gate in `checkAppointment()` (see below) does fire and redirect away in all these cases today.

**This does not mean there's no bug** — it means the real, verified defect is narrower than originally scoped, and the thing currently preventing the worst case is an **undocumented accident of ui-router internals**, not an intentional safeguard. It will silently stop protecting anyone the moment a second call site passes a stale `bookViewObj`, or the URL is ever given a param (the natural "fix" someone might reach for later). The proposed fix below makes the protection explicit and intentional instead of relying on this accident.

---

## 1. Root cause, exact code

**`checkAppointment()`** (`resources/assets/js/controller/opd/bookViewMasterController.js`, ~lines 130-166) populates `$scope.patientDetails` from `sessionStorage.cookieEncounterObject` based only on "is there any data at all," never "is this the right patient's data":

```js
$scope.checkAppointment=function(){
    $scope.encounterDetailsObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject')) || {};
    if($scope.encounterDetailsObj && Object.keys($scope.encounterDetailsObj).length > 0) {
        $scope.patientDetails.patientName = $scope.encounterDetailsObj.patientName;
        // ... uhIdNumber, genderCode, age, birthDate, patientId, encounterId, admissionId, etc.
    } else {
        $state.go('encounterQueueManagement');
        return false;
    }
};
$scope.checkAppointment();

/**Redirect Page Code*/
if(angular.equals({},$stateParams.bookViewObj)) {
    $state.go('bedManagement');
} else {
    $scope.bookViewObj = $stateParams.bookViewObj;
}
```

That `sessionStorage` key is written by 15 places total — the one correct entry point (`redirectBookViewUrl()`) plus 14 independent sites in `encounterQueueManagementController.js`, `doctorDeskController.js`, `counterWorklistController.js`, `casualtyWorklistController.js`, `encounterQueueEHCController.js`, and `progressNoteController.js`, all writing it for their own, unrelated navigation to `OPDCoverSheet` — confirmed, all 6 files read in full, every write site immediately followed by `$state.go("OPDCoverSheet"...)` or similar, never `bookView`. It's never cleared by ordinary navigation (only by a separate family of "finish tab"/save controllers, or `sessionStorage.clear()` on logout).

Critically, `redirectBookViewUrl()` already passes the full `encounterObj` — including `encounterId` and `patientId` — as `$state.go`'s `bookViewObj` param, alongside the sessionStorage write. That data is currently only used for an empty-object check, then discarded.

## 2. The exact failure scenario, walked through against current code

**Scenario A — transient flash (verified live defect):**
1. Clinician opens `bookView` for Patient X via `redirectBookViewUrl`. `sessionStorage.cookieEncounterObject` = X.
2. Same tab, they navigate through any of the 14 other flows and select Patient Y for `OPDCoverSheet`. `sessionStorage.cookieEncounterObject` is overwritten with Y.
3. Clinician returns to `#/bookView` (browser Back, retyped URL, bookmark). `$stateParams.bookViewObj` resolves to `{}` (per section 0).
4. `checkAppointment()` runs **before** the `{}` gate. It reads `sessionStorage` — still Y's data — and the presence check passes, so `$scope.patientDetails` is populated with **Patient Y's name, UHID, gender, age, birthdate, patientId, encounterId, admissionId** and bound into the DOM.
5. Only afterward does the separate `{}` gate fire and redirect to `bedManagement`. Until that transition completes, the DOM has already rendered Patient Y's data. **This is a real, verifiable window where the wrong patient's data paints on screen**, even though the user is ultimately bounced away with no explanation.

**Scenario B — sustained render (currently blocked, but by accident):** if any future code ever supplies a non-empty `bookViewObj` that doesn't match `sessionStorage`, nothing today would catch it — `checkAppointment()`'s check is presence-only, and the `{}` gate wouldn't fire since `bookViewObj` would be non-empty. Not reachable today, but zero defense if that assumption is ever violated.

## 3. Proposed fix — recommended minimal option, controller-only

```diff
 $scope.checkAppointment=function(){
     $scope.encounterDetailsObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject')) || {};
-    if($scope.encounterDetailsObj && Object.keys($scope.encounterDetailsObj).length > 0) {
+    var hasStateContext = !angular.equals({}, $stateParams.bookViewObj);
+    var contextMatches = hasStateContext
+        && $scope.encounterDetailsObj
+        && Object.keys($scope.encounterDetailsObj).length > 0
+        && String($scope.encounterDetailsObj.encounterId) === String($stateParams.bookViewObj.encounterId)
+        && String($scope.encounterDetailsObj.patientId) === String($stateParams.bookViewObj.patientId);
+
+    if(contextMatches) {
         $scope.patientDetails.patientName = $scope.encounterDetailsObj.patientName;
         // ... (unchanged)
     } else {
-        $state.go('encounterQueueManagement');
+        $state.go(hasStateContext ? 'bedManagement' : 'encounterQueueManagement');
         return false;
     }
 };
```

No change to `redirectBookViewUrl()` or the route definition needed — `bookViewObj` already carries `encounterId`/`patientId`; this is the first code that reads them for anything beyond an empty-object check.

**Why this closes both scenarios:**
- Scenario A: `hasStateContext` is `false` (bookViewObj resolves to `{}`) → `$scope.patientDetails` is never populated with Y's data at all → falls straight to the redirect. The flash is gone because scope is never bound to the wrong data in the first place.
- Scenario B: `hasStateContext` is `true` but the identifiers won't match a different patient's sessionStorage → still redirects safely. No longer an accident of ui-router internals — an explicit, intentional check.
- Normal usage: sessionStorage and `bookViewObj` are written from the same object in the same synchronous call, so they always match → behavior is byte-identical to today for every legitimate visit.

**What this does NOT do — the larger "fail-correct" alternative, not recommended now:** this is a fail-safe fix (redirect away, never render wrong data), not fail-correct (survive refresh and still show the right patient). Making bookmarking/refresh actually work would require a URL-embedded identifier (`url: '/bookView/:encounterId/:patientId'`, the exact pattern already used at `app.js:10266`'s `opdBilling` route) plus a real re-fetch-by-id path, since sessionStorage can't be trusted as the sole source of truth on a fresh reload. That's materially larger scope, not needed to close the verified defect, and better treated as a separate future enhancement if clinical workflow actually needs it (see open question 3 below).

## 4. The 14 other write-sites — confirmed unaffected

All 6 controller files behind the 15 write sites read in full. Every write site's own follow-up call targets `OPDCoverSheet`/`OPDcoversheetNew` or simply continues on the current screen — never `bookView`. The only `$state.go` anywhere targeting `bookView` is inside `redirectBookViewUrl()` itself. The proposed fix touches only `checkAppointment()`'s internal condition — none of the 14 sites' code, cookie shape, or navigation targets are read or modified.

## 5. Open decisions — need your input, possibly clinical-workflow input

1. **Silent redirect vs. explicit message.** Today (and in this fix) a mismatch redirects with zero explanation. Should the clinician see a message ("Your book-view session expired — please reselect the patient")?
2. **Redirect target.** Existing code already redirects to two different screens depending on which check fails (`encounterQueueManagement` if session data is entirely missing, `bedManagement` if `bookViewObj` is empty). This fix routes the new "mismatch" case to `bedManagement`, matching the existing empty-`bookViewObj` case. Is that the right landing screen, or would `encounterQueueManagement` (where a clinician can re-search) be more useful in both cases?
3. **Is the larger fail-correct fix (section 3) worth doing at all?** Does clinical workflow rely on bookmarking/refreshing this screen mid-session, or is "reselect the patient" an acceptable recovery path? If nobody bookmarks this screen, the minimal fix is likely sufficient indefinitely.
4. Entry 29 (editable-looking-but-non-saving fields) is a separate, already-logged issue — see the companion document `docs/DECISION_NEEDED_BookViewUnsavableHistoryFields.md` — not addressed here, flagged only because it compounds the "can this screen's data be trusted" question.

## 6. Verification plan, if approved

Real browser automation, not just code inspection:
1. Reproduce the stale-context scenario pre-fix: open `bookView` for Patient X via the real UI flow, navigate through to `OPDCoverSheet` for Patient Y, browser-Back to `#/bookView`. Confirm Patient Y's data is visible in `$scope.patientDetails`/the header before the redirect completes.
2. Apply the fix, repeat step 1. Confirm `$scope.patientDetails` is never populated with Y's data, and the redirect fires with no flash.
3. Confirm normal single-patient usage via `redirectBookViewUrl` is byte-identical to pre-fix behavior across all tabs.
4. Smoke-check at least one flow from each of the 6 writer controllers into `OPDCoverSheet`/`OPDcoversheetNew` — confirm untouched.
5. Log in `docs/BUSINESS_RULE_MANIFEST.md` if approved — this is a Behavioral Change (a case that previously rendered wrong-patient data, even transiently, now redirects instead), not a byte-identical refactor.

## 7. Severity and urgency

Patient-safety finding, narrowed but not dismissed by this investigation. The currently-reachable failure mode is a transient flash of wrong-patient demographic/clinical-summary data before a silent redirect — smaller than entry 28's original "sustained render" framing, but still a real clinician-facing event, and the only thing preventing the larger sustained-render scenario is an undocumented accident of framework internals with zero test coverage. Recommend treating as high-priority given the fix itself is small (one conditional, no route changes) even though the blast radius is narrower than first believed.
