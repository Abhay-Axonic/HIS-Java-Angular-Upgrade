import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BloodBagTypeRow,
  BloodBagTypeSavePayload,
  BloodBagTypeStatusPayload,
  BloodBagTypeUpdatePayload,
} from './blood-bag-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/bloodBank.url.config.js (17-23), all under
 * `/global/bloodBank/*` -- NOT `/bloodBank/*` (that prefix belongs to the
 * sibling `bloodBankMaster` screen from batch 22, a different DAO/entity).
 */
@Injectable({ providedIn: 'root' })
export class BloodBagTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:48-86 (initMasterList) -- real POST, body params, genuinely working pagination.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, BloodBagTypeRow>> {
    return this.http.post<ApiResponse<never, BloodBagTypeRow>>(
      `${this.base}/global/bloodBank/getBloodBagTypeMasterList`,
      { organizationId, offset, noOfRecordsPerPage, unitId },
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/global/bloodBank/getBloodBagTypeMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:126-225 (save) -- real create. Duplicate-check is description-only,
  // case-insensitive, NOT org/unit-scoped -- see class doc, faithfully reproduced.
  save(payload: BloodBagTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/saveBloodBagTypeMaster`, payload);
  }

  // ctrl:232-260 (showUpdateBtn) -- response.data.listObject[0], ARRAY-wrapped.
  getById(
    bloodBagTypeId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, BloodBagTypeRow>> {
    return this.http.get<ApiResponse<never, BloodBagTypeRow>>(
      `${this.base}/global/bloodBank/getBloodBagTypeMasterById/${bloodBagTypeId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:263-336 (update) -- real update, no confirm modal in legacy.
  update(payload: BloodBagTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodBagTypeMaster`, payload);
  }

  // ctrl:342-378 (updateStatus) -- real status toggle, DOES reload the list.
  updateStatus(payload: BloodBagTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodBagTypeMasterStatus`, payload);
  }
}
