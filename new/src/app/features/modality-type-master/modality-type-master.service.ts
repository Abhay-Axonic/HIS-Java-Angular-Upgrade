import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ModalityTypeDetail,
  ModalityTypeObj,
  ModalityTypeRow,
  ModalityTypeStatusPayload,
  SpecialityOption,
} from './modality-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/opd/unit/modalityTypeMasterController.js
 * and resources/assets/js/config/opd.url.config.js:44-50 /
 * adt.url.config.js:19 -- not guessed. Every call in the legacy controller
 * is a POST via GenericService/BillingGenericService.serviceAction, so this
 * service uses POST uniformly rather than "upgrading" reads to GET.
 *
 * All 6 `opd.url.config.js` constants (lines 44-50) are built as
 * `API + OPD + "/..."` where `API = "/api"` and `OPD = "/opd"`
 * (opd.url.config.js:6,14) -- i.e. the real paths are `/api/opd/...`, not
 * bare `/opd/...`. Caught live during Step 5 browser verification: a bare
 * `/opd/...` path 404s against the real backend (confirmed via Tomcat's own
 * 404 page, not just the dev proxy). Fixed here before merge -- this is a
 * rebuild implementation error, not a legacy behavior to preserve.
 */
@Injectable({ providedIn: 'root' })
export class ModalityTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:85-98 -- BillingGenericService in legacy, same POST contract.
  getActiveSpecialityList(unitId: number, organizationId: number): Observable<ApiResponse<never, SpecialityOption>> {
    return this.http.post<ApiResponse<never, SpecialityOption>>(`${this.base}/adt/getActiveSpecialityList`, {
      unitId,
      organizationId,
    });
  }

  // ctrl:116-149.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ModalityTypeRow>> {
    return this.http.post<ApiResponse<never, ModalityTypeRow>>(`${this.base}/api/opd/getModalityTypeMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:123-165 -- count response in `object`.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/opd/getModalityTypeCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:222-277 -- real create, posts the shared ModalityTypeObj verbatim.
  save(payload: ModalityTypeObj): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/saveModalityTypeMaster`, payload);
  }

  // ctrl:285-309.
  getById(
    modalityTypeId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, ModalityTypeDetail>> {
    return this.http.post<ApiResponse<never, ModalityTypeDetail>>(`${this.base}/api/opd/getModalityTypeById`, {
      modalityTypeId,
      organizationId,
      unitId,
    });
  }

  // ctrl:354-400 -- real update, same shared ModalityTypeObj as save().
  update(payload: ModalityTypeObj): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateModalityTypeMaster`, payload);
  }

  // ctrl:312-351 -- real status toggle.
  updateStatus(payload: ModalityTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateStatusForModalityType`, payload);
  }
}
