import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from './api-config';
import { ApiResponse } from './api-response';
import { SessionService } from './session.service';

export interface RegistrationFieldConfig {
  mobileDigit: number;
  [key: string]: unknown;
}

/**
 * Rebuild-side equivalent of `$rootScope.getDefaultRegistrationValidationDetails()`
 * (app.js line 2667) -- fetches the same
 * `/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/{orgId}/{unitId}`
 * endpoint and exposes `registrationFieldObj.mobileDigit`, which
 * insuranceMaster's mobile-number validation reads (see
 * docs/PHASE2_PILOT_AUDIT.md section 2). Shared here rather than
 * duplicated per-screen because the legacy app also fetches it once,
 * globally, on `$rootScope`.
 */
@Injectable({ providedIn: 'root' })
export class RegistrationConfigService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);
  private readonly session = inject(SessionService);

  readonly fieldConfig = signal<RegistrationFieldConfig>({ mobileDigit: 10 });

  constructor() {
    const organizationId = this.session.organizationId;
    const unitId = this.session.unitId;
    if (organizationId == null || unitId == null) return;
    this.http
      .get<ApiResponse<never, RegistrationFieldConfig>>(
        `${this.base}/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/${organizationId}/${unitId}`,
      )
      .subscribe({
        next: (res) => {
          if (res.status === 'success' && res.listObject?.length) {
            this.fieldConfig.set(res.listObject[0]);
          }
        },
      });
  }
}
