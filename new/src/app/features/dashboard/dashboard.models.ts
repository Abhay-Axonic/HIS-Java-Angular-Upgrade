/**
 * One Quick Links tile (CEO-dashboard.html:225-333). `moduleId` is the
 * permission id passed to `checkAccess()`; `infoCode` is the value legacy's
 * `click(info)` receives -- OPD=1, IPD=2, EHR=4, Nursing=5, LIS=6, RIS=7
 * (confirmed by reading the view literally; `click(3)`/DayCare has no tile
 * anywhere in this view, not reproduced). `icon` is a FontAwesome class
 * name, matching legacy's own markup for THIS screen specifically (unlike
 * `doctor-dashboard`'s sibling grid, which uses `<img>` icons -- CEO-
 * dashboard.html's Quick Links tiles are `<i class="fa fa-blind">` etc.,
 * confirmed by reading the view, not assumed from the sibling).
 */
export interface QuickLinkTile {
  moduleId: number;
  infoCode: number;
  label: string;
  icon: string;
}

/**
 * One row of `getOperationCountList`/`getAdmissionDischargeDeathCount`'s
 * `listObject` (ctrl:520-645, 648-782) -- same shape, reused for both the
 * (real) Diagnostics chart and the (real, correctly-labeled) Administration
 * panel's first chart. `intcountArr` is indexed positionally against a
 * fixed category list (`["Lab","Radiology"]` for Diagnostics,
 * `["Admission","Discharge","Death"]` for Admission/Discharge/Death) --
 * confirmed by reading the chart-building code, which zips `dates[i]` to
 * `intcountArr[i]` with no separate index field of its own.
 */
export interface VisitTypeSeriesRow {
  visitTypeId?: number;
  visitType?: string;
  intcountArr?: number[] | null;
}

/** One payment-mode/amount pair inside `getBillingListByVisitType`'s
 * `billingCollectionList` (ctrl:1153-1219). */
export interface BillingCollectionEntry {
  paymentMode: string;
  amount: number;
}

/**
 * One row of `getBillingListByVisitType`'s `listObject` (ctrl:1153-1219) --
 * feeds the 4 real Finance pies. `visitTypeId` is matched literally
 * (1=OP, 2=IP, 3=DC, 4=ER, ctrl:1167/1179/1191/1203), not `visitType`'s
 * string label.
 */
export interface BillingByVisitTypeRow {
  visitTypeId: number;
  visitType?: string;
  billingCollectionList?: BillingCollectionEntry[] | null;
}

/**
 * One row of `getOPVisitsCount`'s `listObject` (ctrl:784-885) -- real
 * content is OP-visit counts by Primary/Follow-Up/Secondary, feeding the
 * Administration panel's MISLABELED second chart (see dashboard.ts's class
 * doc). `totatPatCount` (sic) is the real backend field name, preserved
 * literally, not "corrected" to `totalPatCount`. Legacy repeatedly
 * overwrites a single accumulator while iterating `billingCollectionList`
 * (ctrl:818-823), so only the LAST entry's count actually reaches the
 * chart for each row -- a faithfully-reproduced quirk, not a summed total.
 */
export interface OpVisitsCountRow {
  userDefinedVisitTypeId?: number;
  billingCollectionList?: Array<{ totatPatCount?: number }> | null;
}

/**
 * One row of `getDashboardBedOccupancyList`'s `listObject` (ctrl:1221-1314)
 * -- feeds the Operations panel's Bed Occupancy chart. Any of the 3 counts
 * may be `null`/absent for a given bed category; legacy only pushes a
 * segment for the fields that are actually non-null (ctrl:1240-1264), not
 * reproduced as a fixed 3-segment shape.
 */
export interface BedOccupancyRow {
  bedCategoryId?: number | null;
  bedCategory: string;
  bedVacantCount?: number | null;
  houskeepingCount?: number | null;
  occupiedCount?: number | null;
}

/** One entry of `getDetailedNursingStationList`'s `listObject` -- same real
 * field names as `doctor-dashboard`/`dashboard-info`'s own copies of this
 * type (each screen owns its own DTO copy, per this project's convention). */
export interface NursingStationOption {
  nursingStationId: number;
  nursingStationDesc: string;
}

/** `getDeptListByNursingStationId`'s response shape -- opaque here for the
 * same reason as `doctor-dashboard`/`dashboard-info`'s identical types:
 * neither `object` nor `listObject` is read by this screen beyond caching
 * both to sessionStorage for other screens. */
export type NursingStationWard = Record<string, unknown>;
export type DepartmentNursingStationEntry = Record<string, unknown>;

// ---- View models for this rebuild's hand-rolled CSS charts ----
//
// No charting library exists anywhere in `new/` yet (confirmed: not in
// package.json, and neither `doctor-dashboard` nor any other rebuilt
// screen renders a real chart -- their "Finance"/"Administration"/
// "Operations" panels are decorative static `<img>`s only, see
// doctor-dashboard.html:107-181). Rather than introduce a brand-new
// dependency for one screen, these 4 real charts are rendered with plain
// CSS (conic-gradient pies, flex stacked bars) driven by these small view
// models -- see dashboard.ts's class doc for the full rationale.

/** One slice of a CSS conic-gradient pie chart. */
export interface PieSlice {
  label: string;
  value: number;
  color: string;
  percent: number;
}

/** One segment of a stacked-bar group (one visit-type/category slice within
 * a single bar). */
export interface StackedBarSegment {
  label: string;
  value: number;
  color: string;
}

/** One bar (category) of a stacked-bar chart, e.g. one bed category, or one
 * of "Lab"/"Radiology". */
export interface StackedBarGroup {
  category: string;
  segments: StackedBarSegment[];
  total: number;
}
