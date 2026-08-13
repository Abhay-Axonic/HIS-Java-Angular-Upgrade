import { Component, inject, signal, OnInit } from '@angular/core';
import { MlcReportingWorklistService } from './mlc-reporting-worklist.service';

@Component({
  selector: 'app-mlc-reporting-worklist',
  imports: [],
  templateUrl: './mlc-reporting-worklist.html',
  styleUrl: './mlc-reporting-worklist.scss',
})
export class MlcReportingWorklistComponent implements OnInit {
  private readonly service = inject(MlcReportingWorklistService);

  readonly rows = signal<any[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getWorklist(1, 1).subscribe({
      next: (res: any) => {
        const list = res?.listObject;
        this.rows.set(Array.isArray(list) ? list : []);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.loading.set(false);
        this.error.set('Worklist failed: ' + (err?.error?.message ?? 'the server rejected the request'));
      },
    });
  }
}
