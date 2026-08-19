import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AllergyMasterService } from './allergy-master.service';
import {
  AllergyForm,
  AllergyRow,
  AllergyTypeOption,
  emptyAllergyForm,
} from './allergy-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 *   views/master/GlobalMaster/allergy-master.html +
 *   resources/assets/js/controller/master/unit/allergyMasterController.js
 *   (state `allergyMaster`, module id 489, parent 476 Clinical Workstation
 *   -> 369 Global Master).
 *
 * A REAL legacy screen (a dedicated controller, full CRUD — the static-shell
 * rule does not apply). The endpoints all exist and were verified by calling
 * with the cached token, but the backend's list/detail/save/update/status
 * paths currently fail (the phantom cims_code/cims_type columns — the
 * swallowed SQLGrammarException). The rebuild wires everything and surfaces
 * the backend's own error envelope honestly; the count works.
 *
 * Divergences (flagged, not silent):
 * 1. The legacy save is TWO steps (the first click opens a confirm modal —
 *    popUpFlag/showModal — and only the modal's OK posts); the rebuild saves
 *    directly (the serviceMaster model).
 * 2. The cims columns render unconditionally (the legacy gated them on the
 *    cookieObjectAppConfig cimsOption == 'Y' — the rebuild does not load that
 *    config flag).
 * 3. The status toggle maps the legacy 'A'/'I' strings to the house toggle.
 * 4. The backend's own failures surface as the envelope's message ("Something
 *    went wrong!!") — the honest state while the phantom-column finding is
 *    open.
 */
@Component({
  selector: 'app-allergy-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './allergy-master.html',
  styleUrl: './allergy-master.scss',
})
export class AllergyMaster {
  private readonly service = inject(AllergyMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId ?? 1;
  readonly unitId = this.session.unitId ?? 1;

  pageSizeOptions = PAGE_SIZE_OPTIONS;
  allergyTypes = signal<AllergyTypeOption[]>([]);
  rows = signal<AllergyRow[]>([]);
  form = signal<AllergyForm>({ ...emptyAllergyForm });
  searchTerm = signal('');
  pageSize = signal(10);
  totalCount = signal(0);
  page = signal(1);
  saving = signal(false);
  editing = signal(false);
  /** Signal-aware field setter — rerouted from the in-place `form().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.form.update((f) => ({...f, [key]: value}));
  }

  togglingId = signal<number | null>(null);

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pages = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      [r.allergyName, r.allergyDesc, r.allergyTypeDesc, r.cimsCode]
        .filter(Boolean)
        .some((v) => String(v).toLowerCase().includes(term)),
    );
  });

  constructor() {
    this.loadAllergyTypes();
    this.loadList();
  }

  private envelopeError(status?: string): boolean {
    return status !== 'success';
  }

  loadAllergyTypes(): void {
    this.service.searchAllergyTypes().subscribe({
      next: (res) => {
        if (this.envelopeError(res.status)) {
          this.notify.error(res.message ?? 'Allergy type list failed');
          return;
        }
        this.allergyTypes.set(Array.isArray(res.listObject) ? res.listObject : []);
      },
      error: () => this.notify.error('Allergy type list failed'),
    });
  }

  loadList(): void {
    const offset = (this.page() - 1) * this.pageSize();
    this.service
      .searchAllergies(this.organizationId, this.unitId, offset, this.pageSize())
      .subscribe({
        next: (res) => {
          if (this.envelopeError(res.status)) {
            this.notify.error(res.message ?? 'Allergy list failed');
            this.rows.set([]);
            return;
          }
          this.rows.set(Array.isArray(res.listObject) ? res.listObject : []);
        },
        error: () => this.notify.error('Allergy list failed'),
      });
    this.service.countAllergies(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (this.envelopeError(res.status)) return;
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

  onEdit(row: AllergyRow): void {
    this.service.getAllergyById(row.allergyId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (this.envelopeError(res.status) || !Array.isArray(res.listObject) || !res.listObject[0]) {
          this.notify.error(res.message ?? 'Allergy detail failed');
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          allergyId: d.allergyId,
          allergyName: d.allergyName ?? '',
          allergyDesc: d.allergyDesc ?? '',
          allergyTypeId: d.allergyTypeId ?? null,
          cimsCode: d.cimsCode ?? '',
          cimsType: d.cimsType ?? '',
        });
        this.editing.set(true);
      },
      error: () => this.notify.error('Allergy detail failed'),
    });
  }

  resetForm(): void {
    this.form.set({ ...emptyAllergyForm });
    this.editing.set(false);
  }

  onSave(): void {
    const f = this.form();
    if (!f.allergyName?.trim()) {
      this.notify.error('Allergy Name is required');
      return;
    }
    const payload = {
      allergyName: f.allergyName,
      allergyDesc: f.allergyDesc,
      cimsCode: f.cimsCode,
      cimsType: f.cimsType,
      allergyTypeId: f.allergyTypeId,
      allergyId: f.allergyId,
      status: f.allergyId ? undefined : ('A' as const),
      organizationId: this.organizationId,
      unitId: this.unitId,
    };
    this.saving.set(true);
    const call = this.editing() ? this.service.updateAllergy(payload) : this.service.saveAllergy(payload);
    call.subscribe({
      next: (res) => {
        this.saving.set(false);
        if (this.envelopeError(res.status)) {
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

  onToggle(row: AllergyRow): void {
    const next: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.togglingId.set(row.allergyId);
    this.service.updateAllergyStatus({ allergyId: row.allergyId, status: next }).subscribe({
      next: (res) => {
        this.togglingId.set(null);
        if (this.envelopeError(res.status)) {
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
