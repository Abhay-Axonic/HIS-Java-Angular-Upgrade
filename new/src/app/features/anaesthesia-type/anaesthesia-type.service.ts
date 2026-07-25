import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AnaesthesiaTypeRow,
  AnaesthesiaTypeSavePayload,
  AnaesthesiaTypeStatusPayload,
  AnaesthesiaTypeUpdatePayload,
} from './anaesthesia-type.models';

/**
 * Endpoint paths per Batch 19 Step 0/1 audit
 * (`docs/ANGULAR_PENDING_FINDINGS.md`), taken directly from
 * `resources/assets/js/controller/otims/global/anaesthesiaTypeController.js`
 * -- all under `/api/otims`, all POST except getById (GET), matching legacy's
 * own `GenericService.serviceAction` calls exactly.
 */
@Injectable({ providedIn: 'root' })
export class AnaesthesiaTypeService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    orgId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, AnaesthesiaTypeRow>> {
    return this.http.post<ApiResponse<never, AnaesthesiaTypeRow>>(`${this.base}/api/otims/getListAnesthesiaTypeMaster`, {
      orgId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/otims/getAnesthesiaTypeMasterCount`, { orgId, unitId });
  }

  getById(id: number, orgId: number, unitId: number): Observable<ApiResponse<never, AnaesthesiaTypeRow>> {
    return this.http.get<ApiResponse<never, AnaesthesiaTypeRow>>(
      `${this.base}/api/otims/getAnesthesiaTypeMasterById/${id}/${orgId}/${unitId}`,
    );
  }

  save(payload: AnaesthesiaTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/saveAnesthesiaTypeMaster`, payload);
  }

  update(payload: AnaesthesiaTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateAnesthesiaTypeMaster`, payload);
  }

  updateStatus(payload: AnaesthesiaTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateAnesthesiaTypeMasterStatus`, payload);
  }
}
