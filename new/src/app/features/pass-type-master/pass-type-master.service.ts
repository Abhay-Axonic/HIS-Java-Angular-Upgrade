import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PassTypeDetail,
  PassTypeRow,
  PassTypeSavePayload,
  PassTypeStatusPayload,
  PassTypeUpdatePayload,
} from './pass-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/visitorTypeMasterController.js
 * -- not guessed. BASE_URL/ROOT_URL are both "" (resources/assets/js/config/url.config.js),
 * so these are the literal relative paths legacy calls, all under
 * `/api/unit/visitor`. moduleId 821 (app.js:19937-19960 resolve block,
 * confirmed live).
 */
@Injectable({ providedIn: 'root' })
export class PassTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:72-114 (initVisitorTypeMasterList) -- POST { unitId, organizationId, offset, noOfRecordsPerPage }.
  getPassTypeMasterList(
    unitId: number,
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, PassTypeRow>> {
    return this.http.post<ApiResponse<never, PassTypeRow>>(`${this.base}/api/unit/visitor/getPassTypeMasterList`, {
      unitId,
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:89-113 -- POST { unitId, organizationId }, count returned in `object`.
  getPassTypeMasterCount(unitId: number, organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/unit/visitor/getPassTypeMasterCount`, {
      unitId,
      organizationId,
    });
  }

  // ctrl:116-143 (getVisitorTypeMasterList) -- POST, used only by real
  // Next/Previous/page-number navigation (setPage(page,'true')), honors the
  // requested offset/size. Distinct from getPassTypeMasterList above, which
  // is what initial load and the "Show N entries" change both call.
  getPassTypeMasterPage(
    unitId: number,
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, PassTypeRow>> {
    return this.http.post<ApiResponse<never, PassTypeRow>>(`${this.base}/api/unit/visitor/getPassTypeMasterList`, {
      unitId,
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:194-223 -- POST, real create (only reached once popUpFlag flips
  // false via PopupOkBtn -- see class doc comment).
  savePassTypeMaster(payload: PassTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/visitor/savePassTypeMaster`, payload);
  }

  // ctrl:239-252 -- POST (legacy posts a `{ visitorPassTypeId }` body, not a
  // path param); response.data.listObject[0] is the record.
  getPassTypeMasterById(visitorPassTypeId: number): Observable<ApiResponse<never, PassTypeDetail>> {
    return this.http.post<ApiResponse<never, PassTypeDetail>>(`${this.base}/api/unit/visitor/getPassTypeMasterById`, {
      visitorPassTypeId,
    });
  }

  // ctrl:304-307 -- POST, real update (no confirm-modal step, unlike create).
  updatePassTypeMaster(payload: PassTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/visitor/updatePassTypeMaster`, payload);
  }

  // ctrl:270-273 -- POST, real status toggle.
  updatePassTypeMasterStatus(payload: PassTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/visitor/updatePassTypeMasterStatus`, payload);
  }
}
