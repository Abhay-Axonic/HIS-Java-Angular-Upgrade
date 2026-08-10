# Unit Service Master — screen 27 (module 583, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/unit-service-master.html` (menu node
583 'Unit Service Master', parent 570 Billing -> 531 Unit Master; the state
binds UnitServiceMasterController — a REAL screen, verified by the DECOY
test).

## CLASSIFICATION — LIVE with a BROKEN LIST (C7 + C8 at selection)

- State: REGISTERED app.js:9496, url /unitServiceMaster, controller
  UnitServiceMasterController (NOT MasterController — passes the DECOY test).
- Menu node: 583 'Unit Service Master', chain 583 -> 570 -> 531 (C5 PASS).
- Backend: UnitServiceMasterDetailsController (Global, /api/global/*) —
  POST UnitServiceMasterDetails (save), GET list
  (UnitServiceMasterDetails/org/{orgId}/unit/{unitId}/{offset}/{recordPerPage}),
  count, byId, PUT (update), getAllActiveTax.
- LIVE-VERIFIED BY CALLING (body status): the COUNT works — GET
  /api/global/UnitServiceMasterDetails/count/org/1/unit/1 -> 200 success
  object: 1 (a row EXISTS in dev). The LIST is UNCONDITIONALLY BROKEN — GET
  .../org/1/unit/1/0/20 -> HTTP 200 body {"status":"error","code":"501",
  "message":"Something went wrong!!"} on every call.

## BROKEN LIST — a real legacy defect on the Billing read path (the walk's
## masked-error class, screen-17 shape; NOT a phantom)

ROOT CAUSE (verified live, the server stack at catalina 8014434):
IllegalArgumentException at the AliasToBean transform —
UnitServiceMasterDetailsDto.isOutsourceJob is `char` (DTO:17; isOutsource
DTO:15 is the same latent shape) while the SQL column is_outsource_job
returns a String ('N'); Hibernate cannot set String->char. The DAO's
catch-and-return (UnitServiceMasterDetailsDaoImpl:101-106) SWALLOWS the
exception and returns the ERROR envelope with HTTP 200 — the masked-error
shape: the caller sees HTTP 200 and must read the body status (the walk's
screen-17 class). The count works (its query doesn't use AliasToBean with
char fields). The list is broken for EVERY configuration, not just dev.

NOT FIXED (Rule 10) — a backend BR candidate (the char-field DTO type vs
SQL column). The rebuild CALLS the list, detects the error envelope, and
surfaces the defect honestly with the named mechanism (the screen-17/24/25
pattern) — the screen cannot be green while the list 501s.

## Scoped backend check

- Masked-error shape: FINDING (above — HTTP 200 + body 501, the
  catch-and-return swallow at DAO:101-106; a NEW mechanism vs the phantom
  class — the DTO-field-type mismatch).
- False-success on the WRITE path: the save (POST UnitServiceMasterDetails)
  was not probed (the list being broken blocks the screen's write flow);
  the write path carries the same catch-and-return pattern risk — flagged
  for the write-path check when the BR is taken.
- Dup-check / INNER-JOIN: the list query is a 10+ table multi-join (the
  biggest in the walk so far) — the INNER-JOIN drop risk is present by
  construction; noted, not live-verified (the list 501s before the joins
  can drop anything).

## Build state

- Route: NEW at /unitServiceMaster (moduleId 583).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 583
  used only by this route; C5 PASS with the node chain). ng build exit 0.
  Smoke: route renders, the honest defect banner names the char-field
  mechanism with the count evidence, 0 page errors.
