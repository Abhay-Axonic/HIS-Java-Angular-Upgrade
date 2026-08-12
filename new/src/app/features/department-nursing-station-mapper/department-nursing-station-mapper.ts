import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { DepartmentNursingStationMapperService } from './department-nursing-station-mapper.service';
import { DepartmentNursingStationMapperRow } from './department-nursing-station-mapper.models';

/**
 * Parity rebuild of the legacy `departmentNursingStationMapper` screen —
 * REAL DATA (2 live rows, DQ-032 close-out final master batch (a); module
 * 746).
 */
@Component({
  selector: 'app-department-nursing-station-mapper',
  standalone: true,
  imports: [],
  templateUrl: './department-nursing-station-mapper.html',
  styleUrl: './department-nursing-station-mapper.scss',
})
export class DepartmentNursingStationMapper {
  private readonly service = inject(DepartmentNursingStationMapperService);
  private readonly session = inject(SessionService);

  readonly rows = signal<DepartmentNursingStationMapperRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.session.organizationId ?? 1, this.session.unitId ?? 1, 0, 10)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'The mapper list could not be loaded.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: DepartmentNursingStationMapperRow): string[] {
    return [
      String(row.departmentId ?? '—'),
      String(row.nursingStationId ?? '—'),
      String(row.departmentName ?? '—'),
      String(row.nursingStationName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
