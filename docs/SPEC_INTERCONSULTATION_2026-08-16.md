# SPEC — OPD INTERCONSULTATION (modern Angular rebuild)

SERVED-BRANCH: `fix/patient-type-details-status` @ 43d4a47f. NO code until the
owner reviews. Source of truth: the legacy state `interconsultation`
(app.js:13455) + its template `views/OPD/inter-consultation.html` (70 lines) +
the corrected backend's `ConsultDoctorController` (class /api/opd) +
`ConsultDoctorDto`.

## 0. THE MAPPING FINDING (read first)

The legacy `#/interconsultation` is a **static HTML mockup, not a working
screen**: the template is 70 lines with ZERO ng-model / ng-click bindings (the
"Consult Order" input is unbound, the state's controller is the generic
MasterController) — the same class as prescriptionDetails. **The real
referral flow in the legacy is the OPD coversheet's consult-doctor family**,
whose backend is the corrected `ConsultDoctorController` — and THAT is what
this spec ports.

## 1. Sections and fields (the real flow's model)

### 1.1 Patient / encounter context header (read-only)
The DTO carries the context: `patientId`, `patientName`, `uhidNumber`,
`encounterId`, `admissionId`, `visitTypeId` — populated from the loaded
encounter (the same search-first flow as the other OPD screens).

### 1.2 Referral creation
- From-doctor: `refByDoctorId` + `refBySpecialityId` (the encounter's doctor —
  from the context, read-only).
- To-department: `refToSpecialityId` — the speciality cascade
  (`GET /adt/getActiveSpecialityList`).
- To-doctor: `refToDoctorId` — the doctor list by speciality
  (`GET /adt/getDoctorsListAganistSpecialities` — the legacy typo ported
  faithfully).
- Reason: `notes` (free text).
- Urgency: `priority` (the urgency level — the legacy's priority master).
- `defaultSelfTariffId` (the tariff context).

### 1.3 Referral list (incoming and outgoing)
`POST /api/opd/consultDoctorListByEncounter` + the DTO's criteria
(encounterId/patientId/unit/org) — the rows carry refByDoctor/refToDoctor,
specialityName, priority, referredStatus, status, createdDate.

### 1.4 The accept/complete flow (DEFERRED per the owner)
The legacy/backend's state-changing endpoints: `POST /api/opd/updateReferredStatus`
(the accept/reject — the referredStatus!), `POST /api/opd/inactiveConsultDoctor`
(the cancel-equivalent; the legacy's `CANCELCONSULTDOCTOR = /api/opd/cancelDoctorConsult`
path's sibling), `POST /api/opd/updateConsultDoctorNotes`.

## 2. The DTO — ConsultDoctorDto (corrected backend, quoted)

consultDoctorId (Integer) · organizationId · unitId · organizationIdp · unitIdp
· defaultSelfTariffId · refByDoctorId · refBySpecialityId · refByDoctor
(String) · refToDoctorId · refToDoctor (String) · refToSpecialityId ·
specialityName (String) · patientId · patientName (String) · encounterId ·
notes (String) · status (Character) · createdDate (String) · createdDateNew
(Date) · createdBy · updatedDate (String) · updatedBy · uhidNumber (String) ·
priority (String) · referredStatus (String) · admissionId · visitTypeId.

DATE CONVENTION: the referral's dates are STRING (createdDate/updatedDate —
dd-M-yyyy HH:mm:ss via formatLegacyNow — the registration/prescription
convention, NOT the encounter's Long).

## 3. API inventory (core)

- Refer-to cascade: GET `/adt/getActiveSpecialityList` ·
  `/adt/getDoctorsListAganistSpecialities` (both POST).
- Save: POST `/api/opd/consultDoctor` + ConsultDoctorDto (the referral
  creation).
- List: POST `/api/opd/consultDoctorListByEncounter` (incoming + outgoing for
  the encounter).
- DEFERRED: POST `/api/opd/updateReferredStatus` (accept/reject),
  `/api/opd/inactiveConsultDoctor` (cancel), `/api/opd/updateConsultDoctorNotes`
  (the notes), `getConsultationPatientListForDocDesk`.

## 4. Core scope (first build) vs deferred (second pass)

CORE: patient/encounter context header · refer-to department + doctor
selection (the cascade) · reason for referral (free text) · urgency level ·
save referral (POST /api/opd/consultDoctor) · referral list (incoming +
outgoing via consultDoctorListByEncounter).

DEFERRED: accept/reject/complete workflow (updateReferredStatus +
referredStatus rendering) · referral notes and attachments
(updateConsultDoctorNotes) · notification to the referred doctor.

## 5. Risks / notes

- The legacy screen being a mockup means no legacy UI to match — the backend
  DTOs are the contract (same as prescriptionDetails).
- The save's live outcome shares the corrected backend's encounter-chain
  state (the masked-500 class — not claimed until it clears).
- The list's rows render referredStatus/priority as display text in this
  build; the state-changing actions land in pass 2.
