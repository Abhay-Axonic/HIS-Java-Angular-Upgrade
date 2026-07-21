import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { EmployeeCategoryMasterService } from './employee-category-master.service';
import { EmployeeCategoryForm, EmployeeCategoryRow, emptyEmployeeCategoryForm } from './employee-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/employee-category-master.html +
 * resources/assets/js/controller/master/global/common/employeeCategoryMasterController.js
 * (`employeeCategoryMasterController`, no `controllerAs`). Route guard:
 * moduleId 498 (app.js:14257-14284 resolve block, confirmed live), wired at
 * the route level, not here.
 *
 * ORG-scoped only -- the legacy controller never builds or sends a `unitId`
 * on any of its calls (getAllEmployeeCategoryMasterList / getEmployeeCategoryCount
 * / saveEmployeeCategoryMaster / getEmployeeCategoryMasterById /
 * updateEmployeeCategoryMaster / updateEmployeeCategoryMasterStatus), unlike
 * several sibling "master" screens in this batch. Confirmed by reading the
 * controller in full.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - **The Save confirm-modal only ever fires ONCE per page load -- a real
 *   legacy bug, not a copy-paste omission.** `$scope.popUpFlag` is
 *   initialized to `true` exactly once at controller construction (ctrl:13)
 *   and is flipped to `false` forever by `PopupOkBtn` (ctrl:139-142) the
 *   first time the confirm modal's "Ok" is clicked. Unlike this same batch's
 *   `qualificationMasterController` (whose `init()` explicitly resets
 *   `popUpFlag = true` on every successful save, ctrl:33-73), this
 *   controller's `initMaster()` (ctrl:35-47, called after every successful
 *   save/update at ctrl:185/273) never touches `popUpFlag` at all. Net
 *   effect: the FIRST "Save" click of a session opens the "Are you sure you
 *   want to Save?" modal as expected; every subsequent "Save" click for the
 *   rest of the page's lifetime (including for brand-new entries after the
 *   form resets) silently skips the modal and saves immediately, because
 *   `popUpFlag` is now permanently `false`. Reproduced verbatim via the
 *   `confirmModalConsumed` signal below -- NOT reset in `resetForm()`,
 *   matching `initMaster()` exactly. `update()` never used a confirm modal
 *   to begin with (ctrl:241-288), so it is unaffected either way.
 *
 * - `showUpdateBtn` (ctrl:210-238) uses `growl.error` on a failed by-id
 *   fetch (unlike this batch's `qualification-master`, which uses a raw
 *   `alert("Error!!")` for the same case) -- reproduced here via
 *   `notify.error`, not `window.alert`.
 *
 * - `updateStatus` (ctrl:290-326) POSTs the ENTIRE `$scope.common` edit-form
 *   object as the payload -- a cross-cutting anti-pattern also found in
 *   this batch's `counterMaster`/`cabinMaster`. Rebuilt here as a clean
 *   minimal payload (`employeeCategoryId`, `status`, `updatedBy`,
 *   `updatedDate`) instead; this is a low-severity payload cleanup only, not
 *   a user-visible behavior change. On success it calls BOTH `initMaster()`
 *   (resets the add/edit form back to blank Save-mode) AND
 *   `initMasterList()` (a full server refetch of the list + count) --
 *   unlike `qualification-master`'s `updateStatus`, which only resets the
 *   form and never reloads the list. Both effects are reproduced here via
 *   `resetForm()` + `loadList()`.
 *
 * - The header search box (view:70, `ng-model="searchKeyword"` with a plain
 *   `filter:searchKeyword` binding, view:123) IS wired here -- no
 *   custom search-crash on this screen. `commonList` only ever holds the
 *   CURRENT PAGE's already-fetched rows (`initMasterList`/`masterList`
 *   refetch on every page change), so a search only ever matches within the
 *   current page -- same practical interpretation already used for this
 *   identical legacy `filter:keyword` pattern in `qualification-master`/
 *   `asset-category-master`. Reproduced via `displayedRows`, matching only
 *   the two visible text columns (code/description).
 *
 * - The per-column sort caret icons (view:78-121, "Status(n)"/"Action taken"
 *   headers included) carry no `ng-click` anywhere in the view or
 *   controller -- confirmed dead UI, same as every other screen in this
 *   batch. Omitted here rather than given working sort behavior this
 *   rebuild would be inventing.
 *
 * - **The shared `<modal>` directive's x close button crashes in legacy**
 *   (`common/commonPopUpDirectives.js:10` hardcodes `ng-click="cancel();
 *   stopLoader();"`, and `employeeCategoryMasterController.js` never defines
 *   `$scope.cancel`) -- per the standing policy (`docs/
 *   ANGULAR_PENDING_FINDINGS.md` entry 55, `docs/ANGULAR_REBUILD_PLAYBOOK.md`
 *   Step 3), this is NOT reproduced. The x button here is an ordinary,
 *   working `closeConfirmModal()`, same pattern as `qualification-master`.
 */
