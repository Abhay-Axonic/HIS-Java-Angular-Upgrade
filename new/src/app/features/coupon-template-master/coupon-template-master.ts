import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CouponTemplateMasterService } from './coupon-template-master.service';
import { CouponTemplateRecord, CouponTemplateDetail, emptyCouponTemplateRecord } from './coupon-template-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of the legacy `couponTemplateMaster` screen (list) +
 * `couponTemplate` (add/edit form). The legacy family is:
 *   couponTemplateMasterController.js -> list + status toggle + navigate
 *   couponTemplateController.js        -> the multi-tab add/edit builder
 *      (group details, service selection by speciality/sub-speciality,
 *       percentage/amount per detail -- the discount configuration that
 *       feeds billing).
 *
 * Backend: `/api/billing/coupon/*` (BR-157 fixed: save binds verified
 * org/unit, status toggle is org-scoped). Module id 775 (legacy resolve).
 *
 * SME-RELEVANT (Step 6): the `couponTemplateDetailsDtoList` rows carry a
 * percentage AND/OR amount per service -- the discount rule that applies
 * when a patient's bill includes that service. The domain question is
 * whether percentage vs. amount precedence, and the allowed range, match
 * billing policy. See the batch report's SME points.
 */
@Component({
  selector: 'app-coupon-template-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './coupon-template-master.html',
  styleUrl: './coupon-template-master.scss',
})
export class CouponTemplateMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CouponTemplateMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  list = signal<CouponTemplateRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  searchTerm = signal('');

  // ---- form state (the add/edit builder) ----
  record = signal<CouponTemplateRecord>(emptyCouponTemplateRecord());
  details = signal<CouponTemplateDetail[]>([]);
  isEdit = signal(false);
  showForm = signal(false);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const rows = res.listObject ?? [];
          this.list.set(rows);
          this.totalCount.set(rows.length);
        }
      },
      error: () => this.notify.error('Failed to load coupon templates'),
    });
  }

  filteredList = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) return this.list();
    return this.list().filter((r) => (r.couponTemplateName ?? '').toLowerCase().includes(term));
  });

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- create/edit ----
  startCreate(): void {
    this.record.set(emptyCouponTemplateRecord());
    this.details.set([]);
    this.isEdit.set(false);
    this.showForm.set(true);
  }

  startEdit(row: CouponTemplateRecord): void {
    this.record.set({ ...row });
    this.isEdit.set(true);
    this.showForm.set(true);
    // load the detail rows for the edit form
    this.api.getDetails(row.couponTemplateMasterId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object?.couponTemplateDetailsDtoList) {
          this.details.set(res.object.couponTemplateDetailsDtoList);
        }
      },
      error: () => this.notify.error('Failed to load coupon details'),
    });
  }

  addDetailRow(): void {
    this.details.update((d) => [...d, { serviceId: 0, percentage: null, amount: null, status: 'A' }]);
  }

  removeDetailRow(index: number): void {
    this.details.update((d) => d.filter((_, i) => i !== index));
  }

  cancelForm(): void {
    this.showForm.set(false);
    this.isEdit.set(false);
  }

  private requiredFieldsValid(): boolean {
    const r = this.record();
    return !!r.couponTemplateName?.trim();
  }

  save(): void {
    if (!this.requiredFieldsValid()) {
      this.notify.error('Coupon template name is required');
      return;
    }
    const r = this.record();
    const payload = {
      couponTemplateMasterId: this.isEdit() ? r.couponTemplateMasterId : null,
      couponTemplateName: r.couponTemplateName.trim(),
      couponTemplateDescription: r.couponTemplateDescription ?? null,
      expireDays: r.expireDays ?? null,
      status: r.status,
      serviceId: r.serviceId ?? null,
      organizationId: this.organizationId,
      unitId: this.unitId,
      createdBy: this.userId,
      updatedBy: this.userId,
      couponTemplateDetailsDtoList: this.details().map((d) => ({
        ...d,
        organizationId: this.organizationId,
        unitId: this.unitId,
      })),
    };
    this.api.save(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Coupon template saved');
          this.showForm.set(false);
          this.loadList();
        } else {
          this.notify.error(res.message ?? 'Save failed');
        }
      },
      error: () => this.notify.error('Save failed'),
    });
  }

  // ---- status toggle (org-scoped post-BR-157) ----
  onToggleStatus(row: CouponTemplateRecord): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.updateStatus(row.couponTemplateMasterId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) =>
            rows.map((x) =>
              x.couponTemplateMasterId === row.couponTemplateMasterId ? { ...x, status: nextStatus } : x,
            ),
          );
          this.notify.success('Status updated');
        } else {
          this.notify.error(res.message ?? 'Update failed');
        }
      },
      error: () => this.notify.error('Update failed'),
    });
  }
}
