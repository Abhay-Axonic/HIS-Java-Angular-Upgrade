import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AreaOption,
  CityOption,
  CountryOption,
  DistrictOption,
  ReferralDetail,
  ReferralMasterRow,
  ReferralTypeOption,
  StateOption,
} from './referral-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js (constants referenced by
 * referralMasterController.js) -- backend is the Global Maven module's
 * `com.param.global.controller.ReferralMasterController`
 * (`@RequestMapping("/adt")`, despite the frontend living under the ADT
 * folder -- confirmed, not the ADT module) plus ADT module's
 * `ReferralTypeMasterController` for the referral-type dropdown. No golden
 * trace capture tool run was practical from this session (see class doc);
 * this endpoint list is the audited substitute signature.
 */
@Injectable({ providedIn: 'root' })
export class ReferralMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ---- init-time calls ----
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(
      `${this.base}/adt/getActiveCountryList/${organizationId}`,
    );
  }

  getActiveReferralTypeList(organizationId: number): Observable<ApiResponse<never, ReferralTypeOption>> {
    return this.http.post<ApiResponse<never, ReferralTypeOption>>(`${this.base}/adt/getActiveReferralTypeList`, {
      organizationId,
    });
  }

  getReferralList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ReferralMasterRow>> {
    return this.http.post<ApiResponse<never, ReferralMasterRow>>(`${this.base}/adt/getReferralMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getReferralCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getReferralCount`, { organizationId });
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
  saveReferral(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveReferralMaster`, payload);
  }

  getReferralById(referralId: number): Observable<ApiResponse<never, ReferralDetail>> {
    return this.http.post<ApiResponse<never, ReferralDetail>>(`${this.base}/adt/getReferralById`, { referralId });
  }

  updateReferral(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateReferralMaster`, payload);
  }

  updateStatus(payload: {
    referralId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForReferral`, payload);
  }
}
