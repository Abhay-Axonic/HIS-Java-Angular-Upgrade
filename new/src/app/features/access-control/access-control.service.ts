import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AccessControlRow } from './access-control.models';

/**
 * NO ENDPOINT (noData class, DQ-2026-08-12-005/-019): the legacy
 * accessControlController is a 60-line STUB with zero serviceAction/api/URI
 * references (verified 2026-08-12 — grep count 0). The legacy screens render
 * an empty angular-ui-tree editor with no data path. The service exists only
 * to keep the house pattern; list() is never expected to return rows.
 */
@Injectable({ providedIn: 'root' })
export class AccessControlService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // No backend list endpoint exists (stub controller). Kept as a never-called
  // contract so the component follows the house shape; the component renders
  // the noData state without calling it.
  getList(): Observable<ApiResponse<never, AccessControlRow>> {
    return this.http.get<ApiResponse<never, AccessControlRow>>(`${this.base}/access-control/noop`);
  }
}
