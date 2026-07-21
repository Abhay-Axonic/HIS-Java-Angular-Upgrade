import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { WeekendMasterService } from './weekend-master.service';
import { WeekendMasterForm, WeekendMasterRow, emptyWeekendMasterForm } from './weekend-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/weekend-master.html +
 * resources/assets/js/controller/master/unit/weekendMasterController.js
 * (`weekendMasterController`, no `controllerAs` -- 0 `vm.` bindings
 * anywhere, confirmed via grep -- classic `$scope` pattern). Wired via
 * app.js's central `$stateProvider` (line ~14613), moduleId 697, no
 * requireSuperUser. `$state` IS in this controller's own DI list and the
 * missing-cookie guard returns before anything else runs -- no instance of
 * the recurring missing-`$state`-DI crash bug here.
 *
 * Confirmed real, working CRUD against `/api/global/unit/*`, all 6
 * endpoints confirmed end-to-end (Controller -> Service -> DAO) in
 * `legacy/Global/src/main/java/com/param/global/{controller,service,dao}/
 * WeekendMaster*`, plus the separate `/adt/getActiveDayListById` day-name
 * lookup (`HolidayCalenderMasterController`, a different screen's
 * controller, reused here exactly as legacy does). Golden-trace capture
 * (`tools/golden-trace/golden-traces/weekendMaster.json`) independently
 * confirms the real initial-load signature: exactly
 * `POST getListOfWeekendMaster` + `POST getWeekendMasterCount`, both fired
 * once, matching source exactly (no duplicate-call bug here, unlike this
 * batch's sibling `rack-masters`).
 *
 * **Uses the shared `<modal>` directive** (`weekend-master.html:183`,
 * `<modal visible="showModal"></modal>`) -- per
 * `docs/ANGULAR_PENDING_FINDINGS.md` entry 55's already-resolved standing
 * policy, the directive's hardcoded close-button `ng-click="cancel();
 * stopLoader();"` crashes in legacy today (`weekendMasterController.js`
 * never defines `$scope.cancel`, confirmed via full-file grep) -- this is
 * an accepted, disclosed standing improvement to fix in the shared
 * component, not a parity gap to reproduce. `closeConfirmModal()` below is
 * an ordinary, working close.
 *
 * **Save vs. Update is asymmetric, same family as this codebase's
 * `floor-master`/`qualification-master`:** clicking Save (`saveBtnFlag`,
 * ctrl:197-262) validates the one HTML5-`required` field (the date picker,
 * view:25), then opens the shared confirm modal (only the modal's Ok ->
 * `PopupOkBtn()`, ctrl:192-195, re-runs `save()` with `popUpFlag` now
 * false, which is when the real POST fires). Clicking Update
 * (`updateBtnFlag`, ctrl:299-350) validates the same field and POSTs
 * immediately, with no confirm step at all.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - **`save()`/`update()` only reset the form AND reload the list on a
 *   `'success'` response** (ctrl:232-246/327-336) -- an `'error'` response
 *   shows a growl and does nothing else (no reload, no reset). This is a
 *   genuine divergence from this codebase's `floor-master`/`row-masters`,
 *   which both reload the list unconditionally regardless of
 *   success/failure -- read fresh from source here, not assumed to match
 *   those siblings. Reproduced verbatim: `confirmSave()`/`updateRecord()`
 *   only call `resetForm()`/`loadList()` inside the success branch.
 * - **`updateStatus` (ctrl:352-388) never reloads the list or resets the
 *   form on either branch**, and only growls on success -- no error
 *   feedback, no revert of the already-toggled checkbox on failure (the
 *   commented-out `// $scope.$broadcast('show-errors-reset')`, ctrl:375,
 *   confirms this was even more stripped-down than the neighboring
 *   save/update handlers, not just an oversight). Reproduced via
 *   `toggleStatus()`: the list signal is patched locally up front (mirrors
 *   the view's `ng-model="info.status"` two-way binding, which flips the
 *   checkbox in the DOM immediately regardless of what the server returns),
 *   a growl fires only on success, and nothing is ever reverted or reloaded.
 * - **Editing an existing row does NOT refresh the visible "Select Day"
 *   readonly display** -- `showUpdateBtn` (ctrl:264-296) sets
 *   `$scope.common` (which includes the correct `day`/`dayId` fields off
 *   the fetched record) but never assigns `$scope.dayName`, the SEPARATE
 *   scope variable the view's readonly "Select Day" box actually renders
 *   (view:43, `ng-model="dayName"`). `dayName` is only ever set by `test()`
 *   (ctrl:77-100), fired on a live datepicker change -- so editing a row
 *   whose date the user doesn't touch shows whatever `dayName` happened to
 *   hold before (blank on first use, or a stale value from a previous
 *   create/edit), not the edited row's real day. Reproduced verbatim: `edit()`
 *   populates `form()` (including the correct `dayId`) but leaves the
 *   separate `dayName` signal untouched; only `onDateChange()` (mirrors
 *   `test()`) and `resetForm()` (mirrors `initMaster()`, which sets
 *   `dayName = ""`) ever write to it.
 * - **The table's "Year" and "Date" columns are bound to the exact same
 *   field** (`weekendDate`, view:144/146 -- both `{{info.weekendDate}}`,
 *   confirmed via source) -- there is no separate year-only value anywhere
 *   in the response. Reproduced verbatim in the table markup below (both
 *   columns render the same value); not "fixed" into a real year/date
 *   split, since legacy has no year-only field to split it from.
 * - The list's search box (`ng-model="searchKeyword"`, view:107) feeds a
 *   real working `commonList | filter:searchKeyword` pipe on the table
 *   (view:143) -- *and*, on the very same input, an
 *   `ng-keyup="serachTestmaster(searchKeyword);"` that calls a function
 *   never defined anywhere in weekendMasterController.js (confirmed via
 *   full-file grep) -- the identical dead-function-name pattern this
 *   codebase's `floor-master` already reproduces (a different screen, same
 *   copy-paste-artifact root cause). Both halves reproduced here: a real
 *   working `displayedRows` filter, and `onSearchKeyup()` throwing.
 * - The clockpicker time fields' `ng-init="initTime();"` (view:67/86) also
 *   calls a function never defined anywhere in the controller -- a THIRD
 *   dead reference on this one screen, on top of the two above. Unlike the
 *   search box, this has no live interactive counterpart worth reproducing
 *   here: legacy's `initTime()` was presumably meant to wire up the jQuery
 *   `clockpicker` widget this rebuild deliberately doesn't port (native
 *   `<input type="time">` is used instead, matching this codebase's other
 *   rebuilt time fields) -- a disclosed simplification, not a behavior
 *   change to any real data path, so no crash-reproduction hook is built
 *   for it.
 */
