import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ServiceMasterRow } from './application-configuration.models';

/**
 * Screen 660 — service-master dropdown feed. Endpoint from
 * applicationConfigurationController.js (getPharmacyServiceList, ctrl:953-966):
 * GET /billing/getListOfServiceMaster/{orgId}/{unitId}. The legacy state is
 * commented out (app.js:15352-15357, MasterController shell). Confirmed live
 * 2026-08-12: SUCCESS with 1 real row {serviceMasterId, serviceStandardName,
 * ...} — REAL FEED (the screen is a config FORM fed by this dropdown,
 * deferred to the transaction methodology per DQ-2026-08-12-034).
 */
@Injectable({ providedIn: 'root' })
export class ApplicationConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // GET /billing/getListOfServiceMaster/{organizationId}/{unitId} — service
  // master feed for the config form's dropdown.
  getServiceMasters(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, ServiceMasterRow>> {
    return this.http.get<ApiResponse<never, ServiceMasterRow>>(
      `${this.base}/billing/getListOfServiceMaster/${organizationId}/${unitId}`,
    );
  }
}
