import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { PatientListService } from './patient-list.service';

@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [],
  templateUrl: './patient-list.html',
  styleUrl: './patient-list.scss',
})
export class PatientList {
  private svc = inject(PatientListService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected rows = signal<unknown[]>([]);

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
