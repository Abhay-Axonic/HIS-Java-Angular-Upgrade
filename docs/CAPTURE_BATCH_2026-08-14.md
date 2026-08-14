# FULL-ROUTE CAPTURE BATCH — 2026-08-14 (382 routes, live initial-load captures)

Method: one playwright session (the app's own session, token+cookie), navigate
every route in tools/route_paths_baseline.txt, record each screen's OWN API
calls + HTTP statuses from the performance entries (the app's real calls, not
probes), plus the final URL (bounces). Served branch during the run:
fix/insurance-master-audit-fields @ a06c9a9d. Results file: /tmp/capture_run1.txt.

## The counts

| class | count | meaning |
|---|---|---|
| clean | 294 of 382 | every API call on initial load returned 200 |
| bad | 30 of 382 | at least one non-200 call (see below) |
| no-call | 45 of 382 | no API calls on initial load (markup/static forms, or calls fire only on interaction) |
| bounced | 13 of 382 | accessDenied (module gate / appConfigMissing — the known env classes) |
| errors | 0 | no navigation crashes |

## The bad list — triaged buckets

**405 route-template artifacts (NOT findings — the baseline routes carry literal
:param placeholders that can't resolve):** display/:orgId/:unitId/:cookieskioskId/:userId,
inPatientFeedback/:organizationId/..., outPatientFeedback/:organizationId/...,
webAppointmentBooking/:orgId/:unitId. The NaN/placeholder values in the captured
URLs are the route templates, not the app.

**KNOWN (already ledged):** ambulanceMaster's 404 (the getCountAmbulanceTypeList
count-404, DQ-092 — catchError-degraded).

**REAL CANDIDATES (24 screens — triage OWED, per-screen verdict pending):**
- 500s (16): QMScounterMaster (getQMSCounterMasterList/Count), assetReturnAcceptanceDetails,
  assetTypeMaster (get list), autoServicePostingConfiguration (/config/billing/list/1/1),
  correctiveMaintenanceReturnWorklist, correctiveMaintenanceScheduleWorklist,
  detailsAssetIssueAcceptance, detailsAssetReturn, detailsFixedAssetMaster,
  fixedAssetMaster (/api/unit/fixed/asset/), implantMaster (/api/globals/item/
  implantItemMaster/unitId/1 — a LITERAL 'unitId' in the path), linenIssueAcceptanceDetails,
  linenIssuePendingDetails, notesMaster, parameterMaster (LIS), shiftClosing (400 getRefundList).
- 404s (10): bookAppointment (getActivePrefixList/), chargeConversion (400 getVisitTypeList),
  detailsConsignmentManagerDashboard (store list), detailsCorrectiveMaintenance
  (getSerialNoByItemId//1/1 — empty item id on load), dueDepositReport, modalityTypeMaster
  (List + Count), mpShahEncounterList/Summary/PatientList (the three getEncounter*
  endpoints), 
- The 400/404/405 shapes need the per-screen trace (probe-shape vs defect) before
  any verdict — the capture is the HYPOTHESIS list, not the finding.

## TRIAGE RUN 2 (2026-08-14, per-candidate fetch + source/jar verification)

| verdict class | screens | evidence |
|---|---|---|
| REBUILD-SIDE PHANTOM ENDPOINT (the path exists ONLY in the rebuild's service — zero matches in the legacy SOURCE and the DEPLOYED jars, -a binary grep) | dueDepositReport (/api/billing/dueDepositReport), modalityTypeMaster (getModalityTypeMasterList), correctiveMaintenanceReturnWorklist (getReplaceDetailOnReturnByMultipleCriteria), mpShahEncounterList/Summary/PatientList (getEncounterListByDateRange etc. — the encounter list exists under a different path in AdmissionRequestController) | 404 non-json on every fetch; grep -rla on the deployed {Billing,Global,ADT,AssetManagement,Pharmacy,OPD} jars = 0 hits |
| TRAILING-SLASH / PARAM 404s (the endpoint EXISTS) | bookAppointment (getActivePrefixList/ — WebAppointmentController has the mapping; the trailing slash 404s), detailsConsignmentManagerDashboard (getStoreListById/ — GetStoreListUsingSession exists), detailsCorrectiveMaintenance (getSerialNoByItemId//1/1 — the EMPTY item id on load; with a real id it returns 200 'Added Successfully.' — a mislabeled message on an empty READ, all CM tables 0, NO write landed) | the fetch with the real param returns 200 |
| 500s — BACKEND class (SQL/DAO check OWED) | assetTypeMaster, fixedAssetMaster, autoServicePostingConfiguration, detailsAssetIssueAcceptance, detailsAssetReturn, detailsFixedAssetMaster, linenIssueAcceptanceDetails, linenIssuePendingDetails, parameterMaster | ERR- refs captured (e29fc6d1, 32f3254f, 0a00d2f5, f431aacb, 8d34ab13, 1a7fc25f, 8d0456cb, 44442e8d, 9d99b88c); SQL errors went to the lost log4j stream — the DAO-side check is the next step |
| PROBE-SHAPE (400/405 on the minimal payload — NOT a verdict) | QMScounterMaster, correctiveMaintenanceScheduleWorklist, notesMaster, shiftClosing | 400 'Invalid parameters' / 405 — the app's own call shapes differ |
| TRIAGED EARLIER | implantMaster (0 rows for the filter — the 500 is query-level, entity-vs-DDL drift candidate), ambulanceMaster (KNOWN, DQ-092), the 4 route-template 405s (artifacts) | — |
