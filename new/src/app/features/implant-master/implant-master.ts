import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { ImplantMasterService } from './implant-master.service';
import {
  AssetCategoryOption,
  ImplantMasterRow,
  ManufacturerResult,
  ProductCategoryOption,
  SpecialityOption,
  SpecialityRow,
  StorageUnitOption,
  TaxOption,
} from './implant-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const CODE_PATTERN = /^[a-z0-9]*[-]?[a-z0-9]*$/i;
const SELECT_ASSET_CATEGORY: AssetCategoryOption = { id: 0, category: 'Select Asset Category Type' };
const SELECT_PRODUCT_CATEGORY: ProductCategoryOption = { id: 0, category: 'Select Product Category' };
const SELECT_TAX: TaxOption = { id: 0, type: 'Select Tax' };
const SELECT_UNIT: StorageUnitOption = { id: 0, unit: 'Select Storage Unit' };

/**
 * Parity rebuild of views/master/UnitMaster/Inventory/implant-master.html +
 * resources/assets/js/controller/master/unit/implantMasterController.js.
 * Second pilot batch replacement for `OTBillingCompletion` (the
 * originally-picked "both" clinical+billing screen, found during audit to
 * be a stale clone of a completely different screen's template -- see the
 * pending-findings doc). `implantMaster`'s wiring was independently
 * re-verified sound (43 of 45 view bindings resolve; the 2 that don't are
 * both inert/cosmetic, not a mismatched-screen situation).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - `getActiveSpecialityList`'s payload hardcodes `organizationId`/`unitId`
 *   to `1` -- a real legacy bug (every org/unit sees org/unit 1's
 *   speciality list), reproduced verbatim rather than silently "fixed" to
 *   use the real session values.
 * - The Save button's "disabled" look only covers 6 of the 9 required
 *   fields (omits Purchase Tax/Sale Tax/Storage Unit) -- reproduced via
 *   `[class.disabled]`, not a real `[disabled]`, matching legacy's actual
 *   click-still-opens-the-modal behavior.
 * - The `UpdateFixedAssetType` speciality-ID-loss bug is reproduced as-is:
 *   a speciality row added via "+" *during an edit session* is saved with
 *   `id: undefined` (only pre-existing, untouched rows from the original
 *   edit-load keep their real ID) -- flagged in this pilot batch's
 *   pending-findings doc as a real data-loss bug worth an owner decision,
 *   not silently fixed here.
 * - Legacy's missing `$state` DI (crash on missing/expired session
 *   cookie, same bug class as the already-fixed `organizationMaster` bug)
 *   isn't reproducible in this rebuild's architecture -- `SessionService`
 *   never throws.
 * - The redundant "optimistic local push, then immediately re-fetch the
 *   whole list anyway" pattern on create/update is collapsed to just the
 *   re-fetch -- output is provably identical (Safe Refactoring per
 *   RULES.md), not a behavior change.
 * - `getActiveSpecialityList()` (the dead, empty-body function bound to
 *   the "Add" speciality button) and the read-only "view specialities"
 *   popup's `ViewTax()` no-argument call-site bug are not reproduced --
 *   the Add button's real behavior (opening the modal) comes entirely
 *   from the Bootstrap `data-toggle` attribute, independent of the
 *   Angular handler, so nothing is lost by simplifying this to a direct
 *   `showSpecialityModal.set(true)` call.
 */
