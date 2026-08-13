import { Component, inject, signal, OnInit } from '@angular/core';
import { NurseDashboardService } from './nurse-dashboard.service';

@Component({
  selector: 'app-nurse-dashboard',
  imports: [],
  templateUrl: './nurse-dashboard.html',
  styleUrl: './nurse-dashboard.scss',
})
export class NurseDashboardComponent implements OnInit {
  private readonly service = inject(NurseDashboardService);

  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.occupancy(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('occupancy failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
    this.service.assignedBeds(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('assignedBeds failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
  }
}
