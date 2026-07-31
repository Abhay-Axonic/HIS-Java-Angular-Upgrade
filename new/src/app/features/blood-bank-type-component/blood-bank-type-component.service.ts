import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BloodBagOption, BloodBankTypeComponentRow, BloodComponentOption, DetailedComponentRow } from './blood-bank-type-component.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/bloodBank/bloodBankTypeComponentMasterController.js
 * and resources/assets/js/config/bloodBank.url.config.js, confirmed against
 * BloodBankTypeComponentMasterController.java (`@RequestMapping("/bloodBank")`).
 * Real, functional golden-trace capture (2026-07-31, live deployment,
 * authenticated session) confirmed `getBloodBagList` returned a real
 * seeded row ("Bag Type Three").
 */
@Injectable({ providedIn: 'root' })
export class BloodBankTypeComponentService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getBagTypeList(organizationId: number, unitId: number): Observable<ApiResponse<never, BloodBagOption>> {
    return this.http.get<ApiResponse<never, BloodBagOption>>(`${this.base}/bloodBank/getBloodBagList/${organizationId}/${unitId}`);
  }

  // ctrl:84-106 -- GET with a query param appended (bloodComponentDesc=""),
  // matching legacy's own construction exactly.
  getComponentList(organizationId: number, unitId: number): Observable<ApiResponse<never, BloodComponentOption>> {
    return this.http.get<ApiResponse<never, BloodComponentOption>>(
      `${this.base}/bloodBank/getBloodComponentList/${organizationId}/${unitId}?bloodComponentDesc=`,
    );
  }

  getDetailedComponentList(bloodBagTypeId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, DetailedComponentRow>> {
    return this.http.get<ApiResponse<never, DetailedComponentRow>>(
      `${this.base}/bloodBank/getDetailedComponentList/${bloodBagTypeId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:183-214 -- GET path variables offset/noOfRecordsPerPage only, no org/unit.
  getList(offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, BloodBankTypeComponentRow>> {
    return this.http.get<ApiResponse<never, BloodBankTypeComponentRow>>(
      `${this.base}/bloodBank/getBloodBankTypeComponentMasterList/${offset}/${noOfRecordsPerPage}`,
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/bloodBank/getBloodBankTypeComponentMasterCount/${organizationId}/${unitId}`);
  }

  // ctrl:253-343 -- POST, real create. selectBloodBankTypeComponentMasterList
  // is a plain array of bloodComponentId numbers, matching legacy's own
  // `.push(value.bloodComponentId)` construction exactly.
  save(payload: {
    bloodComponentId?: number;
    bloodComponentDesc?: string;
    bloodBagTypeId: number | '';
    bloodBagTypeDesc?: string;
    status: 'A';
    organizationId: number;
    unitId: number;
    isDeleted: 'N';
    createdBy: number;
    createdDate: string;
    updatedBy: number;
    updatedDate: string;
    selectBloodBankTypeComponentMasterList: number[];
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/bloodBank/saveBloodBankTypeComponentMaster`, payload);
  }

  // ctrl:346-352 -- GET path variable, org/unit appended (used by showUpdateBtn/edit).
  getById(bloodBagTypeId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, DetailedComponentRow>> {
    return this.http.get<ApiResponse<never, DetailedComponentRow>>(
      `${this.base}/bloodBank/getBloodBankTypeComponentMasterById/${bloodBagTypeId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:391-480 -- POST, real update, same payload shape as save().
  update(payload: {
    bloodComponentId?: number;
    bloodComponentDesc?: string;
    bloodBagTypeId: number | '';
    bloodBagTypeDesc?: string;
    status: 'A';
    organizationId: number;
    unitId: number;
    isDeleted: 'N';
    createdBy: number;
    createdDate: string;
    updatedBy: number;
    updatedDate: string;
    selectBloodBankTypeComponentMasterList: number[];
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/bloodBank/updateBloodBankTypeComponentMaster`, payload);
  }

  // ctrl:482-518 -- GET path variables, note the order is
  // bloodBagTypeId/organizationId/unitId/status, matching
  // BloodBankTypeComponentMasterController.java's @PathVariable order
  // exactly (NOT the same order the JS builds the URL string in, which
  // interleaves organizationId/unitId/status differently -- the compiled
  // route mapping is the source of truth here, confirmed by reading the
  // Java controller directly, not the JS string concatenation alone).
  updateStatus(bloodBagTypeId: number, organizationId: number, unitId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/bloodBank/updateBloodBankTypeComponentMasterStatus/${bloodBagTypeId}/${organizationId}/${unitId}/${status}`,
    );
  }
}
