import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CityDetail, CityRow, CountryOption, DistrictOption, StateOption } from './city-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js and
 * resources/assets/js/controller/master/cityMasterController.js -- not
 * guessed. All list/save/update/status calls are real POSTs (ctrl audit
 * confirmed no dead bindings on this screen).
 */
@Injectable({ providedIn: 'root' })
export class CityMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:72-86 -- GET, organizationId appended directly to the path (no slash).
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  // ctrl:202-222 -- POST { countryId }. No guard on a blank countryId in
  // legacy (ng-change fires even for the blank option) -- reproduced as-is,
  // see onCountryChange() in city-master.ts.
  getStatesByCountry(countryId: number | ''): Observable<ApiResponse<never, StateOption>> {
    return this.http.post<ApiResponse<never, StateOption>>(`${this.base}/adt/getActiveStateListByCountryId`, {
      countryId,
    });
  }

  // ctrl:224-244 -- POST { stateId }. Same no-guard behavior as above.
  getDistrictsByState(stateId: number | ''): Observable<ApiResponse<never, DistrictOption>> {
    return this.http.post<ApiResponse<never, DistrictOption>>(`${this.base}/adt/getActiveDistrictListByStateId`, {
      stateId,
    });
  }

  // ctrl:99-147 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getCityList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, CityRow>> {
    return this.http.post<ApiResponse<never, CityRow>>(`${this.base}/adt/getCityMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:132-146 -- POST { organizationId }, count returned in `object`.
  getCityCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getCityCount`, { organizationId });
  }

  // ctrl:318-358 -- POST { cityId }; response.data.listObject[0] is the record.
  getCityById(cityId: number): Observable<ApiResponse<never, CityDetail>> {
    return this.http.post<ApiResponse<never, CityDetail>>(`${this.base}/adt/getCityById`, { cityId });
  }

  // ctrl:253-315 -- POST, real create.
  saveCity(payload: {
    cityCode: string;
    cityName: string;
    districtId: number | '';
    stateId: number | '';
    countryId: number | '';
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    updatedDate: string;
    createdDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveCityMaster`, payload);
  }

  // ctrl:403-457 -- POST, real update. No `status`/created* fields sent,
  // matching legacy exactly.
  updateCity(payload: {
    cityCode: string;
    cityName: string;
    districtId: number | '';
    stateId: number | '';
    countryId: number | '';
    organizationId: number;
    cityId: number;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateCityMaster`, payload);
  }

  // ctrl:361-400 -- POST, real status toggle.
  updateCityStatus(cityId: number, status: 'A' | 'I', updatedBy: number, updatedDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForCity`, {
      cityId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}
