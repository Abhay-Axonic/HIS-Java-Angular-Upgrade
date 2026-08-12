import { Component, inject, signal } from '@angular/core';
import { AllInclusivePackageService } from './all-inclusive-package.service';
import { GenderRow } from './all-inclusive-package.models';

/**
 * Parity rebuild of screen 625 `allInclusivePackage` — the legacy state is a
 * package-creation FORM: app.js:10219-10221 registers it COMMENTED OUT with the
 * generic MasterController shell and template
 * views/master/UnitMaster/billingMaster/all-inclusive-package.html (zero
 * screen-specific controller logic).
 *
 * LIVE-VERIFIED CLASSIFICATION (DQ-2026-08-12-034): NOT a master list — 625 is
 * a registration/entry form (transaction class). The only live feed on its path
 * is the gender master list: GET /adt/getActiveGenderList (GETACTIVEGENDERLIST,
 * adt.url.config.js:170), verified live 2026-08-12: SUCCESS, 3 rows {id, desc,
 * code, createdBy, createdDate, status} — rendered as a real feed table below.
 * The package-creation form itself is write-path, deferred to the transaction
 * methodology (DQ-025/032/034).
 */
@Component({
  selector: 'app-all-inclusive-package',
  standalone: true,
  imports: [],
  templateUrl: './all-inclusive-package.html',
  styleUrl: './all-inclusive-package.scss',
})
export class AllInclusivePackage {
  private readonly service = inject(AllInclusivePackageService);

  readonly rows = signal<GenderRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getGenders().subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'The gender feed could not be loaded.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: GenderRow): string[] {
    return [
      String(row.code ?? '—'),
      String(row.desc ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
