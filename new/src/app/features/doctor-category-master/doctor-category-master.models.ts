/**
 * Field names/types read directly from `DoctorCategoryMasterDto`
 * (legacy/Global/src/main/java/com/param/global/billing/dto/DoctorCategoryMasterDto.java)
 * and the legacy view's `ng-model`s (doctor-category-master.html), not
 * guessed.
 */
export interface DoctorCategoryRow {
  doctorCategoryId: number;
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
  status: 'A' | 'I';
}

export interface DoctorCategoryDetail {
  doctorCategoryId: number;
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
}

export interface DoctorCategorySavePayload {
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
  organizationId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

export interface DoctorCategoryUpdatePayload {
  doctorCategoryId: number;
  doctorCategoryCode: string;
  doctorCategoryDescription: string;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

export interface DoctorCategoryStatusPayload {
  doctorCategoryId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
