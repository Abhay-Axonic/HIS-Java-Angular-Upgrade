import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { WrapperExpiryMasterService } from './wrapper-expiry-master.service';
import { WrapperExpiryRow } from './wrapper-expiry-master.models';

/**
 * Parity rebuild of the legacy `wrapperExpiryMaster` screen
 * (CSSD/unit/wrapperExpiryMasterController.js + wrapper-expiry-master.html).
 * Lists the wrapper expiry masters. Module id 554. Live-verified 2026-08-12:
 * 1 real row (WRPTEST01 "Test Wrapper", 30 days, status I).
 */
@Component({
  selector: 'app-wrapper-expiry-master',
  standalone: true,
  imports: [],
  templateUrl: './wrapper-expiry-master.html',
  styleUrl: './wrapper-expiry-master.scss',
})
export class WrapperExpiryMaster {
  private readonly service = inject(WrapperExpiryMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<WrapperExpiryRow[]>([]);
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
            this.error.set(res.message ?? 'Could not load wrapper expiry masters.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: WrapperExpiryRow): string[] {
    return [
      String(row.code ?? '—'),
      String(row.name ?? '—'),
      String(row.duration ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
