import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import {
  ProcedureTypeRow,
  ProcedureTypeSavePayload,
  ProcedureTypeService,
} from './procedure-type.service';

@Component({
  selector: 'app-procedure-type',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './procedure-type.html',
  styleUrl: './procedure-type.scss',
})
export class ProcedureType {
  private svc = inject(ProcedureTypeService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected rows = signal<ProcedureTypeRow[]>([]);
  protected loading = signal(false);
  protected saving = signal(false);
  protected searchTerm = '';

  protected form = {
    procedureTypeId: 0,
    procedureTypeCode: '',
    procedureTypeName: '',
    procedureTypeDesc: '',
    status: 'A',
  };

  protected editingId = signal(0);
  protected formValid = signal(true);

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
  }

  protected loadList(): void {
    this.loading.set(true);
    this.svc.list(this.orgId(), this.unitId(), 0, 100).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.rows.set(r.listObject ?? []);
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

  protected filteredRows = computed(() => {
    const term = this.searchTerm.trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter((r) =>
      (r.procedureTypeCode || '').toLowerCase().includes(term) ||
      (r.procedureTypeName || '').toLowerCase().includes(term) ||
      (r.procedureTypeDesc || '').toLowerCase().includes(term)
    );
  });

  protected edit(row: ProcedureTypeRow): void {
    this.editingId.set(row.procedureTypeId);
    this.form = {
      procedureTypeId: row.procedureTypeId,
      procedureTypeCode: row.procedureTypeCode || '',
      procedureTypeName: row.procedureTypeName || '',
      procedureTypeDesc: row.procedureTypeDesc || '',
      status: row.status || 'A',
    };
  }

  protected clearForm(): void {
    this.editingId.set(0);
    this.form = {
      procedureTypeId: 0,
      procedureTypeCode: '',
      procedureTypeName: '',
      procedureTypeDesc: '',
      status: 'A',
    };
    this.formValid.set(true);
  }

  protected toggleStatus(row: ProcedureTypeRow): void {
    const next = row.status === 'A' ? 'I' : 'A';
    this.svc.status(row.procedureTypeId, this.orgId(), this.unitId(), next).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.notify.success('Status changed successfully');
          this.loadList();
        } else {
          this.notify.error(r?.message || 'Status change failed');
          this.loadList();
        }
      },
      error: () => {
        this.notify.error('Status request failed');
        this.loadList();
      },
    });
  }

  protected save(): void {
    if (!this.form.procedureTypeCode.trim() || !this.form.procedureTypeName.trim()) {
      this.formValid.set(false);
      this.notify.warning('Save blocked: code and name are required');
      return;
    }
    this.formValid.set(true);
    const now = new Date().toISOString();
    const payload: ProcedureTypeSavePayload = {
      procedureTypeCode: this.form.procedureTypeCode.trim(),
      procedureTypeName: this.form.procedureTypeName.trim(),
      procedureTypeDesc: this.form.procedureTypeDesc.trim() || null,
      status: this.form.status || 'A',
      orgId: this.orgId(),
      unitId: this.unitId(),
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
    if (!this.form.procedureTypeCode.trim() || !this.form.procedureTypeName.trim()) {
      this.formValid.set(false);
      this.notify.warning('Update blocked: code and name are required');
      return;
    }
    this.formValid.set(true);
    const now = new Date().toISOString();
    const payload: ProcedureTypeSavePayload = {
      procedureTypeId: this.editingId(),
      procedureTypeCode: this.form.procedureTypeCode.trim(),
      procedureTypeName: this.form.procedureTypeName.trim(),
      procedureTypeDesc: this.form.procedureTypeDesc.trim() || null,
      status: this.form.status || 'A',
      orgId: this.orgId(),
      unitId: this.unitId(),
      updatedBy: this.userId(),
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
