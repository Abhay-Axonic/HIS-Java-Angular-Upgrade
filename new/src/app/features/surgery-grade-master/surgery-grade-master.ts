import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setPage,
  setFirstPage,
  setPreviousPage,
  setNextPage,
  setLastPage,
  getPagesList,
} from '../../core/client-pagination.util';
import { SurgeryGradeMasterService } from './surgery-grade-master.service';
import { SurgeryGradeForm, SurgeryGradeRow, emptySurgeryGradeForm } from './surgery-grade-master.models';

/**
 * Parity rebuild of `views/master/GlobalMaster/surgery-grade-master.html`
 * (124 lines) + `resources/assets/js/controller/opd/global/
 * surgeryGradeMasterController.js` (289 lines, `surgeryGradeMasterController`,
 * no `controllerAs` -- every binding is classic `$scope.*`, confirmed zero
 * `vm.*` usage anywhere in the view). State `surgeryGradeMaster`
 * (`app.js:15470-15475`) has **NO `resolve` block at all** -- confirmed by
 * direct read, single registration, no shadow state -- so there is
 * deliberately **no `data.moduleId`** on this screen's eventual route
 * (registered separately by the orchestrating session), same no-gate class
 * as `billingGroupMaster`/`periodicityService`. `$state` IS in this
 * controller's own DI list but never actually called (dead injection,
 * harmless) -- the recurring missing-`$state`-DI crash bug does NOT apply
 * here.
 *
 * Backend confirmed real end-to-end by reading source directly:
 * `SurgeryGradeMasterController.java` (`@RequestMapping("api/global")`) ->
 * `SurgeryGradeMasterServiceImpl` -> `SurgeryGradeMasterDaoImpl` -> table
 * `public.m_surgery_grade_master`. Golden trace this session: 2 calls on
 * load (template fetch + `getSurgeryGradeMasterList`), both 200 -- no
 * separate count call, this screen has no server-side pagination-count
 * endpoint at all; it fetches the FULL result set in one shot and paginates
 * entirely client-side via legacy's own `PaginationService`
 * (`resources/assets/js/service/paginationService.js`), ported verbatim to
 * `../../core/client-pagination.util.ts`. There is no search box anywhere in
 * this screen's view -- only a "Show N records" page-size selector -- so
 * `PaginationState.search` is never set by any UI here and `filteredData`
 * always equals the full list.
 *
 * ================================================================
 * CONFIRMED, MEDIUM-HIGH SEVERITY -- duplicate-check broken on BOTH Create
 * and Update, in two independently confirmed, DIFFERENT ways (see
 * `docs/ANGULAR_PENDING_FINDINGS.md`, `PENDING-batch18-step1-surgerygrademaster`)
 * ================================================================
 * - **Create -- cross-organization false positive via an operator-precedence
 *   bug.** `GET_SURGERY_GRADE_LIST_BY_NAME` (`SurgeryGradeMaster.java:40-47`)
 *   reads `WHERE LOWER(x)=:p OR x=:p AND orgnisationId=:orgId` -- JPQL `AND`
 *   binds tighter than `OR`, so this parses as
 *   `LOWER(x)=:p OR (x=:p AND orgId=:orgId)`. Since the DAO always passes the
 *   lowercased value, the `LOWER(...)` branch has NO org-scope at all -- a
 *   Create can be incorrectly rejected as "already exists" based on a name
 *   match in ANY organization, not just the caller's own. This is entirely
 *   server-side and directly user-observable (a rejected Create). Reproduced
 *   by simply displaying whatever `ALREADY_EXIST`/error response the server
 *   returns (see `doCreate()` below) -- no client-side org-scoping
 *   workaround added.
 * - **Update -- CONFIRMED PERMANENT NO-OP.** See
 *   `surgery-grade-master.models.ts`'s doc comment on
 *   `SurgeryGradeUpdatePayload` for the full mechanism: legacy's own update
 *   payload never includes `orgnisationId`, so the server-side duplicate
 *   check's `orgnisationId=:orgId` predicate always binds `null` and matches
 *   zero rows -- an Update can NEVER be rejected as a duplicate, ever,
 *   regardless of any real collision. **Faithfully reproduced by
 *   deliberately NOT sending `orgnisationId`/`organizationId` in the update
 *   payload** (`doUpdate()` below) -- sending it would silently start making
 *   the check work, an undisclosed behavioral change this project's standing
 *   policy prohibits. Already logged in the findings doc for the owner's
 *   future decision; not fixed here.
 *
 * ================================================================
 * Dangling `if` (no braces) in legacy's list-load callback -- a real legacy
 * JS-fragility bug, NOT reproduced here (nothing to reproduce in typed code)
 * ================================================================
 * `init()`'s list callback (ctrl:96-105) reads:
 * ```js
 * if (response.data.status == "success")
 *     $scope.pagination.data=[];
 *     $scope.surgeryGradeList = response.data.listObject;
 *     if($scope.surgeryGradeList.length > 0){ ... }
 * ```
 * Only the first statement is actually gated by the `if` -- everything after
 * runs unconditionally, even on a non-success response, so a failure
 * response with no `listObject` throws a `TypeError` reading `.length` on
 * `undefined`. This is a real, narrow legacy bug, but there is no
 * unbraced-if equivalent risk in this rebuild's typed code -- `loadList()`
 * below just handles the success/non-success cases correctly and normally
 * (empty list on non-success), which is not a divergence to flag, just
 * correct handling in a language where the bug class doesn't exist.
 *
 * ================================================================
 * `popUpFlag`/confirm-modal re-arm timing -- AMBIGUOUS, flagged for Step 5
 * live verification, same treatment as `hospitalAccountMaster`
 * ================================================================
 * `$scope.popUpFlag` starts `true` (ctrl:61). `saveSurgeryGrade()`
 * (ctrl:125-174): if valid and `popUpFlag` is true, toggles `showModal`
 * instead of saving; `PopupOkBtn()` (ctrl:119-122) sets `popUpFlag=false`
 * then calls `saveSurgeryGrade()` again, which this time falls through to
 * the real POST. Read in isolation, `popUpFlag` is reset back to `true`
 * ONLY inside `init()` (ctrl:33-114), which the save handler's own success
 * branch re-enters (ctrl:161) -- the FAILURE branch (ctrl:163-169) never
 * touches it, so a rejected create would naively appear to leave
 * `popUpFlag` stuck `false` forever, silently skipping the confirm modal on
 * every subsequent Save click. However, this codebase has independently
 * found (entry 152, re-confirmed live on `cardTypeMaster`/`raceMaster`) that
 * the SHARED `<modal>` directive's own Ok-button/`$watch` may re-arm this
 * class of flag on every close, structurally, regardless of what any
 * individual screen's own success/failure branches do -- a digest-timing
 * question this project has NOT independently re-verified for THIS specific
 * screen's markup. Built here, as instructed, "as currently understood":
 * `confirmArmed` is reset to `true` ONLY on a successful create (via
 * `resetForm()`, matching the controller's own explicit, literal `init()`
 * re-entry logic), and deliberately NOT reset in the failure branch.
 * // TODO(step5-verify): confirm live against real, unmodified legacy
 * // whether a rejected Create (e.g. the cross-org false-positive above)
 * // actually leaves the confirm modal permanently skipped on every
 * // subsequent Save click, or whether the shared `<modal>` directive's own
 * // digest-driven re-arm (entry 152) prevents this in practice -- adjust
 * // `doCreate()`'s error handling below to match whichever is confirmed.
 *
 * ================================================================
 * Status-toggle -- no rollback on error, faithfully reproduced
 * ================================================================
 * `updateStatus()` (ctrl:209-244) only ever growls a toast on success/error
 * -- no revert of `info.status` on failure, and no list reload either way.
 * The view's checkbox is two-way bound (`ng-model="info.status"`), so the
 * visual flip happens immediately regardless of the response. Reproduced
 * via an optimistic local patch in `toggleStatus()` below, via
 * `<app-status-toggle>`, never reverted on error.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - Growl messages are hardcoded literal strings throughout (ctrl:156-159,
 *   164-167, 231-234, 237-240, 269-272, 277-280), NOT `response.data.message`
 *   passthrough -- reproduced verbatim, including legacy's own typo
 *   ("Surgery grade already exits...!!", not "exists").
 * - `saveMode`/edit-button visibility mirrors `saveBtnFlag`/`updateBtnFlag`
 *   (ctrl:55-56, view:50-54) -- two SEPARATE buttons (Save / Update), unlike
 *   some sibling screens whose "Update" button is dead code; here both are
 *   real and toggle correctly.
 * - `showUpdateBtn` (ctrl:177-206) flips to Update mode IMMEDIATELY on
 *   click, before the GET even resolves (ctrl:179-180) -- reproduced via
 *   `saveMode.set(false)` at the very top of `edit()`. Its failure branch is
 *   a raw native `alert("Error!!")` (ctrl:201), NOT `growl.error` --
 *   reproduced verbatim via `window.alert(...)`, matching this codebase's
 *   established convention for this exact quirk (e.g. `discount-category-
 *   master`, `currency-master`).
 * - No `startLoader()`/`stopLoader()` spinner symmetry modeled beyond what's
 *   already established as this codebase's convention (`$rootScope.
 *   startLoader()` is called unconditionally at the top of `saveSurgeryGrade()`
 *   even when validation fails or the confirm modal merely opens, and
 *   `stopLoader()` only fires inside the POST's own response callback --
 *   a narrow, pre-existing legacy loader-symmetry gap, not modeled via a
 *   `LoaderService` here, matching sibling screens `billing-group-master`/
 *   `sterilization-type-master`'s established no-loader convention).
 * - The table header reads "Status(v)" (view:70) -- legacy's own literal
 *   text, not a placeholder or typo introduced here.
 * - Second field's required-error text reads "Description is required"
 *   (view:30) even though the field's own label is "Name" -- a real,
 *   harmless, visible legacy copy-paste mismatch (not a silently-lost
 *   message), preserved verbatim per this codebase's precedent
 *   (`discount-category-master`'s identical class of mismatch).
 */
