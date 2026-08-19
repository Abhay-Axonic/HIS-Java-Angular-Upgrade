import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { SurgeryMasterService } from './surgery-master.service';
import {
  emptySurgeryForm,
  SurgeryForm,
  SurgeryGradeOption,
  SurgeryRow,
  SpecialityOption,
} from './surgery-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 *   views/master/GlobalMaster/surgery-master.html +
 *   resources/assets/js/controller/opd/global/surgeryMasterController.js
 *   (state `surgeryMaster`, module id 487, parent 476 Clinical Workstation
 *   -> 369 Global Master).
 *
 * A REAL legacy screen (a dedicated controller, full CRUD). The list, the
 * by-id detail, the grade dropdown and the speciality dropdown are
 * live-verified (200 "success"); the write paths are wired to the endpoints
 * and their failure surfaces the backend's own envelope honestly — their
 * success with real data is UNVERIFIED by the no-seed rule (a real save
 * would write a row into public.m_surgery_master).
 *
 * Divergences (flagged, not silent):
 * 1. The legacy save is TWO steps (popUpFlag/showModal confirm); the rebuild
 *    saves directly (the serviceMaster model).
 * 2. The status toggle maps the legacy 'A'/'I' strings onto the house toggle.
 * 3. The legacy grid is client-side paginated over the FULL list (the
 *    controller returns no offset/count — PaginationService.searchData); the
 *    rebuild does the same client-side pagination + search.
 */
