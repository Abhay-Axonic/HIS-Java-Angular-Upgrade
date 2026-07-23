import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { CounterSelectionService } from '../../core/counter-selection.service';
import { CounterListItem } from '../../core/counter-selection.models';
import { DoctorDashboardService } from './doctor-dashboard.service';
import { NursingStationOption, StoredPermissions } from './doctor-dashboard.models';

/** One `checkAccess(id)`-gated Quick Links tile (doctor-dashboard.html:196-264).
 * `moduleId` is the permission id passed to `checkAccess()`; `infoCode` is
 * the (unrelated, numerically different) value legacy passes to `click()`
 * -- NOT the same number, confirmed by reading the view literally: OPD is
 * `checkAccess(1)`/`click(1)`, but IPD is `checkAccess(26)`/`click(2)`, EHR
 * is `checkAccess(243)`/`click(4)`, Nursing is `checkAccess(44)`/`click(5)`
 * -- `click(3)` is never used anywhere in this view. Preserved exactly as
 * found, not renumbered. */
interface QuickLinkTile {
  moduleId: number;
  infoCode: number;
  label: string;
  icon: string;
}

const QUICK_LINK_TILES: QuickLinkTile[] = [
  { moduleId: 1, infoCode: 1, label: 'OPD', icon: 'resources/assets/images/icon/dashboard/OPD_Img.png' },
  { moduleId: 26, infoCode: 2, label: 'IPD', icon: 'resources/assets/images/icon/dashboard/Home_IPD.png' },
  { moduleId: 243, infoCode: 4, label: 'EHR', icon: 'resources/assets/images/icon/dashboard/Home_img.png' },
  { moduleId: 44, infoCode: 5, label: 'Nursing', icon: 'resources/assets/images/icon/dashboard/Home_Nursing.png' },
];

