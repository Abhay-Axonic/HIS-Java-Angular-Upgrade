import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { HolidayCalenderMasterService } from './holiday-calender-master.service';
import { HolidayRecord, HolidayRow, emptyHoliday } from './holiday-calender-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/holiday-calendar-master.html
 * (note: templateUrl uses the CORRECT "calendar" spelling despite the
 * state name/controller/URL all using legacy's "Calender" typo -- app.js
 * confirmed directly) + resources/assets/js/controller/adt/global/
 * holidayCalenderMasterController.js. moduleId 375 (app.js:3258-3284
 * resolve block). Single, non-duplicated state registration confirmed
 * (grep across the whole legacy tree found no second
 * `.state('holidayCalenderMaster', ...)`) -- not an addItemPOCreation-
 * shaped dead-frontend case.
 *
 * **Confirmed genuinely distinct from the already-built sibling
 * `holidayCalendarMasterUnit` (batch 10, entry 111), not a duplicate:**
 * separately-registered state (`holidayCalenderMaster` at app.js:3258 vs.
 * `holidayCalendarMasterUnit` at app.js:8497), separate moduleId (375 vs.
 * 536), separate controller/view/backend-entity chain end-to-end. THIS
 * screen (`holidayCalenderMaster`) is the GLOBAL, organization-level
 * create/edit/list CRUD master for holiday records themselves (own
 * dedicated Controller/Service/DAO -> `HolidayCalenderMasterController`,
 * writes `com.param.global.model.HolidayCalenderMaster` /
 * `adt.m_holiday_calender_master`). `holidayCalendarMasterUnit` is an
 * entirely different screen: a dual-list PICK/ASSIGN mapping UI that
 * selects from the global calendar and maps chosen holidays onto one
 * specific unit (own controller/entity ->
 * `com.param.global.model.UnitHolidayCalenderMapper`). No shared
 * endpoints, no shared entity, no scope overlap -- confirmed via direct
 * source read of both, not assumed from the similar titles.
 *
 * Backend-existence confirmed end-to-end via source read (no live token
 * available, matching entry 67's methodology): all 6 endpoints this
 * screen calls (saveHolidayCalenderMaster/getHolidayCalenderMasterList/
 * updateHolidayCalenderMaster/getHolidayCalenderById/
 * updateStatusForHolidayCalender/getActiveDayListById) implemented by
 * HolidayCalenderMasterController.java -> HolidayCalenderMasterServiceImpl.java
 * -> HolidayCalenderMasterDaoImpl.java.
 *
 * **Dev-DB note, not a code defect:** `adt.m_holiday_calender_master` has
 * 4 existing rows in this environment, but all 4 have a NULL
 * `organization_id` (and blank code/desc/date/day_id -- created 2026-07-21,
 * apparently orphaned test artifacts predating this audit). Since both
 * `GET_HOLIDAY_LIST` and `getHolidayCount` filter on the logged-in
 * session's real `organizationId` (=1, the only seeded org, "DEMO"), none
 * of these 4 rows will appear when this screen is actually loaded/tested
 * live -- the list will render empty on first load despite the table not
 * being empty. Flagged for whoever runs Step 5 live testing; not something
 * this rebuild's code can or should paper over.
 *
 * Uses the shared `<modal>` directive (view:112) whose legacy controller
 * never defines `cancel` -- entry 55's standing policy applies (this
 * rebuild's working close button is a disclosed, pre-approved shared-
 * component improvement, not re-litigated here).
 *
 * **Deliberate bugs/quirks preserved, not fixed by this rebuild:**
 * - **Status toggle never reverts and never reloads, in either branch**
 *   (ctrl:391-428) -- same bug class already documented in area-master's
 *   own class comment (a real, screen-specific controller-logic gap, not a
 *   shared-component issue -- entry 55's policy does not cover this).
 *   Because legacy's `ng-model="info.status"` mutates the row in place the
 *   instant the checkbox is clicked, a FAILED status update leaves the
 *   checkbox showing the new (wrong, never-persisted) state with no
 *   correction. Reproduced faithfully: `toggleStatus()` flips the row
 *   locally and permanently regardless of the server's response.
 * - **Page navigation only re-fetches the row list, never the count** --
 *   matching legacy's own `getHolidayCalenderMasterList` vs.
 *   `initHolidayCalenderMasterList` split (ctrl:186-213 vs 136-184), same
 *   pattern already disclosed for sibling `holidayCalendarMasterUnit`
 *   (entry 111) and for this batch's own `area-master`.
 * - **`test()` (ctrl:50-72, the datepicker's `ng-change` handler) is the
 *   ONLY thing that sets `holidayDayId`/fetches the day-name** -- there is
 *   no independent `<select>` for day-of-week (a commented-out dead one
 *   exists at view:59-73, never rendered). `holidayDayId` is entirely
 *   derived from the selected date client-side (`date.getDay()`, with
 *   Sunday=0 remapped to 7 to match `m_day_master`'s 1=Monday..7=Sunday
 *   convention -- confirmed against live DB data). Reproduced via
 *   `onDateChange()` below, which fires on every date change exactly like
 *   `ng-change="test()"` does, on both create AND edit.
 * - `saveHoliday`'s fallback (ctrl:261-264: if `holidayDayId` is
 *   undefined when Save is clicked, recompute it from the date one more
 *   time) is reproduced verbatim in `confirmSave()` below, even though in
 *   practice `onDateChange` should always have already set it once a
 *   required date is picked.
 * - The Save button's confirm-modal step (`saveHoliday`'s `popUpFlag`
 *   dance, ctrl:246-317) only exists for CREATE. `updateHoliday`
 *   (ctrl:431-496) saves immediately with no confirm step -- same
 *   asymmetry already documented for sibling `area-master`/`city-master`.
 * - Search filters only the CURRENT PAGE's already-fetched rows
 *   (`holidayList|filter:searchKeyword`, view:197), not the full dataset.
 * - Server-side duplicate-DESCRIPTION check confirmed
 *   (`getHolidayCalenderByName`/`getHolidayByNameNotId`, matches on
 *   `holidayCalenderDesc` only, NOT code or date) -- no independent
 *   frontend uniqueness check exists.
 * - The Edit (pencil) icon is visually disabled via `ng-class="{disabled:
 *   info.status=='I'}"` (view:208) but has no matching `ng-click` guard --
 *   this is CSS-only; clicking a pencil on an Inactive row still fires
 *   `showUpdateBtn`. Reproduced as a CSS class only, not an actual click
 *   guard, matching legacy's real (cosmetic-only) behavior.
 */
