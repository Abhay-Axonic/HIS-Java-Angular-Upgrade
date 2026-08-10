import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  ResourceConfigurationRow,
  ResourceConfigListPayload,
} from './resource-configuration.models';

@Injectable({ providedIn: 'root' })
export class ResourceConfigurationService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** POST /api/otims/getSurgeryResourceConfigList — the list (legacy payload
   * shape: orgId/unitId/offset/noOfRecordsPerPage — NOT organizationId). */
  list(payload: ResourceConfigListPayload): Observable<{ listObject?: ResourceConfigurationRow[] }> {
    return this.http.post<{ listObject?: ResourceConfigurationRow[] }>(
      `${this.base}/api/otims/getSurgeryResourceConfigList`,
      payload
    );
  }

  /** POST /api/otims/getSurgeryResourceConfigListCount — the count. */
  count(payload: ResourceConfigListPayload): Observable<{ listObject?: unknown[] }> {
    return this.http.post<{ listObject?: unknown[] }>(
      `${this.base}/api/otims/getSurgeryResourceConfigListCount`,
      payload
    );
  }
}
