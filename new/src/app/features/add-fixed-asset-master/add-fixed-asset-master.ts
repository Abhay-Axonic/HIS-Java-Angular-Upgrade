import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { ReportService } from '../../core/report.service';
import { AddFixedAssetMasterService } from './add-fixed-asset-master.service';
import { AfamCancelReason, AfamOption, AfamRow, AfamStatusMasterEntry } from './add-fixed-asset-master.models';
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

const PAGE_SIZE_OPTIONS = ['10', '25', '50', '100'] as const;

const ALL_STORE: AfamOption = { id: 0, store: 'All' };
const ALL_STATUS: AfamOption = { id: 0, status: 'All' };

/**
 * Rebuild of the legacy `addfixedAssetMaster` state -- see
 * addFixedAssetMasterController.js (398 lines) + template
 * add-fixed-asset-master.html (320 lines). Real cookieObject guard, real
 * module-access resolve block (moduleId 220).
 *
 * NAMING TRAP: despite the state/component name, this is NOT a create
 * form. It's the "Add Fixed Asset Opening Balance" search/list/bulk-approve
 * /cancel screen (confirmed live via screenshot: Date From/Till filters
 * defaulting to today, Store/Status dropdowns, Add New button, table of
 * OPB No/OPB Date/Store/Requestor/Status(n)/Action taken, "No Record
 * Found" on empty). The actual create/edit form is a separate, much
 * larger, NOT-yet-built screen (`addNewfixedAssetMaster`) -- Edit and
 * "Add New" both target it in legacy (`NavigateToEdit`/`NavigateToAddNew`
 * -> `$state.go('addNewfixedAssetMaster')`); since that destination
 * doesn't exist yet in this rebuild, both show a "not yet available"
 * toast instead of navigating (same message/wording as
 * dashboard-info.ts's `goBack()`). Details (`NavigateToDetails`) is a
 * real, already-built sibling (`detailsFixedAssetMaster`) and does
 * navigate for real, via the same `commonDetailService`/
 * `CommonDetailHandoffService` id-handoff slot that screen already reads
 * from.
 *
 * Bug faithfully reproduced: `isEdit()`/`isCancel()`
 * (addFixedAssetMasterController.js:187-205) are if/else-if chains with
 * NO trailing `else` -- for any `status` value outside
 * `{Pending, New, Closed, Cancelled}`, both fall through returning
 * `undefined`. Legacy's `ng-class="{disabled: vm.model.isEdit(fixed)}"`
 * treats that `undefined` as falsy, so the Edit/Cancel icons render
 * ENABLED (not safely disabled) for any unrecognized status -- reproduced
 * below by keeping the exact same if/else-if-only shape (no default
 * branch) and binding `[class.disabled]` directly to the `boolean |
 * undefined` result, so `undefined` likewise fails to apply the
 * `disabled` class.
 *
 * Payload note: the completed Step 0/1 audit's paraphrase said the search
 * payload includes `organizationId`, but the actual controller
 * (addFixedAssetMasterController.js:135-142) sends `fromDate/toDate/
 * storeId/statusId/unitId/userId` -- no `organizationId`, `userId`
 * instead. Followed the real source here, not the paraphrase (see this
 * project's "verify before claiming parity" standing note). The
 * `approve` payload DOES include `organizationId` (ctrl:238-245),
 * confirmed against source.
 */
