import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { GenericMasterService } from './generic-master.service';
import {
  AssetCategoryOption,
  AssetTypeOption,
  ComboRow,
  GenericComboOption,
  GenericFormState,
  GenericMasterRow,
  ProductCategoryOption,
  StrengthUnitOption,
  emptyGenericForm,
} from './generic-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

interface ComboDetailRow {
  name: string;
  strengthUnit: number;
  strength: string;
}

/**
 * Parity rebuild of
 * views/master/GlobalMaster/AssetManagenent/Master/generic-master.html +
 * resources/assets/js/controller/master/pharmacy/genericMasterController.js
 * ("GenericMaster.Controller", state `genericMaster`, moduleId 463,
 * self-registered via the controller file's own `.config($stateProvider)`
 * block at genericMasterController.js:742-771 -- NOT in app.js, this is one
 * of the 469 externally-registered states entry 3 in
 * docs/ANGULAR_PENDING_FINDINGS.md documents. No requireSuperUser check).
 *
 * **Step 0 re-verification (this screen was flagged by a static axis-3
 * check as a false positive per docs/ANGULAR_PENDING_FINDINGS.md entry 85
 * -- confirmed genuinely clean, not re-derived blindly):** the state
 * declares `controllerAs: "vm"`, the controller does `var self = this;`
 * and assigns the list via `self.model.genericList = response.data`
 * (ctrl:79-96), and the view's `ng-repeat="genericName in
 * vm.model.pagination.data"` (view:214) resolves against that real data.
 * `vm.*` usage is genuinely load-bearing here (60+ real bindings), not the
 * "declared but dead" pattern entry 11 found elsewhere.
 *
 * **Deliberate quirks/bugs preserved verbatim, not fixed (see this
 * screen's drafted docs/ANGULAR_PENDING_FINDINGS.md entry for the full
 * writeup and severity read):**
 * - The list is fetched WITHOUT pagination params (`GET
 *   /api/globals/generics/{orgId}` returns every row for the org) --
 *   search and paging are both done CLIENT-SIDE against the full list,
 *   matching legacy's `PaginationService.searchData`/`setTotalPages`
 *   exactly (search across assetType/assetCategory/code/productCategory/
 *   name). This is NOT the scaffold's default server-driven pagination --
 *   using that here would silently change real behavior (e.g. searching
 *   would only search the current page), so it's deliberately not used.
 * - The Save icon has NO `disabled` attribute in legacy -- the `ng-class`
 *   that greys it out is purely cosmetic (view:138); clicking it ALWAYS
 *   opens the confirm-save modal via a raw `data-toggle="modal"`
 *   attribute regardless of form validity. Only the modal's own "Ok"
 *   button runs `ValidateGenericMaster()`. Reproduced exactly:
 *   `openSaveConfirm()` always opens the modal; `confirmSave()` is the
 *   only place validation actually runs.
 * - One Save action serves both create AND update (`SaveGeneric`,
 *   ctrl:432-440) -- there is no separate "Update" button/icon anywhere in
 *   this view, unlike several sibling masters. Branches on whether
 *   `form().id` is set (populated only via `editGeneric`).
 * - The confirm-close-combination flow (`CloseCombination`, ctrl:677-683)
 *   clears the combination rows and closes both popups but does **not**
 *   reset `isCombination` back to `false` -- so a user can end up with the
 *   toggle still ON but zero combination rows, which then fails
 *   `ValidateGenericMaster`'s own "Add Generic Combination" check on the
 *   next Save attempt. Reproduced by `confirmCloseCombination()` leaving
 *   `form().isCombination` untouched.
 * - Generic Code is DISABLED while creating a new record (auto-filled
 *   from the manufacturer-prefix service, `getPrefix()`, screenId
 *   hardcoded to `178` in legacy itself) and only becomes EDITABLE once
 *   an existing record is opened via Edit (`GetGenericData` sets
 *   `codeDisableFlag = false`, ctrl:264) -- the inverse of the more usual
 *   "locked on edit" pattern, but that's what legacy actually does.
 * - `PopulateAssetCategoryDropdown(1, "")` is fired at init with a
 *   hardcoded `assetTypeId` of `1`, independently of whatever the asset
 *   type dropdown's own load actually returns (ctrl:72-74) -- reproduced
 *   as-is (`loadAssetCategories(1)` fires in parallel with
 *   `loadAssetTypes()` at construction, not chained after it).
 * - The combination sub-form's "Generic Name" dropdown option list
 *   (`activeGenericOptions`) is a disclosed, NOT-silently-fixed deviation
 *   from legacy -- see generic-master.models.ts's `GenericComboOption`
 *   doc comment for the full writeup (legacy's own list accumulates
 *   duplicate options on every save/update reload; this rebuild derives
 *   it reactively instead).
 *
 * **Step 4 golden-trace re-audit note (disclosed deviation, not silently
 * fixed):** legacy's golden trace (tools/golden-trace/golden-traces/genericMaster.json)
 * shows only 3 initial calls (assets/types, assets/categories, manufacturers/
 * prefix) because `GetGenericList()`/`GetStrengthUnitList()` are only ever
 * fired from INSIDE `PopulateAssetTypeDropdown()`'s own success callback
 * (ctrl:196-207) -- an implicit, fragile dependency chain: if the asset-type
 * call ever failed in legacy, the generics list and strength-unit dropdown
 * would silently never load at all. This rebuild's constructor fires
 * `loadGenericList()`/`loadStrengthUnits()` independently, not gated behind
 * `loadAssetTypes()`'s response -- so in a real environment (5 total calls
 * either way once everything resolves) the observable end state is the
 * same, but this rebuild is more resilient to a partial failure than
 * legacy's literal chain. Kept deliberately (same precedent as
 * insurance-master's entry-4 self-correcting deviation), not reproduced as
 * a fragile chain for parity-for-parity's-sake.
 */
