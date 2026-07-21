/**
 * Field set taken directly from
 * views/master/GlobalMaster/employee-category-master.html +
 * resources/assets/js/controller/master/global/common/employeeCategoryMasterController.js
 * (`employeeCategoryMasterController`, no `controllerAs`). Only two editable
 * fields on this screen: category code + description. ORG-scoped only --
 * legacy never sends `unitId` in any payload here (unlike several sibling
 * master screens in this batch), so it is intentionally not modeled.
 */
export interface EmployeeCategoryRow {
  employeeCategoryId: number;
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
  status: 'A' | 'I';
}

/** Shape of GETEMPLOYEECATEGORYMASTERBYID's `listObject[0]` (ctrl:210-238) --
 * only the fields `showUpdateBtn` actually reads back into the form. */
export interface EmployeeCategoryDetail {
  employeeCategoryId: number;
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
}

/** Mirrors `save()`'s `data` object (ctrl:157-168). */
export interface EmployeeCategorySavePayload {
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
  organizationId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `update()`'s `data` object (ctrl:249-257) -- note no `status`
 * field is sent on update; status only changes via the separate
 * `updateStatus` endpoint. */
export interface EmployeeCategoryUpdatePayload {
  employeeCategoryId: number;
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** Minimal payload for the status toggle. Legacy's `updateStatus` (ctrl:290-326)
 * POSTs the ENTIRE `$scope.common` edit-form object (a cross-cutting
 * anti-pattern also found in counterMaster/cabinMaster) -- rebuilt here as a
 * clean minimal payload instead. Low-severity cleanup only: the server-visible
 * effect (status flip) and the client-visible effect (form reset + list
 * reload, see class comment) are both preserved exactly. */
export interface EmployeeCategoryStatusPayload {
  employeeCategoryId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.common`). `employeeCategoryId` is
 * only meaningfully present while editing an existing row. */
export interface EmployeeCategoryForm {
  employeeCategoryId?: number;
  employeeCategoryCode: string;
  employeeCategoryDescription: string;
}

export function emptyEmployeeCategoryForm(): EmployeeCategoryForm {
  return {
    employeeCategoryCode: '',
    employeeCategoryDescription: '',
  };
}
