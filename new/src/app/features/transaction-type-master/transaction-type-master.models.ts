export type TransactionTypeStatus = 'A' | 'I';

/**
 * Row shape returned by `getTransactionTypeMasterList`'s `listObject`
 * (`GET_TRANSACTION_TYPE_LIST` named query, `TransactionTypeMaster.java`) --
 * `transactionTypeId`/`transactionTypeName`/`transactionTypeDesc`/`status`,
 * ordered by `transactionTypeId` DESC server-side. No `unitId` anywhere in
 * this shape -- the query is org-scoped only (see the component's class doc
 * comment).
 */
export interface TransactionTypeRow {
  transactionTypeId: number;
  transactionTypeName: string;
  transactionTypeDesc: string;
  status: TransactionTypeStatus;
}

/**
 * Editable form state -- mirrors `$scope.transactionType`
 * (`transactionTypeMasterController.js`'s `init()`: `$scope.transactionType = {}`,
 * every field starts genuinely undefined until Edit is clicked). Legacy's
 * own form labels BOTH of these "Transaction Type Code"/"Transaction Type
 * Description" in the UI even though the underlying/backend field names are
 * `transactionTypeName`/`transactionTypeDesc` -- a real legacy label choice,
 * not a rebuild typo; the field names below are kept as the real DTO names.
 */
export interface TransactionTypeForm {
  transactionTypeId: number | null;
  transactionTypeName: string;
  transactionTypeDesc: string;
}

export function emptyTransactionTypeForm(): TransactionTypeForm {
  return { transactionTypeId: null, transactionTypeName: '', transactionTypeDesc: '' };
}

/**
 * POST body for `saveTransactionTypeMaster` (ctrl:`saveTransactionType`, the
 * real POST fired only via `PopupOkBtn` after the confirm modal's Ok).
 * Deliberately has NO `unitId` field -- see the component's class doc
 * comment on org-only scoping being a confirmed, entity-wide, deliberate
 * design for this screen, not an omission to fix.
 */
export interface TransactionTypeSavePayload {
  transactionTypeName: string;
  transactionTypeDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  orgId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateTransactionTypeMaster` (ctrl:`updateTransactionType` --
 * no confirm modal concept at all, confirmed by source: this function has
 * zero `popUpFlag`/`showModal` references). */
export interface TransactionTypeUpdatePayload {
  transactionTypeId: number;
  transactionTypeName: string;
  transactionTypeDesc: string;
  updatedBy: number;
  updatedDate: string;
  orgId: number;
}

/** POST body for `updateStatusForTransactionType` (ctrl:`updateStatus`). */
export interface TransactionTypeStatusPayload {
  transactionTypeId: number;
  status: TransactionTypeStatus;
  updatedBy: number;
  updatedDate: string;
  orgId: number;
}
