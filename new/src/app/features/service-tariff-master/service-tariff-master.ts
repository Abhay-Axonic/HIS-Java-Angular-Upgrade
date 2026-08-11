import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { ServiceTariffMasterService } from './service-tariff-master.service';

@Component({
  selector: 'app-service-tariff-master',
  standalone: true,
  imports: [],
  templateUrl: './service-tariff-master.html',
  styleUrl: './service-tariff-master.scss',
})
export class ServiceTariffMaster {
  private svc = inject(ServiceTariffMasterService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected backendError = signal('');
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
    this.backendError.set('');
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
