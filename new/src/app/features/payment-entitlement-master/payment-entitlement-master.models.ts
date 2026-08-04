export type PaymentEntitlementStatus = 'A' | 'I';

/** Row shape returned by `GET_PAYMENT_ENTITLEMENT_LIST` (legacy named
 * query, aliasToBean): generic `id`/`desc`/`code`/`status`. */
export interface PaymentEntitlementRow {
  id: number;
  code: string;
  desc: string;
  status: PaymentEntitlementStatus;
}

/** Form model matching `$scope.common` in
 * paymentEntitmentMasterController.js. */
export interface PaymentEntitlementForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyPaymentEntitlementForm(): PaymentEntitlementForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for updatePaymententitlementStatus (ctrl `visitObj`). */
export interface PaymentEntitlementStatusPayload {
  paymentEntitlementId: number;
  status: PaymentEntitlementStatus;
  updatedBy: number;
  updateDate: string;
}
