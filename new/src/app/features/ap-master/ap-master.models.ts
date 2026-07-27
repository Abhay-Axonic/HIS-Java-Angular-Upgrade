export type ApMasterStatus = 'A' | 'I';

/** Row shape returned by `getAPCodeMasterList` (ctrl:69-79). */
export interface ApMasterRow {
  aPCodeId: number;
  aPCode: string;
  aPCodeDesc: string;
  status: ApMasterStatus;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:38-46). */
export interface ApMasterForm {
  aPCodeId?: string; // string on edit-load, see class doc comment
  aPCode: string;
  aPCodeDesc: string;
}

export function emptyApMasterForm(): ApMasterForm {
  return { aPCodeId: undefined, aPCode: '', aPCodeDesc: '' };
}

/** POST body for `saveAPCodeMaster` (ctrl:139-198). */
export interface ApMasterSavePayload {
  aPCode: string;
  aPCodeDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
  unitId: number;
  orgId: number;
}

/** POST body for `updateAPCodeMaster` (ctrl:232-277) -- `aPCodeId` sent as
 * whatever type the form currently holds it as (string, from edit-load's
 * `.toString()` coercion -- see class doc comment); no `status` field. */
export interface ApMasterUpdatePayload {
  aPCodeId: string;
  aPCode: string;
  aPCodeDesc: string;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  orgId: number;
}

/** POST body for `updateAPCodeMasterStatus` (ctrl:279-319). */
export interface ApMasterStatusPayload {
  aPCodeId: number;
  status: ApMasterStatus;
  createdDate: string;
  updatedDate: string;
  unitId: number;
  orgId: number;
}
