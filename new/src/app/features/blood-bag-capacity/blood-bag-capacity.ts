import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BloodBagCapacityService } from './blood-bag-capacity.service';
import { BloodBagCapacityForm, BloodBagCapacityRow, emptyBloodBagCapacityForm } from './blood-bag-capacity.models';

const PAGE_SIZES = [10, 25, 50, 100];

/**
 * Parity rebuild of `views/master/UnitMaster/bloodBank/blood-bag-capacity.html`
 * + `resources/assets/js/controller/bloodBank/bloodBagCapacityMasterController.js`.
 * Full Step 1 audit: `docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-bloodbagcapacity-step1-findings`.
 *
 * Module gate: id 735, confirmed via the live
 * `.state('bloodBagCapacity', ...)` resolve block (`app.js:16321-16348`).
 *
 * ================================================================
 * SEVERITY-1, owner-approved correction: Edit-form state is kept fully
 * ISOLATED from status-toggle actions
 * ================================================================
 * Legacy's `updateStatus()` (ctrl:312-320) mutates the SAME `$scope.common`
 * object the Edit form's Code/Desc fields are bound to -- overwriting only
 * `id`/`status`/dates. Sequence: open Row A for edit (populates
 * `$scope.common` with Row A's code/desc/id) -- then, before saving, toggle
 * Row B's status. `updateStatus` silently overwrites
 * `$scope.common.bloodBagCapacityId`/`status` to Row B's values while the
 * visible Code/Desc fields still show Row A's text. Clicking "Update" next
 * would write Row B's id with Row A's unrelated data -- a genuine
 * wrong-record write risk, not a cosmetic bug.
 *
 * This rebuild does not share ANY state between `form` (the edit signal) and
 * `toggleStatus()` below -- `toggleStatus(row)` builds its status-update
 * payload entirely from the `row` parameter passed to it by the template's
 * `(toggled)` binding, never touching `form()` at all. There is structurally
 * no window in which a toggle click can affect what a pending Save/Update
 * submits, regardless of timing. This is not a special case bolted on for
 * this screen -- every screen in this batch follows the same
 * signal-per-concern pattern; bloodBagCapacity is simply the one where
 * legacy's shared-object approach happened to create an observable risk.
 *
 * ================================================================
 * Duplicate-check bugs -- CONFIRMED server-side, NOT fixable from this
 * frontend
 * ================================================================
 * (1) Update's duplicate-check query parameter literally named
 * `bloodBagCapacityCode` is bound to the DTO's `bloodBagCapacityDesc` value
 * (dao.java:222) -- the "code already exists" guard on Update actually
 * compares description text against the code column. (2) Create's
 * duplicate-check has no org/unit scope. Both live in the same backend
 * endpoints this rebuild calls; the separate, pending legacy-fix branches
 * are what actually resolve them, not anything client-side.
 *
 * ================================================================
 * Validation -- toast-style (identical shape to bloodDonortype)
 * ================================================================
 * Manual JS null/empty checks on both fields, surfaced via `growl` error
 * toast, not inline help-block -- no HTML5 `required` attribute on either
 * input.
 *
 * ================================================================
 * Confirm-modal: fires ONLY on this page-load's first-ever Save (same
 * one-way `popUpFlag` shape as ARmaster/bloodDonortype)
 * ================================================================
 *
 * ================================================================
 * Status toggle -- optimistic, never reverted on failure (faithful)
 * ================================================================
 * `updateStatus`'s error branch only shows a growl; it never reverts
 * `info.status`. Reproduced: the row's status is applied optimistically and
 * left as-is regardless of the server response.
 *
 * ================================================================
 * Known, disclosed, non-blocking issue -- NOT fixed here
 * ================================================================
 * The shared `<modal>` directive's close (×) button hardcodes `cancel()`,
 * undefined in this controller -- per the standing entry-55 policy, this
 * rebuild's confirm modal has ordinary working close behavior instead.
 */
@Component({
  selector: 'app-blood-bag-capacity',
  imports: [FormsModule, StatusToggle],
  templateUrl: './blood-bag-capacity.html',
  styleUrl: './blood-bag-capacity.scss',
})
export class BloodBagCapacity {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(BloodBagCapacityService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<BloodBagCapacityForm>(emptyBloodBagCapacityForm());
  saveMode = signal(true);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. */
  private confirmArmed = true;

  rows = signal<BloodBagCapacityRow[]>([]);
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
        r.bloodBagCapacityCode?.toLowerCase().includes(keyword) ||
        r.bloodBagCapacityDesc?.toLowerCase().includes(keyword),
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

  updateField<K extends keyof BloodBagCapacityForm>(field: K, value: BloodBagCapacityForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  private validate(): boolean {
    const f = this.form();
    if (!f.bloodBagCapacityCode) {
      this.notify.error('Please Enter Blood Bag Capacity Code');
      return false;
    }
    if (!f.bloodBagCapacityDesc) {
      this.notify.error('Please Enter Blood Bag Capacity Description');
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
        bloodBagCapacityCode: f.bloodBagCapacityCode,
        bloodBagCapacityDesc: f.bloodBagCapacityDesc,
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
            this.notify.success(res.message ?? 'Blood Bag Capacity Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Blood Bag Capacity already exist...!!');
          }
        },
      });
  }

  edit(row: BloodBagCapacityRow): void {
    this.saveMode.set(false);
    this.api.getById(row.bloodBagCapacityId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            bloodBagCapacityId: detail.bloodBagCapacityId,
            bloodBagCapacityCode: detail.bloodBagCapacityCode,
            bloodBagCapacityDesc: detail.bloodBagCapacityDesc,
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
        bloodBagCapacityId: f.bloodBagCapacityId as number,
        bloodBagCapacityCode: f.bloodBagCapacityCode,
        bloodBagCapacityDesc: f.bloodBagCapacityDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Blood Bag Capacity Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Blood Bag Capacity already exist...!!');
          }
        },
      });
  }

  /**
   * Built entirely from `row` -- deliberately never reads `form()`. See
   * class doc comment: this is the structural fix for the logged
   * wrong-record write race.
   */
  toggleStatus(row: BloodBagCapacityRow): void {
    const nextStatus = row.status === 'A' ? 'I' : 'A';
    this.rows.update((list) =>
      list.map((r) => (r.bloodBagCapacityId === row.bloodBagCapacityId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        bloodBagCapacityId: row.bloodBagCapacityId,
        status: nextStatus,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Blood Bag Capacity Status Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        // Faithful: no revert on transport error either -- see class doc comment.
      });
  }

  private resetForm(): void {
    this.form.set(emptyBloodBagCapacityForm());
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
