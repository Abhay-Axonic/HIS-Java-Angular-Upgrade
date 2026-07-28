import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ChecklistEquipmentForm, ChecklistEquipmentRow } from './checklist-equipment-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/equipmentMaster/checklistEquipmentMasterController.js
 * -- not guessed. All under `/global/equipmentchecklist`. Confirmed live
 * 2026-07-28: real create/upsert-update/status-toggle round trips, all
 * HTTP 200.
 */
@Injectable({ providedIn: 'root' })
export class ChecklistEquipmentMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, ChecklistEquipmentRow>> {
    return this.http.post<ApiResponse<never, ChecklistEquipmentRow>>(
      `${this.base}/global/equipmentchecklist/getAllEquipmentChecklistMasterListPaginationList`,
      { organizationId, offset, noOfRecordsPerPage, unitId },
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(
      `${this.base}/global/equipmentchecklist/getAllEquipmentChecklistMasterListPaginationListCount`,
      { organizationId, unitId },
    );
  }

  // ctrl:153-201 -- single upsert endpoint, serves both save and update
  // depending on whether checklistEquipmentMasterId is set. Real,
  // self-excluding-on-update duplicate-check server-side.
  save(payload: ChecklistEquipmentForm & { organizationId: number; unitId: number; userId: number }): Observable<ApiResponse> {
    const now = formatDateTime(new Date());
    return this.http.post<ApiResponse>(`${this.base}/global/equipmentchecklist/saveChecklistEquipmentMaster`, {
      checklistEquipmentMasterId: payload.checklistEquipmentMasterId,
      checklistCode: payload.checklistCode,
      checklistName: payload.checklistName,
      status: 'A',
      createdBy: payload.userId,
      createdDate: now,
      updatedBy: payload.userId,
      updatedDate: now,
      unitId: payload.unitId,
      organizationId: payload.organizationId,
    });
  }

  // ctrl:203-237.
  updateStatus(payload: {
    checklistEquipmentMasterId: number;
    status: ChecklistEquipmentRow['status'];
    updatedBy: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/equipmentchecklist/updateChecklistEquipmentMasterStatus`, {
      checklistEquipmentMasterId: payload.checklistEquipmentMasterId,
      status: payload.status,
      updatedBy: payload.updatedBy,
      updatedDate: formatDateTime(new Date()),
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:40/43/206. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
