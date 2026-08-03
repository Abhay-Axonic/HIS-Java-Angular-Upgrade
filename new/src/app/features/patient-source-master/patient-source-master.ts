import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PatientSourceMasterService } from './patient-source-master.service';
import { PatientSourceForm, PatientSourceRow, emptyPatientSourceForm } from './patient-source-master.models';

/**
 * Parity rebuild of the REAL, reachable `patientSourceMaster` screen.
 *
 * `patientSourceMasterController.js`
 * (`resources/assets/js/controller/adt/global/patientSourceMasterController.js`)
 * is registered on TWO separate legacy ui-router states, the same
 * shared-dispatcher-family shape already documented and corrected on
 * `relation-master`/`occupation-master`/`marital-status-master` (see
 * `relation-master.ts`'s class doc comment for the full mechanism writeup):
 * 1. `.state('patientSourceMaster', {url:'/patientSourceMaster', templateUrl:
 *    'views/master/GlobalMaster/patient-source-master.html', ...})` -- a
 *    real, loadable route, but confirmed via direct grep of
 *    `new-side-bar.html` to have NO live link anywhere in the app. Its own
 *    view is mismatched to the controller: form name `patientsourceform`
 *    (controller checks `$scope.commonform.$valid`), `ng-model="patientsource.*"`
 *    (controller reads/writes `$scope.common.*`), buttons
 *    `ng-click="savePatientSource()"`/`ng-click="updatePatientSource()"`
 *    (controller only defines `$scope.save`/`$scope.update`), list
 *    `ng-repeat="info in patientsourceList"` (controller populates
 *    `$scope.commonList`, never `$scope.patientsourceList`) -- unreachable
 *    and non-functional even if it were reachable.
 * 2. The shared `.state('commonMaster', {url:'/commonMaster/:url', resolve:
 *    {getController:...}})` dispatcher, entry `patientsource` ->
 *    `patientSourceMasterController`, template `views/master/common-master-screen.html`.
 *    **Confirmed via direct grep: `new-side-bar.html:3398` links here**
 *    (`href="#/commonMaster/patientsource"`, gated by `checkAccess(394)` --
 *    note the anchor carries a SECOND, duplicate `ng-if` attribute
 *    (`checkAccess('Religion Master','screen')`) that HTML5's duplicate-
 *    attribute parsing silently drops, keeping only the first -- a real,
 *    harmless copy-paste artifact shared with the `referraltype` link right
 *    below it, not modeled here since it has no observable effect: the
 *    first `ng-if` alone already gates real access correctly, and this
 *    rebuild's own `moduleAccessGuard` enforces the single real moduleId
 *    (394) already).
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
 * `POST /adt/savePatientSourceMaster`, HTTP 200, "Patientsource Added
 * Successfully...!!", row appeared in the table with a working status
 * toggle), edited it (real `POST /adt/updatePatientSourceMaster`, HTTP 200,
 * edit visible after reload), toggled its status (real
 * `POST /adt/updateStatusForPatientSource`, HTTP 200). Test rows
 * deactivated after verification.
 *
 * **Real, confirmed-live behavioral quirks reproduced below:**
 * - **Confirm-modal never-rearms-after-failure -- FIXED, matching this
 *   codebase's standing entry-55-class convention** (same fix already
 *   applied across every other screen sharing this dispatcher family, e.g.
 *   `relation-master`/`occupation-master`/batch 22/23's `shiftMaster` etc.
 *   -- see `docs/ANGULAR_PENDING_FINDINGS.md` entry 152 and
 *   `PENDING-confirmmodal-shared-directive-reaudit`: the shared `<modal>`
 *   directive's own `oneClickOnly`/`$watch` mechanism unconditionally
 *   closes `showModal` and re-arms `popUpFlag` on every real Ok click,
 *   independent of this screen's own save success/failure branching, so the
 *   modal can never actually get stuck open or skipped in real legacy no
 *   matter what `$scope.save`'s own callback does). `$scope.save()`
 *   (ctrl:177-238) gates on `$scope.popUpFlag`, set `false` by
 *   `PopupOkBtn()` and reset back to `true` only via `$scope.init()`
 *   (ctrl:220), called ONLY on the SUCCESS branch of the real save --
 *   modeled here as rearming on BOTH outcomes (`popupArmed`), matching the
 *   directive's real behavior rather than the controller's own (masked)
 *   success-only reset.
 * - **Update has no confirm-modal gate at all** (ctrl:316-366) -- saves
 *   directly on click, matching `relation-master`/`occupation-master`'s
 *   identical save-only asymmetry.
 * - **The "Patient Source" field uses the generic pattern** (no
 *   letters-only restriction) -- confirmed via direct template read:
 *   `common-master-screen.html`'s letters-only block is scoped to
 *   `LISDynamicLabel=='Priority Master'||'Relation'` only;
 *   `patientSourceMasterController.js` sets `LISDynamicLabel = "Patient
 *   Source"` (ctrl:34), so this screen takes the generic branch (required +
 *   maxlength 256, no pattern) -- same as `occupation-master`.
 * - **`updateStatus` has no revert-on-failure and does not reload the
 *   list** (ctrl:276-313) -- just a growl either way, matching
 *   `occupation-master`/`relation-master`'s identical toggle shape.
 * - The Color Code field (`common.colorCode`) is deliberately NOT modeled
 *   here -- confirmed via the controller's own `save()`/`update()` payload
 *   construction (ctrl:190-199/323-330) that it is never sent to the
 *   backend; decorative-only UI, out of scope for this rebuild. Same
 *   reasoning as `relation-master`/`occupation-master`.
 */
