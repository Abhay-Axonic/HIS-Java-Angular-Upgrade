import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ReasonOfDiscardMasterService } from './reason-of-discard-master.service';
import { ReasonOfDiscardRow } from './reason-of-discard-master.models';

/**
 * Parity rebuild of the legacy `reasonOfDiscardMaster` screen
 * (bloodBank/reasonOfDiscardMasterController.js + reason-of-discard-master.html).
 * Backend: POST /bloodBank/getReasonOfDiscardMasterList (body
 * {organizationId, unitId, offset, noOfRecordsPerPage}). Module id 737.
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-reason-of-discard-master',
  standalone: true,
  imports: [],
  templateUrl: './reason-of-discard-master.html',
  styleUrl: './reason-of-discard-master.scss',
})
export class ReasonOfDiscardMaster {
  private readonly service = inject(ReasonOfDiscardMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ReasonOfDiscardRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load reasons of discard.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: ReasonOfDiscardRow): string[] {
    return [String(row.reasonOfDiscardCode ?? '—'), String(row.reasonOfDiscardDesc ?? '—')];
  }
}
