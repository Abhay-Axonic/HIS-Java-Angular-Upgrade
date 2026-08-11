import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CounterWorklistService } from './counter-worklist.service';

@Component({
  selector: 'app-counter-worklist',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './counter-worklist.html',
  styleUrls: ['./counter-worklist.scss'],
})
export class CounterWorklist implements OnInit {
  private svc = inject(CounterWorklistService);

  protected loading = signal(false);
  protected searched = signal(false);
  protected searchError = signal('');
  protected nursingStations = signal<any[]>([]);
  protected encounters = signal<any[]>([]);

  search = {
    organizationId: 1,
    unitId: 1,
    nursingStationId: '1',
    doctorId: '0',
    clinicId: '0',
    fromDate: '',
    toDate: '',
  };

  ngOnInit(): void {
    const to = new Date();
    const from = new Date();
    from.setDate(to.getDate() - 7);
    this.search.fromDate = this.fmt(from);
    this.search.toDate = this.fmt(to);
    this.svc.getNursingStations(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.nursingStations.set(r.listObject || []);
      },
      error: () => {},
    });
  }

  private fmt(d: Date): string {
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
      d.getDate()
    ).padStart(2, '0')}`;
  }

  searchWorklist(): void {
    this.loading.set(true);
    this.searchError.set('');
    this.searched.set(false);
    this.svc.search(this.search).subscribe({
      next: (r: any) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.encounters.set(r.listObject || []);
          this.searched.set(true);
        } else {
          // the endpoint falls through to a masked ERROR/null envelope —
          // surfaced honestly (see the AMBER banner for the mechanism)
          this.searchError.set(
            'Search returned an error envelope — the worklist feed is ' +
              'broken by phantom columns (see the banner above).'
          );
        }
      },
      error: (err: any) => {
        this.loading.set(false);
        this.searchError.set('Search request failed: ' + (err?.status || 'unknown'));
      },
    });
  }
}
