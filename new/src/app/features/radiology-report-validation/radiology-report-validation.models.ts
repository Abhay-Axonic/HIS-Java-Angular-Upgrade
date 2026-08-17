/** Lab/Radiology worklist row — the corrected backend's list rows (generic
 * key/value; the display fields per screen are patient/order/sample/status).
 * DATE CONVENTION: String dates (dd-M-yyyy HH:mm:ss via formatLegacyNow) —
 * the registration/prescription convention, NOT the encounter's Long. */
export interface LabRow {
  patientId?: number;
  patientName?: string;
  orderId?: number;
  orderDetailId?: number;
  sampleId?: number;
  testId?: number;
  testName?: string;
  sampleType?: string;
  status?: string;
  createdDate?: string;
  [key: string]: unknown;
}

export interface SavePayload {
  patientId?: number | null;
  orderDetailId?: number | null;
  testId?: number | null;
  result?: string | null;
  remark?: string | null;
  status?: string | null;
  unitId?: number | null;
  organizationId?: number | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
  [key: string]: unknown;
}
