import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { EmployeeTypeaheadResult, RoleTypeaheadResult, UserAuthenticationRow } from './user-authentication.models';

/**
 * Endpoint paths and payload shapes taken directly from
 * userAuthenticationMasterController.js -- not guessed. See
 * docs/DECISION_NEEDED_UserAuthenticationPlaintextPasswordDisclosure.md for
 * the confirmed backend behavior of `getUserAuthenticationList` (DAO:
 * legacy/Global/.../UserRoleMapperDaoImpl.java:306-375): the raw HTTP
 * response includes a decrypted, plaintext `password` field per row. That
 * is an existing backend fact this rebuild does not change. This service's
 * return type (`ApiResponse<never, UserAuthenticationRow>`) simply never
 * declares `password` on the row shape it hands back to callers -- see
 * user-authentication.models.ts for why.
 *
 * Field-name note: this legacy controller is inconsistent about the
 * organisation-id key across its own three calls -- `searchEmployeeByName`
 * sends `organizationId` (correct spelling), while `searchRoleByName` and
 * `getUserAuthenticationList` both send `organisationId` (typo). Kept
 * verbatim per-endpoint, matching billing-group-master.service.ts's
 * precedent for legacy's own inconsistent API contracts -- not something
 * this rebuild gets to silently "fix".
 */
@Injectable({ providedIn: 'root' })
export class UserAuthenticationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /**
   * userAuthenticationMasterController.js:134-158 (`getUserAuthenticatedList`).
   * Legacy loads the full unit-scoped list (no offset/page params sent) and
   * paginates client-side via PaginationService -- mirrored the same way
   * here, not with a server-side offset/count pair like billing-group-master.
   */
  getList(
    organisationId: number,
    unitId: number,
    roleName: string,
    empName: string,
  ): Observable<ApiResponse<never, UserAuthenticationRow>> {
    return this.http.post<ApiResponse<never, UserAuthenticationRow>>(
      `${this.base}/api/global/getUserAuthenticationList`,
      { organisationId, unitId, roleName, empName },
    );
  }

  /** userAuthenticationMasterController.js:84-98 (`getListByUserName`). */
  searchEmployeeByName(
    organizationId: number,
    unitId: number,
    firstName: string,
  ): Observable<ApiResponse<never, EmployeeTypeaheadResult>> {
    return this.http.post<ApiResponse<never, EmployeeTypeaheadResult>>(
      `${this.base}/api/global/searchEmployeeByName`,
      { organizationId, unitId, firstName },
    );
  }

  /** userAuthenticationMasterController.js:111-125 (`getListByRoleName`). */
  searchRoleByName(
    organisationId: number,
    unitId: number,
    roleName: string,
  ): Observable<ApiResponse<never, RoleTypeaheadResult>> {
    return this.http.post<ApiResponse<never, RoleTypeaheadResult>>(
      `${this.base}/api/global/searchRoleByName`,
      { organisationId, unitId, roleName },
    );
  }
}
