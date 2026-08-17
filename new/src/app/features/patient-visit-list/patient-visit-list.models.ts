/** Patient visit list — the appointment queue that feeds the encounter
 * screen. Rows via the corrected backend's searchAppointmentsByCriteria
 * (SlotMasterDto criteria). DATE CONVENTION: STRING dates (dd-M-yyyy
 * HH:mm:ss) — the registration/prescription convention, NOT the encounter's
 * Long. */
export interface VisitRow {
  appointmentId?: number;
  slotId?: number;
  patientId?: number;
  patientName?: string;
  uhidNumber?: string;
  encounterId?: number;
  doctorId?: number;
  doctorName?: string;
  specialityId?: number;
  specialityName?: string;
  appointmentDate?: string;
  fromTimeString?: string;
  toTimeString?: string;
  status?: string;
  [key: string]: unknown;
}

export interface VisitCriteria {
  organizationId?: number | null;
  unitId?: number | null;
  doctorId?: number | null;
  appointmentDate?: string | null;
  fromDate?: string | null;
  toDate?: string | null;
}

export interface IdNameRow {
  id: number;
  name?: string;
  desc?: string;
}
