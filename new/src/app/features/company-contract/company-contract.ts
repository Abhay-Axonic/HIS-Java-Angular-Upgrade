import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { CompanyContractService } from './company-contract.service';

@Component({
  selector: 'app-company-contract',
  standalone: true,
  imports: [],
  templateUrl: './company-contract.html',
  styleUrl: './company-contract.scss',
})
export class CompanyContract {
  private svc = inject(CompanyContractService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected companies = signal<{ id: number; name?: string }[]>([]);
  protected visitTypes = signal<{ id: number; name?: string }[]>([]);
  protected packageTypes = signal<{ id: number; name?: string }[]>([]);

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
    this.svc.visitTypes(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.visitTypes.set((r.listObject as { id: number; name?: string }[]) ?? []);
        }
      },
      error: () => { /* auxiliary */ },
    });
    this.svc.packageTypes(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.packageTypes.set((r.listObject as { id: number; name?: string }[]) ?? []);
        }
      },
      error: () => { /* auxiliary */ },
    });
  }
}
