import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  HospitalDoctorRow,
  HospitalDoctorSavePayload,
  HospitalDoctorStatusPayload,
  OutsideHospitalDetail,
  OutsideHospitalRow,
  OutsideHospitalSavePayload,
  OutsideHospitalStatusPayload,
  OutsideHospitalUpdatePayload,
} from './out-side-hospital-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/outSideHospitalMasterController.js
 * (a SELF-REGISTERED state -- `.config($stateProvider)` inside the
 * controller file itself, no entry in config/app.js) and confirmed for
 * real on the Java side against `OutsideHospitalMasterController.java`
 * (`@RequestMapping("/api/global")`) -- full Controller->Service->DAO
 * chains for all 9 endpoints this screen calls, confirmed via direct
 * source read (Step 0's backend-existence check, entry 67's methodology).
 * `public.m_outside_hospital_master` exists in this local dev DB (0 seeded
 * rows). `public.t_outside_hospital_doctor_mapper` (backing the "Doctor
 * Details" sub-feature below) does NOT exist in this local dev DB --
 * confirmed via `pg_tables` -- same recurring dev-DB-gap class as
 * BR-038/039/043/046 and entries 100/101/113/115-117; the Java chain
 * (`OutsideHospitalDoctorMapperDaoImpl.java`) is genuinely real and
 * correctly implemented, only the table is missing. Held for owner
 * approval, not fixed here.
 */
@Injectable({ providedIn: 'root' })
export class OutSideHospitalMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:51-92 -- POST { organizationId, offset, noOfRecordsPerPage }
  // (unitId is commented out in legacy's own payload, ctrl:61 -- the
  // backend's own count-via-list-size logic also only filters by
  // organizationId, not unitId; reproduced by NOT sending unitId here,
  // matching legacy's actual request body byte-for-byte).
  getOutsideHospitalMasterList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<number, OutsideHospitalRow>> {
    return this.http.post<ApiResponse<number, OutsideHospitalRow>>(
      `${this.base}/api/global/getOutsideHospitalMasterList`,
      { organizationId, offset, noOfRecordsPerPage },
    );
  }

  // ctrl:143-199 -- real create, gated behind the shared confirm modal
  // (popUpFlag/PopupOkBtn/showModal).
  saveOutsideHospitalMaster(payload: OutsideHospitalSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveOutsideHospitalMaster`, payload);
  }

  // ctrl:216-244 -- GET, id/organizationId/unitId as path segments;
  // response.data.listObject[0] is the record.
  getOutsideHospitalMasterById(
    outsideHospitalId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, OutsideHospitalDetail>> {
    return this.http.get<ApiResponse<never, OutsideHospitalDetail>>(
      `${this.base}/api/global/getOutsideHospitalMasterById/${outsideHospitalId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:247-295 -- real update, NO confirm modal (unlike save).
  updateOutsideHospitalMaster(payload: OutsideHospitalUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateOutsideHospitalMaster`, payload);
  }

  // ctrl:297-333 -- real status toggle. See .models.ts doc comment: sent
  // here as an independent payload, NOT built from a shared mutated form
  // object (disclosed deviation from legacy's shared-`$scope.common`
  // mutation race, same class as entries 94/107).
  updateOutsideHospitalMasterStatus(payload: OutsideHospitalStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateOutsideHospitalMasterStatus`, payload);
  }

  // ---- "Doctor Details" sub-feature (ctrl:335-511) ----

  // ctrl:415-431 -- POST { organizationId, unitId, outsideHospitalId }.
  getHospitalDoctorsList(
    organizationId: number,
    unitId: number,
    outsideHospitalId: number,
  ): Observable<ApiResponse<never, HospitalDoctorRow>> {
    return this.http.post<ApiResponse<never, HospitalDoctorRow>>(
      `${this.base}/api/global/getOutsideHospitalDoctorMapperList`,
      { organizationId, unitId, outsideHospitalId },
    );
  }

  // ctrl:348-412 -- real create/update (single endpoint, branches on
  // whether outsideHosDocId is present -- see
  // OutsideHospitalDoctorMapperDaoImpl.saveOutsideHospitalDoctorMapper).
  saveOutsideHospitalDoctorMapper(payload: HospitalDoctorSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveOutsideHospitalDoctorMapper`, payload);
  }

  // ctrl:434-475 -- POST { outsideHosDocId, organizationId, unitId };
  // response.data.listObject[0] is the record.
  getOutsideHospitalDoctorMapperById(
    outsideHosDocId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, { outsideHosDocId: number; hospDoctorName: string }>> {
    return this.http.post<ApiResponse<never, { outsideHosDocId: number; hospDoctorName: string }>>(
      `${this.base}/api/global/getOutsideHospitalDoctorMapperById`,
      { outsideHosDocId, organizationId, unitId },
    );
  }

  // ctrl:434-475 (updateStatusDoctor) -- real status toggle for a mapped doctor.
  updateOutsideHospitalDoctorMapperStatus(payload: HospitalDoctorStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateOutsideHospitalDoctorMapperStatus`, payload);
  }
}
