/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/room-master.html +
 * resources/assets/js/controller/adt/unit/roomMasterController.js
 * (`roomMasterController`, no `controllerAs`, wired via app.js's central
 * `$stateProvider` at line ~8656 -- NOT self-registered). moduleId 550, no
 * requireSuperUser.
 */
export interface WardOption {
  wardId: number;
  wardName: string;
  /** 'Y'/'N' -- drives the `hideBed` UI rule (ctrl:141-162): a labor ward or
   * hemodialysis ward hides the Bed Category select, the Billing Ward Bed
   * Category display, the Isolation checkbox, and the "Red" ER checkbox. */
  islaborWard?: string;
  ishemodialysisward?: string;
}

export interface BedCategoryOption {
  bedCategoryId: number;
  bedCategoryDesc: string;
}

export interface BillingBedCategoryOption {
  billingBedCategoryId: number;
  billingBedDesc: string;
}

/** Row shape returned by getRoomMasterList (ctrl:225-253). */
export interface RoomMasterRow {
  roomId: number;
  wardName: string;
  roomCode: string;
  roomDesc: string;
  billingBedDesc?: string;
  bedCategoryDesc?: string;
  isIsolation: 'Y' | 'N';
  isModality: 'Y' | 'N';
  status: 'A' | 'I';
}

/** Shape of getRoomMasterById's `listObject[0]` (ctrl:357-399) -- NOTE this
 * is `listObject[0]`, not `object`. */
export interface RoomMasterDetail {
  roomId: number;
  wardId: number;
  roomCode: string;
  roomDesc: string;
  bedCategoryId: number | null;
  billingBedCategoryId: number | null;
  billingBedDesc: string | null;
  isIsolation: 'Y' | 'N';
  erCategoryId: number | null;
  isModality: 'Y' | 'N';
  islaborWard?: string;
  ishemodialysisward?: string;
}

/** Editable form state -- mirrors legacy's `$scope.roomMasterObj` (ctrl:57-73). */
export interface RoomMasterForm {
  roomId?: number;
  wardId: number | null;
  roomCode: string;
  roomDesc: string;
  bedCategoryId: number | null;
  billingBedCategoryId: number | null;
  billingBedDesc: string;
  isIsolation: 'Y' | 'N';
  /** Shared by all 4 ER-category checkboxes in legacy (Red=1/Yellow=2/Green=3/Black=4)
   * -- a single `ng-model` across mutually-exclusive-looking checkboxes, not a real
   * radio group. Modeled here as a single nullable value, matching how legacy's
   * shared-model checkboxes actually behave (checking one un-checks the others
   * because they all watch the same underlying value). */
  erCategoryId: number | null;
  isModality: 'Y' | 'N';
}

export function emptyRoomMasterForm(): RoomMasterForm {
  return {
    roomId: undefined,
    wardId: null,
    roomCode: '',
    roomDesc: '',
    bedCategoryId: null,
    billingBedCategoryId: null,
    billingBedDesc: '',
    isIsolation: 'N',
    erCategoryId: null,
    isModality: 'N',
  };
}

/** POST body for /api/adt/saveRoomMaster (ctrl:285-354). `bedCategoryId`/
 * `billingBedCategoryId` are only included when non-empty, matching legacy's
 * own conditional `if (...) data.bedCategoryId = ...` construction. */
export interface RoomMasterSavePayload {
  erCategoryId: number | null;
  isIsolation: 'Y' | 'N';
  roomCode: string;
  roomDesc: string;
  wardId: number | null;
  isModality: 'Y' | 'N';
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  billingBedCategoryId?: number;
  bedCategoryId?: number;
}

/** POST body for /api/adt/updateRoomMaster (ctrl:442-501). */
export interface RoomMasterUpdatePayload {
  isIsolation: 'Y' | 'N';
  roomCode: string;
  roomDesc: string;
  roomId: number;
  wardId: number | null;
  erCategoryId: number | null;
  updatedBy: number;
  organizationId: number;
  unitId: number;
  updatedDate: string;
  isModality: 'Y' | 'N';
  billingBedCategoryId?: number;
  bedCategoryId?: number;
}

/** POST body for /api/adt/updateRoomMasterStatus (ctrl:402-439). */
export interface RoomMasterStatusPayload {
  roomId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
