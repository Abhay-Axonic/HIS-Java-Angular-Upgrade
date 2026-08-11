# OPD Coversheet — screen 45 (module 9, Opd)

Parity rebuild of the legacy OPD coversheet (menu node 9 'OPD Coversheet',
parent 2 Opd; state OPDCoverSheet registered app.js:13412; controller
OPDCoverSheetController loaded index.html:906; template
views/OPD/CoverSheet/opd-coversheet.html — a 26,660-line clinical
transaction surface).

## CLASSIFICATION — LIVE (the OPD subtree's sixth and final candidate; C5
## PASS — registered + menu node 9 chain 9 -> 2 Opd)

- State: REGISTERED app.js:13412 (C5 PASS). The menu's actionLink for node
  9 is MIS-COPIED (#/encounterQueueManagement — node 6's route), the
  MENU-BROKEN shape: the node advertises the screen under a wrong link, but
  the state is genuinely reachable via `$state.go("OPDCoverSheet")` from
  patient list / the encounter queue (the drill-down pattern).
- C7 genuinely new (no feature dir, no route on main). C8 clean (moduleId
  9 unused).

## BACKEND BY ENDPOINT TRUTH (all CALLED with the cached token; body status
## field inspected)

- /api/global/coversheet/templates — LIVE: 200 success listObject [] for
  template types 1..7 (dev-empty, WORKING).
- /api/global/templates/complaints + /api/global/templates/diagnosis —
  LIVE: 200 success [] (dev-empty, WORKING).
- /api/global/getActiveButtonMaster — BROKEN: the DAO's second SQL
  (`select disclaimer from public.m_unit_master ...`,
  UserRoleMasterDaoImpl.getActiveButtonMaster) references a column the dev
  schema does NOT have (0 columns match 'disclaimer' in any spelling); the
  HibernateException is caught and the controller returns the ERROR/null-code
  envelope — the MASKED-ERROR class with a phantom COLUMN (the
  visit_type_id / role_desc family; here on a button/action-bar feed).
  Banner finding (AMBER-permanent per the banner-tier rule: the response it
  would read is the thing that lies).

## SCOPED BACKEND CHECK

The coversheet's 83 clinical tabs (vitals, complaints, diagnosis,
medication, orders, investigations, allergies, histories, immunization)
are ALL patient/encounter-scoped — the legacy enters with
params.appointmentObj from patientList / the queue. The rebuild renders the
surface + the feeds that work WITHOUT a patient context (the template
feeds above) and documents the patient-gated paths. No write-path
false-success shape in the feeds surfaced here.

## Build state

- Route: NEW at /OPDCoverSheet (moduleId 9).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C5 PASS; C8 clean).
  ng build exit 0. Smoke: route renders, the AMBER finding banner names the
  phantom disclaimer column, the working template feeds render honestly,
  0 page errors.
