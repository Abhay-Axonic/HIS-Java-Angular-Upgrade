import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { HisLedgerName, TallyCommKeys, TallyLedgerRow, TallyUnitOption } from './tally-interfacing.models';

/**
 * HTTP calls for `tallyInterfacing`. Every path below is taken directly
 * from `legacy/Global/src/main/java/com/param/global/controller/
 * TallyIntegrationController.java`'s `@RequestMapping("api/global")` +
 * per-method `@RequestMapping` values, and from
 * `tallyInterfacingController.js`'s own `BASE_URL + ROOT_URL + "..."`
 * URI construction -- not guessed.
 *
 * `authInterceptor` (already global, app.config.ts) attaches the same
 * Authorization/userId/unitId headers legacy's GenericService.serviceAction
 * does, for every call below EXCEPT `sendXmlDirectToTally` -- that one is a
 * raw cross-origin POST straight to the locally-configured Tally software
 * (`$http.post(url, data, config)` in legacy, with its own explicit
 * `Accept`/`Content-Type: application/xml` headers, no auth headers at
 * all), matching legacy's `sendToHost`/`sendRequstToTally`/
 * `sendCreateLedgerRequest`/`sendRefreshTallyLedgerNameList` local-host
 * branch exactly.
 */
@Injectable({ providedIn: 'root' })
export class TallyInterfacingService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getUnitsByOrganization(organizationId: number): Observable<ApiResponse<never, TallyUnitOption>> {
    return this.http.get<ApiResponse<never, TallyUnitOption>>(`${this.base}/unit/org/${organizationId}`);
  }

  getTallyInterfaceCommunicationKeys(unitId: number, organizationId: number): Observable<ApiResponse<TallyCommKeys>> {
    return this.http.post<ApiResponse<TallyCommKeys>>(`${this.base}/api/global/getTallyInterfaceCommunicationKeys`, {
      unitId,
      organizationId,
    });
  }

  getIpLedger(unitId: string, searchDate: string): Observable<ApiResponse<never, TallyLedgerRow>> {
    return this.http.post<ApiResponse<never, TallyLedgerRow>>(`${this.base}/api/global/getIpLedger`, { unitId, searchDate });
  }

  getOpLedger(unitId: string, searchDate: string): Observable<ApiResponse<never, TallyLedgerRow>> {
    return this.http.post<ApiResponse<never, TallyLedgerRow>>(`${this.base}/api/global/getOpLedger`, { unitId, searchDate });
  }

  getInventoryLedger(unitId: string, searchDate: string): Observable<ApiResponse<never, TallyLedgerRow>> {
    return this.http.post<ApiResponse<never, TallyLedgerRow>>(`${this.base}/api/global/getInventoryLedger`, {
      unitId,
      searchDate,
    });
  }

  getDoctorShareLedger(unitId: string, searchDate: string): Observable<ApiResponse<never, TallyLedgerRow>> {
    return this.http.post<ApiResponse<never, TallyLedgerRow>>(`${this.base}/api/global/getDoctorShareLedger`, {
      unitId,
      searchDate,
    });
  }

  getAllLedgersNameFromHis(unitId: string, searchDate: string): Observable<ApiResponse<never, HisLedgerName>> {
    return this.http.post<ApiResponse<never, HisLedgerName>>(`${this.base}/api/global/getAllLedgersNameFromHis`, {
      unitId,
      searchDate,
    });
  }

  /** Backing call for both Transfer and the "Send to Tally" leg -- body is
   * the currently-active tab's row array, sent back exactly as received
   * (legacy: `GenericService.serviceAction("POST", URI, $scope.getTallyIntegrationData)`). */
  sendTallyData(rows: TallyLedgerRow[]): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/global/sendTallyData`, rows);
  }

  exportTallyData(rows: TallyLedgerRow[]): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/global/exportTallyData`, rows);
  }

  sendLedgerNameRequest(unitId: string, searchDate: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/global/sendLedgerNameRequest`, { unitId, searchDate });
  }

  /** Legacy's own `sendRefreshLedgerNameRequest()` posts only `searchDate`
   * here, no `unitId` -- a real quirk, faithfully reproduced (see
   * tally-interfacing.ts's call site comment; harmless in practice since
   * the backend implementation ignores both fields and always returns the
   * same static "List of Accounts" export-format XML). */
  sendLedgerNameRequestForRefresh(searchDate: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/global/sendLedgerNameRequest`, { searchDate });
  }

  /** `object` on the response is `true` (boolean) on success, or the
   * offending ledger name (string) when `message === 'Ledger Not Found In
   * Tally'` -- see `TallyIntegrationServiceImpl.verifyLedgerNames`. Typed
   * `unknown` here; the caller only ever reads it in the not-found branch. */
  verifyLedgerNames(ledgerNamesFromTally: string, ledgerNamesFromHis: HisLedgerName[]): Observable<ApiResponse<unknown, string>> {
    return this.http.post<ApiResponse<unknown, string>>(`${this.base}/api/global/verifyLedgerNames`, {
      ledgerNamesFromTally,
      ledgerNamesFromHis,
    });
  }

  sendCreateLedgerRequest(newLedger: string, underGroupName: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/global/sendCreateLedgerRequest`, {
      newLedger,
      underGroupName,
    });
  }

  refreshTallyLedgerNameList(ledgerNamesFromTally: string): Observable<ApiResponse<never, string>> {
    return this.http.post<ApiResponse<never, string>>(`${this.base}/api/global/refreshTallyLedgerNameList`, {
      ledgerNamesFromTally,
    });
  }

  /** Backend relay path -- used when the selected unit's config has
   * `isTallyLocalHost === 'N'`. `code` on the response is legacy's own
   * 200/500/404 tri-state (see
   * docs/ANGULAR_PENDING_FINDINGS.md,
   * PENDING-tallyinterfacing-sendrequesttotally-relay-500-masks-404-BACKEND
   * -- the backend can in fact never emit 404, only 200 or 500, a real
   * backend defect reported there, not fixed here). */
  sendRequestToTally(tallyIp: string, tallyPortNo: string, requestXmlString: string): Observable<ApiResponse<string> & { code?: string }> {
    return this.http.post<ApiResponse<string> & { code?: string }>(`${this.base}/api/global/sendRequestToTally`, {
      tallyIp,
      tallyPortNo,
      requestXmlString,
    });
  }

  /** Direct-to-Tally leg used when `isTallyLocalHost === 'Y'` (this dev
   * environment's default, since the only seeded unit config has
   * `is_tally_local_host` NULL) -- a raw cross-origin POST straight to the
   * locally-configured IP:port, matching legacy's own `$http.post(url,
   * data, config)` exactly (same explicit XML headers, no auth headers).
   * This external dependency is never live in this dev environment by
   * design -- expected to reject/error, handled gracefully by the caller. */
  sendXmlDirectToTally(url: string, xml: string): Observable<string> {
    return this.http.post(url, xml, {
      headers: { Accept: 'application/xml', 'Content-Type': 'application/xml' },
      responseType: 'text',
    });
  }
}
