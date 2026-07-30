import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setTotalPages,
  setPage,
  setFirstPage,
  setPreviousPage,
  setNextPage,
  setLastPage,
  getPagesList,
  sortBy as sortPagination,
} from '../../core/client-pagination.util';
import { ProductCategoryMappingService } from './product-category-mapping.service';
import { ProductCategoryMappingRow, StoreOption } from './product-category-mapping.models';

type SortField = 'assetType' | 'assetCategory' | 'productCategory';

/**
 * Parity rebuild of
 * views/PharmacyInventory/StockAdjustment/product-category-mapping.html +
 * resources/assets/js/controller/master/unit/productCategroyMappingController.js
 * (legacy state `productRestriction.productCategoryMapping`, moduleId 633,
 * confirmed identically on both the parent `productRestriction` state's own
 * resolve block AND this child state's own resolve block). Phase 4 batch 32.
 *
 * The tab-shell parent (`productRestrictionController.js` /
 * `product-restriction.html`) has no logic of its own beyond switching
 * between this tab and a sibling `itemRestriction` tab (not built in this
 * batch) -- confirmed by a full read of both parent files. This screen
 * reads its own cookie data and builds its own store dropdown
 * independently; there is no data dependency on the parent, so it is
 * rebuilt here as a standalone routed screen rather than a real tab-shell
 * component.
 *
 * ============================================================================
 * Golden trace (Steps 0-2, already verified live -- reproduced exactly):
 * ============================================================================
 * - Store dropdown: `GET /api/global/user/stores/{orgId}/{unitId}/{userId}`.
 * - On store selection: `GET /api/unit/product/restrictions/product/category/mapper/{orgId}/{storeId}`
 *   -- one entry per product category in the org; a category with no
 *   mapping row yet comes back `id: null, status: false` (LEFT JOIN
 *   coalesce, not an error).
 * - Toggle ON: `POST /api/unit/product/restrictions/pcMapper/`
 *   `{storeId, productCategoryId, status: true}` -> 201.
 * - Toggle OFF: `PATCH /api/unit/product/restrictions/product/category/mapper/{id}/false`,
 *   empty body -> 200, requires a real `id` (see Defect B below).
 *
 * ============================================================================
 * DEFECT B -- FIXED with disclosure (silent bug, self-contained fix)
 * ============================================================================
 * Legacy's `ToggleStatus` (ctrl:105-114) always calls
 * `CreateProductCategoryMapping` (a plain `POST`, always INSERTs) on toggle
 * ON, regardless of whether a mapping row already exists (just inactive)
 * for this store+category -- it never checks the row's own `id` before
 * deciding create-vs-reactivate. Two real, live-confirmed bugs share this
 * root cause: (1) toggling a row back ON without an intervening list
 * refresh always inserts a genuinely NEW, duplicate row, so the same
 * product category renders twice with conflicting statuses once the list
 * is next fetched -- confirmed live via a controlled create/refresh/
 * deactivate/re-activate sequence, cross-checked against the DB at every
 * step; (2) relatedly, the success handler for create (ctrl:128-138) never
 * refreshes the row/id it just created, so toggling OFF immediately after
 * toggling ON (same page load, no refresh) sends `PATCH .../null/false`
 * and fails with a real HTTP 405 -- the checkbox silently snaps back and
 * the user's attempt is discarded.
 *
 * FIX (this rebuild only -- no backend change; the backend's generic
 * `PATCH .../{id}/{status}` endpoint, confirmed via
 * `ProductRestrictionDao.updateProductRestrictionMapper`
 * (`UPDATE ... SET isActive=:status WHERE id=:id`), already supports
 * reactivating an existing row with `status=true` just as well as
 * deactivating with `false` -- legacy's frontend simply never called it
 * that way): `toggleStatus()` below always applies an optimistic local
 * flip first (matching legacy's own two-way-bound-checkbox behavior), then
 * routes to `createMapping()` (POST) ONLY when the row's `id` is genuinely
 * null -- otherwise it calls `updateMappingStatus()` (the same generic
 * PATCH used for deactivation) with the new status, reusing the existing
 * row instead of duplicating it. The very first toggle on a category with
 * no mapping yet still refreshes the list after create, so the row's real
 * id is populated locally before any subsequent toggle.
 *
 * ============================================================================
 * DEFECT A -- NOT fixed, faithfully reproduced, MANDATORY visible caveat
 * ============================================================================
 * Deactivating a mapping here only flips a flag in the backend -- it does
 * NOT exclude that product category from 5 real downstream consumers
 * (barcode printing, non-billable consumption, shelf allocation, item
 * transfers, stop-transaction item search). This is a known, owner-
 * approved, deliberately-NOT-fixed backend gap (a separate future
 * initiative) -- out of scope for this screen's rebuild, and NOT
 * compensated for client-side. Per the explicit owner decision for this
 * screen, this is treated as "faithfully reproduce" (not "silent bug
 * corrected with disclosure") specifically because fixing the actual gap
 * is out of scope, not because it isn't real. The one MANDATORY addition
 * is the `.disclosure-banner` in the template -- always rendered above the
 * table (not hover-only, not collapsed by default), see the template for
 * the exact wording used.
 *
 * ============================================================================
 * Other audited behavior, reproduced faithfully (not bugs to fix):
 * ============================================================================
 * - Store change while the list is non-empty prompts a confirm modal
 *   (`ChangeFromStore`/`ConfirmStorePopup`, ctrl:174-194) -- the dropdown
 *   visually reverts to the previous store immediately while the modal is
 *   open, applying the new store only on "Ok". Reproduced via
 *   `onStoreChange()`/`confirmStoreChange()`/`closeStoreChangeModal()`
 *   below, same shape as `asset-category-master.ts`'s own
 *   `showStoreChangeModal` precedent.
 * - Selecting the "Select Store" placeholder itself still fires a fetch
 *   with `storeId=0` (`if (store.id == undefined) store.id = 0`, ctrl:84-86)
 *   -- reproduced via a `{ id: 0, store: 'Select Store' }` sentinel option
 *   rather than a null/undefined selection, so the same `storeId=0`
 *   request fires. A visible, faithfully-reproduced quirk, not corrected.
 * - Column header sort carets (`pagination.sortBy(pagination, field,
 *   sortReverse = !sortReverse)`, view:63-93) share ONE `sortReverse` flag
 *   across all 3 sortable columns in legacy (not a per-column flag) --
 *   reproduced via a single shared `sortReverse` signal in `sortBy()`
 *   below, not three independent ones.
 * - The "Status(n)" column header text (view:94-96) is literal legacy copy
 *   (looks like an unfinished `{{n}}` interpolation, but is genuinely what
 *   ships) -- kept verbatim, same precedent as `ward-store-mapper.html`'s
 *   identical literal text.
 * - The legacy template's `ng-repeat="... | filter : search"` (view:104)
 *   references a bare `search` expression that is never bound to anything
 *   in this `controllerAs: 'vm'` screen (the real search state lives at
 *   `vm.model.pagination.search`) -- AngularJS's `filter` with an
 *   `undefined` expression is a documented no-op, so this is dead,
 *   provably inert markup, not a second real filter layer to reproduce
 *   (unlike the genuine double-filter quirks preserved elsewhere in this
 *   codebase, e.g. `unit-sub-speciality-mapping.ts`'s `displayedRows`).
 *   Confirmed by reading the controller in full: no `$scope.search` or
 *   `vm.search` is ever assigned. Not modeled here.
 * - `GetCategoryMappingList` only calls `PaginationService.searchData` when
 *   the response is non-empty (ctrl:95-97) -- an org with zero product
 *   categories would leave the previous store's stale pagination state on
 *   screen. This rebuild always re-derives pagination state from the fresh
 *   (possibly empty) list instead, a minor simplification (every org in
 *   this system has product categories; this branch is effectively
 *   unreachable) rather than one of the 2 disclosed defects above.
 * - Toggling status does NOT reset pagination back to page 1 (unlike this
 *   codebase's `ward-store-mapper.ts`, which re-derives its whole
 *   `PaginationState` via `searchData` on every toggle and incurs that
 *   side effect) -- legacy's own `ToggleStatus` never calls
 *   `PaginationService` at all, it just mutates the bound row in place, so
 *   the user stays on the same page. `patchRow()` below reproduces that
 *   exactly by patching `fullList`/`filteredData`/`data` directly instead
 *   of calling `searchData`/`setTotalPages`.
 */
