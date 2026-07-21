import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  PrefixDetail,
  PrefixDropdownResponse,
  PrefixRow,
  PrefixSavePayload,
  PrefixSaveResponse,
  PrefixUpdatePayload,
} from './prefix-configuration.models';

/**
 * Endpoint paths taken directly from `CONSTANTS.MASTER.UNIT.PREFIX_CONFIGURATION`
 * in resources/assets/js/constants/constants.js:273-279 (built from
 * `unitsApi = rootUrl + '/api/units'`, and `rootUrl` is `''` per
 * resources/assets/js/config/url.config.js:13/19 -- BASE_URL/ROOT_URL are
 * also both `''` there) -- not guessed:
 *   DROPDOWN_API: /api/units/configuration/prefixconfiguration/dropdowns
 *   DETAIL_API:   /api/units/configuration/prefixconfiguration/  (+ id)
 *   SAVE_API:     /api/units/configuration/prefixconfiguration/save
 *   UPDATE_API:   /api/units/configuration/prefixconfiguration/  (+ id)
 *
 * The search-list endpoint is NOT one of the CONSTANTS.MASTER.UNIT.
 * PREFIX_CONFIGURATION.* keys -- the legacy controller builds it manually
 * (ctrl:99) as `BASE_URL + ROOT_URL + "/api/units/configuration/
 * prefixconfiguration/orgUnitWiseList/" + unitId + "/" + organizationId +
 * "/" + flag`, reproduced here as a literal path for the same reason.
 */
@Injectable({ providedIn: 'root' })
export class PrefixConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdowns(): Observable<PrefixDropdownResponse> {
    return this.http.get<PrefixDropdownResponse>(
      `${this.base}/api/units/configuration/prefixconfiguration/dropdowns`,
    );
  }

  search(unitId: number, organizationId: number, unitLevel: boolean): Observable<PrefixRow[]> {
    return this.http.get<PrefixRow[]>(
      `${this.base}/api/units/configuration/prefixconfiguration/orgUnitWiseList/${unitId}/${organizationId}/${unitLevel}`,
    );
  }

  getById(id: number): Observable<PrefixDetail> {
    return this.http.get<PrefixDetail>(`${this.base}/api/units/configuration/prefixconfiguration/${id}`);
  }

  save(payload: PrefixSavePayload): Observable<PrefixSaveResponse> {
    return this.http.post<PrefixSaveResponse>(
      `${this.base}/api/units/configuration/prefixconfiguration/save`,
      payload,
    );
  }

  update(id: number, payload: PrefixUpdatePayload): Observable<PrefixSaveResponse> {
    return this.http.patch<PrefixSaveResponse>(
      `${this.base}/api/units/configuration/prefixconfiguration/${id}`,
      payload,
    );
  }
}
