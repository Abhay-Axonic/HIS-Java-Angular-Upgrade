import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

export interface AssetIssueDropdowns {
  fromStore?: Record<string, unknown>[];
  toStore?: Record<string, unknown>[];
  status?: Record<string, unknown>[];
  indentType?: Record<string, unknown>[];
  priority?: Record<string, unknown>[];
}

@Injectable({ providedIn: 'root' })
export class AssetIssueIndentService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  dropdowns(unitId: number, orgId: number, userId: number): Observable<AssetIssueDropdowns> {
    return this.http.get<AssetIssueDropdowns>(`${this.base}/api/asset/issue/pending/dropdowns/${unitId}/${orgId}/${userId}`);
  }

  issueListDropdowns(unitId: number, orgId: number, userId: number): Observable<AssetIssueDropdowns> {
    return this.http.get<AssetIssueDropdowns>(`${this.base}/api/asset/issue/list/dropdowns/${unitId}/${orgId}/${userId}`);
  }

  pendingSearch(payload: Record<string, unknown>): Observable<Record<string, unknown>[]> {
    return this.http.post<Record<string, unknown>[]>(`${this.base}/api/asset/issue/pending/search`, payload);
  }

  issuedSearch(payload: Record<string, unknown>): Observable<Record<string, unknown>[]> {
    return this.http.post<Record<string, unknown>[]>(`${this.base}/api/asset/issue/list/search`, payload);
  }
}
