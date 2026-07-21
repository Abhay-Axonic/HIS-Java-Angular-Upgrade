import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { MealPreferenceMasterService } from './meal-preference-master.service';
import { MealPreferenceForm, MealPreferenceRow, emptyMealPreferenceForm } from './meal-preference-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/meal-preference-master.html +
 * resources/assets/js/controller/adt/unit/mealPreferenceMasterController.js
 * (`mealPreferenceMasterController`, no `controllerAs` -- plain `$scope`).
 * State `mealPreferenceMaster` (app.js:8629-8657), moduleId 549 (resolve
 * block, confirmed live).
 *
 * Confirmed real, working CRUD: create (ctrl:194-251), update
 * (ctrl:325-374), edit-populate `showUpdateBtn` (ctrl:256-280), status
 * toggle `updateStatus` (ctrl:283-322) -- all wired to real
 * `/api/adt/unit/...` endpoints, see meal-preference-master.service.ts.
 *
 * **Deliberate quirks/bugs preserved, per audit + explicit rebuild decision:**
 * - **Dead search box, NOT wired to anything (real, confirmed bug,
 *   view:76-78):** legacy's search `<input>` carries no `ng-model`, no
 *   `ng-change`, no handler of any kind -- `$scope.searchKeyword` is
 *   declared (ctrl:53) but never read or written anywhere else in the
 *   file. This is inert markup, not even a crash-on-keystroke like this
 *   codebase's amenity-master/floor-master search boxes -- reproduced here
 *   as a genuinely unbound `<input>` with no model, no filter, and no
 *   event handler at all. Do not "improve" this into a working filter.
 * - Status vocabulary is `'A'`/`'I'` (view:135, `ng-true-value`/
 *   `ng-false-value`), matching this codebase's amenity-master/
 *   floor-master/qms-department-master convention -- unlike
 *   manufacturer-master's boolean vocabulary (status-vocabulary drift
 *   between sibling masters is expected and NOT normalized away).
 * - **No client-side duplicate-code check** -- unlike manufacturer-master's
 *   `ValidateCode`/`ValidateCodeForUpdate`, this screen relies entirely on
 *   the server's own response (`response.data.status !== 'success'`) to
 *   reject a duplicate `mealPreferenceCode`; no such check exists anywhere
 *   in this controller. Not added here; a duplicate-code rejection surfaces
 *   through the same `notify.error(res.message)` path as any other save
 *   failure.
 * - Save has a real, validation-GATED confirm-modal step (ctrl:211-216):
 *   unlike manufacturer-master/asset-category-master (whose confirm modal
 *   opens unconditionally on click, CSS-disabled look only), this screen's
 *   `$scope.mealPreferenceForm.$valid` check runs BEFORE the modal opens
 *   at all -- an invalid submit never shows the modal. Reproduced via
 *   `openConfirmModal()`'s `validateRequired()` guard below, matching this
 *   codebase's qms-department-master convention.
 * - Update (ctrl:325-374) has NO confirm-modal step -- posts directly on
 *   click, after the same required-field check.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close/× button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of legacy's shared
 *   `<modal>` directive's `cancel()`/`stopLoader()` crash
 *   (`commonPopUpDirectives.js:10,20` hardcodes `ng-click="cancel();
 *   stopLoader();"` on the × and `stopLoader();` on Cancel, and
 *   `mealPreferenceMasterController.js` defines neither) -- a working close
 *   is an accepted standing improvement, not a parity gap.
 * - **Status toggle success resets the form to create-mode but does NOT
 *   reload the row list** (ctrl:301-312: the success branch calls
 *   `$scope.init()`, which resets form/session-derived state but never
 *   calls `initBillingBedList()`) -- the row's own visible status flip
 *   relies entirely on the two-way-bound checkbox already having changed,
 *   same "looks right because of two-way binding, not an explicit reload"
 *   pattern as this codebase's amenity-master. Reproduced via an
 *   optimistic `list.update()` plus a form reset on success, with no
 *   `loadListAndCount()` call either way, and no revert-on-failure (legacy
 *   has none -- the failure branch only growls).
 * - `showUpdateBtn`'s failure path uses a **native `alert()`**
 *   (ctrl:276, `alert("Error!!")`), not `growl.error` like every other
 *   error path in this same controller -- a real, if inconsistent, choice
 *   in legacy. Reproduced verbatim via `window.alert(...)`, not silently
 *   upgraded to the notification service.
 * - Both create and update call `initBillingBedList()` (the list+count
 *   reload) UNCONDITIONALLY after the success/error branch (ctrl:243-247,
 *   365-369) -- even a server-reported failure reloads the list, though a
 *   genuine HTTP-level failure (never reaching legacy's single-argument
 *   `.then()`) would not. Reproduced by calling `loadListAndCount()` after
 *   both the success and (app-level) error branches of `confirmSave()`/
 *   `update()` below; a real HTTP failure shows a generic "Something Went
 *   Wrong" notice instead of reproducing total silence, matching this
 *   codebase's established convention (see amenity-master, bed-category-master).
 */
