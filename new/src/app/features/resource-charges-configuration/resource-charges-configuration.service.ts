/**
 * resource-charges-configuration — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/resource-charges-configuration.html
 *   (menu node 761 'Resource Charges Configuration', parent 514 OTIMS ->
 *   369 Global Master, controller resourceChargesConfigurationController,
 *   loaded index.html:1657).
 *
 * SCREEN 23 — DANGLE class (the 477/478/479 precedent; same as screen 22's
 * surgicalCalendar). State registered app.js:18871 (url
 * /resourceChargesConfiguration, NO resolve gate), menu node 761 EXISTS but
 * carries NO actionLink (the menu cannot navigate to it), controller
 * loaded, REAL backend ResourceChargeConfigurationController /api/otims/*
 * (6 endpoints). The rebuild REGISTERS the route (moduleId 761) and
 * RESTORES a menu-advertised screen.
 *
 * LIVE-VERIFIED BY CALLING (body inspected, ENDPOINT-VERIFICATION STANDARD):
 * GET /api/otims/resourceChargeConfiguration/1/1 -> 200 success listObject []
 * (dev-empty); POST /adt/getActiveSpecialityList -> 200 success 2 REAL rows
 * (General Medicine, Orthopedics); GET /api/procedure/getRoleList/1/1 -> 200
 * success 2 REAL rows (Surgeon, Ass. Surgeon); POST
 * /api/otims/getResourceChareges -> 200 success [] (dev-empty).
 *
 * SCOPED BACKEND CHECK: phantom PASS (live 200s prove resolution);
 * INNER-JOIN FINDING — GET_RESOURCE_CHARGE_CONFIGURATION_LIST INNER JOINs
 * specialityMaster + serviceMaster + procedureRoleMaster (a config row with
 * a missing join-partner is DROPPED — the walk's FIFTH list-path instance);
 * dup-check PASS — IS_RESOURCE_CHARGE_CONFIGURATION_ALREADY_EXIST and
 * _NOT_BY_ID both org+unit+role+service all-AND scoped (NOT the Q29/Q32
 * class); org-trust FINDING (payload-carried org, module-wide class,
 * screen-17 record); swallowed PASS — the DAO and service both RETHROW
 * ServiceException (the global-handler pattern) and the service returns the
 * DAO's response verbatim — NO false-success shape (the walk's first clean
 * write path in the OTIMS module).
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface ResourceChargeRow {
  resourceChargeConfigurationId: number;
  specialityId: number;
  specialityName: string;
  serviceId: number;
  serviceStandardName: string;
  amount: number;
  status: string; // 'A' | 'I'
  roleId: number;
  procedureRoleName: string;
}

export interface SpecialityRow {
  specialityId: number;
  specialityName: string;
  isClinicalSpeciality?: string;
}

export interface RoleRow {
  id: number;
  name: string;
}

export interface ResourceChargeSavePayload {
  orgId: number;
  specialityId: number;
  roleId: number;
  serviceId: number;
  amount: number;
  status: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
}

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class ResourceChargesConfigurationService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(`${this.base}/api/otims/resourceChargeConfiguration/${orgId}/${unitId}`);
  }

  unitServices(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // the live unit-service feed (the legacy typeahead's search endpoint is
    // commented out — dead in legacy; this feed is the working alternative)
    return this.http.get<ApiEnvelope>(`${this.base}/global/UnitServiceMapper/unit/${unitId}/org/${orgId}/0/100`);
  }

  specialities(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/adt/getActiveSpecialityList`, { organizationId: orgId, unitId });
  }

  roles(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(`${this.base}/api/procedure/getRoleList/${orgId}/${unitId}`);
  }

  save(payload: ResourceChargeSavePayload): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/resourceChargeConfiguration`, payload);
  }

  update(payload: ResourceChargeSavePayload): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/updateResourceChargeConfiguration`, payload);
  }

  status(resourceChargeConfigurationId: number, status: string): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/updateResourceChargeConfigurationStatus`, {
      resourceChargeConfigurationId, status,
    });
  }
}
