import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import {
  DoctorRow,
  SpecialityRow,
  SurgicalCalendarRow,
  SurgicalCalendarService,
} from './surgical-calendar.service';

@Component({
  selector: 'app-surgical-calendar',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './surgical-calendar.html',
  styleUrl: './surgical-calendar.scss',
})
export class SurgicalCalendar {
  private svc = inject(SurgicalCalendarService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected rows = signal<SurgicalCalendarRow[]>([]);
  protected specialities = signal<SpecialityRow[]>([]);
  protected doctors = signal<DoctorRow[]>([]);
  protected loading = signal(false);
  protected saving = signal(false);
  protected searchTerm = '';

  protected form = {
    specialityId: 0,
    doctorId: 0,
    fromDate: '',
    toDate: '',
    fromTime: '',
    toTime: '',
  };

  protected formValid = signal(true);
  protected formError = signal('');

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }
  private userId(): number {
    return Number(this.session.current?.userId ?? 1);
  }

  constructor() {
    this.loadList();
    this.loadSpecialities();
  }

  protected loadList(): void {
    this.loading.set(true);
    this.svc.list(this.orgId(), this.unitId(), 0, 100).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.rows.set((r.listObject as SurgicalCalendarRow[]) ?? []);
        } else {
          this.notify.error(r?.message || 'List failed');
        }
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('List request failed');
      },
    });
  }

  protected loadSpecialities(): void {
    this.svc.specialities(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.specialities.set((r.listObject as SpecialityRow[]) ?? []);
        }
      },
      error: () => this.notify.error('Speciality feed failed'),
    });
  }

  protected onSpecialityChange(): void {
    this.form.doctorId = 0;
    this.doctors.set([]);
    if (!this.form.specialityId) {
      return;
    }
    this.svc.doctorsBySpeciality(this.form.specialityId, this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.doctors.set((r.listObject as DoctorRow[]) ?? []);
        } else {
          this.notify.error(r?.message || 'Doctor feed failed');
        }
      },
      error: () => this.notify.error('Doctor feed request failed'),
    });
  }

  protected filteredRows = computed(() => {
    const term = this.searchTerm.trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter((r) =>
      (r.doctorName || '').toLowerCase().includes(term) ||
      (r.speciality || '').toLowerCase().includes(term) ||
      (r.procedureDate || '').toLowerCase().includes(term) ||
      (r.day || '').toLowerCase().includes(term)
    );
  });

  protected save(): void {
    if (!this.form.specialityId || !this.form.doctorId || !this.form.fromDate || !this.form.toDate) {
      this.formValid.set(false);
      this.formError.set('Speciality, doctor, and date range are required.');
      this.notify.warning('Save blocked: speciality, doctor, and date range are required');
      return;
    }
    if (!this.form.fromTime || !this.form.toTime) {
      this.formValid.set(false);
      this.formError.set('From and to time are required.');
      this.notify.warning('Save blocked: from and to time are required (the slot generator needs them)');
      return;
    }
    // the time format guard (text inputs with an HH:MM pattern)
    const timeRe = /^([01]\d|2[0-3]):[0-5]\d$/;
    if (!timeRe.test(this.form.fromTime) || !timeRe.test(this.form.toTime)) {
      this.formValid.set(false);
      this.formError.set('From and to time must be in HH:MM format (e.g. 09:00).');
      this.notify.warning('Save blocked: times must be in HH:MM format');
      return;
    }
    this.formValid.set(true);
    this.formError.set('');
    // The legacy contract: the service REQUIRES surgicalCalendarDetailsDtoList
    // (NPE at ServiceImpl:68 if null — verified live: a top-level-only payload
    // 500s). The legacy builds it client-side as selectedDayList — one row per
    // day in the range, each with dayId/fromTime/toTime. The rebuild mirrors
    // that: one detail row per calendar day between fromDate and toDate.
    const detailRows = [];
    const start = new Date(this.form.fromDate);
    const end = new Date(this.form.toDate);
    const dayNames = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
      detailRows.push({
        dayId: ((d.getDay() + 6) % 7) + 1, // m_day_master: Mon=1..Sun=7; JS getDay(): Sun=0
        fromTime: this.form.fromTime || null,
        toTime: this.form.toTime || null,
        specialityId: this.form.specialityId,
        doctorId: this.form.doctorId,
        organisationId: this.orgId(),
        unitId: this.unitId(),
      });
    }
    const payload = {
      specialityId: this.form.specialityId,
      doctorId: this.form.doctorId,
      fromDate: this.form.fromDate,
      toDate: this.form.toDate,
      organisationId: this.orgId(),
      unitId: this.unitId(),
      createdBy: this.userId(),
      updatedBy: this.userId(),
      // NOTE: the DTO has NO top-level fromTime/toTime (Jackson 400s on
      // unknown properties — verified live) — times live in the detail rows.
      surgicalCalendarDetailsDtoList: detailRows,
    };
    this.saving.set(true);
    this.svc.save(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r?.status === 'success') {
          this.notify.success('Added Successfully.');
          this.loadList();
        } else {
          this.notify.error(r?.message || 'Save failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Save request failed');
      },
    });
  }
}
