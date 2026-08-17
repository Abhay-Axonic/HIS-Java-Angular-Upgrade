# SPEC — OPD APPOINTMENT SCHEDULING (modern Angular rebuild)

SERVED-BRANCH: `fix/patient-type-details-status` @ f25cfc67. NO code until the
owner reviews. Source of truth: the legacy state `appointmentScheduling`
(app.js:11273) + its template
`views/AppointmentScheduling/appointment-scheduling.html` (2,493 lines) +
`controller/opd/transection/appointmentSchedulingController.js` + the corrected
backend's `AppointmentSchedulingController` (class /api/opd) + `SlotMasterDto`.

## 1. Sections and fields (the legacy's model)

### 1.1 Department / doctor selection (the cascade)
Speciality → sub-speciality → doctor:
- GETACTIVESPECIALITY = GET `/adt/getActiveSpecialityList`
- GETSUBSPECIALITYBYSPECIALITYID = `/adt/getSubSpecialityBySpecialityId`
- GETDOCTORSLISTAGANISTSPECIALITIES = `/adt/getDoctorsListAganistSpecialities`
  (the legacy constant's typo is the real endpoint — ported faithfully)
- modality: GETACTIVEMODALITYTYPELIST + GETMODALITYBYSUBSPECIALITYID
- GETSYSTEMGENERATEDVISITTYPEID (the visit-type)

### 1.2 Date picker
`datepicker` (the appointment date) — the legacy's dd/MM/yyyy datepicker;
the slots fetch for the chosen date.

### 1.3 Available slots
The calendar slot fetch (day view): GETDAILYSLOTS =
`POST /api/opd/getDailySlots` (+ GETWEEKLYSLOTS / GETMONTHLYSLOTS /
GETYEARLYSLOTS for the deferred calendar views). The slot rows: slotId,
fromTimeString/toTimeString, the booked/available state.

### 1.4 Book appointment (the patient + the slot + save)
- Patient search: GETTEMPORYPATIENTFORPREREGISTRATION (`/api/global/patientSearch`
  — the UHID lookup) + the patientSearchObj (patientName, mobileNumber,
  identificationTypeId, identificationNumber, genderId) + the search's
  patientDetails (prefixId, firstName, middleName, lastName, genderId, DOB,
  age, mobileNumber, email, bloodGroup) + reasonForAppointmentId (GETACTIVEREASONLIST).
- Save: SCHEDULEAPPOINTMENT = `POST /api/opd/scheduleAppointment` +
  **SlotMasterDto** (below).
- ISOVERBOOKINGALLOWEDORNOT (the overbooking gate).

### 1.5 Appointment list view
SEARCHAPPOINTMENTSBYCRITERIA = `POST /api/opd/searchAppointmentsByCriteria`
(the booked appointments for the day/doctor).

## 2. The save DTO — SlotMasterDto (corrected backend, quoted)

slotId (Integer) · previousSlotId · organizationId · unitId · billId ·
specialityId · doctorId · fromDate (String) · toDate (String) ·
appointmentDate (String) · appointmentDate1 (String) · appointmentDateNew
(Date) · appointmentDate2 (String) · dayId · day (String) ·
fromTimeString (String) · toTimeString (String) · fromTimeString2 ·
toTimeString2 · fromTime (Instant) · toTime (Instant) · timePerSlot ·
breakFromTime (Instant) · breakToTime · slotTypeId · visitTypeId ·
visitTypeName (String) · createdBy · createdDate (String) · updatedBy ·
updatedDate (String).

DATE CONVENTION: the appointment's dates/times are the STRING form
(dd-M-yyyy HH:mm:ss-style via formatLegacyNow) with the Instant pairs for the
slots — NOT the encounter's Long. The build's payloads use THESE names.

## 3. API inventory (core)

- Speciality / sub-speciality / doctor: GET /adt/getActiveSpecialityList ·
  /adt/getSubSpecialityBySpecialityId · /adt/getDoctorsListAganistSpecialities.
- Slots: POST /api/opd/getDailySlots (day view; week/month/year deferred).
- Patient search: POST /api/global/patientSearch (the UHID lookup) +
  GETACTIVEPREFIXLIST / GETACTIVEGENDERLIST / GETACTIVEIDENTIFICATION /
  GETACTIVERELATIONLIST / GETACTIVEREASONLIST (the patient + reason masters).
- Save: POST /api/opd/scheduleAppointment + SlotMasterDto.
- List: POST /api/opd/searchAppointmentsByCriteria.

## 4. Core scope (first build) vs deferred (second pass)

CORE: department + doctor selection (the speciality → sub-speciality → doctor
cascade) · date picker · available slots display (the day view) · book
appointment (patient UHID search, slot pick, save via scheduleAppointment) ·
appointment list view (searchAppointmentsByCriteria).

DEFERRED: calendar views (week/month/year — the list/day view first) ·
recurring/cyclic appointments · teleconsultation flags ·
SMS/email notifications · rescheduling + cancellation ·
web appointment (public-facing).

## 5. Risks / notes

- The corrected backend's `scheduleAppointmentList` endpoint 500s in the
  route-capture (the known FAILING class — the list feeds the scheduling
  screen); the rebuild's list uses `searchAppointmentsByCriteria` instead,
  and the scheduleAppointmentList 500 stays a backend-owner item.
- The save's live outcome shares the corrected backend's encounter-chain
  state (the registration's masked-500 class — the appointment save needs a
  real patient + slot to attach to).
- The `getDoctorsListAganistSpecialities` typo must be ported verbatim (the
  same class as the registration's `getAvtive...` typos).
