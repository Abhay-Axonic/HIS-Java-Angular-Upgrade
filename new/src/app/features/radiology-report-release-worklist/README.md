# radiology-report-release-worklist (Radiology Report Release Worklist)

Read-only (shorter loop per DQ-076). THE GATE IS A RENDER TRACE — NOT A
WRITE-VERIFIED SCREEN. No T4/T5; no write to verify.

## T0 — primary read smoked (2026-08-13)
- POST `/adt/getSpecialityListByUserIdByUnitId` payload {'userId': 1, 'unitId': 1, 'organizationId': 1} → the read
  completes (the Radiology Report Release Worklist screen renders live through the app's own login flow).

Built from the eleven's batch (DQ-088); the collision precheck passed.
