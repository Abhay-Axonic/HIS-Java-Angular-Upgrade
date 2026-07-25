/**
 * Field names/types read directly from `OperationTableTypeDto.java`
 * (legacy/Global/src/main/java/com/param/global/otims/dto/
 * OperationTableTypeDto.java) and `operationTableTypeMasterController.js`'s
 * `$scope.common` object (ctrl:45-51, `{ tableDesc, tableName, tableCode,
 * operationTableTypeId }`). See `operation-table-type.ts`'s class doc
 * comment for the full Step 1 audit summary this rebuild is built from
 * (docs/ANGULAR_PENDING_FINDINGS.md, entry
 * `PENDING-batch18-step1-operationtabletype`).
 */
export interface OperationTableTypeForm {
  operationTableTypeId?: number;
  tableCode: string;
  tableName: string;
  tableDesc: string;
}

export function emptyOperationTableTypeForm(): OperationTableTypeForm {
  return { operationTableTypeId: undefined, tableCode: '', tableName: '', tableDesc: '' };
}

/** Shape of one row in `getOperationTableTypeList`'s `listObject`
 * (`GET_OPERATION_TABLE_TYPE_LIST` named query, `OperationTableType.java:
 * 15-20`) -- also the exact shape `getOperationTableTypeListById` returns
 * (`GET_OPERATION_TABLE_TYPE_LIST_BY_ID`, same 5 projected columns). `status`
 * is a legacy 'A'/'I' character column (view's own `ng-true-value="'A'"`/
 * `ng-false-value="'I'"`, view:118). */
export interface OperationTableTypeRow {
  operationTableTypeId: number;
  tableCode: string;
  tableName: string;
  tableDesc: string;
  status: 'A' | 'I';
}

/** POST body for `saveOperationTableType` (ctrl:158-201, the `save()`
 * function -- this screen's ONLY create path; unlike some sibling screens,
 * `save()` here never branches into an update, since edit is handled by a
 * wholly separate `update()` function/button, see this screen's `.ts` class
 * doc comment). `orgId`/`unitId` are load-bearing, not merely sent for
 * parity: `OperationTableTypeServiceImpl.saveOperationTableType` runs
 * `getOperationTableTypeListByName` (the real, org/unit-scoped duplicate
 * check, `OperationTableType.java:34-39`) against these same fields before
 * ever reaching the DAO's insert. `createdDate`/`updatedDate` are also
 * load-bearing, not decorative: `OperationTableTypeDaoImpl.
 * saveOperationTableType` (:41-64) calls `GlobalCommonDateUtils.getDate(dto.
 * getCreatedDate(), "dd-M-yyyy HH:mm:ss")` unconditionally -- a `null`
 * input throws inside that helper (`dateString.length()` on `null`,
 * confirmed by reading `GlobalCommonDateUtils.java:297-320` directly; the
 * NPE is swallowed by the DAO's own outer `catch (Exception e)`, silently
 * producing an ERROR response) -- so these two fields must always be sent,
 * matching legacy's own `moment(date).format('DD-MM-YYYY HH:mm:ss')` stamp
 * (ctrl:160-161). */
export interface OperationTableTypeCreatePayload {
  tableCode: string;
  tableName: string;
  tableDesc: string;
  orgId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
  status: 'A';
}

/** POST body for `updateOperationTableType` (ctrl:229-256, the `update()`
 * function). `orgId`/`unitId` are load-bearing here too: `updateOperation
 * TableType`'s service method runs `getOperationTableTypeListByNameNotById`
 * (`OperationTableType.java:40-46`) scoped by these same fields plus
 * `tableName`/`operationTableTypeId != :id` before the DAO's update --
 * exactly the "correctly org/unit-scoped, but tableName-only" duplicate
 * check this screen's Step 1 audit confirmed (see class doc comment).
 * `status` is deliberately omitted: the DAO's `updateOperationTableType`
 * (`OperationTableTypeDaoImpl.java:67-84`) never reads it off the incoming
 * DTO at all (only `tableCode`/`tableName`/`tableDesc`/`updatedBy`/
 * `updatedDate`), even though legacy's own payload includes whatever stale
 * `status` happened to be sitting on the freshly-fetched `common` object. */
export interface OperationTableTypeUpdatePayload {
  operationTableTypeId: number;
  tableCode: string;
  tableName: string;
  tableDesc: string;
  orgId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateOperationTableTypeStatus` (ctrl:280-309). The DAO
 * (`updateOperationTableTypeStatus`, `OperationTableTypeDaoImpl.java:87-103`)
 * loads the row by `operationTableTypeId` alone (no org/unit predicate --
 * see class doc comment on this already-disclosed, project-wide gap) and
 * only ever writes back `status`/`updatedBy`/`updatedDate` -- so this clean,
 * purpose-built payload sends exactly the 4 fields the backend actually
 * reads, deliberately NOT the literal shared `$scope.common` object legacy's
 * own `updateStatus()` mutates and re-sends (see class doc comment for how
 * this rebuild reproduces the *observable* behavior of that bug -- the
 * add-form not being reset after a toggle -- without porting the literal
 * shared-mutable-object mechanism, which this signals-based architecture
 * has no equivalent of anyway). */
export interface OperationTableTypeStatusPayload {
  operationTableTypeId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
