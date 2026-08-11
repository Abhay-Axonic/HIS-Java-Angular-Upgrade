# Doctor Consultation — screen 31 (module 612, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/doctor-consultation-master.html` (menu
node 612 'Doctor Consulation' [sic], parent 570 Billing -> 531 Unit Master,
controller doctorConsulationMasterController loaded index.html:871).

## CLASSIFICATION — LIVE (C7 + C8 at selection)

- State: REGISTERED app.js:8317 `doctorConsulationMaster` — the LEGACY TYPO
  (the node links #/doctorConsulationMaster; C5's DEAD flag was a
  state-name mismatch, not an absent screen — the sweep's LIVE was right).
  Controller binds doctorConsulationMasterController (NOT MasterController —
  passes the DECOY test).
- Menu node: 612 'Doctor Consulation' -> 570 -> 531 (C5 chain holds once the
  state name is matched).
- BACKEND BY ENDPOINT TRUTH (the 585 lesson): DoctorConsultationServiceMapper-
  Controller (Global, @RequestMapping api/billing/unit) — a DIFFERENT class
  name than both state spellings. Endpoints: save, getList, ListCount,
  update, ById, Status, BySpecialityAndDoctorId.
- LIVE-VERIFIED BY CALLING (body status): list -> 200 success [] (dev-empty);
  count -> 200 success object: 0. PAYLOAD CONTRACT: `tabNumber: 1` REQUIRED
  (omitting it -> masked 200 error — the probe-payload class; the
  controller's empty catch :60-62 swallows into the null-message envelope).

## SCOPED BACKEND CHECK (billing write-path)

- FALSE-SUCCESS: ABSENT — the save (DAO:45-63) and updateStatus
  catch-and-FALL-THROUGH to honest ERROR envelopes (never
  SUCCESS-from-catch; SUCCESS only after the real write). The BR-188 class
  is NOT present.
- getDate: the safe overload (a DATE object, not null — no getDate(null) NPE
  class; the BR-187/screen-26 contrast).
- INNER-JOIN drop: the list LEFT JOINs doctorMaster + INNER JOINs
  speciality/newVisitService/followupVisitService (a row with a missing
  INNER partner is dropped — the walk's class, by construction; dev-empty
  so not demonstrated).
- DDL: clean (m_doctor_consultation_service_mapper, doctor schema).
- The controller's EMPTY catch on unexpected exceptions -> the masked
  null-message 200 (the screen-30/31 shared pattern — surfaced, BR
  candidate).

## Build state

- Route: NEW at /doctorConsultation (moduleId 612).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 612
  only route; C5's DEAD flag explained above — hand-confirmed the typo'd
  state). ng build exit 0. Smoke: route renders, the honest dev-empty list
  with the count evidence, 0 page errors.
