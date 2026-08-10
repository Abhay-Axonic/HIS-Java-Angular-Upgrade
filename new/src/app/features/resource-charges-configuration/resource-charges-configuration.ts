import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import {
  ResourceChargeRow,
  ResourceChargesConfigurationService,
  RoleRow,
  SpecialityRow,
} from './resource-charges-configuration.service';

@Component({
  selector: 'app-resource-charges-configuration',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './resource-charges-configuration.html',
  styleUrl: './resource-charges-configuration.scss',
})
export class ResourceChargesConfiguration {
  private svc = inject(ResourceChargesConfigurationService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected rows = signal<ResourceChargeRow[]>([]);
  protected specialities = signal<SpecialityRow[]>([]);
  protected roles = signal<RoleRow[]>([]);
  protected services = signal<ResourceChargeRow[]>([]);
  protected loading = signal(false);
  protected saving = signal(false);
  protected searchTerm = signal('');

  protected form = {
    resourceChargeConfigurationId: 0,
    specialityId: 0,
    roleId: 0,
    serviceId: 0,
    serviceName: '',
    amount: 0,
    status: 'A',
  };

  protected editingId = signal(0);
  protected formValid = signal(true);
  protected formError = signal('');

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
    this.loadList();
    this.loadSpecialities();
    this.loadRoles();
    this.loadServices();
  }

  protected loadList(): void {
    this.loading.set(true);
    this.svc.list(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.rows.set((r.listObject as ResourceChargeRow[]) ?? []);
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

  protected loadSpecialities(): void {
    this.svc.specialities(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          const all = (r.listObject as SpecialityRow[]) ?? [];
          this.specialities.set(all.filter((s) => s.isClinicalSpeciality === 'Y' || !s.isClinicalSpeciality));
        }
      },
      error: () => this.notify.error('Speciality feed failed'),
    });
  }

  protected loadRoles(): void {
    this.svc.roles(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.roles.set((r.listObject as RoleRow[]) ?? []);
        }
      },
      error: () => this.notify.error('Role feed failed'),
    });
  }

  protected loadServices(): void {
    this.svc.unitServices(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.services.set((r.listObject as ResourceChargeRow[]) ?? []);
        }
      },
      error: () => this.notify.error('Service feed failed'),
    });
  }

  protected onServiceSelect(): void {
    const sel = this.services().find((s) => s.serviceId === this.form.serviceId);
    if (sel) {
      this.form.serviceName = sel.serviceStandardName || '';
    }
  }

  protected filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter((r) =>
      (r.specialityName || '').toLowerCase().includes(term) ||
      (r.serviceStandardName || '').toLowerCase().includes(term) ||
      (r.procedureRoleName || '').toLowerCase().includes(term)
    );
  });

  protected edit(row: ResourceChargeRow): void {
    this.editingId.set(row.resourceChargeConfigurationId);
    this.form = {
      resourceChargeConfigurationId: row.resourceChargeConfigurationId,
      specialityId: row.specialityId,
      roleId: row.roleId,
      serviceId: row.serviceId,
      serviceName: row.serviceStandardName || '',
      amount: row.amount,
      status: row.status || 'A',
    };
  }

  protected clearForm(): void {
    this.editingId.set(0);
    this.form = {
      resourceChargeConfigurationId: 0,
      specialityId: 0,
      roleId: 0,
      serviceId: 0,
      serviceName: '',
      amount: 0,
      status: 'A',
    };
    this.formValid.set(true);
    this.formError.set('');
  }

  protected toggleStatus(row: ResourceChargeRow): void {
    const next = row.status === 'A' ? 'I' : 'A';
    this.svc.status(row.resourceChargeConfigurationId, next).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.notify.success('Status changed successfully');
        } else {
          this.notify.error(r?.message || 'Status change failed');
        }
        this.loadList();
      },
      error: () => {
        this.notify.error('Status request failed');
        this.loadList();
      },
    });
  }

  protected save(): void {
    if (!this.form.specialityId || !this.form.roleId || !this.form.serviceId || !this.form.serviceName || !this.form.amount) {
      this.formValid.set(false);
      this.formError.set('Speciality, role, service, and amount are required.');
      this.notify.warning('Save blocked: speciality, role, service, and amount are required');
      return;
    }
    this.formValid.set(true);
    this.formError.set('');
    // the legacy contract: DD-MM-YYYY HH:mm:ss (moment format — the Dozer
    // DateConverter parses with this exact format; ISO-8601 500s with
    // 'Unable to parse source object using specified date format' —
    // verified live)
    const d = new Date();
    const pad = (n: number) => String(n).padStart(2, '0');
    const now = `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    const payload = {
      orgId: this.orgId(),
      unitId: this.unitId(),
      specialityId: this.form.specialityId,
      roleId: this.form.roleId,
      serviceId: this.form.serviceId,
      amount: this.form.amount,
      status: this.form.status || 'A',
      createdBy: this.userId(),
      updatedBy: this.userId(),
      createdDate: now,
      updatedDate: now,
    };
    this.saving.set(true);
    this.svc.save(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r?.status === 'success') {
          this.notify.success('Added Successfully.');
          this.clearForm();
          this.loadList();
        } else {
          this.notify.error(r?.message || 'Save failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Save request failed');
      },
    });
  }

  protected update(): void {
    if (!this.form.specialityId || !this.form.roleId || !this.form.serviceId || !this.form.amount) {
      this.formValid.set(false);
      this.formError.set('Speciality, role, service, and amount are required.');
      this.notify.warning('Update blocked: speciality, role, service, and amount are required');
      return;
    }
    this.formValid.set(true);
    this.formError.set('');
    // the legacy date contract: DD-MM-YYYY HH:mm:ss (Dozer DateConverter)
    const d = new Date();
    const pad = (n: number) => String(n).padStart(2, '0');
    const now = `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    const payload = {
      orgId: this.orgId(),
      unitId: this.unitId(),
      specialityId: this.form.specialityId,
      roleId: this.form.roleId,
      serviceId: this.form.serviceId,
      amount: this.form.amount,
      status: this.form.status || 'A',
      resourceChargeConfigurationId: this.editingId(),
      createdBy: this.userId(),
      updatedBy: this.userId(),
      createdDate: now,
      updatedDate: now,
    };
    this.saving.set(true);
    this.svc.update(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r?.status === 'success') {
          this.notify.success('Updated Successfully.');
          this.clearForm();
          this.loadList();
        } else {
          this.notify.error(r?.message || 'Update failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Update request failed');
      },
    });
  }
}
