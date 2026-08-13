import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BloodGroupRow,
  DonorPayload,
  DonorSaveResult,
  DonorTypeRow,
  GenderRow,
  IdentificationRow,
  NationalityRow,
  PrefixRow,
} from './donor-registration.models';

/**
 * Screen 719 feeds (all verified live 2026-08-12; DQ-056 / drift map):
 *  - prefix / gender / nationality / identification: adt master lists (LIVE)
 *  - blood group / donor type: bloodbank master lists, org/unit path (LIVE)
 *  - save: POST /api/bloodbank/donorRegistration — HONEST chain (all catches
 *    log-and-throw ServiceException; HTTP 500 carries the real message; the
 *    T0 write smoke passed BUILDABLE, exit 0).
 *  - patientAutofillSearch is a TODO STUB (empty 200) — deliberately NOT
 *    wired; the autofill control is omitted (honest state).
 */
@Injectable({ providedIn: 'root' })
export class DonorRegistrationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getPrefixes(): Observable<ApiResponse<never, PrefixRow>> {
    return this.http.get<ApiResponse<never, PrefixRow>>(
      `${this.base}/adt/getActivePrefixList`,
    );
  }

  getGenders(): Observable<ApiResponse<never, GenderRow>> {
    return this.http.get<ApiResponse<never, GenderRow>>(
      `${this.base}/adt/getActiveGenderList`,
    );
  }

  getNationalities(): Observable<ApiResponse<never, NationalityRow>> {
    return this.http.get<ApiResponse<never, NationalityRow>>(
      `${this.base}/adt/getActiveNationalityList`,
    );
  }

  getBloodGroups(): Observable<ApiResponse<never, BloodGroupRow>> {
    return this.http.get<ApiResponse<never, BloodGroupRow>>(
      `${this.base}/global/bloodBank/getActiveBloodGroupMaster/1/1`,
    );
  }

  getDonorTypes(): Observable<ApiResponse<never, DonorTypeRow>> {
    return this.http.get<ApiResponse<never, DonorTypeRow>>(
      `${this.base}/global/bloodBank/getActiveBloodDonorTypeMaster/1/1`,
    );
  }

  getIdentifications(): Observable<ApiResponse<never, IdentificationRow>> {
    return this.http.get<ApiResponse<never, IdentificationRow>>(
      `${this.base}/adt/getActiveIdentificationList`,
    );
  }

  saveDonor(payload: DonorPayload): Observable<ApiResponse<DonorSaveResult, never>> {
    return this.http.post<ApiResponse<DonorSaveResult, never>>(
      `${this.base}/api/bloodbank/donorRegistration`,
      payload,
    );
  }
}
