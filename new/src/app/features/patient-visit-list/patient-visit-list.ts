import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { PatientVisitListService } from './patient-visit-list.service';
import type { IdNameRow, VisitCriteria, VisitRow } from './patient-visit-list.models';

/** OPD Patient Visit List — the appointment list/queue that feeds the
 * encounter screen (docs/SPEC_OPD_FOUR_VARIANTS_2026-08-16.md; the legacy
 * screen is real — appointmentListController). The row click carries the
 * patient + encounter into the encounter screen. DATE CONVENTION: STRING
 * dates via formatLegacyNow — the registration/prescription convention, NOT
 * the encounter's Long. */
@Component({
  selector: 'app-patient-visit-list',
  imports: [FormsModule],
  templateUrl: './patient-visit-list.html',
  styleUrl: './patient-visit-list.scss',
})
export class PatientVisitListComponent {
  private api = inject(PatientVisitListService);
  private session = inject(SessionService);
  private router = inject(Router);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;

  readonly doctorId = signal<number | null>(null);
  readonly doctorList = signal<IdNameRow[]>([]);
  readonly appointmentDate = signal('');
  readonly rows = signal<VisitRow[]>([]);
  readonly loading = signal(false);
  readonly loaded = signal(false);

  constructor() {
    this.api.getDoctors(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.doctorList.set(r.listObject ?? []);
    });
  }

  load(): void {
    const criteria: VisitCriteria = {
      organizationId: this.orgId,
      unitId: this.unitId,
      doctorId: this.doctorId(),
      appointmentDate: this.appointmentDate() || null,
    };
    this.loading.set(true);
    this.api.list(criteria).subscribe({
      next: (r) => {
        this.loading.set(false);
        this.loaded.set(true);
        if (r.status === 'success') this.rows.set(r.listObject ?? []);
      },
      error: () => {
        this.loading.set(false);
        this.loaded.set(true);
        this.notify.error('Visit list load failed');
      },
    });
  }

  openVisit(row: VisitRow): void {
    // carry the patient + encounter into the encounter screen (module 5)
    const session = this.session.current as Record<string, unknown> | null;
    if (session) {
      session['patientId'] = row.patientId;
      session['encounterId'] = row.encounterId;
    }
    this.router.navigate(['/encounter']);
  }
}
