# User Nursing Station Mapper — Unit Master 531 batch 5 (DQ-2026-08-12-015)

Route `userNursingStationMapper`, module 689.

Lists the user -> nursing-station mapper rows. POST /api/global/getUser-
NursingStationMapperlist with the legacy payload {organisationId, unitId,
offset, noOfRecordsPerPage} (British spelling organisationId — verified in
the legacy controller). Confirmed live 2026-08-12: SUCCESS with 1 real row
(userId 1, employeeName "Admin  Admin", empCode "EMP123").
