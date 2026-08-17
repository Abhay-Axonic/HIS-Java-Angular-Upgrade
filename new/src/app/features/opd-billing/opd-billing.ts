import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { OpdBillingService } from './opd-billing.service';
import type { BillLine, BillPayload, BillResponseTotals } from './opd-billing.models';

/** OPD Billing — the core scope per the approved approach (the reconciliation
 * note's DUAL-DISPLAY strategy: the frontend-port calculation AND the
 * backend's returned numbers render side by side, so discrepancies surface
 * where the biller is already checking; the frontend calc retires only once
 * the backend's numbers are trusted).
 * The bill's dates are LONG epoch-millis (the encounter convention). */
@Component({
  selector: 'app-opd-billing',
  imports: [FormsModule],
  templateUrl: './opd-billing.html',
  styleUrl: './opd-billing.scss',
})
export class OpdBillingComponent {
  private api = inject(OpdBillingService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  // encounter context (read-only header)
  readonly encounterId = signal<number | null>(null);
  readonly patientId = signal<number | null>(null);
  readonly doctorName = signal('');
  readonly billNumber = signal('');

  // the bill lines (services being charged)
  readonly lines = signal<BillLine[]>([]);
  readonly serviceName = signal('');
  readonly serviceRate = signal('');
  readonly serviceQty = signal('1');

  // the DUAL-DISPLAY: frontend-port calculation vs the backend's returned numbers
  readonly frontendTotal = computed(() =>
    Math.round(this.lines().reduce((sum, l) => sum + (l.quantity || 0) * (l.rate || 0), 0) * 100) / 100,
  );
  readonly backendResult = signal<BillResponseTotals | null>(null);

  readonly saving = signal(false);

  constructor() {
    const ctx = this.session.current as Record<string, unknown> | null;
    const eid = ctx?.['encounterId'];
    const pid = ctx?.['patientId'];
    this.encounterId.set(typeof eid === 'number' ? eid : null);
    this.patientId.set(typeof pid === 'number' ? pid : null);
  }

  addLine(): void {
    const name = this.serviceName().trim();
    const rate = Number(this.serviceRate());
    const qty = Number(this.serviceQty()) || 1;
    if (!name || Number.isNaN(rate)) {
      this.notify.warning('Enter a service name and rate');
      return;
    }
    this.lines.update((ls) => [...ls, { serviceId: null, serviceName: name, quantity: qty, rate }]);
    this.serviceName.set('');
    this.serviceRate.set('');
    this.serviceQty.set('1');
  }

  removeLine(i: number): void {
    this.lines.update((ls) => ls.filter((_, idx) => idx !== i));
  }

  updateLine<K extends keyof BillLine>(i: number, key: K, value: unknown): void {
    this.lines.update((ls) => ls.map((l, idx) => (idx === i ? { ...l, [key]: value as BillLine[K] } : l)));
  }

  save(): void {
    if (this.lines().length === 0) {
      this.notify.warning('Add at least one billed service');
      return;
    }
    this.saving.set(true);
    const now = Date.now();
    const total = this.frontendTotal();
    const payload: BillPayload = {
      organisationId: this.orgId,
      unitId: this.unitId,
      encounterId: this.encounterId(),
      patientId: this.patientId(),
      billDateTime: now,
      grossAmount: total,
      netAmount: total,
      totalBillAmount: total,
      paidAmount: 0,
      outstanding: total,
      discountAmount: 0,
      concessionAmount: 0,
      gst: 0,
      roundOff: 0,
      isCreditBill: 'N',
      status: 'A',
      billStatusId: 1,
      createdDate: now,
      updatedDate: now,
      createdBy: this.userId,
      updatedBy: this.userId,
      billRemark: '',
      billCancelled: 'N',
    };
    this.api.saveBill(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') {
          const totals = (r.listObject?.[0] ?? r.object) as BillResponseTotals | null | undefined;
          this.backendResult.set(totals ?? null);
          this.billNumber.set(totals?.billNumber ?? this.billNumber());
          this.notify.warning(`Bill saved — backend total ${totals?.totalBillAmount ?? 'n/a'}`);
        } else {
          this.notify.warning(r.message ?? 'Bill save failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Bill save failed');
      },
    });
  }
}
