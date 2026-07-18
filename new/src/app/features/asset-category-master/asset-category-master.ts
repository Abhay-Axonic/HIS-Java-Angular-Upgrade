import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { AssetCategoryMasterService } from './asset-category-master.service';
import { AssetCategoryRow, AssetTypeOption } from './asset-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const CODE_PATTERN = /^[a-z0-9]*[-]?[a-z0-9]*$/i;

type SortField = 'assetType' | 'code' | 'category';

/**
 * Parity rebuild of
 * views/master/GlobalMaster/AssetManagenent/Master/asset-category-master.html +
 * resources/assets/js/controller/master/AssetManagenent/assetCategoryMasterController.js.
 * Second Phase-2-style pilot batch (controllerAs:'vm' self-registered
 * screens). No new validation, no new fields -- exact parity is the goal,
 * including two real legacy quirks preserved deliberately (see below) and
 * flagged in this pilot batch's pending-findings doc rather than silently
 * fixed.
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - Legacy's `categoryCode`-empty and `categoryDescription`-empty
 *   validation messages are both literally "Enter Asset Category Code" --
 *   a copy-paste label bug in the legacy view (the description field's
 *   error text was never updated). Preserved verbatim; the correct-looking
 *   text would be a silent behavior change.
 * - The search box filters only the CURRENT PAGE's already-fetched rows
 *   (`vm.model.pagination.data`), not the full `assetCategories` list --
 *   legacy wires the search box to a bare Angular `filter`, bypassing
 *   `PaginationService.searchData` entirely. A user searching for a
 *   category on a different page sees "No Record Found" even though the
 *   category exists. Reproduced exactly via `displayedRows`.
 * - Legacy's missing `$state` DI (crash on missing/expired session cookie,
 *   same bug class as the already-fixed `organizationMaster` bug) isn't
 *   reproducible in this rebuild's architecture at all -- `SessionService`
 *   never throws, it just returns `null`/`undefined` fields -- so there is
 *   nothing to preserve here; noted for the record only.
 * - The Save button's "disabled" look is CSS-only in legacy (no HTML5
 *   `disabled`) -- clicking it with empty required fields still opens the
 *   confirm modal, and validation only blocks after "Ok" is clicked.
 *   Reproduced via `[class.disabled]` rather than a real `[disabled]`.
 * - `ToggleStatus`'s 406-error handler always reverts `status` back to
 *   `true` regardless of which direction the toggle was attempted --
 *   correct for a failed deactivation, a silent no-op (not a real revert)
 *   for a failed activation. Reproduced as-is.
 */
