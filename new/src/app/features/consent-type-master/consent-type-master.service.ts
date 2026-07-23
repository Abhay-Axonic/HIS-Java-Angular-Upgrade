import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ConsentTypeDetail,
  ConsentTypeRow,
  ConsentTypeSavePayload,
  ConsentTypeStatusPayload,
  ConsentTypeUpdatePayload,
} from './consent-type-master.models';

/**
 * Endpoint paths per the completed Step 0/1 audit for this batch -- all six
 * confirmed real, live Controller -> Service -> DAO chains under
 * `ConsentTypeMasterController`'s class-level `@RequestMapping("/adt")`
 * (legacy/ADT/.../controller/ConsentTypeMasterController.java:19) -- no
 * `/api` segment, matching encounterTypeMaster/deliveryTypeMaster's own
 * services in this batch. Taken from
 * resources/assets/js/controller/adt/global/consentTypeMasterController.js
 * (396 lines, `consentTypeMasterController`, no `controllerAs`). Legacy's
 * own `GenericService.serviceAction("POST", ...)` calls every one of these
 * six as a POST, including the single-record fetch used by Edit -- reproduced
 * as POST here too, not GET.
 */
@Injectable({ providedIn: 'root' })
export class ConsentTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:87-122 (initConsentTypeMasterList) -- POST {organizationId, unitId,
  // offset, noOfRecordsPerPage}.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ConsentTypeRow>> {
    return this.http.post<ApiResponse<never, ConsentTypeRow>>(`${this.base}/adt/getConsentTypeMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:105-138 (same function, the parallel count call) -- POST
  // {organizationId, unitId}; count returned in `object`.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getConsentTypeCount`, { organizationId, unitId });
  }

  // ctrl:203-267 (saveConsentType) -- POST, real create. The backend's own
  // duplicate-name check is confirmed broken (compares the submitted value
  // as-is against a lowercased stored column) -- NOT fixed here, see the
  // component's class doc comment.
  save(payload: ConsentTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveConsentTypeMaster`, payload);
  }

  // ctrl:270-298 (showUpdateBtn) -- POST {consentTypeMasterId};
  // response.data.listObject[0] is the single record.
  getById(consentTypeMasterId: number): Observable<ApiResponse<never, ConsentTypeDetail>> {
    return this.http.post<ApiResponse<never, ConsentTypeDetail>>(`${this.base}/adt/getConsentTypeById`, {
      consentTypeMasterId,
    });
  }

  // ctrl:342-394 (updateConsentType) -- POST, real update; no `status` field
  // sent, matching legacy exactly.
  update(payload: ConsentTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateConsentTypeMaster`, payload);
  }

  // ctrl:301-339 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: ConsentTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForConsentType`, payload);
  }
}
