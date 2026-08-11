import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ProfilePanelTestRow } from './profile-panel-test-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/lis/unit/profilepanelTestMasterController.js`
 * (lines 171-172 list URI1: LIS_UNIT + LIST_PRIOFILE_PANEL_MASTER + S +
 * orgId + S + orgUnitId + S + offset + S + noOfRecordsPerPage) and
 * lis.url.config.js:566 (LIST_PRIOFILE_PANEL_MASTER = "/PanelList"),
 * lis.url.config.js:8 (LIS_UNIT = "/LIS/unit") -- not guessed.
 *
 * Dev-empty: the DAO answers status 'error' + "Panel Test Records Not Found."
 * (PanelTestDaoImpl.java:241-243) when the dev DB has no rows -- surfaced
 * verbatim by the component's error state, no masked banner.
 */
@Injectable({ providedIn: 'root' })
export class ProfilePanelTestMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:171-172 — GET /LIS/unit/PanelList/{org}/{unit}/{off}/{size}.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, ProfilePanelTestRow>> {
    return this.http.get<ApiResponse<never, ProfilePanelTestRow>>(
      `${this.base}/LIS/unit/PanelList/${organizationId}/${unitId}/${offset}/${size}`,
    );
  }
}
