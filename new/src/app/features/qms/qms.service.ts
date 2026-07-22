import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { QmsActiveDept, QmsKioskOption, QmsPrnFormatEntry } from './qms.models';

/**
 * Endpoint paths taken directly from QMSMasterController.js. All 4 POST/GET
 * calls below are confirmed live (Step 0 backend-existence check, this
 * batch) -- including `getQMSPrnFormat`, which was previously 500ing on a
 * missing DB column and has since been fixed and confirmed 200.
 *
 * `authInterceptor` (new/src/app/core/auth.interceptor.ts) is already wired
 * globally -- every call here gets Authorization/userId/unitId automatically.
 */
@Injectable({ providedIn: 'root' })
export class QmsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveQmsMasterList(organisationId: number, unitId: number): Observable<ApiResponse<never, QmsKioskOption>> {
    return this.http.post<ApiResponse<never, QmsKioskOption>>(`${this.base}/api/pharmacy/getActiveQMSMasterList`, {
      organisationId,
      unitId,
    });
  }

  getQmsPrnFormat(unitId: number): Observable<ApiResponse<never, QmsPrnFormatEntry>> {
    return this.http.post<ApiResponse<never, QmsPrnFormatEntry>>(
      `${this.base}/api/unit/applicationConfiguration/getQMSPrnFormat`,
      { unitId },
    );
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

  saveToken(payload: {
    qmsKioskMasterId: number;
    startLetter?: string;
    startNumber?: number;
    qmsDeptMasterId: number;
    createdBy: number;
    updatedBy: number;
    organisationId: number;
    unitId: number;
  }): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/pharmacy/saveToken`, payload);
  }

  /**
   * Branding image, image type `1` for QMS (vs. `3` for `display` -- see
   * DisplayService.getScreenImage). Publicly reachable with no auth check --
   * a separate, already-documented security gap, not something to fix here.
   */
  getScreenImage(orgId: number): Observable<ApiResponse<{ imageURL: string | null }>> {
    return this.http.get<ApiResponse<{ imageURL: string | null }>>(
      `${this.base}/api/unit/applicationConfiguration/screen-images/${orgId}/1`,
    );
  }
}
