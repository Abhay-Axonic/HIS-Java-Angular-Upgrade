import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { CounterSelectionService } from '../../core/counter-selection.service';
import { CounterListItem } from '../../core/counter-selection.models';
import { HomeService } from './home.service';
import { NursingStationOption, NursingStationWard } from './home.models';

/**
 * Rebuild of the legacy `home` state (`url: '/home'`, no resolve block --
 * no `moduleId`/`requireSuperUser`, confirmed by reading the live
 * `.state('home', ...)` in app.js:20743-20748 directly) -- a genuinely
 * thin screen (`views/common/dashboard/home.html`, 112 lines): a
 * decorative background image plus the SAME 2 session-setup popups
 * `doctorDashboard`/`nurseDashboard` use (nursing-station select, counter
 * select). Both popups' controller logic lives in the SHARED
 * `nurseDashboardMasterController.js`, also used by the separate,
 * not-in-this-batch `nurseDashboard` screen.
 *
 * Deliberately NOT built here, confirmed dead for `home` specifically by
 * exhaustive grep (zero matches in home.html): `openTriageNurse`,
 * `openTriageReceptionist`, `iframForTriage` -- the shared controller's
 * external Supabase AI-triage functions. `home.html` never wires up any
 * `ng-click`/markup calling them.
 *
 * Session-state dependencies reused from the shared, screen-agnostic core
 * services rather than reinvented here (same family `asset-scheduler`/
 * `pm-request`/etc. already use for `selectedTopStore`):
 *  - `StoreSelectionService` -- `selectedTopStore` popup (app.js's
 *    `storePopupAcesss()`/`storePopup()`, called unconditionally here the
 *    same way legacy's own `modules.length >= 0` filter always passes in
 *    practice).
 *  - `CounterSelectionService` (new this screen, core/counter-selection.*)
 *    -- `isCounterApplicable`/`isUserCounterApplicable`/`counterList`
 *    session state, mirroring `$rootScope.getCounterDetails()`/
 *    `selectCounter()`. Built as a shared core service, not screen-local,
 *    since the identical logic is needed again for the not-yet-built
 *    `nurseDashboard` (same controller file) and very likely
 *    `doctorDashboard` (same session-state family, different controller).
 *
 * The nursing-station popup's own logic (`selectsetNursingStation`,
 * the `getDetailedNursingStationList`/`getDeptListByNursingStationId`
 * call sequence) stays screen-local, matching legacy: it's defined fresh
 * per controller instantiation against `$scope`, not `$rootScope` --
 * unlike the store/counter popups, legacy itself doesn't share this state
 * globally.
 */
@Component({
  selector: 'app-home',
  imports: [FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(HomeService);

  readonly storeSelection = inject(StoreSelectionService);
  readonly counterSelection = inject(CounterSelectionService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  // ---- Nursing-station popup state (screen-local, see class doc) ----
  nursingStationList = signal<NursingStationOption[]>([]);
  nursingStationModalVisible = signal(false);
  selectedNursingStation = signal<NursingStationOption | null>(null);

  // Raw counter <select> binding -- legacy's own `ng-model="counterMstObj"`
  // on a plain (non ng-options) <select> takes the *option's raw value*
  // (a `{id, name}` counterList entry), a different shape than the
  // confirmed selection (`CounterSelectionService.selected()`, `{counterId,
  // counterDesc}`) `selectCounter()`/`confirmCounter()` persist to
  // sessionStorage -- kept as two distinct signals here for that reason,
  // not a rebuild-invented split.
  selectedCounterOption = signal<CounterListItem | null>(null);

  constructor() {
    // Baseline login guard -- cookieObject == undefined -> $state.go('login')
    // (nurseDashboardMasterController.js:19-23). $state IS in this
    // controller's own DI list in legacy (unlike the missing-$state-DI bug
    // found elsewhere), so this is a straight, defensive port, not a fix.
    if (!this.session.current) {
      this.router.navigate(['/login']);
      return;
    }
    // cookieObjectAppConfig == undefined -> $state.go('login') (:29-33).
    if (!this.session.appConfig) {
      this.router.navigate(['/login']);
      return;
    }

    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;
    const nurseRole = this.session.appConfig['nurseRole'];

    // Store popup -- var cookieObjectStore = JSON.parse(sessionStorage.
    // getItem('selectedTopStore')); if undefined -> storePopupAcesss()
    // (:56-59), which (its module-permission filter always passes in
    // practice, see StoreSelectionService's own doc) unconditionally calls
    // storePopup() -- checkAndFetchStores() + show(), same call-site
    // contract every other screen using this service already follows.
    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
      this.storeSelection.show();
    }

    // Counter code (:61-78).
    if (this.counterSelection.isApplicable()) {
      this.counterSelection.checkAndPrepare();
    }

    // Nursing station (:120-163).
    const cachedWards = this.readNursingStationsWardsList();
    if (cachedWards == null) {
      this.loadDetailedNursingStationList(nurseRole);
    }
    // else: legacy assigns $scope.wardListForGetBedList = cachedWards --
    // home.html has no binding reading it; nothing further to do.

    // Unconditional bed-list-by-nurse call (:171-195) -- real, live
    // network call whose result home.html never renders; only cached to
    // sessionStorage as a side effect for OTHER screens.
    this.api.getBedListByNurseId(this.organizationId, this.unitId, this.userId, formatShiftDate(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const bedIds = (res.listObject ?? []).map((b) => b.bedId);
          sessionStorage.setItem('mappedNurseBedList', JSON.stringify(bedIds));
        }
      },
    });
  }

  private loadDetailedNursingStationList(nurseRole: unknown): void {
    this.api.getDetailedNursingStationList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const list = res.listObject ?? [];
        this.nursingStationList.set(list);

        // Loose equality matches legacy's own `$scope.nurseRole == 1` (:131).
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
   * (home.html:56, nurseDashboardMasterController.js:92-117). */
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
        const wards: NursingStationWard[] = res.object ?? [];
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
   * (home.html:82). */
  onSelectCounter(counter: CounterListItem | null): void {
    this.selectedCounterOption.set(counter);
    this.counterSelection.selectCounter(counter);
  }

  private readNursingStationsWardsList(): NursingStationWard[] | null {
    try {
      const raw = sessionStorage.getItem('nursingStationsWardsList');
      return raw ? (JSON.parse(raw) as NursingStationWard[]) : null;
    } catch {
      return null;
    }
  }
}

/** Matches legacy's `moment(new Date()).format('DD-MM-YYYY')`
 * (nurseDashboardMasterController.js:177). */
function formatShiftDate(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  return `${dd}-${mm}-${yyyy}`;
}
