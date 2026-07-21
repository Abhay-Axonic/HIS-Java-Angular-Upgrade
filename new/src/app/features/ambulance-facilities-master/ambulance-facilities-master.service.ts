import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AmbulanceFacilityDetail,
  AmbulanceFacilityRow,
  AmbulanceFacilitySavePayload,
  AmbulanceFacilityStatusPayload,
  AmbulanceFacilityUpdatePayload,
} from './ambulance-facilities-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js:84-89 and
 * resources/assets/js/controller/lis/global/ambulanceFacilitiesController.js
 * -- not guessed. Legacy dispatches every one of these through
 * `GenericService.serviceAction("POST", URI, data)` regardless of the
 * semantic operation (list/get/create/update/status-change all POST) --
 * reproduced verbatim here rather than "upgrading" any of them to
 * GET/PUT/PATCH, matching the convention already used by
 * ambulance-type-master.service.ts / ambulance-master.service.ts for this
 * exact sibling module.
 *
 * `getList`'s response carries BOTH the page of rows (`listObject`) AND the
 * total row count (`object`) in a single call, for both the "reset to page
 * 1" fetch (ctrl:63-93, `initAmbulanceFacilitiesMasterList`) and the
 * "navigate to an arbitrary page" fetch (ctrl:96-124,
 * `initAmbulanceFacilitiesMasterList1`) -- both legacy functions hit this
 * exact same URI/shape, they only differ in whether the controller resets
 * the pager back to page 1 afterward. One service method covers both call
 * sites here; see the component for how the two call sites are modeled.
 */
@Injectable({ providedIn: 'root' })
export class AmbulanceFacilitiesMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<number, AmbulanceFacilityRow>> {
    return this.http.post<ApiResponse<number, AmbulanceFacilityRow>>(`${this.base}/adt/getAmbulanceFacilitiesList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getSingle(ambulanceFacilitiesMasterId: number, organizationId: number): Observable<ApiResponse<never, AmbulanceFacilityDetail>> {
    return this.http.post<ApiResponse<never, AmbulanceFacilityDetail>>(`${this.base}/adt/getSingleAmbulanceFacilities`, {
      ambulanceFacilitiesMasterId,
      organizationId,
    });
  }

  save(payload: AmbulanceFacilitySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveAmbulanceFacilities`, payload);
  }

  update(payload: AmbulanceFacilityUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateAmbulanceFacilities`, payload);
  }

  updateStatus(payload: AmbulanceFacilityStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/changeAmbulanceFacilitiesStatus`, payload);
  }
}
