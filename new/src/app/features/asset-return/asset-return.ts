import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { AssetReturnService } from './asset-return.service';
import {
  AssetReturnCancelReason,
  AssetReturnOption,
  AssetReturnRow,
  AssetReturnStatusMasterEntry,
} from './asset-return.models';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setFirstPage,
  setPreviousPage,
  setNextPage,
  setLastPage,
  setPage,
  setTotalPages,
  getPagesList,
} from '../../core/client-pagination.util';

const ALL_STORE: AssetReturnOption = { id: 0, store: 'All Stores' };
const ALL_STATUS: AssetReturnOption = { id: 0, status: 'All' };

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}

/**
 * Rebuild of the legacy `assetReturn` state -- see
 * assetReturnController.js (367 lines) + asset-return.html (420 lines).
 * Real module-access resolve block (moduleId 216). Closest structural
 * sibling: asset-return-acceptance.ts (same real workflow's other half) --
 * bulk actions/status lookups/print action pattern borrowed from there and
 * from grn.ts's fuller bulk-approve/bulk-cancel modal implementation
 * (this screen needed both, acceptance.ts's pilot only needed print+details).
 *
 * **Bugs faithfully reproduced here, NOT fixed -- see inline comments at
 * each trigger point for the full explanation:**
 *
 * 1. **Table column swap** (asset-return.html:172-193 header block vs.
 *    :263-266 row block, verified directly against source, not assumed):
 *    the "Returning Store" column header's own sort-icon sets `sortType
 *    = 'fromStore'`, but the row underneath it renders `{{item.toStore}}`.
 *    The "Accepting Store" header two columns over sets `sortType =
 *    'toStore'` but renders `{{item.fromStore}}`. Header labels and their
 *    bound data/sort-key are cross-wired relative to each other -- see
 *    `sortField`/the template's column comments. Do NOT "fix" this to
 *    look more sensible.
 * 2. **Print** (`printReturn`, ctrl:311-327): legacy's `$scope.assetReturn`
 *    calls `reportService.generate(REPORT_API_URI1, data)` where
 *    `REPORT_API_URI1` is never declared/injected anywhere in the
 *    controller (contrast asset-return-acceptance.ts's `printReport()`,
 *    which correctly uses `ReportService` with a real URI) -- every click
 *    throws before any request goes out. Reproduced via an equivalent
 *    throw, not wired to the real `ReportService`.
 * 3. **Cancel modal Save button** (asset-return.html:405): gated by
 *    `ng-disabled="cancelNote == ''"` -- the free-text NOTE field, not the
 *    reason dropdown, even though `ValidateCancelation()` (ctrl:266-273)
 *    is what actually requires a reason to be selected. See
 *    `confirmCancel()`/the cancel modal template.
 *
 * **Add New / Edit** (`AddAssetReturnDetails`/`EditAssetReturnDetails`,
 * ctrl:296-304) both `commonDetailService.setDataId(...)` then
 * `$state.go('addNewAssetReturn')` -- that create/edit form is a separate
 * screen not built in this batch and has no route yet, so navigating
 * would 404. Both still perform the real handoff write (parity with
 * legacy's side effect) but substitute a warning notice for the navigate.
 *
 * **Select-all subtlety, verified against source, easy to get wrong**:
 * `SelectAllRequests`/`SelectAssetReturn` (ctrl:148-206) iterate
 * `self.model.assetReturnData` -- the FULL current search result set --
 * not `pagination.data` (the current page's slice), even though the
 * table itself only ever renders `pagination.data`. "Select all" really
 * does select every eligible row across every page, not just the visible
 * page. Reproduced here via `pagination().filteredData` rather than
 * `pagination().data` in `toggleSelectAll`/`toggleSelect`.
 */
