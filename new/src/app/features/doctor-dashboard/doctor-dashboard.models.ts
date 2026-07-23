/**
 * One entry of `getDetailedNursingStationList`'s `listObject`
 * (doctorDashboardController.js:100-116) -- populates the "Select Nursing
 * Station" dropdown. Same real field names confirmed for the identical
 * endpoint consumed by `home` (nurseDashboardMasterController.js) -- see
 * `new/src/app/features/home/home.models.ts`'s `NursingStationOption`, not
 * imported from there since each rebuilt screen owns its own DTO copy by
 * this project's convention (billing-group-master/asset-scheduler don't
 * cross-import either).
 */
export interface NursingStationOption {
  nursingStationId: number;
  nursingStationDesc: string;
}

/**
 * `getDeptListByNursingStationId`'s response shape
 * (doctorDashboardController.js:116-133, :158-172). `doctor-dashboard.html`
 * has zero bindings consuming either `object` (ward ids) or `listObject`
 * (department ids) beyond caching them to sessionStorage
 * (`nursingStationsWardsList`, `specialtyNursingStationCookies*`) for OTHER
 * screens -- deliberately typed as opaque here rather than guessed, since
 * this screen never reads their internal shape (same reasoning as
 * `home.models.ts`'s identical types for the identical endpoint).
 */
export type NursingStationWard = Record<string, unknown>;
export type DepartmentNursingStationEntry = Record<string, unknown>;

/**
 * Shape of `localStorage.getItem('permissions')`, read defensively by
 * `checkAccess()` (see doctor-dashboard.ts's class doc for why: legacy's
 * own `doctorDashboardController.js:181-186` has NO null-guard on
 * `permissions` before calling `.modules.filter(...)` -- a real, confirmed
 * crash-class gap sibling controllers (`nurseDashboardMasterController`,
 * `CEOdashbordController`, `dashboardInfoMasterController`,
 * `sidebarController`) already guard against with an
 * `if (permissions == undefined) return false;` check doctorDashboard's
 * own controller simply never got. The rebuild uses the defensive form.
 */
export interface StoredPermissions {
  modules?: Array<{ id: number; moduleType: number }>;
}
