/**
 * appointment-list — parity rebuild of the legacy appointment-list screen
 *   (menu node 7 'Appointment List', parent 2 Opd, controller
 *   appointmentListController loaded index.html:905; the state's title is
 *   'Patient Visit List').
 *
 * SCREEN 43 — LIVE class (the OPD subtree's fourth candidate; C7 verified
 * genuinely new). The screen lists scheduled appointments filtered by
 * doctor/speciality/subspeciality/modality/visit-type.
 *
 * BACKEND BY ENDPOINT TRUTH: the screen calls GETAPPOINTMENTSCHEDULELIST =
 * /api/opd/scheduleAppointmentList — the SAME endpoint as screen 41
 * (appointment-scheduling). Its handler is LIVE; the list path currently
 * throws server-side (501 honest-error envelope 'Something went wrong!!').
 * ROOT CAUSE OWED ONCE (not per screen — shared endpoint; candidates: a
 * phantom column or missing join in GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST;
 * the DAO catch at AppointmentSchedulingDaoImpl:872-875 discards the
 * exception into a null-message envelope — the screen-41 finding).
 *
 * SCOPED BACKEND CHECK: read-only list surface (no write in this screen's
 * controller). No write-path false-success shape.
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class AppointmentListService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // the legacy's doctorModalityObj contract (doctorId/specialityId/etc.
    // default to 0)
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/opd/scheduleAppointmentList`,
      {
        organizationId: orgId,
        unitId,
        offset: 0,
        noOfRecordsPerPage: 20,
        doctorId: 0,
        specialityId: 0,
        subSpecialityId: 0,
        modalityId: 0,
        visitTypeId: 0,
        isAppointmentRequired: 'Y',
      }
    );
  }
}
