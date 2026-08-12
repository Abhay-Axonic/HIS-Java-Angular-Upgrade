import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { SiteOfAdministrationService } from './site-of-administration.service';
import { SiteOfAdministrationRow } from './site-of-administration.models';

/**
 * Parity rebuild of the legacy `siteOfAdministration` screen
 * (controller/master/global/siteOfAdministartionController.js [legacy
 * filename typo "Administartion"] + site-of-administration.html). Backend:
 * POST /api/global/clinical/getSiteMasterList (body {organizationId} ONLY —
 * no unitId). Module id 753. Live-verified 2026-08-12: SUCCESS with 4 real
 * rows — real data grid, no banner.
 */
@Component({
  selector: 'app-site-of-administration',
  standalone: true,
  imports: [],
  templateUrl: './site-of-administration.html',
  styleUrl: './site-of-administration.scss',
})
export class SiteOfAdministration {
  private readonly service = inject(SiteOfAdministrationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<SiteOfAdministrationRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId()).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load sites of administration.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: SiteOfAdministrationRow): string[] {
    return [
      String(row.id ?? '—'),
      String(row.name ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
