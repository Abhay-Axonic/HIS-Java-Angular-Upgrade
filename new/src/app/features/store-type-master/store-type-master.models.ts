/**
 * Field names/types read directly from `StoreTypeMasterDto.java`
 * (legacy/Global/src/main/java/com/param/global/common/StoreTypeMasterDto.java)
 * and `storeTypeController.js` -- not guessed.
 *
 * Legacy's own form object is `$scope.common` (ctrl:48-56):
 * `{ storeTypeId:"", code:"", type:"", addedBy, updatedBy, organizationId,
 * unitId }` -- every field starts as an empty string except the audit/org
 * fields, which start populated from the session cookie immediately on
 * init. There is no separate "description" field on the wire -- the
 * screen's "Description" label (view:27) is bound to `common.type`, same
 * shape as `discountCategoryMaster`'s `type` field for its own
 * "Description" label.
 */
export interface StoreTypeForm {
  storeTypeId?: number;
  code: string;
  type: string;
}

export function emptyStoreTypeForm(): StoreTypeForm {
  return { storeTypeId: undefined, code: '', type: '' };
}

/** Shape of one row in `getStoreTypeList`'s `listObject`
 * (`GET_STORE_TYPE_MASTER_LIST` named query, `StoreTypeMaster.java:16-19`)
 * -- only 4 fields are aliased onto the DTO: `storeTypeId`/`code`/`type`/
 * `isActive` (a REAL boolean column `is_active`, confirmed live against
 * the 6 real seed rows -- not an 'A'/'I' string like several sibling
 * screens). */
export interface StoreTypeRow {
  storeTypeId: number;
  code: string;
  type: string;
  isActive: boolean;
}

/** Shape of `getStoreTypeById`'s `listObject[0]`
 * (`GET_STORE_TYPE_BY_ID` named query, same 4 fields as the list row --
 * confirmed live: `organizationId`/`unitId`/`isDeleted`/audit fields all
 * come back `null` on this call, not just unselected in the DTO). */
export type StoreTypeDetail = StoreTypeRow;

/** POST body for `saveStoreType` (ctrl:153-196, payload at ctrl:48-56 as
 * `$scope.common`). No `storeTypeId` -- DB-generated
 * (`m_store_type_id_seq`). Unlike `discountCategoryMaster`, legacy sends
 * NO `addedDate`/`updatedDate` here at all -- `StoreTypeMasterDaoImpl.
 * saveStoreType` (Global module) computes both server-side via
 * `GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.
 * getUnitId()), ...)`, ignoring any date the client might send -- so
 * there is no client-side `formatDateTime()` helper needed for this
 * screen at all. */
export interface StoreTypeSavePayload {
  code: string;
  type: string;
  addedBy: number;
  updatedBy: number;
  organizationId: number;
  unitId: number;
}

/** POST body for `updateStoreType` (ctrl:232-271, payload built at
 * ctrl:239-242 on top of whatever `getStoreTypeById` returned into
 * `common`). Same no-client-date-fields note as save applies here too. */
export interface StoreTypeUpdatePayload {
  storeTypeId: number;
  code: string;
  type: string;
  addedBy: number;
  updatedBy: number;
  organizationId: number;
  unitId: number;
}

/** POST body for `updateStoreTypeStatus` (ctrl:273-306, payload at
 * ctrl:278-282). Legacy's own `obj.updatedBy` reads `$scope.updatedBy`,
 * a variable this controller never assigns anywhere else (only
 * `common.updatedBy` exists) -- so legacy's real wire payload silently
 * drops this key (`JSON.stringify` omits `undefined` properties). Confirmed
 * live this is harmless: `StoreTypeMasterDaoImpl.updateStoreTypeStatus`
 * (Global module) never reads `dto.getUpdatedBy()` at all -- it sets the
 * audit column from `sessionService.getUserId()` server-side instead, so
 * the field's value on the wire has zero observable effect either way.
 * Sent here as the real session `userId` (matching every other screen's
 * convention) purely for wire-shape consistency, not because it changes
 * server behavior. */
export interface StoreTypeStatusPayload {
  storeTypeId: number;
  isActive: boolean;
  updatedBy: number;
}
