import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { WingMasterService } from './wing-master.service';
import { WingMasterForm, WingMasterRow, emptyWingMasterForm } from './wing-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/wing-master.html +
 * resources/assets/js/controller/adt/unit/wingMasterController.js
 * (`wingMasterController`, no `controllerAs`). moduleId 540. Confirmed a
 * near line-for-line copy of amenityMasterController.js -- same "code +
 * name" master shape as this codebase's own qualification-master/
 * degree-master, which this rebuild otherwise mirrors closely.
 *
 * Confirmed real, working CRUD: save (ctrl:142-195), update (ctrl:228-277),
 * edit-populate `showUpdateBtn` (ctrl:197-225), status toggle `updateStatus`
 * (ctrl:279-313) -- all wired to real endpoints, see wing-master.service.ts.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - Save vs. Update is asymmetric, same family as qualification-master/
 *   degree-master: clicking Save (`saveBtnFlag`, ctrl:142-195) validates,
 *   then opens the shared "Are you sure you want to Save?" confirm modal
 *   before actually POSTing (only the modal's Ok -> `PopupOkBtn()`,
 *   ctrl:138-141, re-runs `save()` with `popUpFlag` now false, which is when
 *   the real POST fires). Clicking Update (`update()`, ctrl:228-277)
 *   validates and POSTs immediately, with no confirm step at all. Reproduced
 *   via `save()` (opens the modal) / `confirmSave()` (Ok, does the real
 *   POST) vs. `update()`.
 * - `$scope.popUpFlag` (ctrl:35) starts `true` but `PopupOkBtn` (ctrl:138-141)
 *   sets it to `false` and NEVER resets it back to `true` anywhere else in
 *   this controller -- so, in legacy, once a user completes one save via the
 *   confirm modal, every subsequent Save click for the rest of that page
 *   visit skips the modal entirely and POSTs immediately (until the state is
 *   re-entered fresh). This is the identical `popUpFlag` shape already
 *   present, unaddressed, in this codebase's qualification-master/
 *   degree-master (their `saveQualificationMaster`/`saveDegreeMaster` have
 *   the same never-reset flag) -- neither of those rebuilds reproduces the
 *   "modal only works once per visit" behavior, always re-opening the modal
 *   on every Save click instead. Matched here for consistency with that
 *   established precedent rather than reintroducing the quirk ad hoc for
 *   just this one screen.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's x close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of legacy's shared
 *   `<modal>` directive's `cancel()` crash (`$scope.cancel` is never defined
 *   in this controller either) -- a working close is an accepted standing
 *   improvement, not a parity gap.
 * - Both `save` and `update` only ever check `$scope.wingForm.$valid` with no
 *   `else` branch (ctrl:163, ctrl:245) -- an invalid submit is a silent
 *   no-op in both directions: no growl, no modal. Reproduced verbatim via
 *   early `return` in both `save()` and `update()`.
 * - After a successful save OR update (ctrl:178-179, ctrl:262), legacy calls
 *   `initWingList($scope.orgId, $scope.offset, ...)` -- but `$scope.offset`
 *   is initialized once to `0` (ctrl:16) and never reassigned anywhere else
 *   in this controller (only the separate `pager.startIndex`, used by
 *   `getWingList` on pagination-link clicks, changes with the page), so
 *   every successful save/update unconditionally refetches BOTH the list and
 *   the count starting from offset 0 -- i.e. the grid silently jumps back to
 *   page 1. Reproduced via `reloadFirstPage()` (mirrors qualification-master's
 *   own method of the same name for the same underlying shape).
 * - `updateStatus` (ctrl:279-313) does NOT call `$scope.init()`/re-fetch the
 *   list on success, unlike qualification-master's/degree-master's own
 *   `updateStatus` (which reset the form via `$scope.init()`) -- it only
 *   growls and does nothing else. Reproduced by patching the `list` signal
 *   locally only, with no form reset and no reload on either branch.
 * - `showUpdateBtn` (ctrl:197-225) sets `saveBtnFlag`/`updateBtnFlag`
 *   synchronously BEFORE the `GETWINGMASTERLISTBYID` response resolves, and
 *   uses `growl.error(response.data.message)` (the real server message) on a
 *   failed response -- NOT a native `alert` like qualification-master's/
 *   degree-master's own `showUpdateBtn`. Both reproduced verbatim:
 *   `saveMode.set(false)` up front; `notify.error(res.message ?? ...)` on
 *   failure.
 * - The header search box (view:74, `ng-keyup="serachTestmaster(searchKeyword)"`)
 *   calls a function that is never defined anywhere in
 *   wingMasterController.js (confirmed via full-file read) -- the identical
 *   dead-search-box crash already reproduced in this codebase's
 *   degree-master (see docs/ANGULAR_PENDING_FINDINGS.md). Every keystroke in
 *   the search box throws `serachTestmaster is not a function` in legacy,
 *   today. Reproduced verbatim via `onSearchKeyup()`, unbound to any model,
 *   NOT implemented as a working filter.
 */
