import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AradDetail, AradRejectReason, AradStatus } from './asset-return-acceptance-details.models';

/**
 * Endpoint paths from CONSTANTS.ASSET.ASSET_RETURN_ACCEPTANCE (DETAIL_API,
 * SAVE_API) and CONSTANTS.GLOBAL (STATUS_API, REJECT_REASON_API) in
 * constants/constants.js -- not guessed. Matches
 * asset-return-acceptance.service.ts's base-path style (this detail
 * screen's own list-view counterpart).
 */
@Injectable({ providedIn: 'root' })
export class AssetReturnAcceptanceDetailsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDetail(id: number | string, unitId: number): Observable<AradDetail> {
    return this.http.get<AradDetail>(`${this.base}/api/asset/management/asset/return/acceptance/${id}/${unitId}`);
  }

  getStatuses(): Observable<AradStatus[]> {
    return this.http.get<AradStatus[]>(`${this.base}/api/global/status`);
  }

  getRejectReasons(unitId: number): Observable<AradRejectReason[]> {
    return this.http.get<AradRejectReason[]>(`${this.base}/api/global/reject/reasons/${unitId}`);
  }

  save(id: number, payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/asset/management/asset/return/acceptance/${id}`, payload);
  }
}
