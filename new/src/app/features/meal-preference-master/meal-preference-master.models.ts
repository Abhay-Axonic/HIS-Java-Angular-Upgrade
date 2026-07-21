export type MealPreferenceStatus = 'A' | 'I';

/** Row shape from getMealPreferenceMasterList (ctrl:77-132, view:130-140). */
export interface MealPreferenceRow {
  mealPreferenceId: number;
  mealPreferenceCode: string;
  mealPreferenceDesc: string;
  status: MealPreferenceStatus;
}

/** Shape returned by getMealPreferenceMasterById's `listObject[0]`
 * (ctrl:256-280) -- only these 3 fields are ever read off it. */
export interface MealPreferenceDetail {
  mealPreferenceId: number;
  mealPreferenceCode: string;
  mealPreferenceDesc: string;
}

/** Editable form state -- mirrors `$scope.mealPrefMasterObj` (ctrl:57). */
export interface MealPreferenceForm {
  mealPreferenceId?: number;
  mealPreferenceCode: string;
  mealPreferenceDesc: string;
}

export function emptyMealPreferenceForm(): MealPreferenceForm {
  return { mealPreferenceId: undefined, mealPreferenceCode: '', mealPreferenceDesc: '' };
}

/** POST body for saveMealPreferenceMaster (ctrl:200-210). */
export interface MealPreferenceSavePayload {
  mealPreferenceCode: string;
  mealPreferenceDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateMealPreferenceMaster (ctrl:330-338) -- no
 * `createdBy`/`status`, matching legacy exactly. */
export interface MealPreferenceUpdatePayload {
  mealPreferenceCode: string;
  mealPreferenceDesc: string;
  mealPreferenceId: number;
  unitId: number;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for updateMealPreferenceMasterStatus (ctrl:291-296). */
export interface MealPreferenceStatusPayload {
  mealPreferenceId: number;
  status: MealPreferenceStatus;
  updatedBy: number;
  updatedDate: string;
}
