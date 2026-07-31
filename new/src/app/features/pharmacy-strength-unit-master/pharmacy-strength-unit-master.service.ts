import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { StrengthUnitCreateResponse, StrengthUnitMutateResponse, StrengthUnitRow } from './pharmacy-strength-unit-master.models';

/**
 * Endpoint paths taken directly from resources/assets/js/constants/constants.js:161
 * (`STRENGTH_UNIT_API: globalsApi + '/strength/units/'`) and confirmed
 * against StrengthUnitController.java (`@RequestMapping("/api/globals/strength/units")`).
 * This is a genuinely different, REST-style backend contract from every
 * other legacy screen in this app -- confirmed live (2026-07-31, real
 * authenticated requests): GET returns a raw array, POST/PATCH return
 * `{id, message}`/`{message}` directly, none of it wrapped in the usual
 * `{status, listObject}` envelope. Not guessed -- see the class comment on
 * pharmacy-strength-unit-master.ts for the full live-verification trail
 * including a real create-round-trip (`POST` -> `psql` confirm -> delete).
 */
@Injectable({ providedIn: 'root' })
export class PharmacyStrengthUnitMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:48-63 -- GET, raw array response (`self.model.UnitList = response.data`).
  getList(organizationId: number): Observable<StrengthUnitRow[]> {
    return this.http.get<StrengthUnitRow[]>(`${this.base}/api/globals/strength/units/${organizationId}`);
  }

  // ctrl:79-94 -- GET single record by id.
  getById(organizationId: number, id: number): Observable<StrengthUnitRow> {
    return this.http.get<StrengthUnitRow>(`${this.base}/api/globals/strength/units/${organizationId}/${id}`);
  }

  // ctrl:124-159 -- POST, real create. Legacy's own URL concatenation has an
  // accidental double slash (`STRENGTH_UNIT_API + "/" + organizationId`,
  // where the constant itself already ends in `/`) -- confirmed live this
  // has zero behavioral effect (Spring/Tomcat normalize it, verified via a
  // real 201 Created), so the correct single-slash form is used here
  // rather than reproducing a typo with no observable difference.
  create(payload: {
    id: number | undefined;
    code: string;
    unit: string;
    organizationId: number;
    unitId: number;
    userId: number;
    status: true;
  }): Observable<StrengthUnitCreateResponse> {
    return this.http.post<StrengthUnitCreateResponse>(
      `${this.base}/api/globals/strength/units/${payload.organizationId}`,
      payload,
    );
  }

  // ctrl:162-189 -- PATCH, real update, body is JUST { code, unit } (id
  // already in the URL path, confirmed -- legacy does not resend it).
  update(organizationId: number, id: number, code: string, unit: string): Observable<StrengthUnitMutateResponse> {
    return this.http.patch<StrengthUnitMutateResponse>(`${this.base}/api/globals/strength/units/${organizationId}/${id}`, {
      code,
      unit,
    });
  }

  // ctrl:96-121 -- PATCH, same endpoint as update(), body is JUST { status }.
  updateStatus(organizationId: number, id: number, status: boolean): Observable<StrengthUnitMutateResponse> {
    return this.http.patch<StrengthUnitMutateResponse>(`${this.base}/api/globals/strength/units/${organizationId}/${id}`, {
      status,
    });
  }
}
