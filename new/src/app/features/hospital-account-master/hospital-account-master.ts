import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { HospitalAccountMasterService } from './hospital-account-master.service';
import { HospitalAccountForm, HospitalAccountRow, emptyHospitalAccountForm } from './hospital-account-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/UnitMaster/billingMaster/
 * hospital-account-master.html` (192 lines) + `resources/assets/js/
 * controller/billing/unit/hospitalAccountMasterController.js` (334 lines,
 * `hospitalAccountMasterController`, no `controllerAs` -- classic
 * `$scope.*` throughout, confirmed zero `vm.*` in the view). State
 * `hospitalAccountMaster`, **module ID 788, no `requireSuperUser`**,
 * confirmed directly from the live `.state(...)` resolve block
 * (app.js:20598-20626) -- see docs/ANGULAR_PENDING_FINDINGS.md,
 * `PENDING-batch18-step1-hospitalaccountmaster`, the authoritative Step 1
 * audit for this screen (read in full before this file was written).
 *
 * A pure 2-field (Code/Description) + isDefault-flag lookup master for the
 * hospital's own billing "accounts" (e.g. cash/bank ledgers a receipt can be
 * posted against) -- no rate/amount/formula field anywhere on the entity,
 * so no Step 6b required. Confirmed real end-to-end: `HospitalAccountMasterController.java`
 * (`@RequestMapping("api/billing")`) -> `HospitalAccountMasterServiceImpl` ->
 * `HospitalAccountMasterDaoImpl` -> table `billing.m_hospital_accounts`. A
 * fresh golden trace this session confirmed the real, live shape: on load,
 * 2 parallel, unchained calls (list + count), both 200 -- reproduced
 * exactly by `loadListAndCount()` below.
 *
 * ================================================================
 * Duplicate-check -- REAL, WORKING, but description-only (code-blind).
 * FAITHFULLY REPRODUCED, not augmented with a client-side Code check.
 * ================================================================
 * `HospitalAccountMasterServiceImpl.saveHospitalAccountMaster`/
 * `updateHospitalAccountMaster` (service.java:33-101) both call a real,
 * correctly org+unit-scoped, precedence-safe "already exists" query first
 * and reject with the server's own `ALREADY_EXIST` message before any
 * write -- genuinely functioning on both Create and Update. **Gap: only
 * `hospitalAccountDesc` (lowercased) is ever compared -- `hospitalAccountCode`
 * is never checked for uniqueness anywhere**, even though the UI presents
 * both Code and Description as identifying fields. This is real,
 * server-side, incomplete-but-working behavior -- nothing added here to
 * compensate for it client-side (the server already rejects on a duplicate
 * Description; a duplicate Code alone is silently allowed, in real legacy
 * today, faithfully carried into this rebuild by simply not inventing a
 * check legacy doesn't have).
 *
 * ================================================================
 * Status-toggle -- NO rollback on error. FAITHFULLY REPRODUCED.
 * ================================================================
 * `hospital-account-master.html:160-163` binds the toggle directly to the
 * live row's `info.status` (optimistic client-side flip via `ng-model`);
 * `updateStatus()` (ctrl:296-331) shows `growl.error(...)` on a backend
 * failure but never reverts `info.status` -- a failed update leaves the
 * toggle showing the wrong, unpersisted state indefinitely, with only a
 * transient toast as the signal. `toggleStatus()` below uses the scaffold's
 * correct `<app-status-toggle>`/`signal.update()` MECHANISM (a real,
 * working re-render), while still reproducing legacy's exact no-revert
 * BEHAVIOR on a failed request -- the mechanism being correct doesn't
 * change what happens on error.
 *
 * ================================================================
 * popUpFlag / confirm-modal re-arm timing -- genuinely ambiguous from
 * static reading (shared-directive digest-timing race), NOT resolved here.
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:39) and is set `false` exactly
 * once, in `PopupOkBtn()` (ctrl:147-150) -- grepping the WHOLE controller
 * file for every `popUpFlag` occurrence confirms the controller's own code
 * NEVER sets it back to `true` anywhere, including inside `save()`'s
 * success branch (ctrl:186-206, which only calls `initHospitalAccountMaster()`
 * -- confirmed by reading that function, ctrl:42-55, that it touches
 * `$scope.common` only, never `popUpFlag`). Per the findings entry, the
 * shared `<modal>` directive's own `$watch(attrs.visible)` is understood to
 * reset `popUpFlag=true` on every `showModal -> false` transition,
 * independent of the controller's own success/error branches, via a
 * jQuery-bound handler racing an async `$http` digest -- this cannot be
 * resolved by static reading and needs a live Step 5 click-test
 * (specifically an error-path click-through) to confirm timing matches
 * real legacy exactly. `confirmArmed` below is reset to `true` ONLY on
 * doCreate()'s SUCCESS branch (not on error) -- the interpretation this
 * rebuild commits to, as currently understood from the controller's own
 * explicit logic, per this batch's standing instruction not to guess at
 * the exact shared-directive race.
 * // TODO(step5-verify): confirm live legacy's actual re-arm timing on both
 * // the success AND (especially) the error path of a real confirm-then-save
 * // click-through, and adjust `confirmArmed`'s reset here if it turns out
 * // to differ from "success-only".
 *
 * ================================================================
 * NEW FINDING, found while building this screen (past the original Step 1
 * audit) -- once a user clicks the edit-pencil on ANY row, this screen can
 * NEVER return to "Save" (create) mode again for the rest of that page
 * load, not even after a successful Update. FAITHFULLY REPRODUCED.
 * ================================================================
 * Grepping the WHOLE controller file for `saveBtnFlag`/`updateBtnFlag`
 * turns up exactly 4 occurrences: both initialized once at the top
 * (`saveBtnFlag=true`/`updateBtnFlag=false`, ctrl:11-12) and both flipped
 * exactly once, together, inside `showUpdateBtn()` (ctrl:217,219,
 * `saveBtnFlag=false`/`updateBtnFlag=true`) -- **neither flag is EVER set
 * back anywhere else in the file**, including `update()`'s own success
 * branch (ctrl:270-294), which calls `initHospitalAccountMaster()`
 * (resets `$scope.common` only) and reloads the list, but never touches
 * either flag. Since the view gates the two buttons with mutually-exclusive
 * `ng-if="saveBtnFlag"`/`ng-if="updateBtnFlag"` (view:58-65) and nothing
 * else in the controller ever restores `saveBtnFlag=true`, a user who edits
 * any single row is left permanently on the "Update"-labeled button --
 * confirmed live-reachable, not merely theoretical: after that click, the
 * ONLY way to create a brand-new hospital account is a full page
 * reload/re-navigation to the state, resetting `$scope` from scratch. This
 * was NOT called out in the original Step 1 audit entry and is flagged
 * here prominently for the orchestrating session's attention before Step
 * 4/5. Reproduced via `editMode` below: set `true` by `edit()`, and never
 * reset back to `false` by `doUpdate()`'s success handler (mirroring
 * `initHospitalAccountMaster()`'s real, confirmed scope) -- there is no
 * "cancel edit" affordance in legacy's view either, so none is added here.
 *
 * ================================================================
 * NEW FINDING, found while building this screen -- `isDefault`'s
 * "only one default account" enforcement is real, but asymmetric between
 * Create and Update, and lives entirely server-side (nothing this
 * Angular-only rebuild can compensate for from the frontend).
 * ================================================================
 * `HospitalAccountMasterDaoImpl.updateAllHospitalAccount` (:213-232) resets
 * EVERY hospital account in the given org+unit to `isDefault='N'` via a
 * bulk update. `HospitalAccountMasterServiceImpl.saveHospitalAccountMaster`
 * (service.java:33-57) only calls it when the INCOMING row's own
 * `isDefault=='Y'` (correctly gated) -- but `.updateHospitalAccountMaster`
 * (service.java:82-101) calls it UNCONDITIONALLY, before checking the
 * incoming row's `isDefault` value at all. Net effect: editing ANY hospital
 * account's Code/Description via Update -- even with the "is Default"
 * checkbox left unchecked -- silently clears the org+unit's real default
 * flag off whichever OTHER account currently holds it, before writing this
 * account's own (possibly non-default) value back. This is real,
 * server-side, confirmed-by-reading-both-DAO-methods-directly behavior,
 * not something any frontend change can correct -- flagged here for the
 * owner's/orchestrating session's awareness (candidate for
 * docs/ANGULAR_PENDING_FINDINGS.md), not acted on in this Angular-only
 * rebuild.
 *
 * ================================================================
 * Other faithfully reproduced details
 * ================================================================
 * - Growl messages pass through the server's real `response.data.message`
 *   on every branch (ctrl:190,200,232,275,284,315,322) -- NOT hardcoded
 *   literals (unlike this batch's `sterilizationTypeMaster` sibling).
 *   Reproduced via `res.message` throughout below.
 * - Both Code and Description have their OWN distinct `name` attribute in
 *   legacy's markup (view:16,31, `name="code"`/`name="desc"` -- genuinely
 *   distinct, unlike `sterilizationTypeMaster`'s duplicate-`name="code"`
 *   bug) -- both required-field messages already work correctly in real
 *   legacy; the only change here is the standing validation-visibility
 *   policy (rendering them visibly instead of via legacy's globally-hidden
 *   `.help-block` CSS), not a correctness fix.
 * - Copy-paste artifact: legacy's own paginated-refetch function is named
 *   `$scope.PriorityMasterList` (ctrl:107-126) -- a leftover name from a
 *   different master's template. Unlike this codebase's `bank-master`
 *   sibling (where the identically-named function is a real dead-write
 *   bug, writing into a scope variable the view never renders), THIS
 *   screen's `PriorityMasterList` correctly assigns its response straight
 *   into `$scope.hospitalAccountList` -- the exact variable the table
 *   renders (view:156) -- confirmed by direct reading. Cosmetic name only,
 *   no behavioral gap to preserve; not reproduced under that literal name.
 * - Search (`ng-model="searchKeyword"` + `filter:searchKeyword` on the
 *   `ng-repeat`, view:69/156) is a plain CLIENT-SIDE filter over only the
 *   CURRENTLY LOADED SERVER PAGE, with no backend search call at all --
 *   matching this batch's established "current-page-only search"
 *   convention. AngularJS's bare `filter:expression` (no field key) matches
 *   against every property of each row object, so `displayedRows` below
 *   mirrors that with a full-object substring match (same approach as this
 *   codebase's `bank-master` sibling), not a narrower code/description-only
 *   filter.
 * - The edit pencil applies a CSS-only `disabled` look on inactive rows
 *   (`ng-class="{disabled: info.status=='I'}"`, view:164) but has no real
 *   `ng-disabled`/click guard -- `showUpdateBtn()` still fires and lets a
 *   user edit an "inactive" row. Faithfully reproduced: `edit()` below has
 *   no status guard either.
 * - `$scope.updateScope` (ctrl:13,218) is set but never read anywhere else
 *   in the file -- a dead scope variable, not reproduced.
 * - Per entry 55's standing policy, legacy's shared `<modal>` directive's
 *   close (x) button calls a bare `cancel()` this controller never defines
 *   (grepped: zero `cancel` definitions anywhere in this file) -- a live,
 *   disclosed legacy crash. This rebuild uses a plain, working
 *   `closeConfirmModal()` instead, per the standing policy (no new sign-off
 *   needed).
 * - Date fields (`createdDate`/`updatedDate`) are client-generated
 *   (`moment(date).format('DD-MM-YYYY HH:mm:ss')`, ctrl:155-156/251-252/
 *   298-299) and parsed server-side via `GlobalCommonDateUtils.getDate(...,
 *   "dd-M-yyyy HH:mm:ss")` -- reproduced via the same `formatDateTime()`
 *   helper convention already established in this codebase's `bank-master`
 *   sibling.
 */
@Component({
  selector: 'app-hospital-account-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './hospital-account-master.html',
  styleUrl: './hospital-account-master.scss',
})
export class HospitalAccountMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(HospitalAccountMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common, ctrl:42-49) ----
  form = signal<HospitalAccountForm>(emptyHospitalAccountForm());
  /** Mirrors `saveBtnFlag`/`updateBtnFlag` collapsed into one flag (they're
   * always each other's exact opposite in legacy). Set `true` by `edit()`
   * and NEVER reset back to `false` anywhere below -- including
   * `doUpdate()`'s own success branch -- matching legacy's real, confirmed
   * behavior. See class doc comment's "NEW FINDING" writeup: this is a
   * one-way transition in real legacy too, not a rebuild gap. */
  editMode = signal(false);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag`. See class doc comment for the full,
   * genuinely-ambiguous re-arm-timing writeup and its TODO(step5-verify). */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initHospitalAccountMasterList/PagerService) ----
  list = signal<HospitalAccountRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side-only, current-page-only filter mirroring
  // `hospitalAccountList | filter:searchKeyword` -- see class doc comment.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initHospitalAccountMasterList (ctrl:67-105): fetches list +
  // count together, in parallel, unchained -- matching this screen's own
  // golden trace (2 calls, both 200, no chaining between them).
  private loadListAndCount(): void {
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors setPage(page,true) -> `$scope.PriorityMasterList` (ctrl:131-141/
  // 107-126) -- same real endpoint as loadListAndCount's own list call; see
  // class doc comment on why this is NOT reproduced as a discard-the-response
  // bug the way this codebase's bank-master sibling is.
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getPage(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
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

  updateField<K extends keyof HospitalAccountForm>(field: K, value: HospitalAccountForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-Code / required-Description validation, both correctly
  // field-bound in legacy already (no name-collision bug on this screen) --
  // rendered VISIBLY per the standing validation-visibility policy, not
  // "faithfully" hidden behind legacy's globally-broken `.help-block` CSS ----
  codeRequiredError = computed(() => this.formSubmitted() && this.form().hospitalAccountCode.trim() === '');
  descRequiredError = computed(() => this.formSubmitted() && this.form().hospitalAccountDesc.trim() === '');

  private validate(): boolean {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    const f = this.form();
    return f.hospitalAccountCode.trim() !== '' && f.hospitalAccountDesc.trim() !== '';
  }

  // ---- save entry point (mirrors save(), ctrl:152-212 -- CREATE path only;
  // update() below is the separate, always-enabled "Update" button) ----
  save(): void {
    if (!this.validate()) return;
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doCreate();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:147-150): disarms the gate, closes the modal,
  // then saves.
  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doCreate();
  }

  private doCreate(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        hospitalAccountCode: f.hospitalAccountCode,
        hospitalAccountDesc: f.hospitalAccountDesc,
        isDefault: f.isDefault,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        status: 'A',
        orgnizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:190, real server message
            this.resetForm(); // ctrl:195 -- initHospitalAccountMaster()
            this.loadListAndCount(); // ctrl:196 -- reload happens only on success in legacy
            this.confirmArmed = true; // see class doc comment's TODO(step5-verify)
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:200, real server message
          }
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:214-242) ----
  edit(row: HospitalAccountRow): void {
    this.editMode.set(true); // see class doc comment -- NEVER reset back below, matching legacy
    this.formSubmitted.set(false);
    this.api.getById(row.hospitalAccountId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          const d = res.object;
          this.form.set({
            hospitalAccountId: d.hospitalAccountId,
            hospitalAccountCode: d.hospitalAccountCode,
            hospitalAccountDesc: d.hospitalAccountDesc,
            isDefault: d.isDefault,
          });
        } else {
          this.notify.error(res.message ?? 'Error!'); // ctrl:230-235, real server message
        }
      },
    });
  }

  // ---- update (mirrors update(), ctrl:245-294 -- no confirm modal, ever) ----
  update(): void {
    if (!this.validate()) return;
    const f = this.form();
    this.api
      .update({
        hospitalAccountId: f.hospitalAccountId as number,
        hospitalAccountCode: f.hospitalAccountCode,
        hospitalAccountDesc: f.hospitalAccountDesc,
        isDefault: f.isDefault,
        updatedBy: this.userId,
        status: 'A', // dead field on this path -- see models.ts doc comment
        orgnizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:275, real server message
            this.resetForm(); // ctrl:279 -- initHospitalAccountMaster() -- clears fields, NOT editMode (see class doc comment)
            this.loadListAndCount(); // ctrl:280 -- reload happens only on success in legacy
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:284, real server message
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:296-331) ----
  toggleStatus(row: HospitalAccountRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing --
    // never reverted below on error, matching legacy's own lack of a
    // revert branch. See class doc comment.
    this.list.update((rows) => rows.map((r) => (r.hospitalAccountId === row.hospitalAccountId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        hospitalAccountId: row.hospitalAccountId,
        status: nextStatus,
        updatedDate: formatDateTime(new Date()),
        updatedBy: this.userId,
        orgnizationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:315, real server message
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:322, real server message
          }
        },
        // No error callback -- matching legacy's own lack of a rollback/
        // recovery branch on a hard HTTP failure (see class doc comment).
      });
  }

  // mirrors initHospitalAccountMaster() (ctrl:42-55) as re-entered by
  // save()/update()'s success handler only -- clears form fields ONLY,
  // never `editMode` (see class doc comment's "NEW FINDING").
  private resetForm(): void {
    this.form.set(emptyHospitalAccountForm());
    this.formSubmitted.set(false);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:155-156/251-252/298-299 -- same convention as this codebase's
 * bank-master sibling. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
