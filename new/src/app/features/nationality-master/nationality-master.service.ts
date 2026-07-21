import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CountryOption, NationalityRow } from './nationality-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js:43-47,61,396
 * (ADDNATIONALITY/GETNATIONALITYLIST/GETSINGLENATIONALITY/UPDATENATIONALITY/
 * UPDATENATIONALITYSTATUS/GETACTIVECOUNTRYLIST/COUNTNATIONALITYMASTER) and
 * confirmed against NationalityMasterController.java (`@RequestMapping("/adt")`)
 * -- not guessed. All calls audited as real, full CRUD with no dead
 * bindings on this screen's primary path.
 */
@Injectable({ providedIn: 'root' })
export class NationalityMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:80-93 -- GET, organizationId appended directly to the path (no slash,
  // matching the `GETACTIVECOUNTRYLIST + $scope.organizationId` string concat).
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  // ctrl:104-153 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getNationalityList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, NationalityRow>> {
    return this.http.post<ApiResponse<never, NationalityRow>>(`${this.base}/adt/getNationalityMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:112-152 -- POST { organizationId }, count returned in `object`.
  getNationalityCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getNationalityCount`, { organizationId });
  }

  // ctrl:279-308 -- POST { nationalityId }; response.data.listObject[0] is the record.
  getNationalityById(nationalityId: number): Observable<ApiResponse<never, NationalityRow>> {
    return this.http.post<ApiResponse<never, NationalityRow>>(`${this.base}/adt/getNationalityById`, { nationalityId });
  }

  // ctrl:216-276 -- POST, real create.
  saveNationality(payload: {
    nationalityCode: string;
    nationalityName: string;
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    createdDate: string;
    updatedDate: string;
    countryId: number | '';
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveNationalityMaster`, payload);
  }

  // ctrl:351-399 -- POST, real update. No `status`/created* fields sent,
  // matching legacy exactly.
  updateNationality(payload: {
    nationalityCode: string;
    nationalityName: string;
    nationalityId: number;
    countryId: number | '';
    updatedBy: number;
    updatedDate: string;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateNationalityMaster`, payload);
  }

  // ctrl:311-348 -- POST, real status toggle.
  updateStatus(nationalityId: number, status: 'A' | 'I', updatedBy: number, updatedDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForNationality`, {
      nationalityId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}
