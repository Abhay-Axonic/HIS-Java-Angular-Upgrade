import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { InterimHandoverService } from './interim-handover.service';
import { DenominationRow } from './interim-handover.models';

/**
 * Parity rebuild of the legacy `interimHandover` state (598) — template
 * views/master/UnitMaster/billingMaster/interim-handover.html, controller
 * billing/cashDeskManagement/global/interimHandoverController.js.
 *
 * CLASSIFICATION (live-verified 2026-08-12, backend localhost:8080, headers
 * Authorization Bearer tools/.token_cache.json + userId:1 unitId:1
 * organizationId:1): write-path cash-entry form — NOT a master list. The
 * screen's denomination "feed" is a POST SAVE endpoint,
 * GETDENOMINATIONLIST1 = "/adt/getListOfDenominations" (adt.url.config.js:698;
 * interimHandoverController.js:75-107), which answers HTTP 200
 * {"status":"success","listObject":[],"message":"Added Successfully."} — 0
 * rows, save envelope. The real surface is a mid-shift cash handover form
 * (handoverTo, date, denomination qty/amount rows, remarks, cash/cheque/card
 * tabs) persisted via POST /adt/saveInterimHandover
 * (interimHandoverController.js:453-475). Deferred to the transaction
 * methodology; this rebuild renders the honest state (see template).
 */
@Component({
  selector: 'app-interim-handover',
  standalone: true,
  imports: [],
  templateUrl: './interim-handover.html',
  styleUrl: './interim-handover.scss',
})
export class InterimHandover {
  private readonly service = inject(InterimHandoverService);
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
