import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RoleCreationService } from './role-creation.service';
import {
  DashboardOption,
  RoleForm,
  RoleRow,
  ROLE_TYPES,
  VaccinationOption,
  emptyRoleForm,
} from './role-creation.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/userManagement/role-creation.html +
 * resources/assets/js/controller/master/roleCreationMasterController.js
 * (`roleCreationMasterController`, plain `$scope`, NO `controllerAs` --
 * confirmed 0 `vm.*` bindings in the view, all plain `common.*`/bare
 * bindings). Wired via app.js's central `$stateProvider`
 * (app.js:16141-16168), moduleId 672, no `requireSuperUser`. `$state` IS in
 * this controller's own DI list (ctrl:7) and the missing-cookie guard
 * (ctrl:26-29) returns before anything else runs -- NOT an instance of the
 * recurring missing-`$state`-DI crash bug.
 *
 * **Step 0: confirmed genuinely distinct from this batch's sibling
 * `role-management` (Phase 4 batch 9, moduleId 675) and the further sibling
 * `userRoleManagement` (moduleId 674, resolve block immediately preceding
 * this one in app.js) -- three separate real states, not a dead/duplicate
 * pair.** `role-management` only lists existing roles (status toggle + a
 * real "View User" mapping modal); it has NO create-role form at all --
 * confirmed by reading `role-management.html` directly, no `roleName`/
 * `roleTypeId`/`dashboardId`/save()/update() anywhere in it.
 * `role-creation` is genuinely the screen that creates new role TYPES with
 * a real form. Own endpoints on the same shared
 * `UserRoleMasterController.java` confirmed genuinely different methods
 * from `role-management`'s (`saveUserRoleMaster`/`updateUserRoleMaster`/
 * `getUserRoleMasterList`/`getUserRoleMasterById`/`getUserRoleMasterCount`
 * here vs. `getActiveUserRoleMasterList`/`getUserListByRoleId`/
 * `updateUserRoleMapperStatus` there) -- confirmed by direct comparison of
 * both controllers' source.
 *
 * **Backend confirmed real end to end** for all 8 endpoints this screen
 * calls -- see `.service.ts`'s header comment.
 *
 * **Uses the shared `<modal visible="showModal">` directive**
 * (`role-creation.html:373`) -- per `docs/ANGULAR_PENDING_FINDINGS.md`
 * entry 55's already-resolved standing policy, the directive's hardcoded
 * close-button `ng-click="cancel();stopLoader();"` crashes in legacy today
 * (`roleCreationMasterController.js` never defines `$scope.cancel`,
 * confirmed via full-file grep -- one more, previously-uncounted instance
 * of this shared-directive crash beyond entry 55/63's tally). `closeConfirmModal()`
 * below is an ordinary, working close -- an accepted standing improvement,
 * not a parity gap.
 *
 * **A real, previously-undocumented finding: the confirm modal does NOT
 * gate the save, and its own Ok button can duplicate-submit (new finding,
 * this batch).** Unlike this codebase's other confirm-modal screens (e.g.
 * `weekend-master`, where `save()` only opens the modal and the REAL POST
 * only fires from the modal's own `PopupOkBtn()`), `roleCreationMasterController.js`'s
 * `save()` (ctrl:391-463) performs its full validation AND fires the real
 * `saveUserRoleMaster` POST unconditionally, in the SAME call that also
 * flips `$scope.showModal` -- confirmed by direct read: there is no
 * `if(popUpFlag)`-style gate anywhere in `save()`, unlike the gated pattern
 * this codebase's other screens use. The confirm dialog therefore appears
 * WHILE (or after) the real save request is already in flight, not before
 * it. Its own Ok button (`PopupOkBtn`, ctrl:383-386) sets `popUpFlag = false`
 * and calls `save()` AGAIN -- since `save()` has no gate on `popUpFlag`
 * either, a user who clicks Ok on the now-showing "Are you sure..." dialog
 * fires a SECOND, genuinely duplicate `saveUserRoleMaster` POST with the
 * same form data (only prevented in practice if the first request's
 * success handler has already reset the form via `initMaster()` before the
 * second click lands -- a real race, not a guarantee). Reproduced verbatim
 * below: `onSaveClick()` validates and POSTs immediately (same as legacy),
 * `showConfirmModal` opens alongside it, and the modal's Ok
 * (`confirmSaveAgain()`) re-invokes the exact same save path.
 *
 * **A second real, previously-undocumented finding: `updateStatus` reuses
 * the SAME shared form object the create/edit form is built from, and can
 * silently corrupt an in-progress edit (new finding, this batch).**
 * `$scope.updateStatus(Id, status)` (ctrl:598-632) writes
 * `updatedBy`/`updatedDate`/`status`/`roleId` directly onto `$scope.common`
 * -- the SAME object the top-of-page form's `roleName`/`roleTypeId`/etc.
 * inputs are bound to. If a user is mid-edit of role A (`common.roleId` =
 * A) and clicks the status toggle on an unrelated row B in the table below,
 * `common.roleId` silently becomes B while `common.roleName`/`roleTypeId`
 * (still A's, still visible/unchanged on screen) are left alone -- a
 * subsequent Update click would then PATCH role B's identity with role A's
 * still-displayed name/type data. Reproduced verbatim below: `toggleStatus()`
 * overwrites `form().roleId` (the one field of the shared object that
 * actually determines which record a later Update targets), leaving every
 * other visible form field untouched, exactly matching legacy's own
 * partial-overwrite shape.
 *
 * **The "View Role" info-circle modal (`#vaccination`, view:235-373) is
 * real copy-paste residue, not a working feature, reproduced as an equally
 * broken placeholder:**
 * - Its 4th field is mislabeled "Is Counter Applicable" but its
 *   `ng-model`/`name` are BOTH still `common.roleName` (view:311-319,
 *   an exact copy of the FIRST field, never updated to the field it claims
 *   to be) -- reproduced by rendering the same duplicate-`roleName`-bound
 *   text input under that mislabel, not "fixed" into a real
 *   `isCounterApplicable` text field legacy never actually built here.
 * - Its own duplicate `isCounterApplicable` checkboxes (view:326-338, two
 *   IDENTICAL blocks, both bound to the same `common.isCounterApplicable`,
 *   same duplicate `id="checkMenu"`) are reproduced as two checkboxes bound
 *   to the same field.
 * - Its embedded "Role"/"Role" 2-column table (view:340-352) is fully
 *   static dead markup (two empty `<td>`s, no binding at all) -- reproduced
 *   as static empty cells.
 * - **Opening it ALSO silently switches the main top-of-page form into
 *   Update mode for that role**, not just the modal's own fields --
 *   `viewFunction()` (ctrl:510-540) sets `saveBtnFlag = false;
 *   updateBtnFlag = true;` and populates the SAME shared `$scope.common`
 *   the top form's inputs are bound to, exactly like `showUpdateBtn()`
 *   does. There is no separate, modal-scoped copy of this state in legacy.
 *   Closing the modal afterward leaves the main screen showing "Update"
 *   (not "Save") pre-filled with the viewed role's data, as if the user had
 *   clicked Edit. Reproduced verbatim: `openView()` below also flips
 *   `saveMode` and writes the same shared `form` signal `edit()` uses.
 * - **Its own nested Save button (view:357-364, `ng-click="save()"`) is a
 *   genuinely live, working call to the SAME create-path `save()` as the
 *   main screen** -- since `viewFunction()` (ctrl:510-540) populates
 *   `common` from the clicked row's real data (including `roleId`) but
 *   `save()`'s create branch never checks whether `common.roleId` is
 *   already set, clicking this button inside what looks like a read-only
 *   "view" modal would fire a genuine `saveUserRoleMaster` CREATE call
 *   using the currently-viewed role's data -- a real risk of creating an
 *   unwanted duplicate role. Reproduced verbatim: this modal's own Save
 *   button below calls the identical `onSaveClick()`.
 *
 * **The list's search/sort machinery is entirely dead** -- confirmed via
 * full-view read: `commonList|orderBy:column:reverse|filter:searchKeyword`
 * (view:189) references `column`/`reverse`/`searchKeyword`, but the view
 * has NO search input and NO sortable column header anywhere that assigns
 * any of the three. Not reproduced as a search/sort UI at all (there is no
 * dead input to leave inert -- there was never a live one to begin with).
 *
 * **The "Vaccination" multi-select** (`ng-dropdown-multiselect`, a 3rd-party
 * AngularJS widget) is rebuilt here as a plain multi-select `<select
 * multiple>` -- a disclosed simplification of the widget itself (checkbox
 * dropdown vs. native multi-select), not a behavior change to the
 * underlying real data flow (`getVaccination`, required-if-Y validation,
 * `vaccination: number[]` payload field) it drives.
 */
@Component({
  selector: 'app-role-creation',
  imports: [FormsModule, StatusToggle],
  templateUrl: './role-creation.html',
  styleUrl: './role-creation.scss',
})
export class RoleCreation {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RoleCreationService);

  private readonly organisationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  readonly roleTypes = ROLE_TYPES;

  // ---- list + server-side pagination (initMasterList/PagerService) ----
  list = signal<RoleRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ---- top-of-page create/edit form (`$scope.common`) ----
  form = signal<RoleForm>(emptyRoleForm());
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  formSubmitted = signal(false);

  // ---- vaccination sub-feature ----
  dashboardList = signal<DashboardOption[]>([]);
  vaccinationOptions = signal<VaccinationOption[]>([]);
  selectedVaccinationIds = signal<number[]>([]);
  vaccinationListVisible = computed(() => this.form().prophylacticVaccinationApplication === 'Y');

  // ---- confirm-save modal (see class doc comment: does not gate save) ----
  showConfirmModal = signal(false);

  // ---- "View Role" (#vaccination) modal ----
  showViewModal = signal(false);

  constructor() {
    this.loadList();
    this.api.getDashboardList({ organizationId: this.organisationId }).subscribe({
      next: (res) => {
        if (res.status === 'success') this.dashboardList.set(res.listObject ?? []);
      },
    });
    this.api.getVaccinationList({ organisationId: this.organisationId, unitId: this.unitId }).subscribe({
      next: (res) => {
        if (res.status === 'success') this.vaccinationOptions.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api
      .getRoleList({ organisationId: this.organisationId, offset, noOfRecordsPerPage: this.pageSize(), unitId: this.unitId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            const rows = (res.listObject ?? []).map((r) => ({ ...r, roleType: roleTypeLabel(r.roleTypeId) }));
            this.list.set(rows);
          }
        },
      });
    this.api.getRoleCount({ organisationId: this.organisationId, unitId: this.unitId }).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateFormField<K extends keyof RoleForm>(field: K, value: RoleForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  onVaccinationFlagChange(value: 'Y' | 'N'): void {
    this.updateFormField('prophylacticVaccinationApplication', value);
  }

  // ---- Save click (mirrors save(), ctrl:391-463) -- validates AND fires
  // the real POST immediately, same call also opens the confirm modal --
  // see class doc comment finding. ----
  onSaveClick(): void {
    this.formSubmitted.set(true);
    const f = this.form();
    if (!f.roleName || !f.roleTypeId) return; // commonform.$valid gate (HTML5 `required`)

    if (f.prophylacticVaccinationApplication === 'Y' && this.selectedVaccinationIds().length === 0) {
      this.notify.warning('Please select Vaccination..!!');
      return;
    }

    this.showConfirmModal.set(true);
    const now = formatDateTime(new Date());
    this.api
      .saveRole({
        roleName: f.roleName,
        // ctrl:409's `common.roleTypeId != "" ? common.roleTypeId : 3` default
        // is unreachable dead code here too: the guard above (mirroring
        // commonform.$valid's HTML5 `required` on this field) already
        // guarantees `f.roleTypeId` is non-empty by this point, exactly
        // like legacy's own `required` attribute makes it dead there.
        roleTypeId: f.roleTypeId as number,
        dashboardId: f.dashboardId,
        isCounterApplicable: f.isCounterApplicable,
        prophylacticVaccinationApplication: f.prophylacticVaccinationApplication,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
        status: 'A',
        unitId: this.unitId,
        organisationId: this.organisationId,
        vaccination: f.prophylacticVaccinationApplication === 'Y' ? this.selectedVaccinationIds() : [],
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Role Creation Successfully');
            this.resetForm();
            this.loadList();
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  /** Per `docs/ANGULAR_PENDING_FINDINGS.md` entry 55's standing policy: an
   * ordinary working close here (legacy's own `cancel()` crashes) -- an
   * accepted standing improvement, not a parity gap. */
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  /** PopupOkBtn(), ctrl:383-386 -- re-invokes the SAME save path. Faithful
   * reproduction of the duplicate-submission risk described in the class
   * doc comment: clicking Ok here fires a SECOND real save POST with
   * whatever is still in `form()` at the time. */
  confirmSaveAgain(): void {
    this.showConfirmModal.set(false);
    this.onSaveClick();
  }

  // ---- Update click (mirrors update(), ctrl:543-596) -- no confirm modal,
  // no duplicate-submit risk (single call, no modal re-entry). ----
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    const f = this.form();
    if (!f.roleName || !f.roleTypeId || !f.roleId) return;
    this.api
      .updateRole({
        roleId: f.roleId,
        roleName: f.roleName,
        roleTypeId: f.roleTypeId,
        dashboardId: f.dashboardId,
        isCounterApplicable: f.isCounterApplicable,
        prophylacticVaccinationApplication: f.prophylacticVaccinationApplication,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        unitId: this.unitId,
        organisationId: this.organisationId,
        vaccination: f.prophylacticVaccinationApplication === 'Y' ? this.selectedVaccinationIds() : [],
        vaccinations: [],
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyRoleForm());
    this.selectedVaccinationIds.set([]);
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- Edit (showUpdateBtn, ctrl:465-508) ----
  edit(roleId: number): void {
    this.saveMode.set(false);
    this.api.getRoleById(roleId, this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const detail = res.listObject?.[0];
          if (!detail) return;
          this.form.set({
            roleId: detail.roleId,
            roleName: detail.roleName,
            roleTypeId: detail.roleTypeId ?? '',
            dashboardId: detail.dashboardId ?? '',
            isCounterApplicable: detail.isCounterApplicable,
            prophylacticVaccinationApplication: detail.prophylacticVaccinationApplication,
          });
          this.selectedVaccinationIds.set(
            detail.prophylacticVaccinationApplication === 'Y' ? (detail.vaccinations ?? []).map((v) => v.vaccinationId) : [],
          );
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- View Role (info-circle icon, viewFunction(), ctrl:510-540) -- see
  // class doc comment: opens a genuinely broken copy-paste modal, whose own
  // Save button is live and can create a duplicate role. ----
  openView(roleId: number): void {
    this.saveMode.set(false);
    this.api.getRoleById(roleId, this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const detail = res.listObject?.[0];
          if (!detail) return;
          this.form.set({
            roleId: detail.roleId,
            roleName: detail.roleName,
            roleTypeId: detail.roleTypeId ?? '',
            dashboardId: detail.dashboardId ?? '',
            isCounterApplicable: detail.isCounterApplicable,
            prophylacticVaccinationApplication: detail.prophylacticVaccinationApplication,
          });
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
    this.showViewModal.set(true);
  }

  closeViewModal(): void {
    this.showViewModal.set(false);
  }

  // ---- status toggle (updateStatus, ctrl:598-632) -- see class doc
  // comment finding: silently overwrites the shared form's roleId. ----
  toggleStatus(row: RoleRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.roleId === row.roleId ? { ...r, status: nextStatus } : r)));
    // Faithful reproduction of the shared-object corruption bug described
    // in the class doc comment: this overwrites the in-progress
    // create/edit form's roleId, exactly like legacy's `$scope.common.roleId = Id`.
    this.form.update((f) => ({ ...f, roleId: row.roleId }));
    this.api
      .updateRoleStatus({ roleId: row.roleId, status: nextStatus, updatedBy: this.userId, updatedDate: formatDateTime(new Date()) })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** ctrl:195-247's `angular.forEach` mapping block, client-side. */
function roleTypeLabel(roleTypeId: number | undefined): string | undefined {
  return ROLE_TYPES.find((t) => t.roleTypeId === roleTypeId)?.roleType;
}

/** `moment(date).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:397-398/548-549. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
