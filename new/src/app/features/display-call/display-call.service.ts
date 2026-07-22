import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { QmsKioskOption } from './display-call.models';

/**
 * `POST /api/pharmacy/getActiveQMSMasterList` -- the ONE real backend call
 * this screen makes (ctrl:59-70, a hardcoded literal string, not a named
 * `*.url.config.js` constant). Confirmed against a real Java
 * Controller -> Service -> DAO chain during this batch's Step 0
 * backend-existence check (`QMSKioskMasterController.java:86-93` ->
 * `QMSKioskMasterServiceImpl.java:97-99` -> `QMSKioskMasterDaoImpl.java:196-207`,
 * named query `GET_ACTIVE_QMS_MASTER_LIST` on `m_qms_kiosk_master`) -- see
 * docs/ANGULAR_PENDING_FINDINGS.md for full citations.
 *
 * `authInterceptor` (new/src/app/core/auth.interceptor.ts) is already
 * wired globally -- every HttpClient call here gets the Authorization/
 * userId/unitId headers automatically.
 */
@Injectable({ providedIn: 'root' })
export class DisplayCallService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveQmsMasterList(organisationId: number, unitId: number): Observable<ApiResponse<never, QmsKioskOption>> {
    return this.http.post<ApiResponse<never, QmsKioskOption>>(`${this.base}/api/pharmacy/getActiveQMSMasterList`, {
      organisationId,
      unitId,
    });
  }
}
