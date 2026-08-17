import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { WalkInAppointmentService } from './walk-in-appointment.service';
import type { IdNameRow, PatientRow, SlotPayload } from './walk-in-appointment.models';

/** OPD Walk-in Appointment — the self-registration variant of appointment
 * scheduling: a simplified booking without a pre-scheduled slot (per
 * docs/SPEC_OPD_FOUR_VARIANTS_2026-08-16.md). The legacy screen is real
 * (546-line template, selfRegistrationController). DATE CONVENTION: STRING
 * dates via formatLegacyNow — the registration/prescription convention, NOT
 * the encounter's Long. */
@Component({
  selector: 'app-walk-in-appointment',
  imports: [FormsModule],
  templateUrl: './walk-in-appointment.html',
  styleUrl: './walk-in-appointment.scss',
})
export class WalkInAppointmentComponent {
  private api = inject(WalkInAppointmentService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  readonly specialityList = signal<IdNameRow[]>([]);
  readonly doctorList = signal<IdNameRow[]>([]);
  readonly specialityId = signal<number | null>(null);
  readonly doctorId = signal<number | null>(null);
  readonly appointmentDate = signal('');

  readonly searchKeyword = signal('');
  readonly patientResults = signal<PatientRow[]>([]);
  readonly searchOpen = signal(false);
  readonly selectedPatient = signal<PatientRow | null>(null);

  readonly saving = signal(false);

  constructor() {
    this.api.getSpecialities(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.specialityList.set(r.listObject ?? []);
    });
  }

  onSpecialityChange(id: number | null): void {
    this.specialityId.set(id);
    this.doctorId.set(null);
    this.doctorList.set([]);
    if (id != null) {
      this.api.getDoctors(id).subscribe((r) => {
        if (r.status === 'success') this.doctorList.set(r.listObject ?? []);
      });
    }
  }

  searchPatient(): void {
    const kw = this.searchKeyword().trim();
    if (!kw) return;
    this.api.searchPatient({ search: kw, organizationId: this.orgId, unitId: this.unitId }).subscribe((r) => {
      if (r.status === 'success') this.patientResults.set(r.listObject ?? []);
      this.searchOpen.set(true);
    });
  }

  pickPatient(p: PatientRow): void {
    this.selectedPatient.set(p);
    this.searchOpen.set(false);
  }

  book(): void {
    if (this.doctorId() == null || !this.appointmentDate().trim() || !this.selectedPatient()) {
      this.notify.warning('Select a doctor, a date and a patient');
      return;
    }
    this.saving.set(true);
    const now = formatLegacyNow();
    const payload: SlotPayload = {
      organizationId: this.orgId,
      unitId: this.unitId,
      specialityId: this.specialityId(),
      doctorId: this.doctorId(),
      appointmentDate: this.appointmentDate(),
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
    };
    this.api.book(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') {
          this.notify.warning('Walk-in appointment booked');
          this.selectedPatient.set(null);
        } else {
          this.notify.warning(r.message ?? 'Booking failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Booking failed');
      },
    });
  }
}

/** The SlotMasterDto's String-date format (dd-M-yyyy HH:mm:ss) — the
 * registration/prescription convention, NOT the encounter's Long. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
