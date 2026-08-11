import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { DepositMatrixConfigurationService, DepositMatrixRow } from './deposit-matrix-configuration.service';

@Component({
  selector: 'app-deposit-matrix-configuration',
  standalone: true,
  imports: [],
  templateUrl: './deposit-matrix-configuration.html',
  styleUrl: './deposit-matrix-configuration.scss',
})
export class DepositMatrixConfiguration {
  private svc = inject(DepositMatrixConfigurationService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected rows = signal<DepositMatrixRow[]>([]);
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
          this.rows.set((r.listObject as DepositMatrixRow[]) ?? []);
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
