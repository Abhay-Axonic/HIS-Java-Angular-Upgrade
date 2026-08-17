# SPEC — OPD FOUR VARIANTS (walk-in, cyclic scheduling, cyclic list, visit list)

SERVED-BRANCH: `fix/patient-type-details-status` @ 18996b83. One paragraph per
screen — all are lightweight variants of the already-built appointment
scheduling + encounter screens; all four use the corrected backend's
SlotMasterDto payloads with STRING dates via formatLegacyNow (the
registration/prescription convention, NOT the encounter's Long).

1. **WALK-IN APPOINTMENT** (`#/walkinAppointment`, views/OPD/walkin-appointment.html
546 lines, REAL) — the self-registration variant: a simplified booking without
a pre-scheduled slot. Core: date + speciality->doctor cascade + patient search
(/api/global/patientSearch) + book via POST /api/opd/scheduleAppointment
(SlotMasterDto — the same save as appointment scheduling). Deferred: the
self-registration's full patient-creation flow (ADDPATIENTREGISTRATION), the
OTP/ABDM, the billing link.

2. **CYCLIC APPOINTMENT SCHEDULING** (`#/cyclicAppointmentScheduling`,
cyclic-appointment-scheduling.html 1679 lines, REAL) — recurring appointments
(weekly physiotherapy etc.). Core: start date (cyclic.datepickerFrom) +
days-of-week (cyclic.dayE1..dayE7) + doctor (via GETDOCTORBYSPECIALITYID) +
time; save = POST /api/billing/generateCyclicServiceAppointment with
**@RequestBody List<SlotMasterDto>** (one SlotMasterDto per recurrence).
Deferred: the appointment template (getAppointmentTemplate/...MapperData), the
overbooking rule, the service-cyclic billing integration.

3. **CYCLIC APPOINTMENTS LIST** (`#/cyclicAppointments`, cyclic-appointments.html
132 lines, **MOCKUP — zero bindings**, generic MasterController) — the list
view for recurring appointments. Core: the read-only grid via
searchAppointmentsByCriteria (the working endpoint — the cyclic rows are
appointments). Deferred: the cancel (BLOCKEDSCHEDULEAPPOINTMENTLIST/
CANCELSCHEDULEAPPOINTMENT), the accept/reschedule.

4. **PATIENT VISIT LIST** (`#/appointmentList`, views/common/appointments/
appointment-list.html, REAL — appointmentListController) — the appointment
list/queue that feeds the encounter screen. Core: the filters (doctor/date/
referral + the gender/prefix/reason/referral-type masters) + the queue grid via
searchAppointmentsByCriteria; the row click carries the patient+encounter into
the encounter screen. Deferred: the cancel (CANCELSCHEDULEAPPOINTMENT), the
blocked-slot list, the modality list.

All four: tsc clean, routes registered (walkinAppointment,
cyclicAppointmentScheduling, cyclicAppointments, appointmentList), one commit.
