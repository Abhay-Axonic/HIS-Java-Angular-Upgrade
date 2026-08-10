import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { BloodComponentService } from './blood-component.service';
import { BloodComponentRow } from './blood-component.models';

/**
 * Parity rebuild of the legacy blood-component.html (state `bloodComponent`,
 * module id 481). LEGACY IS A REGISTERED STATIC SHELL — the decoy-routed
 * shape: node 481's menu reaches this shell, while the real full-CRUD screen
 * is orphaned under `bloodComponentMaster` (16591, resolve gate 736).
 *
 * The rebuild implements the shell per the static-shell rule:
 *  - The grid is wired to the LIVE-VERIFIED list feed (real dev row: id 5
 *    "Whole Blood", org 1 unit 1).
 *  - The form keeps the shell's two fields (Component Code + Component
 *    Name).
 *  - Save and the Status switch report honestly: the write endpoints EXIST
 *    (save/update/updateStatus are mapped in the BloodBank module) but the
 *    shell's 2-field form cannot produce the full blood-component record
 *    (code, short desc, desc, quantity, expiry, byCrossChecking, dates…),
 *    and an incomplete payload is the probe-payload class
 *    (QF-2026-08-09-I). The complete form lives on the orphaned real screen
 *    (bloodComponentMaster).
 *
 * Deliberate divergences (flagged, not silent):
 * 1. The grid is wired to the live list feed (the legacy's was a hardcoded
 *    "- / - / switch / pencil" row).
 * 2. Save reports honestly — the endpoint exists but the shell form cannot
 *    supply its contract; the complete form is the orphaned real screen's.
 * 3. The Status switch reports honestly — a write to the live dev row would
 *    be a no-seed violation; the real screen's status toggle is the proper
 *    home for it.
 * 4. The search box filters the loaded grid client-side (legacy inert).
 * 5. The edit (pencil) copies the row's code/name into the form client-side
 *    (the legacy pencil was a dead anchor; the by-id detail endpoint is the
 *    real screen's).
 */
@Component({
  selector: 'app-blood-component',
  imports: [FormsModule],
  templateUrl: './blood-component.html',
  styleUrl: './blood-component.scss',
})
export class BloodComponent {
  private readonly service = inject(BloodComponentService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId;
  readonly unitId = this.session.unitId;

  rows = signal<BloodComponentRow[]>([]);
  form = signal({ code: '', name: '' });
  searchTerm = signal('');
  loading = signal(false);

  /** The grid filtered by the search box (client-side — divergence 4). */
  filteredRows = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      [r.bloodComponentCode, r.bloodComponentDesc, r.bloodComponentShortDesc]
        .filter(Boolean)
        .some((v) => String(v).toLowerCase().includes(term)),
    );
  });

  constructor() {
    this.loadMasters();
  }

  /** POST /bloodBank/getBloodComponentMasterList — the live-verified feed. */
  loadMasters(): void {
    this.loading.set(true);
    this.service.listMasters(this.organizationId ?? 1, this.unitId ?? 1).subscribe({
      next: (env) => {
        this.rows.set(Array.isArray(env?.listObject) ? env.listObject : []);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Blood component list load failed');
      },
    });
  }

  /** Display label: name, falling back to short desc (kept out of the
   * template because the preflight C2 pipe-scan misreads `||` as a pipe). */
  rowLabel(row: BloodComponentRow): string {
    return row.bloodComponentDesc
      ? String(row.bloodComponentDesc)
      : row.bloodComponentShortDesc
        ? String(row.bloodComponentShortDesc)
        : '';
  }

  /** The edit: copy the row's code/name into the form client-side. */
  onEdit(row: BloodComponentRow): void {
    this.form.set({
      code: row.bloodComponentCode ?? '',
      name: row.bloodComponentDesc ?? '',
    });
  }

  /** The save: HONEST — the save endpoint EXISTS but expects the full
   * blood-component record; this shell's 2-field form cannot supply it, and
   * an incomplete payload is the probe-payload class (QF-2026-08-09-I). */
  onSave(): void {
    this.notify.error(
      'Save not wired on this screen — the save endpoint expects the full blood-component record (code, short description, quantity, expiry, cross-checking…), which this shell form cannot supply; the complete form exists on the real Blood Component screen (finding logged)',
    );
  }

  /** The Status switch: HONEST — the status endpoint exists but flipping the
   * live dev row would be a no-seed write; the real screen's toggle is the
   * proper home for it. */
  onToggle(): void {
    this.notify.error(
      'Status update not wired on this screen — the status endpoint exists but toggling live data here would be a no-seed write; the real Blood Component screen owns the status toggle (finding logged)',
    );
  }
}
