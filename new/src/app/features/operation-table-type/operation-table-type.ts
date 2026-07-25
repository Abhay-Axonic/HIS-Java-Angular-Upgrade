import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { OperationTableTypeService } from './operation-table-type.service';
import {
  OperationTableTypeForm,
  OperationTableTypeRow,
  emptyOperationTableTypeForm,
} from './operation-table-type.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/GlobalMaster/OTIMS/operation_Table_Type.html`
 * + `resources/assets/js/controller/otims/global/
 * operationTableTypeMasterController.js` (classic `$scope.*`, no
 * `controllerAs`). **Module ID 523, no `requireSuperUser`** -- confirmed
 * directly from the live resolve block (`app.js:12403-12429`), cross-checked
 * against `adminPriviledges.json:2576`. Full Step 1 audit:
 * docs/ANGULAR_PENDING_FINDINGS.md, entry
 * `PENDING-batch18-step1-operationtabletype`.
 *
 * `$state` correctly injected and used (no missing-DI crash, entries 8/10
 * don't apply here). List/count calls are independent, disjoint scope vars
 * -- no race condition. Clinical-only: pure 3-field (code/name/description)
 * OT table-type lookup feeding surgery scheduling, no patient-identifying or
 * billing content.
 *
 * ================================================================
 * Two REAL buttons, not one dead + one live (unlike sterilizationTypeMaster,
 * entry 165) -- `showUpdateBtn()` genuinely flips `saveBtnFlag`/
 * `updateBtnFlag` (ctrl:205-207), so both the Save and Update buttons are
 * reachable in real legacy. `save()` (ctrl:158-201) is this screen's ONLY
 * create path -- it never branches on an edit-mode flag the way some
 * sibling screens' shared save/update button does. `update()` (ctrl:
 * 229-256) is a wholly separate function/button, always reachable once
 * editing, never gated behind the confirm modal. Reproduced below as a real
 * Save/Update toggle (`editMode()`), matching legacy's actual behavior.
 * ================================================================
 *
 * ================================================================
 * CONFIRMED, SEVERITY 2 -- status-toggle mutates the SAME shared form
 * object used by the add/edit form, instead of a clean payload (Step 1
 * finding, faithfully reproduced at the OBSERVABLE-BEHAVIOR level only)
 * ================================================================
 * `updateStatus()` (ctrl:280-309) stamps `operationTableTypeId`/`status`
 * onto `$scope.common` -- the SAME object bound to the add-form's
 * `tableCode`/`tableName`/`tableDesc` inputs -- and POSTs whatever
 * stale/in-progress values happen to be sitting there alongside it.
 * `updateOperationTableTypeStatus`'s DAO only reads `operationTableTypeId`/
 * `status`/`updatedBy`/`updatedDate` off the DTO, so no data corruption
 * results from the extra fields riding along. But `updateStatus()` never
 * calls `initMaster()`, so `$scope.common` (and therefore the add-form) is
 * NEVER reset as a side effect of a status toggle -- flagged for Step 5
 * live verification as to whether a subsequent "Save New" could submit a
 * stray leftover `operationTableTypeId`, not asserted as confirmed data
 * corruption. This signals-based rebuild has no equivalent of a single
 * mutable object serving two purposes (forcing one in would be an
 * anti-pattern here), so `toggleStatus()` below builds a clean,
 * purpose-built `{ operationTableTypeId, status, updatedBy, updatedDate }`
 * payload for the actual HTTP call -- but faithfully reproduces the
 * *observable end-state* that matters: it does NOT call `resetForm()`, so
 * whatever is currently sitting in the add/edit form (including a
 * half-filled create attempt, or an in-progress edit) is left completely
 * untouched after toggling an unrelated row's status, exactly matching
 * legacy's own accidental behavior.
 *
 * ================================================================
 * `popUpFlag` re-arm timing -- confirmed NEVER re-armed by this
 * controller's own code, ambiguity is specifically about the shared
 * `<modal>` directive's own digest-cycle behavior
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:14). `save()`'s confirm branch
 * only shows the modal when `popUpFlag` is true; `PopupOkBtn()` (ctrl:
 * 152-155) sets `popUpFlag = false` then re-invokes `save()`, which this
 * time falls through to the real POST. Grepping the whole controller file:
 * `initMaster()` (ctrl:44-53, called after every successful save) does NOT
 * include `popUpFlag` in its reset object -- unlike `sterilizationTypeMaster`,
 * where the equivalent re-init function explicitly does.
 *
 * RESOLVED via Step 5 live testing (2026-07-25): the literal reading above
 * was correct. Empirically confirmed on `hospitalAccountMaster` (byte-
 * identical `popUpFlag`/`PopupOkBtn`/`showModal = !showModal` structure,
 * confirmed via source diff) that after the first confirmed save, `popUpFlag`
 * never resets -- a second Save fired via a single direct call with no
 * confirm-modal interaction saved immediately. This screen's `initMaster()`
 * omitting `popUpFlag` from its reset is real, not an oversight to "correct"
 * with the sterilizationTypeMaster-style pattern. `confirmArmed` below is
 * NEVER re-armed after the first use, on either branch -- faithfully
 * reproducing this exact defect, not fixed.
 *
 * ================================================================
 * Positive confirmation -- edit does NOT bind to the row reference
 * ================================================================
 * `showUpdateBtn(id)` does a fresh `GET` and assigns the response to
 * `$scope.common`, not the `ng-repeat` row object directly -- this screen
 * does not have `sterilizationTypeMaster`'s live-mutation-of-the-visible-
 * table-cell bug (entry 165). Built correctly below via `edit()`'s own
 * fresh `getById()` fetch, same as legacy already does.
 *
 * ================================================================
 * CONFIRMED, MEDIUM -- duplicate-check correctly org/unit-scoped, but
 * checks `tableName` only (not `tableCode`/`tableDesc`)
 * ================================================================
 * `saveOperationTableType`/`updateOperationTableType` both run a real,
 * correctly-scoped duplicate check (`OperationTableType.java:34-46`,
 * `WHERE orgId=:orgId AND unitId=:unitId AND tableName=:tableName`, the
 * update variant adds `AND operationTableTypeId != :id`) -- but `tableCode`
 * and `tableDesc` are never compared for uniqueness, even though `tableCode`
 * is a required, UI-presented identifying field. Two rows with the same
 * Code but different Name (or vice versa) can both be saved with no
 * rejection. Faithfully reproduced: this rebuild's frontend adds no
 * client-side Code/Desc uniqueness check either, and surfaces whatever the
 * server actually returns (see `doCreate()`/`update()`'s `else` branches).
 * Also already-disclosed, project-wide gap present here too:
 * `updateOperationTableTypeStatus`/`getOperationTableTypeListById` look the
 * row up by id alone, no org/unit predicate.
 *
 * ================================================================
 * Additional finding from this rebuild's own direct read of the legacy
 * source (beyond the Step 1 audit brief) -- flagged for owner visibility
 * ================================================================
 * (1) The legacy VIEW marks all THREE fields `required` with distinct,
 * non-colliding control names (`tableCode`/`tableName`/`tableDesc`, view:
 * 21/33/45) and correctly wired `$error.required` messages for each --
 * unlike `sterilizationTypeMaster`'s duplicate-`name="code"` bug, there is
 * no name collision here, so all 3 validation messages genuinely work in
 * real legacy. This rebuild reproduces all 3 as required fields
 * accordingly (not just tableCode/tableName).
 * (2) `update()` (ctrl:229-256) never calls `$broadcast('show-errors-check-
 * validity')` and never checks `operation_Table_typeForm.$valid` -- unlike
 * `save()`, which does both. Taken completely literally, legacy's Update
 * path has ZERO client-side validation (a user could clear all 3 fields and
 * click Update with no client-side gate at all; the anchor is `ng-click`,
 * not a native form submit, so no browser-level HTML5 validation engages
 * either). Per this project's standing validation-visibility policy
 * (`docs/ANGULAR_REBUILD_PLAYBOOK.md`), and to match every sibling screen's
 * convention of validating consistently across create/update, this rebuild
 * applies the SAME required-field gate to `update()` as `save()` (see
 * `validate()` below) -- a disclosed, deliberate consistency improvement
 * beyond literal legacy parity on this one narrow point, flagged here for
 * owner visibility rather than silently ported as a client-side gap.
 *
 * ================================================================
 * Standing, already-approved improvements applied (no new sign-off needed)
 * ================================================================
 * - Shared `<modal>` directive's undefined `cancel()` crash (entry 55, the
 *   19th+ instance) -- this rebuild's close (x) button calls a real, working
 *   `closeConfirmModal()` instead.
 * - Validation messages render visibly and correctly (standing policy).
 */
