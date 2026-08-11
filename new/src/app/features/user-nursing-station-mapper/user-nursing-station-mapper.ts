import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { UserNursingStationMapperService } from './user-nursing-station-mapper.service';
import { UserNursingStationMapperRow } from './user-nursing-station-mapper.models';

/**
 * Parity rebuild of the legacy `userNursingStationMapper` screen
 * (master/unit/userNursingStationMapperController.js +
 * user-nursing-station-mapper.html). Lists the user->nursing-station mapper
 * rows. Module id 689. Live-verified 2026-08-12: 1 real row (Admin  Admin /
 * EMP123).
 */
@Component({
  selector: 'app-user-nursing-station-mapper',
  standalone: true,
  imports: [],
  templateUrl: './user-nursing-station-mapper.html',
  styleUrl: './user-nursing-station-mapper.scss',
})
export class UserNursingStationMapper {
  private readonly service = inject(UserNursingStationMapperService);
  private readonly session = inject(SessionService);

  readonly rows = signal<UserNursingStationMapperRow[]>([]);
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
            this.error.set(res.message ?? 'Could not load user nursing station mappers.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: UserNursingStationMapperRow): string[] {
    return [
      String(row.employeeName ?? '—'),
      String(row.empCode ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
