import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ColonyTestWithMediaMasterService } from './colony-test-with-media-master.service';
import { ColonyTestWithMediaRow } from './colony-test-with-media-master.models';

/**
 * Parity rebuild of the legacy `colonyTestWithMediaMaster` screen
 * (controller/lis/global/mediaColnoyAdditionController.js [legacy filename
 * typo "Colnoy"] + colony-test-with-media.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-023): the list endpoint returns the
 * generic error envelope (HTTP 200, status=error, code 500, message
 * "Internal Server Error") on EVERY call, empty DB or not — NOT dev-empty.
 * LIS fall-through class: the DAO's size()>0 guard is a dead branch — the
 * HQL projection (`Transformers.aliasToBean` into Long-typed
 * createdDate/updatedDate DTO fields without the entity's @Convert
 * converter) throws first, and the exception is swallowed at
 * DAO/service/controller. Module 434.
 */
@Component({
  selector: 'app-colony-test-with-media-master',
  standalone: true,
  imports: [],
  templateUrl: './colony-test-with-media-master.html',
  styleUrl: './colony-test-with-media-master.scss',
})
export class ColonyTestWithMediaMaster {
  private readonly service = inject(ColonyTestWithMediaMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ColonyTestWithMediaRow[]>([]);
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
          this.error.set(res.message ?? 'The colony test with media list could not be loaded (see banner).');
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

  rowCells(row: ColonyTestWithMediaRow): string[] {
    return [
      String(row.colonyTestName ?? '—'),
      String(row.mediaName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