/**
 * Parity rebuild of views/common/dashboard/doctor-dashboard.html +
 * doctorDashboardController.js. No `data`/`resolve` block on the legacy
 * state (confirmed by reading `.state('doctorDashboard', ...)`,
 * app.js:20731-20736 directly) -- no `moduleId`/`requireSuperUser` on this
 * screen's route, same as `home`/`billingGroupMaster`.
 *
 * ---- Session-state reuse (see docs/ANGULAR_REBUILD_PLAYBOOK.md Step 3) ----
 * Same 3-popup family `home` (nurseDashboardMasterController.js) already
 * established a pattern for, reused rather than reinvented:
 *  - `StoreSelectionService` -- `selectedTopStore` popup, rendered once
 *    app-shell-wide (app.html), same call-site contract every screen using
 *    it already follows (`if (getSelectedStoreId() == null) { checkAndFetchStores(); show(); }`).
 *  - `CounterSelectionService` -- `isCounterApplicable`/
 *    `isUserCounterApplicable`/`counterList` session state
 *    (`$rootScope.getCounterDetails()`/`selectCounter()`). ONE deliberate
 *    divergence from reusing it wholesale: this screen's OWN
 *    `$scope.confirmCounter` (doctorDashboardController.js:81-93) warns
 *    `'Select Counter'`, not the `'Please select counter..!!'` wording
 *    `CounterSelectionService.confirmSelection()` hardcodes (copied from
 *    `nurseDashboardMasterController`'s own, differently-worded copy of the
 *    same block). A warning message is observable, user-facing behavior --
 *    silently adopting the sibling's wording would be a real, if minor,
 *    parity miss, not a harmless reuse. This component reuses the shared
 *    service's STATE (`counterList`/`visible`/`selected`) and its
 *    `checkAndPrepare()`/`selectCounter()` logic (byte-identical between
 *    both controllers), but implements this screen's own `confirmCounter()`
 *    with this screen's own exact wording.
 *  - Nursing-station popup logic stays screen-local (own service, own
 *    component state), matching legacy: it's defined fresh per controller
 *    against `$scope`, not `$rootScope`, in EVERY screen that has it,
 *    `home` included.
 *
 * ---- Defensive fix (NOT a literal port) ----
 * `checkAccess(id)` (doctorDashboardController.js:181-186) has NO
 * null-guard on `permissions` before `.modules.filter(...)` -- a real,
 * confirmed crash (blank page, `TypeError: Cannot read properties of
 * undefined`) on any session where `localStorage.getItem('permissions')`
 * hasn't been populated yet. Sibling dashboard controllers
 * (`nurseDashboardMasterController`, `CEOdashbordController`,
 * `dashboardInfoMasterController`, `sidebarController`) already guard this
 * exact call with `if (permissions == undefined) return false;` --
 * `doctorDashboardController` is (along with `financerDashboardController`)
 * one of only two that don't. Per this project's standing policy (never
 * literally port a crash-class bug; see `organizationMaster`'s fix and
 * every dashboard sibling's own already-guarded version), `checkAccess()`
 * below is defensive.
 *
 * ---- Modal-sequencing design decision (documented per playbook Step 3) ----
 * Legacy can trigger up to 3 independent Bootstrap modals on a single load
 * with NO coordination between them: the store-selection popup (app-shell-
 * level, `StoreSelectionService`), the counter-selection popup, and the
 * nursing-station-selection popup (both this screen's own markup). All 3
 * conditions are evaluated back-to-back, synchronously, in the same
 * constructor-equivalent code path (ctrl:57-143) -- a session that's
 * simultaneously missing a store choice, has 2+ counters, AND has 2+
 * nursing stations would show all 3 `.modal('show')` calls in the same
 * digest cycle in legacy, and whichever wins is undefined jQuery/Bootstrap
 * z-index/backdrop stacking, not a designed behavior.
 *
 * That race isn't meaningfully portable to Angular's component model (there
 * is no equivalent DOM stacking accident to reproduce). Design decision for
 * THIS screen's 2 self-owned modals (counter, nursing-station):
 * `activeLocalModal` below is a single computed signal that shows exactly
 * one at a time, in the same order the source code evaluates them
 * (counter block first, ctrl:63-94; nursing-station block second,
 * ctrl:96-174) -- counter takes priority if both happen to want to show on
 * the same tick; nursing-station is held until counter is dismissed.
 *
 * The 3rd modal, store-selection, is deliberately NOT folded into this same
 * strict queue, for a concrete reason: `StoreSelectionService` has its own
 * known, disclosed bug (single-store auto-select never hides the modal it
 * already showed synchronously -- see that service's class doc) that can
 * leave `storeSelection.visible()` stuck `true` for an entire session with
 * no further state transition. Gating this screen's own 2 modals behind
 * "wait for store-selection to close" would let that pre-existing, purely
 * cosmetic store-popup bug silently block BOTH of this screen's own,
 * functionally-important modals forever -- a strictly worse outcome than
 * legacy's actual undefined-stacking behavior, which at least still shows
 * every modal eventually. `home.ts` (the closest sibling, same session-
 * state family) already established the precedent of leaving
 * store-selection uncoordinated with its own local modals; this screen
 * follows that same precedent rather than inventing a stricter, riskier
 * rule store-selection's own known bug would make actively harmful.
 */
