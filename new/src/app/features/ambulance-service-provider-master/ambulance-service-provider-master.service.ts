import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AmbulanceTariffDetail,
  AmbulanceTariffOption,
  AreaOption,
  CityOption,
  CountryOption,
  DistrictOption,
  ServiceProviderDetail,
  ServiceProviderRow,
  ServiceProviderSavePayload,
  ServiceProviderStatusPayload,
  ServiceProviderUpdatePayload,
  StateOption,
} from './ambulance-service-provider-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/lis/global/ambulanceServiceProviderController.js
 * and resources/assets/js/config/adt.url.config.js:61-96/131-157 -- not
 * guessed. Every geo/tariff lookup and every CRUD call in the legacy
 * controller goes through `GenericService.serviceAction(...)`; only the
 * initial country list uses a literal `"GET"` method
 * (ctrl:73-82, `GETACTIVECOUNTRYLIST` has the organizationId appended
 * directly onto the URL path, `adt.url.config.js:61`), everything else is
 * POST -- reproduced with that exact same GET/POST split, not "upgraded"
 * uniformly.
 */
@Injectable({ providedIn: 'root' })
export class AmbulanceServiceProviderMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:73-82 -- GET, organizationId appended as the final path segment.
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  // ctrl:84-98 -- "Ambulance Tariff Name" dropdown.
  getAmbulanceTariffActiveList(organizationId: number): Observable<ApiResponse<never, AmbulanceTariffOption>> {
    return this.http.post<ApiResponse<never, AmbulanceTariffOption>>(`${this.base}/adt/getAmbulanceTarrifActiveList`, {
      organizationId,
    });
  }

  // ctrl:99-110 -- the UNFILTERED full state master list, fetched
  // unconditionally on init (before any country is ever chosen). See the
  // component's class doc comment for the real "unfiltered until the user
  // interacts" quirk this causes.
  getStateMasterList(organizationId: number): Observable<ApiResponse<never, StateOption>> {
    return this.http.post<ApiResponse<never, StateOption>>(`${this.base}/adt/getStateMasterList`, { organizationId });
  }

  // ctrl:111-123 -- unfiltered, same quirk as getStateMasterList.
  getDistrictMasterList(organizationId: number): Observable<ApiResponse<never, DistrictOption>> {
    return this.http.post<ApiResponse<never, DistrictOption>>(`${this.base}/adt/getDistrictMasterList`, {
      organizationId,
    });
  }

  // ctrl:124-135 -- unfiltered, same quirk as getStateMasterList.
  getCityMasterList(organizationId: number): Observable<ApiResponse<never, CityOption>> {
    return this.http.post<ApiResponse<never, CityOption>>(`${this.base}/adt/getCityMasterList`, { organizationId });
  }

  // ctrl:140-158 -- fired by the Country select's ng-change.
  getStateListByCountryId(countryId: string): Observable<ApiResponse<never, StateOption>> {
    return this.http.post<ApiResponse<never, StateOption>>(`${this.base}/adt/getActiveStateListByCountryId`, {
      countryId,
    });
  }

  // ctrl:161-180 -- fired by the State select's ng-change.
  getDistrictListByStateId(stateId: number): Observable<ApiResponse<never, DistrictOption>> {
    return this.http.post<ApiResponse<never, DistrictOption>>(`${this.base}/adt/getActiveDistrictListByStateId`, {
      stateId,
    });
  }

  // ctrl:183-203 -- fired by the District select's ng-change.
  getCityListByDistrictId(districtId: number): Observable<ApiResponse<never, CityOption>> {
    return this.http.post<ApiResponse<never, CityOption>>(`${this.base}/adt/getActiveCityListByDistrictId`, {
      districtId,
    });
  }

  // ctrl:206-227 -- fired by the City select's ng-change. No unfiltered
  // preload for Area anywhere in init() -- this is the ONLY way this list
  // is ever populated, so it starts genuinely empty.
  getAreaByCityId(cityId: number): Observable<ApiResponse<never, AreaOption>> {
    return this.http.post<ApiResponse<never, AreaOption>>(`${this.base}/adt/getAreaByCityId`, { cityId });
  }

  // ctrl:229-249 -- fired by the Ambulance Tariff Name select's ng-change,
  // auto-fills 5 form fields on success. Gated on `status === 'success'` in
  // legacy -- reproduced with that same gate (see the component).
  getSingleAmbulanceTarrif(
    ambulanceTarrifMasterId: string,
    organizationId: number,
  ): Observable<ApiResponse<never, AmbulanceTariffDetail>> {
    return this.http.post<ApiResponse<never, AmbulanceTariffDetail>>(`${this.base}/adt/getSingleAmbulanceTarrif`, {
      ambulanceTarrifMasterId,
      organizationId,
    });
  }

  // ctrl:260-288 -- ALWAYS offset 0 / 10 records; see the component's class
  // doc comment for the confirmed-dead pagination this reproduces (no
  // count call exists anywhere in this controller).
  getServiceProviderList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ServiceProviderRow>> {
    return this.http.post<ApiResponse<never, ServiceProviderRow>>(`${this.base}/adt/getServiceProviderList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:300-378 -- real create.
  save(payload: ServiceProviderSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveServiceProvider`, payload);
  }

  // ctrl:428-471.
  getSingle(
    serviceProviderMasterId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, ServiceProviderDetail>> {
    return this.http.post<ApiResponse<never, ServiceProviderDetail>>(`${this.base}/adt/getSingleServiceProvider`, {
      serviceProviderMasterId,
      organizationId,
    });
  }

  // ctrl:515-577 -- real update, no confirm modal (unlike save()).
  update(payload: ServiceProviderUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateServiceProvider`, payload);
  }

  // ctrl:474-512 -- real status toggle.
  updateStatus(payload: ServiceProviderStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/changeServiceProviderStatus`, payload);
  }
}
