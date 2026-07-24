import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { StoreTypeMasterService } from './store-type-master.service';
import { StoreTypeForm, StoreTypeRow, emptyStoreTypeForm } from './store-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/UnitMaster/Inventory/store-type-master.html`
 * (149 lines) + `resources/assets/js/controller/master/storeTypeController.js`
 * (309 lines, `storeTypeController`, no `controllerAs` -- every binding is
 * classic `$scope.*`, confirmed zero `vm.*` usage in the view). State
 * `storeTypeMaster` (app.js:18820-18847, `url: '/storeTypeMaster'`) is
 * registered directly in `app.js` (NOT self-registered) with a real
 * `resolve` block gating on `permissions.modules` id **805** -- read
 * directly from app.js, not inferred. `data.moduleId` below is therefore
 * 805. Independently corroborated: the real sidebar entry
 * (`views/common/new-side-bar.html:3677`, `ng-if="checkAccess(805)"`,
 * `href="#/storeTypeMaster"`) confirms this is a genuine, reachable,
 * non-decoy menu link, not a dead route. No `requireSuperUser` check in
 * the resolve block.
 *
 * `$state` IS in this controller's own DI list (ctrl:7), so the recurring
 * missing-`$state`-DI crash bug (entries 8/10) does NOT apply here -- the
 * `initStoreTypeMaster()` login-redirect guard (ctrl:19-25, `cookieObject
 * == undefined -> $state.go('login')`) is a real, working guard in
 * legacy.
 *
 * Backend confirmed real end-to-end, read directly AND exercised live
 * against the running dev backend (not trusted from a lighter prior
 * pass): `StoreTypeMasterController.java` (`@RequestMapping("/api/global")`)
 * -> `StoreTypeMasterServiceImpl` -> `StoreTypeMasterDaoImpl` -> table
 * `public.m_store_type`. `getStoreTypeList`/`getStoreTypeCount` confirmed
 * 200 with the 6 real seed rows (Main/Sub/Consignment/CSSD/Linen/
 * Narcotics Store); `getStoreTypeById`, `updateStoreType` (incl. its
 * duplicate-code-plus-type guard), and `updateStoreTypeStatus` all
 * exercised live via direct scope invocation against the legacy app
 * itself (browser-automation coordinate clicks on the pencil/toggle icons
 * were unreliable in this session -- see Step 5 notes -- direct
 * `angular.element(...).scope()` invocation was used as the documented
 * workaround, matching the prior two screens' approach) and reverted
 * immediately after, leaving the dev DB's 6 seed rows unchanged.
 *
 * ================================================================
 * CONFIRMED BACKEND DEFECT, NOT FIXED HERE -- see
 * `store-type-master.service.ts`'s class doc comment for the full
 * writeup: list/by-id/duplicate-check are ORGANIZATION-scoped only (the
 * `unitId` clause is commented out in all 3 named queries), while
 * `getStoreTypeCount` IS both organization- AND unit-scoped. Reproduced
 * as-is; flagged in this batch's findings entry as a billing/inventory-
 * data-integrity gap, not merely cosmetic, since it can leak rows across
 * units within the same organization and/or under/over-report the pager.
 * ================================================================
 *
 * ================================================================
 * Confirm-modal on Save -- legacy's OWN unconditional re-arm (not the
 * shared-`<modal>`-directive mechanism entry 152 found on
 * `discountCategoryMaster`/`GLcode`)
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:45), gates whether clicking Save
 * opens the shared `<modal>` (`showModal = !showModal`, ctrl:162-163) or
 * actually POSTs (ctrl:165-190). Unlike the sibling screens built earlier
 * in this batch, THIS controller's own `save()`/`update()` response
 * handlers unconditionally call `$scope.initStoreTypeMaster()`
 * (ctrl:186/264) *outside* the success/error branching -- i.e. on every
 * response the promise actually resolves with (success OR a business
 * "error" status) -- which resets `popUpFlag` back to `true`, `showModal`
 * to `false`, AND the whole form back to empty, in legacy's own code,
 * with no dependency on the shared directive's `$watch` mechanism.
 * Confirmed live: forcing a real duplicate (code+type both matching an
 * existing row) through `save()` closed the modal and re-armed the gate
 * exactly the same as a real success. Reproduced below via
 * `confirmArmed`/`showConfirmModal` both being reset in `doSave()`'s
 * `next` handler unconditionally (mirrors ctrl:186 exactly).
 *
 * The one gap legacy's own code still has (and this rebuild corrects, per
 * this codebase's standing "NOW-STANDARD corrected pattern" -- see
 * `discountCategoryMaster`/`GLcode`'s own class doc comments): a genuine
 * hard HTTP failure (5xx/network error) never reaches legacy's
 * single-argument `.then(function(response){...})` at all (ctrl:167-187),
 * so `initStoreTypeMaster()` never runs and the modal/gate would stay
 * stuck open in real legacy too. The `error` handler below re-arms
 * `confirmArmed` and closes `showConfirmModal` the same way `doSave()`'s
 * `next` handler does, so a hard failure here can't permanently stick the
 * confirm modal shut -- matching the corrected pattern applied to every
 * other screen in this batch.
 *
 * `update()` (ctrl:232-271) has the SAME unconditional-reset shape as
 * `save()` -- notably, after either a successful Update OR a rejected
 * (duplicate) Update, legacy resets the whole screen back to CREATE mode
 * (`saveBtnFlag=true`/`updateBtnFlag=false`), not just back to a blank
 * Update form. Reproduced verbatim via `resetForm()` being called
 * unconditionally from `update()`'s `next` handler too, not gated on
 * `res.status`.
 *
 * Per entry 55's standing policy: legacy's shared `<modal>` directive's
 * close (x) button calls a bare `cancel()` this controller never defines
 * (grepped: zero `cancel` definitions anywhere in this file) -- a live,
 * disclosed legacy crash on every screen using that directive without its
 * own `cancel`. This rebuild uses a plain working `closeConfirmModal()`
 * instead, per the standing policy (not re-litigated further here).
 *
 * ================================================================
 * Validation: identical `storeTypeForm.$valid` gate on BOTH save() and
 * update() (ctrl:160/243) -- a single HTML5-`required` + `show-errors`
 * mechanism, no duplicate manual JS validation layered on top (unlike the
 * `insuranceMaster` pattern)
 * ================================================================
 * Per this codebase's standing policy (entry 111/batch-11), the
 * validation messages below render VISIBLY by default -- legacy's own
 * `.help-block{display:none!important}` CSS defect is NOT reproduced.
 * Unlike `discountCategoryMaster`, legacy's own help-block text here is
 * NOT a copy-paste bug -- "Code is required" (view:14) and "Description
 * is required" (view:28) are each correct for their own field, so no
 * verbatim-bug text to preserve.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - No `startLoader()`/`stopLoader()` symmetry: legacy calls
 *   `$rootScope.startLoader()` before every POST but only calls
 *   `stopLoader()` inside the response handler's OWN branches (ctrl:166-
 *   171/180-185, etc.) -- functionally the same as calling it
 *   unconditionally, since every branch that can be reached does call it,
 *   so no divergence to reproduce beyond simply not showing a spinner
 *   this rebuild doesn't otherwise model.
 * - `save()`/`update()`/`updateStatus()` all show `response.data.message`
 *   verbatim via `growl.success`/`growl.error` -- UNLIKE
 *   `discountCategoryMaster`/`GLcode`, which hardcode literal strings.
 *   Reproduced via `this.notify.success(res.message ?? '')`/
 *   `this.notify.error(res.message ?? '')`, passing the real server
 *   message through rather than a hardcoded literal. Server-side
 *   constants (`ICommonConstants.java`, read directly): save success
 *   = "Added Successfully.", update/status success = "Updated
 *   Successfully.", save/update duplicate = "Already Exists." A genuine
 *   unhandled server exception (not a business "already exists" rejection)
 *   returns `message: null` in the outer catch-all response
 *   (`Response(ERROR, ERROR_CODE, null, null, null)`) -- `res.message` is
 *   `null`/`undefined` in that case, reproduced as an empty-string notify
 *   rather than inventing text legacy never shows either.
 * - `showUpdateBtn` (ctrl:198-229) flips to Update mode IMMEDIATELY on
 *   click, before the POST even resolves (ctrl:201-202) -- reproduced by
 *   `saveMode.set(false)` at the very top of `edit()`.
 * - Status toggle (`updateStatus`, ctrl:273-306): the view's checkbox
 *   (`ng-model="info.isActive"`, `ng-true-value="true"`,
 *   `ng-false-value="false"`, `ng-change="updateStatus(...)"`) flips
 *   optimistically client-side before the request resolves, and legacy's
 *   handler has NO revert branch on failure (only a growl toast).
 *   Reproduced via `<app-status-toggle>` + an unconditional optimistic
 *   `list.update()`, never reverted regardless of response status.
 * - The search box (`ng-model="searchKeyword"` + `filter:searchKeyword`
 *   on the `ng-repeat`, view:47/96) filters only the CURRENTLY LOADED
 *   SERVER PAGE (`commonList`, populated fresh per paginated fetch), same
 *   "current-page-only search" quirk already documented on several
 *   sibling master screens. Reproduced via `filteredList` over `list()`.
 * - The table header's "Status(n)" label (view:83) is legacy's own
 *   literal text, not a placeholder or typo introduced here.
 */
