export type BloodBagCapacityStatus = 'A' | 'I';

/** Row shape from `getBloodBagCapacityMasterList`'s `listObject`
 * (model.java:17-25): `bloodBagCapacityId, bloodBagCapacityCode,
 * bloodBagCapacityDesc, organizationId, unitId, status`. */
export interface BloodBagCapacityRow {
  bloodBagCapacityId: number;
  bloodBagCapacityCode: string;
  bloodBagCapacityDesc: string;
  organizationId: number;
  unitId: number;
  status: BloodBagCapacityStatus;
}

/** Editable form state -- mirrors `$scope.common`, but deliberately kept
 * isolated from status-toggle actions (see component class doc comment --
 * this is the fix for the logged wrong-record write race). */
export interface BloodBagCapacityForm {
  bloodBagCapacityId: number | null;
  bloodBagCapacityCode: string;
  bloodBagCapacityDesc: string;
}

export function emptyBloodBagCapacityForm(): BloodBagCapacityForm {
  return { bloodBagCapacityId: null, bloodBagCapacityCode: '', bloodBagCapacityDesc: '' };
}

/** POST body for `saveBloodBagCapacityMaster` (ctrl:161-173). */
export interface BloodBagCapacitySavePayload {
  bloodBagCapacityCode: string;
  bloodBagCapacityDesc: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateBloodBagCapacityMaster` (ctrl:270-279) -- no status/createdBy. */
export interface BloodBagCapacityUpdatePayload {
  bloodBagCapacityId: number;
  bloodBagCapacityCode: string;
  bloodBagCapacityDesc: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateBloodBagCapacityMasterStatus`. Deliberately built
 * from ONLY the toggled row's own id/status -- see component class doc
 * comment (the fix for the logged wrong-record write race; legacy
 * reuses/mutates the shared `$scope.common` edit-form object for this
 * instead). */
export interface BloodBagCapacityStatusPayload {
  bloodBagCapacityId: number;
  status: BloodBagCapacityStatus;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}
