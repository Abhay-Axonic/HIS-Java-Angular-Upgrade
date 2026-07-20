import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CorrectiveMaintenanceHandoffService } from '../../core/corrective-maintenance-handoff.service';
import { CorrectiveMaintenanceService } from './corrective-maintenance.service';
import { CmCompletionObj, CmRow, CmStore } from './corrective-maintenance.models';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setTotalPages,
  setFirstPage,
  setPreviousPage,
  setNextPage,
  setLastPage,
  setPage,
  getPagesList,
} from '../../core/client-pagination.util';

function emptyCompletionObj(): CmCompletionObj {
  return { completionDate: '', completionTime: '', isComplete: false, completionRemak: '' };
}

function pad(n: number): string {
  return String(n).padStart(2, '0');
}

/** `DD-MM-YYYY HH:mm:ss`, matching legacy's `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`. */
function nowFormatted(): string {
  const d = new Date();
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

/** `YYYY-MM-DD` (html date input value) -> `DD-MM-YYYY`. */
function ymdToDmy(ymd: string): string {
  const [yyyy, mm, dd] = ymd.split('-');
  if (!yyyy || !mm || !dd) return ymd;
  return `${dd}-${mm}-${yyyy}`;
}

/**
 * Round-trips a `DD-MM-YYYY[...]` value the way legacy's
 * `moment(value,"DD-MM-YYYY").toDate()` followed by
 * `moment(...).format('DD-MM-YYYY HH:mm:ss')` does -- the time-of-day is
 * dropped and reset to midnight since the source string never carried one.
 */
function toMidnightDateTime(ddMmYyyy: string | undefined): string {
  if (!ddMmYyyy) return '';
  const datePart = ddMmYyyy.split(' ')[0];
  const [dd, mm, yyyy] = datePart.split('-');
  if (!dd || !mm || !yyyy) return ddMmYyyy;
  return `${dd}-${mm}-${yyyy} 00:00:00`;
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}

/**
 * Rebuild of the legacy `correctiveMaintenance` state -- see
 * correctiveMaintenanceController.js (442 lines) +
 * corrective-maintenance.html (433 lines). No data.moduleId (no resolve
 * block in app.js:10686-10691), real cookieObject guard only -- matches
 * corrective-maintenance-worklist, this batch's other Asset Management
 * screen with no module gate.
 *
 * Every ng-click in the legacy view is real and wired (confirmed by
 * reading the full controller + view): search, the `updateCorectiveMaintenance`
 * bulk "convert selected Pending rows back to Requested" action (checkbox
 * column + Save button, gated on `counter`), a details/edit navigation with
 * two call sites (`getDetails(info,0)` from the pencil icon when
 * maintenanceStatusId==5, `getDetails(info,1)` from the always-visible eye
 * icon), and three modals: Completion Details, Reopen Details, and a Cancel
 * confirmation. The one exception is view line 153's duplicate `ng-style`
 * attribute (`setColor` twice, the second referencing an undefined
 * `set_color`) -- the HTML parser silently drops the second, redundant,
 * duplicate attribute before Angular ever compiles it, so it's inert and
 * not reproduced here. The Print icon (view line 217-220) has no ng-click
 * at all in legacy -- rendered as a plain, inert icon below, not wired to
 * anything.
 *
 * Bug reproduced faithfully, not fixed: `getCurrentIndex($index)` and
 * `getReopenReqIndex($index)` are invoked from the `ng-repeat` over
 * `pagination.data | filter:searchKeyword` (the paginated, filtered
 * slice), but both handlers then index straight into
 * `$scope.corectiveMaintenanceList` (the full, unpaginated result set) via
 * that same `$index`. On any page after the first, or with a search
 * keyword active, this reads the WRONG row into the completion/reopen
 * modal. Reproduced here identically: `$index` from the `pagination().data`
 * loop is used to index `this.list` directly, not `this.pagination().data`.
 *
 * Simplification, not a behavior change: legacy's checkbox `ng-disabled`
 * expression is `info.maintenanceStatusId != 5 || info.maintenanceStatusId
 * == 6` (view line 153) -- the `== 6` clause is logically dead (whenever
 * status is 6, `!= 5` is already true), so the whole expression is
 * equivalent to `maintenanceStatusId !== 5` alone, used below. TypeScript's
 * control-flow narrowing also flags the original two-literal form as
 * TS2367 ("no overlap") once written out directly, which independently
 * confirms the redundancy.
 *
 * Bug reproduced faithfully, not fixed: `updateCompletionDetails()`'s guard
 * `finalCompletionObj.isComplete == ""` (correctiveMaintenanceController.js:329)
 * relies on JS loose-equality coercion -- `false == ""` is `true`, so this
 * guard actually fires (blocking save with "Please Select Is Complete")
 * whenever the Complete checkbox is left unchecked, not only when the value
 * is literally an empty string. Reproduced below as `isComplete === false`,
 * which is the exact same observable behavior for this boolean-only field.
 */
@Component({
  selector: 'app-corrective-maintenance',
  imports: [FormsModule, RouterLink],
  templateUrl: './corrective-maintenance.html',
  styleUrl: './corrective-maintenance.scss',
})
export class CorrectiveMaintenance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CorrectiveMaintenanceHandoffService);
  private readonly api = inject(CorrectiveMaintenanceService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  storeList = signal<CmStore[]>([]);
  storeId = 0;
  statusId = 0;
  searchKeyword = '';

  /** Full, unpaginated result set -- see the class doc comment re: the buggy `$index` reads. */
  private list: CmRow[] = [];
  pagination = signal<PaginationState<CmRow>>(createPaginationState<CmRow>());

  counter = signal(0);

  // Completion Details modal
  showCompletionModal = signal(false);
  completionObj = signal<CmCompletionObj>(emptyCompletionObj());
  private currentIndex: number | null = null;

  // Reopen Details modal
  showReopenModal = signal(false);
  reopenDate = signal(nowFormatted());
  reopenRemark = signal('');
  reOpenstatus = signal(false);
  private reopenIndex: number | null = null;

  // Cancel confirmation modal
  showCancelModal = signal(false);
  private correctiveMaintenanceIdIndex: number | null = null;

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.getStoreList();
    this.search();
  }

  private getStoreList(): void {
    this.loader.start();
    this.api.getStoreList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.storeList.set(res.listObject ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  search(): void {
    this.loader.start();
    this.api
      .search({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        maintenanceStatusId: this.statusId,
        storeId: this.storeId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdBy: this.userId,
        isWorklist: 0,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          this.list = (res.listObject ?? []).map((row) => ({ ...row, check: false }));
          this.completionObj.set(emptyCompletionObj());
          this.counter.set(0);

          const p = this.pagination();
          p.data = [];
          if (this.list.length > 0) {
            searchData(this.list, p);
          }
          this.pagination.set({ ...p });
        },
        error: () => this.loader.stop(),
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

  setColor(referenceId: number | null | undefined): string {
    return referenceId != null ? 'red' : '';
  }

  // ---- row navigation ----
  getDetails(info: CmRow, editflag: number): void {
    this.handoff.setObject({ ...info, editflag });
    this.router.navigate(['/detailsCorrectiveMaintenance']);
  }

  // ---- bulk "Pending -> Requested" update ----
  getCountOfCheck(checked: boolean): void {
    this.counter.update((c) => (checked ? c + 1 : c - 1));
  }

  updateCorectiveMaintenance(): void {
    const finalSaveList = this.list
      .filter((v) => v.check === true)
      .map((v) => ({
        correctiveMaintenanceId: v.correctiveMaintenanceId,
        maintenanceStatusId: 1,
        updatedDate: nowFormatted(),
        updatedBy: this.userId,
      }));

    if (finalSaveList.length > 0) {
      this.loader.start();
      this.api.updatePendingToNewRequest(finalSaveList).subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.loader.stop();
            this.search();
            this.notify.success(res.message ?? '');
          } else if (res.status === 'error') {
            this.loader.stop();
            this.notify.error(res.message ?? '');
          }
        },
        error: () => this.loader.stop(),
      });
    } else {
      this.notify.warning('Please Select atleast one request');
    }
  }

  // ---- cancel request ----
  cancelRequest(correctiveMaintenanceId: number | undefined): void {
    this.correctiveMaintenanceIdIndex = correctiveMaintenanceId ?? null;
    this.showCancelModal.set(true);
  }

  closeCancelModal(): void {
    this.showCancelModal.set(false);
  }

  finalCancelReq(): void {
    // Legacy's modal buttons carry `data-dismiss="modal"` alongside their
    // ng-click, so the dialog closes immediately on click, independent of
    // the async response -- reproduced here by closing before the request
    // even resolves.
    this.showCancelModal.set(false);
    this.loader.start();
    const data = {
      correctiveMaintenanceId: this.correctiveMaintenanceIdIndex,
      maintenanceStatusId: 6,
      updatedDate: nowFormatted(),
      updatedBy: this.userId,
    };
    this.api.cancelRequest(data).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.loader.stop();
          this.search();
          this.notify.success(res.message ?? '');
        } else if (res.status === 'error') {
          this.loader.stop();
          this.notify.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- completion details ----
  getCurrentIndex(index: number): void {
    this.completionObj.set(emptyCompletionObj());
    this.currentIndex = index;
    // Buggy-but-faithful: `index` comes from the paginated/filtered table,
    // `this.list` is the full result set -- see class doc comment.
    const info = this.list[index];
    if (info?.isComplete) {
      this.completionObj.set({
        completionDate: info.completionDate ?? '',
        completionTime: info.completionTime ?? '',
        isComplete: info.isComplete ?? false,
        completionRemak: info.completionRemak ?? '',
      });
    }
    this.showCompletionModal.set(true);
  }

  /** Row backing the open Completion Details modal, so the template can read its `isComplete` disabled-state. */
  currentCompletionRow(): CmRow | undefined {
    return this.currentIndex != null ? this.list[this.currentIndex] : undefined;
  }

  closeCompletionModal(): void {
    this.showCompletionModal.set(false);
  }

  updateCompletionField<K extends keyof CmCompletionObj>(field: K, value: CmCompletionObj[K]): void {
    this.completionObj.update((c) => ({ ...c, [field]: value }));
  }

  updateCompletionDetails(): void {
    this.loader.start();
    const row = this.currentCompletionRow();
    const c = this.completionObj();
    const finalCompletionDate = c.completionDate ? ymdToDmy(c.completionDate) + ' 00:00:00' : '';

    if (!finalCompletionDate) {
      this.loader.stop();
      this.notify.warning('Please Select Completion Date');
      return;
    }
    if (c.completionTime === '') {
      this.loader.stop();
      this.notify.warning('Please Select Completion Time');
      return;
    }
    // See class doc comment: legacy's `isComplete == ""` guard actually
    // fires whenever the checkbox is left unchecked.
    if (c.isComplete === false) {
      this.loader.stop();
      this.notify.warning('Please Select Is Complete');
      return;
    }

    const finalCompletionObj = {
      completionDate: finalCompletionDate,
      completionTime: c.completionTime,
      isComplete: c.isComplete,
      completionRemak: c.completionRemak,
      updatedBy: this.userId,
      updatedDate: nowFormatted(),
      correctiveMaintenanceId: row?.correctiveMaintenanceId,
    };

    this.api.completeByRequester(finalCompletionObj).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.loader.stop();
          this.notify.success(res.message ?? '');
          this.search();
        } else if (res.status === 'error') {
          this.loader.stop();
          this.notify.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- reopen ----
  getReopenReqIndex(index: number): void {
    this.reopenIndex = index;
    // Same buggy-but-faithful index source as getCurrentIndex() above.
    const info = this.list[index];
    this.reOpenstatus.set(info?.reOpen ?? false);
    this.reopenRemark.set(info?.remark ?? '');
    this.showReopenModal.set(true);
  }

  closeReopenModal(): void {
    this.showReopenModal.set(false);
  }

  reopenRequest(): void {
    const info = this.reopenIndex != null ? this.list[this.reopenIndex] : undefined;
    if (!info) return;

    const data = {
      itemId: info.itemId,
      storeId: info.storeId,
      requestTypeId: info.requestTypeId,
      requestCriticalityId: info.requestCriticalityId,
      serialNumber: info.serialNumber,
      isDown: info.isDown,
      requestDateTime: nowFormatted(),
      downDateTime: toMidnightDateTime(info.downDateTime),
      resolutionExpectedDateTime: toMidnightDateTime(info.resolutionExpectedDateTime),
      requestDetails: info.requestDetails,
      maintenanceStatusId: 5, // pending = 5, reopen = 7
      remark: this.reopenRemark(),
      createdBy: this.userId,
      createdDate: nowFormatted(),
      status: 'A',
      updatedBy: this.userId,
      updatedDate: nowFormatted(),
      unitId: this.unitId,
      organizationId: this.organizationId,
      referenceId: info.correctiveMaintenanceId,
      batchId: info.batchId,
      referenceLogNumber: info.callLogNumber,
      downTime: info.downTime,
      resolutionExpectedTime: info.resolutionExpectedTime,
    };

    this.loader.start();
    this.api.saveCorrectiveMaintenance(data).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.search();
          this.reopenRemark.set('');
          this.loader.stop();
          this.notify.success(res.message ?? '');
        } else if (res.status === 'error') {
          this.loader.stop();
          this.notify.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}
