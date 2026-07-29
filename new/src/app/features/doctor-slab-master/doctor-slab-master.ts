import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DoctorSlabMasterService } from './doctor-slab-master.service';
import {
  DoctorOption,
  DoctorSlabDetail,
  DoctorSlabMasterListRow,
  DoctorSlabRow,
  emptyDoctorSlabRow,
} from './doctor-slab-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/billingMaster/doctor-slab-master.html +
 * resources/assets/js/controller/billing/unit/doctorSlabMasterController.js
 * (`doctorSlabMasterController`, classic `$scope` pattern -- no `vm.*`
 * bindings anywhere in the view). Legacy state `doctorSlabMaster`,
 * `url: '/doctorSlabMaster'`, module gate 1020
 * (config/app.js:14696-14719, resolve block: `item.id == 1020`).
 *
 * Business rule (preserve exactly -- see doctor-slab-master.ts/.html
 * comments at each call site below for the precise validation order/
 * messages): a doctor's commission is a dynamic ordered list of
 * percentage-based tiers (`slabFrom`-`slabTo` @ `percentage`%). A new
 * tier's `slabFrom` always auto-populates from the previous tier's
 * `slabTo` (ctrl:98, `addSlab`) and is permanently read-only afterward
 * (view:38, `ng-disabled="true"`) -- there is no independent `slabFrom`
 * validation live anywhere (ctrl:145-152's `slabFromFound` check is
 * commented out in legacy; NOT ported here).
 */
