import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { IpConsultationConfigurationService } from './ip-consultation-configuration.service';
import { IpConsultationConfigRow } from './ip-consultation-configuration.models';

/**
 * Parity rebuild of the legacy `IPConsultationConfiguration` screen
 * (resources/assets/js/controller/master/global/
 * IPConsultationConfigurationMasterController.js + views/master/UnitMaster/
 * billingMaster/ip-consultation-configuration.html). Doctor IP-consultation
 * service-mapper grid. Route/state url '/IPConsultationConfiguration'
 * (app.js:17250-17277), module id 684 (app.js:17263), title from app.js:17254.
 * Module id 684.
 *
 * SCOPED REBUILD: read-only list of the mapper rows. The legacy add/edit
 * form (speciality + doctor + 5 service pickers) and its save/update calls
 * are out of scope here; only the list endpoint is wired.
 * Live 2026-08-12: POST /api/global/getDoctorIPConsultationServiceMapperList
 * returns SUCCESS with an empty listObject — honest dev-empty, the @empty
 * branch shows "No records." (no masked-error banner: nothing is lying).
 */
@Component({
  selector: 'app-ip-consultation-configuration',
  standalone: true,
  imports: [],
  templateUrl: './ip-consultation-configuration.html',
  styleUrl: './ip-consultation-configuration.scss',
})
export class IpConsultationConfiguration {
  private readonly service = inject(IpConsultationConfigurationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<IpConsultationConfigRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service
      .getList({
        organizationId: this.session.organizationId ?? 1,
        unitId: this.session.unitId ?? 1,
        offset: 0,
        noOfRecordsPerPage: 10,
      })
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'Could not load IP consultation configuration.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: IpConsultationConfigRow): string[] {
    return [
      String(row.specialityName ?? '—'),
      String(row.doctorName ?? '—'),
      String(row.firstConsultationServiceName ?? '—'),
      String(row.routineConsultationServiceName ?? '—'),
      String(row.noOfRoutineServices ?? '—'),
      String(row.followupRoutineServiceName ?? '—'),
      String(row.icuPrimaryConsultationServiceName ?? '—'),
    ];
  }
}
