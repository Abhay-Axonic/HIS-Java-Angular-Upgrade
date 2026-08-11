# Unit Service Mapping — screen 36 (module 584, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/unit-service-mapping.html` (menu node
584 'Unit Service Mapping', parent 570 Billing -> 531 Unit Master,
controller UnitServiceMappingController — the DECOY test passes: a REAL
controller with 28 template binds).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the recheck's first new
## LIVE — the sweep's class-name grep missed it, per-screen endpoint truth
## found it; the 585 lesson)

- State: REGISTERED (C5 PASS — node chain 584 -> 570 -> 531). Controller
  binds UnitServiceMappingController (NOT MasterController).
- The screen maps org-level service masters to the unit: speciality feed ->
  subspeciality-by-speciality -> services feed -> save mapping.
- LIVE-VERIFIED BY CALLING (body status):
  - the mapping-list feed GET /billing/organizationMaster/speciality/1/
    subSpeciality/1/org/1/unit/1 -> 200 success listObject [] (dev-empty);
  - the subspeciality feed GET /adt/subspeciality/orgId/1/unitId/1/
    specialityId/1 -> 200 success with REAL rows (Cardiology/CARDIO);
  - the save mapper POST /global/UnitServiceMapper -> 400 (the
    probe-payload class — the save needs the exact mapper contract).

## SCOPED BACKEND CHECK

- The save (UnitServiceMapper) was NOT live-probed: its payload contract is
  mapper-specific, and probing a billing WRITE without the full contract
  risks a false-success reading. Honest position: 'save not exercised,
  contract unknown' — flagged for the BR's scoped write-path check (the
  owner's billing warning: a save that lies is the worst case; a save whose
  contract is unknown must not be guessed at).
- The read feeds are live (above).

## Build state

- Route: NEW at /unitServiceMapping (moduleId 584).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 584
  only route; C5 PASS). ng build exit 0. Smoke: route renders, the
  subspeciality feed's real rows visible (Cardiology), the honest dev-empty
  mapping list, 0 page errors.
