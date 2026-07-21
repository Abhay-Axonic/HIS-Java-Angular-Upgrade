export type QMSStatus = 'A' | 'I';

/** Kiosk dropdown option -- getActiveQMSMasterList (ctrl:89-100). Identical
 * endpoint/shape to this codebase's qms-department-master sibling. */
export interface KioskOption {
  qmsKioskMasterId: number;
  qmsDesc: string;
}

/** Raw list entry from getActiveDeptListByKioskId (ctrl:103-123) BEFORE
 * legacy's own controller-side remap into the `{id,label}` shape the
 * ng-dropdown-multiselect widget expects (ctrl:113-118) -- kept separate
 * from `DepartmentOption` to mirror where that remapping actually happens
 * in legacy (the controller, not a service/DTO layer). */
export interface QMSDeptOptionRaw {
  qmsDeptMasterId: number;
  qmsDeptName: string;
}

/** Department multiselect option for a chosen kiosk, post-remap
 * (ctrl:115-118). Kept as a local `{id,label}` pair since the SAVE payload
 * legacy builds reuses this exact widget shape verbatim (see
 * `QMSCounterSavePayload` below). */
export interface DepartmentOption {
  id: number;
  label: string;
}

/** Row shape returned by getQMSCounterMasterList (ctrl:148-159,
 * view:171-186). */
export interface QMSCounterRow {
  qmsCounterId: number;
  qmsCounterCode: string;
  qmsCounterDesc: string;
  status: QMSStatus;
}

/** Mirrors $scope.common (ctrl:47-58) -- the single object used for the
 * add/edit form. `qmsCounterId` is absent on a fresh create and present
 * only after an edit-fetch overwrites `common` wholesale (ctrl:286). */
export interface QMSCounterCommon {
  qmsCounterId?: number;
  qmsCounterCode: string;
  qmsCounterDesc: string;
  qmsKioskMasterId: number | '';
  createdBy: number;
  updatedBy: number;
  organisationId: number;
  unitId: number;
}

/** Matches ctrl:47-58's blank object literal exactly (British "organisation"
 * spelling is legacy's own wire field name here, confirmed against the
 * controller -- NOT a typo to normalize). */
export function emptyQMSCounterCommon(userId: number, organisationId: number, unitId: number): QMSCounterCommon {
  return {
    qmsCounterId: undefined,
    qmsCounterCode: '',
    qmsCounterDesc: '',
    qmsKioskMasterId: '',
    createdBy: userId,
    updatedBy: userId,
    organisationId,
    unitId,
  };
}

/** getQMSCounterMasterById's response.data.object (ctrl:280-307/412-442) --
 * full record plus its saved department list. Each entry's exact shape is
 * inferred from its two legacy usages: `edit()` reads `qmsDeptMasterId`/
 * `qmsDeptName` off it (ctrl:290-298), and the read-only "Department
 * Details" popup additionally renders a `qmsDesc` column per row
 * (QMS-counter-Master.html:244, "QMS Name") -- kept optional since it's only
 * ever read, never written, by this rebuild. */
export interface QMSCounterDetail extends QMSCounterCommon {
  departmentList: Array<{ qmsDeptMasterId: number; qmsDeptName: string; qmsDesc?: string }>;
}

/** SAVE payload (ctrl:216-268): `departmentList` is the FULL `{id,label}`
 * widget shape, sent as-is off `$scope.departmentModel` (ctrl:227). */
export interface QMSCounterSavePayload extends QMSCounterCommon {
  departmentList: DepartmentOption[];
}

/** UPDATE payload (ctrl:315-374): `departmentList` is independently rebuilt
 * as `{id}`-only (ctrl:327-336) -- NOT the same `{id,label}` shape `save()`
 * sends. A real asymmetry already present in legacy, kept as-is here rather
 * than normalized to match save(). */
export interface QMSCounterUpdatePayload extends QMSCounterCommon {
  qmsCounterId: number;
  departmentList: Array<{ id: number }>;
}
