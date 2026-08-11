import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { LoyaltyCardConfigurationService } from './loyalty-card-configuration.service';
import { LoyaltyCardConfigRow } from './loyalty-card-configuration.models';

/**
 * Parity rebuild of the legacy `loyaltyCardConfiguration` screen
 * (master/unit/loyaltyCardConfigurationMasterController.js +
 * loyalty-card-configuration.html). Lists the loyalty card configuration
 * (min start amount, threshold, min recharge). Module id 743. Live-verified
 * 2026-08-12: 1 real row (cardConfigurationId 1, 1000/5000/1000).
 */
@Component({
  selector: 'app-loyalty-card-configuration',
  standalone: true,
  imports: [],
  templateUrl: './loyalty-card-configuration.html',
  styleUrl: './loyalty-card-configuration.scss',
})
export class LoyaltyCardConfiguration {
  private readonly service = inject(LoyaltyCardConfigurationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<LoyaltyCardConfigRow[]>([]);
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
            this.error.set(res.message ?? 'Could not load loyalty card configuration.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: LoyaltyCardConfigRow): string[] {
    return [
      String(row.minStartAmount ?? '—'),
      String(row.thresholdAmount ?? '—'),
      String(row.minRechargeAmount ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
