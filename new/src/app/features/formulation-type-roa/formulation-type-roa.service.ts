import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  FormulationRoaRow,
  FormulationRoaSavePayload,
  FormulationTypeRow,
  RoaOption,
} from './formulation-type-roa.models';

@Injectable({ providedIn: 'root' })
export class FormulationTypeRoaService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/globals/formulationTypeAndROA/{orgId} — the mapping list. */
  list(orgId: number): Observable<FormulationRoaRow[]> {
    return this.http.get<FormulationRoaRow[]>(`${this.base}/api/globals/formulationTypeAndROA/${orgId}`);
  }

  /** GET /api/globals/formulationTypeAndROA/dropdown/{orgId} — ROA options. */
  roaDropdown(orgId: number): Observable<RoaOption[]> {
    return this.http.get<RoaOption[]>(`${this.base}/api/globals/formulationTypeAndROA/dropdown/${orgId}`);
  }

  /** GET /api/globals/formulationTypes/{orgId} — the formulation-type feed (raw list envelope). */
  formulationTypes(orgId: number): Observable<FormulationTypeRow[]> {
    return this.http.get<FormulationTypeRow[]>(`${this.base}/api/globals/formulationTypes/${orgId}`);
  }

  /** POST /api/globals/formulationTypeAndROA/save — the mapping save. */
  save(payload: FormulationRoaSavePayload): Observable<{ id?: number; message?: string }> {
    return this.http.post<{ id?: number; message?: string }>(
      `${this.base}/api/globals/formulationTypeAndROA/save`,
      payload
    );
  }
}