@Component({
  selector: 'app-weekend-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './weekend-master.html',
  styleUrl: './weekend-master.scss',
})
export class WeekendMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(WeekendMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  form = signal<WeekendMasterForm>(emptyWeekendMasterForm());
  // Separate from `form` on purpose -- mirrors legacy's genuinely separate
  // `$scope.dayName` variable and its edit-mode staleness bug; see class comment.
  dayName = signal('');
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initMasterList/PagerService) ----
  list = signal<WeekendMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: a real working filter, live alongside the
  // crashing ng-keyup handler on the same input -- see class comment.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.day?.toLowerCase().includes(keyword) || r.weekendDate?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getListOfWeekendMaster(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getWeekendMasterCount(this.organizationId, this.unitId).subscribe({
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

  updateFormField<K extends keyof WeekendMasterForm>(field: K, value: WeekendMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- date picker change (mirrors test(), ctrl:77-100) ----
  onDateChange(dateStr: string): void {
    const date = dateStr ? new Date(`${dateStr}T00:00:00`) : null;
    this.updateFormField('weekendDate', date);
    if (!date) return;
    let day = date.getDay();
    if (day === 0) day = 7;
    this.updateFormField('dayId', day);
    this.api.getActiveDayListById({ dayId: day }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.dayName.set(res.listObject?.[0]?.day ?? '');
        }
      },
    });
  }

  isRequiredMissing(): boolean {
    return this.formSubmitted() && !this.form().weekendDate;
  }

  /** `yyyy-MM-dd` string for the native `<input type="date">`'s value binding. */
  toDateInputValue(d: Date): string {
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  // ---- save flow: Save validates then opens the confirm modal; only the
  // modal's Ok actually POSTs (mirrors save()'s popUpFlag-gated double call
  // via PopupOkBtn, ctrl:197-262). ----
  onSaveClick(): void {
    this.formSubmitted.set(true);
    if (!this.form().weekendDate) return;
    this.showConfirmModal.set(true);
  }

  /** Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy): the
   * shared `<modal>` directive's x close button crashes in legacy
   * (`cancel()` is never defined on this controller's $scope) -- an
   * ordinary working close here is an accepted standing improvement, not a
   * parity violation. */
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveWeekendMaster({
        updatedBy: this.userId,
        updatedDate: now,
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdDate: now,
        createdBy: this.userId,
        weekendDate: formatDate(f.weekendDate as Date),
        isHalfDay: f.isHalfDay,
        dayId: f.dayId,
        fromTime: f.fromTime,
        toTime: f.toTime,
      })
      .subscribe({
        next: (res) => {
          // ctrl:232-246 -- reset + reload ONLY on success; an 'error'
          // response only growls, nothing else -- see class comment.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update (mirrors update(), ctrl:299-350) -- no confirm modal ----
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.form().weekendDate) return;
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .updateWeekendMaster({
        weekendId: f.weekendId as number,
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
        weekendDate: formatDate(f.weekendDate as Date),
        unitId: this.unitId,
        isHalfDay: f.isHalfDay,
        dayId: f.dayId,
        fromTime: f.fromTime,
        toTime: f.toTime,
      })
      .subscribe({
        next: (res) => {
          // ctrl:327-336 -- reset + reload ONLY on success, same asymmetry as save().
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyWeekendMasterForm());
    this.dayName.set('');
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:264-296) -- deliberately does NOT
  // touch `dayName`, see class comment's day-display bug note. ----
  edit(row: WeekendMasterRow): void {
    this.saveMode.set(false);
    this.api.getWeekendMasterById(row.weekendId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            weekendId: detail.weekendId,
            weekendDate: new Date(detail.weekendDate),
            dayId: detail.dayId,
            isHalfDay: detail.isHalfDay,
            fromTime: detail.fromTimeString ?? '',
            toTime: detail.toTimeString ?? '',
          });
          // dayName is deliberately NOT set here -- see class comment.
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:352-388) -- never
  // reloads the list or resets the form on either branch. ----
  toggleStatus(row: WeekendMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Patch the list signal locally up front -- mirrors the view's
    // ng-model="info.status" two-way binding, which flips the checkbox in
    // the DOM immediately regardless of the server response.
    this.list.update((rows) => rows.map((r) => (r.weekendId === row.weekendId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateWeekendMasterStatus({
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        status: nextStatus,
        weekendId: row.weekendId,
      })
      .subscribe({
        next: (res) => {
          // ctrl:368-383 -- growl only on success; no error branch, no list
          // reload/reset either way (the commented-out
          // show-errors-reset broadcast, ctrl:375, confirms this).
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          }
        },
      });
  }

  /** Faithful crash replication -- see class doc comment. Legacy's
   * weekend-master.html:107 wires `ng-keyup="serachTestmaster(searchKeyword);"`
   * to a function never defined anywhere in weekendMasterController.js. */
  onSearchKeyup(): void {
    throw new TypeError('serachTestmaster is not a function');
  }
}

/** `moment(date).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:203/214/307/311. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}

/** Same format as formatDateTime, applied to the date-picker's own value
 * (`moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss')`, ctrl:214/311
 * -- a date-only value formatted with a time component, always midnight
 * here since the native date input carries no time part). */
function formatDate(d: Date): string {
  return formatDateTime(d);
}
