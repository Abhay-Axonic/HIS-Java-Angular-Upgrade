import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { FixedAssetMasterService } from './fixed-asset-master.service';
import {
  AssetCategoryOption,
  AssetTypeOption,
  FixedAssetRow,
  ManufacturerOption,
  ProductCategoryOption,
} from './fixed-asset-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const CODE_PATTERN = /^[a-z0-9]*[-]?[a-z0-9]*$/i;
const SELECT_ASSET_TYPE: AssetTypeOption = { id: 0, type: 'Select Asset Type' };
const SELECT_ASSET_CATEGORY: AssetCategoryOption = { id: 0, category: 'Select Asset Category Type' };
const SELECT_PRODUCT_CATEGORY: ProductCategoryOption = { id: 0, category: 'Select Product Category' };

type SortField = 'assetCategory' | 'productCategory' | 'assetType' | 'itemCode' | 'productCode' | 'name' | 'manufactureName';

/**
 * Parity rebuild of
 * views/master/UnitMaster/assetManagement/fixed-asset-master.html +
 * resources/assets/js/controller/master/unit/fixedAssetMasterController.js
 * (self-registered state `fixedAssetMaster`, moduleId 644, no
 * requireSuperUser -- confirmed from that file's own
 * `.config($stateProvider)` block; no entry for this state anywhere in
 * app.js).
 *
 * **Explicitly out of scope, disclosed, not silently dropped:** legacy's
 * controller also supports being reached from a different screen (an item
 * request's "Create Fixed Asset" action) via `commonDetailService`
 * cross-screen state hand-off (`self.model.itemDetails/manufacturer/
 * manufactureId/itemRequestId`), which pre-fills the form and links the new
 * asset back to an `ItemRequestDetail` row on create. That hand-off has no
 * equivalent service in this rebuild yet and is not exercised by
 * navigating directly to `/fixedAssetMaster` (the only reachable entry
 * point via this project's own routing) -- this rebuild covers the
 * screen's own primary, directly-reachable behavior only, matching how
 * prior batches have scoped out other screens' cross-screen hand-off
 * flows (see e.g. entry 27's dead alternate-flow treatment).
 *
 * **Findings preserved deliberately (see
 * docs/ANGULAR_PENDING_FINDINGS.md batch 11 entry, placeholder "Entry N"):**
 * - `PopulateAssetTypeDropdown` hardcodes `if (value.id == 2)` when
 *   building the Asset Type dropdown from the real `/api/global/assets/types`
 *   response -- so this screen only ever offers ONE real Asset Type choice
 *   (whichever asset type has id 2, e.g. "Equipment"), regardless of how
 *   many asset types actually exist. Reproduced exactly via the same
 *   filter.
 * - Immediately after that dropdown loads, legacy's own code calls
 *   `PopulateAssetCategoryDropdown(self.model.assetType, undefined)` with
 *   `self.model.assetType` bugged to be the WHOLE dropdown array (not a
 *   single option) at that point in execution -- so the resulting
 *   `assetTypeId` computed inside is always 0, meaning the very first
 *   category-dropdown fetch on every page load is
 *   `GET /api/global/assets/categories/0/{unitId}/{orgId}` (an
 *   effectively-empty "no type selected" query), not a query for the one
 *   real asset type. Reproduced via the same initial `assetTypeId: 0` call
 *   sequence.
 * - Both the Save button (`ng-class="{disabled: ...}"` on a plain `<a>`
 *   with `data-toggle="modal"`) and the row-level Edit pencil icon
 *   (`ng-class="{disabled: !item.status}"` on a plain `ng-click`) use a
 *   CSS-only "disabled" look in legacy -- neither actually blocks the
 *   click. The confirm-Save modal opens regardless of field validity
 *   (`ValidateFixedAssetMaster` only runs after "Ok"), and an inactive
 *   row's Edit icon still loads it for editing. Reproduced via
 *   `[class.disabled]` with the click handler always wired.
 * - Two independent, stacked validation layers, same class as the
 *   `insuranceMaster` pattern (entry 1): `SaveFixedAsset` first runs
 *   `ValidateFixedAssetMaster` (required fields only); if that passes,
 *   `CreateFixedAssetType`/`UpdateFixedAssetType` each run a SEPARATE,
 *   second layer (`ValidateItemCode(ForUpdate)`, `ValidateProductCode(ForUpdate)`
 *   -- duplicate-code checks against the in-memory list, plus the same
 *   `CODE_PATTERN` regex -- and `validateManufacture`, which additionally
 *   BLANKS the manufacturer field on failure). Both layers preserved.
 * - `ToggleStatus` has no revert of the optimistically-flipped switch on a
 *   failed request at all (worse than sibling `assetCategoryMaster`'s
 *   always-revert-to-`true`), and unconditionally calls `ClearFields()`
 *   whenever ANY row is toggled to inactive -- wiping out whatever the
 *   user currently has typed in the form above, even if they were editing
 *   a completely different row. Reproduced exactly.
 * - `GetFixedAssetMasterData` (edit) looks up the matching Asset Type
 *   purely by `id` in the (single-option, id-2-only) dropdown -- if the
 *   fetched record's real `assetTypeId` isn't 2 (a plausible state for any
 *   older/differently-typed record), the lookup returns nothing and
 *   `assetType` ends up unset. Legacy's own subsequent `Validate` then
 *   crashes on `self.model.assetType.id` (`Cannot read properties of
 *   undefined`). This rebuild uses a safe `?.id` lookup here (matching
 *   this project's standing practice of not porting a literal
 *   `ReferenceError`/`TypeError` crash where an idiomatic, safe
 *   equivalent already exists elsewhere in this codebase -- e.g. the
 *   missing-`$state`-DI class) -- disclosed as a known, deliberate
 *   divergence, not a silent one: editing such a record here shows an
 *   empty Asset Type selection and a validation error instead of a blank
 *   crashed page.
 * - The Manufacturer typeahead's `ng-change` in legacy
 *   (`vm.model.isManufacturerValid[$index] = false; vm.model.isValidManufacturer = false`)
 *   references `$index`, which does not exist in this input's scope (it's
 *   not inside an `ng-repeat`), against `vm.model.isManufacturerValid`,
 *   which is never initialized anywhere in the controller -- so this
 *   throws `TypeError: Cannot set properties of undefined` on every
 *   keystroke, which also prevents the second statement
 *   (`vm.model.isValidManufacturer = false`) from ever running, meaning
 *   the "Enter Manufacturer" error never clears once shown. Per this
 *   project's standing practice for a straightforward broken-reference
 *   crash (entries 40/44/54), this rebuild clears the manufacturer
 *   validation flag correctly on input instead. Flagged for fast-track
 *   owner consideration.
 * - `clearSearch()` in legacy calls
 *   `PaginationService.searchData(self.model.genericList, self.model.pagination)`
 *   -- `genericList` is never defined anywhere in this controller (the
 *   real list is `fixedAssetList`); `searchData` still assigns
 *   `pagination.filteredData = undefined` without throwing, but the
 *   `setTotalPages` call it makes immediately after reads
 *   `pagination.filteredData.length`, which throws
 *   `TypeError: Cannot read properties of undefined (reading 'length')`
 *   -- so clicking the "Clear" button crashes EVERY time, confirmed by
 *   reading `paginationService.js` directly. Per the same standing
 *   practice, this rebuild's Clear button resets the search box against
 *   the real list instead of a broken reference. Flagged for fast-track
 *   owner consideration.
 * - The missing-`$state`-DI crash bug (same class as entries 8/10/104/106)
 *   isn't reproducible in this rebuild's architecture -- `SessionService`
 *   never throws; noted for the record only.
 */
