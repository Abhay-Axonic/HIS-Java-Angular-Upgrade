/**
 * Row shape returned by `GET /api/globals/generics/{orgId}`
 * (`GenericController.getAll` -> `GenericListResponse.java`). This is a raw
 * JSON array body -- unlike `createTabularTemplates`/most other rebuilt
 * screens, `genericMaster`'s GET endpoints are NOT wrapped in the
 * `{status, listObject}` `ApiResponse` envelope (confirmed by reading
 * `GenericController.java`: `getAll`/`getById`/`getCombinationDetailsById`
 * all return a bare `ResponseEntity<List<...>>`/`ResponseEntity<...>`, while
 * only `create`/`update` return `CommonResponse`, which DOES match the
 * envelope shape). Both response shapes are modeled below accordingly.
 */
export interface GenericMasterRow {
  id: number;
  assetType: string;
  assetCategory: string;
  productCategory: string;
  code: string;
  name: string;
  status: boolean;
}

export interface AssetTypeOption {
  id: number;
  type: string;
}

export interface AssetCategoryOption {
  id: number;
  category: string;
}

export interface ProductCategoryOption {
  id: number;
  category: string;
}

export interface StrengthUnitOption {
  id: number;
  unit: string;
}

/** Raw shape returned by `GET /api/globals/generics/{orgId}/{id}` (GenericDetailsResponse.java). */
export interface GenericDetails {
  id: number;
  assetTypeId: number;
  assetCategoryId: number;
  productCategoryId: number;
  code: string;
  name: string;
  status: boolean;
  items: GenericComboItemDto[];
}

/** Raw shape returned by `GET /api/globals/generics/getCombinationDetail/{id}`
 * (a `List<GenericRequestItem>`), and also the per-row shape sent back on
 * save/update (`GenericRequestItem.java`). */
export interface GenericComboItemDto {
  id: number | null;
  genericId?: number;
  genericName?: string;
  subGenericId: number | null;
  strengthUnit: number;
  /** Legacy's own naming: this is actually the strength-unit-of-measure ID
   * (`strengthUnitList` option id), not a free-text strength value --
   * preserved verbatim, it's the real backend field name (`GenericRequestItem.strength`). */
  strength: number | null;
}

/** Option shown in the combination sub-form's "Generic Name" dropdown --
 * legacy's `genericMasterList` (ctrl:20-22,612-618): a placeholder
 * ("Select Generic") followed by every ACTIVE generic from the main list.
 *
 * Deliberate, disclosed deviation from legacy (see
 * docs/ANGULAR_PENDING_FINDINGS.md, drafted entry for this screen): legacy's
 * `GetGenericMaster()` (ctrl:612-618) appends to `genericMasterList` every
 * time the main list reloads (after every save/update) WITHOUT ever
 * clearing it first -- so the real legacy dropdown accumulates duplicate
 * entries, once per active generic, on every single save/update during a
 * session. This rebuild instead derives the option list reactively
 * (`computed()` over the always-current row list), which never
 * accumulates duplicates. Matches the precedent already set by this
 * codebase's insurance-master ledger-backfill deviation (entry 4): the
 * self-correcting version is kept, not silently "fixed" without
 * disclosure, and not deliberately re-broken to match a bug nobody would
 * want kept on purpose.
 */
export interface GenericComboOption {
  id: number;
  name: string;
}

/** One row of the "Is Combination" sub-form (ctrl: `genericIsCombinationList`). */
export interface ComboRow {
  id: number | null;
  subGenericId: number | null;
  name: GenericComboOption | null;
  strengthUnit: number;
  strengthUnitName: StrengthUnitOption | null;
}

export interface GenericFormState {
  id: number | null;
  assetTypeId: number | null;
  assetCategoryId: number | null;
  productCategoryId: number | null;
  code: string;
  name: string;
  isCombination: boolean;
}

export function emptyGenericForm(): GenericFormState {
  return {
    id: null,
    assetTypeId: null,
    assetCategoryId: null,
    productCategoryId: null,
    code: '',
    name: '',
    isCombination: false,
  };
}