@Component({
  selector: 'app-product-category-mapping',
  imports: [FormsModule, StatusToggle],
  templateUrl: './product-category-mapping.html',
  styleUrl: './product-category-mapping.scss',
})
export class ProductCategoryMapping {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(ProductCategoryMappingService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- store dropdown (mirrors self.model.Stores/self.model.store) ----
  private readonly SELECT_STORE: StoreOption = { id: 0, store: 'Select Store' };
  stores = signal<StoreOption[]>([this.SELECT_STORE]);
  selectedStore = signal<StoreOption>(this.SELECT_STORE);
  private oldStore: StoreOption = this.SELECT_STORE;
  private pendingStore: StoreOption | null = null;
  showStoreChangeModal = signal(false);

  // ---- mapping list + client-side pagination (mirrors PaginationService usage) ----
  private fullList: ProductCategoryMappingRow[] = [];
  pagination = signal<PaginationState<ProductCategoryMappingRow>>(createPaginationState<ProductCategoryMappingRow>());
  // Shared across all 3 sortable columns, matching legacy's single
  // `sortReverse` scope var -- see class doc.
  private sortReverse = false;

  constructor() {
    this.loadStores();
  }

  // ---- init (mirrors PopulateStore, ctrl:62-81) ----
  private loadStores(): void {
    this.loader.start();
    this.api.getStores(this.organizationId, this.unitId, this.userId).subscribe({
      next: (list) => {
        this.loader.stop();
        this.stores.set([this.SELECT_STORE, ...(list ?? [])]);
      },
      error: () => {
        this.loader.stop();
        // legacy: empty error handler, silently swallowed (PopulateStore's errorCallback body is empty)
      },
    });
  }

  // ---- store dropdown change (mirrors SetOldValueStore/ChangeFromStore, ctrl:174-187) ----
  onStoreFocus(): void {
    this.oldStore = this.selectedStore();
  }

  onStoreChange(newStore: StoreOption): void {
    if (this.fullList.length > 0) {
      // Legacy reverts the visible dropdown to the previous store
      // immediately and asks for confirmation before actually switching --
      // see class doc.
      this.selectedStore.set(this.oldStore);
      this.pendingStore = newStore;
      this.showStoreChangeModal.set(true);
      return;
    }
    this.applyStoreChange(newStore);
  }

  // ---- store-change confirm modal (mirrors ConfirmStorePopup, ctrl:189-194) ----
  confirmStoreChange(): void {
    if (this.pendingStore) this.applyStoreChange(this.pendingStore);
    this.pendingStore = null;
    this.showStoreChangeModal.set(false);
  }

  closeStoreChangeModal(): void {
    this.pendingStore = null;
    this.showStoreChangeModal.set(false);
  }

  private applyStoreChange(store: StoreOption): void {
    this.selectedStore.set(store);
    this.loadCategoryMappingList(store.id);
  }

  // ---- mapping list (mirrors GetCategoryMappingList, ctrl:83-104) ----
  private loadCategoryMappingList(storeId: number): void {
    this.loader.start();
    this.api.getCategoryMappingList(this.organizationId, storeId).subscribe({
      next: (rows) => {
        this.loader.stop();
        this.fullList = rows ?? [];
        const p = this.pagination();
        searchData(this.fullList, p);
        this.pagination.set({ ...p });
      },
      error: () => {
        this.loader.stop();
        // legacy: empty error handler, silently swallowed (GetCategoryMappingList's errorCallback body is empty)
      },
    });
  }

  // ---- search box (mirrors pagination.searchData wiring, view:34-36) ----
  onSearchChange(value: string): void {
    const p = this.pagination();
    p.search = value;
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  // ---- page-size select (mirrors pagination.setTotalPages wiring, view:44-46) ----
  onPageSizeChange(): void {
    const p = this.pagination();
    setTotalPages(p);
    this.pagination.set({ ...p });
  }

  page(action: 'first' | 'prev' | 'next' | 'last' | number): void {
    const p = this.pagination();
    if (action === 'first') setFirstPage(p);
    else if (action === 'prev') setPreviousPage(p);
    else if (action === 'next') setNextPage(p);
    else if (action === 'last') setLastPage(p);
    else setPage(action, p);
    this.pagination.set({ ...p });
  }

  pagesList(): number[] {
    return getPagesList(this.pagination());
  }

  // ---- column sort (mirrors pagination.sortBy wiring, view:63-93 -- see
  // class doc for the single shared sortReverse flag) ----
  sortBy(field: SortField): void {
    this.sortReverse = !this.sortReverse;
    const p = this.pagination();
    sortPagination(p, field, this.sortReverse);
    this.pagination.set({ ...p });
  }

  // ---- status toggle (mirrors ToggleStatus, ctrl:105-114) ----
  // ---- Defect B fix, corrected (see class doc) ----
  // Legacy always POSTs a brand-new row on toggle ON, regardless of
  // whether a mapping already exists (just inactive) for this
  // store+category -- confirmed live to produce a genuine duplicate row
  // and a UI that renders the same category twice with conflicting
  // statuses. The backend's generic `PATCH .../{id}/{status}` endpoint
  // (`ProductRestrictionDao.updateProductRestrictionMapper`) is a plain
  // `SET isActive=:status`, not a deactivate-only endpoint -- legacy's
  // frontend simply never called it with `true`. So: reactivate via that
  // same endpoint whenever a row id is already known; only POST-create
  // when the id is genuinely null (no mapping has ever existed).
  toggleStatus(row: ProductCategoryMappingRow, nextStatus: boolean): void {
    // Optimistic flip -- legacy's checkbox is two-way bound, so the visual
    // change happens immediately, before either backend call resolves.
    this.patchRow(row, nextStatus);
    if (nextStatus && row.id == null) {
      this.createMapping(row);
    } else {
      this.updateMappingStatus(row, nextStatus);
    }
  }

  // ---- toggle ON, no existing row yet (mirrors CreateProductCategoryMapping, ctrl:116-139) ----
  private createMapping(row: ProductCategoryMappingRow): void {
    const storeId = this.selectedStore().id;
    this.loader.start();
    this.api.create({ storeId, productCategoryId: row.productCategoryId, status: true }).subscribe({
      next: (res) => {
        this.loader.stop();
        this.notify.success(res?.message ?? 'Product Category Mapping created successfully');
        // Re-fetch so the new row's real id is populated locally -- see
        // class doc; still needed for the very first toggle on a category
        // that has never had a mapping row before.
        this.loadCategoryMappingList(this.selectedStore().id);
      },
      error: () => {
        this.loader.stop();
        this.patchRow(row, false);
        this.notify.error('Something went wrong');
      },
    });
  }

  // ---- toggle ON reactivate / toggle OFF (mirrors UpdateProductCategoryMapping,
  // ctrl:141-160, generalized to both directions -- see class doc) ----
  private updateMappingStatus(row: ProductCategoryMappingRow, status: boolean): void {
    if (row.id == null) {
      // Defensive only -- should be unreachable: toggleStatus() only
      // routes here when nextStatus is false (row.id is always non-null
      // once a row has ever been listed with a real mapping) or when
      // row.id is already known non-null.
      this.patchRow(row, !status);
      this.notify.error('Something went wrong');
      return;
    }
    this.loader.start();
    this.api.updateStatus(row.id, status).subscribe({
      next: (res) => {
        this.loader.stop();
        this.notify.success(res?.message ?? 'Product Category Mapping updated successfully');
      },
      error: () => {
        this.loader.stop();
        this.patchRow(row, !status);
        this.notify.error('Something went wrong');
      },
    });
  }

  // Patches status in place across fullList/filteredData/data (new array
  // references throughout, per docs/ANGULAR_PENDING_FINDINGS.md entry 12)
  // WITHOUT calling searchData/setTotalPages -- deliberately does not reset
  // pagination to page 1, matching legacy's own in-place mutation with no
  // PaginationService involvement at all on this path. See class doc.
  private patchRow(row: ProductCategoryMappingRow, status: boolean): void {
    const apply = (r: ProductCategoryMappingRow) => (r.productCategoryId === row.productCategoryId ? { ...r, status } : r);
    this.fullList = this.fullList.map(apply);
    const p = this.pagination();
    p.filteredData = p.filteredData.map(apply);
    p.data = p.data.map(apply);
    this.pagination.set({ ...p });
  }
}
