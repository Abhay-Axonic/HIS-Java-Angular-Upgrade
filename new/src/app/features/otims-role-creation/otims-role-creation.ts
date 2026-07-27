import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { OtimsRoleCreationService } from './otims-role-creation.service';
import {
  OTIMS_ROLE_NAME_OPTIONS,
  OTIMS_ROLE_PLACEHOLDER,
  OtimsRoleForm,
  OtimsRoleRow,
  emptyOtimsRoleForm,
} from './otims-role-creation.models';

/**
 * Parity rebuild of views/master/UnitMaster/userManagement/OTIMS-role-creation.html
 * + resources/assets/js/controller/master/OTIMSroleCreationController.js
 * (236 lines, `OTIMSroleCreationController`). State `OTIMSroleCreation` is
 * registered in `app.js:20133` with **no `resolve` block** -- no moduleId
 * gate, confirmed directly from the live route config (matches
 * `states_enriched.json`'s `has_resolve: false`).
 *
 * Legacy backend fix already merged to `main` this session,
 * `fix/otimsrolecreation-timestamp-swapped-pattern` (same swapped
 * `"dd-M-yyyy HH:ss:mm"` DAO pattern as `bloodBagType`). No rebuild-side
 * action needed.
 *
 * **Risk-tag note:** `states_enriched.json` tags this screen
 * `clinical_risk: yes` purely on an "otims" keyword match in the state
 * name -- confirmed a false positive by reading the entity directly:
 * `OtimsRoleMaster` (`m_otims_role_master`) holds only
 * `roleId`/`roleName`/`status`/`orgId`/`unitId` plus audit columns, a
 * pure org/unit-scoped role-label lookup for OT staff scheduling, no
 * patient/clinical data or FK into any clinical table. Admin-tier, not
 * clinical-tier.
 *
 * ================================================================
 * `roleName` is a HARDCODED 4-option `<select>`, not free text
 * ================================================================
 * Legacy's view (`OTIMS-role-creation.html:12-15`) is a static
 * `<select>` with exactly 4 real options ("OT Manager", "Surgeon",
 * "admin" -- lowercase, "Anaesthetist") plus a placeholder with no
 * `value` attribute -- reproduced verbatim via
 * `OTIMS_ROLE_NAME_OPTIONS`, not fetched from any API.
 *
 * ================================================================
 * ZERO client-side validation -- faithfully reproduced, not fixed
 * ================================================================
 * `save()`/`update()` (ctrl:69-125, 159-197) have no field checks of
 * any kind -- confirmed by reading the full controller (no `growl.error`
 * for a missing/placeholder selection anywhere). Because the placeholder
 * `<option>` has no `value` attribute, its bound value defaults to its
 * own text content ("Select OTIMS User Role") -- so clicking Save
 * without ever touching the dropdown submits that literal placeholder
 * string as a real role name, with no client-side gate stopping it.
 * Live-confirmed against real legacy 2026-07-27: a blank-selection Save
 * attempt reached the real save/duplicate-check endpoint with no
 * client-side block. Reproduced here identically -- no `validate()`
 * method exists on this component, unlike every sibling screen this
 * batch.
 *
 * ================================================================
 * Update's duplicate-check has NO self-exclusion -- a real,
 * near-total-blocking legacy bug, faithfully reproduced (server-side,
 * no client-side workaround added)
 * ================================================================
 * `OtimsRoleCreationMasterDaoImpl.updateOtimsRoleMaster` (dao:90-123)
 * reuses the exact same `CHECK_FOR_UNIQUE_OTIMS_ROLE` named query as
 * `saveOtimsRoleMaster`, which has no `roleId != :roleId` exclusion
 * clause. Confirmed at the source level and live: editing a row
 * WITHOUT changing its role name (e.g. clicking Edit then Update with
 * no changes) always fails as `"Already Exists."`, because the query
 * matches the row against itself. Since `roleName` is this screen's
 * only editable field, Update can only ever succeed by changing to a
 * genuinely different, not-yet-used role name -- a real, severe,
 * directly-observable bug. This is a server-side defect; the rebuild
 * makes no attempt to pre-check or work around it client-side, so it
 * reproduces identically (same class as `dietUnitMater`'s self-collision
 * bug, batch 22).
 *
 * ================================================================
 * No pagination, no search -- legacy has neither
 * ================================================================
 * `masterList()` (ctrl:38-51) fetches ALL rows for org/unit in one call,
 * no offset/limit params anywhere, and the view has no search box or
 * Show-entries control. Reproduced as a single unfiltered fetch, no
 * pager UI built.
 *
 * ================================================================
 * Confirm-modal fix -- applied here too (owner decision, 2026-07-27,
 * extending batch 22's decision to all 5 batch 23 screens)
 * ================================================================
 * `save()` has the identical `popUpFlag`-never-rearms shape as this
 * batch's other screens. Live-verified against real legacy: after one
 * confirmed Save, a second independent Save (even one later rejected as
 * a duplicate) shows no confirm modal for the rest of the page session.
 * Fixed here via `popupArmed`, rearmed on both success and failure.
 * `update()` has NO confirm-modal gate in legacy; reproduced as-is.
 */
