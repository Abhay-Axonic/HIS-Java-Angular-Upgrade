import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { OccupationMasterService } from './occupation-master.service';
import { OccupationForm, OccupationRow, emptyOccupationForm } from './occupation-master.models';

/**
 * Parity rebuild of the REAL, reachable `occupationMaster` screen.
 *
 * =====================================================================
 * SEVERITY-1 CORRECTION, 2026-07-28 -- see `relation-master.ts`'s class doc
 * comment for the full writeup of this shared-family mistake; summarized
 * here. `occupationMasterController.js` is registered on TWO legacy
 * ui-router states: (1) a dedicated `/occupationMaster` state with its own
 * template (`occupation-master.html`) -- confirmed via direct grep of
 * `new-side-bar.html` to have NO live link anywhere; this is the state this
 * component previously modeled, and entry 80's "broken" verdict is accurate
 * for it. (2) The shared `commonMaster` dispatcher, entry `occupation` ->
 * `occupationMasterController`, template `common-master-screen.html`.
 * **Confirmed via direct grep: `new-side-bar.html:3395` links here**
 * (`href="#/commonMaster/occupation"`) -- the ONLY path a real user reaches.
 *
 * `common-master-screen.html` correctly names its form `commonform`
 * (matching `$scope.commonform.$valid`), Save/Update buttons
 * `ng-click="save()"`/`ng-click="update()"` (matching the controller's real
 * `$scope.save`/`$scope.update`), and its list `ng-repeat="info in commonList"`
 * (matching `$scope.commonList`) -- none of entry 80's 3 mismatch axes apply.
 *
 * **Confirmed live 2026-07-28, real unmodified legacy, via the actual
 * sidebar-linked route:** created a row (real `POST /adt/saveOccupationMaster`,
 * HTTP 200, "Occupation Added Successfully...!!", row appeared with a
 * working status toggle), toggled its status (real
 * `POST /adt/updateStatusForOccupation`, HTTP 200). Update/edit not
 * individually re-exercised for this screen this pass (identical controller
 * shape to `relationMaster`, where both were confirmed live) -- see
 * `docs/ANGULAR_PENDING_FINDINGS.md` correction entry for the full record.
 * Test row deactivated after verification.
 *
 * **Real, confirmed-live behavioral quirks reproduced below:**
 * - **Confirm-modal never-rearms-after-failure -- FIXED**, same shape and
 *   same fix as `relation-master`'s equivalent (see that component's doc
 *   comment for the full mechanism): `$scope.init()` (ctrl:28-59) resets
 *   `popUpFlag` back to `true` on save SUCCESS only, not failure. Rearmed
 *   on both outcomes here, matching this codebase's standing entry-55-class
 *   convention.
 * - **Update has no confirm-modal gate at all** (ctrl:333-384) -- saves
 *   directly on click.
 * - **The "Occupation" field uses the generic pattern** (no letters-only
 *   restriction, unlike `relation-master`'s sibling field) -- confirmed via
 *   direct template read: `common-master-screen.html`'s letters-only block
 *   is scoped to `LISDynamicLabel=='Priority Master'||'Relation'` only.
 * - **`updateStatus` has no revert-on-failure and does not reload the
 *   list** (ctrl:293-330) -- just a growl either way.
 * - The Color Code field (`common.colorCode`) is deliberately NOT modeled
 *   here -- confirmed via the controller's own payload construction that it
 *   is never sent to the backend; decorative-only, out of scope for this
 *   correction. Same reasoning as `relation-master`.
 * =====================================================================
 */
@Component({
  selector: 'app-occupation-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './occupation-master.html',
  styleUrl: './occupation-master.scss',
})
export class OccupationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(OccupationMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<OccupationForm>(emptyOccupationForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ctrl:49/190/28-59 -- rearmed on BOTH outcomes here (legacy only rearms
  // on success, via init() -- fixed, see class doc comment).
  private popupArmed = true;

  rows = signal<OccupationRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getOccupationList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getOccupationCount(this.organizationId).subscribe();
  }

  updateField<K extends keyof OccupationForm>(field: K, value: OccupationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ctrl:20-22 -- required code (max 10) + required occupation name (max
  // 256, generic pattern, no letters-only restriction).
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.code.trim() && f.code.length <= 10 && f.desc.trim() && f.desc.length <= 256);
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().code.trim() && this.form().code.length <= 10);
  }
  isDescInvalid(): boolean {
    const desc = this.form().desc;
    return this.formSubmitted() && !(desc.trim() && desc.length <= 256);
  }

  onSaveClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    if (this.popupArmed) {
      this.showConfirmModal.set(true);
      return;
    }
    this.confirmSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmFromModal(): void {
    this.popupArmed = false;
    this.showConfirmModal.set(false);
    this.confirmSave();
  }

  private confirmSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveOccupation({
        code: f.code,
        desc: f.desc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Occupation Added Successfully...!!');
            this.form.set(emptyOccupationForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Occupation already exits...!!');
          }
          this.loadList();
          this.popupArmed = true; // FIX: rearm on both outcomes.
        },
      });
  }

  // mirrors $scope.update (ctrl:333-384) -- no confirm-modal gate.
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updateOccupation({
        id: f.id as number,
        code: f.code,
        desc: f.desc,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Occupation Updated Successfully...!!');
            this.form.set(emptyOccupationForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Occupation already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // mirrors showUpdateBtn (ctrl:259-283).
  edit(row: OccupationRow): void {
    this.saveMode.set(false);
    this.api.getOccupationById(row.id).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:280 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({ id: d.id, code: d.code, desc: d.desc });
      },
    });
  }

  // mirrors updateStatus (ctrl:293-330) -- no revert-on-failure, no list
  // reload after.
  toggleStatus(row: OccupationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updateOccupationStatus({
        id: row.id,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Occupation Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
