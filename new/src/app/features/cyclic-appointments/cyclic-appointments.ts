import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { CyclicAppointmentsService } from './cyclic-appointments.service';
import type { AppointmentRow, SlotCriteria } from './cyclic-appointments.models';

/** OPD Cyclic Appointments List — the read-only list view for recurring
 * appointments (docs/SPEC_OPD_FOUR_VARIANTS_2026-08-16.md; the legacy screen
 * is a mockup — the backend is the contract). DATE CONVENTION: STRING dates
 * via formatLegacyNow — the registration/prescription convention, NOT the
 * encounter's Long. */
@Component({
  selector: 'app-cyclic-appointments',
  imports: [FormsModule],
  templateUrl: './cyclic-appointments.html',
  styleUrl: './cyclic-appointments.scss',
})
export class CyclicAppointmentsComponent {
  private api = inject(CyclicAppointmentsService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;

  readonly fromDate = signal('');
  readonly toDate = signal('');
  readonly rows = signal<AppointmentRow[]>([]);
  readonly loading = signal(false);
  readonly loaded = signal(false);

  load(): void {
    const criteria: SlotCriteria = {
      organizationId: this.orgId,
      unitId: this.unitId,
      fromDate: this.fromDate() || null,
      toDate: this.toDate() || null,
    };
    this.loading.set(true);
    this.api.list(criteria).subscribe({
      next: (r) => {
        this.loading.set(false);
        this.loaded.set(true);
        if (r.status === 'success') this.rows.set(r.listObject ?? []);
      },
      error: () => {
        this.loading.set(false);
        this.loaded.set(true);
        this.notify.error('List load failed');
      },
    });
  }
}
