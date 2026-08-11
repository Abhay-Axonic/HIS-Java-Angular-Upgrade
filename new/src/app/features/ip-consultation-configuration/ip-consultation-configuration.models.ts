export interface IpConsultationConfigRow {
  doctorIPConsultationServiceMapperId: number;
  specialityName: string;
  doctorName: string;
  firstConsultationServiceName: string;
  routineConsultationServiceName: string;
  noOfRoutineServices: number;
  followupRoutineServiceName: string;
  icuPrimaryConsultationServiceName: string;
}

/**
 * POST body for /api/global/getDoctorIPConsultationServiceMapperList
 * (legacy IPConsultationConfigurationMasterController.js:93-98 — the exact
 * object passed to promiseFactoryWithObject for the list call).
 */
export interface IpConsultationConfigListPayload {
  organizationId: number;
  unitId: number;
  offset: number;
  noOfRecordsPerPage: number;
}
