import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AdmissionDetail, PatientSearchResult, PeriodicityServiceSearchResult } from './periodicity-service.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/services/unit/periodicityServiceOrdersController.js
 * -- not guessed. `unitIdId` (not `unitId`) in searchServices' payload is a
 * real typo in the legacy request body, kept verbatim for backend
 * compatibility (see this pilot batch's audit doc, finding 4 on this screen).
 */
@Injectable({ providedIn: 'root' })
export class PeriodicityServiceApi {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveGenderList(): Observable<ApiResponse<never, unknown>> {
    return this.http.get<ApiResponse<never, unknown>>(`${this.base}/adt/getActiveGenderList`);
  }

  getActiveIdentificationList(): Observable<ApiResponse<never, unknown>> {
    return this.http.get<ApiResponse<never, unknown>>(`${this.base}/adt/getActiveIdentificationList`);
  }

  searchPatient(payload: Record<string, unknown>): Observable<ApiResponse<never, PatientSearchResult>> {
    return this.http.post<ApiResponse<never, PatientSearchResult>>(
      `${this.base}/adt/patientSearchByMultipleCriteriaForIPD`,
      payload,
    );
  }

  getAdmissionDetail(admissionId: number): Observable<ApiResponse<AdmissionDetail>> {
    return this.http.get<ApiResponse<AdmissionDetail>>(`${this.base}/api/ipd/admissions/patient/${admissionId}`);
  }

  getPeriodicityServicesByAdmissionId(admissionId: number): Observable<ApiResponse<never, unknown>> {
    return this.http.get<ApiResponse<never, unknown>>(`${this.base}/api/periodicity/order/${admissionId}`);
  }

  searchServices(
    visitTypeId: number | null,
    unitId: number,
    serviceName: string,
  ): Observable<ApiResponse<never, PeriodicityServiceSearchResult>> {
    return this.http.post<ApiResponse<never, PeriodicityServiceSearchResult>>(
      `${this.base}/api/global/search/services/periodicity`,
      { visitTypeId, unitIdId: unitId, serviceName },
    );
  }

  getRate(
    serviceId: number,
    organizationId: number,
    unitId: number,
    visitTypeId: number | null,
    periodicityServiceConfigId: number,
  ): Observable<ApiResponse<unknown>> {
    return this.http.get<ApiResponse<unknown>>(
      `${this.base}/api/periodicity/config/getRate/${serviceId}/${organizationId}/${unitId}/${visitTypeId}/${periodicityServiceConfigId}`,
    );
  }

  orderPeriodicityService(orders: unknown[]): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/periodicity/order`, { pServiceOrderList: orders });
  }
}
