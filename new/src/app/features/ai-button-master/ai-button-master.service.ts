import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AiButtonMasterRecord } from './ai-button-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/aiButtonMasterController.js,
 * all under `UserRoleMasterController.java`'s `@RequestMapping("api/global/ ")`
 * (legacy/Global/src/main/java/com/param/global/unit/controller/UserRoleMasterController.java) --
 * the SAME controller class `roleManagement` (Phase 4 batch 9) and
 * `roleCreation` (batch 10) already use, just different methods. Full
 * Controller -> Service -> DAO chain confirmed real for all 5 endpoints
 * below (UserRoleMasterServiceImpl.java / UserRoleMasterDaoImpl.java).
 *
 * IMPORTANT (see docs/ANGULAR_PENDING_FINDINGS.md entry N, this batch):
 * `public.button_master` and `public.m_unit_master.combined_ai_json` --
 * confirmed via live Tomcat logs and a direct `psql` check against this
 * environment's `his_local` database -- do not exist in this dev DB. Every
 * one of these 5 calls fails identically in legacy today (same DB, same
 * missing table/column). This is a local-environment gap, the same class
 * as BR-038/039/043/046 and entries 62/100/101 (the MRD table family), not
 * a rebuild defect -- the Java code chain is genuinely complete.
 */
@Injectable({ providedIn: 'root' })
export class AiButtonMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getButtonMaster(hospitalId: number, unitId: number): Observable<ApiResponse<never, AiButtonMasterRecord>> {
    return this.http.post<ApiResponse<never, AiButtonMasterRecord>>(`${this.base}/api/global/getButtonMaster`, {
      hospitalId,
      unitId,
    });
  }

  /** Response `object` is a raw JSON string: `{ urlObject: { URL }, aiPayload: {...} }` -- parsed by the caller. */
  getAiButtonConfigMaster(unitId: number): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/global/getAiButtonConfigMaster`, { unitId });
  }

  saveButtonMaster(payload: {
    label: string;
    systemPrompt: string;
    userPrompt: string;
    jsonObject: string;
    hospitalId: number;
    unitId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveButtonMaster`, payload);
  }

  updateButtonMaster(payload: {
    label: string;
    systemPrompt: string;
    userPrompt: string;
    tabId: number;
    buttonId: number;
    jsonObject: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateButtonMaster`, payload);
  }

  updateButtonMasterStatus(buttonId: number, isStatus: boolean): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateButtonMasterStatus`, {
      buttonId,
      isStatus,
    });
  }
}
