import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { AutoServicePostingConfigurationService } from './auto-service-posting-configuration.service';
import { AutoPostingConfigRow } from './auto-service-posting-configuration.models';

/**
 * Parity rebuild of the legacy `autoServicePostingConfiguration` screen
 * (master/unit/autoServicePostingConfigurationController.js +
 * auto-service-posting-configuration.html).
 *
 * MASKED-ERROR BANNER (DQ-2026-08-12-013): the list endpoint 500s with the
 * correlation envelope "Something went wrong. Reference: ERR-<uuid>" — NOT
 * dev-empty. Root cause: the raw SQL in AutoPostingConfigDaoImpl.java:92,134
 * selects autopostin0_.is_bed_charges_service, a column that does not exist
 * in public.t_auto_posting_services_cofiguration (phantom column; verified
 * information_schema 2026-08-12; 2nd phantom column found, after
 * parameterMaster's technique_id). Per the BANNER-TIER RULE the screen
 * renders an AMBER-permanent banner naming the column and the envelope.
 * Module id 775.
 */
@Component({
  selector: 'app-auto-service-posting-configuration',
  standalone: true,
  imports: [],
  templateUrl: './auto-service-posting-configuration.html',
  styleUrl: './auto-service-posting-configuration.scss',
})
export class AutoServicePostingConfiguration {
  private readonly service = inject(AutoServicePostingConfigurationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<AutoPostingConfigRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly masked = signal(false);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.session.organizationId ?? 1, this.session.unitId ?? 1)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
            this.masked.set(false);
          } else {
            const msg = res.message ?? 'Could not load auto service posting configuration.';
            this.rows.set([]);
            this.masked.set(this.isCorrelationEnvelope(msg));
            this.error.set(msg);
          }
        },
        error: (err) => {
          this.loading.set(false);
          // The auth interceptor rejects HTTP-500 with a generic message, so
          // the backend's correlation envelope never reaches `next`. For THIS
          // screen a 500 IS the masked-error class (phantom column
          // is_bed_charges_service, DQ-2026-08-12-013).
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

  rowCells(row: AutoPostingConfigRow): string[] {
    return [
      String(row.serviceId ?? '—'),
      String(row.specialityId ?? '—'),
      String(row.postingTime ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
