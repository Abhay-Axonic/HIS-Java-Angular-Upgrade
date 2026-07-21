import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CtdtComponent, CtdtSavedEntry } from './create-tabular-data-templates.models';

@Injectable({ providedIn: 'root' })
export class CreateTabularDataTemplatesService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveTabularTemplateMaster(organizationId: number, unitId: number): Observable<ApiResponse<never, CtdtComponent>> {
    return this.http.get<ApiResponse<never, CtdtComponent>>(`${this.base}/api/global/unit/template/tabular/getActiveTabularTemplateMaster/${organizationId}/${unitId}`);
  }

  getTemplateDataMapperById(id: number, organizationId: number, unitId: number): Observable<ApiResponse<never, CtdtSavedEntry>> {
    return this.http.get<ApiResponse<never, CtdtSavedEntry>>(`${this.base}/api/global/unit/template/tabular/mapper/getTabularTemplateDataMapperById/${id}/${organizationId}/${unitId}`);
  }

  saveTabularTemplateDataMapper(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/template/tabular/mapper/saveTabularTemplateDataMapper`, payload);
  }
}
