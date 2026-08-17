/** Encounter save payload — field names EXACTLY as the corrected backend's
 * EncounterMasterDto. The dates are LONG epoch-millis: the backend converts
 * them itself (getStringDateFromLongDate), so the frontend sends Date.now(). */
export interface EncounterPayload {
  encounterId?: number | null;
  patientId?: number | null;
  patientTypeId?: number | null;
  contractId?: number | null;
  visitTypeId?: number | null;
  doctorId?: number | null;
  sourceId?: number | null;
  encounterDate?: number | null;
  encounterDateString?: string | null;
  isReferal?: string | null;
  deptId?: number | null;
  subDeptId?: number | null;
  remark?: string | null;
  kinDetailsId?: number | null;
  paymentEntitlementTypeId?: number | null;
  encounterNumber?: string | null;
  status: string;
  unitId: number;
  organizationId: number;
  appointmentId?: number | null;
  isScheduledAppointment?: string | null;
  specialityId?: number | null;
  createdBy: number;
  updatedBy: number;
  createdDate: number;
  updatedDate: number;
  /** the complaints ride the encounter payload (ComplaintAppointmentDetailsDto rows) */
  listComplaintDataSave?: ComplaintRow[] | null;
}

export interface ComplaintRow {
  complaintId?: number | null;
  complaint?: string | null;
  [key: string]: unknown;
}

/** Vitals row — the AppointmentVitalMapperDto. The dates are STRING
 * (dd-M-yyyy HH:mm:ss) — the OPPOSITE of the encounter DTO. */
export interface VitalRow {
  appointmentVitalMapperId?: number | null;
  vitalId: number | null;
  appointmentId?: number | null;
  patientId?: number | null;
  value: string;
  status: string;
  isSelfTriage?: string;
  createdBy: number;
  createdDate?: string | null;
  updatedBy: number;
  updatedDate?: string | null;
  unitId: number;
  organizationId: number;
  encounterId?: number | null;
  isReviewedFlag?: string;
  isAdmissionFlag?: string;
}

export interface IdNameRow {
  id: number;
  name?: string;
  desc?: string;
}
