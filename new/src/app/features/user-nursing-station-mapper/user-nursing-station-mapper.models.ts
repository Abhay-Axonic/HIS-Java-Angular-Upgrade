export type MapperStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/global/getUserNursingStationMapperlist
 * (legacy userNursingStationMapperController.js; GET_USER_NURSING_STATION_
 * MAPPER_LIST = "/api/global/getUserNursingStationMapperlist" from
 * global.url.config.js:200). Confirmed live 2026-08-12: SUCCESS with 1 real
 * row — userId 1, employeeName "Admin  Admin", empCode "EMP123", status 'A'.
 */
export interface UserNursingStationMapperRow {
  userId: number;
  employeeName: string;
  empCode: string;
  status: MapperStatus;
}
