import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class OpdCoversheetService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  /** /api/global/coversheet/templates — LIVE (200 success, dev-empty). */
  templates(
    orgId: number,
    unitId: number,
    templateTypeId: number
  ): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/coversheet/templates`,
      { orgId, unitId, templateTypeId, doctorId: 1, specialityId: 0 }
    );
  }

  /** /api/global/templates/complaints — LIVE (200 success, dev-empty). */
  complaints(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/templates/complaints`,
      { orgId, unitId }
    );
  }

  /** /api/global/templates/diagnosis — LIVE (200 success, dev-empty). */
  diagnosis(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/templates/diagnosis`,
      { orgId, unitId }
    );
  }

  /** /api/global/getActiveButtonMaster — BROKEN (phantom disclaimer column). */
  buttonMaster(payload: Record<string, unknown>): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/getActiveButtonMaster`,
      payload
    );
  }
}
