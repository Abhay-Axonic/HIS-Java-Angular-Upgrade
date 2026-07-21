/** Row shape returned by POST /mrdrow/getMrdRowList's `listObject`
 * (ctrl:84-105/113-135/137-159 -- `getRow`/`initRowGroupMasterList`/
 * `masterList` all hit the same endpoint and only ever read `rowCode`,
 * `rowDescription`, `status`, `rowId` off each entry -- view:98-109). */
export interface RowMasterRow {
  rowId: number;
  rowCode: string;
  rowDescription: string;
  status: 'A' | 'I';
}

/** Response shape of GET /mrdrow/getMrdRowById/{rowId}/{orgId}/{unitId}
 * (ctrl:188-198 -- read off `response.data.object`, NOT `listObject`,
 * unlike most other rebuilt screens' getById endpoints). */
export interface RowMasterDetail {
  rowId: number;
  rowCode: string;
  rowDescription: string;
}

/** Editable form state -- mirrors legacy's loose `$scope.rowCode`/
 * `$scope.rowDescription`/`$scope.rowID` globals (ctrl:26-29/193-195). */
export interface RowMasterForm {
  rowId?: number;
  rowCode: string;
  rowDescription: string;
}

export function emptyRowMasterForm(): RowMasterForm {
  return { rowId: undefined, rowCode: '', rowDescription: '' };
}

/** POST body for /mrdrow/saveMrdRow (ctrl:31-57). */
export interface RowMasterSavePayload {
  rowCode: string;
  rowDescription: string;
  unitId: number;
  organizationId: number;
  updatedBy: number;
  createdBy: number;
  status: 'A';
}

/** PUT body for /mrdrow/updateMrdRow (ctrl:201-211). `status` is hardcoded
 * `'A'` in legacy's `showUpdateBtn` regardless of the row's actual current
 * status -- harmless in practice since `viewBtn` (ctrl:179-199) refuses to
 * populate the edit form for any row whose status is already `'I'`, so an
 * edit can only ever be submitted for an already-active row. Reproduced
 * verbatim (hardcoded `'A'`), not derived from the row's own state. */
export interface RowMasterUpdatePayload {
  rowId: number;
  rowCode: string;
  rowDescription: string;
  status: 'A';
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
}

/** PUT body for /mrdrow/updateMrdRowStatus (ctrl:230-254). */
export interface RowMasterStatusPayload {
  rowId: number;
  rowCode: string;
  rowDescription: string;
  status: 'A' | 'I';
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
}
