import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { LoyaltyCardCreationService } from './loyalty-card-creation.service';
import { LoyaltyCardRow } from './loyalty-card-creation.models';

/**
 * Parity rebuild of the legacy `loyaltyCardCreation` screen
 * (master/unit/loyaltyCardCreationMasterController.js +
 * views/master/GlobalMaster/loyalty-card-creation.html).
 * Backend: POST /api/unit/getLoyaltiCardMasterList (body
 * {organisationId, unitId, offset, noOfRecordsPerPage}). Module id 729.
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-loyalty-card-creation',
  standalone: true,
  imports: [],
  templateUrl: './loyalty-card-creation.html',
  styleUrl: './loyalty-card-creation.scss',
})
export class LoyaltyCardCreation {
  private readonly service = inject(LoyaltyCardCreationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<LoyaltyCardRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load loyalty cards.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: LoyaltyCardRow): string[] {
    return [String(row.cardNo ?? '—'), String(row.status ?? '—')];
  }
}
