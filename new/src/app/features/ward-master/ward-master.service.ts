import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  FloorOption,
  GenderListItem,
  IcuTypeOption,
  WardMasterRow,
  WardMasterSavePayload,
  WardMasterUpdatePayload,
} from './ward-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/unit/wardMasterController.js and
 * resources/assets/js/config/adt.url.config.js -- not guessed. All calls
 * audited as real (no dead bindings on this screen): see the class comment
 * in ward-master.ts.
 */
@Injectable({ providedIn: 'root' })
export class WardMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:68-84 -- POST { unitId, organizationId }.
  getActiveFloorList(unitId: number, organizationId: number): Observable<ApiResponse<never, FloorOption>> {
    return this.http.post<ApiResponse<never, FloorOption>>(`${this.base}/adt/getActiveFloorList`, {
      unitId,
      organizationId,
    });
  }

  // ctrl:86-106 -- POST { organizationId, unitId }. Legacy inlines this URL
  // as a literal string (`'/adt/getActiveICUTypeList'`) rather than a named
  // *.url.config.js constant -- kept verbatim.
  getActiveIcuTypeList(organizationId: number, unitId: number): Observable<ApiResponse<never, IcuTypeOption>> {
    return this.http.post<ApiResponse<never, IcuTypeOption>>(`${this.base}/adt/getActiveICUTypeList`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:108-130 -- GET, no params (legacy builds an empty `data` object but
  // GenericService issues a GET, so it's never sent).
  getActiveGenderList(): Observable<ApiResponse<never, GenderListItem>> {
    return this.http.get<ApiResponse<never, GenderListItem>>(`${this.base}/adt/getActiveGenderList`);
  }

  // ctrl:159-197/216-244 -- POST { organizationId, unitId, offset, noOfRecordsPerPage }.
  getWardMasterList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, WardMasterRow>> {
    return this.http.post<ApiResponse<never, WardMasterRow>>(`${this.base}/api/adt/getWardMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:199-213 -- POST { unitId, organizationId }, count returned in `object`.
  getWardMasterCount(unitId: number, organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/adt/getWardMasterCount`, {
      unitId,
      organizationId,
    });
  }

  // ctrl:377-410 -- GET, wardId/organizationId/unitId as path segments;
  // response.data.listObject[0] is the record.
  getWardMasterById(
    wardId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, WardMasterRow>> {
    return this.http.get<ApiResponse<never, WardMasterRow>>(
      `${this.base}/api/adt/getWardMasterById/${wardId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:276-374 -- POST, real create.
  saveWardMaster(payload: WardMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/saveWardMasterMaster`, payload);
  }

  // ctrl:455-541 -- POST, real update (no confirm-modal step, unlike create).
  updateWardMaster(payload: WardMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/updateWardMaster`, payload);
  }

  // ctrl:415-452 -- POST, real status toggle.
  updateWardMasterStatus(
    wardId: number,
    status: 'A' | 'I',
    updatedBy: number,
    updatedDate: string,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/updateWardMasterStatus`, {
      wardId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}