@Component({
  selector: 'app-wing-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './wing-master.html',
  styleUrl: './wing-master.scss',
})
export class WingMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(WingMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<WingMasterForm>(emptyWingMasterForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initWingList/PagerService) ----
  list = signal<WingMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadList();
  }

  // Mirrors initWingList (ctrl:56-95) -- fires the list + count endpoints
  // together for whatever `currentPage`/`pageSize` currently is.
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

  // Mirrors the offset-0 refetch every successful save/update triggers --
  // see class comment.
  private reloadFirstPage(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  // Mirrors getWingList (ctrl:97-116), used by setPage(page, true) on
  // pagination-link clicks -- list-only refetch, no count.
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof WingMasterForm>(field: K, value: WingMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.wingCode.trim() && f.wingDesc.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal; only
  // the modal's "Ok" actually POSTs (mirrors save's popUpFlag-gated double
  // call via PopupOkBtn, ctrl:138-195; see class comment). ----
  save(): void {
    this.submitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:163 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  // Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 -- an ordinary working
  // close, not a reproduction of the shared <modal> directive's undefined
  // `cancel()` crash. See class comment.
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDmyHms(new Date());
    this.api
      .save({
        wingCode: f.wingCode,
        wingDesc: f.wingDesc,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:178-179 -- initWingMaster()/initWingList() run
          // unconditionally on success only (the error branch does neither);
          // see class comment re: offset-0 reset.
          if (res.status === 'success') this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update (mirrors update(), ctrl:228-277) -- no confirm modal at all ----
  update(): void {
    this.submitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:245 guard (no else branch)
    const f = this.form();
    this.api
      .update({
        wingCode: f.wingCode,
        wingDesc: f.wingDesc,
        wingId: f.wingId as number,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          if (res.status === 'success') this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyWingMasterForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:197-225) ----
  edit(row: WingMasterRow): void {
    this.saveMode.set(false);
    this.api.getById(row.wingId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            wingId: detail.wingId,
            wingCode: detail.wingCode,
            wingDesc: detail.wingDesc,
          });
        } else {
          // ctrl:213-218 -- growl.error(response.data.message), the real
          // server message, NOT a native alert (contrast with
          // qualification-master's/degree-master's own showUpdateBtn).
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:279-313) -- patches the
  // list in place; no form reset and no reload on either branch (contrast
  // with qualification-master's/degree-master's own updateStatus, which DO
  // reset the form on success; see class comment). ----
  toggleStatus(row: WingMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.wingId === row.wingId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        wingId: row.wingId,
        status: nextStatus,
        updatedDate: formatDmyHms(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  /** Faithful crash replication -- see class doc comment.
   * docs/ANGULAR_PENDING_FINDINGS.md: legacy's wing-master.html:74 wires
   * `ng-keyup="serachTestmaster(searchKeyword)"` to a function never defined
   * anywhere in wingMasterController.js. */
  onSearchKeyup(): void {
    throw new TypeError('serachTestmaster is not a function');
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:145-146/232-233/281-282. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
