export type BloodBankStatus = 'A' | 'I';

/** Row shape returned by `getBloodBankMasterList` (ctrl:70-76). */
export interface BloodBankRow {
  bloodBankId: number;
  bloodBankCode: string;
  bloodBankDesc: string;
  address: string;
  contactNo: string;
  status: BloodBankStatus;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:28-35). */
export interface BloodBankForm {
  bloodBankId?: number;
  bloodBankCode: string;
  bloodBankDesc: string;
  address: string;
  contactNo: string;
}

export function emptyBloodBankForm(): BloodBankForm {
  return { bloodBankId: undefined, bloodBankCode: '', bloodBankDesc: '', address: '', contactNo: '' };
}

/** POST body for `saveBloodBankMaster` (ctrl:136-225). */
export interface BloodBankSavePayload {
  bloodBankCode: string;
  bloodBankDesc: string;
  address: string;
  contactNo: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateBloodBankMaster` (ctrl:263-336) -- no `status`. */
export interface BloodBankUpdatePayload {
  bloodBankId: number;
  bloodBankCode: string;
  bloodBankDesc: string;
  address: string;
  contactNo: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateBloodBankMasterStatus` (ctrl:342-378). */
export interface BloodBankStatusPayload {
  bloodBankId: number;
  status: BloodBankStatus;
  updatedBy: number;
  updatedDate: string;
}
