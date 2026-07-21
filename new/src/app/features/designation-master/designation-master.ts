import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DesignationMasterService } from './designation-master.service';
import { DesignationMasterForm, DesignationMasterRow, emptyDesignationMasterForm } from './designation-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/designation-master.html +
 * resources/assets/js/controller/master/global/common/designationMasterController.js
 * (`designationMasterController`, no `controllerAs`). moduleId 500, wired at
 * the route level via `moduleAccessGuard`, not here. Org-level only -- no
 * unitId anywhere in this controller or its endpoints.
 *
 * Confirmed real, working CRUD: save (ctrl:194-251), update (ctrl:322-369),
 * edit-populate `showUpdateBtn` (ctrl:254-279), status toggle `updateStatus`
 * (ctrl:282-319) -- all wired to real endpoints, see designation-master.service.ts.
 *
 * **Deliberate quirks preserved, not bugs introduced by this rebuild:**
 * - Save vs. Update is asymmetric, same family as degree-master/
 *   qualification-master: clicking Save (`saveBtnFlag`, ctrl:194-251)
 *   validates, then opens the shared "Are you sure you want to Save?" confirm
 *   modal (`showModal`/`popUpFlag` dance) before actually POSTing -- only the
 *   modal's Ok -> `PopupOkBtn()` (ctrl:188-191) re-runs `saveDesignation()`
 *   with `popUpFlag` now false, which is when the real POST fires. Clicking
 *   Update (`updateBtnFlag`, ctrl:322-369) validates and POSTs immediately,
 *   with NO confirm step at all. Reproduced via `save()`/`confirmSave()` vs.
 *   `update()`.
 * - Both `saveDesignation` (ctrl:241-247) and `updateDesignation`
 *   (ctrl:359-365) call `initDesignationList(orgId, offset, noOfRecordsPerPage)`
 *   unconditionally after the POST resolves, success or error -- and that
 *   function always re-fetches using `$scope.offset`, which is never mutated
 *   anywhere else in this controller (it stays its initial value, 0), then
 *   resets the pager display to page 1 once the count comes back
 *   (`setPage(1, false)`). Net effect: every save/update attempt snaps the
 *   list back to the first page, regardless of which page the user was
 *   viewing or whether the call succeeded. Reproduced via `reloadFirstPage()`,
 *   called unconditionally in both `confirmSave()`'s and `update()`'s
 *   `next` handler.
 * - **The shared `<modal>` directive's x close button crashes in legacy**
 *   (`common/commonPopUpDirectives.js:10` hardcodes `ng-click="cancel();
 *   stopLoader();"`, and `designationMasterController.js` never defines
 *   `$scope.cancel`) -- but per the standing policy resolved 2026-07-21
 *   (`docs/ANGULAR_PENDING_FINDINGS.md` entry 55, `docs/
 *   ANGULAR_REBUILD_PLAYBOOK.md` Step 3), a shared component's correct,
 *   working default behavior differing from a bug baked into legacy's own
 *   shared directive is an accepted, disclosed standing improvement, not a
 *   parity gap to reproduce. The x button here is an ordinary, working
 *   `closeConfirmModal()`, same as this codebase's other screens using this
 *   shared modal pattern (e.g. qualification-master).
 * - `showUpdateBtn` (ctrl:254-279) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETDESIGNATIONMASTERBYID` call
 *   resolves, and uses a raw `alert("Error!!")` on a failed response -- the
 *   only place in this controller that doesn't use `growl`. Both reproduced
 *   verbatim (`saveMode.set(false)` up front; `window.alert` on failure, with
 *   no reset back to save-mode).
 * - `updateStatus` (ctrl:282-319) calls `$scope.init()` on success (which
 *   resets the form/`saveBtnFlag`/`updateBtnFlag` back to blank Save-mode)
 *   but never calls `initDesignationList` on EITHER branch -- toggling a
 *   row's status never re-fetches the list from the server, and never
 *   reverts on failure either. Reproduced by patching the `list` signal
 *   locally, resetting the form on success only, and never reloading or
 *   reverting.
 * - The per-column sort caret icons (view lines 90-129, including the
 *   "Status(n)"/"Action taken" column headers) carry no `ng-click` anywhere
 *   in the view or controller -- confirmed dead UI, omitted here rather than
 *   given working sort behavior this rebuild would be inventing (same
 *   treatment as this codebase's other simple master screens).
 *
 * **1 confirmed reproducible crash bug, NOT fixed, faithfully reproduced:**
 * The search input (designation-master.html:62,
 * `ng-keyup="serachTestmaster(searchKeyword)"`) calls a function that is
 * never defined anywhere in designationMasterController.js (confirmed via
 * full-file read -- the exact same copy-paste-leftover bug already found on
 * degree-master's search box). Every keystroke in the search box throws
 * `serachTestmaster is not a function` in legacy, today. Reproduced via a
 * direct throw -- see `onSearchKeyup()`. Not implemented as a working filter,
 * and not omitted -- this is a real, live, easily-triggered bug.
 */
@Component({
  selector: 'app-designation-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './designation-master.html',
  styleUrl: './designation-master.scss',
})
export class DesignationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DesignationMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.designationMasterObj) ----
  form = signal<DesignationMasterForm>(emptyDesignationMasterForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false); // drives inline required-field display, like show-errors-check-validity
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initDesignationList/PagerService) ----
  list = signal<DesignationMasterRow[]>([]);
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
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  // Mirrors initDesignationList always re-fetching at $scope.offset (never
  // mutated -- stays 0) and then resetting the pager display to page 1; see
  // class comment.
  private reloadFirstPage(): void {
    this.currentPage.set(1);
    this.loadList();
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

  updateField<K extends keyof DesignationMasterForm>(field: K, value: DesignationMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.designationCode.trim() && f.designationDesc.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal; only
  // the modal's "Ok" actually POSTs (mirrors saveDesignation's popUpFlag-gated
  // double call via PopupOkBtn, ctrl:187-251). ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDmyHms(new Date());
    this.api
      .save({
        employeeDesignationCode: f.designationCode,
        employeeDesignationDescription: f.designationDesc,
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
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:241-247 -- initDesignationList runs unconditionally after
          // every save attempt, success or not; see class comment.
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update (mirrors updateDesignation, ctrl:322-369) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .update({
        employeeDesignationCode: f.designationCode,
        employeeDesignationDescription: f.designationDesc,
        employeeDesignationId: f.employeeDesignationId as number,
        updatedBy: this.userId,
        organizationId: this.organizationId,
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
          // ctrl:359-365 -- same unconditional reload as save; see class comment.
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyDesignationMasterForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn -- flips into update mode BEFORE the
  // GETDESIGNATIONMASTERBYID response arrives, and uses a raw alert() on
  // failure; both preserved verbatim; see class comment). ----
  edit(row: DesignationMasterRow): void {
    this.saveMode.set(false);
    this.api.getById(row.employeeDesignationId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            employeeDesignationId: detail.employeeDesignationId,
            designationCode: detail.employeeDesignationCode,
            designationDesc: detail.employeeDesignationDescription,
          });
        } else {
          window.alert('Error!!'); // ctrl:275 -- native alert("Error!!") in legacy
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- patches the list in place,
  // resets the add/edit form on success only, and never reloads or reverts
  // on failure; see class comment). ----
  toggleStatus(row: DesignationMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.employeeDesignationId === row.employeeDesignationId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        employeeDesignationId: row.employeeDesignationId,
        status: nextStatus,
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
        },
      });
  }

  /** Faithful crash replication -- see class doc comment. Legacy's
   * designation-master.html:62 wires `ng-keyup="serachTestmaster(searchKeyword)"`
   * to a function never defined anywhere in designationMasterController.js. */
  onSearchKeyup(): void {
    throw new TypeError('serachTestmaster is not a function');
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:195-197/283-285/323-325. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
