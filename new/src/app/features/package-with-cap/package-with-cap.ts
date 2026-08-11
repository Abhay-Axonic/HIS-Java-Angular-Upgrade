import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { PackageWithCapService } from './package-with-cap.service';
import { PackageRow } from './package-with-cap.models';

/**
 * Parity rebuild of the legacy `packageWithCAP` screen
 * (services/unit/packageWithCAPController.js + views/master/UnitMaster/billingMaster/
 * package-with-CAP-main.html): package master list (packageTypeId 3) via
 * POST /api/packages/filter. Module id 624. Live-verified 2026-08-12:
 * SUCCESS with empty listObject — honest dev-empty, the @empty branch
 * renders "No records." until the table has rows. The add/edit/view
 * builder is a separate legacy flow (new-package-with-CAP.html) — flagged
 * follow-up, not part of this list-only rebuild.
 */
@Component({
  selector: 'app-package-with-cap',
  standalone: true,
  imports: [],
  templateUrl: './package-with-cap.html',
  styleUrl: './package-with-cap.scss',
})
export class PackageWithCap {
  private readonly service = inject(PackageWithCapService);
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
