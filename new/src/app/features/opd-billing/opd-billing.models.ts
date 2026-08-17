/** OPD Billing payloads — field names EXACTLY as the corrected backend's
 * BillingMasterDto (the @RequestBody of POST /api/billing/saveBill).
 * DATE CONVENTION: Long epoch-millis (billDateTime/createdDate/updatedDate) —
 * the ENCOUNTER DTO's convention, NOT the registration/prescription Strings. */
export interface BillPayload {
  billingMasterId?: number | null;
  encounterId?: number | null;
  visitTypeId?: number | null;
  patientId?: number | null;
  billNumber?: string | null;
  billDateTime?: number | null;
  netAmount?: number | null;
  discountAmount?: number | null;
  grossAmount?: number | null;
  concessionAmount?: number | null;
  gst?: number | null;
  totalBillAmount?: number | null;
  outstanding?: number | null;
  roundOff?: number | null;
  isCreditBill?: string | null;
  unitId: number;
  organisationId: number;
  status: string;
  createdDate?: number | null;
  createdBy?: number | null;
  updatedDate?: number | null;
  updatedBy?: number | null;
  paidAmount?: number | null;
  billStatusId?: number | null;
  billRemark?: string | null;
  billCancelled?: string | null;
}

/** One billed service line (the frontend's working copy). */
export interface BillLine {
  serviceId: number | null;
  serviceName: string;
  quantity: number;
  rate: number;
}

export interface BillResponseTotals {
  netAmount?: number | null;
  grossAmount?: number | null;
  totalBillAmount?: number | null;
  billNumber?: string | null;
  billingMasterId?: number | null;
}
