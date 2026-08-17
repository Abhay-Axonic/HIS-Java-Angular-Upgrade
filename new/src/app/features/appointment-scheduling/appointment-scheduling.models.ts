/** Appointment scheduling payloads — field names EXACTLY as the corrected
 * backend's SlotMasterDto (the @RequestBody of POST /api/opd/scheduleAppointment
 * and POST /api/opd/searchAppointmentsByCriteria).
 * DATE CONVENTION: the appointment fields (appointmentDate/fromDate/toDate/
 * createdDate/updatedDate) are STRING — dd-M-yyyy HH:mm:ss via formatLegacyNow,
 * the SAME as registration and prescription; the slot times ride the
 * fromTimeString/toTimeString String pairs (the Instant pairs are backend-side).
 * NOT the encounter's Long epoch-millis. */
export interface SlotPayload {
  slotId?: number | null;
  previousSlotId?: number | null;
  organizationId?: number | null;
  unitId?: number | null;
  billId?: number | null;
  specialityId?: number | null;
  doctorId?: number | null;
  fromDate?: string | null;
  toDate?: string | null;
  appointmentDate?: string | null;
  appointmentDate1?: string | null;
  appointmentDate2?: string | null;
  dayId?: number | null;
  day?: string | null;
  fromTimeString?: string | null;
  toTimeString?: string | null;
  fromTimeString2?: string | null;
  toTimeString2?: string | null;
  timePerSlot?: number | null;
  slotTypeId?: number | null;
  visitTypeId?: number | null;
  visitTypeName?: string | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
}

export interface SlotRow {
  slotId?: number;
  id?: number;
  fromTimeString?: string;
  toTimeString?: string;
  fromTime?: string;
  toTime?: string;
  isBooked?: boolean | string;
  status?: string;
  [key: string]: unknown;
}

export interface AppointmentRow {
  appointmentId?: number;
  slotId?: number;
  patientName?: string;
  patientId?: number;
  fromTimeString?: string;
  toTimeString?: string;
  appointmentDate?: string;
  doctorName?: string;
  [key: string]: unknown;
}

export interface IdNameRow {
  id: number;
  name?: string;
  desc?: string;
  code?: string;
}

export interface PatientRow {
  patientId?: number;
  id?: number;
  uhidNumber?: string;
  firstName?: string;
  middleName?: string;
  lastName?: string;
  age?: string;
  genderDesc?: string;
  [key: string]: unknown;
}