@Component({
  selector: 'app-holiday-calender-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './holiday-calender-master.html',
  styleUrl: './holiday-calender-master.scss',
})
export class HolidayCalenderMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(HolidayCalenderMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.holiday + $scope.datepicker/$scope.dayName) ----
  holiday = signal<HolidayRecord>(emptyHoliday());
  dayName = signal('');
  saveMode = signal(true);
  formSubmitted = signal(false);

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initHolidayCalenderMasterList/PagerService) ----
  holidayList = signal<HolidayRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE only (see class comment).
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.holidayList();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.holidayCalenderCode?.toLowerCase().includes(keyword) ||
        r.holidayCalenderDesc?.toLowerCase().includes(keyword) ||
        r.holidayDate?.toLowerCase().includes(keyword) ||
        r.day?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  // ---- mirrors initHolidayCalenderMasterList (ctrl:136-184) -- list AND count together. ----
  private loadListAndCount(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getHolidayList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.holidayList.set(res.listObject ?? []);
      },
    });
    this.api.getHolidayCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  // ---- mirrors getHolidayCalenderMasterList (ctrl:186-213) -- list ONLY, used by page nav. ----
  private loadListOnly(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getHolidayList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.holidayList.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadListOnly();
  }

  updateHolidayField<K extends keyof HolidayRecord>(field: K, value: HolidayRecord[K]): void {
    this.holiday.update((h) => ({ ...h, [field]: value }));
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- mirrors test() (ctrl:50-72), the datepicker's ng-change handler --
  // fires on every date pick, on create AND edit, deriving holidayDayId
  // client-side and fetching the day-name for display. ----
  onDateChange(dateStr: string): void {
    this.updateHolidayField('date', dateStr);
    if (!dateStr) return;
    const [y, m, d] = dateStr.split('-').map(Number);
    const dayId = jsDayToLegacyDayId(new Date(y, m - 1, d).getDay());
    this.updateHolidayField('holidayDayId', dayId);
    this.api.getDayById(dayId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) this.dayName.set(res.listObject[0].day);
      },
    });
  }

  private requiredFieldsValid(): boolean {
    const h = this.holiday();
    return !!(h.holidayCode.trim() && h.holidayDesc.trim() && h.date);
  }

  // ---- save (mirrors saveHoliday's popUpFlag confirm-modal dance, ctrl:246-317) ----
  saveHoliday(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const h = this.holiday();
    // ctrl:261-264 -- fallback recompute if holidayDayId somehow wasn't set
    // by onDateChange yet. Reproduced verbatim even though it should be
    // unreachable given `date` is a required field validated above.
    let dayId = h.holidayDayId;
    if (dayId === '' || dayId == null) {
      const [y, m, d] = h.date.split('-').map(Number);
      dayId = jsDayToLegacyDayId(new Date(y, m - 1, d).getDay());
    }
    const now = formatDateTime(new Date());
    this.api
      .saveHoliday({
        holidayCalenderCode: h.holidayCode,
        holidayCalenderDesc: h.holidayDesc,
        holidayDate: formatHolidayDate(h.date),
        dayId,
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Holiday Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Holiday already exits...!!');
          }
          this.loadListAndCount();
        },
      });
  }

  // ---- update (mirrors updateHoliday, ctrl:431-496) -- no confirm step ----
  updateHoliday(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const h = this.holiday();
    this.api
      .updateHoliday({
        holidayCalenderId: h.holidayId as number,
        holidayCalenderCode: h.holidayCode,
        holidayCalenderDesc: h.holidayDesc,
        holidayDate: formatHolidayDate(h.date),
        dayId: h.holidayDayId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Holiday Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Holiday already exits...!!');
          }
          this.loadListAndCount();
        },
      });
  }

  private resetForm(): void {
    this.holiday.set(emptyHoliday());
    this.dayName.set('');
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:320-388) ----
  edit(holidayCalenderId: number): void {
    this.saveMode.set(false);
    this.api.getHolidayById(holidayCalenderId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error('Something went wrong...!!');
          return;
        }
        const detail = res.listObject[0];
        // detail.holidayDate is 'dd-MM-yyyy' (GET_HOLIDAY_LIST_BY_ID) --
        // convert to the native date input's 'yyyy-MM-dd', matching what
        // legacy's getChangedFormatedDate (app.js:565-570) does before
        // handing it to the datepicker.
        const [dd, mm, yyyy] = detail.holidayDate.split('-');
        this.holiday.set({
          holidayId: detail.holidayCalenderId,
          holidayCode: detail.holidayCalenderCode,
          holidayDesc: detail.holidayCalenderDesc,
          holidayDayId: detail.dayId,
          date: `${yyyy}-${mm}-${dd}`,
        });
        // ctrl:355-358's `dayId===0` -> also send `day:7` branch is a
        // confirmed dead no-op (see service.ts comment) -- not reproduced.
        this.api.getDayById(detail.dayId).subscribe({
          next: (r) => {
            if (r.status === 'success' && r.listObject?.length) this.dayName.set(r.listObject[0].day);
          },
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:391-428) -- flips the row
  // locally and PERMANENTLY (never reverts, never reloads from server, in
  // either branch) -- see class comment. ----
  toggleStatus(row: HolidayRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.holidayList.update((rows) =>
      rows.map((r) => (r.holidayCalenderId === row.holidayCalenderId ? { ...r, status: nextStatus } : r)),
    );
    this.api.updateHolidayStatus(row.holidayCalenderId, nextStatus, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Holiday Updated Successfully...!!');
        } else {
          this.notify.error('Something went wrong...!!');
        }
      },
    });
  }
}

/** JS `Date.getDay()` (0=Sunday..6=Saturday) -> `m_day_master.day_id`
 * (1=Monday..7=Sunday), matching ctrl:52-55's `if(day==0) day=7`. */
function jsDayToLegacyDayId(jsDay: number): number {
  return jsDay === 0 ? 7 : jsDay;
}

/** `$rootScope.getFormatDate` (app.js:242-246): 'D-M-YYYY' -- deliberately
 * NOT zero-padded, matching legacy exactly -- plus the literal ' 00:00:00'
 * suffix both saveHoliday/updateHoliday append (ctrl:257-259/437-438). */
function formatHolidayDate(isoDate: string): string {
  const [y, m, d] = isoDate.split('-').map(Number);
  return `${d}-${m}-${y} 00:00:00`;
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:248-250/392-394/433-435. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
