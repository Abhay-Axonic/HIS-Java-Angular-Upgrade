import { Component, inject, signal, OnInit } from '@angular/core';
import { RadiologyReportReleaseWorklistService } from './radiology-report-release-worklist.service';

@Component({
  selector: 'app-radiology-report-release-worklist',
  imports: [],
  templateUrl: './radiology-report-release-worklist.html',
  styleUrl: './radiology-report-release-worklist.scss',
})
export class RadiologyReportReleaseWorklistComponent implements OnInit {
  private readonly service = inject(RadiologyReportReleaseWorklistService);

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
