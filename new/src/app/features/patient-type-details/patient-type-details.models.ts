export type PatientTypeStatus = 'A' | 'I';

/**
 * Row shape returned by GET /api/billing/getPatientType/{orgId}/{unitId}
 * (legacy patientTypeDetailsController.js:931 — same list endpoint as the
 * patientType screen; the DETAIL screen then loads per-type service rows via
 * getPatientTypeServiceWiseDetails/{id}/{org}/{unit}). Confirmed live
 * 2026-08-12: SUCCESS with 3 real rows.
 */
export interface PatientTypeRow {
  patientTypeId: number;
  patientTypeCode: string;
  patientType: string;
  status: PatientTypeStatus;
}

/**
 * Service-wise detail row returned by
 * GET /api/billing/getPatientTypeServiceWiseDetails/{patientTypeId}/{org}/{unit}
 * (ctrl:1157). The legacy view binds this to the service-mapper grid.
 */
export interface PatientTypeServiceDetail {
  patientTypeDetailsId: number;
  serviceId: number;
  serviceName: string;
  status: PatientTypeStatus;
}

/** POST body for /api/billing/savePatientTypeWiseDetails (ctrl:1104). */
export interface PatientTypeDetailsSavePayload {
  patientTypeId: number;
  patientTypeDetailsDtoList: Array<{
    serviceId: number;
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    unitId: number;
  }>;
}

/** POST body for /api/billing/updatePatientTypeServiceStatus (ctrl:1364). */
export interface PatientTypeServiceStatusPayload {
  patientTypeDetailsId: number;
  status: PatientTypeStatus;
  updatedBy: number;
}