@Component({
  selector: 'app-meal-preference-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './meal-preference-master.html',
  styleUrl: './meal-preference-master.scss',
})
export class MealPreferenceMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(MealPreferenceMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.mealPrefMasterObj) ----
  form = signal<MealPreferenceForm>(emptyMealPreferenceForm());
  formSubmitted = signal(false);
  saveBtnFlag = signal(true); // ng-if="saveBtnFlag"
  updateBtnFlag = signal(false); // ng-if="updateBtnFlag"
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors PagerService.GetPager) ----
  list = signal<MealPreferenceRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initBillingBedList (ctrl:77-132): fetches list + count together,
  // always from offset 0, and forces the pager back to page 1 once the
  // count responds (`setPage(1,false)`, ctrl:126-128).
  private loadListAndCount(): void {
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors getBillingBedList (ctrl:134-162) via setPage(page,true): just
  // re-fetches the list at the new offset, no count refetch.
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof MealPreferenceForm>(field: K, value: MealPreferenceForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors mealPreferenceForm.$valid via
  // the 2 `required` inputs, view:16/31) ----
  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.mealPreferenceCode.trim() && f.mealPreferenceDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().mealPreferenceCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().mealPreferenceDesc.trim();
  }

  // ---- save (mirrors saveMealPrefMaster's first, validate-then-open-modal
  // call, ctrl:194-216) ----
  openConfirmModal(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // ---- confirm save (mirrors PopupOkBtn -> saveMealPrefMaster's second,
  // popUpFlag=false call, ctrl:188-251) ----
  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        mealPreferenceCode: f.mealPreferenceCode,
        mealPreferenceDesc: f.mealPreferenceDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
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
          // ctrl:243-247 -- reload runs unconditionally after success/error.
          this.loadListAndCount();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // ---- update (mirrors updateMealPrefMaster, ctrl:325-374 -- no confirm
  // modal, same required-field gate) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.validateRequired()) return;
    const f = this.form();
    this.api
      .update({
        mealPreferenceCode: f.mealPreferenceCode,
        mealPreferenceDesc: f.mealPreferenceDesc,
        mealPreferenceId: f.mealPreferenceId as number,
        unitId: this.unitId,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:365-369 -- reload runs unconditionally after success/error.
          this.loadListAndCount();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyMealPreferenceForm());
    this.formSubmitted.set(false);
    this.saveBtnFlag.set(true);
    this.updateBtnFlag.set(false);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:256-280) ----
  edit(row: MealPreferenceRow): void {
    this.saveBtnFlag.set(false);
    this.updateBtnFlag.set(true);
    this.api.getById(row.mealPreferenceId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          this.form.set({
            mealPreferenceId: d.mealPreferenceId,
            mealPreferenceCode: d.mealPreferenceCode,
            mealPreferenceDesc: d.mealPreferenceDesc,
          });
        } else {
          // ctrl:276 -- native alert(), not growl, unlike every other error
          // path in this controller. Reproduced verbatim; see class comment.
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:283-322) ----
  toggleStatus(row: MealPreferenceRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic update, matching legacy's two-way-bound checkbox flipping
    // immediately regardless of the response -- see class doc comment.
    this.list.update((rows) =>
      rows.map((r) => (r.mealPreferenceId === row.mealPreferenceId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        mealPreferenceId: row.mealPreferenceId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:305 -- `$scope.init()` resets the form/flags on success
            // but does NOT reload the list; see class doc comment.
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:195-197/284-286/326-328. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
