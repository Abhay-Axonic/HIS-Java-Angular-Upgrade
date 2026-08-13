# Shift Closing (600) — flow map (T0–T6 record)

Rebuilt 2026-08-13 on `rebuild/transfer-screen`. Legacy:
`templates/views/master/UnitMaster/billingMaster/shift-closing.html` +
`controller/billing/cashDeskManagement/global/shiftClosingController.js` +
`Billing/.../cashDesk/controller/ShiftClosingMasterController.java`.

## T0 — write smoke (park_sweep.py, DQ-059)

`POST /api/global/saveShiftCloseBalance` (ShiftClosingMasterController:146,
honest rethrow — no masking) -> HTTP 200 "Added Successfully." +
`billing.m_shift_close_balance` row landed (id-based verified, teardown
clean). **BUILDABLE.**

Contract findings:
- **Entity inversion**: the `ShiftClosingMaster` entity maps
  `billing.m_shift_close_balance` (NOT `m_shift_close_master` — that table
  holds a 2026-08-03 dev row written by a different path).
- **Date mapper trap**: the Dozer format is `dd-M-yyyy` — the smoke's
  `"2099-01-01 00:00:00"` became year 0006 (day/month rollover). The form
  sends `dd-M-yyyy` (no time). Any fixture must use id-based markers, never
  by-date counts.
- `listOfIds` (receipt-export branch): the DAO calls `size()` before the
  null check (NPE on null; ShiftClosingMasterDaoImpl:392) — the form sends
  `[]` (skips the branch).
- The `t_denomination_details.cash_in_out_id` drift belongs to the
  cash-handover write (`/api/global/saveCashHandoverMaster`, DQ-062), NOT
  this endpoint.

## T1/T2 — reads (probed 2026-08-13)

- `getRefundList`: HTTP 400 + stray-quote SQL bug at
  ShiftClosingMasterDaoImpl:541-542 — not consumable (honest state, unwired).
- `getShiftClosingBalanceSheet` / `getCashHandoverDetails`: **401
  "UNAUTHORIZED"** with or without the cached token (the reads need the full
  session; the token header alone is rejected) — unwired; the golden-trace
  session is the verification path.
- Security-surface finding (DQ-065): the WRITES (this save + the cash
  handover) execute with NO Authorization header while the reads 401 —
  money-class inconsistency, queued for the backend owner.

## T3 — build

Form: difference + refund amount + Save -> `saveShiftCloseBalance` with the
session org/unit/user, `dd-M-yyyy` dates, `listOfIds: []`. Honest state for
the unwired reads (deferred block retained). Files: models.ts, service.ts,
shift-closing.ts, .html, .scss (signals + FormsModule + @if/@for).

## T4 — write verification

The smoke's failure paths stand as evidence: the `dd-M-yyyy` mangling probe
(by-date count falsely read 0 — the id-based count caught the landed row),
the `listOfIds` null-NPE path (backend defect, unreachable from this form),
and the qty-skip branch (cash-handover flow). Idempotency: the balance save
creates a fresh row per call (no natural key) — the form is a cash-entry
write, not an idempotent one; duplicate-close prevention is a status-machine
question (T5) not enforced server-side.

## T5 — machine

No server-side status preconditions on saveShiftCloseBalance (the balance
row is append-only; the shift-closing "machine" lives in the UI
convention). The transfer-screen pattern (server preconditions absent ->
log-and-proceed, Amendment 9) applies. The cash-handover write (the
denomination entry) is the same class as the shift-close but drift-blocked
(DQ-062) — the shift-closing screen's full close loop cannot be completed
until the DDL fix lands.

## T6 — gates

tsc EXIT 0 / ng build EXIT 0 / check_route_targets PASS / browser golden
trace on :4201 (merge gate) — see SESSION_LOG for the trace record.
