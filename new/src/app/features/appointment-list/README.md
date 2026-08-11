# Appointment List — screen 43 (module 7, Opd)

Parity rebuild of the legacy appointment-list screen (menu node 7
'Appointment List', parent 2 Opd, controller appointmentListController
loaded index.html:905; the state's title is 'Patient Visit List').

## CLASSIFICATION — LIVE (the OPD subtree's fourth candidate; C7 verified
## genuinely new)

- State: REGISTERED app.js:12948 (C5 PASS — node chain 7 -> 2 Opd).
- The screen lists scheduled appointments filtered by doctor/speciality/
  subspeciality/modality/visit-type.
- BACKEND BY ENDPOINT TRUTH: the screen calls GETAPPOINTMENTSCHEDULELIST =
  /api/opd/scheduleAppointmentList — the SAME endpoint as screen 41
  (appointment-scheduling). Its handler is LIVE; the list path currently
  throws server-side (501 honest-error envelope 'Something went wrong!!').
  ROOT CAUSE OWED ONCE (not per screen — shared endpoint; candidates: a
  phantom column or missing join in GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST;
  the DAO catch at AppointmentSchedulingDaoImpl:872-875 discards the
  exception into a null-message envelope — the screen-41 finding).
- LIVE-VERIFIED BY CALLING: the endpoint with the legacy's doctorModalityObj
  contract (doctorId/specialityId/etc. = 0) -> 200 body 501 honest error.

## SCOPED BACKEND CHECK

Read-only list surface (no write in this screen's controller). No
write-path false-success shape.

## Build state

- Route: NEW at /appointmentList (moduleId 7).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C5 PASS). ng build exit
  0. Smoke: route renders, the honest error state (shared endpoint), 0 page
  errors.
