import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PatientTypeMasterService } from './patient-type-master.service';
import {
  PatientTypeForm,
  PatientTypeRow,
  emptyPatientTypeForm,
} from './patient-type-master.models';

/**
 * Parity rebuild of the legacy `patientType` screen
 * (billing/unit/patientTypeController.js + views/master/UnitMaster/billingMaster/
 * patient-type.html). Code + description master grid with save / update /
 * status toggle. Backend: /api/billing/* (GET list with org/unit path vars,
 * POST save/update/status). Module id 764. Live-verified 2026-08-12: list
 * returns SUCCESS with 3 real rows.
 */
@Component({
  selector: 'app-patient-type-master',
  standalone: true,
  imports: [FormsModule, StatusToggle],
  templateUrl: './patient-type-master.html',
  styleUrl: './patient-type-master.scss',
})
export class PatientTypeMaster {
  private readonly service = inject(PatientTypeMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly rows = signal<PatientTypeRow[]>([]);
  readonly loading = signal(false);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly form = signal<PatientTypeForm>(emptyPatientTypeForm());
  readonly editingId = signal<number | null>(null);

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }
  private unitId(): number {
    return this.session.unitId ?? 1;
  }
  private userId(): number {
    return this.session.userId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), this.unitId()).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load patient types.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  edit(row: PatientTypeRow): void {
    this.editingId.set(row.patientTypeId);
    this.form.set({ code: row.patientTypeCode, desc: row.patientType });
  }

  resetForm(): void {
    this.editingId.set(null);
    this.form.set(emptyPatientTypeForm());
  }

  save(): void {
    const f = this.form();
    if (!f.code.trim() || !f.desc.trim()) {
      this.notify.error('Code and description are required.');
      return;
    }
    this.saving.set(true);
    const base = { updatedBy: this.userId(), organizationId: this.orgId() };
    const call = this.editingId() !== null
      ? this.service.update({
          patientTypeId: this.editingId()!,
          patientTypeCode: f.code.trim(),
          patientType: f.desc.trim(),
          ...base,
        })
      : this.service.save({
          patientTypeCode: f.code.trim(),
          patientType: f.desc.trim(),
          status: 'A',
          createdBy: this.userId(),
          ...base,
        });
    call.subscribe({
      next: (res) => {
        this.saving.set(false);
        if (res.status === 'success') {
          this.notify.success(this.editingId() !== null ? 'Patient type updated.' : 'Patient type saved.');
          this.resetForm();
          this.load();
        } else {
          this.notify.error(res.message ?? 'Save failed.');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Save request failed.');
      },
    });
  }

  toggle(row: PatientTypeRow, status: 'A' | 'I'): void {
    // DQ-128: PatientTypeDaoImpl.updatePatientTypeStatus parses
    // updatedDate via GlobalCommonDateUtils.getDate (the NPE-prone String
    // overload) -- the payload must carry it legacy-formatted, exactly as
    // patientTypeController.js:473-478 sends it. The screen's other two
    // writes (save/update) are unaffected: their DAO dates are
    // server-computed via TimeZoneComponent.getDateByzone (verified).
    this.service.changeStatus({ patientTypeId: row.patientTypeId, status, updatedBy: this.userId(), updatedDate: formatLegacyNow() })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Status updated.');
            this.load();
          } else {
            this.notify.error(res.message ?? 'Status update failed.');
          }
        },
        error: () => this.notify.error('Status update request failed.'),
      });
  }

  rowCells(row: PatientTypeRow): string[] {
    return [String(row.patientTypeCode ?? '—'), String(row.patientType ?? '—')];
  }
}

/** Legacy: `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')` (patientTypeController.js:473-478). */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
