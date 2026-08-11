export type PassBedCategoryMapperStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/unit/getVisitorPassMapperList
 * (legacy passBedCategoryMapperController.js:120-135, URI hardcoded at
 * ctrl:126). Projection is what the legacy view reads
 * (views/master/UnitMaster/Visit Type/pass-Bed-Category-Mapper.html:137-144 —
 * visitorPassTypeDesc/bedCategoryDesc/passDesc/noOfPass cells,
 * visitorPassMapperId passed to updateStatus).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface PassBedCategoryMapperRow {
  visitorPassMapperId: number;
  visitorPassTypeDesc: string;
  bedCategoryDesc: string;
  passDesc: string;
  noOfPass: number;
  status: PassBedCategoryMapperStatus;
}
