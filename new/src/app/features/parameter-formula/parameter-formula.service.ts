import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { FormulaRow } from './parameter-formula.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/lis/unit/parameterFormulaController.js`
 * (lines 176-177 list URI1: LIS_UNIT + FORMULA_API + S + orgId + S +
 * orgUnitId + S + offset + S + noOfRecordsPerPage) and lis.url.config.js:574
 * (FORMULA_API = "/formula"), lis.url.config.js:8 (LIS_UNIT = "/LIS/unit") --
 * not guessed.
 *
 * Dev-empty: the DAO answers status 'error' + "Formula Records Not Found."
 * (FormulaMasterDaoImpl.java:237-240) when the dev DB has no rows -- surfaced
 * verbatim by the component's error state, no masked banner.
 */
@Injectable({ providedIn: 'root' })
export class ParameterFormulaService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:176-177 — GET /LIS/unit/formula/{org}/{unit}/{off}/{size}.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, FormulaRow>> {
    return this.http.get<ApiResponse<never, FormulaRow>>(
      `${this.base}/LIS/unit/formula/${organizationId}/${unitId}/${offset}/${size}`,
    );
  }
}
