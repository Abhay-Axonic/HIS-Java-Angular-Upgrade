import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { DoctorWiseService } from './doctor-wise.service';
import { SlotBlockRow } from './doctor-wise.models';

/**
 * Parity rebuild of the legacy `doctorWise` screen
 * (opd/unit/SlotBlockUnblockControllerDoctorWise.js + doctor-wise.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-016): same masked class as scheduleWise
 * — the shared /api/opd/getSlots/blockUnblock endpoint returns the blank
 * generic envelope (swallowed exception + date-parse failure). The pair
 * share the endpoint, NOT a state (owner's cluster check: two controller
 * files, two templates). Module 658.
 */
@Component({
  selector: 'app-doctor-wise',
  standalone: true,
  imports: [],
  templateUrl: './doctor-wise.html',
  styleUrl: './doctor-wise.scss',
})
export class DoctorWise {
  private readonly service = inject(DoctorWiseService);
  private readonly session = inject(SessionService);

  readonly rows = signal<SlotBlockRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly masked = signal(false);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getSlots(
      this.session.organizationId ?? 1,
      this.session.unitId ?? 1,
      1, '2026-08-01', '2026-08-12', [1], [1], '08:00', '20:00',
    ).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
          this.masked.set(false);
        } else {
          this.rows.set([]);
          this.masked.set(true);
          this.error.set(res.message ?? 'The slot list could not be loaded (see banner).');
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

  rowCells(row: SlotBlockRow): string[] {
    return [
      String(row.appointmentDate ?? '—'),
      String(row.doctorName ?? '—'),
      String(row.specialityName ?? '—'),
      String(row.fromTimeString ?? '—'),
      String(row.toTimeString ?? '—'),
    ];
  }
}
