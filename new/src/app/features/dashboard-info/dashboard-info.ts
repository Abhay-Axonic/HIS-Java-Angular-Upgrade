import { Component, inject } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { QuickLinksPanel } from '../../shared/quick-links/quick-links-panel';
import { DashboardInfoService } from './dashboard-info.service';
import { NursingStationWard } from './dashboard-info.models';

/**
 * Rebuild of the legacy `dashboardInfo` state (`url: '/dashboardInfo'`,
 * `views/common/dashboard/CEO-dashboard-info.html` +
 * `dashboardInfoMasterController.js`). No `data`/`resolve` block on the
 * legacy state at all (confirmed reading `.state('dashboardInfo', ...)`,
 * app.js:20698-20706 directly) -- no `moduleId`/`requireSuperUser` on this
 * route, same shape as `billingGroupMaster`/`periodicityService`/`home`.
 *
 * A genuine 0-field "quick-links hub" screen: navigation tiles/icons plus
 * 2 conditional API calls, no form, no list, no save. Shared 1:1 with
 * `doctor-dashboard-info`/`nurse-dashboard-info` (same controller file,
 * `dashboardInfoMasterController.js`, entirely flat -- zero per-route
 * branching; only the view template differs per screen). Built as 3
 * separate feature folders rather than one shared folder -- no existing
 * precedent in `new/src/app/features/` for near-identical sibling screens
 * sharing a folder (even closely related pairs like
 * `asset-issue-acceptance`/`asset-return-acceptance` are separate folders)
 * -- so each screen's parity target stays independently auditable. The
 * genuinely shared piece (the Quick Links tile grid, byte-identical
 * behavior/markup across all 3) is factored into
 * `shared/quick-links/QuickLinksPanel` instead, matching the established
 * shared-component precedent (`<app-status-toggle>`).
 *
 * ---- What's ported ----
 * The 2 real `nurseRole == 1`-gated API calls
 * (dashboardInfoMasterController.js:256-302): `getDetailedNursingStationList`,
 * then (only when exactly 1 station comes back) `getDeptListByNursingStationId`.
 * Neither call's response is rendered anywhere in `CEO-dashboard-info.html`
 * (confirmed reading the view in full) -- both exist purely to prime
 * `sessionStorage` (`nursingStationsWardsList`, `specialtyNursingStationCookies*`,
 * `nursingStationDesc`, `nursingStationId`) for OTHER screens. The 2+
 * stations branch shows `#nursingStationModal` in legacy -- that element
 * does not exist anywhere in `CEO-dashboard-info.html`'s own markup
 * (confirmed by grep), so on THIS screen specifically it's a real,
 * confirmed silent dead end (jQuery `.modal('show')` on a selector with no
 * matching element = no-op), not a gap this rebuild introduces.
 *
 * ---- What's deliberately NOT ported, and why ----
 *  - The counter-selection block (dashboardInfoMasterController.js:236-254,
 *    `$scope.getCounterDetails()`, resolves via `$rootScope` since this
 *    controller doesn't shadow it) -- reads/writes `$rootScope.counterId`/
 *    `counterDesc` only; no counter `<select>`/modal exists anywhere in
 *    this screen's own view, and the block makes no new network call
 *    (`CounterSelectionService.checkAndPrepare()`, the equivalent already
 *    built for `home`, only reads cached local/sessionStorage). A
 *    completely inert side effect for this screen specifically -- not
 *    reproduced, consistent with the given audit scoping ("2 conditional
 *    API calls", not 3).
 *  - The Quick Links grid's ~690 individual level-3 sub-menu items (the
 *    per-category expansion content) -- see `QuickLinksPanel`'s own class
 *    doc for the full scope-decision writeup (a scripted extraction
 *    attempt was made and abandoned after finding it silently dropped a
 *    real item on the very first spot-check).
 *  - The right-column Finance/Administration/Operation/Task/Notification
 *    cards have NO `ng-click` anywhere in legacy's markup -- reproduced as
 *    non-interactive, decorative-only, matching exactly.
 *  - No Chart.js `<canvas>` on this screen -- `CEO-dashboard-info.html`'s
 *    copy of that block is fully commented out (:202-209), unlike
 *    `doctor-dashboard-info.html`'s live (but non-functional, all bound
 *    scope vars dead/commented in the controller) copy. See
 *    `doctor-dashboard-info.ts` for that one screen's divergence.
 */
@Component({
  selector: 'app-dashboard-info',
  imports: [QuickLinksPanel],
  templateUrl: './dashboard-info.html',
  styleUrl: './dashboard-info.scss',
})
export class DashboardInfo {
  private readonly session = inject(SessionService);
  private readonly api = inject(DashboardInfoService);

  constructor() {
    const unitId = this.session.unitId ?? 0;
    const organizationId = this.session.organizationId ?? 0;
    const userId = this.session.userId ?? 0;
    const nurseRole = this.session.appConfig?.nurseRole;

    // dashboardInfoMasterController.js:256-259 -- loose equality matches
    // legacy's own `$scope.nurseRole == 1`.
    if (nurseRole == 1 && this.readCachedWards() == null) {
      this.api.getDetailedNursingStationList(userId, organizationId, unitId).subscribe({
        next: (res) => {
          if (res.status !== 'success') return;
          const list = res.listObject ?? [];
          if (list.length === 1) {
            this.applyNursingStation(list[0].nursingStationId, list[0].nursingStationDesc, organizationId, unitId);
          }
          // list.length > 1: legacy shows #nursingStationModal, absent
          // from this screen's own view -- confirmed real dead end, not
          // reproduced as a fabricated modal.
        },
      });
    }
  }

  private applyNursingStation(
    nursingStationId: number,
    nursingStationDesc: string,
    organizationId: number,
    unitId: number,
  ): void {
    this.api.getDeptListByNursingStationId(nursingStationId, organizationId, unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const departmentNursingStationlist = res.listObject ?? [];
        const wards: NursingStationWard[] = res.object ?? [];
        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify({ departmentNursingStationlist }));
        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify({ departmentNursingStationlist }));
        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(wards));
        sessionStorage.setItem('nursingStationDesc', JSON.stringify(nursingStationDesc));
        sessionStorage.setItem('nursingStationId', JSON.stringify(nursingStationId));
      },
    });
  }

  private readCachedWards(): NursingStationWard[] | null {
    try {
      const raw = sessionStorage.getItem('nursingStationsWardsList');
      return raw ? (JSON.parse(raw) as NursingStationWard[]) : null;
    } catch {
      return null;
    }
  }
}
