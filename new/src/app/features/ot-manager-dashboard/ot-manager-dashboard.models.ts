/** Nursing/OT worklist row — the corrected backend's list rows (generic
 * key/value; the display fields per screen are patient/admission/status).
 * DATE CONVENTION: String dates (dd-M-yyyy HH:mm:ss via formatLegacyNow) —
 * the registration/prescription convention, NOT the encounter's Long. */
export interface NursingRow {
  patientId?: number;
  patientName?: string;
  admissionId?: number;
  admissionNumber?: string;
  bedId?: number;
  bedDesc?: string;
  doctorId?: number;
  doctorName?: string;
  status?: string;
  createdDate?: string;
  [key: string]: unknown;
}

export interface SavePayload {
  patientId?: number | null;
  admissionId?: number | null;
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
