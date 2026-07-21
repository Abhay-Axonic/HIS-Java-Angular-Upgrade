import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AssetCategoryOption,
  AssetTypeOption,
  GenericComboItemDto,
  GenericDetails,
  GenericMasterRow,
  ProductCategoryOption,
  StrengthUnitOption,
} from './generic-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/pharmacy/genericMasterController.js
 * and resources/assets/js/constants/constants.js (not guessed) -- every one
 * confirmed to resolve to a real, implemented Java controller method during
 * this screen's Step 0 backend-existence check:
 *  - GENERIC_MASTER_API                    -> /api/globals/generics/         (GenericController)
 *  - GENERIC_MASTER_IS_COMBINATION_DETAILS -> /api/globals/generics/getCombinationDetail/{id}
 *  - ASSET_TYPE_API                        -> /api/global/assets/types/{unitId}/{orgId}       (GlobalController)
 *  - ASSET_CATEGORY_API                    -> /api/global/assets/categories/{assetTypeId}/{unitId}/{orgId}
 *  - PRODUCT_CATEGORY_API                  -> /api/global/products/categories/{assetCategoryId}/{orgId}
 *  - MANUFATURER_PREFIX_API                -> /api/globals/manufacturers/prefix/{orgId}/178   (ManufacturerController; "178" is a hardcoded screenId in legacy itself)
 *  - ITEM_MASTER.UNIT_API                  -> /api/globals/strength/units/{orgId}              (StrengthUnitController)
 *
 * NOTE the response-envelope split (see generic-master.models.ts's header
 * comment): getAll/getById/getCombinationDetail/dropdowns all return RAW
 * JSON (array or plain object), never `{status, listObject}` -- only
 * create/update go through the `CommonResponse`/`ApiResponse` envelope.
 * Reading `res.status === 'success'` against one of the raw endpoints would
 * silently always be `undefined` -- confirmed against GenericController.java
 * directly before writing this, not assumed from the scaffold's default shape.
 */
@Injectable({ providedIn: 'root' })
export class GenericMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAll(organizationId: number): Observable<GenericMasterRow[]> {
    return this.http.get<GenericMasterRow[]>(`${this.base}/api/globals/generics/${organizationId}`);
  }

  getById(organizationId: number, genericId: number): Observable<GenericDetails> {
    return this.http.get<GenericDetails>(`${this.base}/api/globals/generics/${organizationId}/${genericId}`);
  }

  getCombinationDetail(genericId: number): Observable<GenericComboItemDto[]> {
    return this.http.get<GenericComboItemDto[]>(`${this.base}/api/globals/generics/getCombinationDetail/${genericId}`);
  }

  create(payload: Record<string, unknown>): Observable<ApiResponse & { id?: number }> {
    return this.http.post<ApiResponse & { id?: number }>(`${this.base}/api/globals/generics/`, payload);
  }

  /** Also used for the status-toggle-only PATCH (ctrl's `ToggleStatus`) --
   * confirmed against `GenericService.update()` (Java) that this is a
   * partial/merge update (each field only applied `if != null`), so a
   * status-only payload is safe and cannot null out the other fields --
   * not the same risk class as entry 8's `implantMaster` speciality-array bug. */
  update(genericId: number, payload: Record<string, unknown>): Observable<ApiResponse & { id?: number }> {
    return this.http.patch<ApiResponse & { id?: number }>(`${this.base}/api/globals/generics/${genericId}`, payload);
  }

  getAssetTypes(unitId: number, organizationId: number): Observable<AssetTypeOption[]> {
    return this.http.get<AssetTypeOption[]>(`${this.base}/api/global/assets/types/${unitId}/${organizationId}`);
  }

  getAssetCategories(assetTypeId: number, unitId: number, organizationId: number): Observable<AssetCategoryOption[]> {
    return this.http.get<AssetCategoryOption[]>(
      `${this.base}/api/global/assets/categories/${assetTypeId}/${unitId}/${organizationId}`,
    );
  }

  getProductCategories(assetCategoryId: number, organizationId: number): Observable<ProductCategoryOption[]> {
    return this.http.get<ProductCategoryOption[]>(
      `${this.base}/api/global/products/categories/${assetCategoryId}/${organizationId}`,
    );
  }

  getStrengthUnits(organizationId: number): Observable<StrengthUnitOption[]> {
    return this.http.get<StrengthUnitOption[]>(`${this.base}/api/globals/strength/units/${organizationId}`);
  }

  getPrefix(organizationId: number): Observable<{ code: string }> {
    return this.http.get<{ code: string }>(`${this.base}/api/globals/manufacturers/prefix/${organizationId}/178`);
  }
}
