import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  RackMasterDetail,
  RackMasterRow,
  RackMasterSavePayload,
  RackMasterStatusPayload,
  RackMasterUpdatePayload,
} from './rack-masters.models';

/**
 * Endpoint paths taken directly from MrdRackMasterController.java (Global
 * module, mapped under `/mrdrack`) and rackMastersController.js -- not
 * guessed. All 5 endpoints called by that controller are wired here.
 * `getActiveMrdRacks/{orgId}/{unitId}` exists on the Java controller but has
 * no caller anywhere in rackMastersController.js/rack-master.html, so it is
 * intentionally not modeled -- it belongs to a different screen's dropdown,
 * not this one (same situation row-masters.service.ts documents for its own
 * sibling `getActiveMrdRows` endpoint).
 *
 * `getMrdRackList` is the ONLY list-fetch endpoint on this screen -- unlike
 * some sibling masters, there is no separate "count" endpoint: the Java
 * service (`MrdRackMasterServiceImpl.getRackList`) returns BOTH the page of
 * rows (as `listObject`) AND the total count (as `object`, a `BigInteger`)
 * in a single `Response` from one call. Confirmed by reading
 * `MrdRackMasterServiceImpl.java` directly, not assumed from the request
 * shape alone.
 */
@Injectable({ providedIn: 'root' })
export class RackMastersService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:83-101/109-126 -- POST { unitId, organizationId, page, size }.
  // `page` is actually a 0-based record OFFSET (Hibernate's
  // `setFirstResult`), not a page number -- same "page"-named-offset
  // convention row-masters' `getMrdRowList` uses. `object` carries the
  // total record count in the SAME response as the list.
  getMrdRackList(
    unitId: number,
    organizationId: number,
    page: number,
    size: number,
  ): Observable<ApiResponse<number, RackMasterRow>> {
    return this.http.post<ApiResponse<number, RackMasterRow>>(`${this.base}/mrdrack/getMrdRackList`, {
      unitId,
      organizationId,
      page,
      size,
    });
  }

  // ctrl:31-80 -- POST, real create.
  saveMrdRack(payload: RackMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/mrdrack/saveMrdRack`, payload);
  }

  // ctrl:168-188 -- GET, rackId/organizationId/unitId as path segments;
  // response.data.object (NOT listObject) is the single record.
  getMrdRackById(rackId: number, organizationId: number, unitId: number): Observable<ApiResponse<RackMasterDetail>> {
    return this.http.get<ApiResponse<RackMasterDetail>>(
      `${this.base}/mrdrack/getMrdRackById/${rackId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:190-217 -- PUT, full row object per the Java DTO's actual required shape.
  updateMrdRack(payload: RackMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/mrdrack/updateMrdRack`, payload);
  }

  // ctrl:219-243 -- PUT, also the full row object (same DTO shape as
  // updateMrdRack), not a minimal { rackId, status } payload.
  updateMrdRackStatus(payload: RackMasterStatusPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/mrdrack/updateMrdRackStatus`, payload);
  }
}
