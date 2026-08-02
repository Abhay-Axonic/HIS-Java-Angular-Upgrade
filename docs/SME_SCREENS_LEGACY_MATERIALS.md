# SME-Gated Screens — Legacy Review Materials (for owner's dedicated domain review)

Prepared 2026-08-02, per owner request: direct access to the CURRENT legacy screens for the owner's
own pre-build domain audit. All paths/URLs verified from `legacy/HIS-WAR/.../config/app.js` state
registrations and live controller/template reads. No Step 0-7 work has been started on any of these.

Access: legacy app at `http://localhost:8080/#/<state>` — login `Admin` / `12345`, org DEMO / unit DEMO.

---

## 1. allergyTypeMaster — CLINICAL-SAFETY (highest stakes)

- **Legacy URL:** `http://localhost:8080/#/allergyTypeMaster`
- **State:** `allergyTypeMaster` (config/app.js) | **Template:** `views/master/GlobalMaster/allergy-type-master.html` | **Controller:** allergy-type master controller
- **Backend:** `AllergyTypeMasterDaoImpl` (Global) — entity `adt.m_allergy_type_master` (check exact table name); `allergyTypeId` is a real FK on `AllergyPatientMapper` (patient allergy record → coversheet alert) and `AntibioticTestDoseDto` (pharmacy nursing workstation)
- **Fields (ng-model, from template):**
  - `common.allergyTypeDesc` (Description)
  - `common.allergyTypeName` (Name)
  - `common.isGeneric` (Is Generic)
  - `info.status` (A/I toggle)
  - `searchKeyword`, `noOfRecordsPerPage`
- **Domain questions for your review (from SME_GAP_ANALYSIS.md):** Are these 3 fields the complete allergy-type taxonomy? Additional types/severities/contraindication flags the system should surface but doesn't?
- **⚠ Security note (verified this session):** `AllergyTypeMasterDaoImpl` has **0 VerifiedIdentityService refs** — `updateAllergyTypeMaster`/`updateAllergyTypeMasterStatus` are pure `findById(dto.getAllergyTypeId())` with no org check (BR-143 shape). NOT batch-clean until fixed. Flagged, not touched.

## 2. doseMaster — CLINICAL-SAFETY (medication dosing)

- **Legacy URL:** `http://localhost:8080/#/doseMaster`
- **State:** `doseMaster` (config/app.js) | **Template:** `views/master/GlobalMaster/dose-master.html`
- **Backend:** `DoseMasterDaoImpl` (Global, `org/common`) — `doseId` is the real FK populating dose selection on `MedicationModel`/medication orders (every medication dose dropdown)
- **Fields (ng-model, from template):**
  - `common.doseCode` (Code)
  - `common.doseDesc` (Description)
  - `info.status` (A/I toggle)
  - `searchKeyword`, `noOfRecordsPerPage`
- **Domain questions for your review:** Does the current dose taxonomy match clinical practice? Missing dose units/formulations?
- **⚠ Security note (verified this session):** `DoseMasterDaoImpl` has **0 VerifiedIdentityService refs** — `updateDoseMaster`/`updateDoseMasterStatus` are pure `findById(dto.getDoseId())` with no org check (BR-143 shape). NOT batch-clean until fixed. Flagged, not touched.

## 3. routeAdministration — HEDGED DOMAIN-ADJACENT (lighter review)

- **Legacy URL:** `http://localhost:8080/#/routeAdministration`
- **State:** `routeAdministration` (registered inline in `routeOfAdministrationController.js` config block, line 235) | **Template:** `views/master/GlobalMaster/AssetManagenent/Master/route-of-adminstration.html`
- **Backend:** `RouteOfAdministrationDao` (Pharmacy, `routeOfAdministration/`) — reference data consumed by `addNewOrderSetMaster` (clinical order-set builder)
- **Fields (ng-model, from template):**
  - `vm.model.code` (Code)
  - `vm.model.description` (Description)
  - `vm.model.search`, `vm.model.pagination.pageSize`, `routeOfAdministrationData.status`
