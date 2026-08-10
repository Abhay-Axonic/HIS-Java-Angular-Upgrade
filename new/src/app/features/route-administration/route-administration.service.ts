import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  RouteOfAdministrationRow,
  RouteOfAdministrationSavePayload,
} from './route-administration.models';

@Injectable({ providedIn: 'root' })
export class RouteAdministrationService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/global/route/of/administration/{orgId} — raw list envelope. */
  list(orgId: number): Observable<RouteOfAdministrationRow[]> {
    return this.http.get<RouteOfAdministrationRow[]>(`${this.base}/api/global/route/of/administration/${orgId}`);
  }

  /** GET /api/global/route/of/administration/{orgId}/{id} — byId envelope. */
  byId(orgId: number, id: number): Observable<RouteOfAdministrationRow> {
    return this.http.get<RouteOfAdministrationRow>(`${this.base}/api/global/route/of/administration/${orgId}/${id}`);
  }

  /** POST /api/global/route/of/administration/{orgId}/{userId} — create (org-safe server-side). */
  save(orgId: number, userId: number, payload: { code: string; description: string; status: boolean }): Observable<{ id?: number; message?: string }> {
    return this.http.post<{ id?: number; message?: string }>(
      `${this.base}/api/global/route/of/administration/${orgId}/${userId}`,
      payload
    );
  }

  /** PATCH /api/global/route/of/administration/{orgId}/{id} — update/status (org-safe server-side). */
  update(orgId: number, id: number, payload: { code?: string; description?: string; status?: boolean }): Observable<{ id?: number; message?: string }> {
    return this.http.patch<{ id?: number; message?: string }>(
      `${this.base}/api/global/route/of/administration/${orgId}/${id}`,
      payload
    );
  }
}
