export type AnaesthesiaTypeStatus = 'A' | 'I';

/**
 * Row shape from `getListAnesthesiaTypeMaster`'s `listObject`
 * (`AnesthesiaTypeMaster.java`, `GET_LIST_ANESTHESIA_TYPE_MASTER`):
 * `anesthesiaTypeId, anesthesiaTypeDesc, anesthesiaTypeName, anesthesiaTypeCode, status, orgId, unitId`.
 * No created/updated audit fields returned in the list.
 */
export interface AnaesthesiaTypeRow {
  anesthesiaTypeId: number;
  anesthesiaTypeCode: string;
  anesthesiaTypeName: string;
  anesthesiaTypeDesc: string;
  status: AnaesthesiaTypeStatus;
  orgId: number;
  unitId: number;
}

/** Editable form state -- mirrors `$scope.common`. */
export interface AnaesthesiaTypeForm {
  anesthesiaTypeId: number | null;
  anesthesiaTypeCode: string;
  anesthesiaTypeName: string;
  anesthesiaTypeDesc: string;
}

export function emptyAnaesthesiaTypeForm(): AnaesthesiaTypeForm {
  return { anesthesiaTypeId: null, anesthesiaTypeCode: '', anesthesiaTypeName: '', anesthesiaTypeDesc: '' };
}

/** POST body for `saveAnesthesiaTypeMaster` (ctrl:`$scope.common`, full object). */
export interface AnaesthesiaTypeSavePayload {
  anesthesiaTypeCode: string;
  anesthesiaTypeName: string;
  anesthesiaTypeDesc: string;
  status: 'A';
  orgId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateAnesthesiaTypeMaster`. */
export interface AnaesthesiaTypeUpdatePayload {
  anesthesiaTypeId: number;
  anesthesiaTypeCode: string;
  anesthesiaTypeName: string;
  anesthesiaTypeDesc: string;
  orgId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateAnesthesiaTypeMasterStatus`. */
export interface AnaesthesiaTypeStatusPayload {
  anesthesiaTypeId: number;
  status: AnaesthesiaTypeStatus;
  orgId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}
