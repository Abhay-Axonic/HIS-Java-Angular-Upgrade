/**
 * patient-list — parity rebuild of the legacy patient-list screen (menu
 *   node 8 'Patient List', parent 2 Opd, controller patientListController
 *   loaded index.html:898; template views/common/appointments/
 *   patient-list.html).
 *
 * SCREEN 44 — LIVE class (the OPD subtree's fifth candidate; C7 verified
 * genuinely new). The screen lists patients (with health-card + search
 * paths).
 *
 * BACKEND BY ENDPOINT TRUTH:
 * - GETPATIENTLIST = /api/global/getPatientDetails — LIVE-VERIFIED by
 *   CALLING: 200 success listObject [] (dev-empty) — a WORKING list
 *   endpoint (the first OPD list that returns success, not the shared 501
 *   path);
 * - the search path uses GETTEMPORYPATIENTFORPREREGISTRATION =
 *   /api/global/patientSearch (the screen-40 endpoint — the 501 honest
 *   error on empty criteria, root cause owed there);
 * - the health-card feed GETHEALTHCARD = /api/global/getHealthCard.
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
export class PatientListService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/getPatientDetails`,
      { organizationId: orgId, unitId }
    );
  }
}
