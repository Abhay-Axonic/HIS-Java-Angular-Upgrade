import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  AllergyTypeApiEnvelope,
  AllergyTypeRow,
  SaveAllergyTypePayload,
  UpdateAllergyTypeStatusPayload,
} from './allergy-type-master.models';

/**
 * The allergy-type-master endpoints (the legacy allergyTypeMasterController's
 * full CRUD). All verified by CALLING with the cached token:
 *   POST /api/global/getAllergyTypeMasterList            -> 200, "success"
 *   POST /api/global/getAllergyTypeMasterListCount       -> 200, "success"
 *   GET  /api/global/getAllergyTypeMasterListById/{id}/{org}/{unit} -> 200, "success"
 *   POST /api/global/saveAllergyTypeMaster               -> 200, "error" (empty probe)
 *   POST /api/global/updateAllergyTypeMaster             -> 200, "error" (empty probe)
 *   POST /api/global/updateAllergyTypeMasterStatus       -> 200, "error" (empty probe)
 * The write paths' success with real data is UNVERIFIED by the no-seed rule
 * (a real save would write a row into emr.m_allergy_type_master).
 */
@Injectable({ providedIn: 'root' })
export class AllergyTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  searchAllergyTypes(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<AllergyTypeApiEnvelope<AllergyTypeRow[]>> {
    return this.http.post<AllergyTypeApiEnvelope<AllergyTypeRow[]>>(
      `${this.base}/api/global/getAllergyTypeMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }

  countAllergyTypes(
    organizationId: number,
    unitId: number,
  ): Observable<AllergyTypeApiEnvelope<null>> {
    return this.http.post<AllergyTypeApiEnvelope<null>>(
      `${this.base}/api/global/getAllergyTypeMasterListCount`,
      { organizationId, unitId },
    );
  }

  getAllergyTypeById(
    id: number,
    organizationId: number,
    unitId: number,
  ): Observable<AllergyTypeApiEnvelope<AllergyTypeRow[]>> {
    return this.http.get<AllergyTypeApiEnvelope<AllergyTypeRow[]>>(
      `${this.base}/api/global/getAllergyTypeMasterListById/${id}/${organizationId}/${unitId}`,
    );
  }

  saveAllergyType(payload: SaveAllergyTypePayload): Observable<AllergyTypeApiEnvelope<null>> {
    return this.http.post<AllergyTypeApiEnvelope<null>>(
      `${this.base}/api/global/saveAllergyTypeMaster`,
      payload,
    );
  }

  updateAllergyType(payload: SaveAllergyTypePayload): Observable<AllergyTypeApiEnvelope<null>> {
    return this.http.post<AllergyTypeApiEnvelope<null>>(
      `${this.base}/api/global/updateAllergyTypeMaster`,
      payload,
    );
  }

  updateAllergyTypeStatus(payload: UpdateAllergyTypeStatusPayload): Observable<AllergyTypeApiEnvelope<null>> {
    return this.http.post<AllergyTypeApiEnvelope<null>>(
      `${this.base}/api/global/updateAllergyTypeMasterStatus`,
      payload,
    );
  }
}
