import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { TemplateIcdCodeMappingService } from './template-icd-code-mapping.service';
import { TemplateIcdCodeMappingRow } from './template-icd-code-mapping.models';

/**
 * Parity rebuild of the legacy `templateICDCodeMapping` screen
 * (master/unit/templateICDCodeMappingMasterController.js +
 * views/template/template-ICD-code-mapping.html).
 * Backend: GET /api/global/getTemplateDiagnosisMapperlist/{offset}/{noOfRecordsPerPage}
 * (path params, no body — org/unit not in the call, matching legacy).
 * Module id 688. Live-verified 2026-08-12: SUCCESS with an empty
 * listObject — honest dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-template-icd-code-mapping',
  standalone: true,
  imports: [],
  templateUrl: './template-icd-code-mapping.html',
  styleUrl: './template-icd-code-mapping.scss',
})
export class TemplateIcdCodeMapping {
  private readonly service = inject(TemplateIcdCodeMappingService);

  readonly rows = signal<TemplateIcdCodeMappingRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(0, 10).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load template ICD code mappings.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: TemplateIcdCodeMappingRow): string[] {
    return [String(row.title ?? '—'), String(row.status ?? '—')];
  }
}
