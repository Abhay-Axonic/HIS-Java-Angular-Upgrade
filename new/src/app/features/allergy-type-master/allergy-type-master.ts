import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AllergyTypeMasterService } from './allergy-type-master.service';
import {
  AllergyTypeForm,
  AllergyTypeRow,
  emptyAllergyTypeForm,
} from './allergy-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 *   views/master/GlobalMaster/allergy-type-master.html +
 *   resources/assets/js/controller/master/unit/allergyTypeMasterController.js
 *   (state `allergyTypeMaster`, module id 488, parent 476 Clinical Workstation
 *   -> 369 Global Master).
 *
 * A REAL legacy screen (a dedicated controller, full CRUD). The list, the
 * count and the by-id detail are live-verified (200 "success"); the write
 * paths are wired to the endpoints and their failure surfaces the backend's
 * own envelope honestly — their success with real data is UNVERIFIED by the
 * no-seed rule (a real save would write a row).
 *
 * Divergences (flagged, not silent):
 * 1. The legacy save is TWO steps (popUpFlag/showModal confirm); the rebuild
 *    saves directly (the serviceMaster model).
 * 2. The status toggle maps the legacy 'A'/'I' strings onto the house toggle.
 */
@Component({
  selector: 'app-allergy-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './allergy-type-master.html',
  styleUrl: './allergy-type-master.scss',
})
export class AllergyTypeMaster {
  private readonly service = inject(AllergyTypeMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId ?? 1;
  readonly unitId = this.session.unitId ?? 1;

  pageSizeOptions = PAGE_SIZE_OPTIONS;
  rows = signal<AllergyTypeRow[]>([]);
  form = signal<AllergyTypeForm>({ ...emptyAllergyTypeForm });
  searchTerm = signal('');
  pageSize = signal(10);
  totalCount = signal(0);
  page = signal(1);
  saving = signal(false);
  editing = signal(false);
  togglingId = signal<number | null>(null);

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pages = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      [r.allergyTypeName, r.allergyTypeDesc]
        .filter(Boolean)
        .some((v) => String(v).toLowerCase().includes(term)),
    );
  });

  constructor() {
    this.loadList();
  }

  loadList(): void {
    const offset = (this.page() - 1) * this.pageSize();
    this.service
      .searchAllergyTypes(this.organizationId, this.unitId, offset, this.pageSize())
      .subscribe({
        next: (res) => {
          if (res.status !== 'success') {
            this.notify.error(res.message ?? 'Allergy type list failed');
            this.rows.set([]);
            return;
          }
          this.rows.set(Array.isArray(res.listObject) ? res.listObject : []);
        },
        error: () => this.notify.error('Allergy type list failed'),
      });
    this.service.countAllergyTypes(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        this.totalCount.set(res.object ?? 0);
      },
      error: () => undefined,
    });
  }

  onPageSizeChange(): void {
    this.page.set(1);
    this.loadList();
  }

  setPage(p: number): void {
    if (p < 1 || p > this.totalPages()) return;
    this.page.set(p);
    this.loadList();
  }

  onEdit(row: AllergyTypeRow): void {
    this.service.getAllergyTypeById(row.allergyTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !Array.isArray(res.listObject) || !res.listObject[0]) {
          this.notify.error(res.message ?? 'Allergy type detail failed');
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          allergyTypeId: d.allergyTypeId,
          allergyTypeName: d.allergyTypeName ?? '',
          allergyTypeDesc: d.allergyTypeDesc ?? '',
          isGeneric: d.isGeneric ?? 'N',
        });
        this.editing.set(true);
      },
      error: () => this.notify.error('Allergy type detail failed'),
    });
  }

  resetForm(): void {
    this.form.set({ ...emptyAllergyTypeForm });
    this.editing.set(false);
  }

  onSave(): void {
    const f = this.form();
    if (!f.allergyTypeName?.trim()) {
      this.notify.error('Allergy Type Name is required');
      return;
    }
    const now = this.now();
    const payload = {
      allergyTypeName: f.allergyTypeName,
      allergyTypeDesc: f.allergyTypeDesc,
      isGeneric: f.isGeneric,
      allergyTypeId: f.allergyTypeId,
      status: f.allergyTypeId ? undefined : ('A' as const),
      createdDate: f.allergyTypeId ? undefined : now,
      updatedDate: now,
      createdBy: f.allergyTypeId ? undefined : this.session.userId,
      updatedBy: this.session.userId,
      organizationId: this.organizationId,
      unitId: this.unitId,
    };
    this.saving.set(true);
    const call = this.editing() ? this.service.updateAllergyType(payload) : this.service.saveAllergyType(payload);
    call.subscribe({
      next: (res) => {
        this.saving.set(false);
        if (res.status !== 'success') {
          this.notify.error(res.message ?? 'Save failed');
          return;
        }
        this.notify.success(res.message ?? 'Saved');
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
   * it with "dd-M-yyyy HH:mm:ss" (GlobalCommonDateUtils.getDate). */
  private now(): string {
    const d = new Date();
    const p = (n: number) => String(n).padStart(2, '0');
    return `${p(d.getDate())}-${p(d.getMonth() + 1)}-${d.getFullYear()} ${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`;
  }

  onToggle(row: AllergyTypeRow): void {
    const next: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.togglingId.set(row.allergyTypeId);
    this.service.updateAllergyTypeStatus({ allergyTypeId: row.allergyTypeId, status: next }).subscribe({
      next: (res) => {
        this.togglingId.set(null);
        if (res.status !== 'success') {
          this.notify.error(res.message ?? 'Status update failed');
          return;
        }
        this.notify.success(res.message ?? 'Status updated');
        this.loadList();
      },
      error: () => {
        this.togglingId.set(null);
        this.notify.error('Status update failed');
      },
    });
  }
}
