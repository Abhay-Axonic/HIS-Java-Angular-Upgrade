/**
 * doctor-consultation — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/doctor-consultation-master.html
 *   (menu node 612 'Doctor Consulation' [sic], parent 570 Billing -> 531
 *   Unit Master, controller doctorConsulationMasterController loaded
 *   index.html:871).
 *
 * SCREEN 31 — LIVE class. State app.js:8317 `doctorConsulationMaster` (the
 * LEGACY TYPO — the node links #/doctorConsulationMaster; C5's DEAD flag
 * was a state-name mismatch, not an absent screen; the sweep's LIVE was
 * right). Controller binds doctorConsulationMasterController (NOT
 * MasterController — passes the DECOY test).
 *
 * BACKEND BY ENDPOINT TRUTH (the 585 lesson): the Java controller is
 * DoctorConsultationServiceMapperController (Global,
 * @RequestMapping api/billing/unit) — a DIFFERENT class name than both
 * state spellings. Endpoints: doctorConsultationServiceMapper (save),
 * getDoctorConsultationServiceMapperList, ListCount,
 * updateDoctorConsultationServiceMapper, ById, Status,
 * BySpecialityAndDoctorId.
 *
 * LIVE-VERIFIED BY CALLING (body status): GET the list -> 200 success
 * listObject [] (dev-empty); the count -> 200 success object: 0. PAYLOAD
 * CONTRACT: `tabNumber: 1` is REQUIRED (omitting it -> masked 200 error,
 * the probe-payload class; the controller's empty catch
 * DoctorConsultationServiceMapperController:60-62 swallows into the
 * null-message envelope).
 *
 * SCOPED BACKEND CHECK (billing write-path): FALSE-SUCCESS ABSENT — the
 * save (DAO:45-63) and updateStatus catch-and-FALL-THROUGH to honest ERROR
 * envelopes (never SUCCESS-from-catch; SUCCESS only after the real write).
 * The getDate calls pass a DATE object (the safe overload — no
 * getDate(null) NPE class, the BR-187/screen-26 contrast). INNER-JOIN drop:
 * the list LEFT JOINs doctorMaster + INNER JOINs speciality/newVisit/
 * followupVisit services (a row with a missing INNER partner is dropped —
 * the walk's class, by construction; dev-empty so not demonstrated).
 * DDL clean (m_doctor_consultation_service_mapper, doctor schema).
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
export class DoctorConsultationService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // tabNumber: 1 is REQUIRED (the legacy JS:110-115 contract)
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/billing/unit/getDoctorConsultationServiceMapperList`,
      { organizationId: orgId, unitId, offset: 0, noOfRecordsPerPage: 100, tabNumber: 1 }
    );
  }

  count(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/billing/unit/getDoctorConsultationServiceMapperListCount`,
      { organizationId: orgId, unitId, tabNumber: 1 }
    );
  }
}
