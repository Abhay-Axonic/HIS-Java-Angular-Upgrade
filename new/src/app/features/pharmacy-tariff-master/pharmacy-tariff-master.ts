import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PharmacyTariffMasterService } from './pharmacy-tariff-master.service';
import { PharmacyTariffRow } from './pharmacy-tariff-master.models';

/**
 * Parity rebuild of the legacy `pharmacyTariffMaster` screen
 * (billing/unit/pharmacyTariffMasterController.js + pharmacy-tariff-master.html).
 * Pharmacy tariff grid: asset/product category, mark-up/down % + amount,
 * status toggle. Backend: /api/units/pharmacy/tariff/*. Module id 803.
 * List live-verified 2026-08-12 (3 real rows).
 */
@Component({
  selector: 'app-pharmacy-tariff-master',
  standalone: true,
  imports: [FormsModule, StatusToggle],
  templateUrl: './pharmacy-tariff-master.html',
  styleUrl: './pharmacy-tariff-master.scss',
})
export class PharmacyTariffMaster {
  private readonly service = inject(PharmacyTariffMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly rows = signal<PharmacyTariffRow[]>([]);
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
          this.error.set(res.message ?? 'Could not load pharmacy tariffs.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  toggle(row: PharmacyTariffRow): void {
    this.service.changeStatus(row.id, row.status === 'A' ? 'I' : 'A', this.orgId(), this.unitId())
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Tariff status updated.');
            this.load();
          } else {
            this.notify.error(res.message ?? 'Status update failed.');
          }
        },
        error: () => this.notify.error('Status update request failed.'),
      });
  }

  rowCells(row: PharmacyTariffRow): string[] {
    return [
      String(row.assetCategoryName ?? '—'),
      String(row.productCategoryName ?? '—'),
      String(row.markUpDown ?? 0),
      String(row.markUpDownPer ?? 0),
    ];
  }
}
