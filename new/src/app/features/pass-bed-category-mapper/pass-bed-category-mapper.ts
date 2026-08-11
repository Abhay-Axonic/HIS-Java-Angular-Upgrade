import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { PassBedCategoryMapperService } from './pass-bed-category-mapper.service';
import { PassBedCategoryMapperRow } from './pass-bed-category-mapper.models';

/**
 * Parity rebuild of the legacy `passBedCategoryMapper` screen
 * (master/unit/passBedCategoryMapperController.js +
 * views/master/UnitMaster/Visit Type/pass-Bed-Category-Mapper.html).
 * Backend: POST /api/unit/getVisitorPassMapperList (body
 * {unitId, organisationId, offset, noOfRecordsPerPage}). Module id 822.
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-pass-bed-category-mapper',
  standalone: true,
  imports: [],
  templateUrl: './pass-bed-category-mapper.html',
  styleUrl: './pass-bed-category-mapper.scss',
})
export class PassBedCategoryMapper {
  private readonly service = inject(PassBedCategoryMapperService);
  private readonly session = inject(SessionService);

  readonly rows = signal<PassBedCategoryMapperRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }
  private unitId(): number {
    return this.session.unitId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), this.unitId(), 0, 10).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load pass bed category mappers.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: PassBedCategoryMapperRow): string[] {
    return [
      String(row.visitorPassTypeDesc ?? '—'),
      String(row.bedCategoryDesc ?? '—'),
      String(row.passDesc ?? '—'),
      String(row.noOfPass ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
