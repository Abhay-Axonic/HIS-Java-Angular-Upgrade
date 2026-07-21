import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RowMastersService } from './row-masters.service';
import { RowMasterForm, RowMasterRow, emptyRowMasterForm } from './row-masters.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/row-master.html +
 * resources/assets/js/controller/billing/unit/rowMastersController.js
 * (controller name `RowMasters.Controller`). moduleId 1070 is declared
 * inside the controller's own self-registered `.config()` block
 * (ctrl:261-288), not app.js's central `$stateProvider` -- architecturally
 * orphaned but functionally live/reachable at `/rowMasters`; doesn't affect
 * this rebuild's route entry. No `controllerAs` in practice: the state
 * config declares `controllerAs:"vm"` but the entire controller body and
 * view use plain `$scope.*` throughout -- zero `vm.` references anywhere --
 * vestigial scaffolding, built here exactly like every other `$scope`-based
 * screen.
 *
 * Confirmed real, working CRUD against `/mrdrow/*`: save (ctrl:31-81), list
 * (ctrl:84-159), get-by-id/edit-populate (ctrl:179-199), update
 * (ctrl:201-228), status toggle (ctrl:230-254). No `<modal>` directive
 * anywhere in this screen (0 occurrences) -- the Save/Update anchors carry a
 * dead `data-toggle="modal"` Bootstrap attribute with no matching modal
 * element/`data-target` (view:74/76), so no confirm-modal step exists here,
 * unlike this codebase's `ward-master`/`degree-master`. `save()` posts
 * directly, no confirm step.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - `saveRow` (ctrl:31-81) validates `rowCode`/`rowDescription` with two
 *   separate growls ("Please Enter Row Code"/"Please Enter Row Description")
 *   before building the payload. `showUpdateBtn` (ctrl:201-228) -- the
 *   *update* path -- has **no validation of any kind**, client or growl: it
 *   builds `objUpdate` straight from whatever is currently in
 *   `$scope.rowCode`/`$scope.rowDescription` and PUTs it immediately. This
 *   is a genuine asymmetry (not the create/update confirm-modal asymmetry
 *   seen elsewhere in this codebase -- there's no modal here at all): saving
 *   a blank code/description is blocked with a growl, but *updating* a row
 *   down to a blank code/description is not. Reproduced verbatim: `save()`
 *   growls and early-returns on either empty field; `update()` does not
 *   call `formSubmitted.set(true)` and has no early-return guard at all.
 * - `viewBtn` (ctrl:179-199) reads `response.data.object.rowId/rowCode/
 *   rowDescription` and switches to edit mode **unconditionally**, with no
 *   `response.data.status === 'success'` check at all (contrast with this
 *   codebase's `ward-master.edit()`/`degree-master.edit()`, which both guard
 *   on `res.status`). Reproduced as-is in `edit()`: no status check, only
 *   the pre-existing `status === 'I'` guard (ctrl:181-187, "Please update
 *   status then you can edit.") blocks entry into edit mode.
 * - `updateStatus` (ctrl:230-254) shows a growl **only on success**
 *   (`response.data.status == 'success'`) -- there is no `else` branch, so a
 *   failed/errored status toggle produces no user-visible feedback
 *   whatsoever, silently. Unconditionally, regardless of that success/error
 *   split, it then calls `$scope.clearFunction()` (wiping only the
 *   `rowCode`/`rowDescription` text fields -- not `rowID`/save-vs-update
 *   mode) and `$scope.getRow()` (a full server reload of the list). So
 *   toggling *any* row's status silently blanks whatever code/description
 *   text the user currently has typed into the form above (even in the
 *   middle of creating or editing an unrelated row), while leaving that
 *   in-progress row's save-vs-update mode untouched. Reproduced via
 *   `toggleStatus()`: no error notification on failure, and the form's
 *   `rowCode`/`rowDescription` (only) are cleared and the list reloaded
 *   after every response, success or not.
 * - The list's search box (`ng-model="searchKeyword"`, view:80) feeds a real
 *   working `getMrd |filter:searchKeyword` pipe on the table (view:98) --
 *   *and*, on the very same input, an `ng-keyup="serachDepartmentMaster(searchKeyword)"`
 *   that calls a function never defined anywhere in
 *   rowMastersController.js (confirmed via full-file grep). This view was
 *   evidently copy-pasted wholesale from a Speciality/Department Master
 *   screen -- the leftover `Specialityform` form name, `specialityCode`/
 *   `specialityName` field names, and the trailing script's
 *   `.text("Department Master")` (view:142, cosmetic/dead, not reproduced)
 *   are all further evidence of that. Because `ng-model` updates on the
 *   `input` event (which fires before `keyup`), the filter itself keeps
 *   working on every keystroke even though the *same* keystroke also throws
 *   in the `ng-keyup` handler -- both halves are real and are both
 *   reproduced here: `displayedRows` is a working client-side filter over
 *   `rowCode`/`rowDescription`, and `onSearchKeyup()` throws, per
 *   docs/ANGULAR_PENDING_FINDINGS.md's standing convention for this class of
 *   copy-paste crash bug (see `degree-master.ts`, whose own copy-pasted
 *   equivalent is named `serachTestmaster` -- a different undefined name,
 *   same root cause).
 * - Legacy's `getRow()`/`initRowGroupMasterList()`/`masterList()` are three
 *   near-duplicate functions that all hit `/mrdrow/getMrdRowList` and all
 *   get invoked redundantly (page load alone fires `getRow()` then
 *   `initRowGroupMasterList()`; a successful save fires `getRow()` then
 *   `initializeController()`, which itself re-invokes both). Collapsed here
 *   into a single `loadList()`, called once per page-load/save/update/status
 *   change -- not a behavior change (all three hit the identical endpoint
 *   with the identical params and all three just re-set the same list/count
 *   signals), purely dropping wasted duplicate network calls, per the task
 *   brief.
 * - `$scope.unitDeptMasterList` is populated by `initRowGroupMasterList`/
 *   `masterList` but never read by the view (which binds `getMrd`, not
 *   `unitDeptMasterList`, at view:98) -- dead state, not modeled here.
 */
@Component({
  selector: 'app-row-masters',
  imports: [FormsModule, StatusToggle],
  templateUrl: './row-masters.html',
  styleUrl: './row-masters.scss',
})
export class RowMasters {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RowMastersService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors loose $scope.rowCode/rowDescription/rowID) ----
  form = signal<RowMasterForm>(emptyRowMasterForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false); // only ever set by save() -- see class comment on update()'s asymmetry

  // ---- list + server-side pagination (mirrors getRow/PagerService) ----
  list = signal<RowMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: a real working filter, live alongside the
  // crashing ng-keyup handler on the same input -- see class comment.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.rowCode?.toLowerCase().includes(keyword) || r.rowDescription?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getMrdRowList(this.unitId, this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
        }
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

  updateFormField<K extends keyof RowMasterForm>(field: K, value: RowMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isRequiredMissing(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  // ---- save (mirrors saveRow, ctrl:31-81) -- growl-validated, no modal ----
  save(): void {
    this.formSubmitted.set(true);
    const f = this.form();
    if (!f.rowCode.trim()) {
      this.notify.error('Please Enter Row Code');
      return;
    }
    if (!f.rowDescription.trim()) {
      this.notify.error('Please Enter Row Description');
      return;
    }
    this.api
      .saveMrdRow({
        rowCode: f.rowCode,
        rowDescription: f.rowDescription,
        unitId: this.unitId,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        createdBy: this.userId,
        status: 'A',
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

  // ---- update (mirrors showUpdateBtn, ctrl:201-228) -- NO validation at
  // all, unlike save() -- see class comment ----
  update(): void {
    const f = this.form();
    this.api
      .updateMrdRow({
        rowId: f.rowId as number,
        rowCode: f.rowCode,
        rowDescription: f.rowDescription,
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        createdBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          }
          this.resetForm();
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyRowMasterForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  private clearFunction(): void {
    this.updateFormField('rowCode', '');
    this.updateFormField('rowDescription', '');
  }

  // ---- edit (mirrors viewBtn, ctrl:179-199) -- no response.status check ----
  edit(row: RowMasterRow): void {
    if (row.status === 'I') {
      this.notify.error('Please update status then you can edit.');
      return;
    }
    this.api.getMrdRowById(row.rowId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.form.set({
          rowId: res.object?.rowId as number,
          rowCode: res.object?.rowCode as string,
          rowDescription: res.object?.rowDescription as string,
        });
        this.saveMode.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:230-254) ----
  toggleStatus(row: RowMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateMrdRowStatus({
        rowId: row.rowId,
        rowCode: row.rowCode,
        rowDescription: row.rowDescription,
        status: nextStatus,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        createdBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          // ctrl:244-249 -- growl only on success, no error branch at all;
          // see class comment.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          }
          // ctrl:250-251 -- clearFunction() + getRow() run unconditionally,
          // regardless of success/failure.
          this.clearFunction();
          this.loadList();
        },
        error: () => {
          this.clearFunction();
          this.loadList();
        },
      });
  }

  /** Faithful crash replication -- see class doc comment. Legacy's
   * row-master.html:80 wires `ng-keyup="serachDepartmentMaster(searchKeyword)"`
   * to a function never defined anywhere in rowMastersController.js (a
   * differently-named copy-paste artifact from the same class of bug as
   * degree-master's `serachTestmaster`). Per
   * docs/ANGULAR_PENDING_FINDINGS.md's standing convention for this bug
   * class, the crash is reproduced directly rather than silently fixed;
   * unlike degree-master, the search box's `filter:searchKeyword` pipe (see
   * `displayedRows`) genuinely keeps working alongside it. */
  onSearchKeyup(): void {
    throw new TypeError('serachDepartmentMaster is not a function');
  }
}
