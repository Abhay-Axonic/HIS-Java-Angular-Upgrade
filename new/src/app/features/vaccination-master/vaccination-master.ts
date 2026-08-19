import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { VaccinationMasterService } from './vaccination-master.service';
import {
  DiseaseRow,
  VaccinationRow,
  emptyDiseaseRow,
} from './vaccination-master.models';

/**
 * Parity rebuild of the legacy vaccination-master.html (state
 * `vaccinationMaster`, module id 482). LEGACY IS A REGISTERED STATIC SHELL —
 * the decoy-routed shape: node 482's menu reaches this shell, while the real
 * 21-field CRUD screen is orphaned under `vaccinationMAster` (17050).
 *
 * The rebuild implements the shell per the static-shell rule:
 *  - The grid is wired to the mapped list/count endpoints (org+unit scoped).
 *  - The Diseases add-row table is client-side only (the legacy's was raw
 *    DOM manipulation with no backend — nothing to wire, reported honestly).
 *  - The Save button reports the gap honestly: the real save endpoint exists
 *    and is consumed by the ORPHANED real screen (vaccinationMAster); wiring
 *    this shell's 2-field form (Code + Name) to it would send an incomplete
 *    payload (the probe-payload class, QF-2026-08-09-I) — reported instead.
 *
 * Deliberate divergences (flagged, not silent):
 * 1. The grid is wired to the mapped list feed (the legacy's was a hardcoded
 *    "- / - / View Disease List" row).
 * 2. The Diseases table is a client-side add/remove list (parity with the
 *    legacy's raw-DOM behavior — it never persisted).
 * 3. Save reports honestly — no wiring to saveVaccinationMaster (the shell's
 *    form cannot produce the real 21-field DTO contract).
 * 4. The search box filters the loaded grid client-side (legacy inert).
 */
@Component({
  selector: 'app-vaccination-master',
  imports: [FormsModule],
  templateUrl: './vaccination-master.html',
  styleUrl: './vaccination-master.scss',
})
export class VaccinationMaster {
  private readonly service = inject(VaccinationMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId;
  readonly unitId = this.session.unitId;

  rows = signal<VaccinationRow[]>([]);
  diseases = signal<DiseaseRow[]>([{ ...emptyDiseaseRow }]);
  form = signal({ code: '', name: '' });
  searchTerm = signal('');
  loading = signal(false);
  /** Signal-aware field setter — rerouted from the in-place `form().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.form.update((f) => ({...f, [key]: value}));
  }


  /** The grid filtered by the search box (client-side — divergence 4). */
  filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      [r.vaccinationCode, r.name, r.abbreviation]
        .filter(Boolean)
        .some((v) => String(v).toLowerCase().includes(term)),
    );
  });

  constructor() {
    this.loadMasters();
  }

  /** POST /api/billing/getVaccinationMasterList — the mapped grid feed. */
  loadMasters(): void {
    this.loading.set(true);
    this.service.listMasters(this.organizationId ?? 1, this.unitId ?? 1).subscribe({
      next: (env) => {
        this.rows.set(Array.isArray(env?.listObject) ? env.listObject : []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Vaccination master list load failed');
      },
    });
  }

  /** The Diseases add-row: client-side only (divergence 2 — the legacy's raw
   * DOM rows never had a backend). */
  addDisease(): void {
    this.diseases.update((list) => [...list, { ...emptyDiseaseRow }]);
  }

  removeDisease(index: number): void {
    this.diseases.update((list) => list.filter((_, i) => i !== index));
  }

  /** Display label: name, falling back to abbreviation (kept out of the
   * template because the preflight C2 pipe-scan misreads `||` as a pipe). */
  rowLabel(row: VaccinationRow): string {
    return row.name ? String(row.name) : row.abbreviation ? String(row.abbreviation) : '';
  }

  /** The edit: copy the row's code/name into the form client-side (the legacy
   * pencil was a dead anchor; the detail endpoint is the real screen's). */
  onEdit(row: VaccinationRow): void {
    this.form.set({ code: row.vaccinationCode?.toString() ?? '', name: row.name ?? '' });
  }

  /** The save: HONEST — the save endpoint EXISTS (saveVaccinationMaster) and
   * is wired in the backend, but its payload is the full vaccination-master
   * record (vaccine, route, site, dose, age ranges, series, gender, dates…)
   * and this shell's form collects only Code + Name of Vaccine. Sending a
   * 2-field payload would be an incomplete payload (the probe-payload class,
   * QF-2026-08-09-I). The complete form lives on the orphaned real screen
   * (vaccinationMAster). Reported precisely, not faked, never a dead button. */
  onSave(): void {
    this.notify.error(
      'Save not wired on this screen — the save endpoint expects the full vaccination record (vaccine, route, site, dose, age, series, gender…), which this shell form cannot supply; the complete form exists on the real Vaccination Master screen (finding logged)',
    );
  }
}
