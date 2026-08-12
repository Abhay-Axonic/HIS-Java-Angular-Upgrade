import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { CashOpeningBalanceService } from './cash-opening-balance.service';
import { DenominationRow } from './cash-opening-balance.models';

/**
 * Parity rebuild of the legacy `cashOpeningBalance` state (597) — template
 * views/master/UnitMaster/billingMaster/cash-opening-balance.html, controller
 * billing/cashDeskManagement/global/cashOpeningBalanceController.js.
 *
 * CLASSIFICATION (live-verified 2026-08-12, backend localhost:8080, headers
 * Authorization Bearer tools/.token_cache.json + userId:1 unitId:1
 * organizationId:1): write-path cash-entry form — NOT a master list. The
 * screen's denomination "feed" is a POST SAVE endpoint,
 * GETDENOMINATIONLIST = "/api/global/getListOfDenominations"
 * (adt.url.config.js:697; cashOpeningBalanceController.js:80-114), which
 * answers HTTP 200 {"status":"success","listObject":[],"message":"Added
 * Successfully."} — 0 rows, save envelope. The real screen surface is a
 * qty-entry form (handoverTo user, date, per-denomination qty/amount rows,
 * remarks, total) persisted via POST /api/global/saveCashHandoverMaster
 * (cashOpeningBalanceController.js:198-253). Deferred to the transaction
 * methodology; this rebuild renders the honest state (see template).
 */
@Component({
  selector: 'app-cash-opening-balance',
  standalone: true,
  imports: [],
  templateUrl: './cash-opening-balance.html',
  styleUrl: './cash-opening-balance.scss',
})
export class CashOpeningBalance {
  private readonly service = inject(CashOpeningBalanceService);
  private readonly session = inject(SessionService);

  readonly rows = signal<DenominationRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly probed = signal(false);
  readonly probeStatus = signal('');
  readonly probeMessage = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service
      .getList(this.session.organizationId ?? 1, this.session.unitId ?? 1)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          this.probed.set(true);
          this.probeStatus.set(res.status ?? '');
          this.probeMessage.set(res.message ?? '');
          this.rows.set(res.listObject ?? []);
        },
        error: (err) => {
          this.loading.set(false);
          this.probed.set(true);
          this.probeStatus.set(`http-${err?.status ?? 'unknown'}`);
          this.probeMessage.set(`${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: DenominationRow): string[] {
    return [
      String(row.denomination ?? '—'),
      String(row.currencyCode ?? row.currencySymbol ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
