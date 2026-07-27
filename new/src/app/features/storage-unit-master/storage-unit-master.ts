import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { StorageUnitMasterService } from './storage-unit-master.service';
import { STORAGE_UNIT_CODE_PATTERN, StorageUnitForm, StorageUnitRow, emptyStorageUnitForm } from './storage-unit-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/PharmacyInventory/storage-unit-master.html
 * + resources/assets/js/controller/master/pharmacy/storageUnitMasterController.js
 * (291 lines, `StorageController.Controller`, self-registering via its
 * own `.config($stateProvider)`). State `storageunitMaster` has a
 * `resolve` block gating on module id **470** -- confirmed directly
 * from the live route config.
 *
 * Modern REST-style backend (`StorageUnitController.java`,
 * `ResponseEntity`), not the old `{status,message,listObject,object}`
 * envelope -- see storage-unit-master.service.ts.
 *
 * ================================================================
 * ONE Save button handles BOTH create and update -- unlike every
 * other screen in this batch/project (no separate saveBtnFlag/
 * updateBtnFlag-gated buttons, no title change)
 * ================================================================
 * `SaveStorageUnits()` (ctrl:36-44) branches internally on whether
 * `self.model.id` is set. The view's Save button (view:75-80) is a
 * single, always-visible element with a fixed "Save" title -- never
 * relabeled "Update". Reproduced identically: `save()` below handles
 * both paths, button text never changes.
 *
 * ================================================================
 * Confirm modal fires on EVERY Save click, unconditionally -- no
 * popUpFlag-style gate exists on this screen at all
 * ================================================================
 * The Save link uses native Bootstrap `data-toggle="modal"` (view:75),
 * not an Angular-side flag -- confirmed by reading the full controller,
 * no `popUpFlag`/`showModal` reference anywhere. The modal opens on
 * every click regardless of field validity (the button's
 * `ng-class="{disabled: ...}"` is purely a visual CSS class, not a real
 * HTML/Angular disable) -- validation only happens after confirming,
 * inside `SaveStorageUnits()`. This structurally cannot have the
 * never-rearms bug this batch's other screens have -- reproduced as an
 * unconditional `showConfirmModal.set(true)` on every `save()` call,
 * both for create AND edit (since there's only one button/flow).
 *
 * ================================================================
 * Duplicate-check is 100% client-side, scanning the in-memory list --
 * confirmed by reading the full DAO/Service (no server-side uniqueness
 * query exists), faithfully reproduced, not hardened server-side
 * ================================================================
 * `ValidateCode()`/`ValidateCodeForUpdate()` (ctrl:121-153) loop
 * `self.model.storageUnitList` in JS. Reproduced via the same
 * client-side scan against `list()`, excluding the row's own id on
 * update. A real gap (not enforced server-side) but faithful to
 * legacy's actual behavior.
 *
 * ================================================================
 * Deactivating a Storage Unit still assigned to an Item is BLOCKED
 * server-side -- a real referential-integrity guard, not a bug
 * ================================================================
 * `StorageUnitService.update` (service:83-130): if the incoming status
 * is `false` and `getStorageUnitCountFromItem(id) > 0`, the service
 * forces the status back to `true`, sets message "Storage Unit is
 * assigned to Items", and throws -- surfaced to the frontend as a
 * non-2xx response. `ToggleStatus` (ctrl:96-119) specifically checks
 * `err.status == 406` to revert the optimistic checkbox flip and show
 * the real server message; any OTHER error does NOT revert the
 * checkbox (confirmed by source read -- likely an oversight in legacy,
 * reproduced as-is, not corrected). Not independently live-reproduced
 * this session (would require seeding an Item referencing this unit);
 * confirmed at the source level only.
 *
 * ================================================================
 * `status` is a real BOOLEAN on this screen, not 'A'/'I' like every
 * other screen in this batch/project.
 * ================================================================
 *
 * Scoped by organizationId ONLY -- no unitId anywhere in this screen's
 * API (unlike every sibling screen), confirmed via the controller's
 * URL mapping (`/{orgId}` and `/{orgId}/{id}`, no unit segment).
 *
 * Edit pencil is visually dimmed for inactive rows
 * (`ng-class="{disabled: !storageUnit.status}"` on the icon only, not
 * the containing `<a>`) but the click handler is NOT actually blocked
 * -- reproduced as a cosmetic-only dim, edit still works on inactive rows.
 *
 * Pagination + search are both client-side (`PaginationService`,
 * fetches the full list once) -- reproduced as a single unpaginated
 * fetch with local filter+slice, same shape as `dietUnitMater` (batch 22).
 */
@Component({
  selector: 'app-storage-unit-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './storage-unit-master.html',
  styleUrl: './storage-unit-master.scss',
})
export class StorageUnitMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(StorageUnitMasterService);

  private organizationId = 0;
  private userId = 0;

  form = signal<StorageUnitForm>(emptyStorageUnitForm());
  isEdit = signal(false);
  isCodeInvalid = signal(false);
  isUnitNameInvalid = signal(false);

  showConfirmModal = signal(false);

  list = signal<StorageUnitRow[]>([]);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  filteredRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => [r.code, r.unitName].join(' ').toLowerCase().includes(keyword));
  });

  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredRows().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  displayedRows = computed(() => {
    const start = (this.currentPage() - 1) * this.pageSize();
    return this.filteredRows().slice(start, start + this.pageSize());
  });

  isSaveDisabled = computed(() => !this.form().code || !this.form().unitName);

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  private loadList(): void {
    this.loader.start();
    this.api.getAll(this.organizationId).subscribe({
      next: (rows) => {
        this.loader.stop();
        this.list.set(rows ?? []);
      },
      error: () => this.loader.stop(),
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  updateField<K extends keyof StorageUnitForm>(field: K, value: StorageUnitForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
    if (field === 'code') this.isCodeInvalid.set(false);
    if (field === 'unitName') this.isUnitNameInvalid.set(false);
  }

  // Confirm modal fires unconditionally, matching legacy's native
  // Bootstrap data-toggle -- see class doc.
  save(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // Mirrors SaveStorageUnits (ctrl:36-44) -- validates only after
  // confirming, then branches create vs update on `form().id`.
  confirmSave(): void {
    this.showConfirmModal.set(false);
    if (!this.validate()) return;
    const f = this.form();
    if (!f.id) {
      this.doCreate();
    } else {
      this.doUpdate();
    }
  }

  // Mirrors ValidateStorageUnit + ValidateCode/ValidateCodeForUpdate (ctrl:121-241).
  private validate(): boolean {
    const f = this.form();
    let valid = true;
    if (!f.code) {
      this.isCodeInvalid.set(true);
      valid = false;
    }
    if (!f.unitName) {
      this.isUnitNameInvalid.set(true);
      valid = false;
    }
    if (!valid) return false;

    const duplicate = this.list().some((r) => r.code === f.code && r.id !== f.id);
    if (duplicate) {
      this.notify.error('Duplicate code');
      return false;
    }
    if (!STORAGE_UNIT_CODE_PATTERN.test(f.code)) {
      this.notify.error('Code can only be alpha numeric with single hypen.');
      return false;
    }
    return true;
  }

  private doCreate(): void {
    const f = this.form();
    this.loader.start();
    this.api.create(this.organizationId, { code: f.code, unitName: f.unitName, status: true }).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.update((rows) => [...rows, { id: res.id, code: f.code, unitName: f.unitName, status: true }]);
        this.notify.success(res.message ?? 'Storage Unit Inserted Successfully');
        this.clearFields();
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Something went wrong');
      },
    });
  }

  private doUpdate(): void {
    const f = this.form();
    this.loader.start();
    this.api.update(this.organizationId, f.id as number, { code: f.code, unitName: f.unitName }).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.update((rows) => rows.map((r) => (r.id === f.id ? { ...r, code: f.code, unitName: f.unitName } : r)));
        this.notify.success(res.message ?? 'Storage Unit Updated Successfully');
        this.clearFields();
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Something went wrong');
      },
    });
  }

  // Mirrors ClearFields (ctrl:214-222).
  clearFields(): void {
    this.form.set(emptyStorageUnitForm());
    this.isCodeInvalid.set(false);
    this.isUnitNameInvalid.set(false);
    this.isEdit.set(false);
  }

  clearSearch(): void {
    this.searchKeyword.set('');
    this.clearFields();
  }

  // Mirrors PopulateStorageUnit (ctrl:77-93) -- real GET by id, despite
  // the row already being in `list()` client-side.
  edit(row: StorageUnitRow): void {
    this.loader.start();
    this.api.getById(this.organizationId, row.id).subscribe({
      next: (res) => {
        this.loader.stop();
        this.form.set({ id: res.id, code: res.code, unitName: res.unitName });
        this.isEdit.set(true);
      },
      error: () => this.loader.stop(),
    });
  }

  // Mirrors ToggleStatus (ctrl:96-119) -- optimistic native checkbox
  // flip; on a 406 (blocked -- unit assigned to items), revert and show
  // the real server message; any other error does NOT revert the
  // checkbox, matching legacy's own apparent oversight (see class doc).
  toggleStatus(row: StorageUnitRow): void {
    const nextStatus = !row.status;
    this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    if (!nextStatus) {
      this.clearFields();
    }
    this.loader.start();
    this.api.update(this.organizationId, row.id, { status: nextStatus }).subscribe({
      next: () => {
        this.loader.stop();
        this.notify.success('Status changed successfully');
      },
      error: (err: HttpErrorResponse) => {
        this.loader.stop();
        if (err.status === 406) {
          this.notify.error(err.error?.message ?? 'Storage Unit is assigned to Items');
          this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: true } : r)));
        } else {
          this.notify.error('Something Went Wrong');
        }
      },
    });
  }
}
