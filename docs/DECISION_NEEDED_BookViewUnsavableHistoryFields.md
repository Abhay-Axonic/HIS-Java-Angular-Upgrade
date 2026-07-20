# Decision needed: `bookView`'s History/Diagnosis "editable" fields — real fix vs. interim cleanup

*Blueprint (docs only, no code applied). 2026-07-20. Follow-up on `docs/ANGULAR_PENDING_FINDINGS.md` entries 29-31, found during the Phase 4 first-batch Step 0/1 audit. This document includes a material correction to entries 29-31's own severity framing — read section 0 before anything else.*

*IMPLEMENTED 2026-07-20 on `fix/bookview-unsavable-history-fields` (held for review, not merged/pushed) — Task B (interim cleanup) approved and applied, with one further correction found during implementation: of the "4 standalone checkboxes" described in section 3 below, only `noSignificantVaccination` is actually live. `noSignificantMedical`, `noSignificantFamiliy`, and `noSignificantSurgical` were found, on direct inspection of the raw source while implementing, to already sit inside pre-existing, unclosed HTML comment blocks — already fully dead before this session touched anything, not merely low-signal as this document originally assumed. Only `noSignificantVaccination` received the proposed `disabled` attribute; the other three were left untouched (a proposed edit to them was made, then reverted, once the pre-existing comment was discovered — applying it would have nested a new comment inside an already-open one, prematurely closing it and making previously-dead markup live, a real bug). The six dead `ng-repeat` blocks (section 3(i)) were confirmed genuinely live markup, outside any comment, and were fixed as proposed. See the commit on that branch for full detail and verification.*

---

## 0. Correction to entries 29-31 — read this first

Entries 29-31 describe the History-tab and Diagnosis-tab "add new row" inputs as real, API-populated, genuinely editable fields a clinician could type into and silently lose. **That is not what the code does.**

Every one of the affected `ng-repeat` blocks' backing arrays is **never assigned anywhere** in `bookViewMasterController.js` or `book-view.html` — independently confirmed by grepping the full controller and view for all six variable names:

| `ng-repeat` block (`book-view.html`) | Backing array | Assigned anywhere? |
|---|---|---|
| Personal History add-row (lines 1109, 2999) | `historyPersonalList` | **No** |
| Family History add-row (1253, 3100) | `historyFamilyList` | **No** |
| Surgical History add-row (1347, 3251) | `historySurgicalList` | **No** |
| Medical History add-row (1624) | `historyMedicalList` | **No** |
| Travel History add-row (1869) | `historyTravelsList` | **No** |
| Diagnosis add-row (2226) | `dataListDiagnosis` | **No** |

Confirmed against the vendored Angular source itself (`resources/assets/css/angular.js:27485-27497`): `ngRepeat` over an `undefined` collection produces zero DOM elements, no error. **These six blocks render nothing in production today** — no input boxes, no add/remove buttons, no ICD-10/Surgery/Surgeon typeaheads. A clinician cannot type into them because they are not on the page, not because they're merely unwise to use.

What *does* render in the same tabs is genuinely read-only: the "Active"/"Resolved" summary tables (`personalHistoryCurrentList`, `familyHistoryCurrentList`, `surgicalHistoryCurrentList`, `medicalHistoryCurrentList`, `patientDiagnosisList`) use plain `{{ }}` interpolation with zero `ng-model` attributes anywhere.

Of entry 30's 23 dead bindings, ~18 belong to these six invisible blocks. The remaining ~5 (`getInvestigationDetails`, `getGroupServiceDetails`, `saveTemplateTransaction`, `setPackageOrderTabActive`, `printDischargeSumReport`) sit outside any dead array and ARE genuinely visible, clickable, and broken — entry 30's "silent console TypeError" description holds for those five, not for the History/Diagnosis ones.