@Component({
  selector: 'app-patient-source-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './patient-source-master.html',
  styleUrl: './patient-source-master.scss',
})
export class PatientSourceMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PatientSourceMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<PatientSourceForm>(emptyPatientSourceForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ctrl:29/171-174/220 -- starts true; rearmed on BOTH outcomes here
  // (legacy's own controller only rearms on success via init() -- the
  // shared directive itself rearms on both, see class doc comment).
  private popupArmed = true;

  rows = signal<PatientSourceRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getPatientSourceList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
      error: (err) => console.error('patient-source-master list load error', err),
    });
    this.api.getPatientSourceCount(this.organizationId).subscribe({
      error: (err) => console.error('patient-source-master count load error', err),
    });
  }

  updateField<K extends keyof PatientSourceForm>(field: K, value: PatientSourceForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // common-master-screen.html -- required code (max 10) + required Patient
  // Source name (max 256, generic pattern, no letters-only restriction).
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

  // mirrors $scope.save (ctrl:177-238) -- confirm-modal-gated, rearmed on
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
      .savePatientSource({
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
            this.notify.success('Patientsource Added Successfully...!!');
            this.form.set(emptyPatientSourceForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Patientsource already exits...!!');
          }
          // ctrl:229-233 -- unconditional, both success and failure.
          this.loadList();
          // FIX (see class doc comment): rearm on both outcomes, not just
          // success.
          this.popupArmed = true;
        },
        error: (err) => console.error('patient-source-master save error', err),
      });
  }

  // mirrors $scope.update (ctrl:316-366) -- no confirm-modal gate.
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updatePatientSource({
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
            this.notify.success('Patientsource Updated Successfully...!!');
            this.form.set(emptyPatientSourceForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Patientsource already exits...!!');
          }
          // ctrl:359-363 -- unconditional, both success and failure.
          this.loadList();
        },
        error: (err) => console.error('patient-source-master update error', err),
      });
  }

  // mirrors showUpdateBtn (ctrl:241-265).
  edit(row: PatientSourceRow): void {
    this.saveMode.set(false);
    this.api.getPatientSourceById(row.id).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:261 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({ id: d.id, code: d.code, desc: d.desc });
      },
      error: (err) => console.error('patient-source-master get-by-id error', err),
    });
  }

  // mirrors updateStatus (ctrl:276-313) -- no revert-on-failure, no list
  // reload after (just a growl either way). See class doc comment.
  toggleStatus(row: PatientSourceRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updatePatientSourceStatus({
        id: row.id,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Patientsource Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: (err) => console.error('patient-source-master status toggle error', err),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:179-181/278-280/317-319. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
