import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DenominationRow } from './interim-handover.models';

/**
 * Endpoint + payload taken directly from the legacy
 * `interimHandoverController.js` ($scope.getDenominationList, lines 75-107)
 * and the url.config constant GETDENOMINATIONLIST1 =
 * "/adt/getListOfDenominations" (adt.url.config.js:698). Backed by
 * InterimHandoverMasterController.java (POST /getListOfDenominations at
 * :31-32, class-level "/adt" path), DAO
 * InterimHandoverMasterDaoImpl.getListOfDenominations.
 *
 * Verified live 2026-08-12 (backend localhost:8080, Authorization Bearer
 * tools/.token_cache.json + userId:1 unitId:1 organizationId:1): POST
 * {unitId, organizationId} -> HTTP 200 {"status":"success","listObject":[],
 * "message":"Added Successfully."} — a SAVE-endpoint envelope (0 rows),
 * NOT a list. Screen 598 is a write-path cash-entry form; the feed is
 * deferred to the transaction methodology.
 */
@Injectable({ providedIn: 'root' })
export class InterimHandoverService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:interimHandoverController.getDenominationList — POST
  // /adt/getListOfDenominations {unitId, organizationId}.
  getList(orgId: number, unitId: number): Observable<ApiResponse<never, DenominationRow>> {
    return this.http.post<ApiResponse<never, DenominationRow>>(
      `${this.base}/adt/getListOfDenominations`,
      { unitId, organizationId: orgId },
    );
  }
}
