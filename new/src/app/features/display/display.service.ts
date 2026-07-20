import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { QmsActiveDept, QmsTokenDisplayItem, QmsTokenMessage, UnitBillingAppConfig } from './display.models';

/**
 * Endpoint paths and payload shapes taken directly from
 * QMSTokenDisplayMasterController.js (~lines 53-100, 123-256). This screen
 * uses `GenericServiceForWithoutLogin`, not the usual session-authenticated
 * `GenericService` -- it's an unauthenticated kiosk display, confirmed by
 * the route's resolve block having no session/module check at all (see
 * app.js:20149). `authInterceptor` still attaches its headers harmlessly;
 * the backend simply doesn't require them here, matching legacy exactly.
 *
 * organisationId/unitId/qmsKioskMasterId are sent as the raw route-param
 * strings, not parsed to numbers -- legacy passes $stateParams values
 * through unparsed, and matching that exactly (rather than coercing to
 * number) keeps the outgoing request body byte-for-byte identical.
 */
@Injectable({ providedIn: 'root' })
export class DisplayService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getBillingAppConfig(orgId: string, unitId: string): Observable<ApiResponse<UnitBillingAppConfig>> {
    return this.http.get<ApiResponse<UnitBillingAppConfig>>(
      `${this.base}/api/unit/applicationConfiguration/billing/orgId/${orgId}/unitId/${unitId}`,
    );
  }

  getScreenImage(orgId: string): Observable<ApiResponse<{ imageURL: string | null }>> {
    return this.http.get<ApiResponse<{ imageURL: string | null }>>(
      `${this.base}/api/unit/applicationConfiguration/screen-images/${orgId}/3`,
    );
  }

  getActiveDeptListByKioskId(
    organisationId: string,
    unitId: string,
    qmsKioskMasterId: string,
  ): Observable<ApiResponse<never, QmsActiveDept>> {
    return this.http.post<ApiResponse<never, QmsActiveDept>>(
      `${this.base}/api/pharmacy/getActiveDeptListByKioskId`,
      { organisationId, unitId, qmsKioskMasterId },
    );
  }

  getTokenListForDisplay(
    organisationId: string,
    unitId: string,
    qmsKioskMasterId: string,
  ): Observable<ApiResponse<never, QmsTokenDisplayItem>> {
    return this.http.post<ApiResponse<never, QmsTokenDisplayItem>>(
      `${this.base}/api/pharmacy/getTokenListForDisplay`,
      { organisationId, unitId, qmsKioskMasterId },
    );
  }

  getTokenDisplayMessage(
    organisationId: string,
    unitId: string,
    qmsKioskMasterId: string,
  ): Observable<ApiResponse<never, QmsTokenMessage>> {
    return this.http.post<ApiResponse<never, QmsTokenMessage>>(
      `${this.base}/api/pharmacy/getTokenDisplayMessage`,
      { organisationId, unitId, qmsKioskMasterId },
    );
  }
}
