# mlc-reporting-worklist (MLC Reporting Worklist, Billing)

Read-only worklist (shorter loop per DQ-076). THE GATE IS A RENDER TRACE —
NOT A WRITE-VERIFIED SCREEN. No T4/T5; no write to verify.

## T0 — primary read smoked (2026-08-13)
- POST `/adt/getPatientListMedicolegalDetailsMLCWorklist` {organizationId, unitId} → 200 success (empty list)

The read completes — buildable per DQ-076. Legacy state ungated; parity route.
