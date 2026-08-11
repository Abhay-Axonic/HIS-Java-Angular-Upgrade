import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { HistopathologyTestRow } from './histopathology-test.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/lis/unit/histopathologyTestController.js`
 * (lines 430-431: LIS_UNIT + LIST_TEST_MASTER + S + orgId + S + isHistoCyto
 * + S + orgUnitId + S + offset + S + noOfRecordsPerPage + S + searchKey;
 * isHistoCyto is 'H' for this screen, :407) and lis.url.config.js:493
 * (LIST_TEST_MASTER = "/listTestMaster"), lis.url.config.js:8 (LIS_UNIT =
 * "/LIS/unit") -- not guessed. Legacy passes the raw searchKey as the last
 * path segment (null when the search box is empty); reproduced as-is.
 *
 * Dev-empty: the DAO answers status 'error' + "Test not found."
 * (SingleParamTestMasterDaoImpl.java:617) when no rows match -- surfaced
 * verbatim by the component's error state, no masked banner.
 */
@Injectable({ providedIn: 'root' })
export class HistopathologyTestService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:430-431 — GET /LIS/unit/listTestMaster/{org}/{histo}/{unit}/{off}/{size}/{searchKey}.
  getList(
    organizationId: number,
    histoCyto: string,
    unitId: number,
    offset: number,
    size: number,
    searchKey: string | null,
  ): Observable<ApiResponse<never, HistopathologyTestRow>> {
    return this.http.get<ApiResponse<never, HistopathologyTestRow>>(
      `${this.base}/LIS/unit/listTestMaster/${organizationId}/${histoCyto}/${unitId}/${offset}/${size}/${searchKey}`,
    );
  }
}