@Component({
  selector: 'app-otims-role-creation',
  imports: [FormsModule, StatusToggle],
  templateUrl: './otims-role-creation.html',
  styleUrl: './otims-role-creation.scss',
})
export class OtimsRoleCreation {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(OtimsRoleCreationService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  readonly roleNameOptions = OTIMS_ROLE_NAME_OPTIONS;
  readonly placeholder = OTIMS_ROLE_PLACEHOLDER;

  // ---- form/edit-mode state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<OtimsRoleForm>(emptyOtimsRoleForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- FIXED here, see class doc

  // ---- list -- single unfiltered fetch, no pagination/search (see class doc) ----
  list = signal<OtimsRoleRow[]>([]);

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.organizationId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  private loadList(): void {
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
      error: () => this.loader.stop(),
    });
  }

  updateRoleName(value: string): void {
    this.form.update((f) => ({ ...f, roleName: value }));
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:64-129 --
  // NO validation of any kind, see class doc) ----
  save(): void {
    if (this.popupArmed()) {
      this.showConfirmModal.set(true);
    } else {
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.popupArmed.set(false); // mirrors PopupOkBtn's `$scope.popUpFlag = false;`
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        roleName: f.roleName,
        orgId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'RoleMaster Create Successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
            this.popupArmed.set(true); // fixed here, see class doc
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyOtimsRoleForm());
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:134-149 -- populated directly
  // from the row already in `list`, no server round-trip) ----
  edit(row: OtimsRoleRow): void {
    this.saveMode.set(false);
    this.form.set({ roleId: row.roleId, roleName: row.roleName });
  }

  // ---- update (mirrors update, ctrl:154-197 -- no confirm modal; the
  // duplicate-check self-collision bug is server-side, see class doc) ----
  update(): void {
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        roleId: f.roleId as number,
        roleName: f.roleName,
        orgId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'RoleMaster Updated Successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:203-225 -- no list
  // reload on either branch, confirmed by source read: no
  // `masterList()` call anywhere in the function) ----
  toggleStatus(row: OtimsRoleRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, applied BEFORE the API call resolves -- legacy's
    // `ng-model="info.status"` checkbox is two-way bound so the visual
    // change happens immediately regardless of the response, and there's
    // no reload on either branch. Matches the operationTheatreType/
    // cardTypeMaster precedent for this identical shape (batch 22).
    this.list.update((rows) => rows.map((r) => (r.roleId === row.roleId ? { ...r, status: nextStatus } : r)));
    this.loader.start();
    this.api
      .updateStatus({
        roleId: row.roleId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully');
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
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
