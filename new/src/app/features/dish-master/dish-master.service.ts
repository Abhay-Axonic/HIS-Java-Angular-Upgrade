import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DietTypeOption,
  DishIngredientRow,
  DishMasterRow,
  KeyIngredientOption,
  MealPreferenceOption,
  MealTypeOption,
  NutritionalValueRow,
  UomOption,
} from './dish-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/diet.url.config.js (loaded AFTER
 * adt.url.config.js in index.html -- see dish-master.models.ts's
 * `MealPreferenceOption` doc for why its redeclared
 * `GETACTIVEMEALPREFERENCELIST` constant is the one that actually fires)
 * and confirmed by a real golden-trace capture this session
 * (`tools/golden-trace/capture.js` against
 * `{"name":"dishMaster","url":"/dishMaster"}`, real Playwright automation
 * bootstrapping legacy and driving `$state.go('dishMaster')`, not a
 * hand-derived substitute). The trace
 * (`tools/golden-trace/golden-traces/dishMaster.json`) confirms exactly 6
 * calls fire on load, in this order, matching the audited controller's
 * `init()` (ctrl:30-247) call-for-call -- every one fired independently,
 * with no awaited chaining between them:
 *   1. GET  /api/kitchen/master/dishMaster?organizationId=1&unitId=1                        (ctrl:124-144, dish list)
 *   2. GET  /api/kitchen/master/keyIngredientsMaster/getUnitMasterDetails?organizationId=1&unitId=1 (ctrl:147-166, UOM list)
 *   3. GET  /api/kitchen/master/keyIngredientsMaster/getActive?organizationId=1&unitId=1     (ctrl:168-185, active key-ingredients)
 *   4. POST /api/adt/unit/getActiveMealPreferenceMasterList                                   (ctrl:187-206, meal preference list)
 *   5. GET  /api/kitchen/master/mealMaster/getActive?organizationId=1&unitId=1               (ctrl:210-227, $scope.getMeal())
 *   6. GET  /api/kitchen/master/dietTypeMaster/getActive?organizationId=1&unitId=1           (ctrl:230-246, $scope.getDietType())
 * See dish-master.ts's class doc for the full Step 4 comparison and the
 * Edit-gating race-condition fix built around calls 4-6.
 */
@Injectable({ providedIn: 'root' })
export class DishMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ---- init-time calls (fired unconditionally, independently, in this order) ----
  getDishList(organizationId: number, unitId: number): Observable<ApiResponse<never, DishMasterRow>> {
    return this.http.get<ApiResponse<never, DishMasterRow>>(`${this.base}/api/kitchen/master/dishMaster`, {
      params: { organizationId, unitId },
    });
  }

  getUomList(organizationId: number, unitId: number): Observable<ApiResponse<never, UomOption>> {
    return this.http.get<ApiResponse<never, UomOption>>(
      `${this.base}/api/kitchen/master/keyIngredientsMaster/getUnitMasterDetails`,
      { params: { organizationId, unitId } },
    );
  }

  getActiveKeyIngredientList(organizationId: number, unitId: number): Observable<ApiResponse<never, KeyIngredientOption>> {
    return this.http.get<ApiResponse<never, KeyIngredientOption>>(
      `${this.base}/api/kitchen/master/keyIngredientsMaster/getActive`,
      { params: { organizationId, unitId } },
    );
  }

  /** Body shape matches `$scope.mealMasterPreferenceObj` (ctrl:67-72) exactly, page-size capped at 10 -- see class doc. */
  getActiveMealPreferenceList(payload: {
    organizationId: number;
    unitId: number;
    offset: number;
    noOfRecordsPerPage: number;
  }): Observable<ApiResponse<never, MealPreferenceOption>> {
    return this.http.post<ApiResponse<never, MealPreferenceOption>>(
      `${this.base}/api/adt/unit/getActiveMealPreferenceMasterList`,
      payload,
    );
  }

  getActiveMealTypeList(organizationId: number, unitId: number): Observable<ApiResponse<never, MealTypeOption>> {
    return this.http.get<ApiResponse<never, MealTypeOption>>(`${this.base}/api/kitchen/master/mealMaster/getActive`, {
      params: { organizationId, unitId },
    });
  }

  getActiveDietTypeList(organizationId: number, unitId: number): Observable<ApiResponse<never, DietTypeOption>> {
    return this.http.get<ApiResponse<never, DietTypeOption>>(
      `${this.base}/api/kitchen/master/dietTypeMaster/getActive`,
      { params: { organizationId, unitId } },
    );
  }

  // ---- diet-preference checkbox filter (ctrl:253-273, $scope.getDishMasterList) ----
  // CONFIRMED DEAD IN LEGACY, faithfully reproduced not fixed -- see dish-master.ts's
  // class doc. Payload is deliberately narrow: legacy's `changedValue()` only ever
  // builds `{ dietPrefId: item.mealPreferenceId }` objects (ctrl:276-294), never the
  // full `MealPreferenceMasterDto` the backend's `List<MealPreferenceMasterDto>`
  // parameter type suggests -- `mealPreferenceId`/`dietTypeId` are left unset (0/null)
  // on every element the DAO deserializes, which is itself part of why this filter
  // returns nothing useful server-side. Reproduced with the same narrow shape.
  getDishListByDietPrefId(
    selectedDietPreference: Array<{ dietPrefId: number }>,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, DishMasterRow>> {
    return this.http.post<ApiResponse<never, DishMasterRow>>(
      `${this.base}/api/kitchen/master/dishMaster/getDishListByDietPrefId`,
      selectedDietPreference,
      { params: { organizationId, unitId } },
    );
  }

  /**
   * `listObject2` (KeyIngredient detail rows) is a real, second list field on
   * the same `Response` object (`Response.java:24,159`, constructor overload
   * taking a 7th `listObject2` arg) -- not a typo, not folded into
   * `listObject`. See `DishMasterServiceImpl.getNutritionalValues` (service:157-222).
   */
  getNutritionalValues(
    dishId: number,
  ): Observable<ApiResponse<never, NutritionalValueRow> & { listObject2?: DishIngredientRow[] }> {
    return this.http.get<ApiResponse<never, NutritionalValueRow> & { listObject2?: DishIngredientRow[] }>(
      `${this.base}/api/kitchen/master/dishMaster/getNutritionalValues`,
      { params: { dishId } },
    );
  }

  // ---- CRUD ----
  saveDish(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/kitchen/master/dishMaster`, payload);
  }

  updateDish(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/api/kitchen/master/dishMaster`, payload);
  }

  /** Payload deliberately minimal -- matches ctrl:617-622 exactly (no unitId/organizationId/updatedDate sent). */
  updateStatus(payload: { dishId: number; status: 'A' | 'I'; updatedBy: number }): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/api/kitchen/master/dishMaster/updateStatus`, payload);
  }
}
