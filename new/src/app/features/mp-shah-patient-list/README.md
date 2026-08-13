# mp-shah-patient-list (Patient List (MPShah))

Read-only (shorter loop per DQ-076). THE GATE IS A RENDER TRACE — NOT A
WRITE-VERIFIED SCREEN. No T4/T5; no write to verify.

## T0 — primary read smoked (2026-08-13)
- POST `/api/global/unit/getPatientList` payload {'organizationId': 1, 'unitId': 1, 'searchKeyword': ''} → the read
  completes (the Patient List (MPShah) screen renders live through the app's own login flow).

Built from the eleven's batch (DQ-088); the collision precheck passed.
