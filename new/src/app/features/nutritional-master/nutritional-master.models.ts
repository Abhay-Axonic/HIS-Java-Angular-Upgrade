export type NutritionalMasterStatus = 'A' | 'I';

/** Row shape from `getAll`'s `listObject`
 * (`GET_ALL_NUTRITION_MASTER_LIST`): `nutritionId, organizationId, unitId,
 * nutritionCode, nutritionName, status`. */
export interface NutritionalMasterRow {
  nutritionId: number;
  organizationId: number;
  unitId: number;
  nutritionCode: string;
  nutritionName: string;
  status: NutritionalMasterStatus;
}

/** Editable form state -- mirrors `$scope.nutritionalMasterObj`. */
export interface NutritionalMasterForm {
  nutritionId: number | null;
  nutritionCode: string;
  nutritionName: string;
}

export function emptyNutritionalMasterForm(): NutritionalMasterForm {
  return { nutritionId: null, nutritionCode: '', nutritionName: '' };
}

/** POST body for the save endpoint (ctrl:`saveNutrition`, `$scope.nutritionalMasterObj`). */
export interface NutritionalMasterSavePayload {
  nutritionCode: string;
  nutritionName: string;
  organizationId: number;
  unitId: number;
  status: 'A';
  createdBy: number;
  createdDate: string;
  updatedBy: number;
  updatedDate: string;
}

/** PUT body for the update endpoint (ctrl:`updateNutrition`, same
 * `$scope.nutritionalMasterObj`, now carrying `nutritionId`). */
export interface NutritionalMasterUpdatePayload {
  nutritionId: number;
  nutritionCode: string;
  nutritionName: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/**
 * PUT body for the status endpoint (ctrl:`updateStatus`, `data`) --
 * deliberately, faithfully, NOT org/unit-scoped: `organizationId`/`unitId`
 * are commented out in legacy's own payload build
 * (`nutritionalMasterController.js`, `//organizationId : $scope.organizationId,`
 * `//unitId : $scope.unitId`) -- the frontend never attempted to send them.
 * See the component class doc comment for the corresponding, unfixable-here
 * backend gap this pairs with.
 */
export interface NutritionalMasterStatusPayload {
  nutritionId: number;
  status: NutritionalMasterStatus;
  updatedBy: number;
}
