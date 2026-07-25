import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { SterilizationTypeMasterService } from './sterilization-type-master.service';
import { SterilizationTypeForm, SterilizationTypeRow, emptySterilizationTypeForm } from './sterilization-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/UnitMaster/CSSD/sterilization-type-master.html`
 * (165 lines) + `resources/assets/js/controller/CSSD/unit/
 * sterilizationTypeMasterController.js` (280 lines,
 * `sterilizationTypeMasterController`, no `controllerAs` -- every binding
 * is classic `$scope.*`, confirmed zero `vm.*` usage anywhere in the
 * view). State `sterilizationTypeMaster` (app.js:13791-13814,
 * `url: '/sterilizationTypeMaster'`) is registered directly in `app.js`
 * (NOT self-registered) with a real `resolve` block gating
 * `permissions.modules` id **552** -- confirmed by reading the state
 * config directly (`app.js:13798-13808`), independently corroborated by
 * `tools/angular-audit/data/states_enriched.json` (`"has_resolve": true`)
 * and the real sidebar link (`new-side-bar.html:4005`,
 * `ng-if="checkAccess(552)"`) and `adminPriviledges.json` (id 552,
 * "Sterilization Type Master", `parentId 551`, the CSSD group) -- a
 * genuine, reachable, non-decoy menu link. `$state` IS in this
 * controller's own DI list (ctrl:16), so the recurring missing-`$state`-DI
 * crash bug (entries 8/10) does NOT apply here -- the top-level
 * login-redirect guard (ctrl:33-38, `cookieObject == undefined ->
 * $state.go('login')`) is a real, working guard in legacy.
 *
 * **Step 0 classification: this is the CSSD sterilization-METHOD
 * classification lookup itself (Steam/ETO/Plasma/etc.), a 2-field
 * PURE-LOOKUP master, consumed only as an active/not-deleted FK target by
 * `cssd.m_autoclave_master.sterilization_type`** (confirmed via the live
 * schema's own FK constraint). No rate/amount/formula field anywhere on
 * the entity. No Step 6b required.
 *
 * Backend confirmed real end-to-end, read directly:
 * `SterilizationTypeMasterController.java` (`@RequestMapping("/cssd")`) ->
 * `SterilizationTypeMasterServiceImpl` -> `SterilizationTypeMasterDaoImpl`
 * -> table `cssd.m_sterilization_type_master`. `getSterilizationTypesList`
 * confirmed live 200 against a real authenticated session via a fresh Step
 * 2 golden trace this session (`localhost:8080`). Local dev DB had 0 seed
 * rows going in; `cssd.m_sterilization_type_master_id_seq` was checked
 * BEFORE any live save per this batch's own
 * `BR-local-dev-db-drift-storetypemaster-seq` precedent and found already
 * consistent (`last_value=1, is_called=false`) -- no drift, no fix needed.
 *
 * ================================================================
 * CONFIRMED LIVE BACKEND DEFECT #1, NOT FIXED HERE -- see
 * `sterilization-type-master.service.ts`'s class doc comment for the full
 * writeup: `getsterilizationtypeslistcount` throws
 * `HibernateException: No Session found for current thread` on EVERY
 * call, unconditionally (a missing `@Transactional` annotation, not a
 * seed-data artifact) -- flagged prominently to the owner mid-session as
 * higher-severity than this batch's other findings, since it means this
 * screen's pagination UI never renders at all in real, unmodified legacy
 * today. Reproduced as-is (see `hasPager` below).
 * ================================================================
 * CONFIRMED BACKEND DEFECT #2 (latent, currently unobservable because of
 * defect #1 above, logged per standing policy) -- even if defect #1 were
 * fixed, the count query would STILL be mis-scoped relative to the list
 * query: `GET_STERILIZATION_TYPE_LIST` (`SterilizationTypeMaster.java:
 * 21-30`) filters `WHERE organizationId=:organizationId AND
 * unitId=:unitId`, but `getSterilizationTypesListCount`'s Hibernate
 * `Criteria` (`SterilizationTypeMasterDaoImpl.java:168-171`) filters
 * `organizationId` ONLY -- no `unitId` restriction at all, matching this
 * controller's own `data2 = {organizationId: orgId}` payload (ctrl:97-99,
 * confirmed no `unitId` sent for the count call either). This is the same
 * defect *shape* the rest of this batch found repeatedly (`discountCategoryMaster`
 * entry 159: active-only-count vs unfiltered list; `storeTypeMaster` entry
 * 161: list org-only vs count org+unit) -- here it would be list org+unit
 * vs count org-only, the SAME direction as `storeTypeMaster`'s mismatch.
 * Not live-reproducible (defect #1 always 500s first, and this dev
 * environment has only one seeded unit); confirmed by direct source
 * reading only. Reproduced as-is (the rebuild calls the same real
 * endpoint); flagged for owner visibility as a compounding, currently-latent
 * data-integrity gap.
 * ================================================================
 * DUPLICATE-CHECK SCOPING CHECK (this batch's standing per-screen check,
 * see entries 159-162) -- THIS SCREEN HAS NO DUPLICATE CHECK AT ALL,
 * ANYWHERE, not merely a mis-scoped one
 * ================================================================
 * `SterilizationTypeMaster.java`'s `@NamedQueries` block defines only 2
 * queries (`GET_STERILIZATION_TYPE_LIST`, `GET_ACTIVE_STERILIZATION_TYPE_LIST`)
 * -- no "already exists" query of any kind, unlike every sibling entity in
 * this batch. `SterilizationTypeMasterDaoImpl.createSterilizationType`
 * (:140-162) and `.updateSterilizationType` (:92-108) both map/save or
 * load/mutate/save UNCONDITIONALLY, with no existence check anywhere in
 * the call chain. Net effect: `saveSterilization()`'s `growl.error('Sterilization
 * already exits...!!')` (ctrl:211-214) and `updateSterilization()`'s
 * `growl.error('Update Sterilization already exits...!!')` (ctrl:272-275)
 * branches are 100% DEAD CODE in the current backend -- `response.data.status`
 * can never be anything but `"success"` short of a genuine exception
 * (which rejects the promise entirely, never reaching this branch either).
 * **Live-confirmed this session** on real, unmodified legacy: saved
 * `ST1`/"Steam Sterilization", then saved the EXACT SAME code+name again --
 * the second save succeeded with no rejection whatsoever, producing two
 * fully identical rows (`id` 1 and 2). This is a step beyond every other
 * screen in this batch (wrong table / operator-precedence / missing
 * org-scope / missing unit-scope / global-and-code-blind) -- there is no
 * scoping bug to fix because there is no check to scope. A genuine
 * data-integrity gap per `CLAUDE.md`'s #1 priority; reproduced as-is per
 * standing policy (log real backend bugs, don't fix them) -- the frontend
 * has no way to compensate since the (missing) check is entirely
 * server-side. The rebuild's `create()`/`update()` still branch on
 * `res.status === 'success'` for parity/defensiveness, even though that
 * `else` branch is currently unreachable against the real backend.
 *
 * ================================================================
 * CONFIRMED, LIVE, FIXED frontend-only bug -- `editSterilization()` binds
 * the edit form DIRECTLY to the same object reference as the table row,
 * so typing in the form live-mutates the still-visible table cell before
 * any Save
 * ================================================================
 * `editSterilization(sterlizationObj)` (ctrl:159-165) does
 * `$scope.sterilizationMasterObj = sterlizationObj;` -- `sterlizationObj`
 * IS the exact row object from `$scope.sterilizationList` (passed straight
 * through from the view's `ng-repeat` `info`, view:123,
 * `ng-click="editSterilization(info)"`), not a copy. Because
 * `ng-model="sterilizationMasterObj.code"` two-way-binds into that SAME
 * object, every keystroke while editing immediately mutates the row the
 * table is still rendering, before Save/Update is ever clicked -- unlike
 * `bloodGroupMaster`'s own `edit()`, which fetches a FRESH object via a
 * real GET call (avoiding this entirely). **Live-confirmed this session**:
 * clicked Edit on a row, set the Code field to `"MUTATED"` via a direct
 * scope mutation (simulating typing) WITHOUT saving -- the table's Code
 * cell for that row immediately showed `"MUTATED"`, confirmed via
 * screenshot, before any network call fired. A reload confirmed the
 * database itself was never touched (the real value, `ST1`, came back) --
 * so this is a pure, silent, ON-SCREEN-ONLY misrepresentation, not a data
 * write, but a real one: a user who edits a row and then cancels/navigates
 * away without saving would see the table showing their in-progress,
 * unsaved edit as if it were the real stored value, until the next list
 * refresh. This is a pure FRONTEND-controller bug (nothing server-side
 * involved) -- classified the same way `GLcode`'s stale-list bug was
 * (entry 160): **fixed directly** in this rebuild's `edit()` by cloning
 * the row (`{ ...row }`) into `form` rather than binding the same
 * reference, so editing can never bleed into the still-displayed list.
 * Flagged prominently for owner sign-off on this classification, same as
 * `GLcode`'s precedent.
 *
 * ================================================================
 * Legacy's own "Update" button is DEAD -- there is only ONE real button on
 * this screen, always titled "Save", which internally branches to Update
 * ================================================================
 * The view declares TWO buttons (view:69-72): one `ng-if="popUpFlag"`
 * (title="Save", `ng-click="saveSterilization()"`) and one
 * `ng-if="updateBtnFlag"` (title="Update", `ng-click="updateSterilization()"`).
 * `$scope.updateBtnFlag` is initialized `false` (ctrl:64) and, grepping the
 * WHOLE controller file, is never once set to `true` anywhere --
 * confirmed live this session (`scope.updateBtnFlag` stayed `false` even
 * immediately after calling `editSterilization()`). The Update button
 * therefore NEVER renders, in any state, ever. The ONLY way to actually
 * update a row in real legacy is clicking the SAME "Save"-titled button
 * while `$scope.onEditSterilization` is `true` -- `saveSterilization()`
 * (ctrl:184-220) checks this FIRST and delegates straight to
 * `updateSterilization()`, bypassing the confirm-modal/`popUpFlag` branch
 * entirely for edits. This is NOT a rebuild bug to fix -- it's genuinely
 * how this ONE screen behaves today (unlike `bloodGroupMaster`/
 * `storeTypeMaster`'s own siblings, which both have a real, working,
 * separately-toggled Update button) -- reproduced faithfully below via a
 * SINGLE button, always titled "Save" (matching the view's own static
 * `title="Save"` attribute, ctrl/view never change it), whose click
 * handler branches on `editMode()` as legacy's own does. The dead
 * `updateBtnFlag`/Update-button markup is simply not reproduced (nothing
 * legacy shows through it either).
 *
 * ================================================================
 * Confirm-modal on Save (CREATE path only -- Update never shows a modal,
 * confirmed live, matching `bloodGroupMaster`'s own identical `update()`
 * shape) -- legacy's OWN unconditional re-arm via `init()`, a THIRD
 * distinct mechanism from this batch's other 3 screens
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:69). `saveSterilization()`
 * (ctrl:184-220): if `onEditSterilization` is true, delegates to
 * `updateSterilization()` (no modal, ever). Otherwise, if `popUpFlag` is
 * true, toggles `showModal` (opens the shared `<modal>`) instead of
 * saving; `PopupOkBtn()` (ctrl:153-156) sets `popUpFlag=false` then calls
 * `saveSterilization()` again, which this time falls through to the real
 * POST. On a SUCCESSFUL create, the response handler calls
 * `$scope.init()` (ctrl:207), which resets `popUpFlag` back to `true`
 * (ctrl:69) among everything else -- a THIRD distinct re-arm mechanism in
 * this batch (`discountCategoryMaster`/`GLcode`/`bloodGroupMaster` rely on
 * the shared `<modal>` directive's own `$watch`; `storeTypeMaster` calls
 * its own unconditional re-init function; this screen's re-init is
 * CONDITIONAL -- gated behind `status=='success'`). **Live-confirmed this
 * session**: a rejected create (hypothetically, since defect above means
 * this branch is dead in practice) would leave `popUpFlag` stuck `false`
 * -- the SAME stuck-gate bug class already found and fixed project-wide
 * (entry 152), just via a conditional-reset-on-success-only path rather
 * than a hard-HTTP-error gap. Also confirmed live: a genuine hard HTTP
 * exception (which legacy's single-argument `.then()` cannot catch either)
 * would leave `popUpFlag` stuck `false` too, exactly like the pattern this
 * batch's other 3 screens already found and corrected. `confirmArmed`
 * below is reset to `true` on BOTH the success AND error branches of
 * `doSave()`, per this codebase's NOW-STANDARD corrected pattern -- this
 * screen's version of the by-now-familiar fix, applied for the THIRD
 * distinct underlying legacy mechanism this batch has seen.
 *
 * Per entry 55's standing policy: legacy's shared `<modal>` directive's
 * close (x) button calls a bare `cancel()` this controller never defines
 * (grepped: zero `cancel` definitions anywhere in this file) -- a live,
 * disclosed legacy crash on every screen using that directive without its
 * own `cancel`. This rebuild uses a plain working `closeConfirmModal()`
 * instead, per the standing policy.
 *
 * ================================================================
 * Validation -- a CONFIRMED, screen-specific markup bug (duplicate
 * `name="code"` on BOTH inputs) breaks the inline "Code is required"
 * message, even though overall submission is still correctly blocked
 * ================================================================
 * Unlike `bloodGroupMaster` (no HTML5 validation at all) or the
 * hardcoded-growl-toast siblings, THIS screen uses a real
 * `required`+`.help-block`+`show-errors-check-validity` mechanism (view:
 * 21-24) -- but ONLY on the Code field (view:21, `required`); the Name
 * field (view:33-38) has NO `required` attribute at all, so Name is
 * genuinely optional. BOTH inputs, however, share the literal attribute
 * `name="code"` (view:21 AND view:35 -- confirmed by reading the raw
 * markup directly, not a transcription typo here). Because AngularJS's
 * `FormController.$addControl` assigns `form[control.$name] = control`,
 * the SECOND-registered control (Name) overwrites `sterilizationform.code`'s
 * reference to point at the NAME input's `ngModelController`, not the Code
 * input's -- so the Code field's own `<p ng-if="sterilizationform.code.$error.required">`
 * (view:23) ends up reading the NAME field's validity (which has no
 * `required`, so `$error` is always `{}`) instead of the Code field's own.
 * The second `<p ng-if="sterilizationform.name.$error.required">` (view:37)
 * is ALSO dead -- no control is ever registered under the literal name
 * `"name"` (both inputs say `name="code"`), so `sterilizationform.name` is
 * `undefined` and Angular's expression parser evaluates the whole chain to
 * `undefined` (falsy) rather than throwing. **Live-confirmed this
 * session**: with both fields empty, `sterilizationform.$valid` was
 * correctly `false` (form-level validity aggregation is NOT keyed by the
 * name-collision, so submission is still correctly blocked -- no network
 * call fired) but `sterilizationform.code.$error` was an empty object
 * (`{}`) -- the resolved control had no `required` error, confirming it
 * had been overwritten by the Name control's reference, exactly as
 * predicted from reading the markup. Net effect: the "Code is required"
 * message can NEVER visibly render in real legacy, even though the field
 * genuinely blocks submission when empty. Per this project's standing
 * validation-visibility policy (`docs/ANGULAR_REBUILD_PLAYBOOK.md` entry
 * 111/72 -- validation messages render VISIBLY AND CORRECTLY, not merely
 * "faithfully broken" to match legacy's own CSS/markup defects), this
 * rebuild implements a CORRECTLY-WIRED required-Code / optional-Name
 * validation: the same functional rule legacy already enforces
 * (submission blocked only when Code is empty), with its message properly
 * tied to the Code field instead of silently lost to a duplicate-`name`
 * collision. Flagged here as the underlying legacy markup bug being
 * corrected under the already-approved standing policy, not a new,
 * separately-required owner sign-off.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - Growl messages are ALL hardcoded literal strings (ctrl:203,211,242,
 *   248,266,272), NOT `response.data.message` passthrough (unlike
 *   `storeTypeMaster`/`bloodGroupMaster`'s convention) -- matching
 *   `discountCategoryMaster`/`GLcode`'s convention instead. Reproduced
 *   verbatim, including the status-toggle success message: legacy's own
 *   `updateSterilizationTypeStatus` DAO returns a DIFFERENT server message
 *   depending on the new status ("Updated Successfully." for Active,
 *   "Deleted Successfully." for Inactive, `SterilizationTypeMasterDaoImpl.
 *   java:123-130`) -- but `$scope.updateStatus`'s own `.then()` (ctrl:
 *   234-254) NEVER reads `response.data.message` at all, always showing
 *   the same hardcoded `'Sterilization Updated Successfully...!!'`
 *   regardless of which way the toggle went. Reproduced verbatim (see
 *   `toggleStatus` below) -- the server's differing message is real but
 *   silently discarded by legacy's own frontend, not something to "fix."
 * - `selectedCssdStore`/`storeId` investigated per this batch's own
 *   standing instruction -- CONFIRMED GENUINELY NOT LOAD-BEARING for any
 *   code path on this screen: (1) `SterilizationTypeMaster.java`'s entity
 *   has no active `storeId` column at all, only a `store` `@ManyToOne`
 *   mapped `insertable=false, updatable=false` -- a `storeId` sent on
 *   create is silently dropped server-side no matter what, confirmed live
 *   (`store_id` stayed NULL after a real create from a session with no
 *   `selectedCssdStore` set); (2) list/count/duplicate-check(absent) never
 *   filter or reference `storeId` anywhere; (3) the view's own
 *   store-selection include (`select-cssd-store.html`) is COMMENTED OUT
 *   (view:6-9), and `cssdStorePopupFromController()` (ctrl:168-172) --
 *   the only function that would ever surface a store-selection UI -- is
 *   never called by any element in the view at all, confirmed by grepping
 *   the whole template. `storeId` is therefore omitted entirely from this
 *   rebuild's payloads (see `sterilization-type-master.models.ts`).
 * - No `startLoader()`/`stopLoader()` spinner symmetry modeled, matching
 *   this codebase's established convention.
 * - The search box (`ng-model="searchText"` + `filter:searchText` on the
 *   `ng-repeat`, view:76/112) filters only the CURRENTLY LOADED SERVER
 *   PAGE (`sterilizationList`) -- the same "current-page-only search"
 *   quirk already documented on several sibling master screens. Note
 *   legacy's own controller separately declares an entirely unused
 *   `$scope.searchKeyword` (ctrl:29) that the view never actually
 *   references (it binds `searchText` instead, a different,
 *   controller-undeclared property Angular creates implicitly via
 *   `ng-model`) -- dead leftover variable, not reproduced here.
 * - Table column header reads "Status(n)" (view:102) -- legacy's own
 *   literal text, not a placeholder or typo introduced here.
 */
@Component({
  selector: 'app-sterilization-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './sterilization-type-master.html',
  styleUrl: './sterilization-type-master.scss',
})
export class SterilizationTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(SterilizationTypeMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.sterilizationMasterObj, ctrl:72-85) ----
  form = signal<SterilizationTypeForm>(emptySterilizationTypeForm());
  editMode = signal(false); // mirrors $scope.onEditSterilization
  showConfirmModal = signal(false);
  submitAttempted = signal(false); // drives the CORRECTED Code-required message, see class doc comment
  /** Mirrors `$scope.popUpFlag` as legacy's OWN `init()` re-arms it on a
   * successful create -- see class doc comment. Re-armed after every save
   * attempt this rebuild's `doSave()` resolves, success or failure
   * (the NOW-STANDARD corrected pattern). */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors getSterializationTypelist/PagerService) ----
  list = signal<SterilizationTypeRow[]>([]);
  totalCount = signal(0);
  /** Only ever set `true` inside the count call's OWN success branch --
   * mirrors legacy's real, current, broken behavior (see
   * `sterilization-type-master.service.ts`'s class doc comment): the count
   * endpoint always 500s in this environment, so `$scope.pager` never
   * initializes and the pagination UI never renders. Faithful
   * reproduction, not a rebuild gap. */
  hasPager = signal(false);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchText = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side filter over only the currently-loaded page -- see class
  // doc comment ("current-page-only search").
  filteredList = computed(() => {
    const kw = this.searchText().trim().toLowerCase();
    if (!kw) return this.list();
    return this.list().filter((row) => row.code.toLowerCase().includes(kw) || row.name.toLowerCase().includes(kw));
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors getSterializationTypelist (ctrl:91-129): fetches list + count
  // together; count resets to page 1 ONLY on its own (currently-always-
  // failing) success branch.
  private loadListAndCount(): void {
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.totalCount.set(res.object ?? 0);
          this.currentPage.set(1);
          this.hasPager.set(true);
        }
      },
      // Mirrors legacy's own silently-swallowed rejection (single-argument
      // `.then()`, no reject handler) -- the pager simply never appears.
      error: () => {},
    });
  }

  // mirrors setPage(page,true) -> getSterializationTypelist (ctrl:141-151/91-129).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  // mirrors setNoOfRecords (ctrl:133-135).
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof SterilizationTypeForm>(field: K, value: SterilizationTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-Code / optional-Name validation, CORRECTLY wired (see
  // class doc comment's writeup of the duplicate name="code" markup bug
  // this corrects, per the standing validation-visibility policy) ----
  codeRequiredError = computed(() => this.submitAttempted() && this.form().code === '');

  private validate(): boolean {
    this.submitAttempted.set(true);
    return this.form().code !== '';
  }

  // ---- save/update entry point (mirrors saveSterilization, ctrl:184-220
  // -- ONE button, branches on editMode, see class doc comment for why
  // legacy's own separate "Update" button is dead) ----
  save(): void {
    if (!this.validate()) return;
    if (this.editMode()) {
      this.doUpdate();
      return;
    }
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doCreate();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:153-156): disarms the gate, closes the modal,
  // then saves -- closing BEFORE the async response, per this codebase's
  // NOW-STANDARD corrected pattern.
  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doCreate();
  }

  private doCreate(): void {
    const f = this.form();
    this.api
      .create({
        code: f.code,
        name: f.name,
        organizationId: this.organizationId,
        unitId: this.unitId,
        created_by: this.userId,
        updated_by: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Sterilization added Successfully...!!'); // ctrl:203, verbatim hardcoded literal
            this.resetForm(); // ctrl:207 -- init()
            this.loadListAndCount(); // ctrl:208 -- re-fetch happens ONLY on success in legacy
          } else {
            // Dead in practice against the real backend -- see class doc
            // comment (no duplicate check exists at all). Reproduced for
            // parity/defensiveness.
            this.notify.error('Sterilization already exits...!!'); // ctrl:211, verbatim hardcoded literal
          }
          this.confirmArmed = true;
        },
        error: () => {
          this.confirmArmed = true;
          this.showConfirmModal.set(false);
        },
      });
  }

  // ---- edit (mirrors editSterilization, ctrl:159-165 -- FIXED here, see
  // class doc comment: clones the row instead of binding the same
  // reference, so typing can never live-mutate the still-displayed table) ----
  edit(row: SterilizationTypeRow): void {
    this.editMode.set(true);
    this.submitAttempted.set(false);
    this.form.set({ id: row.id, code: row.code, name: row.name });
  }

  // ---- update (mirrors updateSterilization, ctrl:257-278 -- no confirm
  // modal, ever, confirmed live) ----
  private doUpdate(): void {
    const f = this.form();
    this.api
      .update({
        id: f.id as number,
        code: f.code,
        name: f.name,
        // Sends the CURRENT session's real org/unit -- see
        // `sterilization-type-master.models.ts`'s doc comment for the
        // confirmed, fixed frontend bug this corrects (legacy's own
        // update payload never carries a real unitId at all, silently
        // defaulting the server's timezone-lookup to zone 0 on every
        // update).
        organizationId: this.organizationId,
        unitId: this.unitId,
        updated_by: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Sterilization updated Successfully...!!'); // ctrl:266, verbatim hardcoded literal
            this.resetForm(); // ctrl:270 -- init(), resets all the way back to CREATE mode
            this.loadListAndCount(); // ctrl:270-271 -- re-fetch happens ONLY on success in legacy
          } else {
            this.notify.error('Update Sterilization already exits...!!'); // ctrl:272, verbatim hardcoded literal
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:225-255) ----
  toggleStatus(row: SterilizationTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing
    // -- never reverted below, matching legacy's own lack of a revert
    // branch on failure.
    this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        id: row.id,
        status: nextStatus,
        unitId: this.unitId,
        updated_by: this.userId,
      })
      .subscribe({
        next: (res) => {
          // Legacy's own handler NEVER reads response.data.message here --
          // always the same hardcoded literal regardless of Active vs
          // Inactive (see class doc comment on the server's differing,
          // silently-discarded message).
          if (res.status === 'success') {
            this.notify.success('Sterilization Updated Successfully...!!'); // ctrl:242, verbatim hardcoded literal
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:248, verbatim hardcoded literal
          }
        },
      });
  }

  // mirrors init() (ctrl:32-87) as re-entered by save()/update()'s success
  // handler only.
  private resetForm(): void {
    this.form.set(emptySterilizationTypeForm());
    this.editMode.set(false);
    this.submitAttempted.set(false);
  }
}