**Still real:** four standalone checkboxes — `noSignificantMedical` (1600), `noSignificantFamiliy` (3080), `noSignificantSurgical` (3230), `noSignificantVaccination` (1996) — sit outside the dead repeats and render as functional-looking, clickable checkboxes. Clicking one only toggles a local flag gating a repeat that still renders nothing and is never sent anywhere. Harmless (no text lost, not a data field) but confusing UX.

**Bottom line: the specific data-loss scenario entry 29 describes — typing real documentation into a rendered field, it silently vanishing — cannot occur via these add-row blocks today, because the fields don't exist on screen.** The underlying capability gap (no save path anywhere in this controller) is real and matters for future work, but it is not an active, exploitable trap right now.

---

## 1. Root cause — two independent, stacked defects

**(a) No save path exists at all.** Confirmed independently: zero `PUT`/`DELETE` calls anywhere in the controller's 77 top-level `$scope.*` functions; the 23 `POST` calls that exist are all `GenericServiceParamHeader`/`BillingGenericService` GET-with-body fetches (e.g. `getComplaintsByPatientIdForBookView`), never data-persisting writes.

**(b) The would-be editable markup is dead on arrival.** The six blocks above iterate over variables `bookViewMasterController.js` never initializes — a second, independent bug layered on top of (a). Even if a save function existed, there's currently no rendered UI to trigger it from in these sections.

## 2. Real save function — is it safe to build? (Task A)

**Endpoints and DTOs line up cleanly for all 5 categories** — confirmed, not assumed. The exact same Java controller/service/DAO/DTO stack that serves each bookView GET also exposes a real, unwired save endpoint:

| Category | bookView's GET (wired) | Real save endpoint (exists, unwired) |
|---|---|---|
| Personal History | `getPatientPersonalHistoryForBookView` | `savePatientPersonalHistoryDetails` |
| Family History | `getPatientFamilyHistoryForBookView` | `savePatientFamilyHistory` |
| Surgical History | `getPatientSurgicalHistoryForBookView` | `savePatientSurgicalHistory` |
| Medical History | `getPatientMedicalHistoryForBookView` | `savePatientMedicalHistoryDetails` |
| Diagnosis | `getPatientDiagnosisByPatientIdForBookView` | `savePatientDiagnosis` |

**But wiring it safely is not a minimal change**, for reasons visible in the sibling `socialHistoryTabController.js`'s real save function (`saveHistoryPersonal`, ~lines 2380-2660):

- **A role-gated doctor-review workflow is embedded in the payload** (`roleId == 3` vs. else, `isReviewedFlag`/`isEnterInErrorStatus` fields) — and `bookView` itself already independently, partially replicates half of this exact logic (`getCurrentPatientpersonalHistory`, building a dead, never-consumed `personalHistoryCurrentListDoctor` array), meaning whoever wrote bookView was aware of this workflow and left it half-wired.
- **A "No Significant History" singleton-row special case** that must run before the generic per-row save and short-circuits it — real business logic, not save mechanics.
- **`$scope.roleId` in bookView is itself sourced from the same `sessionStorage.cookieEncounterObject`** flagged as unreliable in `docs/DECISION_NEEDED_BookViewStalePatientContext.md` (entry 28, companion document) — wiring a save function whose behavior branches on `roleId` on top of an already-flagged-unreliable role value compounds an unresolved risk.
- **Unanswered workflow question:** the OPD Coversheet screens (`OPDCoverSheetController.js`/`OPDcoversheetNewController.js`, 30k+ lines each) are the real, live editing surface for these same tables today. A second independent save path from `bookView` — a screen whose name and every other tab signal "read-only dashboard" — would create a second write path into the same rows with no visible reconciliation logic, unless someone confirms that's actually intended.

**Verdict: not a safe minimal fix.** Technically buildable (endpoints match), but porting the role-branching/validation/no-significant-history logic by reading it out of a sibling risks getting a real clinical business rule wrong. Not recommended now, per this project's standing caution against forcing a "real fix" without confidence.

## 3. Interim read-only fix (Task B) — the recommended path, and now smaller than originally scoped

Given section 0's correction, there's less to actually fix than entries 29-31 implied:

1. **The six dead `ng-repeat` blocks:** no `disabled`/`readonly` needed on inputs that never render. The honest fix is to delete the dead markup outright (or permanently `ng-if="false"` it) rather than "harden" something invisible — leaving inert markup in the DOM only invites a future editor to half-wire it back without realizing the save-endpoint gap in section 2.
2. **The four standalone "No Significant ___" checkboxes** — this codebase already has a precedent in this same file: `descriptiveExamination`/`sysDescriptiveExamination` textareas (lines 2760, 2831) use a plain `disabled` attribute to make an `ng-model`-bound element genuinely inert. Apply the same pattern to all four checkboxes (lines 1600, 1996, 3080, 3230). No other "read-only" UI convention exists anywhere in this codebase's legacy views to match instead.
3. **The 23 dead bindings from entry 30:** the ~18 tied to the six dead-array blocks resolve automatically once step 1 removes that markup. The remaining ~5 genuinely-visible ones are a separate decision, outside this document's scope — recommend hiding those too on the same logic, but leave that call to entry 30's own resolution.

No controller changes required for this fix — `bookViewMasterController.js` doesn't need to change, since it never wired these variables in the first place.

## 4. Overall recommendation

**Pursue the interim cleanup (Task B), not a real save function (Task A), and the urgency is lower than entries 29-31's original "most severe finding" framing.** The scenario that made this finding urgent — a clinician's real documentation silently vanishing — isn't currently reachable, since the fields aren't on the page. That doesn't make this a non-issue, but there's no active data-loss exposure forcing an urgent "real fix" that would otherwise require absorbing real workflow-correctness risk (the role-gated review logic, the concurrent-edit-with-coversheet question) to close a gap nobody is currently hitting.

## 5. Open questions needing clinical-workflow input

1. **Does anyone document Personal/Family/Surgical/Medical/Travel History or add a Diagnosis from `bookView` specifically, ever — or does every clinician use the OPD Coversheet screens, with `bookView` purely for cross-encounter review?** Determines whether Task A is worth building at all, independent of the risk discussion above.
2. **Is the role-gated "nurse enters, doctor reviews" workflow** (visible in the sibling controllers, partially-but-dead in bookView itself) **something `bookView` is meant to participate in at all?** Needs a clinical/product answer before any future real-save work, not a guess from reading sibling code.
3. Should the standalone checkboxes be hidden (recommended) or kept visible-but-inert as a "coming soon" signal? Recommend hiding — this codebase has no "coming soon" convention, and an inert checkbox with no explanation reads as broken, not promised.

## 6. Verification plan, if approved

Real browser automation (a UI-removal fix, no controller/backend logic touched — verification is "confirm nothing that used to render still renders, and everything that should still work still works"):
1. Load `bookView` for a real seeded patient/encounter with non-empty History/Diagnosis data.
2. Confirm the read-only "Active"/"Resolved" tables in all four History sub-tabs and Diagnosis tab still render real fetched rows, unchanged.
3. Confirm the removed add-row inputs/buttons/typeaheads are absent from the DOM in both the History tab and its Past Visit duplicate, and no new console errors appear.
4. Confirm the four checkboxes render `disabled` and do not toggle on click.
5. Full-page smoke check across all 19 first-level tabs to confirm nothing else was affected.
6. Log in `docs/BUSINESS_RULE_MANIFEST.md` if approved — arguably not a behavioral change (nothing observable changes for any user who could previously interact with these elements, since they never rendered), but flagged for the owner/Checker to confirm that classification rather than deciding it unilaterally here.

## 7. Severity/urgency framing

**Downgraded from entries 29-31's original framing, with evidence.** Real, confirmed defect (dead markup shipped alongside a genuinely missing save capability), worth cleaning up, but not an active data-loss risk today. The genuinely active risk on this screen remains entry 28 (`docs/DECISION_NEEDED_BookViewStalePatientContext.md`), unaffected by this investigation and still the higher-priority `bookView` issue. Recommend scheduling this as a normal cleanup item, not an emergency one.