@Component({
  selector: 'app-doctor-slab-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './doctor-slab-master.html',
  styleUrl: './doctor-slab-master.scss',
})
export class DoctorSlabMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DoctorSlabMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- doctor dropdown (ctrl:38-49, re-fetched on init AND after every
  // successful save/status-toggle -- ctrl:213/286 both call init() again) ----
  doctorsList = signal<DoctorOption[]>([]);
  /** Kept as a string to match the legacy `<select ng-model="doctorId">`
   * (option values are `{{info.doctorId}}` strings) and its `doctorId == ""`
   * empty check (ctrl:133) exactly. */
  doctorId = signal<string>('');

  // ---- editable slab-tier list ($scope.doctorSlabList, ctrl:51-56) ----
  doctorSlabList = signal<DoctorSlabRow[]>([emptyDoctorSlabRow()]);

  // ---- summary table + pagination ($scope.doctorSlabMasterList) ----
  doctorSlabMasterList = signal<DoctorSlabMasterListRow[]>([]);
  /** `$scope.commonListCount` -- set from `response.data.object`, the 5th
   * `Response` constructor argument. `DoctorSlabMasterDaoImpl.getdoctorSlabList`
   * (Billing/.../dao/DoctorSlabMasterDaoImpl.java:88) hardcodes this to
   * `null` (`new Response<>(SUCCESS, SUCCESS_CODE, null, list, null)`) --
   * there is no separate count endpoint for this screen. See this batch's
   * audit report: this is a confirmed, real backend bug (not reproduced
   * here as a client-side guess) that makes `PagerService.GetPager`'s
   * `totalPages` always resolve to 0 and its `pages` array always empty,
   * so the pagination control (`ng-if="pager.pages.length"`,
   * doctor-slab-master.html:138) never renders on real, unmodified
   * legacy today, regardless of how many doctors actually have slabs
   * configured -- only the first page (default 10 rows) is ever visible.
   * Faithfully reproduced below via `totalPages`/`pagerPages`, NOT
   * normalized to the `Math.max(1, ...)` "always show page 1" pattern
   * several sibling screens use with their own, genuinely-working count
   * endpoints. */
  commonListCount = signal<number | null>(null);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  currentPage = signal(1);
  /** Local-only filter (`ng-repeat="info in doctorSlabMasterList|filter:searchKeyword"`,
   * view:120) -- a real, live Angular `filter` over the already-loaded
   * page, not a backend search call. There is no dead/decorative search
   * box on this screen (unlike several other master screens audited so
   * far) -- this one is genuinely wired, just client-side only. */
  searchKeyword = signal('');

  totalPages = computed(() => {
    const count = this.commonListCount();
    if (count == null) return 0;
    return Math.ceil(count / this.pageSize());
  });

  /** Faithful port of `PagerService.GetPager`'s windowing algorithm
   * (resources/assets/js/controller/lis/common/pagerService.js) -- kept
   * exact (including the >10-total-pages windowing legacy has) so that if
   * the backend count bug above is ever fixed, this screen's pagination
   * already matches legacy's real algorithm rather than a generic
   * always-page-1 stand-in. */
  pagerPages = computed(() => {
    const total = this.totalPages();
    if (total <= 0) return [] as number[];
    const current = this.currentPage();
    let startPage: number;
    let endPage: number;
    if (total <= 10) {
      startPage = 1;
      endPage = total;
    } else if (current <= 6) {
      startPage = 1;
      endPage = 10;
    } else if (current + 4 >= total) {
      startPage = total - 9;
      endPage = total;
    } else {
      startPage = current - 5;
      endPage = current + 4;
    }
    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  });

  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.doctorSlabMasterList();
    return this.doctorSlabMasterList().filter((row) => (row.doctorName ?? '').toLowerCase().includes(keyword));
  });

  // ---- "Slab Info" read-only modal (view info icon, ctrl:322-359 flag=1) ----
  doctorSlabDetailsList = signal<DoctorSlabDetail[]>([]);
  showSlabInfoModal = signal(false);

  constructor() {
    // ctrl:62 ($scope.init()) + ctrl:258 (unconditional initial list load) --
    // both run once at controller instantiation, matching this constructor.
    this.loadDoctorsList();
    this.loadDoctorSlabMasterList(0, this.pageSize());
  }

  private loadDoctorsList(): void {
    this.api.getActiveDoctorList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.doctorsList.set(res.status === 'success' ? (res.listObject ?? []) : []);
      },
    });
  }

  /** ctrl:229-256 (`getDocotrSlabMasterList`). Legacy uses `BillingGenericService`
   * with `NOTIFICATION_MSG_STATUS_FALSE` for this call -- its own generic
   * error growl is suppressed, and there is no explicit error branch in
   * the controller either, so a failed load here is silent on real
   * legacy too; no error handler is added here for parity. */
  private loadDoctorSlabMasterList(offset: number, noOfRecordsPerPage: number): void {
    this.api.getDoctorSlabList(this.organizationId, this.unitId, offset, noOfRecordsPerPage).subscribe({
      next: (res) => {
        this.doctorSlabMasterList.set(res.listObject ?? []);
        this.commonListCount.set(typeof res.object === 'number' ? res.object : null);
        this.setPage(1, false);
      },
    });
  }

  /** ctrl:302-312 (`setPage`), exact `(page, flag)` semantics: `flag` only
   * decides whether to re-fetch; the pager display itself always updates. */
  setPage(page: number, reload: boolean): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    if (reload) {
      this.loadDoctorSlabMasterList((page - 1) * this.pageSize(), this.pageSize());
    }
  }

  /** ctrl:313-316 (`setNoOfRecords`). Legacy always reloads from
   * `$scope.offset`, a top-level scope var initialized to `0` in `init()`
   * and never reassigned anywhere else in the whole controller (paging
   * via `setPage` only ever mutates its own local `pager.startIndex`, not
   * `$scope.offset` itself) -- so changing the page-size dropdown always
   * reloads from offset 0 on real legacy, regardless of which page was
   * previously showing. Reproduced exactly, not "corrected" to preserve
   * the current page. */
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadDoctorSlabMasterList(0, this.pageSize());
  }

  // ---- slab-tier list editing (ctrl:65-130, 362-371) ----

  /** ctrl:65-102 (`addSlab`), 1-based `cIndex` matching
   * `ng-click="addSlab($index+1)"` (view:71) exactly -- validates the
   * CLICKED row (not the about-to-be-created one), in this exact order:
   * Slab To filled -> Percentage filled -> Percentage <= 100 -> Slab To >
   * Slab From. Only on passing all four does a new row get appended to
   * the END of the list (`push`, not inserted after the clicked row) with
   * its `slabFrom` seeded from the clicked row's `slabTo`. */
  addSlab(cIndex: number): void {
    const list = this.doctorSlabList();
    const row = list[cIndex - 1];
    if (!row) return;

    if (row.slabTo === '') {
      this.notify.warning('Please Enter Slab To ');
      return;
    }
    if (row.percentage === '') {
      this.notify.warning('Please Enter Slab Percentage ');
      return;
    }
    if (Number(row.percentage) > 100) {
      this.notify.warning('Slab Percentage less than or equals to 100');
      this.updateRow(cIndex - 1, { percentage: '' });
      return;
    }
    if (parseInt(row.slabTo, 10) <= parseInt(row.slabFrom, 10)) {
      this.notify.warning('Slab to  should be greater than slab from ');
      this.updateRow(cIndex - 1, { slabTo: '' });
      return;
    }

    this.doctorSlabList.update((rows) => [
      ...rows,
      { slabFrom: row.slabTo, slabTo: '', percentage: '', status: 'A' as const },
    ]);
  }

  /** ctrl:104-130 (`removeSlab`), 1-based `index` matching
   * `ng-click="removeSlab($index+1)"` (view:74). If this is the ONLY row
   * left, legacy does nothing at all (a lone row can never be removed --
   * `if(le != 1)` guards the whole body). Otherwise: a row that was only
   * ever added client-side (no `doctorSlabMasterId`, never persisted) is
   * dropped from the array outright; a row that WAS loaded from the
   * server (has a `doctorSlabMasterId`) is kept but soft-deleted
   * in-place (`status = 'I'`) so the next Save sends it through with the
   * inactive status rather than removing it from the array. */
  removeSlab(index: number): void {
    const list = this.doctorSlabList();
    if (list.length === 1) return;

    const next: DoctorSlabRow[] = [];
    list.forEach((row, i) => {
      const position = i + 1;
      if (position !== index) {
        next.push(row);
      } else if (row.doctorSlabMasterId != null) {
        next.push({ ...row, status: 'I' });
      }
    });
    this.doctorSlabList.set(next);
  }

  /** ctrl:362-371 (`toChecklimit`), bound via
   * `ng-change="toChecklimit(slabList.percentage,$index)"` (view:64) on
   * EVERY row's Percentage field, not just newly-added ones. Note this is
   * a *different* message than `addSlab`'s equivalent >100 check
   * (ctrl:80-87) -- not a typo here, legacy really does use two different
   * strings for the same rule in two different places. */
  toChecklimit(percentage: string, index: number): void {
    if (Number(percentage) > 100) {
      this.notify.warning('Percentage should be less than or equals to 100 !!!');
      this.updateRow(index, { percentage: '' });
    }
  }

  /** Template wiring for the Percentage field's `ng-change="toChecklimit(...)"`
   * (view:64): the new value is applied first (matching `ng-model`'s own
   * synchronous update before `ng-change` fires), then `toChecklimit` runs
   * and may immediately clear it back out again if it's over 100. */
  onPercentageChange(index: number, value: string): void {
    this.updateRow(index, { percentage: value });
    this.toChecklimit(value, index);
  }

  updateRow(index: number, patch: Partial<DoctorSlabRow>): void {
    this.doctorSlabList.update((rows) => rows.map((r, i) => (i === index ? { ...r, ...patch } : r)));
  }

  // ---- save (ctrl:132-225, `saveDoctorSlab`) ----

  /** Single unified Save button for BOTH create (fresh doctor selection,
   * the one default blank row) and edit-then-save (after the pencil icon
   * loads an existing doctor's real rows into the same list) -- there is
   * no separate Update function anywhere in legacy; the backend decides
   * per-row create vs. update from each row's own `doctorSlabMasterId`.
   * Validation order, exactly as legacy has it: doctor selected -> every
   * row's Slab To filled -> every row's Percentage filled -> every row's
   * Slab From < Slab To. (The commented-out `slabFromFound` block,
   * ctrl:145-152, is dead code in legacy -- deliberately not ported.) */
  saveDoctorSlab(): void {
    const doctorId = this.doctorId();
    if (doctorId === '' || doctorId == null) {
      this.notify.warning('Please select doctor.');
      return;
    }

    const list = this.doctorSlabList();

    if (list.some((r) => r.slabTo === '' || r.slabTo == null)) {
      this.notify.warning('Please Enter Slab To ');
      return;
    }
    if (list.some((r) => r.percentage === '' || r.percentage == null)) {
      this.notify.warning('Please Enter Percentage ');
      return;
    }
    const isValidSlab = list.every((r) => parseInt(r.slabFrom, 10) < parseInt(r.slabTo, 10));
    if (!isValidSlab) {
      this.notify.warning('Slab to  should be greater than slab from');
      return;
    }

    const currentDate = formatDateTime(new Date());
    const doctorIdNum = Number(doctorId);
    const payload = list.map((row) => ({
      ...row,
      doctorId: doctorIdNum,
      unitId: this.unitId,
      organizationId: this.organizationId,
      createdDate: currentDate,
      updatedDate: currentDate,
      status: row.status,
      createdBy: this.userId,
      updatedBy: this.userId,
      amount: null,
    }));

    this.api.doctorSlabSave(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Doctor slab added successfully...!!');
          this.doctorId.set('');
          this.doctorSlabList.set([emptyDoctorSlabRow()]);
          this.loadDoctorsList();
          this.loadDoctorSlabMasterList(0, this.pageSize());
        } else {
          this.notify.error(res.message ?? '');
        }
      },
    });
  }

  // ---- status toggle (ctrl:260-297, `updateStatus`) ----

  /** Per-DOCTOR toggle, not per-tier -- the payload is `{doctorId, status,
   * updatedBy, updatedDate, organizationId}` with no per-row identifier,
   * and the real backend query
   * (`DoctorSlabMasterDaoImpl.updateSlabStatusByDoctorId`) flips every
   * slab row belonging to that doctor at once. See this batch's audit
   * report for a confirmed, more serious scoping gap in that same server
   * query (no `unitId`/`organizationId` filter at all) -- flagged there,
   * not fixed here; this method sends legacy's exact real payload.
   *
   * Uses `<app-status-toggle>`'s standard update-on-confirmed-success
   * pattern (project-wide standing convention, not re-litigated per
   * screen) rather than legacy's own raw `ng-model` checkbox, which flips
   * instantly on click and never reverts on failure. */
  toggleStatus(row: DoctorSlabMasterListRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    const updatedDate = formatDateTime(new Date());
    this.api.updateSlabStatusByDoctorId(row.doctorId, nextStatus, this.userId, updatedDate, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.doctorSlabMasterList.update((rows) =>
            rows.map((r) => (r.doctorId === row.doctorId ? { ...r, status: nextStatus } : r)),
          );
          this.notify.success('Doctor slab Updated Successfully...!!');
          // ctrl:286 (`$scope.init()`) resets the editable slab-tier form
          // and re-fetches the doctor dropdown, but deliberately does NOT
          // clear `$scope.doctorId` the way a successful Save does
          // (ctrl:212) -- that asymmetry is real, not an omission here.
          this.doctorSlabList.set([emptyDoctorSlabRow()]);
          this.loadDoctorsList();
          this.loadDoctorSlabMasterList(0, this.pageSize());
        } else {
          // ctrl:289-295: hardcoded generic message, NOT `res.message`
          // passed through (unlike saveDoctorSlab's error branch above).
          this.notify.error('Something went wrong...!!');
        }
      },
      error: () => this.notify.error('Something went wrong...!!'),
    });
  }

  // ---- view / edit (ctrl:322-359, `viewInfo`) ----

  /** flag=1: read-only "Slab Info" modal (info icon). Legacy resets
   * `doctorSlabDetailsList` to `[]` UNCONDITIONALLY on every response
   * (ctrl:334, before the flag/length check), but only actually OPENS the
   * modal (`angular.element('#doctorSlabInfo').modal('show')`) when the
   * response has rows (view:338, inside the `length>0` guard). A first
   * click on a doctor with zero rows silently no-ops (modal never opens).
   * But if the modal is already open from an earlier click and a second
   * info-icon click (a different doctor, now with zero rows) resolves
   * empty, the already-open modal's list still gets cleared to `[]` and
   * its "No Records Found" fallback row becomes visible -- reproduced
   * here by always clearing the list first, matching that exact edge
   * case rather than only updating it when non-empty. */
  viewSlabInfo(row: DoctorSlabMasterListRow): void {
    this.api.getDoctorSlabListByDoctorId(this.organizationId, this.unitId, row.doctorId).subscribe({
      next: (res) => {
        const list = res.listObject ?? [];
        this.doctorSlabDetailsList.set(list);
        if (list.length > 0) {
          this.showSlabInfoModal.set(true);
        }
      },
    });
  }

  closeSlabInfoModal(): void {
    this.showSlabInfoModal.set(false);
  }

  /** flag=0: edit (pencil icon). The pencil is only ever visually dimmed
   * for an Inactive doctor (`ng-class="{disabled: info.status=='I'}"`,
   * view:127) -- there is no real `ng-disabled`/guard anywhere, so the
   * click still fires and still loads the row for editing even when it
   * LOOKS disabled. Reproduced exactly (cosmetic-only "disabled" look,
   * not an actual functional restriction) via `disabledLook`, not
   * `[disabled]`, in the template. */
  editDoctorSlab(row: DoctorSlabMasterListRow): void {
    this.api.getDoctorSlabListByDoctorId(this.organizationId, this.unitId, row.doctorId).subscribe({
      next: (res) => {
        const list = res.listObject ?? [];
        if (list.length === 0) return;
        this.doctorId.set(String(row.doctorId));
        this.doctorSlabList.set(
          list.map((v) => ({
            doctorSlabMasterId: v.doctorSlabMasterId ?? null,
            slabFrom: String(v.slabFrom ?? ''),
            slabTo: String(v.slabTo ?? ''),
            percentage: String(v.percentage ?? ''),
            status: v.status ?? 'A',
            amount: v.amount ?? null,
          })),
        );
      },
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:142-143/261-263, same helper shape already used by other rebuilt
 * screens in this batch (e.g. prefix-master.ts). */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
