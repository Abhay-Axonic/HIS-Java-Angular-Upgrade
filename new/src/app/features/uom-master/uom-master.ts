import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { UomMasterService } from './uom-master.service';
import {
  UomMasterForm,
  UomUnitRow,
  emptyUomMasterForm,
} from './uom-master.models';

/**
 * Parity rebuild of
 *   views/master/GlobalMaster/OTMS/uom-master.html
 *   (state `uomMaster`, module id 466, parent 457 Pharmacy -> 369 Global
 *   Master; controller: the generic MasterController — which contains NO
 *   screen-specific logic).
 *
 * LEGACY IS A STATIC SHELL (hand-confirmed): the 119-line template has two
 * decorative ng-models (`common.code`, `common.uomCode` — the MasterController
 * never defines a `common` object, so they bind to nothing), an inert Save
 * anchor, an inert search box, and one hardcoded grid row. Zero API calls,
 * zero constants. The static-shell precedent (owner-approved, screen 4)
 * applies: the rebuild IMPLEMENTS the shell with the endpoints that
 * demonstrably exist, and every missing endpoint is reported honestly in the
 * UI.
 *
 * Verified by CALLING with the cached token:
 *   GET /api/global/uom/units/{orgId} -> 200 ([] in dev — the only UOM
 *   endpoint; the units list, org-scoped).
 * NO UOM write path exists (no save/update/status endpoint for UomUnit) —
 * finding, not fixed; the Save and the Status toggle report it honestly.
 *
 * Divergences (flagged, not silent):
 * 1. The edit (pencil) copies the row's code/units into the form client-side
 *    (the legacy pencil was a dead anchor; no detail endpoint exists).
 * 2. The search box filters the loaded list client-side (the legacy's inert).
 * 3. Save and the status toggle are intentionally not wired (no endpoints
 *    exist) — each shows an honest notice.
 */
@Component({
  selector: 'app-uom-master',
  imports: [FormsModule],
  templateUrl: './uom-master.html',
  styleUrl: './uom-master.scss',
})
export class UomMaster {
  private readonly service = inject(UomMasterService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId;

  rows = signal<UomUnitRow[]>([]);
  form = signal<UomMasterForm>({ ...emptyUomMasterForm });
  searchTerm = signal('');
  loading = signal(false);
  /** Signal-aware field setter — rerouted from the in-place `form().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.form.update((f) => ({...f, [key]: value}));
  }


  filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      [r.code, r.units].filter(Boolean).some((v) => String(v).toLowerCase().includes(term)),
    );
  });

  constructor() {
    this.loadUnits();
  }

  loadUnits(): void {
    this.loading.set(true);
    this.service.searchUnits(this.organizationId ?? 1).subscribe({
      next: (list) => {
        this.rows.set(Array.isArray(list) ? list : []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Units of measurement load failed');
      },
    });
  }

  /** The edit: copy the row's code/units into the form (client-side — no
   * detail endpoint exists; the legacy pencil was a dead anchor). */
  onEdit(row: UomUnitRow): void {
    this.form.set({ code: row.code ?? '', units: row.units ?? '' });
  }

  /** The status toggle: HONEST — no UOM update endpoint exists. */
  onToggle(): void {
    this.notify.error('Status update not wired — no UOM update endpoint exists (finding logged)');
  }

  /** The save: HONEST — no UOM save endpoint exists. */
  onSave(): void {
    this.notify.error('Save not wired — no UOM save endpoint exists (finding logged)');
  }
}
