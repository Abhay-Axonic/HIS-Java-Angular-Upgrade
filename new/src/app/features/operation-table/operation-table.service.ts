import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { OperationTableRow, TableTypeOption, TheatreOption } from './operation-table.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/otims/global/operationTableMasterController.js
 * and confirmed against OperationTableController.java (`@RequestMapping("/api/otims")`).
 */
@Injectable({ providedIn: 'root' })
export class OperationTableService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:58-73 -- POST { orgId, unitId }.
  getActiveTableTypeList(orgId: number, unitId: number): Observable<ApiResponse<never, TableTypeOption>> {
    return this.http.post<ApiResponse<never, TableTypeOption>>(`${this.base}/api/otims/getActiveOperationTableTypeList`, {
      orgId,
      unitId,
    });
  }

  // ctrl:77-92 -- POST { orgId, unitId }.
  getActiveTheatreList(orgId: number, unitId: number): Observable<ApiResponse<never, TheatreOption>> {
    return this.http.post<ApiResponse<never, TheatreOption>>(`${this.base}/api/otims/getActiveOperationTheatreList`, {
      orgId,
      unitId,
    });
  }

  // ctrl:101-140 -- POST { orgId, offset, noOfRecordsPerPage, unitId }.
  getList(orgId: number, unitId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, OperationTableRow>> {
    return this.http.post<ApiResponse<never, OperationTableRow>>(`${this.base}/api/otims/getOperationTableList`, {
      orgId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:119-120 -- POST { orgId, unitId }, count in `object`.
  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/otims/getOperationTableListCount`, { orgId, unitId });
  }

  // ctrl:189-257 -- POST, real create.
  save(payload: {
    tableCode: string;
    tableName: string;
    tableDesc: string;
    tableTypeId: number | '';
    operationTheatreId: number | '';
    createdBy: number;
    updatedBy: number;
    createdDate: string;
    updatedDate: string;
    status: 'A';
    unitId: number;
    orgId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/saveOperationTable`, payload);
  }

  // ctrl:259-288 -- GET path variable only, no org/unit needed (confirmed
  // against OperationTableController.java's single @PathVariable).
  getById(operationTableId: number): Observable<ApiResponse<never, OperationTableRow>> {
    return this.http.get<ApiResponse<never, OperationTableRow>>(`${this.base}/api/otims/getOperationTableListById/${operationTableId}`);
  }

  // ctrl:291-347 -- POST, real update, no confirm modal.
  update(payload: {
    operationTableId: number;
    tableCode: string;
    tableName: string;
    tableDesc: string;
    tableTypeId: number | '';
    operationTheatreId: number | '';
    updatedBy: number;
    updatedDate: string;
    unitId: number;
    orgId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateOperationTable`, payload);
  }

  // ctrl:349-382 -- POST, real status toggle.
  updateStatus(
    operationTableId: number,
    status: 'A' | 'I',
    updatedBy: number,
    updatedDate: string,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateOperationTableStatus`, {
      operationTableId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}
