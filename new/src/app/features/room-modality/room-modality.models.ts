/**
 * Field set taken directly from
 * views/bedManagement/room-modality.html +
 * resources/assets/js/controller/adt/unit/roomModalityController.js
 * (`roomModalityController`, no `controllerAs` -- classic `$scope`
 * pattern). Wired via app.js's central `$stateProvider`
 * (config/app.js:20824, `.state('roomModality', {...})`) -- self-registered
 * in the sense of having its own dedicated controller, but the state
 * config itself lives in the shared app.js file, not a standalone
 * `.config($stateProvider)` block. No `data`/`resolve` block at all, so no
 * moduleId/requireSuperUser gate in legacy (same shape as
 * `billingGroupMaster`/`periodicityService`).
 *
 * Naming note (NOT a bug): `roomModalityId` is the FK to the selected
 * *Modality Room* (from `getActiveModalityRoom` -- a radiology-capable
 * room), while `roomModalityMasterId` is the PK of this screen's own
 * assignment row (Room + Sub-Speciality + Modality). The names look
 * similar but refer to two different entities; both are read verbatim off
 * the legacy payload shape (ctrl:56-67, ctrl:212-260, ctrl:321-342).
 */

/** Option from POST /api/adt/getActiveModalityRoom (ctrl:78-92). */
export interface ModalityRoomOption {
  roomId: number;
  roomDesc: string;
}

/** Option from POST /adt/getSubSpecialityBySpecialityId (ctrl:100-124),
 * filtered server-side by `cookieObjectAppConfig.radiologySpecialityId`. */
export interface SubSpecialityOption {
  subSpecialityId: number;
  subSpecialityName: string;
}

/** Option from POST /api/opd/getModalityBySubSpecialityId (ctrl:131-159),
 * cascades off the selected Sub-Speciality. */
export interface ModalityOption {
  modalityId: number;
  modalityDesc: string;
}

/** Row shape returned by getRoomModalityList (ctrl:212-260). Note the
 * list projection uses `roomName`, distinct from the Room dropdown's own
 * `roomDesc` field -- two different endpoints, two different projections
 * of the same underlying room, not an inconsistency to "fix". */
export interface RoomModalityRow {
  roomModalityMasterId: number;
  roomName: string;
  subSpecialityName: string;
  modalityDesc: string;
  status: 'A' | 'I';
}

/** Shape of both `$scope.roomModalityObj` (ctrl:56-67) -- the create/update
 * payload -- and `getRoomModalityById`'s `listObject[0]` (ctrl:329-340),
 * which replaces the form object wholesale on edit. `''` mirrors legacy's
 * own literal `""` initial values for the not-yet-selected FK fields. */
export interface RoomModalityRecord {
  roomModalityMasterId: number | '';
  roomModalityId: number | '';
  specialityId: number | '';
  subSpecialityId: number | '';
  modalityId: number | '';
  unitId: number;
  organizationId: number;
  updatedBy: number;
  createdBy: number;
  status: 'A' | 'I';
}

/** Mirrors ctrl:56-67's `$scope.roomModalityObj` literal, including
 * `specialityId` being pre-seeded from `radiologySpecialityId` (read
 * defensively via SessionService.appConfig -- see the class comment in
 * room-modality.ts for why this doesn't port ctrl:46's crash). */
export function emptyRoomModality(
  unitId: number,
  organizationId: number,
  userId: number,
  specialityId: number | '',
): RoomModalityRecord {
  return {
    roomModalityMasterId: '',
    roomModalityId: '',
    specialityId,
    subSpecialityId: '',
    modalityId: '',
    unitId,
    organizationId,
    updatedBy: userId,
    createdBy: userId,
    status: 'A',
  };
}

/** POST body for /api/global/updateRoomModalityMasterStatus (ctrl:345-380). */
export interface RoomModalityStatusPayload {
  roomModalityMasterId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
