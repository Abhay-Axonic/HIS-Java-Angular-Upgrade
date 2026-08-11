import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { TemplateMasterService } from './template-master.service';
import { LabTemplateRow } from './template-master.models';

/**
 * Parity rebuild of the legacy `templateMaster` screen
 * (labTemplateMasterController.js + views/master/LIS/template-master.html).
 * Lists the lab templates. Module id 58. Live-verified 2026-08-12: the
 * endpoint returns a RAW ARRAY [] (honest empty; raw-list response shape,
 * DQ-2026-08-12-020).
 */
@Component({
  selector: 'app-template-master',
  standalone: true,
  imports: [],
  templateUrl: './template-master.html',
  styleUrl: './template-master.scss',
})
export class TemplateMaster {
  private readonly service = inject(TemplateMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<LabTemplateRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    // The endpoint returns a RAW ARRAY (legacy reads response.data directly);
    // the array IS the list, no envelope unwrap needed.
    this.service.getList(this.session.organizationId ?? 1, this.session.unitId ?? 1, 0, 10)
      .subscribe({
        next: (rows) => {
          this.loading.set(false);
          this.rows.set(Array.isArray(rows) ? rows : []);
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: LabTemplateRow): string[] {
    return [
      String(row.templateCode ?? '—'),
      String(row.templateName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
