import { Component, inject, signal, OnInit } from '@angular/core';
import { MpShahEncounterSummaryService } from './mp-shah-encounter-summary.service';

@Component({
  selector: 'app-mp-shah-encounter-summary',
  imports: [],
  templateUrl: './mp-shah-encounter-summary.html',
  styleUrl: './mp-shah-encounter-summary.scss',
})
export class MpShahEncounterSummaryComponent implements OnInit {
  private readonly service = inject(MpShahEncounterSummaryService);

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
