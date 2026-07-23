import { Component, inject } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { QuickLinksPanel } from '../../shared/quick-links/quick-links-panel';
import { DoctorDashboardInfoService } from './doctor-dashboard-info.service';
import { NursingStationWard } from './doctor-dashboard-info.models';

/**
 * Rebuild of the legacy `doctorDashboardInfo` state
 * (`url: '/doctorDashboardInfo'`, `views/common/dashboard/doctor-dashboard-info.html`
 * + `dashboardInfoMasterController.js`). No `data`/`resolve` block on the
 * legacy state (confirmed reading `.state('doctorDashboardInfo', ...)`,
 * app.js:20750-20758 directly) -- no `moduleId`/`requireSuperUser`.
 *
 * Shares its controller 1:1 with `dashboard-info`/`nurse-dashboard-info`
 * (see that screen's class doc for the full "why 3 folders, not 1" and
 * "what's ported / not ported" writeup -- identical here except for the
 * ONE real divergence below). Built as its own folder per this batch's
 * "independently auditable" default.
 *
 * ---- The one real, confirmed divergence among these 3 screens ----
 * `doctor-dashboard-info.html` (:200-211) has a LIVE Chart.js `<canvas>`
 * in its "finance" row:
 *   <canvas id="line" class="chart chart-line" chart-data="data"
 *     chart-labels="labels" chart-series="series" chart-options="options"
 *     chart-dataset-override="datasetOverride" chart-click="onClick()">
 * `CEO-dashboard-info.html`/`nurse-dashboard-info.html` have the EXACT
 * SAME block, but fully commented out. Every scope variable this canvas
 * binds to (`data`/`labels`/`series`/`options`/`datasetOverride`/
 * `onClick`) only exists inside `dashboardInfoMasterController.js`'s own
 * dead, commented-out code (:308-595) -- there is no live assignment to
 * any of them anywhere in the controller. The chart is real markup that
 * legacy actually renders on THIS screen only, but decorative/
 * non-functional: an empty chart with no data source. Reproduced
 * faithfully as an empty, unwired `<canvas>` placeholder in
 * `doctor-dashboard-info.html` below -- not implemented as a real,
 * working chart (there is no real chart to port; inventing one would be a
 * behavior addition legacy never had).
 */
@Component({
  selector: 'app-doctor-dashboard-info',
  imports: [QuickLinksPanel],
  templateUrl: './doctor-dashboard-info.html',
  styleUrl: './doctor-dashboard-info.scss',
})
export class DoctorDashboardInfo {
  private readonly session = inject(SessionService);
  private readonly api = inject(DoctorDashboardInfoService);

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
