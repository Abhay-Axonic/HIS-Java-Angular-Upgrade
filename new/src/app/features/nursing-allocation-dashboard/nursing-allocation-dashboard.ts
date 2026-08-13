import { Component, inject, signal, OnInit } from '@angular/core';
import { NursingAllocationDashboardService } from './nursing-allocation-dashboard.service';

@Component({
  selector: 'app-nursing-allocation-dashboard',
  imports: [],
  templateUrl: './nursing-allocation-dashboard.html',
  styleUrl: './nursing-allocation-dashboard.scss',
})
export class NursingAllocationDashboardComponent implements OnInit {
  private readonly service = inject(NursingAllocationDashboardService);

  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.allocations(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('allocations failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
  }
}
