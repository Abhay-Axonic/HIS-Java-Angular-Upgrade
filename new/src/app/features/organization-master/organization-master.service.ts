import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpEvent, HttpEventType } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { GeoOption, ModuleNode, OrganizationRecord, TimezoneOption } from './organization-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/organizationController.js.
 * Golden-trace-confirmed in docs/PHASE2_PILOT_AUDIT.md section 8 (8 real
 * calls captured, all present here). The single-slash `/adt/getStateByCountryId/`
 * is used here even though the legacy controller has an accidental
 * double-slash (`/adt//getStateByCountryId/`) -- an obvious typo that
 * HTTP servers normalize identically either way, not a behaviorally
 * meaningful quirk worth preserving verbatim (unlike the `orgnisationId`
 * field-name typo in billingGroupMaster's API contract, which IS load-bearing).
 */
@Injectable({ providedIn: 'root' })
export class OrganizationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // Fetched unconditionally on load in the legacy controller even though
  // its own UI (the licence sub-table) is commented out in the view --
  // preserved here purely for network-level parity with the golden trace,
  // not because the result is rendered anywhere.
  getLicenceTypeList(organizationId: number): Observable<ApiResponse<never, unknown>> {
    return this.http.get<ApiResponse<never, unknown>>(`${this.base}/org/getLiceneceTypeMasterList/${organizationId}`);
  }

  getTimezones(): Observable<ApiResponse<never, TimezoneOption>> {
    return this.http.get<ApiResponse<never, TimezoneOption>>(`${this.base}/timezones`);
  }

  getCountryList(): Observable<ApiResponse<never, GeoOption>> {
    return this.http.get<ApiResponse<never, GeoOption>>(`${this.base}/adt/getAllCountryMasterList`);
  }

  getStatesByCountry(countryId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.get<ApiResponse<never, GeoOption>>(`${this.base}/adt/getStateByCountryId/${countryId}`);
  }

  getDistrictsByState(stateId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.post<ApiResponse<never, GeoOption>>(`${this.base}/adt/getActiveDistrictListByStateId`, {
      stateId,
    });
  }

  getCitiesByDistrict(districtId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.post<ApiResponse<never, GeoOption>>(`${this.base}/adt/getActiveCityListByDistrictId`, {
      districtId,
    });
  }

  // Legacy uses a paginated "load more" variant here
  // (getAreaByCityIdPagination) for what is otherwise a plain dropdown --
  // flagged in the audit as an odd pattern worth a closer look. Rebuilt
  // as a single generously-sized page rather than replicating
  // infinite-scroll for a dropdown; documented, not silent.
  getAreasByCity(cityId: number, organizationId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.post<ApiResponse<never, GeoOption>>(`${this.base}/adt/getAreaByCityIdPagination`, {
      cityId,
      offset: 0,
      noOfRecordsPerPage: 200,
    });
  }

  getOrganizationList(offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, OrganizationRecord>> {
    return this.http.get<ApiResponse<never, OrganizationRecord>>(
      `${this.base}/allArganizationList/${offset}/${noOfRecordsPerPage}`,
    );
  }

  getOrganizationCount(): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/organization/count`);
  }

  getById(organizationId: number): Observable<ApiResponse<OrganizationRecord>> {
    return this.http.get<ApiResponse<OrganizationRecord>>(`${this.base}/organization/${organizationId}`);
  }

  save(record: OrganizationRecord): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/organization/save`, record);
  }

  update(record: OrganizationRecord): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/organization/update`, record);
  }

  updateStatus(organizationId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.base}/organization/status/${organizationId}/${status}`);
  }

  uploadLogo(organizationId: number, file: File): Observable<HttpEvent<ApiResponse>> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<ApiResponse>(`${this.base}/organization/image/${organizationId}`, form, {
      reportProgress: true,
      observe: 'events',
    });
  }

  // Module tree + existing permissions -- fetched unconditionally on load
  // in the legacy controller (getModules()/$scope.getPermissions()), not
  // gated behind opening the "Module Access" modal. Preserved the same way.
  getModuleTree(): Observable<ApiResponse<never, { moduleId: number; moduleName: string; parentId: number; actionLink?: string }>> {
    return this.http.get<ApiResponse<never, { moduleId: number; moduleName: string; parentId: number; actionLink?: string }>>(
      `${this.base}/module`,
    );
  }

  getPermissions(organizationId: number, unitId: number): Observable<{ privileges?: unknown }> {
    return this.http.get<{ privileges?: unknown }>(`${this.base}/privilege/${organizationId}/${unitId}`);
  }

  savePermissions(payload: {
    roleId: string;
    privilegeId: string;
    moduleType: number;
    modules: { moduleId: number; moduleName: string; actionLink?: string; parentId: number; moduleType: number }[];
    organisationId: number;
    unitId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/privilege`, payload);
  }
}

export function isProgressEvent(event: HttpEvent<unknown>): event is HttpEvent<unknown> & { loaded: number; total?: number } {
  return event.type === HttpEventType.UploadProgress;
}
