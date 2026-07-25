import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BloodGroupMasterService } from './blood-group-master.service';
import { BloodGroupForm, BloodGroupRow, emptyBloodGroupForm } from './blood-group-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/UnitMaster/bloodBank/blood-group-master.html`
 * (164 lines) + `resources/assets/js/controller/bloodBank/bloodGroupMasterController.js`
 * (362 lines, `bloodGroupMasterController`, no `controllerAs` -- every
 * binding is classic `$scope.*`, confirmed zero `vm.*` usage anywhere in
 * the view). State `bloodGroupMaster` (app.js:16203-16208,
 * `url: '/bloodGroupMaster'`) is registered directly in `app.js` (NOT
 * self-registered) with **NO `resolve` block at all** -- confirmed
 * directly by reading the state config (unlike its own sibling
 * `bloodBagType`, immediately below it in app.js, which DOES gate on
 * module 731). `tools/angular-audit/data/states_enriched.json` confirms
 * this independently (`"has_resolve": false`). This is the SAME shape as
 * the already-documented `billingGroupMaster`/`periodicityService` gap
 * (`docs/ANGULAR_REBUILD_PLAYBOOK.md`'s "Known caveats", item 1) --
 * module permission for this screen is enforced ONLY by the real sidebar
 * link's own `ng-if="checkAccess(982)"` (`new-side-bar.html:5006`), not by
 * the router itself, so a user who removes the menu item but still knows
 * the URL can reach this screen directly in real, unmodified legacy today.
 * `data.moduleId` is therefore correctly OMITTED below, matching legacy
 * exactly, not inventing a stricter rule than legacy actually has. No
 * `requireSuperUser` check anywhere either.
 *
 * **Step 0 classification (resolved by the orchestrating session, entry
 * 158): this is the blood-TYPE classification lookup itself (A+/B-/O+
 * etc.), a 2-field PURE-LOOKUP master, NOT a transfusion/donor-safety
 * workflow screen** -- deliberately distinguished from its own Blood Bank
 * module siblings (`transfusionPrecheckMaster`, `donorRejectionMaster`,
 * `donorScreeningMaster`, `bloodTestResult`), which are plausibly
 * SME-adjacent and were NOT selected for this batch. No Step 6b required.
 *
 * `$state` IS in this controller's own DI list (ctrl:5), so the recurring
 * missing-`$state`-DI crash bug (entries 8/10) does NOT apply here -- the
 * top-level login-redirect guard (ctrl:14-19, `cookieObject == undefined
 * -> $state.go('login')`) is a real, working guard in legacy.
 *
 * Backend confirmed real end-to-end, read directly:
 * `BloodGroupMasterController.java` (`@RequestMapping("global/bloodBank")`)
 * -> `BloodGroupMasterServiceImpl` -> `BloodGroupMasterDaoImpl` -> table
 * `blood_bank.m_blood_group_master`. `getBloodGroupMasterList` confirmed
 * live 200 in the batch-17 candidate research pass (entry 158) against a
 * real authenticated session; re-confirmed here via a fresh Step 2 golden
 * trace against `localhost:8080` (see this screen's own findings entry).
 * Local dev DB has 0 seed rows -- `blood_bank.seq_m_blood_group_master`
 * was independently checked BEFORE any live save (per this batch's
 * standing `BR-local-dev-db-drift-storetypemaster-seq` precedent) and
 * found already consistent with 0 existing rows (`last_value=1,
 * is_called=false`) -- no drift, no fix needed here, unlike
 * `storeTypeMaster`'s pre-existing 6-row seed gap.
 *
 * ================================================================
 * CONFIRMED BACKEND DEFECT, NOT FIXED HERE -- see
 * `blood-group-master.service.ts`'s class doc comment for the full
 * writeup: the duplicate-description check (`saveBloodGroupMaster`/
 * `updateBloodGroupMaster`) is scoped GLOBALLY across every organization
 * AND unit -- no `organizationId`/`unitId` clause at all, and never
 * checks `bloodGroupCode` either. Reproduced as-is; flagged in this
 * batch's findings entry as a data-integrity gap (can incorrectly block a
 * legitimate save under one organization purely because an unrelated
 * organization already used the same description).
 * ================================================================
 * Separately noted, not a new per-screen finding -- this screen's
 * `updateBloodGroupMaster`/`updateBloodGroupMasterStatus` DAO calls both
 * look the target row up by `bloodGroupId` alone (`findById(...)`, no
 * `organizationId`/`unitId` predicate at all), so in principle a crafted
 * request from a different org/unit could modify a row it can never
 * actually see through this screen's own (org+unit-scoped) list/getById
 * calls. This is the same already-disclosed, project-wide, zero-backend-
 * authorization gap `module-access.guard.ts`'s own doc comment describes
 * (no `@PreAuthorize`/`@Secured`/`hasRole` usage anywhere in `legacy/`) --
 * not a new, screen-specific defect, so not re-logged as a fresh finding
 * here; noted only so it isn't silently missed during this screen's own
 * Step 1 audit.
 *
 * ================================================================
 * Confirm-modal on Save -- relies on the shared `<modal>` directive's own
 * re-arm mechanism (entry 152's "major update"), the SAME shape as
 * `discountCategoryMaster`/`GLcode` (NOT `storeTypeMaster`'s own
 * unconditional-reset code)
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:11), gates whether clicking Save
 * opens the shared `<modal>` (`showModal = !showModal`, ctrl:185-186) or
 * actually POSTs (ctrl:188-214). Read in isolation, this controller's OWN
 * code NEVER resets `popUpFlag` back to `true` anywhere (grepped the whole
 * file) -- naively suggesting the confirm modal would only ever appear
 * ONCE per page load, for the very first Save, and silently skip
 * confirmation on every subsequent Save click for the rest of the session
 * (worse than the `employee-category-master` "worse variant" entry 152
 * found, since that screen at least had its own gate variable to reason
 * about). Entry 152 confirmed (live, on `cardTypeMaster`/`raceMaster`,
 * the identical shared `<modal>` directive this screen also uses --
 * view:156, `<modal visible="showModal"></modal>`) that this does **NOT**
 * happen in real legacy: `commonPopUpDirectives.js`'s `oneClickOnly`
 * directive (bound to the modal's own hardcoded Ok button,
 * `one-click-only="PopupOkBtn();"`) sets `scope.$parent["popUpFlag"] =
 * true` SYNCHRONOUSLY on every click, before `PopupOkBtn()`'s own
 * `popUpFlag = false` even runs, and the directive's `$watch` on
 * `showModal` does the identical reset again the moment the modal closes
 * -- both independent of whatever the subsequent async save's response
 * turns out to be. So the gate is, in practice, re-armed on literally
 * every Ok click in real legacy, unconditionally. `confirmArmed` below is
 * reset to `true` after EVERY save attempt (success or failure) to
 * reproduce that real always-rearms behavior, per this codebase's
 * NOW-STANDARD corrected pattern -- not a deviation, a faithful
 * reproduction of what the shared directive actually does.
 *
 * Per entry 55's standing policy: legacy's shared `<modal>` directive's
 * close (x) button calls a bare `cancel()` this controller never defines
 * (grepped: zero `cancel` definitions anywhere in this file) -- a live,
 * disclosed legacy crash on every screen using that directive without its
 * own `cancel`. This rebuild uses a plain working `closeConfirmModal()`
 * instead, per the standing policy (not re-litigated further here).
 *
 * ================================================================
 * Validation: THIS screen has NO HTML5 `required`/`show-errors`/
 * `.help-block` mechanism at all -- unlike `discountCategoryMaster`/
 * `GLcode`/`storeTypeMaster`, its own validation is a plain manual JS
 * check inside `save()`/`update()` that shows a `growl.error` TOAST and
 * returns, with no inline per-field message or red-highlight styling
 * (confirmed: view has no `required` attribute and no `.help-block`
 * element anywhere on either input, view:13/26). Reproduced below via
 * `NotificationService.error(...)` toasts only -- deliberately NOT adding
 * an inline validation-message UI this screen's own legacy never had; the
 * entry-111 validation-visibility standing policy applies to making
 * EXISTING help-block-style messages visible, not to inventing a new
 * validation layer for a screen that never had one. Also note: legacy's
 * check is a raw `== undefined || == ""` comparison with NO `.trim()` --
 * a whitespace-only value (`"   "`) is genuinely accepted as valid by
 * real legacy. Reproduced verbatim (`=== ''`, no trim) rather than
 * "improving" this into a stricter check.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - No `startLoader()`/`stopLoader()` symmetry needed to model: legacy
 *   only calls `startLoader()` inside `save()`'s ELSE branch (ctrl:165-167,
 *   i.e. only on the real POST attempt, not the modal-opening click) and
 *   inconsistently elsewhere -- not modeled here, matching this codebase's
 *   established convention of not reproducing the spinner itself.
 * - `save()`/`update()`/`updateStatus()` all show `response.data.message`
 *   VERBATIM via `growl.success`/`growl.error` (ctrl:197-210/299-313/
 *   339-352) -- NOT hardcoded literal strings like
 *   `discountCategoryMaster`/`GLcode`. Reproduced via
 *   `this.notify.success(res.message ?? '')`/`this.notify.error(res.message
 *   ?? '')`, matching `storeTypeMaster`'s same convention. Server-side
 *   constants (`ICommonConstants.java`, read directly): save success =
 *   "Added Successfully.", update/status success = "Updated
 *   Successfully.", save/update duplicate = "Already Exists."
 * - Form reset + list re-fetch (`initMaster()` + `initMasterList(...)`)
 *   happen ONLY inside the `status == 'success'` branch of `save()`/
 *   `update()` (ctrl:201-204/298-305) -- UNLIKE `storeTypeMaster`, which
 *   resets/refetches unconditionally. On a rejected ("already exists")
 *   save/update, the form stays populated with whatever the user typed
 *   and the list is NOT re-fetched. Reproduced by only calling
 *   `resetForm()`/`loadListAndCount()` inside the success branch.
 * - `showUpdateBtn` (ctrl:226-254) flips to Update mode IMMEDIATELY on
 *   click, before the GET even resolves (ctrl:229-231) -- reproduced by
 *   `saveMode.set(false)` at the very top of `edit()`. On a failed
 *   `getBloodGroupMasterById` (ctrl:242-248), legacy growl-errors but does
 *   NOT revert `saveBtnFlag`/`updateBtnFlag` back to Save mode, leaving
 *   the screen stuck showing "Update" over whatever the form last held --
 *   reproduced verbatim (no revert in the `edit()` error branch either).
 * - Status toggle (`updateStatus`, ctrl:322-358): the view's checkbox
 *   (`ng-model="info.status"`, `ng-true-value="'A'"`,
 *   `ng-false-value="'I'"`, `ng-change="updateStatus(info.bloodGroupId,
 *   info.status)"`) flips optimistically client-side before the request
 *   resolves, and legacy's handler has NO revert branch on failure (only a
 *   growl toast). Reproduced via `<app-status-toggle>` + an unconditional
 *   optimistic `list.update()`, never reverted regardless of response
 *   status. Legacy's own payload construction mutates whatever
 *   `$scope.common` happens to currently hold (leftover from the last
 *   edit, or the empty `initMaster()` default) and POSTs the WHOLE object
 *   -- confirmed harmless by reading `BloodGroupMasterDaoImpl.
 *   updateBloodGroupMasterStatus` directly: it only ever reads
 *   `bloodGroupId`/`status`/`updatedBy`/`updatedDate` off the DTO, never
 *   `bloodGroupCode`/`bloodGroupDesc`, so sending stale code/desc values
 *   (as legacy's own payload does) has zero observable effect. This
 *   rebuild sends only the 4 fields the backend actually reads, built
 *   from the toggled row's own id/new-status (not whatever the edit form
 *   currently holds) -- a strictly cleaner wire payload with an identical
 *   observable result, matching `storeTypeMaster`'s own precedent of
 *   simplifying a confirmed-inert legacy field rather than replicating
 *   staleness that provably changes nothing server-side.
 * - The search box (`ng-model="searchKeyword"` + `filter:searchKeyword`
 *   on the `ng-repeat`, view:65/118) filters only the CURRENTLY LOADED
 *   SERVER PAGE (`commonList`), same "current-page-only search" quirk
 *   already documented on several sibling master screens. Reproduced via
 *   `filteredList` over `list()`.
 * - Legacy's own field labels ("Blood Bag Type Code"/"Blood Bag Type
 *   Description", view:14/27) do NOT match its own table column headers
 *   ("Blood Group Code"/"Blood Group Description", view:75/85) or its own
 *   page legend ("Blood Group Master", view:4) -- a real, disclosed
 *   copy-paste inconsistency in legacy's own markup (this screen has
 *   nothing to do with blood BAGS), not a rebuild typo. Reproduced
 *   verbatim in the template below.
 * - The table header's "Status(n)" label (view:95) is legacy's own literal
 *   text, not a placeholder or typo introduced here.
 */
