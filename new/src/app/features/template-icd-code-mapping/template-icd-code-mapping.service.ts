import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { TemplateIcdCodeMappingRow } from './template-icd-code-mapping.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/config/lis.url.config.js:287`
 * (GET_TEMPLATE_DIAGNOSIS_MAPPER_LIST = "/api/global/getTemplateDiagnosisMapperlist")
 * — not guessed. GET with offset/noOfRecordsPerPage as path params matches
 * the legacy `initMasterList` call at
 * `controller/master/unit/templateICDCodeMappingMasterController.js:281-285`
 * (URI1 = ...GET_TEMPLATE_DIAGNOSIS_MAPPER_LIST + "/" + offset + "/" +
 * noOfRecordsPerPage, GenericService GET, no body) and the backend mapping
 * `/getTemplateDiagnosisMapperlist/{offset}/{noOfRecordsPerPage}` (GET,
 * TemplateDiagnosisMapperController.java:85-87). Note org/unit are NOT in
 * the call. Confirmed live 2026-08-12: SUCCESS with an empty listObject —
 * honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class TemplateIcdCodeMappingService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:281-285 (templateICDCodeMappingMasterController.js), lis.url.config.js:287 —
  // GET /api/global/getTemplateDiagnosisMapperlist/{offset}/{noOfRecordsPerPage}.
  // Dev-empty: SUCCESS + [].
  getList(
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, TemplateIcdCodeMappingRow>> {
    return this.http.get<ApiResponse<never, TemplateIcdCodeMappingRow>>(
      `${this.base}/api/global/getTemplateDiagnosisMapperlist/${offset}/${noOfRecordsPerPage}`,
    );
  }
}
