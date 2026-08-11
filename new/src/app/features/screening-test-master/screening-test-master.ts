import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ScreeningTestMasterService } from './screening-test-master.service';
import { ScreeningTestRow } from './screening-test-master.models';

/**
 * Parity rebuild of the legacy `screeningTestMaster` screen
 * (bloodBank/screeningTestMasterController.js + screening-test-master.html).
 * Backend: POST /bloodBank/getScreeningTestMasterList (body
 * {organizationId, unitId, offset, noOfRecordsPerPage}). Module id 738.
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-screening-test-master',
  standalone: true,
  imports: [],
  templateUrl: './screening-test-master.html',
  styleUrl: './screening-test-master.scss',
})
export class ScreeningTestMaster {
  private readonly service = inject(ScreeningTestMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ScreeningTestRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load screening tests.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: ScreeningTestRow): string[] {
    return [String(row.screeningTestCode ?? '—'), String(row.screeningTestDesc ?? '—')];
  }
}
