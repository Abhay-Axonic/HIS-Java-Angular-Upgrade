import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BloodDonorTypeRow,
  BloodDonorTypeSavePayload,
  BloodDonorTypeStatusPayload,
  BloodDonorTypeUpdatePayload,
} from './blood-donor-type.models';

/**
 * Endpoint paths per Batch 19 Step 0/1 audit, taken directly from
 * `resources/assets/js/controller/bloodBank/bloodDonorTypeMasterController.js`
 * and `resources/assets/js/config/bloodBank.url.config.js` -- all under
 * `/global/bloodBank`, all POST except getById (GET), matching legacy's own
 * `GenericService.serviceAction` calls exactly.
 *
 * NOTE: the server-side duplicate-check (`getBloodDonorTypeMasterByName`/
 * `_NotId`) has no organizationId/unitId scope today
 * (`docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-blooddonortype-duplicatecheck-missing-scope`) -- this is a
 * backend-side bug in the SAME live endpoint both legacy and this rebuild
 * call. It cannot be fixed from this Angular service; it needs the separate,
 * pending legacy-fix branch to be approved and merged. Calling this endpoint
 * from either frontend hits identical current behavior until then.
 */
@Injectable({ providedIn: 'root' })
export class BloodDonorTypeService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BloodDonorTypeRow>> {
    return this.http.post<ApiResponse<never, BloodDonorTypeRow>>(`${this.base}/global/bloodBank/getBloodDonorTypeMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/global/bloodBank/getBloodDonorTypeMasterCount`, {
      organizationId,
      unitId,
    });
  }

  getById(id: number, organizationId: number, unitId: number): Observable<ApiResponse<never, BloodDonorTypeRow>> {
    return this.http.get<ApiResponse<never, BloodDonorTypeRow>>(
      `${this.base}/global/bloodBank/getBloodDonorTypeMasterById/${id}/${organizationId}/${unitId}`,
    );
  }

  save(payload: BloodDonorTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/saveBloodDonorTypeMaster`, payload);
  }

  update(payload: BloodDonorTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodDonorTypeMaster`, payload);
  }

  updateStatus(payload: BloodDonorTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodDonorTypeMasterStatus`, payload);
  }
}
