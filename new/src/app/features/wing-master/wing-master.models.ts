/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/wing-master.html +
 * resources/assets/js/controller/adt/unit/wingMasterController.js
 * (`wingMasterController`, no `controllerAs`). Only two editable fields on
 * this screen: wing code + wing name (`wingDesc`). Confirmed a near
 * line-for-line copy of amenityMasterController.js/qualificationMasterController.js's
 * shape -- same "code + desc" master pattern, same popup/pagination plumbing.
 */
export interface WingMasterRow {
  wingId: number;
  wingCode: string;
  wingDesc: string;
  status: 'A' | 'I';
}

/** Shape of GETWINGMASTERLISTBYID's `listObject[0]` -- only the fields
 * `showUpdateBtn` (ctrl:197-225) actually reads back into the form. */
export interface WingMasterDetail {
  wingId: number;
  wingCode: string;
  wingDesc: string;
}

/** Mirrors `save`'s `wingObj` (ctrl:148-158). */
export interface WingMasterSavePayload {
  wingCode: string;
  wingDesc: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  status: 'A';
  organizationId: number;
  unitId: number;
  updatedDate: string;
}

/** Mirrors `update`'s `wingObj` (ctrl:235-243) -- note no `status`/`createdBy`
 * field is sent on update; status only changes via the separate
 * `updateWingMasterStatus` endpoint. */
export interface WingMasterUpdatePayload {
  wingCode: string;
  wingDesc: string;
  wingId: number;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** Mirrors `updateStatus`'s `wingObj` (ctrl:284-289). */
export interface WingMasterStatusPayload {
  wingId: number;
  status: 'A' | 'I';
  updatedDate: string;
  updatedBy: number;
}

/** Form/edit-buffer shape (mirrors `$scope.common`, ctrl:40-44).
 * `wingId` is only present while editing an existing row. */
export interface WingMasterForm {
  wingId?: number;
  wingCode: string;
  wingDesc: string;
}

export function emptyWingMasterForm(): WingMasterForm {
  return {
    wingCode: '',
    wingDesc: '',
  };
}
