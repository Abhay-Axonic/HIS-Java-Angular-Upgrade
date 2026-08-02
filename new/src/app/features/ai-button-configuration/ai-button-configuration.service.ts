import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ButtonMasterOption, DoctorOption, SavedConfigRow, SpecialityOption } from './ai-button-configuration.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/aiButtonConfugurationController.js,
 * all under `UserRoleMasterController.java`'s `@RequestMapping("api/global/ ")`
 * -- the SAME controller class `aIButtonMaster` (Phase 4 batch 10),
 * `roleManagement` (batch 9) and `roleCreation` (batch 10) already use.
 * Full Controller -> Service -> DAO chain confirmed real for every endpoint
 * below (UserRoleMasterServiceImpl.java / UserRoleMasterDaoImpl.java).
 *
 * IMPORTANT (see docs/ANGULAR_PENDING_FINDINGS.md entry 113 and this
 * batch's own entry): `public.button_master` -- confirmed via entry 113 --
 * AND `public.button_master_config` -- confirmed independently via a direct
 * `psql \dt`/`to_regclass` check during this batch's own audit -- do not
 * exist in this dev DB (`his_local`). Every one of `getButtonMaster`,
 * `getSavedConfig`, `saveConfig`, `getAiButtonConfigMasterForDocAndSpeciality`
 * and `updateButtonMasterConfigStatus` fails identically in legacy today
 * (same DB, same missing tables) -- confirmed live via real POSTs against
 * unmodified legacy returning `{"status":"error","code":"501","message":
 * "Something went wrong!!"}`. This is a local-environment gap, the same
 * class as BR-038/039/043/046/entry 113, not a rebuild defect -- the Java
 * code chain is genuinely complete. `getDoctor`/`getSpeciality` DO work
 * (they read `doctor.m_doctor_master`/`doctor.m_speciality_master`, which
 * exist and are seeded).
 */
@Injectable({ providedIn: 'root' })
export class AiButtonConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDoctors(organizationId: number, unitId: number): Observable<ApiResponse<never, DoctorOption>> {
    return this.http.post<ApiResponse<never, DoctorOption>>(`${this.base}/api/global/getDoctor`, {
      organizationId,
      unitId,
    });
  }

  getSpecialities(organizationId: number, unitId: number): Observable<ApiResponse<never, SpecialityOption>> {
    return this.http.post<ApiResponse<never, SpecialityOption>>(`${this.base}/api/global/getSpeciality`, {
      organizationId,
      unitId,
    });
  }

  getButtonMaster(hospitalId: number, unitId: number): Observable<ApiResponse<never, ButtonMasterOption>> {
    return this.http.post<ApiResponse<never, ButtonMasterOption>>(`${this.base}/api/global/getButtonMaster`, {
      hospitalId,
      unitId,
    });
  }

  /** Response `object` is a raw JSON string (or null if unconfigured) -- parsed by the caller. */
  getAiButtonConfigMasterForDocAndSpeciality(buttonId: number, hospitalId: number): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.base}/api/global/getAiButtonConfigMasterForDocAndSpeciality`, {
      buttonId,
      hospitalId,
    });
  }

  saveConfig(payload: {
    doctorId: number | null;
    specialityId: number | null;
    doctorName: string | null;
    specialityName: string | null;
    label: string;
    buttonId: number;
    /** Must be `null` for a genuine create -- see ai-button-configuration.ts's class doc
     * comment (BR-PENDING-aibuttonconfiguration-stale-configid) for why this matters. */
    configId: number | null;
    jsonObject: string;
    hospitalId: number;
    unitId: number;
  }): Observable<ApiResponse & { code?: string }> {
    return this.http.post<ApiResponse & { code?: string }>(`${this.base}/api/global/saveConfig`, payload);
  }

  getSavedConfig(hospitalId: number, unitId: number): Observable<ApiResponse<never, SavedConfigRow>> {
    return this.http.post<ApiResponse<never, SavedConfigRow>>(`${this.base}/api/global/getSavedConfig`, {
      hospitalId,
      unitId,
    });
  }

  /**
   * NOTE: the wire field is named `buttonId` but actually carries the
   * `configId` being toggled -- matches legacy's real (mis-named but
   * functioning) DTO reuse: the view calls
   * `statusChanged(button.configId, button.isStatus)`, and the DAO does
   * `update button_master_config set status=:status where config_id=:id`
   * bound from `buttonMasterDto.getButtonId()`. Preserved verbatim; renaming
   * the wire key would break the real backend, which isn't being touched.
   */
  updateButtonMasterConfigStatus(
    configId: number,
    isStatus: boolean,
    hospitalId: number,
    unitId: number,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateButtonMasterConfigStatus`, {
      buttonId: configId,
      isStatus,
      hospitalId,
      unitId,
    });
  }
}
