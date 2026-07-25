import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BloodBagCapacityRow,
  BloodBagCapacitySavePayload,
  BloodBagCapacityStatusPayload,
  BloodBagCapacityUpdatePayload,
} from './blood-bag-capacity.models';

/**
 * Endpoint paths per Batch 19 Step 0/1 audit, taken directly from
 * `resources/assets/js/controller/bloodBank/bloodBagCapacityMasterController.js`
 * and `resources/assets/js/config/bloodBank.url.config.js` -- all under
 * `/global/bloodBank`, all POST except getById (GET).
 *
 * NOTE: two server-side bugs live in these same endpoints, confirmed but
 * NOT fixable from this frontend (`docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-bloodbagcapacity-step1-findings`):
 * - Create-time duplicate-check has no org/unit scope.
 * - Update's duplicate-check binds the query parameter literally named
 *   `bloodBagCapacityCode` to the DTO's `bloodBagCapacityDesc` value --
 *   the "code already exists" guard on Update is actually comparing
 *   description text against the code column.
 * Both need the separate, pending legacy-fix branches to be approved and
 * applied; calling these endpoints from either frontend hits identical
 * current behavior until then.
 */
@Injectable({ providedIn: 'root' })
export class BloodBagCapacityService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BloodBagCapacityRow>> {
    return this.http.post<ApiResponse<never, BloodBagCapacityRow>>(`${this.base}/global/bloodBank/getBloodBagCapacityMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/global/bloodBank/getBloodBagCapacityMasterCount`, {
      organizationId,
      unitId,
    });
  }

  getById(id: number, organizationId: number, unitId: number): Observable<ApiResponse<never, BloodBagCapacityRow>> {
    return this.http.get<ApiResponse<never, BloodBagCapacityRow>>(
      `${this.base}/global/bloodBank/getBloodBagCapacityMasterById/${id}/${organizationId}/${unitId}`,
    );
  }

  save(payload: BloodBagCapacitySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/saveBloodBagCapacityMaster`, payload);
  }

  update(payload: BloodBagCapacityUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodBagCapacityMaster`, payload);
  }

  updateStatus(payload: BloodBagCapacityStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodBagCapacityMasterStatus`, payload);
  }
}
