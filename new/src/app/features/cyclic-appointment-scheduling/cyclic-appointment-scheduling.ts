import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { CyclicAppointmentSchedulingService } from './cyclic-appointment-scheduling.service';
import type { CyclicSlotPayload, IdNameRow } from './cyclic-appointment-scheduling.models';

/** OPD Cyclic Appointment Scheduling — recurring appointments (e.g. weekly
 * physiotherapy) per docs/SPEC_OPD_FOUR_VARIANTS_2026-08-16.md. Core: start
 * date + days-of-week + doctor + time; the save POSTs ONE SlotMasterDto per
 * recurrence to /api/billing/generateCyclicServiceAppointment (List body).
 * DATE CONVENTION: STRING dates via formatLegacyNow — the registration/
 * prescription convention, NOT the encounter's Long. */
@Component({
  selector: 'app-cyclic-appointment-scheduling',
  imports: [FormsModule],
  templateUrl: './cyclic-appointment-scheduling.html',
  styleUrl: './cyclic-appointment-scheduling.scss',
})
export class CyclicAppointmentSchedulingComponent {
  private api = inject(CyclicAppointmentSchedulingService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  readonly specialityList = signal<IdNameRow[]>([]);
  readonly doctorList = signal<IdNameRow[]>([]);
  readonly specialityId = signal<number | null>(null);
  readonly doctorId = signal<number | null>(null);
  readonly startDate = signal('');
  readonly time = signal('');

  // the recurrence days (Sunday=0 … Saturday=6, matching the legacy's dayE1..dayE7)
  readonly days = signal<boolean[]>([false, false, false, false, false, false, false]);
  readonly dayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

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

  toggleDay(i: number): void {
    this.days.update((arr) => arr.map((v, idx) => (idx === i ? !v : v)));
  }

  save(): void {
    const chosen = this.days().map((v, i) => (v ? i : -1)).filter((i) => i >= 0);
    if (this.doctorId() == null || !this.startDate().trim() || !this.time().trim() || !chosen.length) {
      this.notify.warning('Select a doctor, a start date, a time and at least one day');
      return;
    }
    this.saving.set(true);
    const now = formatLegacyNow();
    const base: CyclicSlotPayload = {
      organizationId: this.orgId,
      unitId: this.unitId,
      specialityId: this.specialityId(),
      doctorId: this.doctorId(),
      appointmentDate: this.startDate(),
      fromTimeString: this.time(),
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
    };
    const payloads: CyclicSlotPayload[] = chosen.map((dayIdx) => ({ ...base, dayId: dayIdx + 1 }));
    this.api.saveCyclic(payloads).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') this.notify.warning('Cyclic appointments saved');
        else this.notify.warning(r.message ?? 'Cyclic save failed');
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Cyclic save failed');
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
