import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ProcedureConfigurationsService } from './procedure-configurations.service';
import { ProcedureConfigurationRow } from './procedure-configurations.models';

/**
 * Parity rebuild of the legacy `procedureConfigurations` screen
 * (controller/otims/global/procedureConfigurationsController.js +
 * procedure-configurations.html). Backend: GET
 * /api/otims/getProcedureConfigList/{org}/{unit} (2 path segments, no
 * body). Module id 696. Live-verified 2026-08-12: SUCCESS with an empty
 * listObject — honest dev-empty grid ("No records."), no banner.
 */
@Component({
  selector: 'app-procedure-configurations',
  standalone: true,
  imports: [],
  templateUrl: './procedure-configurations.html',
  styleUrl: './procedure-configurations.scss',
})
export class ProcedureConfigurations {
  private readonly service = inject(ProcedureConfigurationsService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ProcedureConfigurationRow[]>([]);
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
    this.service.getList(this.orgId(), this.unitId()).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load procedure configurations.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: ProcedureConfigurationRow): string[] {
    return [
      String(row.id ?? '—'),
      String(row.name ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
