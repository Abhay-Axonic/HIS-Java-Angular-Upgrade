import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  BloodComponentApiEnvelope,
  BloodComponentRow,
} from './blood-component.models';

/**
 * The blood-component endpoints. VERIFIED BY CALLING with the cached token
 * (2026-08-10) — the fragment class: the constants (bloodBank.url.config.js:
 * 97-103) carry NO /api prefix; the controller is
 * BloodComponentMasterController @RequestMapping("/bloodBank"), so the full
 * paths are /bloodBank/* (contrast /api/billing/* for vaccinationMaster).
 *   POST /bloodBank/getBloodComponentMasterList        -> 200 success (1 row)
 *   POST /bloodBank/getBloodComponentMasterCount       -> 200 success (obj 1)
 *   GET  /bloodBank/getActiveBloodComponentMaster/1/1  -> 200 success (1 row)
 *   GET  /bloodBank/getBloodComponentMasterById/1/1/1  -> 200 success ([])
 * The write endpoints (save/update/updateStatus) are MAPPED; their success
 * is UNVERIFIED per the WRITE-VERIFICATION rule — never "working".
 */
@Injectable({ providedIn: 'root' })
export class BloodComponentService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** POST /bloodBank/getBloodComponentMasterList — org+unit scoped grid feed. */
  listMasters(
    organizationId: number,
    unitId: number,
  ): Observable<BloodComponentApiEnvelope<BloodComponentRow[]>> {
    return this.http.post<BloodComponentApiEnvelope<BloodComponentRow[]>>(
      `${this.base}/bloodBank/getBloodComponentMasterList`,
      { organizationId, unitId },
    );
  }
}
