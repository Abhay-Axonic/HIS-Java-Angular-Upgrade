import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NursingStationWardMappingService } from './nursing-station-ward-mapping.service';
import { NursingStationWardMappingRow } from './nursing-station-ward-mapping.models';

/**
 * Parity rebuild of the legacy `nursingStationWardMapping` screen
 * (nursingStationWardMappingController.js + nursing-station-ward-mapping.html).
 * Lists the nursing station -> ward mapping rows. Module id 548.
 * Live-verified 2026-08-12: 1 real row (Updated Desc / station 1).
 */
@Component({
  selector: 'app-nursing-station-ward-mapping',
  standalone: true,
  imports: [],
  templateUrl: './nursing-station-ward-mapping.html',
  styleUrl: './nursing-station-ward-mapping.scss',
})
export class NursingStationWardMapping {
  private readonly service = inject(NursingStationWardMappingService);
  private readonly session = inject(SessionService);

  readonly rows = signal<NursingStationWardMappingRow[]>([]);
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
            this.error.set(res.message ?? 'Could not load nursing station ward mappings.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: NursingStationWardMappingRow): string[] {
    return [
      String(row.nursingStationDesc ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
