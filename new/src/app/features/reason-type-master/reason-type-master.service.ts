import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ReasonTypeDetail,
  ReasonTypeRow,
  ReasonTypeSavePayload,
  ReasonTypeStatusPayload,
  ReasonTypeUpdatePayload,
} from './reason-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/global.url.config.js:109-114 and
 * resources/assets/js/controller/master/global/common/reasonTypeMasterController.js.
 * BASE_URL/ROOT_URL are both "" in this deployment (url.config.js), so
 * these are the literal relative paths legacy calls.
 *
 * Confirmed real, live Controller -> Service -> DAO chain for every one of
 * these 5 endpoints (`ReasonTypeMasterController.java` ->
 * `ReasonTypeMasterServiceImpl.java` -> `ReasonTypeMasterDaoImpl.java`, all
 * under legacy/Global/src/main/java/com/param/global/org/common/).
 *
 * IMPORTANT, faithfully preserved, NOT fixed here (see class doc comment on
 * reason-type-master.ts and docs/ANGULAR_PENDING_FINDINGS.md draft entry for
 * this batch): `getList()` below calls the real
 * `/api/common/getReasonTypeMasterList` endpoint with the same
 * {organizationId, offset, noOfRecordsPerPage} legacy's own frontend sends
 * -- but the REAL backend query (`GET_REASON_TYPE_MASTER_LIST`,
 * ReasonTypeMaster.java:23-29) has no `WHERE organizationId=...` clause at
 * all, and `ReasonTypeMasterDaoImpl.getReasonTypeMasterList()` (lines
 * 56-72) has its `.setInteger("orgId", ...)`/`.setFirstResult(...)`/
 * `.setMaxResults(...)` calls all commented out. Every call to this
 * endpoint -- from ANY organization, at ANY offset/page-size -- returns the
 * identical full, unpaginated, all-organizations table. This is a real bug
 * in the actual Java backend, not something this Angular service can or
 * should mask with a client-side filter.
 */
@Injectable({ providedIn: 'root' })
export class ReasonTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:79-132 (GETREASONTYPEMASTERLIST) -- POST { organizationId, offset,
  // noOfRecordsPerPage }. See class doc comment: the real backend ignores
  // all three and always returns every row for every organization.
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, ReasonTypeRow>> {
    return this.http.post<ApiResponse<never, ReasonTypeRow>>(`${this.base}/api/common/getReasonTypeMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:117-131 (GETREASONTYPEMASTERCOUNT) -- POST { organizationId };
  // count returned in `object`. Correctly org-scoped on the backend
  // (Hibernate Criteria + Restrictions.eq), unlike getList above.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getReasonTypeMasterCount`, { organizationId });
  }

  // ctrl:193-250 (SAVEREASONTYPEMASTER) -- POST, real create; backend
  // rejects a case-insensitive duplicate (code+desc+org) via
  // getReasonTypeMasterByName.
  save(payload: ReasonTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveReasonTypeMaster`, payload);
  }

  // ctrl:253-278 (GETREASONTYPEMASTERBYID) -- GET
  // /api/common/getReasonTypeMasterById/{reasonTypeId}/{orgId};
  // response.data.listObject[0] is the record.
  getById(reasonTypeId: number, organizationId: number): Observable<ApiResponse<never, ReasonTypeDetail>> {
    return this.http.get<ApiResponse<never, ReasonTypeDetail>>(
      `${this.base}/api/common/getReasonTypeMasterById/${reasonTypeId}/${organizationId}`,
    );
  }

  // ctrl:321-368 (UPDATEREASONTYPEMASTER) -- POST, real update; backend
  // rejects a case-insensitive duplicate (code+desc+org, excluding self) via
  // getReasonTypeMasterByNameNotById.
  update(payload: ReasonTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateReasonTypeMaster`, payload);
  }

  // ctrl:280-318 (UPDATEREASONTYPEMASTERSTATUS) -- POST, real status toggle.
  updateStatus(payload: ReasonTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateReasonTypeMasterStatus`, payload);
  }
}
