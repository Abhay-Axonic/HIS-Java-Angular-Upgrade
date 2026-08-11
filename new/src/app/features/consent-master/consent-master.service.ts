import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ConsentRow } from './consent-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/adt/global/consentMasterController.js`
 * (GETCONSENTLIST = "/adt/getConsentMasterList" from adt.url.config.js:249).
 * Confirmed live 2026-08-12: blank generic envelope (masked —
 * DQ-2026-08-12-023, 3rd phantom column form_template, triple-swallowed).
 */
@Injectable({ providedIn: 'root' })
export class ConsentMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:124-127 — POST /adt/getConsentMasterList {organizationId, unitId,
  // offset, noOfRecordsPerPage}.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, ConsentRow>> {
    return this.http.post<ApiResponse<never, ConsentRow>>(
      `${this.base}/adt/getConsentMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage: size },
    );
  }
}
