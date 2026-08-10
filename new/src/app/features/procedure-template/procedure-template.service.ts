/**
 * procedure-template — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/procedure-template.html
 *   (state registered app.js:20447, controller procedureTemplateController,
 *   loaded index.html:1743).
 *
 * SCREEN 25 — DANGLE-ORPHANED (the last OTIMS dangle-with-backend): NO menu
 * node links #/procedureTemplate. Node 515 ' Procedure Configuration'
 * (parent 514) links '#/procedureConfiguration' — a state that does NOT
 * exist (dead link); node 696 'Procedure Configurations' has no actionLink.
 * The state takes params.commonObj (a drill-down from the parent
 * procedureConfiguration screen — the legacy controller reads $scope.common
 * populated by the PREVIOUS screen). The rebuild registers the route so the
 * state is reachable by URL.
 *
 * PHANTOM-BROKEN CORE — the walk's SIXTH phantom finding: the config-template
 * read (getProcedureConfigTemplate) and the save (saveProcedureConfigTemplate
 * calls it internally) BOTH 500 with correlation-id envelopes. Root cause:
 * GET_CHECKLIST_LIST_BY_PROCEDURE_CONFIG_ID selects
 * `otimsRoleMaster.roleDesc as roleName` but public.m_role_master's DDL
 * column is role_name — role_desc DOES NOT EXIST (RoleMaster.java:25 maps
 * roleDesc; the DDL never got a role_desc column). The checklist query's
 * 3-way INNER JOIN (checklist -> checklistMaster -> roleMaster) surfaces the
 * phantom on every call. NOT FIXED (Rule 10) — BR-190-class candidate.
 *
 * The rebuild CALLS the list + the template fetch, detects the error
 * envelopes, and names the phantom honestly (the screen-17/24 pattern).
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
export class ProcedureTemplateService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  configList(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(`${this.base}/api/otims/getProcedureConfigList/${orgId}/${unitId}`);
  }

  template(orgId: number, unitId: number, procedureConfigId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/getProcedureConfigTemplate`, {
      orgId, unitId, procedureConfigId,
    });
  }
}
