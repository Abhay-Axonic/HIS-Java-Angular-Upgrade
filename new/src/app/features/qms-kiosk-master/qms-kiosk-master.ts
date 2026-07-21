import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { QmsKioskMasterService } from './qms-kiosk-master.service';
import { QmsKioskRow } from './qms-kiosk-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/QMSMaster/QMS-kiosk-Master.html +
 * resources/assets/js/controller/qms/QMSkioskMasterController.js
 * (`QMSkioskMasterController`, no `controllerAs`). Route guard: moduleId
 * 795 (app.js:19994-20021 resolve block, confirmed live).
 *
 * Already-audited real CRUD (per this batch's brief): `save()` (ctrl:153),
 * `update()` (ctrl:232), edit-populate `showUpdateBtn()` (ctrl:198),
 * status toggle `updateStatus()` (ctrl:271), pagination `setPage()`
 * (ctrl:134) -- all real, all wired to
 * `/api/pharmacy/{getQMSMasterList,getQMSMasterCount,saveQMSMaster,
 * getQMSMasterById,updateQMSMaster,updateQMSMasterStatus}`.
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - `$scope.common.createdBy`/`updatedBy` (ctrl:52-53) are set from
 *   `$scope.createdBy`/`$scope.updatedBy`, which are never assigned
 *   anywhere in this controller -- always `undefined` in the real save
 *   payload. `updateStatus`'s `updatedBy` (ctrl:279) is the exact same
 *   always-undefined variable. Both are reproduced by omitting the field
 *   entirely (equivalent on the wire to `JSON.stringify` dropping an
 *   `undefined` value), NOT "fixed" to send the real userId -- contrast
 *   `QMSdepartmentMasterController`, which does that correctly.
 * - The view has a genuine copy-paste bug in its required-field error
 *   markup (view:11-14 vs. 25-28): the Code field's `<p class="help-block"
 *   ng-if="qmsForm.qmsDesc.$error.required">` checks the *Description*
 *   field's validity (and shows "Description is required" text) instead of
 *   its own -- never renamed after being duplicated from the Description
 *   field's identical block. The actual `qmsForm.$valid` gate used to allow
 *   the save/update POST to fire is unaffected (each input's own HTML5
 *   `required` still works correctly for that), only the *displayed* error
 *   hint is wrong: leaving Code empty while Description is filled shows NO
 *   error at all anywhere on screen, even though the button silently does
 *   nothing. Reproduced verbatim via one shared `descRequiredVisible`
 *   signal driving both fields' error paragraphs.
 * - `save()`/`update()`'s response handler (ctrl:167-188, 243-263) calls
 *   `$scope.initQMSMaster()` UNCONDITIONALLY at the end, regardless of
 *   whether `response.data.status` was `'success'` or `'error'` -- i.e. a
 *   failed save/update (business-level error, still HTTP 200) still fully
 *   clears the form and flips the button back to "Save" mode, exactly as
 *   a successful one does. Only the list reload and the
 *   `show-errors-reset` broadcast (which un-highlights the now-blanked
 *   required fields) are gated on success -- so a failed attempt leaves
 *   the form blank AND still visually flagged as invalid. Reproduced via
 *   `resetFields()` running in both branches, but `submitted.set(false)`
 *   (this rebuild's equivalent of `show-errors-reset`) only on success.
 * - Neither `save()` nor `update()` registers a `.catch` on its HTTP
 *   promise (ctrl:167-188, 243-263) -- a genuine transport-level failure
 *   (as opposed to a `{status:'error'}` business response, which is still
 *   a normal resolved promise) is silently swallowed: no growl, no form
 *   reset, nothing. Reproduced by giving `create()`/`update()`'s
 *   `.subscribe()` no `error` handler.
 * - `save()`'s confirm-modal step (the `popUpFlag`/`showModal` dance via
 *   the shared `<modal>` directive, re-entered through `PopupOkBtn`,
 *   ctrl:149-196) only exists for CREATE. `update()` (ctrl:232-269) runs
 *   the identical validation but POSTs immediately with no confirm step at
 *   all -- same asymmetry already established in this codebase's
 *   `country-master`/`ward-master`. Reproduced via `onSaveClick()` (opens
 *   the modal) vs. `onUpdateClick()` (saves directly).
 * - Legacy's confirm modal stays open (with the global loader spinning)
 *   until the actual save response comes back, only closing once
 *   `initQMSMaster()`'s `showModal = false` runs inside that response
 *   handler. This rebuild instead closes the modal synchronously on "Ok"
 *   click, before the request resolves -- the same normalization every
 *   other rebuilt screen in this codebase already makes (see
 *   `asset-category-master`/`country-master`'s `confirmSave()`), not a new
 *   deviation invented here.
 * - `showUpdateBtn` (ctrl:198-229) flips into update mode
 *   (`saveBtnFlag = false; updateBtnFlag = true`) and blanks the form
 *   SYNCHRONOUSLY, before the `getQMSMasterById` call resolves, and on a
 *   failed response has no fallback/reset branch at all -- the screen is
 *   left in "Update" mode with blank fields, referencing an id whose fetch
 *   failed. Both are reproduced verbatim in `edit()`.
 * - `updateStatus` (ctrl:271-304) relies on the legacy checkbox's
 *   `ng-model="info.status"` two-way binding directly against the row in
 *   `commonList`, which flips the value BEFORE the HTTP call is even
 *   fired -- and there is NO revert-on-error anywhere in this function,
 *   unlike this codebase's usual optimistic-update-with-revert pattern
 *   (e.g. `asset-category-master`'s 406 revert). Reproduced as an
 *   unconditional optimistic update with no revert branch: a failed status
 *   change shows an error toast but the toggle stays in its new position.
 * - The search box (`ng-repeat="info in commonList | filter:searchKeyword"`,
 *   view:95) filters only whatever page of data is currently loaded --
 *   this screen's list is genuinely server-paginated (`offset`/
 *   `noOfRecordsPerPage`, ctrl:67-107), so unlike `asset-category-master`'s
 *   "quirk" (a client-side-fetched-in-full list filtered by page only),
 *   here filtering the current page IS the entire available dataset
 *   client-side; there is nothing more to filter, matching this codebase's
 *   `country-master` precedent.
 * - Legacy's `$scope.offset` is initialized to `0` and never reassigned
 *   anywhere in this controller -- every reload triggered by save/update/
 *   page-size-change always refetches from offset 0 (page 1), while actual
 *   page-navigation clicks use a separate `pager.startIndex` computed
 *   value. This rebuild collapses both call sites into one `loadList()`
 *   driven by `currentPage`, reset to `1` wherever legacy's reload used a
 *   hardcoded offset of `0` (`reloadFirstPage()`) -- observably identical,
 *   matching this codebase's `country-master` precedent rather than
 *   keeping two near-duplicate fetch functions.
 */
@Component({
  selector: 'app-qms-kiosk-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './qms-kiosk-master.html',
  styleUrl: './qms-kiosk-master.scss',
})
export class QmsKioskMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(QmsKioskMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;

  // ---- form state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  qmsCode = signal('');
  qmsDesc = signal('');
  private editId = signal<number | undefined>(undefined);
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + pagination state (mirrors initQMSkioskMasterList/setPage) ----
  list = signal<QmsKioskRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Current page's rows only -- see class comment on why this is the whole
  // dataset available to filter, not a client-side-fetch-in-full quirk.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter(
      (r) => r.qmsCode?.toLowerCase().includes(keyword) || r.qmsDesc?.toLowerCase().includes(keyword),
    );
  });

  // Legacy bug (view:14): the Code field's own error paragraph checks
  // `qmsForm.qmsDesc.$error.required` instead of its own field -- both
  // paragraphs are really driven by Description's emptiness. Reproduced
  // via one shared signal for both.
  descRequiredVisible = computed(() => this.submitted() && !this.qmsDesc().trim());

  constructor() {
    this.loadList();
  }

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

  // ---- required-field validation (mirrors qmsForm.$valid gating) ----
  private validateRequired(): boolean {
    return !!(this.qmsCode().trim() && this.qmsDesc().trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors save()'s
  // popUpFlag-gated double call via PopupOkBtn). ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.create();
  }

  private create(): void {
    this.api
      .create({
        qmsKioskMasterId: '',
        qmsCode: this.qmsCode(),
        qmsDesc: this.qmsDesc(),
        organisationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.reloadFirstPage();
            this.submitted.set(false);
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.resetFields();
        },
        // legacy: no .catch registered on this promise chain (ctrl:167-188)
        // -- a genuine transport-level failure is silently swallowed here
        // too, see class comment.
      });
  }

  private resetFields(): void {
    this.qmsCode.set('');
    this.qmsDesc.set('');
    this.editId.set(undefined);
    this.saveMode.set(true);
  }

  // ---- update flow: no confirm modal at all, matching legacy's update()
  // being wired directly to the Update button. ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const id = this.editId();
    if (id == null) return;
    this.api
      .update({
        qmsKioskMasterId: id,
        qmsCode: this.qmsCode(),
        qmsDesc: this.qmsDesc(),
        organisationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.reloadFirstPage();
            this.submitted.set(false);
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.resetFields();
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn -- flips into update mode and blanks
  // the form BEFORE the getQMSMasterById response arrives, and has no
  // fallback/reset branch on failure, both preserved verbatim; see class
  // comment). ----
  edit(row: QmsKioskRow): void {
    this.saveMode.set(false);
    this.editId.set(row.qmsKioskMasterId);
    this.qmsCode.set('');
    this.qmsDesc.set('');
    this.api.getById(row.qmsKioskMasterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.editId.set(detail.qmsKioskMasterId);
          this.qmsCode.set(detail.qmsCode);
          this.qmsDesc.set(detail.qmsDesc);
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- optimistic, no revert on
  // error at all; see class comment). ----
  toggleStatus(row: QmsKioskRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.qmsKioskMasterId === row.qmsKioskMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api.updateStatus({ qmsKioskMasterId: row.qmsKioskMasterId, status: nextStatus }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }
}
