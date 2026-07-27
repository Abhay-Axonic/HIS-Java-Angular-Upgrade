export type MealMasterStatus = 'A' | 'I';

/** Row shape returned by `getAllMealMasterListPaginationList` (backend
 * query GET_ACTIVE_MEAL_MASTER_LIST -- ACTIVE ONLY, see class doc). */
export interface MealMasterRow {
  mealId: number;
  code: string;
  name: string;
  servingTime: string; // "HH:mm", 24-hour, server-formatted via to_char(...,'HH24:MI')
  status: MealMasterStatus;
}

/** Editable form state -- mirrors `$scope.mealMasterObj` (ctrl:51-64). */
export interface MealMasterForm {
  id?: number;
  code: string;
  name: string;
  servingTime: string;
}

export function emptyMealMasterForm(): MealMasterForm {
  return { id: undefined, code: '', name: '', servingTime: '' };
}

/** POST body for create (ctrl:200-245) / PUT body for update (ctrl:280-319). */
export interface MealMasterSavePayload {
  mealId?: number;
  code: string;
  name: string;
  servingTime: string;
  organizationId: number;
  unitId: number;
  createdBy?: number;
  updatedBy: number;
  status: 'A';
}

/** PUT body for `updateStatus` (ctrl:247-278). */
export interface MealMasterStatusPayload {
  mealId: number;
  status: MealMasterStatus;
  updatedBy: number;
}
