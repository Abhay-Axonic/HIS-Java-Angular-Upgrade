import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AreaSearchResult,
  GeoOption,
  HospitalMasterRecord,
  HospitalMasterRow,
  ManualSubscriptionForm,
  OrganizationOption,
  SubscriptionConfig,
  SubscriptionHistoryRow,
  SubscriptionTypeOption,
  TimezoneOption,
} from './hospital-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/hospitalMasterController.js.
 * Full Controller -> Service -> DAO chain confirmed for every endpoint
 * below -- `GlobalUnitMasterController` (Global module, no class-level
 * `@RequestMapping`, so paths below match the JS constants verbatim since
 * legacy's `BASE_URL`/`ROOT_URL` are both `""`) for everything under
 * `/unit/*`/`/timezones`; `UnitServiceMapperController`
 * (`@RequestMapping("/global")`) for `getSuperAdminUser`/
 * `serviceAreaBysearchKeyword`; `LiceneceTypeMasterController`
 * (`@RequestMapping("/org")`) for `getLiceneceTypeMasterList`;
 * `CountryMasterController`/`StateMasterController`/`CityMasterController`/
 * `AreaMasterController` (all `@RequestMapping("/adt")`) for the geo
 * cascade; `OrganizationMasterController` (no class-level mapping) for
 * `/organization/active`. Golden-trace-confirmed
 * (tools/golden-trace/golden-traces/hospitalMaster.json): all 10
 * screen-specific initial-load calls match source exactly.
 */
@Injectable({ providedIn: 'root' })
export class HospitalMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ---- reference data (all fired unconditionally on load, ctrl:97/174/195/234/252/272) ----

  /** Static asset (ctrl:165-169, `$http.get('resources/assets/adminPriviledges.json')`)
   * -- the full system module tree, JSON-stringified into
   * `unit.previlageString` on every blank/new record. NOT cosmetic: the
   * backend actually consumes it (`GlobalUnitMasterServiceImpl.java:259`,
   * `p.setModules(unitMasterDto.getPrevilageString())`) to seed a newly
   * created hospital's default module privileges -- omitting this would be
   * a real functional gap (new hospitals created via the rebuild would get
   * no default module privileges), not just a network-parity nicety.
   * Bundled at new/public/assets/adminPriviledges.json (same content,
   * copied verbatim from the legacy static file). */
  getAdminPrivileges(): Observable<unknown> {
    return this.http.get<unknown>('assets/adminPriviledges.json');
  }

  /** Fetched unconditionally on load (ctrl:177-195) even though the
   * licence-details sub-feature it feeds is entirely commented out in the
   * view (both the add/edit form, view:399-440, and the view-trigger icon,
   * view:550) -- unreachable UI, same treatment as
   * organization-master.service.ts's identical call. Preserved here purely
   * for network-level parity with the golden trace. */
  getLicenceTypeList(organizationId: number): Observable<ApiResponse<never, unknown>> {
    return this.http.get<ApiResponse<never, unknown>>(`${this.base}/org/getLiceneceTypeMasterList/${organizationId}`);
  }

  getSuperAdminUser(organizationId: number, unitId: number, userId: number): Observable<ApiResponse<{ isSuperUser: string }>> {
    return this.http.get<ApiResponse<{ isSuperUser: string }>>(
      `${this.base}/global/getSuperAdminUser/${organizationId}/${unitId}/${userId}`,
    );
  }

  getTimezones(): Observable<ApiResponse<never, TimezoneOption>> {
    return this.http.get<ApiResponse<never, TimezoneOption>>(`${this.base}/timezones`);
  }

  getCountryList(organizationId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.get<ApiResponse<never, GeoOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  getStatesByCountry(countryId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.get<ApiResponse<never, GeoOption>>(`${this.base}/adt/getStateByCountryId/${countryId}`);
  }

  getDistrictsByState(stateId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.post<ApiResponse<never, GeoOption>>(`${this.base}/adt/getActiveDistrictListByStateId`, { stateId });
  }

  getCitiesByDistrict(districtId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.post<ApiResponse<never, GeoOption>>(`${this.base}/adt/getActiveCityListByDistrictId`, { districtId });
  }

  /** Live-search-as-you-type typeahead (ctrl:1069-1095) -- genuinely
   * different mechanism from this codebase's sibling `organization-master`,
   * which uses a plain paginated select instead. Read fresh from source,
   * not assumed to match the sibling. */
  searchAreaByCity(cityId: number, searchKeyword: string): Observable<ApiResponse<never, AreaSearchResult>> {
    return this.http.post<ApiResponse<never, AreaSearchResult>>(`${this.base}/global/serviceAreaBysearchKeyword`, {
      cityId,
      searchKeyword,
    });
  }

  getOrganizationList(): Observable<ApiResponse<never, OrganizationOption>> {
    return this.http.get<ApiResponse<never, OrganizationOption>>(`${this.base}/organization/active`);
  }

  // ---- list ----

  getUnitList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, HospitalMasterRow>> {
    return this.http.get<ApiResponse<never, HospitalMasterRow>>(
      `${this.base}/unit/${offset}/${noOfRecordsPerPage}/${organizationId}`,
    );
  }

  getUnitCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/unit/count/${organizationId}`);
  }

  getUnitById(unitId: number): Observable<ApiResponse<HospitalMasterRecord>> {
    return this.http.get<ApiResponse<HospitalMasterRecord>>(`${this.base}/unit/${unitId}`);
  }

  save(record: HospitalMasterRecord): Observable<ApiResponse<{ unitId: number }>> {
    return this.http.post<ApiResponse<{ unitId: number }>>(`${this.base}/unit/save`, record);
  }

  update(record: HospitalMasterRecord): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/unit/update`, record);
  }

  updateStatus(unitId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.base}/unit/status/${unitId}/${status}`);
  }

  uploadLogo(unitId: number, file: File): Observable<HttpEvent<ApiResponse>> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<ApiResponse>(`${this.base}/unit/image/${unitId}`, form, {
      reportProgress: true,
      observe: 'events',
    });
  }

  // ---- subscription sub-feature (Subscription button, editMode &&
  // isSuperAdmin==='Y' only) ----

  getSubscriptionTypeList(): Observable<ApiResponse<never, SubscriptionTypeOption>> {
    return this.http.get<ApiResponse<never, SubscriptionTypeOption>>(`${this.base}/unit/subcription/types`);
  }

  getSubscriptionConfig(unitId: number): Observable<ApiResponse<SubscriptionConfig>> {
    return this.http.post<ApiResponse<SubscriptionConfig>>(`${this.base}/unit/subcription/config/details`, { unitId });
  }

  saveSubscriptionConfig(config: SubscriptionConfig): Observable<ApiResponse<SubscriptionConfig>> {
    return this.http.post<ApiResponse<SubscriptionConfig>>(`${this.base}/unit/subcription/config`, config);
  }

  getSubscriptionHistory(unitId: number, organizationId: number): Observable<ApiResponse<never, SubscriptionHistoryRow>> {
    return this.http.post<ApiResponse<never, SubscriptionHistoryRow>>(`${this.base}/unit/subcription/unitDetails`, {
      unitId,
      organizationId,
    });
  }

  createSubscription(payload: ManualSubscriptionForm & { unitId: number; organizationId: number }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/unit/subcription/details`, payload);
  }
}
