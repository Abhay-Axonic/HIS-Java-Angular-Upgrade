import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { KitCreationService } from './kit-creation.service';
import { KitRow } from './kit-creation.models';

/**
 * Parity rebuild of the legacy `kitCreation` screen
 * (CSSD/unit/kitCreationController.js + kit-creation.html). Lists the
 * instrument kits. Module id 555. Live-verified 2026-08-12: SUCCESS empty
 * (honest dev-empty).
 */
@Component({
  selector: 'app-kit-creation',
  standalone: true,
  imports: [],
  templateUrl: './kit-creation.html',
  styleUrl: './kit-creation.scss',
})
export class KitCreation {
  private readonly service = inject(KitCreationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<KitRow[]>([]);
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
            this.error.set(res.message ?? 'Could not load kits.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: KitRow): string[] {
    return [
      String(row.kitCode ?? '—'),
      String(row.kitName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
