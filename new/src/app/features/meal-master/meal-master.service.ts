import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { MealMasterRow, MealMasterSavePayload, MealMasterStatusPayload } from './meal-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/diet.url.config.js:48-51 and the inline
 * `/api/kitchen/master/mealMaster/*` literals in
 * mealMasterController.js, all under `/api/kitchen/master/mealMaster*`.
 */
@Injectable({ providedIn: 'root' })
export class MealMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:95-133 (initMasterList) -- real POST with a body, genuinely
  // working pagination. NOTE: the list query is ACTIVE-ONLY and
  // org-scoped only (no unitId), while the count query is org+unit
  // scoped -- see class doc, faithfully reproduced.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, MealMasterRow>> {
    return this.http.post<ApiResponse<never, MealMasterRow>>(
      `${this.base}/api/kitchen/master/mealMaster/getAllMealMasterListPaginationList`,
      { organizationId, offset, noOfRecordsPerPage, unitId },
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(
      `${this.base}/api/kitchen/master/mealMaster/getAllMealMasterListPaginationListCount`,
      { organizationId, unitId },
    );
  }

  // ctrl:213-218 (saveMealMaster's real POST branch) -- duplicate-check
  // by code AND by serving time, both org-scoped, both server-side.
  save(payload: MealMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/kitchen/master/mealMaster`, payload);
  }

  // ctrl:292-294 (updateMeal's real PUT branch) -- same duplicate-check
  // shape as save, correctly self-excluding by mealId.
  update(payload: MealMasterSavePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/api/kitchen/master/mealMaster`, payload);
  }

  // ctrl:247-278 (updateStatus) -- does NOT reload the list.
  updateStatus(payload: MealMasterStatusPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/api/kitchen/master/mealMaster/updateStatus`, payload);
  }
}
