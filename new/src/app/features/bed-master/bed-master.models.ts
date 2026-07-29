/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/bed-master.html +
 * resources/assets/js/controller/adt/unit/bedMasterController.js
 * (`bedMasterController`, no `controllerAs`, 1000 lines, read in full).
 * Legacy state title is "Ward Bed Master" (app.js:8768) with TWO tabs --
 * "Normal Bed" and "Moving Bed" -- driven by this one controller, with
 * mostly-separate list/save/update logic per tab (see bed-master.ts's class
 * doc for the full audit trail).
 */

export type BedStatus = 'A' | 'I';
export type YN = 'Y' | 'N';

/** GETACTIVEWARDMASTERLIST response row (ctrl:73-89). Same shape roomMaster
 * already uses -- `islaborWard`/`ishemodialysisward` drive the `hideBed`/
 * `showDialysisSelect` UI rules (ctrl:233-286, `getWardId`). */
export interface WardOption {
  wardId: number;
  wardName: string;
  islaborWard?: string;
  ishemodialysisward?: string;
  floorId?: number | null;
}

/** GETACTIVEBEDCATEGORYLIST response row (ctrl:91-102) -- NOTE this is the
 * globally-scoped `/adt/getActiveBedCategoryList` GET (no org/unit params at
 * all, matches legacy exactly), NOT roomMaster's org/unit-scoped
 * `getBedCategoryListByOrgUnit` POST. Different endpoint, deliberately not
 * reused. */
export interface BedCategoryOption {
  bedCategoryId: number;
  bedCategoryDesc: string;
}

/** GETROOMBYWARDID response row (ctrl:261-286, `getWardId`). `bedCategoryId`/
 * `billingBedCategoryId`/`billingBedDesc` are denormalized onto the room row
 * itself -- selecting a Room reads these 3 fields directly off this array
 * (`getBillingAndBedCategoryId`, ctrl:544-558), with NO separate API call.
 * This is the ONLY mechanism that populates billing/bed-category on CREATE;
 * `getBedCategory()`/GETACTIVEBILLINGBEDCATEGORYBYBEDCATEGORYID is
 * unreachable from create (its own `<select>` is hardcoded `disabled`,
 * view:77-83) and only ever fires on the EDIT path (`showUpdateBtn`,
 * ctrl:454-458). See bed-master.ts class doc. */
export interface RoomOption {
  roomId: number;
  roomDesc: string;
  bedCategoryId?: number | null;
  billingBedCategoryId?: number | null;
  billingBedDesc?: string | null;
}

/** `/api/opd/getAsset` response row (ctrl:413-430, `dialysischair()`, fired
 * unconditionally on load) -- backs the "Dialysis chair" select, only shown
 * when `showDialysisSelect === 'Y'` (a hemodialysis ward selected). */
export interface DialysisAssetOption {
  itemId: number;
  assetype: string;
}

/** Row shape returned by `bedList`/`bedMasterUniversalSearch` (isMovingBed
 * omitted) for the Normal Bed tab (ctrl:113-173/945-970). */
export interface BedMasterRow {
  bedId: number;
  wardName: string;
  roomDesc: string;
  bedCategoryDesc?: string;
  billingBedDesc?: string;
  bedNumber: string;
  bedCode: string;
  isVirtual: YN;
  isEarMarking: YN;
  status: BedStatus;
}

/** `listObject[0]` shape from `GET /api/adt/bed/{id}` (`showUpdateBtn`,
 * ctrl:432-490) for the Normal Bed tab. A 409 (`code==='409'`) means the bed
 * is currently in-use (non-vacant, `bedStatusId != 3` server-side) and the
 * fetch is REJECTED -- no data comes back, faithfully reproduced (see
 * bed-master.ts). */
export interface BedMasterDetail {
  bedId: number;
  wardId: number;
  floorId?: number | null;
  roomId: number;
  bedCategoryId?: number | null;
  billingBedCategoryId?: number | null;
  bedCode: string;
  bedDesc: string;
  isVirtual: YN;
  isEarMarking: YN;
  earmarkingDesc?: string | null;
}

/** Row shape returned by `bedMovingBedList`/`bedMasterUniversalSearch`
 * (isMovingBed='Y') for the Moving Bed tab (ctrl:642-727/972-997). */
export interface MovingBedRow {
  bedId: number;
  bedCode: string;
  bedDesc: string;
  bedNumber: string;
  status: BedStatus;
}

