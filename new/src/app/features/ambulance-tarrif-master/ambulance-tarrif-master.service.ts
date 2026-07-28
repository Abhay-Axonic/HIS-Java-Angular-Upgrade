import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AmbulanceTarrifDetail,
  AmbulanceTarrifRow,
  AmbulanceTarrifSavePayload,
  AmbulanceTarrifStatusPayload,
  AmbulanceTarrifUpdatePayload,
} from './ambulance-tarrif-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js:70-75 and
 * resources/assets/js/controller/lis/global/ambulanceTarrifController.js --
 * not guessed. Legacy calls every one of these through
 * `GenericService.serviceAction("POST", URI, data)` regardless of the
 * semantic operation (list/get/create/update/status-change all POST) --
 * reproduced verbatim here rather than "upgrading" any of them to
 * GET/PUT/PATCH, since that's not a real REST contract on the backend, just
 * this project's generic POST-everything dispatcher.
 *
 * `getList`'s response carries BOTH the page of rows (`listObject`) AND the
 * total row count (`object`) in a single call -- the backend
 * (`AmbulanceTarrifDaoImpl.getAmbulanceTarrifList`) runs a second, unpaged
 * query server-side just to compute that count, but the frontend only ever
 * makes the one call. The legacy controller's own separate
 * `COUNTAMBULANCETARRIFMASTER` call is commented-out dead code
 * (ctrl:112-123), never actually invoked. Reproduced as one call, not two.
 *
 * Deliberately NOT reproduced: legacy's `init()` (ctrl:52-67) also fires a
 * `GETAMBULANCETARRIFACTIVELIST` call into `$scope.ambulanceTypeList` on
 * every page load/reset. That list is never read anywhere in the current
 * view -- the one `<select>` that would have consumed it (a dropdown for
 * "Ambulance Type") is commented out in the HTML (view:14-15), replaced by
 * a free-text input bound straight to `ambulanceTarrif.ambulanceType`. It's
 * live, non-dead code that runs on every load, but has zero observable
 * effect on the page (nothing renders from it, nothing reads it). Omitted
 * here as inert background traffic, not a behavioral change -- flagged for
 * the record in the component's class doc.
 */
@Injectable({ providedIn: 'root' })
export class AmbulanceTarrifMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<number, AmbulanceTarrifRow>> {
    return this.http.post<ApiResponse<number, AmbulanceTarrifRow>>(`${this.base}/adt/getAmbulanceTarrifList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getSingle(ambulanceTarrifMasterId: number, organizationId: number): Observable<ApiResponse<never, AmbulanceTarrifDetail>> {
    return this.http.post<ApiResponse<never, AmbulanceTarrifDetail>>(`${this.base}/adt/getSingleAmbulanceTarrif`, {
      ambulanceTarrifMasterId,
      organizationId,
    });
  }

  save(payload: AmbulanceTarrifSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveAmbulanceTarrif`, payload);
  }

  update(payload: AmbulanceTarrifUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateAmbulanceTarrif`, payload);
  }

  changeStatus(payload: AmbulanceTarrifStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/changeAmbulanceTarrifStatus`, payload);
  }
}
