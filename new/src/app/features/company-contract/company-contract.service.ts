/**
 * company-contract — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/company-contract-master.html
 *   (menu node 608 'Company Contract', parent 570 Billing -> 531 Unit
 *   Master, controller companyContractController — the DECOY test passes: a
 *   REAL controller with 40 template binds, loaded index.html:1606).
 *
 * SCREEN 37 — LIVE class (the recheck's second new LIVE; per-screen endpoint
 * truth found it — the 585 lesson). The screen configures company contracts
 * per unit: company list -> associate-company list -> visit-type/grade/
 * package-type feeds -> create contract (createCompanyContarct).
 *
 * LIVE-VERIFIED BY CALLING (body status):
 * - the company list POST /unit/CompanyContract/getCompanyMasterList
 *   {organizationId, unitId, companyTypeId: 3} -> 200 success listObject []
 *   (dev-empty);
 * - the visit-type feed {organizationId, unitId} -> 200 success with REAL
 *   rows (ABC test1 / Day Care / Emergency);
 * - the grade list {organizationId, unitId} -> 200 success [] (dev-empty);
 * - the package-type feed GET /Unit/Service/EHCpackage/getPackgeType/1/1 ->
 *   200 success with REAL package rows (All Inclusive Package / EHC
 *   Package);
 * - the associate-company feed {organizationId, unitId} -> LOUD 500
 *   (honest error, message populated — a secondary dropdown; root-cause
 *   owed, not the primary grid).
 * PAYLOAD LESSON: the legacy contract is {organizationId, unitId} — the
 * orgId key is REJECTED by Jackson at deserialization (UnrecognizedProperty
 * on CompanyMasterDto, 57 known properties) and the exception fires BEFORE
 * the controller's try, so the global handler 500s instead of the empty
 * catch masking it (the earlier 500 was MY payload — verified in the
 * catalina.out stack).
 *
 * SCOPED BACKEND CHECK (billing write path): createCompanyContarct — the
 * controller catches and RETHROWS ServiceException (clean, no swallow); the
 * DAO catch returns the honest ERR_500 (message populated). The
 * SUCCESS-from-catch false-success class is ABSENT on this write path.
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
export class CompanyContractService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  companies(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/unit/CompanyContract/getCompanyMasterList`,
      { organizationId: orgId, unitId, companyTypeId: 3 }
    );
  }

  visitTypes(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/unit/CompanyContract/getVisitTypeList`,
      { organizationId: orgId, unitId }
    );
  }

  packageTypes(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/Unit/Service/EHCpackage/getPackgeType/${orgId}/${unitId}`
    );
  }
}
