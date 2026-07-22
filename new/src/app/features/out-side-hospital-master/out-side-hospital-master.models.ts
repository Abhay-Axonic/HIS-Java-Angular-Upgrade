export type OutsideHospitalStatus = 'A' | 'I';

/** Row shape returned by getOutsideHospitalMasterList (ctrl:51-92,
 * `GET_OUT_SIDE_HOSPITAL_MASTER_LIST`). */
export interface OutsideHospitalRow {
  outsideHospitalId: number;
  outsideHospitalCode: string;
  outsideHospitalDesc: string;
  status: OutsideHospitalStatus;
}

/** Response shape of getOutsideHospitalMasterById (ctrl:216-244,
 * `GET_OUT_SIDE_HOSPITAL_MASTER_BY_ID`, `response.data.listObject[0]`). */
export interface OutsideHospitalDetail {
  outsideHospitalId: number;
  outsideHospitalCode: string;
  outsideHospitalDesc: string;
}

/** Mirrors `$scope.common` (ctrl:34-40). */
export interface OutsideHospitalForm {
  outsideHospitalId?: number;
  outsideHospitalCode: string;
  outsideHospitalDesc: string;
}

export function emptyOutsideHospitalForm(): OutsideHospitalForm {
  return {
    outsideHospitalId: undefined,
    outsideHospitalCode: '',
    outsideHospitalDesc: '',
  };
}

/** POST body for saveOutsideHospitalMaster (ctrl:143-199). */
export interface OutsideHospitalSavePayload {
  outsideHospitalCode: string;
  outsideHospitalDesc: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateOutsideHospitalMaster (ctrl:247-295) -- no
 * `createdBy`/`status`, matching legacy exactly (only present on create). */
export interface OutsideHospitalUpdatePayload {
  outsideHospitalId: number;
  outsideHospitalCode: string;
  outsideHospitalDesc: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for updateOutsideHospitalMasterStatus (ctrl:297-333). Legacy
 * builds this by mutating `$scope.common` in place (see class doc comment
 * -- a real, disclosed shared-object identity-overwrite risk, same class
 * as entries 94/107, deliberately NOT reproduced here). The rebuild sends
 * an independent, minimal payload instead -- confirmed sufficient because
 * `OutsideHospitalMasterDaoImpl.updateOutsideHospitalMasterStatus` only
 * ever reads `outsideHospitalId`/`status`/`updatedBy`/`updatedDate` off the
 * DTO, ignoring code/desc entirely. */
export interface OutsideHospitalStatusPayload {
  outsideHospitalId: number;
  status: OutsideHospitalStatus;
  updatedBy: number;
  updatedDate: string;
}

// ---- "Doctor Details" sub-feature (per-outside-hospital doctor mapper,
// t_outside_hospital_doctor_mapper -- NOT present in this local dev DB,
// see class doc comment / findings) ----

export interface HospitalDoctorRow {
  outsideHosDocId: number;
  hospDoctorName: string;
  status: OutsideHospitalStatus;
}

/** Mirrors `$scope.obj` (ctrl:342-346). */
export interface HospitalDoctorForm {
  outsideHosDocId?: number;
  hospDoctorName: string;
}

export function emptyHospitalDoctorForm(): HospitalDoctorForm {
  return { outsideHosDocId: undefined, hospDoctorName: '' };
}

/** POST body for saveOutsideHospitalDoctorMapper (ctrl:348-412). */
export interface HospitalDoctorSavePayload {
  outsideHosDocId?: number;
  outsideHospitalId: number;
  hospDoctorName: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateOutsideHospitalDoctorMapperStatus (ctrl:434-475). */
export interface HospitalDoctorStatusPayload {
  outsideHosDocId: number;
  status: OutsideHospitalStatus;
  updatedBy: number;
  updatedDate: string;
}
