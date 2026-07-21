import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { GeoOption, InsuranceMasterRecord } from './insurance-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/insuranceMasterController.js
 * and the shared adt.url.config.js / global.url.config.js constants it
 * references. Golden-trace-confirmed in docs/PHASE2_PILOT_AUDIT.md
 * section 8.
 */
@Injectable({ providedIn: 'root' })
export class InsuranceMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.get<ApiResponse<never, GeoOption>>(
      `${this.base}/adt/getActiveCountryList/${organizationId}`,
    );
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

  getAreasByCity(cityId: number): Observable<ApiResponse<never, GeoOption>> {
    return this.http.post<ApiResponse<never, GeoOption>>(`${this.base}/adt/getAreaByCityId`, { cityId });
  }

  getGeneralLedgerList(organizationId: number, generalLedgerName?: string): Observable<ApiResponse<never, { generalLedgerId: number; generalLedgerName: string }>> {
    const body = generalLedgerName ? { generalLedgerName } : {};
    return this.http.post<ApiResponse<never, { generalLedgerId: number; generalLedgerName: string }>>(
      `${this.base}/org/getGeneralLedgerMasterActiveList/${organizationId}`,
      body,
    );
  }

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, InsuranceMasterRecord>> {
    return this.http.post<ApiResponse<never, InsuranceMasterRecord>>(
      `${this.base}/api/unit/billing/getInsuranceMasterList`,
      { orgId: organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/unit/billing/getInsuranceMasterCount`, {
      unitId,
      orgId: organizationId,
    });
  }

  getById(id: number, organizationId: number, unitId: number): Observable<ApiResponse<never, InsuranceMasterRecord>> {
    return this.http.get<ApiResponse<never, InsuranceMasterRecord>>(
      `${this.base}/api/unit/billing/getInsuranceMasterById/${id}/${organizationId}/${unitId}`,
    );
  }

  save(record: InsuranceMasterRecord): Observable<ApiResponse<{ insuranceId: number }>> {
    return this.http.post<ApiResponse<{ insuranceId: number }>>(
      `${this.base}/api/unit/billing/saveInsuranceMaster`,
      record,
    );
  }

  update(record: InsuranceMasterRecord): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/unit/billing/updateInsuranceMaster`, record);
  }

  updateStatus(insuranceId: number, status: 'A' | 'I', updatedBy: number, updatedDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/billing/updateInsuranceMasterStatus`, {
      insuranceId,
      status,
      updatedBy,
      updatedDate,
    });
  }

  uploadDocument(
    organizationId: number,
    unitId: number,
    insuranceId: number,
    file: File,
  ): Observable<ApiResponse> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<ApiResponse>(
      `${this.base}/api/unit/billing/insurance/image/${organizationId}/${unitId}/${insuranceId}`,
      form,
    );
  }
}
