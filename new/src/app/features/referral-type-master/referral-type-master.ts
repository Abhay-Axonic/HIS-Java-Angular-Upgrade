import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ReferralTypeMasterService } from './referral-type-master.service';
import { ReferralTypeForm, ReferralTypeRow, emptyReferralTypeForm } from './referral-type-master.models';

/**
 * Parity rebuild of the REAL, reachable `referralTypeMaster` screen.
 *
 * `referralTypeMasterController.js`
 * (`resources/assets/js/controller/adt/global/referralTypeMasterController.js`)
 * is registered on TWO separate legacy ui-router states, the same
 * shared-dispatcher-family shape already documented and corrected on
 * `relation-master`/`occupation-master`/`marital-status-master`/
 * `patient-source-master` (see `relation-master.ts`'s class doc comment for
 * the full mechanism writeup):
 * 1. `.state('referralTypeMaster', {url:'/referralTypeMaster', templateUrl:
 *    'views/master/GlobalMaster/referral-type-master.html', ...})` -- a
 *    real, loadable route, but confirmed via direct grep of
 *    `new-side-bar.html`/`side-bar.html` to have NO live link anywhere in
 *    the app. Its own view is mismatched to the controller: form name
 *    `referraltypeform` (controller checks `$scope.commonform.$valid`),
 *    `ng-model="referraltype.*"` (controller reads/writes `$scope.common.*`),
 *    buttons `ng-click="saveReferralType()"`/`ng-click="updateReferralType()"`
 *    (controller only defines `$scope.save`/`$scope.update`), list
 *    `ng-repeat="info in referraltypeList"` (controller populates
 *    `$scope.commonList`, never `$scope.referraltypeList`) -- unreachable
 *    and non-functional even if it were reachable.
 * 2. The shared `.state('commonMaster', {url:'/commonMaster/:url', resolve:
 *    {getController:...}})` dispatcher, entry `referraltype` ->
 *    `referralTypeMasterController`, template
 *    `views/master/common-master-screen.html`. **Confirmed via direct grep:
 *    `side-bar.html:150` and `new-side-bar.html:3401` link here**
 *    (`href="#/commonMaster/referraltype"`, gated by `checkAccess(395)` --
 *    note the anchor carries a SECOND, duplicate `ng-if` attribute
 *    (`checkAccess('Religion Master','screen')`) that HTML5's duplicate-
 *    attribute parsing silently drops, keeping only the first -- the same
 *    harmless copy-paste artifact already documented on
 *    `patient-source-master`'s link right above it, not modeled here since
 *    it has no observable effect: the first `ng-if` alone already gates
 *    real access correctly, and this rebuild's own `moduleAccessGuard`
 *    enforces the single real moduleId (395) already).
 *
 * `common-master-screen.html` correctly names its form `commonform`
 * (matching `$scope.commonform.$valid`), its Save/Update buttons
 * `ng-click="save()"`/`ng-click="update()"` (matching the controller's real,
 * fully-implemented `$scope.save`/`$scope.update`), and its list
 * `ng-repeat="info in commonList"` (matching `$scope.commonList`) -- this is
 * the only reachable, functional path.
 *
 * **Confirmed live 2026-07-28, real unmodified legacy, via the actual
 * sidebar-linked route:** created a row (real
 * `POST /adt/saveReferralTypeMaster`, HTTP 200, "Referral Type Added
 * Successfully...!!", row appeared in the table with a working status
 * toggle), attempted a duplicate name (real `POST /adt/saveReferralTypeMaster`,
 * HTTP 200 with `status:"error"`, "Referral Type already exits...!!"),
 * edited it (real `POST /adt/updateReferralTypeMaster`, HTTP 200, edit
 * visible after reload), toggled its status (real
 * `POST /adt/updateStatusForReferralType`, HTTP 200). Test row deactivated
 * after verification.
 *
 * **Real, confirmed-live behavioral quirks reproduced below:**
 * - **The description field/table column is labelled "Referral Type
 *   Source"**, not "Referral Type" -- confirmed via direct controller read,
 *   `$scope.LISDynamicLabel = "Referral Type Source";` (ctrl:34), which
 *   `common-master-screen.html` binds verbatim into both the form field
 *   label and the table's second column header (`{{LISDynamicLabel}}`).
 *   A real, visible legacy quirk (likely a copy/paste leftover), faithfully
 *   reproduced rather than "corrected" to the more sensible "Referral Type".
 * - **Confirm-modal never-rearms-after-failure -- FIXED, matching this
 *   codebase's standing entry-55-class convention** (same fix already
 *   applied across every other screen sharing this dispatcher family, e.g.
 *   `relation-master`/`occupation-master`/`patient-source-master`/batch
 *   22/23's `shiftMaster` etc. -- see `docs/ANGULAR_PENDING_FINDINGS.md`
 *   entry 152 and `PENDING-confirmmodal-shared-directive-reaudit`: the
 *   shared `<modal>` directive's own `oneClickOnly`/`$watch` mechanism
 *   unconditionally closes `showModal` and re-arms `popUpFlag` on every
 *   real Ok click, independent of this screen's own save success/failure
 *   branching, so the modal can never actually get stuck open or skipped in
 *   real legacy no matter what `$scope.save`'s own callback does).
 *   `$scope.save()` (ctrl:177-237) gates on `$scope.popUpFlag`, set `false`
 *   by `PopupOkBtn()` and reset back to `true` only via `$scope.init()`
 *   (ctrl:219), called ONLY on the SUCCESS branch of the real save --
 *   modeled here as rearming on BOTH outcomes (`popupArmed`), matching the
 *   directive's real behavior rather than the controller's own (masked)
 *   success-only reset.
 * - **Update has no confirm-modal gate at all** (ctrl:315-366) -- saves
 *   directly on click, matching `relation-master`/`occupation-master`/
 *   `patient-source-master`'s identical save-only asymmetry.
 * - **The "Referral Type Source" field uses the generic pattern** (no
 *   letters-only restriction) -- confirmed via direct template read:
 *   `common-master-screen.html`'s letters-only block is scoped to
 *   `LISDynamicLabel=='Priority Master'||'Relation'` only; this screen's
 *   `LISDynamicLabel` is `"Referral Type Source"`, so it takes the generic
 *   branch (required + maxlength 256, no pattern) -- same as
 *   `occupation-master`/`patient-source-master`.
 * - **`updateStatus` has no revert-on-failure and does not reload the
 *   list** (ctrl:275-312) -- just a growl either way, matching
 *   `occupation-master`/`relation-master`/`patient-source-master`'s
 *   identical toggle shape.
 * - **Update's success-text casing differs from Save's and from
 *   updateStatus's** -- `update()`'s growl reads "Referral Type **u**pdated
 *   Successfully...!!" (lowercase u, ctrl:341-345), while `updateStatus()`'s
 *   reads "Referral Type **U**pdated Successfully...!!" (capital U,
 *   ctrl:297-301) -- a real, visible legacy string inconsistency,
 *   reproduced verbatim rather than normalized.
 * - The Color Code field (`common.colorCode`) is deliberately NOT modeled
 *   here -- confirmed via the controller's own `save()`/`update()` payload
 *   construction (ctrl:189-198/323-330) that it is never sent to the
 *   backend; decorative-only UI, out of scope for this rebuild. Same
 *   reasoning as `relation-master`/`occupation-master`/`patient-source-master`.
 */
