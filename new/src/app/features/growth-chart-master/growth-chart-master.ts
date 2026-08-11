import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { GrowthChartMasterService } from './growth-chart-master.service';
import { GrowthChartRow } from './growth-chart-master.models';

/**
 * Parity rebuild of the legacy `growthChartMaster` screen
 * (growthChartMasterController.js + growth-chart-master.html). Lists the
 * growth chart entries (height/weight/circumference ranges per gender/age).
 * Module id 962. Live-verified 2026-08-12: 1 real row (Female Updated,
 * 70-71cm / 90-91kg).
 */
@Component({
  selector: 'app-growth-chart-master',
  standalone: true,
  imports: [],
  templateUrl: './growth-chart-master.html',
  styleUrl: './growth-chart-master.scss',
})
export class GrowthChartMaster {
  private readonly service = inject(GrowthChartMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<GrowthChartRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.session.organizationId ?? 1, this.session.unitId ?? 1, 0, 100)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'Could not load growth charts.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: GrowthChartRow): string[] {
    return [
      String(row.gender ?? '—'),
      String(row.ageInMonths ?? '—'),
      `${row.heightFrom ?? '—'}–${row.heightTo ?? '—'} cm`,
      `${row.weightFrom ?? '—'}–${row.weightTo ?? '—'} kg`,
      String(row.status ?? '—'),
    ];
  }
}
