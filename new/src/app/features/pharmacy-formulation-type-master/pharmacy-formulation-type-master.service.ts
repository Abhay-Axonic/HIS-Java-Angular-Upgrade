import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  FormulationTypeCreatePayload,
  FormulationTypeDetail,
  FormulationTypeRow,
  FormulationTypeStatusPayload,
  FormulationTypeUpdatePayload,
} from './pharmacy-formulation-type-master.models';

/**
 * Endpoint paths taken directly from `CONSTANTS.MASTER.GLOBAL.PHARAMACY.
 * FORMULATION_TYPE.FORMULATION_TYPE_API` (`constants.js:130`, `globalsApi +
 * '/formulationTypes/'`) and `formulationTypeMasterController.js`'s own call
 * sites, not guessed -- all 4 confirmed real end-to-end (no dead-backend
 * issues) during this screen's Step 0/1 audit:
 *  - GET   /api/globals/formulationTypes/{orgId}       -- list all
 *  - GET   /api/globals/formulationTypes/{orgId}/{id}  -- get one for edit
 *  - POST  /api/globals/formulationTypes/              -- create
 *  - PATCH /api/globals/formulationTypes/{id}           -- update AND
 *    status-toggle (same endpoint, two different payload shapes -- see
 *    update()/updateStatus() below and pharmacy-formulation-type-
 *    master.models.ts).
 *
 * getAll/getById return raw JSON (array / plain object) -- NOT wrapped in
 * the `{status, listObject}` envelope other masters use. Confirmed directly
 * from `response.data` usage at ctrl:43/65-68, not assumed from the
 * scaffold's default shape.
 */
@Injectable({ providedIn: 'root' })
export class PharmacyFormulationTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAll(organizationId: number): Observable<FormulationTypeRow[]> {
    return this.http.get<FormulationTypeRow[]>(`${this.base}/api/globals/formulationTypes/${organizationId}`);
  }

  getById(organizationId: number, id: number): Observable<FormulationTypeDetail> {
    return this.http.get<FormulationTypeDetail>(
      `${this.base}/api/globals/formulationTypes/${organizationId}/${id}`,
    );
  }

  create(payload: FormulationTypeCreatePayload): Observable<{ id?: number; message?: string }> {
    return this.http.post<{ id?: number; message?: string }>(`${this.base}/api/globals/formulationTypes/`, payload);
  }

  /** Also used by `updateStatus()` below -- same endpoint, different
   * (smaller) payload shape, matching legacy's `ToggleStatus` (ctrl:74-98)
   * hitting the identical `PATCH /{id}` URI as `updateFormulationType`
   * (ctrl:184-210). */
  update(id: number, payload: FormulationTypeUpdatePayload): Observable<{ id?: number; message?: string }> {
    return this.http.patch<{ id?: number; message?: string }>(`${this.base}/api/globals/formulationTypes/${id}`, payload);
  }

  updateStatus(id: number, payload: FormulationTypeStatusPayload): Observable<{ id?: number; message?: string }> {
    return this.http.patch<{ id?: number; message?: string }>(`${this.base}/api/globals/formulationTypes/${id}`, payload);
  }
}
