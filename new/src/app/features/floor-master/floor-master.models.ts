/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/floor-master.html +
 * resources/assets/js/controller/master/unit/floorMasterController.js
 * (`floorMasterController`, no `controllerAs`). Only two editable fields on
 * this screen: floor code + a free-text description.
 *
 * NOTE: legacy's local scope variable for the description field is named
 * `floorMasterObj.floorDesc` (ctrl:57, ctrl:273-275), but every wire
 * payload/response field is `floorName` (SAVEFLOORMASTER/UPDATEFLOORMASTER
 * request bodies, GETFLOORMASTERLIST/GETFLOORBYID response rows -- see
 * ctrl:204-206, 273-274, 331-332). `floorDesc` is purely an internal
 * scope-variable name, not part of any API contract, so it is NOT preserved
 * here -- the form field is named `floorName` throughout to match the real
 * wire field consistently with the row/payload types below.
 */
export interface FloorMasterRow {
  floorId: number;
  floorCode: string;
  floorName: string;
  status: 'A' | 'I';
}

/** Shape of GETFLOORBYID's `listObject[0]` (ctrl:266-280) -- only the fields
 * `showUpdateBtn` actually reads back into the form. */
export interface FloorMasterDetail {
  floorId: number;
  floorCode: string;
  floorName: string;
}

/** Mirrors `saveFloorMaster`'s `data` object (ctrl:203-213). */
export interface FloorMasterSavePayload {
  floorCode: string;
  floorName: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `updateFloorMaster`'s `data` object (ctrl:330-336) -- note no
 * `unitId`/`organizationId`/`status` field is sent on update, matching
 * legacy exactly (only present on the create payload). */
export interface FloorMasterUpdatePayload {
  floorCode: string;
  floorName: string;
  floorId: number;
  updatedBy: number;
  updatedDate: string;
}

/** Mirrors `updateStatus`'s `data` object (ctrl:292-297) -- no
 * `unitId`/`organizationId` sent here either. */
export interface FloorMasterStatusPayload {
  floorId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.floorMasterObj`, ctrl:57).
 * `floorId` is only present while editing an existing row. */
export interface FloorMasterForm {
  floorId?: number;
  floorCode: string;
  floorName: string;
}

export function emptyFloorMasterForm(): FloorMasterForm {
  return {
    floorCode: '',
    floorName: '',
  };
}
