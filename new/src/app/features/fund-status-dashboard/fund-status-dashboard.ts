import { Component, inject, signal, OnInit } from '@angular/core';
import { FundStatusDashboardService } from './fund-status-dashboard.service';
import { TrustRow, FundCollectionRow } from './fund-status-dashboard.models';

@Component({
  selector: 'app-fund-status-dashboard',
  imports: [],
  templateUrl: './fund-status-dashboard.html',
  styleUrl: './fund-status-dashboard.scss',
})
export class FundStatusDashboardComponent implements OnInit {
  private readonly service = inject(FundStatusDashboardService);

  readonly trusts = signal<TrustRow[]>([]);
  readonly fundRows = signal<FundCollectionRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getActiveTrustList(1, 1).subscribe({
      next: (res) => {
        this.trusts.set(Array.isArray(res?.listObject) ? res.listObject : []);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.loading.set(false);
        this.error.set('Trust list failed: ' + (err?.error?.message ?? 'the server rejected the request'));
      },
    });
  }

  selectTrust(trustId: number): void {
    this.service.getFundCollectionListByTrust(1, 1).subscribe({
      next: (res) => {
        this.fundRows.set(Array.isArray(res?.listObject) ? res.listObject : []);
      },
      error: (err: any) => {
        this.error.set('Fund list failed: ' + (err?.error?.message ?? 'the server rejected the request'));
      },
    });
  }
}
