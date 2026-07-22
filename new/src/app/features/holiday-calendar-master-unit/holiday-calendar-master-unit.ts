import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ApiResponse } from '../../core/api-response';
import { HolidayCalendarMasterUnitService } from './holiday-calendar-master-unit.service';
import { GlobalCalendarEntry, SelectedCalendarItem, UnitHolidayCalendarRow } from './holiday-calendar-master-unit.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/holiday-calendar-master-unit.html
 * + resources/assets/js/controller/adt/unit/holidayCalendarMasterUnitController.js
 * (`holidayCalendarMasterUnitController`, plain `$scope`, no `controllerAs`
 * declared at all in the state config -- consistent, zero `vm.*` bindings
 * anywhere). Wired via app.js's central `$stateProvider`
 * (app.js:8497-8523), moduleId 536, no requireSuperUser. `$state` IS in
 * this controller's own DI list and the missing-cookie guard runs before
 * anything else -- no instance of the recurring missing-`$state`-DI crash
 * bug here. Confirmed the only registration of state name
 * "holidayCalendarMasterUnit" anywhere in the codebase.
 *
 * Backend confirmed fully real for all 5 endpoints this controller calls
 * (Controller -> Service -> DAO, table
 * `public.t_unit_holiday_calender_mapper`) -- see this screen's
 * `.service.ts` doc comment, including the resolved
 * `UnitHolidayCalenderMapper` entity-duplicate check. Golden-trace capture
 * (`tools/golden-trace/golden-traces/holidayCalendarMasterUnit.json`)
 * independently confirms the real initial-load signature: exactly
 * `POST getHolidayCalenderListNotMappedToUnit` + `POST getUnitHolidayCalenerList`
 * + `POST getUnitHolidayCalenerCount`, all fired once, matching source
 * exactly.
 *
 * **Sibling comparison against `weekendMaster`** (same "Admin -
 * Org/Unit Master" module, same nav section) **-- a genuinely different
 * workflow category, not a bug:** `weekendMaster` is a create/edit CRUD
 * master; this screen has NO create/edit form of its own at all -- it's a
 * dual-list mapping/assignment screen (pick from a GLOBAL calendar list,
 * move to a per-unit selection, Save persists the whole selection), closer
 * in shape to this codebase's `departmentStoreMapper`/`userCounterMapper`
 * family than to `weekendMaster`. Does NOT use the shared `<modal>`
 * directive (confirmed: no `<modal ...>` custom element anywhere in this
 * view -- the one Bootstrap `.modal` div it does have, `#detailsModal`, is
 * a plain raw Bootstrap modal, not the shared directive), so entry 55's
 * standing policy does not apply here. `<dietCssdPopUp>`/`<shortcut>` also
 * confirmed unused.
 *
 * **Faithfully reproduced quirks/bugs, not fixed:**
 * - **The "Update" button/`update()` path is permanently dead and
 *   permanently hidden together.** `updateBtnFlag` (view:82,
 *   `ng-if="updateBtnFlag"`) is declared nowhere in the controller except
 *   implicitly `undefined` -- there is no `showUpdateBtn`-style function
 *   anywhere that ever sets it true, and no `update()` function is defined
 *   at all (confirmed via full-file grep). Since the button can never
 *   render, this is inert in practice, not a live crash risk -- not
 *   reproduced (nothing observable to reproduce; only the always-visible
 *   Save button/`save()` path is built below).
 * - **The Save/Update growl notifications are NOT emitted by this
 *   controller at all** (confirmed: zero `growl.` references anywhere in
 *   `holidayCalendarMasterUnitController.js`) -- both `save()` and
 *   `updateStatus()` call `BillingGenericService.serviceAction(...,
 *   NOTIFICATION_MSG_STATUS_TRUE)`, and 100% of this screen's toast
 *   behavior comes from that shared service's own built-in
 *   auto-notification logic (success message shown only if non-null; an
 *   error with `code === "409"` shown as a WARNING, not an error) --
 *   reproduced exactly via `notifyLikeBillingGenericService()` below,
 *   rather than inventing this screen's own message text.
 * - **Status toggle never reverts on a failed response** -- the checkbox
 *   flips optimistically (mirrors the view's `ng-model="info.status"`
 *   two-way binding, which updates the DOM immediately regardless of the
 *   server's answer) and `updateStatus()`'s success branch reloads the
 *   list+count fresh from the server (which will correct an optimistic
 *   flip that the server actually rejected as a `'success'`-shaped
 *   response would never happen, but there is no error-branch handling at
 *   all -- a failed/`'error'` response leaves the already-flipped checkbox
 *   exactly as flipped, with only the auto-growl as feedback). Reproduced
 *   via `toggleStatus()` below: no revert, no reload, on the error path.
 * - **Page navigation only re-fetches the list, never the count** --
 *   `setPage(page, true)` (ctrl:200-210) calls `getList(...)` alone, not
 *   `initMasterList(...)`; only the initial load, "Show N entries" change,
 *   and post-save/post-toggle reloads re-fetch count. Reproduced: pager
 *   clicks call `loadListOnly()`, everything else calls
 *   `loadListAndCount()`.
 * - **Sort carets (Holiday Code/Holiday/Date/Day/Status(n) column
 *   headers) are pure static decoration** -- confirmed in the legacy view
 *   itself, not just dead wiring: the `<i class="fa fa-caret-up/down">`
 *   icons have no `ng-click` of any kind (unlike, e.g., this codebase's
 *   `create-tabular-templates`, where at least SOME sort wiring exists but
 *   does nothing -- here there was never any wiring attempted at all).
 *   `column`/`reverse` (used in the dead `orderBy:column:reverse` pipe
 *   expression) are declared nowhere in the controller. No click handler
 *   built for these icons, matching legacy's own zero-effort baseline.
 * - **The left-hand "Global Master" checkbox list's `ng-checked` binding
 *   is dead/always-false** (view:19,
 *   `ng-checked="selectedListItem.indexOf(info.holidayCalenderId) != -1"`)
 *   -- `selectedListItem` holds `{holidayId, holidayCalenderDesc}` OBJECTS,
 *   never raw numeric ids, so `.indexOf(numericId)` can never find a match
 *   and always evaluates `-1`/false. Harmless: the real checked state is
 *   driven by the co-existing `ng-model="specialityMst[info.holidayCalenderId]"`
 *   binding, which works correctly on its own. Not reproduced as literal
 *   dead logic (there is nothing observable an always-false expression
 *   would add); `isChecked()` below reproduces only the real, working
 *   `ng-model`-driven checked state.
 * - **The "Status(n)" column header text is copied verbatim** -- an
 *   apparent unfinished-label artifact in legacy's own markup (view:144),
 *   not a typo this rebuild corrects.
 *
 * **Disclosed omission, not a silent fix:** the "Add Holiday" modal
 * (`#detailsModal`, view:208-291) is a fully orphaned fragment in legacy --
 * its only possible trigger (`data-target="#detailsModal"`, view:64-66) is
 * inside an entirely commented-out block (view:55-71), and its own "Save"
 * button has no `ng-click` at all (just `data-dismiss="modal"`). There is
 * NO live path to ever open this modal in legacy today, and nothing inside
 * it does anything even if force-opened via devtools. Omitted here rather
 * than ported as inert unreachable markup -- zero observable behavior
 * differs for any real user, the same reasoning already applied to this
 * codebase's `weekend-master` (`initTime()`'s jQuery clockpicker, disclosed
 * simplification, no live data path affected). Flagged as its own finding,
 * not bundled silently into "matches legacy".
 */
@Component({
  selector: 'app-holiday-calendar-master-unit',
  imports: [FormsModule, StatusToggle],
  templateUrl: './holiday-calendar-master-unit.html',
  styleUrl: './holiday-calendar-master-unit.scss',
})
export class HolidayCalendarMasterUnit {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(HolidayCalendarMasterUnitService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- left picker: global calendar entries not yet mapped to this unit
  // (mirrors $scope.calenderList/specialityMst/tempSelectItem) ----
  globalList = signal<GlobalCalendarEntry[]>([]);
  private checkedIds = signal<Set<number>>(new Set());
  private tempSelectItem = signal<SelectedCalendarItem[]>([]);

  // ---- right preview: queued-but-not-yet-saved selection (mirrors
  // $scope.selectedListItem, populated only by the "<<>>" button) ----
  selectedListItem = signal<SelectedCalendarItem[]>([]);

  // ---- main table: rows already mapped/saved for this unit + server-side
  // pagination (mirrors unitCalenderList/PagerService) ----
  list = signal<UnitHolidayCalendarRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Mirrors the legacy `filter:searchKeyword` pipe -- a loose match across
  // every field on the row, not a single named field.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => String(v ?? '').toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadGlobalList();
    this.loadListAndCount();
  }

  private loadGlobalList(): void {
    this.api.getHolidayCalendarListNotMappedToUnit(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.globalList.set(res.listObject ?? []);
      },
    });
  }

  // ctrl:154-177 (`initMasterList`) -- list + count together.
  private loadListAndCount(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api
      .getUnitHolidayCalendarList({
        organizationId: this.organizationId,
        unitId: this.unitId,
        offset,
        noOfRecordsPerPage: this.pageSize(),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.list.set(res.listObject ?? []);
        },
      });
    this.api
      .getUnitHolidayCalendarCount({ organizationId: this.organizationId, unitId: this.unitId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        },
      });
  }

  // ctrl:180-198 (`getList`) -- list only, used by pager navigation.
  private loadListOnly(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api
      .getUnitHolidayCalendarList({
        organizationId: this.organizationId,
        unitId: this.unitId,
        offset,
        noOfRecordsPerPage: this.pageSize(),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.list.set(res.listObject ?? []);
        },
      });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  // Mirrors setPage(page, true) -- list only, no re-count (see class doc comment).
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadListOnly();
  }

  isChecked(holidayCalenderId: number): boolean {
    return this.checkedIds().has(holidayCalenderId);
  }

  // Mirrors addTempItem(holidayCalenderId, holidayCalenderDesc), ctrl:90-103.
  toggleCheck(entry: GlobalCalendarEntry): void {
    const already = this.tempSelectItem().some((t) => t.holidayId === entry.holidayCalenderId);
    if (already) {
      this.tempSelectItem.update((items) => items.filter((t) => t.holidayId !== entry.holidayCalenderId));
      this.checkedIds.update((s) => {
        const next = new Set(s);
        next.delete(entry.holidayCalenderId);
        return next;
      });
    } else {
      this.tempSelectItem.update((items) => [
        ...items,
        { holidayId: entry.holidayCalenderId, holidayCalenderDesc: entry.holidayCalenderDesc },
      ]);
      this.checkedIds.update((s) => new Set(s).add(entry.holidayCalenderId));
    }
  }

  // Mirrors selectItem() -- the "<<>>" button; a straight overwrite of the
  // preview list with whatever is currently checked, not an append.
  moveSelected(): void {
    this.selectedListItem.set([...this.tempSelectItem()]);
  }

  save(): void {
    const now = formatDateTime(new Date());
    this.api
      .saveUnitHolidayCalendar({
        status: 'A',
        createdDate: now,
        createdBy: this.userId,
        updatedDate: now,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        unitHolidayCalenderMapperDtosList: this.selectedListItem(),
      })
      .subscribe({
        next: (res) => {
          this.notifyLikeBillingGenericService(res);
          if (res.status === 'success') {
            // ctrl:116-121 -- reload the not-yet-mapped list, clear the
            // picker/preview state, uncheck everything, reload the mapped
            // table (list + count together).
            this.loadGlobalList();
            this.selectedListItem.set([]);
            this.tempSelectItem.set([]);
            this.checkedIds.set(new Set());
            this.loadListAndCount();
          }
        },
      });
  }

  // Mirrors updateStatus(id, status), ctrl:128-146.
  toggleStatus(row: UnitHolidayCalendarRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip up front -- mirrors the view's
    // ng-model="info.status" two-way binding, which flips the checkbox in
    // the DOM immediately regardless of the server response.
    this.list.update((rows) =>
      rows.map((r) => (r.unitHolidayCalenderId === row.unitHolidayCalenderId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatusForUnitHolidayCalendar({
        unitHolidayCalenderId: row.unitHolidayCalenderId,
        status: nextStatus,
        updatedDate: formatDateTime(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.notifyLikeBillingGenericService(res);
          // ctrl:140-143 -- reload list + count ONLY on success; no revert,
          // no reload, on failure -- see class doc comment.
          if (res.status === 'success') this.loadListAndCount();
        },
      });
  }

  /** Mirrors BillingGenericService.serviceAction's own auto-growl logic
   * (legacy/HIS-WAR/.../service/GenericService.js:519-550) -- this
   * controller never calls growl itself; see class doc comment. */
  private notifyLikeBillingGenericService(res: ApiResponse & { code?: string }): void {
    if (res.status === 'success') {
      if (res.message) this.notify.success(res.message);
    } else {
      if (res.code === '409') this.notify.warning(res.message ?? '');
      else this.notify.error(res.message ?? '');
    }
  }
}

/** `moment(date).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:107-109/131-135. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
