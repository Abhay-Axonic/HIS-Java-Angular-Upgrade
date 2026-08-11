import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { UnitServiceMappingService } from './unit-service-mapping.service';

@Component({
  selector: 'app-unit-service-mapping',
  standalone: true,
  imports: [],
  templateUrl: './unit-service-mapping.html',
  styleUrl: './unit-service-mapping.scss',
})
export class UnitServiceMapping {
  private svc = inject(UnitServiceMappingService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected subSpecialities = signal<{ subSpecialityMasterId: number; subSpecialityMasterName: string }[]>([]);
  protected mappings = signal<unknown[]>([]);

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
    // the subspeciality feed (verified live with real rows) — speciality 1
    this.svc.subspecialities(this.orgId(), this.unitId(), 1).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.subSpecialities.set(
            (r.listObject as { subSpecialityMasterId: number; subSpecialityMasterName: string }[]) ?? []
          );
        }
      },
      error: () => { /* auxiliary */ },
    });
    // the mapping list for speciality 1 / subspeciality 1 (dev-empty)
    this.svc.mappings(this.orgId(), this.unitId(), 1, 1).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.mappings.set((r.listObject as unknown[]) ?? []);
        } else {
          this.notify.error(r?.message || 'Mapping list failed');
        }
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Mapping list request failed');
      },
    });
  }
}
