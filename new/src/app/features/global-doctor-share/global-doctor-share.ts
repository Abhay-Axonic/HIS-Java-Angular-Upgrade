import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { GlobalDoctorShareService } from './global-doctor-share.service';

@Component({
  selector: 'app-global-doctor-share',
  standalone: true,
  imports: [],
  templateUrl: './global-doctor-share.html',
  styleUrl: './global-doctor-share.scss',
})
export class GlobalDoctorShare {
  private svc = inject(GlobalDoctorShareService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected rows = signal<unknown[]>([]);
  protected grades = signal<unknown[]>([]);

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
    this.svc.grades(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.grades.set((r.listObject as unknown[]) ?? []);
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
