import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DepartmentMasterUnitService } from './department-master-unit.service';
import {
  DepartmentMasterUnitRow,
  DetailsForm,
  SpecialityCandidate,
  StagedSpeciality,
  emptyDetailsForm,
} from './department-master-unit.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * `views/master/UnitMaster/organizationMaster/department-master-unit.html`
 * (166 lines) +
 * `resources/assets/js/controller/billing/unit/departmentMasterUnitController.js`
 * (389 lines, `departmentMasterUnitController`, no `controllerAs` --
 * classic `$scope.*` throughout, confirmed zero `vm.*` usage). State
 * `departmentMasterUnit` (app.js:8443-8469, `url: '/departmentMasterUnit'`)
 * is registered directly in `app.js` with a real `resolve` block gating
 * `permissions.modules` id **533**, no `requireSuperUser` -- confirmed by
 * reading the state config directly. `$state` IS in this controller's own
 * DI list (ctrl:7) and the top-level `cookieObject == undefined ->
 * $state.go('login')` guard (ctrl:9-12) is real and correctly wired -- the
 * recurring missing-`$state`-DI crash bug (entries 8/10) does NOT apply
 * here.
 *
 * **Risk tag CORRECTED this session** from entry 118's folder-path-only
 * "billing-only" call to content-verified **CLINICAL-ONLY** -- see
 * `department-master-unit.models.ts`'s own doc comment and
 * docs/ANGULAR_PENDING_FINDINGS.md `PENDING-departmentmasterunit-billing-
 * mistag` for the full correction writeup. No Step 6b trigger (no
 * calculation/dosing/patient-identification/consent content).
 *
 * **Uses a plain Bootstrap modal (`#detailsMapperModal`,
 * `data-dismiss="modal"`) for its Details dialog, NOT the shared
 * prototypal `<modal>` directive** -- entry 55's `cancel()`-crash standing
 * policy does not apply to this screen; there is no legacy crash to
 * preserve here. This rebuild uses a normal, working Angular modal
 * (`showDetailsModal` signal + the shared `.modal-backdrop`/`.modal`
 * classes from `src/styles.scss`, same pattern already used by several
 * sibling screens' confirm-modals).
 *
 * **Confirmed dead, unreachable copy-paste leftover code, NOT built here:**
 * `showUpdateBtn()`/`updateDeptAtUnitLevel()`/`getSpecialityByGroup()`
 * (ctrl:234-298) -- the pencil/edit icon that would trigger the first two
 * is commented out in the view (view:136-140), and `getSpecialityByGroup`
 * is never called by anything in the controller or view at all (grepped).
 * These are copy-paste leftovers from a different screen's template, not a
 * live feature to port.
 *
 * ================================================================
 * The real UI: a two-pane checkbox picker (check -> stage -> Save), NOT a
 * single-step "check and Save" flow
 * ================================================================
 * Reading the view directly (view:9-45): the "Global Master" pane (left)
 * lists every unmapped-speciality candidate from `getOrgActiveSpecialityList`
 * as checkboxes; checking one calls `addTempSpecialty` (ctrl:84-97), which
 * toggles membership in `$scope.tempSelectSpecialty` (add if unchecked-to-
 * checked, remove if checked-to-unchecked). The "<<>>" button
 * (view:25, `selectSpeciality()`, ctrl:79-81) then REPLACES
 * `$scope.selectedSpecialityList` with a fresh copy of whatever is
 * currently in `tempSelectSpecialty` -- and `saveDeptAtUnitLevel()`
 * (ctrl:100-131, the floppy-disk Save button, view:70-73) reads ONLY
 * `selectedSpecialityList`, not `tempSelectSpecialty`, when building the
 * save payload. So checking boxes alone does nothing towards Save --
 * moving them via "<<>>" into the "Unit Master" pane (right, view:29-41,
 * display-only labels) is a required intermediate step. Faithfully
 * reproduced below as `checkedCandidateIds` (check state) ->
 * `moveToSelected()` (the "<<>>" button, REPLACES `selectedSpecialityList`,
 * matching `angular.copy`'s overwrite-not-merge semantics) ->
 * `selectedSpecialityList` (what `save()` actually reads). The task
 * brief's own summary describes this as "pick ... check some ... Save" in
 * simplified form; the real, source-verified mechanism is this 2-step
 * check-then-move-then-save flow, built here to match the actual
 * controller/view rather than the shorthand summary.
 *
 * ================================================================
 * RETRACTED, 2026-07-25 -- the `allowedOverbookingSlots` typo has NO
 * observable effect; original severity/failure-mode claim was wrong
 * ================================================================
 * The typo itself is real (ctrl:181-183's `$scope.allowedOverbookingSlot`,
 * missing the trailing "s", means the empty-string fallback-to-zero clause
 * never fires). But Step 5 live testing (direct request replay against
 * unmodified legacy) confirmed sending `allowedOverbookingSlots: ""`
 * returns 200, not 400 -- Spring/Jackson coerces the empty string to `0`
 * for this boxed `Integer` field regardless of the app's own dead client
 * fallback. The persisted row went from `null` to `0` after the call, same
 * end result the JS fallback was (uselessly) trying to produce. This
 * rebuild sends the field's raw string value verbatim, same as legacy --
 * which is fine, since the server produces the correct result either way.
 * No fix needed, nothing to reproduce -- this is dead code with no
 * behavioral consequence, not a live defect.
 *
 * ================================================================
 * CONFIRMED, MEDIUM -- status-toggle has no rollback on error, with a
 * naming trap
 * ================================================================
 * `updateStatus()`'s success handler (ctrl:146-151) calls
 * `$scope.initBillingGroupMaster()` -- despite the name, this is the
 * CREATE-FORM-RESET function (ctrl:34-48), NOT a list reload (confirmed by
 * direct read) -- so success never re-fetches the mapped-department list
 * either (harmless, since the optimistic client-side flip already matches
 * the server). On FAILURE, there is no rollback of the toggle's visible
 * state at all -- same no-round-trip-confirmation bug class already found
 * on this batch's other 3 screens. Faithfully reproduced in `toggleStatus`
 * below: the flip is applied optimistically and never reverted on error.
 *
 * ================================================================
 * Narrow race condition (severity 3), faithfully reproduced -- no
 * request-token guard on the two success-triggered reloads
 * ================================================================
 * Both `saveDeptAtUnitLevel()` and `updateDetails()` trigger
 * `initBillingGroupMasterList()` (ctrl:125/216) unconditionally on
 * success, overwriting `$scope.unitDeptMasterList`/`$scope.commonListCount`
 * with no guard against an overlapping in-flight request -- e.g. opening
 * the Details modal on one row while clicking the picker's Save on another
 * in quick succession. Whichever response resolves last wins regardless of
 * which fired last. Narrow trigger (requires near-simultaneous distinct
 * actions); not fixed here, matching this batch's own standing policy of
 * not adding request-cancellation logic legacy doesn't have.
 *
 * ================================================================
 * Duplicate-check -- the strongest of this batch, but not airtight; NOT
 * strengthened further here
 * ================================================================
 * The candidate picker itself is server-filtered
 * (`GET_ORG_ACTIVE_SPECIALITY_LIST_NOT_MAPPED_TO_UNIT` excludes anything
 * already mapped, active or inactive, to this unit) -- a real, working
 * mechanism, better than every other screen in this batch. Gap:
 * `saveUnitSpecialtiy` itself has no existence re-check at save time
 * (plain unconditional Hibernate `save()`, no DB unique constraint on
 * `(unit_id, speciality_id)` either), and the Save button has NO in-flight
 * guard -- a rapid double-click before the picker/table refreshes could
 * submit the same speciality mapping twice. Narrow (protected on every
 * normal single-click, sequential-use path; only exposed by a genuine
 * double-click or true concurrency). Faithfully reproduced: `save()` below
 * adds no debounce/disable-while-saving guard, per this batch's standing
 * policy of not silently adding a fix legacy doesn't have without owner
 * sign-off.
 *
 * ================================================================
 * Search: a real server round trip on every keystroke, PLUS a redundant
 * client-side filter layered on top of whatever's currently loaded
 * ================================================================
 * `searchDepartmentMasterUnit(searchString)` (ctrl:365-388) is wired via
 * `ng-keyup` (view:77) -- fires on every keystroke, no debounce, calling
 * the real `/adt/unitDepartmentMasterUniversalSearch` endpoint each time
 * (or reloading the full paginated list via `initBillingGroupMasterList`
 * when the box is cleared to exactly `''`). Independently, the SAME
 * `searchKeyword` scope var also drives a plain Angular `filter:searchKeyword`
 * pipe directly in the `ng-repeat` (view:122) -- applied on top of
 * whichever list is currently loaded (paginated page OR search results),
 * checking all of a row's own string/number fields for a substring match.
 * This is a double-filtering quirk (server search + client re-filter of
 * the same criterion), reproduced faithfully below via `onSearchChange`
 * (the real per-keystroke server call) plus `filteredList` (the redundant
 * client-side re-filter). Also faithfully reproduced: legacy's own
 * `$scope.offset` is initialized to `0` (ctrl:25) and never reassigned
 * anywhere else in this controller, so clearing the search box always
 * reloads from offset 0 (page 1) regardless of which page was showing
 * beforehand -- `onSearchChange` mirrors this by resetting `currentPage`
 * to 1 on clear, not restoring whatever page was active.
 *
 * ================================================================
 * Toast provenance -- see `department-master-unit.service.ts`'s class doc
 * comment for which calls get an automatic message (via
 * `BillingGenericService`'s own success/error wrapper) vs. the
 * controller's own explicit, hardcoded (and typo'd) literals (via plain
 * `GenericService`) vs. no toast at all (search). Reproduced per-call
 * below to match.
 * ================================================================
 * - Save (picker): automatic toast, server message = `COMMON_SUCCESS`
 *   ("Added Successfully.", `ICommonConstants.java:19`) on success; no
 *   controller-side growl call exists for this action.
 * - Status toggle: automatic toast, server message = `COMMON_UPDATE`
 *   ("Updated Successfully.", `ICommonConstants.java:21`) on success; same
 *   automatic-wrapper mechanism, no controller-side growl call either.
 * - Details-modal Update: controller's OWN explicit, hardcoded literals --
 *   `'Updated successfully...!!'` (ctrl:195) on success, `'Somethung went
 *   wrong.!!'` (ctrl:222, legacy's own typo, reproduced verbatim) on a
 *   soft `status !== 'success'` response. **RESOLVED, 2026-07-25:** the
 *   hard-HTTP-400 premise above was wrong (see the retracted finding at the
 *   top of this file) -- a cleared `allowedOverbookingSlots` returns a real
 *   200, always taking the soft-success path with its own hardcoded
 *   `'Updated successfully...!!'` toast. No hard-error path is actually
 *   reachable via this specific field, so the single-argument-`.then()`
 *   silent-swallow question is moot for this trigger (it may still apply
 *   to a genuine network/server error, unrelated to this finding). No
 *   change needed to the success/error toast wiring below.
 * - Search: no toast of any kind, ever (confirmed: called with no
 *   `notificationStatus` argument at all).
 */
@Component({
  selector: 'app-department-master-unit',
  imports: [FormsModule, StatusToggle],
  templateUrl: './department-master-unit.html',
  styleUrl: './department-master-unit.scss',
})
export class DepartmentMasterUnit {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DepartmentMasterUnitService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- checkbox picker state (mirrors specialityMasterList/
  // tempSelectSpecialty/selectedSpecialityList -- see class doc comment's
  // "two-pane checkbox picker" writeup) ----
  specialityMasterList = signal<SpecialityCandidate[]>([]);
  checkedCandidateIds = signal<Set<number>>(new Set());
  selectedSpecialityList = signal<StagedSpeciality[]>([]);

  // ---- mapped-department list + server-side pagination (mirrors
  // unitDeptMasterList/commonListCount/pager) ----
  list = signal<DepartmentMasterUnitRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Redundant client-side re-filter over whatever's currently loaded -- see
  // class doc comment ("double-filtering quirk"). Mirrors Angular's
  // built-in `filter:expression` default behavior (any stringifiable field
  // matches).
  filteredList = computed(() => {
    const kw = this.searchKeyword().toLowerCase();
    if (!kw) return this.list();
    return this.list().filter((row) =>
      Object.values(row).some((v) => v !== null && v !== undefined && String(v).toLowerCase().includes(kw)),
    );
  });

  // ---- Details modal (mirrors getFlagByMapperId/updateDetails) ----
  showDetailsModal = signal(false);
  detailsForm = signal<DetailsForm>(emptyDetailsForm());

  constructor() {
    this.loadCandidates();
    this.loadListAndCount();
  }

  // ---- checkbox picker ----

  // mirrors addTempSpecialty (ctrl:84-97).
  toggleCandidate(candidate: SpecialityCandidate): void {
    this.checkedCandidateIds.update((ids) => {
      const next = new Set(ids);
      if (next.has(candidate.specialityId)) {
        next.delete(candidate.specialityId);
      } else {
        next.add(candidate.specialityId);
      }
      return next;
    });
  }

  // mirrors selectSpeciality (ctrl:79-81) -- REPLACES selectedSpecialityList
  // with the currently-checked set, matching angular.copy's overwrite (not
  // merge/append) semantics.
  moveToSelected(): void {
    const checked = this.checkedCandidateIds();
    this.selectedSpecialityList.set(
      this.specialityMasterList()
        .filter((c) => checked.has(c.specialityId))
        .map((c) => ({ specialityId: c.specialityId, specialityName: c.specialityName })),
    );
  }

  // mirrors saveDeptAtUnitLevel (ctrl:100-131, the floppy-disk Save
  // button). No in-flight/disable-while-saving guard -- faithful, see
  // class doc comment's duplicate-check writeup.
  save(): void {
    if (this.selectedSpecialityList().length === 0) {
      this.notify.warning('Please assign at least one department..!!'); // ctrl:102, verbatim
      return;
    }
    const now = formatDateTime(new Date());
    this.api
      .save({
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        unitSpecialtyMapperDtosList: this.selectedSpecialityList(),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Added Successfully.'); // auto-toast, server message = COMMON_SUCCESS
            this.resetPickerSelection(); // ctrl:121-124
            this.loadListAndCount(); // ctrl:125
            this.loadCandidates(); // ctrl:126
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private resetPickerSelection(): void {
    this.selectedSpecialityList.set([]);
    this.checkedCandidateIds.set(new Set());
  }

  private loadCandidates(): void {
    this.api.getCandidates(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.specialityMasterList.set(res.listObject ?? []);
      },
    });
  }

  // ---- mapped-department list + pagination ----

  // mirrors initBillingGroupMasterList (ctrl:304-326) -- $scope.offset is
  // never reassigned away from 0 anywhere in legacy, see class doc
  // comment's search writeup, so a full reload always starts at offset 0.
  private loadListAndCount(): void {
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  // mirrors setPage(page,true) -> getBillingGroupMasterList (ctrl:351-361).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  // mirrors setNoOfRecords (ctrl:300-302).
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  // mirrors searchDepartmentMasterUnit (ctrl:365-388) -- see class doc
  // comment's search writeup for the double-filtering + offset-always-0
  // quirks this reproduces.
  onSearchChange(value: string): void {
    this.searchKeyword.set(value);
    if (value === '') {
      this.currentPage.set(1);
      this.loadListAndCount();
      return;
    }
    this.api
      .search({ organizationId: this.organizationId, specialityName: value, unitId: this.unitId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.list.set(res.listObject ?? []);
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:134-152) ----
  toggleStatus(row: DepartmentMasterUnitRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip first, matching legacy's own ng-model-before-handler
    // timing -- NEVER reverted below on failure, matching legacy's own
    // lack of a rollback branch. See class doc comment.
    this.list.update((rows) =>
      rows.map((r) => (r.unitSpecialityMapperId === row.unitSpecialityMapperId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        unitSpecialityMapperId: row.unitSpecialityMapperId,
        status: nextStatus,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: formatDateTime(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.'); // auto-toast, server message = COMMON_UPDATE
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  // ---- Details modal (mirrors getFlagByMapperId/updateDetails,
  // ctrl:154-231) ----

  openDetails(row: DepartmentMasterUnitRow): void {
    this.detailsForm.set({
      unitSpecialityMapperId: row.unitSpecialityMapperId,
      isSurgicalCode: row.isSurgicalCode || 'N',
      isClinicalSpeciality: row.isClinicalSpeciality || 'N',
      isRenderingSpeciality: row.isRenderingSpeciality || 'N',
      isKioskDept: row.isKioskDept || 'N',
      isNursingAssessmentRequired: row.isNursingAssessmentRequired || 'N',
      deptWiseAppointment: row.deptWiseAppointment || 'N',
      // No '' / null fallback for these 2 -- matches ctrl:162-163's direct,
      // un-defaulted passthrough.
      allowedOverbookingSlots: row.allowedOverbookingSlots != null ? String(row.allowedOverbookingSlots) : '',
      pddDays: row.pddDays != null ? String(row.pddDays) : '',
    });
    this.showDetailsModal.set(true);
  }

  updateDetailsField<K extends keyof DetailsForm>(field: K, value: DetailsForm[K]): void {
    this.detailsForm.update((f) => ({ ...f, [field]: value }));
  }

  // Cancel/X -- data-dismiss="modal" in legacy, which just closes without
  // resetting any scope var (confirmed: no reset call on Cancel). Mirrored
  // here: closing does not reset detailsForm either.
  closeDetailsModal(): void {
    this.showDetailsModal.set(false);
  }

  updateDetails(): void {
    const f = this.detailsForm();
    this.api
      .updateDetails({
        unitSpecialityMapperId: f.unitSpecialityMapperId,
        isSurgicalCode: f.isSurgicalCode,
        isRenderingSpeciality: f.isRenderingSpeciality,
        isClinicalSpeciality: f.isClinicalSpeciality,
        deptWiseAppointment: f.deptWiseAppointment,
        isKioskDept: f.isKioskDept,
        isNursingAssessmentRequired: f.isNursingAssessmentRequired,
        // FAITHFUL typo-bug reproduction -- see department-master-
        // unit.models.ts's UpdateDetailsPayload doc comment. Sent verbatim,
        // never defaulted, even when ''.
        allowedOverbookingSlots: f.allowedOverbookingSlots,
        pddDays: f.pddDays,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Updated successfully...!!'); // ctrl:195, verbatim hardcoded literal
            this.showDetailsModal.set(false);
            this.resetPickerSelection(); // ctrl:213-215
            this.loadListAndCount(); // ctrl:216
            this.loadCandidates(); // ctrl:217
          } else {
            this.notify.error('Somethung went wrong.!!'); // ctrl:222, verbatim hardcoded typo
          }
        },
        // See class doc comment's "toast provenance" section for the
        // disclosed, unresolved discrepancy between this and a strict
        // reading of legacy's own single-argument `.then()` here.
        error: () => this.notify.error('Somethung went wrong.!!'),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:109-113/143. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
