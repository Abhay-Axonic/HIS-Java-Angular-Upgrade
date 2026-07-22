import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DeptSpecialityDetail,
  InstructionMasterRow,
  LanguageOption,
  RawSpecialityOption,
  SaveInstructionPayload,
  TranslatedInstructionRow,
} from './instruction-master.models';

/** `getInspectionMasterListById`'s response carries a THIRD payload field
 * beyond the standard `object`/`listObject` envelope
 * (`Response.java`'s `resObject`, InstructionMasterDaoImpl.java:249) --
 * `object` = the instruction record, `listObject` = its department
 * mappings, `resObject` = its saved translated-language rows
 * (ctrl:398-402). */
interface InstructionByIdResponse extends ApiResponse<Record<string, unknown>, DeptSpecialityDetail> {
  resObject?: TranslatedInstructionRow[];
}

/**
 * Endpoint paths and methods taken directly from
 * resources/assets/js/controller/master/global/common/inspectionMasterController.js.
 * Backend chain confirmed via full source read (Controller -> Service ->
 * DAO, all present -- see class comment in instruction-master.ts):
 *   POST /api/common/getInspectionMasterList       -> InstructionMasterController.java:60-68
 *   POST /api/common/getInspectionMasterCount       -> InstructionMasterController.java:107-115
 *   POST /api/common/saveInspectionMaster           -> InstructionMasterController.java:39-47
 *   POST /api/common/updateInspectionMaster         -> InstructionMasterController.java:70-78
 *   POST /api/common/updateInspectionMasterStatus    -> InstructionMasterController.java:97-105
 *   GET  /api/common/getInspectionMasterListById/{id}/{orgId} -> InstructionMasterController.java:49-58
 *   POST /api/common/getDeptSpecilityById            -> InstructionMasterController.java:129-137
 *   POST /api/common/getConvertedLanguageListById     -> InstructionMasterController.java:139-147
 *   POST /adt/getSpecialityList                       -> SpecialityMasterController.java:48
 *   GET  /LIS/global/listLanguageMaster/{orgId}/{unitId} -> LanguageMasterController.java:111-125
 *
 * `BASE_URL`/`ROOT_URL` are both `''` in this deployment, so these are the
 * complete paths.
 */
@Injectable({ providedIn: 'root' })
export class InstructionMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, InstructionMasterRow>> {
    return this.http.post<ApiResponse<never, InstructionMasterRow>>(
      `${this.base}/api/common/getInspectionMasterList`,
      { organizationId, offset, noOfRecordsPerPage },
    );
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getInspectionMasterCount`, {
      organizationId,
    });
  }

  getById(instructionId: number, organizationId: number): Observable<InstructionByIdResponse> {
    return this.http.get<InstructionByIdResponse>(
      `${this.base}/api/common/getInspectionMasterListById/${instructionId}/${organizationId}`,
    );
  }

  save(payload: SaveInstructionPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveInspectionMaster`, payload);
  }

  update(payload: SaveInstructionPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateInspectionMaster`, payload);
  }

  updateStatus(payload: {
    instructionId: number;
    status: 'A' | 'I';
    updatedDate: string;
    updatedBy: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateInspectionMasterStatus`, payload);
  }

  getDeptSpecilityById(payload: {
    organizationId: number;
    unitId: number;
    instructionId: number;
  }): Observable<ApiResponse<never, DeptSpecialityDetail>> {
    return this.http.post<ApiResponse<never, DeptSpecialityDetail>>(
      `${this.base}/api/common/getDeptSpecilityById`,
      payload,
    );
  }

  getConvertedLanguageListById(payload: {
    organizationId: number;
    unitId: number;
    instructionId: number;
  }): Observable<ApiResponse<never, TranslatedInstructionRow>> {
    return this.http.post<ApiResponse<never, TranslatedInstructionRow>>(
      `${this.base}/api/common/getConvertedLanguageListById`,
      payload,
    );
  }

  getSpecialityList(organizationId: number, unitId: number): Observable<ApiResponse<never, RawSpecialityOption>> {
    return this.http.post<ApiResponse<never, RawSpecialityOption>>(`${this.base}/adt/getSpecialityList`, {
      organizationId,
      unitId,
    });
  }

  /**
   * Known risk carried over from docs/ANGULAR_PENDING_FINDINGS.md entry
   * 114 (found on `frequencyMaster`, which calls this exact same shared
   * `LIS/global/listLanguageMaster` endpoint): the response returns HTTP
   * 200 but has been observed causing `HttpClient` to throw a parsing
   * error where legacy's AngularJS `$http` does not. Left unhandled here,
   * matching `frequencyMaster`'s own rebuild (background-only, does not
   * block this screen's list/save) -- not fixed in this Angular-only
   * session.
   */
  getLanguageMasterList(organizationId: number, unitId: number): Observable<ApiResponse<never, LanguageOption>> {
    return this.http.get<ApiResponse<never, LanguageOption>>(
      `${this.base}/LIS/global/listLanguageMaster/${organizationId}/${unitId}`,
    );
  }
}
