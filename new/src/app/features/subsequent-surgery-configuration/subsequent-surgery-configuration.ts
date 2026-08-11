import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { SubsequentSurgeryConfigurationService } from './subsequent-surgery-configuration.service';
import { SubsequentSurgeryConfigurationRow } from './subsequent-surgery-configuration.models';

/**
 * Parity rebuild of the legacy `subsequentSurgeryConfiguration` screen
 * (controller/otims/global/subsequentSurgeryConfigurationController.js +
 * subsequent-surgery-configuration.html). Backend: POST
 * /api/otims/getSubSequentProcedureConfigList (body {orgId, unitId, offset,
 * noOfRecordsPerPage}). Module id 516. Live-verified 2026-08-12: SUCCESS
 * with an empty listObject — honest dev-empty grid ("No records."), no
 * banner.
 */
@Component({
  selector: 'app-subsequent-surgery-configuration',
  standalone: true,
  imports: [],
  templateUrl: './subsequent-surgery-configuration.html',
  styleUrl: './subsequent-surgery-configuration.scss',
})
export class SubsequentSurgeryConfiguration {
  private readonly service = inject(SubsequentSurgeryConfigurationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<SubsequentSurgeryConfigurationRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load subsequent surgery configurations.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: SubsequentSurgeryConfigurationRow): string[] {
    return [
      String(row.id ?? '—'),
      String(row.name ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
