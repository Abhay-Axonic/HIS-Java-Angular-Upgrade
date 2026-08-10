import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { VaccinationApiEnvelope, VaccinationRow } from './vaccination-master.models';

/**
 * The vaccination-master endpoints. MAPPED in the Billing module's
 * VaccinationMasterController (lis.url.config.js:527-533) and consumed by the
 * orphaned real screen (state vaccinationMAster, app.js:17050). NOTE: this
 * screen's README states "mapped, NOT live-verified" — the live calls were
 * skipped by owner choice on 2026-08-09; none of these were CALLED with the
 * token this screen (unlike screens 6-9, whose feeds were live-verified).
 *
 * The walk's node (482 -> #/vaccinationMaster) reaches the 8427 SHELL, which
 * this service implements: list + count + active feed (the grid) and the ROA
 * dropdown feed (the shell's form has no ROA field — kept in the service for
 * parity with the real screen's contract, documented in the README).
 */
@Injectable({ providedIn: 'root' })
export class VaccinationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** POST /api/billing/getVaccinationMasterList — org+unit scoped grid feed. */
  listMasters(
    organizationId: number,
    unitId: number,
  ): Observable<VaccinationApiEnvelope<VaccinationRow[]>> {
    return this.http.post<VaccinationApiEnvelope<VaccinationRow[]>>(
      `${this.base}/api/billing/getVaccinationMasterList`,
      { organizationId, unitId },
    );
  }

  /** POST /api/billing/getVaccinationMasterCount — the pager count. */
  countMasters(
    organizationId: number,
    unitId: number,
  ): Observable<VaccinationApiEnvelope<unknown[]>> {
    return this.http.post<VaccinationApiEnvelope<unknown[]>>(
      `${this.base}/api/billing/getVaccinationMasterCount`,
      { organizationId, unitId },
    );
  }

  /** GET /api/billing/getRouteOfAdministrationList/{org}/{unit} — ROA feed. */
  routeOfAdministrations(
    organizationId: number,
    unitId: number,
  ): Observable<VaccinationApiEnvelope<{ administrationId: number; description: string }[]>> {
    return this.http.get<
      VaccinationApiEnvelope<{ administrationId: number; description: string }[]>
    >(`${this.base}/api/billing/getRouteOfAdministrationList/${organizationId}/${unitId}`);
  }
}
