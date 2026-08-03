import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PatientSourceDetail,
  PatientSourceRow,
  PatientSourceSavePayload,
  PatientSourceStatusPayload,
  PatientSourceUpdatePayload,
} from './patient-source-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/global/patientSourceMasterController.js
 * and resources/assets/js/config/adt.url.config.js:199-203/382 -- not
 * guessed. All POST, all under `/adt`.
 *
 * Reached via the real, sidebar-linked `/commonMaster/patientsource` route
 * (`common-master-screen.html`) -- confirmed live 2026-07-28: real
 * save/update/status-toggle/edit-fetch round trips against real,
 * unmodified legacy. See patient-source-master.ts's class doc comment for
 * the full writeup.
 */
@Injectable({ providedIn: 'root' })
export class PatientSourceMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:65-114 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getPatientSourceList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, PatientSourceRow>> {
    return this.http.post<ApiResponse<never, PatientSourceRow>>(`${this.base}/adt/getPatientSourceMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:65-113 -- POST { organizationId }, count returned in `object`.
  getPatientSourceCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getPatientSourceCount`, {
      organizationId,
    });
  }

  // ctrl:177-238 -- confirmed live 2026-07-28, real save, real persistence.
  savePatientSource(payload: PatientSourceSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/savePatientSourceMaster`, payload);
  }

  // ctrl:241-265 -- POST { id }; response.data.listObject[0] is the record.
  getPatientSourceById(id: number): Observable<ApiResponse<never, PatientSourceDetail>> {
    return this.http.post<ApiResponse<never, PatientSourceDetail>>(`${this.base}/adt/getPatientSourceById`, { id });
  }

  // ctrl:316-366 -- confirmed live 2026-07-28, real update, real persistence.
  // No confirm-modal gate (matches occupation-master/relation-master's
  // own save-only asymmetry).
  updatePatientSource(payload: PatientSourceUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updatePatientSourceMaster`, payload);
  }

  // ctrl:276-313 -- confirmed live 2026-07-28, real status toggle.
  updatePatientSourceStatus(payload: PatientSourceStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForPatientSource`, payload);
  }
}
