import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { SampleTypeMediaMappingRow } from './sample-type-media-mapping-master.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/lis.url.config.js`
 * (LIST_SAMPLE_TYPE_MEDIA_MASTER = "/listSampleTypeMedia", line 259) and the
 * URI assembly at `controller/lis/global/sampleTypeMediaMappingController.js`
 * lines 134/153 (LIS_GLOBAL + /listSampleTypeMedia + orgId + offset +
 * noOfRecordsPerPage) — not guessed.
 *
 * MASKED (DQ-2026-08-12-023): confirmed live 2026-08-12 — HTTP 200,
 * status=error, "Internal Server Error" on every call, empty DB or not (LIS
 * fall-through class: size()>0 guard dead-branch; the aliasToBean Long-typed
 * createdDate/updatedDate projection throws and the exception is swallowed).
 */
@Injectable({ providedIn: 'root' })
export class SampleTypeMediaMappingMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:134/153 (sampleTypeMediaMappingController.js), lis.url.config.js:259
  // — GET /LIS/global/listSampleTypeMedia/{org}/{offset}/{size}. Masked:
  // always HTTP 200 status=error "Internal Server Error".
  getList(organizationId: number, offset: number, size: number): Observable<ApiResponse<never, SampleTypeMediaMappingRow>> {
    return this.http.get<ApiResponse<never, SampleTypeMediaMappingRow>>(
      `${this.base}/LIS/global/listSampleTypeMedia/${organizationId}/${offset}/${size}`,
    );
  }
}
