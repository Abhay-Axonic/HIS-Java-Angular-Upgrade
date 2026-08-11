/**
 * corporate-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/corporate-master-global.html (menu node 574
 *   'Corporate Master', parent 570 Billing -> 531 Unit Master, controller
 *   corporateMasterController loaded index.html:1628). NOTE: the template
 *   lives under GlobalMaster/ (the legacy's global-master folder) even
 *   though the node hangs under Billing.
 *
 * SCREEN 39 — LIVE class (the recheck's LAST confirmed LIVE; the 585
 * lesson). The screen is the corporate-master CRUD surface (companyTypeId
 * 3 = corporate): company list + relation list + save.
 *
 * LIVE-VERIFIED BY CALLING (body status):
 * - POST /api/global/getCompanyMasterList {organizationId, unitId,
 *   companyTypeId: 3} -> 200 success listObject [] (dev-empty);
 * - the save endpoint /api/global/saveCompanyMaster exists (mapped).
 * PAYLOAD LESSON: {organizationId, unitId} — the orgId key 500s at Jackson
 * deserialization (the screen-37/38 class, shared CompanyMasterDto).
 *
 * SCOPED WRITE-PATH CHECK (billing master write): saveCompanyMaster — the
 * controller catch returns an HONEST ERROR with the REAL message (the
 * BR-batch2 masking fix, live-proven: 'e.getMessage() != null ? ... :
 * Company master save failed'); the DAO catch falls to honest
 * ERROR/COMMON_ERROR; SUCCESS only after the real save + the
 * company-contract-grade-mapper batch. The SUCCESS-from-catch
 * false-success class is ABSENT. (The DAO's COMMON_ERROR_MESSAGE fallback
 * is the generic honest envelope, not the masked-null class.)
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class CorporateMasterService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  companies(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/getCompanyMasterList`,
      { organizationId: orgId, unitId, companyTypeId: 3 }
    );
  }
}
