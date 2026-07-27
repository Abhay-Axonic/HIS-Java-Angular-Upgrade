export type DietUnitStatus = 'A' | 'I';

/** Row shape returned by `getDietUnitMasterList` (view calls it
 * "Code"/"Description" but the wire fields are `uomName`/`uomDescription`).
 * `DietUnitMasterDaoImpl.getDietUnitMasterList` has NO status filter and
 * NO offset/limit in its SQL at all -- every row for the org, every time,
 * regardless of what `offset`/`noOfRecordsPerPage` the caller sends (see
 * diet-unit-master.ts's class doc comment). */
export interface DietUnitRow {
  uomId: number;
  uomName: string;
  uomDescription: string;
  status: DietUnitStatus;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:43-50). */
export interface DietUnitForm {
  uomId?: number;
  uomName: string;
  uomDescription: string;
}

export function emptyDietUnitForm(): DietUnitForm {
  return { uomId: undefined, uomName: '', uomDescription: '' };
}

/** POST body for `saveDietUnitMaster` (ctrl:145-203). */
export interface DietUnitSavePayload {
  uomName: string;
  uomDescription: string;
  unitId: number;
  organizationId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateDietUnitMaster` (ctrl:241-289) -- no `unitId`, no
 * `status`, matching legacy exactly (neither is ever sent on update). */
export interface DietUnitUpdatePayload {
  uomId: number;
  uomName: string;
  uomDescription: string;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateDietUnitMasterStatus` (ctrl:291-327) -- built from
 * the CURRENT `$scope.common` object (whatever was last loaded/edited),
 * not a fresh minimal payload; modeled here as the same fields since that
 * is the real wire shape. */
export interface DietUnitStatusPayload {
  uomId: number;
  status: DietUnitStatus;
  updatedBy: number;
  updatedDate: string;
}