@Component({
  selector: 'app-doctor-dashboard',
  imports: [FormsModule],
  templateUrl: './doctor-dashboard.html',
  styleUrl: './doctor-dashboard.scss',
})
export class DoctorDashboard {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DoctorDashboardService);

  readonly storeSelection = inject(StoreSelectionService);
  readonly counterSelection = inject(CounterSelectionService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  readonly quickLinks = QUICK_LINK_TILES;

  // getUnitNameForTop() (app.js:333-343) -- reads the same cookieObject
  // field every load; not reactive, doesn't change during this component's
  // life.
  readonly unitName = (this.session.current?.['unitNme'] as string | undefined) ?? '';

  // ---- Nursing-station popup state (screen-local, see class doc) ----
  nursingStationList = signal<NursingStationOption[]>([]);
  nursingStationModalVisible = signal(false);
  selectedNursingStation = signal<NursingStationOption | null>(null);

  // ---- Modal-sequencing computed (see class doc) ----
  activeLocalModal = computed<'counter' | 'nursingStation' | null>(() => {
    if (this.counterSelection.visible()) return 'counter';
    if (this.nursingStationModalVisible()) return 'nursingStation';
    return null;
  });

  constructor() {
    // Baseline login guard -- cookieObject == undefined -> $state.go('login')
    // (ctrl:26-30). Matches `home`'s identical guard for the same reason:
    // `moduleAccessGuard` already enforces "must be logged in" at the route
    // level for every route (this one included, despite having no
    // `moduleId`), so this is belt-and-suspenders, not the only line of
    // defense -- but it's what the legacy controller itself literally does,
    // so it's kept as a faithful, harmless double-check.
    if (!this.session.current) {
      this.router.navigate(['/login']);
      return;
    }
    // cookieObjectAppConfig == undefined -> $state.go('login') (ctrl:38-41).
    // NOTE: this is doctorDashboardController's OWN inline check, not the
    // shared `$rootScope.checkCookies()` helper other sibling screens
    // (`bookViewMasterController`, `dashboardInfoMasterController`) call --
    // that shared helper redirects an appConfig-miss to
    // `applicationConfiguration` instead. Faithfully following THIS
    // controller's own literal code (both misses go to 'login'), not the
    // sibling's different helper.
    if (!this.session.appConfig) {
      this.router.navigate(['/login']);
      return;
    }

    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;
    const nurseRole = this.session.appConfig['nurseRole'];

    // Dead reads, faithfully NOT ported: doctorDashboardController.js:48-55
    // also assigns $scope.radiologySpecialityId/hemodialysisSpecialityId/
    // labSpecialityId/doctorRole/roleName from cookieObjectAppConfig/
    // labConfig, but doctor-dashboard.html has zero bindings reading any of
    // them (grepped the view in full) -- same "no consumer, nothing further
    // to do" call `home.ts` already made for `home.html`'s equivalent dead
    // reads. Only `nurseRole` is kept, because it's the live gate for the
    // nursing-station flow below.

    // Store popup (ctrl:57-60) -- storePopupAcesss()'s own module-permission
    // filter always passes in practice (see StoreSelectionService's class
    // doc). Bug fix (found live, Step 5): unlike the ~25 legacy controllers
    // that call `.modal('show')` synchronously themselves,
    // doctorDashboardController.js (this screen's own controller) has no
    // such call -- it relies solely on storePopup()'s internal
    // `stores.length > 1` gate. Calling show() here unconditionally showed
    // a permanently stuck, empty modal for 0/1-store accounts, which
    // legacy never shows at all in that case. checkAndFetchStores() alone
    // now handles showing the modal only when there are real options.
    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
    }

    // Counter code (ctrl:63-80).
    if (this.counterSelection.isApplicable()) {
      this.counterSelection.checkAndPrepare();
    }

    // Nursing station (ctrl:96-174).
    const cachedWards = this.readNursingStationsWardsList();
    if (cachedWards == null) {
      this.loadDetailedNursingStationList(nurseRole);
    }
    // else: legacy assigns $scope.wardListForGetBedList = cachedWards --
    // doctor-dashboard.html has no binding reading it; nothing further to do.
  }

  private loadDetailedNursingStationList(nurseRole: unknown): void {
    this.api.getDetailedNursingStationList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const list = res.listObject ?? [];
        this.nursingStationList.set(list);

        // Loose equality matches legacy's own `$scope.nurseRole == 1`
        // (ctrl:107). Preserved literally, including the fact that this is
        // almost certainly a copy-paste artifact from
        // nurseDashboardMasterController's identical block -- a DOCTOR
        // session's `nurseRole` would not normally be 1, which means this
        // entire branch (and the real network call that feeds it) is dead
        // for most real doctor users today. Not silently "corrected" to
        // `doctorRole == 1` here -- that would invent behavior legacy never
        // had, per this project's no-deviation-without-sign-off policy.
        // Worth flagging to the owner as a likely bug, same treatment as
        // every other suspected-but-unconfirmed legacy divergence this
        // project has logged rather than silently fixed.
        if (nurseRole == 1) {
          if (list.length > 1) {
            this.nursingStationModalVisible.set(true);
          } else if (list.length === 1) {
            this.applyNursingStation(list[0].nursingStationId, list[0].nursingStationDesc);
          }
        }
      },
    });
  }

  /** `ng-click="selectsetNursingStation()"` on the popup's Ok button
   * (doctor-dashboard.html:423, ctrl:148-174). */
  selectsetNursingStation(): void {
    const station = this.selectedNursingStation();
    if (!station) {
      this.notify.warning('Please select nursing station..!!');
      return;
    }
    this.applyNursingStation(station.nursingStationId, station.nursingStationDesc, true);
  }

  private applyNursingStation(nursingStationId: number, nursingStationDesc: string, hideModal = false): void {
    this.api.getDeptListByNursingStationId(nursingStationId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const departmentNursingStationlist = res.listObject ?? [];
        const wards = res.object ?? [];
        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify({ departmentNursingStationlist }));
        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify({ departmentNursingStationlist }));
        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(wards));
        sessionStorage.setItem('nursingStationDesc', JSON.stringify(nursingStationDesc));
        sessionStorage.setItem('nursingStationId', JSON.stringify(nursingStationId));
        if (hideModal) {
          this.nursingStationModalVisible.set(false);
        }
      },
    });
  }

  /** `ng-change="selectCounter(counterMstObj)"` on the counter `<select>`
   * (doctor-dashboard.html:382, shared `$rootScope.selectCounter`). */
  onSelectCounter(counter: CounterListItem | null): void {
    this.counterSelection.selectCounter(counter);
  }

  /** This screen's OWN `$scope.confirmCounter` (ctrl:81-93) -- shadows
   * `CounterSelectionService.confirmSelection()`'s wording on purpose, see
   * class doc. */
  confirmCounter(): void {
    const stored = this.counterSelection.selected();
    if (!stored || stored.counterId === undefined || stored.counterId === null || stored.counterId === '') {
      this.notify.warning('Select Counter');
      return;
    }
    if (Number(stored.counterId) > 0) {
      this.counterSelection.visible.set(false);
    }
  }

  /** `checkAccess(id)` (ctrl:181-186) -- DEFENSIVE, see class doc. */
  checkAccess(id: number): boolean {
    const permissions = this.readPermissions();
    if (!permissions?.modules) return false;
    return permissions.modules.some((m) => m.id === id && m.moduleType === 1);
  }

  /** `click(info)` (ctrl:189-196) -- legacy navigates to the
   * `doctorDashboardInfo` state, passing `info` as a state param. That
   * destination is now built (Phase 4 batch 13) and confirmed (Step 0/1
   * audit) to be a flat, generic quick-links hub with zero per-param
   * branching -- the passed code isn't consumed by anything real, so a
   * plain navigation with no parameter is a faithful match for legacy's
   * actual behavior, not an approximation. */
  click(infoCode: number): void {
    void infoCode;
    void this.router.navigate(['/doctorDashboardInfo']);
  }

  private readNursingStationsWardsList(): unknown[] | null {
    try {
      const raw = sessionStorage.getItem('nursingStationsWardsList');
      return raw ? (JSON.parse(raw) as unknown[]) : null;
    } catch {
      return null;
    }
  }

  private readPermissions(): StoredPermissions | null {
    try {
      const raw = localStorage.getItem('permissions');
      return raw ? (JSON.parse(raw) as StoredPermissions) : null;
    } catch {
      return null;
    }
  }
}
