import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { UomUnitRow } from './uom-master.models';

/**
 * The uom-master endpoints. Verified by CALLING with the cached token
 * (QF-2026-08-09-E: call, don't grep):
 *   GET /api/global/uom/units/{orgId} -> 200 ([] in dev — the units list,
 *        GlobalController.getUomUnits -> GlobalService.getUomUnitsList ->
 *        GlobalDao.getUomUnitsList: single HQL, org-scoped).
 * There is NO UOM write path anywhere on this screen's path: no save, no
 * update/status, no delete endpoint exists for UomUnit (the GlobalController
 * exposes only the units GET; the UomUnit DAO has no save/update). The
 * rebuild's Save and Status toggle report this honestly in the UI (the
 * owner's static-shell standing decision), and it is logged as a finding.
 */
@Injectable({ providedIn: 'root' })
export class UomMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/global/uom/units/{orgId} — the units list (grid + the search). */
  searchUnits(orgId: number): Observable<UomUnitRow[]> {
    return this.http.get<UomUnitRow[]>(`${this.base}/api/global/uom/units/${orgId}`);
  }
}
