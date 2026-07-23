/**
 * One entry of `getDetailedNursingStationList`'s `listObject`
 * (nurseDashboardMasterController.js:124-160) -- populates the "Select
 * Nursing Station" dropdown. Real field names, confirmed by the view's
 * own `{{departmentNursing.nursingStationDesc}}` binding (home.html:46).
 */
export interface NursingStationOption {
  nursingStationId: number;
  nursingStationDesc: string;
}

/**
 * `getDeptListByNursingStationId`'s response shape
 * (nurseDashboardMasterController.js:100-116, :140-155). `home.html` has
 * zero bindings consuming either `object` or `listObject` beyond caching
 * them to sessionStorage (`nursingStationsWardsList`,
 * `specialtyNursingStationCookies*`) for OTHER screens
 * (`nurseDashboard`/downstream ward/bed screens) -- deliberately typed as
 * opaque here rather than guessed, since this screen never reads their
 * internal shape.
 */
export type NursingStationWard = Record<string, unknown>;
export type DepartmentNursingStationEntry = Record<string, unknown>;

/**
 * `getbedlistbyNurseId`'s response shape (nurseDashboardMasterController.js:
 * 171-195) -- a real, live POST fired unconditionally on init, matching
 * legacy's own network behavior exactly. `home.html` itself never renders
 * `mappedNurseBedList`; the response is only cached to sessionStorage as a
 * side effect other (non-`home`) screens read. Only `bedId` is used by
 * that cache-building step -- the rest of the DTO is opaque here.
 */
export interface MappedNurseBed {
  bedId: number;
  [key: string]: unknown;
}
