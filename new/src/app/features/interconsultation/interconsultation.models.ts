/** Interconsultation payloads — field names EXACTLY as the corrected
 * backend's ConsultDoctorDto (the @RequestBody of POST /api/opd/consultDoctor
 * and POST /api/opd/consultDoctorListByEncounter).
 * DATE CONVENTION: the referral's dates (createdDate/updatedDate) are STRING —
 * dd-M-yyyy HH:mm:ss via formatLegacyNow, the registration/prescription
 * convention, NOT the encounter's Long. */
export interface ConsultPayload {
  consultDoctorId?: number | null;
  organizationId?: number | null;
  unitId?: number | null;
  defaultSelfTariffId?: number | null;
  refByDoctorId?: number | null;
  refBySpecialityId?: number | null;
  refByDoctor?: string | null;
  refToDoctorId?: number | null;
  refToDoctor?: string | null;
  refToSpecialityId?: number | null;
  specialityName?: string | null;
  patientId?: number | null;
  patientName?: string | null;
  encounterId?: number | null;
  notes?: string | null;
  status?: string | null;
  createdDate?: string | null;
  createdBy?: number | null;
  updatedDate?: string | null;
  updatedBy?: number | null;
  uhidNumber?: string | null;
  priority?: string | null;
  referredStatus?: string | null;
  admissionId?: number | null;
  visitTypeId?: number | null;
}

/** The referral list row (the DTO's display fields). */
export interface ReferralRow {
  consultDoctorId?: number | null;
  refByDoctor?: string | null;
  refToDoctor?: string | null;
  refToSpecialityId?: number | null;
  specialityName?: string | null;
  patientName?: string | null;
  notes?: string | null;
  priority?: string | null;
  referredStatus?: string | null;
  status?: string | null;
  createdDate?: string | null;
  [key: string]: unknown;
}

export interface IdNameRow {
  id: number;
  name?: string;
  desc?: string;
}
