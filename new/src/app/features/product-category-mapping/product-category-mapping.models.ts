/**
 * Parity rebuild of
 * views/PharmacyInventory/StockAdjustment/product-category-mapping.html +
 * resources/assets/js/controller/master/unit/productCategroyMappingController.js
 * (legacy state `productRestriction.productCategoryMapping`, url
 * `/productRestriction/productCategoryMapping`, moduleId 633 -- confirmed
 * identically on BOTH the parent `productRestriction` state's resolve block
 * AND this child state's own resolve block, app.js-equivalent read directly
 * from each controller file). The tab-shell parent
 * (`productRestrictionController.js` / `product-restriction.html`) has no
 * logic beyond tab navigation to this screen and a sibling `itemRestriction`
 * tab (not built in this batch) -- confirmed by a full read of both parent
 * files; this screen reads its own cookie data and builds its own store
 * dropdown independently, with no data dependency on the parent.
 *
 * Field names/shapes read directly from the real backend, not guessed:
 * `legacy/Pharmacy/.../dto/productRestriction/ProductRestrictionResponse.java`
 * (base class: id/storeId/storeName/assetCategoryId/assetCategory/
 * productCategoryId/productCategory/status) +
 * `ProductRestrictionPCListResponse.java` (adds assetTypeId/assetType) for
 * the mapping-list rows, `legacy/Global/.../dto/global/Store.java` for the
 * store dropdown (only `id`/`store` are ever populated on this code path --
 * `isMainStore` stays null and is dropped by Jackson's NON_NULL inclusion,
 * see `GlobalService.getUserStoreList`), and
 * `legacy/Entity/.../dto/CommonResponse.java` for the create/deactivate
 * action response envelope (only `message` is actually used, matching
 * legacy's own `response.data.message` growl usage).
 */

/** Store dropdown option, from `GET /api/global/user/stores/{orgId}/{unitId}/{userId}`. */
export interface StoreOption {
  id: number;
  store: string;
}

/**
 * One row per product category in the org for the selected store --
 * backend LEFT JOINs every product category against this store's mapping
 * rows, so a category with no mapping row yet comes back with `id: null,
 * status: false`. NOT an error; confirmed live in the golden trace (Step 2).
 */
export interface ProductCategoryMappingRow {
  id: number | null;
  storeId?: number;
  storeName?: string;
  assetCategoryId: number;
  assetCategory: string;
  productCategoryId: number;
  productCategory: string;
  assetTypeId: number;
  assetType: string;
  status: boolean;
}

/** Minimal shape read off `CommonResponse` for the create/deactivate calls. */
export interface MappingActionResponse {
  status?: string;
  message?: string;
}
