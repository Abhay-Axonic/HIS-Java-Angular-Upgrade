/**
 * Rebuild of legacy `advisedVisits` (OPD) -- see
 * advisedVisitsController.js (346 lines) / advised-visits.html (1261
 * lines, of which only lines 1-156 are real -- see class-doc in
 * advised-visits.ts and the findings-doc entry for the dropped dead
 * markup). Field names match the real backend query verbatim
 * (AdvisedDateAppointmentDaoImpl.getAdvisedDateAppointmentList's native
 * SQL column aliases) -- confirmed no diagnosis/medication/treatment
 * field exists anywhere in this query; `doctorId`/`doctorName` are pure
 * filter/display fields.
 */
export interface AdvisedVisitRow {
  advisedDateAppoId?: number;
  patientId?: number;
  encounterId?: number;
  status?: string;
  doctorId?: number;
  advisedDate?: string;
  patientName?: string;
  doctorName?: string;
  encounterDate?: string;
  mobileNumber?: string;
}

/** DoctorMasterController's autofill-search result shape. */
export interface DoctorAutoFillOption {
  doctorId: number;
  firstName?: string;
  middleName?: string;
  lastName?: string;
  [key: string]: unknown;
}
