import { Component, inject } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { QuickLinksPanel } from '../../shared/quick-links/quick-links-panel';
import { NurseDashboardInfoService } from './nurse-dashboard-info.service';
import { NursingStationWard } from './nurse-dashboard-info.models';

/**
 * Rebuild of the legacy `nurseDashboardInfo` state
 * (`url: '/nurseDashboardInfo'`, `views/common/dashboard/nurse-dashboard-info.html`
 * + `dashboardInfoMasterController.js`). No `data`/`resolve` block on the
 * legacy state (confirmed reading `.state('nurseDashboardInfo', ...)`,
 * app.js:20721-20729 directly) -- no `moduleId`/`requireSuperUser`.
 *
 * Shares its controller 1:1 with `dashboard-info`/`doctor-dashboard-info`
 * (see that screen's class doc for the full "why 3 folders, not 1" and
 * "what's ported / not ported" writeup -- identical here). Built as its
 * own folder per this batch's "independently auditable" default.
 *
 * Only cosmetic divergence on THIS screen (no behavioral difference):
 * `nurse-dashboard-info.html` uses a different icon set for the
 * right-column cards (real `<img>` icons under
 * `resources/assets/images/icon/home_icon/`) and different heading/icon
 * colors (`#FFB800`/`#00003B`) than `CEO-dashboard-info.html`/
 * `doctor-dashboard-info.html`'s plain `fa-*` icons -- reproduced exactly
 * in `nurse-dashboard-info.html`/`.scss`. Like the other 2 screens, the
 * Chart.js `<canvas>` block is fully commented out here (:211-218) -- no
 * chart on this screen, matching `dashboard-info`, not
 * `doctor-dashboard-info`.
 */
@Component({
  selector: 'app-nurse-dashboard-info',
  imports: [QuickLinksPanel],
  templateUrl: './nurse-dashboard-info.html',
  styleUrl: './nurse-dashboard-info.scss',
})
export class NurseDashboardInfo {
  private readonly session = inject(SessionService);
  private readonly api = inject(NurseDashboardInfoService);

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
