# SPEC — OPD PENDING ORDERS (modern Angular rebuild)

SERVED-BRANCH: `fix/patient-type-details-status` @ 0d8274e5. NO code until the
owner reviews. Source of truth: the legacy state `pendingOrders` (app.js:4741)
+ its template `views/Nursing/pending-orders.html` (643 lines) + controller
`master/unit/allPendingOrdersForNursingController.js` + the corrected backend's
`NursingOrderMapperController` (class /api/opd) + `NursingOrderMapperDto`.

## 0. The mapping note

The legacy `#/pendingOrders` is the **NURSING pending-orders screen** (state
app.js:4741, controller allPendingOrdersForNursingController) — the orders
across services (the owner's lab/radiology/pharmacy framing) that the nursing
team acts on. Its backend family is the corrected `NursingOrderMapperController`.
The owner's core scope maps directly onto it.

## 1. Sections and fields (the legacy's model)

### 1.1 Filters
- Date range: `searchObj.fromDate` / `searchObj.toDate` (the datepickers) —
  the orders by date.
- Department: the speciality (the corrected DTO's specialityId /
  specialityName; the legacy's modality filter `modalityId` +
  GETMODALITYBYSUBSPECIALITYID for radiology).
- The patient search: `/adt/searchListOfPatientsByWardId` (the legacy's
  patient picker).
- The legacy's list fetches: `POST /api/opd/pendingOrders/getNurseOrdersByDate`
  and `POST /api/opd/pendingOrders/getNurseOrdersDateWiseList`.

### 1.2 The order list grid (the core)
The rows are the NursingOrderMapperDto's display fields:
| column | field |
|---|---|
| Patient | patientName |
| Order date | startDate (String, dd-M-yyyy …) |
| Service | serviceStandardName |
| Department | specialityName / subSpecialityName |
| Doctor | doctorId (the doctor name in the DTO's join) |
| Frequency | frequency / administrationTimefreq |
| Instruction | instruction |
| Status | status (Character) |
| Row state | checkStatus (Boolean — the selection), color |

### 1.3 View order details (the core)
The row's details: nursingOrderMapperId, nursingOrderId, patientId,
admissionId, serviceId, specialityId, subSpecialityId, frequencyId,
startDate/stopDate, instruction — the selected row's full DTO.

### 1.4 Action buttons (the legacy's — DEFERRED per the owner)
The legacy's actions: checkAll (the selection), saveActualAdministrationTime
(`POST /api/opd/pendingOrders/saveActualAdministrationTime` — the nursing
administration-time write), saveIssueAcceptance (the blood-bag acceptance),
removenursingRylesOrder, reprintBarCode, saveAppointment (the radiology
scheduling — `POST /api/opd/scheduleRadiologyAppointmentForInPatient`),
getIssuedBags, medicationsList.

## 2. The DTO — NursingOrderMapperDto (corrected backend, quoted)

nursingOrderMapperId (Integer) · nursingOrderId · frequencyId · instruction
(String) · startDate (String) · stopDate (String) · patientId · admissionId ·
doctorId · organizationId · unitId · status (Character) · createdBy ·
createdDate (String) · updatedBy · updatedDate (String) · nursingOrderDesc ·
frequency (String) · administrationTimefreq (String) · serviceId ·
specialityId · subSpecialityId · serviceStandardName (String) ·
specialityName (String) · subSpecialityName (String) · color (String) ·
checkStatus (Boolean) · patientName (String).

DATE CONVENTION: the order's dates are STRING (dd-M-yyyy HH:mm:ss via
formatLegacyNow — the registration/prescription convention, NOT the
encounter's Long).

## 3. API inventory (core)

- List by date: POST `/api/opd/pendingOrders/getNurseOrdersByDate` +
  NursingOrderMapperDto (the fromDate/toDate/specialityId/unit/org criteria).
- Date-wise list: POST `/api/opd/pendingOrders/getNurseOrdersDateWiseList`.
- By patient: POST `/api/opd/pendingOrders/getPendingNurOrdListByPatientId`.
- The plain list: POST `/api/opd/pendingOrders/getNurseOrders` (the
  corrected's primary list).
- Patient picker: POST `/adt/searchListOfPatientsByWardId` (the legacy's).

DEFERRED actions (they change order state — per the owner): the status
updates (`POST /api/opd/updateNursingOrdersStatusByNurse`,
`/pendingOrders/saveActualAdministrationTime`,
`/updateNursingOrdersStartOrStop`), the reschedule (the radiology
appointment scheduling), the cancel (removenursingRylesOrder), printing
(reprintBarCode).

## 4. Core scope (first build) vs deferred (second pass)

CORE: filter by date range + department · order list grid (patient, order
date, service, status, doctor) · view order details.

DEFERRED: accept/reject/cancel actions (they change order state) · reschedule
· printing — plus the legacy's nursing-specific actions (administration time,
blood-bag acceptance, radiology scheduling).

## 5. Risks / notes

- The corrected backend's pendingOrders endpoints were exercised in the
  route-capture era; `getNurseOrders`/`getNurseOrdersByDate` are POST +
  NursingOrderMapperDto — the exact body's fields (fromDate/toDate/
  specialityId) are the DTO's names, ported verbatim.
- The list is read-only in this build (the owner's deferral) — the grid's
  checkStatus/selection is display-only until the actions land.
- The save/action live tests share the corrected backend's encounter-chain
  state (not claimed until the backend's masked-500 class clears).
