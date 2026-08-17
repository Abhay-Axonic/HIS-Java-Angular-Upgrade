import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { InterconsultationService } from './interconsultation.service';
import type { ConsultPayload, IdNameRow, ReferralRow } from './interconsultation.models';

/** OPD Interconsultation — the core scope per
 * docs/SPEC_INTERCONSULTATION_2026-08-16.md: patient/encounter context header,
 * refer-to department + doctor selection (the cascade), reason (notes),
 * urgency (priority), save referral, referral list (incoming + outgoing).
 * The legacy screen is a dead mockup — the backend DTOs are the contract.
 * DATE CONVENTION: String dates (dd-M-yyyy HH:mm:ss via formatLegacyNow) —
 * the registration/prescription convention, NOT the encounter's Long. */
@Component({
  selector: 'app-interconsultation',
  imports: [FormsModule],
  templateUrl: './interconsultation.html',
  styleUrl: './interconsultation.scss',
})
export class InterconsultationComponent {
  private api = inject(InterconsultationService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  // encounter context (read-only header)
  readonly encounterId = signal<number | null>(null);
  readonly patientId = signal<number | null>(null);
  readonly patientName = signal('');
  readonly refByDoctorId = signal<number | null>(null);

  // the refer-to cascade
  readonly specialityList = signal<IdNameRow[]>([]);
  readonly doctorList = signal<IdNameRow[]>([]);
  readonly refToSpecialityId = signal<number | null>(null);
  readonly refToDoctorId = signal<number | null>(null);

  // the referral fields
  readonly notes = signal('');
  readonly priority = signal('');

  // the list
  readonly referrals = signal<ReferralRow[]>([]);
  readonly saving = signal(false);
  readonly loaded = signal(false);

  constructor() {
    const ctx = this.session.current as Record<string, unknown> | null;
    const eid = ctx?.['encounterId'];
    const pid = ctx?.['patientId'];
    this.encounterId.set(typeof eid === 'number' ? eid : null);
    this.patientId.set(typeof pid === 'number' ? pid : null);
    this.api.getSpecialities(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.specialityList.set(r.listObject ?? []);
    });
    this.loadList();
  }

  onSpecialityChange(id: number | null): void {
    this.refToSpecialityId.set(id);
    this.refToDoctorId.set(null);
    this.doctorList.set([]);
    if (id != null) {
      this.api.getDoctors(id).subscribe((r) => {
        if (r.status === 'success') this.doctorList.set(r.listObject ?? []);
      });
    }
  }

  save(): void {
    if (this.refToSpecialityId() == null || this.refToDoctorId() == null || !this.notes().trim()) {
      this.notify.warning('Select the department, doctor and enter the reason');
      return;
    }
    this.saving.set(true);
    const now = formatLegacyNow();
    const payload: ConsultPayload = {
      organizationId: this.orgId,
      unitId: this.unitId,
      encounterId: this.encounterId(),
      patientId: this.patientId(),
      refByDoctorId: this.refByDoctorId(),
      refToSpecialityId: this.refToSpecialityId(),
      refToDoctorId: this.refToDoctorId(),
      notes: this.notes(),
      priority: this.priority() || null,
      status: 'A',
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
    };
    this.api.saveReferral(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') {
          this.notify.warning('Referral saved');
          this.notes.set('');
          this.priority.set('');
          this.loadList();
        } else {
          this.notify.warning(r.message ?? 'Referral save failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Referral save failed');
      },
    });
  }

  loadList(): void {
    this.api
      .listByEncounter({ encounterId: this.encounterId(), unitId: this.unitId, organizationId: this.orgId })
      .subscribe({
        next: (r) => {
          this.loaded.set(true);
          if (r.status === 'success') this.referrals.set(r.listObject ?? []);
        },
        error: () => this.loaded.set(true),
      });
  }
}

/** The ConsultDoctorDto's String-date format (dd-M-yyyy HH:mm:ss) — the
 * registration/prescription convention, NOT the encounter's Long. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
