# Patient List — screen 44 (module 8, Opd)

Parity rebuild of the legacy patient-list screen (menu node 8 'Patient
List', parent 2 Opd, controller patientListController loaded index.html:898;
template views/common/appointments/patient-list.html).

## CLASSIFICATION — LIVE (the OPD subtree's fifth candidate; C7 verified
## genuinely new)

- State: REGISTERED app.js:13467 (C5 PASS — node chain 8 -> 2 Opd).
- The screen lists patients (with health-card + search paths).
- BACKEND BY ENDPOINT TRUTH:
  - GETPATIENTLIST = /api/global/getPatientDetails — LIVE-VERIFIED by
    CALLING: 200 success listObject [] (dev-empty) — a WORKING list
    endpoint (the first OPD list that returns success, not the shared 501
    path);
  - the search path uses GETTEMPORYPATIENTFORPREREGISTRATION =
    /api/global/patientSearch (the screen-40 endpoint — the 501 honest
    error on empty criteria, root cause owed there);
  - the health-card feed GETHEALTHCARD = /api/global/getHealthCard.

## SCOPED BACKEND CHECK

Read-only list surface (no write in this screen's controller). No
write-path false-success shape.

## Build state

- Route: NEW at /patientList (moduleId 8).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C5 PASS). ng build exit
  0. Smoke: route renders, the working list's honest dev-empty state, 0
  page errors.
