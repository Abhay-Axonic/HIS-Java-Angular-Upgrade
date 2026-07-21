import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { KioskOption, QMSDeptCommon, QMSDeptRow } from './qms-department-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/qms/QMSdepartmentMasterController.js --
 * every URI there is a literal `BASE_URL + ROOT_URL + "/api/pharmacy/..."`
 * string (both config values are empty strings in this deployment, see
 * api-config.ts), not a named constant, so nothing was "looked up" beyond
 * reading the controller itself. All eight calls are POST, matching
 * GenericService.serviceAction("POST", ...) at every call site.
 */
@Injectable({ providedIn: 'root' })
export class QmsDepartmentMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:64-75. Legacy assigns `response.data.listObject` with NO
  // `status === 'success'` gate at all -- reproduced by not gating on it
  // here either (see the component's constructor comment).
  getActiveKiosks(organisationId: number, unitId: number): Observable<ApiResponse<never, KioskOption>> {
    return this.http.post<ApiResponse<never, KioskOption>>(`${this.base}/api/pharmacy/getActiveQMSMasterList`, {
      organisationId,
      unitId,
    });
  }

  // ctrl:101-119.
  getList(
    organisationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, QMSDeptRow>> {
    return this.http.post<ApiResponse<never, QMSDeptRow>>(`${this.base}/api/pharmacy/getQMSDeptMasterList`, {
      organisationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:102,121-128.
  getCount(organisationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/pharmacy/getQMSDeptMasterCount`, {
      organisationId,
      unitId,
    });
  }

  // ctrl:173-196. No `status` gate in legacy either -- see component.
  getReceptionListByKiosk(
    organisationId: number,
    unitId: number,
    qmsKioskMasterId: number,
  ): Observable<ApiResponse<never, unknown>> {
    return this.http.post<ApiResponse<never, unknown>>(`${this.base}/api/pharmacy/getReceptionListByKioskId`, {
      organisationId,
      unitId,
      qmsKioskMasterId,
    });
  }

  // ctrl:201-244. Posts `$scope.common` verbatim, including the always-''
  // `qmsDeptMasterId` field on a fresh create -- reproduced as-is.
  save(payload: QMSDeptCommon): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/saveQMSDeptMaster`, payload);
  }

  // ctrl:246-283, request body is the reduced 3-field object legacy builds
  // just before this call (ctrl:251-255).
  getById(
    qmsDeptMasterId: number,
    organisationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, QMSDeptCommon>> {
    return this.http.post<ApiResponse<never, QMSDeptCommon>>(`${this.base}/api/pharmacy/getQMSDeptMasterById`, {
      qmsDeptMasterId,
      organisationId,
      unitId,
    });
  }

  // ctrl:286-327.
  update(payload: QMSDeptCommon): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/updateQMSDeptMaster`, payload);
  }

  // ctrl:329-362. `updatedBy` is deliberately omitted here -- legacy sends
  // `$scope.updatedBy`, a scope property assigned nowhere in this
  // controller, so it's always `undefined` and dropped by JSON
  // serialization. This is the one latent divergence from
  // QMSkioskMasterController this screen has NOT (yet) inherited fully:
  // create/update correctly use `$scope.userId` (see emptyQMSDeptCommon),
  // but updateStatus still reads the never-assigned `updatedBy`.
  // Reproduced verbatim -- not "fixed" to send a real user id.
  updateStatus(qmsDeptMasterId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/updateQMSDeptMasterStatus`, {
      qmsDeptMasterId,
      status,
    });
  }
}
