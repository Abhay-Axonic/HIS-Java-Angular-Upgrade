export type ArMasterStatus = 'A' | 'I';

/** Row shape from `getARCodeMasterList`'s `listObject` -- confirmed exactly
 * 4 fields (`ARCodeMaster.java:17-26`): `aRCodeId, aRCodeDesc, aRCode, status`.
 * No org/unit/audit fields returned in the list. `getARCodeMasterById`
 * returns the same 4-field shape. */
export interface ArMasterRow {
  aRCodeId: number;
  aRCode: string;
  aRCodeDesc: string;
  status: ArMasterStatus;
}

/** Editable form state -- mirrors `$scope.common`. */
export interface ArMasterForm {
  aRCodeId: number | null;
  aRCode: string;
  aRCodeDesc: string;
}

export function emptyArMasterForm(): ArMasterForm {
  return { aRCodeId: null, aRCode: '', aRCodeDesc: '' };
}

/** POST body for `saveARCodeMaster` (ctrl:135-146). */
export interface ArMasterSavePayload {
  aRCode: string;
  aRCodeDesc: string;
  status: 'A';
  createdDate: string;
  createdBy: number;
  unitId: number;
  orgId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateARCodeMaster` (ctrl:223-232) -- deliberately no `status`. */
export interface ArMasterUpdatePayload {
  aRCodeId: number;
  aRCode: string;
  aRCodeDesc: string;
  unitId: number;
  orgId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateARCodeMasterStatus` (ctrl:270-278). */
export interface ArMasterStatusPayload {
  status: ArMasterStatus;
  aRCodeId: number;
  unitId: number;
  orgId: number;
  updatedBy: number;
  updatedDate: string;
}
