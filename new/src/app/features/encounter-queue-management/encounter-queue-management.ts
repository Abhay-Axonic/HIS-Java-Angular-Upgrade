import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { EncounterQueueManagementService } from './encounter-queue-management.service';

@Component({
  selector: 'app-encounter-queue-management',
  standalone: true,
  imports: [],
  templateUrl: './encounter-queue-management.html',
  styleUrl: './encounter-queue-management.scss',
})
export class EncounterQueueManagement {
  private svc = inject(EncounterQueueManagementService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected stations = signal<{ userNursingStationMpprId: number; nursingStationId?: number }[]>([]);
  protected departments = signal<number[]>([]);
  protected doctors = signal<{ doctorId: number; firstName?: string }[]>([]);

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
    this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.svc.nursingStations(this.userId(), this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.stations.set(
            (r.listObject as { userNursingStationMpprId: number; nursingStationId?: number }[]) ?? []
          );
        } else {
          this.notify.error(r?.message || 'Nursing stations failed');
        }
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Nursing stations request failed');
      },
    });
    this.svc.departments(1, this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.departments.set((r.object as number[]) ?? []);
        }
      },
      error: () => { /* auxiliary */ },
    });
    this.svc.doctors(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.doctors.set((r.listObject as { doctorId: number; firstName?: string }[]) ?? []);
        }
      },
      error: () => { /* auxiliary */ },
    });
  }
}
