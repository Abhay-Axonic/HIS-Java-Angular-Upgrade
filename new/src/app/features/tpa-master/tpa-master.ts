import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { TpaMasterService } from './tpa-master.service';

@Component({
  selector: 'app-tpa-master',
  standalone: true,
  imports: [],
  templateUrl: './tpa-master.html',
  styleUrl: './tpa-master.scss',
})
export class TpaMaster {
  private svc = inject(TpaMasterService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected companies = signal<{ id: number; name?: string }[]>([]);
  protected arCodes = signal<{ aRCodeId: number; aRCode?: string; aRCodeDesc?: string }[]>([]);
  protected countries = signal<{ countryId: number; countryName?: string }[]>([]);

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
    this.svc.arCodes(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.arCodes.set(
            (r.listObject as { aRCodeId: number; aRCode?: string; aRCodeDesc?: string }[]) ?? []
          );
        }
      },
      error: () => { /* auxiliary */ },
    });
    this.svc.countries(this.orgId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.countries.set(
            (r.listObject as { countryId: number; countryName?: string }[]) ?? []
          );
        }
      },
      error: () => { /* auxiliary */ },
    });
  }
}
