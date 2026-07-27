import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BloodBankMasterService } from './blood-bank-master.service';
import { BloodBankForm, BloodBankRow, emptyBloodBankForm } from './blood-bank-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/UnitMaster/bloodBank/blood-bank-master.html
 * + resources/assets/js/controller/bloodBank/bloodBankMasterController.js
 * (382 lines, `bloodBankMasterController`). State `bloodBankMaster` is
 * registered in `app.js:16492` with a `resolve` block gating on module id
 * **742** -- confirmed directly from the live route config.
 *
 * Confirmed real, live CRUD during Step 0-2, all 6 calls under
 * `/bloodBank/*`: list + count (ctrl:48-86, real POST with a body,
 * built from the function's OWN parameters -- genuinely working
 * pagination, unlike this batch's APmaster), save (ctrl:136-225),
 * edit-load (ctrl:232-260), update (ctrl:263-336), status toggle
 * (ctrl:342-378) -- see blood-bank-master.service.ts.
 *
 * ================================================================
 * Hardcoded organizationId/unitId -- FIXED here (owner decision,
 * 2026-07-27), not faithfully reproduced
 * ================================================================
 * Legacy (ctrl:20-21) sets `$scope.organizationId = 1; $scope.unitId = 1;`
 * as literal constants -- confirmed by direct source read, never read from
 * `cookieObject` at all, unlike every sibling screen in this batch. Not
 * independently live-distinguishable in this single-org/single-unit dev
 * environment (both values coincidentally match the hardcoded ones), so
 * this is a silent/invisible bug today -- a real multi-tenant deployment
 * would misattribute every save to org/unit 1 regardless of who's
 * actually logged in, with no visible symptom to the user. Per the
 * owner's explicit decision, corrected here: `organizationId`/`unitId`
 * are read from the real session, same as every other screen in this
 * batch, not hardcoded.
 *
 * ================================================================
 * Manual field-by-field validation (not just HTML5 `required`) --
 * reproduced verbatim
 * ================================================================
 * `save()` (ctrl:139-166) and `update()` (ctrl:266-293) both do explicit
 * JS checks on all 4 fields (Code/Description/Address/Contact No) with
 * their own `growl.error(...)` messages BEFORE the real HTML5 `required`
 * attributes would even block a native submit -- reproduced as an
 * explicit per-field check here rather than relying on HTML5 validity
 * alone, matching the exact real messages.
 *
 * ================================================================
 * Confirm-modal fix -- applied here too (owner decision, 2026-07-27),
 * now live-confirmed (previously only inferred from code shape)
 * ================================================================
 * `save()` has the identical `popUpFlag`-never-rearms shape as this
 * batch's other screens -- live-verified during Step 2 (2026-07-26): a
 * second, independent save with no failure in between skips the confirm
 * modal entirely. Fixed here via `popupArmed`. `update()` has NO
 * confirm-modal gate in legacy -- fires immediately; reproduced as-is.
 */
@Component({
  selector: 'app-blood-bank-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './blood-bank-master.html',
  styleUrl: './blood-bank-master.scss',
})
export class BloodBankMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BloodBankMasterService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<BloodBankForm>(emptyBloodBankForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- FIXED here, see class doc

  // ---- list + real, working server-side pagination (mirrors initMasterList/masterList/setPage) ----
  list = signal<BloodBankRow[]>([]);
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
    this.organizationId = this.session.organizationId ?? 0; // owner-approved fix, see class doc
    this.unitId = this.session.unitId ?? 0; // owner-approved fix, see class doc
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
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

  updateField<K extends keyof BloodBankForm>(field: K, value: BloodBankForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors save()/update()'s explicit per-field checks (ctrl:139-166,
  // 266-293) -- same 4 fields, same order, same real growl messages.
  private validate(): boolean {
    const f = this.form();
    if (!f.bloodBankCode?.trim()) {
      this.notify.error('Please Enter Code ');
      return false;
    }
    if (!f.bloodBankDesc?.trim()) {
      this.notify.error('Please Enter Description ');
      return false;
    }
    if (!f.address?.trim()) {
      this.notify.error('Please Enter Address ');
      return false;
    }
    if (!f.contactNo?.trim()) {
      this.notify.error('Please Enter Contact No ');
      return false;
    }
    return true;
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().bloodBankCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().bloodBankDesc.trim();
  }

  isAddressInvalid(): boolean {
    return this.formSubmitted() && !this.form().address.trim();
  }

  isContactNoInvalid(): boolean {
    return this.formSubmitted() && !this.form().contactNo.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:130-225) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.validate()) return;
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
        bloodBankCode: f.bloodBankCode,
        bloodBankDesc: f.bloodBankDesc,
        address: f.address,
        contactNo: f.contactNo,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
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
    this.form.set(emptyBloodBankForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:232-260 -- ARRAY-wrapped GET
  // response, listObject[0]) ----
  edit(row: BloodBankRow): void {
    this.loader.start();
    this.api.getById(row.bloodBankId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        const record = res.listObject?.[0];
        if (res.status === 'success' && record) {
          this.saveMode.set(false);
          this.form.set({
            bloodBankId: record.bloodBankId,
            bloodBankCode: record.bloodBankCode,
            bloodBankDesc: record.bloodBankDesc,
            address: record.address,
            contactNo: record.contactNo,
          });
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update, ctrl:263-336 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.validate()) return;
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        bloodBankId: f.bloodBankId as number,
        bloodBankCode: f.bloodBankCode,
        bloodBankDesc: f.bloodBankDesc,
        address: f.address,
        contactNo: f.contactNo,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
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

  // ---- status toggle (mirrors updateStatus, ctrl:342-378 -- DOES reload
  // the list on success) ----
  toggleStatus(row: BloodBankRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.loader.start();
    this.api
      .updateStatus({
        bloodBankId: row.bloodBankId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
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