@Component({
  selector: 'app-surgery-grade-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './surgery-grade-master.html',
  styleUrl: './surgery-grade-master.scss',
})
export class SurgeryGradeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(SurgeryGradeMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.surgeryGradeObj, ctrl:107-112) ----
  form = signal<SurgeryGradeForm>(emptySurgeryGradeForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false); // drives *required* error display, mirrors show-errors-check-validity
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment's "AMBIGUOUS"
   * section. Reset to `true` ONLY on a successful create, per the
   * controller's own explicit `init()` re-entry logic -- deliberately NOT
   * reset on a failed create. Flagged for Step 5 live verification. */
  private confirmArmed = true;

  // ---- full list + entirely client-side pagination (mirrors init() +
  // PaginationService, ctrl:82-105) ----
  private fullList: SurgeryGradeRow[] = [];
  pagination = signal<PaginationState<SurgeryGradeRow>>(createPaginationState<SurgeryGradeRow>());

  constructor() {
    this.loadList();
  }

  // ctrl:82-105 (init) -- fetches the FULL result set in one shot; see
  // class doc comment on the dangling-if bug this correctly avoids by
  // construction (typed handling, not a literal reproduction of the crash).
  private loadList(): void {
    this.api.getList(this.organizationId).subscribe({
      next: (res) => {
        this.fullList = res.status === 'success' ? (res.listObject ?? []) : [];
        const p = this.pagination();
        searchData(this.fullList, p);
        this.pagination.set({ ...p });
      },
    });
  }

  // mirrors the page-size <select>'s ng-change="pagination.setTotalPages(pagination)" (view:38-40).
  setPageSize(): void {
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  page(action: 'first' | 'prev' | 'next' | 'last' | number): void {
    const p = this.pagination();
    if (action === 'first') setFirstPage(p);
    else if (action === 'prev') setPreviousPage(p);
    else if (action === 'next') setNextPage(p);
    else if (action === 'last') setLastPage(p);
    else setPage(action, p);
    this.pagination.set({ ...p });
  }

  pagesList(): number[] {
    return getPagesList(this.pagination());
  }

  updateField<K extends keyof SurgeryGradeForm>(field: K, value: SurgeryGradeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors surgeryGradeform.$valid via the
  // 2 `required` inputs -- surgeryGradeCode/surgeryGrade, view:14,28) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.surgeryGradeCode.trim() && f.surgeryGrade.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().surgeryGradeCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().surgeryGrade.trim();
  }

  // ---- save / confirm-modal (mirrors saveSurgeryGrade/PopupOkBtn, ctrl:118-174) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the surgeryGradeform.$valid gate
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doCreate();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:119-122): disarms the gate, closes the modal,
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
        surgeryGradeCode: f.surgeryGradeCode,
        surgeryGrade: f.surgeryGrade,
        unitId: this.unitId,
        orgnisationId: this.organizationId,
        status: 'A',
        createdDate: now,
        createdBy: this.userId,
        updatedDate: now,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Surgery grade added Successfully...!!'); // ctrl:156-159, verbatim
            this.resetForm(); // ctrl:161 -- init(), re-arms confirmArmed too
            this.loadList();
          } else {
            // Real, disclosed backend defect (cross-org false-positive) --
            // see class doc comment. Reproduced by simply surfacing whatever
            // the server returns; no client-side org-scoping workaround.
            this.notify.error('Surgery grade already exits...!!'); // ctrl:164-167, verbatim typo, not corrected
            // Deliberately NOT re-arming confirmArmed here -- see class doc
            // comment's "AMBIGUOUS" section and the TODO(step5-verify) above.
          }
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:177-206) ----
  edit(row: SurgeryGradeRow): void {
    // ctrl:179-180 -- legacy flips saveBtnFlag/updateBtnFlag to "Update"
    // mode IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.api.getById(row.surgeryGradeId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const r = res.listObject[0];
          this.form.set({
            surgeryGradeId: r.surgeryGradeId,
            surgeryGradeCode: r.surgeryGradeCode,
            surgeryGrade: r.surgeryGrade,
          });
        } else {
          window.alert('Error!!'); // ctrl:201 -- a real native alert, not growl
        }
      },
    });
  }

  // ---- update (mirrors updateSurgeryGrade, ctrl:247-286 -- no confirm
  // modal, ever). Payload deliberately omits orgnisationId/organizationId --
  // see surgery-grade-master.models.ts's SurgeryGradeUpdatePayload doc
  // comment for why (the confirmed permanent no-op duplicate-check). ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the surgeryGradeform.$valid gate
    const f = this.form();
    this.api
      .update({
        surgeryGradeCode: f.surgeryGradeCode,
        surgeryGrade: f.surgeryGrade,
        surgeryGradeId: f.surgeryGradeId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Surgery grade updated Successfully...!!'); // ctrl:269-272, verbatim
            this.resetForm(); // ctrl:273 -- init(), resets all the way back to CREATE mode
            this.loadList();
          } else {
            // Dead in practice against the real backend today (the no-op
            // duplicate-check above can never actually reject an update) --
            // reproduced for parity/defensiveness, matching the controller's
            // own literal branch.
            this.notify.error('Surgery grade already exits...!!'); // ctrl:277-280, verbatim typo
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:209-244 -- no rollback
  // on error, faithfully reproduced) ----
  toggleStatus(row: SurgeryGradeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing --
    // never reverted below, matching legacy's own lack of a revert branch
    // on failure.
    this.fullList = this.fullList.map((r) =>
      r.surgeryGradeId === row.surgeryGradeId ? { ...r, status: nextStatus } : r,
    );
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });

    this.api
      .updateStatus({
        surgeryGradeId: row.surgeryGradeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Surgery grade Updated Successfully...!!'); // ctrl:231-234, verbatim
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:237-240, verbatim -- no revert
          }
        },
      });
  }

  // mirrors init() (ctrl:33-114) as re-entered by a successful
  // create/update -- resets the form AND re-arms confirmArmed, matching the
  // controller's own literal re-entry (see class doc comment's "AMBIGUOUS"
  // section for why the FAILURE branch deliberately does not call this).
  private resetForm(): void {
    this.form.set(emptySurgeryGradeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.confirmArmed = true;
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:126-128/210-212/248-250. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
