import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AppointmentSlotRow } from './appointment-scheduling-unit-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/opd/unit/appointmentSchedulingUnitMasterController.js`
 * (GETALLSLOTSNEW = API+OPD+"/getSlotsNew" from opd.url.config.js:37) — not
 * guessed. Confirmed live 2026-08-12: 500 with the correlation envelope
 * (MASKED, DQ-2026-08-12-016).
 */
@Injectable({ providedIn: 'root' })
export class AppointmentSchedulingUnitMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl GETALLSLOTSNEW — POST /api/opd/getSlotsNew.
  getSlots(organizationId: number, unitId: number, slotTypeId: number): Observable<ApiResponse<never, AppointmentSlotRow>> {
    return this.http.post<ApiResponse<never, AppointmentSlotRow>>(
      `${this.base}/api/opd/getSlotsNew`,
      { organizationId, unitId, slotTypeId },
    );
  }
}
