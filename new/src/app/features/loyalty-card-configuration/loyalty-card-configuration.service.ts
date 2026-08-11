import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { LoyaltyCardConfigRow } from './loyalty-card-configuration.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/master/unit/loyaltyCardConfigurationMasterController.js`
 * (initMasterList; GET_LOYALTY_CARD_CONFIGURATION_LIST =
 * "/api/unit/getLoyaltyCardConfigurationList" from
 * global.url.config.js:230) — not guessed. NOTE the legacy payload uses the
 * British spelling organisationId. Confirmed live 2026-08-12: SUCCESS with 1
 * real row.
 */
@Injectable({ providedIn: 'root' })
export class LoyaltyCardConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl initMasterList — POST /api/unit/getLoyaltyCardConfigurationList.
  getList(organisationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, LoyaltyCardConfigRow>> {
    return this.http.post<ApiResponse<never, LoyaltyCardConfigRow>>(
      `${this.base}/api/unit/getLoyaltyCardConfigurationList`,
      { organisationId, unitId, offset, noOfRecordsPerPage: size },
    );
  }
}
