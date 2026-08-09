/**
 * immunization-schedule — parity rebuild of the legacy
 *   views/master/GlobalMaster/immunization-schedule.html
 *   (state `immunizationSchedule`, module id 483, parent 476 Clinical
 *   Workstation -> 369 Global Master; controller: the generic
 *   MasterController — which contains NO screen-specific logic).
 *
 * LEGACY IS A REGISTERED STATIC SHELL (hand-confirmed 2026-08-09):
 * app.js:8433 `.state('immunizationSchedule', { url, templateUrl, controller:
 * "MasterController", title })` — REGISTERED, and the privilege tree carries
 * node 483 'Immunization Schedule' (parent 476 -> 369, actionLink
 * `#/immunizationSchedule` matches). The 176-line template has ZERO ng-*
 * bindings (the only "ng-" strings are `padding-*` class-name fragments),
 * ZERO API calls, one hardcoded grid row ("- / - / View Disease List / -"),
 * an inert Save anchor (data-toggle to a nonexistent #confirmLogout modal)
 * and an inert search box. The generic MasterController has no
 * immunization logic.
 *
 * The static-shell precedent (owner-approved, screens 4-5) applies: the
 * rebuild IMPLEMENTS the shell with the endpoints that demonstrably exist,
 * and every missing endpoint is reported honestly in the UI (never a silent
 * dead button).
 *
 * BACKEND REALITY (verified by CALLING with the cached token — 2026-08-09):
 *   - NO ImmunizationSchedule controller/DAO/entity exists anywhere in the
 *     backend (grep across all modules).
 *   - The VaccinationMasterController (Billing, /api/billing/*) real CRUD
 *     (save/update/status/byId/list/count/getActive) serves the OTHER real
 *     vaccination screen (state vaccinationMAster, app.js:17050) — not this
 *     shell.
 *   - The only real feed on THIS screen's path is the vaccine catalog:
 *     GET /api/billing/getActiveVaccinationMaster/{org}/{unit} -> 200
 *     "success" ([] in dev). The Vaccine select is wired to it.
 *   - No disease-list endpoint exists on this path (the Disease List modal
 *     has nothing to load) and no immunization-schedule list/save/update
 *     endpoints exist — reported honestly, logged as a finding.
 *
 * Divergences (flagged, not silent):
 * 1. The Vaccine select is wired to the real vaccine catalog feed (the
 *    legacy select was empty and inert).
 * 2. The Save button reports the missing save path honestly (no
 *    immunization-schedule save endpoint exists).
 * 3. The Disease List button reports the missing disease-list path
 *    honestly (no endpoint on this path).
 * 4. The Age (Day/Month/Year) and Sequence selects are static UI (the
 *    legacy's were empty selects with no data source; no schedule fields
 *    have a backend).
 * 5. The grid renders the honest empty state — no list endpoint exists.
 */
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { ImmunizationScheduleService } from './immunization-schedule.service';
import { VaccineOption } from './immunization-schedule.models';

@Component({
  selector: 'app-immunization-schedule',
  imports: [FormsModule],
  templateUrl: './immunization-schedule.html',
  styleUrl: './immunization-schedule.scss',
})
export class ImmunizationSchedule {
  private readonly service = inject(ImmunizationScheduleService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId;
  readonly unitId = this.session.unitId;

  vaccines = signal<VaccineOption[]>([]);
  loadingVaccines = signal(false);
  form = signal({
    ageUnit: 'Day',
    ageValue: '',
    vaccineId: null as number | null,
    sequence: '',
  });

  constructor() {
    this.loadVaccines();
  }

  /** GET /api/billing/getActiveVaccinationMaster/{org}/{unit} — the real
   * vaccine catalog feed (the only live endpoint on this screen's path). */
  loadVaccines(): void {
    this.loadingVaccines.set(true);
    this.service.activeVaccinationMasters(this.organizationId ?? 1, this.unitId ?? 1).subscribe({
      next: (env) => {
        this.vaccines.set(Array.isArray(env?.listObject) ? env.listObject : []);
        this.loadingVaccines.set(false);
      },
      error: () => {
        this.loadingVaccines.set(false);
        this.notify.error('Vaccine list load failed');
      },
    });
  }

  /** The save: HONEST — no immunization-schedule save endpoint exists. */
  onSave(): void {
    this.notify.error(
      'Save not wired — no immunization-schedule save endpoint exists (finding logged)',
    );
  }

  /** The Disease List: HONEST — no disease-list endpoint exists on this path. */
  onDiseaseList(): void {
    this.notify.error(
      'Disease list not available — no disease-list endpoint exists on this path (finding logged)',
    );
  }
}