@Component({
  selector: 'app-store-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './store-type-master.html',
  styleUrl: './store-type-master.scss',
})
export class StoreTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(StoreTypeMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common, ctrl:48-56) ----
  form = signal<StoreTypeForm>(emptyStoreTypeForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. Re-armed after
   * every save attempt legacy's own code resolves (success or business
   * error), matching legacy's real unconditional `initStoreTypeMaster()`
   * call; ALSO re-armed on a hard HTTP failure, which is this rebuild's
   * one deliberate correction over a literal port (see doc comment). */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initStoreTypeMasterList/PagerService) ----
  list = signal<StoreTypeRow[]>([]);
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
    return this.list().filter((row) => row.code.toLowerCase().includes(kw) || row.type.toLowerCase().includes(kw));
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initStoreTypeMasterList (ctrl:67-107): fetches list + count
  // together and resets to page 1 once the count responds.
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

  // mirrors getStoreTypeList (ctrl:110-129) via setPage(page,true).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.list.set(res.listObject ?? []);
      },
    });
  }

  // mirrors setNoOfRecords (ctrl:63-65).
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof StoreTypeForm>(field: K, value: StoreTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors storeTypeForm.$valid via the 2
  // `required` inputs -- code, type, view:12,26) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.code.trim() && f.type.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().code.trim();
  }

  isTypeInvalid(): boolean {
    return this.formSubmitted() && !this.form().type.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:149-196) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // matches storeTypeForm.$valid gate, ctrl:160
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:149-152): disarms the confirm gate, closes
  // the modal, then saves.
  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    this.api
      .save({
        code: f.code,
        type: f.type,
        addedBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? ''); // ctrl:172-175, verbatim server message
            this.loadListAndCount(); // ctrl:177 -- re-fetch happens ONLY on success in legacy
          } else {
            this.notify.error(res.message ?? ''); // ctrl:181-184, verbatim server message ("Already Exists.")
          }
          // Re-arm the confirm gate and reset the form unconditionally --
          // matches legacy's own real behavior (see class doc comment):
          // `initStoreTypeMaster()` (ctrl:186) runs regardless of
          // success/error, but the list re-fetch above does not.
          this.confirmArmed = true;
          this.resetForm();
        },
        // A hard HTTP error never reaches legacy's own single-argument
        // `.then()` (ctrl:167-187) either -- this is this rebuild's one
        // deliberate correction (see class doc comment): re-arm the gate
        // and close the modal so a genuine backend exception can't
        // permanently stick this screen's confirm modal shut.
        error: () => {
          this.confirmArmed = true;
          this.showConfirmModal.set(false);
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:198-229) ----
  edit(row: StoreTypeRow): void {
    // ctrl:200-201 -- legacy flips saveBtnFlag/updateBtnFlag to "Update"
    // mode IMMEDIATELY on click, before the POST even resolves.
    this.saveMode.set(false);
    this.api.getById(row.storeTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          this.form.set({
            storeTypeId: res.listObject[0].storeTypeId,
            code: res.listObject[0].code,
            type: res.listObject[0].type,
          });
        } else {
          this.notify.error(res.message ?? ''); // ctrl:217-221 -- growl.error, verbatim server message
        }
      },
    });
  }

  // ---- update (mirrors update, ctrl:232-271 -- same storeTypeForm.$valid
  // gate as save(), no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // ctrl:243, same $valid gate as save()
    const f = this.form();
    this.api
      .update({
        storeTypeId: f.storeTypeId as number,
        code: f.code,
        type: f.type,
        addedBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? ''); // ctrl:250-253, verbatim server message
            this.loadListAndCount(); // ctrl:255 -- re-fetch happens ONLY on success in legacy
          } else {
            this.notify.error(res.message ?? ''); // ctrl:258-261, verbatim server message ("Already Exists.")
          }
          // ctrl:264 -- initStoreTypeMaster() runs unconditionally here
          // too, resetting all the way back to CREATE mode (not just
          // clearing the Update form), reproduced verbatim.
          this.resetForm();
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:273-306) ----
  toggleStatus(row: StoreTypeRow): void {
    const nextActive = !row.isActive;
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing
    // -- never reverted below, per the class doc comment (legacy has no
    // revert branch at all for this screen).
    this.list.update((rows) => rows.map((r) => (r.storeTypeId === row.storeTypeId ? { ...r, isActive: nextActive } : r)));
    this.api
      .updateStatus({
        storeTypeId: row.storeTypeId,
        isActive: nextActive,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? ''); // ctrl:289-292, verbatim server message
          } else {
            this.notify.error(res.message ?? ''); // ctrl:297-300, verbatim server message -- no revert
          }
        },
      });
  }

  // mirrors initStoreTypeMaster() (ctrl:19-58) as re-entered
  // unconditionally by save()/update()'s response handler.
  private resetForm(): void {
    this.form.set(emptyStoreTypeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }
}
