import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { DonorRejectionMasterService } from './donor-rejection-master.service';
import { DonorRejectionRow } from './donor-rejection-master.models';

/**
 * Parity rebuild of the legacy `donorRejectionMaster` screen
 * (bloodBank/donorRejectionMasterController.js + donor-rejection-master.html).
 * Backend: POST /bloodBank/getDonorRejectionMasterList (body
 * {organizationId, unitId, offset, noOfRecordsPerPage}). Module id 740.
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-donor-rejection-master',
  standalone: true,
  imports: [],
  templateUrl: './donor-rejection-master.html',
  styleUrl: './donor-rejection-master.scss',
})
export class DonorRejectionMaster {
  private readonly service = inject(DonorRejectionMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<DonorRejectionRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load donor rejections.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: DonorRejectionRow): string[] {
    return [String(row.donorRejectionCode ?? '—'), String(row.donorRejectionDesc ?? '—')];
  }
}
