import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { MappingofMicroorganismRow } from './mapping-of-microorganism.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/lis.url.config.js`
 * (GET_TOTAL_ANTIBIOTIC_ORGANIM_LIST_MAPPER_MASTER =
 * "/listAntibioticOrganismMpprMaster", line 387, duplicated at 390) and the
 * URI assembly at `controller/lis/global/antibioticOrganismMpprController.js`
 * lines 184/205 (LIS_GLOBAL + /listAntibioticOrganismMpprMaster + orgId +
 * offset + noOfRecordsPerPage) — not guessed.
 *
 * MASKED (DQ-2026-08-12-023): confirmed live 2026-08-12 — HTTP 200,
 * status=error, "Internal Server Error" on every call, empty DB or not (LIS
 * fall-through class: size()>0 guard dead-branch; the aliasToBean Long-typed
 * createdDate/updatedDate projection throws and the exception is swallowed).
 */
@Injectable({ providedIn: 'root' })
export class MappingofMicroorganismService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:184/205 (antibioticOrganismMpprController.js), lis.url.config.js:387
  // — GET /LIS/global/listAntibioticOrganismMpprMaster/{org}/{offset}/{size}.
  // Masked: always HTTP 200 status=error "Internal Server Error".
  getList(organizationId: number, offset: number, size: number): Observable<ApiResponse<never, MappingofMicroorganismRow>> {
    return this.http.get<ApiResponse<never, MappingofMicroorganismRow>>(
      `${this.base}/LIS/global/listAntibioticOrganismMpprMaster/${organizationId}/${offset}/${size}`,
    );
  }
}
