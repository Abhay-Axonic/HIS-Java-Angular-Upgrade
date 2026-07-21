/**
 * Shapes taken directly from
 * resources/assets/js/controller/billing/unit/gradeCreationMasterController.js
 * -- not guessed.
 */

export type GradeStatus = 'A' | 'I';

/** Row shape returned by getContractGradeMasterList (ctrl:75-85, `commonList`)
 * -- the fields the legacy table actually renders
 * (grade-creation-master.html:93-108). */
export interface GradeRow {
  contractGradeMasterId: number;
  code: string;
  gradeDesc: string;
  status: GradeStatus;
}

/** Shape returned by getContractGradeMasterById (ctrl:210-226), assigned
 * wholesale to `$scope.common` off `listObject[0]`, matching legacy. */
export interface GradeDetail {
  contractGradeMasterId: number;
  code: string;
  gradeDesc: string;
}

/** Editable form state -- mirrors legacy's `$scope.common`
 * (ctrl:40-44, `initMaster()`). */
export interface GradeForm {
  contractGradeMasterId?: number;
  code: string;
  gradeDesc: string;
}

/** Matches ctrl:40-44's `initMaster()` reset shape exactly
 * (`contractGradeMasterId:"", code:"", gradeDesc:""`). */
export function emptyGradeForm(): GradeForm {
  return {
    contractGradeMasterId: undefined,
    code: '',
    gradeDesc: '',
  };
}

/** POST body for saveContractGradeMaster (ctrl:155-165). */
export interface GradeSavePayload {
  code: string;
  gradeDesc: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  status: 'A';
  unitId: number;
  orgId: number;
  updatedDate: string;
}

/** POST body for updateContractGradeMaster (ctrl:241-249) -- no
 * `createdBy`/`status`, matching legacy exactly (only present on the create
 * payload). */
export interface GradeUpdatePayload {
  code: string;
  gradeDesc: string;
  contractGradeMasterId: number;
  orgId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for updateContractGradeMasterStatus (ctrl:287-292,
 * `visitObj` -- legacy's own variable name, kept in code comments only). */
export interface GradeStatusPayload {
  contractGradeMasterId: number;
  status: GradeStatus;
  updatedDate: string;
  updatedBy: number;
}
