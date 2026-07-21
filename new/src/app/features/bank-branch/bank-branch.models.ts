export type BankBranchStatus = 'A' | 'I';

/** Row shape returned by getBankBranchMasterList (ctrl:78-134,
 * `$scope.bankBranchList`, view:128-137). */
export interface BankBranchRow {
  branchId: number;
  branchCode: string;
  branchDesc: string;
  status: BankBranchStatus;
}

/** Shape returned by getBankBranchMasterById (ctrl:266-291) -- assigned
 * field-by-field to `$scope.bankBranchMasterObj` (ctrl:283-285), not
 * wholesale. */
export interface BankBranchDetail {
  branchId: number;
  branchCode: string;
  branchDesc: string;
}

/**
 * Editable form state -- mirrors legacy's `$scope.bankBranchMasterObj`
 * (ctrl:51-58). Legacy's own object literal also seeds `bankId` and
 * `branchAddress`, but NEITHER is ever bound to any input in the view
 * (bank-Branch.html only has `branchCode`/`branchDesc` inputs, view:16-38)
 * NOR ever read back out in `saveBankBranch()`/`updateBankBranch()`'s
 * request bodies (ctrl:212-222/340-347) -- unlike bank-master's
 * `branchDesc`, which IS a dead field but still gets sent to the backend.
 * Here `bankId`/`branchAddress` are pure inert copy-paste leftovers with no
 * observable behavior anywhere, so they are deliberately NOT modeled here.
 */
export interface BankBranchForm {
  branchId?: number;
  branchCode: string;
  branchDesc: string;
}

export function emptyBankBranchForm(): BankBranchForm {
  return {
    branchId: undefined,
    branchCode: '',
    branchDesc: '',
  };
}

/** POST body for saveBankBranchMaster (ctrl:212-222). Field names --
 * including `orgnisationId` -- are legacy's own literal spelling, kept
 * verbatim since this is the real backend contract (unlike
 * employeeCategoryController.js's sibling screen, THIS controller uses
 * `orgnisationId` consistently everywhere, so there is no undefined-org-id
 * bug here -- see class doc comment for the sibling-divergence note). */
export interface BankBranchSavePayload {
  branchDesc: string;
  branchCode: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  orgnisationId: number;
  unitId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateBankBranchMaster (ctrl:340-347) -- no `unitId` or
 * `status`, matching legacy exactly. */
export interface BankBranchUpdatePayload {
  branchDesc: string;
  branchCode: string;
  branchId: number;
  orgnisationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for updateBankBranchMasterStatus (ctrl:301-306) -- no org/unit
 * id at all, matching legacy exactly. */
export interface BankBranchStatusPayload {
  branchId: number;
  status: BankBranchStatus;
  updatedBy: number;
  updatedDate: string;
}
