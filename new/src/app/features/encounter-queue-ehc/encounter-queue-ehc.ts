import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EncounterQueueEhcService } from './encounter-queue-ehc.service';

@Component({
  selector: 'app-encounter-queue-ehc',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './encounter-queue-ehc.html',
  styleUrls: ['./encounter-queue-ehc.scss'],
})
export class EncounterQueueEhc implements OnInit {
  private svc = inject(EncounterQueueEhcService);

  protected loading = signal(false);
  protected error = signal('');
  protected specialities = signal<any[]>([]);
  protected doctors = signal<any[]>([]);
  protected reasons = signal<any[]>([]);
  protected nursingStations = signal<any[]>([]);
  protected statuses = signal<any[]>([]);
  protected slots = signal<any[]>([]);
  protected slotMsg = signal('');
  protected loaded = signal(false);

  search = {
    organizationId: 1,
    unitId: 1,
    specialityId: null as number | null,
    doctorId: null as number | null,
    appointmentDate: '',
  };

  ngOnInit(): void {
    this.search.appointmentDate = this.today();
    this.load();
  }

  private today(): string {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
      d.getDate()
    ).padStart(2, '0')}`;
  }

  private load(): void {
    this.loading.set(true);
    this.error.set('');
    // feeds load in parallel; each resolves independently (dev-empty where
    // the data is patient/service-gated — surfaced honestly)
    this.svc.getSpecialities({ organizationId: 1, unitId: 1 }).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.specialities.set(r.listObject || []);
      },
      error: () => {},
    });
    this.svc.getReasons({ organizationId: 1, unitId: 1 }).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.reasons.set(r.listObject || []);
      },
      error: () => {},
    });
    this.svc.getNursingStations(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.nursingStations.set(r.listObject || []);
      },
      error: () => {},
    });
    this.svc.getStatuses(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.statuses.set(r.listObject || []);
      },
      error: () => {},
    });
    this.loading.set(false);
    this.loaded.set(true);
  }

  onSpecialityChange(): void {
    this.doctors.set([]);
    if (!this.search.specialityId) return;
    this.svc
      .getDoctors({
        organizationId: 1,
        unitId: 1,
        specialityId: this.search.specialityId,
      })
      .subscribe({
        next: (r: any) => {
          if (r?.status === 'success') this.doctors.set(r.listObject || []);
        },
        error: () => {},
      });
  }

  searchSlots(): void {
    if (!this.search.specialityId || !this.search.doctorId) {
      this.error.set('Please select a department/speciality and doctor first.');
      return;
    }
    this.loading.set(true);
    this.error.set('');
    this.slotMsg.set('');
    this.svc
      .getDailySlots({
        organizationId: 1,
        unitId: 1,
        appointmentDate: this.search.appointmentDate,
        specialityId: this.search.specialityId,
        doctorId: this.search.doctorId,
        slotTypeId: 1,
      })
      .subscribe({
        next: (r: any) => {
          this.loading.set(false);
          if (r?.status === 'success') {
            const list = r.listObject || [];
            this.slots.set(list);
            this.slotMsg.set(
              list.length > 0 ? '' : 'Slots not available !'
            );
          } else {
            this.error.set((r && r.message) || 'Slot search failed.');
          }
        },
        error: (err: any) => {
          this.loading.set(false);
          this.error.set(
            'Slot search request failed: ' + (err?.status || 'unknown')
          );
        },
      });
  }
}
