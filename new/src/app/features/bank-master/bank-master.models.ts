export type ActiveStatus = 'A' | 'I';

/** Branch dropdown option (ctrl:42-72, GETACTIVEBANKBRANCHMASTERLIST). */
export interface BranchOption {
  branchId: number;
  branchDesc: string;
}

/** Row shape returned by GETBANKMASTERLIST (view:182-198). */
export interface BankRow {
  bankId: number;
  branchDesc: string;
  bankCode: string;
  bankName: string;
  bankAddress: string;
  status: ActiveStatus;
}

/** Shape returned by GETBANKMASTERBYID (ctrl:236-266) -- assigned wholesale
 * to `$scope.common`, then `branchId` is immediately re-stamped as a string
 * (ctrl:251, `.toString()`) to match the `<select>` binding. */
export interface BankDetail {
  bankId: number;
  branchId: number;
  bankCode: string;
  bankName: string;
  bankAddress: string;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:48-55). Legacy's own
 * `initBankMaster()` object literal also includes a `branchDesc` field that
 * is NEVER bound to any input anywhere in the view -- it stays `""` for the
 * lifetime of the page and is only read back out again in `save()`'s
 * request body. Deliberately NOT modeled as an editable form field here
 * (there's nothing to edit); the save payload below hardcodes it to `''`
 * instead, matching legacy's own always-empty value exactly. */
export interface BankForm {
  bankId?: number;
  /** `<select>` value, kept as a string like legacy's own `.toString()` on
   * edit (ctrl:251) and the view's raw `value="{{info.branchId}}"` option binding. */
  branchId: string;
  bankCode: string;
  bankName: string;
  bankAddress: string;
}

export function emptyBankForm(): BankForm {
  return {
    bankId: undefined,
    branchId: '',
    bankCode: '',
    bankName: '',
    bankAddress: '',
  };
}

/** POST body for saveBankMaster (ctrl:183-196). Field names -- including
 * `orgnisationId` -- are legacy's own literal spelling, kept verbatim since
 * this is the real backend contract, not a local typo to "fix". */
export interface BankSavePayload {
  branchId: string;
  bankCode: string;
  bankName: string;
  /** Always `''` -- see `BankForm`'s doc comment. Not a rebuild omission;
   * legacy's own `initBankMaster()` never populates it either. */
  branchDesc: string;
  bankAddress: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  status: 'A';
  orgnisationId: number;
  unitId: number;
  updatedDate: string;
}

/** POST body for updateBankMaster (ctrl:284-294) -- no `branchDesc`,
 * `createdBy`, or `status`, matching legacy exactly (only present, or only
 * meaningful, on the create payload). */
export interface BankUpdatePayload {
  bankId: number;
  branchId: string;
  bankCode: string;
  bankName: string;
  bankAddress: string;
  updatedBy: number;
  orgnisationId: number;
  unitId: number;
  updatedDate: string;
}

/** POST body for updateBankMasterStatus (ctrl:330-335). */
export interface BankStatusPayload {
  bankId: number;
  status: ActiveStatus;
  updatedDate: string;
  updatedBy: number;
}
