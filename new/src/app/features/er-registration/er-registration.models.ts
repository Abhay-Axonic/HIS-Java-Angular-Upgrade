/** ER registration payload — the corrected backend's AdmissionDto
 * (the @RequestBody of POST /adt/saveUnknownPatient) + the unknown-patient
 * record's fields (UnknownPatientRegistrationDto: priorityId, isMedicoLegal,
 * age, accompaniedBy, patientName, tUhid; createdDate/updatedDate STRING).
 * DATE CONVENTION: String dates via formatLegacyNow — the registration/
 * prescription convention, NOT the encounter's Long. */
export interface ErRegistrationPayload {
  organizationId?: number | null;
  unitId?: number | null;
  patientId?: number | null;
  patientName?: string | null;
  uhidNumber?: string | null;
  genderId?: number | null;
  age?: number | null;
  ageFormat?: string | null;
  accompaniedBy?: string | null;
  priorityId?: number | null;
  isMedicoLegal?: string | null;
  encounterId?: number | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
  [key: string]: unknown;
}

export interface IdNameRow { id: number; name?: string; desc?: string; }
export interface PatientRow { patientId?: number; uhidNumber?: string; firstName?: string; lastName?: string; [key: string]: unknown; }
