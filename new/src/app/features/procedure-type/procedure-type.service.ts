/**
 * procedure-type — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/procedure-type.html
 *   (menu node 520 'Procedure Type', parent 514 OTIMS -> 369 Global Master,
 *   controller procedureTypeMasterController, loaded index.html:1732).
 *
 * SCREEN 21 — the first screen selected under the C7 ALREADY-REBUILT gate
 * (owner directive 2026-08-10): the C7 sweep across the 19 OTIMS states
 * showed procedureType is GENUINELY NOT-REBUILT (anaesthesiaType,
 * operationTableType, checklistMaster were already rebuilt by prior batches
 * under kebab dirs the walk's hand-list had misnamed).
 *
 * CLASS: LIVE (state registered app.js:12385 with resolve gate item.id==520,
 * menu node 520->514->369, controller loaded, REAL full-CRUD backend
 * ProcedureTypeMasterConttroller /api/otims/* — note the typo'd class name
 * 'Conttroller', verified live). REVISED classification: the walk previously
 * walked past 520 as NOT-A-SCREEN-YET — that was WRONG, this is a live
 * screen with a working backend (see README).
 *
 * FINDING (backend, NOT fixed — Rule 10, BR candidate): the save path has
 * the BR-187 false-success shape — ProcedureTypeMasterServiceImpl:74
 * `return new Response(SUCCESS, ...)` sits OUTSIDE the
 * if(res.getObject()!=null) guard (58-71); the DAO catch-and-returns the
 * ERROR envelope without rethrowing, so an incomplete-DTO save (updatedDate
 * omitted -> DAO NPE) returns SUCCESS with nothing written. The walk's
 * SEVENTH confirmed false-success instance. The rebuild mitigates
 * client-side: always sends both dates + rejects incomplete forms.
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface ProcedureTypeRow {
  procedureTypeId: number;
  procedureTypeCode: string;
  procedureTypeName: string;
  procedureTypeDesc?: string | null;
  status: string; // 'A' | 'I'
}

export interface ProcedureTypeSavePayload {
  procedureTypeId?: number;
  procedureTypeCode: string;
  procedureTypeName: string;
  procedureTypeDesc?: string | null;
  status: string;
  orgId: number;
  unitId: number;
  createdBy?: number;
  updatedBy?: number;
  createdDate?: string;
  updatedDate?: string;
}

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: ProcedureTypeRow[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class ProcedureTypeService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/getListProcedureTypeMaster`, {
      orgId, unitId, offset, noOfRecordsPerPage,
    });
  }

  count(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/getProcedureTypeMasterCount`, { orgId, unitId });
  }

  byId(procedureTypeId: number, orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(`${this.base}/api/otims/getProcedureTypeMasterById/${procedureTypeId}/${orgId}/${unitId}`);
  }

  save(payload: ProcedureTypeSavePayload): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/saveProcedureTypeMaster`, payload);
  }

  update(payload: ProcedureTypeSavePayload): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/updateProcedureTypeMaster`, payload);
  }

  status(procedureTypeId: number, orgId: number, unitId: number, status: string): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/updateProcedureTypeMasterStatus`, {
      procedureTypeId, orgId, unitId, status,
    });
  }
}
