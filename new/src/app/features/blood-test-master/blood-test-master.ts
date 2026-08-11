import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { BloodTestMasterService } from './blood-test-master.service';
import { BloodTestRow } from './blood-test-master.models';

/**
 * Parity rebuild of the legacy `bloodTestMaster` screen
 * (bloodBank/bloodTestMasterController.js + blood-test-master.html).
 * Backend: POST /global/bloodBank/getBloodTestMasterList (body
 * {organizationId, unitId, offset, noOfRecordsPerPage}). Module id 734.
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-blood-test-master',
  standalone: true,
  imports: [],
  templateUrl: './blood-test-master.html',
  styleUrl: './blood-test-master.scss',
})
export class BloodTestMaster {
  private readonly service = inject(BloodTestMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<BloodTestRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load blood tests.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: BloodTestRow): string[] {
    return [String(row.bloodTestCode ?? '—'), String(row.bloodTestDesc ?? '—')];
  }
}
