import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ColonyTestWithMediaRow } from './colony-test-with-media-master.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/lis.url.config.js`
 * (GET_TOTAL_COLONY_ADDTION_MEDIA_CLASS_LIST = "/listMediaColonyMapperMaster",
 * line 376) and the URI assembly at
 * `controller/lis/global/mediaColnoyAdditionController.js` [legacy filename
 * typo] lines 143/162 (LIS_GLOBAL + /listMediaColonyMapperMaster + orgId +
 * offset + noOfRecordsPerPage) — not guessed.
 *
 * MASKED (DQ-2026-08-12-023): confirmed live 2026-08-12 — HTTP 200,
 * status=error, "Internal Server Error" on every call, empty DB or not (LIS
 * fall-through class: size()>0 guard dead-branch; the aliasToBean Long-typed
 * createdDate/updatedDate projection throws and the exception is swallowed).
 */
@Injectable({ providedIn: 'root' })
export class ColonyTestWithMediaMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:143/162 (mediaColnoyAdditionController.js), lis.url.config.js:376
  // — GET /LIS/global/listMediaColonyMapperMaster/{org}/{offset}/{size}.
  // Masked: always HTTP 200 status=error "Internal Server Error".
  getList(organizationId: number, offset: number, size: number): Observable<ApiResponse<never, ColonyTestWithMediaRow>> {
    return this.http.get<ApiResponse<never, ColonyTestWithMediaRow>>(
      `${this.base}/LIS/global/listMediaColonyMapperMaster/${organizationId}/${offset}/${size}`,
    );
  }
}
