import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { DoseMasterService } from './dose-master.service';
import { DoseRow } from './dose-master.models';

/**
 * Parity rebuild of the legacy `doseMaster` screen
 * (controller/master/global/common/doseMasterController.js +
 * dose-master.html). Backend: POST /api/common/getDoseMasterList (body
 * {organizationId, offset, noOfRecordsPerPage} ONLY — no unitId). Module
 * id 502. Live-verified 2026-08-12: SUCCESS with 1 real row — real data
 * grid, no banner.
 */
@Component({
  selector: 'app-dose-master',
  standalone: true,
  imports: [],
  templateUrl: './dose-master.html',
  styleUrl: './dose-master.scss',
})
export class DoseMaster {
  private readonly service = inject(DoseMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<DoseRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), 0, 10).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load doses.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: DoseRow): string[] {
    return [
      String(row.id ?? '—'),
      String(row.name ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
