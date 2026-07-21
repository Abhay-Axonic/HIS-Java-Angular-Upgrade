import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { RaaDropdowns, RaaRow } from './rejected-asset-acceptance.models';

/**
 * Rebuild-side equivalent of `CONSTANTS.ASSET.REJECTED_ASSET_ACCEPTANCE`
 * (legacy resources/assets/js/constants/constants.js:1313-1319, under
 * `assetApi = rootUrl + '/api/asset'`). Only SEARCH_API and DROPDOWN_API
 * are used by this screen -- DETAIL_API/SAVE_API/UPDATE_API belong to the
 * separate `detailsRejectedAssetAcceptance` state, not this list screen.
 */
@Injectable({ providedIn: 'root' })
export class RejectedAssetAcceptanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdowns(unitId: number, organizationId: number, userId: number): Observable<RaaDropdowns> {
    return this.http.get<RaaDropdowns>(
      `${this.base}/api/asset/indent/assetIssue/reject/asset/acceptance/dropdowns/${unitId}/${organizationId}/${userId}`,
    );
  }

  search(payload: Record<string, unknown>): Observable<RaaRow[]> {
    return this.http.post<RaaRow[]>(`${this.base}/api/asset/indent/assetIssue/reject/asset/acceptance/search`, payload);
  }
}
