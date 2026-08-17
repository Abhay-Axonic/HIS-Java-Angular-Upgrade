# SPEC — LAB MODULE (14 screens) + RADIOLOGY MODULE (5 screens)

SERVED-BRANCH: `fix/patient-type-details-status` @ dbfb7c8c. Both modules are
order-driven: orders placed in the encounter flow to the lab/radiology
worklists. Core scope: worklists read-only; the result-entry forms carry
save. DATE CONVENTION: String dates via formatLegacyNow (the registration/
prescription convention, NOT the encounter's Long). The lab's contract is the
corrected LIS module; the radiology's is the corrected RadiologyOrdersListDto
family + the OPD's verified getListOfRadiologyTests endpoint.

## LAB (14 — the legacy's lab/LIS states, all REAL screens)

1. **LAB HOME SAMPLE COLLECTION** (`#/labHomeSampleCollection`) — the sample
collection home (the owner's Sample Collection; the legacy's Laboratory +
Radiology tabs live here): the sample list via the LIS
`LabCentralReceivingController` (class `/LIS/centralReceiving`,
GET getSampleListForInOutPatient). Core: the sample worklist grid. Deferred:
the collection write, the barcode.
2. **SAMPLE ACCEPTANCE** (`#/sampleAcceptance`) — the acceptance worklist:
`/LIS/centralReceiving` GET getSampleListForInOutPatient + POST
acceptOrRejectSample. Core: the read-only acceptance grid. Deferred: the
accept/reject write.
3. **CENTRIFUGATION WORKLIST** (`#/centrifugationWorkList`) — `/LIS/centralReceiving`
GET getCentrifugationWorklist. Core: the read-only grid.
4. **SAMPLE PROCESSING WORK LIST** (`#/sampleProcessingWorkList`) — the
processing ladder's worklist (the LIS transaction family). Core: read-only.
5. **WORKLIST FOR REPORT ENTRY** (`#/worklistForReportEntry`, the owner's
Report Entry Work List) — the result-entry worklist: the LIS
`BioChemResultEntryController` (class `/LIS/BioChemistry`, POST /ResultEntry +
/ResultEntry/Count). Core: the read-only worklist. Deferred: the entry write.
6. **CONSOLIDATED RESULT ENTRY** (`#/consolidatedResultEntry`) — the
consolidated result entry: the biochem ResultEntry family. Core: the
worklist + the entry form (save). Deferred: the test-level validation.
7. **CONSOLIDATED REPORT VALIDATION** (`#/consolidatedReportValidation`) — the
validation ladder (`BiochemValidationWorklistController` family). Core:
read-only.
8. **CONSOLIDATED REPORT RELEASE** (`#/consolidatedReportRelease`) — the
release ladder (`BiochemReportReleaseController`). Core: read-only.
9. **SEND TO CR** (`#/sendToCR`) — the central-receiving send list
(`/LIS/centralReceiving`). Core: read-only.
10. **SAMPLE RECOLLECTION** (`#/sampleRecollection`) — the recollection list.
Core: read-only.
11. **CONFIGURATION WORKLIST** (`#/configurationWorkList`) — the lab
configuration worklist. Core: read-only.
12. **MICROSCOPIC EXAMINATION WORK LIST** (`#/microscopicExaminiationWorkList`)
— the microscopic examination ladder (the legacy typo ported). Core:
read-only.
13. **IN PATIENT** (`#/inPatient`) — the IP sample list
(`/LIS/centralReceiving` getSampleListForInOutPatient visitTypeId=IP). Core:
read-only.
14. **OUT PATIENT** (`#/outPatient`) — the OP sample list (visitTypeId=OP).
Core: read-only.

## RADIOLOGY (5 — the legacy's radiology states, all REAL)

15. **RADIOLOGY WORKBENCH** (`#/radiologyWorkbench`) — the radiology order
workbench: the order list via POST `/api/opd/getListOfRadiologyTests` (the
verified corrected endpoint) + the patient autofill search. Core: the
read-only order grid.
16. **RADIOLOGY RESULT ENTRY** (`#/radiologyResultEntry`) — the result-entry
form: the CommonRadiologyDto-aligned payload; the save posts to the
radiology result-entry family (flagged: the exact save path needs the
corrected radiology controller's read — the RadiologyOrdersListDto /
CommonRadiologyDto are the contract). Core: the worklist + the result form
(save). Deferred: the image/attachment upload.
17. **RADIOLOGY RESULT VALIDATION** (`#/radiologyResultValidation`) — the
validation ladder. Core: read-only.
18. **RADIOLOGY REPORT VALIDATION** (`#/radiologyReportValidation`) — the
report-validation ladder. Core: read-only.
19. **RADIOLOGY REPORT RELEASE WORK LIST** (`#/radiologyReportReleaseWorkList`)
— the release ladder (radiologyReportReleaseController). Core: read-only.

All 19: tsc clean, routes registered (the legacy state names), TWO commits
(LAB in one, RADIOLOGY in one), each verified with git show.
