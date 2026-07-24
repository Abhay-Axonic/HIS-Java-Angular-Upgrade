import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { GlCodeMasterService } from './gl-code-master.service';
import { GlCodeForm, GlCodeRow, emptyGlCodeForm } from './gl-code-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/UnitMaster/billingMaster/GL-code.html`
 * (142 lines) + `resources/assets/js/controller/master/unit/gLcodeMasterController.js`
 * (346 lines, `gLcodeMasterController`, no `controllerAs` -- every binding
 * is classic `$scope.*`, confirmed zero `vm.*` usage in the view -- also
 * confirmed independently via `states_enriched.json`'s `"controllerAs":
 * null` entry for this state). State `GLcode` (app.js:14891-14918,
 * `url: '/GLcode'`) is registered directly in `app.js` (NOT
 * self-registered, NOT `MasterController`-routed) with a real `resolve`
 * block gating on `permissions.modules` id **618** -- read directly from
 * app.js, not inferred. `data.moduleId` below is therefore 618.
 * Independently corroborated: the real sidebar entry
 * (`views/common/new-side-bar.html:3342`, `ng-if="checkAccess(618)"`,
 * `href="#/GLcode"`, under Global Master > Organization Structure) and
 * `adminPriviledges.json` id 618 ("GL Code", `parentId 370`) both confirm
 * this is a genuine, reachable, non-decoy menu link, not a dead route.
 * No `requireSuperUser` check in the resolve block.
 *
 * `$state` IS in this controller's own DI list (ctrl:7), so the recurring
 * missing-`$state`-DI crash bug (entries 8/10) does NOT apply here -- the
 * `cookieObject == undefined -> $state.go('login')` guard (ctrl:23-27) is
 * a real, working guard in legacy. This rebuild doesn't need a component-
 * level equivalent: `moduleAccessGuard` (core/module-access.guard.ts)
 * already enforces both "must be logged in" and the module-618 check
 * centrally, at the route level, for every screen.
 *
 * Backend confirmed real end-to-end, read directly (not trusted from the
 * entry-158 research pass): `GeneralLedgerMasterController.java`
 * (`@RequestMapping("/org")`) -> `GeneralLedgerMasterServiceImpl` ->
 * `GeneralLedgerMasterServiceDaoImpl` -> table
 * `public.m_general_ledger_master` (`GeneralLedgerMaster.java:67`). This
 * screen operates at the ORGANIZATION level only -- `unitId` is read from
 * the login cookie (ctrl:29) but never sent on the wire anywhere (every
 * `unitId:` reference in every payload is commented out, ctrl:69/74/119/172),
 * and every named query filters by `organizationId` alone.
 *
 * ================================================================
 * CONFIRMED BACKEND DEFECT, NOT FIXED HERE (legacy bug, logged, not a
 * rebuild bug) -- the duplicate-name check is scoped WRONG on both save
 * and update, in two different ways
 * ================================================================
 * `GET_GENERAL_LEDGER_MASTER_BY_NAME` (GeneralLedgerMaster.java:26-30,
 * used by `save()`'s duplicate guard,
 * GeneralLedgerMasterServiceImpl.java:25-38) reads:
 * `WHERE LOWER(name)=:ledgerName OR name=:ledgerName AND organizationId=:orgId`.
 * HQL/JPQL `AND` binds tighter than `OR`, so this parses as
 * `LOWER(name)=:ledgerName OR (name=:ledgerName AND organizationId=:orgId)`
 * -- the first clause (the one that actually matches in practice, since
 * `:ledgerName` is always pre-lowercased by the DAO,
 * GeneralLedgerMasterServiceDaoImpl.java:48) has NO organization filter at
 * all. Net effect: `save()`'s "already exists" check is effectively
 * GLOBAL across every organization, not per-organization as the
 * `organizationId` parameter's presence implies it should be -- creating
 * "Cash" under Org B would be incorrectly blocked as a duplicate if Org A
 * already has a ledger named "Cash".
 * `GET_LEDGER_BY_NAME_NOT_ID` (GeneralLedgerMaster.java:58-62, used by
 * `update()`'s duplicate guard, GeneralLedgerMasterServiceImpl.java:63-78)
 * is worse -- it has NO `organizationId` parameter or filter AT ALL, only
 * `ledgerId !=:ledgerId`, so `update()`'s duplicate check is unambiguously
 * global across every organization, no operator-precedence ambiguity
 * needed to reach that conclusion.
 * Both are confirmed by direct source reading, not guessed. **Not
 * live-reproducible in this dev environment**: `m_organization_master` has
 * exactly one seed row (`organization_id=1`, "DEMO") -- confirmed via
 * direct `psql` query -- so there is no second organization to create a
 * same-named ledger under and observe the false-positive block live. Given
 * a single organization, this bug's practical effect in THIS environment
 * is indistinguishable from a correctly-scoped per-org check (same-org
 * duplicates are still caught, just via an over-broad global check rather
 * than the intended narrower one). Logged per this project's standing
 * policy (log real backend bugs found during a parity rebuild, don't
 * silently fix them) -- flagged prominently in this batch's findings entry
 * since it's a cross-organization billing-classification correctness gap,
 * not merely cosmetic, though notably the OPPOSITE failure mode from
 * `discountCategoryMaster`'s wrong-table bug (over-blocking here, vs.
 * under-detecting there).
 *
 * ================================================================
 * Two-click confirm on Save only -- `confirmArmed` re-arms on failure too,
 * matching incident-category-master.ts's confirmSave()/doSave() shape
 * exactly. NOT a legacy-bug fix -- this MATCHES legacy's real (non-buggy)
 * behavior, per the corrected understanding in
 * docs/ANGULAR_PENDING_FINDINGS.md entry 152's "Major update"
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:13), gates whether clicking Save
 * opens the shared `<modal>` (`showModal = !showModal`, ctrl:178-180) or
 * actually POSTs (ctrl:181-210). This screen's OWN code never explicitly
 * resets `popUpFlag` back to `true` anywhere after it's first disarmed by
 * `PopupOkBtn` (ctrl:151-154) -- naively suggesting it would stay
 * permanently disarmed (skipping the confirm modal on every subsequent
 * Save click) after the very first confirm-driven save of the session,
 * success or failure. Per entry 152's corrected understanding (confirmed
 * live on `cardTypeMaster`/`raceMaster`, the identical shared `<modal>`
 * directive), this does **NOT** happen in real legacy: the directive's own
 * hardcoded Ok-button (`commonPopUpDirectives.js`'s `oneClickOnly`
 * directive click handler, which fires `scope.$parent["popUpFlag"] = true`
 * BEFORE evaluating `PopupOkBtn()` at all, plus its own `$watch` on
 * `showModal` which does the same on every close) resets the gate to armed
 * on every Ok click, structurally, regardless of what any individual
 * screen's own success/failure branches do -- so the flag can never
 * actually stay stuck in unmodified legacy. `GLcode` is a new instance of
 * the SAME shared-directive mechanism (confirmed above: this screen's own
 * `<modal visible="showModal">` binding, view:124), so the same correction
 * applies for the same reason -- `confirmArmed` below is reset to `true`
 * after EVERY save attempt, success or failure, which reproduces legacy's
 * real always-rearms behavior rather than diverging from it. `update()`
 * (ctrl:249-305) has no confirm-modal concept at all, matching legacy
 * exactly.
 *
 * Per entry 55's standing policy: legacy's shared `<modal>` directive's
 * close (x) button calls a bare `cancel()` this controller never defines
 * (grepped: zero `cancel` definitions anywhere in this file) -- a live,
 * disclosed legacy crash on every screen using that directive without its
 * own `cancel`. This rebuild uses a plain working `closeConfirmModal()`
 * instead, per the standing policy (not re-litigated further here).
 *
 * ================================================================
 * NO client-side validation anywhere on this screen -- faithfully
 * reproduced, not a silent bug
 * ================================================================
 * Neither input has an HTML `required` attribute, `ng-required`, nor any
 * wrapping `<form>` element at all (view:16,32 -- confirmed by reading the
 * whole 142-line view) -- the `*` markers next to both labels are purely
 * cosmetic. `save()` (ctrl:156) and `update()` (ctrl:249) both proceed
 * unconditionally with whatever is in `$scope.common`, including blank
 * strings; the DB columns (`general_ledger_code`/`general_ledger_name`)
 * are both nullable (confirmed via `\d public.m_general_ledger_master`),
 * so an empty-field submission genuinely succeeds server-side, creating a
 * visibly blank row -- not a silent failure (the blank row IS visible in
 * the table afterward), so per this tier's policy this is faithfully
 * reproduced (no client-side gate added) rather than "fixed," and flagged
 * here for the owner's awareness as a real legacy weakness, not a rebuild
 * regression.
 *
 * ================================================================
 * Save success vs. failure diverge in what happens next -- NOT the same
 * "always reload" shape as update() or as discountCategoryMaster's save()
 * ================================================================
 * `save()`'s response handler (ctrl:186-209) only calls `initMaster()`
 * (reset the form) and `initMasterList(...)` (reload list+count) inside
 * the SUCCESS branch. The error branch (ctrl:199-206) only shows a growl
 * -- no reload, no form reset. Reproduced below: `doSave()`'s success path
 * calls `resetForm()` + `loadListAndCount()`; the error path only notifies
 * (plus re-arms `confirmArmed`, the rebuild-only correction above).
 *
 * `update()` (ctrl:249-305) is different again: it calls
 * `initMasterList(...)` UNCONDITIONALLY (ctrl:290-296, outside the
 * success/fail branching), on EVERY update attempt regardless of outcome
 * -- matching `discountCategoryMaster`'s `update()` shape. But only the
 * SUCCESS branch clears the form (`$scope.common={}`, ctrl:279) -- on
 * failure the form stays populated with the rejected edit. Separately,
 * `initMasterList`'s own count-response callback (ctrl:89-104)
 * unconditionally resets `saveBtnFlag=true;updateBtnFlag=false` EVERY time
 * it runs, regardless of why it was called -- so after ANY update attempt
 * (success or failure), the UI flips back to showing the "Save" button,
 * not "Update", even on a rejected update. If the user then clicks that
 * now-mislabeled Save button with the still-populated (rejected) form
 * values, it would attempt to CREATE a new row with those values rather
 * than retry the update -- a real, if confusing, legacy interaction quirk
 * (visible, not silent -- the button genuinely changes), faithfully
 * reproduced below via `update()` unconditionally setting `saveMode(true)`
 * and reloading, while only clearing the form on success. Flagged in this
 * batch's findings entry as a UX quirk, not fixed (not silent, not data
 * loss -- the user sees a "Save" button and the same values they typed).
 *
 * `initMasterList`'s pagination behavior also differs by caller: when
 * `updateBtnFlag` was true at the time of the count response (i.e. called
 * from `update()`), it re-fetches the CURRENT page (`setPage($scope.pageNo,
 * true)`, ctrl:94-95); otherwise (called from `save()` or the initial
 * load) it resets to page 1 (`setPage(1,false)`, ctrl:98-99). Reproduced
 * below via two helpers: `loadListAndCount()` (resets to page 1, used by
 * `save()`/initial load) and `reloadAfterUpdate()` (stays on the current
 * page, used by `update()`) -- a simplification of legacy's shared
 * callback/flag-timing plumbing into the two visibly-distinct outcomes,
 * not a byte-for-byte port of the async race itself.
 *
 * ================================================================
 * CONFIRMED LIVE LEGACY BUG, FIXED HERE (silent/invisible frontend bug,
 * same "fix directly" precedent as identificationMaster/currencyMaster,
 * NOT the discountCategoryMaster "log a backend bug, don't fix it"
 * precedent -- this is pure frontend controller code, nothing server-side)
 * -- legacy's on-screen list silently goes STALE after the first Update of
 * a session, even though the underlying database write succeeds
 * ================================================================
 * Root cause, confirmed by reading source AND reproducing live against
 * real running legacy (not guessed): `update()`'s response handler
 * (ctrl:290-291) does `$scope.offset = pageOffset; $scope.initMasterList(
 * $scope.organizationId, $scope.offset, ...)`. `pageOffset` (ctrl:107,
 * `var pageOffset = '';`) is a module-level variable ONLY ever assigned
 * inside `masterList()` (ctrl:108-129, the pagination-page-fetch function
 * `setPage` calls with `flag=true`) -- it is NEVER assigned by the initial
 * page load or by `save()`'s own post-save reload (both call
 * `setPage(1,false)`, i.e. `flag=false`, which does not call `masterList`).
 * So on the FIRST Update of any fresh session (before any manual
 * pagination click has ever run `masterList`), `pageOffset` is still its
 * initial value, the empty string `''` -- producing the malformed request
 * `GET /org/getGeneralLedgerMasterList/{orgId}/{offset=''}/{...}` ->
 * literally `.../1//10` (double slash, empty path segment) -> Spring
 * cannot bind an empty string to an `int @PathVariable` -> **HTTP 404**.
 * Legacy's `.then(function(response){...})` has no rejection handler, so
 * this 404 is silently swallowed -- `$scope.commonList` is simply never
 * updated, leaving the table showing the PRE-update value even though the
 * write itself succeeded.
 * **Live-confirmed, this session, on real unmodified legacy**: updated
 * `GL001`'s description from "Cash Account" to "Cash Account (Updated)" --
 * got a real success growl, but the on-screen table kept showing "Cash
 * Account" (the stale value). Direct `psql` query against
 * `m_general_ledger_master` confirmed the DATABASE row was correctly
 * updated to "Cash Account (Updated)" the whole time. The network trace
 * showed exactly the predicted malformed request:
 * `GET /org/getGeneralLedgerMasterList/1//10` -> **404**, alongside the
 * 3 other (valid, `/1/0/10`) list calls from earlier in the session. A
 * subsequent full page navigation away and back (a fresh `initMasterList`
 * call using `offset=0`, always valid) correctly showed "Cash Account
 * (Updated)" -- proving the database was never wrong, only the live
 * in-session view. Not previously documented anywhere in this project's
 * findings; discovered during this screen's own Step 5 testing, not
 * inherited from the entry-158 research pass.
 * This is a genuinely SILENT bug (no error surfaces to the user -- the
 * success growl still fires) that could easily make a real user believe
 * their edit was lost and re-enter it, or distrust the system generally.
 * Per this codebase's established precedent for silent/invisible
 * FRONTEND-only bugs (identificationMaster/currencyMaster; distinct from
 * the discountCategoryMaster/GLcode's-own-duplicate-check-scoping-bug
 * class of BACKEND bugs, which get logged-not-fixed), this is corrected
 * directly here rather than faithfully reproduced: `reloadAfterUpdate()`
 * (below) derives its offset from `this.currentPage()`, a properly
 * tracked signal that is always valid (never an uninitialized/empty
 * value), so this rebuild's Update flow always correctly refreshes the
 * visible list to match what was actually just written to the database.
 * Flagged prominently for owner sign-off on this classification, per
 * standing policy, even though the fix is already applied.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - `startLoader()`/`stopLoader()` are called around every one of this
 *   controller's 6 API calls (ctrl:61,83,91,109,125,164,190,201,207,221,
 *   223,230,254,267,272,313) -- reproduced via `LoaderService`, matching
 *   `currency-master.ts`'s convention (unlike `discountCategoryMaster`,
 *   which never uses the loader at all).
 * - `save()`/`update()`/`updateStatus()`/`getById`'s error branch all
 *   growl `response.data.message` directly (ctrl:191/236/273/323/331,
 *   never a hardcoded literal) -- reproduced via `res.message ?? '...'`
 *   fallbacks, matching `currency-master.ts`'s convention, NOT
 *   `discountCategoryMaster`'s hardcoded-string convention.
 * - `showUpdateBtn` (ctrl:217-246) flips to Update mode IMMEDIATELY on
 *   click, before the GET even resolves (ctrl:218-219) -- reproduced by
 *   `saveMode.set(false)` at the very top of `edit()`.
 * - `showUpdateBtn`'s failure branch is `growl.error(response.data.message)`
 *   (ctrl:234-239), not a native `alert()` -- unlike `discountCategoryMaster`.
 * - Status toggle (`updateStatus`, ctrl:307-343): the view's checkbox
 *   (`ng-model="info.status"`, `ng-true-value="'A'"`, `ng-false-value="'I'"`,
 *   `ng-change="updateStatus(...)"`) flips optimistically client-side
 *   before the request resolves, and legacy's handler has NO revert branch
 *   on failure (only a growl toast). Reproduced via `<app-status-toggle>` +
 *   an unconditional optimistic `list.update()`, never reverted regardless
 *   of response status.
 * - The search box (`ng-model="searchKeyword"` + `filter:searchKeyword` on
 *   the `ng-repeat`, view:57/84) is AngularJS's generic `filter` filter,
 *   which technically matches against every stringified property of each
 *   row object (including the numeric id and the raw 'A'/'I' status char),
 *   not just the two visible text fields. Reproduced via the established
 *   sibling-screen convention (`discount-category-master.ts`) of filtering
 *   the two meaningfully-searchable text fields (code, name) rather than a
 *   byte-for-byte port of AngularJS's generic reflection-based filter --
 *   same "current-page-only search" quirk (filters only the currently
 *   loaded server page, not the full dataset) as every sibling master
 *   screen.
 * - The table header's "Status(n)" label (view:79) is legacy's own literal
 *   text, not a placeholder or typo introduced here.
 * - `BillingGenericService` is injected (ctrl:7) but never called anywhere
 *   in this controller (grepped the whole file) -- a harmless dead
 *   dependency, no rebuild equivalent needed.
 */
