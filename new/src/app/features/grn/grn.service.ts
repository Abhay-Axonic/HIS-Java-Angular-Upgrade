import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CancelReason,
  DropdownOption,
  GrnListRow,
  GrnSearchRequest,
  ItemSearchResult,
  StatusMasterEntry,
  VendorSearchResult,
} from './grn.models';

interface GrnDropdownResponse {
  deliveryStores: DropdownOption[];
  requestingStores: DropdownOption[];
  status: DropdownOption[];
  assetType: DropdownOption[];
}

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/procurement/quotation/grnController.js
 * and resources/assets/js/constants/constants.js -- not guessed. See
 * this pilot batch's audit doc for the golden-trace confirmation of
 * these exact calls (on-load sequence: cancel/reasons, status,
 * grn/dropdowns all fire in parallel; grn/search fires only after
 * grn/dropdowns resolves, same as legacy's PopulateDropdown().then(initialCall)).
 * Note: the API base path is `/api/procurements` (plural) per
 * `constants.js`'s `procurementApi` var -- confirmed via golden-trace
 * capture after an initial rebuild draft got this wrong (singular).
 *
 * `GenericService.serviceAction` in legacy is a thin wrapper around
 * $http with no special envelope beyond what's modeled in ApiResponse
 * elsewhere in this app; GRN's raw list/dropdown responses are NOT
 * wrapped in {status, listObject} the way Billing's endpoints are --
 * legacy reads `response.data` directly as the payload. Kept as-is.
 */
@Injectable({ providedIn: 'root' })
export class GrnService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getCancelReasons(): Observable<CancelReason[]> {
    return this.http.get<CancelReason[]>(`${this.base}/api/global/cancel/reasons`);
  }

  getStatusMaster(): Observable<StatusMasterEntry[]> {
    return this.http.get<StatusMasterEntry[]>(`${this.base}/api/global/status`);
  }

  getDropdowns(unitId: number, organizationId: number, userId: number): Observable<GrnDropdownResponse> {
    return this.http.get<GrnDropdownResponse>(
      `${this.base}/api/procurements/quotation/grn/dropdowns/${unitId}/${organizationId}/${userId}`,
    );
  }

  searchGrn(request: GrnSearchRequest): Observable<GrnListRow[]> {
    return this.http.post<GrnListRow[]>(`${this.base}/api/procurements/quotation/grn/search`, request);
  }

  approveGrn(grnIdList: number[], statusId: number, approvalStatusId: number, userId: number): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/procurements/quotation/grn/approve`, {
      grnIdList,
      statusId,
      approvalStatusId,
      userId,
    });
  }

  cancelGrn(
    id: number,
    statusId: number,
    approvalStatusId: number,
    cancelReasonId: number | undefined,
    cancelNote: string | undefined,
    userId: number,
  ): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/procurements/quotation/grn/cancel/${id}`, {
      statusId,
      approvalStatusId,
      cancelReasonId,
      cancelNote,
      userId,
    });
  }

  searchVendors(search: string, unitId: number): Observable<VendorSearchResult[]> {
    return this.http.get<VendorSearchResult[]>(`${this.base}/api/global/vendor/search/${search}/${unitId}`);
  }

  searchItems(unitId: number, search: string, fromStoreId: number | undefined): Observable<ItemSearchResult[]> {
    return this.http.get<ItemSearchResult[]>(
      `${this.base}/api/global/items/${unitId}/false/${search}/${fromStoreId ?? ''}`,
    );
  }
}
