/**
 * Parity rebuild of
 * views/master/GlobalMaster/DietNutrition/dish-master.html +
 * resources/assets/js/controller/dietAndKitchen/global/dishMasterController.js
 * (Diet & Nutrition module). Field names/shapes read directly from
 * `legacy/DietAndKitchen/src/main/java/com/param/global/master/dto/`
 * (DishMasterDto, DishMasterDietPrefMapperDto, KeyIngredientsMasterDto,
 * MealMasterDto, DietTypeMasterDto) plus the ADT module's
 * `MealPreferenceMasterDto` and the `GET_DISH_MASTER_LIST` /
 * `GET_DIET_PREF_DETAILS_BY_DISH_ID` / `GET_MEAL_PREF_DETAILS_BY_DISH_ID` /
 * `GET_DIET_TYPE_DETAILS_BY_DISH_ID` named queries (DishMaster.java,
 * DishMasterDietPrefMapper.java) -- not guessed. See dish-master.ts's class
 * doc for the full audit trail.
 */

/** `/api/kitchen/master/keyIngredientsMaster/getUnitMasterDetails` response row. */
export interface UomOption {
  uomId: number;
  uomDescription: string;
}

/** `/api/kitchen/master/keyIngredientsMaster/getActive` response row. */
export interface KeyIngredientOption {
  keyIngredientId: number;
  ingredientDescription: string;
}

/**
 * `/api/adt/unit/getActiveMealPreferenceMasterList` response row (ctrl:186-206).
 * NOTE: `GETACTIVEMEALPREFERENCELIST` is declared TWICE in legacy's own
 * config scripts -- once in adt.url.config.js (`/adt/getMealPreference`)
 * and again in diet.url.config.js (`/api/adt/unit/getActiveMealPreferenceMasterList`).
 * diet.url.config.js loads AFTER adt.url.config.js in index.html, so its
 * `var` redeclaration wins at runtime (confirmed by golden-trace capture,
 * which shows the POST actually firing against the diet.url.config.js
 * path) -- the ADT-module endpoint is the one this screen really calls.
 */
export interface MealPreferenceOption {
  mealPreferenceId: number;
  mealPreferenceCode?: string;
  mealPreferenceDesc: string;
}

/** `/api/kitchen/master/mealMaster/getActive` response row ($scope.getMeal, ctrl:210-227). */
export interface MealTypeOption {
  mealId: number;
  name: string;
}

/** `/api/kitchen/master/dietTypeMaster/getActive` response row ($scope.getDietType, ctrl:230-246). */
export interface DietTypeOption {
  dietTypeId: number;
  dietTypeDescription: string;
}

/**
 * Per-ingredient sub-mapping row -- `$scope.keyIngredientObjList`
 * (ctrl:111-117, 368-397). Saved as a `DishMasterMapper` row per entry
 * (dish x key-ingredient x value x uom), NOT a single aggregate field.
 * `value` is genuinely `Integer` server-side (`KeyIngredientsMasterDto.value`),
 * not `Double` -- a real legacy type narrowing, ported faithfully (not
 * something this rebuild silently widens).
 */
export interface DishKeyIngredientRow {
  keyIngredientId: number | '';
  value: number | '';
  uomId: number | '';
  dishMasterKeyIngredientsMapperId: number | '';
}

export function emptyIngredientRow(): DishKeyIngredientRow {
  return { keyIngredientId: '', value: '', uomId: '', dishMasterKeyIngredientsMapperId: '' };
}

/** Mirrors legacy's `$scope.dummyDishMasterObj`/`$scope.dishMasterObj` (ctrl:94-108). */
export interface DishMasterRecord {
  dishId: number | '';
  dishCode: string;
  dishDescription: string;
  dishValue: number | '';
  uomId: number | '';
}

export function emptyDish(): DishMasterRecord {
  return { dishId: '', dishCode: '', dishDescription: '', dishValue: '', uomId: '' };
}

/** Embedded per-dish mapper rows, from the 3 dependent named queries the DAO joins in per row. */
export interface DietPreferenceMapperRow {
  dietPreferenceId: number;
  dietPreferenceDesc: string;
}

/**
 * NOTE the field name: `GET_MEAL_PREF_DETAILS_BY_DISH_ID` (the query behind
 * a dish row's `dietMealTypeList`, i.e. its MEAL TYPE selections) aliases
 * its label column as `mealPreferenceDesc`, not `mealTypeDesc`/`name` --
 * confirmed directly in DishMasterDietPrefMapper.java's named-query
 * source. The view (dish-master.html:349) reads exactly this field for the
 * "Meal Type" list column. Reproduced with the same (confusing but real)
 * field name rather than silently renaming it.
 */
export interface MealTypeMapperRow {
  mealTypeId: number;
  mealPreferenceDesc: string;
}

export interface DietTypeMapperRow {
  dietTypeId: number;
  dietTypeDescription: string;
}

export interface DishIngredientRow {
  keyIngredientId: number;
  ingredientDescription?: string;
  value: number;
  uomId?: number;
  uomName?: string;
  dishMasterKeyIngredientsMapperId?: number;
}

/**
 * List row shape returned by both the plain GET (list) and
 * `getDishListByDietPrefId` -- `GET_DISH_MASTER_LIST` (DishMaster.java:28-34)
 * plus 3 dependent-mapper fetches the DAO makes per row
 * (DishMasterDaoImpl.getDishMasterList, dao:139-177) and a 4th
 * (ingredientsList) added by the service layer
 * (DishMasterServiceImpl.getDishMasterList, service:52-72). This is also
 * the ONLY source used to populate the edit form (`editDishMaster`,
 * ctrl:405-524 reads straight off the already-fetched row) -- there is no
 * separate "get dish by id" endpoint.
 *
 * `uomName` is aliased from `unitMaster.uomDescription` in the named query
 * (DishMaster.java:30, "unitMaster.uomDescription as uomName") -- despite
 * the JSON key, this screen's OWN list/UOM-dropdown both consistently use
 * the `uomDescription` *column value*, just carried under the `uomName`
 * JSON key on this particular response. A different, internally-used query
 * path (`getNutritionalValues`) aliases a different column
 * (`unitMaster.uomName`) under the same `uomName` key -- a real,
 * pre-existing field-mapping inconsistency between the two query paths
 * that does not manifest as a bug on THIS screen's own UI. Not unified
 * here -- see dish-master.ts's class doc.
 *
 * Optional fields below reflect a real narrower response shape: when
 * `getDishListByDietPrefId` is called with an EMPTY selection, the DAO
 * falls back to `GET_DISH_ACTIVE_MASTER_DROP_DOWN`, which selects only
 * `dishId`/`dishDescription` (+ a re-fetched `dietPreferenceList`) -- no
 * `dishCode`/`dishValue`/`uomName`/`status`/`dietMealTypeList`/
 * `dietTypeList`/`ingredientsList`. See dish-master.ts's class doc for why
 * this filter path is faithfully reproduced, not fixed.
 */
export interface DishMasterRow {
  dishId: number;
  dishCode?: string;
  dishDescription: string;
  dishValue?: number;
  uomId?: number;
  uomName?: string;
  status?: 'A' | 'I';
  dietPreferenceList?: DietPreferenceMapperRow[];
  dietMealTypeList?: MealTypeMapperRow[];
  dietTypeList?: DietTypeMapperRow[];
  ingredientsList?: DishIngredientRow[];
}

/** `getNutritionalValues` `listObject` row (NutritionalValuesDto). */
export interface NutritionalValueRow {
  id: number;
  name: string;
  uomName: string;
  value: number;
}
