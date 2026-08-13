import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { StoreRow } from './store-stock-dashboard.models';

@Injectable({ providedIn: 'root' })
export class StoreStockDashboardService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  getStores(orgId: number, unitId: number, userId: number): Observable<StoreRow[]> {
    return this.http.get<StoreRow[]>(
      `${this.base}/api/global/user/stores/${orgId}/${unitId}/${userId}`
    );
  }
}
