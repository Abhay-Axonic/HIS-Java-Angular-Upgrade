export type YN = 'Y' | 'N';
export type ActiveStatus = 'A' | 'I';

/** Parent-department dropdown option (ctrl:59-75, GET
 * /adt/getSpecialityListForSubSpeciality/{organizationId}). */
export interface DepartmentOption {
  specialityId: number;
  specialityName: string;
}

/** General-ledger typeahead suggestion (ctrl:495-512,
 * GETGENERALLEDGERMASTERACTIVELIST, `/org/getGeneralLedgerMasterActiveList/{orgId}`). */
export interface GeneralLedgerOption {
  generalLedgerId: number;
  generalLedgerName: string;
}

/** Row shape returned by both GETSUBSPECIALITYLIST (view:218-234) and the
 * universal-search endpoint (ctrl:517-543) -- identical shape from either
 * source. */
export interface SubSpecialityRow {
  subSpecialityMasterId: number;
  specialityName: string;
  subSpecialityMasterCode: string;
  subSpecialityMasterName: string;
  generalLedgerName: string;
  isModality?: YN;
  isMriModality?: YN;
  isCTModality?: YN;
  status: ActiveStatus;
}

/** Shape returned by GETSINGLESUBSPECIALITY (ctrl:352-387) -- assigned
 * field-by-field into `$scope.subSpeciality`, not wholesale like some
 * sibling masters' edit-populate. */
export interface SubSpecialityDetail {
  subSpecialityMasterId: number;
  specialityId: number;
  subSpecialityMasterName: string;
  subSpecialityMasterCode: string;
  generalLedgerId: number;
  generalLedgerName: string;
  isModality?: YN;
  isMriModality?: YN;
  isCTModality?: YN;
  subHeaderName?: string;
}

/** Editable form state -- mirrors `$scope.subSpeciality` (ctrl:47-49, an
 * empty object literal: every field starts genuinely `undefined`, not
 * defaulted -- reproduced via `emptySubSpecialityForm()` below rather than
 * pre-seeding e.g. the modality flags to `'N'`). */
export interface SubSpecialityForm {
  subSpecialityId?: number;
  /** `<select>` value, kept as a string like legacy's own `.toString()` on
   * edit (ctrl:372) and the view's raw `value="{{info.specialityId}}"`
   * option binding. */
  specialityId: string;
  subSpecialityCode: string;
  subSpecialityName: string;
  generalLedgerId?: number;
  generalLedgerName: string;
  subHeaderName: string;
  isModality?: YN;
  isMriModality?: YN;
  isCTModality?: YN;
}

export function emptySubSpecialityForm(): SubSpecialityForm {
  return {
    subSpecialityId: undefined,
    specialityId: '',
    subSpecialityCode: '',
    subSpecialityName: '',
    generalLedgerId: undefined,
    generalLedgerName: '',
    subHeaderName: '',
    isModality: undefined,
    isMriModality: undefined,
    isCTModality: undefined,
  };
}

/** POST body for saveSubSpecialtiyMaster (ctrl:286-301, legacy's own typo
 * in the endpoint NAME -- "Specialtiy" -- kept only in the service's URL
 * string, not reproduced in this field-name interface). */
export interface SubSpecialitySavePayload {
  specialityId: string;
  subSpecialityMasterCode: string;
  subSpecialityMasterName: string;
  generalLedgerId?: number;
  status: 'A';
  isModality?: YN;
  isMriModality?: YN;
  isCTModality?: YN;
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  subHeaderName: string;
}

/** POST body for updateSubSpecialityMaster (ctrl:442-455) -- no `status`
 * field, matching legacy exactly (only present on the create payload). */
export interface SubSpecialityUpdatePayload {
  subSpecialityMasterId: number;
  specialityId: string;
  subSpecialityMasterCode: string;
  subSpecialityMasterName: string;
  organizationId: number;
  isModality?: YN;
  isCTModality?: YN;
  isMriModality?: YN;
  generalLedgerId?: number;
  updatedBy: number;
  updatedDate: string;
  subHeaderName: string;
}

/** POST body for updateStatusForSubSpeciality (ctrl:398-403). */
export interface SubSpecialityStatusPayload {
  subSpecialityMasterId: number;
  status: ActiveStatus;
  updatedBy: number;
  updatedDate: string;
}
