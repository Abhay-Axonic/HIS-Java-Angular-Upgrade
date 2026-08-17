# SPEC — OPD PRESCRIPTION DETAILS (modern Angular rebuild)

SERVED-BRANCH: `fix/patient-type-details-status` @ e0575b47. NO code until the
owner reviews. Source of truth: the legacy state `prescriptionDetails`
(app.js:13563) + its template `views/common/appointments/Master Management/prescription-Details.html`
(137 lines) + the corrected backend's prescription family (PrescriptionDto,
PrescriptionDetailsDto, MedicationController).

## 0. THE MAPPING FINDING (important — read first)

The legacy `#/prescriptionDetails` is a **static HTML mockup, not a working
screen**: the template has ZERO ng-model / ng-click / Angular bindings — the
Title and ICD-Search inputs are unbound, the Doctor select is empty, the Save
icon is a bare `<a href="">`, the prescription table's "Medicine" cell is a
"Document Date" placeholder, and the only JS is a `$(document).ready` jQuery
block (page height + sidebar active state). The state's controller is the
generic `MasterController`. **The real prescription-writing in the legacy lives
in the OPD coversheet's medication section**, whose backend is the corrected
`MedicationController` (`/api/opd`) — and THAT is what this spec ports.

## 1. Sections and fields

### 1.1 Patient / encounter header (from the encounter context)
The PrescriptionDto carries the context: `patientId`, `encounterId`, `doctorId`,
`visitTypeId`, `admissionId`, `prescriptionNo`, `prescriptionDate` (String).
The header is read-only, populated from the loaded encounter (the same
search-first flow as the encounter screen).

### 1.2 Medication search (item lookup by name)
`GET /api/global/unit/itemMaster/{searchKeyword}/unit/{unitId}/org/{orgId}`
(UnitServiceMapperController.searchItemByNameAndCode — the item/generic/brand
lookup). The result row supplies `itemId` + the item description.

### 1.3 Prescription line (the PrescriptionDetailsDto)
| field | type | meaning |
|---|---|---|
| itemId | Integer | the medication |
| dose | Double | the dose amount |
| doseUnitId | Integer | dose unit master |
| frequencyId | Integer | the frequency master (the legacy mock's 1–12 = Once a day … Now) |
| routeId | Integer | administration route |
| orderQty | Integer | quantity |
| startDate / stopDate | String | the duration |
| remark | String | instruction |
| prnflag / statflag / infusionflag | Boolean | as-required / now / infusion |
| uomUnitId, storeId | Integer | unit-of-measure + store |
| statusId, isActive, isDeleted | | the row state |
| addedBy / addedDate (String) | | the audit |

### 1.4 Add multiple lines + Save
The lines accumulate in the prescription; the save posts the header + the
lines together.

## 2. The save endpoint + DTOs (corrected backend, quoted)

**Save: `POST /api/opd/saveMedication`** (MedicationController.java:51) with
`@RequestBody PrescriptionDto` (the header) — the class path is `/api/opd`
(MedicationController.java:39); `saveMedication` splits the lines into
stat/normal groups internally.

**PrescriptionDto** (Global/.../opd/coversheet/dto/PrescriptionDto.java):
prescriptionId, organizationId, unitId, visitTypeId, encounterId, doctorId,
patientId, prescriptionNo (String), prescriptionDate (String), isActive
(Boolean), addedBy, addedDate (String), isDeleted, statusId, priorityId,
isValidate, authorisedBy, authorisedDate, isIntervention, updatedBy,
updatedDate (String), admissionId, isDischarge, statflag — plus the nested
lines: `prescriptionDetailsDtosList` (List<PrescriptionDetailsDto>),
`statPrescriptionDetailsDtosList`, `normalPrescriptionDetailsDtosList`.
**Dates are STRING** (dd-M-yyyy HH:mm:ss — the same String convention as the
registration DTO, NOT the encounter's Long).

**PrescriptionDetailsDto** (the line): prescriptionDetailsid, organizationId,
unitId, prescriptionId, itemId, storeId, uomUnitId, orderQty, pendingQty,
refillQty, strength (Double), isDiscontinued, prescriptionDate (String),
isPreStop, isIntervention, addedBy, addedDate (String), isDeleted, isActive,
statusId, remark, prnflag, statflag, infusionflag (Booleans), dose (Double),
doseUnitId, frequencyId, routeId, startDate (String), stopDate (String).

## 3. API inventory (core)

- Context load: the encounter/patient search (same class as the encounter
  screen's search-first flow).
- Medication search: GET `/api/global/unit/itemMaster/{searchKeyword}/unit/{unitId}/org/{orgId}`.
- Frequency/route/dose-unit masters: the coversheet's frequency family
  (PrescriptionFrequecnyDto — the 1–12 values) + the route/dose-unit masters —
  exact list endpoints to be confirmed against the corrected backend during
  the build's smoke (the same method-class as the registration's master GETs).
- Save: POST `/api/opd/saveMedication` + PrescriptionDto (header + lines).

## 4. Core scope (first build) vs deferred (second pass)

CORE: patient/encounter header · medication search (item by name) ·
prescription line (medicine, dose, frequency, route, duration, instruction,
quantity) · add multiple lines · save (header + lines in one POST).

DEFERRED: favourite templates · drug-interaction checking · narcotic flags ·
discharge medication · print/PDF — plus the coversheet's other medication
history/verification surfaces (getMedicationHistoryByEncounterId etc.), which
are a read-back pass.

## 5. Risks / notes

- The legacy screen being a mockup means there is no legacy UI behavior to
  match pixel-for-pixel — the spec's contract is the BACKEND DTOs (the real
  behavior is the corrected backend's saveMedication flow).
- The save's live outcome shares the corrected backend's encounter-chain state
  (the registration save currently returns the masked 'Something went
  wrong!!'; the prescription's save needs a real encounterId to attach to).
- The frequency select's 1–12 values come from the frequency master, not the
  mock's hardcoded options — the build must load the master.