@Component({
  selector: 'app-gl-code-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './gl-code-master.html',
  styleUrl: './gl-code-master.scss',
})
export class GlCodeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(GlCodeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common, ctrl:40-46) ----
  form = signal<GlCodeForm>(emptyGlCodeForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment (FIXED, diverges
   * from legacy): re-armed after every save attempt, success or failure. */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initMasterList/PagerService) ----
  list = signal<GlCodeRow[]>([]);
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
      (row) => row.generalLedgerCode.toLowerCase().includes(kw) || row.generalLedgerName.toLowerCase().includes(kw),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initMasterList (ctrl:59-106) when called from the initial load
  // or save(): resets to page 1.
  private loadListAndCount(): void {
    this.loader.start();
    this.api.getList(this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.totalCount.set(res.object ?? 0);
          this.currentPage.set(1);
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // mirrors initMasterList (ctrl:59-106) when called from update(): stays
  // on the current page instead of resetting to page 1 (ctrl:93-95) -- see
  // class doc comment.
  private reloadAfterUpdate(): void {
    const page = this.currentPage();
    const offset = (page - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.totalCount.set(res.object ?? 0);
      },
      error: () => this.loader.stop(),
    });
  }

  // mirrors masterList (ctrl:108-129) via setPage(page,true).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
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

  updateField<K extends keyof GlCodeForm>(field: K, value: GlCodeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:150-215) ----
  // No validation gate at all -- see class doc comment ("NO client-side
  // validation anywhere").
  save(): void {
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:151-154): disarms the confirm gate, closes the
  // modal, then saves.
  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        generalLedgerCode: f.generalLedgerCode,
        generalLedgerName: f.generalLedgerName,
        status: 'A', // ctrl:170 -- legacy hardcodes 'A' on every create
        organizationId: this.organizationId,
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          // Re-arm the confirm gate on both outcomes -- matches legacy's
          // real behavior (see class doc comment): the shared `<modal>`
          // directive's own watch re-arms `popUpFlag` on every close
          // regardless of the save's outcome.
          this.confirmArmed = true;
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:191-194, response.data.message
            this.resetForm(); // ctrl:197 -- initMaster()
            this.loadListAndCount(); // ctrl:198 -- reload ONLY on success, see class doc comment
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:202-205, response.data.message
            // No form reset, no reload on failure -- matches legacy exactly
            // (ctrl:199-206), unlike update()/discountCategoryMaster's save().
          }
        },
        // A hard HTTP error never reaches `next` at all. Legacy's own
        // `.then(function(response){...})` is single-argument (no
        // rejection handler), so it ALSO never runs on a real 500 -- no
        // growl either way. But legacy's shared `<modal>` directive still
        // independently re-arms `popUpFlag` on every modal close via its
        // own `$watch`, completely decoupled from the save promise's
        // outcome -- so the confirm gate must still be re-armed here too,
        // matching the identical, already-established correction on
        // `discountCategoryMaster`/entry 159.
        error: () => {
          this.loader.stop();
          this.confirmArmed = true;
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:217-246) ----
  edit(row: GlCodeRow): void {
    // ctrl:218-219 -- legacy flips saveBtnFlag/updateBtnFlag to "Update"
    // mode IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.loader.start();
    this.api.getById(row.generalLedgerId, this.organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.listObject?.length) {
          this.form.set({
            generalLedgerId: res.listObject[0].generalLedgerId,
            generalLedgerCode: res.listObject[0].generalLedgerCode,
            generalLedgerName: res.listObject[0].generalLedgerName,
          });
        } else {
          this.notify.error(res.message ?? 'Error!'); // ctrl:234-239, response.data.message -- real growl, not alert()
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update, ctrl:249-305 -- no confirm modal, no
  // validation, unlike save()) ----
  update(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .update({
        generalLedgerId: f.generalLedgerId as number,
        generalLedgerCode: f.generalLedgerCode,
        generalLedgerName: f.generalLedgerName,
        updatedBy: this.userId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:273-276, response.data.message
            this.form.set(emptyGlCodeForm()); // ctrl:279 -- $scope.common={}
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:281-284, response.data.message
            // Form NOT reset on failure -- matches legacy exactly (ctrl:279
            // only runs in the success branch).
          }
          // Runs UNCONDITIONALLY, success or failure (ctrl:290-296) -- also
          // the mechanism that flips saveMode back to true even on a
          // rejected update, per class doc comment.
          this.saveMode.set(true);
          this.reloadAfterUpdate();
        },
        error: () => {
          this.loader.stop();
          this.saveMode.set(true);
          this.reloadAfterUpdate();
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:307-343) ----
  toggleStatus(row: GlCodeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing --
    // never reverted below, per the class doc comment (legacy has no
    // revert branch at all for this screen).
    this.list.update((rows) => rows.map((r) => (r.generalLedgerId === row.generalLedgerId ? { ...r, status: nextStatus } : r)));
    this.loader.start();
    this.api
      .updateStatus({
        generalLedgerId: row.generalLedgerId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:323-326, response.data.message
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:331-334, response.data.message -- no revert
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // mirrors initMaster() (ctrl:37-50) as re-entered by a successful save.
  // Deliberately does NOT touch confirmArmed, see class doc comment.
  private resetForm(): void {
    this.form.set(emptyGlCodeForm());
    this.saveMode.set(true);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:161-162/252-253/309-310. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
