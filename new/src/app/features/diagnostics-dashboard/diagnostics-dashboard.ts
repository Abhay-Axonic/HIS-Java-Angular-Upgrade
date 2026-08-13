import { Component, inject, signal, OnInit } from '@angular/core';
import { DiagnosticsDashboardService } from './diagnostics-dashboard.service';

@Component({
  selector: 'app-diagnostics-dashboard',
  imports: [],
  templateUrl: './diagnostics-dashboard.html',
  styleUrl: './diagnostics-dashboard.scss',
})
export class DiagnosticsDashboardComponent implements OnInit {
  private readonly service = inject(DiagnosticsDashboardService);

  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.financeCounts(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('financeCounts failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
  }
}
