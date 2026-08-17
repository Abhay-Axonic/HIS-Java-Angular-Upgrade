# SPEC — OPD PATIENT VISIT / ENCOUNTER DETAILS (modern Angular rebuild)

SERVED-BRANCH: `fix/patient-type-details-status` @ 8bc1b90a. NO code until the
owner reviews. Sources of truth: legacy state `encounter.encounterDetails`
(app.js:11421) + its template `views/master/GlobalMaster/Encounter/encounter-details.html`
(1,976 lines — the encounter's ADMINISTRATIVE surface), the parent state
`encounter` (app.js:11382) + `encounter.html` (3,498 lines — the visit's
billing/settlement surface), the clinical core in the legacy OPD coversheet
(state OPDCoverSheet app.js:13412, `views/OPD/CoverSheet/opd-coversheet.html`
26,660 lines — vitals/complaints/ICD live HERE), controllers
`EncounterController` + `OPDCoverSheetController`, and the backend
`EncounterMasterDto` (corrected source).

## 0. The mapping correction (important)

The owner's route `#/encounter/encounterDetails` is the encounter's
ADMINISTRATIVE surface: the patient header, the complaints + notes blocks, and
the appointment/insurance/settlement/contract modals. The CLINICAL core the
owner lists (vitals, ICD diagnosis) lives in the legacy **OPD coversheet**
(same OPD module family, `OPDCoverSheetController`). This spec assembles the
core scope from the legacy's ACTUAL homes; the rebuild is one screen with the
owner's six core sections.

## 1. Sections, fields, actions (the legacy's model)

### 1.1 Patient header (encounter-details.html — `patientDetails` object)
`prefixId`, `firstName`, `middleName`, `lastName`, `genderId`, `birthDate`
(datepickerDOB), `age` (computed from DOB), `mobileNumber`, `email`,
`ehcPackageId`, `remark` — plus the encounter context (from the loaded
encounter): encounterId, encounterNumber, doctorId, visitTypeId, encounterDate.
Action: the UHID/encounter search loads the header (the screen is
encounter-context-dependent — it needs a patient + encounter loaded first).

### 1.2 Vitals (OPD coversheet)
`pulseRate`, `pulseType`, `temperature`, `weight`, `height`, `bmi`, `bmiStatus`,
`SPO2`, respiration — the vitals block loads via
`GETAPPOINTMENTVITALMAPPERBYTRAND` (`/opd/getAppointmentVitalMapperByTrand`),
masters via `GETVITALMASTERLIST`, and saves via the vital-mapper save (the
legacy's vitals save on the coversheet's Save).

### 1.3 Chief complaints (coversheet + details)
The complaints table (`complaints`, `complaintListByPatientId`,
`complaintSectionNote`) — list load
`GETCOMPLAINTSLIST` (`/opd/getComplaintAppointment`), save
`UPDATECOMPLAINTAPPOINTMENT`; the backend DTO nests them:
`complaintlistData` / `listComplaintDataSave` (ComplaintAppointmentDetailsDto).

### 1.4 Diagnosis / ICD (coversheet)
ICD-10 search `DIAGNOSISAUTOFILLSEARCH` (`/opd/diagnosisAutoFillSearch`) +
the patient's existing diagnosis load `GETPATIENTDIAGNOSISBYPATIENTID`; the
diagnosis grid adds rows (ICD code + description) for the save.

### 1.5 Clinical notes (coversheet + details)
`clinObj.clinicalNote` (free text) + the notes list per encounter.

### 1.6 Save
The encounter-data save posts the EncounterMasterDto payload (below) with the
nested complaints/diagnosis/vitals lists, plus the audit fields (createdBy /
createdDate / updatedBy / updatedDate — the backend's Long-epoch dates via the
formatLegacyNow-family convention the registration build used).

## 2. Backend DTO — EncounterMasterDto (corrected source,
`Global/.../opd/encounter/dto/EncounterMasterDto.java`), quoted field names:

encounterId (Integer) · patientId (Integer) · patientTypeId · contractId ·
visitTypeId · doctorId · sourceId · encounterDate (Long) · encounterDateString
(String) · encounterTime (Instant) · isReferal (Character) · deptId ·
subDeptId · remark (String) · kinDetailsId · paymentEntitlementTypeId ·
encounterNumber (String) · createdDate (Long) · createdBy · updatedDate (Long)
· updatedBy · status (Character) · unitId · organizationId · encounterDateTime
(Date) · listKinDetailsDto (List<KinDetailsDto>) · appointmentId ·
isScheduledAppointment (Character) · doctorName · specialityId ·
specialityName · subSpecialityId · subSpecialityName · doctorList
(List<Integer>) · complaintlistData (List<ComplaintsDto>) ·
complaintlistDataLastVisit · listComplaintDataSave
(List<ComplaintAppointmentDetailsDto>).

The vitals save uses the appointment-vital-mapper DTO family (the
`getAppointmentVitalMapperByTrand` response), not a field on
EncounterMasterDto. The rebuild's payloads use THESE names.

## 3. API inventory (core)

- Search/context load: the patient/encounter search endpoint (the
  encounter-details' load — UHID -> encounter context).
- Vitals: GET `/opd/getAppointmentVitalMapperByTrand` · GET
  `/opd/getVitalMasterList` · vitals save (the legacy coversheet's save).
- Complaints: GET `/opd/getComplaintAppointment` · save
  `UPDATECOMPLAINTAPPOINTMENT`.
- Diagnosis: GET `/opd/diagnosisAutoFillSearch` (ICD-10) ·
  `GETPATIENTDIAGNOSISBYPATIENTID`.
- Save: the encounter-data save (POST the EncounterMasterDto payload + nested
  lists).

## 4. Core scope (first build) vs deferred (second pass)

CORE: patient header (read-only from the encounter context) · vitals entry ·
chief complaints · diagnosis (ICD search + add) · clinical notes · save.

DEFERRED: prescription · orders · referral/interconsultation ·
attachments/documents · templates/clinical forms · speech-to-text — plus the
encounter-details' administrative modals (insurance/contract settlement,
deposit/collection, appointment reschedule) which are NOT part of the clinical
entry and belong to a billing-phase pass.

## 5. Existing rebuild — do NOT duplicate

The rebuild already HAS two parity features in the older style: `encounter/`
(screen 46, module 5 — the route at app.routes.ts:1741) and `opd-coversheet/`
(screen 45, module 9 — the route around app.routes.ts:1738). Both are the
earlier minimal parity rebuilds (the live smoke showed the encounter route
renders a static shell with 0 API calls; the coversheet route bounced to /).
THIS build supersedes them for the clinical-entry scope: the new feature
replaces their routes' loadComponent (the old files retained, same convention
as the registration build); the owner decides keep/delete of the old files.

## 6. Risks / notes

- The screen is encounter-context-dependent: the header/notes/complaints all
  need a loaded encounter — the search-first flow is the entry (the same
  UHID-search class the registration's patientSearch uses).
- The save's success depends on the corrected backend's registration/encounter
  chain (the registration's save currently returns the masked 'Something went
  wrong!!' — the encounter's save will share the outcome until the backend's
  INSERT class is fixed).
- The DTO's Long dates (createdDate/updatedDate/encounterDate) differ from the
  String dates the registration used — the payload must send the Long-epoch
  form the backend parses.
