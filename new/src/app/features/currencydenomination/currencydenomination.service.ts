import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CurrencyDenominationDetail,
  CurrencyDenominationRow,
  CurrencyDenominationSavePayload,
  CurrencyDenominationStatusPayload,
  CurrencyDenominationUpdatePayload,
  CurrencyOption,
} from './currencydenomination.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/global.url.config.js` (lines 86-91) and
 * verified directly against `CurrencyDenominationMasterController`'s
 * class-level `@RequestMapping("api/billing")` (NOT `/api/billing/...`
 * guessed -- the earlier `consentTypeMaster` batch had a real 404 bug from
 * assuming an `/api` prefix that didn't exist; here the prefix genuinely
 * IS present, confirmed straight from the annotation):
 *   SAVECURRENCYDENOMINATIONMASTER        = /api/billing/saveCurrencyDenominationMaster        (ctrl:238-239)
 *   GETCURRENCYDENOMINATIONMASTERBYID     = /api/billing/getCurrencyDenominationMasterById/{id}/{orgId} (ctrl:282-285, GET)
 *   GETCURRENCYDENOMINATIONMASTERLIST     = /api/billing/getCurrencyDenominationMasterList     (ctrl:100-101)
 *   UPDATECURRENCYDENOMINATIONMASTER      = /api/billing/updateCurrencyDenominationMaster       (ctrl:368-369)
 *   UPDATECURRENCYDENOMINATIONMASTERSTATUS= /api/billing/updateCurrencyDenominationMasterStatus (ctrl:320-322)
 *   GETCURRENCYDENOMINATIONCOUNT          = /api/billing/getCurrencyDenominationCount           (ctrl:102-103)
 *
 * `getCurrencyOptions` is the ONE exception: it backs the "Currency"
 * dropdown (view:17-21) via `GETCURRENCYMASTERLIST`, which resolves to
 * `/billing/getCurrencyMasterList` (global.url.config.js:63) -- a
 * DIFFERENT controller (`CurrencyMasterController`, `@RequestMapping("/billing")`,
 * confirmed directly, no `api` segment at all). Both prefixes are real and
 * intentionally different; this is not a typo to "fix" into consistency.
 */
@Injectable({ providedIn: 'root' })
export class CurrencydenominationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:60-83 -- GETCURRENCYMASTERLIST, note the different (`/billing`, no
  // `/api`) base path described in the class doc above.
  getCurrencyOptions(orgnisationId: number): Observable<ApiResponse<never, CurrencyOption>> {
    return this.http.post<ApiResponse<never, CurrencyOption>>(`${this.base}/billing/getCurrencyMasterList`, {
      orgnisationId,
    });
  }

  // ctrl:94-147 (initCurrencyDenominatoinMaster) -- server-side paginated
  // list, POST.
  getList(
    orgnisationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, CurrencyDenominationRow>> {
    return this.http.post<ApiResponse<never, CurrencyDenominationRow>>(
      `${this.base}/api/billing/getCurrencyDenominationMasterList`,
      { orgnisationId, offset, noOfRecordsPerPage },
    );
  }

  // ctrl:111-146 -- separate count call used to size the pager.
  getCount(orgnisationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/billing/getCurrencyDenominationCount`, {
      orgnisationId,
    });
  }

  // ctrl:279-304 (showUpdateBtn) -- GET with 2 path params, in this exact
  // order (currencyId i.e. the denomination row id, then orgId).
  getById(currencyDenominationId: number, orgnisationId: number): Observable<ApiResponse<never, CurrencyDenominationDetail>> {
    return this.http.get<ApiResponse<never, CurrencyDenominationDetail>>(
      `${this.base}/api/billing/getCurrencyDenominationMasterById/${currencyDenominationId}/${orgnisationId}`,
    );
  }

  // ctrl:210-269 (saveCurrencyDenomination, POST branch only -- see
  // component class doc for the popUpFlag-gated confirm step).
  save(payload: CurrencyDenominationSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveCurrencyDenominationMaster`, payload);
  }

  // ctrl:348-399 (updateCurrencyDenomination) -- no confirm modal at all.
  update(payload: CurrencyDenominationUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateCurrencyDenominationMaster`, payload);
  }

  // ctrl:307-345 (updateStatus).
  updateStatus(payload: CurrencyDenominationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateCurrencyDenominationMasterStatus`, payload);
  }
}
