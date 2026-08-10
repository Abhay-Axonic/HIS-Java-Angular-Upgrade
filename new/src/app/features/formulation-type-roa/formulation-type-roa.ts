import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { FormulationTypeRoaService } from './formulation-type-roa.service';
import {
  FormulationRoaRow,
  FormulationTypeRow,
  RoaOption,
} from './formulation-type-roa.models';

@Component({
  selector: 'app-formulation-type-roa',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './formulation-type-roa.html',
  styleUrl: './formulation-type-roa.scss',
})
export class FormulationTypeRoa {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(FormulationTypeRoaService);

  rows = signal<FormulationRoaRow[]>([]);
  formulationTypes = signal<FormulationTypeRow[]>([]);
  roaOptions = signal<RoaOption[]>([]);

  selectedFormulationTypeId = signal<number | null>(null);
  selectedRoaId = signal<number | null>(null);
  mappedRoaIds = signal<number[]>([]);
  saving = signal(false);

  private orgId = this.session.organizationId ?? 1;
  private unitId = this.session.unitId ?? 1;

  searchTerm = signal('');
  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter(
      (r) =>
        (r.formulationType || '').toLowerCase().includes(q) ||
        (r.routeOfAdministration || '').toLowerCase().includes(q)
    );
  });

  constructor() {
    this.loadList();
    this.loadFormulationTypes();
    this.loadRoaOptions();
  }

  loadList() {
    this.service.list(this.orgId).subscribe({
      next: (res) => {
        if (Array.isArray(res)) this.rows.set(res);
        else this.notify.error(`Formulation-ROA list load failed: unexpected envelope`);
      },
      error: (e) => this.notify.error(`Formulation-ROA list load failed: ${e.message ?? e}`),
    });
  }

  loadFormulationTypes() {
    this.service.formulationTypes(this.orgId).subscribe({
      next: (res) => {
        if (Array.isArray(res)) this.formulationTypes.set(res);
        else this.notify.error(`Formulation type list load failed: unexpected envelope`);
      },
      error: (e) => this.notify.error(`Formulation type list load failed: ${e.message ?? e}`),
    });
  }

  loadRoaOptions() {
    this.service.roaDropdown(this.orgId).subscribe({
      next: (res) => {
        if (Array.isArray(res)) this.roaOptions.set(res);
        else this.notify.error(`ROA dropdown load failed: unexpected envelope`);
      },
      error: (e) => this.notify.error(`ROA dropdown load failed: ${e.message ?? e}`),
    });
  }

  toggleRoa(id: number) {
    const cur = this.mappedRoaIds();
    this.mappedRoaIds.set(cur.includes(id) ? cur.filter((x) => x !== id) : [...cur, id]);
  }

  /**
   * Save — HONEST. The save endpoint EXISTS and is live-mapped
   * (POST /api/globals/formulationTypeAndROA/save; org/unit set server-side
   * from VerifiedIdentityService — the org-safe pattern). This form sends
   * the full legacy payload (formulationtypeid + routeOfAdministrationId[] +
   * status + unit/org/user). Per WRITE-VERIFICATION the outcome is reported
   * from the response; a success here is "not erroring" — not a persistence
   * claim beyond the observed response.
   */
  save() {
    const ftId = this.selectedFormulationTypeId();
    const roaIds = this.mappedRoaIds();
    if (!ftId || roaIds.length === 0) {
      this.notify.error('Select a formulation type and at least one ROA');
      return;
    }
    const payload = {
      formulationtypeid: ftId,
      routeOfAdministrationId: roaIds,
      status: true,
      unitId: this.unitId,
      organizationId: this.orgId,
      userId: this.session.userId ?? 0,
    };
    this.saving.set(true);
    this.service.save(payload).subscribe({
      next: (res) => {
        this.saving.set(false);
        if (res && res.message) {
          this.notify.success(res.message);
          this.selectedFormulationTypeId.set(null);
          this.mappedRoaIds.set([]);
          this.loadList();
        } else {
          this.notify.error(`Save not accepted: ${JSON.stringify(res)}`);
        }
      },
      error: (e) => {
        this.saving.set(false);
        this.notify.error(`Save failed: ${e.message ?? e}`);
      },
    });
  }
}
