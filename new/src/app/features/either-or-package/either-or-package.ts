import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { EitherOrPackageService } from './either-or-package.service';
import { PackageRow } from './either-or-package.models';

/**
 * Parity rebuild of the legacy `eitherOrPackage` screen
 * (services/unit/eitherOrPackageController.js + views/master/UnitMaster/billingMaster/
 * either-or-package-main.html): package master list (packageTypeId 2) via
 * POST /api/packages/filter. Module id 626. Live-verified 2026-08-12:
 * SUCCESS with empty listObject — honest dev-empty, the @empty branch
 * renders "No records." until the table has rows. The add/edit/view
 * builder is a separate legacy flow (add-new-either-or-package.html) —
 * flagged follow-up, not part of this list-only rebuild.
 */
@Component({
  selector: 'app-either-or-package',
  standalone: true,
  imports: [],
  templateUrl: './either-or-package.html',
  styleUrl: './either-or-package.scss',
})
export class EitherOrPackage {
  private readonly service = inject(EitherOrPackageService);
  private readonly session = inject(SessionService);

  readonly rows = signal<PackageRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load packages.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: PackageRow): string[] {
    return [
      String(row.packageType ?? '—'),
      String(row.packageCode ?? '—'),
      String(row.packageName ?? '—'),
      String(row.versionName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
