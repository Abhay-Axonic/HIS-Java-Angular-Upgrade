import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  ManufacturerCreatePayload,
  ManufacturerCreateResponse,
  ManufacturerDetail,
  ManufacturerPrefixResponse,
  ManufacturerRow,
  ManufacturerStatusPayload,
  ManufacturerUpdatePayload,
} from './manufacturer-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/pharmacy/manufacturerMasterController.js
 * and `CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER` in
 * resources/assets/js/constants/constants.js:145-146 (`globalsApi` =
 * `rootUrl + '/api/globals'`, constants.js:9) -- not guessed. All 5 calls
 * are real (GET/GET/GET/POST/PATCH/PATCH), matching
 * `GenericService.serviceAction(...)` at every call site.
 */
@Injectable({ providedIn: 'root' })
export class ManufacturerMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:53-70.
  getManufacturers(organizationId: number): Observable<ManufacturerRow[]> {
    return this.http.get<ManufacturerRow[]>(`${this.base}/api/globals/manufacturers/${organizationId}`);
  }

  // ctrl:104-133.
  getManufacturerById(organizationId: number, id: number): Observable<ManufacturerDetail> {
    return this.http.get<ManufacturerDetail>(`${this.base}/api/globals/manufacturers/${organizationId}/${id}`);
  }

  // ctrl:72-93 -- `177` is a hardcoded manufacturer-type id in legacy, not a
  // typo or a guess; reproduced verbatim (see manufacturer-master.models.ts).
  getPrefix(organizationId: number): Observable<ManufacturerPrefixResponse> {
    return this.http.get<ManufacturerPrefixResponse>(
      `${this.base}/api/globals/manufacturers/prefix/${organizationId}/177`,
    );
  }

  // ctrl:200-235.
  create(organizationId: number, payload: ManufacturerCreatePayload): Observable<ManufacturerCreateResponse> {
    return this.http.post<ManufacturerCreateResponse>(
      `${this.base}/api/globals/manufacturers/${organizationId}`,
      payload,
    );
  }

  // ctrl:238-278.
  update(
    organizationId: number,
    id: number,
    payload: ManufacturerUpdatePayload,
  ): Observable<{ message?: string }> {
    return this.http.patch<{ message?: string }>(
      `${this.base}/api/globals/manufacturers/${organizationId}/${id}`,
      payload,
    );
  }

  // ctrl:141-163 -- growl message is hardcoded on success ('Status changed
  // successfully'), not read from the response, so the response type here
  // is intentionally not used for anything beyond confirming the call
  // resolved.
  toggleStatus(
    organizationId: number,
    id: number,
    payload: ManufacturerStatusPayload,
  ): Observable<unknown> {
    return this.http.patch(`${this.base}/api/globals/manufacturers/${organizationId}/${id}`, payload);
  }
}
