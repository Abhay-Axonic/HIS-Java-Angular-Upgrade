import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ShortLeaveReasonMasterService } from './short-leave-reason-master.service';
import { ShortLeaveReasonFormState, ShortLeaveReasonRow, emptyShortLeaveReasonForm } from './short-leave-reason-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/Adt/short-leave-Reasons-master.html
 * + resources/assets/js/controller/adt/global/shortLeaveReasonMasterController.js.
 * State `shortLeaveReasonMaster` (app.js:4143-4168), moduleId 406, no
 * `controllerAs` (plain `$scope`).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - Save uses the shared `<modal>` confirm-save directive
 *   (commonPopUpDirectives.js) exactly like asset-category-master's /
 *   bed-category-master's Create flow; Update has no confirm step at all --
 *   `updateShortLeaveReason()` (ctrl:335) posts directly. Reproduced as two
 *   genuinely different flows, matching legacy's two separate functions.
 * - Genuine legacy quirk, reproduced as-is: after a FAILED save (server
 *   returns status != 'success', i.e. a duplicate code/name -- ctrl:243-251),
 *   `$scope.init()` (which resets `popUpFlag = true`, ctrl:54) is only
 *   called on the SUCCESS branch. `popUpFlag` stays `false` from the
 *   `PopupOkBtn()` click that just ran (ctrl:193-196), so the NEXT click of
 *   the Save button skips the confirm modal entirely and saves directly
 *   (ctrl:216-220's `if ($scope.popUpFlag)` branch is now false). Modeled
 *   here via the `popUpFlag` signal, which is only reset to `true` on a
 *   successful save or on Cancel/&times; -- both of those reset it in
 *   legacy too, via the `<modal>` directive's own `$watch`
 *   (commonPopUpDirectives.js:38-50).
 * - `showUpdateBtn()`/`edit()`'s loading indicator: legacy calls
 *   `$rootScope.startLoader()` unconditionally but only calls
 *   `stopLoader()` on the SUCCESS branch (ctrl:264-291) -- a real bug that
 *   would leave the single shared loader overlay stuck forever on any
 *   edit-fetch failure. Not reproduced: `LoaderService`'s overlay blocks
 *   the whole app, and leaving it stuck is a worse regression than simply
 *   not wiring the loader for this one call -- consistent with
 *   bed-category-master's `edit()`, which also doesn't use the loader.
 * - The edit (pencil) icon has no status/disabled gating at all in
 *   legacy's view (unlike asset-category-master's inactive-row quirk) --
 *   inactive rows remain editable here, reproduced as-is.
 * - The search box (`filter:searchKeyword`, view:145) filters only the
 *   CURRENT server-fetched PAGE (`shortLeaveReasonList`), not the full
 *   dataset -- pagination is server-side (offset/noOfRecordsPerPage).
 *   Reproduced via `displayedRows()`, matching bed-category-master's
 *   identical quirk.
 */
@Component({
  selector: 'app-short-leave-reason-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './short-leave-reason-master.html',
  styleUrl: './short-leave-reason-master.scss',
})
export class ShortLeaveReasonMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(ShortLeaveReasonMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.shortleavereason) ----
  form = signal<ShortLeaveReasonFormState>(emptyShortLeaveReasonForm());
  formSubmitted = signal(false);
  saveBtnFlag = signal(false); // true while editing -> hides Save
  updateBtnFlag = signal(true); // true while adding -> hides Update

  showConfirmModal = signal(false);
  // See class doc comment: only reset to true on a successful save or on
  // Cancel/close -- NOT reset after a failed (duplicate) save.
  private popUpFlag = signal(true);

  // ---- list + pagination (server-driven, mirrors PagerService usage) ----
  list = signal<ShortLeaveReasonRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters only the CURRENT server-fetched
  // page, not the full dataset (see class doc comment).
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) =>
      Object.values(r).some((v) => v != null && typeof v !== 'object' && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  updateField<K extends keyof ShortLeaveReasonFormState>(field: K, value: ShortLeaveReasonFormState[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
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

  isRequiredMissing(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!f.shortLeaveReasonCode.trim() && !!f.shortLeaveReasonName.trim();
  }

  // ---- save (create) -- mirrors saveShortLeaveReason() (ctrl:198-262) ----
  saveShortLeaveReason(): void {
    this.formSubmitted.set(true);
    if (!this.validateRequired()) return;
    if (this.popUpFlag()) {
      this.showConfirmModal.set(true);
    } else {
      this.doSave();
    }
  }

  // ---- confirm-modal Ok button -- mirrors PopupOkBtn() (ctrl:193-196) ----
  popupOkBtn(): void {
    this.showConfirmModal.set(false);
    this.popUpFlag.set(false);
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
    this.popUpFlag.set(true);
  }

  private doSave(): void {
    this.loader.start();
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        shortLeaveReasonCode: f.shortLeaveReasonCode,
        shortLeaveReasonName: f.shortLeaveReasonName,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Short Leave Reason Added Successfully...!!');
            this.resetAfterSuccess();
          } else {
            this.notify.error('Short Leave Reason already exits...!!');
          }
          this.loadList();
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Something Went Wrong');
        },
      });
  }

  // Full $scope.init() equivalent for the success path -- resets the form
  // and popUpFlag (ctrl:47-54) and pagination back to page 1 / default size,
  // matching $scope.init()'s own offset/noOfRecordsPerPage re-init.
  private resetAfterSuccess(): void {
    this.form.set(emptyShortLeaveReasonForm());
    this.formSubmitted.set(false);
    this.saveBtnFlag.set(false);
    this.updateBtnFlag.set(true);
    this.popUpFlag.set(true);
    this.currentPage.set(1);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:264-291) ----
  edit(id: number): void {
    this.saveBtnFlag.set(true);
    this.updateBtnFlag.set(false);
    this.formSubmitted.set(false);
    this.api.getById(id).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          this.form.set({
            shortLeaveReasonId: d.shortLeaveReasonId,
            shortLeaveReasonCode: d.shortLeaveReasonCode,
            shortLeaveReasonName: d.shortLeaveReasonName,
          });
        } else {
          this.notify.error('Error!!');
        }
      },
      error: () => this.notify.error('Error!!'),
    });
  }

  // ---- update (mirrors updateShortLeaveReason, ctrl:335-387 -- no confirm modal) ----
  updateShortLeaveReason(): void {
    this.formSubmitted.set(true);
    if (!this.validateRequired()) return;

    this.loader.start();
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .update({
        shortLeaveReasonId: f.shortLeaveReasonId as number,
        shortLeaveReasonCode: f.shortLeaveReasonCode,
        shortLeaveReasonName: f.shortLeaveReasonName,
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Short Leave Reason Updated Successfully...!!');
            this.resetAfterSuccess();
          } else {
            this.notify.error('Short Leave Reason already exits...!!');
          }
          this.loadList();
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Something Went Wrong');
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:293-333 -- no
  // revert-on-failure, only a growl error; reproduced as-is) ----
  toggleStatus(row: ShortLeaveReasonRow): void {
    const status: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.shortLeaveReasonId === row.shortLeaveReasonId ? { ...r, status } : r)),
    );
    this.loader.start();
    this.api
      .updateStatus({
        shortLeaveReasonId: row.shortLeaveReasonId,
        status,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') this.notify.success('Short Leave Reason Updated Successfully...!!');
          else this.notify.error('Something went wrong...!!');
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Something went wrong...!!');
        },
      });
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
