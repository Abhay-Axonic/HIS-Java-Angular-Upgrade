import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DoctorRegistrationRow } from './doctor-registration.models';

/**
 * Endpoint path taken directly from the hardcoded URI in
 * `controller/master/unit/doctorRegistrationController.js:4877`
 * (BASE_URL + ROOT_URL + "/api/global/doctorDetailsUniversalSearch" — no
 * url.config.js constant for THIS call; SEARCHDOCTORDETAILSBYCRITERIA at
 * global.url.config.js:17-18 is a different endpoint,
 * /searchDoctorDetailsByCriteria) — not guessed. Backend confirms
 * POST /doctorDetailsUniversalSearch (DoctorMasterController.java:306-307).
 * POST body is the legacy `doctorSearchObj` (ctrl:192-201: doctorName:'',
 * specialityId:'0', docDesignationId:'0', docCategoryId:'0',
 * organizationId, unitId, typeId:1, identificationTypeId:0) plus
 * offset/noOfRecordsPerPage added at ctrl:876-877. Confirmed live
 * 2026-08-12: SUCCESS with an empty listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class DoctorRegistrationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:4877 (doctorRegistrationController.js, hardcoded URI), body
  // doctorSearchObj ctrl:192-201 + offset/noOfRecordsPerPage ctrl:876-877 —
  // POST /api/global/doctorDetailsUniversalSearch. Dev-empty: SUCCESS + [].
  getList(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, DoctorRegistrationRow>> {
    return this.http.post<ApiResponse<never, DoctorRegistrationRow>>(
      `${this.base}/api/global/doctorDetailsUniversalSearch`,
      {
        doctorName: '',
        specialityId: '0',
        docDesignationId: '0',
        docCategoryId: '0',
        organizationId,
        unitId,
        typeId: 1,
        identificationTypeId: 0,
        offset: 0,
        noOfRecordsPerPage: 10,
      },
    );
  }
}
