import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BloodBagTypeMasterService } from './blood-bag-type-master.service';
import { BloodBagTypeForm, BloodBagTypeRow, emptyBloodBagTypeForm } from './blood-bag-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/UnitMaster/bloodBank/blood-bag-type-master.html
 * + resources/assets/js/controller/bloodBank/bloodBagTypeMasterController.js
 * (405 lines, `bloodBagTypeMasterController`). State `bloodBagType` is
 * registered in `app.js:16263` with a `resolve` block gating on module id
 * **731** -- confirmed directly from the live route config.
 *
 * Confirmed real, live CRUD during Step 0-2 (2026-07-27), all 7 calls
 * under `/global/bloodBank/*` (NOT `/bloodBank/*` -- that prefix belongs
 * to the unrelated `bloodBankMaster` screen from batch 22): list + count
 * (ctrl:48-86, real POST with a body built from the function's OWN
 * parameters -- genuinely working pagination), save (ctrl:126-225),
 * edit-load (ctrl:232-260), update (ctrl:263-336), status toggle
 * (ctrl:342-378, DOES reload the list) -- see blood-bag-type-master.service.ts.
 *
 * Legacy backend fix already merged to `main` on this batch's timestamp
 * sweep, `fix/bloodbagtype-timestamp-swapped-pattern`
 * (`BloodBagTypeMasterDaoImpl.java`'s date-parse pattern was
 * `"dd-M-yyyy HH:ss:mm"`, swapping minutes/seconds -- corrected to
 * `"dd-M-yyyy HH:mm:ss"`). No rebuild-side action needed; the frontend
 * always sent a correctly-formatted string, only the backend's PARSE of
 * it was wrong.
 *
 * ================================================================
 * Duplicate-check: description-only, case-insensitive, NOT org/unit-
 * scoped, NOT code-checked -- faithfully reproduced (real legacy bug)
 * ================================================================
 * Confirmed at the source level: `BloodBagTypeMaster.java`'s
 * `GET_BLOOD_BAG_TYPE_MASTER_BY_NAME`/`_NOT_ID` named queries
 * (`WHERE LOWER(bloodBagTypeMaster.bloodBagTypeDesc) = :bloodBagTypeDesc`)
 * carry NO `organizationId`/`unitId` predicate and never reference
 * `bloodBagTypeCode` at all -- the same systemic "missing org/unit scope
 * + code-blind" defect class documented project-wide (33/38 screens
 * sampled). Live-verified 2026-07-27: created `BBT1`/"Bag Type One",
 * then `BBT2` with a DIFFERENT code but the SAME description --
 * correctly (per this bug) rejected as a duplicate. This is a real,
 * directly-observable legacy defect (Save either succeeds or shows
 * "Already Exist"), not a silent/invisible one, so per standing policy
 * it is faithfully reproduced here, not fixed -- the rebuild's own
 * duplicate-detection (via the save response) mirrors this exactly, no
 * client-side pre-check is added. The org/unit-unscoped half is not
 * independently live-reproducible in this single-org/single-unit dev
 * environment (same caveat as `bloodBankMaster`'s over-blocking finding,
 * batch 22).
 *
 * ================================================================
 * Validation message text differs by one space between Save and Update
 * -- reproduced verbatim, not normalized
 * ================================================================
 * `save()`'s Capacity Max message (ctrl:164) is
 * `"Please Enter  Max Capacity "` (TWO spaces after "Enter"); `update()`'s
 * (ctrl:302) is `"Please Enter Max Capacity "` (one space) -- confirmed
 * via a byte-level grep, not a transcription error. Reproduced exactly
 * as two distinct literal strings below, not unified into one.
 *
 * ================================================================
 * Confirm-modal fix -- applied here too (same defect as every other
 * screen in this batch), live-confirmed 2026-07-27
 * ================================================================
 * `save()` has the identical `popUpFlag`-never-rearms shape as this
 * batch's other screens -- `popUpFlag` starts `true`, `PopupOkBtn()`
 * sets it `false` and never anywhere resets it back to `true` (not in
 * `initMaster()`, not on a later failure). Live-verified: after the
 * first confirmed Save, a second Save (even one that gets rejected as a
 * duplicate) shows no confirm modal at all for the rest of the page
 * session. Fixed here via `popupArmed`, rearmed on success AND on a
 * failed save (matching this batch's established pattern). `update()`
 * has NO confirm-modal gate in legacy; reproduced as-is.
 */
@Component({
  selector: 'app-blood-bag-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './blood-bag-type-master.html',
  styleUrl: './blood-bag-type-master.scss',
})
export class BloodBagTypeMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BloodBagTypeMasterService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<BloodBagTypeForm>(emptyBloodBagTypeForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- FIXED here, see class doc

  // ---- list + real, working server-side pagination (mirrors initMasterList/masterList/setPage) ----
  list = signal<BloodBagTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  // Mirrors view:`commonList|orderBy:column:reverse|filter:searchKeyword` --
  // a client-side filter over the CURRENT page's rows only, not the full list.
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) =>
      [r.bloodBagTypeCode, r.bloodBagTypeDesc, r.itemName, String(r.capacityMax), String(r.capacityMin)]
        .join(' ')
        .toLowerCase()
        .includes(keyword),
    );
  });

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.organizationId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
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

  updateField<K extends keyof BloodBagTypeForm>(field: K, value: BloodBagTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors save() (ctrl:139-176) / update() (ctrl:277-314)'s explicit
  // per-field checks, same 5 fields, same order. The Capacity Max message
  // genuinely differs by one space between the two call sites -- see class doc.
  private validate(forUpdate: boolean): boolean {
    const f = this.form();
    if (!f.bloodBagTypeCode?.trim()) {
      this.notify.error('Please Enter Blood Bag Type Code');
      return false;
    }
    if (!f.bloodBagTypeDesc?.trim()) {
      this.notify.error('Please Enter Blood Bag Type Description');
      return false;
    }
    if (!f.itemName?.trim()) {
      this.notify.error('Please Enter Item Name ');
      return false;
    }
    if (!f.capacityMax?.trim()) {
      this.notify.error(forUpdate ? 'Please Enter Max Capacity ' : 'Please Enter  Max Capacity ');
      return false;
    }
    if (!f.capacityMin?.trim()) {
      this.notify.error('Please Enter Min Capacity');
      return false;
    }
    return true;
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().bloodBagTypeCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().bloodBagTypeDesc.trim();
  }

  isItemNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().itemName.trim();
  }

  isCapacityMaxInvalid(): boolean {
    return this.formSubmitted() && !this.form().capacityMax.trim();
  }

  isCapacityMinInvalid(): boolean {
    return this.formSubmitted() && !this.form().capacityMin.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:120-225) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.validate(false)) return;
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
        bloodBagTypeCode: f.bloodBagTypeCode,
        bloodBagTypeDesc: f.bloodBagTypeDesc,
        itemName: f.itemName,
        capacityMax: f.capacityMax,
        capacityMin: f.capacityMin,
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
            this.popupArmed.set(true); // fixed here, see class doc
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyBloodBagTypeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:232-260 -- ARRAY-wrapped GET
  // response, listObject[0]) ----
  edit(row: BloodBagTypeRow): void {
    this.loader.start();
    this.api.getById(row.bloodBagTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        const record = res.listObject?.[0];
        if (res.status === 'success' && record) {
          this.saveMode.set(false);
          this.form.set({
            bloodBagTypeId: record.bloodBagTypeId,
            bloodBagTypeCode: record.bloodBagTypeCode,
            bloodBagTypeDesc: record.bloodBagTypeDesc,
            itemName: record.itemName,
            capacityMax: String(record.capacityMax),
            capacityMin: String(record.capacityMin),
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
    if (!this.validate(true)) return;
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        bloodBagTypeId: f.bloodBagTypeId as number,
        bloodBagTypeCode: f.bloodBagTypeCode,
        bloodBagTypeDesc: f.bloodBagTypeDesc,
        itemName: f.itemName,
        capacityMax: f.capacityMax,
        capacityMin: f.capacityMin,
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
  toggleStatus(row: BloodBagTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.loader.start();
    this.api
      .updateStatus({
        bloodBagTypeId: row.bloodBagTypeId,
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
