import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { DiscountRightsService } from './discount-rights.service';
import { DiscountRightsRow } from './discount-rights.models';

/**
 * Parity rebuild of the legacy `discountRights` state (module 745,
 * "Financial Rights") — views/master/UnitMaster/billingMaster/
 * discount-rights.html driven by discountRightsMasterController.js
 * (billing/global), state registered in app.js:18323.
 *
 * LIST (REAL, live-verified 2026-08-12): POST /billing/getDiscountRightsMasterList
 * returned HTTP 200, status "success", with 1 real row (discountRightId 99902,
 * roleName "Admin", employee "PRE_01 Admin Admin", toDiscountAmount 222.22);
 * the companion GET /billing/getDiscountRightsMasterCount reports 2 total.
 * No masked envelope, no dev-empty — a real table is the honest build.
 * Table columns mirror the legacy template's <thead> (discount-rights.html:
 * 504-530). Write-path actions (status toggle, edit) are out of scope for
 * this list rebuild.
 */
@Component({
  selector: 'app-discount-rights',
  standalone: true,
  imports: [],
  templateUrl: './discount-rights.html',
  styleUrl: './discount-rights.scss',
})
export class DiscountRights {
  private readonly service = inject(DiscountRightsService);
  private readonly session = inject(SessionService);

  readonly rows = signal<DiscountRightsRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly masked = signal(false);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.session.organizationId ?? 1, this.session.unitId ?? 1, 0, 10)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
            this.masked.set(false);
          } else {
            this.rows.set([]);
            this.masked.set(true);
            this.error.set(res.message ?? 'The discount-rights list could not be loaded (see banner).');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.rows.set([]);
          this.masked.set(true);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  /** Column order matches the legacy table (discount-rights.html:538-567). */
  rowCells(row: DiscountRightsRow): string[] {
    return [
      String(row.roleName ?? '—'),
      String(row.employeeName ?? '—'),
      String(row.fromDiscountAmount ?? '—'),
      String(row.toDiscountAmount ?? '—'),
      String(row.fromFinancialClearanceAmount ?? '—'),
      String(row.toFinancialClearanceAmount ?? '—'),
      String(row.fromCreditNoteAmount ?? '—'),
      String(row.toCreditNoteAmount ?? '—'),
      String(row.fromExpenseApprovalAmount ?? '—'),
      String(row.toExpenseApprovalAmount ?? '—'),
      String(row.fromBadDebitAmount ?? '—'),
      String(row.toBadDebitAmount ?? '—'),
      String(row.fromRefundAmount ?? '—'),
      String(row.toRefundAmount ?? '—'),
      String(row.isUHIDMergeApproval ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
