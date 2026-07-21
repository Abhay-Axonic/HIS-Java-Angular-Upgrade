import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { AiaDropdowns, AiaRow } from './asset-issue-acceptance.models';

@Injectable({ providedIn: 'root' })
export class AssetIssueAcceptanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdowns(unitId: number, organizationId: number, userId: number): Observable<AiaDropdowns> {
    return this.http.get<AiaDropdowns>(`${this.base}/api/asset/issue/acceptance/dropdowns/${unitId}/${organizationId}/${userId}`);
  }

  search(payload: Record<string, unknown>): Observable<AiaRow[]> {
    return this.http.post<AiaRow[]>(`${this.base}/api/asset/issue/acceptance/search`, payload);
  }
}
