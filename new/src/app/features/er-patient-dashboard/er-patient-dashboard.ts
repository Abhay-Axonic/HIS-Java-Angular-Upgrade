import { Component, inject, signal, OnInit } from '@angular/core';
import { ErPatientDashboardService } from './er-patient-dashboard.service';

@Component({
  selector: 'app-er-patient-dashboard',
  imports: [],
  templateUrl: './er-patient-dashboard.html',
  styleUrl: './er-patient-dashboard.scss',
})
export class ErPatientDashboardComponent implements OnInit {
  private readonly service = inject(ErPatientDashboardService);

  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.counts(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('counts failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
    this.service.opVisits(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('opVisits failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
  }
}
