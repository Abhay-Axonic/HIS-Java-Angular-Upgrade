/**
 * appointment-scheduling — parity rebuild of the legacy
 *   views/master/UnitMaster/.../appointment-scheduling.html (menu node 4
 *   'Appointment Scheduling', parent 2 Opd, controller
 *   appointmentSchedulingController loaded index.html:904).
 *
 * SCREEN 41 — LIVE class (the OPD subtree's second candidate; C7 verified
 * genuinely new). The screen is the appointment-scheduling TRANSACTION
 * surface: prefix/reason feeds -> schedule appointment (the save),
 * scheduled-appointment list (the primary read), blocked list, search by
 * criteria, cancel.
 *
 * BACKEND BY ENDPOINT TRUTH: AppointmentSchedulingController (api/opd):
 * scheduleAppointment (save), scheduleAppointmentList,
 * blockedScheduleAppointmentList, searchAppointmentsByCriteria,
 * cancelScheduledAppointment. The controller catches LOG + return the
 * HONEST ERROR envelope (COMMON_ERROR_MESSAGE — populated).
 *
 * LIVE-VERIFIED BY CALLING (body status): scheduleAppointmentList with
 * {organizationId, unitId, offset, noOfRecordsPerPage} -> HTTP 200 body
 * {"status":"error","code":"501","message":"Something went wrong!!"} — the
 * handler is LIVE; the list path throws server-side (root cause owed: a
 * phantom column or missing join in the large GET_SCHEDULED_APPOINTMENTS_
 * SLOTS_LIST query).
 *
 * FINDING (the masked-error class on an OPD path): the DAO's
 * scheduledAppointmentList catch (AppointmentSchedulingDaoImpl:872-875)
 * logs the exception and falls to `return new Response(ERROR, null, null,
 * null, null)` — the NULL-MESSAGE envelope; the service rethrows it as
 * ApplicationException with a null message; the controller then returns
 * the populated "Something went wrong!!" envelope. The error IS surfaced
 * (the user sees a message) but the UNDERLYING EXCEPTION IS DISCARDED at
 * the DAO — debugging the 501 requires the log, not the response. NOT
 * FIXED (Rule 10) — surfaced for the BR scoped-check.
 *
 * SCOPED BACKEND CHECK: the save (scheduleAppointment) was NOT live-probed
 * (full SlotMasterDto contract unknown). Honest position: 'save not
 * exercised, contract unknown' (the screen-36 pattern). The save DAO shows
 * the GOOD guard pattern (appointmentDate null-guard ternary + TimeZone
 * defaults) with one latent NPE: getAppointmentDate2().substring(0,10)
 * (line 95) on the visit-type branch — unguarded substring.
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
export class AppointmentSchedulingService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/opd/scheduleAppointmentList`,
      { organizationId: orgId, unitId, offset: 0, noOfRecordsPerPage: 20 }
    );
  }
}
