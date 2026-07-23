export type WardStoreMapperStatus = 'A' | 'I';

/** Raw shape of `storeList()`'s response (ctrl:138-150) -- GET,
 * `response.data` itself IS the array, with NO
 * `{status,message,object,listObject}` envelope -- identical
 * endpoint/shape to department-store-mapper's `storeList()` (both screens
 * share the exact same `/api/global/user/stores/{orgId}/{unitId}/{userId}`
 * call). Not a typo; read directly off the real response. */
export interface WardStoreOption {
  id: number;
  store: string;
}

/** One entry of `getActiveWardMasterList`'s `listObject` (ctrl:152-167). */
export interface WardOption {
  wardId: number;
  wardName: string;
}

/** Row shape returned by `getWardStoremappedListPagination` (ctrl:57-95) --
 * the only fields the legacy view actually renders
 * (ward-store-mapper.html:96-105). */
export interface WardStoreMapperRow {
  wardStoreMapperId: number;
  storeName: string;
  wardName: string;
  status: WardStoreMapperStatus;
}

/** Shape returned by `getWardStoreMapperById` (ctrl:279-300), assigned
 * wholesale to `$scope.wardObj` off `response.data.object` (a single
 * object, NOT `listObject[0]`). `id` is legacy's own field name for the
 * mapped storeId -- the Store `<select>`'s `ng-model` is bound directly to
 * `wardObj.id` (view:11), NOT `wardObj.storeId` (see the component's class
 * doc comment: `wardObj.storeId` is a dead field, set once in
 * `initCommon()`/`initWardStoreMapped()` and never read by the view).
 * Kept verbatim here rather than renamed, since it's the literal field the
 * real endpoint returns and this rebuild's `edit()` reads. */
export interface WardStoreMapperDetail {
  wardStoreMapperId: number;
  id: number; // storeId
  wardId: number;
  storeName?: string;
  wardName?: string;
  status?: WardStoreMapperStatus;
}

/** POST body for `saveWardStoremapper` (ctrl:192-242) / `updateWardStoremapper`
 * (ctrl:320-370) -- both are real, live endpoints, each wired to its own
 * visible Save/Update button (ward-store-mapper.html:57-64) -- unlike
 * department-store-mapper's two orphaned save/update functions.
 * `wardStoreMapperId` is only present/meaningful on update. */
export interface WardStoreMapperSavePayload {
  wardStoreMapperId?: number | '';
  id: number | ''; // storeId, matches wardObj.id
  wardId: number | '';
  orgId: number;
  unitId: number;
  status: 'A' | 'I';
  createdBy: number;
  updatedBy: number;
}
