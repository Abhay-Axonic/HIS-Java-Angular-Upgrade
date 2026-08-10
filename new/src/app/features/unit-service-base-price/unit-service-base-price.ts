import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { UnitServiceBasePriceService, ServiceBasePriceRow } from './unit-service-base-price.service';

@Component({
  selector: 'app-unit-service-base-price',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './unit-service-base-price.html',
  styleUrl: './unit-service-base-price.scss',
})
export class UnitServiceBasePrice {
  private svc = inject(UnitServiceBasePriceService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected rows = signal<ServiceBasePriceRow[]>([]);
  protected loading = signal(false);
  protected searchTerm = '';

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }

  constructor() {
    this.loadList();
  }

  protected loadList(): void {
    this.loading.set(true);
    this.svc.list(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.rows.set((r.listObject as ServiceBasePriceRow[]) ?? []);
        } else {
          this.notify.error(r?.message || 'List failed');
        }
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('List request failed');
      },
    });
  }

  protected filteredRows = computed(() => {
    const q = this.searchTerm.toLowerCase();
    return this.rows().filter((r) =>
      !q || (r.serviceName || '').toLowerCase().includes(q) || String(r.basePrice ?? '').includes(q)
    );
  });

  protected onSearch(): void {
    // live-filter via computed
  }

  protected clearSearch(): void {
    this.searchTerm = '';
  }
}
