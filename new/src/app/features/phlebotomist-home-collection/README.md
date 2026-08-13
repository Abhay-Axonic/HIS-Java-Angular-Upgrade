# Phlebotomist Home Collection (module 155) — flow map (T0–T6 record)

Rebuilt 2026-08-13 on `rebuild/transfer-screen`. Legacy:
`views/LIS/Phlebotomy/user-allocation-for-home-collection.html` +
`controller/lis/transaction/phlabotomy/userAllocationForHomecollectionController.js`
+ `LIS/.../transaction/controller/PhlebotomistHomeCollectionController.java`
(class `/LIS/PhlebotomistHomeCollection`).

## ⚠ PARITY RECORD — THE FIRST GUARD-RELATED EXCEPTION IN THE PROJECT (PROMINENT, NOT A FOOTNOTE)

Every other route in the app carries a moduleId/moduleIds and the guard.
This one is DIFFERENT in the legacy and the difference is deliberate:

- The legacy state `userAllocationForHomecollection` (app.js:7091) has **NO
  resolve block** — no in-state module check, unlike its neighbours
  (phlebotomyWorkList 274, patientArrival 276, etc.). Its access control is
  the **menu-level module presence**: module **155 ("Home Collection")** is
  in the role's permissions (verified 2026-08-13 in the dev session's 3038
  modules) and the menu renders the item only when the module is present.
- The rebuild therefore registers `phlebotomistHomeCollection` WITH
  `moduleAccessGuard` + `moduleId: 155` — the guard checks exactly what the
  legacy menu checked (module presence in the permissions). This is the
  closest parity to the legacy semantics.
- **Do NOT remove the guard, do NOT add another one silently.** If the
  legacy module mapping changes, update this record. The inline route
  comment in app.routes.ts repeats this warning.
- Preflight behavior (verified 2026-08-13): `check_route_targets.py` only
  validates lazy `loadComponent` targets — it does NOT flag guard presence
  or absence; tsc/ng build neither. No check treats the guard state as a
  defect, so no exception encoding was needed beyond this documentation.

## T0 — write smoke (park_sweep.py, DQ-061)

`POST /LIS/PhlebotomistHomeCollection/save` (@RequestBody List<Dto>) ->
HTTP 200 "Added Successfully." + `lab.t_phlebotomist_home_collection` row
landed (id-based verified, teardown clean). **BUILDABLE.**

Contract (the DAO NPEs on nulls — all REQUIRED):
- `fromTime` / `toTime`: `'HH:mm:ss'` (LocalTime.parse, DAO:45).
- `createdDate` / `updatedDate` + `phlebotomistCollectionDate`:
  `'dd-MM-yyyy HH:mm:ss'` (getDate, DAO:59).
- The write targets its OWN table, NOT the lab sample tables
  (`t_lab_sample_dtls`' 7 flags belong to a different lab flow).

## T1/T2 — reads (probed 2026-08-13)

- The search feed (`getUserAllocationHomeCollectionList`) is a read —
  session-gated like the other reads (the token-only probe 401s; the
  browser session verifies it). Honest-state, unwired in this build.
- Menu reachability: **REACHABLE** — module 155 "Home Collection" is in the
  dev role's module list (menu renders the item). Not a dangle.

## T3 — build

Form: user id + collection date + from/to times + Save ->
`save([entry])` with the session org/unit/user and the REQUIRED date/time
shapes. Honest deferred block for the unwired search feed. Files:
models.ts, service.ts, phlebotomist-home-collection.ts, .html, .scss
(signals + FormsModule + @if/@for). Route registered with the guard (155).

## T4 — write verification

The smoke's failure paths are the evidence: the null-time NPE
(LocalTime.parse) and the null-date NPE (getDate) — both unreachable from
this form (the defaults are populated); the honest 500s (rethrow chain —
the LIS service rethrows, no masking). Idempotency: no natural key — each
save creates a fresh row (append-only collection log).

## T5 — machine

No server-side status machine on the collection save (append-only log).
The allocation "machine" (assigned → collected) lives in the search/status
feeds, which are session-gated and not part of this build — the honest
state covers it.

## T6 — gates

tsc EXIT 0 / ng build EXIT 0 / check_route_targets PASS (360 routes incl.
this one) / browser golden trace on :4201 (merge gate) — see SESSION_LOG.
