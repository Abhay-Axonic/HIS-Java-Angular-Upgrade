import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { MappingActionResponse, ProductCategoryMappingRow, StoreOption } from './product-category-mapping.models';

/**
 * Endpoint paths + HTTP methods confirmed live via
 * `tools/golden-trace/capture.js` (Step 2) against the real backend
 * controllers: `com.param.pharmacy.master.unit.controller.ProductRestrictionController`
 * (`@RequestMapping("/api/unit/product/restrictions")`) for the mapping
 * list/create/deactivate calls, and `com.param.global.controller.GlobalController`
 * (`@RequestMapping("/api/global")`) for the store dropdown -- the SAME
 * `/api/global/user/stores/{orgId}/{unitId}/{userId}` endpoint already used
 * by `ward-store-mapper.service.ts`/`department-store-mapper.service.ts`.
 */
@Injectable({ providedIn: 'root' })
export class ProductCategoryMappingService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ---- store dropdown (mirrors PopulateStore, ctrl:62-81) ----
  getStores(organizationId: number, unitId: number, userId: number): Observable<StoreOption[]> {
    return this.http.get<StoreOption[]>(`${this.base}/api/global/user/stores/${organizationId}/${unitId}/${userId}`);
  }

  // ---- mapping list for the selected store (mirrors GetCategoryMappingList, ctrl:83-104) ----
  getCategoryMappingList(organizationId: number, storeId: number): Observable<ProductCategoryMappingRow[]> {
    return this.http.get<ProductCategoryMappingRow[]>(
      `${this.base}/api/unit/product/restrictions/product/category/mapper/${organizationId}/${storeId}`,
    );
  }

  // ---- toggle ON, no existing row yet: create a mapping (mirrors
  // CreateProductCategoryMapping, ctrl:116-139) ----
  // Backend `ProductRestrictionController.Create` always INSERTs a new row
  // -- it never checks for/reuses an existing one for the same
  // store+category. Only called when the row's id is genuinely null (no
  // mapping has ever existed) -- see the component's class doc "Defect B"
  // section. Calling this when a row already exists (even inactive)
  // produces a real, confirmed duplicate row -- use updateStatus() instead.
  create(payload: { storeId: number; productCategoryId: number; status: true }): Observable<MappingActionResponse> {
    return this.http.post<MappingActionResponse>(`${this.base}/api/unit/product/restrictions/pcMapper/`, payload);
  }

  // ---- toggle ON/OFF on a known existing row: reactivate or deactivate a
  // mapping (mirrors UpdateProductCategoryMapping, ctrl:141-160, generalized
  // -- see class doc "Defect B"). Confirmed via
  // `ProductRestrictionDao.updateProductRestrictionMapper`
  // (`UPDATE ... SET isActive=:status WHERE id=:id`) that this endpoint is
  // a plain generic status setter, not a false-only "deactivate" -- legacy's
  // frontend just never called it with `true`. Empty PATCH body, matching
  // legacy's `GenericService.serviceAction("PATCH", URI, {})` call exactly.
  updateStatus(id: number, status: boolean): Observable<MappingActionResponse> {
    return this.http.patch<MappingActionResponse>(
      `${this.base}/api/unit/product/restrictions/product/category/mapper/${id}/${status}`,
      {},
    );
  }
}
