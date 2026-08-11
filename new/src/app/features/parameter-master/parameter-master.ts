import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ParameterMasterService } from './parameter-master.service';
import { ParameterRow } from './parameter-master.models';

/**
 * Parity rebuild of the legacy `parameterMaster` screen
 * (lis/unit/parameterMasterController.js + parameter-master.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-009): the list endpoint 500s with the
 * correlation envelope "Something went wrong. Reference: ERR-<uuid>" — NOT
 * dev-empty. Root cause: the GET_PAGINATED_PARAMETER_MASTER_LIST named query
 * selects parameterMaster.techniqueId, a column that does not exist in
 * lab.m_parameter_master (phantom column; verified information_schema
 * 2026-08-12; ParameterMaster.java:131/:289). Per the BANNER-TIER RULE the
 * screen renders an AMBER-permanent banner naming the column and the
 * envelope — the response is the thing that lies (empty would be honest;
 * this is a backend defect). Module id 562.
 */
@Component({
  selector: 'app-parameter-master',
  standalone: true,
  imports: [],
  templateUrl: './parameter-master.html',
  styleUrl: './parameter-master.scss',
})
export class ParameterMaster {
  private readonly service = inject(ParameterMasterService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ParameterRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly masked = signal(false);

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
            this.masked.set(false);
          } else {
            const msg = res.message ?? 'Could not load parameters.';
            this.rows.set([]);
            // The correlation envelope = masked backend defect (not honest
            // dev-empty). Anything else = the descriptive class.
            this.masked.set(this.isCorrelationEnvelope(msg));
            this.error.set(msg);
          }
        },
        error: (err) => {
          this.loading.set(false);
          // The auth interceptor rejects HTTP-500 with a generic message, so
          // the backend's correlation envelope ("Something went wrong.
          // Reference: ERR-...") never reaches `next`. But for THIS screen a
          // 500 IS the masked-error class (phantom column technique_id,
          // DQ-2026-08-12-009) — surface it as such, not as a plain failure.
          const status = err?.status ?? 0;
          this.rows.set([]);
          if (status === 500) {
            this.masked.set(true);
            this.error.set('Something went wrong. Reference: ERR-<backend> (HTTP 500 — see banner).');
          } else {
            this.masked.set(false);
            this.error.set(`Request failed: ${status} ${err?.message ?? ''}`);
          }
        },
      });
  }

  /** True when the message is the ErrorCorrelationUtil masked envelope. */
  private isCorrelationEnvelope(msg: string): boolean {
    return /reference:\s*err-/i.test(msg);
  }

  rowCells(row: ParameterRow): string[] {
    return [
      String(row.parameterCode ?? '—'),
      String(row.parameterName ?? '—'),
      String(row.unitName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
