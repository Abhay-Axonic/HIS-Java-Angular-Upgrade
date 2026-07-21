import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  QmsKioskDetail,
  QmsKioskRow,
  QmsKioskSavePayload,
  QmsKioskStatusPayload,
  QmsKioskUpdatePayload,
} from './qms-kiosk-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/qms/QMSkioskMasterController.js -- all six
 * calls are POST in legacy (`GenericService.serviceAction("POST", ...)`),
 * including the two "get" endpoints -- kept as POST here for parity rather
 * than "corrected" to GET. `BASE_URL`/`ROOT_URL` are both `''` in this
 * deployment (resources/assets/js/config/url.config.js), so these are the
 * complete paths, not guessed prefixes.
 */
@Injectable({ providedIn: 'root' })
export class QmsKioskMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organisationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, QmsKioskRow>> {
    return this.http.post<ApiResponse<never, QmsKioskRow>>(`${this.base}/api/pharmacy/getQMSMasterList`, {
      organisationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organisationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/pharmacy/getQMSMasterCount`, {
      organisationId,
      unitId,
    });
  }

  getById(id: number, organisationId: number, unitId: number): Observable<ApiResponse<never, QmsKioskDetail>> {
    return this.http.post<ApiResponse<never, QmsKioskDetail>>(`${this.base}/api/pharmacy/getQMSMasterById`, {
      qmsKioskMasterId: id,
      organisationId,
      unitId,
    });
  }

  create(payload: QmsKioskSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/saveQMSMaster`, payload);
  }

  update(payload: QmsKioskUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/updateQMSMaster`, payload);
  }

  updateStatus(payload: QmsKioskStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/pharmacy/updateQMSMasterStatus`, payload);
  }
}