- **Security status:** ✅ BR-139-remediated (merged 2026-07-31, commit `36fce20c`/merge `4d14a46d`). The VerifiedIdentityService check lives in the **service layer** (`RouteOfAdministrationService.java` — 1 ref, verified this session), not the DAO; the DAO shows 0 refs by design (same service-layer pattern as BR-128/BR-135). Backend is batch-clean.
- **Your domain question (earlier):** Do route-of-administration values match clinical practice? Routes missing/unused?

## 4. formulationTypeROAMaster — HEDGED DOMAIN-ADJACENT (lighter review)

- **Legacy URL:** `http://localhost:8080/#/formulationTypeROAMaster`
- **State:** `formulationTypeROAMaster` (registered inline in `formulationTypeROAMasterController.js` config block, line 343) | **Template:** `views/master/GlobalMaster/AssetManagenent/Master/formulation-type-ROA-master.html`
- **Backend:** `FormulationTypeAndROADao` (Pharmacy, `formulationTypeAndROA/`) — same order-set consumer as routeAdministration
- **Fields (ng-model, from template):**
  - `vm.model.formulationType` (Formulation Type select)
  - `item.formRoa` (per-item ROA mapping rows)
  - `roa.status`, `vm.model.search`, `vm.model.noOfRecordsPerPage`
- **Security status:** ✅ BR-139-remediated (same commit as routeAdministration). VerifiedIdentityService lives in the **service layer** (`FormulationTypeAndROAService.java` — 1 ref, verified this session). Backend is batch-clean.
- **Your domain question (earlier):** Do formulation-type + ROA mappings match clinical practice per drug type?

## 5. corporateMasterGlobal — BILLING-ACCURACY (billing domain framework)

- **Legacy URL:** `http://localhost:8080/#/corporateMasterGlobal`
- **State:** `corporateMasterGlobal` (config/app.js line 8027) | **Template:** `views/master/GlobalMaster/corporate-master-global.html` | **Controller:** `controller/billing/unit/corporateMasterController.js`
- **Backend:** `CompanyMasterDaoImpl`/`CompanyMasterServiceImpl` (Global) — **BR-128-remediated** (1 VerifiedIdentityService ref in service; sweep-confirmed "Already fixed")
- **Fields (ng-model, from template):** `companyCode`, `companyDesc`, `address`, `countryId`, `stateId`, `cityId`, `districtId`, `areaId`, `zipcode`, `emailId`, **`creditLimit`**, **`creditDays`**, `paymentTerms`, `generalLedgerName`, `arCode`, `arCodeId`, `blockReportPrint`, `startDate`, `endDate`, + employee/next-of-kin sub-workflow (data.dobKin etc.)
- **Validation (from controller, lines 806-1161):** `creditLimit == ""` → blocked (save); `startDate`/`endDate` required; `companyCode`/`companyDesc`/`address`/`countryId` required; stateId check commented out
- **Billing domain framework to apply (per our earlier conversation):**
  1. **Role-based edit restriction with mandatory justification notes** — check whether corporate master edits are role-gated and whether justification is captured
  2. **Credit-limit correctness** — is `creditLimit`/`creditDays` calculated/validated correctly (monetary-cap input to billing)
  3. **Package inclusion/exclusion/reallocation logic** — how the corporate entity's billing config interacts with packages
  4. **Co-pay calculation correctness** — corporate co-pay paths
- **Security status:** ✅ batch-clean (BR-128). The only one of the 5 that clears the backend gate today.

---

## Cross-cutting security status (this session's verified reads)

| Screen | Backend | VerifiedIdentityService | Batch-clean? |
|---|---|---|---|
| corporateMasterGlobal | CompanyMasterServiceImpl | ✅ 1 ref (BR-128) | ✅ YES |
| allergyTypeMaster | AllergyTypeMasterDaoImpl | ❌ 0 refs | ⛔ until fixed |
| doseMaster | DoseMasterDaoImpl | ❌ 0 refs | ⛔ until fixed |
| routeAdministration | RouteOfAdministrationService | ✅ 1 ref (BR-139, service layer) | ✅ YES |
| formulationTypeROAMaster | FormulationTypeAndROAService | ✅ 1 ref (BR-139, service layer) | ✅ YES |

Owner review blocks: allergyTypeMaster + doseMaster (full pre-build domain audit — do not start Step 0-7);
routeAdministration + formulationTypeROAMaster (lighter domain review); corporateMasterGlobal (billing
framework). Nothing started.
