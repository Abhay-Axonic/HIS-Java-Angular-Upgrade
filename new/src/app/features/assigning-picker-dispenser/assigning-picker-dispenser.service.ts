import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AssigningPickerRow,
  AssigningPickerSearchRequest,
  CancelReason,
  DoctorTypeaheadResult,
  DropdownOption,
  PatientTypeaheadResult,
} from './assigning-picker-dispenser.models';

interface PickerDropdownResponse {
  visitType: DropdownOption[];
  status: DropdownOption[];
}

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/pharmacy/assigningPickerDispenserController.js
 * and resources/assets/js/constants/constants.js -- not guessed. See this
 * pilot batch's audit doc for the full on-load call sequence.
 */
@Injectable({ providedIn: 'root' })
export class AssigningPickerDispenserService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getPharmacyRole(organizationId: number, userId: number, unitId: number): Observable<ApiResponse<never, unknown>> {
    return this.http.post<ApiResponse<never, unknown>>(`${this.base}/adt/getPharmacyUserRoleType`, {
      organizationId,
      userId,
      unitId,
    });
  }

  getCurrency(unitId: number): Observable<{ symbol: string }> {
    return this.http.get<{ symbol: string }>(`${this.base}/api/global/currencies/${unitId}`);
  }

  getCancelReasons(): Observable<CancelReason[]> {
    return this.http.get<CancelReason[]>(`${this.base}/api/global/cancel/reasons`);
  }

  getDropdowns(): Observable<PickerDropdownResponse> {
    return this.http.get<PickerDropdownResponse>(`${this.base}/api/pharmacy/ip/assign/picker/dropdowns`);
  }

  search(request: AssigningPickerSearchRequest): Observable<AssigningPickerRow[]> {
    return this.http.post<AssigningPickerRow[]>(`${this.base}/api/pharmacy/ip/assign/picker/search`, request);
  }

  searchPatientNames(unitId: number, search: string, visitTypeId: number): Observable<PatientTypeaheadResult[]> {
    return this.http.get<PatientTypeaheadResult[]>(`${this.base}/api/global/patient/search/${unitId}/${search}/${visitTypeId}`);
  }

  searchUhid(search: string, organizationId: number): Observable<PatientTypeaheadResult[]> {
    return this.http.get<PatientTypeaheadResult[]>(`${this.base}/api/global/patient/search/uhid/${search}/${organizationId}`);
  }

  searchDoctorNames(organizationId: number, unitId: number, search: string): Observable<DoctorTypeaheadResult[]> {
    return this.http.get<DoctorTypeaheadResult[]>(`${this.base}/api/global/doctor/search/${organizationId}/${unitId}/${search}`);
  }

  cancel(id: number, statusId: number | undefined, cancelNote: string): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/pharmacy/op/sale/workList/cancel`, {
      id,
      statusId,
      cancelNote,
      isFromAssignpicker: true,
    });
  }
}
