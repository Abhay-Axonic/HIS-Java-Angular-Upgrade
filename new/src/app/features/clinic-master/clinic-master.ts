import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ClinicMasterService } from './clinic-master.service';
import { ClinicMasterForm, ClinicMasterRow, FloorOption, WingOption, emptyClinicMasterForm } from './clinic-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/clinic-master.html +
 * resources/assets/js/controller/master/unit/clinicMasterController.js
 * (`clinicMasterController`, classic `$scope`, no `controllerAs`). Legacy
 * state `clinicMaster` (config/app.js:8164-8190, resolve block read live,
 * not guessed), moduleId 504, no `requireSuperUser`. No missing-`$state`-DI
 * issue -- `$state` IS in this controller's DI list and the
 * `$cookies`-missing guard correctly redirects to login.
 *
 * **Backend confirmed real this session (Step 0, entry 67's methodology):**
 * all 6 of `ClinicMasterController.java`'s endpoints
 * (save/update/list/count/byId/updateStatus) and both dropdown-dependency
 * endpoints (`FloorMasterController.java`/`WingMasterController.java`, both
 * `/adt`) have full, genuine Controller->Service->DAO chains -- confirmed
 * via direct source read, not assumed. `m_clinic_master` exists in this
 * local dev DB (0 seeded rows -- empty list is a data gap, not a code
 * defect); `adt.m_floor_master` has 1 seeded row, `adt.m_wing_master` has 0.
 * Golden-trace capture (2026-07-22) fired exactly the 4 expected calls on
 * load with no console/network anomaly.
 *
 * **Real bugs/quirks found and faithfully reproduced, not fixed:**
 * - **`saveClinicMaster()`/`updateClinicMaster()` both have the identical
 *   copy-paste bug** (ctrl:171, ctrl:236): the failure branch checks
 *   `response.data.status == "success"` a SECOND time instead of
 *   `"error"` -- so on ANY failed save/update (including the real,
 *   backend-confirmed duplicate-clinic-description check,
 *   `ClinicMasterServiceImpl.saveClinicMaster`/`updateClinicMaster`,
 *   `ALREADY_EXIST`), no error message is ever shown to the user AT ALL.
 *   Worse: the form-reset-and-reload (`$scope.initClinicMaster()` +
 *   `$scope.getClinicMasterList(...)`, ctrl:179-180/244-245) runs
 *   UNCONDITIONALLY after the if/else, on both branches -- so a failed
 *   save/update silently clears the user's typed input and reloads the
 *   list, with zero indication anything went wrong. Reproduced verbatim:
 *   `confirmSave()`/`update()` below never surface an error notification on
 *   a non-success response, and always reset the form + reload the list
 *   regardless of outcome.
 * - **`updatedClinicStatus` (the status-toggle endpoint, ctrl:251-277) has
 *   NO revert-on-failure and NO list reload on either branch** -- the
 *   checkbox's own two-way `ng-model`/`ng-true-value`/`ng-false-value`
 *   flips optimistically before the request even fires, and legacy never
 *   corrects it back on a failed response (unlike sibling
 *   `district-master`, whose `updateStatus` unconditionally reloads the
 *   list+count on both branches, self-correcting a failed toggle). Unlike
 *   save/update above, this endpoint's growl IS correctly wired
 *   (`'error'` checked correctly, ctrl:265-272) -- a failed toggle DOES
 *   show a real error toast, it just never visually reverts. Reproduced via
 *   `toggleStatus()` below: optimistic flip, real error growl on failure,
 *   no revert, no reload, matching ctrl exactly.
 * - **The view's `<modal visible="showModal">` (clinic-master.html:249) is
 *   completely dead/orphaned in legacy** -- `showModal` is never assigned
 *   anywhere in this controller (confirmed via exhaustive grep), unlike
 *   sibling `outSideHospitalMaster`'s own `<modal>`, which IS genuinely
 *   opened via `popUpFlag`/`PopupOkBtn`. This is NOT an instance of entry
 *   55's shared-modal `cancel()`-crash population (that population is
 *   screens where the modal is reachable) -- it's a different, narrower
 *   shape: a modal element that can never open at all, same class as
 *   `holidayCalendarMasterUnit`'s orphaned "Add Holiday" modal (entry 111).
 *   Correctly omitted from this rebuild -- there is nothing to reproduce.
 * - **Save/Update have NO confirm-modal step at all** -- both POST
 *   immediately once the form validates (no `popUpFlag`/`PopupOkBtn`
 *   gating anywhere in this controller).
 * - **The Floor dropdown silently caps at 10 floors and includes INACTIVE
 *   floors** -- see `FloorOption`'s doc comment in `.models.ts`. The Wing
 *   dropdown is active-only and uncapped. Both reproduced via calling the
 *   identical endpoints with the identical (missing) parameters legacy
 *   itself sends.
 * - **The search box's `ng-keyup="serachTestmaster(...)"` (view:137) calls
 *   a function that only exists in an unrelated LIS controller** -- the
 *   same copy-paste-into-19+-Master-screens pattern already documented for
 *   this codebase's `modality-master`/`generic-master` siblings (entry 95).
 *   Harmless: the table's own `filter:searchKeyword` (view:213) performs a
 *   real, working, CURRENT-PAGE-ONLY substring filter independently.
 *   Reproduced via `displayedRows()` below; the dead handler itself has
 *   nothing to port (there's no real behavior on the other end of it).
 * - **Sort carets (all 6 columns) are pure static decoration** -- no
 *   `ng-click`, no backing variables. Not reproduced (nothing to wire).
 * - **The Edit pencil's `ng-class="{disabled: ...}"` (view:222) is
 *   cosmetic-only** -- the click handler stays live regardless of the
 *   row's status, matching this codebase's other "cosmetic disable"
 *   siblings (entry 95). Reproduced: `edit()` is never gated on status.
 * - **`updateClinicMasterStatus`'s backend update (`UPDATE_CLINIC_STATUS_BY_ID`)
 *   is a bare `SET status = :status` -- it does NOT record `updatedBy`/
 *   `updatedDate` at all**, unlike almost every sibling master's status
 *   endpoint. Backend-only, not observable from this screen's own UI
 *   (which never displays an updated-by/updated-date column) -- noted for
 *   completeness, not something the Angular payload needs to compensate
 *   for (there's nothing for the frontend to send that the backend query
 *   would use anyway).
 * - **Backend-only, not rebuild-actionable:** `updateClinicMaster()`
 *   overwrites BOTH `cerated_date` (sic) and `updated_date` to "now" before
 *   sending (ctrl:221-222), and the DAO genuinely persists whatever
 *   `cerated_date` it's given -- every update silently loses the original
 *   creation timestamp. Also, `ClinicMasterDaoImpl.save/updateClinicMaster`
 *   both hardcode `created_by` to the literal `1` server-side, ignoring
 *   whatever `created_by` the request body actually carries. Neither is
 *   visible anywhere in this screen's own UI (no created/updated-by/date
 *   column exists in the view) -- reproduced only in the sense that the
 *   Angular payload sends the exact same `cerated_date`/`updated_date`
 *   values legacy does; the backend-side overwrite itself is out of this
 *   rebuild's scope to change.
 */
