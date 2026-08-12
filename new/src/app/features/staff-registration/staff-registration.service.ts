import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { RelationRow } from './staff-registration.models';

/**
 * Screen 650 feeds. Legacy state staffRegistration (app.js:14382-14386,
 * staffRegistrationController) is a registration form; the only live feed on
 * its path is the relation master list: GETACTIVERELATIONLIST =
 * "/adt/getActiveRelationList" (adt.url.config.js:343). Confirmed live
 * 2026-08-12: SUCCESS with 2 real rows — REAL FEED (the registration form
 * itself is write-path, deferred to the transaction methodology per
 * DQ-2026-08-12-034).
 */
@Injectable({ providedIn: 'root' })
export class StaffRegistrationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // GET /adt/getActiveRelationList — relation master feed (no params).
  getRelations(): Observable<ApiResponse<never, RelationRow>> {
    return this.http.get<ApiResponse<never, RelationRow>>(
      `${this.base}/adt/getActiveRelationList`,
    );
  }
}
