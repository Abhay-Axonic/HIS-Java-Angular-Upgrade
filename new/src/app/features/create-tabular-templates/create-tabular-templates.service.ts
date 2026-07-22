import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { TabularTemplateRow } from './create-tabular-templates.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/template/createTabularTemplatesController.js
 * (not guessed) -- all confirmed to resolve to a real, complete
 * Controller -> Service -> DAO chain in
 * Global/src/main/java/com/param/global/unit/{controller,service,dao}/
 * TabularTemplateMaster* during this screen's Step 0 backend-existence
 * check (243-line DaoImpl, real Hibernate named queries, not stubs):
 *  - getTabularTemplateMasterList
 *  - getTabularTemplateMasterCount
 *  - getTabularTemplateMasterById/{id}/{orgId}/{unitId}
 *  - saveTabularTemplateMaster
 *  - updateTabularTemplateMaster
 *  - updateTabularTemplateMasterStatus
 * All 6 (plus the sibling screen's getActiveTabularTemplateMaster) live
 * under the same TabularTemplateMasterController -- confirming
 * createTabularTemplates (this screen, master CRUD for template
 * code/description) and createTabularDataTemplates (the already-built row/
 * column data-mapper screen) are genuinely distinct, not duplicates.
 */
@Injectable({ providedIn: 'root' })
export class CreateTabularTemplatesService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, TabularTemplateRow>> {
    return this.http.post<ApiResponse<never, TabularTemplateRow>>(
      `${this.base}/api/global/unit/template/tabular/getTabularTemplateMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(
      `${this.base}/api/global/unit/template/tabular/getTabularTemplateMasterCount`,
      { organizationId, unitId },
    );
  }

  getById(tabularTemplateId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, TabularTemplateRow>> {
    return this.http.get<ApiResponse<never, TabularTemplateRow>>(
      `${this.base}/api/global/unit/template/tabular/getTabularTemplateMasterById/${tabularTemplateId}/${organizationId}/${unitId}`,
    );
  }

  create(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/template/tabular/saveTabularTemplateMaster`, payload);
  }

  update(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/template/tabular/updateTabularTemplateMaster`, payload);
  }

  updateStatus(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(
      `${this.base}/api/global/unit/template/tabular/updateTabularTemplateMasterStatus`,
      payload,
    );
  }
}
