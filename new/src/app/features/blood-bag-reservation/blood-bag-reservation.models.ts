/** Blood Bank worklist row — the corrected backend's list rows (generic
 * key/value; the display fields per screen are donor/patient/bag/status).
 * DATE CONVENTION: String dates (dd-M-yyyy HH:mm:ss via formatLegacyNow) —
 * the registration/prescription convention, NOT the encounter's Long. */
export interface BloodRow {
  donorId?: number;
  donorName?: string;
  patientId?: number;
  patientName?: string;
  bagId?: number;
  bagNumber?: string;
  bloodGroup?: string;
  component?: string;
  status?: string;
  createdDate?: string;
  [key: string]: unknown;
}

export interface SavePayload {
  donorId?: number | null;
  patientId?: number | null;
  bagId?: number | null;
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
