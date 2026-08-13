import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { TrustRow, FundCollectionRow } from './fund-status-dashboard.models';

@Injectable({ providedIn: 'root' })
export class FundStatusDashboardService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  getActiveTrustList(orgId: number, unitId: number): Observable<{ listObject: TrustRow[] | null }> {
    return this.http.post<{ listObject: TrustRow[] | null }>(
      `${this.base}/api/billing/getActiveTrustList`,
      { orgId, unitId }
    );
  }

  getTrustList(orgId: number, unitId: number): Observable<{ listObject: TrustRow[] | null }> {
    return this.http.get<{ listObject: TrustRow[] | null }>(
      `${this.base}/api/billing/getTrustList/${orgId}/${unitId}`
    );
  }

  getFundCollectionListByTrust(orgId: number, unitId: number): Observable<{ listObject: FundCollectionRow[] | null }> {
    return this.http.post<{ listObject: FundCollectionRow[] | null }>(
      `${this.base}/api/billing/getFundCollectionListByTrust/${orgId}/${unitId}`,
      {}
    );
  }
}
