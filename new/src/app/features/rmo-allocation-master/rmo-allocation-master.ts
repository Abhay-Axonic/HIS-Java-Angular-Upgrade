import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { RmoAllocationMasterService } from './rmo-allocation-master.service';
import { RmoAllocationRow } from './rmo-allocation-master.models';

/**
 * Parity rebuild of the legacy `RMOAllocationMaster` screen
 * (rmoAllocationMasterController.js + RMO-allocation-master.html). Lists the
 * RMO doctor allocations. Module id 824. Live-verified 2026-08-12: SUCCESS
 * empty (honest dev-empty).
 */
@Component({
  selector: 'app-rmo-allocation-master',
  standalone: true,
  imports: [],
  templateUrl: './rmo-allocation-master.html',
  styleUrl: './rmo-allocation-master.scss',
})
export class RmoAllocationMaster {
  private readonly service = inject(RmoAllocationMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<RmoAllocationRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.session.organizationId ?? 1, this.session.unitId ?? 1, 0, 10)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'Could not load RMO allocations.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: RmoAllocationRow): string[] {
    return [
      String(row.doctorName ?? '—'),
      String(row.specialityName ?? '—'),
      String(row.wardName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
