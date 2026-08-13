import { Component, inject, signal, OnInit } from '@angular/core';
import { OtManagerDashboardService } from './ot-manager-dashboard.service';

@Component({
  selector: 'app-ot-manager-dashboard',
  imports: [],
  templateUrl: './ot-manager-dashboard.html',
  styleUrl: './ot-manager-dashboard.scss',
})
export class OtManagerDashboardComponent implements OnInit {
  private readonly service = inject(OtManagerDashboardService);

  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.specialities(1, 1).subscribe({
      next: (res) => { this.loading.set(false); },
      error: (err: any) => { this.loading.set(false); this.error.set('specialities failed: ' + (err?.error?.message ?? 'the server rejected the request')); },
    });
  }
}
