import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { AccountPayableStatusService } from './account-payable-status.service';
import { AccountPayableStatusRow } from './account-payable-status.models';

/**
 * Parity rebuild of the legacy `accountPayableStatus` state (582) — template
 * views/master/UnitMaster/billingMaster/account-payable-status.html driven by
 * the REAL billing/unit/accountPayableMasterController.js (the state binds
 * the generic MasterController shell — the DECOY shape; DQ-2026-08-12-030
 * corrected the classification to REAL DATA: 5 live rows via
 * /getActiveAccountPayable).
 */
@Component({
  selector: 'app-account-payable-status',
  standalone: true,
  imports: [],
  templateUrl: './account-payable-status.html',
  styleUrl: './account-payable-status.scss',
})
export class AccountPayableStatus {
  private readonly service = inject(AccountPayableStatusService);
  private readonly session = inject(SessionService);

  readonly rows = signal<AccountPayableStatusRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.session.organizationId ?? 1, this.session.unitId ?? 1)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'The account-payable status list could not be loaded.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: AccountPayableStatusRow): string[] {
    return [
      String(row.accCode ?? '—'),
      String(row.accDesc ?? '—'),
      String(row.accPayableCode ?? '—'),
      String(row.accEmail ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
