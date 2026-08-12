import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { OrderSetListService } from './order-set-list.service';
import { OrdersetListRow } from './order-set-list.models';

/**
 * Parity rebuild of the legacy `orderSetList` state (588) — template
 * views/master/UnitMaster/billingMaster/order-set-list.html.
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-031): SHARES the masked endpoint with
 * 587 Order Set — LIST_ORDER_MASTER_FOR_ICPC joins osm.icpc_id, absent from
 * doctor.m_orderset_master (4th phantom column). One banner text for both.
 * DDL fix is a BR candidate (Rule 10, not applied). Module 588.
 */
@Component({
  selector: 'app-order-set-list',
  standalone: true,
  imports: [],
  templateUrl: './order-set-list.html',
  styleUrl: './order-set-list.scss',
})
export class OrderSetList {
  private readonly service = inject(OrderSetListService);
  private readonly session = inject(SessionService);

  readonly rows = signal<OrdersetListRow[]>([]);
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
            this.error.set(res.message ?? 'The order-set list could not be loaded (see banner).');
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

  rowCells(row: OrdersetListRow): string[] {
    return [
      String(row.ordersetName ?? '—'),
      String(row.ordersetTypeName ?? '—'),
      String(row.diagnosisName ?? '—'),
      String(row.specialityName ?? '—'),
      String(row.doctorName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