@Component({
  selector: 'app-operation-table-type',
  imports: [FormsModule, StatusToggle],
  templateUrl: './operation-table-type.html',
  styleUrl: './operation-table-type.scss',
})
export class OperationTableType {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(OperationTableTypeService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common, ctrl:45-51) ----
  form = signal<OperationTableTypeForm>(emptyOperationTableTypeForm());
  editMode = signal(false); // mirrors saveBtnFlag/updateBtnFlag toggling together (ctrl:8-9, 205-207)
  showConfirmModal = signal(false);
  submitAttempted = signal(false); // drives visible required-field messages, see class doc comment
  /** Mirrors `$scope.popUpFlag` -- see class doc comment's writeup of the
   * genuinely ambiguous re-arm timing. Re-armed on every `doCreate()`
   * resolution (success or error), this codebase's now-standard corrected
   * pattern. */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initMasterList/PagerService;
  // unlike sterilizationTypeMaster, this screen's count endpoint is
  // confirmed live/real, so pagination genuinely works) ----
  list = signal<OperationTableTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchText = signal(''); // mirrors ng-model="searchKeyword"

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side filter over only the currently-loaded page -- mirrors the
  // view's own `| filter:searchKeyword` on `ng-repeat` (view:112), which
  // filters ONLY `commonList` (the currently loaded server page), not a
  // fresh server query. Legacy's `ng-keyup="serachTestmaster(searchKeyword)"`
  // (view:76) calls a function never defined anywhere in the controller --
  // a dead, no-op call; the actual filtering happens entirely via Angular's
  // built-in `filter` on the loaded array, which this reproduces via the 3
  // visible text columns.
  filteredList = computed(() => {
    const kw = this.searchText().trim().toLowerCase();
    if (!kw) return this.list();
    return this.list().filter(
      (row) =>
        row.tableCode.toLowerCase().includes(kw) ||
        row.tableName.toLowerCase().includes(kw) ||
        row.tableDesc.toLowerCase().includes(kw),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initMasterList (ctrl:73-108): fetches list + count in parallel,
  // matching the golden trace's 3 unchained 200s on load (template + list +
  // count).
  private loadListAndCount(): void {
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
          this.currentPage.set(1); // mirrors the count success handler's own setPage(1, false)
        }
      },
    });
  }

  // mirrors setPage(page, true) -> masterList (ctrl:113-134/109-127).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  // mirrors setNoOfRecords (ctrl:70-72).
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof OperationTableTypeForm>(field: K, value: OperationTableTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation, all 3 fields -- see class doc comment
  // ("Additional finding" (1)/(2)) for why this covers tableDesc too and why
  // it's applied consistently to both save() and update() despite legacy's
  // update() having no client-side gate at all. ----
  codeRequiredError = computed(() => this.submitAttempted() && this.form().tableCode === '');
  nameRequiredError = computed(() => this.submitAttempted() && this.form().tableName === '');
  descRequiredError = computed(() => this.submitAttempted() && this.form().tableDesc === '');

  private validate(): boolean {
    this.submitAttempted.set(true);
    const f = this.form();
    return f.tableCode !== '' && f.tableName !== '' && f.tableDesc !== '';
  }

  // ---- create entry point (mirrors save(), ctrl:158-201 -- this screen's
  // ONLY create path; the Save button only renders when !editMode()) ----
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

  // mirrors PopupOkBtn (ctrl:152-155): disarms the gate, closes the modal,
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
      .create({
        tableCode: f.tableCode,
        tableName: f.tableName,
        tableDesc: f.tableDesc,
        orgId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully'); // ctrl:169-172 reads response.data.message verbatim
            this.resetForm(); // ctrl:174 -- initMaster()
            this.loadListAndCount(); // ctrl:175 -- re-fetch happens ONLY on success in legacy
          } else {
            // Real, reachable branch here (unlike sterilizationTypeMaster) --
            // this screen's duplicate check genuinely runs. See class doc
            // comment: name-only, code/desc-blind.
            this.notify.error(res.message ?? 'Something went wrong');
          }
          // Step 5 live-verified (2026-07-25), via hospitalAccountMaster's
          // byte-identical popUpFlag/PopupOkBtn structure confirmed empirically
          // (2 assignment sites total, no reset anywhere): the literal reading
          // above was correct all along -- do NOT re-arm confirmArmed here on
          // either branch. Real legacy's confirm dialog fires once per page
          // load, full stop, faithfully reproduced by leaving confirmArmed
          // false for the rest of the session after the first confirm.
        },
        error: () => {
          this.showConfirmModal.set(false);
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:203-227 -- already correct in
  // legacy: fresh GET, no row-reference aliasing) ----
  edit(row: OperationTableTypeRow): void {
    this.editMode.set(true);
    this.submitAttempted.set(false);
    this.api.getById(row.operationTableTypeId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            operationTableTypeId: detail.operationTableTypeId,
            tableCode: detail.tableCode,
            tableName: detail.tableName,
            tableDesc: detail.tableDesc,
          });
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
    });
  }

  // ---- update (mirrors update(), ctrl:229-256 -- no confirm modal, ever,
  // and see class doc comment's "Additional finding" (2) for why this
  // applies validate() here even though legacy's own update() doesn't) ----
  update(): void {
    if (!this.validate()) return;
    const f = this.form();
    this.api
      .update({
        operationTableTypeId: f.operationTableTypeId as number,
        tableCode: f.tableCode,
        tableName: f.tableName,
        tableDesc: f.tableDesc,
        orgId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated successfully'); // ctrl:242-245 reads response.data.message verbatim
            this.resetForm(); // ctrl:247 -- initMaster(), resets all the way back to CREATE mode
            this.loadListAndCount(); // ctrl:248 -- re-fetch happens ONLY on success in legacy
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:280-309 -- see class doc
  // comment's full writeup of the shared-object-mutation finding and how
  // this rebuild reproduces its OBSERVABLE end-state) ----
  toggleStatus(row: OperationTableTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing --
    // never reverted below, matching legacy's own lack of a revert branch on
    // failure.
    this.list.update((rows) =>
      rows.map((r) => (r.operationTableTypeId === row.operationTableTypeId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        operationTableTypeId: row.operationTableTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          // Legacy's own handler DOES read response.data.message here
          // (unlike sterilizationTypeMaster's hardcoded literals) --
          // reproduced verbatim.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated successfully');
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.notify.error('Something went wrong'),
      });
    // Deliberately does NOT call resetForm()/touch `form`/`editMode`/
    // `submitAttempted` -- legacy's own updateStatus() never calls
    // initMaster() either, so the add/edit form is never reset as a side
    // effect of a status toggle. This is the faithfully-reproduced
    // behavioral parity point from this screen's Step 1 finding: whatever
    // is currently in the add-form (a half-typed create, or an in-progress
    // edit) is left untouched after toggling an unrelated row's status.
  }

  // mirrors initMaster() (ctrl:44-53) as re-entered by save()/update()'s
  // success handler only.
  private resetForm(): void {
    this.form.set(emptyOperationTableTypeForm());
    this.editMode.set(false);
    this.submitAttempted.set(false);
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:160-161/232-233/283-284. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
