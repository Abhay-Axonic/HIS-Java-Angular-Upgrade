import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { SampleTypeMediaMappingMasterService } from './sample-type-media-mapping-master.service';
import { SampleTypeMediaMappingRow } from './sample-type-media-mapping-master.models';

/**
 * Parity rebuild of the legacy `sampleTypeMediaMappingMaster` screen
 * (controller/lis/global/sampleTypeMediaMappingController.js +
 * sample-type-media-mapping.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-023): the list endpoint returns the
 * generic error envelope (HTTP 200, status=error, code 500, message
 * "Internal Server Error") on EVERY call, empty DB or not — NOT dev-empty.
 * The DAO's size()>0 guard is a dead branch: the HQL projection
 * (`Transformers.aliasToBean` into Long-typed createdDate/updatedDate DTO
 * fields without the entity's @Convert converter) throws first, and the
 * exception is swallowed at DAO/service/controller. Module 426.
 */
@Component({
  selector: 'app-sample-type-media-mapping-master',
  standalone: true,
  imports: [],
  templateUrl: './sample-type-media-mapping-master.html',
  styleUrl: './sample-type-media-mapping-master.scss',
})
export class SampleTypeMediaMappingMaster {
  private readonly service = inject(SampleTypeMediaMappingMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<SampleTypeMediaMappingRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly masked = signal(false);

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), 0, 10).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
          this.masked.set(false);
        } else {
          this.rows.set([]);
          this.masked.set(true);
          this.error.set(res.message ?? 'The sample type media mapping list could not be loaded (see banner).');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.rows.set([]);
        this.masked.set(true);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: SampleTypeMediaMappingRow): string[] {
    return [
      String(row.sampleTypeName ?? '—'),
      String(row.mediaName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
