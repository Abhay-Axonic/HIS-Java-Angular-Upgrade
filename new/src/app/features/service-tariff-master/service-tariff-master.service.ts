/**
 * service-tariff-master — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/service-tariff-master.html
 *   (menu node 610 'Service Tariff Master', parent 570 Billing -> 531 Unit
 *   Master, controller serviceTariffMasterController loaded index.html:872).
 *
 * SCREEN 30 — LIVE class. State app.js:8257 binds serviceTariffMasterController
 * (NOT MasterController — passes the DECOY test).
 *
 * BACKEND BY ENDPOINT TRUTH (the 585 lesson): the Java controller is
 * UnitServiceTariffMasterController (billing/unit, @RequestMapping api/unit)
 * — a DIFFERENT class name than the state root (the class-name grep missed
 * it). Endpoints: getUnitServiceTariffMasterList (the legacy's PRIMARY grid
 * call, JS:999), getUnitServiceTariffMasterListForWeb (the WORKING variant),
 * saveUnitServiceTariffMaster, service/tariff/uniqueCheck, getBasePrice...
 *
 * FINDING (2026-08-10, the walk's masked-error class, a NEW mechanism):
 * the legacy's PRIMARY grid endpoint is UNCONDITIONALLY BROKEN for the
 * empty-filter case. GET /api/unit/getUnitServiceTariffMasterList with the
 * exact legacy payload (all filter lists []) -> HTTP 200 body
 * {"status":"error","code":null,"message":null} — a masked 200 with a NULL
 * message. MECHANISM (server stack, syntax error at or near ")"):
 * UnitServiceTariffMasterDaoImpl's dynamic query appends
 * `AND ustm.tariffId IN (:tariffMasterIdList)` then setParameterList with
 * the EMPTY list -> generated SQL `IN ()` -> PostgreSQL syntax error; the
 * DAO rethrows ServiceException (clean), but the CONTROLLER's catch is
 * EMPTY (UnitServiceTariffMasterController:51-52 `catch(Exception e){ }`)
 * — the exception is swallowed and the null-message error envelope
 * returned with HTTP 200. THE FIX EXISTS: the ForWeb DAO uses the no-op
 * placeholder `IN (1)` (always-true, no binding) — so the ForWeb endpoint
 * works. The legacy frontend calls the BROKEN endpoint; its grid cannot
 * load on a default (empty-filter) state. NOT FIXED (Rule 10) — BR
 * candidate (the fix pattern is the ForWeb DAO's placeholder).
 *
 * THE REBUILD: calls the WORKING ForWeb endpoint (the intended grid data,
 * dev-empty) and surfaces the base-endpoint defect honestly on an amber
 * banner (the screen-17/24/25 pattern).
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
export class ServiceTariffMasterService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // the WORKING variant (the ForWeb DAO's IN (1) placeholder): the legacy
    // primary endpoint is broken for empty filters (IN () syntax error)
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/unit/getUnitServiceTariffMasterListForWeb`,
      {
        unitId, organizationId: orgId,
        tariffMasterList: [], paymentEntitlementList: [], visitTypeList: [],
        patientTypeList: [], bedCategoryList: [], specialityList: [],
        subSpecialityList: [], serviceList: [],
      }
    );
  }
}
