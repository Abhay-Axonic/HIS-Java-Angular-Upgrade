# Unit Master 531 — billing/unit family batch 1 (DQ-2026-08-12-008)

Three screens rebuilt (the other three of the family's six — doctorConsulation-
Master, doctorWiseShare, doctorReferralShareMaster — were ALREADY rebuilt on
main under alias routes; see DQ-2026-08-12-008):

| route | module | feature dir | backend |
|-------|--------|-------------|---------|
| patientType | 764 | patient-type-master | GET /api/billing/getPatientType/{org}/{unit} (REAL data, 3 rows); POST save/update/updateStatus |
| patientTypeDetails | 765 | patient-type-details | same list endpoint; GET getPatientTypeServiceWiseDetails/{id}/{org}/{unit}; POST savePatientTypeWiseDetails / updatePatientTypeServiceStatus |
| pharmacyTariffMaster | 803 | pharmacy-tariff-master | GET /api/units/pharmacy/tariff/getAll/{org}/{unit} (REAL data, 3 rows); POST save/update; GET update/status/{id}/{status} |

## Envelope note (owner's per-batch check)

ALL three are REAL data or honest empty — ZERO generic envelopes. The family
uses a THIRD envelope shape distinct from commonMaster's: `stores`,
`listObject2`, `resObject` fields alongside `listObject`. Live-verified
2026-08-12: patientType "List Size DB: 3", patientTypeDetails "List Size
Cache: 3", pharmacyTariffMaster 3 rows (Pharmaceutical / Test PMS Product
Category).

## House pattern

signal() + @if/@for only (C9); API_BASE_URL injected; no manual HttpHeaders
(auth interceptor owns auth); endpoints from the legacy controllers verbatim
(method + path + payload), not guessed.
