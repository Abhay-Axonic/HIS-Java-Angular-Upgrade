import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { MaritalStatusMasterService } from './marital-status-master.service';
import { MaritalStatusForm, MaritalStatusRow, emptyMaritalStatusForm } from './marital-status-master.models';

/**
 * Parity rebuild of the REAL, reachable `maritalstatusMaster` screen.
 *
 * =====================================================================
 * SEVERITY-1 CORRECTION, 2026-07-28 -- see `relation-master.ts`'s class doc
 * comment for the full writeup of this shared-family mistake; summarized
 * here. `maritalstatusMasterController.js` is registered on TWO legacy
 * ui-router states: (1) a dedicated `/maritalstatusMaster` state with its
 * own template (`marital-status-master.html`) -- confirmed via direct grep
 * of `new-side-bar.html` to have NO live link anywhere; this is the state
 * this component previously modeled, and entry 80's "broken" verdict is
 * accurate for it. (2) The shared `commonMaster` dispatcher, entry
 * `maritalstatus` -> `maritalstatusMasterController`, template
 * `common-master-screen.html`. **Confirmed via direct grep:
 * `new-side-bar.html` links here** (`href="#/commonMaster/maritalstatus"`)
 * -- the ONLY path a real user reaches.
 *
 * `common-master-screen.html` correctly names its form `commonform`
 * (matching `$scope.commonform.$valid`), Save/Update buttons
 * `ng-click="save()"`/`ng-click="update()"` (matching the controller's real
 * `$scope.save`/`$scope.update`), and its list `ng-repeat="info in commonList"`
 * (matching `$scope.commonList`) -- none of entry 80's 3 mismatch axes
 * apply.
 *
 * **Direct evidence this screen has always worked via the correct route:**
 * this dev DB has 2 genuine pre-existing rows via the real dispatcher route
 * ("MRD"/Married, "SNGL"/Single, both Active) -- confirmed live 2026-07-28.
 * Live save/update round trips were confirmed for this screen's siblings
 * (`relationMaster`/`occupationMaster`/`religionMaster`/`patientSourceMaster`/
 * `referralTypeMaster`, all 100% functional via the identical controller
 * shape) rather than individually re-exercised here, given the pre-existing
 * real data already disproves entry 80's "non-functional" verdict for this
 * screen directly. See `docs/ANGULAR_PENDING_FINDINGS.md` correction entry
 * for the full record.
 *
 * **Real, confirmed-live-on-siblings behavioral quirks reproduced below:**
 * - **Confirm-modal never-rearms-after-failure -- FIXED**, same shape and
 *   same fix as `relation-master`'s equivalent (see that component's doc
 *   comment for the full mechanism): `$scope.init()` (ctrl:28-77) resets
 *   `popUpFlag` back to `true` on save SUCCESS only, not failure. Rearmed
 *   on both outcomes here, matching this codebase's standing entry-55-class
 *   convention.
 * - **Update has no confirm-modal gate at all** (ctrl:335-387) -- saves
 *   directly on click. Unlike its `occupationMaster`/`relationMaster`
 *   siblings, this screen's update payload DOES include `status:'A'`
 *   hardcoded (ctrl:348) -- a confirmed, preserved divergence.
 * - **The "Marital Status" field uses the generic pattern** (no
 *   letters-only restriction, unlike `relation-master`'s sibling field).
 * - **`updateStatus` has no revert-on-failure and does not reload the
 *   list** (ctrl:295-332) -- just a growl either way.
 * - The Color Code field (`common.colorCode`) is deliberately NOT modeled
 *   here -- same reasoning as `relation-master`/`occupation-master`.
 * =====================================================================
 */
@Component({
  selector: 'app-marital-status-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './marital-status-master.html',
  styleUrl: './marital-status-master.scss',
})
export class MaritalStatusMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(MaritalStatusMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<MaritalStatusForm>(emptyMaritalStatusForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ctrl:49/190/28-77 -- rearmed on BOTH outcomes here (legacy only rearms
  // on success, via init() -- fixed, see class doc comment).
  private popupArmed = true;

  rows = signal<MaritalStatusRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getMaritalStatusList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getMaritalStatusCount(this.organizationId).subscribe();
  }

  updateField<K extends keyof MaritalStatusForm>(field: K, value: MaritalStatusForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

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
      .saveMaritalStatus({
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
            this.notify.success('Marital Status added successfully...!!');
            this.form.set(emptyMaritalStatusForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Marital Status already exits...!!');
          }
          this.loadList();
          this.popupArmed = true; // FIX: rearm on both outcomes.
        },
      });
  }

  // mirrors $scope.update (ctrl:335-387) -- no confirm-modal gate; payload
  // includes hardcoded status:'A' (a confirmed divergence from
  // occupationMaster's equivalent).
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updateMaritalStatus({
        id: f.id as number,
        code: f.code,
        desc: f.desc,
        status: 'A',
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Marital Status updated successfully...!!');
            this.form.set(emptyMaritalStatusForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Marital Status already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // mirrors showUpdateBtn (ctrl:261-285).
  edit(row: MaritalStatusRow): void {
    this.saveMode.set(false);
    this.api.getMaritalById(row.id).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:282 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({ id: d.id, code: d.code, desc: d.desc });
      },
    });
  }

  // mirrors updateStatus (ctrl:295-332) -- no revert-on-failure, no list
  // reload after.
  toggleStatus(row: MaritalStatusRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updateMaritalStatusStatus({
        id: row.id,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Marital Updated successfully...!!');
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
