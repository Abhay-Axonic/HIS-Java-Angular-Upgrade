import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { CancelReasonRow } from './cancellation-master.models';

@Injectable({ providedIn: 'root' })
export class CancellationMasterService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/global/cancel/reasons — the ONLY endpoint for this screen.
   * Returns a RAW LIST (not the {listObject} envelope) — verified by
   * calling (HTTP 200, body is the list, dev-empty). */
  list(): Observable<CancelReasonRow[]> {
    return this.http.get<CancelReasonRow[]>(`${this.base}/api/global/cancel/reasons`);
  }
}
