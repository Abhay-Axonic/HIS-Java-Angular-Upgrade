import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ApplicationConfigurationService } from './application-configuration.service';
import { ServiceMasterRow } from './application-configuration.models';

/**
 * Parity rebuild of screen 660 `applicationConfiguration` — a CONFIG FORM fed
 * by the service-master dropdown. Legacy state: the applicationConfiguration
 * state is COMMENTED OUT (app.js:15352-15357, template
 * views/master/UnitMaster/configuration/application-master.html,
 * MasterController); the real config logic lives in
 * applicationConfigurationController.js (getPharmacyServiceList, ctrl:953-966:
 * GET /billing/getListOfServiceMaster/{orgId}/{unitId}).
 *
 * LIVE-VERIFIED CLASSIFICATION (DQ-2026-08-12-034): NOT a master list — a
 * config form fed by the service-master dropdown (a ~100-field service DTO).
 * The service-master feed, verified live 2026-08-12: SUCCESS, 1 row
 * {serviceMasterId, serviceStandardName, ...} — rendered as a real feed table
 * below. The configuration form itself is write-path, deferred to the
 * transaction methodology (DQ-025/032/034).
 */
@Component({
  selector: 'app-application-configuration',
  standalone: true,
  imports: [],
  templateUrl: './application-configuration.html',
  styleUrl: './application-configuration.scss',
})
export class ApplicationConfiguration {
  private readonly service = inject(ApplicationConfigurationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ServiceMasterRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service
      .getServiceMasters(this.session.organizationId ?? 1, this.session.unitId ?? 1)
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'The service-master feed could not be loaded.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: ServiceMasterRow): string[] {
    return [
      String(row.serviceMasterId ?? '—'),
      String(row.serviceStandardName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
