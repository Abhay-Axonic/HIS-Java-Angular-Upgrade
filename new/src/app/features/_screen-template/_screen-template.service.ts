import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ScreenTemplateRecord } from './_screen-template.models';

/**
 * TEMPLATE -- replace every `__legacyModulePath__` below with the real
 * endpoint paths taken directly from the legacy controller's
 * GenericService/BillingGenericService calls (grep the actual
 * `resources/assets/js/controller/.../<screen>Controller.js` and the
 * matching `*.url.config.js`), not guessed. See docs/PHASE2_PILOT_AUDIT.md
 * for what a real golden-trace-confirmed service looks like.
 *
 * `authInterceptor` (new/src/app/core/auth.interceptor.ts) is already
 * wired globally in app.config.ts -- every HttpClient call made through
 * this service automatically gets the Authorization/userId/unitId headers
 * legacy's own GenericService.serviceAction attaches. Nothing to add here.
 */
@Injectable({ providedIn: 'root' })
export class ScreenTemplateService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    recordPerPage: number,
  ): Observable<ApiResponse<never, ScreenTemplateRecord>> {
    return this.http.post<ApiResponse<never, ScreenTemplateRecord>>(
      `${this.base}/__legacyModulePath__/getList`,
      { organizationId, unitId, offset, recordPerPage },
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/__legacyModulePath__/getCount`, {
      organizationId,
      unitId,
    });
  }

  changeStatus(id: number, status: 'A' | 'I', organizationId: number, unitId: number): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/__legacyModulePath__/changeStatus`, {
      id,
      status,
      organizationId,
      unitId,
    });
  }

  /**
   * Multi-field-optional search example -- see
   * new/src/app/shared/search/optional-params.ts. Only send fields the
   * caller actually filled in; confirm AND vs. OR combination against the
   * real legacy DAO query before assuming either.
   */
  search(params: Partial<Record<'code' | 'description', string>>): Observable<ApiResponse<never, ScreenTemplateRecord>> {
    return this.http.post<ApiResponse<never, ScreenTemplateRecord>>(
      `${this.base}/__legacyModulePath__/search`,
      params,
    );
  }
}
