import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { RackOption, RackRow } from './rack-master.models';

@Injectable({ providedIn: 'root' })
export class RackMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getStores(unitId: number, userId: number): Observable<RackOption[]> {
    return this.http
      .get<RackOption[] | { listObject?: RackOption[]; stores?: Array<RackOption & { storeId?: number }> }>(
        `${this.base}/api/global/store/getStoreListById/${userId}/${unitId}`,
      )
      .pipe(
        map((res) => {
          const rows = Array.isArray(res) ? res : res.listObject ?? res.stores ?? [];
          return rows.map((store) => ({ ...store, id: store.id ?? store.storeId, store: store.store ?? store.storeName }));
        }),
      );
  }

  getRacks(unitId: number, orgId: number): Observable<RackRow[]> {
    return this.http.get<RackRow[]>(`${this.base}/api/units/rack/${unitId}/${orgId}`);
  }

  getRack(id: number): Observable<RackRow> {
    return this.http.get<RackRow>(`${this.base}/api/units/rack/${id}`);
  }

  create(payload: Record<string, unknown>): Observable<{ id?: number; message?: string }> {
    return this.http.post<{ id?: number; message?: string }>(`${this.base}/api/units/rack/`, payload);
  }

  update(id: number, payload: Record<string, unknown>): Observable<{ message?: string }> {
    return this.http.patch<{ message?: string }>(`${this.base}/api/units/rack/${id}`, payload);
  }

  toggle(id: number, payload: Record<string, unknown>): Observable<{ message?: string }> {
    return this.http.patch<{ message?: string }>(`${this.base}/api/units/rack/${id}`, payload);
  }
}
