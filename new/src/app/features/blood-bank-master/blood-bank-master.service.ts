import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BloodBankRow,
  BloodBankSavePayload,
  BloodBankStatusPayload,
  BloodBankUpdatePayload,
} from './blood-bank-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/bloodBank/bloodBankMasterController.js
 * (382 lines, `bloodBankMasterController`), all under `/bloodBank/*`.
 */
@Injectable({ providedIn: 'root' })
export class BloodBankMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:48-86 (initMasterList) -- real POST, body params.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, BloodBankRow>> {
    return this.http.post<ApiResponse<never, BloodBankRow>>(`${this.base}/bloodBank/getBloodBankMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/bloodBank/getBloodBankMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:136-225 (save) -- real create.
  save(payload: BloodBankSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/bloodBank/saveBloodBankMaster`, payload);
  }

  // ctrl:232-260 (showUpdateBtn) -- response.data.listObject[0], ARRAY-wrapped.
  getById(bloodBankId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, BloodBankRow>> {
    return this.http.get<ApiResponse<never, BloodBankRow>>(
      `${this.base}/bloodBank/getBloodBankMasterById/${bloodBankId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:263-336 (update) -- real update, no confirm modal in legacy.
  update(payload: BloodBankUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/bloodBank/updateBloodBankMaster`, payload);
  }

  // ctrl:342-378 (updateStatus) -- real status toggle.
  updateStatus(payload: BloodBankStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/bloodBank/updateBloodBankMasterStatus`, payload);
  }
}
