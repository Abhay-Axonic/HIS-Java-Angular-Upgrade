import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { RejectionMasterDetail, RejectionMasterListRow } from './rejection-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/common/rejectionMasterController.js
 * and resources/assets/js/config/global.url.config.js:102-107 -- not guessed.
 * All 6 confirmed against a real Java implementation:
 * RejectionMasterController.java (@RequestMapping("api/common")) ->
 * RejectionMasterServiceImpl -> RejectionMasterDaoImpl. No `unit_id`/
 * `organization_id` custom headers needed -- this controller takes plain
 * `@RequestBody`/`@PathVariable` params only, unlike systemMaster's.
 */
@Injectable({ providedIn: 'root' })
export class RejectionMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, RejectionMasterListRow>> {
    return this.http.post<ApiResponse<never, RejectionMasterListRow>>(`${this.base}/api/common/getRejectionMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getRejectionMasterCount`, { organizationId });
  }

  getById(rejectId: number, organizationId: number): Observable<ApiResponse<never, RejectionMasterDetail>> {
    return this.http.get<ApiResponse<never, RejectionMasterDetail>>(
      `${this.base}/api/common/getRejectionMasterById/${rejectId}/${organizationId}`,
    );
  }

  create(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveRejectionMaster`, payload);
  }

  update(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateRejectionMaster`, payload);
  }

  updateStatus(payload: {
    id: number;
    isActive: boolean;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateRejectionMasterStatus`, payload);
  }
}
