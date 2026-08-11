import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ScheduleWiseService } from './schedule-wise.service';
import { SlotBlockRow } from './schedule-wise.models';

/**
 * Parity rebuild of the legacy `scheduleWise` screen
 * (opd/unit/SlotBlockUnblockControllerScheduleWise.js + schedule-wise.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-016): the slot-block list endpoint
 * returns the BLANK GENERIC envelope (HTTP 200, status=error, code/message
 * null) — NOT dev-empty. Root cause: SlotMasterController.java:101-104
 * swallows the real exception; the DAO (SlotMasterDaoImpl:1857) throws a
 * date-parse failure (GlobalCommonDateUtils.getDate appends " 00:00:00" to
 * <12-char dates then parses with "yyyy-MM-dd" — always fails for the
 * 'YYYY-MM-DD' the frontend sends). The SQL runs clean in psql. The pair
 * scheduleWise + doctorWise share this endpoint (not a state). Module 657.
 */
@Component({
  selector: 'app-schedule-wise',
  standalone: true,
  imports: [],
  templateUrl: './schedule-wise.html',
  styleUrl: './schedule-wise.scss',
})
export class ScheduleWise {
  private readonly service = inject(ScheduleWiseService);
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
