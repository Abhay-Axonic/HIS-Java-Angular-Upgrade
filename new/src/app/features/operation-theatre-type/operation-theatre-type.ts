import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { OperationTheatreTypeService } from './operation-theatre-type.service';
import { OperationTheatreTypeRow } from './operation-theatre-type.models';

@Component({
  selector: 'app-operation-theatre-type',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './operation-theatre-type.html',
  styleUrl: './operation-theatre-type.scss',
})
export class OperationTheatreType {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(OperationTheatreTypeService);

  rows = signal<OperationTheatreTypeRow[]>([]);
  loading = signal(false);
  saving = signal(false);

  private orgId = this.session.organizationId ?? 1;
  private unitId = this.session.unitId ?? 1;

  searchTerm = signal('');
  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter(
      (r) =>
        (r.theatreTypeName || '').toLowerCase().includes(q) ||
        (r.theatreTypeCode || '').toLowerCase().includes(q) ||
        (r.theatreTypeDesc || '').toLowerCase().includes(q)
    );
  });

  /** The form model — the full DTO contract (both dates) per the
   * false-success finding: an incomplete DTO (updatedDate omitted) makes the
   * DAO NPE and the service return a false SUCCESS while writing nothing
   * (the BR-187 class, confirmed on this save path too). The form always
   * sends createdDate AND updatedDate. */
  form = signal<OperationTheatreTypeRow>({
    theatreTypeCode: '',
    theatreTypeName: '',
    theatreTypeDesc: '',
    status: 'A',
    orgId: this.orgId,
    unitId: this.unitId,
  });
  editingId = signal<number | null>(null);

  constructor() {
    this.loadList();
  }

  loadList() {
    this.loading.set(true);
    this.service
      .list({ orgId: this.orgId, unitId: this.unitId, offset: 0, noOfRecordsPerPage: 50 })
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (Array.isArray(res.listObject)) this.rows.set(res.listObject);
          else {
            const detail = (res as any)?.message ?? 'unexpected envelope';
            this.notify.error(
              `Operation theatre type list load failed: the server reported "${detail}".`
            );
          }
        },
        error: (e) => {
          this.loading.set(false);
          this.notify.error(
            `Operation theatre type list load failed: ${e.message ?? e}. Check the server log for the correlation reference.`
          );
        },
      });
  }

  private nowStamp(): string {
    const d = new Date();
    const p = (n: number) => String(n).padStart(2, '0');
    return `${p(d.getDate())}-${p(d.getMonth() + 1)}-${d.getFullYear()} ${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`;
  }

  /** Client-side guard: the legacy form has required inputs for all three
   * fields; the backend's dup-check + the false-success class make a partial
   * submit unsafe. Reject before sending. */
  private formValid(): boolean {
    const f = this.form();
    return !!(f.theatreTypeCode && f.theatreTypeCode.trim() && f.theatreTypeName && f.theatreTypeName.trim());
  }

  save() {
    if (this.saving()) return;
    if (!this.formValid()) {
      this.notify.error(
        'Save blocked: theatre type code and name are required. The backend reports a false success for incomplete payloads — nothing is sent until the form is complete.'
      );
      return;
    }
    this.saving.set(true);
    const stamp = this.nowStamp();
    const dto: OperationTheatreTypeRow = {
      ...this.form(),
      createdBy: 1,
      createdDate: stamp,
      updatedBy: 1,
      updatedDate: stamp,
    };
    this.service.save(dto).subscribe({
      next: (res) => {
        this.saving.set(false);
        if (res.status === 'success') {
          this.notify.success('Operation theatre type saved.');
          this.form.set({ theatreTypeCode: '', theatreTypeName: '', theatreTypeDesc: '', status: 'A', orgId: this.orgId, unitId: this.unitId });
          this.loadList();
        } else {
          this.notify.error(
            `Save failed: the server reported "${res.message ?? 'unknown error'}". Check the server log for the correlation reference.`
          );
        }
      },
      error: (e) => {
        this.saving.set(false);
        this.notify.error(
          `Save failed: ${e.message ?? e}. Check the server log for the correlation reference.`
        );
      },
    });
  }

  edit(row: OperationTheatreTypeRow) {
    this.editingId.set(row.operationTheatreTypeId ?? null);
    this.form.set({
      theatreTypeCode: row.theatreTypeCode ?? '',
      theatreTypeName: row.theatreTypeName ?? '',
      theatreTypeDesc: row.theatreTypeDesc ?? '',
      status: row.status ?? 'A',
      orgId: row.orgId ?? this.orgId,
      unitId: row.unitId ?? this.unitId,
    });
  }

  update() {
    if (this.saving()) return;
    const id = this.editingId();
    if (id === null) {
      this.notify.error('Update blocked: no row selected for update.');
      return;
    }
    if (!this.formValid()) {
      this.notify.error('Update blocked: theatre type code and name are required.');
      return;
    }
    this.saving.set(true);
    const stamp = this.nowStamp();
    const dto: OperationTheatreTypeRow = {
      ...this.form(),
      operationTheatreTypeId: id,
      updatedBy: 1,
      updatedDate: stamp,
    };
    this.service.update(dto).subscribe({
      next: (res) => {
        this.saving.set(false);
        if (res.status === 'success') {
          this.notify.success('Operation theatre type updated.');
          this.editingId.set(null);
          this.form.set({ theatreTypeCode: '', theatreTypeName: '', theatreTypeDesc: '', status: 'A', orgId: this.orgId, unitId: this.unitId });
          this.loadList();
        } else {
          this.notify.error(
            `Update failed: the server reported "${res.message ?? 'unknown error'}". Check the server log for the correlation reference.`
          );
        }
      },
      error: (e) => {
        this.saving.set(false);
        this.notify.error(
          `Update failed: ${e.message ?? e}. Check the server log for the correlation reference.`
        );
      },
    });
  }

  toggleStatus(row: OperationTheatreTypeRow) {
    const next = row.status === 'A' ? 'I' : 'A';
    this.service
      .status({ ...row, status: next, updatedBy: 1 })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Operation theatre type status updated.');
            this.loadList();
          } else {
            this.notify.error(
              `Status update failed: the server reported "${res.message ?? 'unknown error'}".`
            );
          }
        },
        error: (e) => {
          this.notify.error(`Status update failed: ${e.message ?? e}.`);
        },
      });
  }
}
