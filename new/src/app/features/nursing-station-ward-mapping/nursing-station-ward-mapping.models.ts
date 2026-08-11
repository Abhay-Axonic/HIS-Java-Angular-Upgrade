export type MappingStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/adt/unit/getNursingStationWardMapperList
 * (legacy nursingStationWardMappingController.js; GETNURSINGSTATIONWARDMAPPER-
 * LIST = "/api/adt/unit/getNursingStationWardMapperList" from
 * adt.url.config.js:650). Confirmed live 2026-08-12: SUCCESS with 1 real row
 * — nursingStationId 1, nursingStationDesc "Updated Desc", status 'A'.
 * NOTE the legacy payload uses recordPerPage (not noOfRecordsPerPage).
 */
export interface NursingStationWardMappingRow {
  nursingStationId: number;
  nursingStationDesc: string;
  status: MappingStatus;
}
