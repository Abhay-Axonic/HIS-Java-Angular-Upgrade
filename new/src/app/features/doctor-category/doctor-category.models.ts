/**
 * Field set taken directly from views/master/GlobalMaster/doctor-category.html
 * + resources/assets/js/controller/master/global/billing/doctorCategoryController.js
 * (`doctorCategoryController`, no `controllerAs`). Only two fields on this
 * screen: doctor category code + description.
 */
export interface DoctorCategoryRow {
  doctorCategoryId: number;
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
  status: 'A' | 'I';
}

/** Shape of GETDOCTORCATEGORYMASTERBYID's `listObject[0]` -- only the
 * fields `showUpdateBtn` (ctrl:259-285) actually reads back into the form. */
export interface DoctorCategoryDetail {
  doctorCategoryId: number;
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
}

/** Mirrors `saveDoctorCategory`'s `data` object (ctrl:204-213). */
export interface DoctorCategorySavePayload {
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `updateDoctorCategory`'s `data` object (ctrl:333-340) -- note no
 * `status` field is sent on update; status only changes via the separate
 * `updateStatus` endpoint. */
export interface DoctorCategoryUpdatePayload {
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
  doctorCategoryId: number;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** Mirrors `updateStatus`'s `data` object (ctrl:295-300). */
export interface DoctorCategoryStatusPayload {
  doctorCategoryId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.doctorCategory`). `doctorCategoryId`
 * is only present while editing an existing row. */
export interface DoctorCategoryForm {
  doctorCategoryId?: number;
  docCategoryCode: string;
  docCategoryDesc: string;
}

export function emptyDoctorCategoryForm(): DoctorCategoryForm {
  return {
    docCategoryCode: '',
    docCategoryDesc: '',
  };
}
