/**
 * Screen 635 — storewise generic mapper feed row. The endpoint
 * GET /api/unit/storewise/generic/mapper/{unitId} (STORE_WISE_GENERIC_API,
 * constants.js:244) returns a RAW ARRAY — no envelope, no status key
 * (live-verified 2026-08-12, HTTP 200). The exact column set was NOT captured
 * in the live call; the fields below are the mapper-domain columns the legacy
 * screen maps (generic + store + category columns), all optional, with an index
 * signature so unverified columns do not break reading. REAL FEED (raw-array
 * class, DQ-2026-08-12-034).
 */
export interface StorewiseGenericMapperRow {
  id?: number;
  unitId?: number;
  storeId?: number;
  storeName?: string;
  genericId?: number;
  genericCode?: string;
  genericName?: string;
  vedId?: number;
  assetCategoryId?: number;
  productCategoryId?: number;
  status?: string;
  [key: string]: unknown;
}
