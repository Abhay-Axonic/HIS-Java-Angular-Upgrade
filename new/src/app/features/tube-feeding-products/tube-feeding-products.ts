import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { TubeFeedingProductsService } from './tube-feeding-products.service';
import { TubeFeedingProductRecord, TubeFeedingProductRow, emptyTubeFeedingProduct } from './tube-feeding-products.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/GlobalMaster/DietNutrition/tube-feeding-products.html +
 * resources/assets/js/controller/master/global/tubeFeedingProductsController.js.
 * No `data.moduleId` on this route -- legacy's state genuinely has no
 * resolve block (app.js:19683-19689, confirmed by direct read), same class
 * as billingGroupMaster/periodicityService.
 *
 * Real, functional golden-trace capture (2026-07-31, live deployment,
 * authenticated session) confirmed `getTubeFeedingProductsList`/
 * `getTubeFeedingProductsCount` fire on load and both return 200 with the
 * expected envelope shape.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - `save` (ctrl:147-205) has the same `popUpFlag` confirm-modal dance as
 *   `nationality-master`/`asset-category-master` -- validation runs before
 *   the modal opens (`$scope.tubeFeedingProducts.$valid`, i.e. both fields
 *   non-empty per the view's `required="required"` attrs), modal only
 *   opens if valid. `update` (ctrl:243-292) has no confirm step, validates
 *   and POSTs directly.
 * - On success, `save`/`update` both call `$scope.initMaster()` (resets the
 *   form only, ctrl:189/277) then unconditionally reload the list at the
 *   CURRENT offset/page-size (ctrl:190/278) -- unlike `nationality-master`,
 *   this screen does NOT reset pagination back to page 1 on success.
 *   Reproduced: `resetForm()` only clears the form; `loadList()` always
 *   runs at whatever page/size is currently selected.
 * - `updateStatus` (ctrl:294-330) reloads the whole list after a successful
 *   toggle (ctrl:317), unlike `nationality-master`'s optimistic-only
 *   pattern. Reproduced: `toggleStatus` flips the row locally for instant
 *   feedback, then always calls `loadList()` regardless of success/failure
 *   (matching legacy calling it unconditionally too, ctrl:316-325 -- the
 *   `loadList` call sits outside the success/error branches).
 * - `showUpdateBtn`/`edit` (ctrl:212-240) has no explicit failure-branch UI
 *   beyond a growl error -- reproduced via `NotificationService.error(...)`.
 */
@Component({
  selector: 'app-tube-feeding-products',
  imports: [FormsModule, StatusToggle],
  templateUrl: './tube-feeding-products.html',
  styleUrl: './tube-feeding-products.scss',
})
export class TubeFeedingProducts {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(TubeFeedingProductsService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  record = signal<TubeFeedingProductRecord>(emptyTubeFeedingProduct());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  list = signal<TubeFeedingProductRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the list client-side over the
  // current server-paginated page (view line 91: `commonList |orderBy:
  // 'productsCode'|filter:searchText`), same pattern as nationality-master.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = [...this.list()].sort((a, b) => a.productsCode.localeCompare(b.productsCode));
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.productsCode?.toLowerCase().includes(keyword) || r.productsDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof TubeFeedingProductRecord>(field: K, value: TubeFeedingProductRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  isRequiredMissing(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  private requiredFieldsValid(): boolean {
    const r = this.record();
    return !!(r.productsCode.trim() && r.productsDesc.trim());
  }

  openConfirmModal(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const r = this.record();
    const now = formatDateTime(new Date());
    this.api
      .save({
        productsDesc: r.productsDesc,
        productsCode: r.productsCode,
        organisationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Tube Feeding Product Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Something went wrong...!!');
          }
          this.loadList();
        },
      });
  }

  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const r = this.record();
    this.api
      .update({
        tubeFeedingProductsId: r.tubeFeedingProductsId as number,
        productsDesc: r.productsDesc,
        productsCode: r.productsCode,
        organisationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Tube Feeding Product Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Something went wrong...!!');
          }
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.record.set(emptyTubeFeedingProduct());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  edit(id: number): void {
    this.saveMode.set(false);
    this.api.getById(id, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error(res.message ?? 'Error!!');
          return;
        }
        const detail = res.listObject[0];
        this.record.set({
          tubeFeedingProductsId: detail.tubeFeedingProductsId,
          productsCode: detail.productsCode,
          productsDesc: detail.productsDesc,
        });
      },
    });
  }

  toggleStatus(row: TubeFeedingProductRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.tubeFeedingProductsId === row.tubeFeedingProductsId ? { ...r, status: nextStatus } : r)),
    );
    this.api.updateStatus(row.tubeFeedingProductsId, nextStatus, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') this.notify.success(res.message ?? 'Status updated');
        else this.notify.error(res.message ?? 'Something went wrong...!!');
        this.loadList();
      },
      error: () => this.loadList(),
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:154-155/154/246-247/296-297. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
