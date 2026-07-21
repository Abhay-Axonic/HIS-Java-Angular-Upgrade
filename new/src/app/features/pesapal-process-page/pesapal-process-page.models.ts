/**
 * Response shape from POST /api/global/getPesapayPaymentStatus -- see
 * pesapalSuccessPageController.js:65-89.
 */
export interface PesapayPaymentStatus {
  transactionStatus: 'COMPLETED' | 'PENDING' | 'FAILED' | 'CONSUMED' | string;
  amount?: number;
  transactionId?: string;
  transactionType?: string;
}

/**
 * sessionStorage `tempAppointmentObjet` -- key name (including the
 * "Objet" typo) matches legacy exactly; shared with
 * selfRegistrationController.js, not free to "correct". Only the 4 fields
 * this screen itself writes are typed here; other fields already on the
 * object are preserved via spread, not modeled.
 */
export interface TempAppointmentObject {
  isFromPesaPalSuccessPage?: string;
  pesapalPaymentStatus?: string;
  pesaPalAmount?: number;
  resTransactionId?: string;
  transactionType?: string;
  [key: string]: unknown;
}
