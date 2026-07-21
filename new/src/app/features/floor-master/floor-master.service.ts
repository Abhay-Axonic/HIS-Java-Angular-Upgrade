import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  FloorMasterDetail,
  FloorMasterRow,
  FloorMasterSavePayload,
  FloorMasterStatusPayload,
  FloorMasterUpdatePayload,
} from './floor-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/floorMasterController.js and
 * their literal string values in
 * resources/assets/js/config/adt.url.config.js:600-605:
 *   GETFLOORMASTERLIST   = /adt/getFloorMasterList   (ctrl:85-88)
 *   SAVEFLOORMASTER      = /adt/saveFloorMaster      (ctrl:222-223)
 *   GETFLOORCOUNT        = /adt/getFloorCount        (ctrl:87-88)
 *   UPDATESTATUSFORFLOOR = /adt/updateStatusForFloor (ctrl:298-299)
 *   GETFLOORBYID         = /adt/getFloorById         (ctrl:266-267)
 *   UPDATEFLOORMASTER    = /adt/updateFloorMaster    (ctrl:339-340)
 * `BASE_URL`/`ROOT_URL` (config/url.config.js:13,19) are both `''` in this
 * deployment, so these are the complete paths -- no `/api` prefix (this
 * screen's own Java controller, FloorMasterController.java, is mapped at
 * plain `/adt`, unlike some sibling masters whose service layer sits behind
 * `/api/...`). ALL SIX calls are POST in legacy
 * (`GenericService.serviceAction("POST", ...)`), including the by-id lookup
 * -- unlike ward-master/qualification-master's by-id lookups, which are GET.
 */
@Injectable({ providedIn: 'root' })
export class FloorMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:79-117 -- POST { organizationId, unitId, offset, noOfRecordsPerPage }.
  getFloorMasterList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, FloorMasterRow>> {
    return this.http.post<ApiResponse<never, FloorMasterRow>>(`${this.base}/adt/getFloorMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:98-101/119-133 -- POST { organizationId, unitId }, count returned in `object`.
  getFloorCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getFloorCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:197-254 -- POST, real create.
  saveFloorMaster(payload: FloorMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveFloorMaster`, payload);
  }

  // ctrl:263-267 -- POST { floorId }, response.data.listObject[0] is the record.
  getFloorById(floorId: number): Observable<ApiResponse<never, FloorMasterDetail>> {
    return this.http.post<ApiResponse<never, FloorMasterDetail>>(`${this.base}/adt/getFloorById`, { floorId });
  }

  // ctrl:325-371 -- POST, real update (no confirm-modal step, unlike create).
  updateFloorMaster(payload: FloorMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateFloorMaster`, payload);
  }

  // ctrl:285-322 -- POST, real status toggle.
  updateStatusForFloor(payload: FloorMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForFloor`, payload);
  }
}
