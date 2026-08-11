import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PackageRow } from './package-with-cap.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/services/unit/packageWithCAPController.js` —
 * initPackageMasterList (ctrl:56-68): body built at lines 58-66
 * (packageTypeId:3 at line 63), URI `/api/packages/filter` at line 67,
 * POST via BillingGenericService at line 68. The search path
 * (getSearchServicesList, ctrl:40-50) posts the same body to the same URI.
 * Same envelope as the sibling multicounterPackageController.js list.
 * Live-verified 2026-08-12: SUCCESS with empty listObject (honest
 * dev-empty — the endpoint is real, the table just has no rows).
 */
@Injectable({ providedIn: 'root' })
export class PackageWithCapService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:56-68 — POST /api/packages/filter {orgId, unitId, packageCode:'',
  // packageName:'', packageTypeId:3, offset:0, noOfRecordsPerPage:10,
  // searchKeyword:null}. listObject -> rows, object -> total count.
  getList(orgId: number, unitId: number): Observable<ApiResponse<number, PackageRow>> {
    return this.http.post<ApiResponse<number, PackageRow>>(`${this.base}/api/packages/filter`, {
      orgId,
      unitId,
      packageCode: '',
      packageName: '',
      packageTypeId: 3,
      offset: 0,
      noOfRecordsPerPage: 10,
      searchKeyword: null,
    });
  }
}
