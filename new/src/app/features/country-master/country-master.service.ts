import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CountryDetail, CountryRow, CountrySavePayload, CountryUpdatePayload } from './country-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js:
 *   ADDCOUNTRY            = /adt/saveCountryMaster        (ctrl:232-233)
 *   GETCOUNTRYLIST        = /adt/getCountryMasterList      (ctrl:98-99, 143)
 *   GETSINGLECOUNTRY      = /adt/getCountryById             (ctrl:283-284)
 *   UPDATECOUNTRY         = /adt/updateCountryMaster        (ctrl:375-376)
 *   UPDATECOUNTRYSTATUS   = /adt/updateStatusForCountry     (ctrl:321-322)
 *   COUNTCOUNTRYMASTER    = /adt/getCountryCount            (ctrl:99-100)
 * `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this deployment, so
 * these are the complete paths, not guessed prefixes. All six calls are POST
 * in legacy (`GenericService.serviceAction("POST", ...)`), including the two
 * "get" endpoints -- kept as POST here for parity rather than "corrected" to GET.
 */
@Injectable({ providedIn: 'root' })
export class CountryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, CountryRow>> {
    return this.http.post<ApiResponse<never, CountryRow>>(`${this.base}/adt/getCountryMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getCountryCount`, { organizationId });
  }

  getById(countryId: number, organizationId: number): Observable<ApiResponse<never, CountryDetail>> {
    return this.http.post<ApiResponse<never, CountryDetail>>(`${this.base}/adt/getCountryById`, {
      countryId,
      organizationId,
    });
  }

  create(payload: CountrySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveCountryMaster`, payload);
  }

  update(payload: CountryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateCountryMaster`, payload);
  }

  updateStatus(
    countryId: number,
    status: 'A' | 'I',
    updatedBy: number,
    updatedDate: string,
    organizationId: number,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForCountry`, {
      countryId,
      status,
      updatedBy,
      updatedDate,
      organizationId,
    });
  }
}
