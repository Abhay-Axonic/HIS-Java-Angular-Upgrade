import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { DoctorConsultationService } from './doctor-consultation.service';

@Component({
  selector: 'app-doctor-consultation',
  standalone: true,
  imports: [],
  templateUrl: './doctor-consultation.html',
  styleUrl: './doctor-consultation.scss',
})
export class DoctorConsultation {
  private svc = inject(DoctorConsultationService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected rows = signal<unknown[]>([]);
  protected count = signal<number | null>(null);

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }

  constructor() {
    this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.svc.count(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.count.set(typeof r.object === 'number' ? r.object : null);
        }
      },
      error: () => { /* auxiliary */ },
    });
    this.svc.list(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.rows.set((r.listObject as unknown[]) ?? []);
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
}
