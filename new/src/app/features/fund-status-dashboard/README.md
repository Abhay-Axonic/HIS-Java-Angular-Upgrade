# fundStatusDashboard (Billing — global fund status)

## What this is

The billing global fund-status dashboard (legacy state `fundStatusDashboard`,
app.js:20945, `/fundStatusDashboard`). Read-only dashboard — no write path.

## THE GATE IS A RENDER TRACE — NOT A WRITE-VERIFIED SCREEN

Per the read-side-drift rule (DQ-076) and the shorter loop: this screen was
T0-smoked (primary reads), built, and merge-gated by a RENDER TRACE (the
route renders with the live feeds). It is NOT write-verified — there is no
write to verify. Nobody may read this README as a write-tested screen.

## T0 — primary reads smoked (2026-08-13)

- POST `/api/billing/getActiveTrustList` {orgId, unitId} → 200 success (empty list)
- GET `/api/billing/getTrustList/1/1` → 200 success (empty list)
- POST `/api/billing/getFundCollectionListByTrust/1/1` → 200 (empty object —
  the 405 on GET was a wrong-method probe; POST is the method)
All three reads survive — the billing family's writes are drift-blocked
(m_billing_master flags) but these reads complete. Buildable per DQ-076.

## Parity / guard note

Legacy state ungated (no resolve); the new route carries no guard — parity.

## Honest states

- Trust list empty in dev → the empty-state renders ("no trusts configured")
  — correct, not broken.
- Fund-collection list empty → same.
