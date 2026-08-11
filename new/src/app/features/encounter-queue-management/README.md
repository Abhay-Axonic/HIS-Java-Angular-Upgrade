# Encounter Queue Management — screen 42 (module 6, Opd)

Parity rebuild of the legacy
`views/master/GlobalMaster/Encounter/encounter-queue-management.html` (menu
node 6 'Encounter Queue Management', parent 2 Opd, controller
encounterQueueManagementController loaded index.html:952).

## CLASSIFICATION — LIVE (the OPD subtree's third candidate; C7 verified
## genuinely new)

- State: REGISTERED app.js:7622 (C5 PASS — node chain 6 -> 2 Opd).
- The screen is a QUEUE-MANAGEMENT surface: nursing-station list ->
  dept-by-station -> doctors-by-speciality; the queue renders from the
  selected station's encounters.
- LIVE-VERIFIED BY CALLING (body status) — all three feeds:
  - GET /api/global/getDetailedNursingStationList/{userId}/{org}/{unit} ->
    200 success with REAL rows (userNursingStationMpprId 7,
    nursingStationId 1);
  - GET /api/global/getDeptListByNursingStationId/{station}/{org}/{unit} ->
    200 success listObject [1] object [1,2];
  - POST /api/global/doctor/activeWithSpeciality {organizationId, unitId} ->
    200 success with REAL rows (doctorId 1, firstName Demo).

## SCOPED BACKEND CHECK

The screen's surface is feed-driven (no master save in this controller's
read path — the queue is a live view). No write-path false-success shape on
the read surface. The queue's patient/encounter actions live in the
encounter-consultation flow, not this screen's controller.

## Build state

- Route: NEW at /encounterQueueManagement (moduleId 6).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C5 PASS). ng build exit
  0. Smoke: route renders, the real feed rows visible (station/dept/doctor),
  0 page errors.
