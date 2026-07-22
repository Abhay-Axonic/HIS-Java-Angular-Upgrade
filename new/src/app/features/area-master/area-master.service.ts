import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AreaDetail, AreaRow, CityOption, CountryOption, DistrictOption, StateOption } from './area-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js and
 * resources/assets/js/controller/master/areaMasterController.js -- not
 * guessed. Controller/Service/DAO chain confirmed real end-to-end:
 * AreaMasterController.java -> AreaMasterServiceImpl.java ->
 * AreaMasterDaoImpl.java (all 6 area-specific endpoints); the 3 cascading
 * lookups above City live in sibling controllers by design (each level's
 * "list children of X" lives in the CHILD master's own controller, e.g.
 * getActiveStateListByCountryId is served by DistrictMasterController, not
 * CountryMasterController/StateMasterController) -- confirmed via direct
 * @RequestMapping reads, same structure already used by city-master.
 */
@Injectable({ providedIn: 'root' })
export class AreaMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:74-88 -- GET, organizationId appended directly to the path (no slash).
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  // ctrl:204-224 -- POST { countryId }. No guard on a blank countryId in
  // legacy (ng-change fires even for the blank option), and the downstream
  // state/district/city lists/selections are never cleared on a parent
  // change -- reproduced as-is (same quirk already documented for
  // city-master's onCountryChange/onStateChange).
  getStatesByCountry(countryId: number | ''): Observable<ApiResponse<never, StateOption>> {
    return this.http.post<ApiResponse<never, StateOption>>(`${this.base}/adt/getActiveStateListByCountryId`, { countryId });
  }

  // ctrl:226-246 -- POST { stateId }. Same no-guard/no-clear behavior as above.
  getDistrictsByState(stateId: number | ''): Observable<ApiResponse<never, DistrictOption>> {
    return this.http.post<ApiResponse<never, DistrictOption>>(`${this.base}/adt/getActiveDistrictListByStateId`, { stateId });
  }

  // ctrl:248-269 -- POST { districtId }. Same no-guard/no-clear behavior.
  getCitiesByDistrict(districtId: number | ''): Observable<ApiResponse<never, CityOption>> {
    return this.http.post<ApiResponse<never, CityOption>>(`${this.base}/adt/getActiveCityListByDistrictId`, { districtId });
  }

  // ctrl:101-149 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getAreaList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, AreaRow>> {
    return this.http.post<ApiResponse<never, AreaRow>>(`${this.base}/adt/getAreaMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:134-148 -- POST { organizationId }, count returned in `object`.
  getAreaCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getAreaCount`, { organizationId });
  }

  // ctrl:344-386 -- POST { areaId }; response.data.listObject[0] is the record.
  getAreaById(areaId: number): Observable<ApiResponse<never, AreaDetail>> {
    return this.http.post<ApiResponse<never, AreaDetail>>(`${this.base}/adt/getAreaById`, { areaId });
  }

  // ctrl:278-341 -- POST, real create.
  saveArea(payload: {
    areaCode: string;
    areaName: string;
    districtId: number | '';
    stateId: number | '';
    countryId: number | '';
    cityId: number | '';
    postCode: string;
    status: 'A';
    updatedBy: number;
    createdBy: number;
    organizationId: number;
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveAreaMaster`, payload);
  }

  // ctrl:430-487 -- POST, real update. Includes `cityId` (ctrl:445) -- see
  // the correction in area-master.ts's class-level comment; an earlier
  // draft wrongly omitted this field based on a misread of legacy source.
  updateArea(payload: {
    areaCode: string;
    areaName: string;
    districtId: number | '';
    stateId: number | '';
    countryId: number | '';
    cityId: number | '';
    areaId: number;
    postCode: string;
    status: 'A';
    updatedBy: number;
    organizationId: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateAreaMaster`, payload);
  }

  // ctrl:389-427 -- POST, real status toggle.
  updateAreaStatus(areaId: number, status: 'A' | 'I', updatedBy: number, updatedDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForArea`, {
      areaId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}
