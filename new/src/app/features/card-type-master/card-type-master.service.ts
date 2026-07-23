import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CardTypeRow,
  CardTypeSavePayload,
  CardTypeStatusPayload,
  CardTypeUpdatePayload,
} from './card-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/cardTypeMasterController.js (285
 * lines, `cardTypeMasterController`, no `controllerAs`). State
 * `cardTypeMaster` is SELF-REGISTERED at the bottom of this SAME controller
 * file (ctrl:271-278, `config($stateProvider)` + `.config(config)`), not in
 * `app.js` -- and has NO `resolve` block at all, so there is no `moduleId`
 * to gate this route on (see card-type-master.ts's class doc comment).
 * All 4 calls are real, live, POST, under `/api/billing/*`.
 */
@Injectable({ providedIn: 'root' })
export class CardTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:70-86 -- POST { orgnisationId }. Fetches the FULL result set in one
  // shot -- no offset/count params at all. Legacy's own field spelling
  // (ctrl:71, `orgnisationId : $scope.organizationId`) -- neither
  // "organisationId" nor "organizationId" -- kept verbatim as the real wire
  // contract (same spelling this codebase's bank-master already uses for
  // the identical reason).
  getList(organizationId: number): Observable<ApiResponse<never, CardTypeRow>> {
    return this.http.post<ApiResponse<never, CardTypeRow>>(`${this.base}/api/billing/getCardTypeMasterList`, {
      orgnisationId: organizationId,
    });
  }

  // ctrl:100-160 (saveCardType) -- POST, real create.
  save(payload: CardTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveCardTypeMaster`, payload);
  }

  // ctrl:219-269 (updateCardType) -- POST, real update.
  update(payload: CardTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateCardTypeMaster`, payload);
  }

  // ctrl:175-216 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: CardTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateStatusForCardType`, payload);
  }
}
