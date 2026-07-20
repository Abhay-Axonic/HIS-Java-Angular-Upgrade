import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CorrectiveMaintenanceReturnWorklistService } from './corrective-maintenance-return-worklist.service';
import { AcceptReturnObj, CmrwRow, DetailsOfCM, ReasonListItem, RejectReturnObj, StoreListItem } from './corrective-maintenance-return-worklist.models';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setPage,
  setFirstPage,
  setPreviousPage,
  setNextPage,
  setLastPage,
  getPagesList,
} from '../../core/client-pagination.util';

/**
 * Rebuild of the legacy `correctiveMaintenanceReturnWorklist` state --
 * self-registered inside correctiveMaintenanceReturnWorklistController.js's
 * own `config($stateProvider)` block (ctrl:317-324), not app.js. No
 * data.moduleId (no resolve block), real cookieObject guard only.
 * `controllerAs` is NOT used in legacy (plain `$scope`).
 *
 * PARTIALLY-DEAD verdict: a real, live screen (search/list, detail lookup,
 * accept-save, reject-save all wired to real HTTP endpoints), with ONE
 * confirmed reproducible crash bug -- see `saveAcceptReject()` below.
 *
 * Sharpened Step 1 checklist applied:
 * - Structural-surprise check: unlike the sibling `correctiveMaintenanceWorklist`
 *   screen (two of its three `<tr ng-repeat>` blocks are HTML-commented-out
 *   dead tables), THIS view has only one repeat block and it's fully live
 *   (view:201). No dead table to skip here.
 * - Race check: store list, reason list, and worklist search all fire
 *   unsequenced on load (ctrl:99, ctrl:105-111, ctrl:182), but they
 *   populate independent, non-overlapping scope vars -- confirmed NOT a
 *   race/data-clobber bug (unlike the sibling's finding). Fired the same
 *   way here, no sequencing added.
 * - Confirmed reproducible crash bug, ctrl:286: inside `saveAcceptReject`
 *   (the reject-modal's Save handler), the "note is blank" branch does
 *   `$scope.corectiveMaintenanceReturnWorklist[index].isAcceptOrReject = 'N'`
 *   where `index` is never declared anywhere in `saveAcceptReject`'s own
 *   scope -- only `$scope.currentInx` is set, by the SEPARATE function
 *   `showRejecPopup` (ctrl:262). Reading the undeclared `index` identifier
 *   throws `ReferenceError: index is not defined` under this file's own
 *   "use strict" (ctrl:2). Exact trigger condition (traced ctrl:270-313):
 *   the reject modal's Save button only reaches this line when
 *   `rejectReasonId` is NON-empty (a reason WAS selected) AND `noteReject`
 *   IS empty (the note was left blank) -- i.e. only ONE of the two
 *   validation branches crashes. The sibling branch (no reason selected)
 *   uses `$scope.currentInx`, which IS declared, and does NOT crash. The
 *   third branch (both filled) is the real save path and also does not
 *   crash. TypeScript can't express "read of an undeclared identifier"
 *   the same way, so per the linen-issue-acceptance-details.ts convention
 *   the identical OBSERVABLE crash is reproduced via a direct throw at the
 *   equivalent point -- see `saveAcceptReject()`. NOT fixed, no index
 *   value guessed.
 * - `getReplaceDetails()` (ctrl:113-133, calls `getreplaceItemById`) is
 *   defined on $scope but never invoked from anywhere in the view --
 *   confirmed dead code (grepped the full view for `getReplaceDetails`,
 *   `replaceDetailsList`, `replaceBtn`, `returnBtn`: zero references
 *   outside the controller). Not rebuilt, matching this project's
 *   established treatment of confirmed-unreachable code.
 * - The three-state toggle (`ng-toggle tri-toggle`, view:223-226,
 *   directive at resources/assets/js/directive/numbers.directive.js:132)
 *   cycles A -> R -> N -> A... on click (`scope.toggle()`,
 *   numbers.directive.js:412-425), and its outer `ng-click="showRejecPopup(info,$index)"`
 *   fires in the SAME click after the inner toggle's two-way-bound value
 *   has already synced (both handlers run inside the same bubble-phase
 *   click, inner-to-outer, and AngularJS's nested `$apply` digest
 *   resolves before the outer handler runs) -- so `showRejecPopup` always
 *   observes the freshly-cycled value. Reproduced as `toggleAcceptReject()`
 *   below: cycle the value, then apply the same A -> open accept modal /
 *   R -> open reject modal / N -> no-op branching as `showRejecPopup`
 *   (ctrl:261-268). Disabling for completed/rejected rows
 *   (`replaceStatusId == 2 || 3`, view:220) is CSS `pointer-events: none`
 *   in legacy (param.common.master.css `.disabled`), not a JS guard --
 *   reproduced the same way (see .scss), not as an extra TS-level guard.
 * - Legacy applies AngularJS's built-in `filter:searchKeyword` directly to
 *   `pagination.data` (the already-paginated current page, view:201) --
 *   a real, LIVE client-side substring filter layered on top of
 *   PaginationService's own paging, unlike the sibling screen's rebuild
 *   (which left an equivalent `searchKeyword` binding unwired). Reproduced
 *   here via `filteredRows()`, using the same JSON-stringify substring
 *   approximation as `client-pagination.util.ts`'s own `searchData` for
 *   consistency -- disclosed as a reasonable approximation of a search-box
 *   convenience feature, not business logic.
 */