/** `listObject[0]` shape from `GET /api/adt/bed/{id}` for the Moving Bed tab
 * (`showUpdateBtnMovingBed`, ctrl:874-919) -- only 3 fields are read back
 * into the form, unlike the Normal Bed tab's much larger detail shape. */
export interface MovingBedDetail {
  bedId: number;
  bedCode: string;
  bedDesc: string;
}

/** Editable form state for the Normal Bed tab (mirrors `$scope.bedMasterObj`
 * as used by `saveBed`/`updateBed`, ctrl:58-63/327-411/561-625). */
export interface BedMasterForm {
  bedId?: number;
  wardId: number | null;
  floorId?: number | null;
  roomId: number | null;
  bedCategoryId: number | null;
  billingBedCategoryId: number | null;
  billingBedDesc: string;
  bedCode: string;
  bedDesc: string;
  isVirtual: YN;
  earMarking: YN;
  earMarkingDesc: string;
  itemId?: number | null;
}

export function emptyBedMasterForm(): BedMasterForm {
  return {
    wardId: null,
    floorId: null,
    roomId: null,
    bedCategoryId: null,
    billingBedCategoryId: null,
    billingBedDesc: '',
    bedCode: '',
    bedDesc: '',
    isVirtual: 'N',
    earMarking: 'N',
    earMarkingDesc: '',
  };
}

/** Editable form state for the Moving Bed tab (ctrl:756-816/819-866) -- just
 * code + description, no ward/room/category at all. */
export interface MovingBedForm {
  bedId?: number;
  bedCode: string;
  bedDesc: string;
}

export function emptyMovingBedForm(): MovingBedForm {
  return { bedCode: '', bedDesc: '' };
}

/** POST body for `/api/adt/addBed` (Normal Bed, ctrl:327-354). `bedCategoryId`/
 * `billingBedCategoryId`/`itemId` only included when non-null, matching
 * legacy's own conditional `if(...) data.x = ...` construction (ctrl:359-367).
 * SEVERITY-1: this endpoint fails on every real attempt today -- see
 * bed-master.ts class doc / docs/ANGULAR_PENDING_FINDINGS.md. */
export interface BedMasterSavePayload {
  floorId?: number | null;
  wardId: number | null;
  roomId: number | null;
  bedCode: string;
  bedDesc: string;
  isEarMarking: YN;
  isVirtual: YN;
  earmarkingDesc?: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  isMovingBed: 'N';
  bedStatusId: 3;
  bedTypeId: 1;
  bedCategoryId?: number;
  billingBedCategoryId?: number;
  itemId?: number;
}

/** POST body for `/api/adt/updateBed` (Normal Bed, ctrl:561-591). */
export interface BedMasterUpdatePayload {
  floorId?: number | null;
  wardId: number | null;
  roomId: number | null;
  bedCategoryId?: number;
  billingBedCategoryId?: number;
  bedCode: string;
  bedDesc: string;
  isEarMarking: YN;
  isVirtual: YN;
  earmarkingDesc?: string;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
  bedId: number;
  isMovingBed: 'N';
  itemId?: number;
}

/** POST body for `/api/adt/addBed` (Moving Bed, ctrl:756-776) -- deliberately
 * NO floorId/wardId/roomId/bedCategoryId/billingBedCategoryId/itemId, matching
 * legacy's much smaller `data` object exactly. Same broken endpoint as the
 * Normal Bed save -- see class doc. */
export interface MovingBedSavePayload {
  bedCode: string;
  bedDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  isMovingBed: 'Y';
  bedStatusId: 3;
  bedTypeId: 1;
}

/** POST body for `/api/adt/updateBed` (Moving Bed, ctrl:819-834). SEVERITY-1:
 * returns a real "Updated Successfully." response but never actually writes
 * to the DB -- see class doc / docs/ANGULAR_PENDING_FINDINGS.md. */
export interface MovingBedUpdatePayload {
  bedCode: string;
  bedDesc: string;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
  bedId: number;
  isMovingBed: 'Y';
}

/** POST body for `/api/adt/updateStatus` (ctrl:493-542/shared by both tabs).
 * Confirmed silently corrupts `updated_date` (backend date-format-pattern
 * mismatch) -- faithfully reproduced, not fixed. See class doc. */
export interface BedStatusPayload {
  bedId: number;
  status: BedStatus;
  updatedBy: number;
  updatedDate: string;
}