@Component({
  selector: 'app-surgery-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './surgery-master.html',
  styleUrl: './surgery-master.scss',
})
export class SurgeryMaster {
  private readonly service = inject(SurgeryMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId ?? 1;
  readonly unitId = this.session.unitId ?? 1;

  pageSizeOptions = PAGE_SIZE_OPTIONS;
  rows = signal<SurgeryRow[]>([]);
  specialities = signal<SpecialityOption[]>([]);
  surgeryGrades = signal<SurgeryGradeOption[]>([]);
  form = signal<SurgeryForm>({ ...emptySurgeryForm });
  searchTerm = signal('');
  pageSize = signal(10);
  page = signal(1);
  saving = signal(false);
  editing = signal(false);
  togglingId = signal<number | null>(null);
  dropdownError = signal('');
  /** Signal-aware field setter — rerouted from the in-place `form().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.form.update((f) => ({...f, [key]: value}));
  }


  filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    let list = this.rows();
    if (term) {
      list = list.filter((r) =>
        [r.surgeryName, r.surgeryCode, r.surgeryCodeCpt, r.specialityName, r.surgeryGrade]
          .filter(Boolean)
          .some((v) => String(v).toLowerCase().includes(term)),
      );
    }
    return list;
  });

  totalCount = computed(() => this.filteredRows().length);
  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pages = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedRows = computed(() => {
    const start = (this.page() - 1) * this.pageSize();
    return this.filteredRows().slice(start, start + this.pageSize());
  });

  constructor() {
    this.loadList();
    this.loadDropdowns();
  }

  loadList(): void {
    this.service.listSurgeryMasters(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error(res.message ?? 'Surgery master list failed');
          this.rows.set([]);
          return;
        }
        this.rows.set(Array.isArray(res.listObject) ? res.listObject : []);
      },
      error: () => this.notify.error('Surgery master list failed'),
    });
  }

  loadDropdowns(): void {
    this.service.listActiveSpecialities(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.dropdownError.set('Speciality list failed');
          this.specialities.set([]);
          return;
        }
        this.specialities.set(Array.isArray(res.listObject) ? res.listObject : []);
      },
      error: () => this.dropdownError.set('Speciality list failed'),
    });
    this.service.listSurgeryGrades(this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.dropdownError.set('Surgery grade list failed');
          this.surgeryGrades.set([]);
          return;
        }
        this.surgeryGrades.set(Array.isArray(res.listObject) ? res.listObject : []);
      },
      error: () => this.dropdownError.set('Surgery grade list failed'),
    });
  }

  onPageSizeChange(): void {
    this.page.set(1);
  }

  setPage(p: number): void {
    if (p < 1 || p > this.totalPages()) return;
    this.page.set(p);
  }

  onEdit(row: SurgeryRow): void {
    this.service.getSurgeryMasterById(row.surgery_id, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !Array.isArray(res.listObject) || !res.listObject[0]) {
          this.notify.error(res.message ?? 'Surgery master detail failed');
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          surgery_id: d.surgery_id ?? row.surgery_id,
          specialityId: d.specialityId ?? null,
          surgeryCode: d.surgeryCode ?? '',
          surgeryCodeCpt: d.surgeryCodeCpt ?? '',
          surgeryName: d.surgeryName ?? '',
          surgeryGradeId: d.surgeryGradeId ?? null,
        });
        this.editing.set(true);
      },
      error: () => this.notify.error('Surgery master detail failed'),
    });
  }

  resetForm(): void {
    this.form.set({ ...emptySurgeryForm });
    this.editing.set(false);
  }

  onSave(): void {
    const f = this.form();
    if (!f.specialityId) {
      this.notify.error('Specialty is required');
      return;
    }
    if (!f.surgeryCode?.trim()) {
      this.notify.error('Surgery Code is required');
      return;
    }
    if (!f.surgeryCodeCpt?.trim()) {
      this.notify.error('Surgery Code CPT4 is required');
      return;
    }
    if (!f.surgeryName?.trim()) {
      this.notify.error('Name is required');
      return;
    }
    if (!f.surgeryGradeId) {
      this.notify.error('Grade is required');
      return;
    }
    const now = this.now();
    this.saving.set(true);
    if (this.editing()) {
      this.service
        .updateSurgeryMaster({
          surgery_id: f.surgery_id ?? 0,
          specialityId: f.specialityId,
          surgeryCode: f.surgeryCode,
          surgeryCodeCpt: f.surgeryCodeCpt,
          surgeryName: f.surgeryName,
          surgeryGradeId: f.surgeryGradeId,
          updatedBy: this.session.userId,
          updatedDate: now,
        })
        .subscribe({
          next: (res) => {
            this.saving.set(false);
            if (res.status !== 'success') {
              this.notify.error(res.message ?? 'Update failed');
              return;
            }
            this.notify.success(res.message ?? 'Surgery master updated successfully');
            this.resetForm();
            this.loadList();
          },
          error: () => {
            this.saving.set(false);
            this.notify.error('Update failed');
          },
        });
      return;
    }
    this.service
      .saveSurgeryMaster({
        specialityId: f.specialityId,
        surgeryCode: f.surgeryCode,
        surgeryCodeCpt: f.surgeryCodeCpt,
        surgeryName: f.surgeryName,
        surgeryGradeId: f.surgeryGradeId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
        createdBy: this.session.userId,
        updatedBy: this.session.userId,
      })
      .subscribe({
        next: (res) => {
          this.saving.set(false);
          if (res.status !== 'success') {
            this.notify.error(res.message ?? 'Save failed');
            return;
          }
          this.notify.success(res.message ?? 'Surgery master added successfully');
          this.resetForm();
          this.loadList();
        },
        error: () => {
          this.saving.set(false);
          this.notify.error('Save failed');
        },
      });
  }

  /** The legacy date format: moment('DD-MM-YYYY HH:mm:ss') — the DAO parses
   * it with "dd-M-yyyy HH:mm:ss" (GlobalCommonDateUtils.getDate); a null
   * dateString NPEs (the named allergyTypeMaster mechanism) — the dates MUST
   * be sent. */
  private now(): string {
    const d = new Date();
    const p = (n: number) => String(n).padStart(2, '0');
    return `${p(d.getDate())}-${p(d.getMonth() + 1)}-${d.getFullYear()} ${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`;
  }

  onToggle(row: SurgeryRow): void {
    const next: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.togglingId.set(row.surgery_id);
    this.service
      .updateSurgeryMasterStatus({
        surgery_id: row.surgery_id,
        status: next,
        updatedBy: this.session.userId,
        updatedDate: this.now(),
      })
      .subscribe({
        next: (res) => {
          this.togglingId.set(null);
          if (res.status !== 'success') {
            this.notify.error(res.message ?? 'Status update failed');
            return;
          }
          this.notify.success(res.message ?? 'Surgery master updated successfully');
          this.loadList();
        },
        error: () => {
          this.togglingId.set(null);
          this.notify.error('Status update failed');
        },
      });
  }
}
