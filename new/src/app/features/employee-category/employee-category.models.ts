export type EmployeeCategoryStatus = 'A' | 'I';

/** Row shape returned by getEmployeeCategoryMasterList (ctrl:70-119,
 * `$scope.employeeList`, view:129-136). */
export interface EmployeeCategoryRow {
  employeeCategoryId: number;
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
  status: EmployeeCategoryStatus;
}

/** Shape returned by getEmployeeCategoryMasterById (ctrl:240-264) --
 * assigned field-by-field to `$scope.employeeCategory` (not wholesale). */
export interface EmployeeCategoryDetail {
  employeeCategoryId: number;
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
}

/** Editable form state -- mirrors legacy's `$scope.employeeCategory`
 * (ctrl:49, field names `employeeCode`/`employeeDesc` kept verbatim --
 * legacy's own model uses different names from the row/payload's
 * `employeeCategoryCode`/`employeeCategoryDescription`). */
export interface EmployeeCategoryForm {
  employeeCategoryId?: number;
  employeeCode: string;
  employeeDesc: string;
}

export function emptyEmployeeCategoryForm(): EmployeeCategoryForm {
  return {
    employeeCategoryId: undefined,
    employeeCode: '',
    employeeDesc: '',
  };
}

/**
 * POST body for saveEmployeeCategoryMaster (ctrl:187-196).
 *
 * `organizationId` is typed as possibly `undefined` ON PURPOSE -- this is a
 * real, confirmed bug in legacy, faithfully reproduced, not a rebuild
 * mistake. `employeeCategoryController.js`'s `init()` (ctrl:29-60) only ever
 * assigns the org id to `$scope.orgnisationId` (its own typo'd spelling,
 * ctrl:38); `$scope.organizationId` (no typo) is never assigned ANYWHERE in
 * this controller. Every call site that builds a request body or URL using
 * `$scope.organizationId` -- `saveEmployee` (ctrl:193), `updateEmployee`
 * (ctrl:319), `showUpdateBtn`'s URI (ctrl:249), `getEmpCategoryMasterList`
 * (ctrl:130), and the post-save/post-update list reloads (ctrl:229,349) --
 * therefore always sends `undefined` for the org id. The ONE exception is
 * the very first page-load call (ctrl:168-170), which correctly passes
 * `$scope.orgnisationId`. See employee-category.ts's `organizationId` vs.
 * `brokenOrganizationId` fields for how this is reproduced.
 */
export interface EmployeeCategorySavePayload {
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number | undefined;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateEmployeeCategoryMaster (ctrl:314-321) -- same
 * always-`undefined`-organizationId bug as the save payload, see its doc
 * comment. No `createdBy`/`status`, matching legacy exactly. */
export interface EmployeeCategoryUpdatePayload {
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
  employeeCategoryId: number;
  organizationId: number | undefined;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for updateEmployeeCategoryMasterStatus (ctrl:274-279) -- this
 * one call is NOT affected by the org-id bug (legacy never includes
 * organizationId here at all). */
export interface EmployeeCategoryStatusPayload {
  employeeCategoryId: number;
  status: EmployeeCategoryStatus;
  updatedBy: number;
  updatedDate: string;
}
