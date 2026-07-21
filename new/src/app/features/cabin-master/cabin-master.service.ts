import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CabinDetail,
  CabinRow,
  CabinSavePayload,
  CabinStatusPayload,
  CabinUpdatePayload,
} from './cabin-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/cabinMasterController.js and
 * their literal string values in
 * resources/assets/js/config/global.url.config.js:148-153:
 *   SAVE_CABIN_MASTER          = /api/global/unit/saveCabinMaster          (ctrl:176)
 *   UPDATE_CABIN_MASTER        = /api/global/unit/updateCabinMaster        (ctrl:263)
 *   UPDATE_CABIN_MASTER_STATUS = /api/global/unit/updateCabinMasterStatus  (ctrl:305)
 *   GET_CABIN_MASTER_BY_ID     = /api/global/unit/getCabinMasterById       (ctrl:220)
 *   GET_CABIN_MASTER_LIST      = /api/global/unit/getCabinMasterList       (ctrl:78, 114)
 *   GET_CABIN_MASTER_COUNT     = /api/global/unit/getCabinMasterCount      (ctrl:79)
 * `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this deployment, so
 * these are the complete paths. List/count/save/update/status calls are all
 * POST in legacy (`GenericService.serviceAction("POST", ...)`); only the
 * by-id lookup is a GET with cabinId/organizationId/unitId as path segments,
 * in that exact order (ctrl:220, `S+ id +S+ $scope.organizationId +S+
 * $scope.unitId`).
 *
 * Real backend duplicate-name check (CabinMasterServiceImpl.saveCabinMaster/
 * updateCabinMaster) returns transport-level HTTP 200 with a JSON body
 * `{status:'error', code:'409', message:'Already Exists.'}` -- the "409" is
 * a cosmetic field inside the payload, never a real HTTP status. No special
 * HTTP-error handling is needed here beyond the standard `res.status ===
 * 'error'` branch already used for every other error message on this screen.
 */
@Injectable({ providedIn: 'root' })
export class CabinMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, CabinRow>> {
    return this.http.post<ApiResponse<never, CabinRow>>(`${this.base}/api/global/unit/getCabinMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/unit/getCabinMasterCount`, {
      organizationId,
      unitId,
    });
  }

  save(payload: CabinSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/saveCabinMaster`, payload);
  }

  getById(cabinId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, CabinDetail>> {
    return this.http.get<ApiResponse<never, CabinDetail>>(
      `${this.base}/api/global/unit/getCabinMasterById/${cabinId}/${organizationId}/${unitId}`,
    );
  }

  update(payload: CabinUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateCabinMaster`, payload);
  }

  // Minimal payload -- see CabinStatusPayload doc comment for why this
  // deliberately does NOT replicate legacy's full-`$scope.common` POST.
  updateStatus(payload: CabinStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateCabinMasterStatus`, payload);
  }
}
