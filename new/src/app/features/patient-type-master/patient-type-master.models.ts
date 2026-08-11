export type PatientTypeStatus = 'A' | 'I';

/**
 * Row shape returned by GET /api/billing/getPatientType/{orgId}/{unitId}
 * (legacy patientTypeController.js:304, patientTypeDetailsController.js:931).
 * Confirmed live 2026-08-12: SUCCESS with 3 real rows ("List Size DB: 3"),
 * THIRD envelope shape (stores/listObject2/resObject fields, ApiResponse
 * generic). Row fields below are the projection the legacy view reads.
 */
export interface PatientTypeRow {
  patientTypeId: number;
  patientTypeCode: string;
  patientType: string;
  status: PatientTypeStatus;
}

/** Form model — mirrors $scope.patientTypeObj (code + description). */
export interface PatientTypeForm {
  code: string;
  desc: string;
}

export function emptyPatientTypeForm(): PatientTypeForm {
  return { code: '', desc: '' };
}

/** POST body for /api/billing/savePatientType (ctrl:271). */
export interface PatientTypeSavePayload {
  patientTypeCode: string;
  patientType: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
}

/** POST body for /api/billing/updatePatientType (ctrl:396). */
export interface PatientTypeUpdatePayload {
  patientTypeId: number;
  patientTypeCode: string;
  patientType: string;
  updatedBy: number;
  organizationId: number;
}

/** POST body for /api/billing/updatePatientTypeStatus (ctrl:483). */
export interface PatientTypeStatusPayload {
  patientTypeId: number;
  status: PatientTypeStatus;
  updatedBy: number;
}
