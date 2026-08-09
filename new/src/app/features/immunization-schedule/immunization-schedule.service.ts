import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { VaccinationApiEnvelope, VaccineOption } from './immunization-schedule.models';

/**
 * The immunization-schedule endpoints. Verified by CALLING with the cached
 * token (QF-2026-08-09-E: call, don't grep):
 *   GET /api/billing/getActiveVaccinationMaster/{org}/{unit} -> 200
 *   "success" ([] in dev) — the vaccine catalog (VaccinationMasterController
 *   -> VaccinationMasterService.getActiveVaccinationMaster ->
 *   VaccinationMasterDaoImpl:255 -> named query GET_ACTIVE_Vaccination_Master,
 *   org+unit scoped). This is the ONLY real endpoint on this screen's path.
 *
 * There is NO immunization-schedule backend: no list, no save, no
 * update/status, no disease-list endpoint exists for this screen anywhere
 * (grep across all modules — no ImmunizationSchedule controller/DAO/entity).
 * The VaccinationMasterController's full CRUD serves the OTHER real screen
 * (state vaccinationMAster, app.js:17050). The rebuild wires only the
 * vaccine feed; every missing path is reported honestly in the UI.
 */
@Injectable({ providedIn: 'root' })
export class ImmunizationScheduleService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/billing/getActiveVaccinationMaster/{org}/{unit} — the vaccine
   * catalog for the Vaccine select. */
  activeVaccinationMasters(
    organizationId: number,
    unitId: number,
  ): Observable<VaccinationApiEnvelope<VaccineOption[]>> {
    return this.http.get<VaccinationApiEnvelope<VaccineOption[]>>(
      `${this.base}/api/billing/getActiveVaccinationMaster/${organizationId}/${unitId}`,
    );
  }
}
