# incident-category (Incident Categories feed — Quality)

Read-only list (shorter loop per DQ-076). THE GATE IS A RENDER TRACE — NOT A
WRITE-VERIFIED SCREEN. No T4/T5; no write to verify.

## T0 — primary read smoked (2026-08-13)
- GET `/api/global/unit/getActiveIncidentCategoryMaster/{orgId}/{unitId}` →
  200 with REAL rows (incidentCategoryId 2, INC3, ...) — the earlier 404 was
  a missing-path-params probe (the endpoint takes the org/unit in the path).

The read completes — buildable per DQ-076. Legacy state ungated; parity route.
