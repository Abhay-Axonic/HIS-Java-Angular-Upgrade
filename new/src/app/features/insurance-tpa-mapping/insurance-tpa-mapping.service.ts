import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { InsuranceOption, InsuranceTpaMapperRow, TpaOption } from './insurance-tpa-mapping.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/unit/insuranceTPAMappingController.js
 * and confirmed against InsuranceTPAMapperController.java
 * (`@RequestMapping("api/billing")`) and CompanyMasterController.java
 * (`getActiveTPA`). See class comment on insurance-tpa-mapping.ts for why
 * Update/Edit are deliberately NOT wired to this service at all --
 * legacy's own backend has no update endpoint for this feature.
 */
@Injectable({ providedIn: 'root' })
export class InsuranceTpaMappingService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveTpaList(organizationId: number, unitId: number): Observable<ApiResponse<never, TpaOption>> {
    return this.http.post<ApiResponse<never, TpaOption>>(`${this.base}/api/global/getActiveTPA`, {
      organizationId,
      unitId,
    });
  }

  getInsuranceNotMapped(companyId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, InsuranceOption>> {
    return this.http.post<ApiResponse<never, InsuranceOption>>(`${this.base}/api/billing/getInsuranceListNotMappedToMapper`, {
      companyId,
      organizationId,
      unitId,
    });
  }

  getList(organizationId: number, unitId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, InsuranceTpaMapperRow>> {
    return this.http.post<ApiResponse<never, InsuranceTpaMapperRow>>(`${this.base}/api/billing/getInsuranceTPAMapperList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/billing/getInsuranceTPAMapperListCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:128-166 -- POST, real create.
  save(payload: {
    companyId: number | '';
    status: 'A';
    createdDate: string;
    createdBy: number;
    updatedDate: string;
    updatedBy: number;
    unitId: number;
    organizationId: number;
    insuranceTPAMapperDtoList: InsuranceOption[];
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveInsuranceTPAMapper`, payload);
  }

  // ctrl:170-190 -- POST, real status toggle.
  updateStatus(
    insuranceTpaMapperId: number,
    status: 'A' | 'I',
    organizationId: number,
    unitId: number,
    updatedBy: number,
    updatedDate: string,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateInsuranceTPAMapperStatus`, {
      insuranceTpaMapperId,
      status,
      organizationId,
      unitId,
      updatedDate,
      updatedBy,
    });
  }
}
