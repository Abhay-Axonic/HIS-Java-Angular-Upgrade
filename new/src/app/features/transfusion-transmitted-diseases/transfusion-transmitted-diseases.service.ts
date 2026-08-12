import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BloodBagRow } from './transfusion-transmitted-diseases.models';

/** Blood-bag search payload — legacy transfusionTransmittedDiseasesController.js:73-79. */
export interface SearchBloodBagPayload {
  unitId: number;
  organizationId: number;
  bloodBagNo?: string;
  fromDate?: string;
  toDate?: string;
}

/**
 * Screen 722 — the ONLY endpoint in the legacy controller. The state
 * transfusionTransmittedDiseases (app.js:18234-18238) is a search/entry form;
 * transfusionTransmittedDiseasesController.js contains a single backend
 * constant: SEARCH_BAG_DETAILS = "/api/bloodbank/searchBloodBag"
 * (bloodBank.url.config.js:141), POSTed from a date-range / bloodBagNo form
 * (controller:57-100) — a blood-bag SEARCH, not a list feed. Search/write-path
 * class (DQ-2026-08-12-034): the method below documents the contract; the
 * screen renders an honest no-list state until the search write-path is wired
 * under the transaction methodology.
 */
@Injectable({ providedIn: 'root' })
export class TransfusionTransmittedDiseasesService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // POST /api/bloodbank/searchBloodBag — SEARCH_BAG_DETAILS
  // (bloodBank.url.config.js:141); result rows land in res.listObject.
  searchBloodBags(payload: SearchBloodBagPayload): Observable<ApiResponse<never, BloodBagRow>> {
    return this.http.post<ApiResponse<never, BloodBagRow>>(
      `${this.base}/api/bloodbank/searchBloodBag`,
      payload,
    );
  }
}
