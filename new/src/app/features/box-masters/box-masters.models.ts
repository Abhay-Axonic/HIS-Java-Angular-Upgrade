export type BoxStatus = 'A' | 'I';

/** Row shape returned by getMrdBoxList (ctrl:82-101/109-131/134-156,
 * view:98-110) -- the only fields the legacy view renders. */
export interface BoxRow {
  boxId: number;
  boxCode: string;
  boxDescription: string;
  status: BoxStatus;
}

/** getMrdBoxById's response.data.object (ctrl:184-195). */
export interface BoxDetail {
  boxId: number;
  boxCode: string;
  boxDescription: string;
}

/** Editable form state -- mirrors the loose `$scope.boxCode`/
 * `$scope.boxDescription`/`$scope.boxID` scope variables legacy uses
 * directly (there is no single `$scope.common` object on this screen,
 * unlike amenity-master/qms-department-master). */
export interface BoxForm {
  boxId?: number;
  boxCode: string;
  boxDescription: string;
}

/** Matches legacy's blank state exactly (`boxCode`/`boxDescription` start
 * as `""`, there is no id until an edit-fetch populates one). */
export function emptyBoxForm(): BoxForm {
  return { boxId: undefined, boxCode: '', boxDescription: '' };
}

/** POST body for saveMrdBox (ctrl:47-56). */
export interface BoxSavePayload {
  boxCode: string;
  boxDescription: string;
  unitId: number;
  organizationId: number;
  updatedBy: number;
  createdBy: number;
  status: 'A';
}

/** PUT body for updateMrdBox (ctrl:198-207) -- status is hardcoded 'A' in
 * legacy regardless of the record's actual prior status. Harmless: `edit()`
 * (mirrors legacy's `viewBtn`, ctrl:174-195) already refuses to open an
 * Inactive row for editing, so Update is only ever reachable via a row that
 * was Active to begin with -- see component doc comment. */
export interface BoxUpdatePayload {
  boxId: number;
  boxCode: string;
  boxDescription: string;
  status: 'A';
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
}

/** PUT body for updateMrdBoxStatus (ctrl:226-238) -- legacy passes the row's
 * OWN boxCode/boxDescription through (closure args off the clicked row,
 * ctrl:103/view row), not whatever happens to be sitting in the add/edit
 * form fields at that moment. */
export interface BoxStatusPayload {
  boxId: number;
  boxCode: string;
  boxDescription: string;
  status: BoxStatus;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
}
