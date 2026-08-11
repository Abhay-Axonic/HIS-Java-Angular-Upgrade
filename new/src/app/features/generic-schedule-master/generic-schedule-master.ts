import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { GenericScheduleMasterService } from './generic-schedule-master.service';
import { GenericScheduleRow } from './generic-schedule-master.models';

/**
 * Parity rebuild of the legacy `genericScheduleMaster` screen
 * (controller/pharmacy/genericScheduleMasterController.js +
 * generic-schedule-master.html). Backend: GET
 * /Pharmacy/global/listGenericScheduleMaster/{org}/{unit}/{offset}/{size}
 * (4 path segments). Module id 827. Live-verified 2026-08-12: SUCCESS with
 * 1 real row — real data grid, no banner.
 */
@Component({
  selector: 'app-generic-schedule-master',
  standalone: true,
  imports: [],
  templateUrl: './generic-schedule-master.html',
  styleUrl: './generic-schedule-master.scss',
})
export class GenericScheduleMaster {
  private readonly service = inject(GenericScheduleMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<GenericScheduleRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }
  private unitId(): number {
    return this.session.unitId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), this.unitId(), 0, 10).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load generic schedules.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: GenericScheduleRow): string[] {
    return [
      String(row.id ?? '—'),
      String(row.name ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
