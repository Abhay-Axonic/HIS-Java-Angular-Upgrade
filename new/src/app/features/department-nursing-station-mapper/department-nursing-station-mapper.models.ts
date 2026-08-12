export type MapperStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/global/getDepartmentNursingStationMapperlist
 * (legacy master/global/departmentNursingStationMapperController.js; payload
 * {organisationId, unitId, noOfRecordsPerPage, offset} — British spelling).
 * Verified live 2026-08-12 (DQ-032 close-out): SUCCESS with 2 real rows
 * (departmentNursingStationMpprId null, nursingStationId null, departmentId
 * null, organisationId 1, unitId 1, status A) — REAL DATA, the final master
 * batch (a).
 */
export interface DepartmentNursingStationMapperRow {
  departmentNursingStationMpprId: number;
  nursingStationId: number;
  departmentId: number;
  organisationId: number;
  unitId: number;
  status: MapperStatus;
  departmentName?: string;
  nursingStationName?: string;
}
