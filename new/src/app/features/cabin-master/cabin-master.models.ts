/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/cabin-master.html +
 * resources/assets/js/controller/master/unit/cabinMasterController.js
 * (`cabinMasterController`, no `controllerAs`). Only two editable fields on
 * this screen: cabin code + description, same shape as `qualification-master`.
 */
export interface CabinRow {
  cabinId: number;
  cabinCode: string;
  cabinDesc: string;
  status: 'A' | 'I';
}

/** Shape of GETCABINMASTERBYID's `listObject[0]` (ctrl:213-241) -- only the
 * fields `showUpdateBtn` actually reads back into the form. */
export interface CabinDetail {
  cabinId: number;
  cabinCode: string;
  cabinDesc: string;
}

/** Mirrors `save`'s `data` object (ctrl:158-170). */
export interface CabinSavePayload {
  cabinDesc: string;
  cabinCode: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `update`'s `data` object (ctrl:252-261) -- no `status`/`createdBy`,
 * matching legacy exactly (only present on the create payload). */
export interface CabinUpdatePayload {
  cabinId: number;
  cabinDesc: string;
  cabinCode: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/**
 * Minimal status-toggle payload used by this rebuild. Legacy's own
 * `updateStatus` (ctrl:295-331) POSTs the ENTIRE `$scope.common` edit-form
 * object (whatever code/desc happen to be sitting in the add/edit form at
 * the time, plus the four fields it patches on) to
 * `UPDATE_CABIN_MASTER_STATUS` -- a sloppy full-object POST, not a deliberate
 * contract. Same anti-pattern already found in counterMaster and
 * employeeCategoryMaster. Not a behavior change a user would notice (the
 * backend only reads cabinId/status/updatedBy/updatedDate for this call), so
 * this rebuild sends just those four fields instead of replicating the
 * sloppy full-object POST.
 */
export interface CabinStatusPayload {
  cabinId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.common`). `cabinId` is only
 * present while editing an existing row. */
export interface CabinForm {
  cabinId?: number;
  cabinCode: string;
  cabinDesc: string;
}

export function emptyCabinForm(): CabinForm {
  return {
    cabinCode: '',
    cabinDesc: '',
  };
}
