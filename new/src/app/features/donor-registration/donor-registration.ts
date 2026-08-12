import { Component, inject, signal } from '@angular/core';
import { DonorRegistrationService } from './donor-registration.service';
import { PrefixRow } from './donor-registration.models';

/**
 * Parity rebuild of screen 719 `donorRegistration` — a donor-registration FORM.
 * Legacy state: app.js:18128-18132 (controller donorRegistrationController,
 * template views/master/bloodBankDonor/donor-registration.html).
 *
 * LIVE-VERIFIED CLASSIFICATION (DQ-2026-08-12-034): NOT a master list — 719 is
 * a registration/entry form (transaction class). The only live feed on its path
 * is the prefix master list: GET /adt/getActivePrefixList (GETACTIVEPREFIXLIST,
 * adt.url.config.js:239), verified live 2026-08-12: SUCCESS, 1 row
 * {prefixId, prefixDesc, prefixCode, status} — rendered as a real feed table
 * below. The donor-registration form itself is write-path, deferred to the
 * transaction methodology (DQ-025/032/034).
 */
@Component({
  selector: 'app-donor-registration',
  standalone: true,
  imports: [],
  templateUrl: './donor-registration.html',
  styleUrl: './donor-registration.scss',
})
export class DonorRegistration {
  private readonly service = inject(DonorRegistrationService);

  readonly rows = signal<PrefixRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getPrefixes().subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'The prefix feed could not be loaded.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: PrefixRow): string[] {
    return [
      String(row.prefixCode ?? '—'),
      String(row.prefixDesc ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