@Component({
  selector: 'app-referral-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './referral-type-master.html',
  styleUrl: './referral-type-master.scss',
})
export class ReferralTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ReferralTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<ReferralTypeForm>(emptyReferralTypeForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ctrl:29/171-174/219 -- starts true; rearmed on BOTH outcomes here
  // (legacy's own controller only rearms on success via init() -- the
  // shared directive itself rearms on both, see class doc comment).
  private popupArmed = true;

  rows = signal<ReferralTypeRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getReferralTypeList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
      error: (err) => console.error('referral-type-master list load error', err),
    });
    this.api.getReferralTypeCount(this.organizationId).subscribe({
      error: (err) => console.error('referral-type-master count load error', err),
    });
  }

  updateField<K extends keyof ReferralTypeForm>(field: K, value: ReferralTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // common-master-screen.html -- required code (max 10) + required Referral
  // Type Source name (max 256, generic pattern, no letters-only restriction).
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

  // mirrors $scope.save (ctrl:177-237) -- confirm-modal-gated, rearmed on
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

  // mirrors PopupOkBtn (ctrl:171-174) -- sets popUpFlag false, then calls
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
      .saveReferralType({
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
            this.notify.success('Referral Type Added Successfully...!!');
            this.form.set(emptyReferralTypeForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Referral Type already exits...!!');
          }
          // ctrl:228-232 -- unconditional, both success and failure.
          this.loadList();
          // FIX (see class doc comment): rearm on both outcomes, not just
          // success.
          this.popupArmed = true;
        },
        error: (err) => {
          console.error('referral-type-master save error', err);
          this.notify.error('Something went wrong...!!');
        },
      });
  }

  // mirrors $scope.update (ctrl:315-366) -- no confirm-modal gate.
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updateReferralType({
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
            // ctrl:341-345 -- lowercase "updated" (differs from Save's and
            // updateStatus's casing, see class doc comment).
            this.notify.success('Referral Type updated Successfully...!!');
            this.form.set(emptyReferralTypeForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Referral Type already exits...!!');
          }
          // ctrl:359-363 -- unconditional, both success and failure.
          this.loadList();
        },
        error: (err) => {
          console.error('referral-type-master update error', err);
          this.notify.error('Something went wrong...!!');
        },
      });
  }

  // mirrors showUpdateBtn (ctrl:240-264).
  edit(row: ReferralTypeRow): void {
    this.saveMode.set(false);
    this.api.getReferralTypeById(row.id).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:260 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({ id: d.id, code: d.code, desc: d.desc });
      },
      error: (err) => console.error('referral-type-master edit load error', err),
    });
  }

  // mirrors updateStatus (ctrl:275-312) -- no revert-on-failure, no list
  // reload after (just a growl either way). See class doc comment.
  toggleStatus(row: ReferralTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatusForReferralType({
        id: row.id,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            // ctrl:297-301 -- capital "Updated" (differs from update()'s
            // casing, see class doc comment).
            this.notify.success('Referral Type Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: (err) => {
          console.error('referral-type-master status-toggle error', err);
          this.notify.error('Something went wrong...!!');
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:178-180/277-279/317-319. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
