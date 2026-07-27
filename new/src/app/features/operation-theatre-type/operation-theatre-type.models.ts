export type OperationTheatreTypeStatus = 'A' | 'I';

/** Row shape returned by `getOperationTheatreTypeList` (ctrl:59-98). */
export interface OperationTheatreTypeRow {
  operationTheatreTypeId: number;
  theatreTypeCode: string;
  theatreTypeName: string;
  theatreTypeDesc: string;
  status: OperationTheatreTypeStatus;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:41-46). */
export interface OperationTheatreTypeForm {
  operationTheatreTypeId?: number;
  theatreTypeCode: string;
  theatreTypeName: string;
  theatreTypeDesc: string;
}

export function emptyOperationTheatreTypeForm(): OperationTheatreTypeForm {
  return { operationTheatreTypeId: undefined, theatreTypeCode: '', theatreTypeName: '', theatreTypeDesc: '' };
}

/** POST body for `saveOperationTheatreType` (ctrl:147-202). Duplicate-check
 * now correctly compares BOTH name and code (fix/operationtheatretype-
 * name-check, merged and live-verified this session). */
export interface OperationTheatreTypeSavePayload {
  theatreTypeCode: string;
  theatreTypeDesc: string;
  theatreTypeName: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  status: 'A';
  unitId: number;
  orgId: number;
  updatedDate: string;
}

/** POST body for `updateOperationTheatreType` (ctrl:234-283) -- includes
 * `createdDate`/`status` even on update, matching legacy's real (slightly
 * odd) payload shape verbatim. */
export interface OperationTheatreTypeUpdatePayload {
  operationTheatreTypeId: number;
  theatreTypeCode: string;
  theatreTypeDesc: string;
  theatreTypeName: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  status: 'A';
  unitId: number;
  orgId: number;
  updatedDate: string;
}

/** POST body for `updateOperationTheatreTypeStatus` (ctrl:289-322). */
export interface OperationTheatreTypeStatusPayload {
  operationTheatreTypeId: number;
  status: OperationTheatreTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
