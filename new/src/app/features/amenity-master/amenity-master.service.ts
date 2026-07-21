import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AmenityDetail,
  AmenityMasterSavePayload,
  AmenityMasterStatusPayload,
  AmenityMasterUpdatePayload,
  AmenityRow,
} from './amenity-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/unit/amenityMasterController.js -- not
 * guessed. BASE_URL/ROOT_URL are both "" (resources/assets/js/config/url.config.js),
 * so these are the literal relative paths legacy calls, all under `/adt`
 * (no `/api` prefix -- matching the majority of this codebase's other `/adt`
 * unit-master rebuilds, e.g. ambulance-type-master, bed-type-master,
 * city-master). moduleId 539 (app.js:8770-8792 resolve block, confirmed live).
 */
@Injectable({ providedIn: 'root' })
export class AmenityMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:57-96 -- POST { organizationId, offset, noOfRecordsPerPage, unitId }.
  getAmenityMasterList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, AmenityRow>> {
    return this.http.post<ApiResponse<never, AmenityRow>>(`${this.base}/adt/getAmenityMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:70-94 -- POST { organizationId, unitId }, count returned in `object`.
  getAmenityMasterListCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getAmenityMasterListCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:143-196 -- POST, real create.
  saveAmenityMaster(payload: AmenityMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveAmenityMaster`, payload);
  }

  // ctrl:198-226 -- GET, amenityId as the only path segment;
  // response.data.listObject[0] is the record.
  getAmenityMasterListById(amenityId: number): Observable<ApiResponse<never, AmenityDetail>> {
    return this.http.get<ApiResponse<never, AmenityDetail>>(
      `${this.base}/adt/getAmenityMasterListById/${amenityId}`,
    );
  }

  // ctrl:229-278 -- POST, real update (no confirm-modal step, unlike create).
  updateAmenityMaster(payload: AmenityMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateAmenityMaster`, payload);
  }

  // ctrl:280-314 -- POST, real status toggle.
  updateAmenityMasterStatus(payload: AmenityMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateAmenityMasterStatus`, payload);
  }
}
