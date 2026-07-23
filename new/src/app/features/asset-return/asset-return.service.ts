import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AssetReturnCancelReason,
  AssetReturnDropdowns,
  AssetReturnRow,
  AssetReturnStatusMasterEntry,
} from './asset-return.models';

/**
 * Endpoint paths from `CONSTANTS.ASSET.ASSET_RETURN`
 * (resources/assets/js/constants/constants.js:1296-1305) and
 * `CONSTANTS.GLOBAL.STATUS_API`/`CANCEL_REASON_API` (constants.js:62,70)
 * -- not guessed, and cross-checked against
 * `AssetReturnController.java`'s own `@RequestMapping`/`@GetMapping`/
 * `@PostMapping`/`@PatchMapping` annotations (base path
 * `/api/asset/management/asset/return`). `POST /againstIssue/search` is
 * deliberately NOT included here -- it only feeds the not-yet-built
 * add/edit form, this screen's search flow never calls it.
 */
@Injectable({ providedIn: 'root' })
export class AssetReturnService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdowns(unitId: number, organizationId: number, userId: number): Observable<AssetReturnDropdowns> {
    return this.http.get<AssetReturnDropdowns>(
      `${this.base}/api/asset/management/asset/return/dropdowns/${unitId}/${organizationId}/${userId}`,
    );
  }

  getStatusMaster(): Observable<AssetReturnStatusMasterEntry[]> {
    return this.http.get<AssetReturnStatusMasterEntry[]>(`${this.base}/api/global/status`);
  }

  getCancelReasons(): Observable<AssetReturnCancelReason[]> {
    return this.http.get<AssetReturnCancelReason[]>(`${this.base}/api/global/cancel/reasons`);
  }

  search(payload: Record<string, unknown>): Observable<AssetReturnRow[]> {
    return this.http.post<AssetReturnRow[]>(`${this.base}/api/asset/management/asset/return/search`, payload);
  }

  approve(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/asset/management/asset/return/approve`, payload);
  }

  cancel(id: number, payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/asset/management/asset/return/cancel/${id}`, payload);
  }
}
