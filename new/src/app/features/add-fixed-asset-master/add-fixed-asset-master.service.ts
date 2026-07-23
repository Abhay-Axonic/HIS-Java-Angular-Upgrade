import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AfamCancelReason, AfamDropdowns, AfamRow, AfamSearchRequest, AfamStatusMasterEntry } from './add-fixed-asset-master.models';

/**
 * Endpoint paths taken directly from `CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET`
 * (resources/assets/js/constants/constants.js:1214-1222) and
 * addFixedAssetMasterController.js -- not guessed:
 *   SEARCH_API:   assetApi + '/fixedAsset/addFixedAsset/search'
 *   DROPDOWN_API: assetApi + '/fixedAsset/addFixedAsset/dropdowns'
 *   CANCEL_API:   assetApi + '/fixedAsset/addFixedAsset/cancel/'
 *   APPROVE_API:  assetApi + '/fixedAsset/addFixedAsset/approve'
 * (assetApi = rootUrl + '/api/asset'). Global lookups (`StatusService`,
 * `cancelReasonService`) hit `CONSTANTS.GLOBAL.STATUS_API` /
 * `CANCEL_REASON_API`, i.e. `/api/global/status` and
 * `/api/global/cancel/reasons` -- same shared endpoints GRN's rebuild
 * already uses (see grn.service.ts), reused verbatim here rather than
 * re-declared per screen.
 */
@Injectable({ providedIn: 'root' })
export class AddFixedAssetMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdowns(unitId: number, organizationId: number, userId: number): Observable<AfamDropdowns> {
    return this.http.get<AfamDropdowns>(
      `${this.base}/api/asset/fixedAsset/addFixedAsset/dropdowns/${unitId}/${organizationId}/${userId}`,
    );
  }

  search(request: AfamSearchRequest): Observable<AfamRow[]> {
    return this.http.post<AfamRow[]>(`${this.base}/api/asset/fixedAsset/addFixedAsset/search`, request);
  }

  approve(openingBalanceId: number[], statusId: number, userId: number, unitId: number, organizationId: number): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/asset/fixedAsset/addFixedAsset/approve`, {
      openingBalanceId,
      statusId,
      userId,
      unitId,
      organizationId,
    });
  }

  cancel(
    id: number,
    statusId: number,
    cancelReasonId: number | undefined,
    cancelNote: string | undefined,
    userId: number,
  ): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/asset/fixedAsset/addFixedAsset/cancel/${id}`, {
      statusId,
      cancelReasonId,
      cancelNote,
      userId,
    });
  }

  getCancelReasons(): Observable<AfamCancelReason[]> {
    return this.http.get<AfamCancelReason[]>(`${this.base}/api/global/cancel/reasons`);
  }

  getStatusMaster(): Observable<AfamStatusMasterEntry[]> {
    return this.http.get<AfamStatusMasterEntry[]>(`${this.base}/api/global/status`);
  }
}
