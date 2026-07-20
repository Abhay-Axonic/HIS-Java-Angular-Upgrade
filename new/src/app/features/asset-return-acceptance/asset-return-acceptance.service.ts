import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { AraDropdowns, AraRow } from './asset-return-acceptance.models';

@Injectable({ providedIn: 'root' })
export class AssetReturnAcceptanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdowns(unitId: number, organizationId: number, userId: number): Observable<AraDropdowns> {
    return this.http.get<AraDropdowns>(`${this.base}/api/asset/management/asset/return/acceptance/dropdowns/${unitId}/${organizationId}/${userId}`);
  }

  search(payload: Record<string, unknown>): Observable<AraRow[]> {
    return this.http.post<AraRow[]>(`${this.base}/api/asset/management/asset/return/acceptance/search`, payload);
  }
}
