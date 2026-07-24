import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { IncidentCategoryMasterService } from './incident-category-master.service';
import {
  ActiveStatus,
  IncidentCategoryForm,
  IncidentCategoryRow,
  emptyIncidentCategoryForm,
} from './incident-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/incidentReporting/incident-category-master.html
 * (133 lines) +
 * resources/assets/js/controller/master/incidentCategoryMasterController.js
 * (365 lines, `incidentCategoryMasterController`, no `controllerAs`). State
 * `incidentCategoryMaster` (ctrl:353-360, `url: '/incidentCategoryMaster'`)
 * is SELF-REGISTERED entirely inside this one controller file via its own
 * `.config($stateProvider)` call -- confirmed NOT present anywhere in
 * `resources/assets/js/config/app.js` (grepped, zero matches) -- and has NO
 * `resolve` block at all, so there is no moduleId gate for this screen.
 * `data.moduleId` is therefore deliberately omitted on this route below,
 * matching this codebase's established convention for gate-less legacy
 * states (e.g. `billingGroupMaster`, `periodicityService`). Sidebar entry
 * IS real and reachable: `views/common/new-side-bar.html:5205`,
 * `ng-if="checkAccess(989)"`, `href="#/incidentCategoryMaster"`, and
 * `adminPriviledges.json` id 989 ("Category Master", `actionLink:
 * "#/incidentCategoryMaster"`, parentId 988 "Incident Reporting") confirms
 * it's a genuine, non-decoy menu link, not a dead route.
 *
 * Backend confirmed real end-to-end: `IncidentCategoryMasterController`
 * (`@RequestMapping("/api/global/unit")`) -> `IncidentCategoryMasterServiceImpl`
 * -> `IncidentCategoryMasterDaoImpl` -> `IncidentCategoryMaster` entity
 * (`m_incident_category_master`). PURE-LOOKUP per prior research: this
 * category is only used to classify an already-filed incident report after
 * the fact, not to gate any live clinical decision.
 *
 * ================================================================
 * Two-click confirm on Save, none on Update -- FIXED, diverges from legacy
 * (codebase-wide sweep + owner decision, 2026-07-24, see
 * docs/ANGULAR_PENDING_FINDINGS.md entry 152)
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:13), gates whether clicking Save
 * opens the confirm modal (`showModal = !showModal`, ctrl:189-191) or
 * actually POSTs (ctrl:192-212), and is flipped to `false` exactly once by
 * `PopupOkBtn` (ctrl:142-145). Reading `$scope.initMaster()` (ctrl:38-49) as
 * never referencing `popUpFlag` at all (grepped: exactly 4 occurrences in
 * the whole file -- the initial `= true` at ctrl:13, the `= false` in
 * `PopupOkBtn` at ctrl:143, and the two reads at ctrl:150/189 -- no third
 * assignment anywhere) suggested `popUpFlag` would be disarmed PERMANENTLY
 * by the very first confirm click, for the rest of that page load, unlike
 * sibling screens whose `init()` explicitly re-arms it on a successful
 * save. Confirmed live against real, unmodified legacy on sibling screens
 * sharing the identical shared `<modal>` directive (duplicate-then-retry
 * on `cardTypeMaster`/`raceMaster`) that this does NOT happen: the
 * directive's own hardcoded Ok-button handler re-arms the gate on every
 * click, unconditionally, regardless of what any individual screen's own
 * controller does or doesn't touch. `confirmArmed` below is now reset back
 * to `true` after every save attempt (success or failure), so the modal
 * correctly reappears every time, not just once. `update()` (ctrl:261-310)
 * has no `popUpFlag`/modal concept at all, same as its siblings.
 *
 * ================================================================
 * Validation asymmetry faithfully reproduced, NOT fixed: Save blocks empty
 * fields, Update does not check anything
 * ================================================================
 * The legacy template (view:16,30) has NO `required` attribute on either
 * input, despite the help-block markup referencing
 * `cabinMasterForm.incidentCategoryCode.$error.required` /
 * `...incidentCategoryDesc.$error.required` (view:18,32) -- those error
 * blocks are dead, `$error.required` can never be set with no `required`
 * directive present, so `cabinMasterForm.$valid` is unconditionally `true`
 * everywhere it's checked. The ONLY real gate on Save is a manual
 * `$scope.common.incidentCategoryCode == ""` / `...incidentCategoryDesc ==
 * ""` check (ctrl:156-171, exact empty-string match, no `.trim()`) with a
 * `growl.error` toast. `update()` (ctrl:261-310) has NO equivalent manual
 * check at all -- an edit can be submitted with a blank code or
 * description with no client-side block whatsoever. Reproduced below:
 * `save()` toasts and bails on an exactly-empty field; `update()` performs
 * no validation. The dead help-block markup itself is NOT reproduced (it
 * never renders in legacy either), matching this codebase's convention of
 * omitting provably-dead UI rather than porting inert markup verbatim
 * (e.g. `reason-type-master`'s own dead search box).
 *
 * ================================================================
 * Loader timing quirk faithfully reproduced: Save's spinner doesn't wait
 * for the response, Update's and the status-toggle's do
 * ================================================================
 * `save()`'s own `$rootScope.stopLoader()` (ctrl:211) sits OUTSIDE the
 * `GenericService.serviceAction(...).then(...)` callback, at the same
 * statement level as the call that kicks it off -- so the loader is
 * stopped essentially immediately after firing the POST, not after the
 * real response resolves. `update()` (ctrl:289,297) and `updateStatus()`
 * (ctrl:328,335) both correctly call `stopLoader()` INSIDE their `.then()`
 * callback, in both the success and error branch. Reproduced exactly:
 * `doSave()` below calls `loader.stop()` synchronously right after
 * `.subscribe(...)` is issued (not inside the callback); `update()` and
 * `toggleStatus()` call `loader.stop()` inside `next()`.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - `masterList()` (ctrl:100-120, the per-page reload fired by clicking a
 *   pagination link) assigns `$scope.commonList = response.data.listObject`
 *   UNCONDITIONALLY, with no `response.data.status == "success"` check at
 *   all (unlike the initial `initMasterList` load, ctrl:81-87, which does
 *   check status before assigning) -- so a backend error mid-pagination
 *   would silently blank the table with no toast. Reproduced via
 *   `loadPage()` below assigning `list` from `res.listObject` unconditionally.
 * - `showUpdateBtn(id)` (ctrl:230-258) flips to Update mode IMMEDIATELY on
 *   click, before the GET even resolves (ctrl:232-233) -- reproduced by
 *   `saveMode.set(false)` at the very top of `edit()`, before the API call.
 *   Its failure path is a `growl.error` toast with the real response
 *   message (ctrl:246-252), not a native `alert`.
 * - `updateStatus(id, status)` (ctrl:312-348) calls the FULL
 *   `$scope.initMaster()` + `$scope.initMasterList(...)` on a SUCCESSFUL
 *   toggle -- resetting the create/edit form to blank AND reloading the
 *   list -- but does nothing (only a toast) on failure, and the checkbox's
 *   two-way binding means the visual flip already happened either way with
 *   no revert on error. Reproduced via `toggleStatus()`'s optimistic local
 *   flip (never reverted) plus `resetForm()` + `loadListAndCount()` only on
 *   success.
 * - **The search box IS live** (view:68,93:
 *   `ng-model="searchKeyword"` + `commonList|orderBy:column:reverse|filter:searchKeyword`)
 *   -- unlike this codebase's `reason-type-master`, whose search box is
 *   completely dead. `column`/`reverse` are never assigned anywhere in this
 *   controller (grepped), so the `orderBy` is always a no-op; only the
 *   `filter:searchKeyword` part does anything. Since `commonList` here is
 *   only ever the CURRENTLY-FETCHED SERVER PAGE (this screen uses real
 *   server-side pagination, not a client-side full-list fetch like
 *   `consent-type-master`/`ward-store-mapper`), the search box only ever
 *   filters the current page's rows, same visible behavior as legacy.
 *   Reproduced via `searchKeyword` + `displayedRows` below, filtering
 *   `list()` (the current page) client-side.
 * - `getActiveIncidentCategoryMaster` is a real, live endpoint
 *   (`IncidentCategoryMasterController.java:104-115`) but this screen never
 *   calls it (grepped the whole controller file) -- not wired here, matching
 *   the confirmed Step 0/1 finding that it's unused by this screen itself.
 * - The backend's duplicate-description check backing `save()`
 *   (`GET_INCIDENT_CATEGORY_MASTER_BY_NAME`,
 *   IncidentCategoryMaster.java:54-59) is scoped by `unitId` only -- no
 *   `organizationId` in the `WHERE` clause -- while `update()`'s own
 *   duplicate check (`GET_INCIDENT_CATEGORY_BY_NAME_NOT_ID`, same file
 *   61-67) is scoped by `unitId` AND excludes the current row's own id. Both
 *   are real backend behavior, not pre-checked client-side here.
 */
@Component({
  selector: 'app-incident-category-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './incident-category-master.html',
  styleUrl: './incident-category-master.scss',
})
export class IncidentCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(IncidentCategoryMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  form = signal<IncidentCategoryForm>(emptyIncidentCategoryForm());
  saveMode = signal(true); // true = Save button visible (create); false = Update button visible (edit)
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment (fixed, diverges
   * from legacy): re-armed after every save attempt, success or failure. */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initMasterList/masterList) ----
  list = signal<IncidentCategoryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ---- search box -- filters the CURRENT PAGE only, see class doc ----
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.incidentCategoryCode?.toLowerCase().includes(keyword) || r.incidentCategoryDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initMasterList (ctrl:59-98): fetches list + count together and
  // resets to page 1 once the count responds.
  private loadListAndCount(): void {
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.loader.stop();
          this.list.set(res.listObject ?? []);
        }
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.loader.stop();
          this.totalCount.set(res.object ?? 0);
          this.currentPage.set(1);
        }
      },
    });
  }

  // mirrors masterList (ctrl:100-120) via setPage(page,true) -- see class
  // doc comment: no status check, list assigned unconditionally.
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.set(res.listObject ?? []);
      },
    });
  }

  // mirrors setNoOfRecords (ctrl:55-57).
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof IncidentCategoryForm>(field: K, value: IncidentCategoryForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:142-223) ----
  save(): void {
    const f = this.form();
    // ctrl:156-163 -- exact empty-string match, no .trim(). See class doc
    // comment: this is the ONLY real validation on this screen; update()
    // below has none at all.
    if (f.incidentCategoryCode === '') {
      this.notify.error('Please Enter Code'); // ctrl:157-160, verbatim
      return;
    }
    if (f.incidentCategoryDesc === '') {
      this.notify.error('Please Enter Description'); // ctrl:165-168, verbatim
      return;
    }
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:142-145): disarms the confirm gate, then saves.
  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start(); // ctrl:150-153 -- only started on the real-submit path
    this.api
      .save({
        incidentCategoryCode: f.incidentCategoryCode,
        incidentCategoryDesc: f.incidentCategoryDesc,
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
            this.notify.success(res.message ?? 'Success!'); // ctrl:201, growl.success(response.data.message)
            this.resetForm(); // ctrl:203-205 -- broadcast('show-errors-reset') + initMaster() + reload
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:208, growl.error(response.data.message)
          }
          // Fixed, diverges from legacy (see class doc comment): re-arm the
          // confirm gate on both success and failure, not just once ever.
          this.confirmArmed = true;
        },
      });
    // ctrl:211 -- stopLoader() sits OUTSIDE the .then() callback in legacy;
    // see class doc comment on the loader-timing quirk.
    this.loader.stop();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:230-258) ----
  edit(row: IncidentCategoryRow): void {
    // ctrl:232-233 -- legacy flips saveBtnFlag/updateBtnFlag to "Update" mode
    // IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.loader.start();
    this.api.getById(row.incidentCategoryId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error(res.message ?? 'Error!'); // ctrl:246-252 -- a growl toast, not a native alert
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          incidentCategoryId: d.incidentCategoryId,
          incidentCategoryCode: d.incidentCategoryCode,
          incidentCategoryDesc: d.incidentCategoryDesc,
        });
      },
    });
  }

  // ---- update (mirrors update, ctrl:261-310 -- no confirm modal, no
  // validation at all, see class doc comment) ----
  update(): void {
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        incidentCategoryId: f.incidentCategoryId as number,
        incidentCategoryCode: f.incidentCategoryCode,
        incidentCategoryDesc: f.incidentCategoryDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop(); // ctrl:289,297 -- correctly inside the callback, both branches
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:290
            this.resetForm(); // ctrl:292-294
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:298
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:312-348) ----
  toggleStatus(row: IncidentCategoryRow): void {
    const nextStatus: ActiveStatus = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- legacy's checkbox is two-way bound
    // (ng-model="info.status"), so the visual change happens immediately
    // regardless of the response either way; ctrl:312-348 never reverts it
    // on error. See class doc comment re: the minimal-payload rationale
    // (verified harmless against the real DAO).
    this.list.update((rows) => rows.map((r) => (r.incidentCategoryId === row.incidentCategoryId ? { ...r, status: nextStatus } : r)));
    this.loader.start();
    this.api
      .updateStatus({
        incidentCategoryId: row.incidentCategoryId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop(); // ctrl:328,335 -- correctly inside the callback, both branches
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:329
            // ctrl:331-332 -- a successful toggle calls the FULL
            // initMaster() + initMasterList(), resetting the create/edit
            // form to blank AND reloading the list -- see class doc comment.
            this.resetForm();
            this.loadListAndCount();
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:336, no revert, no reload
          }
        },
      });
  }

  // mirrors initMaster (ctrl:38-49) as re-entered by a successful
  // save/update/status-toggle -- deliberately does NOT touch confirmArmed,
  // see class doc comment.
  private resetForm(): void {
    this.form.set(emptyIncidentCategoryForm());
    this.saveMode.set(true);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:172-173/264-265/314-315. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
