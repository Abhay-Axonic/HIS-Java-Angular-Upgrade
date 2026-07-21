import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BankMasterService } from './bank-master.service';
import { BankForm, BankRow, BranchOption, emptyBankForm } from './bank-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/bank-master.html +
 * resources/assets/js/controller/billing/global/bankMasterController.js
 * (`bankMasterController`, no `controllerAs`). State `bankMaster`
 * (app.js:8136-8157), moduleId 456, confirmed live (not commented out).
 *
 * **Informational note, doesn't change how this screen is built:** despite
 * being state-registered alongside plain ADT/org "Global Master" screens and
 * templated under `views/master/GlobalMaster/`, this screen's own controller
 * physically lives in `resources/assets/js/controller/billing/global/`
 * (alongside unambiguous Billing screens -- bill listing, company receipts,
 * fund sanction, etc.), and every one of its endpoints is under
 * `/api/billing/*`, not `/adt/*`/`/org/*` like true Global Master siblings
 * (e.g. sub-speciality-master). This is Billing-tagged, not a plain
 * low-risk Global Master, contrary to Phase 0's inventory tag/folder
 * placement -- flagged for awareness, not acted on here.
 *
 * Confirmed real, working CRUD during Step 0/1 audit: list+count
 * (ctrl:84-120), save (ctrl:168-234), edit-populate `showUpdateBtn`
 * (ctrl:236-266), update (ctrl:269-323), status toggle `updateStatus`
 * (ctrl:325-358) -- all wired to real endpoints, see bank-master.service.ts.
 *
 * **Deliberate deviations/preserved quirks/bugs, not fixed in this rebuild:**
 * - **A real, previously-uncataloged pagination bug, found while building
 *   this screen and faithfully reproduced, not fixed.** Next/Previous/
 *   page-number clicks call `setPage(page, true)` (view:204-216) ->
 *   `PriorityMasterList` (ctrl:124-142), which fetches a new page from
 *   `GETBANKMASTERLIST` but assigns the response to `$scope.commonList` --
 *   a scope variable the view's `ng-repeat` (bound to `bankList`, view:182)
 *   never reads. The pager's own state (current page highlight, `pager.pages`)
 *   DOES advance (`$scope.pager` is reassigned unconditionally inside
 *   `setPage` itself, ctrl:152, independent of the broken fetch), but the
 *   table's visible rows never change after the first page -- clicking
 *   through pages is a real, silent no-op beyond page 1, in legacy today.
 *   Reproduced via `loadPageForPager()`, which calls the real endpoint (so
 *   the network-call signature matches legacy exactly) but deliberately
 *   discards the response instead of writing it into the `list` signal --
 *   see that method's own comment. Worth logging to
 *   docs/ANGULAR_PENDING_FINDINGS.md; not logged there yet as of this build
 *   (found mid-rebuild, past the original Step 0/1 audit pass for this
 *   screen).
 * - **`branchDesc` is a dead, always-empty form field.** `initBankMaster()`
 *   (ctrl:42-55) seeds `$scope.common.branchDesc = ""`, and `save()`
 *   (ctrl:183-196) POSTs it verbatim -- but no input anywhere in the view
 *   ever binds to `common.branchDesc`, so it is always the empty string on
 *   every create. Reproduced by hardcoding `branchDesc: ''` directly in the
 *   save-payload builder rather than modeling it as an editable form field
 *   (there's nothing to edit). `update()`'s own payload (ctrl:284-294) never
 *   includes this field at all, matching legacy exactly.
 * - **Validation-visibility gap on `bankAddress` (per audit finding, faithfully
 *   reproduced).** Branch/Code/Description all have both an HTML5 `required`
 *   attribute AND a starred `*` label (view:15-63); `bankAddress` (view:62-65)
 *   is ALSO `required="required"` but its label has NO star (view:64) --
 *   silently mandatory. Reproduced identically: `bankAddress`'s label omits
 *   `<span class="required">`, but `requiredFieldsValid()` still gates
 *   Save/Update on it exactly like the other three.
 * - **The manual JS validation check only ever covers `branchId`, in BOTH
 *   `save()` (ctrl:175-182) and `update()` (ctrl:276-282) -- symmetric
 *   between create/update here (unlike this codebase's sub-speciality-master,
 *   where update() has no manual checks at all).** Code/Description/Address
 *   rely solely on the HTML5 `required` attributes (`$scope.bankForm.$valid`)
 *   with no field-specific growl of their own -- an invalid Code/Description/
 *   Address blocks Save/Update as a silent no-op (no growl naming the field),
 *   while a missing Branch gets an explicit `growl.error("Please Select
 *   Branch")`. Reproduced via `runManualValidation()` (branchId only),
 *   called from both `save()` and `update()`.
 * - The confirm modal (shared `<modal>` directive, `PopupOkBtn`/`popUpFlag`)
 *   only gates CREATE (ctrl:201-203); `update()` (ctrl:298-319) saves
 *   immediately with no confirm step -- same asymmetry as this codebase's
 *   amenity-master/sub-speciality-master.
 * - **List reload runs ONLY on the success branch**, in both `save()`
 *   (ctrl:211-218) and `update()` (ctrl:303-309) -- unlike this codebase's
 *   sub-speciality-master (which reloads unconditionally). An error response
 *   growls and stops, nothing else. Reproduced via `loadListAndCount()`
 *   called strictly inside each success branch below.
 * - `showUpdateBtn` (ctrl:236-266) replaces `$scope.common` WHOLESALE from
 *   `response.data.listObject[0]` (not field-by-field like
 *   sub-speciality-master's edit), then immediately re-stamps `branchId` as
 *   a string (ctrl:251) to match the `<select>` binding. Reproduced via
 *   `this.form.set(res.listObject[0])` followed by a `branchId` string
 *   coercion. Failure uses `growl.error(response.data.message)` (ctrl:256-260)
 *   -- a real dynamic server message, not a hardcoded string or a native
 *   `alert()`.
 * - `updateStatus` (ctrl:325-358) growls the server's own `response.data.message`
 *   on both branches and never calls `initBankMaster()`/`initPaymentMasterList()`
 *   on success, and never reverts the already-flipped checkbox on failure
 *   (the view's `ng-model="info.status"` is two-way bound, so the visual
 *   toggle happens immediately regardless of the response). Reproduced by
 *   optimistically patching the `list` signal before the call resolves, with
 *   no reload on either branch.
 * - **Search is a plain client-side filter over whatever page is currently
 *   loaded, with no server call at all** (`bankList|filter:searchKeyword`,
 *   view:87,182 -- the search input has no `ng-keyup`/handler whatsoever).
 *   Reproduced via a `displayedRows` computed signal that filters `list()`
 *   in-memory; `onSearchKeyup` service call intentionally does not exist for
 *   this screen.
 * - **The dead "Details" info-icon (view:195, `data-target="#detailsAddModal"`)
 *   is omitted entirely, not ported.** Confirmed non-functional in legacy:
 *   that modal id only exists in the shared `views/common/top-bar.html` shell
 *   ("Address Details"), is never populated per-row by this screen's
 *   controller, and nothing here builds a real row-details view. Matches
 *   this codebase's established convention of omitting confirmed-dead UI
 *   (e.g. amenity-master's dead sort-caret icons) rather than rendering
 *   inert/misleading markup.
 * - **Legacy's local try/catch-swallow pattern is NOT reproduced here --
 *   there is no direct Angular/RxJS analog.** `save()`, `update()`,
 *   `showUpdateBtn()`, and `updateStatus()` (ctrl:168-360) each wrap their
 *   entire body in `try{...}catch(e){console.log("Exception-",e);}` with no
 *   rethrow and no route to any centralized handler -- a real instance of
 *   the exact billing-adjacent error-swallowing pattern this project's
 *   CLAUDE.md calls its #1 problem to fix. RxJS's `subscribe({next, error})`
 *   here already doesn't swallow synchronous errors the way a bare
 *   try/catch-log does, so there's no equivalent gap to intentionally leave
 *   open. Noted here for the separate billing/error-handling workstream;
 *   not actionable from this Angular-only rebuild.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's x close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of the shared `<modal>`
 *   directive's `cancel()` crash (`bankMasterController.js` never defines
 *   `$scope.cancel`) -- a working close is an accepted standing improvement,
 *   not a parity gap.
 */
@Component({
  selector: 'app-bank-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './bank-master.html',
  styleUrl: './bank-master.scss',
})
export class BankMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BankMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- branch dropdown (mirrors $scope.branchList) ----
  branchOptions = signal<BranchOption[]>([]);

  // ---- form state (mirrors $scope.common) ----
  form = signal<BankForm>(emptyBankForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initPaymentMasterList/PriorityMasterList) ----
  list = signal<BankRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side-only filter, mirroring `bankList|filter:searchKeyword` --
  // this screen has no server-side search call at all. See class doc comment.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadBranches();
    this.loadListAndCount();
  }

  // ctrl:42-72.
  private loadBranches(): void {
    this.api.getActiveBranches(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.branchOptions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors initPaymentMasterList (ctrl:84-120): fetches list + count
  // together, always from offset 0, and resets to page 1 once the count
  // responds.
  private loadListAndCount(): void {
    this.loader.start();
    this.api.getList(this.organizationId, 0, this.pageSize()).subscribe({
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

  // mirrors PriorityMasterList (ctrl:124-142) via setPage(page,true) -- a
  // REAL, faithfully-reproduced bug, not a rebuild mistake: legacy fetches
  // this page but stores it into a scope variable the view never renders
  // (`$scope.commonList`, never `bankList`). See class doc comment. The
  // fetched response is deliberately discarded here (not written into
  // `list`), matching that exact dead-write, while the pager's own current-
  // page state still advances via `goToPage()` below (mirroring `$scope.pager`
  // being reassigned unconditionally inside legacy's `setPage`, independent
  // of this broken fetch).
  private loadPageForPager(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getPageForPager(this.organizationId, offset, this.pageSize()).subscribe({
      // Intentionally not assigned to `list` -- see method + class doc comment.
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page); // pager state advances regardless -- see class doc comment
    this.loadPageForPager(page); // fetch happens, but never reaches the screen -- faithful bug reproduction
  }

  updateField<K extends keyof BankForm>(field: K, value: BankForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 4 HTML5 `required` fields (Branch/Code/Description/Address,
  // view:15,33,48,63) that back `$scope.bankForm.$valid`. `bankAddress` is
  // required here despite its label having no `*` -- see class doc comment.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.branchId && f.bankCode.trim() && f.bankName.trim() && f.bankAddress.trim());
  }

  // Mirrors ctrl:175-182/276-282 -- the ONLY manual JS check, in both
  // save() and update(). Code/Description/Address are deliberately NOT
  // checked here -- see class doc comment.
  private runManualValidation(): boolean {
    const f = this.form();
    if (!f.branchId) {
      this.notify.error('Please Select Branch');
      return false;
    }
    return true;
  }

  isBranchInvalid(): boolean {
    return this.formSubmitted() && !this.form().branchId;
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().bankCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().bankName.trim();
  }

  isAddressInvalid(): boolean {
    return this.formSubmitted() && !this.form().bankAddress.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:168-234) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.runManualValidation()) return; // ctrl:175-182
    if (!this.requiredFieldsValid()) return; // mirrors the `$valid` gate, ctrl:201
    this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        branchId: f.branchId,
        bankCode: f.bankCode,
        bankName: f.bankName,
        branchDesc: '', // always empty -- see class doc comment / BankForm's doc comment
        bankAddress: f.bankAddress,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        status: 'A',
        orgnisationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:212-215, real server message
            this.resetForm();
            this.loadListAndCount(); // ctrl:217-218 -- SUCCESS-branch-only reload, see class doc comment
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!'); // ctrl:222-225, real server message
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyBankForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:236-266) ----
  edit(row: BankRow): void {
    this.saveMode.set(false); // ctrl:239-241, set synchronously BEFORE the fetch resolves
    this.api.getById(row.bankId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          // ctrl:250 -- wholesale assign, then re-stamp branchId as a
          // string (ctrl:251) -- NOT field-by-field like sub-speciality-master.
          this.form.set({ ...d, branchId: String(d.branchId) });
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!'); // ctrl:256-260, real server message (not a native alert)
        }
      },
    });
  }

  // ---- update (mirrors update(), ctrl:269-323 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.runManualValidation()) return; // ctrl:276-282 -- same single branchId check as save()
    if (!this.requiredFieldsValid()) return; // mirrors the `$valid` gate, ctrl:298
    const f = this.form();
    this.api
      .update({
        bankId: f.bankId as number,
        branchId: f.branchId,
        bankCode: f.bankCode,
        bankName: f.bankName,
        bankAddress: f.bankAddress,
        updatedBy: this.userId,
        orgnisationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:304-307, real server message
            this.resetForm();
            this.loadListAndCount(); // ctrl:308-309 -- SUCCESS-branch-only reload, see class doc comment
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!'); // ctrl:312-316, real server message
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:325-358) ----
  toggleStatus(row: BankRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // ctrl:325-358 -- growl only, no initBankMaster()/initPaymentMasterList()
    // call on success, and no revert on failure. See class doc comment.
    this.list.update((rows) => rows.map((r) => (r.bankId === row.bankId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        bankId: row.bankId,
        status: nextStatus,
        updatedDate: formatDateTime(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:342-345, real server message
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!'); // ctrl:348-352, real server message
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:172-173/273-274/327-328. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