@Component({
  selector: 'app-clinic-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './clinic-master.html',
  styleUrl: './clinic-master.scss',
})
export class ClinicMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ClinicMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown option lists (mirrors $scope.floorList/$scope.wingList) ----
  floorOptions = signal<FloorOption[]>([]);
  wingOptions = signal<WingOption[]>([]);

  // ---- form state (mirrors $scope.clinic) ----
  form = signal<ClinicMasterForm>(emptyClinicMasterForm());
  saveMode = signal(true); // true while saveBtnFlag is shown (create), false once editing
  formSubmitted = signal(false);

  // ---- list + server-side pagination + client-side search ----
  list = signal<ClinicMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Real, working search (view:213, `clinicMasterList|filter:searchKeyword`)
  // -- a generic substring filter over the CURRENTLY LOADED PAGE only, not
  // a real backend search call.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadFloorList();
    this.loadWingList();
    this.loadListAndCount();
  }

  // ctrl:57-73.
  private loadFloorList(): void {
    this.api.getFloorMasterList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.floorOptions.set(res.listObject ?? []);
      },
    });
  }

  // ctrl:77-91.
  private loadWingList(): void {
    this.api.getWingMasterListByOrgAndUnit(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.wingOptions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors initClinicMasterList (ctrl:99-116): `promiseFactory.setPromises`
  // is a `$q.all([list, count])` -- both HTTP calls fire together, and
  // BOTH must resolve before either signal is set, matching legacy's
  // atomic list+count update exactly (not 2 independently-resolving
  // subscribes, which could momentarily show one updated ahead of the
  // other).
  private loadListAndCount(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    forkJoin({
      list: this.api.getClinicMasterList(this.organizationId, this.unitId, offset, this.pageSize()),
      count: this.api.getClinicMasterCount(this.organizationId, this.unitId),
    }).subscribe(({ list, count }) => {
      this.list.set(list.listObject ?? []);
      if (typeof count.object === 'number') this.totalCount.set(count.object);
      this.currentPage.set(1);
    });
  }

  // mirrors getClinicMasterList via setPage(page,true): re-fetches just
  // the list at the new offset, no count refetch, no forced page reset.
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getClinicMasterList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof ClinicMasterForm>(field: K, value: ClinicMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors clinicMasterform.$valid -- all
  // 4 fields carry `required`, view:47/63/79/96) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.floorId.trim() && f.wingId.trim() && f.clinicCode.trim() && f.clinicDescription.trim());
  }

  isFloorInvalid(): boolean {
    return this.formSubmitted() && !this.form().floorId.trim();
  }
  isWingInvalid(): boolean {
    return this.formSubmitted() && !this.form().wingId.trim();
  }
  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().clinicCode.trim();
  }
  isDescriptionInvalid(): boolean {
    return this.formSubmitted() && !this.form().clinicDescription.trim();
  }

  // ---- save (mirrors saveClinicMaster, ctrl:153-182 -- no confirm modal) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:159 guard (no else branch)
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveClinicMaster({
        clinicCode: f.clinicCode,
        clinicDescription: f.clinicDescription,
        floorId: Number(f.floorId),
        wingId: Number(f.wingId),
        created_by: this.userId,
        updated_by: this.userId,
        cerated_date: now,
        updated_date: now,
        organizationId: this.organizationId,
        unitId: this.unitId,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          // ctrl:162-178's copy-paste bug: the "error" branch actually
          // checks "success" a second time, so it can NEVER fire -- no
          // error notification exists on a failed save (e.g. a duplicate
          // clinic description), by faithful reproduction. Only show the
          // success toast on an actual success.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          }
          // ctrl:179-180 -- runs UNCONDITIONALLY after the if/else, on both
          // branches: resets the form and reloads the list even on failure.
          this.resetForm();
          this.loadListAndCount();
        },
      });
  }

  // ---- edit (mirrors editClinic, ctrl:185-215) ----
  edit(clinicMasterId: number): void {
    this.saveMode.set(false);
    this.api.getClinicMasterById(clinicMasterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.object) {
          window.alert('Error In Fetching Data'); // ctrl:209 -- a real native alert, not growl
          return;
        }
        const d = res.object;
        this.form.set({
          clinicMasterId: d.clinicMasterId,
          floorId: String(d.floorId),
          wingId: String(d.wingId),
          clinicCode: d.clinicCode,
          clinicDescription: d.clinicDescription,
          // ctrl:203 -- carried through unmodified to update() below; see
          // ClinicMasterForm's doc comment for why this must not be
          // hardcoded.
          status: d.status,
        });
      },
    });
  }

  // ---- update (mirrors updateClinicMaster, ctrl:218-248 -- no confirm
  // modal, same copy-paste error-swallowing bug as save) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:224 guard (no else branch)
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .updateClinicMaster({
        clinicMasterId: f.clinicMasterId as number,
        clinicCode: f.clinicCode,
        clinicDescription: f.clinicDescription,
        floorId: Number(f.floorId),
        wingId: Number(f.wingId),
        created_by: this.userId,
        updated_by: this.userId,
        // ctrl:221-222 -- legacy overwrites BOTH dates to "now" on every
        // update, silently losing the original creation timestamp
        // server-side (see class doc comment). Reproduced verbatim.
        cerated_date: now,
        updated_date: now,
        organizationId: this.organizationId,
        unitId: this.unitId,
        // ctrl:220-247 -- sent as fetched by edit(), NOT hardcoded (see
        // ClinicMasterForm's doc comment).
        status: f.status,
      })
      .subscribe({
        next: (res) => {
          // Same dead error-branch bug as save() -- see comment there.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          }
          this.resetForm();
          this.loadListAndCount();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyClinicMasterForm());
    this.saveMode.set(true);
    this.formSubmitted.set(false);
  }

  // ---- status toggle (mirrors updateClinicStatus, ctrl:251-277 -- see
  // class doc comment: real error growl, but NO revert and NO list reload
  // on either branch) ----
  toggleStatus(row: ClinicMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.clinicMasterId === row.clinicMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api.updateClinicStatus(row.clinicMasterId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
        // ctrl:251-277 -- no reload/reset on either branch, and no revert
        // of the optimistic flip above even on failure. Reproduced as-is.
      },
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:31-33/151-152/220-221. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
