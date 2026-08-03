import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PackageSearchRow } from './multicounter-package.models';

/**
 * Endpoint paths from
 * resources/assets/js/controller/services/unit/multicounterPackageController.js:
 * list/search -> POST /api/packages/filter {orgId, unitId, packageCode,
 * packageName, packageTypeId:5, offset, noOfRecordsPerPage, searchKeyword}.
 * Verified live (BR-159 golden trace): 200 with listObject + object(count).
 * The add/edit builder (legacy state addNewMultiEncounter) is a separate,
 * much larger controller (addNewMultiEncounterController.js, ~70KB) with the
 * package inclusion/exclusion + price-calculation logic -- see the batch
 * report's SME points; the rebuild of the builder is a flagged follow-up.
 */
@Injectable({ providedIn: 'root' })
export class MulticounterPackageService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  search(
    orgId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
    searchKeyword?: string,
  ): Observable<ApiResponse<number, PackageSearchRow>> {
    return this.http.post<ApiResponse<number, PackageSearchRow>>(`${this.base}/api/packages/filter`, {
      orgId,
      unitId,
      packageCode: '',
      packageName: '',
      packageTypeId: 5,
      offset,
      noOfRecordsPerPage,
      searchKeyword: searchKeyword ?? null,
    });
  }

  toggleStatus(parentPackageId: number, active: boolean): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/api/packages/inActivePackage/parentPkgId/${parentPackageId}/PackageTypeId/5/activeInactive/${active}`,
    );
  }
}
