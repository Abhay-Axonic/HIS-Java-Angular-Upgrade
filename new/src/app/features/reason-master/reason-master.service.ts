import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ReasonDetail,
  ReasonRow,
  ReasonSavePayload,
  ReasonStatusPayload,
  ReasonTypeOption,
  ReasonUpdatePayload,
} from './reason-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/global.url.config.js:139-144 and
 * resources/assets/js/controller/master/global/common/reasonMasterController.js.
 * BASE_URL/ROOT_URL are both "" in this deployment (url.config.js), so
 * these are the literal relative paths legacy calls.
 *
 * Confirmed real, live Controller -> Service -> DAO chain for every one of
 * these endpoints (`ReasonMasterController.java` ->
 * `ReasonMasterServiceImpl.java` -> `ReasonMasterDaoImpl.java`, all under
 * legacy/Global/src/main/java/com/param/global/org/common/) -- including
 * the `getReasonTypeList` dropdown source, which is `reasonTypeMaster`'s
 * own list endpoint. See reason-master.ts's class doc comment: that shared
 * endpoint's real backend ignores organizationId/pagination entirely, so
 * this dropdown will list every organization's reason types too, not a
 * bug in reasonMaster's own code but a real, inherited backend defect.
 */
@Injectable({ providedIn: 'root' })
export class ReasonMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:70-87 (GETREASONTYPEMASTERLIST) -- POST { organizationId }. Feeds
  // the "Reason Type" dropdown. See class doc comment: real backend
  // ignores organizationId and returns every organization's reason types.
  getReasonTypeOptions(organizationId: number): Observable<ApiResponse<never, ReasonTypeOption>> {
    return this.http.post<ApiResponse<never, ReasonTypeOption>>(`${this.base}/api/common/getReasonTypeMasterList`, {
      organizationId,
    });
  }

  // ctrl:99-152 (GETREASONMASTERLIST) -- POST { organizationId, offset,
  // noOfRecordsPerPage }. Correctly org-scoped and paginated on the real
  // backend (unlike reasonTypeMaster's own list endpoint above).
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, ReasonRow>> {
    return this.http.post<ApiResponse<never, ReasonRow>>(`${this.base}/api/common/getReasonMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:136-151 (GETREASONMASTERCOUNT) -- POST { organizationId }; count
  // returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getReasonMasterCount`, { organizationId });
  }

  // ctrl:213-273 (SAVEREASONMASTER) -- POST, real create; backend rejects a
  // case-insensitive duplicate (code+desc+reasonTypeId+org) via
  // getReasonMasterByName.
  save(payload: ReasonSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveReasonMaster`, payload);
  }

  // ctrl:276-303 (GETREASONMASTERBYID) -- GET
  // /api/common/getReasonMasterById/{reasonMasterId}/{orgId};
  // response.data.listObject[0] is the record.
  getById(reasonMasterId: number, organizationId: number): Observable<ApiResponse<never, ReasonDetail>> {
    return this.http.get<ApiResponse<never, ReasonDetail>>(
      `${this.base}/api/common/getReasonMasterById/${reasonMasterId}/${organizationId}`,
    );
  }

  // ctrl:349-400 (UPDATEREASONMASTER) -- POST, real update; backend rejects
  // a case-insensitive duplicate (excluding self) via
  // getReasonMasterByNameNotById.
  update(payload: ReasonUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateReasonMaster`, payload);
  }

  // ctrl:306-346 (UPDATEREASONMASTERSTATUS) -- POST, real status toggle.
  updateStatus(payload: ReasonStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateReasonMasterStatus`, payload);
  }
}
