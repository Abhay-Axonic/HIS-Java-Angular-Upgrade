import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AreaOption,
  CampMasterRow,
  CityOption,
  CountryOption,
  DistrictOption,
  PatientTypeOption,
  StateOption,
} from './camp-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js (GETACTIVECOUNTRYLIST/
 * GETSTATELISTBYCOUNTRYID/GETDISTRICTLISTBYSTATEID/GETCITYLISTBYDISTRICTID/
 * GETAREALISTBYCITY -- the exact same constants referralMasterController.js
 * uses, all resolving to the Global module's `/adt/...` geo-cascade
 * endpoints) plus the CampMaster-specific endpoints on
 * legacy/BloodBank/src/main/java/com/param/bloodBank/controller/
 * CampMasterController.java (`@RequestMapping("/global/bloodBank")`) and
 * the optional patient-type dropdown's own
 * `/api/billing/getPatientType/{orgId}/{unitId}` (ctrl:166-184).
 *
 * Golden-trace capture (Step 2, `tools/golden-trace/capture.js`, run this
 * session against `{"name":"campMaster","url":"/campMaster"}`) confirms
 * exactly 4 calls fire on load, matching the audited controller's
 * synchronous init flow call-for-call:
 *   1. POST /global/bloodBank/getCampMasterListPaginationList (ctrl:64-103, initMasterList)
 *   2. POST /global/bloodBank/getCampMasterListCount            (ctrl:64-103, initMasterList)
 *   3. GET  /api/billing/getPatientType/1/1                     (ctrl:166-184, getActivePatientTypeList)
 *   4. GET  /adt/getActiveCountryList/1                         (ctrl:186-195)
 * No state/district/city/area calls fire on load -- those only fire on a
 * user's own `ng-change`/on `edit()`, confirmed absent from the trace, same
 * as referralMaster. See camp-master.ts's class doc for the full Step 4
 * comparison.
 */
@Injectable({ providedIn: 'root' })
export class CampMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ---- init-time calls ----
  getCampList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, CampMasterRow>> {
    return this.http.post<ApiResponse<never, CampMasterRow>>(
      `${this.base}/global/bloodBank/getCampMasterListPaginationList`,
      { organizationId, offset, noOfRecordsPerPage, unitId },
    );
  }

  getCampCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/global/bloodBank/getCampMasterListCount`, {
      organizationId,
      unitId,
    });
  }

  getActivePatientTypeList(organizationId: number, unitId: number): Observable<ApiResponse<never, PatientTypeOption>> {
    return this.http.get<ApiResponse<never, PatientTypeOption>>(
      `${this.base}/api/billing/getPatientType/${organizationId}/${unitId}`,
    );
  }

  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  // ---- geo cascade ----
  getStateByCountry(countryId: number): Observable<ApiResponse<never, StateOption>> {
    return this.http.post<ApiResponse<never, StateOption>>(`${this.base}/adt/getActiveStateListByCountryId`, {
      countryId,
    });
  }

  getDistrictByState(stateId: number): Observable<ApiResponse<never, DistrictOption>> {
    return this.http.post<ApiResponse<never, DistrictOption>>(`${this.base}/adt/getActiveDistrictListByStateId`, {
      stateId,
    });
  }

  getCityByDistrict(districtId: number): Observable<ApiResponse<never, CityOption>> {
    return this.http.post<ApiResponse<never, CityOption>>(`${this.base}/adt/getActiveCityListByDistrictId`, {
      districtId,
    });
  }

  getAreaByCity(cityId: number): Observable<ApiResponse<never, AreaOption>> {
    return this.http.post<ApiResponse<never, AreaOption>>(`${this.base}/adt/getAreaByCityId`, { cityId });
  }

  // ---- CRUD ----
  saveCamp(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/saveCampMaster`, payload);
  }

  updateCamp(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateCampMaster`, payload);
  }

  updateStatus(payload: {
    campId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
    unitId: number;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateCampMasterStatus`, payload);
  }
}
