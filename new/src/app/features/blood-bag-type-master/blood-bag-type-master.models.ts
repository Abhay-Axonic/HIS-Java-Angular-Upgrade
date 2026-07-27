export type BloodBagTypeStatus = 'A' | 'I';

/** Row shape returned by `getBloodBagTypeMasterList` (ctrl:70-76). */
export interface BloodBagTypeRow {
  bloodBagTypeId: number;
  bloodBagTypeCode: string;
  bloodBagTypeDesc: string;
  itemName: string;
  capacityMax: number | string;
  capacityMin: number | string;
  status: BloodBagTypeStatus;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:25-33). */
export interface BloodBagTypeForm {
  bloodBagTypeId?: number;
  bloodBagTypeCode: string;
  bloodBagTypeDesc: string;
  itemName: string;
  capacityMax: string;
  capacityMin: string;
}

export function emptyBloodBagTypeForm(): BloodBagTypeForm {
  return {
    bloodBagTypeId: undefined,
    bloodBagTypeCode: '',
    bloodBagTypeDesc: '',
    itemName: '',
    capacityMax: '',
    capacityMin: '',
  };
}

/** POST body for `saveBloodBagTypeMaster` (ctrl:126-225). */
export interface BloodBagTypeSavePayload {
  bloodBagTypeCode: string;
  bloodBagTypeDesc: string;
  itemName: string;
  capacityMax: string;
  capacityMin: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateBloodBagTypeMaster` (ctrl:263-336) -- no `status`, no `createdBy`. */
export interface BloodBagTypeUpdatePayload {
  bloodBagTypeId: number;
  bloodBagTypeCode: string;
  bloodBagTypeDesc: string;
  itemName: string;
  capacityMax: string;
  capacityMin: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateBloodBagTypeMasterStatus` (ctrl:342-378). */
export interface BloodBagTypeStatusPayload {
  bloodBagTypeId: number;
  status: BloodBagTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
