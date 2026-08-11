import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ConsentMasterService } from './consent-master.service';
import { ConsentRow } from './consent-master.models';

/**
 * Parity rebuild of the legacy `consentMaster` screen
 * (adt/global/consentMasterController.js + consent master template).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-023): the list endpoint returns the
 * BLANK generic envelope (HTTP 200, status=error, no message) — NOT
 * dev-empty. The GET_CONSENT_LIST named query selects c.formTemplateId, a
 * column that does not exist in adt.m_consent_master (3rd phantom column;
 * the exception is triple-swallowed at DAO/service/controller). Module 400.
 */
@Component({
  selector: 'app-consent-master',
  standalone: true,
  imports: [],
  templateUrl: './consent-master.html',
  styleUrl: './consent-master.scss',
})
export class ConsentMaster {
  private readonly service = inject(ConsentMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ConsentRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly masked = signal(false);

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
            this.masked.set(false);
          } else {
            this.rows.set([]);
            this.masked.set(true);
            this.error.set(res.message ?? 'The consent list could not be loaded (see banner).');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.rows.set([]);
          this.masked.set(true);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: ConsentRow): string[] {
    return [
      String(row.consentMasterCode ?? '—'),
      String(row.consentMasterName ?? '—'),
      String(row.consentTypeMasterName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
