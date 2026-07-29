import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ChecklistItemDetail,
  ChecklistItemSuggestion,
  ChecklistMasterRow,
  ChecklistMasterSavePayload,
  ChecklistMasterSaveResponse,
  ChecklistMasterStatusPayload,
  OtimsRole,
} from './checklist-master.models';

/**
 * Endpoint paths taken verbatim from `checklistMasterController.js`
 * (legacy/HIS-WAR/.../controller/otims/global/checklistMasterController.js).
 *
 * Two of the six URLs below contain a literal double slash, kept EXACTLY as
 * legacy sends them (both confirmed live 200 by the orchestrating session
 * before this build started) -- investigated at the source level, not
 * guessed, with two DIFFERENT root causes:
 *
 * 1. `/api/global//getActiveOTRoleList` (ctrl:83) -- NOT a frontend typo.
 *    `UserRoleMapperContrtoller.java:22` declares
 *    `@RequestMapping("api/global/")` (class-level path with a TRAILING
 *    slash baked in); combined with the method's own
 *    `@RequestMapping(value = "/getActiveOTRoleList", ...)` (line 209,
 *    leading slash), Spring's real, resolved mapping for this endpoint
 *    genuinely IS `api/global//getActiveOTRoleList`. The frontend's double
 *    slash is a correct mirror of an odd-but-real backend route, not a
 *    frontend bug.
 * 2. `/global/otims//getChecklistMasterDetailsByMasterId` (ctrl:281) -- IS a
 *    genuine frontend copy/paste artifact. `ChecklistMasterController.java`
 *    (`@RequestMapping("/global/otims")`, no trailing slash) + this method's
 *    own `/getChecklistMasterDetailsByMasterId` (line 58) resolve to a
 *    normal single-slash path server-side -- there is no backend cause for
 *    the extra slash here. It works live only because Spring/Tomcat's
 *    default URL handling tolerates the extra separator; it is not masking
 *    a routing problem, just a harmless typo left in the request URL.
 *
 * Both are preserved verbatim rather than "corrected" to the logically
 * equivalent single-slash form -- changing an already-confirmed-working
 * endpoint string is out of this build's scope (see this screen's own
 * class doc comment for the full disclosure).
 */
@Injectable({ providedIn: 'root' })
export class ChecklistMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:76-89 (otimsRole()).
  getActiveOtRoleList(organisationId: number, unitId: number): Observable<ApiResponse<never, OtimsRole>> {
    return this.http.post<ApiResponse<never, OtimsRole>>(`${this.base}/api/global//getActiveOTRoleList`, {
      organisationId,
      unitId,
    });
  }

  // ctrl:93-116 (getChecklistMasterList()) -- also used, unchanged, for
  // page-navigation reloads (ctrl:318-342, `masterList`, a near-duplicate
  // that hits the SAME endpoint but does not re-read the count -- see the
  // component for the two call sites).
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<number, ChecklistMasterRow>> {
    return this.http.post<ApiResponse<number, ChecklistMasterRow>>(`${this.base}/global/otims/getAllChecklistMasterList`, {
      unitId,
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:120-132 (getCheckItemListByName()) -- autocomplete search against
  // the SEPARATE checklist-item master table.
  searchChecklistItemName(
    description: string,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, ChecklistItemSuggestion>> {
    return this.http.post<ApiResponse<never, ChecklistItemSuggestion>>(`${this.base}/global/otims/searchChecklistItemName`, {
      description,
      organizationId,
      unitId,
    });
  }

  // ctrl:172-233 (saveCheckListMaster()) -- response type widened beyond the
  // shared ApiResponse contract, see ChecklistMasterSaveResponse's doc
  // comment in checklist-master.models.ts.
  save(payload: ChecklistMasterSavePayload): Observable<ChecklistMasterSaveResponse> {
    return this.http.post<ChecklistMasterSaveResponse>(`${this.base}/global/otims/saveChecklistMaster`, payload);
  }

  // ctrl:235-270 (updateStatus()).
  updateStatus(payload: ChecklistMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/otims/updateChecklistMasterStatus`, payload);
  }

  // ctrl:274-294 (getChecklistNameListById()) -- double slash preserved,
  // see class doc comment above (root cause 2: harmless frontend typo).
  getChecklistMasterDetailsByMasterId(
    organizationId: number,
    unitId: number,
    checklistMasterId: number,
  ): Observable<ApiResponse<never, ChecklistItemDetail>> {
    return this.http.post<ApiResponse<never, ChecklistItemDetail>>(
      `${this.base}/global/otims//getChecklistMasterDetailsByMasterId`,
      { organizationId, unitId, checklistMasterId },
    );
  }
}
