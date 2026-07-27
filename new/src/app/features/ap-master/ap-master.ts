import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ApMasterService } from './ap-master.service';
import { ApMasterForm, ApMasterRow, emptyApMasterForm } from './ap-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/UnitMaster/billingMaster/AP-master.html +
 * resources/assets/js/controller/billing/unit/aPmasterMasterController.js
 * (322 lines, `aPmasterMasterController`). State `APmaster` is registered
 * in `app.js:14917` with a `resolve` block gating on module id **617**
 * (`app.js`, `item.id == 617`) -- confirmed directly from the live route
 * config.
 *
 * Confirmed real, live CRUD during Step 0-2, all 6 calls under
 * `/global/billing/*`: list + count (ctrl:56-90, real GET with path
 * params, NOT POST with a body -- unlike this batch's shiftMaster/
 * dietUnitMater), save (ctrl:139-198), edit-load (ctrl:200-229), update
 * (ctrl:232-277), status toggle (ctrl:279-319) -- see ap-master.service.ts.
 *
 * ================================================================
 * Pagination is confirmed LIVE-BROKEN -- faithfully reproduced, NOT fixed
 * (docs/ANGULAR_PENDING_FINDINGS.md, batch 22 Step 2 golden-trace entry)
 * ================================================================
 * `initMasterList`/`masterList` (ctrl:56-90) accept `offset`/
 * `noOfRecordsPerPage` parameters, but build their actual request URL from
 * `$scope.offset`/`$scope.noOfRecordsPerPage` directly -- NOT their own
 * function parameters -- and nothing in the controller ever reassigns
 * `$scope.offset` after its initial value of `0`. Live-verified via direct
 * network capture (2026-07-26): called `masterList(1, 10, 10, 1)`
 * (explicit `offset=10`, simulating "page 2") -- the actual outgoing
 * request was still `.../getAPCodeMasterList/1/1/0/10`, offset `0`,
 * identical to page 1. No page past the first 10 records is EVER
 * reachable through this screen. Reproduced here by computing
 * `pageNumbers`/`totalPages` from the real `totalCount` (so the pager UI
 * looks normal and clickable, matching legacy's own visible page-number
 * buttons) but always fetching with `offset = 0` regardless of which page
 * was clicked -- the same broken end-to-end behavior, not a cosmetically-
 * simplified version of it (contrast with this batch's dietUnitMater,
 * whose pagination is so thoroughly non-functional -- no OFFSET/LIMIT in
 * the SQL at all -- that building fake pager controls wasn't worth it;
 * APmaster's bug is narrower and worth reproducing exactly, since a page
 * count IS real and visible, it's just never actually followed).
 *
 * ================================================================
 * Confirm-modal fix -- applied here too (owner decision, 2026-07-27)
 * ================================================================
 * `save()` (ctrl:139-198) has the identical `popUpFlag`-never-rearms shape
 * as this batch's other screens -- live-verified during Step 2
 * (2026-07-26). Fixed here via `popupArmed`, same shape as
 * shiftMaster.ts/diet-unit-master.ts. `update()` (ctrl:232-277) has NO
 * confirm-modal gate in legacy -- fires immediately; reproduced as-is.
 *
 * ================================================================
 * Edit-load `aPCodeId` is a STRING, not a number
 * ================================================================
 * `showUpdateBtn` (ctrl:200-229) does
 * `$scope.common.aPCodeId = response.data.listObject[0].aPCodeId.toString()`
 * -- confirmed live during Step 2. Modeled as `string | undefined` on
 * `ApMasterForm`, converted back to a number only where the wire payload
 * needs it (the status-toggle payload, which takes a raw `row.aPCodeId`
 * from the list, not the form).
 */
@Component({
  selector: 'app-ap-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ap-master.html',
  styleUrl: './ap-master.scss',
})
export class ApMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(ApMasterService);

  private orgId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<ApMasterForm>(emptyApMasterForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- FIXED here, see class doc

  // ---- list + BROKEN server-side pagination, faithfully reproduced (see class doc) ----
  list = signal<ApMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.orgId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  // ctrl:56-90 -- ALWAYS offset 0, regardless of currentPage, matching the
  // confirmed-live bug (see class doc). Not `(currentPage()-1)*pageSize()`
  // on purpose.
  private loadList(): void {
    this.loader.start();
    this.api.getList(this.orgId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.orgId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  // Updates the highlighted page number only -- does NOT change what's
  // fetched, matching the real broken behavior (see class doc).
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof ApMasterForm>(field: K, value: ApMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 2 HTML5 `required` fields (Code/Description) that back
  // `$scope.aPmasterForm.$valid` (no explicit `.$valid` gate found in
  // save()/update() -- ctrl relies on HTML5 `required` blocking form
  // submission via the browser itself; reproduced as an explicit check
  // here since there's no native form-submit event to rely on).
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.aPCode.trim() && f.aPCodeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().aPCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().aPCodeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:133-198) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    if (this.popupArmed()) {
      this.showConfirmModal.set(true);
    } else {
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.popupArmed.set(false); // mirrors PopupOkBtn's `$scope.popUpFlag = false;`
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        aPCode: f.aPCode,
        aPCodeDesc: f.aPCodeDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
        unitId: this.unitId,
        orgId: this.orgId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Added Successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
            this.popupArmed.set(true); // owner-approved fix, see class doc
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyApMasterForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:200-229 -- ARRAY-wrapped GET
  // response, listObject[0]; aPCodeId coerced to a STRING, see class doc) ----
  edit(row: ApMasterRow): void {
    this.loader.start();
    this.api.getById(row.aPCodeId, this.orgId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        const record = res.listObject?.[0];
        if (res.status === 'success' && record) {
          this.saveMode.set(false);
          this.form.set({
            aPCodeId: record.aPCodeId.toString(), // ctrl:224, verbatim .toString() coercion
            aPCode: record.aPCode,
            aPCodeDesc: record.aPCodeDesc,
          });
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update, ctrl:232-277 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        aPCodeId: f.aPCodeId as string,
        aPCode: f.aPCode,
        aPCodeDesc: f.aPCodeDesc,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        unitId: this.unitId,
        orgId: this.orgId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:279-319) ----
  toggleStatus(row: ApMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .updateStatus({
        aPCodeId: row.aPCodeId,
        status: nextStatus,
        createdDate: now, // ctrl:288, verbatim -- yes, a status toggle also sends createdDate
        updatedDate: now,
        unitId: this.unitId,
        orgId: this.orgId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully');
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