@Component({
  selector: 'app-asset-category-master',
  imports: [FormsModule],
  templateUrl: './asset-category-master.html',
  styleUrl: './asset-category-master.scss',
})
export class AssetCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AssetCategoryMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  private readonly SELECT_ASSET_TYPE: AssetTypeOption = { id: 0, type: 'Select Asset Type' };

  // ---- form state (mirrors self.model.*) ----
  categoryId = signal<number | undefined>(undefined);
  private activeCategoryId: number | undefined;
  categoryDropdown = signal<AssetTypeOption[]>([this.SELECT_ASSET_TYPE]);
  assetType = signal<AssetTypeOption>(this.SELECT_ASSET_TYPE);
  private oldAssetType: AssetTypeOption = this.SELECT_ASSET_TYPE;
  private assetTypeNew: AssetTypeOption | null = null;
  categoryCode = signal('');
  categoryDescription = signal('');

  isValidType = signal(false);
  isValidCode = signal(false);
  isValidCategory = signal(false);

  showStoreChangeModal = signal(false);
  showConfirmModal = signal(false);

  // ---- list + client-side pagination (mirrors PaginationService usage) ----
  assetCategories = signal<AssetCategoryRow[]>([]);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');
  predicate = signal<SortField | null>(null);
  reverse = signal(false);

  totalPages = computed(() => Math.max(1, Math.ceil(this.assetCategories().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedRows = computed(() => {
    const limit = this.pageSize();
    const offset = (this.currentPage() - 1) * limit;
    return this.assetCategories().slice(offset, offset + limit);
  });

  // Legacy quirk preserved: search filters the CURRENT PAGE only (see class comment).
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    let rows = this.pagedRows();
    if (keyword) {
      rows = rows.filter(
        (r) =>
          r.assetType?.toLowerCase().includes(keyword) ||
          r.code?.toLowerCase().includes(keyword) ||
          r.category?.toLowerCase().includes(keyword),
      );
    }
    const field = this.predicate();
    if (field) {
      const dir = this.reverse() ? -1 : 1;
      rows = [...rows].sort((a, b) => (a[field] > b[field] ? 1 : a[field] < b[field] ? -1 : 0) * dir);
    }
    return rows;
  });

  constructor() {
    this.api.getAssetTypes(this.unitId, this.organizationId).subscribe({
      next: (types) => {
        this.categoryDropdown.set([this.SELECT_ASSET_TYPE, ...types]);
        this.loadCategories();
      },
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  private loadCategories(): void {
    this.api.getAssetCategories(this.organizationId).subscribe({
      next: (rows) => this.assetCategories.set(rows ?? []),
    });
  }

  sortBy(field: SortField): void {
    this.predicate.set(field);
    this.reverse.set(!this.reverse());
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  clearSearch(): void {
    this.search.set('');
  }

  // ---- asset-type change / clear-confirm flow ----
  onAssetTypeFocus(): void {
    this.oldAssetType = this.assetType();
  }

  onAssetTypeChange(type: AssetTypeOption): void {
    this.isValidType.set(false);
    if (this.categoryCode().trim() || this.categoryDescription().trim()) {
      this.assetTypeNew = type;
      this.assetType.set(this.oldAssetType);
      this.showStoreChangeModal.set(true);
      return;
    }
    this.assetType.set(type);
  }

  confirmAssetTypeChange(): void {
    if (this.assetTypeNew) this.assetType.set(this.assetTypeNew);
    this.categoryCode.set('');
    this.categoryDescription.set('');
    this.showStoreChangeModal.set(false);
  }

  closeStoreChangeModal(): void {
    this.showStoreChangeModal.set(false);
  }

  // ---- required-field validation (mirrors ValidateAssetCategory) ----
  private validateRequired(): boolean {
    let valid = true;
    if (!this.categoryCode()) {
      this.isValidCode.set(true);
      valid = false;
    }
    if (!this.assetType().id) {
      this.isValidType.set(true);
      valid = false;
    }
    if (!this.categoryDescription()) {
      this.isValidCategory.set(true);
      valid = false;
    }
    return valid;
  }

  // ---- format + duplicate validation (mirrors ValidateCode/ValidateCodeForUpdate) ----
  private validateCode(excludeId?: number): boolean {
    const code = this.categoryCode();
    const duplicate = this.assetCategories().some((c) => c.code === code && c.id !== excludeId);
    if (duplicate) {
      this.notify.error('Duplicate asset category code');
      return false;
    }
    if (!CODE_PATTERN.test(code)) {
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

  // ---- save (mirrors SaveAssetCategory -> CreateAssetCategory/UpdateAssetCategory) ----
  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validateRequired()) return;

    const id = this.categoryId();
    if (id == null) {
      this.create();
    } else {
      this.update();
    }
  }

  private create(): void {
    if (!this.validateCode()) return;
    this.api
      .create({
        assetTypeId: this.assetType().id,
        code: this.categoryCode(),
        category: this.categoryDescription(),
        status: true,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loadCategories();
          this.clearFields();
          this.notify.success(res.message ?? 'Saved successfully');
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  private update(): void {
    if (!this.validateCode(this.activeCategoryId)) return;
    const id = this.activeCategoryId as number;
    this.api
      .update(id, {
        assetTypeId: this.assetType().id,
        code: this.categoryCode(),
        category: this.categoryDescription(),
        status: true,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.assetCategories.update((rows) =>
            rows.map((r) =>
              r.id === id
                ? { ...r, assetType: this.assetType().type, assetTypeId: this.assetType().id, code: this.categoryCode(), category: this.categoryDescription() }
                : r,
            ),
          );
          this.clearFields();
          this.notify.success(res.message ?? 'Saved successfully');
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  clearFields(): void {
    this.categoryId.set(undefined);
    this.activeCategoryId = undefined;
    this.assetType.set(this.SELECT_ASSET_TYPE);
    this.categoryCode.set('');
    this.categoryDescription.set('');
    this.isValidCode.set(false);
    this.isValidType.set(false);
    this.isValidCategory.set(false);
  }

  // ---- edit (mirrors GetAssetCategory) ----
  edit(id: number): void {
    this.isValidCode.set(false);
    this.isValidType.set(false);
    this.isValidCategory.set(false);
    this.activeCategoryId = id;
    this.api.getAssetCategory(id).subscribe({
      next: (detail) => {
        this.categoryId.set(detail.id);
        const type = this.categoryDropdown().find((t) => t.id === detail.assetTypeId) ?? this.SELECT_ASSET_TYPE;
        this.assetType.set(type);
        this.categoryCode.set(detail.code);
        this.categoryDescription.set(detail.category);
      },
    });
  }

  // ---- status toggle (mirrors ToggleStatus) ----
  // Update via the list signal, not a direct mutation of `row` -- see
  // docs/ANGULAR_PENDING_FINDINGS.md entry 12.
  toggleStatus(row: AssetCategoryRow, nextStatus: boolean): void {
    this.assetCategories.update((rows) =>
      rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)),
    );
    if (!nextStatus) this.clearFields();
    this.api.toggleStatus(row.id, nextStatus, this.unitId, this.organizationId, this.userId).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: (err) => {
        if (err?.status === 406) {
          this.notify.error(err?.error?.message ?? 'Status change failed');
          this.assetCategories.update((rows) =>
            rows.map((r) => (r.id === row.id ? { ...r, status: true } : r)),
          );
        }
      },
    });
  }
}
