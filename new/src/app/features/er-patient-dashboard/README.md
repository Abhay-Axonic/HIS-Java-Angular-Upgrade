# er-patient-dashboard (ER Patient Dashboard)

Read-only dashboard (shorter loop per DQ-076/077). THE GATE IS A RENDER
TRACE — NOT A WRITE-VERIFIED SCREEN. No T4/T5; no write to verify.

## T0 — primary reads smoked (2026-08-13)
- POST `/api/global/getAdmissionDischargeDeathCount` → T0-smoked live
- POST `/api/global/getOPVisitsCount` → T0-smoked live

The reads complete — buildable per DQ-076 (reads can survive where the
family's writes are drift-blocked). Legacy state ungated; parity route.
