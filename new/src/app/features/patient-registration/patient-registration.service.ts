/**
 * patient-registration — parity rebuild of the legacy
 *   views/master/UnitMaster/.../patient-registration.html (menu node 3
 *   'Patient Registration', parent 2 Opd, controller
 *   patientRegistrationController loaded index.html:897).
 *
 * SCREEN 40 — LIVE class (the OPD subtree's first candidate; the sweep
 * classified it LIVE with state+node+backend, C7 verified genuinely new).
 * The screen is the patient-registration TRANSACTION surface: patient
 * search (the primary read), patient + kin-details registration (the save),
 * update, convert.
 *
 * BACKEND BY ENDPOINT TRUTH: PatientRegistrationController (api/global):
 * patientRegistration POST (save), patientSearch, getPatientDetails,
 * convertPatient, updatePatient + kin-details CRUD. The controller's
 * catches return the honest ERROR envelope (COMMON_ERROR_MESSAGE) — clean,
 * no swallow. patientSearch's service + DAO catches RETHROW (LOUD).
 *
 * LIVE-VERIFIED BY CALLING (body status): patientSearch with the legacy's
 * empty-criteria payload -> HTTP 200 body {"status":"error","code":"501",
 * "message":null} — the endpoint HANDLER is live (the request reached the
 * service), but the empty-criteria path THROWS (root cause not yet traced
 * — candidate classes: generateQueryForPatientSearch with all-empty params,
 * or a phantom in GET_PATIENT_DATA). The 501-null-message envelope is the
 * honest-error class (message lost in the rethrow chain), NOT a masked
 * false-success. The GET_PATIENT_DATA query carries the walk's INNER-JOIN
 * drop shape (patient.appointmentMastersList INNER JOIN — a patient with
 * no appointment is dropped from search results).
 *
 * SCOPED BACKEND CHECK: the save (patientRegistration) was NOT live-probed
 * — its payload is the full patient+kin DTO contract; probing a billing/
 * patient write without the complete contract risks a false-success
 * reading. Honest position: 'save not exercised, contract unknown'
 * (flagged for the BR's scoped write-path check, the screen-36 pattern).
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

export interface PatientSearchCriteria {
  uhidNumber: string;
  patientName: string;
  genderId: string;
  mobileNumber: string;
  dob: string;
  identificationNumber: string;
  identificationTypeId: string;
  unitId: number;
  organizationId: number;
}

@Injectable({ providedIn: 'root' })
export class PatientRegistrationService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  search(orgId: number, unitId: number, criteria: Partial<PatientSearchCriteria>): Observable<ApiEnvelope> {
    const payload: PatientSearchCriteria = {
      uhidNumber: criteria.uhidNumber ?? '',
      patientName: criteria.patientName ?? '',
      genderId: criteria.genderId ?? '',
      mobileNumber: criteria.mobileNumber ?? '',
      dob: criteria.dob ?? '',
      identificationNumber: criteria.identificationNumber ?? '',
      identificationTypeId: criteria.identificationTypeId ?? '',
      unitId,
      organizationId: orgId,
    };
    return this.http.post<ApiEnvelope>(`${this.base}/api/global/patientSearch`, payload);
  }
}
