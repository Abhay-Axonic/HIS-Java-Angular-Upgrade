import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DegreeMasterService } from './degree-master.service';
import { DegreeMasterFormRecord, DegreeMasterListRow, emptyDegreeMaster } from './degree-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/degree-master.html +
 * resources/assets/js/controller/master/global/common/degreeMasterController.js.
 * moduleId 497 (config/app.js:14201-14224), no controllerAs -- classic
 * `$scope` controller, no resolve-block guard reproduced here (that lives at
 * the route level via `moduleAccessGuard`, wired centrally, not per screen).
 *
 * Confirmed real, working CRUD: save (ctrl:191-254), update (ctrl:325-372),
 * edit-populate `showUpdateBtn` (ctrl:257-282), status toggle `updateStatus`
 * (ctrl:285-322) -- all wired to real endpoints, see degree-master.service.ts.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal ("Are you sure you want to Save?",
 *   commonPopUpDirectives.js's shared `<modal>` directive, wired via legacy's
 *   `PopupOkBtn`/`popUpFlag`) only gates CREATE (`saveDegreeMaster`,
 *   ctrl:191-254); `updateDegreeMaster` (ctrl:325-372) saves immediately with
 *   no confirm step at all -- same asymmetry as this codebase's
 *   speciality-master/city-master. Reproduced via `save()`/`confirmSave()`
 *   vs. `update()`.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's × close button is wired to an ordinary working close
 *   here, NOT a reproduction of legacy's shared-directive `cancel()` crash --
 *   a working close is an accepted standing improvement, not a parity gap.
 * - Unlike speciality-master's update path (which growls "Fill all mandatory
 *   fields." on invalid submit), BOTH `saveDegreeMaster` and
 *   `updateDegreeMaster` here only ever check `$scope.degreeForm.$valid`
 *   with no `else` branch (ctrl:213, ctrl:339) -- an invalid submit is a
 *   silent no-op in both directions: no growl, no modal. Reproduced verbatim
 *   via early `return` in both `save()` and `update()`.
 * - `updateStatus` (ctrl:285-322) calls `$scope.init()` unconditionally on
 *   ANY successful status toggle, which resets `$scope.degreeMasterObj`,
 *   `saveBtnFlag`, and `updateBtnFlag` -- so successfully toggling ANY row's
 *   status also blanks out whatever the user was mid-editing in the form
 *   above, even a different row entirely. Reproduced via `resetForm()` in
 *   the success branch of `toggleStatus()`.
 * - `updateStatus`'s failure branch (ctrl:314-320) only shows a growl error
 *   -- it never reverts the checkbox's already-flipped `ng-model` binding.
 *   Reproduced as-is: no revert on a failed/errored status change here
 *   either (contrast with asset-category-master's partial 406-only revert).
 * - `showUpdateBtn`'s failure branch (ctrl:277-279) is a native `alert("Error!!")`,
 *   not a growl -- translated to `notify.error('Error!!')` here, consistent
 *   with every other rebuilt screen routing user feedback through
 *   NotificationService rather than a native `alert`.
 *
 * **1 confirmed reproducible crash bug, NOT fixed, faithfully reproduced:**
 * The search input (degree-master.html:73,
 * `ng-keyup="serachTestmaster(searchKeyword)"`) calls a function that is
 * never defined anywhere in degreeMasterController.js (confirmed via
 * full-file grep -- likely a copy-paste leftover from an unrelated "test
 * master" screen). Every keystroke in the search box throws
 * `serachTestmaster is not a function` in legacy, today. TypeScript can't
 * express calling a truly undefined global function the same way, so the
 * identical OBSERVABLE crash (throws on every keystroke, search never
 * actually filters anything) is reproduced via a direct throw instead --
 * see `onSearchKeyup()`. Not implemented as a working filter, and not
 * omitted -- this is a real, live, easily-triggered bug distinct from this
 * codebase's other, working master-screen search boxes (e.g.
 * speciality-master's).
 */
@Component({
  selector: 'app-degree-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './degree-master.html',
  styleUrl: './degree-master.scss',
})
export class DegreeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DegreeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.degreeMasterObj) ----
  record = signal<DegreeMasterFormRecord>(emptyDegreeMaster());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initDegreeList/PagerService) ----
  degreeList = signal<DegreeMasterListRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.degreeList.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
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

  updateField<K extends keyof DegreeMasterFormRecord>(field: K, value: DegreeMasterFormRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ---- required-field validation (mirrors degreeForm.$valid via the 2
  // `required` inputs -- degreeCode/degreeDesc, view:16/31) ----
  private requiredFieldsValid(): boolean {
    const r = this.record();
    return !!(r.degreeCode.trim() && r.degreeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.record().degreeCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.record().degreeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveDegreeMaster/PopupOkBtn, ctrl:191-254) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:213 guard (no else branch)
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
      .create({
        degreeCode: r.degreeCode,
        degreeDesc: r.degreeDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:228/244-249 -- $scope.init() resets the form and the list is
          // reloaded unconditionally after every save attempt, success or not.
          this.resetForm();
          this.loadList();
        },
      });
  }

  // ---- update (mirrors updateDegreeMaster, ctrl:325-372) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:339 guard (no else branch)
    const r = this.record();
    const now = formatDateTime(new Date());
    this.api
      .update({
        degreeCode: r.degreeCode,
        degreeDesc: r.degreeDesc,
        degreeId: r.degreeId,
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.resetForm();
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.record.set(emptyDegreeMaster());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:257-282) ----
  edit(degreeId: number): void {
    this.saveMode.set(false);
    this.api.getById(degreeId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error('Error!!'); // ctrl:278 -- native alert("Error!!") in legacy
          return;
        }
        const d = res.listObject[0];
        this.record.set({ degreeId: d.degreeId, degreeCode: d.degreeCode, degreeDesc: d.degreeDesc });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:285-322) ----
  toggleStatus(row: DegreeMasterListRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.degreeList.update((rows) =>
      rows.map((r) => (r.degreeId === row.degreeId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        degreeId: row.degreeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:305-306 -- $scope.init() runs unconditionally on success,
            // clearing the form/mode even for an unrelated row; see class comment.
            this.resetForm();
          } else {
            // ctrl:314-320 -- growl only, no revert of the already-flipped
            // checkbox; see class comment.
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  /** Faithful crash replication -- see class doc comment. Legacy's
   * degree-master.html:73 wires `ng-keyup="serachTestmaster(searchKeyword)"`
   * to a function never defined anywhere in degreeMasterController.js. */
  onSearchKeyup(): void {
    throw new TypeError('serachTestmaster is not a function');
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:198-200/286-288/326-328. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
