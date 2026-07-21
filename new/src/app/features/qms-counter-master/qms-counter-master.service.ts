import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  KioskOption,
  QMSCounterDetail,
  QMSCounterRow,
  QMSCounterSavePayload,
  QMSCounterUpdatePayload,
  QMSDeptOptionRaw,
} from './qms-counter-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/qms/QMScounterMasterController.js. State
 * `QMScounterMaster` registered centrally in app.js (~line 20050), moduleId
 * 797, no `controllerAs` (plain `$scope`) -- confirmed live via the
 * sidebar's own `checkAccess(797)` entry (views/common/new-side-bar.html:5190).
 * All calls are POST, matching `GenericService.serviceAction("POST", ...)`
 * at every call site.
 */
@Injectable({ providedIn: 'root' })
export class QmsCounterMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:89-100 -- identical endpoint to this codebase's
  // qms-department-master sibling's `getActiveKiosks`.
  getActiveKiosks(organisationId: number, unitId: number): Observable<ApiResponse<never, KioskOption>> {
    return this.http.post<ApiResponse<never, KioskOption>>(`${this.base}/api/pharmacy/getActiveQMSMasterList`, {
      organisationId,
      unitId,
    });
  }

  // ctrl:103-123. No `status` gate in legacy -- reproduced by not gating on
  // it here either (component does the raw->{id,label} remap, matching
  // where legacy itself does it).
  getActiveDepartmentsByKiosk(
    organisationId: number,
    unitId: number,
    qmsKioskMasterId: number,
  ): Observable<ApiResponse<never, QMSDeptOptionRaw>> {
    return this.http.post<ApiResponse<never, QMSDeptOptionRaw>>(`${this.base}/api/pharmacy/getActiveDeptListByKioskId`, {
      organisationId,
      unitId,
      qmsKioskMasterId,
    });
  }

  // ctrl:129-170 (initQMSCounterMasterList) -- list half.
  getList(
    organisationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, QMSCounterRow>> {
    return this.http.post<ApiResponse<never, QMSCounterRow>>(`${this.base}/api/pharmacy/getQMSCounterMasterList`, {
      organisationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:129-170 -- count half; gated on status === 'success' in legacy
  // (unlike qms-department-master's ungated kiosk/reception calls).
  getCount(organisationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/pharmacy/getQMSCounterMasterCount`, {
      organisationId,
      unitId,
    });
  }

  // ctrl:216-268.
  save(payload: QMSCounterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/saveQMSCounterMaster`, payload);
  }

  // ctrl:270-312 (edit) AND ctrl:412-442 (the read-only "Department Details"
  // popup) both reuse this SAME endpoint in legacy -- reproduced the same
  // way here (see component's `edit()`/`viewDepartmentDetails()`).
  getById(qmsCounterId: number, organisationId: number, unitId: number): Observable<ApiResponse<QMSCounterDetail>> {
    return this.http.post<ApiResponse<QMSCounterDetail>>(`${this.base}/api/pharmacy/getQMSCounterMasterById`, {
      qmsCounterId,
      organisationId,
      unitId,
    });
  }

  // ctrl:315-374.
  update(payload: QMSCounterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/updateQMSCounterMaster`, payload);
  }

  /**
   * ctrl:376-409. **Faithful bug reproduction, NOT fixed:** legacy's
   * `updateStatus(id,status)` sends `updatedBy: $scope.updatedBy` -- a scope
   * property never assigned anywhere in this controller (only
   * `$scope.userId` is ever set, and IS used correctly by `save`/`update`
   * above) -- so this one call always sends `updatedBy: undefined` today.
   * Reproduced by accepting `updatedBy` as an explicit parameter that the
   * component always passes as `undefined`, rather than silently defaulting
   * it to a real user id here.
   */
  updateStatus(qmsCounterId: number, status: 'A' | 'I', updatedBy: number | undefined): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/updateQMSCounterMasterStatus`, {
      qmsCounterId,
      status,
      updatedBy,
    });
  }
}
