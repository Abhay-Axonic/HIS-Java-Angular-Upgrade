import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CountryOption,
  CurrencyByIdRow,
  CurrencyRow,
  CurrencySavePayload,
  CurrencyStatusPayload,
  CurrencyUpdatePayload,
} from './currency-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/billing/currencyMasterController.js
 * (419 lines, `currencyMasterController`, no `controllerAs`) and
 * resources/assets/js/config/global.url.config.js /
 * resources/assets/js/config/adt.url.config.js -- not guessed.
 *
 * `CurrencyMasterController.java`'s class-level `@RequestMapping` is
 * `"/billing"` (legacy/Global/.../controller/CurrencyMasterController.java:20)
 * -- NO `/api` prefix, unlike `CardTypeMasterController` (`"api/billing/"`).
 * Every `/billing/*` path below is used exactly as-is, verified directly
 * against that annotation -- not assumed from another sibling screen's
 * convention (see this codebase's `consentTypeMaster` precedent, where a
 * wrongly-added `/api` 404'd every call).
 */
@Injectable({ providedIn: 'root' })
export class CurrencyMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:60-78 (init) -- GET, trailing org id appended directly onto
  // GETACTIVECOUNTRYLIST's own trailing slash (adt.url.config.js:61).
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(
      `${this.base}/adt/getActiveCountryList/${organizationId}`,
    );
  }

  // ctrl:197-224 (getCountryCode) -- POST. Legacy builds an unused `data`
  // var (`{countryId: id}`) and actually sends a second one, `dataorg`
  // (`{countryId, organizationId}`) -- the unused var is dead code, not
  // modeled; this sends what's actually on the wire.
  getCountryCode(countryId: number, organizationId: number): Observable<ApiResponse<never, { countryCode: string }>> {
    return this.http.post<ApiResponse<never, { countryCode: string }>>(`${this.base}/adt/getCountryById`, {
      countryId,
      organizationId,
    });
  }

  // ctrl:90-140 (initCurrencyMasterList/getCurrencyMasterList) -- POST,
  // real server-side offset/limit pagination (getAllCurrencyMasterList,
  // "Dilip"-added, DAO uses setFirstResult/setMaxResults). The older,
  // non-paginated `getCurrencyMasterList` endpoint also exists on the
  // backend but is never called by this controller -- not wired here.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, CurrencyRow>> {
    return this.http.post<ApiResponse<never, CurrencyRow>>(`${this.base}/billing/getAllCurrencyMasterList`, {
      orgnisationId: organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:99,126-139 (data1) -- POST, no offset/count params, just the org.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/billing/getCurrencyCount`, {
      orgnisationId: organizationId,
    });
  }

  // ctrl:296-324 (showUpdateBtn) -- GET, path variables, no request body.
  getById(currencyId: number, organizationId: number): Observable<ApiResponse<never, CurrencyByIdRow>> {
    return this.http.get<ApiResponse<never, CurrencyByIdRow>>(
      `${this.base}/billing/getCurrencyMasterById/${currencyId}/${organizationId}`,
    );
  }

  // ctrl:234-291 (saveCurrency, else-branch POST) -- real create.
  save(payload: CurrencySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/saveCurrencyMaster`, payload);
  }

  // ctrl:368-418 (updateCurrency) -- real update. See
  // CurrencyUpdatePayload's doc comment (currency-master.models.ts) for the
  // countryId caveat.
  update(payload: CurrencyUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateCurrencyMaster`, payload);
  }

  // ctrl:327-365 (updateStatus) -- real status toggle.
  updateStatus(payload: CurrencyStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateCurrencyMasterStatus`, payload);
  }
}
