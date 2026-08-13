import { Component, inject, signal, OnInit } from '@angular/core';
import { DetailsConsignmentManagerDashboardService } from './details-consignment-manager-dashboard.service';

@Component({
  selector: 'app-details-consignment-manager-dashboard',
  imports: [],
  templateUrl: './details-consignment-manager-dashboard.html',
  styleUrl: './details-consignment-manager-dashboard.scss',
})
export class DetailsConsignmentManagerDashboardComponent implements OnInit {
  private readonly service = inject(DetailsConsignmentManagerDashboardService);

  readonly rows = signal<any[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.load().subscribe({
      next: (res: any) => {
        const list = res?.listObject ?? res?.object;
        this.rows.set(Array.isArray(list) ? list : []);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.loading.set(false);
        this.error.set('Load failed: ' + (err?.error?.message ?? 'the server rejected the request'));
      },
    });
  }
}
