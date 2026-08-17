/** Walk-in appointment payloads — the corrected backend's SlotMasterDto
 * (the @RequestBody of POST /api/opd/scheduleAppointment), the SAME contract
 * as appointment scheduling. DATE CONVENTION: STRING dates (dd-M-yyyy
 * HH:mm:ss via formatLegacyNow) — the registration/prescription convention,
 * NOT the encounter's Long. */
export interface SlotPayload {
  slotId?: number | null;
  organizationId?: number | null;
  unitId?: number | null;
  specialityId?: number | null;
  doctorId?: number | null;
  appointmentDate?: string | null;
  fromTimeString?: string | null;
  toTimeString?: string | null;
  timePerSlot?: number | null;
  visitTypeId?: number | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
}

export interface IdNameRow {
  id: number;
  name?: string;
  desc?: string;
}

export interface PatientRow {
  patientId?: number;
  uhidNumber?: string;
  firstName?: string;
  middleName?: string;
  lastName?: string;
  [key: string]: unknown;
}
