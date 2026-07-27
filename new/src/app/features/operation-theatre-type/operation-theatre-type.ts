import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { OperationTheatreTypeService } from './operation-theatre-type.service';
import {
  OperationTheatreTypeForm,
  OperationTheatreTypeRow,
  emptyOperationTheatreTypeForm,
} from './operation-theatre-type.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/OTIMS/operation-theatre-type.html
 * + operationTheatreTypeController.js (325 lines). State
 * `operationTheatreType` is registered with a `resolve` block gating on
 * module id **521** -- confirmed directly from the live route config.
 *
 * Confirmed real, live CRUD during Step 0-2, all 6 calls under
 * `/api/otims/*`: list + count (ctrl:59-98), save (ctrl:147-202),
 * edit-load (ctrl:204-231), update (ctrl:234-283), status toggle
 * (ctrl:289-322) -- see operation-theatre-type.service.ts.
 *
 * ================================================================
 * Duplicate-check baseline -- built against the CORRECTED backend
 * ================================================================
 * `fix/operationtheatretype-name-check` (merged, live-verified this
 * session both pre- and post-fix) -- the duplicate-check now correctly
 * compares both `theatreTypeName` and `theatreTypeDesc`, not just Desc.
 * This rebuild's Step 3 was explicitly held until that merge, per the
 * owner's instruction.
 *
 * ================================================================
 * update() has NO client-side validation at all -- faithfully
 * reproduced, NOT fixed
 * ================================================================
 * `save()` (ctrl:147-202) gates on `$scope.operationTheatreTypeform.$valid`
 * (ctrl:166) before either opening the confirm modal or firing the POST.
 * `update()` (ctrl:234-283) has the IDENTICAL check commented out
 * (ctrl:261,283, `/*if ($scope.operationTheatreTypeform.$valid) {*\/`) --
 * confirmed by direct source read during Step 2. An update can be
 * submitted with empty required fields with no client-side block at all.
 * Reproduced exactly: `save()` validates below, `update()` does not.
 *
 * ================================================================
 * Confirm-modal fix -- applied here too (owner decision, 2026-07-27),
 * live-confirmed (previously only inferred from code shape)
 * ================================================================
 * `save()` has the identical `popUpFlag`-never-rearms shape as this
 * batch's other screens -- live-verified during Step 2 (2026-07-26).
 * Fixed here via `popupArmed`. `update()` has NO confirm-modal gate in
 * legacy -- fires immediately; reproduced as-is (on top of also having no
 * validation, see above).
 *
 * ================================================================
 * Status toggle -- does NOT reload the list on success (unlike this
 * batch's other 4 screens)
 * ================================================================
 * `updateStatus()` (ctrl:289-322) only ever growls a toast on success or
 * error -- no `initMasterList()`/reload call in either branch, confirmed
 * by direct source read. Reproduced via an optimistic local patch (list
 * signal updated in place), matching this codebase's cardTypeMaster
 * precedent for the identical "toggle never refreshes" shape -- NOT a
 * real reload.
 */
@Component({
  selector: 'app-operation-theatre-type',
  imports: [FormsModule, StatusToggle],
  templateUrl: './operation-theatre-type.html',
  styleUrl: './operation-theatre-type.scss',
})
export class OperationTheatreType {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(OperationTheatreTypeService);

  private orgId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<OperationTheatreTypeForm>(emptyOperationTheatreTypeForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- FIXED here, see class doc

  // ---- list + real server-side pagination (mirrors initMasterList/masterList/setPage) ----
  list = signal<OperationTheatreTypeRow[]>([]);
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

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.orgId, offset, this.pageSize(), this.unitId).subscribe({
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

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof OperationTheatreTypeForm>(field: K, value: OperationTheatreTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors save()'s $valid gate (ctrl:166) -- the 3 HTML5 `required`
  // fields. NOT called by update() -- see class doc.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.theatreTypeCode.trim() && f.theatreTypeName.trim() && f.theatreTypeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().theatreTypeCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().theatreTypeName.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().theatreTypeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:141-202) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // mirrors ctrl:166's $valid gate
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
        theatreTypeCode: f.theatreTypeCode,
        theatreTypeDesc: f.theatreTypeDesc,
        theatreTypeName: f.theatreTypeName,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        status: 'A',
        unitId: this.unitId,
        orgId: this.orgId,
        updatedDate: now,
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
    this.form.set(emptyOperationTheatreTypeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:204-231 -- ARRAY-wrapped GET
  // response, listObject[0]) ----
  edit(row: OperationTheatreTypeRow): void {
    this.loader.start();
    this.api.getById(row.operationTheatreTypeId, this.orgId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        const record = res.listObject?.[0];
        if (res.status === 'success' && record) {
          this.saveMode.set(false);
          this.form.set({
            operationTheatreTypeId: record.operationTheatreTypeId,
            theatreTypeCode: record.theatreTypeCode,
            theatreTypeName: record.theatreTypeName,
            theatreTypeDesc: record.theatreTypeDesc,
          });
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update, ctrl:234-283 -- NO confirm modal AND NO
  // client-side validation at all, see class doc) ----
  update(): void {
    // Deliberately NOT gated on requiredFieldsValid() -- legacy's own
    // $valid check is commented out here (ctrl:261,283), confirmed by
    // direct source read. Submits whatever is in the form, even empty
    // required fields.
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        operationTheatreTypeId: f.operationTheatreTypeId as number,
        theatreTypeCode: f.theatreTypeCode,
        theatreTypeDesc: f.theatreTypeDesc,
        theatreTypeName: f.theatreTypeName,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: formatDateTime(new Date()),
        status: 'A',
        unitId: this.unitId,
        orgId: this.orgId,
        updatedDate: formatDateTime(new Date()),
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

  // ---- status toggle (mirrors updateStatus, ctrl:289-322 -- does NOT
  // reload the list on success, see class doc) ----
  toggleStatus(row: OperationTheatreTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, applied BEFORE the API call resolves -- legacy's
    // `ng-model="info.status"` checkbox is two-way bound, so the visual
    // change happens immediately regardless of what the response turns
    // out to be, and there's no reload on either branch (ctrl:289-322,
    // confirmed by direct source read). Matches this codebase's
    // cardTypeMaster precedent for the identical shape.
    this.list.update((rows) =>
      rows.map((r) => (r.operationTheatreTypeId === row.operationTheatreTypeId ? { ...r, status: nextStatus } : r)),
    );
    this.loader.start();
    this.api
      .updateStatus({
        operationTheatreTypeId: row.operationTheatreTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully');
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
