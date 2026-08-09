import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  AllergyApiEnvelope,
  AllergyRow,
  AllergyTypeOption,
  SaveAllergyPayload,
  UpdateAllergyStatusPayload,
} from './allergy-master.models';

/**
 * The allergy-master endpoints (the legacy allergyMasterController's full
 * CRUD). All verified by CALLING with the cached token (QF-2026-08-09-E:
 * call, don't grep):
 *   GET  /api/global/getAllergyTypeMaster            -> HTTP 500 (backend defect)
 *   POST /api/global/getAllergyMasterList            -> 200, envelope status "error"
 *   POST /api/global/getAllergyMasterListCount       -> 200, envelope "success"
 *   GET  /api/global/getAllergyMasterListById/{id}/{org}/{unit} -> 200, "error"
 *   POST /api/global/saveAllergyMaster               -> 200, "error"
 *   POST /api/global/updateAllergyMaster             -> 200, "error"
 *   POST /api/global/updateAllergyMasterStatus       -> 200, "error"
 * The list/detail/save/update/status failures are the PHANTOM-COLUMN drift
 * (the named query selects cims_code/cims_type that emr.m_allergy_master
 * lacks — SQLGrammarException, swallowed to the generic envelope). The count
 * works because its query does not select the phantom. Logged, not fixed.
 */
@Injectable({ providedIn: 'root' })
export class AllergyMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  searchAllergyTypes(): Observable<AllergyApiEnvelope<AllergyTypeOption[]>> {
    return this.http.get<AllergyApiEnvelope<AllergyTypeOption[]>>(
      `${this.base}/api/global/getAllergyTypeMaster`,
    );
  }

  searchAllergies(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<AllergyApiEnvelope<AllergyRow[]>> {
    return this.http.post<AllergyApiEnvelope<AllergyRow[]>>(
      `${this.base}/api/global/getAllergyMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }

  countAllergies(
    organizationId: number,
    unitId: number,
  ): Observable<AllergyApiEnvelope<null>> {
    return this.http.post<AllergyApiEnvelope<null>>(
      `${this.base}/api/global/getAllergyMasterListCount`,
      { organizationId, unitId },
    );
  }

  getAllergyById(
    id: number,
    organizationId: number,
    unitId: number,
  ): Observable<AllergyApiEnvelope<AllergyRow[]>> {
    return this.http.get<AllergyApiEnvelope<AllergyRow[]>>(
      `${this.base}/api/global/getAllergyMasterListById/${id}/${organizationId}/${unitId}`,
    );
  }

  saveAllergy(payload: SaveAllergyPayload): Observable<AllergyApiEnvelope<null>> {
    return this.http.post<AllergyApiEnvelope<null>>(
      `${this.base}/api/global/saveAllergyMaster`,
      payload,
    );
  }

  updateAllergy(payload: SaveAllergyPayload): Observable<AllergyApiEnvelope<null>> {
    return this.http.post<AllergyApiEnvelope<null>>(
      `${this.base}/api/global/updateAllergyMaster`,
      payload,
    );
  }

  updateAllergyStatus(payload: UpdateAllergyStatusPayload): Observable<AllergyApiEnvelope<null>> {
    return this.http.post<AllergyApiEnvelope<null>>(
      `${this.base}/api/global/updateAllergyMasterStatus`,
      payload,
    );
  }
}
