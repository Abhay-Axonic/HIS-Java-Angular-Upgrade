import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  MealPreferenceDetail,
  MealPreferenceRow,
  MealPreferenceSavePayload,
  MealPreferenceStatusPayload,
  MealPreferenceUpdatePayload,
} from './meal-preference-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/unit/mealPreferenceMasterController.js
 * and the literal string constants in
 * resources/assets/js/config/adt.url.config.js:624-629 (all under
 * `/api/adt/unit/...`, unlike this codebase's amenity-master/floor-master,
 * whose adt endpoints have no `/api` prefix). All five calls are POST,
 * matching `GenericService.serviceAction("POST", ...)` at every call site
 * -- including the by-id lookup (ctrl:264-268 passes an empty `{}` body on
 * a GET verb in legacy's own `GenericService.serviceAction("GET", ...)`
 * call, but the URL itself is a `POST`-style path; reproduced here as a
 * plain POST with an empty body, since the id/org/unit are passed as URL
 * path segments either way).
 */
@Injectable({ providedIn: 'root' })
export class MealPreferenceMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:77-116.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, MealPreferenceRow>> {
    return this.http.post<ApiResponse<never, MealPreferenceRow>>(`${this.base}/api/adt/unit/getMealPreferenceMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:96-99,117-131.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/adt/unit/getMealPreferenceMasterCount`, {
      unitId,
      organizationId,
    });
  }

  // ctrl:256-280 -- URI path is `/{id}/{organizationId}/{unitId}`.
  getById(
    id: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, MealPreferenceDetail>> {
    return this.http.post<ApiResponse<never, MealPreferenceDetail>>(
      `${this.base}/api/adt/unit/getMealPreferenceMasterById/${id}/${organizationId}/${unitId}`,
      {},
    );
  }

  // ctrl:194-251.
  save(payload: MealPreferenceSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/unit/saveMealPreferenceMaster`, payload);
  }

  // ctrl:325-374.
  update(payload: MealPreferenceUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/unit/updateMealPreferenceMaster`, payload);
  }

  // ctrl:283-322.
  updateStatus(payload: MealPreferenceStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/unit/updateMealPreferenceMasterStatus`, payload);
  }
}
