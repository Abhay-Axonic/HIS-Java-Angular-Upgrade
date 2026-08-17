# OPD MODULE VERIFICATION — Phase 1 of 9 (2026-08-16, verification only, nothing changed)

SERVED-BRANCH: `fix/patient-type-details-status` @ ac229c50. Both apps against the
corrected backend (tunnel :8080 -> 192.168.4.40:8081), the app's own login flows.

## PART 1 — workflow map (see the turn's table): registration `#/patientRegistration` (NO
rebuild), encounter `#/outPatient` -> rebuilt `encounter` (READ-ONLY, 0 calls live),
queue `#/encounterQueueEHC` + `#/encounterQueueManagement` (both rebuilt, reads 200),
documentation `#/OPDCoverSheet` (rebuilt route exists but bounces), orders
`#/prescriptionDetails`/`#/pendingOrders` (NO rebuild), billing
`#/opdBilling/:patientId/:encounterId` + `#/newOPDBilling` (NO rebuild), appointments
(rebuilt: appointmentScheduling + bookAppointment + webAppointmentBooking — feeds 500),
other `#/interconsultation`/`#/encounterData` (NO rebuild).

## PART 2 — legacy drive: WORKFLOW-BREAK at step 1 (registration cannot complete)
- `#/patientRegistration` renders; the form was filled to the gate's requirements
  (the DOB datepicker's ng-model is the scope var `datepicker`, format dd/MM/yyyy — the
  direct patientObj.dob set is the wrong target; names/mobile/identificationNo set; the
  selects walked).
- The Save never fires — the form stays invalid; the evidence names the CORRECTED
  backend's data: (1) the identification select is absent from the DOM (the required
  identificationId has no rendered select), (2) the kin's area select has exactly ONE
  option — the AREA MASTER IS EMPTY on the corrected backend (the local's seeded rows
  don't exist there), (3) the identificationNo required persists (hidden-section field).
- No probe row was created (the save never fired) — cleanup trivially satisfied.
- Steps 2-8 are not drivable: a workflow that cannot register cannot be walked. The
  continuity question (queue -> coversheet -> billing) is unanswerable in the legacy
  until step 1 completes.

## PART 3 — rebuilt screens live at :4201
- encounter: renders, 0 API calls (static shell, no data feed).
- encounterQueueEHC / encounterQueueManagement: render, all 200.
- opdCoversheet: the route BOUNCES to / (text 0) — the route serves nothing.
- appointmentScheduling: renders, 500 scheduleAppointmentList.
- bookAppointment: renders, 3x500 (getSpecialityListByDoctorIdByOrg, getDailySlots,
  getDetailsForBookAppointment).

## Verdict
The OPD workflow is not continuous in EITHER app: the legacy's step 1 is blocked by the
corrected backend's empty masters (area + identification — the same class as the local's
drift gaps, different table); the rebuild covers the queue reads only, the coversheet
route is broken, the appointment feeds 500, and registration/billing/orders/prescription
have no rebuild. Nothing fixed — findings only, per the owner's instruction.
