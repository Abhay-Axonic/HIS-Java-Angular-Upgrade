# BACKEND STATIC COMPARISON — 2026-08-14 (corrected vs this project; no changes made)

Corrected: `~/Downloads/legacy-modernization-backend` (the origin project's legacy/).
This project: `legacy/` in the repo root. Same lineage.

## STEP 2 — HEADER CONTRACT (@RequestHeader)

**VERDICT: SUBSET — and the corrected backend did NOT normalise the forms.**

| | this project | corrected |
|---|---|---|
| controller files with @RequestHeader (method-grade) | 39 | 38 |
| endpoints whose methods declare @RequestHeader | 147 | 145 |
| distinct header names | 16 | 15 |

- **Forms are IDENTICAL — both mixed.** Both declare the underscore pair
  (`unit_id`, `organization_id`, `admission_id`, `doctor_id`, `encounter_id`,
  `gender_id`, `patient_id`, `type_id`) AND camelCase (`unitId`,
  `organizationId`, `organisationId`, `createdDate`, `fromDate`, `toDate`,
  `prescriptionDate`). The corrected backend did NOT normalise to camelCase —
  **the rebuild's interceptor both-forms fix (DQ-102) remains required; the
  underscore pair is NOT dead weight.**
- **The only difference: `userId` — present in this project, absent in the
  corrected.** Both `@RequestHeader(value="userId")` sites live in
  CircuitBreakerController (this project: Global/.../CircuitBreakerController.
  java:48,58 — the only userId decls) — **the whole controller is REMOVED in
  the corrected backend** (2 endpoints — matching the 147→145 endpoint delta).
- **The corrected ADDED no headers** (IN-CORR-NOT-THIS = empty) — nothing the
  rebuild must send that it does not already send.
- Note: the DQ-102 record's "29 controllers / 126 endpoints" used a different
  count method; my method-grade counts (39/147 vs 38/145) confirm the same
  relative difference (the corrected is this minus the CircuitBreaker pair).

## STEP 3 — RESPONSE SHAPES (the rebuild's endpoints, both backends)

**VERDICT: 327 of 328 IDENTICAL — the corrected backend did NOT normalise the
response keys (object/listObject).**

Method: the rebuild's HTTP calls in `new/src/app/features/` were extracted and
matched to the controller methods in both backends; because the controllers
delegate, the Response construction was traced into the ServiceImpls (the
layer where DQ-127's defect lived), with the paths resolved through a single
indexed walk per backend.

- Rebuild endpoints present in BOTH backends: **328**.
- **IDENTICAL shapes: 327.** The object/listObject/other classification
  matches per endpoint — including the insurance getById (the DQ-127 class):
  the corrected backend keeps the same listObject shape; **the rebuilt
  edit()'s `res.listObject?.[0] ?? res.object` guard remains required, and
  every rebuilt edit() without the guard stays at the same risk — nothing was
  normalised.**
- **1 remainder — a delegation-depth artifact, NOT a shape change:**
  `/unit/DoctorShare/getGlobalDoctorShareDepartmentMasterList` —
  this project's impl constructs `Response(ERROR, null, null, null, null)`
  inline; the corrected's impl delegates service→DAO
  (`iGlobalDoctorShareGroupWiseDao.getGroupWiseDepartmentList`) — the DAO
  constructs the same-lineage envelope. The corrected's extra delegation layer
  is structural, not a payload-key change.

## Implications

- The DQ-102 interceptor (sending both underscore + camelCase headers): still
  required — the corrected backend kept the mixed forms.
- The DQ-127 response-key guard: still required — the corrected backend kept
  the listObject-under-getById shape.
- The rebuild sends `userId` in its interceptor headers: harmless — no
  endpoint in the corrected backend declares it, and the rebuild's own header
  list can drop it without breaking anything (no action needed).
