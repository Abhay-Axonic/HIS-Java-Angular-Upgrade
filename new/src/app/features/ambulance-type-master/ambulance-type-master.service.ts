import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AmbulanceTypeDetail,
  AmbulanceTypeRow,
  AmbulanceTypeSavePayload,
  AmbulanceTypeStatusPayload,
  AmbulanceTypeUpdatePayload,
} from './ambulance-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js:63-68 and
 * resources/assets/js/controller/lis/global/ambulanceTypeController.js --
 * not guessed. Legacy calls every one of these through
 * `GenericService.serviceAction("POST", URI, data)` regardless of the
 * semantic operation (list/get/create/update/status-change all POST) --
 * reproduced verbatim here rather than "upgrading" any of them to
 * GET/PUT/PATCH, since that's not a real REST contract on the backend,
 * just this project's generic POST-everything dispatcher.
 *
 * `getList`'s response carries BOTH the page of rows (`listObject`) AND the
 * total row count (`object`) in a single call -- legacy's separate
 * `COUNTAMBULANCETYPEMASTER` endpoint/call is commented out dead code in
 * the controller (ctrl:96-107), never actually invoked. Reproduced as one
 * call, not two, matching what legacy actually does over the wire.
 */
@Injectable({ providedIn: 'root' })
export class AmbulanceTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<number, AmbulanceTypeRow>> {
    return this.http.post<ApiResponse<number, AmbulanceTypeRow>>(`${this.base}/adt/getAmbulanceTypeList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getSingle(ambulanceTypeMasterId: number, organizationId: number): Observable<ApiResponse<never, AmbulanceTypeDetail>> {
    return this.http.post<ApiResponse<never, AmbulanceTypeDetail>>(`${this.base}/adt/getSingleAmbulanceType`, {
      ambulanceTypeMasterId,
      organizationId,
    });
  }

  save(payload: AmbulanceTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveAmbulanceType`, payload);
  }

  update(payload: AmbulanceTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateAmbulanceType`, payload);
  }

  changeStatus(payload: AmbulanceTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/changeAmbulanceStatus`, payload);
  }
}
