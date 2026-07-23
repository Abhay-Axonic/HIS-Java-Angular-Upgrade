import { Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe, NgTemplateOutlet } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { CounterSelectionService } from '../../core/counter-selection.service';
import { CounterListItem } from '../../core/counter-selection.models';
import { DashboardService } from './dashboard.service';
import {
  BedOccupancyRow,
  BillingByVisitTypeRow,
  NursingStationOption,
  OpVisitsCountRow,
  PieSlice,
  QuickLinkTile,
  StackedBarGroup,
  VisitTypeSeriesRow,
} from './dashboard.models';

/** Quick Links tile grid (CEO-dashboard.html:225-333) -- 6 tiles, each its
 * own `checkAccess(moduleId)` gate, `click(infoCode)` on click. Preserved
 * exactly as found: `checkAccess(272)`/`click(6)` for LIS,
 * `checkAccess(828)`/`click(7)` for RIS -- `click(3)` (DayCare) has no tile
 * anywhere in this view, not added here either. */
const QUICK_LINK_TILES: QuickLinkTile[] = [
  { moduleId: 1, infoCode: 1, label: 'OPD', icon: 'fa-blind' },
  { moduleId: 26, infoCode: 2, label: 'IPD', icon: 'fa-stethoscope' },
  { moduleId: 243, infoCode: 4, label: 'EHR', icon: 'fa-hospital-o' },
  { moduleId: 44, infoCode: 5, label: 'Nursing', icon: 'fa-heartbeat' },
  { moduleId: 272, infoCode: 6, label: 'LIS', icon: 'fa-flask' },
  { moduleId: 828, infoCode: 7, label: 'RIS', icon: 'fa-life-ring' },
];

// Legacy's own hardcoded chart-color palettes (CEOdashbordController.js),
// preserved for visual fidelity rather than inventing a fresh one.
const FINANCE_COLORS = ['#22EBC8', '#00D1DB', '#00B3E2', '#0093D6', '#5372B9', '#6F518F'];
const OPERATION_COLORS = ['#006BAF', '#4A6BB8', '#7568BC', '#9A64B9', '#BB5FB0', '#D65AA2'];
const BED_OCCUPANCY_COLORS = ['#006BAF', '#4A6BB8', '#7568BC'];

const ADMIN_DEATH_CATEGORIES = ['Admission', 'Discharge', 'Death'];
const DIAGNOSTICS_CATEGORIES = ['Lab', 'Radiology'];
const VISITS_LABELS = ['Primary', 'Follow Up', 'Secondary'];

