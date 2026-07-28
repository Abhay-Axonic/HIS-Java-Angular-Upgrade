import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ChecklistEquipmentMapperService } from './checklist-equipment-mapper.service';
import {
  AssetCategoryOption,
  ChecklistEquipmentMapperForm,
  ChecklistEquipmentMapperRow,
  ChecklistOption,
  ProductCategoryOption,
  emptyChecklistEquipmentMapperForm,
} from './checklist-equipment-mapper.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/GlobalMaster/EquipmentMaster/checklist-equipment-mapper.html +
 * resources/assets/js/controller/master/global/equipmentMaster/checklistEquipmentMapperMasterController.js
 * (`checklistEquipmentMapperMasterController`, classic `$scope`, no
 * `controllerAs`). Legacy state `checklistEquipmentMapper`
 * (app.js:12718-12742, resolve block confirmed live directly), moduleId 526
 * (shared with this batch's `checklistEquipmentMaster` sibling).
 *
 * **Correction, 2026-07-28:** this component's original task note cited a
 * "two dropdowns share one ng-model" finding (docs/ANGULAR_PENDING_FINDINGS.md
 * entry 24) as applying here -- verified via a direct full read of the real
 * view and this does NOT apply to this screen. Entry 24 is about a
 * completely different pair of files
 * (`create-asset-scheduler.html`/`details-schedule-worklist.html`'s
 * embedded `#addViewChecklistModal`) that happens to reuse the same
 * `checklistMapperObj.checklistEquipmentMasterId` variable name by
 * coincidence. This screen's 3 dropdowns each have their own distinct
 * `ng-model` (view:15/31/47) -- no shared-model bug here.
 *
 * **SEVERITY-1, backend, CONFIRMED and FIXED before this rebuild could be
 * live-tested end to end:** the Asset Category -> Product Category cascade
 * (`getProductCategoryList`, ctrl:118-130) threw a real HTTP 500 on every
 * call in this dev environment -- confirmed via `catalina.out`:
 * `column productcat0_.general_ledger_id does not exist`, a local-dev-DB
 * schema-drift gap (the `ProductCategory` entity maps this column, the real
 * table didn't have it). Since Product Category is a required field in this
 * screen's own client-side validation, NO real user could complete a Save
 * through the normal UI in this environment before the fix. Owner-approved,
 * applied as a single additive `ALTER TABLE`, verified live before (500)
 * and after (200, real data) -- see
 * `docs/BUSINESS_RULE_MANIFEST.md`, `BR-local-dev-db-drift-productcategory-generalledgerid`.
 * Not a rebuild concern once fixed -- purely a local dev DB gap, same class
 * as this session's other schema-drift entries.
 *
 * **Confirmed real, fully-functional CRUD once the above was fixed.** Live
 * round trip confirmed 2026-07-28: create (with a real Asset->Product
 * Category cascade), edit-then-resave (a real upsert, same one-form/one-
 * button/one-function shape as `checklistEquipmentMaster`), and
 * status-toggle, all HTTP 200 -- the edit-then-resave correctly updated the
 * SAME row in place.
 *
 * **Real, confirmed structural quirks reproduced below:**
 * - One form, one button, one function serves both Create and Update, same
 *   shape and same reasoning as `checklistEquipmentMaster` (see that
 *   component's class doc comment for the full upsert mechanism).
 * - **No confirm-modal of any kind** -- confirmed by full-file read.
 * - **Real, working 5-field client-side validation** -- Checklist Name,
 *   Asset Category, Product Category, Item Code, Item Name are each
 *   checked in sequence with a distinct `growl.warning` (ctrl:136-165).
 *   Item Code/Item Name are plain free-text fields, NOT selected from any
 *   list -- confirmed by direct view read (view:63/77), not a data-entry
 *   afterthought.
 * - **Client-side pagination over the full fetched list**, not server-side
 *   offset paging -- this screen uses a different, custom `PaginationService`
 *   (`$scope.pagination`, ctrl:32-46) than every other sibling in this
 *   codebase (`PagerService`/`$scope.pager`), fetching the entire
 *   `checklistEquipmentMapperList` in one call and paginating/filtering it
 *   in the browser. Reproduced as client-side pagination over the full
 *   fetched array, matching the observable behavior (one list fetch, no
 *   server-side offset calls), not the internal `PaginationService`
 *   mechanics themselves.
 * - Status toggle does not reload the list after success (ctrl:199-233,
 *   success branch calls `$scope.init()` only) -- same reasoning and same
 *   "checkbox's own two-way binding keeps the display correct anyway"
 *   conclusion as `checklistEquipmentMaster`.
 */
@Component({
  selector: 'app-checklist-equipment-mapper',
  imports: [FormsModule, StatusToggle],
  templateUrl: './checklist-equipment-mapper.html',
  styleUrl: './checklist-equipment-mapper.scss',
})
export class ChecklistEquipmentMapper {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ChecklistEquipmentMapperService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  checklistOptions = signal<ChecklistOption[]>([]);
  assetCategoryOptions = signal<AssetCategoryOption[]>([]);
  productCategoryOptions = signal<ProductCategoryOption[]>([]);

  form = signal<ChecklistEquipmentMapperForm>(emptyChecklistEquipmentMapperForm());
  searchKeyword = signal('');
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  rows = signal<ChecklistEquipmentMapperRow[]>([]);

  // ctrl:91 -- button label only; the same function always runs.
  saveMode = computed(() => this.form().checklistEquipmentMapperMasterId === null);

  filteredRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredRows().length / this.pageSize())));

  pagedRows = computed(() => {
    const start = (this.currentPage() - 1) * this.pageSize();
    return this.filteredRows().slice(start, start + this.pageSize());
  });

  constructor() {
    this.loadOptions();
    this.loadList();
  }

  private loadOptions(): void {
    this.api.getChecklistOptions(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.checklistOptions.set((res.listObject ?? []).filter((c) => c.status === 'A'));
      },
    });
    this.api.getAssetCategoryOptions(this.unitId, this.organizationId).subscribe({
      next: (list) => this.assetCategoryOptions.set(list ?? []),
    });
  }

  private loadList(): void {
    this.api.getMapperList(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
          this.currentPage.set(1);
        }
      },
    });
  }

  updateField<K extends keyof ChecklistEquipmentMapperForm>(field: K, value: ChecklistEquipmentMapperForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // mirrors ng-change="getProductCategoryList(...)" (view:31).
  onAssetCategoryChange(assetCategoryId: number | ''): void {
    this.updateField('assetCategoryId', assetCategoryId);
    if (assetCategoryId === '') {
      this.productCategoryOptions.set([]);
      return;
    }
    this.api.getProductCategoryOptions(assetCategoryId, this.organizationId).subscribe({
      next: (list) => this.productCategoryOptions.set(list ?? []),
    });
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // mirrors saveChecklistEquipmentMapperMaster (ctrl:134-197) -- real
  // 5-field client-side validation, single upsert call.
  onSaveClick(): void {
    const f = this.form();
    if (f.checklistEquipmentMasterId === '') {
      this.notify.warning('Please select checklist name');
      return;
    }
    if (f.assetCategoryId === '') {
      this.notify.warning('Please select asset category');
      return;
    }
    if (f.productCategoryId === '') {
      this.notify.warning('Please select product category');
      return;
    }
    if (!f.itemCode.trim()) {
      this.notify.warning('Please select item code');
      return;
    }
    if (!f.itemName.trim()) {
      this.notify.warning('Please select item name');
      return;
    }
    this.api
      .save({ ...f, organizationId: this.organizationId, unitId: this.unitId, userId: this.userId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
            this.form.set(emptyChecklistEquipmentMapperForm());
            this.productCategoryOptions.set([]);
          } else {
            this.notify.error(res.message ?? 'Error');
          }
          this.loadList();
        },
      });
  }

  // mirrors $scope.update (ctrl:235-247) -- loads a row into the form for
  // editing (including re-cascading Product Category), and the actual
  // persist happens via onSaveClick's upsert.
  edit(row: ChecklistEquipmentMapperRow): void {
    this.form.set({
      checklistEquipmentMapperMasterId: row.checklistEquipmentMapperMasterId,
      checklistEquipmentMasterId: row.checklistEquipmentMasterId,
      assetCategoryId: row.assetCategoryId,
      productCategoryId: row.productCategoryId,
      itemCode: row.itemCode,
      itemName: row.itemName,
    });
    this.api.getProductCategoryOptions(row.assetCategoryId, this.organizationId).subscribe({
      next: (list) => this.productCategoryOptions.set(list ?? []),
    });
  }

  // mirrors updateStatus (ctrl:199-233) -- no list reload needed after, see
  // class doc comment.
  toggleStatus(row: ChecklistEquipmentMapperRow): void {
    const nextStatus: ChecklistEquipmentMapperRow['status'] = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) =>
      rows.map((r) =>
        r.checklistEquipmentMapperMasterId === row.checklistEquipmentMapperMasterId ? { ...r, status: nextStatus } : r,
      ),
    );
    this.api
      .updateStatus({
        checklistEquipmentMapperMasterId: row.checklistEquipmentMapperMasterId,
        status: nextStatus,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
          } else {
            this.notify.error(res.message ?? 'Error');
          }
        },
      });
  }
}
