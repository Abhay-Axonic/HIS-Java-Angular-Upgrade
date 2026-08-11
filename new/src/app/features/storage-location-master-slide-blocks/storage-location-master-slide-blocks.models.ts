export type RackStatus = 'A' | 'I';

/**
 * Row shape returned by GET /LIS/unit/listRackMaster/{orgId}/{unitId}/
 * {offset}/{size} (legacy storageLocationMstForSlideBlocksController.js:94-95
 * list URI1). Projection of RackMasterDto via the GET_RACK_MASTER_LIST query
 * (RackMasterDaoImpl.java:350-354).
 *
 * Dev-empty: the DAO answers status 'error' with the plain message
 * "Rack Records not found." when the dev DB has no rows
 * (RACK_RECORDS_NOT_FOUND, ICommonConstants.java:815, returned at
 * RackMasterDaoImpl.java:356-359) -- the component surfaces that message
 * verbatim instead of masking it behind a generic banner.
 */
export interface StorageRackRow {
  rackId: number;
  rackCode: string;
  rackName: string;
  status: RackStatus;
}
