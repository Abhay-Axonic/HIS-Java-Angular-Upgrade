import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { SlotBlockRow } from './doctor-wise.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/opd/unit/SlotBlockUnblockControllerDoctorWise.js`
 * (GETALLSLOTSBLOCKUNBLOCK = API+OPD+"/getSlots/blockUnblock" from
 * opd.url.config.js:38) — the SAME endpoint as scheduleWise
 * (DQ-2026-08-12-016). Confirmed live 2026-08-12: blank generic envelope
 * (masked).
 */
@Injectable({ providedIn: 'root' })
export class DoctorWiseService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:242-251 — POST /api/opd/getSlots/blockUnblock (organisationId
  // British spelling in the legacy payload).
  getSlots(
    organisationId: number,
    unitId: number,
    slotTypeId: number,
    fromDate: string,
    toDate: string,
    listDoctorId: number[],
    listSpecialityId: number[],
    fromTime: string,
    toTime: string,
  ): Observable<ApiResponse<never, SlotBlockRow>> {
    return this.http.post<ApiResponse<never, SlotBlockRow>>(
      `${this.base}/api/opd/getSlots/blockUnblock`,
      { organisationId, unitId, slotTypeId, isBlocked: 1, fromDate, toDate,
        listDoctorId, listSpecialityId, fromTime, toTime },
    );
  }
}
