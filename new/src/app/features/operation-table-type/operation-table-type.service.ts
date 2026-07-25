import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  OperationTableTypeCreatePayload,
  OperationTableTypeRow,
  OperationTableTypeStatusPayload,
  OperationTableTypeUpdatePayload,
} from './operation-table-type.models';

/**
 * Endpoint paths taken directly from
 * `operationTableTypeMasterController.js` (`BASE_URL + ROOT_URL +
 * "/api/otims/..."` -- both empty strings in this deployment) and confirmed
 * real end to end by reading source directly: `OperationTableTypeController.
 * java` (`@RequestMapping("/api/otims")`) -> `OperationTableTypeServiceImpl`
 * -> `OperationTableTypeDaoImpl` -> table `otims.m_operation_table_type`.
 * All 6 endpoints below are confirmed live/real/authenticated per this
 * batch's Step 1 audit (docs/ANGULAR_PENDING_FINDINGS.md, entry
 * `PENDING-batch18-step1-operationtabletype`); the golden trace captured 3
 * parallel, unchained 200s on load (template + list + count), matched by
 * `getList`/`getCount` below being fired independently, not chained.
 */
@Injectable({ providedIn: 'root' })
export class OperationTableTypeService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:73-108 (initMasterList, data1) -- POST { orgId, offset,
  // noOfRecordsPerPage, unitId }.
  getList(
    orgId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, OperationTableTypeRow>> {
    return this.http.post<ApiResponse<never, OperationTableTypeRow>>(`${this.base}/api/otims/getOperationTableTypeList`, {
      orgId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:73-108 (initMasterList, data2, fired in parallel with getList
  // above, not chained after it) -- POST { orgId, unitId }, count in
  // `object`.
  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/otims/getOperationTableTypeListCount`, {
      orgId,
      unitId,
    });
  }

  // ctrl:203-227 (showUpdateBtn) -- GET, path-variable id. Fresh fetch, NOT
  // a row-reference alias (legacy already does this correctly, see this
  // component's class doc comment / entry 4 of this batch's brief).
  getById(operationTableTypeId: number): Observable<ApiResponse<never, OperationTableTypeRow>> {
    return this.http.get<ApiResponse<never, OperationTableTypeRow>>(
      `${this.base}/api/otims/getOperationTableTypeListById/${operationTableTypeId}`,
    );
  }

  // ctrl:158-201 (save()'s real POST branch, reached only after the
  // confirm-modal's OK, or immediately once `popUpFlag` has gone false --
  // see class doc comment).
  create(payload: OperationTableTypeCreatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/saveOperationTableType`, payload);
  }

  // ctrl:229-256 (update()). See operation-table-type.models.ts's doc
  // comment for why orgId/unitId are load-bearing here (the real,
  // tableName-only duplicate check the update service method runs).
  update(payload: OperationTableTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateOperationTableType`, payload);
  }

  // ctrl:280-309 (updateStatus()).
  updateStatus(payload: OperationTableTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/otims/updateOperationTableTypeStatus`, payload);
  }
}
