import { Component, inject, signal, OnInit } from '@angular/core';
import { DueDepositReportService } from './due-deposit-report.service';

@Component({
  selector: 'app-due-deposit-report',
  imports: [],
  templateUrl: './due-deposit-report.html',
  styleUrl: './due-deposit-report.scss',
})
export class DueDepositReportComponent implements OnInit {
  private readonly service = inject(DueDepositReportService);

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
