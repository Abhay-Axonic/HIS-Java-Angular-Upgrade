import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { CorporateMasterService } from './corporate-master.service';

@Component({
  selector: 'app-corporate-master',
  standalone: true,
  imports: [],
  templateUrl: './corporate-master.html',
  styleUrl: './corporate-master.scss',
})
export class CorporateMaster {
  private svc = inject(CorporateMasterService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected companies = signal<{ id: number; name?: string }[]>([]);

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
    this.svc.companies(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.companies.set((r.listObject as { id: number; name?: string }[]) ?? []);
        } else {
          this.notify.error(r?.message || 'Company list failed');
        }
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Company list request failed');
      },
    });
  }
}
