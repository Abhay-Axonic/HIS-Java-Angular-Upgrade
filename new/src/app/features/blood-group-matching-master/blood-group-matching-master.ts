import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { BloodGroupMatchingMasterService } from './blood-group-matching-master.service';
import { BloodGroupRow } from './blood-group-matching-master.models';

/**
 * Parity rebuild of the legacy `bloodGroupMatchingMaster` screen
 * (bloodBank/bloodGroupMatchingMasterController.js +
 * blood-group-matching-master.html). Lists the ACTIVE blood groups (real
 * data: A+, A Positive (Updated), etc.) — the groups for which matching
 * rules are configured. Module id 741. Live-verified 2026-08-12 (3 real
 * rows; zero masked errors in the blood bank family, DQ-2026-08-12-010).
 */
@Component({
  selector: 'app-blood-group-matching-master',
  standalone: true,
  imports: [],
  templateUrl: './blood-group-matching-master.html',
  styleUrl: './blood-group-matching-master.scss',
})
export class BloodGroupMatchingMaster {
  private readonly service = inject(BloodGroupMatchingMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<BloodGroupRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getActiveBloodGroups(this.session.organizationId ?? 1, this.session.unitId ?? 1)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'Could not load blood groups.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: BloodGroupRow): string[] {
    return [
      String(row.bloodGroupCode ?? '—'),
      String(row.bloodGroupDesc ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
