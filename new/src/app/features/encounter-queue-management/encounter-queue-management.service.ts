/**
 * encounter-queue-management — parity rebuild of the legacy
 *   views/master/GlobalMaster/Encounter/encounter-queue-management.html
 *   (menu node 6 'Encounter Queue Management', parent 2 Opd, controller
 *   encounterQueueManagementController loaded index.html:952).
 *
 * SCREEN 42 — LIVE class (the OPD subtree's third candidate; C7 verified
 * genuinely new). The screen is a QUEUE-MANAGEMENT surface: nursing-station
 * list -> dept-by-station -> doctors-by-speciality; the queue renders from
 * the selected station's encounters.
 *
 * LIVE-VERIFIED BY CALLING (body status) — all three feeds:
 * - GET /api/global/getDetailedNursingStationList/{userId}/{org}/{unit} ->
 *   200 success with REAL rows (userNursingStationMpprId 7,
 *   nursingStationId 1);
 * - GET /api/global/getDeptListByNursingStationId/{station}/{org}/{unit} ->
 *   200 success listObject [1] object [1,2];
 * - POST /api/global/doctor/activeWithSpeciality {organizationId, unitId} ->
 *   200 success with REAL rows (doctorId 1, firstName Demo).
 *
 * SCOPED BACKEND CHECK: the screen's surface is feed-driven (no master
 * save in this controller's read path — the queue is a live view). No
 * write-path false-success shape on the read surface. The queue's
 * patient/encounter actions live in the encounter-consultation flow, not
 * this screen's controller.
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
export class EncounterQueueManagementService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  nursingStations(userId: number, orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/api/global/getDetailedNursingStationList/${userId}/${orgId}/${unitId}`
    );
  }

  departments(stationId: number, orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/api/global/getDeptListByNursingStationId/${stationId}/${orgId}/${unitId}`
    );
  }

  doctors(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/doctor/activeWithSpeciality`,
      { organizationId: orgId, unitId }
    );
  }
}
