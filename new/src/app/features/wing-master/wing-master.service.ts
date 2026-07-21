import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  WingMasterDetail,
  WingMasterRow,
  WingMasterSavePayload,
  WingMasterStatusPayload,
  WingMasterUpdatePayload,
} from './wing-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/unit/wingMasterController.js and
 * WingMasterController.java (`@RequestMapping(value="/adt")`) -- not
 * guessed. `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this
 * deployment, so these are the complete same-origin paths (plain `/adt/...`,
 * matching the majority of this codebase's other `/adt`-namespace screens --
 * e.g. city-master, bed-category-master, country-master -- rather than the
 * `/api/adt/...` prefix ward-master.service.ts happens to use for its own
 * CRUD calls).
 *  - GETWINGMASTERLIST       = /adt/getWingMasterList       (POST, ctrl:56-95)
 *  - GETWINGMASTERLISTCOUNT  = /adt/getWingMasterListCount  (POST, ctrl:69-93)
 *  - SAVEWINGMASTER          = /adt/saveWingMaster          (POST, ctrl:142-195)
 *  - GETWINGMASTERLISTBYID   = /adt/getWingMasterListById/{wingId} (GET, ctrl:197-225)
 *  - UPDATEWINGMASTER        = /adt/updateWingMaster        (POST, ctrl:228-277)
 *  - UPDATEWINGMASTERSTATUS  = /adt/updateWingMasterStatus  (POST, ctrl:279-313)
 * List/count/save/update/status calls are all POST in legacy
 * (`GenericService.serviceAction("POST", ...)`); only the by-id lookup is a
 * GET with the id as a path segment.
 */
@Injectable({ providedIn: 'root' })
export class WingMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, WingMasterRow>> {
    return this.http.post<ApiResponse<never, WingMasterRow>>(`${this.base}/adt/getWingMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getWingMasterListCount`, {
      organizationId,
      unitId,
    });
  }

  save(payload: WingMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveWingMaster`, payload);
  }

  /** GET with `wingId` as a path segment (ctrl:204), matching legacy's own
   * `GenericService.serviceAction("GET", URI)` call with no body. */
  getById(wingId: number): Observable<ApiResponse<never, WingMasterDetail>> {
    return this.http.get<ApiResponse<never, WingMasterDetail>>(
      `${this.base}/adt/getWingMasterListById/${wingId}`,
    );
  }

  update(payload: WingMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateWingMaster`, payload);
  }

  updateStatus(payload: WingMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateWingMasterStatus`, payload);
  }
}
