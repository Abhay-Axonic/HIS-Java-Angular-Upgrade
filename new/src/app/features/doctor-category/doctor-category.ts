import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DoctorCategoryService } from './doctor-category.service';
import { DoctorCategoryForm, DoctorCategoryRow, emptyDoctorCategoryForm } from './doctor-category.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/doctor-category.html +
 * resources/assets/js/controller/master/global/billing/doctorCategoryController.js
 * (`doctorCategoryController`, no `controllerAs`). Route guard: moduleId 453
 * (app.js:8055-8074 resolve block), wired at the route level, not here.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - Save vs. Update is asymmetric, same family as country-master's own
 *   save/update split: clicking Save (`saveBtnFlag`, ctrl:198-256)
 *   validates, then opens the shared "Are you sure you want to Save?"
 *   confirm modal before actually POSTing (only the modal's Ok ->
 *   `PopupOkBtn()`, ctrl:192-195, re-runs `saveDoctorCategory()` with
 *   `popUpFlag` now false, which is when the real POST fires). Clicking
 *   Update (`updateBtnFlag`, ctrl:328-376) validates and POSTs
 *   immediately, with NO confirm step at all -- not a copy-paste
 *   omission, legacy genuinely has no popup wired to
 *   `updateDoctorCategory()`.
 *
 * - **The shared `<modal>` directive's x close button is a real crash for
 *   THIS screen, reproduced faithfully here, not fixed.**
 *   `common/commonPopUpDirectives.js:10` hardcodes the x button as
 *   `ng-click="cancel();stopLoader();"`. `doctorCategoryController.js`
 *   never defines `$scope.cancel` anywhere in its 377 lines (confirmed),
 *   and it isn't on `$rootScope` either (confirmed: no `$rootScope.cancel`
 *   anywhere in app.js) -- so clicking x throws `cancel is not a
 *   function` instead of closing the modal. This is a CROSS-CUTTING bug
 *   in the shared directive (used by many screens, not just this one);
 *   reproduced here via a direct `throw` (see `crashOnModalClose()`),
 *   following this project's established convention for
 *   unreproducible-in-TS legacy crashes (e.g.
 *   linen-issue-acceptance-details.ts's `rejectionPopup()`). The modal's
 *   own real footer buttons keep working: legacy's `id="cancel"` button
 *   only calls `stopLoader()` (no `cancel()`), and its `id="ok"` button
 *   calls the real `PopupOkBtn()` -- both reproduced as normal, working
 *   `closeConfirmModal()`/`confirmSave()` handlers.
 *
 * - `showUpdateBtn` (ctrl:259-285) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETDOCTORCATEGORYMASTERBYID` call
 *   resolves, and uses a raw `alert("Error!!")` on a failed response --
 *   the only place in this controller that doesn't use `growl`. Both
 *   reproduced verbatim (`saveMode.set(false)` up front; `window.alert`
 *   on failure).
 *
 * - `updateStatus` (ctrl:288-325) calls `$scope.init()` on success (which
 *   resets `doctorCategory`/`saveBtnFlag`/`updateBtnFlag` back to blank
 *   Save-mode) but never calls `initCountryMasterList`/
 *   `getCountryMasterList` afterward on EITHER branch -- toggling a row's
 *   status never re-fetches the list from the server, and never reverts
 *   on failure either (the checkbox's own two-way binding already
 *   flipped before this fires, and there is no revert logic in
 *   `updateStatus` unlike some other screens' 406 handlers). Reproduced
 *   by patching the `list` signal locally, resetting the form on success
 *   only, and never reloading or reverting.
 *
 * - Both `saveDoctorCategory` and `updateDoctorCategory` build
 *   `createdDate`/`updatedDate` client-side via
 *   `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')` (ctrl:199-201,
 *   289-291, 329-331) rather than letting the server stamp them -- kept
 *   via the same local `formatDmyHms()` used elsewhere in this rebuild
 *   (e.g. country-master.ts's `formatDateTime`).
 *
 * - The header search input + icon (view lines 69-71) and the per-column
 *   sort caret icons (view lines 78-120) carry no `ng-model`/`ng-change`/
 *   `ng-click` anywhere in the view or controller -- confirmed dead UI,
 *   not wired to `$scope.searchKeyword` (declared at ctrl:54 but never
 *   read or bound anywhere else). Rendered as inert, disabled markup here
 *   rather than given working behavior this rebuild would be inventing.
 */
@Component({
  selector: 'app-doctor-category',
  imports: [FormsModule, StatusToggle],
  templateUrl: './doctor-category.html',
  styleUrl: './doctor-category.scss',
})
export class DoctorCategory {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DoctorCategoryService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.doctorCategory + saveBtnFlag/updateBtnFlag) ----
  form = signal<DoctorCategoryForm>(emptyDoctorCategoryForm());
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + pagination state (mirrors initCountryMasterList/setPage) ----
  list = signal<DoctorCategoryRow[]>([]);
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

  updateField<K extends keyof DoctorCategoryForm>(field: K, value: DoctorCategoryForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.docCategoryCode.trim() && f.docCategoryDesc.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors saveDoctorCategory's
  // popUpFlag-gated double call via PopupOkBtn, ctrl:192-256). ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  /**
   * Faithful crash replication of the shared modal directive's x close
   * button -- see class doc comment. Wired to the x button only; the
   * modal's real Cancel/Ok buttons use `closeConfirmModal()`/`confirmSave()`
   * and keep working normally.
   */
  crashOnModalClose(): void {
    throw new TypeError('cancel is not a function');
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.create();
  }

  private create(): void {
    const f = this.form();
    const now = formatDmyHms(new Date());
    this.api
      .save({
        doctorCategoryCode: f.docCategoryCode,
        doctorCategoryDescription: f.docCategoryDesc,
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
  // updateDoctorCategory() being wired directly to the Update button. ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const f = this.form();
    this.api
      .update({
        doctorCategoryCode: f.docCategoryCode,
        doctorCategoryDescription: f.docCategoryDesc,
        doctorCategoryId: f.doctorCategoryId as number,
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
    this.form.set(emptyDoctorCategoryForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn -- flips into update mode BEFORE the
  // GETDOCTORCATEGORYMASTERBYID response arrives, and uses a raw alert()
  // on failure; both preserved verbatim; see class comment). ----
  edit(row: DoctorCategoryRow): void {
    this.saveMode.set(false);
    this.api.getById(row.doctorCategoryId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            doctorCategoryId: detail.doctorCategoryId,
            docCategoryCode: detail.doctorCategoryCode,
            docCategoryDesc: detail.doctorCategoryDescription,
          });
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- patches the list in place,
  // resets the add/edit form on success only, and never reloads or
  // reverts on failure; see class comment). ----
  toggleStatus(row: DoctorCategoryRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.doctorCategoryId === row.doctorCategoryId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        doctorCategoryId: row.doctorCategoryId,
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
}

function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
