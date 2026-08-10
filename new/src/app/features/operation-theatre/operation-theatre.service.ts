import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  OperationTheatreRow,
  OperationTheatreListPayload,
} from './operation-theatre.models';

@Injectable({ providedIn: 'root' })
export class OperationTheatreService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** POST /api/otims/getOperationTheatreList — legacy payload shape
   * {orgId, unitId, offset, noOfRecordsPerPage} (NOT organizationId). */
  list(payload: OperationTheatreListPayload): Observable<{ listObject?: OperationTheatreRow[] }> {
    return this.http.post<{ listObject?: OperationTheatreRow[] }>(
      `${this.base}/api/otims/getOperationTheatreList`,
      payload
    );
  }

  /** POST /api/otims/getActiveOperationTheatreTypeList — the type feed. */
  types(payload: { orgId: number; unitId: number }): Observable<{ listObject?: unknown[] }> {
    return this.http.post<{ listObject?: unknown[] }>(
      `${this.base}/api/otims/getActiveOperationTheatreTypeList`,
      payload
    );
  }
}
