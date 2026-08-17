/** ER worklist row — the corrected backend's list rows (generic key/value;
 * the display fields per screen are patient/admission/status/priority).
 * DATE CONVENTION: String dates (dd-M-yyyy HH:mm:ss via formatLegacyNow) —
 * the registration/prescription convention, NOT the encounter's Long. */
export interface ErRow {
  patientId?: number;
  patientName?: string;
  admissionId?: number;
  admissionNumber?: string;
  uhidNumber?: string;
  doctorId?: number;
  doctorName?: string;
  bedId?: number;
  bedDesc?: string;
  zone?: string;
  priority?: string;
  status?: string;
  createdDate?: string;
  [key: string]: unknown;
}
