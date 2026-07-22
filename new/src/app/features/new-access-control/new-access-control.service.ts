import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ActionMasterDetail, ActionMasterForm, ActionMasterRow, ActionMasterSearchOption } from './new-access-control.models';

/**
 * All 6 endpoints confirmed real end-to-end (Controller -> Service -> DAO):
 * `legacy/Global/src/main/java/com/param/global/unit/controller/ActionMasterControllerGlobal.java`
 * (base `/api/global`) -> `ActionMasterServiceGlobalImpl.java` (thin
 * passthrough) -> `ActionMasterGlobalDaoImpl.java` (real Hibernate named
 * queries against `public.m_action_master`).
 */
@Injectable({ providedIn: 'root' })
export class NewAccessControlService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, ActionMasterRow>> {
    return this.http.get<ApiResponse<never, ActionMasterRow>>(
      `${this.base}/api/global/getActionMasterList/${offset}/${noOfRecordsPerPage}`,
    );
  }

  getCount(): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/api/global/getActionMasterCount`);
  }

  getById(actionId: number): Observable<ApiResponse<never, ActionMasterDetail>> {
    return this.http.get<ApiResponse<never, ActionMasterDetail>>(`${this.base}/api/global/getActionMasterById/${actionId}`);
  }

  searchByParentActionName(actionName: string): Observable<ApiResponse<never, ActionMasterSearchOption>> {
    return this.http.post<ApiResponse<never, ActionMasterSearchOption>>(
      `${this.base}/api/global/searchActionMasterByParentActionName`,
      { actionName },
    );
  }

  save(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveActionMaster`, payload);
  }

  update(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateActionMaster`, payload);
  }

  updateStatus(actionId: number, status: 'A' | 'I' | 'D'): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateActionMasterStatus`, { actionId, status });
  }
}