@Component({
  selector: 'app-add-fixed-asset-master',
  imports: [FormsModule, DatePipe],
  templateUrl: './add-fixed-asset-master.html',
  styleUrl: './add-fixed-asset-master.scss',
})
export class AddFixedAssetMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly report = inject(ReportService);
  private readonly api = inject(AddFixedAssetMasterService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  // ---- filter bar (mirrors self.model.fromDate/toDate/store/status) ----
  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  readonly fromDateMin = formatYmd(subtractDays(new Date(), 180));
  readonly todayYmd = formatYmd(new Date());

  storeList = signal<AfamOption[]>([ALL_STORE]);
  statusList = signal<AfamOption[]>([ALL_STATUS]);
  selectedStore = signal<AfamOption>(ALL_STORE);
  selectedStatus = signal<AfamOption>(ALL_STATUS);

  // ---- global lookups (mirrors StatusService/cancelReasonService) ----
  statusMasterList = signal<AfamStatusMasterEntry[]>([]);
  reasons = signal<AfamCancelReason[]>([]);

  // ---- results + client-side pagination (mirrors self.model.fixedAssetData + PaginationService) ----
  private fixedAssetData: AfamRow[] = [];
  pagination = signal<PaginationState<AfamRow>>(createPaginationState<AfamRow>());
  searchKeyword = '';
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  // ---- row selection (mirrors SelectAllRequests/SelectFixedAssetRequest/SelectedRequestIds) ----
  selectedIds = signal<Set<number>>(new Set());
  selectAllRows = signal(false);

  // ---- confirm/approve modal (mirrors OpenPopup('Update', 'confirmSave', {})) ----
  showConfirmModal = signal(false);

  // ---- cancel modal (mirrors OpenPopup('Cancel', 'cancelFixedAssetReqest', data)) ----
  showCancelModal = signal(false);
  cancelReason = signal<number | undefined>(undefined);
  rejectNote = signal('');
  isReasonValid = signal(false);
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

    this.api.getCancelReasons().subscribe({ next: (r) => this.reasons.set(r) });
    this.api.getStatusMaster().subscribe({ next: (s) => this.statusMasterList.set(s) });
    this.populateDropdowns();
  }

  private populateDropdowns(): void {
    this.loader.start();
    this.api.getDropdowns(this.unitId, this.organizationId, this.userId).subscribe({
      next: (res) => {
        this.storeList.set([ALL_STORE, ...(res.store ?? [])]);
        this.statusList.set([ALL_STATUS, ...(res.status ?? [])]);
        this.loader.stop();
        // Faithful replication of PopulateDropdown() -> FixedAssetSearch(): the
        // table populates on load with no user action needed.
        this.search();
      },
      error: () => this.loader.stop(),
    });
  }

  private validateDate(): boolean {
    if (this.fromDate() > this.toDate()) {
      this.notify.error('From Date should lesser than To Date');
      return false;
    }
    return true;
  }

  search(): void {
    if (!this.validateDate()) return;

    this.loader.start();
    this.api
      .search({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        storeId: this.selectedStore().id || undefined,
        statusId: this.selectedStatus().id || undefined,
        unitId: this.unitId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.fixedAssetData = (res ?? []).map((row) => ({ ...row, isSelected: false }));
          const p = this.pagination();
          p.data = [];
          if (this.fixedAssetData.length > 0) searchData(this.fixedAssetData, p);
          this.pagination.set({ ...p });
          this.selectedIds.set(new Set());
          this.selectAllRows.set(false);
          this.loader.stop();
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- row eligibility (mirrors isDisable/isEdit/isCancel) ----
  isDisableRow(row: AfamRow): boolean {
    return row.status !== 'New';
  }

  /** Faithful port of legacy's `isEdit` -- see class doc comment for the
   * deliberately-preserved missing-else bug. */
  isEdit(row: AfamRow): boolean | undefined {
    if (row.status === 'Pending') {
      return true;
    } else if (row.status === 'New') {
      return false;
    } else if (row.status === 'Closed' || row.status === 'Cancelled') {
      return true;
    }
    return undefined;
  }

  /** Faithful port of legacy's `isCancel` -- same missing-else bug. */
  isCancel(row: AfamRow): boolean | undefined {
    if (row.status === 'Pending') {
      return false;
    } else if (row.status === 'New') {
      return false;
    } else if (row.status === 'Closed' || row.status === 'Cancelled') {
      return true;
    }
    return undefined;
  }

  // ---- selection (mirrors SelectAllRequests/SelectFixedAssetRequest) ----
  toggleSelectAll(): void {
    const next = new Set<number>();
    if (this.selectAllRows()) {
      for (const row of this.fixedAssetData) {
        if (!this.isDisableRow(row)) {
          row.isSelected = true;
          next.add(row.id);
        }
      }
    } else {
      for (const row of this.fixedAssetData) row.isSelected = false;
    }
    this.selectedIds.set(next);
  }

  toggleSelect(row: AfamRow): void {
    const next = new Set(this.selectedIds());
    if (row.isSelected) next.add(row.id);
    else next.delete(row.id);
    this.selectedIds.set(next);
    this.selectAllRows.set(this.fixedAssetData.every((r) => this.isDisableRow(r) || r.isSelected));
  }

  private lookupStatusId(name: string): number | undefined {
    return this.statusMasterList().find((s) => s.status === name)?.id;
  }

  // ---- approve flow (mirrors OpenPopup('Update', ...) + UpdateFixedAssetRequest) ----
  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmApprove(): void {
    // Mirrors legacy's Ok button (`data-dismiss="modal"` fires regardless of
    // validation outcome) -- modal closes either way, see class doc comment.
    this.showConfirmModal.set(false);
    if (this.selectedIds().size === 0) {
      this.notify.error('Select fixed asset request to approve');
      return;
    }

    const statusId = this.lookupStatusId('Closed');
    this.loader.start();
    this.api.approve(Array.from(this.selectedIds()), statusId ?? 0, this.userId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        this.selectedIds.set(new Set());
        this.selectAllRows.set(false);
        this.loader.stop();
        this.notify.success(res.message ?? 'Approved successfully');
        this.search();
      },
      error: (err) => {
        this.loader.stop();
        this.notify.error(err?.error?.message ?? 'Approve failed');
      },
    });
  }

  // ---- cancel flow (mirrors OpenPopup('Cancel', ...) + UpdateFixedAssetRequest) ----
  openCancelModal(row: AfamRow): void {
    this.cancelTargetId = row.id;
    this.isReasonValid.set(false);
    this.rejectNote.set('');
    this.cancelReason.set(undefined);
    this.showCancelModal.set(true);
  }

  closeCancelModal(): void {
    this.showCancelModal.set(false);
  }

  onCancelReasonChange(reasonId: number): void {
    this.cancelReason.set(reasonId);
    this.isReasonValid.set(false);
  }

  confirmCancel(): void {
    if (!this.cancelReason()) {
      this.isReasonValid.set(true);
      return;
    }
    if (this.cancelTargetId == null) return;

    const statusId = this.lookupStatusId('Closed');
    this.loader.start();
    this.api.cancel(this.cancelTargetId, statusId ?? 0, this.cancelReason(), this.rejectNote(), this.userId).subscribe({
      next: (res) => {
        this.selectedIds.set(new Set());
        this.closeCancelModal();
        this.rejectNote.set('');
        this.cancelReason.set(undefined);
        this.loader.stop();
        this.notify.success(res.message ?? 'Cancelled successfully');
        this.search();
      },
      error: (err) => {
        this.loader.stop();
        this.notify.error(err?.error?.message ?? 'Cancel failed');
      },
    });
  }

  // ---- print (mirrors printAddfixedAssetMasterReport -> reportService.generate) ----
  printReport(row: AfamRow): void {
    this.report.generate('/api/billing/unit/bill/Report', {
      appName: 'HIS',
      appSecretKey: 'f306b6eccd9d5069d2dc71afe139271a',
      reportDesignFilePath: 'get_asset_OpBalance.rpt',
      reportFormat: 'PDF',
      exportFileName: 'Asset',
      reportParams: [{ paramName: 'asset_id', paramType: 'Integer', paramValue: row.id }],
    });
  }

  // ---- navigation (mirrors NavigateToAddNew/NavigateToEdit/NavigateToDetails) ----
  navigateToAddNew(): void {
    // addNewfixedAssetMaster is not yet built in this rebuild -- see class doc comment.
    this.notify.warning('This section is not yet available in the rebuilt app.');
  }

  navigateToEdit(_row: AfamRow): void {
    // Same not-yet-built destination as Add New (legacy: NavigateToEdit also
    // targets addNewfixedAssetMaster).
    this.notify.warning('This section is not yet available in the rebuilt app.');
  }

  navigateToDetails(row: AfamRow): void {
    this.handoff.setDataId(row.id);
    this.router.navigate(['/detailsFixedAssetMaster']);
  }

  // ---- pagination ----
  onPageSizeChange(size: string): void {
    const p = this.pagination();
    p.pageSize = size;
    setTotalPages(p);
    this.pagination.set({ ...p });
  }

  /** Mirrors the view's `| filter:searchKeyword` on `pagination.data`
   * (add-fixed-asset-master.html:175) -- legacy filters text-search on
   * top of the already-paginated page, not the full result set; kept as-is
   * rather than "fixed" to a full-set filter, since that's genuinely what
   * legacy does. */
  visibleRows(): AfamRow[] {
    const keyword = this.searchKeyword.trim().toLowerCase();
    const data = this.pagination().data;
    if (!keyword) return data;
    return data.filter((row) => JSON.stringify(row).toLowerCase().includes(keyword));
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
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}

function subtractDays(d: Date, days: number): Date {
  const copy = new Date(d);
  copy.setDate(copy.getDate() - days);
  return copy;
}