@Component({
  selector: 'app-fixed-asset-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './fixed-asset-master.html',
  styleUrl: './fixed-asset-master.scss',
})
export class FixedAssetMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(FixedAssetMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors self.model.*) ----
  fixedAssetId = signal<number | undefined>(undefined);
  private activeFixedAssetId: number | undefined;

  assetTypeDropdown = signal<AssetTypeOption[]>([SELECT_ASSET_TYPE]);
  assetType = signal<AssetTypeOption>(SELECT_ASSET_TYPE);
  assetCategoryDropdown = signal<AssetCategoryOption[]>([SELECT_ASSET_CATEGORY]);
  assetCategory = signal<AssetCategoryOption>(SELECT_ASSET_CATEGORY);
  productCategoryDropdown = signal<ProductCategoryOption[]>([SELECT_PRODUCT_CATEGORY]);
  productCategory = signal<ProductCategoryOption>(SELECT_PRODUCT_CATEGORY);

  itemCode = signal('');
  productCode = signal('');
  assetName = signal('');
  manufacturer = signal('');
  private manufactureId: number | undefined;

  isCSSD = signal(false);
  isModality = signal(false);
  isChargeableEquipment = signal(false);
  isAccessories = signal(false);
  isSpares = signal(false);
  isDialyser = signal(false);
  isCritical = signal(false);

  isAssetTypeIdValid = signal(false);
  isValidCode = signal(false);
  isValidProductCat = signal(false);
  isValidItemCode = signal(false);
  isValidProductCode = signal(false);
  isValidAssetName = signal(false);
  isValidManufacturer = signal(false);

  showConfirmModal = signal(false);

  // ---- manufacturer typeahead (mirrors SearchManufacturer/AddManufacturer) ----
  manufacturerResults = signal<ManufacturerOption[]>([]);
  private readonly manufacturerSearch$ = new Subject<string>();

  // ---- list + client-side pagination (mirrors PaginationService usage) ----
  fixedAssets = signal<FixedAssetRow[]>([]);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');
  predicate = signal<SortField | null>(null);
  reverse = signal(false);

  totalPages = computed(() => Math.max(1, Math.ceil(this.fixedAssets().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedRows = computed(() => {
    const limit = this.pageSize();
    const offset = (this.currentPage() - 1) * limit;
    return this.fixedAssets().slice(offset, offset + limit);
  });

  // Legacy quirk preserved: the search box filters only the CURRENT PAGE's
  // already-paginated rows (`vm.model.pagination.data | filter:search`),
  // not the full list -- see class comment.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    if (!keyword) return this.pagedRows();
    return this.pagedRows().filter(
      (r) =>
        r.assetCategory?.toLowerCase().includes(keyword) ||
        r.productCategory?.toLowerCase().includes(keyword) ||
        r.assetType?.toLowerCase().includes(keyword) ||
        r.itemCode?.toLowerCase().includes(keyword) ||
        r.productCode?.toLowerCase().includes(keyword) ||
        r.name?.toLowerCase().includes(keyword) ||
        r.manufactureName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadAssetTypes();
    this.loadFixedAssets();

    this.manufacturerSearch$.pipe(debounceTime(200), distinctUntilChanged()).subscribe((term) => {
      this.runManufacturerSearch(term);
    });
  }

  private loadFixedAssets(): void {
    this.api.getAll().subscribe({
      next: (rows) => this.fixedAssets.set(rows ?? []),
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  // Legacy quirk preserved: filters the Asset Type dropdown down to id===2
  // only, then immediately fetches the category dropdown for assetTypeId
  // 0 (see class comment) -- not the real selected type.
  private loadAssetTypes(): void {
    this.api.getAssetTypes(this.unitId, this.organizationId).subscribe({
      next: (types) => {
        const onlyType = (types ?? []).filter((t) => t.id === 2);
        this.assetTypeDropdown.set([SELECT_ASSET_TYPE, ...onlyType]);
        this.loadAssetCategories(0);
      },
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  private loadAssetCategories(assetTypeId: number, selectedCategoryId?: number, selectedProductCategoryId?: number): void {
    this.assetCategory.set(SELECT_ASSET_CATEGORY);
    this.productCategory.set(SELECT_PRODUCT_CATEGORY);
    this.assetCategoryDropdown.set([SELECT_ASSET_CATEGORY]);
    this.productCategoryDropdown.set([SELECT_PRODUCT_CATEGORY]);
    this.api.getAssetCategories(assetTypeId, this.unitId, this.organizationId).subscribe({
      next: (categories) => {
        this.assetCategoryDropdown.set([SELECT_ASSET_CATEGORY, ...(categories ?? [])]);
        if (selectedCategoryId != null) {
          const match = this.assetCategoryDropdown().find((c) => c.id === selectedCategoryId);
          if (match) {
            this.assetCategory.set(match);
            this.loadProductCategories(match.id, selectedProductCategoryId);
          }
        }
      },
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  private loadProductCategories(assetCategoryId: number, selectedProductCategoryId?: number): void {
    this.productCategory.set(SELECT_PRODUCT_CATEGORY);
    this.productCategoryDropdown.set([SELECT_PRODUCT_CATEGORY]);
    this.api.getProductCategories(assetCategoryId, this.organizationId).subscribe({
      next: (products) => {
        this.productCategoryDropdown.set([SELECT_PRODUCT_CATEGORY, ...(products ?? [])]);
        if (selectedProductCategoryId != null) {
          const match = this.productCategoryDropdown().find((p) => p.id === selectedProductCategoryId);
          if (match) this.productCategory.set(match);
        }
      },
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  onAssetTypeChange(type: AssetTypeOption): void {
    this.isAssetTypeIdValid.set(false);
    this.assetType.set(type);
    this.loadAssetCategories(type.id);
  }

  onAssetCategoryChange(category: AssetCategoryOption): void {
    this.isValidCode.set(false);
    this.assetCategory.set(category);
    this.loadProductCategories(category.id);
  }

  onProductCategoryChange(): void {
    this.isValidProductCat.set(false);
  }

  sortBy(field: SortField): void {
    this.predicate.set(field);
    this.reverse.set(!this.reverse());
    const dir = this.reverse() ? -1 : 1;
    this.fixedAssets.update((rows) =>
      [...rows].sort((a, b) => {
        const av = (a[field] ?? '') as string;
        const bv = (b[field] ?? '') as string;
        return (av > bv ? 1 : av < bv ? -1 : 0) * dir;
      }),
    );
    this.currentPage.set(1);
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- manufacturer typeahead ----
  onManufacturerInput(term: string): void {
    this.manufacturer.set(term);
    this.isValidManufacturer.set(false);
    if (!term) {
      this.manufacturerResults.set([]);
      return;
    }
    this.manufacturerSearch$.next(term);
  }

  private runManufacturerSearch(term: string): void {
    this.api.searchManufacturer(this.organizationId, term).subscribe({
      next: (results) => this.manufacturerResults.set(results ?? []),
      error: () => this.manufacturerResults.set([]),
    });
  }

  selectManufacturer(m: ManufacturerOption): void {
    this.manufacturer.set(m.manufacturerName ?? m.detail);
    this.manufactureId = m.id;
    this.manufacturerResults.set([]);
  }

  // ---- Save button -> confirm modal (mirrors data-toggle="modal", no
  // upfront validation -- see class comment) ----
  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  isSaveDisabledLook(): boolean {
    return (
      !this.assetType().id ||
      !this.assetCategory().id ||
      !this.productCategory().id ||
      !this.itemCode() ||
      !this.productCode() ||
      !this.assetName() ||
      !this.manufacturer()
    );
  }

  // ---- Ok inside confirm modal (mirrors SaveFixedAsset -> ValidateFixedAssetMaster) ----
  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validateRequired()) return;

    const id = this.fixedAssetId();
    if (id == null) {
      this.create();
    } else {
      this.update(id);
    }
  }

  private validateRequired(): boolean {
    let valid = true;
    if (!this.assetType().id) {
      this.isAssetTypeIdValid.set(true);
      valid = false;
    }
    if (!this.assetCategory().id) {
      this.isValidCode.set(true);
      valid = false;
    }
    if (!this.productCategory().id) {
      this.isValidProductCat.set(true);
      valid = false;
    }
    if (!this.itemCode()) {
      this.isValidItemCode.set(true);
      valid = false;
    }
    if (!this.productCode()) {
      this.isValidProductCode.set(true);
      valid = false;
    }
    if (!this.assetName()) {
      this.isValidAssetName.set(true);
      valid = false;
    }
    if (!this.manufacturer()) {
      this.isValidManufacturer.set(true);
      valid = false;
    }
    return valid;
  }

  // ---- second validation layer (mirrors ValidateItemCode/ValidateProductCode/validateManufacture) ----
  private validateItemCode(excludeId?: number): boolean {
    const dup = this.fixedAssets().some((r) => r.itemCode === this.itemCode() && r.id !== excludeId);
    if (dup) {
      this.notify.error('Duplicate Item code');
      return false;
    }
    return true;
  }

  private validateProductCode(excludeId?: number): boolean {
    const dup = this.fixedAssets().some((r) => r.productCode === this.productCode() && r.id !== excludeId);
    if (dup) {
      this.notify.error('Duplicate Product code');
      return false;
    }
    if (!CODE_PATTERN.test(this.productCode())) {
      this.notify.error('Code can only be alpha numeric with single hypen.');
      return false;
    }
    return true;
  }

  private validateManufacturer(): boolean {
    if (!this.manufactureId) {
      this.notify.warning('Please Select valid Manufacturer');
      this.manufacturer.set('');
      return false;
    }
    return true;
  }

  private create(): void {
    if (!this.validateItemCode() || !this.validateProductCode() || !this.validateManufacturer()) return;

    this.api
      .create({
        productCategoryId: this.productCategory().id,
        manufactureId: this.manufactureId,
        itemCode: this.itemCode(),
        productCode: this.productCode(),
        name: this.assetName(),
        status: true,
        isCssd: this.isCSSD(),
        isModality: this.isModality(),
        isChargeableEqupiment: this.isChargeableEquipment(),
        isAccessories: this.isAccessories(),
        isSpares: this.isSpares(),
        isDialyser: this.isDialyser(),
        isCritical: this.isCritical(),
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loadFixedAssets();
          this.currentPage.set(this.totalPages());
          this.clearFields();
          this.notify.success(res.message ?? 'Fixed Asset Inserted Successfully');
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private update(id: number): void {
    if (!this.validateItemCode(this.activeFixedAssetId) || !this.validateProductCode(this.activeFixedAssetId)) return;

    this.api
      .update(id, {
        productCategoryId: this.productCategory().id,
        manufactureId: this.manufactureId,
        itemCode: this.itemCode(),
        productCode: this.productCode(),
        name: this.assetName(),
        status: true,
        isCssd: this.isCSSD(),
        isModality: this.isModality(),
        isChargeableEqupiment: this.isChargeableEquipment(),
        isAccessories: this.isAccessories(),
        isSpares: this.isSpares(),
        isDialyser: this.isDialyser(),
        isCritical: this.isCritical(),
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          const assetTypeLabel = this.assetType().type;
          const assetCategoryLabel = this.assetCategory().category;
          const productCategoryLabel = this.productCategory().category;
          const itemCode = this.itemCode();
          const productCode = this.productCode();
          const name = this.assetName();
          const manufactureName = this.manufacturer();
          const manufactureId = this.manufactureId;
          this.fixedAssets.update((rows) =>
            rows.map((r) =>
              r.id === id
                ? {
                    ...r,
                    itemCode,
                    productCode,
                    name,
                    manufactureId,
                    manufactureName,
                    assetType: assetTypeLabel,
                    assetCategory: assetCategoryLabel,
                    productCategory: productCategoryLabel,
                  }
                : r,
            ),
          );
          this.clearFields();
          this.notify.success(res.message ?? 'Fixed Asset Updated Successfully');
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  clearFields(): void {
    this.fixedAssetId.set(undefined);
    this.activeFixedAssetId = undefined;
    this.assetType.set(SELECT_ASSET_TYPE);
    this.assetCategory.set(SELECT_ASSET_CATEGORY);
    this.productCategory.set(SELECT_PRODUCT_CATEGORY);
    this.assetCategoryDropdown.set([SELECT_ASSET_CATEGORY]);
    this.productCategoryDropdown.set([SELECT_PRODUCT_CATEGORY]);
    this.itemCode.set('');
    this.productCode.set('');
    this.assetName.set('');
    this.manufacturer.set('');
    this.manufactureId = undefined;
    this.isCSSD.set(false);
    this.isModality.set(false);
    this.isChargeableEquipment.set(false);
    this.isAccessories.set(false);
    this.isSpares.set(false);
    this.isDialyser.set(false);
    this.isCritical.set(false);
    this.isAssetTypeIdValid.set(false);
    this.isValidCode.set(false);
    this.isValidProductCat.set(false);
    this.isValidItemCode.set(false);
    this.isValidProductCode.set(false);
    this.isValidAssetName.set(false);
    this.isValidManufacturer.set(false);
  }

  // Legacy bug fixed (not silently -- see class comment): the real
  // `clearSearch()` referenced an undefined `genericList` and crashed on
  // every click. This resets the search box against the real list.
  clearSearchAndFields(): void {
    this.clearFields();
    this.search.set('');
  }

  // ---- edit (mirrors GetFixedAssetMasterData; cosmetic-disable only --
  // callable even for an inactive row, see class comment) ----
  edit(id: number): void {
    this.activeFixedAssetId = id;
    this.api.getById(id).subscribe({
      next: (row) => {
        const type = this.assetTypeDropdown().find((t) => t.id === row.assetTypeId);
        this.assetType.set(type ?? SELECT_ASSET_TYPE);
        this.loadAssetCategories(row.assetTypeId ?? 0, row.assetCategoryId, row.productCategoryId);
        this.fixedAssetId.set(row.id);
        this.itemCode.set(row.itemCode);
        this.productCode.set(row.productCode);
        this.assetName.set(row.name);
        this.manufacturer.set(row.manufactureName ?? '');
        this.manufactureId = row.manufactureId;
        this.isCSSD.set(!!row.isCssd);
        this.isModality.set(!!row.isModality);
        this.isChargeableEquipment.set(!!row.isChargeableEqupiment);
        this.isAccessories.set(!!row.isAccessories);
        this.isSpares.set(!!row.isSpares);
        this.isDialyser.set(!!row.isDialyser);
        this.isCritical.set(!!row.isCritical);
      },
    });
  }

  // ---- status toggle (mirrors ToggleStatus -- no revert on failure at
  // all, and unconditionally clears the on-screen form on deactivate,
  // regardless of which row -- see class comment) ----
  toggleStatus(row: FixedAssetRow): void {
    const nextStatus = !row.status;
    this.fixedAssets.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    if (!nextStatus) this.clearFields();
    this.api.toggleStatus(row.id, nextStatus, this.unitId, this.organizationId, this.userId).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: () => this.notify.error('Something went wrong'),
    });
  }
}
