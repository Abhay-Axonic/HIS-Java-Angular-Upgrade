import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ConsentTypeRow } from './consent-type.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/adt/global/
 * consentTypeMasterController.js` (GETCONSENTTYPELIST =
 * "/adt/getConsentTypeMasterList", adt.url.config.js:242) — not guessed.
 * Confirmed live 2026-08-12: SUCCESS with 3 real rows — REAL DATA
 * (correction of the batch-10 noData misbuild, DQ-2026-08-12-028
 * follow-up).
 */
@Injectable({ providedIn: 'root' })
export class ConsentTypeService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:getConsentTypeList — POST /adt/getConsentTypeMasterList
  // {organizationId, unitId, offset, noOfRecordsPerPage}.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, ConsentTypeRow>> {
    return this.http.post<ApiResponse<never, ConsentTypeRow>>(
      `${this.base}/adt/getConsentTypeMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage: size },
    );
  }
}
