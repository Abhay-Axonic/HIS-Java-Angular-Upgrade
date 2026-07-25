import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  NutritionalMasterRow,
  NutritionalMasterSavePayload,
  NutritionalMasterStatusPayload,
  NutritionalMasterUpdatePayload,
} from './nutritional-master.models';

/**
 * Endpoint paths per Batch 19 Step 0/1 audit, taken directly from
 * `resources/assets/js/controller/dietAndKitchen/global/nutritionalMasterController.js`
 * and `resources/assets/js/config/diet.url.config.js`. Save is POST, Update
 * and status-change are PUT, all three hitting the SAME base path
 * (`/api/kitchen/master/nutritionalMaster`) for save/update -- confirmed by
 * reading both `saveNutrition`/`updateNutrition`, not guessed.
 *
 * `getCount` intentionally NOT implemented here -- see the component class
 * doc comment (`getNutritionMasterCount` 500s server-side today,
 * `docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-nutritionalmaster-count-endpoint-crash`; this rebuild implements
 * corrected, working pagination via a full-list client-side fetch instead of
 * depending on that endpoint at all).
 */
@Injectable({ providedIn: 'root' })
export class NutritionalMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** `offset`/`noOfRecordsPerPage` are real, working server-side params on
   * this endpoint (confirmed) -- called here with a large page size to fetch
   * the full org+unit-scoped list once, then paginated client-side. */
  getAll(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, NutritionalMasterRow>> {
    return this.http.get<ApiResponse<never, NutritionalMasterRow>>(
      `${this.base}/api/kitchen/master/nutritionalMaster/getAll`,
      { params: { organizationId, unitId, offset, noOfRecordsPerPage } },
    );
  }

  save(payload: NutritionalMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/kitchen/master/nutritionalMaster`, payload);
  }

  update(payload: NutritionalMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/api/kitchen/master/nutritionalMaster`, payload);
  }

  updateStatus(payload: NutritionalMasterStatusPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/api/kitchen/master/nutritionalMaster/updateStatus`, payload);
  }
}
