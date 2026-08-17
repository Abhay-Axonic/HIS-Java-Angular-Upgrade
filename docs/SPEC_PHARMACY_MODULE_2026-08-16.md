# SPEC — PHARMACY MODULE (6 screens, batched)

SERVED-BRANCH: `fix/patient-type-details-status` @ 4e85ac99. The owner's
sidebar list — the corrected backend's Pharmacy module is the contract (the
corrected controllers quoted below). DATE CONVENTION: String dates via
formatLegacyNow (dd-M-yyyy HH:mm:ss — the registration/prescription
convention) unless a DTO says otherwise. Core scope: worklists are read-only;
the dispensing/issuing forms carry save but NO stock-level validation in the
first build.

1. **PHARMACOLOGY VALIDATION WORKLIST** (`#/pharmacologyValidaionWorklist` —
the legacy's typo'd sidebar name; the corrected's
`PharmacologyValidationControllerOP`, class `/api/pharmacy/op/pharmacology/validation`):
dropdowns (GET /dropdowns) + the validation worklist (POST /search) + the
validation save (**PATCH /save** — the status change; the corrected's method
is PATCH, not POST). Core: the dropdowns + the read-only worklist grid +
the save button (the PATCH). Deferred: the batch multi-select validation.

2. **DRUG ADMINISTRATION** (`#/drugAdministrator` — the corrected's
`DrugAdministrationController`, class
`/api/pharmacy/nursing/workstation/drug/administration`): the dropdowns
(GET /dropdown + /reasondropdown/{orgId}) + the administration worklist
(POST /search) + the administration save (POST /save — the DrugAdministrationRequest
family: the medication, dose, route, frequency, the reason). Core: the
worklist + the save form (no stock validation). Deferred: the administration
timeline, the barcode scan.

3. **PATIENT ISSUANCE RETURN** (`#/patientIssueReturn` — the corrected's
`PatientIssueReturnController`, class
`/api/pharmacy/nursing/workstation/patient/issue/return/medication`): the
dropdown (GET /dropdown/{unitId}) + the issue/return worklist (POST /search)
+ the against-issue search (POST /againstIssue/search) + the item detail
(GET /{unitId}/{id}). Core: the worklist + the return-against-issue form
(save; no stock validation). Deferred: the consumables variant
(PatientIssueReturnConsumableController).

4. **HOMECARE MEDICATION ENTRY** (`#/homecareMedicationEntry` — the legacy
state is COMMENTED OUT (app.js:7695, dead) and the corrected backend has NO
homecare-specific controller — FLAGGED: the build ports the medication-entry
form to the drug-administration save family (the same medication/DTO shape),
with the honest note that a homecare-specific backend would supersede it.
Core: the medication-entry form (item + dose + route + frequency + save).
Deferred: the homecare billing, the visit scheduling.

5. **WALK-IN SALES** (`#/walkinSales` — the corrected's `WalkInSalesController`,
class `/api/pharmacy/sales/walkins`): the dropdowns (GET /dropdowns/{unitId}/{orgId})
+ the items (GET /items/{screenId}/.../{itemId}/{tempBillId}) + the sale save
(POST — the class's bare POST). Core: the dropdowns + the item search + the
sale form (the item, the qty, the save; no stock validation). Deferred: the
billing/tariff integration, the returns.

6. **PENDING ISSUES** (`#/pendingIssues` — the corrected's
`PendingIssuesController`, class `/api/pharmacy/ip/pending/issues`): the
dropdowns (GET /dropdowns) + the pending-issues worklist (POST /search) +
the previous-issues (POST /pendingpreviousissues) + the item detail
(GET /{unitId}/{orgId}/{storeId}/{id}). Core: the dropdowns + the read-only
pending-issues grid. Deferred: the issue finalization (the picker/dispenser
writes).

All 6: tsc clean, routes registered (pharmacologyValidaionWorklist,
drugAdministrator, patientIssueReturn, homecareMedicationEntry, walkinSales,
pendingIssues), ONE commit. The worklist pattern follows the ER module's
(read-only grids); the forms carry save but no stock-level validation per the
owner.
