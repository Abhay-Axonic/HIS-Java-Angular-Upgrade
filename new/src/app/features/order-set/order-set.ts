import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { OrderSetService } from './order-set.service';
import { OrdersetRow } from './order-set.models';

/**
 * Parity rebuild of the legacy `orderSet` state (587) — template
 * views/master/UnitMaster/billingMaster/order-set.html.
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-031): the list endpoint returns the
 * masked envelope (HTTP 200, status=error, code=500, "Something went
 * wrong!!") — NOT dev-empty. Root cause: LIST_ORDER_MASTER_FOR_ICPC joins
 * osm.icpc_id, a column absent from doctor.m_orderset_master (4th phantom
 * column). DDL fix is a BR candidate (Rule 10, not applied). Module 587.
 */
@Component({
  selector: 'app-order-set',
  standalone: true,
  imports: [],
  templateUrl: './order-set.html',
  styleUrl: './order-set.scss',
})
export class OrderSet {
  private readonly service = inject(OrderSetService);
  private readonly session = inject(SessionService);

  readonly rows = signal<OrdersetRow[]>([]);
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

  rowCells(row: OrdersetRow): string[] {
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
