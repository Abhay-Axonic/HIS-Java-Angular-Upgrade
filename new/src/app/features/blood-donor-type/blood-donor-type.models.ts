export type BloodDonorTypeStatus = 'A' | 'I';

/** Row shape from `getBloodDonorTypeMasterList`'s `listObject`
 * (`BloodDonorTypeMaster.java:19-27`): `bloodDonorTypeId, bloodDonorTypeCode,
 * bloodDonorTypeDesc, organizationId, unitId, status`. */
export interface BloodDonorTypeRow {
  bloodDonorTypeId: number;
  bloodDonorTypeCode: string;
  bloodDonorTypeDesc: string;
  organizationId: number;
  unitId: number;
  status: BloodDonorTypeStatus;
}

/** Editable form state -- mirrors `$scope.common`. */
export interface BloodDonorTypeForm {
  bloodDonorTypeId: number | null;
  bloodDonorTypeCode: string;
  bloodDonorTypeDesc: string;
}

export function emptyBloodDonorTypeForm(): BloodDonorTypeForm {
  return { bloodDonorTypeId: null, bloodDonorTypeCode: '', bloodDonorTypeDesc: '' };
}

/** POST body for `saveBloodDonorTypeMaster` (ctrl:169-181). */
export interface BloodDonorTypeSavePayload {
  bloodDonorTypeCode: string;
  bloodDonorTypeDesc: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateBloodDonorTypeMaster` (ctrl:278-287) -- no status/createdBy. */
export interface BloodDonorTypeUpdatePayload {
  bloodDonorTypeId: number;
  bloodDonorTypeCode: string;
  bloodDonorTypeDesc: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateBloodDonorTypeMasterStatus`. */
export interface BloodDonorTypeStatusPayload {
  bloodDonorTypeId: number;
  status: BloodDonorTypeStatus;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}
