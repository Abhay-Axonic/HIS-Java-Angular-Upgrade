import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { OutSideHospitalMasterService } from './out-side-hospital-master.service';
import {
  HospitalDoctorForm,
  HospitalDoctorRow,
  OutsideHospitalForm,
  OutsideHospitalRow,
  emptyHospitalDoctorForm,
  emptyOutsideHospitalForm,
} from './out-side-hospital-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/out-side-hospital-master.html
 * + resources/assets/js/controller/master/global/outSideHospitalMasterController.js
 * (`outSideHospitalMasterController`, classic `$scope`, no `controllerAs`).
 *
 * **Step 0: this is a SELF-REGISTERED state** -- `.config($stateProvider)`
 * INSIDE the controller file itself (ctrl:515-522), not an entry in
 * config/app.js (confirmed via exhaustive grep: `state('outSideHospitalMaster'`
 * appears exactly once, here). No `.state('outSideHospitalMaster'` /
 * `outSideHospitalMasterController` duplicate/shadow registration exists
 * anywhere else in the tree -- not an `addItemPOCreation`-shaped dead-route
 * case.
 *
 * **This self-registered state has NO resolve block at all** -- confirmed
 * by reading the entire `.state(...)` call (ctrl:516-521): no `moduleId`
 * check, no `requireSuperUser`. This is a genuine, pre-existing legacy gap
 * in the same class as `billingGroupMaster`/`periodicityService`
 * (`docs/ANGULAR_REBUILD_PLAYBOOK.md` known caveat 1) -- NOT a rebuild bug,
 * and NOT something to invent a stricter check for. The route below
 * correctly omits `data.moduleId` entirely.
 *
 * No missing-`$state`-DI issue -- `$state` IS in this controller's
 * (implicit, non-array) DI list and the `$cookies`-missing guard correctly
 * redirects to login.
 *
 * **Backend confirmed real (Step 0, entry 67's methodology):** all 9
 * endpoints this screen calls resolve to genuine
 * `OutsideHospitalMasterController.java` (`/api/global`) methods with full
 * Service->DAO chains, confirmed via direct source read.
 * `public.m_outside_hospital_master` exists in this local dev DB (0 seeded
 * rows -- empty list is a data gap, not a code defect).
 * **`public.t_outside_hospital_doctor_mapper` (backing the "Doctor
 * Details" sub-feature) does NOT exist in this local dev DB** -- confirmed
 * via `pg_tables`; same recurring gap class as BR-038/039/043/046 and
 * entries 100/101/113/115-117 (the Java chain,
 * `OutsideHospitalDoctorMapperDaoImpl.java`, is genuinely real and
 * correctly implemented -- only the table is missing). Held for owner
 * approval, not fixed here; the sub-feature is still built for parity
 * (it's a real, live, wired feature in legacy) but its network calls will
 * 500 in this specific environment until the table is created. Golden
 * trace (2026-07-22) confirms only 1 real call fires on initial load
 * (`POST getOutsideHospitalMasterList` -- the count call, ctrl:70-90, is
 * commented out in legacy itself; the list response's own `object` field
 * carries the count instead, per `OutsideHospitalMasterDaoImpl.getOutsideHospitalMasterList`).
 *
 * **Real bugs/quirks found and faithfully reproduced, not fixed:**
 * - **A genuine, previously-undocumented duplicate-submit risk in the
 *   confirm-modal state machine** (ctrl:8/138-204, the shared `<modal>`
 *   directive's own `$watch`, `commonPopUpDirectives.js:32-45`): `save()`
 *   only opens the confirm modal while `popUpFlag` is `true`; clicking Ok
 *   (`PopupOkBtn`) sets `popUpFlag = false` and re-invokes `save()`, which
 *   this time fires the real POST directly. **Nothing in the save
 *   success/error handler ever resets `popUpFlag` back to `true`** --
 *   only the shared `<modal>` directive's own `$watch` does that, and only
 *   when `showModal` transitions back to `false` (i.e. when the user
 *   explicitly dismisses the modal via Cancel/×). If the modal is left
 *   open after a failed save (a real, live possibility -- e.g. the
 *   backend's own duplicate-description check,
 *   `OutsideHospitalMasterServiceImpl.saveOutsideHospitalMaster`,
 *   `ALREADY_EXIST`) and the user clicks the top-level Save button again
 *   without dismissing the modal, `popUpFlag` is still `false` and the
 *   form still holds the same (rejected) values -- so the SECOND click
 *   fires a real duplicate POST with NO confirm step at all, silently
 *   bypassing the modal entirely. Same underlying bug *class* as entry
 *   107's `roleCreation` finding #2, via a different, more specific
 *   mechanism (a leftover `false` gate flag, not a total absence of
 *   gating). Reproduced verbatim below (`popUpFlag`/`showConfirmModal`
 *   signals mirror ctrl exactly; `closeConfirmModal()` is the only path
 *   that resets `popUpFlag` back to `true`, matching the shared
 *   directive's own reset behavior).
 * - **Per entry 55's standing policy**, the confirm modal's × close button
 *   is wired to an ordinary working `closeConfirmModal()` here, NOT a
 *   reproduction of the shared `<modal>` directive's `cancel()` crash
 *   (`outSideHospitalMasterController.js` defines no `cancel` under any
 *   binding style, confirmed via grep) -- a working close is an accepted
 *   standing improvement, not a parity gap. This screen extends entry 55's
 *   known blast-radius count.
 * - **The search box (view:95, bare `ng-model="searchKeyword"`, no
 *   `ng-keyup`) is a real, working, CURRENT-PAGE-ONLY substring filter**
 *   via the table's own `filter:searchKeyword` (view:157) -- genuinely
 *   live, not dead, and (unlike `clinicMaster`'s sibling search box) has
 *   no dead copy-pasted handler attached to it at all.
 * - **Sort carets (all 4 main-table columns, plus the nested doctor
 *   table's 3) are pure static decoration** -- no `ng-click`, no backing
 *   variables (`column`/`reverse` referenced in `orderBy:column:reverse`
 *   are never assigned anywhere). Not reproduced (nothing to wire).
 * - **Saving OR toggling a doctor inside the nested "Doctor Details" modal
 *   also unconditionally resets the OUTER Outside-Hospital create/edit
 *   form and reloads the OUTER list** (`saveDoctorForOutsideHospital`
 *   ctrl:383-406, `updateStatusDoctor` ctrl:452-462 -- both call
 *   `$scope.initMaster()`/`$scope.initMasterList(...)` on success). A
 *   real, confirmed cross-feature side effect: adding/toggling a doctor in
 *   the background modal silently wipes out whatever the user had typed
 *   into the main form, and reloads the main table -- even though the two
 *   features look unrelated. Reproduced verbatim via `resetMainFormAndReload()`.
 * - **`getOutsideHospitalMasterList` sends NO `unitId`** (ctrl:57-62,
 *   commented out) -- reproduced by omitting it from the request, matching
 *   legacy's actual payload exactly (the backend's own count-via-list-size
 *   logic, `OutsideHospitalMasterDaoImpl.getOutsideHospitalMasterList`,
 *   also only filters by `organizationId`, consistent with this).
 * - **Server-side duplicate-description check exists for both save and
 *   update** (`getOutsideHospitalMasterByName`/`ByNameNotId`,
 *   `ALREADY_EXIST`) -- no independent frontend check exists, matching the
 *   pattern already documented for this codebase's `room-master` sibling
 *   (entry 105, finding 5).
 */
@Component({
  selector: 'app-out-side-hospital-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './out-side-hospital-master.html',
  styleUrl: './out-side-hospital-master.scss',
})
export class OutSideHospitalMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(OutSideHospitalMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  form = signal<OutsideHospitalForm>(emptyOutsideHospitalForm());
  saveMode = signal(true); // true = saveBtnFlag shown (create); false = updateBtnFlag shown (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state machine (mirrors $scope.popUpFlag/$scope.showModal
  // -- see class doc comment for the real duplicate-submit risk this
  // reproduces faithfully) ----
  popUpFlag = signal(true);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination + client-side search ----
  list = signal<OutsideHospitalRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Real, working search (view:157, `commonList|orderBy:column:reverse|filter:searchKeyword`)
  // -- a generic substring filter over the CURRENTLY LOADED PAGE only.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  // ---- "Doctor Details" sub-feature state (mirrors $scope.hospitalId/
  // $scope.obj/$scope.hospitalDoctorsList) ----
  doctorModalOpen = signal(false);
  activeHospitalId = signal<number | null>(null);
  doctorForm = signal<HospitalDoctorForm>(emptyHospitalDoctorForm());
  hospitalDoctorsList = signal<HospitalDoctorRow[]>([]);

  constructor() {
    this.loadList();
  }

  // mirrors initMasterList (ctrl:51-92): a single POST whose response
  // carries BOTH the page of rows (`listObject`) and the total count
  // (`object`) together -- not 2 separate calls (the count endpoint,
  // ctrl:70-90, is commented out in legacy itself).
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getOutsideHospitalMasterList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
          this.currentPage.set(1);
        }
      },
    });
  }

  // mirrors $scope.masterList via setPage(page,true): re-fetches just the
  // list at the new offset (no count refetch, no forced page reset).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getOutsideHospitalMasterList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof OutsideHospitalForm>(field: K, value: OutsideHospitalForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors siteOfAdminform.$valid -- both
  // fields carry `required`, view:38/54) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.outsideHospitalCode.trim() && f.outsideHospitalDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().outsideHospitalCode.trim();
  }
  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().outsideHospitalDesc.trim();
  }

  // ---- save / confirm-modal (mirrors save()/PopupOkBtn, ctrl:138-204 --
  // see class doc comment for the duplicate-submit risk this reproduces
  // faithfully) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:167 guard (no else branch)
    if (this.popUpFlag()) {
      this.showConfirmModal.set(true); // ctrl:169 -- open the confirm modal, do NOT save yet
      return;
    }
    this.doRealSave(); // ctrl:172-196 -- popUpFlag is false: fire the real POST
  }

  // mirrors PopupOkBtn (ctrl:138-141): sets popUpFlag false, re-invokes
  // save() -- which this time takes the real-POST branch above. Does NOT
  // close the modal itself (matches ctrl exactly -- nothing in this path
  // ever sets showConfirmModal/showModal to false).
  confirmSave(): void {
    this.popUpFlag.set(false);
    this.save();
  }

  // mirrors the shared <modal> directive's own $watch resetting
  // popUpFlag/showModal whenever `visible` transitions to false
  // (commonPopUpDirectives.js:40-44) -- the ONLY path that re-arms the
  // confirm gate. Wired to both × and Cancel, per entry 55's standing
  // policy (a working close, not legacy's cancel()-crash).
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
    this.popUpFlag.set(true);
  }

  private doRealSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveOutsideHospitalMaster({
        outsideHospitalCode: f.outsideHospitalCode,
        outsideHospitalDesc: f.outsideHospitalDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else if (res.status === 'error') {
            // ctrl:189-195 -- growl.error fires correctly here (unlike
            // clinicMaster's save/update). Form is NOT reset, list is NOT
            // reloaded, and popUpFlag stays false -- see class doc comment.
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:216-244 -- uses growl.error on
  // failure, NOT a native alert, unlike clinicMaster's editClinic) ----
  edit(outsideHospitalId: number): void {
    this.saveMode.set(false);
    this.api.getOutsideHospitalMasterById(outsideHospitalId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          this.form.set({
            outsideHospitalId: d.outsideHospitalId,
            outsideHospitalCode: d.outsideHospitalCode,
            outsideHospitalDesc: d.outsideHospitalDesc,
          });
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- update (mirrors update(), ctrl:247-295 -- no confirm modal,
  // unlike save) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:254 guard (no else branch)
    const f = this.form();
    this.api
      .updateOutsideHospitalMaster({
        outsideHospitalId: f.outsideHospitalId as number,
        outsideHospitalCode: f.outsideHospitalCode,
        outsideHospitalDesc: f.outsideHospitalDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
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
    this.form.set(emptyOutsideHospitalForm());
    this.saveMode.set(true);
    this.formSubmitted.set(false);
  }

  // ---- status toggle (mirrors updateStatus, ctrl:297-333 -- real create
  // growl on both branches, no revert, always reloads the list on success
  // only) ----
  toggleStatus(row: OutsideHospitalRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.outsideHospitalId === row.outsideHospitalId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateOutsideHospitalMasterStatus({
        outsideHospitalId: row.outsideHospitalId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm(); // ctrl:319 -- $scope.initMaster()
            this.loadList(); // ctrl:320
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
            // ctrl:321-328 -- no reload/reset on failure, and no revert of
            // the optimistic flip above.
          }
        },
      });
  }

  // ---- "Doctor Details" sub-feature (mirrors ctrl:335-511 -- see class
  // doc comment for the outer-form-reset side effect) ----

  openDoctorModal(outsideHospitalId: number): void {
    this.activeHospitalId.set(outsideHospitalId);
    this.doctorForm.set(emptyHospitalDoctorForm());
    this.doctorModalOpen.set(true);
    this.loadHospitalDoctorsList(outsideHospitalId);
  }

  closeDoctorModal(): void {
    this.doctorModalOpen.set(false);
  }

  private loadHospitalDoctorsList(outsideHospitalId: number): void {
    this.api.getHospitalDoctorsList(this.organizationId, this.unitId, outsideHospitalId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.hospitalDoctorsList.set(res.listObject ?? []);
      },
    });
  }

  updateDoctorField<K extends keyof HospitalDoctorForm>(field: K, value: HospitalDoctorForm[K]): void {
    this.doctorForm.update((f) => ({ ...f, [field]: value }));
  }

  // mirrors saveDoctorForOutsideHospital (ctrl:348-412) -- single endpoint
  // handles both add and edit depending on whether outsideHosDocId is set
  // (OutsideHospitalDoctorMapperDaoImpl branches on this itself).
  saveDoctor(): void {
    const name = this.doctorForm().hospDoctorName.trim();
    if (!name) {
      this.notify.error('Enter doctor name'); // ctrl:351-357
      return;
    }
    const hospitalId = this.activeHospitalId();
    if (hospitalId == null) return;
    const now = formatDateTime(new Date());
    this.api
      .saveOutsideHospitalDoctorMapper({
        outsideHosDocId: this.doctorForm().outsideHosDocId,
        outsideHospitalId: hospitalId,
        hospDoctorName: name,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.loadHospitalDoctorsList(hospitalId);
            this.doctorForm.set(emptyHospitalDoctorForm());
            this.resetMainFormAndReload(); // ctrl:394-395 -- real, confirmed side effect
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
            this.doctorForm.set(emptyHospitalDoctorForm());
          }
        },
      });
  }

  // mirrors showUpdateBtnDoctor (ctrl:478-511) -- growl.error on failure,
  // matching this screen's own main edit() (not a native alert).
  editDoctor(outsideHosDocId: number): void {
    const hospitalId = this.activeHospitalId();
    if (hospitalId == null) return;
    this.api.getOutsideHospitalDoctorMapperById(outsideHosDocId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          this.doctorForm.set({ outsideHosDocId: d.outsideHosDocId, hospDoctorName: d.hospDoctorName });
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // mirrors updateStatusDoctor (ctrl:434-475) -- optimistic flip, no
  // revert on failure; success also triggers the outer-form side effect
  // (see class doc comment) but does NOT itself refresh the doctor list.
  toggleDoctorStatus(row: HospitalDoctorRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.hospitalDoctorsList.update((rows) =>
      rows.map((r) => (r.outsideHosDocId === row.outsideHosDocId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateOutsideHospitalDoctorMapperStatus({
        outsideHosDocId: row.outsideHosDocId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetMainFormAndReload(); // ctrl:461-462 -- real, confirmed side effect
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  // ctrl:394-395/461-462 -- a real, confirmed cross-feature side effect:
  // success inside the nested Doctor Details modal also resets the OUTER
  // create/edit form and reloads the OUTER list, even though the two
  // features look unrelated. See class doc comment.
  private resetMainFormAndReload(): void {
    this.resetForm();
    this.loadList();
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:151-152/250-251/299-300/360-361/436-437. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
