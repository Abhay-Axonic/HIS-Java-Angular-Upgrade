import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { LicenseTypeMasterService } from './license-type-master.service';
import { LicenseTypeRow } from './license-type-master.models';

/**
 * Parity rebuild of the legacy `licenseTypeMaster` screen
 * (controller/master/global/licenseTypeMasterController.js +
 * license-type-master.html). Backend: GET
 * /org/getLiceneceTypeMasterList/{org} (note the legacy spelling
 * "Licenece"). Module id 379. Live-verified 2026-08-12: SUCCESS with an
 * empty listObject — honest dev-empty grid ("No records."), no banner.
 */
@Component({
  selector: 'app-license-type-master',
  standalone: true,
  imports: [],
  templateUrl: './license-type-master.html',
  styleUrl: './license-type-master.scss',
})
export class LicenseTypeMaster {
  private readonly service = inject(LicenseTypeMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<LicenseTypeRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load license types.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: LicenseTypeRow): string[] {
    return [
      String(row.id ?? '—'),
      String(row.name ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
