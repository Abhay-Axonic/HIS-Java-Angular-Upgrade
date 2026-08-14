import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PatientTypeDetailsService } from './patient-type-details.service';
import { PatientTypeRow, PatientTypeServiceDetail } from './patient-type-details.models';

/**
 * Parity rebuild of the legacy `patientTypeDetails` screen
 * (billing/unit/patientTypeDetailsController.js + patient-type-details.html).
 * Selects a patient type (list via /api/billing/getPatientType), then shows
 * its service-wise detail rows (getPatientTypeServiceWiseDetails) with
 * status toggles. Module id 765. List live-verified 2026-08-12 (3 real rows).
 */
@Component({
  selector: 'app-patient-type-details',
  standalone: true,
  imports: [FormsModule, StatusToggle],
  templateUrl: './patient-type-details.html',
  styleUrl: './patient-type-details.scss',
})
export class PatientTypeDetails {
  private readonly service = inject(PatientTypeDetailsService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly types = signal<PatientTypeRow[]>([]);
  readonly details = signal<PatientTypeServiceDetail[]>([]);
  readonly selectedTypeId = signal<number | null>(null);
  readonly loading = signal(false);
  readonly loadingDetails = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }
  private unitId(): number {
    return this.session.unitId ?? 1;
  }
  private userId(): number {
    return this.session.userId ?? 1;
  }

  ngOnInit(): void {
    this.loadTypes();
  }

  loadTypes(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getPatientTypes(this.orgId(), this.unitId()).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.types.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load patient types.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  selectType(id: number): void {
    this.selectedTypeId.set(id);
    this.loadingDetails.set(true);
    this.service.getDetails(id, this.orgId(), this.unitId()).subscribe({
      next: (res) => {
        this.loadingDetails.set(false);
        if (res.status === 'success') {
          this.details.set(res.listObject ?? []);
        } else {
          this.details.set([]);
        }
      },
      error: () => {
        this.loadingDetails.set(false);
        this.details.set([]);
      },
    });
  }

  /** Template handler for the patient-type <select> (change). */
  onTypeSelected(event: Event): void {
    const value = Number((event.target as HTMLSelectElement).value);
    if (Number.isFinite(value)) {
      this.selectType(value);
    }
  }

  toggle(row: PatientTypeServiceDetail): void {
    this.service.changeStatus({
      patientTypeServiceId: row.patientTypeServiceId,
      status: row.status === 'A' ? 'I' : 'A',
      updatedBy: this.userId(),
      updatedDate: formatLegacyNow(),
    }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Detail status updated.');
          this.selectType(this.selectedTypeId()!);
        } else {
          this.notify.error(res.message ?? 'Status update failed.');
        }
      },
      error: () => this.notify.error('Status update request failed.'),
    });
  }
}

/** Legacy: `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')` (ctrl:1353). */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