@Component({
  selector: 'app-asset-return',
  imports: [FormsModule, DatePipe],
  templateUrl: './asset-return.html',
  styleUrl: './asset-return.scss',
})
export class AssetReturn {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(AssetReturnService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  // ---- filter bar (mirrors self.model.*) ----
  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  fromStoreList = signal<AssetReturnOption[]>([ALL_STORE]);
  toStoreList = signal<AssetReturnOption[]>([ALL_STORE]);
  statusFilterList = signal<AssetReturnOption[]>([ALL_STATUS]);

  /** Bound to the "Accepting Store" filter (view label order verified:
   * asset-return.html:46 "Accepting Store" wraps the `fromStore` select). */
  fromStore: AssetReturnOption = ALL_STORE;
  /** Bound to the "Returning Store" filter (asset-return.html:62). */
  toStore: AssetReturnOption = ALL_STORE;
  status: AssetReturnOption = ALL_STATUS;
  returnNo = '';

  // ---- global lookups (mirrors StatusService/cancelReasonService) ----
  statusMasterList = signal<AssetReturnStatusMasterEntry[]>([]);
  cancelReasons = signal<AssetReturnCancelReason[]>([]);

  // ---- results + client-side pagination (mirrors PaginationService) ----
  pagination = signal<PaginationState<AssetReturnRow>>(createPaginationState<AssetReturnRow>());

  // ---- row selection (mirrors SelectAllRows/SelectedRequestIds/isItemChecked) ----
  selectAllRows = signal(false);
  isItemChecked = signal(false);
  private selectedIds: number[] = [];

  // ---- column-sort swap bug state (see class doc comment #1) ----
  sortField = signal('');
  sortReverse = signal(false);

  /** Table rows for the current page, sorted per `sortField`/`sortReverse`. */
  sortedRows = computed(() => {
    const field = this.sortField();
    const rows = this.pagination().data;
    if (!field) return rows;
    const reverse = this.sortReverse();
    return [...rows].sort((a, b) => {
      const av = String(a[field] ?? '');
      const bv = String(b[field] ?? '');
      if (av < bv) return reverse ? 1 : -1;
      if (av > bv) return reverse ? -1 : 1;
      return 0;
    });
  });

  // ---- approve confirm modal (mirrors #confirmLogout1) ----
  showApproveModal = signal(false);

  // ---- cancel modal (mirrors #confirmCancel) ----
  showCancelModal = signal(false);
  cancelReason = signal<number | undefined>(undefined);
  cancelNote = signal('');
  private cancelTargetId: number | undefined;

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.api.getStatusMaster().subscribe({ next: (r) => this.statusMasterList.set(r) });
    this.api.getCancelReasons().subscribe({ next: (r) => this.cancelReasons.set(r) });
    this.getDropdowns();
    this.search();
  }

