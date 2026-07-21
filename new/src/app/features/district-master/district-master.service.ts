import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CountryOption,
  DistrictDetail,
  DistrictRow,
  DistrictSavePayload,
  DistrictStatusPayload,
  DistrictUpdatePayload,
  StateOption,
} from './district-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/districtMasterController.js and
 * resources/assets/js/config/adt.url.config.js:61/136-142/406 -- not
 * guessed. BASE_URL/ROOT_URL are both "" (confirmed live via the running
 * app's own `window.BASE_URL`/`window.ROOT_URL`), so these are the literal
 * `/adt/...` relative paths legacy calls (no `/api` prefix), matching this
 * codebase's other `/adt` master rebuilds (amenity-master,
 * ambulance-service-provider-master). Only the country list uses a literal
 * `"GET"` with the organizationId appended to the path
 * (ctrl:75-89, `GETACTIVECOUNTRYLIST` ends in a trailing slash in
 * `adt.url.config.js:61`); everything else is POST -- reproduced with that
 * exact split.
 *
 * **Confirmed live against the real backend this session (2026-07-21)**,
 * not assumed: `getDistrictMasterList`/`getDistrictCount` both returned a
 * real HTTP 200 with `status:"success"` via the app's own `GenericService`
 * from an authenticated session (1 real district row present in this dev
 * DB) -- this screen has a genuinely working backend, unlike
 * `modalityTypeMaster`/the homecare cluster (entries 67/77).
 */
@Injectable({ providedIn: 'root' })
export class DistrictMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:75-89 -- GET, organizationId appended as the final path segment.
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  // ctrl:214-234 -- fired by the Country select's ng-change, and again
  // (ctrl:333-334) inside showUpdateBtn's success handler to cascade the
  // State list before the fetched record's stateId is applied.
  getStateListByCountryId(countryId: string): Observable<ApiResponse<never, StateOption>> {
    return this.http.post<ApiResponse<never, StateOption>>(`${this.base}/adt/getActiveStateListByCountryId`, {
      countryId,
    });
  }

  // ctrl:110-141 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getDistrictMasterList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DistrictRow>> {
    return this.http.post<ApiResponse<never, DistrictRow>>(`${this.base}/adt/getDistrictMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:143-157 -- POST { organizationId }, count returned in `object`.
  getDistrictCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getDistrictCount`, { organizationId });
  }

  // ctrl:243-305 -- real create, gated behind the confirm modal.
  saveDistrictMaster(payload: DistrictSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveDistrictMaster`, payload);
  }

  // ctrl:308-342 -- POST { districtId, organizationId };
  // response.data.listObject[0] is the record.
  getDistrictById(districtId: number, organizationId: number): Observable<ApiResponse<never, DistrictDetail>> {
    return this.http.post<ApiResponse<never, DistrictDetail>>(`${this.base}/adt/getDistrictById`, {
      districtId,
      organizationId,
    });
  }

  // ctrl:385-438 -- real update, no confirm modal (unlike save).
  updateDistrictMaster(payload: DistrictUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateDistrictMaster`, payload);
  }

  // ctrl:345-382 -- real status toggle.
  updateStatusForDistrict(payload: DistrictStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForDistrict`, payload);
  }
}
