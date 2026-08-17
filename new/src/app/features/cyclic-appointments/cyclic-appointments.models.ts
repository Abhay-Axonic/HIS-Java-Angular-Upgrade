/** Cyclic appointments list — read-only grid via the corrected backend's
 * searchAppointmentsByCriteria (SlotMasterDto criteria). DATE CONVENTION:
 * STRING dates (dd-M-yyyy HH:mm:ss) — the registration/prescription
 * convention, NOT the encounter's Long. */
export interface AppointmentRow {
  appointmentId?: number;
  slotId?: number;
  patientId?: number;
  patientName?: string;
  doctorId?: number;
  doctorName?: string;
  specialityName?: string;
  appointmentDate?: string;
  fromTimeString?: string;
  toTimeString?: string;
  status?: string;
  [key: string]: unknown;
}

export interface SlotCriteria {
  organizationId?: number | null;
  unitId?: number | null;
  specialityId?: number | null;
  doctorId?: number | null;
  appointmentDate?: string | null;
  fromDate?: string | null;
  toDate?: string | null;
}
