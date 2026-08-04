export type PaymentModeStatus = 'A' | 'I';

/** Row shape returned by `GET_PAYMENT_MODE_MASTER_LIST` (legacy named
 * query, aliasToBean into PaymentEntitlementMasterDto): generic
 * `id`/`desc`/`code`/`status`. */
export interface PaymentModeRow {
  id: number;
  code: string;
  desc: string;
  status: PaymentModeStatus;
}

/** Form model matching `$scope.common` in paymentModeMasterController.js. */
export interface PaymentModeForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyPaymentModeForm(): PaymentModeForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for updatePaymentModeMasterStatus (ctrl `visitObj`). */
export interface PaymentModeStatusPayload {
  paymentEntitlementId: number;
  status: PaymentModeStatus;
  updateDate: string;
  updatedBy: number;
}
