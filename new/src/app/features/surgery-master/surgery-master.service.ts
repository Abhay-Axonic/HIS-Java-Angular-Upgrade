import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  SaveSurgeryPayload,
  SurgeryApiEnvelope,
  SurgeryGradeOption,
  SurgeryRow,
  SpecialityOption,
  UpdateSurgeryPayload,
  UpdateSurgeryStatusPayload,
} from './surgery-master.models';

/**
 * The surgery-master endpoints (the legacy surgeryMasterController's full
 * CRUD). All verified by CALLING with the cached token (2026-08-09):
 *   POST /api/global/getSurgeryMasterList      -> 200 "success" ([] dev)
 *   POST /api/global/getSurgeryMasterById      -> 200 "success" ([] dev)
 *   POST /api/global/getSurgeryGradeMasterList -> 200 "success" (3 rows)
 *   POST /adt/getActiveSpecialityList          -> 200 "success" (2 rows)
 * The write endpoints are mapped (SurgeryMasterController) but their success
 * with real data is UNVERIFIED by the no-seed rule (a real save would write
 * a row into public.m_surgery_master).
 *
 * NOTE the fragment class: the legacy constants live in opd.url.config.js
 * (API + GLOBAL + "/getSurgeryMasterList") while GETACTIVESPECIALITY lives
 * in adt.url.config.js WITHOUT the /api prefix ("/adt/getActiveSpecialityList")
 * and is served by the Global module's SpecialityMasterController (@RequestMapping("/adt")).
 */
@Injectable({ providedIn: 'root' })
export class SurgeryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  listSurgeryMasters(
    organizationId: number,
    unitId: number,
  ): Observable<SurgeryApiEnvelope<SurgeryRow[]>> {
    return this.http.post<SurgeryApiEnvelope<SurgeryRow[]>>(
      `${this.base}/api/global/getSurgeryMasterList`,
      { unitId, organizationId },
    );
  }

  getSurgeryMasterById(
    surgeryId: number,
    organizationId: number,
    unitId: number,
  ): Observable<SurgeryApiEnvelope<SurgeryRow[]>> {
    return this.http.post<SurgeryApiEnvelope<SurgeryRow[]>>(
      `${this.base}/api/global/getSurgeryMasterById`,
      { surgery_id: surgeryId, unitId, organizationId },
    );
  }

  saveSurgeryMaster(payload: SaveSurgeryPayload): Observable<SurgeryApiEnvelope<null>> {
    return this.http.post<SurgeryApiEnvelope<null>>(
      `${this.base}/api/global/saveSurgeryMaster`,
      payload,
    );
  }

  updateSurgeryMaster(payload: UpdateSurgeryPayload): Observable<SurgeryApiEnvelope<null>> {
    return this.http.post<SurgeryApiEnvelope<null>>(
      `${this.base}/api/global/updateSurgeryMaster`,
      payload,
    );
  }

  updateSurgeryMasterStatus(payload: UpdateSurgeryStatusPayload): Observable<SurgeryApiEnvelope<null>> {
    return this.http.post<SurgeryApiEnvelope<null>>(
      `${this.base}/api/global/updateStatusForSurgeryMaster`,
      payload,
    );
  }

  /** The legacy payload key is the typo'd `orgnisationId` — the DAO reads
   * exactly that (SurgeryGradeMasterDto). */
  listSurgeryGrades(organizationId: number): Observable<SurgeryApiEnvelope<SurgeryGradeOption[]>> {
    return this.http.post<SurgeryApiEnvelope<SurgeryGradeOption[]>>(
      `${this.base}/api/global/getSurgeryGradeMasterList`,
      { orgnisationId: organizationId },
    );
  }

  listActiveSpecialities(
    organizationId: number,
    unitId: number,
  ): Observable<SurgeryApiEnvelope<SpecialityOption[]>> {
    return this.http.post<SurgeryApiEnvelope<SpecialityOption[]>>(
      `${this.base}/adt/getActiveSpecialityList`,
      { unitId, organizationId },
    );
  }
}
