import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PackageRow } from './ehc-package.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/services/unit/ehcPackageController.js` —
 * initPackageMasterList (ctrl:48-62): body built at lines 50-59
 * (packageTypeId:1 at line 55), URI `/api/packages/filter` at line 60,
 * POST via BillingGenericService at line 61. The search path
 * (searchEHCPackage, ctrl:72-106) posts the same body to the same URI.
 * Same envelope as the sibling multicounterPackageController.js list.
 * Live-verified 2026-08-12: SUCCESS with empty listObject (honest
 * dev-empty — the endpoint is real, the table just has no rows).
 */
@Injectable({ providedIn: 'root' })
export class EhcPackageService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:48-62 — POST /api/packages/filter {orgId, unitId, packageCode:'',
  // packageName:'', packageTypeId:1, offset:0, noOfRecordsPerPage:10,
  // searchKeyword:null}. listObject -> rows, object -> total count.
  getList(orgId: number, unitId: number): Observable<ApiResponse<number, PackageRow>> {
    return this.http.post<ApiResponse<number, PackageRow>>(`${this.base}/api/packages/filter`, {
      orgId,
      unitId,
      packageCode: '',
      packageName: '',
      packageTypeId: 1,
      offset: 0,
      noOfRecordsPerPage: 10,
      searchKeyword: null,
    });
  }
}
