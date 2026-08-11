import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { AppointmentSchedulingUnitMasterService } from './appointment-scheduling-unit-master.service';
import { AppointmentSlotRow } from './appointment-scheduling-unit-master.models';

/**
 * Parity rebuild of the legacy `appointmentSchedulingUnitMaster` screen
 * (opd/unit/appointmentSchedulingUnitMasterController.js + appointment-
 * scheduling-unit-master.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-016): the primary list endpoint
 * (getSlotsNew) 500s with the correlation envelope "Something went wrong.
 * Reference: ERR-<uuid>" (ErrorCorrelationUtil) — NOT dev-empty. Module 653.
 */
@Component({
  selector: 'app-appointment-scheduling-unit-master',
  standalone: true,
  imports: [],
  templateUrl: './appointment-scheduling-unit-master.html',
  styleUrl: './appointment-scheduling-unit-master.scss',
})
export class AppointmentSchedulingUnitMaster {
  private readonly service = inject(AppointmentSchedulingUnitMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<AppointmentSlotRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly masked = signal(false);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getSlots(this.session.organizationId ?? 1, this.session.unitId ?? 1, 1)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
            this.masked.set(false);
          } else {
            const msg = res.message ?? 'Could not load appointment slots.';
            this.rows.set([]);
            this.masked.set(this.isCorrelationEnvelope(msg));
            this.error.set(msg);
          }
        },
        error: (err) => {
          this.loading.set(false);
          const status = err?.status ?? 0;
          this.rows.set([]);
          if (status === 500) {
            this.masked.set(true);
            this.error.set('Something went wrong. Reference: ERR-<backend> (HTTP 500 — see banner).');
          } else {
            this.masked.set(false);
            this.error.set(`Request failed: ${status} ${err?.message ?? ''}`);
          }
        },
      });
  }

  /** True when the message is the ErrorCorrelationUtil masked envelope. */
  private isCorrelationEnvelope(msg: string): boolean {
    return /reference:\s*err-/i.test(msg);
  }

  rowCells(row: AppointmentSlotRow): string[] {
    return [
      String(row.appointmentDate ?? '—'),
      String(row.doctorName ?? '—'),
      String(row.specialityName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
