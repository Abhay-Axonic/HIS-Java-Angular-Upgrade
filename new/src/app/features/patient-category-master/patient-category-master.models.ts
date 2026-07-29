export type PatientCategoryStatus = 'A' | 'I';
export type YesNo = 'Y' | 'N';

/**
 * Row shape as actually returned by `getPatientCategoryMasterList`
 * (`GET_PATIENT_CATEGORY_MASTER_LIST` named query, `PatientCategoryMaster.java`
 * ctrl:43-48) -- confirmed live 2026-07-29 via a real authenticated session
 * against `http://localhost:8080`. The named query has **no `ORDER BY`
 * clause at all** (unlike its `GET_ACTIVE_PATIENT_CATEGORY_LIST*` siblings,
 * which do) and **no status filter** -- the list legitimately mixes Active
 * and Inactive rows, matching `patient-category-master.html`'s
 * `ng-repeat="info in patientList"` with no `filter`. The response also
 * carries `createdBy`/`createdDate`/`updatedBy`/`updatedDate`/`organizationId`/
 * `unitId`/`offset`/`noOfRecordsPerPage`/`registrationTypeId`/
 * `registrationTypeName` fields, always `null`/`0` in this projection and
 * never read by the view -- omitted here as dead payload.
 */
export interface PatientCategoryRow {
  patientCategoryId: number;
  patientCategory: string;
  isForeginerCategory: YesNo;
  status: PatientCategoryStatus;
  patientCategoryCode: string;
}

/**
 * Shape returned by `getPatientCategoryMasterById/{id}/{orgId}`
 * (`GET_PATIENT_CATEGORY_MASTER_BY_ID`, ctrl:37-42) -- a GET with path
 * variables, not a POST body, unlike every other endpoint on this screen.
 * Confirmed live: `listObject[0]` carries `status` too, but
 * `showUpdateBtn` (ctrl:251-277) never reads it -- only
 * `patientCategoryId`/`patientCategoryCode`/`patientCategory`/
 * `isForeginerCategory` are assigned back onto the form.
 */
export interface PatientCategoryDetail {
  patientCategoryId: number;
  patientCategory: string;
  isForeginerCategory: YesNo;
  status: PatientCategoryStatus;
  patientCategoryCode: string;
}

/** Form model -- mirrors `$scope.patientCategory` (ctrl:49-51/268-271). */
export interface PatientCategoryForm {
  code: string;
  desc: string;
  isForeigner: boolean;
}

export function emptyPatientCategoryForm(): PatientCategoryForm {
  return { code: '', desc: '', isForeigner: false };
}

/** POST body for savePatientCategoryMaster (ctrl:193-204). */
export interface PatientCategorySavePayload {
  isForeginerCategory: YesNo;
  patientCategory: string;
  patientCategoryCode: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/**
 * POST body for updatePatientCategoryMaster (ctrl:325-334). Deliberately
 * has NO `status` field at all -- a confirmed divergence from this
 * codebase's `maritalStatusMaster`/`ambulanceTypeMaster` sibling screens,
 * whose update payloads DO hardcode `status:'A'`. Verified against the
 * real controller source, not assumed from sibling shape.
 */
export interface PatientCategoryUpdatePayload {
  isForeginerCategory: YesNo;
  patientCategory: string;
  patientCategoryCode: string;
  patientCategoryId: number;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/**
 * POST body for updatePatientCategoryMasterStatus (ctrl:287-292).
 * Deliberately has NO `organizationId` field -- confirmed live: the
 * captured request body was exactly
 * `{patientCategoryId, status, updatedBy, updatedDate}`.
 */
export interface PatientCategoryStatusPayload {
  patientCategoryId: number;
  status: PatientCategoryStatus;
  updatedBy: number;
  updatedDate: string;
}
