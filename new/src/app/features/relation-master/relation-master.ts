import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RelationMasterService } from './relation-master.service';
import { RelationForm, RelationRow, emptyRelationForm } from './relation-master.models';

/**
 * Parity rebuild of the REAL, reachable `relationMaster` screen.
 *
 * =====================================================================
 * SEVERITY-1 CORRECTION, 2026-07-28 -- this component previously modeled
 * Save/Update as permanent no-ops based on a SEVERITY-1 finding
 * (docs/ANGULAR_PENDING_FINDINGS.md entry 80) that turns out to have
 * audited the wrong screen. `relationMasterController.js`
 * (`resources/assets/js/controller/adt/global/relationMasterController.js`)
 * is registered on TWO separate legacy ui-router states:
 * 1. `.state('relationMaster', {url:'/relationMaster', templateUrl:
 *    'views/master/GlobalMaster/relation-master.html', ...})` -- a real,
 *    loadable route, but confirmed via direct grep of
 *    `views/common/new-side-bar.html` to have NO live link anywhere in the
 *    app. This is the state/template entry 80 characterized as broken
 *    (mismatched `saveRelation()`/`relation.*`/`relationList` names) -- an
 *    accurate finding for THIS unreachable path.
 * 2. The shared `.state('commonMaster', {url:'/commonMaster/:url', resolve:
 *    {getController:...}})` dispatcher, entry `relationmaster` ->
 *    `relationMasterController`, template `views/master/common-master-screen.html`.
 *    **Confirmed via direct grep: `new-side-bar.html:3388` links here**
 *    (`href="#/commonMaster/relationmaster"`) -- this is the ONLY path a
 *    real user can ever reach.
 *
 * `common-master-screen.html` correctly names its form `commonform`
 * (matching `$scope.commonform.$valid`), its Save/Update buttons
 * `ng-click="save()"`/`ng-click="update()"` (matching the controller's real,
 * fully-implemented `$scope.save`/`$scope.update`), and its list
 * `ng-repeat="info in commonList"` (matching `$scope.commonList`, the array
 * the real list-fetch actually populates) -- NONE of entry 80's 3 mismatch
 * axes apply here.
 *
 * **Confirmed live 2026-07-28, real unmodified legacy, via the actual
 * sidebar-linked route (not a typed dedicated-state URL):** created a row
 * (real `POST /adt/saveRelationMaster`, HTTP 200, row appeared in the
 * table with a working status toggle), edited it (real
 * `POST /adt/updateRelationMaster`, HTTP 200, edit visible after reload),
 * toggled its status (real `POST /adt/updateStatusForRelation`, HTTP 200).
 * All test rows deactivated after verification.
 *
 * **Real, confirmed-live behavioral quirks reproduced below (not bugs to
 * silently normalize away):**
 * - **Confirm-modal never-rearms-after-failure -- FIXED, matching this
 *   codebase's standing entry-55-class convention (same fix already applied
 *   across every other batch's screens, e.g. batch 22/23's `shiftMaster`/
 *   `dietUnitMater`/`APmaster`/etc.).** `$scope.save()` (ctrl:195-256) gates
 *   on `$scope.popUpFlag`, set `false` by `PopupOkBtn()` and reset back to
 *   `true` by `$scope.init()` (ctrl:28-76) -- but `init()` is called ONLY on
 *   the SUCCESS branch of the real save (ctrl:236-238), not on failure
 *   (e.g. a rejected duplicate code) -- so a failed save leaves the modal
 *   permanently skipped for the rest of the page session. Per this
 *   codebase's established policy, rearmed on BOTH outcomes here
 *   (`popupArmed`, reset to `true` unconditionally after the save response
 *   resolves), not faithfully reproduced.
 * - **Update has no confirm-modal gate at all** (ctrl:331-383) -- saves
 *   directly on click, matching this codebase's `district-master`
 *   save-only-confirms asymmetry.
 * - **The "Relation" field is letters-only, no spaces/digits** --
 *   `common-master-screen.html`'s Description field has a SEPARATE
 *   conditional block for `LISDynamicLabel=='Priority Master'||'Relation'`
 *   using `pattern="[A-Za-z]+"` (confirmed by direct template read), unlike
 *   every other screen sharing this same dispatcher family. Reproduced via
 *   a matching pattern validator.
 * - **`updateStatus` has no revert-on-failure and does not reload the
 *   list** (ctrl:291-329) -- just a growl either way, matching this
 *   codebase's `amenity-master`-style toggle (not `district-master`'s
 *   always-reload-after variant).
 * - The Color Code field on the shared template (`common.colorCode`) is
 *   deliberately NOT modeled here -- confirmed via the controller's own
 *   `save()`/`update()` payload construction (ctrl:207-217/340-347) that it
 *   is never sent to the backend at all; it is decorative-only UI with no
 *   functional effect, out of scope for this correction (which targets the
 *   Save/Update/list-display functional bugs, not new UI surface).
 * =====================================================================
 */
