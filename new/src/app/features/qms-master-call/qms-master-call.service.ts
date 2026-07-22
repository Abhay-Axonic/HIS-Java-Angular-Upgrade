import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { QmsActiveDept, QmsKioskOption } from './qms-master-call.models';

/**
 * Both endpoints below are shared with `QMS` (see qms.service.ts) and
 * `displayCall` -- confirmed live during this batch's Step 0
 * backend-existence check.
 *
 * `authInterceptor` (new/src/app/core/auth.interceptor.ts) is already wired
 * globally -- every call here gets Authorization/userId/unitId automatically.
 */
@Injectable({ providedIn: 'root' })
export class QmsMasterCallService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveQmsMasterList(organisationId: number, unitId: number): Observable<ApiResponse<never, QmsKioskOption>> {
    return this.http.post<ApiResponse<never, QmsKioskOption>>(`${this.base}/api/pharmacy/getActiveQMSMasterList`, {
      organisationId,
      unitId,
    });
  }

  getActiveDeptListByKioskId(
    organisationId: number,
    unitId: number,
    qmsKioskMasterId: number | string,
  ): Observable<ApiResponse<never, QmsActiveDept>> {
    return this.http.post<ApiResponse<never, QmsActiveDept>>(
      `${this.base}/api/pharmacy/getActiveDeptListByKioskId`,
      { organisationId, unitId, qmsKioskMasterId },
    );
  }
}