/**
 * Parity rebuild of `views/common/dashboard/CEO-dashboard.html` +
 * `CEOdashbordController.js` (the legacy "CEO Dashboard" -- the file/state
 * name's real typo "dashbord" is NOT reproduced in this rebuild's own
 * naming). No `data`/`resolve` block on the legacy `dashboard` state --
 * confirmed no `moduleId`/`requireSuperUser` gate exists for this route;
 * access is controlled entirely per-tile via the 6 `checkAccess()` calls
 * below, not at the route level (same shape as `home`/`doctorDashboard`).
 *
 * ---- Session-state reuse (same pattern as `doctorDashboard`) ----
 * Store/Counter/Nursing-station popup handling is structurally identical
 * to `doctor-dashboard.ts` (same 3-popup family, same legacy shape:
 * ctrl:35-38 store, ctrl:61-92 counter, ctrl:94-172 nursing station) --
 * reused here the same way, including the same modal-sequencing design
 * (counter first, nursing-station second, store left uncoordinated) for
 * the same reasons documented in `doctor-dashboard.ts`'s class doc. Counter
 * wording ('Select Counter') and nursing-station wording
 * ('Please select nursing station..!!') are this controller's own literal
 * growl text (ctrl:85,149) -- confirmed identical to `doctorDashboard`'s,
 * not assumed.
 *
 * Store popup: like `doctorDashboard` (and unlike the ~25 controllers
 * `StoreSelectionService`'s own class doc describes), this controller's
 * `storePopupAcesss()` call (ctrl:35-38) has no companion synchronous
 * `.modal('show')` of its own -- it relies solely on
 * `checkAndFetchStores()`'s internal `stores.length > 1` gate. Same
 * call-site contract as `doctorDashboard`, not the "caller shows
 * synchronously" pattern.
 *
 * ---- Quick Links tile gating: SessionService.hasModuleAccess, not a
 * local reimplementation ----
 * Legacy's own `checkAccess(id)` (ctrl:325-334) filters
 * `permissions.modules` by BOTH `id` and `moduleType === 1`.
 * `doctor-dashboard.ts` reimplements that 2-field filter locally (its own
 * `checkAccess()`). This screen instead calls the already-established
 * `SessionService.hasModuleAccess(moduleId)` (single-field `id` match, no
 * `moduleType` filter) directly, per this batch's explicit direction to
 * reuse the shared session service for per-tile gating rather than
 * duplicating a screen-local permissions reader a 3rd time. Disclosed,
 * minor divergence: a permission entry with a matching `id` but a
 * different `moduleType` would show a tile here that legacy's own,
 * stricter 2-field check would hide. Not expected to matter in practice
 * (every real module permission entry audited so far has `moduleType`
 * fixed at 1), but flagged here rather than silently assumed equivalent.
 *
 * ---- Quick Links click(): no functional infoCode payload ----
 * `click(info)` (ctrl:371-378, the SECOND definition -- it overwrites an
 * earlier, dead `click(info)` at ctrl:177-273 that set a batch of
 * now-unused `$scope` booleans) calls `$state.go('dashboardInfo', {params:
 * info})`. `dashboard-info.ts` (already built, batch 13) has NO mechanism
 * to receive or read any such param at all (confirmed by reading its
 * constructor and view in full) -- same conclusion `doctor-dashboard.ts`'s
 * own `click()` already reached for its sibling destination
 * `doctorDashboardInfo`. A plain navigation with no parameter is therefore
 * a faithful match for legacy's actual (non-)effect, not an approximation.
 *
 * ---- 4 real charts + 2 faithfully-reproduced bugs ----
 * No charting library exists anywhere in `new/` yet (not in package.json;
 * every other rebuilt dashboard screen's chart-shaped panels are
 * decorative static `<img>`s only). Rather than pull in a new dependency
 * for one screen, the 4 real charts below are hand-rolled with plain CSS
 * (conic-gradient pies for payment-mode/visit-type breakdowns, flex
 * stacked-bars for category counts) driven by the small view models in
 * `dashboard.models.ts`. This is a disclosed engineering substitution for
 * legacy's Chart.js canvases, not a claim of pixel-parity with them.
 *
 * BUG 1 (Finance panel, 5th chart -- faithfully NOT rendered): legacy's
 * `getListFinanaceCreditCountByVisitType()` (ctrl:384-516, endpoint
 * `getFinanceDetailsCount`) builds a real stacked-bar dataset and renders
 * it into `document.getElementById('bar2')` -- but CEO-dashboard.html's
 * own `<canvas id="bar2">` is fully commented out (:447), so this chart
 * silently never renders in legacy either. Reproduced by omitting this
 * chart's UI (and its data-fetching call) entirely, rather than building a
 * chart with no legacy equivalent to compare it against -- see
 * `dashboard.service.ts`'s class doc for the same decision on the service
 * side.
 *
 * BUG 2 (Administration panel, 2nd chart -- faithfully MISLABELED, see
 * `visitsSlices` below and its template usage): the panel heading is
 * "Administration" and the FIRST chart (`admissionDischargeDeathGroups`,
 * endpoint `getAdmissionDischargeDeathCount`) is correctly real
 * administration data. The SECOND chart under that same heading is wired
 * to `getOPVisitsCount` -- real content is OP-visit counts by
 * Primary/Follow-Up/Secondary (legacy's own chart.js `title.text` for it
 * is literally `'Visits'`, ctrl:872), not administration data at all. This
 * is reproduced deliberately: the chart is built and placed exactly where
 * legacy places it (under "Administration"), fed by exactly the data
 * legacy feeds it (Visits), not "corrected" to either relabel the heading
 * or swap in real administration data.
 */
