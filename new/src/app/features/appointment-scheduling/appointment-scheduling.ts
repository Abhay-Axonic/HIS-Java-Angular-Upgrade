import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { AppointmentSchedulingService } from './appointment-scheduling.service';
import type { AppointmentRow, IdNameRow, PatientRow, SlotPayload, SlotRow } from './appointment-scheduling.models';

/** OPD Appointment Scheduling — the core scope per
 * docs/SPEC_APPOINTMENT_SCHEDULING_2026-08-16.md: department (speciality) +
 * doctor selection, date picker, available slots, book appointment (patient
 * search + slot + save), appointment list.
 * DATE CONVENTION: the appointment payload's dates are STRING
 * (dd-M-yyyy HH:mm:ss via formatLegacyNow) — the registration/prescription
 * convention, NOT the encounter's Long. */
@Component({
  selector: 'app-appointment-scheduling',
  imports: [FormsModule],
  templateUrl: './appointment-scheduling.html',
  styleUrl: './appointment-scheduling.scss',
})
export class AppointmentScheduling {
  private api = inject(AppointmentSchedulingService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  // the department cascade
  readonly specialityList = signal<IdNameRow[]>([]);
  readonly subSpecialityList = signal<IdNameRow[]>([]);
  readonly doctorList = signal<IdNameRow[]>([]);
  readonly specialityId = signal<number | null>(null);
  readonly subSpecialityId = signal<number | null>(null);
  readonly doctorId = signal<number | null>(null);
  readonly appointmentDate = signal('');

  // slots + list
  readonly slots = signal<SlotRow[]>([]);
  readonly appointments = signal<AppointmentRow[]>([]);

  // the booking patient
  readonly searchKeyword = signal('');
  readonly patientResults = signal<PatientRow[]>([]);
  readonly searchOpen = signal(false);
  readonly selectedPatient = signal<PatientRow | null>(null);
  readonly selectedSlot = signal<SlotRow | null>(null);

  readonly saving = signal(false);
  readonly loadingSlots = signal(false);

  constructor() {
    this.api.getSpecialities(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.specialityList.set(r.listObject ?? []);
    });
  }

  onSpecialityChange(id: number | null): void {
    this.specialityId.set(id);
    this.subSpecialityId.set(null);
    this.doctorId.set(null);
    this.subSpecialityList.set([]);
    this.doctorList.set([]);
    if (id != null) {
      this.api.getSubSpecialities(id).subscribe((r) => {
        if (r.status === 'success') this.subSpecialityList.set(r.listObject ?? []);
      });
      this.api.getDoctors(id).subscribe((r) => {
        if (r.status === 'success') this.doctorList.set(r.listObject ?? []);
      });
    }
  }

  loadSlots(): void {
    if (this.specialityId() == null || this.doctorId() == null || !this.appointmentDate().trim()) {
      this.notify.warning('Select speciality, doctor and a date first');
      return;
    }
    this.loadingSlots.set(true);
    this.api
      .getSlots({
        organizationId: this.orgId,
        unitId: this.unitId,
        specialityId: this.specialityId(),
        doctorId: this.doctorId(),
        appointmentDate: this.appointmentDate(),
      })
      .subscribe({
        next: (r) => {
          this.loadingSlots.set(false);
          if (r.status === 'success') this.slots.set(r.listObject ?? []);
        },
        error: () => {
          this.loadingSlots.set(false);
          this.notify.error('Slots load failed');
        },
      });
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

  pickSlot(s: SlotRow): void {
    this.selectedSlot.set(s);
  }

  book(): void {
    if (!this.selectedPatient() || !this.selectedSlot() || this.specialityId() == null || this.doctorId() == null) {
      this.notify.warning('Select a patient, a slot, speciality and doctor');
      return;
    }
    this.saving.set(true);
    const now = formatLegacyNow();
    const slot = this.selectedSlot() as SlotRow;
    const payload: SlotPayload = {
      organizationId: this.orgId,
      unitId: this.unitId,
      specialityId: this.specialityId(),
      doctorId: this.doctorId(),
      appointmentDate: this.appointmentDate(),
      fromTimeString: (slot.fromTimeString ?? slot.fromTime ?? '') as string,
      toTimeString: (slot.toTimeString ?? slot.toTime ?? '') as string,
      slotId: (slot.slotId ?? slot.id ?? null) as number | null,
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
    };
    this.api.saveAppointment(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') {
          this.notify.warning('Appointment booked');
          this.selectedSlot.set(null);
          this.loadList();
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

  loadList(): void {
    this.api
      .searchAppointments({
        organizationId: this.orgId,
        unitId: this.unitId,
        specialityId: this.specialityId(),
        doctorId: this.doctorId(),
        appointmentDate: this.appointmentDate(),
      })
      .subscribe((r) => {
        if (r.status === 'success') this.appointments.set(r.listObject ?? []);
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
