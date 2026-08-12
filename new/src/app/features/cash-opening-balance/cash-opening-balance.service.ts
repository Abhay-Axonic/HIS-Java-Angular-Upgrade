import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DenominationRow } from './cash-opening-balance.models';

/**
 * Endpoint + payload taken directly from the legacy
 * `cashOpeningBalanceController.js` ($scope.getDenominationList, lines
 * 80-114) and the url.config constant GETDENOMINATIONLIST =
 * "/api/global/getListOfDenominations" (adt.url.config.js:697). Backed by
 * CashHandoverMasterController.java (class-level @RequestMapping
 * "/api/global"; POST /getListOfDenominations at :52-53), DAO
 * CashHandoverMasterDaoImpl.getListOfDenominations (:44-59, named query
 * GET_DENOMINATION_MASTER_LIST).
 *
 * Verified live 2026-08-12 (backend localhost:8080, Authorization Bearer
 * tools/.token_cache.json + userId:1 unitId:1 organizationId:1): POST
 * {unitId, organizationId} -> HTTP 200 {"status":"success","listObject":[],
 * "message":"Added Successfully."} — a SAVE-endpoint envelope (0 rows),
 * NOT a list. Screen 597 is a write-path cash-entry form; the feed is
 * deferred to the transaction methodology.
 */
@Injectable({ providedIn: 'root' })
export class CashOpeningBalanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:cashOpeningBalanceController.getDenominationList — POST
  // /api/global/getListOfDenominations {unitId, organizationId}.
  getList(orgId: number, unitId: number): Observable<ApiResponse<never, DenominationRow>> {
    return this.http.post<ApiResponse<never, DenominationRow>>(
      `${this.base}/api/global/getListOfDenominations`,
      { unitId, organizationId: orgId },
    );
  }
}
