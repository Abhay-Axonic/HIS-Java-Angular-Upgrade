import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CombinationMedicationMasterService } from './combination-medication-master.service';
import {
  CombinationMedicationForm,
  CombinationMedicationRow,
  GenericOption,
  UomOption,
  emptyCombinationMedicationForm,
} from './combination-medication-master.models';

/**
 * Parity rebuild of
 *   views/PharmacyInventory/combination-medication-master.html
 *   (state `combinationMedicationMaster`, module id 472,
 *    parent 457 Pharmacy -> 369 Global Master; controller: the generic
 *    MasterController — which contains NO screen-specific logic).
 *
 * LEGACY IS A STATIC SHELL (hand-confirmed): the template has ZERO ng-*
 * bindings and ZERO API calls; every select is an empty <option></option>;
 * the grid holds one hardcoded empty row; the Save anchor targets a
 * '#confirmLogout' modal that does not exist in the template; the search box
 * is inert. The MasterController (common-controller.js) is generic
 * boilerplate with no combination logic; no combination constants exist in
 * the constants files. There was therefore NO legacy behaviour to go wrong —
 * and nothing to be byte-parity with.
 *
 * The rebuild reproduces the form + grid chrome and wires the endpoints that
 * demonstrably exist (all verified by CALLING with the cached token):
 *   generics list  GET  /api/globals/generics/1         -> 200 (real rows)
 *   combinations   GET  /api/globals/generics/IsCombination/1 -> 200 ([] dev)
 *   uom units      GET  /api/global/uom/units/1         -> 200 ([] dev)
 *   detail         GET  /api/globals/generics/getCombinationDetail/{id}
 *   status toggle  PATCH /api/globals/generics/{id} {status} -> 201 (probe)
 * The SAVE has no backend path for this screen (POST /api/globals/generics/
 * -> 405, no-slash -> 404 — the Generic controller's POST is the generic
 * master's) — a logged finding; the save button reports it honestly rather
 * than pretending (the legacy save anchor was itself a dead link).
 *
 * Deliberate divergences (flagged, not silent):
 * 1. Strength: the legacy's select had no options and no data source; the
 *    rebuild binds a number input to m_combination_generic.strength_unit
 *    (a double) — the only data-backed representation.
 * 2. The search box: the legacy's was inert; the rebuild filters the loaded
 *    grid client-side (no combination search endpoint exists).
 * 3. The grid's detail cells (Generic / Strength / CUM / Display name):
 *    the IsCombination list rows carry only id/code/name/status; the detail
 *    fetch (on edit) fills the rest. Empty in dev either way.
 */
@Component({
  selector: 'app-combination-medication-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './combination-medication-master.html',
  styleUrl: './combination-medication-master.scss',
})
export class CombinationMedicationMaster {
  private readonly service = inject(CombinationMedicationMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId;
  readonly unitId = this.session.unitId;

  generics = signal<GenericOption[]>([]);
  combinationMedicines = signal<CombinationMedicationRow[]>([]);
  uomUnits = signal<UomOption[]>([]);
  rows = signal<CombinationMedicationRow[]>([]);
  form = signal<CombinationMedicationForm>({ ...emptyCombinationMedicationForm });
  searchTerm = signal('');
  loading = signal(false);
  togglingId = signal<number | null>(null);
  selectedRowId = signal<number | null>(null);

  /** The grid filtered by the search box (client-side — divergence 2). */
  filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      [r.name, r.code, r.genericName, r.displayGenericName]
        .filter(Boolean)
        .some((v) => String(v).toLowerCase().includes(term)),
    );
  });

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    this.loading.set(true);
    const org = this.organizationId ?? 1;
    this.service.searchGenerics(org).subscribe({
      next: (list) => this.generics.set(Array.isArray(list) ? list : []),
      error: () => this.notify.error('Generic list load failed'),
    });
    this.service.searchCombinationMedicines(org).subscribe({
      next: (list) => {
        const rows = Array.isArray(list) ? list : [];
        this.combinationMedicines.set(rows);
        this.rows.set(rows);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Combination list load failed');
      },
    });
    this.service.searchUomUnits(org).subscribe({
      next: (list) => this.uomUnits.set(Array.isArray(list) ? list : []),
      error: () => this.notify.error('Units of measurement load failed'),
    });
  }

  /** The edit action: fetch the combination detail into the form. */
  onEdit(row: CombinationMedicationRow): void {
    this.selectedRowId.set(row.id);
    this.service.getCombinationDetail(row.id).subscribe({
      next: (detail) => {
        const d = Array.isArray(detail) ? detail[0] : detail;
        this.form.set({
          combinationMedicineId: d?.id ?? row.id,
          genericId: d?.genericId ?? null,
          strength: d?.strength ?? null,
          strengthUnitId: d?.strengthUnitId ?? null,
          displayGenericName: d?.displayGenericName ?? '',
        });
      },
      error: () => this.notify.error('Combination detail load failed'),
    });
  }

  /** The status toggle: PATCH /api/globals/generics/{id} (verified 201). */
  onToggle(row: CombinationMedicationRow): void {
    const status = !row.status;
    this.togglingId.set(row.id);
    this.service.toggleStatus(row.id, status).subscribe({
      next: (res) => {
        this.togglingId.set(null);
        this.notify.success(res?.message ?? 'Status updated');
        this.loadAll();
      },
      error: () => {
        this.togglingId.set(null);
        this.notify.error('Status update failed');
      },
    });
  }

  /** The save: HONEST — no backend path exists for this screen's save
   * (finding logged; the legacy save anchor was a dead link too). */
  onSave(): void {
    this.notify.error('Save not wired — no combination-medication save endpoint exists (finding logged)');
  }
}
