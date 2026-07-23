/**
 * One entry of `getDetailedNursingStationList`'s `listObject`
 * (dashboardInfoMasterController.js:262-267). Same real field names
 * `home.models.ts`'s `NursingStationOption` already confirmed for the
 * identical endpoint (`nurseDashboardMasterController.js`'s own copy of
 * this call) -- kept as a screen-local type here rather than imported
 * from `home`, per this batch's "3 independently auditable folders"
 * default (see this screen's own `.ts` class doc).
 */
export interface NursingStationOption {
  nursingStationId: number;
  nursingStationDesc: string;
}

/**
 * `getDeptListByNursingStationId`'s response shape
 * (dashboardInfoMasterController.js:278-293). None of this batch's 3
 * views (`CEO-dashboard-info.html`/`doctor-dashboard-info.html`/
 * `nurse-dashboard-info.html`) render `object`/`listObject` at all --
 * confirmed by reading all 3 in full -- both calls exist purely to prime
 * `sessionStorage` for OTHER screens (the not-yet-built `nurseDashboard`
 * and downstream ward/bed screens), so both are typed opaquely here
 * rather than guessed.
 */
export type NursingStationWard = Record<string, unknown>;
export type DepartmentNursingStationEntry = Record<string, unknown>;