  private getDropdowns(): void {
    this.loader.start();
    this.api.getDropdowns(this.unitId, this.organizationId, this.userId).subscribe({
      next: (res) => {
        this.fromStoreList.set([ALL_STORE, ...(res.fromStore ?? [])]);
        this.toStoreList.set([ALL_STORE, ...(res.toStore ?? [])]);
        this.statusFilterList.set([ALL_STATUS, ...(res.status ?? [])]);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  search(): void {
    this.loader.start();
    this.api
      .search({
        fromStoreId: this.fromStore?.id || undefined,
        toStoreId: this.toStore?.id || undefined,
        returnNo: this.returnNo || undefined,
        statusId: this.status?.id || undefined,
        unitId: this.unitId,
        fromDate: this.fromDate(),
        toDate: this.toDate(),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          const rows = (res ?? []).map((r) => ({ ...r, isSelected: false }));
          const p = this.pagination();
          p.data = [];
          if (rows.length > 0) searchData(rows, p);
          this.pagination.set({ ...p });
          this.selectAllRows.set(false);
          this.isItemChecked.set(false);
          this.selectedIds = [];
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Error');
        },
      });
  }

  onPageSizeChange(size: string): void {
    const p = this.pagination();
    p.pageSize = size;
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

  toggleSort(field: string): void {
    if (this.sortField() === field) this.sortReverse.set(!this.sortReverse());
    else {
      this.sortField.set(field);
      this.sortReverse.set(false);
    }
  }

  /** Only rows with status 'New' are selectable/cancelable (ctrl:175-180). */
  isDisable(row: AssetReturnRow): boolean {
    return row.status !== 'New';
  }

  // ---- selection -- operates on the FULL filtered set, not just the
  // current page; see class doc comment's "select-all subtlety". ----
  toggleSelectAll(): void {
    const rows = this.pagination().filteredData;
    if (this.selectAllRows()) {
      this.selectedIds = [];
      for (const row of rows) {
        if (!this.isDisable(row)) {
          row.isSelected = true;
          this.selectedIds.push(row.id);
        }
      }
    } else {
      this.selectedIds = [];
      for (const row of rows) row.isSelected = false;
    }
    this.recomputeItemChecked();
    this.pagination.set({ ...this.pagination() });
  }

  toggleSelect(row: AssetReturnRow): void {
    if (row.isSelected) {
      this.selectedIds.push(row.id);
    } else {
      const idx = this.selectedIds.indexOf(row.id);
      if (idx > -1) this.selectedIds.splice(idx, 1);
    }
    const rows = this.pagination().filteredData;
    this.selectAllRows.set(rows.every((r) => this.isDisable(r) || !!r.isSelected));
    this.recomputeItemChecked();
  }

  private recomputeItemChecked(): void {
    const rows = this.pagination().filteredData;
    this.isItemChecked.set(rows.some((r) => !!r.isSelected));
  }

  private lookupStatusId(name: string): number | undefined {
    return this.statusMasterList().find((s) => s.status === name)?.id;
  }

  // ---- approve (mirrors ValidateAndUpdate('Update', ...) + ApproveAssetReturn) ----
  openApproveModal(): void {
    if (!this.isItemChecked()) return;
    this.showApproveModal.set(true);
  }

  closeApproveModal(): void {
    this.showApproveModal.set(false);
  }

  confirmApprove(): void {
    const payload = {
      returnId: this.selectedIds,
      statusId: this.lookupStatusId('Pending'),
      approvalStatusId: this.lookupStatusId('Pending Approval'),
      userId: this.userId,
      unitId: this.unitId,
      organizationId: this.organizationId,
    };
    this.loader.start();
    this.api.approve(payload).subscribe({
      next: (res) => {
        this.selectedIds = [];
        this.loader.stop();
        this.notify.success(res.message ?? '');
        this.selectAllRows.set(false);
        this.isItemChecked.set(false);
        this.closeApproveModal();
        this.search();
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Error');
        this.closeApproveModal();
      },
    });
  }

  // ---- cancel (mirrors ValidateAndUpdate('Cancel', ...) + ApproveAssetReturn's else-branch) ----
  openCancelModal(row: AssetReturnRow): void {
    if (this.isDisable(row)) return;
    this.cancelTargetId = row.id;
    this.showCancelModal.set(true);
  }

  closeCancelModal(): void {
    this.showCancelModal.set(false);
    this.cancelReason.set(undefined);
    this.cancelNote.set('');
  }

  /**
   * `ValidateCancelation` (ctrl:266-273): a reason must actually be
   * selected for the request to succeed -- this is enforced here, even
   * though the Save button's OWN disabled state (see the cancel modal's
   * template) is gated on the NOTE field instead, per bug #3 above. Do
   * NOT change the button's gating to match this check; that would fix a
   * bug legacy has, not reproduce it.
   */
  confirmCancel(): void {
    if (!this.cancelReason()) {
      this.notify.error('Please Select Reason to Cancel');
      return;
    }
    if (this.cancelTargetId == null) return;
    const payload = {
      cancelReasonId: this.cancelReason(),
      cancelNote: this.cancelNote(),
      statusId: this.lookupStatusId('Cancelled'),
      userId: this.userId,
    };
    this.loader.start();
    this.api.cancel(this.cancelTargetId, payload).subscribe({
      next: (res) => {
        this.selectedIds = [];
        this.loader.stop();
        this.notify.success(res.message ?? '');
        this.closeCancelModal();
        this.search();
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Error');
      },
    });
  }

  /**
   * Legacy's `AddAssetReturnDetails`/`EditAssetReturnDetails` (ctrl:296-304)
   * -- see class doc comment. Both still write the real handoff id, only
   * the navigate is substituted with a warning.
   */
  addNew(): void {
    this.handoff.setDataId(0);
    this.notify.warning('This section is not yet available in the rebuilt app.');
  }

  editReturn(row: AssetReturnRow): void {
    if (this.isDisable(row)) return;
    this.handoff.setDataId(row.id);
    this.notify.warning('This section is not yet available in the rebuilt app.');
  }

  /** Real navigation -- `AssetReturnDetails(areturn)`, ctrl:306-309. */
  viewDetails(row: AssetReturnRow): void {
    this.handoff.setDataId(row.id);
    this.router.navigate(['/detailsAssetReturn']);
  }

  /**
   * BUG (faithfully reproduced, NOT fixed) -- see class doc comment #2.
   * `REPORT_API_URI1` is never declared anywhere in
   * assetReturnController.js; every real click threw before a request
   * ever went out. Do not wire this to `ReportService`.
   */
  printReturn(_row: AssetReturnRow): void {
    throw new ReferenceError('REPORT_API_URI1 is not defined');
  }
}
