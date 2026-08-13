import { Component, inject, signal, OnInit } from '@angular/core';
import { NursingShiftDashboardService } from './nursing-shift-dashboard.service';

@Component({
  selector: 'app-nursing-shift-dashboard',
  imports: [],
  templateUrl: './nursing-shift-dashboard.html',
  styleUrl: './nursing-shift-dashboard.scss',
})
export class NursingShiftDashboardComponent implements OnInit {
  private readonly service = inject(NursingShiftDashboardService);

  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.nurseAllocations(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('nurseAllocations failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
  }
}
