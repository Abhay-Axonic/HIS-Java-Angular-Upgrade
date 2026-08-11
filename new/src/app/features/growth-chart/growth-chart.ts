import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GrowthChartService } from './growth-chart.service';

@Component({
  selector: 'app-growth-chart',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './growth-chart.html',
  styleUrls: ['./growth-chart.scss'],
})
export class GrowthChart implements OnInit {
  private svc = inject(GrowthChartService);

  protected loading = signal(false);
  protected loaded = signal(false);
  protected error = signal('');
  protected growthChartList = signal<any[]>([]);
  protected genders = signal<any[]>([]);

  search = {
    organisationId: 1,
    unitId: 1,
    patientId: 1 as number | null,
    genderId: 2 as number | null,
  };

  ngOnInit(): void {
    this.svc.getGenders(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.genders.set(r.listObject || []);
      },
      error: () => {},
    });
    this.loadList();
  }

  loadList(): void {
    this.loading.set(true);
    this.error.set('');
    this.svc.getList(1, 1, 0, 10).subscribe({
      next: (r: any) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.growthChartList.set(r.listObject || []);
          this.loaded.set(true);
        } else {
          this.error.set('Growth chart list feed returned an error envelope.');
        }
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Growth chart list request failed.');
      },
    });
  }

  loadDetails(): void {
    this.loading.set(true);
    this.error.set('');
    this.svc.getDetails(this.search).subscribe({
      next: (r: any) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.growthChartList.set(
            Array.isArray(r.object) ? r.object : [r.object].filter(Boolean)
          );
          this.loaded.set(true);
        } else {
          this.error.set('Patient details feed returned an error envelope.');
        }
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Patient details request failed.');
      },
    });
  }
}