@Component({
  selector: 'app-employee-category-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './employee-category-master.html',
  styleUrl: './employee-category-master.scss',
})
export class EmployeeCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(EmployeeCategoryMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<EmployeeCategoryForm>(emptyEmployeeCategoryForm());
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- mirrors $scope.popUpFlag: starts `false` (modal not yet consumed),
  // permanently flipped to `true` the first time the confirm modal's "Ok" is
  // clicked -- never reset afterward. See class comment for the legacy bug
  // this reproduces. ----
  private confirmModalConsumed = signal(false);

  // ---- list + pagination state (mirrors initMasterList/setPage) ----
  list = signal<EmployeeCategoryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE's already-fetched
  // rows only (see class comment), matching only the two visible text columns.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.employeeCategoryCode?.toLowerCase().includes(keyword) ||
        r.employeeCategoryDescription?.toLowerCase().includes(keyword),
    );
  });

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

  updateField<K extends keyof EmployeeCategoryForm>(field: K, value: EmployeeCategoryForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.employeeCategoryCode.trim() && f.employeeCategoryDescription.trim());
  }

  // ---- save flow: the confirm modal only appears the first time ever (see
  // class comment on confirmModalConsumed / the popUpFlag bug it
  // reproduces); once consumed, every later Save click saves immediately. ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    if (this.confirmModalConsumed()) {
      this.create();
    } else {
      this.showConfirmModal.set(true);
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.confirmModalConsumed.set(true);
    this.create();
  }

  private create(): void {
    const f = this.form();
    const now = formatDmyHms(new Date());
    this.api
      .save({
        employeeCategoryCode: f.employeeCategoryCode,
        employeeCategoryDescription: f.employeeCategoryDescription,
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
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update flow: no confirm modal at all, matching legacy's
  // update() being wired directly to the Update button (ctrl:241-288). ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const f = this.form();
    this.api
      .update({
        employeeCategoryId: f.employeeCategoryId as number,
        employeeCategoryCode: f.employeeCategoryCode,
        employeeCategoryDescription: f.employeeCategoryDescription,
        organizationId: this.organizationId,
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
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyEmployeeCategoryForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:210-238 -- growl.error, not
  // alert(), on a failed fetch; see class comment). ----
  edit(row: EmployeeCategoryRow): void {
    this.saveMode.set(false);
    this.api.getById(row.employeeCategoryId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            employeeCategoryId: detail.employeeCategoryId,
            employeeCategoryCode: detail.employeeCategoryCode,
            employeeCategoryDescription: detail.employeeCategoryDescription,
          });
        } else {
          this.notify.error('Error!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:290-326 -- clean minimal
  // payload instead of the whole $scope.common object; see class comment.
  // On success, resets the form to blank Save-mode AND reloads the list from
  // the server, matching legacy's initMaster() + initMasterList() calls. ----
  toggleStatus(row: EmployeeCategoryRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateStatus({
        employeeCategoryId: row.employeeCategoryId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:154-155/244-245/292-293. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
