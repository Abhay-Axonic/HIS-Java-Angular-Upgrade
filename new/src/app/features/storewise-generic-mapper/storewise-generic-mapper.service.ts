import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { StorewiseGenericMapperRow } from './storewise-generic-mapper.models';

/**
 * Screen 635 — raw-array feed. Endpoint from constants.js:244
 * STORE_WISE_GENERIC_API = unitApi + '/storewise/generic/mapper/' (unitApi =
 * '/api/unit'), consumed by storeWiseGenericMapperController.js
 * (GetGenericMapperList, ctrl:67-84) directly as response.data — the endpoint
 * returns a RAW ARRAY, NOT the {status, listObject} envelope. Live-verified
 * 2026-08-12: GET /api/unit/storewise/generic/mapper/1 -> HTTP 200 with a raw
 * array body (status key absent) — REAL FEED (raw-array class,
 * DQ-2026-08-12-034). Returns the raw array typed as the row shape.
 */
@Injectable({ providedIn: 'root' })
export class StorewiseGenericMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // GET /api/unit/storewise/generic/mapper/{unitId} — raw array, no envelope.
  getMapperList(unitId: number): Observable<StorewiseGenericMapperRow[]> {
    return this.http.get<StorewiseGenericMapperRow[]>(
      `${this.base}/api/unit/storewise/generic/mapper/${unitId}`,
    );
  }
}
