import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {ErRegistrationService} from './er-registration.service';
import type {ErRegistrationPayload, IdNameRow, PatientRow} from './er-registration.models';

/** ER-Encounter (unknown patient registration) — the ER registration: patient
 * search, the personal-details form (priority, mode of arrival, MLC checkbox)
 * per docs/SPEC_ER_MODULE_2026-08-16.md. DATE CONVENTION: String dates via
 * formatLegacyNow — the registration/prescription convention, NOT the
 * encounter's Long. */
@Component({
  selector: 'app-er-registration',
  imports: [FormsModule],
  templateUrl: './er-registration.html',
  styleUrl: './er-registration.scss',
})
export class ErRegistrationComponent {
  private api = inject(ErRegistrationService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  readonly priorityList = signal<IdNameRow[]>([]);
  readonly priorityId = signal<number | null>(null);
  readonly searchKeyword = signal('');
  readonly patientResults = signal<PatientRow[]>([]);
  readonly searchOpen = signal(false);
  readonly patientName = signal('');
  readonly age = signal<number | null>(null);
  readonly ageFormat = signal('Y');
  readonly accompaniedBy = signal('');
  readonly isMedicoLegal = signal(false);
  readonly saving = signal(false);

  constructor() {
    this.api.getPriorityList(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.priorityList.set(r.listObject ?? []);
    });
  }

  searchPatient(): void {
    const kw = this.searchKeyword().trim();
    if (!kw) return;
    this.api.searchPatient({search: kw, organizationId: this.orgId, unitId: this.unitId}).subscribe((r) => {
      if (r.status === 'success') this.patientResults.set(r.listObject ?? []);
      this.searchOpen.set(true);
    });
  }

  pickPatient(p: PatientRow): void {
    this.patientName.set([p.firstName, p.lastName].filter(Boolean).join(' ') || String(p['patientName'] ?? ''));
    this.searchOpen.set(false);
  }

  save(): void {
    if (!this.patientName().trim() || this.priorityId() == null) {
      this.notify.warning('Patient name and priority are required');
      return;
    }
    this.saving.set(true);
    const now = formatLegacyNow();
    const payload: ErRegistrationPayload = {
      organizationId: this.orgId,
      unitId: this.unitId,
      patientName: this.patientName(),
      priorityId: this.priorityId(),
      age: this.age(),
      ageFormat: this.ageFormat(),
      accompaniedBy: this.accompaniedBy() || null,
      isMedicoLegal: this.isMedicoLegal() ? 'Y' : 'N',
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
    };
    this.api.save(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') this.notify.warning('ER registration saved');
        else this.notify.warning(r.message ?? 'Save failed');
      },
      error: () => { this.saving.set(false); this.notify.error('Save failed'); },
    });
  }
}

/** The AdmissionDto's String-date format (dd-M-yyyy HH:mm:ss) — the
 * registration/prescription convention, NOT the encounter's Long. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
