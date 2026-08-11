# Appointment Scheduling — screen 41 (module 4, Opd)

Parity rebuild of the legacy appointment-scheduling screen (menu node 4
'Appointment Scheduling', parent 2 Opd, controller
appointmentSchedulingController loaded index.html:904).

## CLASSIFICATION — LIVE (the OPD subtree's second candidate; C7 verified
## genuinely new)

- State: REGISTERED app.js:11271 (C5 PASS — node chain 4 -> 2 Opd).
- The screen is the appointment-scheduling TRANSACTION surface: prefix/
  reason feeds -> schedule appointment (the save), scheduled-appointment
  list (the primary read), blocked list, search by criteria, cancel.
- BACKEND BY ENDPOINT TRUTH: AppointmentSchedulingController (api/opd):
  scheduleAppointment (save), scheduleAppointmentList,
  blockedScheduleAppointmentList, searchAppointmentsByCriteria,
  cancelScheduledAppointment. Controller catches LOG + return the HONEST
  ERROR envelope (COMMON_ERROR_MESSAGE — populated).
- LIVE-VERIFIED BY CALLING (body status): scheduleAppointmentList ->
  HTTP 200 body {"status":"error","code":"501","message":"Something went
  wrong!!"} — handler LIVE; the list path throws server-side (root cause
  owed: a phantom column or missing join in GET_SCHEDULED_APPOINTMENTS_
  SLOTS_LIST).

## FINDING (the masked-error class on an OPD path)

The DAO's scheduledAppointmentList catch (AppointmentSchedulingDaoImpl:
872-875) logs the exception and falls to `return new Response(ERROR, null,
null, null, null)` — the NULL-MESSAGE envelope; the service rethrows it as
ApplicationException with a null message; the controller returns the
populated "Something went wrong!!". The user sees an error but the
UNDERLYING EXCEPTION IS DISCARDED at the DAO. NOT FIXED (Rule 10) —
surfaced for the BR scoped-check.

## SCOPED BACKEND CHECK

The save (scheduleAppointment) was NOT live-probed (full SlotMasterDto
contract unknown) — 'save not exercised, contract unknown' (screen-36
pattern). The save DAO shows the GOOD guard pattern (appointmentDate
null-guard ternary + TimeZone defaults) with one latent NPE:
getAppointmentDate2().substring(0,10) (line 95) on the visit-type branch —
unguarded substring.

## Build state

- Route: NEW at /appointmentScheduling (moduleId 4).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C5 PASS). ng build exit
  0. Smoke: route renders, the finding banner + the honest error state, 0
  page errors.
