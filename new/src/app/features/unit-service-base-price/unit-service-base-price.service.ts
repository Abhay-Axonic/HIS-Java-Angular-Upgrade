/**
 * unit-service-base-price — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/unit-service-base-price.html
 *   (menu node 585 'Unit Service Base Price', parent 570 Billing -> 531 Unit
 *   Master, controller unitServiceBasePriceController loaded index.html:878).
 *
 * SCREEN 28 — LIVE class (the owner's 585/592 call; 585 is the one the
 * class-name grep MISSED and endpoint truth FOUND — the sweep's strongest
 * liveness evidence). State app.js:9552 binds unitServiceBasePriceController
 * (NOT MasterController — passes the DECOY test).
 *
 * BACKEND BY ENDPOINT TRUTH (the 585 lesson): the controller JS calls
 * /api/unit/billing/getServiceBasePriceList + countServiceBasePriceMaster,
 * and the Java controller is ServiceBasePriceMasterController
 * (billing/unit, com.param.billing.unit, @RequestMapping api/unit/billing)
 * — a DIFFERENT class name than the state root, which is why the sweep's
 * class-name grep missed it. Endpoints: list, byId, update, status, count,
 * search.
 *
 * LIVE-VERIFIED BY CALLING (body status): POST /api/unit/billing/getService-
 * BasePriceList {orgnisationId:1, unitId:1, offset:0, noOfRecordsPerPage:20}
 * -> 200 success with REAL rows (serviceBasePriceId 1, serviceMasterId 3,
 * basePrice 250.0); POST .../countServiceBasePriceMaster {orgnisationId:1}
 * -> 200 success object: 2. NOTE THE CONTRACT: the legacy payload key is
 * `orgnisationId` (the misspelling — NOT organizationId/orgId; the latter
 * 400s HIS-400, the probe-payload class).
 *
 * SCOPED BACKEND CHECK (billing write-path focus): FALSE-SUCCESS SHAPE
 * ABSENT — ServiceBasePriceMasterDaoImpl's update catches BOTH RETHROW
 * ServiceException (the global-handler pattern; SUCCESS only after the real
 * update). INNER-JOIN drop PRESENT (the list INNER JOINs serviceMaster ->
 * specialityMaster -> subSpecialityMaster — a base-price row with a missing
 * join partner is silently dropped; the walk's sixth list-path instance).
 * DDL clean (14 columns, visit_type_id exists — no phantom; the screen-17
 * contrast). Dup-check scoping not live-verified (the update path is the
 * primary write; flagged for the BR).
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface ServiceBasePriceRow {
  serviceBasePriceId: number;
  serviceMasterId: number;
  serviceName?: string;
  visitTypeId?: number | null;
  basePrice: number;
  fromDate?: string;
  toDate?: string;
  status: string;
}

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class UnitServiceBasePriceService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // the legacy contract: `orgnisationId` (the misspelled key, legacy
    // JS:104-105) — NOT organizationId/orgId; the latter 400s HIS-400
    return this.http.post<ApiEnvelope>(`${this.base}/api/unit/billing/getServiceBasePriceList`, {
      orgnisationId: orgId, unitId, offset: 0, noOfRecordsPerPage: 100,
    });
  }
}
