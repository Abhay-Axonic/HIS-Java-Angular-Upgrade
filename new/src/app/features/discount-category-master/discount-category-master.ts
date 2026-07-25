import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DiscountCategoryMasterService } from './discount-category-master.service';
import { DiscountCategoryForm, DiscountCategoryRow, emptyDiscountCategoryForm } from './discount-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/UnitMaster/billingMaster/discount-Category-Master.html`
 * (154 lines) + `resources/assets/js/controller/billing/unit/discountCategoryMasterController.js`
 * (362 lines, `discountCategoryMasterController`, no `controllerAs` --
 * every binding is classic `$scope.*`, confirmed zero `vm.*` usage in the
 * view). State `discountCategoryMaster` (app.js:19910-19934,
 * `url: '/discountCategoryMaster'`) is registered directly in `app.js`
 * (NOT self-registered) with a real `resolve` block gating on
 * `permissions.modules` id **973** -- read directly from app.js, not
 * inferred. `data.moduleId` below is therefore 973. Independently
 * corroborated: the real sidebar entry (`views/common/new-side-bar.html:4199`,
 * `ng-if="checkAccess(973)"`, `href="#/discountCategoryMaster"`, under the
 * Billing group) and `adminPriviledges.json` id 973 ("Discount Category
 * Master", `parentId 570`) both confirm this is a genuine, reachable,
 * non-decoy menu link, not a dead route. No `requireSuperUser` check in
 * the resolve block.
 *
 * `$state` IS in this controller's own DI list (ctrl:11-24), so the
 * recurring missing-`$state`-DI crash bug (entries 8/10) does NOT apply
 * here -- the `init()` login-redirect guard (ctrl:28-34, `cookieObject ==
 * undefined -> $state.go('login')`) is a real, working guard in legacy.
 *
 * Backend confirmed real end-to-end, read directly (not trusted from a
 * lighter prior pass): `DiscountTypeMasterController.java`
 * (`@RequestMapping("/api/global/unit")`) -> `DiscountTypeMasterServiceImpl`
 * -> `DiscountTypeMasterDaoImpl` -> table `public.m_discount_category`
 * (raw SQL, `DiscountTypeMasterDaoImpl.java:108-177`). A same-package
 * `DiscountCategoryMasterServiceImpl.java` also exists but is confirmed an
 * empty, unused stub class (zero methods) -- dead code, not part of this
 * chain.
 *
 * ================================================================
 * CONFIRMED BACKEND DEFECT, NOT FIXED HERE (legacy bug, logged, not a
 * rebuild bug) -- the duplicate-category check queries the WRONG TABLE
 * ================================================================
 * `save()`/`update()`'s server-side "already exists" check
 * (`DiscountTypeMasterServiceImpl.java:23-34/37-48`,
 * `getDiscountCategoryExistOrNotByName(NotById)`) runs a Hibernate named
 * query (`GET_DISCOUNT_CATEGORY_EXIST_OR_NOT_BY_NAME(_NOT_BY_ID)`,
 * defined on the **`DiscountTypeMaster`** entity,
 * `legacy/Entity/.../adt/master/unit/model/DiscountTypeMaster.java:14-45`,
 * mapped to table **`m_discount_type`**) -- a genuinely DIFFERENT table
 * from `m_discount_category`, the table `saveDiscountCategory`/
 * `updateDiscountCategory`/`getDiscountCategoryList`/`getDiscountCategoryById`
 * actually read from and write to (`DiscountTypeMasterDaoImpl.java:28-34`,
 * entity `com.param.entity.model.master.DiscountCategory`). Both tables are
 * real, both exist in the live schema (confirmed via `\d` against the local
 * dev DB), and neither is a view/alias of the other. Net effect: the
 * "Discount category already exists" duplicate guard this screen visibly
 * displays to the user can never actually detect a real duplicate created
 * through THIS screen, because it checks a table this screen never writes
 * to. A DAO code comment (`DiscountTypeMasterDaoImpl.java:40`, "As
 * discussed with divyesh, purva, changed discount type to discount
 * category and naming convention is same for UI") indicates this is a
 * leftover from an incomplete table migration/rename, not a deliberate
 * design. This is a genuine legacy backend bug -- reproduced as-is per
 * this project's standing policy (log real bugs, don't silently fix
 * backend logic during a parity rebuild); the frontend has no way to
 * compensate for it since the check happens entirely server-side. Flagged
 * prominently in this batch's findings entry for owner visibility, since
 * it is a billing-data-integrity gap, not merely cosmetic.
 *
 * ================================================================
 * Two-click confirm on Save (and Update) -- `confirmArmed` re-arms on
 * failure too, matching incident-category-master.ts's confirmSave()/
 * doSave() shape exactly. NOT a legacy-bug fix -- this MATCHES legacy's
 * real (non-buggy) behavior, per the corrected understanding in
 * docs/ANGULAR_PENDING_FINDINGS.md entry 152's "Major update"
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:54), gates whether clicking Save
 * opens the shared `<modal>` (`showModal = !showModal`, ctrl:199-202) or
 * actually POSTs (ctrl:203-234). Read in isolation, this screen's OWN
 * code only ever resets `popUpFlag` back to `true` from `init()`
 * (ctrl:28-66), which is only re-entered on a SUCCESSFUL save
 * (ctrl:221-223) -- naively suggesting a failed save (e.g. a
 * duplicate-code rejection) would leave `popUpFlag` permanently disarmed,
 * skipping the confirm modal on every subsequent Save click. Entry 152
 * confirmed (live, on `cardTypeMaster`/`raceMaster`, the identical shared
 * `<modal>` directive) that this does **NOT** happen in real legacy: the
 * directive's own hardcoded Ok-button (`commonPopUpDirectives.js`, the
 * `oneClickOnly` directive's click handler AND its `$watch` on
 * `showModal`) reset the gate to armed on every Ok click, structurally,
 * regardless of what any individual screen's own success/failure branches
 * do -- so the flag can never actually stay stuck in unmodified legacy.
 * The earlier framing of this bug shape (found independently on
 * `cardTypeMaster`/`identificationMaster`, entries 149/150/152) as a
 * "legacy bug the rebuild fixes" was corrected in entry 152 itself: it is
 * a REBUILD-introduced risk (the rebuild's plain `showConfirmModal`
 * signal has no equivalent implicit shared-directive watch to re-arm it),
 * not a real legacy defect. `discountCategoryMaster` is a new instance of
 * the SAME shared-directive mechanism (confirmed above: this screen's own
 * `<modal visible="showModal">` binding, view:137), so the same
 * correction applies for the same reason -- `confirmArmed` below is reset
 * to `true` after EVERY save attempt, success or failure, which
 * reproduces legacy's real always-rearms behavior rather than diverging
 * from it. `update()` (ctrl:309-361) has no confirm-modal concept at all,
 * matching legacy exactly.
 *
 * Per entry 55's standing policy: legacy's shared `<modal>` directive's
 * close (x) button calls a bare `cancel()` this controller never defines
 * (grepped: zero `cancel` definitions anywhere in this file) -- a live,
 * disclosed legacy crash on every screen using that directive without its
 * own `cancel`. This rebuild uses a plain working `closeConfirmModal()`
 * instead, per the standing policy (not re-litigated further here).
 *
 * ================================================================
 * Validation: identical `discountForm.$valid` gate on BOTH save() and
 * update() (unlike several sibling screens that only validate on create)
 * ================================================================
 * Both `code` and `type` (the "Description" field) carry a real HTML5
 * `required` attribute (view:13,30) plus the shared `show-errors`
 * directive; `save()` (ctrl:187) and `update()` (ctrl:313) both broadcast
 * `show-errors-check-validity` and gate on `discountForm.$valid` before
 * doing anything else -- a single validation mechanism, no duplicate
 * client-side manual check layered on top (unlike the `insuranceMaster`
 * pattern). Per this codebase's standing policy (entry 111/batch-11), the
 * validation messages below render VISIBLY by default -- legacy's own
 * `.help-block{display:none!important}` CSS defect is NOT reproduced.
 * Legacy's own help-block text for the second field (view:32) is a
 * copy-paste bug reading "Code is required" instead of "Description is
 * required" -- faithfully reproduced verbatim below since it's a visible
 * (if wrong) message, not a silent one.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - No `startLoader()`/`stopLoader()` calls ANYWHERE in this controller
 *   (grepped the whole file) -- unlike most sibling master screens, this
 *   one never shows the global loading spinner for any of its 6 calls.
 *   Reproduced by simply not injecting/using `LoaderService` here.
 * - `getDiscountCategoryById`'s failure branch is a raw native
 *   `alert("Error!!")` (ctrl:262), NOT `growl.error` -- reproduced verbatim
 *   via `window.alert(...)`, matching this codebase's established
 *   convention for this exact quirk (e.g. `currency-master`,
 *   `nursing-station-master`).
 * - `save()`'s success/failure growl messages are HARDCODED literal
 *   strings (ctrl:216-220/225-229: "Added Successfully...!!" /
 *   "Discount category already exists...!!"), NOT
 *   `response.data.message` -- reproduced as literal strings, not
 *   `res.message ?? ...`. Same for `update()` (ctrl:334-350: "Updated
 *   Successfully...!!" / "Already exits...!!" -- note legacy's own typo,
 *   "exits" not "exists", preserved verbatim) and `updateStatus()`
 *   (ctrl:290-304: "Updated Successfully...!!" / "Something went
 *   wrong...!!").
 * - `save()`'s success AND failure branches both unconditionally re-fetch
 *   the list afterward (ctrl:232, outside the success/fail branching) --
 *   reproduced via an unconditional `loadListAndCount()` call after every
 *   save attempt. `update()` does the same (ctrl:352-357).
 * - `showUpdateBtn` (ctrl:241-266) flips to Update mode IMMEDIATELY on
 *   click, before the POST even resolves (ctrl:243-244) -- reproduced by
 *   `saveMode.set(false)` at the very top of `edit()`.
 * - Status toggle (`updateStatus`, ctrl:269-306): the view's checkbox
 *   (`ng-model="info.isActive"`, `ng-true-value="true"`,
 *   `ng-false-value="false"`, `ng-change="updateStatus(...)"`) flips
 *   optimistically client-side before the request resolves, and legacy's
 *   handler has NO revert branch on failure (only a growl toast).
 *   Reproduced via `<app-status-toggle>` + an unconditional optimistic
 *   `list.update()`, never reverted regardless of response status.
 * - The search box (`ng-model="search"` + `filter:search` on the
 *   `ng-repeat`, view:68/98) filters only the CURRENTLY LOADED SERVER PAGE
 *   (`discountList`, populated fresh per paginated fetch), same
 *   "current-page-only search" quirk already documented on several
 *   sibling master screens. Reproduced via `filteredList` over `list()`.
 * - The table header's "Status(n)" label (view:85) is legacy's own literal
 *   text, not a placeholder or typo introduced here.
 *
 * ================================================================
 * CONFIRMED BACKEND DEFECT, NOT FIXED HERE -- getCount is active-only,
 * getList is not, so `totalCount` under-reports once any row is deactivated
 * ================================================================
 * `getDiscountCategoryCount` (`DiscountTypeMasterDaoImpl.java:132-149`)
 * filters `Restrictions.eq("isActive", true)` -- it only counts ACTIVE
 * rows. `getDiscountCategoryList`'s raw SQL (same file, 108-130) has NO
 * `is_active` filter at all -- it returns EVERY row for the unit,
 * active or not. Confirmed live: after deactivating one row via the
 * status toggle, `list()` still contained 6 rows (the deactivated one
 * included) while `totalCount()` dropped to 5. This under-counts the
 * pager (`totalPages`/`pageNumbers`) relative to what the current page
 * actually displays whenever an inactive row exists -- a real,
 * server-side pagination-accuracy defect, not something this screen's
 * own code can compensate for (both numbers come directly from their
 * respective endpoints with no client-side re-derivation). Reproduced
 * as-is; flagged in this batch's findings entry.
 */
@Component({
  selector: 'app-discount-category-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './discount-category-master.html',
  styleUrl: './discount-category-master.scss',
})
export class DiscountCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DiscountCategoryMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.discountType, ctrl:58-64) ----
  form = signal<DiscountCategoryForm>(emptyDiscountCategoryForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment (FIXED, diverges
   * from legacy): re-armed after every save attempt, success or failure. */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initDiscountTypeMasterList/PagerService) ----
  list = signal<DiscountCategoryRow[]>([]);
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

  // mirrors initDiscountTypeMasterList (ctrl:76-120): fetches list + count
  // together and resets to page 1 once the count responds.
  private loadListAndCount(): void {
    this.api.getList(this.unitId, this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.totalCount.set(res.object ?? 0);
          this.currentPage.set(1);
        }
      },
    });
  }

  // mirrors getDiscountTypeMasterList (ctrl:122-150) via setPage(page,true).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.unitId, this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.list.set(res.listObject ?? []);
      },
    });
  }

  // mirrors setNoOfRecords (ctrl:71-75).
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof DiscountCategoryForm>(field: K, value: DiscountCategoryForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors discountForm.$valid via the 2
  // `required` inputs -- code, type, view:13,30) ----
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

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:182-234) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // matches discountForm.$valid gate, ctrl:198
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:176-179): disarms the confirm gate, closes the
  // modal, then saves.
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
        code: f.code,
        type: f.type,
        addedBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        addedDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Added Successfully...!!'); // ctrl:216-220, verbatim, hardcoded (not res.message)
            this.resetForm(); // ctrl:221-223 -- broadcast('show-errors-reset') + init()
          } else {
            this.notify.error('Discount category already exists...!!'); // ctrl:225-229, verbatim, hardcoded
          }
          // Re-arm the confirm gate on both outcomes -- matches legacy's real
          // behavior (see class doc comment): the shared `<modal>` directive's
          // own watch re-arms `popUpFlag` on every close regardless of the
          // save's outcome.
          this.confirmArmed = true;
          this.loadListAndCount(); // ctrl:232 -- unconditional re-fetch after every save attempt
        },
        // A hard HTTP error (e.g. a genuine backend exception -- confirmed
        // live via a code longer than the DB's varchar(10) column, which
        // 500s) never reaches `next` at all. Legacy's own `.then(function(
        // response){...})` (ctrl:208-234) is single-argument (no rejection
        // handler), so it ALSO never runs on a real 500 -- no growl either
        // way, faithfully reproduced by showing nothing here. But legacy's
        // shared `<modal>` directive still independently re-arms `popUpFlag`
        // on every modal close via its own `$watch`, completely decoupled
        // from the save promise's outcome -- so the confirm gate must still
        // be re-armed here too, or a genuine backend exception would
        // permanently stick this rebuild's confirm modal shut, which is
        // NOT what real legacy does. Confirmed live: without this handler,
        // the too-long-code 500 above left `confirmArmed` stuck `false` and
        // the very next (valid) Save silently skipped the confirm modal
        // entirely and posted immediately.
        error: () => {
          this.confirmArmed = true;
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:241-266) ----
  edit(row: DiscountCategoryRow): void {
    // ctrl:243-244 -- legacy flips saveBtnFlag/updateBtnFlag to "Update"
    // mode IMMEDIATELY on click, before the POST even resolves.
    this.saveMode.set(false);
    this.api.getById(row.discountTypeId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          this.form.set({
            discountTypeId: res.listObject[0].discountTypeId,
            code: res.listObject[0].code,
            type: res.listObject[0].type,
          });
        } else {
          window.alert('Error!!'); // ctrl:262 -- a real native alert, not growl
        }
      },
    });
  }

  // ---- update (mirrors update, ctrl:309-361 -- same discountForm.$valid
  // gate as save(), but no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // ctrl:324, same $valid gate as save()
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .update({
        discountTypeId: f.discountTypeId as number,
        code: f.code,
        type: f.type,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Updated Successfully...!!'); // ctrl:334-339, verbatim, hardcoded
            this.resetForm(); // ctrl:340-342
          } else {
            this.notify.error('Already exits...!!'); // ctrl:344-350, verbatim -- legacy's own typo, preserved
          }
          this.loadListAndCount(); // ctrl:352-357 -- unconditional re-fetch, same as save()
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:269-306) ----
  toggleStatus(row: DiscountCategoryRow): void {
    const nextActive = !row.isActive;
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing --
    // never reverted below, per the class doc comment (legacy has no
    // revert branch at all for this screen).
    this.list.update((rows) => rows.map((r) => (r.discountTypeId === row.discountTypeId ? { ...r, isActive: nextActive } : r)));
    this.api
      .updateStatus({
        discountTypeId: row.discountTypeId,
        isActive: nextActive,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Updated Successfully...!!'); // ctrl:290-296, verbatim, hardcoded
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:298-304, verbatim, hardcoded -- no revert
          }
        },
      });
  }

  // mirrors init() (ctrl:28-66) as re-entered by a successful save/update --
  // deliberately does NOT touch confirmArmed, see class doc comment.
  private resetForm(): void {
    this.form.set(emptyDiscountCategoryForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:183-185/270-272/310-312. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
