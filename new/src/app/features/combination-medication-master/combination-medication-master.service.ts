import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  ChangeCombinationStatusPayload,
  CombinationMedicationRow,
  GenericOption,
  UomOption,
} from './combination-medication-master.models';

/**
 * The combination-medication-master endpoints. All verified by CALLING with
 * the cached token (QF-2026-08-09-E: literal greps miss fragment-composed
 * paths — call, don't grep):
 *   GET  /api/globals/generics/{orgId}                  -> 200 (real rows)
 *   GET  /api/globals/generics/IsCombination/{flag}     -> 200 ([] in dev)
 *   GET  /api/global/uom/units/{orgId}                  -> 200 ([] in dev)
 *   GET  /api/globals/generics/getCombinationDetail/{id}-> 200 ([] in dev)
 *   PATCH /api/globals/generics/{id} {status}           -> 201 (idempotent
 *         probe: status unchanged, "Generic Updated Successfully")
 * The SAVE path does NOT exist for this screen: POST /api/globals/generics/
 * answers 405 and /api/globals/generics (no slash) 404 — the Generic
 * controller's POST belongs to the generic master, not the combination
 * screen (finding logged). The rebuild's save button reports that honestly.
 */
@Injectable({ providedIn: 'root' })
export class CombinationMedicationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/globals/generics/{orgId} — the generic list (the "Generic" select). */
  searchGenerics(orgId: number): Observable<GenericOption[]> {
    return this.http.get<GenericOption[]>(`${this.base}/api/globals/generics/${orgId}`);
  }

  /** GET /api/globals/generics/IsCombination/{flag} — the combination
   * generics: the "Combination Medicine" select AND the grid's rows. */
  searchCombinationMedicines(orgId: number): Observable<CombinationMedicationRow[]> {
    return this.http.get<CombinationMedicationRow[]>(
      `${this.base}/api/globals/generics/IsCombination/${orgId}`,
    );
  }

  /** GET /api/global/uom/units/{orgId} — the clinical units of measurement
   * (the strength-unit select). */
  searchUomUnits(orgId: number): Observable<UomOption[]> {
    return this.http.get<UomOption[]>(`${this.base}/api/global/uom/units/${orgId}`);
  }

  /** GET /api/globals/generics/getCombinationDetail/{id} — the edit flow's
   * detail fetch (generic + sub-generic + strength + unit). */
  getCombinationDetail(id: number): Observable<CombinationMedicationRow> {
    return this.http.get<CombinationMedicationRow>(
      `${this.base}/api/globals/generics/getCombinationDetail/${id}`,
    );
  }

  /** PATCH /api/globals/generics/{id} — the status toggle (the only
   * status-capable path; verified 201 with an idempotent probe). */
  toggleStatus(id: number, status: boolean): Observable<{ message?: string }> {
    const payload: ChangeCombinationStatusPayload = { status };
    return this.http.patch<{ message?: string }>(
      `${this.base}/api/globals/generics/${id}`,
      payload,
    );
  }
}
