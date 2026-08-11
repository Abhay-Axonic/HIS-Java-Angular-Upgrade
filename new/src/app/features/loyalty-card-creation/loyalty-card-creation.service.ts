import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { LoyaltyCardRow } from './loyalty-card-creation.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/config/global.url.config.js:236`
 * (GET_LOYALTY_CARD_MASTER_LIST = "/api/unit/getLoyaltiCardMasterList")
 * — not guessed. POST body matches the legacy `initMasterList` call at
 * `controller/master/unit/loyaltyCardCreationMasterController.js:68-79`
 * (organisationId — British spelling as legacy writes it — offset,
 * noOfRecordsPerPage, unitId). Confirmed live 2026-08-12: SUCCESS with an
 * empty listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class LoyaltyCardCreationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:68-79 (loyaltyCardCreationMasterController.js), global.url.config.js:236 —
  // POST /api/unit/getLoyaltiCardMasterList. Dev-empty: SUCCESS + [].
  getList(
    organisationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, LoyaltyCardRow>> {
    return this.http.post<ApiResponse<never, LoyaltyCardRow>>(
      `${this.base}/api/unit/getLoyaltiCardMasterList`,
      { organisationId, unitId, offset, noOfRecordsPerPage },
    );
  }
}
