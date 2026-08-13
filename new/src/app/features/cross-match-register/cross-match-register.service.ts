import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { CrossMatchSave, CrossMatchResponse } from './cross-match-register.models';

/**
 * Cross-match register write (DQ-068: BUILDABLE — the full legacy payload
 * lands master + details rows). The reads (worklists) are session-gated
 * backend reads; this feature wires the verified write only.
 */
@Injectable({ providedIn: 'root' })
export class CrossMatchRegisterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  saveCrossMatch(payload: CrossMatchSave): Observable<CrossMatchResponse> {
    return this.http.post<CrossMatchResponse>(
      `${this.base}/api/bloodbank/crossMatch/saveCrossMatchRegister`,
      payload
    );
  }
}
