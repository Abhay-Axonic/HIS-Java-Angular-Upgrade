import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { FieldConfig, GeoRow, IdNameRow, RegistrationPayload } from './registration.models';

/** The legacy registration's endpoint paths — resolved from the url.config
 * constants (including the legacy's `getAvtive...` typos, ported faithfully). */
@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getPrefixList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the legacy endpoint is a GET with the orgId in the path (WebAppointmentController)
    return this.http.get<ApiResponse<never, IdNameRow>>(`${this.base}/api/webappointment/getActivePrefixList/${orgId}`, {
      params: { unitId },
    });
  }

  getGenderList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.http.get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getActiveGenderList`);
  }

  getIdentificationList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.http.get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getActiveIdentificationList`);
  }

  getRelationList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.http.get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getActiveRelationList`);
  }

  getPaymentEntitlementList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the legacy constant's typo is the real endpoint — ported faithfully
    return this.http.get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getAvtivePaymentEntitlementList`);
  }

  getCountry(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the CountryMasterDto has NO unitId field — a unitId in the body is an
    // unknown property and 400s (verified against the corrected backend)
    return this.post('/adt/getCountryById', { organizationId: orgId });
  }

  getStatesByCountry(countryId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    // the StateMasterDto has no org/unit fields — parent id only (the corrected DTO)
    return this.post('/adt/getActiveStateListByCountryId', { countryId });
  }

  getDistrictsByState(stateId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    return this.post('/adt/getActiveDistrictListByStateId', { stateId });
  }

  getCitiesByDistrict(districtId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    return this.post('/adt/getActiveCityListByDistrictId', { districtId });
  }

  getAreasByCity(cityId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    return this.post('/adt/getAreaByCityId', { cityId });
  }

  getFieldConfig(orgId: number, unitId: number): Observable<ApiResponse<never, FieldConfig>> {
    // the legacy endpoint is a GET with orgId/unitId in the path
    return this.http.get<ApiResponse<never, FieldConfig>>(
      `${this.base}/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/${orgId}/${unitId}`,
    );
  }

  save(payload: RegistrationPayload): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/global/patientRegistration', payload);
  }
}
