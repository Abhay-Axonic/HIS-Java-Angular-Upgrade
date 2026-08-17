import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { PendingOrdersService } from './pending-orders.service';
import type { IdNameRow, OrderCriteria, OrderRow } from './pending-orders.models';

/** OPD Pending Orders — the core scope per
 * docs/SPEC_PENDING_ORDERS_2026-08-16.md: filter by date range + department,
 * the order list grid (patient, order date, service, status, doctor), view
 * order details. The grid is READ-ONLY: checkStatus renders but does not
 * trigger actions (the accept/reject/cancel pass is deferred).
 * DATE CONVENTION: the criteria dates are STRING (dd-M-yyyy HH:mm:ss via
 * formatLegacyNow) — the registration/prescription convention, NOT the
 * encounter's Long. */
@Component({
  selector: 'app-pending-orders',
  imports: [FormsModule],
  templateUrl: './pending-orders.html',
  styleUrl: './pending-orders.scss',
})
export class PendingOrdersComponent {
  private api = inject(PendingOrdersService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;

  // filters
  readonly fromDate = signal('');
  readonly toDate = signal('');
  readonly specialityList = signal<IdNameRow[]>([]);
  readonly specialityId = signal<number | null>(null);

  // the grid + details
  readonly orders = signal<OrderRow[]>([]);
  readonly selectedOrder = signal<OrderRow | null>(null);
  readonly loading = signal(false);
  readonly loaded = signal(false);

  constructor() {
    this.api.getSpecialities(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.specialityList.set(r.listObject ?? []);
    });
  }

  load(): void {
    const criteria: OrderCriteria = {
      fromDate: this.fromDate() || null,
      toDate: this.toDate() || null,
      specialityId: this.specialityId(),
      unitId: this.unitId,
      organizationId: this.orgId,
    };
    this.loading.set(true);
    const call = this.fromDate().trim() || this.toDate().trim()
      ? this.api.getOrdersByDate(criteria)
      : this.api.getOrders(criteria);
    call.subscribe({
      next: (r) => {
        this.loading.set(false);
        this.loaded.set(true);
        if (r.status === 'success') this.orders.set(r.listObject ?? []);
      },
      error: () => {
        this.loading.set(false);
        this.loaded.set(true);
        this.notify.error('Orders load failed');
      },
    });
  }

  viewDetails(row: OrderRow): void {
    this.selectedOrder.set(row);
  }

  closeDetails(): void {
    this.selectedOrder.set(null);
  }

  /** The grid is read-only in this build — checkStatus renders only. */
  toggleCheck(row: OrderRow): void {
    // display-only selection; no API call (the actions land in pass 2)
    this.orders.update((list) =>
      list.map((r) => (r.nursingOrderMapperId === row.nursingOrderMapperId ? { ...r, checkStatus: !r.checkStatus } : r)),
    );
  }
}
