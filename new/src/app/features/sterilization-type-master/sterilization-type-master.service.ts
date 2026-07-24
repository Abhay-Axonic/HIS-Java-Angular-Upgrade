import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  SterilizationTypeCreatePayload,
  SterilizationTypeRow,
  SterilizationTypeStatusPayload,
  SterilizationTypeUpdatePayload,
} from './sterilization-type-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/cssd.url.config.js` (`CSSD = "/cssd"`, all 4
 * of this screen's own endpoints build on it -- `BASE_URL`/`ROOT_URL` are
 * both empty strings in this deployment). Confirmed real end to end by
 * reading source directly: `SterilizationTypeMasterController.java`
 * (`@RequestMapping("/cssd")`) -> `SterilizationTypeMasterServiceImpl` ->
 * `SterilizationTypeMasterDaoImpl` -> table
 * `cssd.m_sterilization_type_master`. `getSterilizationTypesList`
 * independently confirmed live 200 both in the prior research pass and via
 * a fresh Step 2 golden trace this session (`localhost:8080`, real
 * authenticated session).
 *
 * ================================================================
 * CONFIRMED LIVE BACKEND DEFECT, NOT FIXED HERE (legacy bug, logged, not a
 * rebuild bug) -- `getsterilizationtypeslistcount` 500s UNCONDITIONALLY,
 * on every single call, independent of seed data
 * ================================================================
 * `SterilizationTypeMasterServiceImpl.getSterilizationTypesListCount`
 * (:79-87) is the ONLY method in that service class missing
 * `@Transactional` -- every sibling method (`updateMaster`,
 * `updateMasterStatus`, `getMasterList`, `getActiveMasterList`,
 * `createMaster`) has it. The DAO's own implementation
 * (`SterilizationTypeMasterDaoImpl.getSterilizationTypesListCount:164-181`)
 * calls `sessionFactory.getCurrentSession()`, which requires an ambient
 * Spring-managed Hibernate session bound to the thread -- one that is
 * never opened without `@Transactional` on the calling service method.
 * Live-confirmed via `catalina.out` on real, unmodified legacy
 * (`localhost:8080/#/sterilizationTypeMaster`), captured this session:
 * `org.hibernate.HibernateException: No Session found for current thread`,
 * thrown from `SterilizationTypeMasterDaoImpl.java:167`, on EVERY load,
 * confirmed both before and after live-creating real test rows (id=1,2) --
 * this is NOT a 0-seed-row artifact, it is an always-on crash.
 *
 * Downstream effect, confirmed by reading the controller directly:
 * `$scope.setPage(1,false)` (ctrl:125) -- which is the ONLY code path that
 * ever initializes `$scope.pager` -- lives exclusively inside the count
 * call's success callback (ctrl:120-128), which never fires because the
 * promise always rejects. `$scope.pager` therefore stays `{}` forever, and
 * the view's entire pagination block (`ng-if="pager.pages.length"`,
 * view:131) never renders -- THIS SCREEN HAS NO WORKING PAGINATION UI AT
 * ALL IN REAL, UNMODIFIED LEGACY TODAY, regardless of how many rows exist.
 * This was flagged prominently to the owner mid-session as a
 * higher-severity finding than this batch's other 4 screens (an
 * unconditional crash, not merely a mis-scoped check), matching
 * `CLAUDE.md`'s explicitly named #1 problem class (silently swallowed
 * errors -- legacy's single-argument `.then()` has no rejection handler,
 * so the 500 produces no visible error to the user at all, just a
 * permanently absent pager). Reproduced as-is in the rebuild: `getCount`
 * below calls the SAME real, broken endpoint and will 500 identically:
 * `totalCount`/`hasPager` in `sterilization-type-master.ts` are only ever
 * set inside the count call's OWN success branch, matching legacy's exact
 * observable behavior (list loads and CRUD works via the independent GET
 * list call; the pager section is simply never shown), not a rebuild gap
 * to compensate for.
 */
@Injectable({ providedIn: 'root' })
export class SterilizationTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:91-118 (getSterializationTypelist, the list half) -- GET with
  // query params, NOT a POST-with-body like several sibling screens.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, SterilizationTypeRow>> {
    return this.http.get<ApiResponse<never, SterilizationTypeRow>>(`${this.base}/cssd/getSterilizationTypesList`, {
      params: { organizationId, unitId, offset, noOfRecordsPerPage },
    });
  }

  // ctrl:91-118 (same function, the parallel count half) -- POST
  // {organizationId} only (ctrl:97-99 -- note: NOT unitId, confirmed by
  // reading the controller's own `data2` object literal directly). See
  // class doc comment: this call 500s unconditionally in real legacy
  // today, for an unrelated reason (missing `@Transactional`), reproduced
  // as-is here.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/cssd/getsterilizationtypeslistcount`, { organizationId });
  }

  // ctrl:184-220 (saveSterilization's create branch). See
  // `sterilization-type-master.models.ts`'s doc comment for why `storeId`
  // is deliberately omitted (confirmed inert server-side).
  create(payload: SterilizationTypeCreatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/cssd/createSterilizationType`, payload);
  }

  // ctrl:257-278 (updateSterilization). See
  // `sterilization-type-master.models.ts`'s doc comment for the confirmed,
  // fixed frontend-only bug this rebuild corrects: sending the CURRENT
  // session's real organizationId/unitId (not a stale row-object value).
  update(payload: SterilizationTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/cssd/updateSterilizationType`, payload);
  }

  // ctrl:225-255 (updateStatus).
  updateStatus(payload: SterilizationTypeStatusPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/cssd/updateSterilizationTypeStatus`, payload);
  }
}
