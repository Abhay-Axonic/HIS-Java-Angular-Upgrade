import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ConsentTypeService } from './consent-type.service';
import { ConsentTypeRow } from './consent-type.models';

/**
 * Parity rebuild of the legacy `consentType` state
 * (adt/global/consentTypeMasterController.js + consent-type.html).
 *
 * REAL DATA (corrected build, DQ-2026-08-12-028 follow-up): the legacy
 * state binds the generic MasterController shell (app.js:12585 — the DECOY
 * shape), but the decoy-groundwork classified 528 as Group A: a real
 * controller (consentTypeMasterController.js, 396 lines) with a real
 * backend (POST /adt/getConsentTypeMasterList — verified live: 3 rows).
 * The batch-10 noData build was a misclassification; this build renders the
 * real list. Module 528.
 */
@Component({
  selector: 'app-consent-type',
  standalone: true,
  imports: [],
  templateUrl: './consent-type.html',
  styleUrl: './consent-type.scss',
})
export class ConsentType {
  private readonly service = inject(ConsentTypeService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ConsentTypeRow[]>([]);
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
            this.error.set(res.message ?? 'The consent-type list could not be loaded.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: ConsentTypeRow): string[] {
    return [
      String(row.consentTypeMasterCode ?? '—'),
      String(row.consentTypeMasterName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
