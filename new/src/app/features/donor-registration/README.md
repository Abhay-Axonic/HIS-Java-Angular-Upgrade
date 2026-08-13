# Donor Registration — screen 719 (blood bank)

Transaction-class rebuild (flow map T0–T6, 2026-08-12). The legacy state
`donorRegistration` (app.js:18128, donorRegistrationController) is a
donor-registration FORM — not a master list. Previously shipped as a
prefix-list shell (counted in the master close-out); this rebuild replaces
the shell with the registration form and RECLASSIFIES the dir as a
transaction screen (REBUILD_COUNT row 6, count unchanged — one dir counted
once).

## T0 — write smoke: PASS, BUILDABLE (DQ-056)

`tools/smoke_donor_registration_write.py` — POST `/api/bloodbank/donorRegistration`
with the exact payload → HTTP 200 honest success envelope, `t_donor_registration`
+ `t_donor_details` rows landed, teardown clean, exit 0. The chain is HONEST:
every catch logs-and-throws ServiceException (real HTTP 500s with the
message, never the masked 501). Write tables are drift-clean (the phantom
column class does NOT hit this flow — verified via the drift map).

Payload contract (literal to `DonorRegistrationDto`, verified 2026-08-12):
- `dob` MUST be `"DD-MM-YYYY HH:mm:ss"` (Dozer mapping date-format; other
  shapes fail with the honest 500 "Unable to parse source object using
  specified date format" — variant-probed).
- `createdDate`/`updatedDate` are accepted in the same format and stored.
- `bloodGroupId` / `donoTypeId` / document-expiry are **NOT DTO fields** —
  the legacy form validates them, the backend drops them at registration
  (the screening flow consumes them later). The form validates them for
  parity but the payload carries only DTO-known fields.
- `patientId` omitted → skips the checkPatientAvailability branch.

## Feeds (all verified live 2026-08-12)

| Feed | Endpoint | State |
|---|---|---|
| Prefix | GET /adt/getActivePrefixList | LIVE (1 row) |
| Gender | GET /adt/getActiveGenderList | LIVE (3 rows) |
| Nationality | GET /adt/getActiveNationalityList | LIVE, dev-empty (BR-038) |
| Blood group | GET /global/bloodBank/getActiveBloodGroupMaster/1/1 | LIVE |
| Donor type | GET /global/bloodBank/getActiveBloodDonorTypeMaster/1/1 | LIVE |
| Identification | GET /adt/getActiveIdentificationList | LIVE (4 rows) |
| Patient autofill | POST /api/bloodbank/patientAutofillSearch | **TODO STUB** (empty 200) — NOT wired; control omitted (honest state) |

## Flow findings (DQ-056)

- `donorNumber` is NULL (success envelope reads "Donor null Saved
  Successfully") — `getGeneratedNo(0, unitId, DONORNUMBER)` returns null
  because `public.t_prefix_running_number` has ZERO rows. Write lands;
  number pending config (backend/config-owned). The rebuild renders the
  honest state — never "Donor null".
- The save chain calls the hardcoded external MABIS biometric endpoint when
  `thumbImage` is sent — thumbImage is NOT sent by this form (external
  dependency flagged for the owner).
- T4: idempotency replay demonstrated — second POST with the same mobile is
  rejected (" Donor Already exist"), no double row; audit fields land
  (status 'A', created_by/updated_by, timestamps).
- T5: the registration machine is new→registered; re-entry blocked by the
  dup guard. Screening/deferral transitions belong to the donor-screening
  screen's flow (honest chains, DonorScreeningDetailController).
- BR-2026-08-12-038 (candidate, NOT applied): nationality validation relaxed
  to "required only when the feed has rows" (dev master empty).

## Gates

`tsc -p tsconfig.app.json` EXIT 0 · `ng build` EXIT 0 · `check_route_targets.py`
PASS · golden trace through the real browser (dev server :4201) — form
renders, save completes, row verified in psql, teardown clean.
