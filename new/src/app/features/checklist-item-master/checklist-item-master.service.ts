import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ChecklistItemForm, ChecklistItemRow } from './checklist-item-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/otims/global/checklistItemMasterController.js
 * -- not guessed. All under `/global/otims`. Confirmed live 2026-07-28:
 * real create/upsert-update/status-toggle round trips, all HTTP 200.
 */
@Injectable({ providedIn: 'root' })
export class ChecklistItemMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, ChecklistItemRow>> {
    return this.http.post<ApiResponse<never, ChecklistItemRow>>(`${this.base}/global/otims/getChecklistItemMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:80-134 -- single upsert endpoint, real self-excluding-on-update
  // duplicate-check server-side (confirmed via direct DAO read -- BOTH
  // create and update paths are protected, code and description).
  save(payload: ChecklistItemForm & { organizationId: number; unitId: number; userId: number }): Observable<ApiResponse> {
    const now = formatDateTime(new Date());
    return this.http.post<ApiResponse>(`${this.base}/global/otims/saveChecklistItemMaster`, {
      checklistItemMasterId: payload.checklistItemMasterId,
      code: payload.code,
      description: payload.description,
      status: 'A',
      createdBy: payload.userId,
      createdDate: now,
      updatedBy: payload.userId,
      updatedDate: now,
      unitId: payload.unitId,
      organizationId: payload.organizationId,
    });
  }

  // ctrl:136-171 -- unlike checklistEquipmentMaster's sibling, the success
  // branch here DOES trigger a real list reload after the status POST
  // resolves (see component for the call site).
  updateStatus(payload: {
    checklistItemMasterId: number;
    status: ChecklistItemRow['status'];
    updatedBy: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/otims/updateChecklistItemMasterStatus`, {
      checklistItemMasterId: payload.checklistItemMasterId,
      status: payload.status,
      updatedBy: payload.updatedBy,
      updatedDate: formatDateTime(new Date()),
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:72/75/139. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
