export type DepartmentStoreMapperStatus = 'A' | 'I';

/** Option shape built client-side by `storeList()`/`deptList()`
 * (ctrl:213-221/247-255) from the raw `getStoreList`/`getDeptListById`
 * responses -- legacy pushes `{ id, label }` onto `getMultipleStoreList`/
 * `getMultipleDepartmentList` for the `ng-dropdown-multiselect` widget.
 * Rebuilt here as a plain checkbox list bound directly to arrays of this
 * shape (see class doc comment -- this screen does NOT use the classic
 * dual-checklist-with-commit-button pattern its Mapper siblings use). */
export interface MultiSelectOption {
  id: number;
  label: string;
}

/** Row shape returned by getDeptStoremappedList (ctrl:337-366) -- the only
 * fields the legacy view renders (department-store-mapper.html:116-125). */
export interface DepartmentStoreMapperRow {
  deptStoreMapperId: number;
  storeName: string;
  deptName: string;
  status: DepartmentStoreMapperStatus;
}

/** Shape returned by getDeptStoreMapperById (ctrl:412-436) -- assigned
 * wholesale to `$scope.deptObj` off `response.data.object` (a single
 * object, NOT `listObject[0]` like this codebase's other Mapper screens).
 * `id` is legacy's own field name for the mapped storeId (`deptObj.id`,
 * see the inline comment at ctrl:425: `//deptObj.id=storeId`), kept
 * verbatim here rather than renamed for clarity, since it's the literal
 * field this rebuild's `edit()` reads. */
export interface DepartmentStoreMapperDetail {
  deptStoreMapperId: number;
  id: number; // storeId
  deptId: number;
}

/** POST body for the one real, view-wired save/update endpoint,
 * `SaveAndUpdateDeptStroreMapper` (ctrl:559-608). `deptStoreMapperId` is
 * `''` on create (the default from `initCommon()`, ctrl:54-68) and the
 * real id (from the by-id fetch) on update -- the backend itself
 * disambiguates create vs. update from this one field, since the view's
 * Save/Update button (view:85) always calls this same function regardless
 * of mode. */
export interface DepartmentStoreMapperSavePayload {
  deptStoreMapperId: number | '';
  orgId: number;
  unitId: number;
  storeIds: number[];
  deptIds: number[];
  status: 'A';
  createdBy: number;
  updatedBy: number;
}

/** POST body for `getDeptStoremappedListNew` (ctrl:532-557) -- fired live
 * on every multiselect selection change. See class doc comment: this
 * call's response is a real, live network call but is NOT wired to the
 * visible table -- reproduced faithfully, not "fixed" into a working
 * live-filter. */
export interface DepartmentStoreMapperFilterPayload {
  orgId: number;
  unitId: number;
  storeIds: number[];
  deptIds: number[];
}
