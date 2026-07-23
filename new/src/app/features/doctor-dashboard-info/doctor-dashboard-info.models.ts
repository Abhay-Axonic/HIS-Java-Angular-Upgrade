/**
 * One entry of `getDetailedNursingStationList`'s `listObject`
 * (dashboardInfoMasterController.js:262-267) -- same controller/endpoint
 * `dashboard-info`/`nurse-dashboard-info` use, kept as a screen-local type
 * here per this batch's "3 independently auditable folders" default (see
 * this screen's own `.ts` class doc).
 */
export interface NursingStationOption {
  nursingStationId: number;
  nursingStationDesc: string;
}

/**
 * `getDeptListByNursingStationId`'s response shape
 * (dashboardInfoMasterController.js:278-293). `doctor-dashboard-info.html`
 * never renders `object`/`listObject` -- both calls exist purely to prime
 * `sessionStorage` for OTHER screens, so both are typed opaquely here.
 */
export type NursingStationWard = Record<string, unknown>;
export type DepartmentNursingStationEntry = Record<string, unknown>;
