# SPEC — ER MODULE (7 screens, batched)

SERVED-BRANCH: `fix/patient-type-details-status` @ 56c0da40. All 7 legacy
screens are REAL (none is a mockup). They share the ER patient context
(t_admission); the ER Patient Dashboard + Orders Workbench are already rebuilt.
DATE CONVENTION: String dates via formatLegacyNow (dd-M-yyyy HH:mm:ss — the
registration/prescription convention) unless a DTO says otherwise; the
corrected DTOs observed carry String createdDate/updatedDate.

1. **ER-ENCOUNTER** (`#/unknownPatientReg`, unknown-patient-reg.html 2261
lines — the most complex) — ER registration: patient search
(POST /adt/patientSearchByMultipleCriteria) + the personal-details form (the
full PatientRegistration-style model: prefix/gender/age/DOB/kin/address/geo
cascade/document type) + **priority** (GET /adt/getActivePriorityList) + mode
of arrival (accompaniedBy) + **MLC checkbox** (isMedicoLegal char). Save =
**POST /adt/saveUnknownPatient + @RequestBody AdmissionDto** (quoted:
UnknownPatientRegistrationDto — unknownPatientId, unit/org, genderId, age
Double, ageFormat, accompaniedBy, priorityId, isMedicoLegal, patientName,
tUhid, createdDate/updatedDate String). Core: search-first + the form +
priority + arrival + MLC + save. Deferred: the company/sponsor verification,
the billing config, the document upload.

2. **TRIAGE WORKLIST** (`#/bedTypeAllocation`, bed-type-allocation.html 789
lines — bedTypeAllocationERController) — the triage zone assignment: the ER
bed list (**GET /adt/getERBedList** — the zone/priority beds) + the allocation
list (**GET /adt/getERBedTypeAllocationList** + the count
GET /adt/getERBedTypeAllocationCount). Core: the read-only zone grid.
Deferred: the allocation write (the zone assignment action).

3. **ER PATIENT LIST** (`#/erPatientList`, Er-patient-list.html 717 lines —
erPatientListController) — the zone-based patient list (Red/Yellow/Green/Black
zones): the ER admitted-patient list (**GET /adt/getERAdmittedPatientList**)
filtered by zone + GETDOCTORBYSPECIALITYID + the document/encounter read.
Core: the zone filter + the read-only grid. Deferred: the zone reassignment.

4. **MAP OF WARD BED** (`#/mapOfBedForER`, bed-management-er.html 2531 lines
— mapOfBedERController) — the ER bed map: the map data
(**GET /adt/getDataForMapOfBed**) + the floor/nursing-station/bed-category
masters + the admitted-patient lists (GETERADMITTEDPATIENTLIST +
GETADMITTEDPATIENTLIST + the multiple-criteria search). Core: the bed map
grid (the occupied/empty bed state, read-only). Deferred: the bed actions.

5. **DUE FOR HOSPITAL DISCHARGE** (`#/ERdueForDischarge`,
er-due-for-discharge.html 449 lines — ERDueForDischargeController) — the
discharge ladder's first list: **GET /adt/getDischagePatientList** (the
legacy typo ported faithfully) + the discharge-type/reason masters + the
patient counts. Core: the read-only due-for-discharge grid. Deferred:
ADDDISCHARGEREQUEST + UPDATEDISCHARGEPDD (the state-changing actions).

6. **ER MARK FOR HOSPITAL DISCHARGE** (`#/erMarkForDischarge`,
Er-mark-for-discharge.html 1145 lines — erMarkForDischargeController) — the
discharge ladder's second list: **GET /adt/getERDischargePatientList** + the
pending-services + pass-issued lists + the ER patient keyword search + the
floor/reason masters. Core: the read-only mark-for-discharge grid. Deferred:
the mark write (the discharge finalization).

7. **READY FOR BILLING** (`#/ERreadyForBilling`, ready-for-billing.html 703
lines — ERReadyForBillingController) — the discharge ladder's third list:
**GET /adt/getPatientReadyForBillingList**. Core: the read-only
ready-for-billing grid. Deferred: FINALDISCHARGEFORPATIENT (the finalization
write).

All 7: tsc clean, routes registered (unknownPatientReg, bedTypeAllocation,
erPatientList, mapOfBedForER, ERdueForDischarge, erMarkForDischarge,
ERreadyForBilling), ONE commit.
