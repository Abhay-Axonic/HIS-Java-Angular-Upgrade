import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { EmployeeCategoryService } from './employee-category.service';
import { EmployeeCategoryForm, EmployeeCategoryRow, emptyEmployeeCategoryForm } from './employee-category.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/emplyoee-category.html (sic --
 * legacy's own filename typo, kept only as a citation, not reproduced as a
 * route/file name here) +
 * resources/assets/js/controller/master/global/billing/employeeCategoryController.js
 * (`employeeCategoryController`, no `controllerAs`). State `employeeCategory`
 * (app.js:8082-8108), moduleId 454, confirmed live (not commented out).
 *
 * **Step 0 duplicate-screen check (see docs/ANGULAR_PENDING_FINDINGS.md --
 * flagged as a new finding, not resolved here):** this state is NOT a
 * duplicate route to `employeeCategoryMaster` (already rebuilt,
 * `features/employee-category-master/`) -- they are two genuinely distinct
 * controller files (`employeeCategoryController.js` vs.
 * `employeeCategoryMasterController.js`), two distinct views with different
 * field layouts/model names, two distinct module ids (454 vs. 498), and
 * different UX (this screen has no loading spinner and no try/catch
 * wrapping; `employeeCategoryMaster` has both). However: **both screens'
 * save/update/status-toggle/get-by-id calls resolve to byte-identical
 * backend URLs** (`/billing/saveEmployeeCategoryMaster`,
 * `/billing/updateEmployeeCategoryMaster`,
 * `/billing/updateEmployeeCategoryMasterStatus`,
 * `/billing/getEmployeeCategoryMasterById` -- confirmed by reading both
 * controllers' endpoint constants side-by-side); only the plain list-fetch
 * endpoint differs by literal string (`getEmployeeCategoryMasterList` here
 * vs. `getAllEmployeeCategoryMasterList` there). Both screens read/write the
 * SAME underlying employee-category master rows under two independently
 * built frontends and two different module permissions -- a real backend
 * data-collision risk between two live, independently-permissioned admin
 * screens, worth an owner decision, but not the literal
 * "same-screen-two-state-names" case the playbook's Step 0 says to stop
 * for (that requires an identical controller/view, which this is not).
 * Proceeding with the rebuild per the playbook's guidance for genuinely
 * distinct-but-overlapping siblings.
 *
 * Confirmed real, working CRUD during Step 0/1 audit: list+count
 * (ctrl:70-119), save (ctrl:181-236), edit-populate `showUpdateBtn`
 * (ctrl:240-264), update (ctrl:309-356), status toggle `updateStatus`
 * (ctrl:267-306) -- all wired to real endpoints (confirmed live, 0/12
 * endpoints 404'd per `check_backend_endpoints.py`), see
 * employee-category.service.ts.
 *
 * **1 confirmed, real, significant bug -- NOT fixed, faithfully
 * reproduced (new finding, not previously catalogued):** a
 * typo'd-vs.-correct variable-name mismatch means `organizationId` is
 * `undefined` on every single call EXCEPT the very first page load. See
 * employee-category.models.ts's `EmployeeCategorySavePayload` doc comment
 * for the full ctrl-line citation. Reproduced here via two separate private
 * fields -- `organizationId` (the real session value, used ONLY in the
 * constructor's initial `loadListAndCount()` call, mirroring ctrl:168-170's
 * one correct use of `$scope.orgnisationId`) and `brokenOrganizationId`
 * (always `undefined`, used in every other call site: page-size change,
 * pagination, save, update, edit-populate, and both post-save/post-update
 * list reloads -- mirroring every other call site's use of
 * `$scope.organizationId`, which legacy never assigns). Angular's
 * `HttpClient` drops `undefined`-valued keys from the JSON body exactly like
 * legacy's `$http` did, so this reproduces the real wire behavior, not just
 * a cosmetic stand-in.
 *
 * **Compounding bug on `getEmpCategoryMasterList` (ctrl:121-148, the
 * pagination-page fetch):** the function declares its own `orgId` parameter
 * but never uses it -- its body reads `$scope.organizationId` directly
 * instead (ctrl:130), so even if a caller passed a correct value in, it
 * would still be discarded. Reproduced by `loadListOnly()` below simply not
 * taking an org-id parameter at all.
 *
 * **Toggling ANY row's status silently resets the create/update form
 * in-progress (ctrl:267-306, `updateStatus`'s success branch calls
 * `$scope.init()`, which clears `$scope.employeeCategory` and forces
 * `saveBtnFlag=true`/`updateBtnFlag=false` -- with no list reload at all).**
 * A previously-uncatalogued, real UX bug: mid-edit state is silently
 * discarded by an unrelated toggle click elsewhere on the same page.
 * Reproduced via `resetForm()` inside `toggleStatus`'s response handler
 * (both branches call it, matching `init()` running unconditionally inside
 * the `if(success)` block -- `init()` itself has no error path here).
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal (shared `<modal>` directive, `PopupOkBtn`/`popUpFlag`)
 *   only gates CREATE (ctrl:181-236); `update()` (ctrl:309-356) saves
 *   immediately with no confirm step -- same asymmetry as amenity-master/
 *   bank-master.
 * - List reload runs ONLY on success in `save()`/`update()`'s response
 *   handler position, but note legacy actually calls the reload
 *   UNCONDITIONALLY after the if/else (ctrl:227-231/347-351, outside the
 *   success/error branches) -- unlike amenity-master/bank-master. Reproduced
 *   by calling `loadListAndCount()` (with the broken org id) after BOTH
 *   branches here, not gated on success.
 * - The search input (view:79-81) has NO `ng-model`, `ng-click`, or
 *   `ng-keyup` at all -- not even a copy-paste crash reference like
 *   amenity-master's `serachTestmaster`. It is purely decorative markup with
 *   zero wiring. Reproduced as a plain, unbound `<input>`.
 * - The edit pencil is NOT visually disabled for Inactive rows (no
 *   `ng-class="disabled"` anywhere on view:134, unlike amenity-master).
 *   Reproduced without any disabled-state binding.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close button is wired to an ordinary working
 *   `closeConfirmModal()`, NOT a reproduction of the shared `<modal>`
 *   directive's `cancel()` crash (`employeeCategoryController.js` never
 *   defines `$scope.cancel`) -- an accepted standing improvement.
 */
@Component({
  selector: 'app-employee-category',
  imports: [FormsModule, StatusToggle],
  templateUrl: './employee-category.html',
  styleUrl: './employee-category.scss',
})
export class EmployeeCategory {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(EmployeeCategoryService);

  private readonly userId = this.session.userId ?? 0;

  // ctrl:38 ($scope.orgnisationId) -- the real, correctly-derived org id.
  // Used ONLY for the very first page load. See class doc comment.
  private readonly organizationId = this.session.organizationId;

  // ctrl: every OTHER call site reads `$scope.organizationId`, which
  // `init()` never assigns -- always `undefined`. See class doc comment.
  private readonly brokenOrganizationId: number | undefined = undefined;

  // ---- form state (mirrors $scope.employeeCategory) ----
  form = signal<EmployeeCategoryForm>(emptyEmployeeCategoryForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initEmployeeCategoryMasterList/getEmpCategoryMasterList) ----
  list = signal<EmployeeCategoryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    // ctrl:168-170 -- the ONE call site using the real org id.
    this.loadListAndCount(this.organizationId);
  }

  // ctrl:70-119 (initEmployeeCategoryMasterList) -- fetches list + count
  // together; count's success handler resets to page 1.
  private loadListAndCount(orgId: number | undefined): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(orgId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(orgId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
          this.currentPage.set(1);
        }
      },
    });
  }

  // ctrl:121-148 (getEmpCategoryMasterList) -- list-only fetch, used by
  // pagination clicks. Legacy's own `orgId` parameter is dead (ctrl:130
  // reads `$scope.organizationId` directly instead) -- reproduced by not
  // accepting an org-id parameter at all, always using the broken value.
  private loadListOnly(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.brokenOrganizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  // ctrl:65-69 (setNoOfRecords) -- calls initEmployeeCategoryMasterList with
  // $scope.organizationId (broken), so BOTH list and count refetch with the
  // undefined org id on a page-size change.
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount(this.brokenOrganizationId);
  }

  // ctrl:153-166 (setPage) -- pagination clicks call getEmpCategoryMasterList
  // (list only, broken org id), never the count.
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadListOnly();
  }

  updateField<K extends keyof EmployeeCategoryForm>(field: K, value: EmployeeCategoryForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors employeeCategoryForm.$valid via
  // the 2 `required` inputs -- Code/Description, view:17/33) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.employeeCode.trim() && f.employeeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().employeeCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().employeeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveEmployee/PopupOkBtn, ctrl:181-236) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:197 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        employeeCategoryCode: f.employeeCode,
        employeeCategoryDescription: f.employeeDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.brokenOrganizationId, // ctrl:193 -- see class doc comment
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
          // ctrl:227-231 -- reload runs UNCONDITIONALLY after both branches,
          // not just on success (unlike amenity-master/bank-master). Uses
          // the broken org id, same as legacy.
          this.resetForm();
          this.loadListAndCount(this.brokenOrganizationId);
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyEmployeeCategoryForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:240-264) ----
  edit(employeeCategoryId: number): void {
    this.saveMode.set(false); // ctrl:242-243, set synchronously BEFORE the fetch resolves
    this.api.getById(employeeCategoryId, this.brokenOrganizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error('Error!'); // ctrl:259-261 -- a native `alert()` in legacy, NOT growl; kept as
          // a notify.error() here rather than a real `alert()` popup, matching this
          // codebase's established convention of not reproducing native alert() calls
          // as blocking browser dialogs (see docs/ANGULAR_PENDING_FINDINGS.md).
          return;
        }
        const d = res.listObject[0];
        this.form.set({ employeeCategoryId: d.employeeCategoryId, employeeCode: d.employeeCategoryCode, employeeDesc: d.employeeCategoryDescription });
      },
    });
  }

  // ---- update (mirrors updateEmployee, ctrl:309-356 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:323 guard (no else branch)
    const f = this.form();
    this.api
      .update({
        employeeCategoryCode: f.employeeCode,
        employeeCategoryDescription: f.employeeDesc,
        employeeCategoryId: f.employeeCategoryId as number,
        organizationId: this.brokenOrganizationId, // ctrl:319 -- see class doc comment
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:347-351 -- reload runs UNCONDITIONALLY, same as save() above.
          this.resetForm();
          this.loadListAndCount(this.brokenOrganizationId);
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:267-306) ----
  toggleStatus(row: EmployeeCategoryRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.employeeCategoryId === row.employeeCategoryId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        employeeCategoryId: row.employeeCategoryId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:288 -- success calls $scope.init(), which resets the
            // create/update form and saveBtnFlag/updateBtnFlag -- a real,
            // previously-uncatalogued bug: any in-progress edit is silently
            // discarded by an unrelated row's status toggle. NOT a list
            // reload (init() never calls initEmployeeCategoryMasterList).
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:182-184/268-270/310-312. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
