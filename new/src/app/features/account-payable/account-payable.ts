import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { AccountPayableService } from './account-payable.service';
import { AccountPayableRow } from './account-payable.models';

/**
 * Parity rebuild of the legacy `accountPayable` state (580) — template
 * views/master/UnitMaster/billingMaster/account-payable.html driven by the
 * REAL billing/unit/accountPayableMasterController.js (the state binds the
 * generic MasterController shell — the DECOY shape; DQ-2026-08-12-030
 * corrected the classification to REAL DATA: 5 live rows).
 */
@Component({
  selector: 'app-account-payable',
  standalone: true,
  imports: [],
  templateUrl: './account-payable.html',
  styleUrl: './account-payable.scss',
})
export class AccountPayable {
  private readonly service = inject(AccountPayableService);
  private readonly session = inject(SessionService);

  readonly rows = signal<AccountPayableRow[]>([]);
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
            this.error.set(res.message ?? 'The account-payable list could not be loaded.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: AccountPayableRow): string[] {
    return [
      String(row.accCode ?? '—'),
      String(row.accDesc ?? '—'),
      String(row.accPayableCode ?? '—'),
      String(row.accEmail ?? '—'),
      String(row.accMobile ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
