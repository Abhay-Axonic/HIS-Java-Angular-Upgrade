import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EncounterService } from './encounter.service';

@Component({
  selector: 'app-encounter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './encounter.html',
  styleUrls: ['./encounter.scss'],
})
export class Encounter implements OnInit {
  private svc = inject(EncounterService);

  search = {
    organizationId: 1,
    unitId: 1,
    patientId: null as number | null,
    fromDate: '',
    toDate: '',
  };
  protected loading = signal(false);
  protected searched = signal(false);
  protected searchError = signal('');
  protected encounters = signal<any[]>([]);
  protected selectedEncounter = signal<any | null>(null);

  ngOnInit(): void {
    // prefill the last 7 days as the search window
    const to = new Date();
    const from = new Date();
    from.setDate(to.getDate() - 7);
    this.search.fromDate = this.fmt(from);
    this.search.toDate = this.fmt(to);
  }

  private fmt(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

  searchEncounter(): void {
    this.loading.set(true);
    this.searchError.set('');
    this.searched.set(false);
    this.svc.searchEncounters(this.search).subscribe({
      next: (res: any) => {
        this.loading.set(false);
        if (res && res.status === 'success') {
          this.encounters.set(res.listObject || []);
          this.searched.set(true);
        } else {
          this.searchError.set((res && res.message) || 'Search failed.');
        }
      },
      error: (err: any) => {
        this.loading.set(false);
        this.searchError.set(
          'Search request failed: ' + (err?.message || err?.status || 'unknown')
        );
      },
    });
  }

  selectEncounter(row: any): void {
    this.selectedEncounter.set(row);
  }

  clearSelection(): void {
    this.selectedEncounter.set(null);
  }

  json(row: any): string {
    return JSON.stringify(row, null, 2);
  }
}