@Component({
  selector: 'app-relation-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './relation-master.html',
  styleUrl: './relation-master.scss',
})
export class RelationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RelationMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<RelationForm>(emptyRelationForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ctrl:49/190/28-76 -- starts true; rearmed on BOTH outcomes here
  // (legacy only rearms on success, via init() -- fixed, see class doc
  // comment).
  private popupArmed = true;

  rows = signal<RelationRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getRelationMasterList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getRelationCount(this.organizationId).subscribe();
  }

  updateField<K extends keyof RelationForm>(field: K, value: RelationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ctrl:20-22 -- required code (max 10) + required, letters-only relation
  // name (max 256, pattern="[A-Za-z]+").
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.code.trim() && f.code.length <= 10 && f.desc.trim() && /^[A-Za-z]+$/.test(f.desc) && f.desc.length <= 256);
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().code.trim() && this.form().code.length <= 10);
  }
  isDescInvalid(): boolean {
    const desc = this.form().desc;
    return this.formSubmitted() && !(desc.trim() && /^[A-Za-z]+$/.test(desc) && desc.length <= 256);
  }

  // mirrors $scope.save (ctrl:195-256) -- confirm-modal-gated, rearmed on
  // every outcome (see class doc comment).
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

  // mirrors PopupOkBtn (ctrl:189-192) -- sets popUpFlag false, then calls
  // save() again, which this time takes the real-save branch.
  confirmFromModal(): void {
    this.popupArmed = false;
    this.showConfirmModal.set(false);
    this.confirmSave();
  }

  private confirmSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveRelationMaster({
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
            this.notify.success('Relation Added Successfully...!!');
            this.form.set(emptyRelationForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Relation already exits...!!');
          }
          // ctrl:247-251 -- unconditional, both success and failure.
          this.loadList();
          // FIX (see class doc comment): rearm on both outcomes, not just
          // success.
          this.popupArmed = true;
        },
      });
  }

  // mirrors $scope.update (ctrl:331-383) -- no confirm-modal gate.
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updateRelationMaster({
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
            this.notify.success('Relation Updated Successfully...!!');
            this.form.set(emptyRelationForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Relation already exits...!!');
          }
          // ctrl:376-380 -- unconditional, both success and failure.
          this.loadList();
        },
      });
  }

  // mirrors showUpdateBtn (ctrl:258-283).
  edit(row: RelationRow): void {
    this.saveMode.set(false);
    this.api.getRelationById(row.id).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:279 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({ id: d.id, code: d.code, desc: d.desc });
      },
    });
  }

  // mirrors updateStatus (ctrl:291-329) -- no revert-on-failure, no list
  // reload after (just a growl either way). See class doc comment.
  toggleStatus(row: RelationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatusForRelation({
        id: row.id,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Relation Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:294-296/333-335. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