@Component({
  selector: 'app-corrective-maintenance-return-worklist',
  imports: [FormsModule],
  templateUrl: './corrective-maintenance-return-worklist.html',
  styleUrl: './corrective-maintenance-return-worklist.scss',
})
export class CorrectiveMaintenanceReturnWorklist {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CorrectiveMaintenanceReturnWorklistService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  readonly fromDateMax = formatYmd(new Date());
  readonly fromDateMin = formatYmd(daysAgo(180));
  readonly toDateMin = formatYmd(new Date());

  statusId = 0;
  storeId = 0;
  searchKeyword = '';

  storeList = signal<StoreListItem[]>([]);
  reasonList = signal<ReasonListItem[]>([]);

  private worklist: CmrwRow[] = [];
  pagination = signal<PaginationState<CmrwRow>>(createPaginationState<CmrwRow>());

  showCompletionModal = signal(false);
  detailsOfCMObj = signal<DetailsOfCM>({});

  showAcceptModal = signal(false);
  acceptReturnObj: AcceptReturnObj = {};

  showRejectModal = signal(false);
  rejectReturnObj: RejectReturnObj = {};
  rejectReasonId: number | '' = '';
  noteReject = '';

  private currentInx: number | '' = '';

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
    this.getReasonList();
    this.search();
  }

  private getStoreList(): void {
    this.loader.start();
    this.api.getDetailedStoreList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.storeList.set(res.listObject ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  private getReasonList(): void {
    this.api
      .getActiveReasonMasterList({ unitId: this.unitId, organizationId: this.organizationId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.reasonList.set(res.listObject ?? []);
        },
      });
  }

  search(): void {
    this.worklist = [];
    this.loader.start();
    this.api
      .getReplaceDetailOnReturnByMultipleCrit({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        replaceStatusId: this.statusId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        storeId: this.storeId,
        createdBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          for (const value1 of res.listObject ?? []) {
            if (value1.replaceStatusId == 1) value1.isAcceptOrReject = 'N';
            if (value1.replaceStatusId == 2) value1.isAcceptOrReject = 'A';
            if (value1.replaceStatusId == 3) value1.isAcceptOrReject = 'R';
            this.worklist.push(value1);
          }
          const p = this.pagination();
          p.data = [];
          if (this.worklist.length > 0) {
            searchData(this.worklist, p);
          }
          this.pagination.set({ ...p });
          this.loader.stop();
        },
        error: () => this.loader.stop(),
      });
  }

  /** Layered on top of pagination.data, matching legacy's live
   *  `filter:searchKeyword` -- see class doc comment. */
  filteredRows(): CmrwRow[] {
    const rows = this.pagination().data;
    if (!this.searchKeyword) return rows;
    const term = this.searchKeyword.toLowerCase();
    return rows.filter((row) => JSON.stringify(row).toLowerCase().includes(term));
  }

  setPageSize(): void {
    const p = this.pagination();
    searchData(this.worklist, p);
    this.pagination.set({ ...p });
  }

  getDetailsOfCM(info: CmrwRow): void {
    this.loader.start();
    this.api
      .getDetailsOfCM({
        correctiveMaintenanceScheduleId: info.correctiveMaintenanceSchedulingId,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          const obj: DetailsOfCM = { ...(res.object ?? {}) };
          obj.newSerialNo = info.newSerialNo;
          obj.requestedSerialNo = info.requestedSerialNo;
          obj.underObservation = !obj.isCompleted;
          this.detailsOfCMObj.set(obj);
          this.loader.stop();
          this.showCompletionModal.set(true);
        },
        error: () => this.loader.stop(),
      });
  }

  closeCompletionModal(): void {
    this.showCompletionModal.set(false);
  }

  private getAcceptDetils(info: CmrwRow): void {
    this.acceptReturnObj = {
      newSerialNo: info.newSerialNo,
      requestedSerialNo: info.requestedSerialNo,
      itemName: info.itemName,
      assetReplacementId: info.assetReplacementId,
      itemCode: info.itemCode,
      assetName: info.assetName,
    };
    this.showAcceptModal.set(true);
  }

  closeAcceptModal(): void {
    this.showAcceptModal.set(false);
  }

  saveAcceptance(): void {
    this.loader.start();
    this.api.returnAsset({ assetReplacementId: this.acceptReturnObj.assetReplacementId }).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.acceptReturnObj = {};
          this.showAcceptModal.set(false);
          this.search();
          this.notify.success('success');
        } else {
          this.notify.error('something went wrong ..!!');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  private rejectDetils(info: CmrwRow): void {
    this.rejectReturnObj = { assetReplacementId: info.assetReplacementId };
    this.showRejectModal.set(true);
  }

  closeRejectModal(): void {
    this.showRejectModal.set(false);
  }

  /** ctrl:261-268 -- fires after the tri-state toggle (view:223-226) has
   *  already cycled `info.isAcceptOrReject`; see class doc comment. */
  toggleAcceptReject(info: CmrwRow, index: number): void {
    if (info.isAcceptOrReject === 'A') info.isAcceptOrReject = 'R';
    else if (info.isAcceptOrReject === 'R') info.isAcceptOrReject = 'N';
    else info.isAcceptOrReject = 'A';

    this.currentInx = index;
    if (info.isAcceptOrReject === 'A') {
      this.getAcceptDetils(info);
    } else if (info.isAcceptOrReject === 'R') {
      this.rejectDetils(info);
    }
    this.pagination.set({ ...this.pagination() });
  }

  saveAcceptReject(): void {
    if (this.rejectReasonId === '') {
      this.notify.warning('Please select reason..!!');
      // ctrl:279 -- indexes the FULL worklist array (matches legacy's
      // $scope.corectiveMaintenanceReturnWorklist) with `currentInx`, an
      // $index taken from the paginated+filtered ng-repeat -- those index
      // spaces don't actually line up in legacy either (a pre-existing,
      // separate data-consistency wrinkle, not the crash bug below).
      // Reproduced as-is, not fixed.
      const row = this.worklist[this.currentInx as number];
      if (row) row.isAcceptOrReject = 'N';
      this.pagination.set({ ...this.pagination() });
      return;
    }
    if (this.noteReject === '') {
      this.notify.warning('Please Enter reason note..!!');
      // Faithful crash replication -- see class doc comment. Legacy reads
      // `$scope.corectiveMaintenanceReturnWorklist[index]` where `index`
      // is never declared in this branch (only `currentInx`, set by the
      // separate `showRejecPopup` function, exists) --
      // correctiveMaintenanceReturnWorklistController.js:286. Under
      // "use strict" this throws ReferenceError immediately. TypeScript
      // can't express reading an undeclared identifier, so the identical
      // observable crash is reproduced via a direct throw. Do NOT fix by
      // substituting currentInx.
      throw new ReferenceError('index is not defined');
    }

    this.loader.start();
    this.api
      .rejectAsset({
        assetReplacementId: this.rejectReturnObj.assetReplacementId,
        reasonId: this.rejectReasonId,
        reasonNote: this.noteReject,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.rejectReturnObj = {};
            this.showRejectModal.set(false);
            this.search();
            this.notify.success('success');
          } else {
            this.notify.error('something went wrong ..!!');
          }
        },
        error: () => this.loader.stop(),
      });
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

function daysAgo(n: number): Date {
  const d = new Date();
  d.setDate(d.getDate() - n);
  return d;
}
