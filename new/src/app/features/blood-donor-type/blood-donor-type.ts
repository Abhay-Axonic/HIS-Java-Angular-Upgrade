import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BloodDonorTypeService } from './blood-donor-type.service';
import { BloodDonorTypeForm, BloodDonorTypeRow, emptyBloodDonorTypeForm } from './blood-donor-type.models';

const PAGE_SIZES = [10, 25, 50, 100];

/**
 * Parity rebuild of
 * `views/master/UnitMaster/bloodBank/blood-donor-type-master.html` +
 * `resources/assets/js/controller/bloodBank/bloodDonorTypeMasterController.js`.
 * Full Step 1 audit: `docs/ANGULAR_PENDING_FINDINGS.md`, Batch 19.
 *
 * Module gate: id 732, confirmed against `adminPriviledges.json` and the
 * live `.state('bloodDonortype', ...)` resolve block.
 *
 * ================================================================
 * Validation -- toast-style, not inline (faithful)
 * ================================================================
 * `save()`/`update()` both run identical manual JS null/empty checks on
 * `bloodDonorTypeCode`/`bloodDonorTypeDesc`, surfaced as a `growl` error
 * toast ("Please Enter Blood Donor Type Code"/"...Description"), not an
 * inline help-block -- no HTML5 `required` attribute exists on either
 * input. Reproduced via `notify.error(...)`, not the inline
 * required/help-block pattern used by screens that actually have HTML5
 * validation (e.g. `anaesthesiaType`).
 *
 * ================================================================
 * Confirm-modal: fires ONLY on this page-load's first-ever Save (same shape
 * as ARmaster, faithfully reproduced)
 * ================================================================
 * `$scope.popUpFlag` is set `true` once at controller init and `false` in
 * `PopupOkBtn`, never reset back -- confirmed by exhaustive grep. The
 * confirm dialog appears before the very first Save of the page session and
 * never again after that. Reproduced via `confirmArmed`, set once and never
 * re-armed.
 *
 * ================================================================
 * Duplicate-check: description only, and NOT org/unit scoped -- a real
 * backend bug, NOT fixable from this frontend
 * ================================================================
 * `getBloodDonorTypeMasterByName`/`_NotId` filter only on
 * `bloodDonorTypeDesc`, with no `organizationId`/`unitId` predicate --
 * confirmed (`docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-blooddonortype-duplicatecheck-missing-scope`). This is server-side
 * logic in the SAME live endpoint both legacy and this rebuild call; it is
 * out of this component's/service's reach to correct. The separate,
 * dedicated legacy-fix branch (proposed, not yet applied) is what actually
 * resolves this -- not a client-side workaround, which would only mask the
 * real behavior without fixing it.
 *
 * ================================================================
 * Edit -- real GET-by-id call
 * ================================================================
 * `showUpdateBtn(id)` fires a real
 * `GET /global/bloodBank/getBloodDonorTypeMasterById/{id}/{orgId}/{unitId}`.
 *
 * ================================================================
 * Known, disclosed, non-blocking issue -- NOT fixed here
 * ================================================================
 * The shared `<modal>` directive's close (×) button hardcodes `cancel()`,
 * undefined in this controller -- per the standing entry-55 policy, this
 * rebuild's confirm modal has ordinary working close behavior instead.
 */
@Component({
  selector: 'app-blood-donor-type',
  imports: [FormsModule, StatusToggle],
  templateUrl: './blood-donor-type.html',
  styleUrl: './blood-donor-type.scss',
})
export class BloodDonorType {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(BloodDonorTypeService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<BloodDonorTypeForm>(emptyBloodDonorTypeForm());
  saveMode = signal(true);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. */
  private confirmArmed = true;

  rows = signal<BloodDonorTypeRow[]>([]);
  totalRecords = signal(0);
  currentPage = signal(1);
  pageSize = signal(10);
  pageSizes = PAGE_SIZES;
  totalPages = computed(() => Math.max(1, Math.ceil(this.totalRecords() / this.pageSize())));

  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.bloodDonorTypeCode?.toLowerCase().includes(keyword) ||
        r.bloodDonorTypeDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadPage(1);
  }

  loadPage(page: number): void {
    this.currentPage.set(page);
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalRecords.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.loadPage(1);
  }

  pagesList(): number[] {
    return Array.from({ length: this.totalPages() }, (_, i) => i + 1);
  }

  updateField<K extends keyof BloodDonorTypeForm>(field: K, value: BloodDonorTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- validation -- toast-style, see class doc ----
  private validate(): boolean {
    const f = this.form();
    if (!f.bloodDonorTypeCode) {
      this.notify.error('Please Enter Blood Donor Type Code');
      return false;
    }
    if (!f.bloodDonorTypeDesc) {
      this.notify.error('Please Enter Blood Donor Type Description');
      return false;
    }
    return true;
  }

  save(): void {
    if (!this.validate()) return;
    if (this.confirmArmed) {
      this.showConfirmModal.set(true);
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        bloodDonorTypeCode: f.bloodDonorTypeCode,
        bloodDonorTypeDesc: f.bloodDonorTypeDesc,
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
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Blood Donor Type Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Blood Donor Type already exist...!!');
          }
        },
      });
  }

  edit(row: BloodDonorTypeRow): void {
    this.saveMode.set(false);
    this.api.getById(row.bloodDonorTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            bloodDonorTypeId: detail.bloodDonorTypeId,
            bloodDonorTypeCode: detail.bloodDonorTypeCode,
            bloodDonorTypeDesc: detail.bloodDonorTypeDesc,
          });
        } else {
          this.notify.error(res.message ?? 'Something went wrong...!!');
        }
      },
    });
  }

  update(): void {
    if (!this.validate()) return;
    const f = this.form();
    this.api
      .update({
        bloodDonorTypeId: f.bloodDonorTypeId as number,
        bloodDonorTypeCode: f.bloodDonorTypeCode,
        bloodDonorTypeDesc: f.bloodDonorTypeDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Blood Donor Type Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Blood Donor Type already exist...!!');
          }
        },
      });
  }

  toggleStatus(row: BloodDonorTypeRow): void {
    const nextStatus = row.status === 'A' ? 'I' : 'A';
    this.rows.update((list) =>
      list.map((r) => (r.bloodDonorTypeId === row.bloodDonorTypeId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        bloodDonorTypeId: row.bloodDonorTypeId,
        status: nextStatus,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Blood Donor Type Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyBloodDonorTypeForm());
    this.saveMode.set(true);
    this.loadPage(this.currentPage());
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