@Component({
  selector: 'app-blood-group-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './blood-group-master.html',
  styleUrl: './blood-group-master.scss',
})
export class BloodGroupMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(BloodGroupMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common, ctrl:40-45) ----
  form = signal<BloodGroupForm>(emptyBloodGroupForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` as actually re-armed by the shared
   * `<modal>` directive's own click/watch mechanism (entry 152) -- see
   * class doc comment. Re-armed after every save attempt this rebuild's
   * `doSave()` resolves, success or failure. */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initMasterList/PagerService) ----
  list = signal<BloodGroupRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side filter over only the currently-loaded page -- see class
  // doc comment ("current-page-only search").
  filteredList = computed(() => {
    const kw = this.searchKeyword().trim().toLowerCase();
    if (!kw) return this.list();
    return this.list().filter(
      (row) => row.bloodGroupCode.toLowerCase().includes(kw) || row.bloodGroupDesc.toLowerCase().includes(kw),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initMasterList (ctrl:58-97): fetches list + count together and
  // resets to page 1 once the count responds.
  private loadListAndCount(): void {
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.totalCount.set(res.object ?? 0);
          this.currentPage.set(1);
        }
      },
    });
  }

  // mirrors masterList (ctrl:99-119) via setPage(page,true).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.list.set(res.listObject ?? []);
      },
    });
  }

  // mirrors setNoOfRecords (ctrl:54-56).
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof BloodGroupForm>(field: K, value: BloodGroupForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors the manual undefined/""
  // checks, ctrl:148-162/260-274 -- NO .trim(), see class doc comment) ----
  private validate(): boolean {
    const f = this.form();
    if (f.bloodGroupCode === '') {
      this.notify.error('Please Enter Code '); // ctrl:149, verbatim
      return false;
    }
    if (f.bloodGroupDesc === '') {
      this.notify.error('Please Enter Description '); // ctrl:157, verbatim
      return false;
    }
    return true;
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:141-219) ----
  save(): void {
    if (!this.validate()) return;
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:141-144): disarms the confirm gate, closes
  // the modal, then saves -- matching the shared directive's own
  // synchronous close-before-async-response behavior (class doc comment).
  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        bloodGroupCode: f.bloodGroupCode,
        bloodGroupDesc: f.bloodGroupDesc,
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
            this.notify.success(res.message ?? ''); // ctrl:197-200, verbatim server message
            this.resetForm(); // ctrl:203 -- initMaster()
            this.loadListAndCount(); // ctrl:204 -- re-fetch happens ONLY on success in legacy
          } else {
            this.notify.error(res.message ?? ''); // ctrl:207-210, verbatim server message ("Already Exists.")
          }
          // Re-arm the confirm gate unconditionally -- matches legacy's
          // real behavior (see class doc comment): the shared `<modal>`
          // directive's own click/watch mechanism re-arms `popUpFlag`
          // regardless of the save's outcome.
          this.confirmArmed = true;
        },
        // A hard HTTP error never reaches legacy's own single-argument
        // `.then()` either -- reproducing the shared directive's real
        // always-rearms behavior means this rebuild's equivalent gate must
        // also not get stuck shut on a genuine backend exception.
        error: () => {
          this.confirmArmed = true;
          this.showConfirmModal.set(false);
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:226-254) ----
  edit(row: BloodGroupRow): void {
    // ctrl:229-231 -- legacy flips saveBtnFlag/updateBtnFlag to "Update"
    // mode IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.api.getById(row.bloodGroupId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          this.form.set({
            bloodGroupId: res.listObject[0].bloodGroupId,
            bloodGroupCode: res.listObject[0].bloodGroupCode,
            bloodGroupDesc: res.listObject[0].bloodGroupDesc,
          });
        } else {
          // ctrl:242-248 -- growl.error only, no revert back to Save mode.
          this.notify.error(res.message ?? '');
        }
      },
      // A hard HTTP error is likewise never reverted in legacy either --
      // reproduced by simply doing nothing further here.
      error: () => {},
    });
  }

  // ---- update (mirrors update, ctrl:257-320 -- same manual validation as
  // save(), no confirm modal) ----
  update(): void {
    if (!this.validate()) return;
    const f = this.form();
    this.api
      .update({
        bloodGroupId: f.bloodGroupId as number,
        bloodGroupCode: f.bloodGroupCode,
        bloodGroupDesc: f.bloodGroupDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? ''); // ctrl:299-302, verbatim server message
            this.resetForm(); // ctrl:305 -- initMaster(), resets all the way back to CREATE mode
            this.loadListAndCount(); // ctrl:306 -- re-fetch happens ONLY on success in legacy
          } else {
            this.notify.error(res.message ?? ''); // ctrl:309-313, verbatim server message ("Already Exists.")
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:322-358) ----
  toggleStatus(row: BloodGroupRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing
    // -- never reverted below, per the class doc comment (legacy has no
    // revert branch at all for this screen).
    this.list.update((rows) => rows.map((r) => (r.bloodGroupId === row.bloodGroupId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        bloodGroupId: row.bloodGroupId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? ''); // ctrl:339-343, verbatim server message
          } else {
            this.notify.error(res.message ?? ''); // ctrl:346-352, verbatim server message -- no revert
          }
        },
      });
  }

  // mirrors initMaster() (ctrl:37-47) as re-entered by save()/update()'s
  // success handler only.
  private resetForm(): void {
    this.form.set(emptyBloodGroupForm());
    this.saveMode.set(true);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:168-169/276-277/324-325. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
