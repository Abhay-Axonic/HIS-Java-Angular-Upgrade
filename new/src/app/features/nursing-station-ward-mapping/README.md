# Nursing Station Ward Mapping — Unit Master 531 batch 5 (DQ-2026-08-12-015)

Route `nursingStationWardMapping`, module 548.

Lists the nursing station -> ward mapping rows. POST /api/adt/unit/get-
NursingStationWardMapperList with the legacy payload {organizationId, unitId,
offset, recordPerPage} — NOTE recordPerPage, NOT noOfRecordsPerPage (verified
legacy nursingStationWardMappingController.js:163-167; the wrong field name
returns 400 "Bad request: Invalid parameters"). Confirmed live 2026-08-12:
SUCCESS with 1 real row (nursingStationId 1, "Updated Desc", status A).
