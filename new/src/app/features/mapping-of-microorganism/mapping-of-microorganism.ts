import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { MappingofMicroorganismService } from './mapping-of-microorganism.service';
import { MappingofMicroorganismRow } from './mapping-of-microorganism.models';

/**
 * Parity rebuild of the legacy `mappingofMicroorganism` screen
 * (controller/lis/global/antibioticOrganismMpprController.js +
 * mapping-of-microorganism.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-023): the list endpoint returns the
 * generic error envelope (HTTP 200, status=error, code 500, message
 * "Internal Server Error") on EVERY call, empty DB or not — NOT dev-empty.
 * LIS fall-through class: the DAO's size()>0 guard is a dead branch — the
 * HQL projection (`Transformers.aliasToBean` into Long-typed
 * createdDate/updatedDate DTO fields without the entity's @Convert
 * converter) throws first, and the exception is swallowed at
 * DAO/service/controller. Module 443.
 */
@Component({
  selector: 'app-mapping-of-microorganism',
  standalone: true,
  imports: [],
  templateUrl: './mapping-of-microorganism.html',
  styleUrl: './mapping-of-microorganism.scss',
})
export class MappingofMicroorganism {
  private readonly service = inject(MappingofMicroorganismService);
  private readonly session = inject(SessionService);

  readonly rows = signal<MappingofMicroorganismRow[]>([]);
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
          this.error.set(res.message ?? 'The micro-organism mapping list could not be loaded (see banner).');
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

  rowCells(row: MappingofMicroorganismRow): string[] {
    return [
      String(row.antibioticName ?? '—'),
      String(row.organismName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
