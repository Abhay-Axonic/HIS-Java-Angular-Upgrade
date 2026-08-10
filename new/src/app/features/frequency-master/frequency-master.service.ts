import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { FrequencyRow, FrequencySavePayload } from './frequency-master.models';

@Injectable({ providedIn: 'root' })
export class FrequencyMasterService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/globals/frequency/{orgId} — the frequency list. */
  list(orgId: number): Observable<FrequencyRow[]> {
    return this.http.get<FrequencyRow[]>(`${this.base}/api/globals/frequency/${orgId}`);
  }

  /** GET /api/globals/frequency/{orgId}/{id} — byId. NOTE: 500s in dev —
   * the getTranslatedFrequencyListForUpdate sub-query hits the phantom
   * public.t_translated_frequency (table absent). Wired for honesty; the
   * error toast reports the backend failure. */
  byId(orgId: number, id: number): Observable<FrequencyRow> {
    return this.http.get<FrequencyRow>(`${this.base}/api/globals/frequency/${orgId}/${id}`);
  }

  /** POST /api/globals/frequency/save — create (payload-carried org —
   * FINDING QF, org-trust on writes is the legacy ternary, not
   * VerifiedIdentityService). LIVE-VERIFIED: 201 with the legacy contract
   * (code/description/noOfAdministrationTime/items/addedLanguageList). The
   * backend REQUIRES `items` (a save without it 500s with an NPE at
   * FrequencyService.saveFrequency:193). */
  save(payload: FrequencySavePayload): Observable<{ id?: number; message?: string }> {
    return this.http.post<{ id?: number; message?: string }>(
      `${this.base}/api/globals/frequency/save`,
      payload
    );
  }

  /** PATCH /api/globals/frequency/{orgId}/{id} — update/status. */
  update(orgId: number, id: number, payload: { code?: string; description?: string; status?: boolean }): Observable<{ id?: number; message?: string }> {
    return this.http.patch<{ id?: number; message?: string }>(
      `${this.base}/api/globals/frequency/${orgId}/${id}`,
      payload
    );
  }
}
