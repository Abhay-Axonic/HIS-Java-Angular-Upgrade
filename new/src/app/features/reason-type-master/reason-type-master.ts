import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ReasonTypeMasterService } from './reason-type-master.service';
import { ActiveStatus, ReasonTypeForm, ReasonTypeRow, emptyReasonTypeForm } from './reason-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/reason-type-master.html +
 * resources/assets/js/controller/master/global/common/reasonTypeMasterController.js
 * (`reasonTypeMasterController`, no `controllerAs`). State `reasonTypeMaster`
 * (app.js:8406-8428), moduleId 494, confirmed live (resolve block checks
 * `permissions.modules` for id 494, no `requireSuperUser` anywhere in this
 * state or anywhere in app.js's grep for that term). Org-scoped Global
 * Master (not unit-scoped) -- `organizationId` off `cookieObject`.
 *
 * =====================================================================
 * SEVERITY 1 -- THIS SCREEN'S CONTROLLER IS NEVER LOADED IN LEGACY TODAY.
 * =====================================================================
 * `index.html:1571` has this exact line, commented out:
 *   <!-- <script src="resources/assets/js/controller/master/global/common/
 *        reasonTypeMasterController.js?version=3.08"></script> -->
 * (Its sibling, `reasonMasterController.js`, is live at index.html:1575 --
 * only reasonTypeMaster's own include is disabled.) No lazy-load
 * (`ocLazyLoad`) or bundler references this file elsewhere either (grepped
 * the whole `resources/assets/js/config/app.js` -- the only ocLazyLoad
 * mentions near this state are unrelated commented-out documentation
 * examples). There is no Maven/gulp/webpack build step that would
 * re-include a commented-out `<script>` tag -- `legacy/HIS-WAR`'s
 * `index.html` is served byte-for-byte as a static file.
 *
 * Net effect, CONFIRMED LIVE via `tools/golden-trace/capture.js` (both in a
 * joint 2-state run and in an isolated single-state re-run, reproduced
 * both times): navigating to `/reasonTypeMaster` throws an uncaught
 * `Error: [$controller:ctrlreg] Controller 'reasonTypeMasterController' is
 * not a registered controller` the instant ui-router tries to bind the
 * view. The static HTML template itself still loads (that's a plain file
 * fetch, unrelated to controller registration), but no `$scope` from this
 * controller is ever created -- every `ng-model`/`ng-click`/`ng-repeat`
 * binding in the view is inert. This is a real, currently-live, total
 * breakage for any real user trying to reach this specific screen -- not
 * a hypothetical or an environment artifact (reproduced against the exact
 * static file legacy itself serves, with no rebuild code involved).
 *
 * Per Step 0, this is exactly the "stop and log it as a decision point"
 * shape the playbook calls out -- except unlike `addItemPOCreation` (dead
 * frontend, entry 2) or `SurgicalSiteInfection` (non-functional mockup,
 * entry 6), the controller's OWN source here is complete, well-formed, and
 * (per this audit) implements correct, real CRUD logic against a REAL,
 * fully-wired backend (`ReasonTypeMasterController.java` ->
 * `ReasonTypeMasterServiceImpl.java` -> `ReasonTypeMasterDaoImpl.java`, all
 * present and correct for every endpoint this screen calls -- see
 * reason-type-master.service.ts). A modern Angular build has no equivalent
 * failure mode to "someone forgot a `<script>` tag" -- every routed
 * component is always bundled -- so there is no meaningful way to
 * "faithfully reproduce" this specific defect in the rebuild (unlike, say,
 * `modalityTypeMaster`'s entry 67, where the failure is a real backend
 * 404 that the rebuild naturally inherits by calling the same real URL).
 * Built here as a REAL, WORKING screen -- the evidently-intended behavior
 * of a complete, backend-verified controller that one legacy deployment
 * mistake currently prevents anyone from reaching at all. This is called
 * out loudly (see batch report) rather than silently glossed over, and
 * legacy's own `index.html` was NOT touched -- fixing it is a separate,
 * out-of-scope decision for whoever owns the legacy codebase.
 *
 * **A second, independent SEVERITY 1 in the real backend, faithfully
 * reproduced (NOT fixed) below:** `ReasonTypeMasterDaoImpl
 * .getReasonTypeMasterList()` (legacy/Global/.../dao/
 * ReasonTypeMasterDaoImpl.java:56-72) calls the named query
 * `GET_REASON_TYPE_MASTER_LIST` (ReasonTypeMaster.java:23-29), which has
 * NO `WHERE organizationId=...` clause at all, and the DAO's own
 * `.setInteger("orgId",...)`/`.setFirstResult(...)`/`.setMaxResults(...)`
 * calls are all present in the source but COMMENTED OUT. Every call to
 * this endpoint, from any org, at any offset/page-size, returns the
 * entire `m_reason_type_master` table across EVERY organization,
 * unpaginated. `getReasonTypeMasterCount` (same file, Hibernate Criteria +
 * `Restrictions.eq("organizationId",...)`) IS correctly scoped, so the
 * pager below computes a page count from the correct per-org total while
 * every actual page of rows is the same full cross-org list -- a visible
 * mismatch, not a hidden one. This also silently affects `reasonMaster`'s
 * own "Reason Type" dropdown, which calls this identical endpoint. Not
 * fixed here -- the rebuild calls the real endpoint and inherits the
 * identical real behavior, per standing instruction not to silently
 * correct a backend business-logic bug.
 *
 * **Sibling divergences from `reasonMaster` (both screens audited
 * together, same batch) -- all faithfully preserved, NOT normalized:**
 * - **No `$rootScope.startLoader()`/`stopLoader()` anywhere in this
 *   controller** (grepped: zero occurrences), unlike `reasonMaster`, which
 *   wraps save/update/edit-populate/status-toggle in the global loader.
 *   Reproduced by simply never calling `LoaderService` here, on any
 *   action.
 * - **No `$scope.$broadcast('show-errors-reset')`** on a successful
 *   save/update (`reasonMaster` calls it, ctrl:252/381; this controller's
 *   save (ctrl:193-250) and update (ctrl:321-368) never call it, on
 *   success or failure). In legacy this broadcast only clears a
 *   `has-error` CSS class on `.form-group` (see `validationDirective.js`)
 *   -- and even that is largely self-correcting the moment the cleared,
 *   still-required fields re-trigger the directive's own `$watch` on the
 *   next digest, so the real user-visible gap is marginal. Reproduced
 *   anyway, for source fidelity: `resetForm()` below does NOT clear
 *   `formSubmitted` (unlike `reasonMaster`'s does).
 * - **The search box is completely dead**, not just differently-wired.
 *   The legacy view's search `<input>` (reason-type-master.html:73) has NO
 *   `ng-model` at all, and its `ng-repeat="info in reasonList"`
 *   (view:125) has no `filter:` applied either -- typing there does
 *   literally nothing. `reasonMaster`'s own search box is genuinely live
 *   (a working `filter:searchKeyword` on its `ng-repeat`), despite ALSO
 *   having its own separate dead `ng-keyup="serachTestmaster(...)"`
 *   handler (a function that doesn't exist in that controller either --
 *   harmless there only because the `filter` does the real work
 *   independently). Reproduced by omitting any search wiring here at all
 *   -- no search box in this screen's rebuilt toolbar.
 * - **FIXED, diverges from legacy (shared with sibling `reasonMaster`,
 *   codebase-wide sweep + owner decision, 2026-07-24, see
 *   docs/ANGULAR_PENDING_FINDINGS.md entry 152):** reading `$scope.popUpFlag`
 *   as flipped permanently `false` by `PopupOkBtn` and only reset by
 *   `$scope.init()` after a *successful* save suggested a failed save would
 *   leave the next Save click skipping the confirm dialog. Confirmed live
 *   against real, unmodified legacy (duplicate-then-retry on
 *   `cardTypeMaster`/`raceMaster`) that this does NOT happen -- the shared
 *   `<modal>` directive's own hardcoded Ok-button handler re-arms the gate
 *   on every click, success or failure alike. `confirmArmed` below is now
 *   re-armed on both branches to match.
 * - Both screens also share: a successful status-toggle calls the full
 *   `$scope.init()` (list truly reloads AND the create/edit form resets to
 *   its blank "create" state) -- meaning toggling status on one row while
 *   mid-edit of a DIFFERENT row silently discards the in-progress edit.
 *   Reproduced via `toggleStatus()` calling `resetForm()` + reload on
 *   success below.
 * - Both controllers' real getById REST method is internally named
 *   `getEmployeeCategoryMasterById` (a copy-paste leftover from an
 *   unrelated screen) -- purely a Java-side internal method name, the
 *   `@RequestMapping` path and behavior are correct either way. Not
 *   reproducible/visible in the rebuild at all; noted for completeness
 *   only.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), this
 *   screen also uses the shared `<modal>` directive (view:158) whose ×
 *   close button is hardcoded to call `cancel()` -- a function this
 *   controller never defines either (grepped, zero matches). The confirm
 *   modal below uses an ordinary working `closeConfirmModal()`, matching
 *   the already-adopted standing policy, not a fresh write-up.
 */
@Component({
  selector: 'app-reason-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './reason-type-master.html',
  styleUrl: './reason-type-master.scss',
})
export class ReasonTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ReasonTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.reasonMasterObj) ----
  form = signal<ReasonTypeForm>(emptyReasonTypeForm());
  saveMode = signal(true); // true = Save button visible (create); false = Update button visible (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initReasonTypeMasterList/getReasonTypeList) ----
  list = signal<ReasonTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initReasonTypeMasterList (ctrl:79-132): fetches list + count
  // together and resets to page 1 once the count responds. NO loader calls
  // here, matching legacy exactly (see class doc comment).
  private loadListAndCount(): void {
    this.api.getList(this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors getReasonTypeList (ctrl:134-161) via setPage(page,true).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof ReasonTypeForm>(field: K, value: ReasonTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors the 2 HTML5 `required`
  // attributes backing `$scope.reasonForm.$valid`, view:16,31) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.reasonTypeCode.trim() && f.reasonTypeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().reasonTypeCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().reasonTypeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveReasonMaster/PopupOkBtn, ctrl:193-250) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the $valid gate, ctrl:209
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:187-190): disarms the confirm gate, then saves.
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
        reasonTypeCode: f.reasonTypeCode,
        reasonTypeDesc: f.reasonTypeDesc,
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
            this.notify.success(res.message ?? 'Success!');
            this.resetForm(); // ctrl:224 -- $scope.init() -- re-arms confirmArmed
          } else {
            this.notify.error(res.message ?? 'Error!');
            // Fixed, diverges from legacy (see class doc comment): re-arm
            // the confirm gate on failure too.
            this.confirmArmed = true;
          }
          // ctrl:240-244 -- reloads the list unconditionally after both branches.
          this.loadListAndCount();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyReasonTypeForm());
    this.saveMode.set(true);
    this.confirmArmed = true; // ctrl's $scope.init() resets popUpFlag = true
    // formSubmitted is deliberately NOT reset here -- see class doc comment
    // (missing show-errors-reset divergence from reasonMaster).
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:253-278) ----
  edit(row: ReasonTypeRow): void {
    this.saveMode.set(false);
    this.api.getById(row.reasonTypeId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          alert('Error!!'); // ctrl:273-275 -- a native alert, not growl.error
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          reasonTypeId: d.reasonTypeId,
          reasonTypeCode: d.reasonTypeCode,
          reasonTypeDesc: d.reasonTypeDesc,
        });
      },
    });
  }

  // ---- update (mirrors updateReasonMaster, ctrl:321-368 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // ctrl:335 -- ONLY gate on update
    const f = this.form();
    this.api
      .update({
        reasonTypeId: f.reasonTypeId as number,
        reasonTypeCode: f.reasonTypeCode,
        reasonTypeDesc: f.reasonTypeDesc,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:359-364 -- reloads the list unconditionally after both branches.
          this.loadListAndCount();
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:280-318) ----
  toggleStatus(row: ReasonTypeRow): void {
    const nextStatus: ActiveStatus = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.reasonTypeId === row.reasonTypeId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        reasonTypeId: row.reasonTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:301-303 -- a successful toggle calls the FULL $scope.init(),
            // which reloads the list AND resets the create/edit form to blank
            // -- see class doc comment (shared, non-divergent quirk).
            this.resetForm();
            this.loadListAndCount();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:194-196/282-284/322-324. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
