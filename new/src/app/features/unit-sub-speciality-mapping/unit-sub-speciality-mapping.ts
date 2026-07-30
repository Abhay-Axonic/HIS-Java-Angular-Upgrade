import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { UnitSubSpecialityMappingService } from './unit-sub-speciality-mapping.service';
import {
  AvailableSubSpeciality,
  PickedSubSpeciality,
  UnitSpecialityOption,
  UnitSubSpecialityRow,
} from './unit-sub-speciality-mapping.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const GENERIC_ERROR = 'Something went wrong, please try again';

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/sub-department-master-unit.html
 * + resources/assets/js/controller/billing/unit/subDepartmentMasterUnitController.js.
 * Legacy state `subDepartmentMasterUnit` (app.js:8524-8544), title
 * 'Sub Department Master', gated by a resolve-block check for **moduleId
 * 534** -- reproduced at the route level via `moduleAccessGuard`
 * (`data: { moduleId: 534 }` in app.routes.ts), not in this component
 * (same convention as referral-master.ts). `$state` IS injected and the
 * controller's `cookieObject == undefined -> $state.go('login')` guard is
 * real; `moduleAccessGuard`'s own `!session.current` check already
 * reproduces that at the route level.
 *
 * ============================================================================
 * NAMING DISCLOSURE (judgement call -- flagged per task instructions, Step 6
 * owner review should confirm or override):
 * ============================================================================
 * Despite legacy's title/URL/UI labels ("Sub Department Master",
 * "Department Code/Description"), this screen does NOT manage a
 * "sub-department" entity at all -- it manages Speciality -> Sub-Speciality
 * MAPPING at the unit level (confirmed via the real backend:
 * `UnitSubSpecialityController`/`UnitSubSpecialityServiceImpl`/
 * `UnitSubSpecialityDaoImpl` -> table `public.t_unit_sub_speciality_mapper`,
 * NOT any department/sub-department table). Worse, legacy's OWN sidebar
 * already has a genuine naming collision this rebuild would otherwise
 * inherit: state `subSpecialityMaster` (app.js:3285, template
 * `sub-department-Master.html`) is ALSO titled 'Sub Department Master' --
 * that one is the real, global Speciality/Sub-Speciality MASTER DATA CRUD
 * screen (rebuilt already at `new/src/app/features/...` route
 * `/subSpecialityMaster`, nav label "Sub Department Master" in the Admin
 * section of shell-nav.data.ts). Two different screens sharing the exact
 * same on-screen name in the ORIGINAL app is itself worth flagging to the
 * owner as a pre-existing legacy UX defect, independent of this rebuild.
 *
 * DECISION MADE HERE: feature folder named `unit-sub-speciality-mapping`
 * (accurate function, not "subDepartmentMasterUnit") and the nav entry
 * below is labeled "Unit Sub-Speciality Mapping" rather than reusing
 * "Sub Department Master" a second time, specifically to avoid colliding
 * with the already-existing `/subSpecialityMaster` nav entry in this same
 * rebuild's own sidebar. The ROUTE PATH itself is kept as
 * `/subDepartmentMasterUnit` (matching legacy's URL exactly, for drop-in
 * continuity per this project's routing convention -- see app.routes.ts's
 * own header comment). This is a judgement call, not dictated by policy;
 * the orchestrating session/owner may prefer reverting the nav label to
 * "Sub Department Master (Unit)" for user familiarity instead -- flagged
 * clearly in the batch report for that decision.
 *
 * ============================================================================
 * Golden-trace capture (Step 2): `tools/golden-trace/capture.js`, run this
 * session against `{"name":"subDepartmentMasterUnit","url":"/subDepartmentMasterUnit"}`
 * -- real Playwright automation bootstrapping the actual legacy AngularJS
 * app and driving `$state.go('subDepartmentMasterUnit')`, not a
 * hand-derived substitute (`tools/golden-trace/golden-traces/subDepartmentMasterUnit.json`).
 * Confirms exactly 3 `$http` calls fire on load, in this order, matching
 * the controller source's own call order (ctrl:72, ctrl:338 via
 * `promiseFactoryWithObject`, which fires list+count together):
 *   1. POST /adt/getActiveUnitSpecialityList   (ctrl:55-71, getSpecialityMasterList)
 *   2. POST /adt/getUnitSubSpecialityList      (ctrl:272-301, initBillingGroupMasterList)
 *   3. POST /adt/getUnitSubSpecialityCount     (ctrl:272-301, same call)
 * No discrepancy from the Step 0/1 audit's on-load assumptions. ONE
 * re-audit finding, however: the Step 0/1 audit's endpoint list also named
 * `getActiveUnitSubSpecialityList` and `getSubSpecialityBySpecialityId` as
 * screen endpoints -- both are real methods on the SAME
 * `UnitSubSpecialityController` class, but neither is called anywhere in
 * `subDepartmentMasterUnitController.js` (confirmed by a full read of that
 * file, corroborated by the golden trace showing only 3 on-load calls
 * where source predicts only 3). They're almost certainly used by other,
 * unrelated screens that share this backend controller class -- not wired
 * into this component's service for that reason (see
 * unit-sub-speciality-mapping.service.ts's own re-audit note).
 *
 * ============================================================================
 * DISCLOSED BUG FIXES (this project's standing policy: silent/invisible
 * legacy bugs are corrected with disclosure, not faithfully reproduced):
 * ============================================================================
 *
 * 1. Exception-swallowing / information-free errors -- every method on
 *    `UnitSubSpecialityController`/`UnitSubSpecialityServiceImpl`/
 *    `UnitSubSpecialityDaoImpl` wraps its body in `catch(Exception e){}`
 *    (empty, no logging) and falls through to `return new Response(ERROR,
 *    null, null, null, null)` -- status "error", zero message, on ANY
 *    failure (bad SQL, constraint violation, null pointer, whatever).
 *    Legacy's own controller does nothing with a failed response at all
 *    (no `else` branch, no growl.error call -- see ctrl:164-176's
 *    `saveSubSpeciality`, ctrl:193-199's `updateStatus`). This rebuild
 *    cannot surface a message legacy never had; every failure path below
 *    shows the generic `GENERIC_ERROR` string instead of silently doing
 *    nothing (fixed) or inventing a fake specific message (not done).
 *
 * 2. `updateStatusForUnitSubSpeciality` can report success while the
 *    underlying update silently failed -- confirmed by reading
 *    `UnitSubSpecialityServiceImpl.updateStatusForUnitSubSpeciality`
 *    (line ~110): it calls `iUnitSubSpecialityDao.getUnitSubSpecialityById(...)`
 *    to check the row exists, then calls
 *    `iUnitSubSpecialityDao.updateStatusForUnitSpecialityList(...)`
 *    WITHOUT capturing or checking its return value at all -- the method
 *    unconditionally returns `Response(SUCCESS, ...)` as long as the
 *    existence check passed, even if the DAO's own update threw and was
 *    swallowed by its own `catch(Exception e){ return Response(ERROR,...) }`
 *    (whose return value the service never even looks at). Cannot fix the
 *    legacy backend from this Angular-only task (would need its own
 *    dedicated Java fix/branch, out of scope here). MITIGATION CHOSEN:
 *    `toggleStatus()` below re-fetches the current page from the server
 *    (`loadList()`) after every toggle response and compares the returned
 *    row's actual `status` against the status the toggle was supposed to
 *    produce; a mismatch surfaces a distinct "could not be confirmed"
 *    error instead of silently trusting the bare success flag. Chosen over
 *    a single-row re-GET because no `getUnitSubSpecialityById`-style
 *    single-row read endpoint is exposed to the frontend at all (the DAO
 *    method of that name has no controller endpoint) -- re-running the
 *    already-existing paginated list call is the cheapest verification
 *    available without inventing a new backend endpoint. This is safe
 *    because the list is ordered by `unitSubSpecialityId DESC` (a
 *    status-independent, stable sort key), so the toggled row never moves
 *    off its page as a side effect of the toggle itself.
 *
 * 3. No duplicate-mapping guard beyond a client-refreshed picker list, AND
 *    the picker list itself is provably incomplete: the picker's own
 *    backend query (`GET_SUBSPECIALITY_BY_SPECIALITY_ID_NOT_MAPPED_TO_UNIT`,
 *    read directly from `SubSpecialityMaster.java`'s named query) only
 *    excludes sub-specialities already mapped with `status='A'` -- a
 *    mapping that was created then DEACTIVATED (status='I') is NOT
 *    excluded, so re-picking the same sub-speciality under the same
 *    speciality and saving again creates a genuine second DB row for the
 *    same (unit, org, speciality, sub-speciality) tuple (no unique
 *    constraint, confirmed via the entity's `@Table` annotation; the DAO's
 *    `save()` is a plain Hibernate INSERT, no upsert). MITIGATION CHOSEN
 *    (extends the SAME picker-list-exclusion pattern legacy already uses,
 *    rather than inventing a different mechanism): `refreshDedupGuard()`
 *    below fetches the FULL current mapping set for the unit (any status,
 *    via the existing `getUnitSubSpecialityList` call with a page size
 *    large enough to cover every row) and builds a client-side
 *    `Set<subSpecialityId>` scoped to the selected speciality, covering
 *    BOTH 'A' and 'I' rows -- something the server's own query does not
 *    do. `filteredAvailable` (a computed signal) subtracts this set from
 *    the picker's raw "not in unit" response, so an inactive-but-still-
 *    present mapping never even appears as pickable, closing the real gap
 *    in the server's own exclusion logic rather than adding a guard that
 *    only LOOKS like protection. This dedup set is refreshed every time a
 *    speciality is selected AND after every successful save (never
 *    locally spliced), matching the referralMaster/campMaster batch-30
 *    precedent of always refreshing list state from the server after a
 *    write rather than trusting stale client state.
 *
 * 4. Naming/UI-label disclosure -- see the class-level "NAMING DISCLOSURE"
 *    block above.
 *
 * ============================================================================
 * Other audited behavior, reproduced faithfully (not bugs to fix):
 * ============================================================================
 * - `saveSubSpeciality` (ctrl:135-178) shows ZERO success feedback in
 *   legacy -- no `growl.success` call anywhere in that method, only
 *   `growl.warning` for the two validation cases (no speciality selected /
 *   no sub-speciality selected). This rebuild adds a light success toast
 *   on save (see `save()` below) as minor, non-behavioral UX polish
 *   consistent with every other rebuilt screen in this codebase (e.g.
 *   referral-master.ts's `confirmSave()`), NOT counted as one of the 4
 *   disclosed bug fixes above -- flagged here only for transparency.
 * - Validation messages ("Please select speciality...!!" / "Please select
 *   at lease one sub-speciality...!!", ctrl:148-161, INCLUDING the "at
 *   lease" typo) are ported verbatim as growl-equivalent notifications,
 *   not "corrected" -- a copy typo is not the kind of silent/invisible bug
 *   this project's disclosure policy targets.
 * - Pagination: legacy's `setPage(page, flag)` (ctrl:327-337), when
 *   navigating pages, re-fetches the LIST ONLY (`getBillingGroupMasterList`,
 *   ctrl:304-325) and reuses the stale `commonListCount` from the last full
 *   list+count fetch -- it does not re-fetch the count on every page click.
 *   This rebuild's `goToPage()`/`onPageSizeChange()` instead re-fetch list
 *   AND count together on every page change, matching the simpler unified
 *   convention already established by referral-master.ts's `loadList()`
 *   rather than reproducing the split-fetch quirk -- not user-visible
 *   (total record count essentially never changes between page clicks in
 *   the same session) and not one of the 4 disclosed bugs, just a
 *   consistency choice with this codebase's existing precedent.
 * - `unitSubDepartmentMasterUniversalSearch` (ctrl:341-364) is a REAL
 *   server-side search (unlike referralMaster's client-only filter) fired
 *   on every keystroke (`ng-keyup`, no debounce) against `specialityName`,
 *   which the backend query matches against speciality code/name AND
 *   sub-speciality code/name (`GET_PAGINATED_UNIT_SUB_DEPARTMENT_MASTER_DETAILS_LIST_SEARCH`).
 *   The legacy template ALSO applies a second, purely client-side
 *   AngularJS `filter:searchKeyword` (no field restriction, matches any
 *   property) on TOP of whatever the server search already returned --
 *   reproduced faithfully via `displayedRows` below (a further client
 *   filter on top of the server-searched/paginated `rows`), redundant but
 *   harmless, exactly as in legacy. No debounce added -- not one of the 4
 *   disclosed bugs, just inefficiency, and changing request cadence would
 *   itself be an undisclosed behavior change.
 * - Clearing the search box reverts to the normal paginated list at
 *   whatever offset/page-size was already set (ctrl:360-362) -- it does
 *   NOT reset to page 1. Reproduced via `onSearchKeywordChange('')` calling
 *   `loadList()` with the current `currentPage()`/`pageSize()` unchanged.
 * - `updateStatus`'s checkbox (`ng-true-value="'A'" ng-false-value="'I'"`,
 *   view line 180-185) flips `info.status` OPTIMISTICALLY before the
 *   `ng-change` handler even runs, and legacy never reverts it on a failed
 *   POST -- reproduced via an optimistic local flip in `toggleStatus()`,
 *   but (per disclosed fix #2 above) this rebuild DOES revert + surface an
 *   error on an explicit `status !== 'success'`/network-error response,
 *   and additionally verifies via re-fetch on a bare "success" -- a
 *   strictly more defensive stance than legacy's, not a silent
 *   pass-through of a fake positive.
 */
@Component({
  selector: 'app-unit-sub-speciality-mapping',
  imports: [FormsModule, StatusToggle],
  templateUrl: './unit-sub-speciality-mapping.html',
  styleUrl: './unit-sub-speciality-mapping.scss',
})
export class UnitSubSpecialityMapping {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(UnitSubSpecialityMappingService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- top "Department" dropdown (mirrors $scope.unitSpecialityMasterList) ----
  specialityList = signal<UnitSpecialityOption[]>([]);
  selectedSpecialityId = signal<number | ''>('');

  // ---- two-pane picker (mirrors subSpecialityMasterList / tempSelectSpecialty / selectedSpecialityList) ----
  availableSubSpecialities = signal<AvailableSubSpeciality[]>([]);
  private checkedIds = signal<Set<number>>(new Set());
  tempSelected = signal<PickedSubSpeciality[]>([]);
  selectedForSave = signal<PickedSubSpeciality[]>([]);

  // Disclosed fix #3: dedup set covering BOTH 'A' and 'I' mappings for the
  // selected speciality, closing the server query's active-only exclusion
  // gap -- see class doc.
  private dedupGuardIds = signal<Set<number>>(new Set());

  isChecked = (id: number) => this.checkedIds().has(id);

  // What the picker actually renders on the left -- the server's own "not
  // in unit" list, further filtered by the dedup guard above.
  filteredAvailable = computed(() => this.availableSubSpecialities().filter((s) => !this.dedupGuardIds().has(s.subSpecialityMasterId)));

  // ---- mapped list + pagination (mirrors $scope.unitSubSpelMasterList / $scope.pager) ----
  rows = signal<UnitSubSpecialityRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');
  loadingMsg = signal('Loading...');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved verbatim: a second, client-side filter on top of
  // whatever `rows` already holds (server-searched or plain paginated) --
  // see class doc.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const list = this.rows();
    if (!keyword) return list;
    return list.filter(
      (r) =>
        r.specialityCode?.toLowerCase().includes(keyword) ||
        r.specialityName?.toLowerCase().includes(keyword) ||
        r.subSpecialityMasterCode?.toLowerCase().includes(keyword) ||
        r.subSpecialityName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadSpecialityList();
    this.loadList();
  }

  // ---- init (mirrors getSpecialityMasterList, ctrl:55-71) ----
  private loadSpecialityList(): void {
    this.api.getActiveUnitSpecialityList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.specialityList.set(res.listObject ?? []);
      },
    });
  }

  // ---- mapped list + count (mirrors initBillingGroupMasterList, ctrl:272-301) ----
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getUnitSubSpecialityList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
          if ((res.listObject ?? []).length === 0) this.loadingMsg.set('No record found...');
        } else {
          this.notify.error(GENERIC_ERROR);
        }
      },
      error: () => this.notify.error(GENERIC_ERROR),
    });
    this.api.getUnitSubSpecialityCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  // ---- department dropdown change (mirrors getSpecilityId, ctrl:73-91) ----
  onSpecialityChange(specialityId: number | ''): void {
    this.selectedSpecialityId.set(specialityId);
    this.resetPickerSelection();
    this.availableSubSpecialities.set([]);
    this.dedupGuardIds.set(new Set());
    if (specialityId === '' || specialityId == null) return;
    this.refreshPickerForSpeciality(specialityId);
  }

  private refreshPickerForSpeciality(specialityId: number): void {
    this.api.getSubspecialityNotInUnit(this.organizationId, this.unitId, specialityId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.availableSubSpecialities.set(res.listObject ?? []);
      },
    });
    this.refreshDedupGuard(specialityId);
  }

  // Disclosed fix #3: fetch every current mapping for the unit (any
  // status) and scope it to `specialityId` client-side -- see class doc.
  private refreshDedupGuard(specialityId: number): void {
    const pageSize = Math.max(this.totalCount(), 1000);
    this.api.getUnitSubSpecialityList(this.organizationId, this.unitId, 0, pageSize).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const ids = new Set<number>(
          (res.listObject ?? []).filter((r) => r.specialityId === specialityId).map((r) => r.subSpecialityId),
        );
        this.dedupGuardIds.set(ids);
      },
    });
  }

  // ---- picker selection (mirrors addTempSpecialty, ctrl:119-132) ----
  toggleCheck(item: AvailableSubSpeciality): void {
    const checked = new Set(this.checkedIds());
    const already = this.tempSelected().find((s) => s.subSpecialityMasterId === item.subSpecialityMasterId);
    if (already) {
      this.tempSelected.update((list) => list.filter((s) => s.subSpecialityMasterId !== item.subSpecialityMasterId));
      checked.delete(item.subSpecialityMasterId);
    } else {
      this.tempSelected.update((list) => [
        ...list,
        { subSpecialityMasterId: item.subSpecialityMasterId, subSpecialityName: item.subSpecialityMasterName },
      ]);
      checked.add(item.subSpecialityMasterId);
    }
    this.checkedIds.set(checked);
  }

  // ---- move to right pane (mirrors selectSpeciality, ctrl:113-116) ----
  moveToSelected(): void {
    this.selectedForSave.set([...this.tempSelected()]);
  }

  private resetPickerSelection(): void {
    this.tempSelected.set([]);
    this.selectedForSave.set([]);
    this.checkedIds.set(new Set());
  }

  // ---- save (mirrors saveSubSpeciality, ctrl:135-178) ----
  save(): void {
    const specialityId = this.selectedSpecialityId();
    if (specialityId === '' || specialityId == null) {
      this.notify.warning('Please select speciality...!!');
      return;
    }
    if (this.selectedForSave().length === 0) {
      this.notify.warning('Please select at lease one sub-speciality...!!');
      return;
    }

    const now = formatDateTime(new Date());
    const payload = {
      specialityId,
      unitId: this.unitId,
      organizationId: this.organizationId,
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
      unitSubSpecialityMapperDtosList: this.selectedForSave().map((s) => ({
        subSpecialityMasterId: s.subSpecialityMasterId,
        subSpecialityName: s.subSpecialityName,
      })),
    };

    this.api.saveUnitSubSpecialtiy(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          // Minor added UX polish beyond legacy's silent success -- see class doc.
          this.notify.success('Sub-Speciality mapping saved successfully');
          this.resetPickerSelection();
          // Disclosed fix #3: refresh BOTH the picker and the dedup guard
          // from the server, never locally splice -- see class doc.
          this.refreshPickerForSpeciality(specialityId);
          // Disclosed fix #3 (continued): refresh the mapped table from
          // the server rather than assuming local state is still correct.
          this.loadList();
        } else {
          // Disclosed fix #1: legacy's own error response carries no
          // message at all -- show a generic, honest failure state.
          this.notify.error(GENERIC_ERROR);
        }
      },
      error: () => this.notify.error(GENERIC_ERROR),
    });
  }

  // ---- search (mirrors searchSubDepartmentMasterUnit, ctrl:341-364) ----
  onSearchKeywordChange(value: string): void {
    this.searchKeyword.set(value);
    if (value.trim() !== '') {
      this.loadingMsg.set('Loading...');
      this.api.unitSubDepartmentMasterUniversalSearch(this.organizationId, this.unitId, value).subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
            if ((res.listObject ?? []).length === 0) this.loadingMsg.set('No record found...');
          } else {
            this.notify.error(GENERIC_ERROR);
          }
        },
        error: () => this.notify.error(GENERIC_ERROR),
      });
    } else {
      this.loadList();
    }
  }

  // ---- status toggle (mirrors updateStatus, ctrl:181-199 -- see disclosed
  // fix #2 in the class doc for the defensive re-fetch mitigation) ----
  toggleStatus(row: UnitSubSpecialityRow, nextStatus: 'A' | 'I'): void {
    const previousStatus = row.status;
    // Optimistic flip, matching legacy's checkbox ng-true-value/ng-false-value
    // binding firing before the handler runs -- see class doc.
    this.rows.update((list) => list.map((r) => (r.unitSubSpecialityId === row.unitSubSpecialityId ? { ...r, status: nextStatus } : r)));

    this.api
      .updateStatusForUnitSubSpeciality({
        unitSubSpecialityId: row.unitSubSpecialityId,
        status: nextStatus,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.verifyStatusAfterToggle(row.unitSubSpecialityId, nextStatus);
          } else {
            this.revertStatus(row.unitSubSpecialityId, previousStatus);
            this.notify.error(GENERIC_ERROR);
          }
        },
        error: () => {
          this.revertStatus(row.unitSubSpecialityId, previousStatus);
          this.notify.error(GENERIC_ERROR);
        },
      });
  }

  // Disclosed fix #2: legacy's service can report "success" even when the
  // DAO's own update silently failed (its return value is never checked).
  // Re-fetch the current page from the server and confirm the row's actual
  // status matches what the toggle was supposed to produce, rather than
  // trusting the bare flag -- see class doc.
  private verifyStatusAfterToggle(unitSubSpecialityId: number, expectedStatus: 'A' | 'I'): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getUnitSubSpecialityList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status !== 'success') return; // can't verify; leave the optimistic value in place rather than guessing
        this.rows.set(res.listObject ?? []);
        const confirmed = (res.listObject ?? []).find((r) => r.unitSubSpecialityId === unitSubSpecialityId);
        if (confirmed && confirmed.status !== expectedStatus) {
          this.notify.error('Status change could not be confirmed -- please refresh and try again');
        } else if (confirmed) {
          this.notify.success('Status updated');
        }
      },
      error: () => {
        // Re-fetch itself failed -- leave the optimistic value as-is (already
        // showing `expectedStatus`), can't distinguish a real failure from a
        // transient network blip here, and reverting on an unrelated GET
        // failure would be its own false signal.
      },
    });
  }

  private revertStatus(unitSubSpecialityId: number, previousStatus: 'A' | 'I'): void {
    this.rows.update((list) =>
      list.map((r) => (r.unitSubSpecialityId === unitSubSpecialityId ? { ...r, status: previousStatus } : r)),
    );
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:139-142/183/191. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
