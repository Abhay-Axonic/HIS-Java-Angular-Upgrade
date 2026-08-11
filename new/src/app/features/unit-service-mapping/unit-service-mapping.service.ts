/**
 * unit-service-mapping — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/unit-service-mapping.html
 *   (menu node 584 'Unit Service Mapping', parent 570 Billing -> 531 Unit
 *   Master, controller UnitServiceMappingController — the DECOY test passes:
 *   a REAL controller with 28 template binds).
 *
 * SCREEN 36 — LIVE class (the recheck's first new LIVE: the sweep's
 * class-name grep missed it; per-screen endpoint truth found it — the 585
 * lesson). The screen maps org-level service masters to the unit: speciality
 * feed -> subspeciality-by-speciality -> services feed -> save mapping.
 *
 * LIVE-VERIFIED BY CALLING (body status):
 * - the mapping-list feed GET /billing/organizationMaster/speciality/1/
 *   subSpeciality/1/org/1/unit/1 -> 200 success listObject [] (dev-empty);
 * - the subspeciality feed GET /adt/subspeciality/orgId/1/unitId/1/
 *   specialityId/1 -> 200 success with REAL rows (Cardiology/CARDIO);
 * - the save mapper POST /global/UnitServiceMapper -> 400 (the probe-payload
 *   class — the save payload needs the exact mapper contract; not probed
 *   further on a WRITE path without the full contract — flagged for the BR).
 *
 * SCOPED BACKEND CHECK: the save (UnitServiceMapper) was NOT live-probed
 * (its payload contract is mapper-specific; probing a billing WRITE without
 * the full contract risks a false-success reading — the honest position is
 * 'save not exercised, contract unknown'). The read feeds are live.
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
export class UnitServiceMappingService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  subspecialities(orgId: number, unitId: number, specialityId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/adt/subspeciality/orgId/${orgId}/unitId/${unitId}/specialityId/${specialityId}`
    );
  }

  mappings(orgId: number, unitId: number, specialityId: number, subSpecialityId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/billing/organizationMaster/speciality/${specialityId}/subSpeciality/${subSpecialityId}/org/${orgId}/unit/${unitId}`
    );
  }
}
