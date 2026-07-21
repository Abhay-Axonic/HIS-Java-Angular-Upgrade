/**
 * Field set taken directly from
 * views/master/GlobalMaster/designation-master.html +
 * resources/assets/js/controller/master/global/common/designationMasterController.js
 * (`designationMasterController`, no `controllerAs`). Only two editable
 * fields on this screen: designation code + description. Org-level only --
 * no unitId anywhere in this controller.
 */
export interface DesignationMasterRow {
  employeeDesignationId: number;
  employeeDesignationCode: string;
  employeeDesignationDescription: string;
  status: 'A' | 'I';
}

/** Shape of GETDESIGNATIONMASTERBYID's `listObject[0]` -- only the fields
 * `showUpdateBtn` (ctrl:254-279) actually reads back into the form. */
export interface DesignationMasterDetail {
  employeeDesignationId: number;
  employeeDesignationCode: string;
  employeeDesignationDescription: string;
}

/** Mirrors `saveDesignation`'s `data` object (ctrl:200-209). */
export interface DesignationMasterSavePayload {
  employeeDesignationCode: string;
  employeeDesignationDescription: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `updateDesignation`'s `data` object (ctrl:327-334) -- note no
 * `status` field is sent on update; status only changes via the separate
 * `updateStatus` endpoint. */
export interface DesignationMasterUpdatePayload {
  employeeDesignationCode: string;
  employeeDesignationDescription: string;
  employeeDesignationId: number;
  updatedBy: number;
  organizationId: number;
  updatedDate: string;
}

/** Mirrors `updateStatus`'s `data` object (ctrl:289-294) -- deliberately no
 * `organizationId` here, matching legacy exactly: this is the one save-family
 * call in this controller that does NOT carry the org-scoping anti-pattern
 * seen elsewhere in this codebase (already a clean, minimal payload). */
export interface DesignationMasterStatusPayload {
  employeeDesignationId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.designationMasterObj`).
 * `employeeDesignationId` is only present while editing an existing row. */
export interface DesignationMasterForm {
  employeeDesignationId?: number;
  designationCode: string;
  designationDesc: string;
}

export function emptyDesignationMasterForm(): DesignationMasterForm {
  return {
    designationCode: '',
    designationDesc: '',
  };
}
