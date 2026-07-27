import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  OperationTheatreTypeRow,
  OperationTheatreTypeSavePayload,
  OperationTheatreTypeStatusPayload,
  OperationTheatreTypeUpdatePayload,
} from './operation-theatre-type.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/otims/global/operationTheatreTypeController.js
 * (325 lines, `operationTheatreTypeController`), all under `/api/otims/*`.
 */
@Injectable({ providedIn: 'root' })
export class OperationTheatreTypeService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:59-98 (initMasterList) -- real POST, body params.
  getList(
    orgId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, OperationTheatreTypeRow>> {
    return this.http.post<ApiResponse<never, OperationTheatreTypeRow>>(
      `${this.base}/api/otims/getOperationTheatreTypeList`,
      { orgId, offset, noOfRecordsPerPage, unitId },
    );
  }

  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/otims/getOperationTheatreTypeCount`, {
      orgId,
      unitId,
    });
  }

  // ctrl:147-202 (save) -- real create. Duplicate-check now correctly
  // compares both name and code (fix merged and live-verified this session).
  save(payload: OperationTheatreTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/saveOperationTheatreType`, payload);
  }

  // ctrl:204-231 (showUpdateBtn) -- response.data.listObject[0], ARRAY-wrapped.
  getById(
    operationTheatreTypeId: number,
    orgId: number,
    unitId: number,
  ): Observable<ApiResponse<never, OperationTheatreTypeRow>> {
    return this.http.get<ApiResponse<never, OperationTheatreTypeRow>>(
      `${this.base}/api/otims/getOperationTheatreTypeById/${operationTheatreTypeId}/${orgId}/${unitId}`,
    );
  }

  // ctrl:234-283 (update) -- real update. Client-side validation is
  // commented out in legacy (ctrl:261,283) -- faithfully reproduced, NOT
  // fixed, see operation-theatre-type.ts's class doc comment.
  update(payload: OperationTheatreTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateOperationTheatreType`, payload);
  }

  // ctrl:289-322 (updateStatus) -- real status toggle. Does NOT reload the
  // list on success in legacy -- faithfully reproduced, see class doc.
  updateStatus(payload: OperationTheatreTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateOperationTheatreTypeStatus`, payload);
  }
}
