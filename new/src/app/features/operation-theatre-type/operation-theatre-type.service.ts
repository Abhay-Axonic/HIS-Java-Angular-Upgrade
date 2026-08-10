import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  OperationTheatreTypeRow,
  OperationTheatreTypeListPayload,
} from './operation-theatre-type.models';

@Injectable({ providedIn: 'root' })
export class OperationTheatreTypeService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** POST /api/otims/getOperationTheatreTypeList — legacy payload {orgId, unitId}. */
  list(
    payload: OperationTheatreTypeListPayload
  ): Observable<{ listObject?: OperationTheatreTypeRow[] }> {
    return this.http.post<{ listObject?: OperationTheatreTypeRow[] }>(
      `${this.base}/api/otims/getOperationTheatreTypeList`,
      payload
    );
  }

  /** POST /api/otims/getActiveOperationTheatreTypeList — active feed. */
  active(
    payload: OperationTheatreTypeListPayload
  ): Observable<{ listObject?: OperationTheatreTypeRow[] }> {
    return this.http.post<{ listObject?: OperationTheatreTypeRow[] }>(
      `${this.base}/api/otims/getActiveOperationTheatreTypeList`,
      payload
    );
  }

  /** GET /api/otims/getOperationTheatreTypeById/{id}/{orgId}/{unitId}. */
  byId(
    id: number,
    orgId: number,
    unitId: number
  ): Observable<{ listObject?: OperationTheatreTypeRow[] }> {
    return this.http.get<{ listObject?: OperationTheatreTypeRow[] }>(
      `${this.base}/api/otims/getOperationTheatreTypeById/${id}/${orgId}/${unitId}`
    );
  }

  /** POST /api/otims/saveOperationTheatreType — full DTO (both dates). */
  save(
    dto: OperationTheatreTypeRow
  ): Observable<{ status?: string; code?: string; message?: string }> {
    return this.http.post<{ status?: string; code?: string; message?: string }>(
      `${this.base}/api/otims/saveOperationTheatreType`,
      dto
    );
  }

  /** POST /api/otims/updateOperationTheatreType — full DTO with id. */
  update(
    dto: OperationTheatreTypeRow
  ): Observable<{ status?: string; code?: string; message?: string }> {
    return this.http.post<{ status?: string; code?: string; message?: string }>(
      `${this.base}/api/otims/updateOperationTheatreType`,
      dto
    );
  }

  /** POST /api/otims/updateOperationTheatreTypeStatus — status toggle. */
  status(
    dto: OperationTheatreTypeRow
  ): Observable<{ status?: string; code?: string; message?: string }> {
    return this.http.post<{ status?: string; code?: string; message?: string }>(
      `${this.base}/api/otims/updateOperationTheatreTypeStatus`,
      dto
    );
  }
}