@Component({
  selector: 'app-generic-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './generic-master.html',
  styleUrl: './generic-master.scss',
})
export class GenericMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(GenericMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;
  private get storeId(): number {
    return (this.session.current?.['storeId'] as number) ?? 0;
  }

  // ---- dropdown source lists ----
  assetTypes = signal<AssetTypeOption[]>([{ id: 0, type: 'Select Asset Type' }]);
  assetCategories = signal<AssetCategoryOption[]>([{ id: 0, category: 'Select Asset Category' }]);
  productCategories = signal<ProductCategoryOption[]>([{ id: 0, category: 'Select Product Category' }]);
  strengthUnits = signal<StrengthUnitOption[]>([]);

  // ---- main list + client-side search/pagination (see class doc) ----
  fullList = signal<GenericMasterRow[]>([]);
  searchTerm = signal('');
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  filteredList = computed(() => {
    const q = this.searchTerm().trim().toLowerCase();
    const rows = this.fullList();
    if (!q) return rows;
    return rows.filter(
      (r) =>
        (r.assetType ?? '').toLowerCase().includes(q) ||
        (r.assetCategory ?? '').toLowerCase().includes(q) ||
        (r.code ?? '').toLowerCase().includes(q) ||
        (r.productCategory ?? '').toLowerCase().includes(q) ||
        (r.name ?? '').toLowerCase().includes(q),
    );
  });
  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredList().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedList = computed(() => {
    const start = (this.currentPage() - 1) * this.pageSize();
    return this.filteredList().slice(start, start + this.pageSize());
  });

  /** Combination-dropdown options -- see class doc + models.ts for the
   * disclosed deviation from legacy's duplicate-accumulating version. */
  activeGenericOptions = computed<GenericComboOption[]>(() => [
    { id: 0, name: 'Select Generic' },
    ...this.fullList()
      .filter((g) => g.status)
      .map((g) => ({ id: g.id, name: g.name })),
  ]);

  // ---- form state ----
  form = signal<GenericFormState>(emptyGenericForm());
  codeDisabled = signal(true);
  private previousGenericCode: string | null = null;

  assetTypeInvalid = signal(false);
  assetCategoryInvalid = signal(false);
  productCategoryInvalid = signal(false);
  codeInvalid = signal(false);
  nameInvalid = signal(false);

  // ---- "Is Combination" sub-form ----
  comboRows = signal<ComboRow[]>([]);
  comboGenericInvalid = signal<Record<number, boolean>>({});
  comboStrengthInvalid = signal<Record<number, boolean>>({});
  showCombinationModal = signal(false);
  showCombinationCloseConfirm = signal(false);

  // ---- view-only combination-details modal ----
  showCombinationDetailsModal = signal(false);
  combinationDetailsList = signal<ComboDetailRow[]>([]);

  // ---- save confirm modal (raw bootstrap markup in legacy, not the
  // shared <modal> directive -- entry 55's standing policy does not apply
  // to this screen) ----
  showSaveConfirmModal = signal(false);

  constructor() {
    this.loadAssetTypes();
    this.loadAssetCategories(1);
    this.loadPrefix();
    this.loadStrengthUnits();
    this.loadGenericList();
  }

  private loadGenericList(): void {
    this.api.getAll(this.organizationId).subscribe({
      next: (rows) => this.fullList.set(rows ?? []),
    });
  }

  private loadAssetTypes(): void {
    this.api.getAssetTypes(this.unitId, this.organizationId).subscribe({
      next: (rows) => {
        const list = [{ id: 0, type: 'Select Asset Type' }, ...(rows ?? [])];
        this.assetTypes.set(list);
        // legacy: genericMaster.assetType = genericMaster.assetTypeList[1] -- defaults to the first REAL option
        if (list.length > 1) {
          this.form.update((f) => ({ ...f, assetTypeId: list[1].id }));
        }
      },
    });
  }

  private loadAssetCategories(assetTypeId: number, preselectId?: number): void {
    this.api.getAssetCategories(assetTypeId || 1, this.unitId, this.organizationId).subscribe({
      next: (rows) => {
        this.assetCategories.set([{ id: 0, category: 'Select Asset Category' }, ...(rows ?? [])]);
        if (preselectId != null) {
          this.form.update((f) => ({ ...f, assetCategoryId: preselectId }));
          this.loadProductCategories(preselectId);
        }
      },
    });
  }

  private loadProductCategories(assetCategoryId: number, preselectId?: number): void {
    this.api.getProductCategories(assetCategoryId, this.organizationId).subscribe({
      next: (rows) => {
        this.productCategories.set([{ id: 0, category: 'Select Product Category' }, ...(rows ?? [])]);
        if (preselectId != null) {
          this.form.update((f) => ({ ...f, productCategoryId: preselectId }));
        }
      },
    });
  }

  private loadStrengthUnits(): void {
    this.api.getStrengthUnits(this.organizationId).subscribe({
      next: (rows) => this.strengthUnits.set(rows ?? []),
    });
  }

  private loadPrefix(): void {
    this.api.getPrefix(this.organizationId).subscribe({
      next: (res) => {
        if (res?.code != null) {
          this.previousGenericCode = res.code;
          this.form.update((f) => ({ ...f, code: res.code }));
        }
      },
    });
  }

  onAssetTypeChange(assetTypeIdRaw: number | string): void {
    const assetTypeId = Number(assetTypeIdRaw);
    this.assetTypeInvalid.set(false);
    this.form.update((f) => ({ ...f, assetTypeId, assetCategoryId: null, productCategoryId: null }));
    this.loadAssetCategories(assetTypeId);
  }

  onAssetCategoryChange(assetCategoryIdRaw: number | string): void {
    const assetCategoryId = Number(assetCategoryIdRaw);
    this.assetCategoryInvalid.set(false);
    this.form.update((f) => ({ ...f, assetCategoryId, productCategoryId: null }));
    this.loadProductCategories(assetCategoryId);
  }

  onProductCategoryChange(productCategoryIdRaw: number | string): void {
    const productCategoryId = Number(productCategoryIdRaw);
    this.productCategoryInvalid.set(false);
    this.form.update((f) => ({ ...f, productCategoryId }));
  }

  onCodeChange(code: string): void {
    this.codeInvalid.set(false);
    this.form.update((f) => ({ ...f, code }));
  }

  onNameChange(name: string): void {
    this.nameInvalid.set(false);
    this.form.update((f) => ({ ...f, name }));
  }

  // ---- Is Combination ----
  onIsCombinationChange(checked: boolean): void {
    this.comboRows.set([]);
    this.form.update((f) => ({ ...f, isCombination: checked }));
    if (checked) {
      this.comboRows.set([this.blankComboRow()]);
      this.showCombinationModal.set(true);
    } else {
      this.showCombinationModal.set(false);
    }
  }

  private blankComboRow(): ComboRow {
    return { id: null, subGenericId: null, name: this.activeGenericOptions()[0], strengthUnit: 0, strengthUnitName: null };
  }

  addComboRow(): void {
    this.comboRows.update((rows) => [...rows, this.blankComboRow()]);
  }

  removeComboRow(index: number): void {
    this.comboRows.update((rows) => rows.filter((_, i) => i !== index));
  }

  /** Native `<select>` bindings always hand back a string id from the DOM
   * -- these two lookups coerce it back to the real option object so
   * equality checks (subGenericId, strengthUnitName.id) stay numeric. */
  activeGenericOptionById(idRaw: number | string): GenericComboOption {
    const id = Number(idRaw);
    return this.activeGenericOptions().find((o) => o.id === id) ?? this.activeGenericOptions()[0];
  }

  strengthUnitById(idRaw: number | string): StrengthUnitOption | null {
    const id = Number(idRaw);
    return this.strengthUnits().find((u) => u.id === id) ?? null;
  }

  onComboGenericChange(index: number, option: GenericComboOption): void {
    this.comboRows.update((rows) =>
      rows.map((r, i) => (i === index ? { ...r, name: option, subGenericId: option?.id || null } : r)),
    );
    this.comboGenericInvalid.update((m) => ({ ...m, [index]: false }));
  }

  onComboStrengthUnitChange(index: number, option: StrengthUnitOption | null): void {
    this.comboRows.update((rows) => rows.map((r, i) => (i === index ? { ...r, strengthUnitName: option } : r)));
    this.comboStrengthInvalid.update((m) => ({ ...m, [index]: false }));
  }

  onComboStrengthInput(index: number, value: string): void {
    this.comboRows.update((rows) => rows.map((r, i) => (i === index ? { ...r, strengthUnit: Number(value) || 0 } : r)));
    this.comboStrengthInvalid.update((m) => ({ ...m, [index]: false }));
  }

  /** Mirrors SetDecimal (ctrl:669-675). */
  onComboStrengthBlur(index: number): void {
    this.comboRows.update((rows) =>
      rows.map((r, i) => {
        if (i !== index) return r;
        const v = r.strengthUnit;
        if (Number.isNaN(v) || !v || v === 0) return { ...r, strengthUnit: 0 };
        return { ...r, strengthUnit: Number(v.toFixed(2)) };
      }),
    );
  }

  /** Mirrors ValidateCombination (ctrl:642-663), reused for both the
   * combination sub-modal's own Ok button AND the main-form validation
   * path when isCombination is true (legacy duplicates this exact logic
   * in ValidateGenericMaster, ctrl:568-590 -- consolidated here since it's
   * literally the same check, not two independently-tunable rules like
   * insuranceMaster's entry-1 case). */
  private validateComboRows(): boolean {
    let valid = true;
    const rows = this.comboRows();
    const genericInvalid: Record<number, boolean> = {};
    const strengthInvalid: Record<number, boolean> = {};
    rows.forEach((row, i) => {
      const dupes = rows.filter((x) => x.subGenericId === row.subGenericId && x.strengthUnit === row.strengthUnit);
      if (!row.subGenericId) {
        valid = false;
        genericInvalid[i] = true;
        this.notify.error('Generic is required');
      } else if ((!row.strengthUnit && row.strengthUnit < 1) || row.strengthUnit == null) {
        strengthInvalid[i] = true;
        valid = false;
        this.notify.error('Strength Unit is required');
      } else if (dupes.length > 1) {
        valid = false;
        genericInvalid[i] = true;
        strengthInvalid[i] = true;
        this.notify.error('Duplicate Generic Combination');
      }
    });
    this.comboGenericInvalid.set(genericInvalid);
    this.comboStrengthInvalid.set(strengthInvalid);
    return valid;
  }

  saveComboDetails(): void {
    if (this.validateComboRows()) {
      this.showCombinationModal.set(false);
    }
  }

  openCombinationCloseConfirm(): void {
    this.showCombinationCloseConfirm.set(true);
  }

  /** Mirrors CloseCombination (ctrl:677-683) -- deliberately does NOT
   * reset `isCombination`, see class doc. */
  confirmCloseCombination(): void {
    this.showCombinationCloseConfirm.set(false);
    this.showCombinationModal.set(false);
    this.comboRows.set([]);
  }

  cancelCombinationClose(): void {
    this.showCombinationCloseConfirm.set(false);
  }

  viewCombinationDetails(row: GenericMasterRow): void {
    this.api.getCombinationDetail(row.id).subscribe({
      next: (items) => {
        const units = this.strengthUnits();
        const details = (items ?? []).map((item) => {
          const unit = units.find((u) => u.id === item.strength);
          return {
            name: item.genericName ?? '-',
            strengthUnit: item.strengthUnit,
            strength: unit?.unit ?? '',
          };
        });
        this.combinationDetailsList.set(details);
        this.showCombinationDetailsModal.set(true);
      },
    });
  }

  // ---- search / pagination ----
  onSearchInput(term: string): void {
    this.searchTerm.set(term);
    this.currentPage.set(1);
  }

  clearSearch(): void {
    this.searchTerm.set('');
    this.currentPage.set(1);
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- clear / edit ----
  clearFields(): void {
    const f = this.form();
    this.form.set({
      ...emptyGenericForm(),
      code: f.code, // legacy's ClearFields (ctrl:491-503) never blanks genericCode
      assetTypeId: this.assetTypes()[1]?.id ?? null,
      assetCategoryId: this.assetCategories()[0]?.id ?? null,
      productCategoryId: this.productCategories()[0]?.id ?? null,
    });
    this.assetCategories.update((list) => list.slice(0, 1));
    this.productCategories.update((list) => list.slice(0, 1));
    this.comboRows.set([]);
    this.codeDisabled.set(true);
  }

  editGeneric(row: GenericMasterRow): void {
    this.codeDisabled.set(false);
    this.api.getById(this.organizationId, row.id).subscribe({
      next: (details) => {
        this.form.set({
          id: details.id,
          assetTypeId: details.assetTypeId,
          assetCategoryId: null,
          productCategoryId: null,
          code: details.code,
          name: details.name,
          isCombination: (details.items ?? []).length > 0,
        });
        this.loadAssetCategories(details.assetTypeId, details.assetCategoryId);
        this.loadProductCategories(details.assetCategoryId, details.productCategoryId);
        if ((details.items ?? []).length > 0) {
          this.comboRows.set(
            details.items.map((item) => ({
              id: item.id,
              subGenericId: item.subGenericId,
              name: this.activeGenericOptions().find((o) => o.id === item.subGenericId) ?? null,
              strengthUnit: item.strengthUnit,
              strengthUnitName: this.strengthUnits().find((u) => u.id === item.strength) ?? null,
            })),
          );
          this.showCombinationModal.set(true);
        } else {
          this.comboRows.set([]);
        }
      },
    });
  }

  // ---- save / update ----
  /** Legacy's Save icon has no real `disabled` attribute (cosmetic
   * ng-class only) -- clicking it always opens the confirm modal. See
   * class doc. */
  openSaveConfirm(): void {
    this.showSaveConfirmModal.set(true);
  }

  closeSaveConfirm(): void {
    this.showSaveConfirmModal.set(false);
  }

  private validateForm(): boolean {
    const f = this.form();
    let valid = true;
    if (!f.assetTypeId) {
      valid = false;
      this.assetTypeInvalid.set(true);
    }
    if (!f.assetCategoryId) {
      valid = false;
      this.assetCategoryInvalid.set(true);
    }
    if (!f.productCategoryId) {
      valid = false;
      this.productCategoryInvalid.set(true);
    }
    if (!f.code) {
      valid = false;
      this.codeInvalid.set(true);
    }
    if (!f.name) {
      valid = false;
      this.nameInvalid.set(true);
    }
    if (f.isCombination && this.comboRows().length === 0) {
      valid = false;
      this.notify.error('Add Generic Combination');
    }
    if (f.isCombination && this.comboRows().length > 0 && !this.validateComboRows()) {
      valid = false;
    }
    return valid;
  }

  /** Mirrors SaveGeneric (ctrl:432-440) -- one action serves both create
   * and update, branching on whether an id is set. Only ever called from
   * the confirm-save modal's Ok button. */
  confirmSave(): void {
    this.showSaveConfirmModal.set(false);
    if (!this.validateForm()) return;
    const f = this.form();
    if (!f.id) {
      this.createGeneric(f);
    } else {
      this.updateGeneric(f);
    }
  }

  private duplicateCode(code: string, excludeId: number | null): boolean {
    return this.fullList().some((row) => row.id !== excludeId && row.code === code);
  }

  private buildComboPayload() {
    return this.comboRows().map((row) => ({
      id: row.id,
      strengthUnit: row.strengthUnit,
      subGenericId: row.subGenericId,
      strength: row.strengthUnitName?.id ?? null,
    }));
  }

  private createGeneric(f: GenericFormState): void {
    if (this.duplicateCode(f.code, null)) {
      this.notify.error('Duplicate Generic code');
      return;
    }
    const payload = {
      productCategoryId: f.productCategoryId,
      code: f.code,
      name: f.name,
      status: true,
      isCombination: f.isCombination,
      items: this.buildComboPayload(),
    };
    this.api.create(payload).subscribe({
      next: (res) => {
        this.notify.success(res.message ?? 'Record saved successfully');
        this.loadGenericList();
        this.clearFields();
        this.loadPrefix();
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  private updateGeneric(f: GenericFormState): void {
    if (this.duplicateCode(f.code, f.id)) {
      this.notify.error('Duplicate Generic code');
      return;
    }
    const payload = {
      productCategoryId: f.productCategoryId,
      code: f.code,
      name: f.name,
      status: true,
      isCombination: f.isCombination,
      items: this.buildComboPayload(),
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
      storeId: this.storeId,
    };
    this.api.update(f.id!, payload).subscribe({
      next: (res) => {
        this.notify.success(res.message ?? 'Updated successfully');
        this.loadGenericList();
        this.clearFields();
        // legacy: restore the cached prefix code rather than re-fetching, if one is cached (ctrl:475-481)
        if (this.previousGenericCode != null) {
          this.form.update((form) => ({ ...form, code: this.previousGenericCode! }));
        } else {
          this.loadPrefix();
        }
      },
    });
  }

  // ---- status toggle ----
  /** Mirrors ToggleStatus (ctrl:305-333) exactly, including two bugs worth
   * keeping visible rather than silently smoothing over (see this screen's
   * drafted docs/ANGULAR_PENDING_FINDINGS.md entry):
   * - legacy's checkbox `ng-model` flips the row optimistically BEFORE
   *   `ToggleStatus()` even runs, and `if (generic.status == false) { ClearFields(); }`
   *   unconditionally wipes the add/edit form -- for ANY row's
   *   deactivation, even one totally unrelated to whatever is currently
   *   open for editing. Reproduced via the optimistic `fullList.update()`
   *   below plus an unconditional `clearFields()` call.
   * - the 406 handler always force-reverts to `status: true`, regardless
   *   of which direction was actually being attempted (same bug shape as
   *   this codebase's already-documented assetCategoryMaster finding,
   *   entry 10, bug 3) -- correct for a failed deactivation, silently
   *   wrong-looking for a failed activation (leaves it showing "active"
   *   even though it was previously inactive and the activation failed).
   */
  onToggleStatus(row: GenericMasterRow): void {
    const nextStatus = !row.status;
    this.fullList.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    if (nextStatus === false) {
      this.clearFields();
    }
    this.api
      .update(row.id, { status: nextStatus, unitId: this.unitId, organizationId: this.organizationId, userId: this.userId, storeId: this.storeId })
      .subscribe({
        next: () => this.notify.success('Status changed successfully'),
        error: (err) => {
          if (err?.status === 406) {
            this.notify.error(err?.error?.message ?? 'Status change refused');
            this.fullList.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: true } : r)));
          } else {
            this.notify.error('Something Went Wrong');
          }
        },
      });
  }
}