@Component({
  selector: 'app-dashboard',
  imports: [FormsModule, NgTemplateOutlet, DecimalPipe],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DashboardService);

  readonly storeSelection = inject(StoreSelectionService);
  readonly counterSelection = inject(CounterSelectionService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  readonly quickLinks = QUICK_LINK_TILES;

  // ---- Nursing-station popup state (screen-local, see class doc) ----
  nursingStationList = signal<NursingStationOption[]>([]);
  nursingStationModalVisible = signal(false);
  selectedNursingStation = signal<NursingStationOption | null>(null);

  // ---- Modal-sequencing computed (see class doc + doctor-dashboard.ts) ----
  activeLocalModal = computed<'counter' | 'nursingStation' | null>(() => {
    if (this.counterSelection.visible()) return 'counter';
    if (this.nursingStationModalVisible()) return 'nursingStation';
    return null;
  });

  // ---- Chart data (see class doc for the 2 faithfully-reproduced bugs) ----
  readonly financeOP = signal<PieSlice[]>([]);
  readonly financeIP = signal<PieSlice[]>([]);
  readonly financeDC = signal<PieSlice[]>([]);
  readonly financeER = signal<PieSlice[]>([]);

  readonly diagnosticsGroups = signal<StackedBarGroup[]>([]);
  readonly bedOccupancyGroups = signal<StackedBarGroup[]>([]);
  readonly admissionDischargeDeathGroups = signal<StackedBarGroup[]>([]);
  /** BUG 2 -- see class doc: real content is Visits, rendered as the
   * Administration panel's 2nd chart. */
  readonly visitsSlices = signal<PieSlice[]>([]);

  readonly financeOPGradient = computed(() => this.pieGradient(this.financeOP()));
  readonly financeIPGradient = computed(() => this.pieGradient(this.financeIP()));
  readonly financeDCGradient = computed(() => this.pieGradient(this.financeDC()));
  readonly financeERGradient = computed(() => this.pieGradient(this.financeER()));
  readonly visitsGradient = computed(() => this.pieGradient(this.visitsSlices()));

  readonly diagnosticsMax = computed(() => this.maxTotal(this.diagnosticsGroups()));
  readonly bedOccupancyMax = computed(() => this.maxTotal(this.bedOccupancyGroups()));
  readonly admissionDischargeDeathMax = computed(() =>
    this.maxTotal(this.admissionDischargeDeathGroups()),
  );

  constructor() {
    // Baseline login guard, matching ctrl:23-26 -- belt-and-suspenders on
    // top of `moduleAccessGuard`'s own "must be logged in" check, same
    // reasoning as `doctorDashboard`'s identical guard.
    if (!this.session.current) {
      this.router.navigate(['/login']);
      return;
    }
    // ctrl:42-45 -- cookieObjectAppConfig undefined -> $state.go('login').
    if (!this.session.appConfig) {
      this.router.navigate(['/login']);
      return;
    }

    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;
    const nurseRole = this.session.appConfig['nurseRole'];

    // Store popup (ctrl:35-38) -- see class doc: no synchronous show() of
    // its own, same call-site contract as doctorDashboard.
    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
    }

    // Counter code (ctrl:61-92).
    if (this.counterSelection.isApplicable()) {
      this.counterSelection.checkAndPrepare();
    }

    // Nursing station (ctrl:94-172).
    const cachedWards = this.readNursingStationsWardsList();
    if (cachedWards == null) {
      this.loadDetailedNursingStationList(nurseRole);
    }

    // Graph code (ctrl:380-1314) -- all 5 real calls fire unconditionally
    // on load, matching legacy exactly. `getFinanceDetailsCount` (the 6th,
    // BUG 1) is deliberately omitted -- see class doc.
    this.loadFinance();
    this.loadDiagnostics();
    this.loadAdmissionDischargeDeath();
    this.loadVisits();
    this.loadBedOccupancy();
  }

  /** `checkAccess(id)` (ctrl:325-334) -- see class doc for why this calls
   * the shared `SessionService` rather than reimplementing the filter
   * locally. */
  hasAccess(moduleId: number): boolean {
    return this.session.hasModuleAccess(moduleId);
  }

  /** `click(info)` (ctrl:371-378) -- see class doc: no functional infoCode
   * payload, `dashboard-info` doesn't consume one. */
  click(infoCode: number): void {
    void infoCode;
    void this.router.navigate(['/dashboardInfo']);
  }

  /** `ng-click="selectsetNursingStation()"` (ctrl:146-172). */
  selectsetNursingStation(): void {
    const station = this.selectedNursingStation();
    if (!station) {
      this.notify.warning('Please select nursing station..!!');
      return;
    }
    this.applyNursingStation(station.nursingStationId, station.nursingStationDesc, true);
  }

  /** `ng-change="selectCounter(counterMstObj)"`. */
  onSelectCounter(counter: CounterListItem | null): void {
    this.counterSelection.selectCounter(counter);
  }

  /** This screen's OWN `$scope.confirmCounter` (ctrl:79-91) -- same literal
   * wording as `doctorDashboard`'s own copy. */
  confirmCounter(): void {
    const stored = this.counterSelection.selected();
    if (
      !stored ||
      stored.counterId === undefined ||
      stored.counterId === null ||
      stored.counterId === ''
    ) {
      this.notify.warning('Select Counter');
      return;
    }
    if (Number(stored.counterId) > 0) {
      this.counterSelection.visible.set(false);
    }
  }

  private loadDetailedNursingStationList(nurseRole: unknown): void {
    this.api
      .getDetailedNursingStationList(this.userId, this.organizationId, this.unitId)
      .subscribe({
        next: (res) => {
          if (res.status !== 'success') return;
          const list = res.listObject ?? [];
          this.nursingStationList.set(list);

          // Loose equality matches legacy's own `$scope.nurseRole == 1`
          // (ctrl:105).
          if (nurseRole == 1 && list.length > 0) {
            if (list.length > 1) {
              this.nursingStationModalVisible.set(true);
            } else {
              this.applyNursingStation(list[0].nursingStationId, list[0].nursingStationDesc);
            }
          }
        },
      });
  }

  private applyNursingStation(
    nursingStationId: number,
    nursingStationDesc: string,
    hideModal = false,
  ): void {
    this.api
      .getDeptListByNursingStationId(nursingStationId, this.organizationId, this.unitId)
      .subscribe({
        next: (res) => {
          if (res.status !== 'success') return;
          const departmentNursingStationlist = res.listObject ?? [];
          const wards = res.object ?? [];
          sessionStorage.setItem(
            'specialtyNursingStationCookies',
            JSON.stringify({ departmentNursingStationlist }),
          );
          sessionStorage.setItem(
            'specialtyNursingStationCookiesNur',
            JSON.stringify({ departmentNursingStationlist }),
          );
          sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(wards));
          sessionStorage.setItem('nursingStationDesc', JSON.stringify(nursingStationDesc));
          sessionStorage.setItem('nursingStationId', JSON.stringify(nursingStationId));
          if (hideModal) {
            this.nursingStationModalVisible.set(false);
          }
        },
      });
  }

  /** ctrl:1153-1219 -- `getBillingListByVisitType`, matched by
   * `visitTypeId` literally (1=OP, 2=IP, 3=DC, 4=ER), feeding the 4 real
   * Finance pies. */
  private loadFinance(): void {
    this.api.getBillingListByVisitType(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const rows = res.listObject ?? [];
        for (const row of rows) {
          const slices = this.toPieSlices(row);
          switch (row.visitTypeId) {
            case 1:
              this.financeOP.set(slices);
              break;
            case 2:
              this.financeIP.set(slices);
              break;
            case 3:
              this.financeDC.set(slices);
              break;
            case 4:
              this.financeER.set(slices);
              break;
          }
        }
      },
    });
  }

  private toPieSlices(row: BillingByVisitTypeRow): PieSlice[] {
    const entries = row.billingCollectionList ?? [];
    const total = entries.reduce((sum, e) => sum + (e.amount || 0), 0);
    return entries.map((entry, i) => ({
      label: entry.paymentMode,
      value: entry.amount || 0,
      color: FINANCE_COLORS[i % FINANCE_COLORS.length],
      percent: total > 0 ? ((entry.amount || 0) / total) * 100 : 0,
    }));
  }

  /** ctrl:520-645 -- `getOperationCountList`, Lab/Radiology operation
   * counts, Operations panel's Diagnostics chart. */
  private loadDiagnostics(): void {
    this.api.getOperationCountList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const rows = (res.listObject ?? []).filter((r) => r.intcountArr != null);
        this.diagnosticsGroups.set(
          this.toStackedGroups(rows, DIAGNOSTICS_CATEGORIES, OPERATION_COLORS),
        );
      },
    });
  }

  /** ctrl:648-782 -- `getAdmissionDischargeDeathCount`, Administration
   * panel's FIRST (correctly labeled) chart. */
  private loadAdmissionDischargeDeath(): void {
    this.api.getAdmissionDischargeDeathCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const rows = (res.listObject ?? []).filter((r) => r.intcountArr != null);
        this.admissionDischargeDeathGroups.set(
          this.toStackedGroups(rows, ADMIN_DEATH_CATEGORIES, FINANCE_COLORS),
        );
      },
    });
  }

  private toStackedGroups(
    rows: VisitTypeSeriesRow[],
    categories: string[],
    colors: string[],
  ): StackedBarGroup[] {
    return categories.map((category, categoryIndex) => {
      const segments = rows.map((row, rowIndex) => ({
        label: row.visitType ?? `Type ${row.visitTypeId}`,
        value: row.intcountArr?.[categoryIndex] ?? 0,
        color: colors[rowIndex % colors.length],
      }));
      return { category, segments, total: segments.reduce((sum, s) => sum + s.value, 0) };
    });
  }

  /** ctrl:784-885 -- `getOPVisitsCount`. BUG 2 (see class doc): real
   * content is Visits, rendered as the Administration panel's 2nd chart.
   * Legacy's final chart vars (`labels5`/`series5`) are hardcoded to
   * `["Primary","Follow Up","Secondary"]` regardless of the actual
   * `userDefinedVisitTypeId` on each row (the code that DOES branch on
   * that id, ctrl:806-814, only feeds an unused `testSeries` variable) --
   * reproduced here by zipping the fixed labels to each row BY ARRAY
   * POSITION, matching what legacy actually renders, not what its dead
   * id-matching code implies it should. */
  private loadVisits(): void {
    this.api.getOPVisitsCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        this.visitsSlices.set(this.toVisitsSlices(res.listObject ?? []));
      },
    });
  }

  private toVisitsSlices(rows: OpVisitsCountRow[]): PieSlice[] {
    // ctrl:818-823 -- repeatedly overwrites one accumulator while iterating
    // `billingCollectionList`, so only the LAST entry's count survives per
    // row; faithfully reproduced, not summed.
    const values = rows.map((row) => {
      const list = row.billingCollectionList ?? [];
      return list.length > 0 ? (list[list.length - 1].totatPatCount ?? 0) : 0;
    });
    const total = values.reduce((sum, v) => sum + v, 0);
    return values.map((value, i) => ({
      label: VISITS_LABELS[i] ?? `Type ${i + 1}`,
      value,
      color: FINANCE_COLORS[i % FINANCE_COLORS.length],
      percent: total > 0 ? (value / total) * 100 : 0,
    }));
  }

  /** ctrl:1221-1314 -- `getDashboardBedOccupancyList`, Operations panel's
   * Bed Occupancy chart. Only non-null counts get a segment, matching
   * ctrl:1240-1264's per-field null checks. */
  private loadBedOccupancy(): void {
    this.api.getDashboardBedOccupancyList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const rows = res.listObject ?? [];
        this.bedOccupancyGroups.set(rows.map((row) => this.toBedOccupancyGroup(row)));
      },
    });
  }

  private toBedOccupancyGroup(row: BedOccupancyRow): StackedBarGroup {
    const segments = [];
    // Legacy's own series label text (ctrl:1485: `["vaccant","House
    // Keeping","Occupied"]`) -- "vaccant" preserved literally rather than
    // silently corrected to "Vacant", per this project's no-deviation-
    // without-sign-off policy on legacy display text.
    if (row.bedVacantCount != null) {
      segments.push({
        label: 'vaccant',
        value: row.bedVacantCount,
        color: BED_OCCUPANCY_COLORS[0],
      });
    }
    if (row.houskeepingCount != null) {
      segments.push({
        label: 'House Keeping',
        value: row.houskeepingCount,
        color: BED_OCCUPANCY_COLORS[1],
      });
    }
    if (row.occupiedCount != null) {
      segments.push({
        label: 'Occupied',
        value: row.occupiedCount,
        color: BED_OCCUPANCY_COLORS[2],
      });
    }
    return {
      category: row.bedCategory,
      segments,
      total: segments.reduce((sum, s) => sum + s.value, 0),
    };
  }

  private maxTotal(groups: StackedBarGroup[]): number {
    return Math.max(1, ...groups.map((g) => g.total));
  }

  /** Renders a `PieSlice[]` as a CSS `conic-gradient(...)` string -- the
   * hand-rolled pie-chart mechanism, see class doc. */
  private pieGradient(slices: PieSlice[]): string {
    const total = slices.reduce((sum, s) => sum + s.value, 0);
    if (total <= 0) return '#e0e0e0';
    let cursor = 0;
    const stops: string[] = [];
    for (const slice of slices) {
      const start = (cursor / total) * 360;
      cursor += slice.value;
      const end = (cursor / total) * 360;
      stops.push(`${slice.color} ${start}deg ${end}deg`);
    }
    return `conic-gradient(${stops.join(', ')})`;
  }

  private readNursingStationsWardsList(): unknown[] | null {
    try {
      const raw = sessionStorage.getItem('nursingStationsWardsList');
      return raw ? (JSON.parse(raw) as unknown[]) : null;
    } catch {
      return null;
    }
  }
}
