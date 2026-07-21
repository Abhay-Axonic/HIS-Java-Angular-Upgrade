import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { FixedAssetTypeDetail, FixedAssetTypeRow, FixedAssetTypeSavePayload } from './fixed-asset-type.models';

/**
 * Endpoint path taken directly from
 * resources/assets/js/controller/master/unit/fixedAssetTypeController.js
 * and CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_TYPE_MASTER_API
 * (constants.js:213, `unitApi + '/fixed/asset/type/'` = `/api/unit/fixed/asset/type/`)
 * -- not guessed. A single base path is reused for list/get/create/update/
 * toggle, exactly as legacy's GenericService calls do (list has no id
 * suffix, everything else appends `{id}`).
 *
 * NOTE: `FIXED_ASSET_TYPE_DU_API` (constants.js:218, a differently-named
 * but byte-identical constant) backs `PopulateFixedAssetType`, which is
 * confirmed dead code never wired to the view -- not ported, see
 * fixed-asset-type.ts's class doc comment.
 */
@Injectable({ providedIn: 'root' })
export class FixedAssetTypeService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);
  private readonly url = `${this.base}/api/unit/fixed/asset/type/`;

  getFixedAssetTypes(): Observable<FixedAssetTypeRow[]> {
    return this.http.get<FixedAssetTypeRow[]>(this.url);
  }

  getFixedAssetTypeForEdit(id: number): Observable<FixedAssetTypeDetail> {
    return this.http.get<FixedAssetTypeDetail>(`${this.url}${id}`);
  }

  create(payload: FixedAssetTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(this.url, payload);
  }

  update(id: number, payload: FixedAssetTypeSavePayload): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.url}${id}`, payload);
  }

  toggleStatus(id: number, status: boolean, unitId: number, organizationId: number, userId: number): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.url}${id}`, { status, unitId, organizationId, userId });
  }
}
