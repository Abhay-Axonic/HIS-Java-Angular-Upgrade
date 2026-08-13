# BR-2026-08-12-040 — AUTH-GAP MEASUREMENT BRIEF (corrected, 2026-08-13)

**Bottom line: there is NO unauthenticated-write surface.** The original
two-instance finding ("cash-desk writes accept no auth header") is
**RETRACTED as a security finding** — mechanical cause: the park-sweep HTTP
helper (tools/park_sweep.py:50-61 `post()`) sends the cached bearer token +
identity headers on every request, so the smokes were never header-less; the
author's reading was wrong. Measured correction below.

## 1. What was measured (tools/auth_gap_probe.py, live 2026-08-13)

A dozen write endpoints across six families, three request variants each:

| variant | result |
|---|---|
| no Authorization header | **401 on all 12** (the filter covers every sampled write) |
| `Authorization: Bearer TOTALLY-INVALID-TOKEN-123` | **401 on all 3 spot-checked** (shift-close, donor, io-chart) — the token IS validated |
| cached dev token (tools/.token_cache.json) | 200 + rows land (the normal authenticated path) |

Sampled endpoints: saveIntakeOutputChart (opd), saveShiftCloseBalance +
saveCashHandoverMaster (billing cash-desk), saveIssueBloodBag (blood bank),
PhlebotomistHomeCollection/save + addSample (LIS), savePatientAdmission +
acceptTransferRequestForADT (ADT), registerDonor (blood bank),
saveNursingOrderMapper (coversheet), walkins/save/prescription (pharmacy),
scheduleAppointment (opd). **12/12 gated, 0/12 header-less-accepting.**

## 2. The residual asymmetry (operational, NOT a security defect)

The SAME valid token that authenticates the writes is rejected by the reads:
getShiftClosingBalanceSheet / getCashHandoverDetails / getRefundList return
401 "UNAUTHORIZED ##/api/global/..." even with a freshly re-minted 145-char
token. The reads validate the full login session (the cookieObject/session
store), not the bearer header alone. The writes authenticate on the bearer
token; the reads demand the session. Consequence: an API client (curl, the
smoke tools, a rebuilt screen without the session cookie) can WRITE with the
token but cannot READ the same controller — the read side is deliberately
stricter. Nothing about this is forgeable: the writes validate the token
claims; the audit trail is intact.

## 3. Why the asymmetry points at the session store, not the controllers

Same controller (ShiftClosingMasterController), same @RequestMapping, same
annotations — the writes execute with a bearer token while the reads 401 with
the identical header. The per-controller code cannot produce that split; the
difference lives in the filter/interceptor layer where the read paths are
session-checked and the write paths are token-checked. The earlier
"filter covers reads, not writes" hypothesis is now measured as: the filter
covers ALL of them; the reads additionally require session state.

## 4. Decision for the backend owner

- **Nothing to fix for authentication.** The sampled write surface is
  token-validated end to end. BR-040 is retracted as a security finding
  (mechanical cause above) and the DQ-065 queue entry is amended.
- Optional operational note: API clients (non-browser) cannot read
  cash-desk endpoints with the token alone — if headless read access is
  wanted, the read gate would need a token-accepting mode (backend-owned
  choice, NOT recommended from a frontend rebuild).
- The park-sweep smokes remain valid as WRITE-path evidence (they ran the
  normal authenticated path); none of the drift/park conclusions change.
