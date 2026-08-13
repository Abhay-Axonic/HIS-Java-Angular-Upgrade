import { Component, inject, signal, OnInit } from '@angular/core';
import { StoreStockDashboardService } from './store-stock-dashboard.service';
import { StoreRow } from './store-stock-dashboard.models';

@Component({
  selector: 'app-store-stock-dashboard',
  imports: [],
  templateUrl: './store-stock-dashboard.html',
  styleUrl: './store-stock-dashboard.scss',
})
export class StoreStockDashboardComponent implements OnInit {
  private readonly service = inject(StoreStockDashboardService);

  readonly stores = signal<StoreRow[]>([]);
  readonly selectedStore = signal<StoreRow | null>(null);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.loadStores();
  }

  loadStores(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getStores(1, 1, 1).subscribe({
      next: (res) => {
        this.stores.set(Array.isArray(res) ? res : []);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.loading.set(false);
        this.error.set('Store list failed: ' + (err?.error?.message ?? 'the server rejected the request'));
      },
    });
  }

  selectStore(store: StoreRow): void {
    this.selectedStore.set(store);
  }
}
