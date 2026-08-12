import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { DietTypeMasterService } from './diet-type-master.service';
import { DietTypeRow } from './diet-type-master.models';

/**
 * Parity rebuild of the legacy `dietTypeMaster` screen
 * (controller/dietAndKitchen/global/dietTypeMasterController.js +
 * diet-type-master.html). Backend: POST
 * /api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList (body
 * {organizationId, unitId, offset, noOfRecordsPerPage}). Module id 510.
 * Live-verified 2026-08-12: SUCCESS with 1 real row — real data grid, no
 * banner.
 */
@Component({
  selector: 'app-diet-type-master',
  standalone: true,
  imports: [],
  templateUrl: './diet-type-master.html',
  styleUrl: './diet-type-master.scss',
})
export class DietTypeMaster {
  private readonly service = inject(DietTypeMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<DietTypeRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load diet types.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: DietTypeRow): string[] {
    return [
      String(row.id ?? '—'),
      String(row.name ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
