import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  FrequencyDetail,
  FrequencyRow,
  FrequencySavePayload,
  FrequencyStatusPayload,
  LanguageMasterRow,
} from './frequency-master.models';

/**
 * Endpoint paths taken directly from `CONSTANTS.MASTER.GLOBAL.CLINICAL`
 * (`constants.js:170-172`: `FREQUENCY_API = globalsApi + '/frequency/'`,
 * `FREQUENCY_SAVE = globalsApi + '/frequency/save'`, `globalsApi = rootUrl +
 * '/api/globals'`) and `frequencyMasterController.js`'s own inline URIs for
 * the language sub-features. Full Controller -> Service -> DAO chain
 * confirmed real end to end in
 * `legacy/Pharmacy/src/main/java/com/param/pharmacy/master/global/{controller,service,dao}/frequency/`
 * (`FrequencyController.java`) for every endpoint below except the two
 * language ones, which live on a separate, also-confirmed-real controller
 * (`legacy/LIS/.../unit/controller/LanguageMasterController.java`).
 */
@Injectable({ providedIn: 'root' })
export class FrequencyMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/globals/frequency/{orgId} -- ctrl:61-74 (`getFrequencyMasterData`).
   * NOT wrapped in `{status, listObject}` -- `response.data` IS the array. */
  getAll(organizationId: number): Observable<FrequencyRow[]> {
    return this.http.get<FrequencyRow[]>(`${this.base}/api/globals/frequency/${organizationId}`);
  }

  /** GET /api/globals/frequency/{orgId}/{id} -- ctrl:262-312
   * (`populateFrequencyMaster`). NOT wrapped -- `response.data` IS the object. */
  getById(organizationId: number, id: number): Observable<FrequencyDetail> {
    return this.http.get<FrequencyDetail>(`${this.base}/api/globals/frequency/${organizationId}/${id}`);
  }

  /** PATCH /api/globals/frequency/{orgId}/{id} -- ctrl:417-451
   * (`updateFrequencyMaster`). Response is a plain `CommonResponse` (just
   * `{id, message}`, no `status` field) -- legacy's success handler shows
   * `response.data.message` unconditionally on any 2xx, matching `growl.success`
   * below; only an HTTP-level failure hits the error branch. */
  update(organizationId: number, id: number, payload: FrequencySavePayload): Observable<{ id?: number; message?: string }> {
    return this.http.patch<{ id?: number; message?: string }>(
      `${this.base}/api/globals/frequency/${organizationId}/${id}`,
      payload,
    );
  }

  /** PATCH, same endpoint/method as `update()` above -- ctrl:314-335
   * (`activateDeactivateFrequencyMaster`), just a partial `{status}` body.
   * The Java side falls back to the existing entity's own values for every
   * field the partial body omits, so this cannot clobber code/description. */
  updateStatus(organizationId: number, id: number, payload: FrequencyStatusPayload): Observable<{ id?: number; message?: string }> {
    return this.http.patch<{ id?: number; message?: string }>(
      `${this.base}/api/globals/frequency/${organizationId}/${id}`,
      payload,
    );
  }

  /** POST /api/globals/frequency/save -- ctrl:387-415 (`createFrequencyMaster`). */
  save(payload: FrequencySavePayload): Observable<{ id?: number; message?: string }> {
    return this.http.post<{ id?: number; message?: string }>(`${this.base}/api/globals/frequency/save`, payload);
  }

  /** GET /LIS/global/listLanguageMaster/{orgId}/{unitId} -- ctrl `getLanguageMasterList()`,
   * called unconditionally on init to populate the "Customize Language" modal's dropdown. */
  getLanguageMasterList(organizationId: number, unitId: number): Observable<ApiResponse<never, LanguageMasterRow>> {
    return this.http.get<ApiResponse<never, LanguageMasterRow>>(
      `${this.base}/LIS/global/listLanguageMaster/${organizationId}/${unitId}`,
    );
  }
}