@Component({
  selector: 'app-implant-master',
  imports: [FormsModule],
  templateUrl: './implant-master.html',
  styleUrl: './implant-master.scss',
})
export class ImplantMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ImplantMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdowns ----
  assetCategoryList = signal<AssetCategoryOption[]>([SELECT_ASSET_CATEGORY]);
  productCategoryList = signal<ProductCategoryOption[]>([SELECT_PRODUCT_CATEGORY]);
  purchaseTaxes = signal<TaxOption[]>([SELECT_TAX]);
  saleTaxes = signal<TaxOption[]>([SELECT_TAX]);
  storageUnits = signal<StorageUnitOption[]>([SELECT_UNIT]);
  specialityList = signal<SpecialityOption[]>([]);

  // ---- form state (mirrors self.model.*) ----
  implantId = signal<number | undefined>(undefined);
  assetCategory = signal<AssetCategoryOption>(SELECT_ASSET_CATEGORY);
  productCategory = signal<ProductCategoryOption>(SELECT_PRODUCT_CATEGORY);
  itemCode = signal('');
  productCode = signal('');
  assetName = signal('');
  manufacturer = signal('');
  private manufactureId: number | undefined;
  purchaseTax = signal<TaxOption>(SELECT_TAX);
  saleTax = signal<TaxOption>(SELECT_TAX);
  unit = signal<StorageUnitOption>(SELECT_UNIT);
  isConsignment = signal(false);
  isSterilized = signal(false);
  isBatchRequired = signal(false);
  status = signal(true);

  isValidCode = signal(false); // Asset Category
  isValidProductCat = signal(false);
  isValidItemCode = signal(false);
  isValidProductCode = signal(false);
  isValidAssetName = signal(false);
  isValidManufacturer = signal(false);
  isPurchaseTaxValid = signal(false);
  isSaleTaxValid = signal(false);
  isStorageUnitValid = signal(false);

  manufacturerResults = signal<ManufacturerResult[]>([]);
  private readonly manufacturerSearch$ = new Subject<string>();

  // ---- speciality picker (mirrors specialityDetailList / #addSpecialityComponent) ----
  showSpecialityPicker = signal(false);
  specialityDetailList = signal<SpecialityRow[]>([]);
  specialityCount = signal(0);
  showSpecialityViewer = signal(false);
  specialityViewerList = signal<{ specialityName: string }[]>([]);

  showConfirmModal = signal(false);

  // ---- list + pagination ----
  rows = signal<ImplantMasterRow[]>([]);
  searchKeyword = signal('');
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  filteredRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.rows();
    return this.rows().filter((r) =>
      Object.values(r).some((v) => v != null && typeof v !== 'object' && String(v).toLowerCase().includes(keyword)),
    );
  });
  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredRows().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedRows = computed(() => {
    const limit = this.pageSize();
    const offset = (this.currentPage() - 1) * limit;
    return this.filteredRows().slice(offset, offset + limit);
  });

  constructor() {
    this.api.getAssetCategories(this.unitId, this.organizationId).subscribe({
      next: (categories) => this.assetCategoryList.set([SELECT_ASSET_CATEGORY, ...categories]),
    });
    this.api.getActiveSpecialityList().subscribe({
      next: (res) => this.specialityList.set(res.listObject ?? []),
    });
    this.api.getDropdowns(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        this.storageUnits.set([SELECT_UNIT, ...(res.storageUnit ?? [])]);
        this.purchaseTaxes.set([SELECT_TAX, ...(res.tax ?? [])]);
        this.saleTaxes.set([SELECT_TAX, ...(res.tax ?? [])]);
      },
    });
    this.loadList();

    this.manufacturerSearch$
      .pipe(
        debounceTime(250),
        switchMap((term) => this.api.searchManufacturer(this.organizationId, term)),
      )
      .subscribe({ next: (r) => this.manufacturerResults.set(r) });
  }

  private loadList(): void {
    this.api.getImplantMasterList(this.unitId).subscribe({ next: (rows) => this.rows.set(rows ?? []) });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- cascading asset-category -> product-category ----
  onAssetCategoryChange(category: AssetCategoryOption): void {
    this.assetCategory.set(category);
    this.isValidCode.set(false);
    this.productCategoryList.set([SELECT_PRODUCT_CATEGORY]);
    this.productCategory.set(SELECT_PRODUCT_CATEGORY);
    if (!category.id) return;
    this.api.getProductCategories(category.id, this.organizationId).subscribe({
      next: (categories) => this.productCategoryList.set([SELECT_PRODUCT_CATEGORY, ...categories]),
    });
  }

  // ---- manufacturer typeahead ----
  onManufacturerChange(term: string): void {
    this.manufacturer.set(term);
    this.manufactureId = undefined;
    this.isValidManufacturer.set(false);
    if (term.length >= 1) this.manufacturerSearch$.next(term);
  }

  selectManufacturer(m: ManufacturerResult): void {
    this.manufacturer.set(m.manufacturerName);
    this.manufactureId = m.id;
    this.manufacturerResults.set([]);
  }

  // ---- speciality picker ----
  openSpecialityPicker(): void {
    if (!this.specialityDetailList().length) {
      this.specialityDetailList.set([{ specialityName: null }]);
    }
    this.showSpecialityPicker.set(true);
  }

  closeSpecialityPicker(): void {
    this.showSpecialityPicker.set(false);
  }

  addSpecialityRow(index: number): void {
    if (index !== this.specialityDetailList().length - 1) return;
    this.specialityDetailList.update((list) => [...list, { specialityName: null }]);
  }

  removeSpecialityRow(index: number): void {
    if (index === 0) return;
    this.specialityDetailList.update((list) => list.filter((_, i) => i !== index));
  }

  onSpecialityRowChange(index: number, option: SpecialityOption): void {
    this.specialityDetailList.update((list) =>
      list.map((r, i) => (i === index ? { ...r, specialityName: option } : r)),
    );
  }

  saveSpecialityDetails(): void {
    const list = this.specialityDetailList();
    if (list.some((r) => !r.specialityName)) {
      this.notify.error('Speciality is required');
      return;
    }
    const ids = list.map((r) => r.specialityName!.specialityId);
    if (new Set(ids).size !== ids.length) {
      this.notify.error('Duplicate Speciality');
      return;
    }
    this.specialityCount.set(list.length);
    this.showSpecialityPicker.set(false);
  }

  viewSpecialities(row: ImplantMasterRow): void {
    this.specialityViewerList.set(row.speciality ?? []);
    this.showSpecialityViewer.set(true);
  }

  closeSpecialityViewer(): void {
    this.showSpecialityViewer.set(false);
  }

  // ---- validation (mirrors ValidateImplantMaster) ----
  private validateRequired(): boolean {
    let valid = true;
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
    if (!this.purchaseTax().id) {
      this.isPurchaseTaxValid.set(true);
      valid = false;
    }
    if (!this.saleTax().id) {
      this.isSaleTaxValid.set(true);
      valid = false;
    }
    if (!this.unit().id) {
      this.isStorageUnitValid.set(true);
      valid = false;
    }
    return valid;
  }

  private validateCodes(excludeId?: number): boolean {
    const itemCode = this.itemCode();
    const productCode = this.productCode();
    if (this.rows().some((r) => r.itemCode === itemCode && r.id !== excludeId)) {
      this.notify.error('Duplicate item code');
      return false;
    }
    if (this.rows().some((r) => r.productCode === productCode && r.id !== excludeId)) {
      this.notify.error('Duplicate product code');
      return false;
    }
    if (!CODE_PATTERN.test(productCode)) {
      this.notify.error('Code can only be alpha numeric with single hypen.');
      return false;
    }
    return true;
  }

  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validateRequired()) return;
    if (!this.validateCodes(this.implantId())) return;

    const specialeety = this.specialityDetailList().map((r) => ({ id: r.id }));
    const payload = {
      productCategoryId: this.productCategory().id,
      manufactureId: this.manufactureId,
      itemCode: this.itemCode(),
      sTaxId: this.saleTax().id,
      pTaxId: this.purchaseTax().id,
      productCode: this.productCode(),
      itemName: this.assetName(),
      storageUnitId: this.unit().id,
      status: true,
      isConsignment: this.isConsignment(),
      isSterilize: this.isSterilized(),
      specialeety,
      isBatchRequired: this.isBatchRequired(),
    };

    const id = this.implantId();
    const request =
      id == null
        ? this.api.create({ ...payload, unitId: this.unitId, organizationId: this.organizationId, userId: this.userId })
        : this.api.update(id, { ...payload, status: this.status() });

    request.subscribe({
      next: (res) => {
        this.loadList();
        this.clearFields();
        this.notify.success(res.message ?? 'Saved successfully');
      },
      error: () => this.notify.error('Something Went Wrong'),
    });
  }

  clearFields(): void {
    this.implantId.set(undefined);
    this.assetCategory.set(SELECT_ASSET_CATEGORY);
    this.productCategory.set(SELECT_PRODUCT_CATEGORY);
    this.productCategoryList.set([SELECT_PRODUCT_CATEGORY]);
    this.itemCode.set('');
    this.productCode.set('');
    this.assetName.set('');
    this.manufacturer.set('');
    this.manufactureId = undefined;
    this.purchaseTax.set(SELECT_TAX);
    this.saleTax.set(SELECT_TAX);
    this.unit.set(SELECT_UNIT);
    this.isConsignment.set(false);
    this.isSterilized.set(false);
    this.isBatchRequired.set(false);
    this.specialityDetailList.set([]);
    this.specialityCount.set(0);
    this.isValidCode.set(false);
    this.isValidProductCat.set(false);
    this.isValidItemCode.set(false);
    this.isValidProductCode.set(false);
    this.isValidAssetName.set(false);
    this.isValidManufacturer.set(false);
    this.isPurchaseTaxValid.set(false);
    this.isSaleTaxValid.set(false);
    this.isStorageUnitValid.set(false);
  }

  // ---- edit ----
  edit(id: number): void {
    this.api.getImplant(id).subscribe({
      next: (detail) => {
        this.implantId.set(detail.id);
        const category = this.assetCategoryList().find((c) => c.id === detail.assetTypeId) ?? SELECT_ASSET_CATEGORY;
        this.assetCategory.set(category);
        if (category.id) {
          this.api.getProductCategories(category.id, this.organizationId).subscribe({
            next: (categories) => {
              this.productCategoryList.set([SELECT_PRODUCT_CATEGORY, ...categories]);
              const pc = categories.find((c) => c.productCategoryId === detail.productCategoryId);
              if (pc) this.productCategory.set(pc);
            },
          });
        }
        this.purchaseTax.set(this.purchaseTaxes().find((t) => t.id === detail.pTaxId) ?? SELECT_TAX);
        this.saleTax.set(this.saleTaxes().find((t) => t.id === detail.sTaxId) ?? SELECT_TAX);
        this.unit.set(this.storageUnits().find((u) => u.id === detail.storageUnitId) ?? SELECT_UNIT);
        this.itemCode.set(detail.itemCode);
        this.productCode.set(detail.productCode);
        this.assetName.set(detail.itemName);
        this.manufacturer.set(detail.manufacturer);
        this.manufactureId = detail.manufactureId;
        this.isConsignment.set(detail.isConsignment);
        this.isSterilized.set(detail.isSterilize);
        this.isBatchRequired.set(detail.isBatchRequired);
        this.status.set(detail.status);
        const specialityRows: SpecialityRow[] = (detail.speciality ?? []).map((s) => ({
          id: s.specialityId,
          specialityName: { specialityId: s.specialityId, specialityName: s.specialityName },
        }));
        this.specialityDetailList.set(specialityRows);
        this.specialityCount.set(specialityRows.length);
      },
    });
  }

  // ---- status toggle ----
  toggleStatus(row: ImplantMasterRow, nextStatus: boolean): void {
    row.status = nextStatus;
    this.api.toggleStatus(row.id, nextStatus).subscribe({
      next: () => this.loadList(),
      error: () => (row.status = !nextStatus),
    });
  }
}
